package com.pwr.faktury.models.adapters;

import com.pwr.faktury.model.Product;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class ProductAdapter{
    Product product;

    public ProductAdapter(Product product) {
        this.product = product;
    }

    public Product get() {
        return this.product;
    }
}
