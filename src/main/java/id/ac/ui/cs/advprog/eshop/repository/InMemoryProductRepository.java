package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.util.IdGenerator;
import id.ac.ui.cs.advprog.eshop.util.UuidIdGenerator;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> productData = new ArrayList<>();
    private final IdGenerator idGenerator;

    public InMemoryProductRepository() {
        this(new UuidIdGenerator());
    }

    public InMemoryProductRepository(IdGenerator idGenerator) {
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(idGenerator.generate());
        }
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findProductById(String productId) {
        for (Product product : productData) {
            if (product.getProductId() != null && product.getProductId().equals(productId)) {
                return product;
            }
        }

        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            return null;
        }
        for (int i = 0; i < productData.size(); i++) {
            Product currentProduct = productData.get(i);
            if (product.getProductId().equals(currentProduct.getProductId())) {
                productData.set(i, product);
                return product;
            }
        }

        return null;
    }

    @Override
    public boolean deleteProduct(String productId) {
        return productData.removeIf(p -> p.getProductId() != null && p.getProductId().equals(productId));
    }
    

}
