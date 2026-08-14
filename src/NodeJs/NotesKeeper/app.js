const express = require("express");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
require("dotenv").config();

const Note = require("./models/Note");

const app = express();

app.set("view engine", "ejs");

app.use(bodyParser.json());

app.use(bodyParser.urlencoded({
    extended: true
}));


mongoose.connect(process.env.MONGO_URI)
    .then(() => {
        console.log("MongoDB connected");
    })
    .catch((error) => {
        console.log("MongoDB connection error:", error);
    });


// Display all notes
app.get("/", async (req, res) => {

    try {

        const notes = await Note.find();

        res.render("home", {
            data: notes
        });

    } catch (error) {

        console.log(error);
        res.status(500).send("Error loading notes");

    }
});


// Add note
app.post("/", async (req, res) => {

    try {

        const note = new Note({
            noteContent: req.body.noteContent
        });

        await note.save();

        res.redirect("/");

    } catch (error) {

        console.log(error);
        res.status(500).send("Error adding note");

    }
});


// Update note
app.post("/update", async (req, res) => {

    try {

        await Note.findByIdAndUpdate(
            req.body.noteId,
            {
                noteContent: req.body.noteContent
            }
        );

        res.redirect("/");

    } catch (error) {

        console.log(error);
        res.status(500).send("Error updating note");

    }
});


// Delete note
app.post("/delete", async (req, res) => {

    try {

        await Note.findByIdAndDelete(req.body.noteId);

        res.redirect("/");

    } catch (error) {

        console.log(error);
        res.status(500).send("Error deleting note");

    }
});


app.listen(3000, () => {
    console.log("App is running on port 3000");
});