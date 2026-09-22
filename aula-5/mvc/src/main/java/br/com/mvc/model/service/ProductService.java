package br.com.mvc.model.service;
import java.util.List;
import org.springframework.stereotype.Service;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.repository.ProductRepository;

@Service 
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product create(Product product){
        return productRepository.save(product);
    }
    
    public List<Product> read(){
        return productRepository.findAll();
    }
}
