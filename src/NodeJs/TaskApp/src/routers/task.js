const express = require("express");
const Task = require("../models/task");
const auth = require("../middleware/auth");

const router = express.Router();

// Allowed sort fields to prevent arbitrary MongoDB field injection
const ALLOWED_SORT_FIELDS = ["createdAt", "updatedAt", "description", "completed"];


// Create a new task
router.post("/tasks", auth, async (req, res) => {

    try {

        const task = new Task({
            description: req.body.description,
            owner: req.user._id
        });

        await task.save();

        res.status(201).send(task);

    } catch (error) {

        console.log(error);

        res.status(400).send({
            error: error.message
        });
    }
});


// Get all tasks of logged-in user
// Supports filtering, pagination, and sorting
router.get("/tasks", auth, async (req, res) => {

    try {

        // --- Build filter ---
        const match = {
            owner: req.user._id
        };

        if (req.query.completed !== undefined) {
            const completedValue = req.query.completed;

            if (completedValue !== "true" && completedValue !== "false") {
                return res.status(400).send({
                    error: "completed must be 'true' or 'false'"
                });
            }

            match.completed = completedValue === "true";
        }

        // --- Build pagination ---
        let limit = 10; // default limit
        let skip = 0;   // default skip

        if (req.query.limit !== undefined) {
            limit = parseInt(req.query.limit, 10);

            if (isNaN(limit) || limit < 1) {
                return res.status(400).send({
                    error: "limit must be a positive number"
                });
            }

            // Cap maximum limit to prevent abuse
            if (limit > 100) {
                limit = 100;
            }
        }

        if (req.query.skip !== undefined) {
            skip = parseInt(req.query.skip, 10);

            if (isNaN(skip) || skip < 0) {
                return res.status(400).send({
                    error: "skip must be zero or a positive number"
                });
            }
        }

        // --- Build sort ---
        const sort = {};

        if (req.query.sortBy) {
            const parts = req.query.sortBy.split(":");

            if (parts.length !== 2) {
                return res.status(400).send({
                    error: "sortBy must be in format field:direction (e.g. createdAt:desc)"
                });
            }

            const field = parts[0];
            const direction = parts[1];

            if (!ALLOWED_SORT_FIELDS.includes(field)) {
                return res.status(400).send({
                    error: "Invalid sort field. Allowed fields: " + ALLOWED_SORT_FIELDS.join(", ")
                });
            }

            if (direction !== "asc" && direction !== "desc") {
                return res.status(400).send({
                    error: "Sort direction must be 'asc' or 'desc'"
                });
            }

            sort[field] = direction === "desc" ? -1 : 1;
        }

        const tasks = await Task.find(match)
            .sort(sort)
            .limit(limit)
            .skip(skip);

        res.send(tasks);

    } catch (error) {

        res.status(500).send({
            error: "Server error"
        });
    }
});


// Update a task (PATCH /tasks/:id)
router.patch("/tasks/:id", auth, async (req, res) => {

    const allowedUpdates = ["description", "completed"];
    const updates = Object.keys(req.body);
    const isValidOperation = updates.every((update) => allowedUpdates.includes(update));

    if (!isValidOperation) {
        return res.status(400).send({
            error: "Invalid updates. Allowed fields: " + allowedUpdates.join(", ")
        });
    }

    try {

        const task = await Task.findOne({
            _id: req.params.id,
            owner: req.user._id
        });

        if (!task) {
            return res.status(404).send({
                error: "Task not found"
            });
        }

        updates.forEach((update) => {
            task[update] = req.body[update];
        });

        await task.save();

        res.send(task);

    } catch (error) {

        res.status(400).send({
            error: error.message
        });
    }
});


// Delete a task (DELETE /tasks/:id)
router.delete("/tasks/:id", auth, async (req, res) => {

    try {

        const task = await Task.findOneAndDelete({
            _id: req.params.id,
            owner: req.user._id
        });

        if (!task) {
            return res.status(404).send({
                error: "Task not found"
            });
        }

        res.send({
            message: "Task deleted successfully",
            task
        });

    } catch (error) {

        res.status(500).send({
            error: "Server error"
        });
    }
});


module.exports = router;