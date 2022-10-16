package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @Test
    public void decrease_stock_test() {
        stockService.decreaseStock(1L, 1L);

        Stock findStock = stockRepository.findById(1L).orElseThrow();

        assertThat(findStock.getQuantity()).isEqualTo(99L);
    }

    @Test
    public void decrease_stock_with_multi_thread() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 Thread의 작업이 완료될때까지 기다림

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseStock(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock findStock = stockRepository.findById(1L).orElseThrow();
        assertThat(findStock.getQuantity()).isEqualTo(0L);
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }
}
