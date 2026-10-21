package br.com.hexagonal.domain.out.persistence;

import br.com.hexagonal.domain.entities.Fatura;

public interface FaturaRepository {
  void salvar(Fatura fatura);
}
