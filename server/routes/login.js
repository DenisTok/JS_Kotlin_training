const express = require('express');
const router = express.Router();
const sha256 = require('js-sha256');
const connection = require('../db').connection;
const tsolt = 'a618f2aa1e7dfb9de83f8cc82284858debbb900c5a45ef3bdf0729506f7735ec';

function validateEmail(email) {
    var re = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
    return re.test(String(email).toLowerCase());
}


router.get('/', (req, res) => res.send('Привет, это API!'));

router.post('/', async (req, res) => { //логин post
    let errmes = "";

    if (validateEmail(req.body.uemail)) {
        try {
            let pasHash = sha256(req.body.uemail + req.body.password + tsolt)
            let token = sha256(req.body.uemail + pasHash + tsolt);

            try {
                let rows = connection.oneOrNone(
                    `SELECT idusers, utoken, urole, uemail FROM users WHERE utoken = $1;`,
                    [token]);                
                res.send(await rows);
            } catch (err) {
                console.error(err);
                res.sendStatus(400);
            }
        } catch (err) {
            console.error(err);
            res.sendStatus(500); //код ошибки 0 ошибка в конекте с таблицей
            
        }
    } else {
        is_user_exist = true;           //если не правильная то выключаем регистрацию - ставим что пользователь есть
        res.sendStatus(406);                 //код ошибки 1 емаил не правильный        
    }
});



module.exports = router;