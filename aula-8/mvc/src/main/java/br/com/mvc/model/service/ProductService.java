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
        if (product.getName() == null || product.getName().isBlank()
                || product.getPreco() == null || product.getPreco().signum() <= 0) {
            throw new IllegalArgumentException("Informe nome e preço maior que zero.");
        }
        product.setId(null);
        product.setStatus(br.com.mvc.model.entity.ProductStatus.INDISPONIVEL);
        return productRepository.save(product);
    }
    
    public List<Product> read(){
        return productRepository.findAll();
    }

    public Product discontinue(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        product.setStatus(br.com.mvc.model.entity.ProductStatus.DESCONTINUADO);
        return productRepository.save(product);
    }
}
