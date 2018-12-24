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
app.use('/reg', reg); //Регистрация
app.use('/reguinfo', reguinfo); //Регистрация пользовательских данных
app.use('/getevents', getevents); //выводин список мероприятий
app.use('/login', login); // Вход
app.use('/getuinfo', getuinfo); //Вывод инф. о пользователе
app.use('/logintoken', logintoken); //Логит по токену
app.use('/postevent', postevent);//Запостить мероприятие
app.use('/regonevent', regonevent); //Зергистрироваться на мероприятие
app.use('/getusersonevent', getusersonevent); //Вывести пользователей на мероприятие
app.use('/verifuseronevent', verifuseronevent); //Отметить пользователя на мероприятие
app.use('/updateevent', updateevent); //обновить инф. на мероприятии
app.use('/updateuinfo', updateuinfo); //Обновить информацию пользователя
app.use('/gettop', getTOP); //Вывести топ

//tests 3000 // no test: process.env.PORT
app.listen(process.env.PORT, () => console.log('API started on PORT: ' + process.env.PORT));
