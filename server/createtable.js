const connection = require('./db').connection;

(async function createTables() {
	try {
		let res = connection.manyOrNone(`
		SELECT ui.uiname, ui.uisecname, ui.users_idusers, SUM(e.epoints)
		FROM events e
		JOIN rating r ON e.idevents = r.events_idevents
		JOIN usersinfo ui ON r.usersInfo_users_idusers = ui.users_idusers
		WHERE r.rverif = true
		GROUP BY ui.uisecname, ui.uiname, ui.users_idusers
		ORDER BY sum DESC;
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