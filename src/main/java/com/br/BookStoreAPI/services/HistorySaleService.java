package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.HistorySaleEntity;
import com.br.BookStoreAPI.models.entities.SaleEntity;
import org.springframework.stereotype.Service;

@Service
public class HistorySaleService {
    public HistorySaleEntity createHistory(SaleEntity saleEntity, BookEntity bookEntity, Integer quantity) {
        HistorySaleEntity historySaleEntity = new HistorySaleEntity();
        historySaleEntity.setSale(saleEntity);
        historySaleEntity.setBook(bookEntity);
        historySaleEntity.setQuantityPurchased(quantity);
        historySaleEntity.setPricePerBook(bookEntity.getPrice());
        historySaleEntity.setTotalAmount(bookEntity.getPrice() * quantity);
        return historySaleEntity;
    }
}
