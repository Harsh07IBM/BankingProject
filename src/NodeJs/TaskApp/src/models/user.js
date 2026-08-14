const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

const userSchema = new mongoose.Schema(
    {
        name: {
            type: String,
            required: true,
            trim: true
        },

        email: {
            type: String,
            required: true,
            unique: true,
            trim: true,
            lowercase: true
        },

        password: {
            type: String,
            required: true,
            minlength: 6
        }
    },
    {
        timestamps: true
    }
);


// Hash password before saving user
userSchema.pre("save", async function () {

    if (!this.isModified("password")) {
        return;
    }

    this.password = await bcrypt.hash(this.password, 8);
});


// Generate JWT authentication token
userSchema.methods.generateAuthToken = async function () {

    const user = this;
    const secret = process.env.JWT_SECRET || "taskappsecret";

    const token = jwt.sign(
        { _id: user._id.toString() },
        secret,
        { expiresIn: "7d" }
    );

    return token;
};


// Remove password from JSON responses
userSchema.methods.toJSON = function () {
    const user = this;
    const userObject = user.toObject();
    delete userObject.password;
    return userObject;
};


const User = mongoose.model("User", userSchema);

module.exports = User;