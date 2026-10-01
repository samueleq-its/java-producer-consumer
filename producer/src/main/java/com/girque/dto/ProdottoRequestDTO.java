package com.girque.dto;

import java.math.BigDecimal;

import com.girque.entity.Prodotto;

public record ProdottoRequestDTO(
		String nome,
		String descrizione,
		BigDecimal prezzo,
		String categoria,
		int quantita) {

	public Prodotto toProdotto() {
		Prodotto p = new Prodotto();
		p.setId(null);
		p.setNome(this.nome);
		p.setDescrizione(this.descrizione);
		p.setPrezzo(this.prezzo);
		p.setCategoria(this.categoria);
		p.setQuantita(this.quantita);
		p.setDataCreazione(null);
		return p;
	}

	public static ProdottoRequestDTO fromProdotto(Prodotto p) {
		return new ProdottoRequestDTO(
				p.getNome(),
				p.getDescrizione(),
				p.getPrezzo(),
				p.getCategoria(),
				p.getQuantita());
	}

}
