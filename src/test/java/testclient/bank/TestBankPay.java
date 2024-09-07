package testclient.bank;

import client.service.Bank;

public class TestBankPay {
    public static void main(String[] args) {
        Bank client = new Bank();
        client.payment("200000001", "password123", "2409079309", 2401.0f);
    }
}
