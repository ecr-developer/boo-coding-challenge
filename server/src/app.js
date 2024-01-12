'use strict';

const express = require('express');
const path = require("path");
const health = require('./health');

const app = express();
app.use(express.json());
app.disable('x-powered-by');
app.set("views", path.join(__dirname, "views"));
// set the view engine to ejs
app.set('view engine', 'ejs');
app.use(health);
app.use('/', require('./routes/profile')());
app.use(express.static(path.join(__dirname, "public")));

module.exports = app;
