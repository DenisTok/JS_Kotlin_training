const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


router.get('/', (req, res) => res.send('Привет, это API!'));

async function checkrolebytoken(utoken) {
    try {
        let rows = connection.oneOrNone(
            `SELECT urole FROM users WHERE utoken = $1`,
            [utoken]);
        let role = await rows;
        if (await role === null) {
            return 0
        } else if (role.urole >= 0) {
            return 1
        }
    } catch (err) {
        console.log(err);
        return 0
    }
}

async function isUserOnEvent(utoken, idevents) {
    try {
        let rows = connection.oneOrNone(
            `SELECT events_idevents, usersinfo_users_idusers FROM rating WHERE events_idevents = $1 AND usersinfo_users_idusers = (SELECT idusers FROM users WHERE utoken = $2)`,
            [idevents, utoken]);
        
        if (await rows === null) {        
            return 1
        } else {
            return 0
        }
    } catch (err) {
        console.log(err);
        return 0
    }
}

router.post('/', async (req, res) => {
    if (await checkrolebytoken(req.body.utoken) != 0) {
        if (await isUserOnEvent(req.body.utoken, req.body.idevents) != 0) {
            try {
                let rows = connection.oneOrNone(
                    `INSERT INTO rating (events_idevents, usersinfo_idusersinfo, usersinfo_users_idusers) 
                VALUES ($1, (SELECT idusersinfo FROM usersinfo WHERE users_idusers = (SELECT idusers FROM users WHERE utoken = $2)), 
                            (SELECT idusers FROM users WHERE utoken = $2)) 
                RETURNING idrating;`,
                    [req.body.idevents, req.body.utoken]);
                if (await rows === null) {
                    res.sendStatus(401)
                } else {
                    res.send(await rows)
                }
            } catch (err) {
                console.error(err);
                res.sendStatus(400); // Информация о пользователе не найдена
            }
        } else {
            res.sendStatus(409);
        }
    } else{
        res.sendStatus(401);
    }
});

module.exports = router;