const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


//router.get('/', (req, res) => res.send('Привет, это API!'));

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

router.post('/', async (req, res) => {

    if (await checkrolebytoken(req.body.utoken) == 1) {
        try {
            let rows = connection.manyOrNone(
                `SELECT ui.uiname, ui.uisecname, ui.users_idusers, SUM(e.epoints)
                FROM events e
                JOIN rating r ON e.idevents = r.events_idevents
                JOIN usersinfo ui ON r.usersInfo_users_idusers = ui.users_idusers
                WHERE r.rverif = true
                GROUP BY ui.uisecname, ui.uiname, ui.users_idusers
                ORDER BY sum DESC;`);
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
        res.sendStatus(401)
    }
});

module.exports = router;