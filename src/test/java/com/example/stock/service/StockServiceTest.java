package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }
}
