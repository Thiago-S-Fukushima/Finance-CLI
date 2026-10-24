package com.finance.repository;

import com.finance.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository
    extends JpaRepository<Lancamento, Long> {
    
}
