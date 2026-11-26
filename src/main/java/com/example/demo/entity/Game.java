package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

// one row of the game table
// @Data (Lombok) generates the getters, setters, toString, equals and hashCode
@Entity
@Table(name = "game")
@Data
public class Game {

	// ---------- identity ----------

	// primary key; MySQL generates it (AUTO_INCREMENT), so it is null for a new game
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// ---------- required text ----------

	// @NotBlank / @Size are the rules checked by @Valid in the controller
	// @Column is only the mapping to the table (the database column)
	@NotBlank(message = "Please enter a title")
	@Size(max = 100, message = "Title must be 100 characters or less")
	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@NotBlank(message = "Please enter a genre")
	@Size(max = 50, message = "Genre must be 50 characters or less")
	@Column(name = "genre", nullable = false, length = 50)
	private String genre;

	// ---------- dropdown values (defaults match the table defaults) ----------

	@Size(max = 20)
	@Column(name = "platform", nullable = false, length = 20)
	private String platform = "steam";

	// backlog / playing / finished / replay
	@Size(max = 20)
	@Column(name = "status", nullable = false, length = 20)
	private String status = "backlog";

	// ---------- optional numbers (Integer / BigDecimal so they can be null) ----------

	@Min(value = 1, message = "Rating must be at least 1")
	@Max(value = 10, message = "Rating must be 10 or less")
	@Column(name = "rating")
	private Integer rating;

	@DecimalMin(value = "0", message = "Hours cannot be negative")
	@Column(name = "hours_played")
	private BigDecimal hoursPlayed;

	// ---------- optional text and dates ----------

	@Column(name = "notes")
	private String notes;

	// the Java default replaces the database default (curdate())
	@Column(name = "date_added")
	private LocalDate dateAdded = LocalDate.now();

	@Column(name = "date_finished")
	private LocalDate dateFinished;

}