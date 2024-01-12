const winston = require('winston')

module.exports = winston.createLogger({
  format: winston.format.combine(
    winston.format.json(),
  ),
  transports: [
    new winston.transports.Console(),
  ]
});
