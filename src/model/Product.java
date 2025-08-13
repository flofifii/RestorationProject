package model;

import java.time.LocalDate;

public class Product {

    private String name;
    private Supplier supplier;
    private Category category;
    private Integer minQuantityDesired;
    private Integer stockQuantity;
    private Integer reorderQuantity;
    private LocalDate dateOfSale;
    private Boolean isFrozen;
    private String description; // optionnel


    public Product(String name, Supplier supplier, Category category, Integer minQuantityDesired,
                   Integer stockQuantity, Integer reorderQuantity, LocalDate dateOfSale,
                   Boolean isFrozen, String description) {
        this.name = name;
        this.supplier = supplier;
        this.category = category;
        this.minQuantityDesired = minQuantityDesired;
        this.stockQuantity = stockQuantity;
        this.reorderQuantity = reorderQuantity;
        this.dateOfSale = dateOfSale;
        this.isFrozen = isFrozen;
        this.description = description;
    }

    //Si pas de description

    public Product(String name, Supplier supplier, Category category,
                   int minQuantityDesired, int stockQuantity, int reorderQuantity,
                   LocalDate dateOfSale, boolean isFrozen) {
        this(name, supplier, category, minQuantityDesired, stockQuantity, reorderQuantity,
                dateOfSale, isFrozen, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getMinQuantityDesired() {
        return minQuantityDesired;
    }

    public void setMinQuantityDesired(Integer minQuantityDesired) {
        this.minQuantityDesired = minQuantityDesired;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public LocalDate getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDate dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public Boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(Boolean frozen) {
        isFrozen = frozen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

