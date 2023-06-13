package com.example.glass.arshopping.models;

public class Cart {

    String seller_id;
    String product_name;
    String quantity;
    String date;
    String product_img;
    String id;
    String cart_id2;


    public Cart(String seller_id, String product_name, String quantity, String date, String cart_id, String product_img, String id) {
        this.seller_id = seller_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.date = date;
        this.cart_id2=cart_id;
        this.product_img=product_img;
        this.id=id;

    }

    public String getId() {  return id;}

    public String getSellerID() {
        return seller_id;
    }

    public String getProduct() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getCartId() {  return cart_id2;}

    public String getProduct_img() {
        return product_img;
    }
}
