const { port } = require('./src/config/index');
const app = require('./src/app');

async function bootstrap() {
    try {
        const appServer = app.listen(port);
    } catch(error) {
        console.error('Err-index', error);
    }
}

bootstrap();
