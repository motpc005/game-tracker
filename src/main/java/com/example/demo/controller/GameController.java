package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Game;
import com.example.demo.service.GameService;

@Controller
public class GameController {

	// the layer behind the controller (constructor injection)
	private final GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	// GET / : the bare address forwards to the list page
	@GetMapping("/")
	public String home() {
		return "redirect:/games";
	}

	// GET /games : show the list
	// status, genre and keyword are optional values from the URL (?status=playing)
	// only one filter applies at a time: status first, then genre, then keyword
	@GetMapping("/games")
	public String list(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String genre,
			@RequestParam(required = false) String keyword,
			Model model) {

		List<Game> games;

		if (status != null && !status.isBlank()) {
			games = gameService.findByStatus(status);
		} else if (genre != null && !genre.isBlank()) {
			games = gameService.findByGenre(genre);
		} else if (keyword != null && !keyword.isBlank()) {
			games = gameService.searchByTitle(keyword);
		} else {
			games = gameService.findAll();
		}

		// "games" is the label list.html uses to find this list
		model.addAttribute("games", games);
		return "games/list";
	}

	// GET /games/new : show an empty form (the new Game has the default values)
	@GetMapping("/games/new")
	public String showAddForm(Model model) {
		model.addAttribute("game", new Game());
		return "games/form";
	}

	// POST /games : save a new game
	// @Valid checks the rules in Game; BindingResult collects any problems
	@PostMapping("/games")
	public String create(
			@Valid @ModelAttribute Game game,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// show the form again with the error messages and typed values
			return "games/form";
		}
		gameService.save(game);
		return "redirect:/games";
	}

	// GET /games/{id}/edit : show the form filled with one game
	@GetMapping("/games/{id}/edit")
	public String showEditForm(
			@PathVariable Integer id,
			Model model) {
		Optional<Game> game = gameService.findById(id);

		// no game with this id: go back to the list instead of an error page
		if (game.isEmpty()) {
			return "redirect:/games";
		}

		model.addAttribute("game", game.get());
		return "games/form";
	}

	// POST /games/{id} : save changes to an existing game
	@PostMapping("/games/{id}")
	public String update(
			@PathVariable Integer id,
			@Valid @ModelAttribute Game game,
			BindingResult bindingResult) {
		// the form doesn't send the id, so attach it from the URL
		// (with an id, save does an UPDATE instead of an INSERT)
		game.setId(id);
		if (bindingResult.hasErrors()) {
			return "games/form";
		}
		gameService.save(game);
		return "redirect:/games";
	}

	// POST /games/{id}/delete : delete one game, then back to the list
	@PostMapping("/games/{id}/delete")
	public String delete(@PathVariable Integer id) {
		gameService.deleteById(id);
		return "redirect:/games";
	}

}