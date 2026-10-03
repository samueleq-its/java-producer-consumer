package com.girque.controller;

import com.girque.services.ProdottoService;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;
import com.girque.dto.ProdottoRequestDTO;
import com.girque.exception.ProdottoNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

	private final ProdottoService prodottoService;

	ProdottoController(ProdottoService prodottoService) {
		this.prodottoService = prodottoService;
	}

	@GetMapping("") // ?nome=[nome]&categoria=[categoria]&sort=[attributo]&direction=[asc/desc]
	public ResponseEntity<List<ProdottoDTO>> getProdotti(FiltroDTO filtro) {

		return ResponseEntity.ok(prodottoService.trovaTutti(filtro));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdottoDTO> getProdottoById(@PathVariable long id) throws ProdottoNotFoundException {
		return ResponseEntity.ok(prodottoService.trovaPerId(id));
	}

	@PostMapping("")
	public ResponseEntity<ProdottoDTO> addProdotto(@RequestBody ProdottoRequestDTO pRequestDTO) {
		ProdottoDTO pDto = prodottoService.creaProdotto(pRequestDTO);
		return ResponseEntity.created(null).body(pDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProdottoDTO> updateProdotto(@PathVariable long id,
			@RequestBody ProdottoRequestDTO pRequestDTO) throws ProdottoNotFoundException {
		return ResponseEntity.ok(prodottoService.aggiornaProdotto(id, pRequestDTO));
	}

	@DeleteMapping("/{id}")
	public void deleteProdotto(@PathVariable long id) {
		prodottoService.eliminaProdotto(id);
	}

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
