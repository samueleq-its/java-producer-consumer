package salamone.viada.consumer.client;

import salamone.viada.consumer.dto.ProdottoDTO;
import salamone.viada.consumer.exception.ApiNonDisponibileException;
import salamone.viada.consumer.exception.ProdottoNonTrovatoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProdottoApiClient {

    private final RestClient prodottoRestClient;

    /**
     * GET /api/prodotti con filtri opzionali.
     */
    public List<ProdottoDTO> trovaTutti(String nome, String categoria,
                                        String sort, String direction) {
        try {
            return prodottoRestClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/prodotti");
                        if (nome != null && !nome.isBlank()) {
                            uriBuilder.queryParam("nome", nome.trim());
                        }
                        if (categoria != null && !categoria.isBlank()) {
                            uriBuilder.queryParam("categoria", categoria.trim());
                        }
                        if (sort != null && !sort.isBlank()) {
                            uriBuilder.queryParam("sort", sort);
                        }
                        if (direction != null && !direction.isBlank()) {
                            uriBuilder.queryParam("direction", direction);
                        }
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProdottoDTO>>() {});
        } catch (RestClientResponseException ex) {
            log.error("Errore HTTP dal Producer: status={}, body={}",
                    ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ApiNonDisponibileException(
                    "Errore durante la chiamata alla Producer API", ex);
        } catch (ResourceAccessException ex) {
            log.error("Producer API non raggiungibile: {}", ex.getMessage());
            throw new ApiNonDisponibileException(
                    "Il servizio API non è attualmente disponibile.", ex);
        }
    }

    /**
     * GET /api/prodotti/{id}
     */
    public ProdottoDTO trovaPerId(Long id) {
        try {
            return prodottoRestClient.get()
                    .uri("/prodotti/{id}", id)
                    .retrieve()
                    .body(ProdottoDTO.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ProdottoNonTrovatoException(
                        "Prodotto con id " + id + " non trovato.");
            }
            log.error("Errore HTTP dal Producer: status={}", ex.getStatusCode());
            throw new ApiNonDisponibileException(
                    "Errore durante la chiamata alla Producer API", ex);
        } catch (ResourceAccessException ex) {
            log.error("Producer API non raggiungibile: {}", ex.getMessage());
            throw new ApiNonDisponibileException(
                    "Il servizio API non è attualmente disponibile.", ex);
        }
    }
}
