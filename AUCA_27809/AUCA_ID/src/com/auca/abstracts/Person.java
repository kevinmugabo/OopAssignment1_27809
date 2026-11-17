package com.auca.abstracts;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import java.time.LocalDate;

public abstract class Person implements Identifiable, Registrable {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public Person(String personId, String firstName, String lastName, String email,
                  String phoneNumber, LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public abstract String getRoleDescription();

    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(personId, "Person ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(firstName, "First name", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        Registrable.requireText(lastName, "Last name", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        Registrable.requireEmail(email, "Email");
        Registrable.requirePhone(phoneNumber, "Phone");
        Registrable.requirePastDate(dateOfBirth, "Date of birth");
    }

    @Override
    public void printDetails() {
        System.out.println("Person ID           : " + personId);
        System.out.println("First name          : " + firstName);
        System.out.println("Last name           : " + lastName);
        System.out.println("Email               : " + email);
        System.out.println("Phone               : " + phoneNumber);
        System.out.println("Date of birth       : " + dateOfBirth);
        System.out.println("Role                : " + getRoleDescription());
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + personId + "] " + getDisplayName() + " - " + getRoleDescription();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{personId=" + personId + ", name=" + getDisplayName()
                + ", email=" + email + ", phone=" + phoneNumber + ", dateOfBirth=" + dateOfBirth + "}";
    }
}
