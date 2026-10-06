package salamone.viada.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoDTO {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private String categoria;
    private Integer quantita;
    private LocalDateTime dataCreazione;
}