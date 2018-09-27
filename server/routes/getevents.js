const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


//router.get('/', (req, res) => res.send('Привет, это API!'));

router.get('/', async (req, res) => { //регистрация информации post    
    try {
        
        let rows = connection.manyOrNone(
            `SELECT * FROM events  WHERE eisarhived = false AND eIsPublished = true AND NOT ePrivate = 2 ORDER BY idevents DESC;`);
        res.send(await rows);
    } catch (err) {
        res.send("[5]"); // Информация о пользователе не найдена
        console.error(err);
    }
});

async function checkrolebytoken(utoken) {
    try {
        let rows = connection.oneOrNone(
            `SELECT urole FROM users WHERE utoken = $1;`,
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
    if (await checkrolebytoken(req.body.utoken) == 1) {
        try {
            let rows = connection.manyOrNone(
                `SELECT * FROM events ORDER BY idevents DESC;`);
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