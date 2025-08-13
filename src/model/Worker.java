package model;

import java.time.LocalDate;

public class Worker extends Person {
    private String function;
    private LocalDate dateHire;
    private LocalDate endDate; // Champ optionnel

    // Constructeur
    public Worker(int personID, String firstName, String lastName,
                  String function, LocalDate dateHire) {
        super(personID, firstName, lastName);
        this.function = function;
        this.dateHire = dateHire;
    }

    // Getters
    public String getFunction() {
        return function;
    }

    public LocalDate getDateHire() {
        return dateHire;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Setters
    public void setFunction(String function) {
        this.function = function;
    }

    public void setDateHire(LocalDate dateHire) {
        this.dateHire = dateHire;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}