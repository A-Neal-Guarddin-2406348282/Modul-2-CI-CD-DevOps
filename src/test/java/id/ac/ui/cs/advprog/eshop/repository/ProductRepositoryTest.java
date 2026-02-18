package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.model.Product;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    // Mockito tidak perlu dipakai. Di commit sebelumnnya sudah hilang juga Mockitonya. Awalnya masih ada
    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreate_AutoGenerateId_WhenIdNullOrBlank() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Auto ID");
        product.setProductQuantity(1);

        Product saved = productRepository.create(product);

        assertNotNull(saved.getProductId());
        assertFalse(saved.getProductId().isBlank());
    }

    @Test
    void testCreate_AutoGenerateId_WhenIdBlank() {
        Product product = new Product();
        product.setProductId("   ");
        product.setProductName("Auto ID Blank");
        product.setProductQuantity(1);

        Product saved = productRepository.create(product);

        assertNotNull(saved.getProductId());
        assertFalse(saved.getProductId().isBlank());
    }

    @Test
    void testFindProductById_WhenNotFound() {
        Product result = productRepository.findProductById("missing");
        assertNull(result);
    }

    @Test
    void testFindProductById_SkipsProductsWithNullId_BranchCovered() {
        Product product = new Product();
        product.setProductId("id-keep");
        product.setProductName("Will be nulled");
        product.setProductQuantity(1);
        productRepository.create(product);

        // Mutate object after inserted (repo menyimpan reference yg sama)
        product.setProductId(null);

        Product result = productRepository.findProductById("anything");
        assertNull(result);
    }

    @Test
    void testFindProductById_WhenFound() {
        Product product = new Product();
        product.setProductId("id-found");
        product.setProductName("Found");
        product.setProductQuantity(7);
        productRepository.create(product);

        Product result = productRepository.findProductById("id-found");

        assertNotNull(result);
        assertSame(product, result);
        assertEquals("Found", result.getProductName());
        assertEquals(7, result.getProductQuantity());
    }

    @Test
    void testUpdateProduct_Success() {
        Product original = new Product();
        original.setProductId("id-1");
        original.setProductName("Old");
        original.setProductQuantity(1);
        productRepository.create(original);

        Product updated = new Product();
        updated.setProductId("id-1");
        updated.setProductName("New");
        updated.setProductQuantity(10);

        Product result = productRepository.updateProduct(updated);

        assertNotNull(result);
        assertEquals("New", result.getProductName());
        assertEquals(10, result.getProductQuantity());
    }

    @Test
    void testUpdateProduct_Fail_WhenIdNull() {
        Product updated = new Product();
        updated.setProductId(null);
        updated.setProductName("X");
        updated.setProductQuantity(1);

        Product result = productRepository.updateProduct(updated);

        assertNull(result);
    }

    @Test
    void testUpdateProduct_Fail_WhenIdBlank() {
        Product updated = new Product();
        updated.setProductId("  ");
        updated.setProductName("X");
        updated.setProductQuantity(1);

        Product result = productRepository.updateProduct(updated);

        assertNull(result);
    }

    @Test
    void testUpdateProduct_Fail_WhenNotFound() {
        Product updated = new Product();
        updated.setProductId("missing");
        updated.setProductName("X");
        updated.setProductQuantity(1);

        Product result = productRepository.updateProduct(updated);

        assertNull(result);
    }

    @Test
    void testUpdateProduct_Fail_WhenNotMatchingButRepositoryHasOtherItems() {
        Product existing = new Product();
        existing.setProductId("id-a");
        existing.setProductName("A");
        existing.setProductQuantity(1);
        productRepository.create(existing);

        Product updateAttempt = new Product();
        updateAttempt.setProductId("id-b"); // beda id -> loop jalan tapi tidak match
        updateAttempt.setProductName("B");
        updateAttempt.setProductQuantity(2);

        Product result = productRepository.updateProduct(updateAttempt);
        assertNull(result);
    }

    @Test
    void testDeleteProduct_Success() {
        Product p = new Product();
        p.setProductId("id-del");
        p.setProductName("Del");
        p.setProductQuantity(1);
        productRepository.create(p);

        boolean deleted = productRepository.deleteProduct("id-del");

        assertTrue(deleted);
        assertNull(productRepository.findProductById("id-del"));
    }

    @Test
    void testDeleteProduct_Fail_WhenNotFound() {
        assertFalse(productRepository.deleteProduct("missing"));
    }

    @Test
    void testDeleteProduct_Fail_WhenIdNull() {
        assertFalse(productRepository.deleteProduct(null));
    }

    @Test
    void testDeleteProduct_SkipsProductsWithNullId_BranchCovered() {
        Product product = new Product();
        product.setProductId("id-keep");
        product.setProductName("Will be nulled");
        product.setProductQuantity(1);
        productRepository.create(product);

        // Bikin id jadi null supaya predicate `p.getProductId() != null` false
        product.setProductId(null);

        boolean deleted = productRepository.deleteProduct("id-keep");
        assertFalse(deleted);
    }

    @Test
    void testDeleteProduct_Fail_WhenIdDifferent_BranchCovered() {
        Product product = new Product();
        product.setProductId("id-1");
        product.setProductName("Keep");
        product.setProductQuantity(1);
        productRepository.create(product);

        boolean deleted = productRepository.deleteProduct("id-2");

        assertFalse(deleted);
        assertNotNull(productRepository.findProductById("id-1"));
    }

    @Test
    void testFindProductById_WhenRepositoryHasOtherNonMatchingId_BranchCovered() {
        Product existing = new Product();
        existing.setProductId("id-existing");
        existing.setProductName("Existing");
        existing.setProductQuantity(1);
        productRepository.create(existing);

        Product result = productRepository.findProductById("id-missing");

        assertNull(result);
    }
}
