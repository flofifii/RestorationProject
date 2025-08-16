package model;

public class Person {
    private int personID;
    private String firstName;
    private String lastName;
    private String mail;
    private String address;
    private String phoneNumber;

    //  Constructeur avec les champs obligatoires seulement
    public Person(int personID, String firstName, String lastName) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //pour le DAO
    public Person(int personID){
        this.personID = personID;
    }

    //  Getters
    public int getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    //  Setters
    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Optionnel : setters pour les champs obligatoires (si besoin de les modifier)
    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}