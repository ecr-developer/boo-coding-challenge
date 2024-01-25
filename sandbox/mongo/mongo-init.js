
print('Start #################################################################');

db = db.getSiblingDB("BooWorld");
db.createUser(
    {
      user: 'api_user',
      pwd: 'api1234',
      roles: [{ role: 'readWrite', db: 'BooWorld' }],
    },
);
db.createCollection("Accounts");
db.createCollection("Comments");

db = db.getSiblingDB("BooWorldTests");
db.createUser(
    {
      user: 'api_user',
      pwd: 'api1234',
      roles: [{ role: 'readWrite', db: 'BooWorldTests' }],
    },
);
db.createCollection("Accounts");
db.createCollection("Comments");

print('END #################################################################');