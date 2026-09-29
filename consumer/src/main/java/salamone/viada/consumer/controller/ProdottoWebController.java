package salamone.viada.consumer.controller;

import salamone.viada.consumer.client.ProdottoApiClient;
import salamone.viada.consumer.dto.ProdottoDTO;
import salamone.viada.consumer.exception.ApiNonDisponibileException;
import salamone.viada.consumer.exception.ProdottoNonTrovatoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/prodotti")
@RequiredArgsConstructor
public class ProdottoWebController {

    private final ProdottoApiClient apiClient;

    @GetMapping
    public String lista(@RequestParam(required = false) String nome,
                        @RequestParam(required = false) String categoria,
                        @RequestParam(required = false) String sort,
                        @RequestParam(required = false, defaultValue = "asc") String direction,
                        Model model) {

        try {
            List<ProdottoDTO> prodotti = apiClient.trovaTutti(nome, categoria, sort, direction);
            model.addAttribute("prodotti", prodotti);
        } catch (ApiNonDisponibileException ex) {
            model.addAttribute("prodotti", List.of());
            model.addAttribute("errore",
                    "Impossibile recuperare i prodotti. " + ex.getMessage());
        }

        model.addAttribute("nome", nome);
        model.addAttribute("categoria", categoria);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("categorieDisponibili", List.of(
                "Informatica", "Accessori", "Audio", "Storage",
                "Mobile", "Wearable", "Networking", "Ufficio"));

        return "prodotti";
    }

    @GetMapping("/{id}")
    public String dettaglio(@PathVariable Long id,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            ProdottoDTO prodotto = apiClient.trovaPerId(id);
            model.addAttribute("prodotto", prodotto);
            return "dettaglio";
        } catch (ProdottoNonTrovatoException ex) {
            redirectAttributes.addFlashAttribute("errore",
                    "Prodotto con id " + id + " non trovato.");
            return "redirect:/prodotti";
        } catch (ApiNonDisponibileException ex) {
            model.addAttribute("errore",
                    "Impossibile recuperare il prodotto. " + ex.getMessage());
            return "error";
        }
    }
}
