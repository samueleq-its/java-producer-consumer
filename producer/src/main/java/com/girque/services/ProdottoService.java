package com.girque.services;

import java.util.List;

import com.girque.dto.ProdottoDTO;

public interface ProdottoService {

	List<ProdottoDTO> trovaTutti();

	void trovaPerId();

	void creaProdotto();

	void aggiornaProdotto();

	void eliminaProdotto();
}
