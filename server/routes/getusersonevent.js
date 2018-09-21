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
    let a = req.body.ename
    if (await checkrolebytoken(req.body.utoken) == 1) {
        try {
            let rows = connection.manyOrNone(
                `SELECT a.idusersinfo, a.uiName, a.uiSecName, 
                        b.events_idevents, b.usersinfo_idusersinfo, b.rverif
                FROM usersinfo a, rating b 
                WHERE a.idusersinfo = b.usersinfo_idusersinfo AND 
                    b.events_idevents = $1;`,
                [req.body.idevents]);
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