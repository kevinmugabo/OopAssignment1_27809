package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;
import java.time.LocalDate;

public class Student extends AcademicMember {
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String personId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth, String memberNumber,
                   Department department, LocalDate joinDate, String academicStatus,
                   String institutionalEmail, String advisorName, String studentNumber,
                   String programName, LocalDate admissionDate, int studyYear,
                   String guardianPhone, boolean scholarshipStatus) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber,
                department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.studentNumber = studentNumber;
        this.programName = programName;
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone;
        this.scholarshipStatus = scholarshipStatus;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public boolean isScholarshipStatus() {
        return scholarshipStatus;
    }

    public void setScholarshipStatus(boolean scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
    }

    @Override
    public String getRoleDescription() {
        return "Student of " + programName + ", year " + studyYear;
    }

    @Override
    public String getAcademicCategory() {
        return "STUDENT";
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(studentNumber, "Student number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(programName, "Program name", 2, 120);
        Registrable.requireNotFutureDate(admissionDate, "Admission date");
        if (studyYear < 1 || studyYear > 6) {
            throw new ValidationException("Study year must be between 1 and 6.");
        }
        Registrable.requirePhone(guardianPhone, "Guardian phone");
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Student number      : " + studentNumber);
        System.out.println("Program name        : " + programName);
        System.out.println("Admission date      : " + admissionDate);
        System.out.println("Study year          : " + studyYear);
        System.out.println("Guardian phone      : " + guardianPhone);
        System.out.println("Scholarship         : " + (scholarshipStatus ? "YES" : "NO"));
    }

    @Override
    public String toString() {
        return super.toString() + ", studentNumber=" + studentNumber + ", program=" + programName
                + ", studyYear=" + studyYear + ", scholarship=" + scholarshipStatus;
    }
}
