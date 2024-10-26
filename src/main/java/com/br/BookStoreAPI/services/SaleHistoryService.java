package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.SaleHistoryEntity;
import com.br.BookStoreAPI.models.entities.SaleItemEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.SaleHistoryRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class SaleHistoryService {

    @Autowired
    private SaleHistoryRepository saleHistoryRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public SaleHistoryEntity createSale(Long userId, List<SaleItemEntity> saleItems) {
        UserEntity user = userRepository.findById(userId).get();
        SaleHistoryEntity saleHistory = new SaleHistoryEntity();
        saleHistory.setUser(user);

        double totalSalePrice = 0;
        for(SaleItemEntity saleItem : saleItems) {
            BookEntity book = bookRepository.findById(saleItem.getBook().getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            saleItem.setUnitPrice(book.getPrice());
            saleItem.setTotalPrice(book.getPrice() * saleItem.getUnitPrice());
            saleItem.setSaleHistory(saleHistory);
            totalSalePrice += saleItem.getTotalPrice();
        }
        saleHistory.setTotalPrice(totalSalePrice);
        saleHistory.setSaleItems(saleItems);
        return saleHistoryRepository.save(saleHistory);
    }
}
