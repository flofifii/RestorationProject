package model;

public class OrderDetail {
    private Order order;   // Référence vers Order (clé étrangère)
    private Item item;        // Référence vers Item
    private Integer quantity;

    // Constructeur
    public OrderDetail(Order order, Item item, Integer quantity) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;

    }

    // Getters
    public Order getOrder() {
        return order;
    }

    public Item item() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }


    // Setters
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setitem(Item item) {
        this.item = item;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

