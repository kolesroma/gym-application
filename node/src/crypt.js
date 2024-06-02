const bcrypt = require("bcrypt");

const saltRounds = 10;

function cryptPassword(password) {
    try {
        return bcrypt.hashSync(password, saltRounds, (err, hash) => {
            if (err) {
                throw Error("Cannot hash password");
            } else {
                return hash;
            }
        });
    } catch (e) {
        console.error(e);
    }
}

function comparePasswords(user, password) {
    return bcrypt.compareSync(password, user.password);
}

module.exports = {
    cryptPassword,
    comparePasswords
};
