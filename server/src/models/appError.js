const logger = require('../config/logger');

class AppError extends Error {
  constructor(referer, error) {
    super();
    this.message = error;
    this.referer = referer;
  }

  static createInstance(referer, err) {
    if (err instanceof AppError) {
      return err;
    }

    logger.error(`[${referer}] := [Error: ${err}]`);
    return new AppError(referer, err);
  }
}

module.exports = AppError;
