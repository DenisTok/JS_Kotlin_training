const express = require('express');
const router = express.Router();
const connection = require('../db').connection;

async function tokenExist(utoken) {
    try {
        let rows = connection.oneOrNone(
            `SELECT idusers FROM users WHERE utoken = $1`,
            [utoken]);
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
    if (await tokenExist(req.body.uToken)) {
        try {
            let rows = connection.one(
                `INSERT INTO usersInfo (users_idusers, uiname, uiSecName, uiMidName, uiPhone, uiSize, uiInfo) 
            VALUES ((SELECT idusers FROM users WHERE utoken = $7), $1, $2, $3, $4, $5, $6) 
            RETURNING idusersinfo;
            `,
                [req.body.uName,
                req.body.uSecName, req.body.uMidName,
                req.body.uPhone, req.body.uSize,
                req.body.uInfo, req.body.uToken]);
            res.send(await rows);
        } catch (err) {
            res.send("[3]"); // Информация уже есть о пользователе
            
        }
    }else{
        console.log("401");
        res.sendStatus(401)
    }
});

module.exports = router;