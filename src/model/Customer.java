package model;

import java.time.LocalDate;

public class Customer extends Person {
    private Integer rewardPoint;
    private LocalDate registrationDate;
    private Boolean hasDiscount;

    // Constructeur
    public Customer(int personID, String firstName, String lastName,
                    Integer rewardPoint, LocalDate registrationDate, Boolean hasDiscount) {
        super(personID, firstName, lastName);
        this.rewardPoint = rewardPoint;
        this.registrationDate = registrationDate;
        this.hasDiscount = hasDiscount;
    }

    // Getters
    public Integer getRewardPoint() {
        return rewardPoint;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public Boolean isHasDiscount() {
        return hasDiscount;
    }

    // Setters
    public void setRewardPoint(Integer rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }
}
