import express from "express";
import { accountRoute } from "./routes/accountRoutes";

const app = express();
const port = 3000;

accountRoute(app);

app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});