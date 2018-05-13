package com.example.marcos.unasp_phpmysql.Model;

/**
 * Created by marcos on 19/03/2018.
 */

public class Product {

    private String product_name, product_origin, product_status, comprador;
    private int product_price, productId;

    public Product(int productId, String product_name, int product_price, String product_origin, String product_status) {
        this.productId = productId;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_origin = product_origin;
        this.product_status = product_status;
    }

    public Product(int id, String post, int preco, String origin, String status, String comprador) {
        this.productId = productId;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_origin = product_origin;
        this.product_status = product_status;
        this.comprador = comprador;
    }

    public String getComprador() {
        return comprador;
    }

    public int getProductId() {
        return productId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public String getProduct_origin() {
        return product_origin;
    }

    public String getProduct_status(){
        return product_status;
    }

}
