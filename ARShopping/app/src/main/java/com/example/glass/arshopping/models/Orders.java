package com.example.glass.arshopping.models;

import android.content.Context;

import java.util.List;

public class Orders {
    private String credit_card_id;
    private int cart_id;
    private int order_id;
    private String product_img;
    private String product_name;
    private String seller_name;
    private String quantity;
    private String order_date;
    private double product_price;
    private String user_id;
    private double total_amount;
    private String adress;
private  String card_number;
    private int product_id;
    public Orders createOrder(double totalAmount, int orderId, int cartId, String address, String creditCardId, String userId, String productImg, String productName, String sellerName, String quantity, String orderDate, double productPrice,int product_id) {
        this.order_id = orderId;
        this.cart_id = cartId;
        this.adress = address;
        this.credit_card_id = creditCardId;
        this.user_id = userId;
        this.product_img = productImg;
        this.product_name = productName;
        this.seller_name = sellerName;
        this.quantity = quantity;
        this.order_date = orderDate;
        this.product_price = productPrice;
        this.total_amount = totalAmount;
        this.product_id=product_id;
        return this;
    }

    public Orders createOrderWithMinimalData(double totalAmount,int orderId, String orderDate) {
        this.total_amount = totalAmount;
        this.order_id = orderId;
        this.order_date = orderDate;
        return this;
    }

    public Orders createOrderWithCardDetails(double totalAmount, String cardNumber, String address, String userId, String orderDate, int orderId) {
        this.total_amount = totalAmount;
        this.card_number = cardNumber;
        this.adress = address;
        this.user_id = userId;
        this.order_date = orderDate;
        this.order_id = orderId;
        return this;
    }


    public String getUserId() {
        return user_id;
    }
    public String getCard_number() {
        return card_number;
    }

    public String getProductImg() {
        return product_img;
    }

    public String getProductName() {
        return product_name;
    }

    public String getSellerName() {
        return seller_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getAdress() {
        return adress;
    }

    public String getOrderDate() {
        return order_date;
    }

    public String getCreditCardId() {
        return credit_card_id;
    }

    public int getCartId() {
        return cart_id;
    }

    public int getProductID() {
        return product_id;
    }

    public double getProductPrice() {
        return product_price;
    }

    public double getTotalAmount() {
        return total_amount;
    }

    public int getOrderId() {
        return order_id;
    }}
