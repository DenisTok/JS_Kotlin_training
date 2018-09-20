const express = require('express');
const bodyParser = require('body-parser');
const app = express();

const index = require('./routes/index');

const reg = require('./routes/reg');

const login = require('./routes/login');
const logintoken = require('./routes/logintoken');

const getevents = require('./routes/getevents');
const postevent = require('./routes/postevent');
const regonevent = require('./routes/regonevent');

const reguinfo = require('./routes/reguinfo');
const getuinfo = require('./routes/getuinfo');

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

app.listen(3000, () => console.log('API started on PORT 3000'));
