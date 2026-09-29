package com.girque.repos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girque.entity.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByCategoria(String categoria);

    List<Prodotto> findByNome(String nome);

    // List<Prodotto> orderByPrezzo(BigDecimal prezzo); // non funziona
}
