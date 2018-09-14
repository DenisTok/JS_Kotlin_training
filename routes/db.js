const pgp = require('pg-promise')();
const connectionString = require('./config').connectionString;
const connection = pgp(connectionString);

connection.connect()
    .then(obj => {
        obj.done();
    })
    .catch(error => {
        console.log('ERROR:', error.message || error);
    });


module.exports.connection = connection;