package com.girque.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girque.dto.ProdottoDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class ProdottoController {

	// TODO: da rimuovere quanto c'è il DB
	private List<ProdottoDTO> prodotti = List.of(
			new ProdottoDTO(1, "nome", "descrizione", new BigDecimal(1.99), "categoria", 10,
					LocalDateTime.now()),
			new ProdottoDTO(2, "nome2", "descrizione2", new BigDecimal(19.99), "categoria2", 2,
					LocalDateTime.now()));

	// GET /api/prodotti
	@GetMapping("/prodotti")
	public ResponseEntity<List<ProdottoDTO>> getProdotti() {
		return ResponseEntity.ok(prodotti);
	}

	// GET /api/prodotti/{id}
	@GetMapping("/prodotti/{id}")
	public ResponseEntity<ProdottoDTO> getProdottoById(@PathVariable int id) {
		Optional<ProdottoDTO> prodotto = prodotti.stream()
				.filter(p -> p.id() == id)
				.findFirst();

		if (prodotto.isPresent()) {
			return ResponseEntity.ok(prodotto.get());
		}

		return ResponseEntity.notFound().build();
	}

	// POST /api/prodotti
	// PUT /api/prodotti/{id}
	// DELETE /api/prodotti/{id}

	// GET /api/prodotti?categoria=Informatica
	// GET /api/prodotti?nome=laptop
	// GET /api/prodotti?sort=prezzo
	// GET /api/prodotti?sort=prezzo&direction=desc

	// GET /api/prodotti?categoria=Accessori&sort=prezzo&direction=asc
	// GET /api/prodotti?nome=pro&sort=prezzo&direction=desc

	/*
	 * NON TROVATO
	 * {
	 * "status": 404,
	 * "message": "Prodotto non trovato",
	 * "timestamp": "2026-09-28T18:30:00"
	 * }
	 * 
	 * 400 Bad Request
	 * 
	 * @RestControllerAdvice per gestione errori centralizzata
	 */

}
