package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.exceptions.ProductNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/api/products")
    public List<Product> listAllProducts() {
        List<Product> products = productService.findAll();
        return products;
    }

    @GetMapping("/api/products/{id}")
    public Product getProduct(@PathVariable("id") long id) {
        return productService.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @PutMapping("/api/products")
    public ResponseEntity<String> saveProduct(@Valid  @RequestBody ProductDto productDto) {
        var savedProduct = productService.update(
                productDto.getId(),
                productDto.getName());
        return ResponseEntity.ok("savedProduct");
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> insertProduct(@Valid @RequestBody ProductDto productDto) {
        var savedProduct = productService.insert(productDto.getName());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedProduct);
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
