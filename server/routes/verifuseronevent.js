const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


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
    
    if (await checkrolebytoken(req.body.utoken) == 1) {
        console.log(req.body)
        try {
            let rows = connection.oneOrNone(
                `UPDATE rating 
                SET rverif = $1
                WHERE events_idevents = $2 AND usersinfo_idusersinfo = $3 
                RETURNING rverif ;`,
                [req.body.rverif, req.body.idevents, req.body.idusersinfo]);
            if (await rows === null) {
                console.log(await rows)
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