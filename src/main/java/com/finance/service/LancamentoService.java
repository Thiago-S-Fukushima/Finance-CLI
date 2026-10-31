package com.finance.service;

import com.finance.entity.Lancamento;
import com.finance.repository.LancamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoService {
    private final LancamentoRepository repository;
    
    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public Lancamento salvar(Lancamento lancamento) {
        return repository.save(lancamento);
    }

    public List<Lancamento> listarTodos() {
        return repository.findAll();
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
