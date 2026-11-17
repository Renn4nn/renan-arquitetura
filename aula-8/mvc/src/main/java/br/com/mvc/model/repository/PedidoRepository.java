package br.com.mvc.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mvc.model.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
