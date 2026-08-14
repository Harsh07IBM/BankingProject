require("dotenv").config();

const express = require("express");
const path = require("path");

require("./db/mongoose");

const userRouter = require("./routers/user");
const taskRouter = require("./routers/task");

const app = express();

// Middleware to read JSON and form request bodies
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Serve static frontend files
app.use(express.static(path.join(__dirname, "../public")));

// User routes
app.use(userRouter);

// Task routes
app.use(taskRouter);

// Test route
app.get("/test", (req, res) => {
    res.send("Server is working");
});

// Export app for testing (server startup is in index.js)
module.exports = app;