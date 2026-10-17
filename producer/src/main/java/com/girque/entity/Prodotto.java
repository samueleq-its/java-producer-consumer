package com.girque.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prodotti")
// @Data // fa crashare l'applicazione all'avvio java.lang.ClassFormatError:
// Invalid method Code length 0 in class Prodotto
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private String categoria;
    private int quantita;
    private LocalDateTime dataCreazione;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantita() {
        return quantita;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    @Override
    public String toString() {
        return "Prodotto [id=" + id + ", nome=" + nome + ", descrizione=" + descrizione + ", prezzo=" + prezzo
                + ", categoria=" + categoria + ", quantita=" + quantita + ", dataCreazione=" + dataCreazione + "]";
    }

}
