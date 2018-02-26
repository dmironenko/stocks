package com.dmironenko.stocks.api.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dmironenko.stocks.api.exceptions.EntityNotFoundException;
import com.dmironenko.stocks.api.model.entities.Stock;
import com.dmironenko.stocks.api.repo.StockRepository;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {

	@Mock
	private StockRepository stockRepository;

	@InjectMocks
	private StockService stockService;

	@Test
	public void findAll_Success() {
		// Given
		Pageable page = mock(Pageable.class);
		@SuppressWarnings("unchecked")
		Page<Stock> given = mock(Page.class);
		when(stockRepository.findAll(page)).thenReturn(given);

		// When
		final Page<Stock> result = stockService.findAll(page);

		// Then
		verify(stockRepository).findAll(page);
		assertEquals(result, given);
	}

	@Test
	public void findOne_Success() {
		// Given
		Stock given = mock(Stock.class);
		when(stockRepository.findOne(43L)).thenReturn(given);

		// When
		final Stock result = stockService.findOne(43L);

		// Then
		verify(stockRepository).findOne(43L);
		assertEquals(result, given);
	}

	@Test(expected = EntityNotFoundException.class)
	public void findOne_NoStockFound_ThrowsEntityNotFoundException() {
		stockService.findOne(43L);
	}

	@Test
	public void create_Success() {
		// Given
		Stock given = mock(Stock.class);
		when(stockRepository.save(given)).thenReturn(given);

		// When
		final Stock result = stockService.create(given);

		// Then
		verify(stockRepository).save(given);
		assertEquals(result, given);
	}

	@Test
	public void update_Success() {
		// Given
		final Long stockId = ThreadLocalRandom.current().nextLong();
		Stock given = Stock.builder().id(stockId).build();

		when(stockRepository.exists(stockId)).thenReturn(true);
		when(stockRepository.save(given)).thenReturn(given);

		// When
		final Stock result = stockService.update(given);

		// Then
		verify(stockRepository).exists(stockId);
		verify(stockRepository).save(given);
		assertEquals(result, given);
	}

	@Test(expected = EntityNotFoundException.class)
	public void update_NoStockFound_ThrowsEntityNotFoundException() {
		// Given
		Stock given = mock(Stock.class);
		when(stockRepository.exists(anyLong())).thenReturn(false);

		// When
		stockService.update(given);
	}
}