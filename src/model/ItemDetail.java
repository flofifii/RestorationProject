package model;

public class ItemDetail {

    private Item item;        // référence vers l'Item (à la carte)
    private Product product;  // référence vers le Product (stock)
    private Integer quantity;     // quantité de produit utilisée pour l'item

    public ItemDetail(Item item, Product product, Integer quantity) {
        this.item = item;
        this.product = product;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
