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

router.post('/', async (req, res) => { //регистрация post
    let errmes = "";
    let is_user_exist = true; //существует ли пользователь

    if (validateEmail(req.body.email)) {
        try {
            //Проверяем есть ли пользователь в системе
            let rows = connection.oneOrNone(
                `SELECT uemail FROM users WHERE uemail = $1;`,
                [req.body.email]);

            if (await rows !== null) { //Если получаем емаил юзера "данные != null"
                // значит пользователь есть    
            } else {
                is_user_exist = false; //если "данные == null", то пользователя нету
            }
        } catch (err) {
            console.error(err);
            errmes += "[0]"//код ошибки 0 ошибка в конекте с таблицей
        }
    } else {
        is_user_exist = true;           //если не правильная то выключаем регистрацию - ставим что пользователь есть
        errmes += "[1]"                   //код ошибки 1 емаил не правильный        
    }

    if (is_user_exist === false) { // если пользователя нет, то регистрируем его
        let pasHash = sha256(req.body.email + req.body.password + tsolt)
        let token = sha256(req.body.email + pasHash + tsolt);

        try {
            let rows = connection.one(
                `INSERT INTO users (uEmail, uToken) 
                VALUES ($1, $2) 
                RETURNING idusers, uToken;`,
                [req.body.email, token]);
            res.send(await rows);
        } catch (err) {
            console.error(err);
        }
        
    } else {
        errmes += "[2]"//код ошибки 2 юзер уже есть
        res.send(errmes);
    }


});


module.exports = router;