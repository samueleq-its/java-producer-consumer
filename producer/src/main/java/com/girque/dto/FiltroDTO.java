package com.girque.dto;

import com.girque.entity.Prodotto;

public record FiltroDTO(
		String nome,
		String categoria,
		String sort,
		String direction) {

	public boolean matches(Prodotto p) {
		return nomeMatches(p) && categoriaMatches(p);
	}

	private boolean nomeMatches(Prodotto p) {
		return nome == null || nome.isEmpty() || p.getNome().toLowerCase().contains(nome.toLowerCase());
	}

	private boolean categoriaMatches(Prodotto p) {
		return categoria == null || categoria.isEmpty()
				|| p.getCategoria().toLowerCase().contains(categoria.toLowerCase());
	}
}
