const express = require('express');
const bodyParser = require('body-parser');
const app = express();

const index = require('./routes/index');

const reg = require('./routes/reg');

const login = require('./routes/login');
const logintoken = require('./routes/logintoken');

const getevents = require('./routes/getevents');
const postevent = require('./routes/postevent');
const updateevent = require('./routes/updateevent');

const regonevent = require('./routes/regonevent');

const reguinfo = require('./routes/reguinfo');
const getuinfo = require('./routes/getuinfo');
const updateuinfo = require('./routes/updateuinfo');

const getusersonevent = require('./routes/getusersonevent');
const verifuseronevent = require('./routes/verifuseronevent');

const getTOP = require('./routes/getTOP');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/', index);
app.use('/reg', reg);
app.use('/reguinfo', reguinfo);
app.use('/getevents', getevents);
app.use('/login', login);
app.use('/getuinfo', getuinfo);
app.use('/logintoken', logintoken);
app.use('/postevent', postevent);
app.use('/regonevent', regonevent);
app.use('/getusersonevent', getusersonevent);
app.use('/verifuseronevent', verifuseronevent);
app.use('/updateevent', updateevent);
app.use('/updateuinfo', updateuinfo);
app.use('/gettop', getTOP);

//tests 3000 // no test: process.env.PORT
app.listen(3000, () => console.log('API started on PORT: ' + 3000));
