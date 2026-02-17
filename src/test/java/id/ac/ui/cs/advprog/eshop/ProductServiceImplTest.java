package id.ac.ui.cs.advprog.eshop;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import id.ac.ui.cs.advprog.eshop.service.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void testCreate() {
        Product p = new Product();
        p.setProductId("id-1");
        when(productRepository.create(p)).thenReturn(p);

        Product result = productService.create(p);

        assertEquals(p, result);
        verify(productRepository).create(p);
    }

    @Test
    void testFindAll() {
        Product p1 = new Product();
        Product p2 = new Product();
        Iterator<Product> it = Arrays.asList(p1, p2).iterator();
        when(productRepository.findAll()).thenReturn(it);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void testFindProductById() {
        Product p = new Product();
        when(productRepository.findProductById("id-1")).thenReturn(p);

        Product result = productService.findProductById("id-1");

        assertEquals(p, result);
        verify(productRepository).findProductById("id-1");
    }

    @Test
    void testUpdateProduct() {
        Product p = new Product();
        when(productRepository.updateProduct(p)).thenReturn(p);

        Product result = productService.updateProduct(p);

        assertEquals(p, result);
        verify(productRepository).updateProduct(p);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.deleteProduct("id-1")).thenReturn(true);

        boolean result = productService.deleteProduct("id-1");

        assertTrue(result);
        verify(productRepository).deleteProduct("id-1");
    }
}