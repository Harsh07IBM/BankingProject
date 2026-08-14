// Set test environment BEFORE importing app
process.env.NODE_ENV = "test";

const request = require("supertest");
const User = require("../models/user");
const {
    userOneId,
    userOne,
    userOneToken,
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


describe("User API", () => {

    // -----------------------------------------------
    // 1. User signup successfully
    // -----------------------------------------------
    test("should sign up a new user", async () => {

        const response = await request(app)
            .post("/users")
            .send({
                name: "New User",
                email: "newuser@test.com",
                password: "newpass123"
            })
            .expect(201);

        // Verify user was created in database
        const user = await User.findOne({ email: "newuser@test.com" });
        expect(user).not.toBeNull();
        expect(user.name).toBe("New User");

        // Verify password is NOT returned in response
        expect(response.body.user.password).toBeUndefined();
    });

    // -----------------------------------------------
    // 2. Password is stored hashed, not as plain text
    // -----------------------------------------------
    test("should store password as hashed, not plain text", async () => {

        await request(app)
            .post("/users")
            .send({
                name: "Hash Test",
                email: "hashtest@test.com",
                password: "myplainpassword"
            })
            .expect(201);

        const user = await User.findOne({ email: "hashtest@test.com" });
        expect(user).not.toBeNull();

        // Password in DB should NOT be the plain text
        expect(user.password).not.toBe("myplainpassword");

        // Password should look like a bcrypt hash (starts with $2a$ or $2b$)
        expect(user.password).toMatch(/^\$2[ab]\$/);
    });

    // -----------------------------------------------
    // 3. User login successfully
    // -----------------------------------------------
    test("should login existing user", async () => {

        const response = await request(app)
            .post("/users/login")
            .send({
                email: userOne.email,
                password: userOne.password
            })
            .expect(200);

        // Should receive a token
        expect(response.body.token).toBeDefined();
        expect(typeof response.body.token).toBe("string");
    });

    // -----------------------------------------------
    // 4. Login fails with incorrect password
    // -----------------------------------------------
    test("should not login with incorrect password", async () => {

        await request(app)
            .post("/users/login")
            .send({
                email: userOne.email,
                password: "wrongpassword"
            })
            .expect(400);
    });

    // -----------------------------------------------
    // 5. Login fails for nonexistent user
    // -----------------------------------------------
    test("should not login nonexistent user", async () => {

        await request(app)
            .post("/users/login")
            .send({
                email: "nobody@test.com",
                password: "password123"
            })
            .expect(400);
    });

    // -----------------------------------------------
    // 6. GET /users/me works with valid JWT
    // -----------------------------------------------
    test("should get profile for authenticated user", async () => {

        const response = await request(app)
            .get("/users/me")
            .set("Authorization", "Bearer " + userOneToken)
            .expect(200);

        expect(response.body.name).toBe(userOne.name);
        expect(response.body.email).toBe(userOne.email);

        // Password should NOT be in the response
        expect(response.body.password).toBeUndefined();
    });

    // -----------------------------------------------
    // 7. GET /users/me fails without authentication
    // -----------------------------------------------
    test("should not get profile without authentication", async () => {

        await request(app)
            .get("/users/me")
            .expect(401);
    });

    // -----------------------------------------------
    // 8. GET /users/me fails with invalid JWT
    // -----------------------------------------------
    test("should not get profile with invalid JWT", async () => {

        await request(app)
            .get("/users/me")
            .set("Authorization", "Bearer invalidtoken123")
            .expect(401);
    });

    // -----------------------------------------------
    // Signup validation: missing fields
    // -----------------------------------------------
    test("should not sign up user with missing required fields", async () => {

        await request(app)
            .post("/users")
            .send({
                name: "No Email User"
            })
            .expect(400);
    });

});
