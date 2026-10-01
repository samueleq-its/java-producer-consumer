package com.girque.services;

import com.girque.repos.ProdottoRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;
import com.girque.dto.ProdottoRequestDTO;
import com.girque.entity.Prodotto;

@Service
public class ProdottoServiceImpl implements ProdottoService {

	private final ProdottoRepository prodottoRepository;

	public ProdottoServiceImpl(ProdottoRepository prodottoRepository) {
		this.prodottoRepository = prodottoRepository;

	}

	@Override
	public List<ProdottoDTO> trovaTutti(FiltroDTO filtro) {
		return prodottoRepository.findAll()
				.stream()
				.filter(filtro::matches)
				// TODO sorting
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
	public ProdottoDTO creaProdotto(ProdottoRequestDTO pRequestDTO) {
		Prodotto p = pRequestDTO.toProdotto();
		Prodotto nuovoProdotto = prodottoRepository.save(p);
		return ProdottoDTO.fromProdotto(nuovoProdotto);
	}

	@Override
	public ProdottoDTO aggiornaProdotto(long id, ProdottoRequestDTO pRequestDTO) {
		if (prodottoRepository.findById(id).isEmpty()) {
			// TODO come gestirlo?
			return null;
		}

		Prodotto p = pRequestDTO.toProdotto();
		p.setId(id);

		Prodotto prodottoAggiornato = prodottoRepository.save(p);

		return ProdottoDTO.fromProdotto(prodottoAggiornato);
	}

	@Override
	public void eliminaProdotto(long id) {
		prodottoRepository.deleteById(id);
	}

}
