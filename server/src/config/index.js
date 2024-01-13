require('dotenv').config();

module.exports = {
    port: process.env.PORT || '3000',
    retriesAttempt: process.env.RETRIES_ATTEMPT || 4,
    xDelayTime: process.env.X_DELAY_TIME || 30000,
    mongo: {
        url: process.env.DB_CONNECTION_STRING || 'mongodb://localhost:27017',
        database: process.env.MONGO_DATABASE || 'BooProfiles',
    }
};
