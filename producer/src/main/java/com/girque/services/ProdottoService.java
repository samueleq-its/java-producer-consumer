package com.girque.services;

import java.util.List;
import java.util.Optional;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;
import com.girque.dto.ProdottoRequestDTO;

public interface ProdottoService {

	List<ProdottoDTO> trovaTutti(FiltroDTO filtro);

	Optional<ProdottoDTO> trovaPerId(long id);

	ProdottoDTO creaProdotto(ProdottoRequestDTO pRequestDTO);

	ProdottoDTO aggiornaProdotto(long id, ProdottoRequestDTO pRequestDTO);

	void eliminaProdotto(long id);
}
