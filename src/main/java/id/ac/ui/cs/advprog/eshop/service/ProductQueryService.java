package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

// Untuk ambil data dari repo
public interface ProductQueryService {
    List<Product> findAll();
    Product findProductById(String productId);

}
