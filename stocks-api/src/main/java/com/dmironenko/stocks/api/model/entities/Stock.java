package com.dmironenko.stocks.api.model.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Stock {
	@Id
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long lockVersion;

	@NotNull
	private String name;

	@Setter
	@NotNull
	@Column(precision = 19, scale = 8)
	private BigDecimal currentPrice;

	@UpdateTimestamp
	private Timestamp lastUpdate;
}
