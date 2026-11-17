package com.auca.bookloan;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import java.time.LocalDate;

public class BookLoan implements Identifiable, Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public BookLoan(String loanId, Student student, LibraryBook book, LocalDate borrowedDate,
                    LocalDate dueDate, LocalDate returnedDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public Student getStudent() {
        return student;
    }

    public LibraryBook getBook() {
        return book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    @Override
    public String getId() {
        return loanId;
    }

    @Override
    public String getDisplayName() {
        return student.getDisplayName() + " borrowed " + book.getTitle();
    }

    @Override
    public boolean isActive() {
        return returnedDate == null;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(loanId, "Loan ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireNotNull(student, "Student");
        Registrable.requireNotNull(book, "Library book");
        Registrable.requireNotNull(borrowedDate, "Borrowed date");
        Registrable.requireNotNull(dueDate, "Due date");
        if (!dueDate.isAfter(borrowedDate)) {
            throw new ValidationException("Due date must be after the borrowed date.");
        }
        if (returnedDate != null && returnedDate.isBefore(borrowedDate)) {
            throw new ValidationException("Returned date must not be before the borrowed date.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Loan ID             : " + loanId);
        System.out.println("Student             : " + (student == null ? "(none)" : student.getRegistrationSummary()));
        System.out.println("Library book        : " + (book == null ? "(none)" : book.getRegistrationSummary()));
        System.out.println("Borrowed date       : " + borrowedDate);
        System.out.println("Due date            : " + dueDate);
        System.out.println("Returned date       : " + (returnedDate == null ? "(not returned yet)" : returnedDate.toString()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + loanId + "] " + student.getDisplayName() + " - " + book.getTitle() + " (due " + dueDate + ", "
                + (returnedDate == null ? "not returned" : "returned " + returnedDate) + ")";
    }

    @Override
    public String toString() {
        return "BookLoan{id=" + loanId + ", student=" + student.getDisplayName() + ", book=" + book.getTitle()
                + ", borrowed=" + borrowedDate + ", due=" + dueDate + ", returned=" + returnedDate + "}";
    }
}
