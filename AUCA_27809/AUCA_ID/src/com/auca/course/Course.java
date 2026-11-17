package com.auca.course;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Course implements Identifiable, Registrable {
    private String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    public Course(String courseId, String courseCode, String courseTitle, int creditHours,
                  Department department, Teacher assignedTeacher) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.department = department;
        this.assignedTeacher = assignedTeacher;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    @Override
    public String getId() {
        return courseId;
    }

    @Override
    public String getDisplayName() {
        return courseCode + " " + courseTitle;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(courseId, "Course ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(courseCode, "Course code", 2, 10);
        Registrable.requireText(courseTitle, "Course title", 2, 120);
        if (creditHours < 1 || creditHours > 6) {
            throw new ValidationException("Credit hours must be between 1 and 6.");
        }
        Registrable.requireNotNull(department, "Department");
        Registrable.requireNotNull(assignedTeacher, "Assigned teacher");
    }

    @Override
    public void printDetails() {
        System.out.println("Course ID           : " + courseId);
        System.out.println("Course code         : " + courseCode);
        System.out.println("Course title        : " + courseTitle);
        System.out.println("Credit hours        : " + creditHours);
        System.out.println("Department          : " + (department == null ? "(none)" : department.getRegistrationSummary()));
        System.out.println("Assigned teacher    : " + (assignedTeacher == null ? "(none)" : assignedTeacher.getRegistrationSummary()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + courseId + "] " + courseCode + " - " + courseTitle + " (" + creditHours + " credits)";
    }

    @Override
    public String toString() {
        return "Course{id=" + courseId + ", code=" + courseCode + ", title=" + courseTitle + ", credits="
                + creditHours + ", department=" + (department == null ? "none" : department.getDepartmentName())
                + ", teacher=" + (assignedTeacher == null ? "none" : assignedTeacher.getDisplayName()) + "}";
    }
}
