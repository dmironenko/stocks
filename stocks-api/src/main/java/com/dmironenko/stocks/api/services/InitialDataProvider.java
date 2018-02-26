package com.dmironenko.stocks.api.services;

import com.dmironenko.stocks.api.model.entities.Stock;
import com.dmironenko.stocks.api.repo.StockRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Component responsible for creation of initial data.
 */
@Component
@ConditionalOnProperty(name = "app.init-data.enabled", havingValue = "true")
final class InitialDataProvider {
	private final StockRepository stockRepository;

	private final Integer initialDataSize;

	public InitialDataProvider(final StockRepository stockRepository, @Value("${app.init-data.size:50}") final Integer initialDataSize) {
		this.stockRepository = stockRepository;
		this.initialDataSize = initialDataSize;
	}

	@PostConstruct
	public void postConstruct() {
		final Random r = new Random();
		final List<Stock> stocks = IntStream.range(0, initialDataSize)
			.mapToObj(i -> Stock.builder()
				.name("Stock" + i)
				.currentPrice(BigDecimal.valueOf(r.nextDouble()))
				.build())
			.collect(Collectors.toList());
		stockRepository.save(stocks);
	}
}
