CREATE TABLE users (
  idusers INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  uEmail VARCHAR(60)  NULL  ,
  uRegDate TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP ,
  uToken VARCHAR(65)  NULL  ,
  uRole INTEGER UNSIGNED  NULL DEFAULT 0   ,
PRIMARY KEY(idusers));


CREATE TABLE events (
  idevents INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  eName VARCHAR(45)  NULL  ,
  ePlace VARCHAR(45)  NULL  ,
  eDate VARCHAR(10)  NULL  ,
  eTime VARCHAR(8)  NULL  ,
  eTimeZone VARCHAR(4)  NULL  ,
  eInfo TEXT  NULL  ,
  ePeople INTEGER UNSIGNED  NULL  ,
  ePoints INTEGER UNSIGNED  NULL  ,
  ePrivate INTEGER UNSIGNED  NULL DEFAULT 1 ,
  eIsPublished BOOL  NULL DEFAULT true ,
  eIsArhived BOOL  NULL DEFAULT false   ,
PRIMARY KEY(idevents));


CREATE TABLE usersInfo (
  idusersInfo INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  users_idusers INTEGER UNSIGNED  NOT NULL  ,
  uiName VARCHAR(20)  NULL  ,
  uiSecName VARCHAR(20)  NULL  ,
  uiMidName VARCHAR(20)  NULL  ,
  uiPhone VARCHAR(20)  NULL  ,
  uiSize VARCHAR(6)  NULL  ,
  uiInfo TEXT  NULL  ,
  uiGroup VARCHAR(45)  NULL    ,
PRIMARY KEY(idusersInfo, users_idusers)  ,
INDEX usersInfo_FKIndex1(users_idusers),
  FOREIGN KEY(users_idusers)
    REFERENCES users(idusers)
      ON DELETE NO ACTION
      ON UPDATE CASCADE);


CREATE TABLE rating (
  idrating INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  events_idevents INTEGER UNSIGNED  NOT NULL  ,
  usersInfo_idusersInfo INTEGER UNSIGNED  NOT NULL  ,
  usersInfo_users_idusers INTEGER UNSIGNED  NOT NULL  ,
  rVerif BOOL  NULL DEFAULT 0   ,
PRIMARY KEY(idrating, events_idevents, usersInfo_idusersInfo, usersInfo_users_idusers)  ,
INDEX rating_FKIndex1(events_idevents)  ,
INDEX rating_FKIndex2(usersInfo_idusersInfo, usersInfo_users_idusers),
  FOREIGN KEY(events_idevents)
    REFERENCES events(idevents)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(usersInfo_idusersInfo, usersInfo_users_idusers)
    REFERENCES usersInfo(idusersInfo, users_idusers)
      ON DELETE CASCADE
      ON UPDATE CASCADE);


