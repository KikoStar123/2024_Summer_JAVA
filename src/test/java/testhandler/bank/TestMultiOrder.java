package testhandler.bank;

import org.json.JSONObject;
import server.service.BankService;

public class TestMultiOrder {

    public static void main(String[] args) {
        BankService bankService = BankService.getInstance();

        // 启动三个不同订单的 wait 请求
        Thread waitThread1 = new Thread(() -> {
            JSONObject result = bankService.waitForPayment("order1", 100.0);
            System.out.println("Wait result for order1: " + result);
        });

        Thread waitThread2 = new Thread(() -> {
            JSONObject result = bankService.waitForPayment("order2", 200.0);
            System.out.println("Wait result for order2: " + result);
        });

        Thread waitThread3 = new Thread(() -> {
            JSONObject result = bankService.waitForPayment("order3", 300.0);
            System.out.println("Wait result for order3: " + result);
        });

        // 启动三个对应的 payment 请求
        Thread paymentThread1 = new Thread(() -> {
            JSONObject result = bankService.processPayment("order1", "200000001", "password123", 100.0);
            System.out.println("Payment result for order1: " + result);
        });

        Thread paymentThread2 = new Thread(() -> {
            JSONObject result = bankService.processPayment("order2", "200000001", "password123", 200.0);
            System.out.println("Payment result for order2: " + result);
        });

        Thread paymentThread3 = new Thread(() -> {
            JSONObject result = bankService.processPayment("order3", "200000001", "password123", 300.0);
            System.out.println("Payment result for order3: " + result);
        });

        // 启动所有线程
        waitThread1.start();
        waitThread2.start();
        waitThread3.start();
        paymentThread1.start();
        paymentThread2.start();
        paymentThread3.start();

        // 等待所有线程完成
        try {
            waitThread1.join();
            waitThread2.join();
            waitThread3.join();
            paymentThread1.join();
            paymentThread2.join();
            paymentThread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
