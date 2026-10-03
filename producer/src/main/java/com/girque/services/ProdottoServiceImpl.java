package com.girque.services;

import com.girque.repos.ProdottoRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.girque.dto.FiltroDTO;
import com.girque.dto.ProdottoDTO;
import com.girque.dto.ProdottoRequestDTO;
import com.girque.entity.Prodotto;
import com.girque.exception.ProdottoNotFoundException;

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
				.sorted(filtro)
				.map(ProdottoDTO::fromProdotto)
				.toList();
	}

	@Override
	public ProdottoDTO trovaPerId(long id) throws ProdottoNotFoundException {
		Prodotto p = prodottoRepository.findById(id).orElseThrow(ProdottoNotFoundException::new);

		ProdottoDTO pDto = ProdottoDTO.fromProdotto(p);

		return pDto;
	}

	@Override
	public ProdottoDTO creaProdotto(ProdottoRequestDTO pRequestDTO) {
		Prodotto p = pRequestDTO.toProdotto();
		p.setDataCreazione(LocalDateTime.now());
		Prodotto nuovoProdotto = prodottoRepository.save(p);
		return ProdottoDTO.fromProdotto(nuovoProdotto);
	}

	@Override
	public ProdottoDTO aggiornaProdotto(long id, ProdottoRequestDTO pRequestDTO) throws ProdottoNotFoundException {
		Prodotto oldProdotto = prodottoRepository.findById(id).orElseThrow(ProdottoNotFoundException::new);

		Prodotto p = pRequestDTO.toProdotto();
		p.setId(oldProdotto.getId());
		p.setDataCreazione(oldProdotto.getDataCreazione());

		Prodotto prodottoAggiornato = prodottoRepository.save(p);

		return ProdottoDTO.fromProdotto(prodottoAggiornato);
	}

	@Override
	public void eliminaProdotto(long id) {
		prodottoRepository.deleteById(id);
	}

}
