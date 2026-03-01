package id.ac.ui.cs.advprog.eshop;

import id.ac.ui.cs.advprog.eshop.controller.ProductController;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProductPost() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Item")
                        .param("productQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:list"));

        verify(productService).create(any(Product.class));
    }

    @Test
    void testProductListPage() throws Exception {
        when(productService.findAll()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attributeExists("products"));

        verify(productService).findAll();
    }

    @Test
    void testEditProductPage_WhenFound() throws Exception {
        Product p = new Product();
        when(productService.findProductById("id-1")).thenReturn(p);

        mockMvc.perform(get("/product/edit/id-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testEditProductPage_WhenNotFound() throws Exception {
        when(productService.findProductById("missing")).thenReturn(null);

        mockMvc.perform(get("/product/edit/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product/list"));
    }

    @Test
    void testEditProductPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "id-1")
                        .param("productName", "Updated")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product/list"));

        verify(productService).updateProduct(any(Product.class));
    }

    @Test
    void testDeleteProductPost() throws Exception {
        mockMvc.perform(post("/product/delete/id-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product/list"));

        verify(productService).deleteProduct("id-1");
    }
}