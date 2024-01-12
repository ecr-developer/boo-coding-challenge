require('dotenv').config();

module.exports = {
    port: process.env.PORT || '3000',
    retriesAttempt: process.env.RETRIES_ATTEMPT || 4,
    xDelayTime: process.env.X_DELAY_TIME || 30000,
};
