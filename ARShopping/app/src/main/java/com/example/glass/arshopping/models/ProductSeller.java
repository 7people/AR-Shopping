package com.example.glass.arshopping.models;

public class ProductSeller {
    int product_id;
    Seller seller;
    double price;

    public ProductSeller(int product_id, Seller seller, double price) {
        this.product_id = product_id;
        this.seller = seller;
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getPrice() {
        return price;
    }
}
