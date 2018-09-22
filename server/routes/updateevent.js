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
        } else if(role.urole == 1 ) {            
            return 1
        }
    } catch (err) {
        return 0
    }
}

router.post('/', async (req, res) => {
    let a = req.body.ename
    if (await checkrolebytoken(req.body.utoken) == 1 && req.body.ename.length > 1) {
        try {
            let rows = connection.oneOrNone(
                `UPDATE events 
                SET ename = $1, ePlace = $2, eDate = $3, eTime = $4, eTimeZone = $5, eInfo = $6, ePeople = $7, ePoints = $8,
                ePrivate = $9, eIsPublished = $10, eIsArhived = $11
                WHERE idevents = $12
                RETURNING idevents;`,
                [req.body.ename, req.body.eplace, req.body.edate, req.body.etime, 
                    req.body.etimezone, req.body.einfo, req.body.epeople, req.body.epoints, 
                    req.body.eprivate, req.body.eispublished, req.body.eisarhived, req.body.idevents]);
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