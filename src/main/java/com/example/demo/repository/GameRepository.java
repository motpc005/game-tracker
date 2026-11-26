package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {

	// exact match filters
	List<Game> findByStatus(String status, Sort sort);

	List<Game> findByGenre(String genre, Sort sort);

	// partial match, ignoring upper/lower case (Containing = %text%)
	List<Game> searchByTitleContainingIgnoreCase(String keyword, Sort sort);
}