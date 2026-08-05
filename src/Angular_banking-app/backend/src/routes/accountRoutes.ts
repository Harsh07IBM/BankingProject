import type { Application } from "express";
import { accountController } from "../controllers/accountController";

export const accountRoute = (app: Application) => {
  app.get("/api/account", accountController.getAccount);
};
