const connection = require('./db').connection;

(async function createTables() {
	try {
		let res = connection.manyOrNone(`
		CREATE TABLE users (
			idusers SERIAL  NOT NULL ,
			uEmail VARCHAR(60)    ,
			uRegDate TIMESTAMP  DEFAULT CURRENT_TIMESTAMP  ,
			uToken VARCHAR(65)    ,
			uRole INTEGER  DEFAULT 0    ,
		  PRIMARY KEY(idusers));
		  
		  
		  
		  CREATE TABLE events (
			idevents SERIAL  NOT NULL ,
			eName VARCHAR(45)    ,
			ePlace VARCHAR(45)    ,
			eDate VARCHAR(10)    ,
			eTime VARCHAR(8)    ,
			eTimeZone VARCHAR(4)    ,
			eInfo TEXT    ,
			ePeople INTEGER    ,
			ePoints INTEGER    ,
			ePrivate INTEGER  DEFAULT 1  ,
			eIsPublished BOOL  DEFAULT true  ,
			eIsArhived BOOL  DEFAULT false    ,
		  PRIMARY KEY(idevents));
		  
		  
		  
		  CREATE TABLE usersInfo (
			idusersInfo SERIAL  NOT NULL ,
			users_idusers INTEGER   NOT NULL ,
			uiName VARCHAR(20)    ,
			uiSecName VARCHAR(20)    ,
			uiMidName VARCHAR(20)    ,
			uiPhone VARCHAR(20)    ,
			uiSize VARCHAR(6)    ,
			uiInfo TEXT    ,
			uiGroup VARCHAR(45)      ,
		  PRIMARY KEY(idusersInfo, users_idusers)  ,
			FOREIGN KEY(users_idusers)
			  REFERENCES users(idusers)
				ON UPDATE CASCADE);
		  
		  
		  CREATE INDEX usersInfo_FKIndex1 ON usersInfo (users_idusers);
		  
		  CREATE INDEX IFK_Rel_01 ON usersInfo (users_idusers);
		  
		  
		  CREATE TABLE rating (
			idrating SERIAL  NOT NULL ,
			events_idevents INTEGER   NOT NULL ,
			usersInfo_idusersInfo INTEGER   NOT NULL ,
			usersInfo_users_idusers INTEGER   NOT NULL ,
			rVerif BOOL  DEFAULT false   ,
		  PRIMARY KEY(idrating, events_idevents, usersInfo_idusersInfo, usersInfo_users_idusers)    ,
			FOREIGN KEY(events_idevents)
			  REFERENCES events(idevents)
				ON DELETE CASCADE
				ON UPDATE CASCADE,
			FOREIGN KEY(usersInfo_idusersInfo, usersInfo_users_idusers)
			  REFERENCES usersInfo(idusersInfo, users_idusers)
				ON DELETE CASCADE
				ON UPDATE CASCADE);
		  
		  
		  CREATE INDEX rating_FKIndex1 ON rating (events_idevents);
		  CREATE INDEX rating_FKIndex2 ON rating (usersInfo_idusersInfo, usersInfo_users_idusers);
		  
		  CREATE INDEX IFK_Rel_02 ON rating (events_idevents);
		  CREATE INDEX IFK_Rel_03 ON rating (usersInfo_idusersInfo, usersInfo_users_idusers);
		  
		  
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