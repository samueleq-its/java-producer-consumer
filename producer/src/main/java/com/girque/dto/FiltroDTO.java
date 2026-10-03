package com.girque.dto;

import java.util.Comparator;

import com.girque.entity.Prodotto;

public record FiltroDTO(
		String nome,
		String categoria,
		SortCriteria sort,
		Direction direction) implements Comparator<Prodotto> {

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
	@Override
	public int compare(Prodotto p1, Prodotto p2) {
		if (sort == null)
			return 0;

		Comparator<Prodotto> comp = sort.getComparator();
		if (direction == Direction.DESC) {
			comp = comp.reversed();
		}
		return comp.compare(p1, p2);
	}

	private boolean nomeMatches(Prodotto p) {
		return nome == null || nome.isEmpty() || p.getNome().toLowerCase().contains(nome.toLowerCase());
	}

	private boolean categoriaMatches(Prodotto p) {
		return categoria == null || categoria.isEmpty()
				|| p.getCategoria().toLowerCase().contains(categoria.toLowerCase());
	}

	private enum SortCriteria {
		ID(Comparator.comparing(Prodotto::getId, Comparator.nullsLast(Comparator.naturalOrder()))),
		NOME(Comparator.comparing(Prodotto::getNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))),
		DESCRIZIONE(Comparator.comparing(Prodotto::getDescrizione,
				Comparator.nullsLast(Comparator.naturalOrder()))),
		PREZZO(Comparator.comparing(Prodotto::getPrezzo, Comparator.nullsLast(Comparator.naturalOrder()))),
		CATEGORIA(Comparator.comparing(Prodotto::getCategoria,
				Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))),
		QUANTITA(Comparator.comparing(Prodotto::getQuantita, Comparator.nullsLast(Integer::compare))),
		DATACREAZIONE(Comparator.comparing(Prodotto::getDataCreazione,
				Comparator.nullsLast(Comparator.naturalOrder())));

		private final Comparator<Prodotto> comparator;

		SortCriteria(Comparator<Prodotto> comparator) {
			this.comparator = comparator;
		}

		public Comparator<Prodotto> getComparator() {
			return comparator;
		}
	}

	private enum Direction {
		ASC,
		DESC;
	}
}
