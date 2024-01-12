const { port } = require('./src/config/index');
const app = require('./src/app');
const logger = require('./src/config/logger');

async function bootstrap() {
    try {
        const appServer = app.listen(port);
        logger.info(`Express started. Listening on ${port}`);
    } catch(error) {
        logger.error(error);
    }
}

bootstrap();
