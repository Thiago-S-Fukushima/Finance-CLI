package com.finance.service;

import com.finance.entity.Lancamento;
import com.finance.repository.LancamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

    public Optional<Lancamento> buscarPorId(long id) {
        return repository.findById(id);
    }

    public Optional<Lancamento> atualizar(Long id, String descricao, BigDecimal valor, LocalDate data) {
        var resultado = repository.findById(id);

        if(resultado.isPresent()) {
            Lancamento lancamento = resultado.get();

            lancamento.setDescricao(descricao);
            lancamento.setValor(valor);
            lancamento.setData(data);

            return Optional.of(repository.save(lancamento));
        }
        
        return Optional.empty();
    }
}
