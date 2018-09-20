const connection = require('./db').connection;

(async function createTables() {
	try {
		let res = connection.oneOrNone(`
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