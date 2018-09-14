const express = require('express');
const router = express.Router();
const connection = require('../db').connection;

router.get('/', async (req, res) => {
	try {
		let rows = connection.manyOrNone(`SELECT * FROM workers ORDER BY id;`);
		res.send(await rows);
	} catch (err) {
		console.error(err);
	}
});

router.get('/:id', async (req, res) => {
	try {
		
		let rows = connection.oneOrNone(
			`SELECT * FROM workers WHERE id = $1;`,
			[+req.params.id]);
		res.send(await rows);
	} catch (err) {
		console.error(err);
	}
});

router.post('/', async (req, res) => {
	try {
		let rows = connection.one(
			`INSERT INTO workers (name, surname) 
			VALUES ($1, $2) 
			RETURNING id, name, surname;`,
			[req.body.name, req.body.surname]);
		res.send(await rows);
	} catch (err) {
		console.error(err);
	}
});

router.put('/:id', async (req, res) => {
	try {
		let rows = connection.none(
			`UPDATE workers SET name = $1, surname = $2 WHERE id = $3`,
			[req.body.name, req.body.surname, +req.params.id]
		);
		res.sendStatus(200);
	} catch (err) {
		console.error(err);
	}
});

router.delete('/:id', async (req, res) => {
	try {
		let rows = connection.none(
			`DELETE FROM workers WHERE id = $1`,
			[+req.params.id]
		);
		res.sendStatus(200);
	} catch (err) {
		console.error(err);
	}
});

module.exports = router;
