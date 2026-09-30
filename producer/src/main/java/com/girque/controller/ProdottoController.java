package com.girque.controller;

import com.girque.services.ProdottoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class ProdottoController {

	private final ProdottoService prodottoService;

	ProdottoController(ProdottoService prodottoService) {
		this.prodottoService = prodottoService;
	}

	@GetMapping("/prodotti")
	public ResponseEntity<List<ProdottoDTO>> getProdotti(FiltroDTO filtro) {

		return ResponseEntity.ok(prodottoService.trovaTutti(filtro));
	}

	@GetMapping("/prodotti/{id}")
	public ResponseEntity<ProdottoDTO> getProdottoById(@PathVariable long id) {
		Optional<ProdottoDTO> prodotto = prodottoService.trovaPerId(id);

		if (prodotto.isPresent()) {
			return ResponseEntity.ok(prodotto.get());
		}

		return ResponseEntity.notFound().build();
	}

	// POST /api/prodotti
	// PUT /api/prodotti/{id}
	// DELETE /api/prodotti/{id}

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
