package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import id.ac.ui.cs.advprog.eshop.model.Product;


public interface ProductRepository {
    Product create(Product product);

    Iterator<Product> findAll();

    Product findProductById(String productId);

    Product updateProduct(Product product);

    boolean deleteProduct(String productId);
}
