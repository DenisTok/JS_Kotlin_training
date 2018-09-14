const express = require('express');
const bodyParser = require('body-parser');
const app = express();

const index = require('./routes/index');

const reg = require('./routes/reg');

const login = require('./routes/login');

const getevents = require('./routes/getevents');

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

app.listen(3000, () => console.log('API started on PORT 3000'));
