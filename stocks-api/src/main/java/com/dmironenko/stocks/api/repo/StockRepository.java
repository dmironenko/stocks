package com.dmironenko.stocks.api.repo;

import com.dmironenko.stocks.api.model.entities.Stock;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {
}
