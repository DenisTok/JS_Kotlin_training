const express = require('express');
const router = express.Router();
const connection = require('../db').connection;

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
    console.log(req.body);

    if (await tokenExist(req) !== false) {
        try {
            let rows = connection.one(
                `INSERT INTO usersInfo (users_idusers, uName, uSecName, uMidName, uPhone, uSize, uInfo) 
            VALUES ((SELECT idusers FROM users WHERE utoken = $7), $1, $2, $3, $4, $5, $6) 
            RETURNING idusersinfo;
            `,
                [req.body.uName,
                req.body.uSecName, req.body.uMidName,
                req.body.uPhone, req.body.uSize,
                req.body.uInfo, req.body.utoken]);
            res.send(await rows);
        } catch (err) {
            res.send("[3]"); // Информация уже есть о пользователе
            console.error(err);
        }
    }else{
        res.sendStatus(401)
    }
});

module.exports = router;