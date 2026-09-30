package com.girque.services;

import com.girque.repos.ProdottoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;

@Service
public class ProdottoServiceImpl implements ProdottoService {

	private final ProdottoRepository prodottoRepository;

	public ProdottoServiceImpl(ProdottoRepository prodottoRepository) {
		this.prodottoRepository = prodottoRepository;

	}

	@Override
	public List<ProdottoDTO> trovaTutti(FiltroDTO filtro) {
		// TODO implementare filtro
		return prodottoRepository.findAll()
				.stream()
				.map(ProdottoDTO::fromProdotto)
				.toList();
	}

	@Override
	public Optional<ProdottoDTO> trovaPerId(long id) {
		var p = prodottoRepository.findById(id);

		Optional<ProdottoDTO> prodottoDTO;
		try {
			prodottoDTO = Optional.ofNullable(ProdottoDTO.fromProdotto(p.orElseThrow()));
		} catch (NoSuchElementException e) {
			prodottoDTO = Optional.empty();
		}

		return prodottoDTO;
	}

	@Override
	public void creaProdotto() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'creaProdotto'");
	}

	@Override
	public void aggiornaProdotto() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'aggiornaProdotto'");
	}

	@Override
	public void eliminaProdotto() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminaProdotto'");
	}

}
