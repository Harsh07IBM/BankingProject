const jwt = require("jsonwebtoken");
const User = require("../models/user");

const auth = async (req, res, next) => {

    try {

        // Get token from Authorization header
        const authHeader = req.header("Authorization");

        if (!authHeader) {
            return res.status(401).send({
                error: "Please authenticate."
            });
        }

        // Expected format:
        // Authorization: Bearer <token>

        const token = authHeader.replace("Bearer ", "");
        const secret = process.env.JWT_SECRET || "taskappsecret";

        // Verify JWT
        const decoded = jwt.verify(
            token,
            secret
        );

        // Find the user who owns this token
        const user = await User.findById(decoded._id);

        if (!user) {
            return res.status(401).send({
                error: "User not found."
            });
        }

        // Store user and token in request
        req.user = user;
        req.token = token;

        // Continue to the actual route
        next();

    } catch (error) {

        res.status(401).send({
            error: "Please authenticate."
        });

    }
};

module.exports = auth;