package br.com.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.mvc.model.service.PedidoService;
import br.com.mvc.model.service.ProductService;

@Controller
@RequestMapping("/sales")
public class PedidoController {
    private final PedidoService pedidos;
    private final ProductService products;

    public PedidoController(PedidoService pedidos, ProductService products) {
        this.pedidos = pedidos;
        this.products = products;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pedidos", pedidos.listar());
        model.addAttribute("products", products.read());
        return "sales";
    }

    @PostMapping
    public String criar(@RequestParam Long productId, @RequestParam int quantity,
                        @RequestParam String emailCliente) {
        pedidos.criar(productId, quantity, emailCliente);
        return "redirect:/sales";
    }

    @PostMapping("/{id}/close")
    public String fechar(@PathVariable Long id) {
        pedidos.fechar(id);
        return "redirect:/sales";
    }
}
