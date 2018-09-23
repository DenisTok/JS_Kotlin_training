const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


//router.get('/', (req, res) => res.send('Привет, это API!'));

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
    if (await tokenExist(req.body.utoken)) {
        try {
            let rows = connection.oneOrNone(
            `SELECT u.uemail, u.urole, ui.uiname, ui.uiSecName, ui.uiMidName, ui.uiPhone, ui.uiSize, ui.uiInfo, ui.uiGroup 
            FROM usersinfo ui, users u 
            WHERE ui.users_idusers = u.idusers AND u.utoken = $1;`,
                [req.body.utoken]);
            if (await rows === null) {
                res.sendStatus(401)
            } else {
                res.send(await rows)
            }
        } catch (err) {
            console.log(err)
            res.sendStatus(400); // Информация о пользователе не найдена
        }
    } else {
        res.sendStatus(401)
    }
});

module.exports = router;