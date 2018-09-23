// SELECT e.ePoints, r.rVerif, e.ename
// FROM events e
// INNER JOIN rating r ON r.events_idevents = e.idevents
// WHERE r.usersInfo_users_idusers = (SELECT idusers FROM users WHERE utoken = 'd5164d32c2d67cd20216d99f25e76f16c1c435a3038d31bbe21b61179b5306c1')
