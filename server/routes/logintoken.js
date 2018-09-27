const express = require('express');
const router = express.Router();
const sha256 = require('js-sha256');
const connection = require('../db').connection;
const tsolt = 'a618f2aa1e7dfb9de83f8cc82284858debbb900c5a45ef3bdf0729506f7735ec';

router.get('/', (req, res) => res.send('Привет, это API!'));

async function tokenExist(uToken) {    
    if (uToken.length == 64) {        
        try {
            let rows = connection.oneOrNone(
                `SELECT idusers, uemail, urole, utoken FROM users WHERE utoken = $1`,
                [uToken]);
            if (await rows === null) {
                return false
            } else {
                return true
            }
        } catch (err) {
            return false
        }
    } else {
        return false
    }
}

router.post('/', async (req, res) => { //логин По токену post
    let errmes = "";

    if (await tokenExist(req.body.utoken)) {
        try {
            try {
                let rows = connection.oneOrNone(
                    `SELECT idusers, urole, uemail, utoken FROM users WHERE utoken = $1;`,
                    [req.body.utoken]);
                res.send(await rows);
            } catch (err) {
                res.sendStatus(400);
            }
        } catch (err) {
            res.sendStatus(400);
        }
    } else {
        res.sendStatus(401);
    }
});



module.exports = router;