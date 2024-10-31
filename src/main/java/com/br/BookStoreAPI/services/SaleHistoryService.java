package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.HistorySaleEntity;
import com.br.BookStoreAPI.models.entities.DetailsSaleEntity;
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
    public HistorySaleEntity createSale(Long userId, List<DetailsSaleEntity> saleItems) {
        UserEntity user = userRepository.findById(userId).get();
        HistorySaleEntity saleHistory = new HistorySaleEntity();
        saleHistory.setUser(user);

        double totalSalePrice = 0;
        for(DetailsSaleEntity saleItem : saleItems) {
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
