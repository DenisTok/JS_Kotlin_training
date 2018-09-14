const express = require('express');
const router = express.Router();
const sha256 = require('js-sha256');
const connection = require('./db').connection;
const tsolt = 'a618f2aa1e7dfb9de83f8cc82284858debbb900c5a45ef3bdf0729506f7735ec';

function validateEmail(email) {
    var re = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
    return re.test(String(email).toLowerCase());
}


router.get('/', (req, res) => res.send('Привет, это API!'));

router.post('/', async (req, res) => { //логин post
    let errmes = "";

    if (validateEmail(req.body.email)) {
        try {
            let pasHash = sha256(req.body.email + req.body.password + tsolt)
            let token = sha256(req.body.email + pasHash + tsolt);

            try {
                let rows = connection.oneOrNone(
                    `SELECT idusers, utoken, urole FROM users WHERE utoken = $1;`,
                    [token]);
                res.send(await rows);
            } catch (err) {
                console.error(err);
            }
        } catch (err) {
            console.error(err);
            errmes += "[0]"//код ошибки 0 ошибка в конекте с таблицей
        }
    } else {
        is_user_exist = true;           //если не правильная то выключаем регистрацию - ставим что пользователь есть
        errmes += "[1]";
        res.send(errmes);                 //код ошибки 1 емаил не правильный        
    }
});

// router.put('/', async (req, res) => {
//     let errmes = ""
//     let is_email_exist = true;
//     console.log(req.body)
//     if (validateEmail(req.body.email)) {
//         try {
//             //Проверяем есть ли новый эмаил пользователя в системе
//             let rows = connection.oneOrNone(
//                 `SELECT uemail FROM users WHERE uemail = $1;`,
//                 [req.body.email]);

//             if (await rows !== null) {  //Если получаем емаил юзера "данные != null"
//                                         // значит пользователь есть    
//             } else {
                
//                 is_email_exist = false; //если "данные == null", то пользователя нету
//             }
            
//         } catch (err) {
//             console.error(err);
//             errmes += "[0]"//код ошибки 0 ошибка в конекте с таблицей
//             console.log(err)
//         }
//     } else {
//         is_email_exist = true;           //если не правильная то выключаем регистрацию - ставим что пользователь есть
//         errmes += "[1]" 
//         console.log(errmes)                  //код ошибки 1 емаил не правильный        
//     }
    
//     if (is_email_exist === false) {
//         let pasHash = sha256(req.body.email + req.body.password + tsolt)
//         let token = sha256(req.body.email + pasHash + tsolt);

//         try {            
//             try {
//                 let rows = connection.oneOrNone(
//                     `UPDATE users SET uemail = $1, utoken = $2 
//                     WHERE utoken = $3
//                     RETURNING *;`,
//                     [req.body.email, token, req.body.utoken]
//                 );
//                 res.send(await rows);
//             } catch (err) {
//                 console.error(err);
//                 errmes += "[0]"//код ошибки 0 ошибка в конекте с таблицей        
//             }
//         } catch (err) {
//             is_user_exist = true;           //если не правильная то выключаем регистрацию - ставим что пользователь есть
//             res.sendStatus(401);            //код ошибки 1 емаил не правильный       
//         }
//     } else {
//         res.sendStatus(409);                //код ошибки 1 емаил не правильный        
//     }

// });


module.exports = router;