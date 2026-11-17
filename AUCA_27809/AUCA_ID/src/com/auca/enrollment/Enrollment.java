package com.auca.enrollment;

import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import java.time.LocalDate;

public class Enrollment implements Identifiable, Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course, LocalDate enrollmentDate,
                      String semester, String enrollmentStatus) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.semester = semester;
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    @Override
    public String getId() {
        return enrollmentId;
    }

    @Override
    public String getDisplayName() {
        return student.getDisplayName() + " in " + course.getCourseCode();
    }

    @Override
    public boolean isActive() {
        return DEFAULT_STATUS.equals(enrollmentStatus);
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(enrollmentId, "Enrollment ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireNotNull(student, "Student");
        Registrable.requireNotNull(course, "Course");
        Registrable.requireNotFutureDate(enrollmentDate, "Enrollment date");
        Registrable.requireChoice(semester, "Semester", new String[]{"FALL", "SPRING", "SUMMER"});
        Registrable.requireChoice(enrollmentStatus, "Enrollment status", new String[]{"ACTIVE", "COMPLETED", "DROPPED"});
    }

    @Override
    public void printDetails() {
        System.out.println("Enrollment ID       : " + enrollmentId);
        System.out.println("Student             : " + (student == null ? "(none)" : student.getRegistrationSummary()));
        System.out.println("Course              : " + (course == null ? "(none)" : course.getRegistrationSummary()));
        System.out.println("Enrollment date     : " + enrollmentDate);
        System.out.println("Semester            : " + semester);
        System.out.println("Enrollment status   : " + enrollmentStatus);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + enrollmentId + "] " + getDisplayName() + " (" + semester + ", " + enrollmentStatus + ")";
    }

    @Override
    public String toString() {
        return "Enrollment{id=" + enrollmentId + ", student=" + student.getDisplayName() + ", course="
                + course.getCourseCode() + ", date=" + enrollmentDate + ", semester=" + semester
                + ", status=" + enrollmentStatus + "}";
    }
}
