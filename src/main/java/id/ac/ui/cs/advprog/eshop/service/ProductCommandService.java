package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;


// Untuk perintah membuat, update, dan delete produk
public interface ProductCommandService {
    Product create(Product product);
    Product updateProduct(Product product);
    boolean deleteProduct(String productId);
}
