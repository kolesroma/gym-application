const express = require('express');
const bodyParser = require('body-parser');
const uuid = require('uuid');
const jwt = require('jsonwebtoken');
const multer = require('multer');
const fs = require('fs');
const mongoose = require('mongoose');
const cors = require('cors');

const crypt = require('./crypt.js');
const mongo = require('./mongo.js');
const jwtModule = require('./jwt.js');
const axios = require("axios");
const {findUserByLogin, insertUser, updatePasswordForUsername, updateImageForUser} = require("./mongo");

const app = express();
const PORT = process.env.PORT || 3000;

const JWT_SECRET = 'mysecretkey';
const MONGO_DB_URL = "mongodb://127.0.0.1:27017/nodegym";

const JAVA_HOST_URL = 'http://localhost:8080';
const JAVA_LOGIN_URL = JAVA_HOST_URL + '/auth/login';
const JAVA_REGISTRATION_STUDENT = JAVA_HOST_URL + '/registration/trainee';
const JAVA_REGISTRATION_TRAINER = JAVA_HOST_URL + '/registration/trainer';
const JAVA_CHANGE_PASSWORD = JAVA_HOST_URL + '/auth/change-password';

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send('Something went wrong!');
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

// app.use((req, res) => {
//     return res.status(500).json({message: 'Something broke!'});
// });

// routes

async function generateJavaToken(login, password) {
    return axios.post(JAVA_LOGIN_URL, {username: login, password: password})
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return error.response.data
        });
}

app.post('/login', (req, res) => {
    const {login, password} = req.body;
    mongo.findUserByLogin(login)
        .then(async user => {
            if (!user) {
                return res.status(401).json({message: 'Invalid credentials'});
            }
            const isValidPassword = crypt.comparePasswords(user, password);
            if (isValidPassword) {
                user.token = jwt.sign({login, "role": "user"}, JWT_SECRET, {expiresIn: '24h'});
                const javaResponse = await generateJavaToken(login, password);
                if (javaResponse.token) {
                    res.status(200).json({
                        message: 'Login successful',
                        tokenNode: user.token,
                        tokenJava: javaResponse.token
                    });
                } else {
                    res.status(403).json(javaResponse);
                }
            } else {
                res.status(401).json({message: 'Invalid credentials code 2222'});
            }
        });
});

function createNewJavaStudent(student) {
    return axios.post(JAVA_REGISTRATION_STUDENT, student)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return error.response.data;
        });
}

function createNewJavaTrainer(trainer) {
    return axios.post(JAVA_REGISTRATION_TRAINER, trainer)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return error.response.data;
        });
}

function createNewJavaUser(type, user) {
    if (type === 'trainee') {
        return createNewJavaStudent(user);
    } else if (type === 'trainer') {
        return createNewJavaTrainer(user);
    } else {
        throw Error("Invalid registration type provided: " + type);
    }
}

app.post('/register/:type', (req, res) => {
    const javaUser = createNewJavaUser(req.params.type, req.body);
    javaUser.then(response => {
        const message = response.message;
        const username = response.username;
        const password = response.password;
        if (username && password) {
            const newUser = {
                id: uuid.v4(),
                login: username,
                password: crypt.cryptPassword(password)
            };
            mongo.insertUser(newUser);
            res.status(201).json({
                message: `User ${username} registered successfully`,
                username: username,
                password: password
            });
        } else {
            return res.status(401).json({message: message});
        }
    });
});

function changePassword(changePasswordModel, config) {
    return axios.post(JAVA_CHANGE_PASSWORD, changePasswordModel, config)
        .then(response => {
            return {status: 'ok', container: response.data};
        })
        .catch(error => {
            return {status: 'bad', container: error.response.data};
        });
}

function updateUserInMongo(username, newPassword) {
    updatePasswordForUsername(crypt.cryptPassword(newPassword), username);
}

app.post('/change-password', (req, res) => {
    const {newPassword} = req.body;
    const body = req.body;
    const config = {
        headers: {authorization: `${req.headers.authorization}`}
    };
    const changedPassword = changePassword(body, config);
    changedPassword.then(response => {
        if (response.status === 'ok') {
            updateUserInMongo(response.container.username, newPassword);
            return res.status(200).json(response.container);
        } else {
            return res.status(400).json(response.container);
        }
    });
});

app.get('/logout', (req, res) => {
    res.setHeader('Authorization', '');
    res.status(200).json({message: 'Logged out'});
});

// filter

app.get('/some', (req, res) => {
    jwtModule.verifyToken(req, res);
    res.status(200).json({message: 'norm'});
});

// images base64

const Photo = mongoose.model('Photo', {data: Buffer, contentType: String});

Photo.on('error', (error) => {
  console.error('Mongoose error:', error);
});

mongoose.connect(MONGO_DB_URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

const db = mongoose.connection;
db.on('error', console.error.bind(console, 'MongoDB connection error:'));

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + file.originalname);
    },
});

const upload = multer({storage});

function decodeToken(rawJwtToken) {
    try {
        return jwt.verify(rawJwtToken, JWT_SECRET);
    } catch (error) {
        console.error('JWT verification failed:', error.message);
    }
}

app.post('/upload-photo', upload.single('photo'), (req, res) => {
    const file = req.file;
    if (!file) {
        return res.status(400).json({error: 'No file uploaded.'});
    }

    const tokenNode = req.headers.tokennode;
    const claims = decodeToken(tokenNode);
    const username = claims.login;

    const fileData = fs.readFileSync(file.path);
    const base64Data = fileData.toString('base64');

    const photo = new Photo({
        data: Buffer.from(base64Data, 'base64'),
        contentType: file.mimetype,
        uploadTimestamp: new Date(),
    });

    photo.save((err, result) => {
        if (err) {
            console.error(err);
            return res.status(500).json({error: 'Error saving the photo.'});
        }
        updateImageForUser(username, result._id + "");
        res.json({message: 'Photo uploaded successfully', photoId: result._id});
    });
});

app.get('/user-photo', (req, res) => {
    const username = decodeToken(req.headers.tokennode).login;
    findUserByLogin(username)
        .then(user => {
            if (!user) {
                return res.status(401).json({message: 'Invalid credentials'});
            }
            Photo.findById(user.photoId, (err, photo) => {
                if (err) {
                    console.error(err);
                    return res.status(500).json({error: 'Error retrieving the photo.'});
                }
                res.contentType(photo.contentType);
                res.send(photo.data);
            });
        });
});
