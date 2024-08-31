package testhandler.shop;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShopConcurrentTest {

    private static final int THREAD_COUNT = 10; // 并发线程数
    private static final int REQUEST_PER_THREAD = 5; // 每个线程发送的请求数

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int threadNumber = i + 1;
            executorService.execute(() -> {
                try {
                    for (int j = 0; j < REQUEST_PER_THREAD; j++) {
                        System.out.println("Thread " + threadNumber + " sending request " + (j + 1));
                        ShopTest.sendAddProductRequest("P12345", "Laptop", "High performance laptop", "base64encodedImageString", 1200.99f, 999.99f, 100, "Warehouse A", 4.7f, true);
                        ShopTest.sendGetProductDetailsRequest("P12345");
                        ShopTest.sendGetAllProductsRequest();
                        ShopTest.sendAddToCartRequest("200000001", "P12345", 2);
                        ShopTest.sendCreateOrderRequest("200000001", "P12345", 2, 1999.98f);
                        System.out.println("Thread " + threadNumber + " completed request " + (j + 1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        System.out.println("All threads finished execution.");
    }
}
