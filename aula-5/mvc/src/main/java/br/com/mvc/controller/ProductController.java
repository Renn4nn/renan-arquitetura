package br.com.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.mvc.model.entity.Product;
import br.com.mvc.model.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    @GetMapping 
    public String read(Model model){
        model.addAttribute("products",productService.read());
        model.addAttribute("product",Product.builder().build());
        return "products";
    }

    @PostMapping 
    public String create(@ModelAttribute Product product){
        productService.create(product);
        return "redirect:/products";
    }
}
