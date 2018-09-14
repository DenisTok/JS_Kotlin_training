const connection = require('./db').connection;

(async function createTables() {
	try {
		let res = connection.oneOrNone(`
		SELECT idusers FROM users WHERE utoken = 'ea44a2d0cfea07459eb8bb79c74f041c8cf7ed52fcb5c22d1154388efdc8ed0c'
		`);
		console.log(await res)
	}
	catch (err) {
		console.error(err);
	}
})();

//			`DELETE FROM users WHERE uemail = $1;`,['chislogrema@gmail.com']);
// `

// 		`ALTER TABLE users ADD COLUMN uRole INTEGER  DEFAULT 0;`);