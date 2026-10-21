package salamone.viada.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoRequestDTO {

    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private String categoria;
    private Integer quantita;
}
