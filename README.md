# FlightHub-CBSE
Hello. This is the backend of a Flight Booking Website developed using SpringBoot (using CBSE approach) and MongoDB.

To run this repository:
1. Clone this repository
2. Create a .env file in root directory and put your MongoDB uri inside like this ```MONGO_URI=<your_mongo_uri>```
3. Run ```mvn clean install```
4. Run FlightHubApplication

If you wanna see it in action with frontend, I made a frontend repo at <a href=https://github.com/iamAden/flighthub-vercel>here</a>. Run intructions will be there.

Also, preferrably don't change the server port or else it won't work at the frontend side.

There is a csv file for the flights dataset if you wanna import it
To import csv to MongoDB (atlas):
1. Download the MongoDB Database Tools <a href=https://www.mongodb.com/try/download/database-tools>here</a>
2. Unzip and add the bin folder to PATH
3. Open Command Prompt
4. To make sure it's correctly set up can run ```mongoimport --version```
5. Now to import, you need to have Mongo connection
6. run ```mongoimport --uri <CONNECTION_STRING> --db <DATABASE_NAME> --collection <COLLECTION_NAME> --type csv --file <FILE_PATH> --headerline```
