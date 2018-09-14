const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


//router.get('/', (req, res) => res.send('Привет, это API!'));

async function tokenExist(req) {
    try {
        let rows = connection.oneOrNone(
            `SELECT idusers FROM users WHERE utoken = $1`,
            [req.body.utoken]);
        if (await rows === null) {
            return false
        } else {
            return true
        }
    } catch (err) {
        return false
    }
}

router.post('/', async (req, res) => { //регистрация информации post
    console.log(req.body.utoken);
    if (await tokenExist(req)) {
        try {
            let rows = connection.oneOrNone(
                `SELECT uName, uSecName, uMidName, uPhone, uSize, uInfo 
            FROM usersinfo
            WHERE users_idusers = (SELECT idusers FROM users WHERE utoken = $1);`,
                [req.body.utoken]);
            if (await rows === null) {
                res.sendStatus(401)
            } else {
                res.send(await rows)
            }
        } catch (err) {
            res.sendStatus(400); // Информация о пользователе не найдена
        }
    } else {
        res.sendStatus(401)
    }
});

module.exports = router;