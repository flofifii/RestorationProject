package model;

public class Category {

    private String name;
    private String description; // optionnel

    public Category(String name) {
        this.name = name;
        //this.description = description; //optionnel
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

