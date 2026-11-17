package com.auca.administrator;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import java.time.LocalDate;

public class Administrator extends Person {
    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    public Administrator(String personId, String firstName, String lastName, String email,
                         String phoneNumber, LocalDate dateOfBirth, String staffNumber,
                         String jobTitle, String assignedOffice, LocalDate employmentDate,
                         int accessLevel, boolean active) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.staffNumber = staffNumber;
        this.jobTitle = jobTitle;
        this.assignedOffice = assignedOffice;
        this.employmentDate = employmentDate;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getRoleDescription() {
        return "Administrator - " + jobTitle;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(staffNumber, "Staff number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(jobTitle, "Job title", 2, 60);
        Registrable.requireText(assignedOffice, "Assigned office", 1, 30);
        Registrable.requireNotFutureDate(employmentDate, "Employment date");
        if (accessLevel < 1 || accessLevel > 5) {
            throw new ValidationException("Access level must be between 1 and 5.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Staff number        : " + staffNumber);
        System.out.println("Job title           : " + jobTitle);
        System.out.println("Assigned office     : " + assignedOffice);
        System.out.println("Employment date     : " + employmentDate);
        System.out.println("Access level        : " + accessLevel);
        System.out.println("Active              : " + (active ? "YES" : "NO"));
    }

    @Override
    public String toString() {
        return super.toString() + ", staffNumber=" + staffNumber + ", jobTitle=" + jobTitle
                + ", accessLevel=" + accessLevel + ", active=" + active;
    }
}
