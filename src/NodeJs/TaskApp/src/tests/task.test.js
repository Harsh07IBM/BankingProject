// Set test environment BEFORE importing app
process.env.NODE_ENV = "test";

const request = require("supertest");
const Task = require("../models/task");
const {
    userOneId,
    userOne,
    userOneToken,
    userTwoId,
    userTwo,
    userTwoToken,
    connectTestDB,
    closeTestDB,
    setupDatabase
} = require("./fixtures");

// Import app AFTER setting NODE_ENV so mongoose doesn't auto-connect
const app = require("../app");

// Connect to in-memory DB before all tests
beforeAll(async () => {
    await connectTestDB();
});

// Seed fresh data before each test
beforeEach(async () => {
    await setupDatabase();
});

// Cleanup after all tests
afterAll(async () => {
    await closeTestDB();
});


describe("Task API", () => {

    // -----------------------------------------------
    // 1. Authenticated user can create a task
    // -----------------------------------------------
    test("should create task for authenticated user", async () => {

        const response = await request(app)
            .post("/tasks")
            .set("Authorization", "Bearer " + userOneToken)
            .send({
                description: "Test task from supertest"
            })
            .expect(201);

        // Verify task was saved in database
        const task = await Task.findById(response.body._id);
        expect(task).not.toBeNull();
        expect(task.description).toBe("Test task from supertest");
        expect(task.completed).toBe(false);
        expect(task.owner.toString()).toBe(userOneId.toString());
    });

    // -----------------------------------------------
    // 2. Unauthenticated user cannot create a task
    // -----------------------------------------------
    test("should not create task without authentication", async () => {

        await request(app)
            .post("/tasks")
            .send({
                description: "Unauthorized task"
            })
            .expect(401);
    });

    // -----------------------------------------------
    // 3. Authenticated user can retrieve their tasks
    // -----------------------------------------------
    test("should get tasks for authenticated user", async () => {

        // Create some tasks for user one
        await new Task({ description: "Task A", owner: userOneId }).save();
        await new Task({ description: "Task B", owner: userOneId }).save();

        const response = await request(app)
            .get("/tasks")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(Array.isArray(response.body)).toBe(true);
        expect(response.body.length).toBe(2);
    });

    // -----------------------------------------------
    // 4. User cannot retrieve another user's tasks
    // -----------------------------------------------
    test("should not return tasks belonging to another user", async () => {

        // Create tasks for user one
        await new Task({ description: "User1 Task", owner: userOneId }).save();

        // Create tasks for user two
        await new Task({ description: "User2 Task", owner: userTwoId }).save();

        // Request as user one
        const response = await request(app)
            .get("/tasks")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        // Should only get user one's tasks
        expect(response.body.length).toBe(1);
        expect(response.body[0].description).toBe("User1 Task");

        // Verify none of the returned tasks belong to user two
        response.body.forEach((task) => {
            expect(task.owner.toString()).toBe(userOneId.toString());
        });
    });

    // -----------------------------------------------
    // 5. completed=true filtering works
    // -----------------------------------------------
    test("should filter tasks by completed=true", async () => {

        await new Task({ description: "Done task", completed: true, owner: userOneId }).save();
        await new Task({ description: "Pending task", completed: false, owner: userOneId }).save();

        const response = await request(app)
            .get("/tasks?completed=true")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.length).toBe(1);
        expect(response.body[0].description).toBe("Done task");
        expect(response.body[0].completed).toBe(true);
    });

    // -----------------------------------------------
    // 6. completed=false filtering works
    // -----------------------------------------------
    test("should filter tasks by completed=false", async () => {

        await new Task({ description: "Done task", completed: true, owner: userOneId }).save();
        await new Task({ description: "Pending task", completed: false, owner: userOneId }).save();

        const response = await request(app)
            .get("/tasks?completed=false")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.length).toBe(1);
        expect(response.body[0].description).toBe("Pending task");
        expect(response.body[0].completed).toBe(false);
    });

    // -----------------------------------------------
    // 7. Pagination using limit works
    // -----------------------------------------------
    test("should limit number of tasks returned", async () => {

        // Create 5 tasks
        for (let i = 1; i <= 5; i++) {
            await new Task({ description: "Task " + i, owner: userOneId }).save();
        }

        const response = await request(app)
            .get("/tasks?limit=3")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.length).toBe(3);
    });

    // -----------------------------------------------
    // 8. Pagination using skip works
    // -----------------------------------------------
    test("should skip tasks with skip parameter", async () => {

        // Create 5 tasks
        for (let i = 1; i <= 5; i++) {
            await new Task({ description: "Task " + i, owner: userOneId }).save();
        }

        const allResponse = await request(app)
            .get("/tasks?limit=100")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        const skipResponse = await request(app)
            .get("/tasks?skip=2&limit=100")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        // Should have 3 tasks after skipping 2 of 5
        expect(skipResponse.body.length).toBe(allResponse.body.length - 2);
    });

    // -----------------------------------------------
    // 9. Sorting ascending works
    // -----------------------------------------------
    test("should sort tasks ascending by createdAt", async () => {

        await new Task({ description: "First task", owner: userOneId }).save();
        await new Task({ description: "Second task", owner: userOneId }).save();
        await new Task({ description: "Third task", owner: userOneId }).save();

        const response = await request(app)
            .get("/tasks?sortBy=createdAt:asc")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.length).toBe(3);

        // Verify ascending order
        for (let i = 1; i < response.body.length; i++) {
            const prev = new Date(response.body[i - 1].createdAt).getTime();
            const curr = new Date(response.body[i].createdAt).getTime();
            expect(curr).toBeGreaterThanOrEqual(prev);
        }
    });

    // -----------------------------------------------
    // 10. Sorting descending works
    // -----------------------------------------------
    test("should sort tasks descending by createdAt", async () => {

        await new Task({ description: "First task", owner: userOneId }).save();
        await new Task({ description: "Second task", owner: userOneId }).save();
        await new Task({ description: "Third task", owner: userOneId }).save();

        const response = await request(app)
            .get("/tasks?sortBy=createdAt:desc")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.length).toBe(3);

        // Verify descending order
        for (let i = 1; i < response.body.length; i++) {
            const prev = new Date(response.body[i - 1].createdAt).getTime();
            const curr = new Date(response.body[i].createdAt).getTime();
            expect(curr).toBeLessThanOrEqual(prev);
        }
    });

    // -----------------------------------------------
    // 11. Filtering + pagination + sorting together
    // -----------------------------------------------
    test("should support filtering, pagination and sorting together", async () => {

        // Create a mix of completed and incomplete tasks
        await new Task({ description: "Incomplete 1", completed: false, owner: userOneId }).save();
        await new Task({ description: "Complete 1", completed: true, owner: userOneId }).save();
        await new Task({ description: "Incomplete 2", completed: false, owner: userOneId }).save();
        await new Task({ description: "Complete 2", completed: true, owner: userOneId }).save();
        await new Task({ description: "Incomplete 3", completed: false, owner: userOneId }).save();

        // Filter: completed=false, limit: 2, skip: 0, sort: createdAt:desc
        const response = await request(app)
            .get("/tasks?completed=false&limit=2&skip=0&sortBy=createdAt:desc")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        // Should get only 2 incomplete tasks
        expect(response.body.length).toBe(2);

        // All should be incomplete
        response.body.forEach((task) => {
            expect(task.completed).toBe(false);
        });

        // Should be in descending order
        if (response.body.length > 1) {
            const first = new Date(response.body[0].createdAt).getTime();
            const second = new Date(response.body[1].createdAt).getTime();
            expect(first).toBeGreaterThanOrEqual(second);
        }
    });

    // -----------------------------------------------
    // 12. Invalid query parameters handled properly
    // -----------------------------------------------
    test("should reject invalid completed parameter", async () => {

        await request(app)
            .get("/tasks?completed=notaboolean")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    test("should reject invalid limit parameter", async () => {

        await request(app)
            .get("/tasks?limit=-5")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    test("should reject invalid skip parameter", async () => {

        await request(app)
            .get("/tasks?skip=-1")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    test("should reject invalid sort field", async () => {

        await request(app)
            .get("/tasks?sortBy=password:asc")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    test("should reject invalid sort direction", async () => {

        await request(app)
            .get("/tasks?sortBy=createdAt:up")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    test("should reject invalid sortBy format", async () => {

        await request(app)
            .get("/tasks?sortBy=createdAt")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(400);
    });

    // -----------------------------------------------
    // GET /tasks without auth should fail
    // -----------------------------------------------
    test("should not get tasks without authentication", async () => {

        await request(app)
            .get("/tasks")
            .expect(401);
    });

});
