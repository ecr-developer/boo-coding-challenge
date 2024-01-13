import '../src/nest-modules/shared-module/testing/supertest-extend';
import { MongoMemoryServer } from "mongodb-memory-server";
import { MongoClient } from "mongodb";

process.env.NODE_ENV = 'e2e';

let mongo: any;
beforeAll(async () => {
    mongo = await MongoMemoryServer.create();
    const mongoUri = mongo.getUri();
    await MongoClient.connect(mongoUri);
});

beforeEach(async () => {
    jest.clearAllMocks();
//    const collections = await MongoClient.connection.db.collections();
//    for (let collection of collections) {
//    await collection.deleteMany({});
//    }
});

afterAll(async () => {
    await mongo.stop();
//    await mongoose.connection.close();
});