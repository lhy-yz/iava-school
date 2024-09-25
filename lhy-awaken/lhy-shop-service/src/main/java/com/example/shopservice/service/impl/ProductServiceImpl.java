package com.example.shopservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lhycommonservice.eneity.Product;
import com.example.shopservice.mapper.ProductMapper;
import com.example.shopservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @date 2024/9/6
 * @apiNote
 **/
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


}
