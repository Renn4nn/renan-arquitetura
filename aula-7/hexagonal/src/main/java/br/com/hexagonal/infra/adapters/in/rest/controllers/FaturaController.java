
package br.com.hexagonal.infra.adapters.in.rest.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hexagonal.domain.in.FaturaService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/fatura")
@RestController
@RequiredArgsConstructor
public class FaturaController {

  private final FaturaService faturaService;

  @PostMapping
  public ResponseEntity<FaturaPostResponse> post(
      @RequestBody FaturaPostRequest faturaPostRequest) {

    UUID faturaId = faturaService.faturaPedido(
        faturaPostRequest.pedidoId(),
        faturaPostRequest.valor());

    FaturaPostResponse response = new FaturaPostResponse(faturaId);

    return ResponseEntity.status(201).body(response);
  }
}
