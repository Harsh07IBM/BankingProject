import { accountService } from "../services/accountService";

export interface Account {
  id: number;
  name: string;
  age: number;
}

export const accountController = {
  getAccount(_req: any, res: any) {
    console.log("Account route hit");
    const account = accountService.getAccount();
    res.json(account);
  },
};
