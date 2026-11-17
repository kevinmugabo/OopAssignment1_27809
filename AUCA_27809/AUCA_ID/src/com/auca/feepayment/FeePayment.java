package com.auca.feepayment;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FeePayment implements Identifiable, Registrable {
    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    public FeePayment(String paymentId, Student student, BigDecimal amount, LocalDate paymentDate,
                      String paymentMethod, String receiptNumber) {
        this.paymentId = paymentId;
        this.student = student;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.receiptNumber = receiptNumber;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Student getStudent() {
        return student;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public String getDisplayName() {
        return "Payment " + receiptNumber;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(paymentId, "Payment ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireNotNull(student, "Student");
        Registrable.requireNotNull(amount, "Amount");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero.");
        }
        Registrable.requireNotFutureDate(paymentDate, "Payment date");
        Registrable.requireChoice(paymentMethod, "Payment method",
                new String[]{"CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD"});
        Registrable.requireText(receiptNumber, "Receipt number", 1, 30);
    }

    @Override
    public void printDetails() {
        System.out.println("Payment ID          : " + paymentId);
        System.out.println("Student             : " + (student == null ? "(none)" : student.getRegistrationSummary()));
        System.out.println("Amount              : " + amount);
        System.out.println("Payment date        : " + paymentDate);
        System.out.println("Payment method      : " + paymentMethod);
        System.out.println("Receipt number      : " + receiptNumber);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + paymentId + "] " + student.getDisplayName() + " paid " + amount + " by "
                + paymentMethod + " on " + paymentDate + " (receipt " + receiptNumber + ")";
    }

    @Override
    public String toString() {
        return "FeePayment{id=" + paymentId + ", student=" + student.getDisplayName() + ", amount=" + amount
                + ", date=" + paymentDate + ", method=" + paymentMethod + ", receipt=" + receiptNumber + "}";
    }
}
