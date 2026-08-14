// Load env vars first so JWT_SECRET is available
require("dotenv").config();

const mongoose = require("mongoose");
const { MongoMemoryServer } = require("mongodb-memory-server");
const jwt = require("jsonwebtoken");
const User = require("../models/user");
const Task = require("../models/task");

let mongoServer;

// --- Test User Data ---

const userOneId = new mongoose.Types.ObjectId();
const userOne = {
    _id: userOneId,
    name: "Test User One",
    email: "userone@test.com",
    password: "password123"
};

const userTwoId = new mongoose.Types.ObjectId();
const userTwo = {
    _id: userTwoId,
    name: "Test User Two",
    email: "usertwo@test.com",
    password: "password456"
};

// Generate tokens using the same secret as the app
const secret = process.env.JWT_SECRET || "taskappsecret";

const userOneToken = jwt.sign(
    { _id: userOneId.toString() },
    secret,
    { expiresIn: "7d" }
);

const userTwoToken = jwt.sign(
    { _id: userTwoId.toString() },
    secret,
    { expiresIn: "7d" }
);

// --- Connect to in-memory MongoDB ---

const connectTestDB = async () => {
    mongoServer = await MongoMemoryServer.create({
        instance: { launchTimeout: 30000 }
    });
    const mongoUri = mongoServer.getUri();

    // Disconnect any existing connection first
    if (mongoose.connection.readyState !== 0) {
        await mongoose.disconnect();
    }

    await mongoose.connect(mongoUri);
};

// --- Close connection and stop server ---

const closeTestDB = async () => {
    if (mongoose.connection.readyState !== 0) {
        await mongoose.connection.dropDatabase();
        await mongoose.disconnect();
    }
    if (mongoServer) {
        await mongoServer.stop();
    }
};

// --- Setup Database (seed test data) ---

const setupDatabase = async () => {
    await User.deleteMany();
    await Task.deleteMany();

    // Create user one (password will be hashed by pre-save hook)
    await new User(userOne).save();

    // Create user two
    await new User(userTwo).save();
};

module.exports = {
    userOneId,
    userOne,
    userOneToken,
    userTwoId,
    userTwo,
    userTwoToken,
    connectTestDB,
    closeTestDB,
    setupDatabase
};
