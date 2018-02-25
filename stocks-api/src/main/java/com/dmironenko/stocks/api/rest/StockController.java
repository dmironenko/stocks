package com.dmironenko.stocks.api.rest;

import com.dmironenko.stocks.api.model.entities.Stock;
import com.dmironenko.stocks.api.services.StockService;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "stocks")
@RequiredArgsConstructor
public class StockController {

	private final StockService stockService;

	@GetMapping
	public Page<Stock> findAll(final Pageable pageable) {
		return stockService.findAll(pageable);
	}

	@GetMapping(path = "/{id}")
	public Stock findOne(@PathVariable("id") final Long id) {
		return stockService.findOne(id);
	}

	@PostMapping
	public ResponseEntity<Stock> create(@Valid @RequestBody final Stock stock) {
		final Stock result = stockService.create(stock);

		return ResponseEntity
			.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(result.getId())
				.toUri())
			.body(result);
	}

	@PutMapping(path = "/{id}")
	public void update(@PathVariable("id") final Long id, @Valid @RequestBody final Stock stock) {
		stock.setId(id);

		stockService.update(stock);
	}
}
