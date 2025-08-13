package model;

public class RestockDetail {

    private Restock restock;
    private Product product;
    private Integer quantity;


    public RestockDetail(Restock restock, Product product, Integer quantity) {
        this.restock = restock;
        this.product = product;
        this.quantity = quantity;
    }

    public Restock getRestock() {
        return restock;
    }

    public void setRestock(Restock restock) {
        this.restock = restock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
