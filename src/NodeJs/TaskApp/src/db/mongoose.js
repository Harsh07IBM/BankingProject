const mongoose = require("mongoose");

const connectDB = async () => {
    // Use test database URI when running tests, otherwise use the normal URI
    const mongoURI = process.env.NODE_ENV === "test"
        ? (process.env.MONGODB_TEST_URI || process.env.MONGO_URI)
        : process.env.MONGO_URI;

    try {
        await mongoose.connect(mongoURI);
        console.log("MongoDB connected successfully");
    } catch (error) {
        console.log("MongoDB connection failed");
        console.log(error);
    }
};

// Auto-connect when not in test mode
// In test mode, tests handle the connection themselves
if (process.env.NODE_ENV !== "test") {
    connectDB();
}

module.exports = { mongoose, connectDB };