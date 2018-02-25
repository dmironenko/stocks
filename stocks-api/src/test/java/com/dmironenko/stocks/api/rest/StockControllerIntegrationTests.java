package com.dmironenko.stocks.api.rest;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.dmironenko.stocks.api.model.entities.Stock;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StockControllerIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private static Stock randomStock() {
		return Stock.builder()
			.name(UUID.randomUUID().toString())
			.currentPrice(randomPrice())
			.build();
	}

	private static BigDecimal randomPrice() {
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		return BigDecimal.valueOf(random.nextDouble()).setScale(8, ROUND_HALF_UP);
	}

	@Test
	public void getOne_Status200() {
		// Given
		final Stock given = randomStock();

		final ResponseEntity<Stock> createResponse = restTemplate.postForEntity("/stocks", given, Stock.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(CREATED);
		final Stock created = createResponse.getBody();

		// When
		final ResponseEntity<Stock> resultResponse = restTemplate.getForEntity("/stocks/" + created.getId(), Stock.class);

		//Then
		assertThat(resultResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		final Stock result = resultResponse.getBody();
		assertThat(result.getId()).isNotNull();
		assertThat(result.getLockVersion()).isNotNull();
		assertThat(result.getLastUpdate()).isNotNull();
		assertThat(result.getCurrentPrice()).isEqualByComparingTo(given.getCurrentPrice());
		assertThat(result.getName()).isEqualTo(given.getName());
	}

	@Test
	public void getOne_NonExistingStock_Status404() {
		// When
		final ResponseEntity<Stock> stockResponseEntity = restTemplate.getForEntity("/stocks/" + Long.MAX_VALUE, Stock.class);

		// Then
		assertThat(stockResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void getAll_StatusOK() {
		// Given
		final Stock stock1 = randomStock();

		final ResponseEntity<Stock> createResponse1 = restTemplate.postForEntity("/stocks", stock1, Stock.class);
		assertThat(createResponse1.getStatusCode()).isEqualTo(CREATED);

		final Stock stock2 = randomStock();
		final ResponseEntity<Stock> createResponse2 = restTemplate.postForEntity("/stocks", stock2, Stock.class);
		assertThat(createResponse2.getStatusCode()).isEqualTo(CREATED);

		// When
		final ResponseEntity<Page<Stock>> getAll = restTemplate.exchange("/stocks", HttpMethod.GET, null,
			new ParameterizedTypeReference<Page<Stock>>() {
			});

		// Then
		assertThat(getAll.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getAll.getBody().getTotalElements()).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void post_NewStock_StatusCreated() {
		// Given
		final Stock given = randomStock();

		// When
		final ResponseEntity<Stock> createResponse = restTemplate.postForEntity("/stocks", given, Stock.class);

		// Then
		assertThat(createResponse.getStatusCode()).isEqualTo(CREATED);

		// Check location header
		final URI expand = restTemplate.getRestTemplate().getUriTemplateHandler().expand("/stocks/{id}", createResponse.getBody().getId());
		assertThat(createResponse.getHeaders().get(LOCATION)).containsExactly(expand.toString());

		final Stock result = createResponse.getBody();
		assertThat(result.getId()).isNotNull();
		assertThat(result.getLockVersion()).isNotNull();
		assertThat(result.getLastUpdate()).isNotNull();
		assertThat(result.getCurrentPrice()).isEqualByComparingTo(given.getCurrentPrice());
		assertThat(result.getName()).isEqualTo(given.getName());
	}

	@Test
	public void put_ExistingStock_StatusOK() {
		// Given
		final Stock given = randomStock();

		final ResponseEntity<Stock> createResponse = restTemplate.postForEntity("/stocks", given, Stock.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(CREATED);

		final BigDecimal newPrice = randomPrice();
		final Stock created = createResponse.getBody();
		created.setCurrentPrice(newPrice);

		final URI expand = restTemplate.getRestTemplate().getUriTemplateHandler().expand("/stocks/{id}", created.getId());
		final RequestEntity<Stock> request = RequestEntity.put(expand).contentType(APPLICATION_JSON_UTF8).body(created);

		// When
		final ResponseEntity<Void> updateResponse = restTemplate.exchange(request, Void.class);
		// Then
		assertThat(updateResponse.getStatusCode()).isEqualTo(OK);

		final ResponseEntity<Stock> updatedResponse = restTemplate.getForEntity("/stocks/" + created.getId(), Stock.class);
		final Stock updated = updatedResponse.getBody();

		assertThat(updated.getCurrentPrice()).isEqualByComparingTo(newPrice);
	}

	@Test
	public void put_NonExistingStock_StatusNotFound() {
		// Given
		final Stock given = randomStock();
		given.setId(Long.MAX_VALUE);

		final URI expand = restTemplate.getRestTemplate().getUriTemplateHandler().expand("/stocks/{id}", Long.MAX_VALUE);
		final RequestEntity<Stock> request = RequestEntity.put(expand).contentType(APPLICATION_JSON_UTF8).body(given);

		// When
		final ResponseEntity<Void> updateResponse = restTemplate.exchange(request, Void.class);
		// Then
		assertThat(updateResponse.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	/**
	 * Simplified page implementation.
	 *
	 * @param <T> type of content
	 */
	private static class Page<T> {

		private long totalElements;
		private List<T> content;

		public long getTotalElements() {
			return totalElements;
		}

		public List<T> getContent() {
			return content;
		}
	}
}
