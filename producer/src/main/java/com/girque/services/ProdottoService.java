package com.girque.services;

import java.util.List;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;
import com.girque.dto.ProdottoRequestDTO;
import com.girque.exception.ProdottoNotFoundException;

public interface ProdottoService {

	/**
	 * returns all products filtered and sorted according to the provided filter.
	 */
	List<ProdottoDTO> trovaTutti(FiltroDTO filtro);

	ProdottoDTO trovaPerId(long id) throws ProdottoNotFoundException;

	ProdottoDTO creaProdotto(ProdottoRequestDTO pRequestDTO);

	ProdottoDTO aggiornaProdotto(long id, ProdottoRequestDTO pRequestDTO) throws ProdottoNotFoundException;

	void eliminaProdotto(long id);
}
