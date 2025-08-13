package model;

import java.time.LocalDate;

public class Order {
    private int number;                  // Identifiant de la commande
    private Customer customer;               // Référence au client
    private String paymentMethod;
    private Boolean isTakeaway;
    private LocalDate creationDate;
    private LocalDate deliveryDate;     // Optionnel
    private String commentary;          // Optionnel
    private String specificRequest;     // Optionnel

    // Constructeur
    public Order(int number, Customer customer, String paymentMethod, Boolean isTakeaway, LocalDate creationDate) {
        this.number = number;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.isTakeaway = isTakeaway;
        this.creationDate = creationDate;
    }

    public int getNumber() {
        return number;
    }

    public Customer getcustomer() {
        return customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Boolean isTakeaway() {
        return isTakeaway;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getCommentary() {
        return commentary;
    }

    public String getSpecificRequest() {
        return specificRequest;
    }

    // Setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setcustomer(Customer customer) {
        this.customer = customer;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTakeaway(Boolean takeaway) {
        isTakeaway = takeaway;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public void setSpecificRequest(String specificRequest) {
        this.specificRequest = specificRequest;
    }
}
