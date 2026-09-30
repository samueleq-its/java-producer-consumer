package com.girque.services;

import java.util.List;
import java.util.Optional;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;

public interface ProdottoService {

	List<ProdottoDTO> trovaTutti(FiltroDTO filtro);

	Optional<ProdottoDTO> trovaPerId(long id);

	void creaProdotto();

	void aggiornaProdotto();

	void eliminaProdotto();
}
