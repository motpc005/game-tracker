package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Game;
import com.example.demo.repository.GameRepository;

@Service
public class GameService {

	// every list is sorted by title, so it's defined once here
	private static final Sort BY_TITLE = Sort.by("title");

	// the layer behind the service (constructor injection)
	private final GameRepository gameRepository;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	// ---------- read ----------

	// one game by id; Optional because the id might not exist
	public Optional<Game> findById(Integer id) {
		return gameRepository.findById(id);
	}

	// every game, A to Z by title
	public List<Game> findAll() {
		return gameRepository.findAll(BY_TITLE);
	}

	// exact match on status
	public List<Game> findByStatus(String status) {
		return gameRepository.findByStatus(status, BY_TITLE);
	}

	// exact match on genre
	public List<Game> findByGenre(String genre) {
		return gameRepository.findByGenre(genre, BY_TITLE);
	}

	// partial match on title ("had" finds "Hades")
	public List<Game> searchByTitle(String keyword) {
		return gameRepository.searchByTitleContainingIgnoreCase(keyword, BY_TITLE);
	}

	// ---------- write ----------

	// used for both add (no id) and edit (has id)
	public Game save(Game game) {
		game.setTitle(capitalizeIfAllLowercase(game.getTitle()));
		return gameRepository.save(game);
	}

	// ---------- delete ----------

	public void deleteById(Integer id) {
		gameRepository.deleteById(id);
	}

	// ---------- helpers ----------

	// "shadow of the colossus" -> "Shadow Of The Colossus"
	// a title with any capital (like "iRacing") is left exactly as typed
	private String capitalizeIfAllLowercase(String title) {
		if (title == null || title.isBlank() || !title.equals(title.toLowerCase())) {
			return title;
		}

		StringBuilder fixed = new StringBuilder();
		for (String word : title.trim().split(" ")) {
			if (word.isEmpty()) {
				continue; // skip the gaps left by double spaces
			}
			fixed.append(word.substring(0, 1).toUpperCase())
					.append(word.substring(1))
					.append(" ");
		}
		return fixed.toString().trim();
	}
}