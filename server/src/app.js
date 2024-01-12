'use strict';

const express = require('express');
const routes = require('./routes');

/*
const app = express();
const port =  process.env.PORT || 3000;

// set the view engine to ejs
app.set('view engine', 'ejs');

// routes
app.use('/', require('./routes/profile')());

// start server
const server = app.listen(port);
console.log('Express started. Listening on %s', port);
*/
const app = express();
app.use(express.json());
//app.disable('x-powered-by');
app.use(routes);

module.exports = app;
