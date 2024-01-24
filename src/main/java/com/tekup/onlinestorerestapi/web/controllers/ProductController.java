package com.tekup.onlinestorerestapi.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tekup.onlinestorerestapi.web.models.Product;
import com.tekup.onlinestorerestapi.web.models.requests.ProductForm;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static List<Product> products = new ArrayList<Product>();
    private static Long idCount = 0L;
    static {
        products.add(new Product(++idCount, "SS-S9", "Samsung Galaxy S9", 500D, 50, "samsung-s9.png"));
        products.add(new Product(++idCount, "NK-5P", "Nokia 5.1 Plus", 60D, 60, null));
        products.add(new Product(++idCount, "IP-7", "iPhone 7", 600D, 30, "iphone-7.png"));
    }

    @GetMapping()
    public ResponseEntity<Object> getAllProduct() {
        if (this.products.isEmpty())
            return new ResponseEntity<>("list product is empty", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(this.products, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") Long id) {
        /*
         * Product productFound=this.findProductById(id);
         * 
         * if(productFound!=null){
         * return new ResponseEntity<>(productFound,HttpStatus.OK);
         * }
         * return new ResponseEntity<>("product not found", HttpStatus.NOT_FOUND);
         */
        try {
            Product productFound = this.findProductById(id);
            return new ResponseEntity<>(productFound, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Object> getProductByCode(@PathVariable("code") String code) {
        try {
            Product productFound = this.findProductByCode(code);
            return new ResponseEntity<>(productFound, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<Object> getProductSortedByPriceAsc() {

        if (this.products.isEmpty())
            return new ResponseEntity<>("list product is empty", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(this.sortProductByPrice(), HttpStatus.OK);
    }

    @GetMapping("/sortedDesc")
    public ResponseEntity<Object> getProductSortedByPriceDesc() {

        if (this.products.isEmpty())
            return new ResponseEntity<>("list product is empty", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(this.sortProductByPriceDesc(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable("id") Long id) {
        Product productFound = null;
        try {
            productFound = this.findProductById(id);
            this.products.remove(productFound);
            return new ResponseEntity("Product is deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Object> addProduct(@RequestBody() ProductForm productForm) {
        this.products.add(new Product(++idCount, productForm.getCode(), productForm.getName(), productForm.getPrice(),
                productForm.getQuantity(), productForm.getImage()));
        return new ResponseEntity<>("Product is created !", HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateProduct(@RequestBody ProductForm productForm, @PathVariable("id") Long id) {
         Product productFound=  null;
        try {
              productFound=this.findProductById(id);
              productFound.setCode(productForm.getCode());
              productFound.setName(productForm.getName());
              productFound.setPrice(productForm.getPrice());
              productFound.setQuantity(productForm.getQuantity());
              productFound.setImage(productForm.getImage());
              return new ResponseEntity<>(productFound,HttpStatus.OK);
        } catch (Exception e) {
              return new ResponseEntity("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    private Product findProductById(Long id) {
        /*
         * for(Product product: products){
         * if(product.getId()==id) return product;
         * }
         * return null;
         */
        return this.products.stream().filter(p -> p.getId() == id).findFirst().get();
    }

    private Product findProductByCode(String code) {
        return this.products.stream().filter(p -> p.getCode().equals(code)).findFirst().get();
    }

    private ArrayList<Product> sortProductByPrice() {
        ArrayList<Product> sortedListProduct = new ArrayList<>(this.products);
        Collections.sort(sortedListProduct, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        return sortedListProduct;
    }

    private ArrayList<Product> sortProductByPriceDesc() {
        ArrayList<Product> sortedListProduct = new ArrayList<>(this.products);
        Collections.sort(sortedListProduct, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        return sortedListProduct;
    }
}