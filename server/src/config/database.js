const { MongoClient } = require('mongodb');
const { mongo } = require('./index');
const AppError = require('../models/appError');

const client = new MongoClient(mongo.url, { useNewUrlParser: true, useUnifiedTopology: true });
let database;

exports.connect = async () => {
    try {
        await client.connect();
        database = client.db(mongo.database);
    } catch (err) {
        if (err instanceof AppError) {
            throw err;
        } else {
            throw new AppError('Error connecting to MongoDB', err);
        }
    }
};

exports.getDatabase = () => database;
exports.getClient = () => client;
