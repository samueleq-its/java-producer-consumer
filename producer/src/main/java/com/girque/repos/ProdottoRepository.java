package com.girque.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girque.entity.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByCategoria(String categoria);
    List<Prodotto> findByName(String nome);
    // List<Prodotto> orderByPrice(BigDecimal prezzo);
}
