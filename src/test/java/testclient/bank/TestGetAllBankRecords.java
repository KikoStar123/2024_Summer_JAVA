package testclient.bank;

import client.service.Bank;
import client.service.BankUser;
import java.util.List;

public class TestGetAllBankRecords {
    public static void main(String[] args) {
        Bank client = new Bank();
        List<BankUser.BankRecord> records = client.getAllBankRecords("200000001"); // 假设用户名为200000001

        System.out.println("Found records:");
        for (BankUser.BankRecord record : records) {
            System.out.println("Balance Change: " + record.getBalanceChange());
            System.out.println("Balance Reason: " + record.getBalanceReason());
            System.out.println("Current Date: " + record.getCurDate());
            System.out.println("---------------------------");
        }
    }
}
