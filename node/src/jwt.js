const jwt = require('jsonwebtoken');

const JWT_SECRET = 'mysecretkey';

function verifyToken(req, res) {
    const authHeader = req.headers['authorization'];
    if (!authHeader.startsWith("Bearer")) {
        return res.status(401).json({message: 'Unauthorized'});
    }
    const token = authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({message: 'Unauthorized'});
    }
    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) {
            return res.status(401).json({message: 'Unauthorized'});
        }
        req.user = user;
    });
}

module.exports = {
    verifyToken
};