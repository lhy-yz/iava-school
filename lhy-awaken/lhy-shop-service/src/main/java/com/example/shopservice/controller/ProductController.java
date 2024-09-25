package com.example.shopservice.controller;

import com.example.lhycommonservice.eneity.Product;
import com.example.shopservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhy
 * @date 2024/9/6
 * @apiNote
 **/
@RestController
@RequiredArgsConstructor
public class ProductController {

    private  final ProductService productService;

    @RequestMapping("product/{id}")
   public Product getProductInfoById(@PathVariable Integer id){

       return productService.getById(id);

   }

}
