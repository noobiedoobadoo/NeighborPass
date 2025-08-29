package com.pranay.expense.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private record Product(Integer productId, String productName, double price) {
    }

    List<Product> productList =  new ArrayList<>(
            List.of(
            new Product(1, "iphone",999.0),
            new Product(2, "Mac Pro",2099.0)
        )
    );

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Product> getProducts(Principal principal) {

        String name = principal.getName();
        System.out.println(name);
        return productList;
    }


    // won't work with basic auth
    // because of csrf - cross site request forgery
    //pass the csrf token with header then it'll work
    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        productList.add(product);
        return product;
    }
}
