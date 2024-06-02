const {MongoClient} = require('mongodb');
const uri = 'mongodb://localhost:27017';

const client = new MongoClient(uri);

client.connect()
    .then(() => {
        console.log('Connected to the MongoDB server');
    })
    .catch((err) => {
        console.error('Error connecting to the MongoDB server:', err);
    });


const db = client.db('nodegym');
const users = db.collection('users');

function insertUser(user) {
    users.insertOne(user)
        .then((result) => {
            console.log('Registered user:', result.insertedId);
        })
        .catch((err) => {
            console.error('Error inserting document:', err);
        });
}

function updatePasswordForUsername(newPasswordHashed, username) {
    users.updateOne({login: username}, {$set: {password: newPasswordHashed}})
        .then((result) => {
            console.log('Updated user password');
        })
        .catch((err) => {
            console.error('Error updating document:', err);
        });
}

function updateImageForUser(username, photoId) {
    users.updateOne({login: username}, {$set: {photoId: photoId}})
        .then((result) => {
            console.log('Updated user photo');
        })
        .catch((err) => {
            console.error('Error updating document:', err);
        });
}

async function findUserByLogin(login) {
    return await users.findOne({login: login});
}

module.exports = {
    insertUser,
    findUserByLogin,
    updatePasswordForUsername,
    updateImageForUser
};
