package com.girque.dto;

import java.time.LocalDateTime;

public record ProdottoDTO(
		int id,
		String nome,
		String descrizione,
		double prezzo,
		String categoria,
		int quantita,
		LocalDateTime dataCreazione) {

	// public static ProdottoDTO fromProdotto(Prodotto p) {

	// }

}
