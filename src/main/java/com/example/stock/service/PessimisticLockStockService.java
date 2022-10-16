package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        Stock findStock = stockRepository.findByIdWithPessimisticLock(id);
        findStock.decreaseStock(quantity);
        stockRepository.saveAndFlush(findStock);
    }
}
