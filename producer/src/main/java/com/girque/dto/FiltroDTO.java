package com.girque.dto;

import com.girque.entity.Prodotto;

public record FiltroDTO(
		String nome,
		String categoria,
		String sort,
		String direction) {

	/**
	 * Checks if a product matches the name and category filters, if they are
	 * specified.
	 */
	public boolean matches(Prodotto p) {
		return nomeMatches(p) && categoriaMatches(p);
	}

	/**
	 * compares two products based on the sorting criteria and direction.
	 */
	public int compare(Prodotto p1, Prodotto p2) {
		int result;
		switch (sortTarget()) {
			case ID:
				result = p1.getId().compareTo(p2.getId());
				break;
			case NOME:
				result = p1.getNome().compareTo(p2.getNome());
				break;
			case DESCRIZIONE:
				result = p1.getDescrizione().compareTo(p2.getDescrizione());
				break;
			case PREZZO:
				result = p1.getPrezzo().compareTo(p2.getPrezzo());
				break;
			case CATEGORIA:
				result = p1.getCategoria().compareTo(p2.getCategoria());
				break;
			case QUANTITA:
				result = p1.getQuantita() - p2.getQuantita();
				break;
			case DATACREAZIONE:
				result = p1.getDataCreazione().compareTo(p2.getDataCreazione());
				break;
			case null:
				result = 0;
				break;
		}

		return sortDirection() == Direction.ASC ? result : -result;
	}

	private boolean nomeMatches(Prodotto p) {
		return nome == null || nome.isEmpty() || p.getNome().toLowerCase().contains(nome.toLowerCase());
	}

	private boolean categoriaMatches(Prodotto p) {
		return categoria == null || categoria.isEmpty()
				|| p.getCategoria().toLowerCase().contains(categoria.toLowerCase());
	}

	private SortTarget sortTarget() {
		try {
			return SortTarget.valueOf(sort.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	private Direction sortDirection() {
		try {
			return Direction.valueOf(direction.toUpperCase());
		} catch (Exception e) {
			return Direction.ASC;
		}
	}

	private enum SortTarget {
		ID, NOME, DESCRIZIONE, PREZZO, CATEGORIA, QUANTITA, DATACREAZIONE;
	}

	private enum Direction {
		ASC, DESC
	}
}
