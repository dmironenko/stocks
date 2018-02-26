package com.dmironenko.stocks.api.services;

import com.dmironenko.stocks.api.exceptions.EntityNotFoundException;
import com.dmironenko.stocks.api.model.entities.Stock;
import com.dmironenko.stocks.api.repo.StockRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Service contains logic to CRU {@link Stock}
 */
@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	/**
	 * Returns a {@link Page} of Stocks meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable pageable
	 * @return a page of entities
	 */
	public Page<Stock> findAll(final Pageable pageable) {
		return stockRepository.findAll(pageable);
	}

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id not null
	 * @return the Stock with the given id
	 * @throws EntityNotFoundException if Stock is not found
	 */
	public Stock findOne(final Long id) {
		return Optional.ofNullable(stockRepository.findOne(id))
			.orElseThrow(() -> new EntityNotFoundException(id));
	}

	/**
	 * Saves a given Stock.
	 *
	 * @param stock to create
	 * @return the saved Stock
	 */
	public Stock create(final Stock stock) {
		return stockRepository.save(stock);
	}

	/**
	 * Update a given Stock.
	 *
	 * @param stock to create
	 * @return the saved Stock
	 */
	public Stock update(final Stock stock) {
		if (!stockRepository.exists(stock.getId())) {
			throw new EntityNotFoundException(stock.getId());
		}

		return stockRepository.save(stock);
	}
}
