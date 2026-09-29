package com.girque.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.girque.entity.Prodotto;

public record ProdottoDTO(
		long id,
		String nome,
		String descrizione,
		BigDecimal prezzo,
		String categoria,
		int quantita,
		LocalDateTime dataCreazione) {

	public static ProdottoDTO fromProdotto(Prodotto p) {
		return new ProdottoDTO(
				p.getId(),
				p.getNome(),
				p.getDescrizione(),
				p.getPrezzo(),
				p.getCategoria(),
				p.getQuantita(),
				p.getDataCreazione());
	}

}
