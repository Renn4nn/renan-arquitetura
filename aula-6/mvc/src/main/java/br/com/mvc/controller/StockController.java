package br.com.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.mvc.model.service.ProductService;
import br.com.mvc.model.service.StockService;

@Controller
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;
    private final ProductService productService;

    public StockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("stocks", stockService.list());
        model.addAttribute("products", productService.read());
        return "stock";
    }

    @PostMapping
    public String move(@RequestParam Long productId, @RequestParam int quantity,
                       @RequestParam String operation) {
        stockService.move(productId, quantity, operation);
        return "redirect:/stock";
    }
}
