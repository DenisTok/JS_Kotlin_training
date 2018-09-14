const express = require('express');
const router = express.Router();
const connection = require('../db').connection;


//router.get('/', (req, res) => res.send('Привет, это API!'));

router.get('/', async (req, res) => { //регистрация информации post    
    try {
        
        let rows = connection.manyOrNone(
            `SELECT * FROM events;`);
        res.send(await rows);
    } catch (err) {
        res.send("[5]"); // Информация о пользователе не найдена
        console.error(err);
    }
});

module.exports = router;