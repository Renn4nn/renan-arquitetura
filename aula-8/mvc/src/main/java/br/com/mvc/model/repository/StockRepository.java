package br.com.mvc.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mvc.model.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {}
