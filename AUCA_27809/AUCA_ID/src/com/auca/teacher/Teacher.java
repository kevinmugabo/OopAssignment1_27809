package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    public Teacher(String personId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth, String memberNumber,
                   Department department, LocalDate joinDate, String academicStatus,
                   String institutionalEmail, String advisorName, String employeeNumber,
                   String specialization, LocalDate hireDate, BigDecimal salary,
                   String officeNumber, String employmentType) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber,
                department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber;
        this.employmentType = employmentType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    @Override
    public String getRoleDescription() {
        return "Teacher specialising in " + specialization;
    }

    @Override
    public String getAcademicCategory() {
        return "FACULTY";
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(employeeNumber, "Employee number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(specialization, "Specialization", 2, 120);
        Registrable.requireNotFutureDate(hireDate, "Hire date");
        Registrable.requireNotNull(salary, "Salary");
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Salary must be greater than zero.");
        }
        Registrable.requireText(officeNumber, "Office number", 1, 20);
        Registrable.requireChoice(employmentType, "Employment type", new String[]{"FULL_TIME", "PART_TIME", "CONTRACT"});
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Employee number     : " + employeeNumber);
        System.out.println("Specialization      : " + specialization);
        System.out.println("Hire date           : " + hireDate);
        System.out.println("Salary              : " + salary);
        System.out.println("Office number       : " + officeNumber);
        System.out.println("Employment type     : " + employmentType);
    }

    @Override
    public String toString() {
        return super.toString() + ", employeeNumber=" + employeeNumber + ", specialization=" + specialization
                + ", employmentType=" + employmentType;
    }
}
