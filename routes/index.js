const express = require('express');
const router = express.Router();

router.get('/', (req, res) => res.send('Привет, это API!'));

module.exports = router;
