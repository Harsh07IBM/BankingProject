const express = require("express");
const User = require("../models/user");
const bcrypt = require("bcryptjs");
const auth = require("../middleware/auth");

const router = new express.Router();

console.log("USER ROUTER LOADED");


// Create a new user
router.post("/users", async (req, res) => {

    try {

        const user = new User(req.body);

        await user.save();

        res.status(201).send({
            message: "User created successfully",
            user
        });

    } catch (error) {

        res.status(400).send({
            error: error.message
        });

    }
});

// User login
router.post("/users/login", async (req, res) => {

    try {

        const { email, password } = req.body;

        // Find user by email
        const user = await User.findOne({ email });

        if (!user) {
            return res.status(400).send({
                error: "Unable to login"
            });
        }

        // Compare entered password with hashed password
        const isMatch = await bcrypt.compare(
            password,
            user.password
        );

        if (!isMatch) {
            return res.status(400).send({
                error: "Unable to login"
            });
        }

        // Generate JWT
        const token = await user.generateAuthToken();

        res.send({
            message: "Login successful",
            token
        });

    } catch (error) {

        res.status(400).send({
            error: error.message
        });

    }
});

// Get logged-in user's profile
router.get("/users/me", auth, async (req, res) => {

    res.send({
        name: req.user.name,
        email: req.user.email
    });

});

module.exports = router;