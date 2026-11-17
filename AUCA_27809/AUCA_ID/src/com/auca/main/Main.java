package com.auca.main;

import com.auca.abstracts.AcademicMember;
import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Object CANCEL = new Object();
    private static final Object BACK = new Object();
    private static final String[] ENTITIES = {"Department", "Student", "Teacher", "Administrator", "Course",
            "Classroom", "Enrollment", "Assessment", "FeePayment", "LibraryBook", "BookLoan"};

    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> payments = new ArrayList<>();
    private static final List<LibraryBook> books = new ArrayList<>();
    private static final List<BookLoan> loans = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice();
            if (choice == -1) {
                continue;
            }
            try {
                switch (choice) {
                    case 1:
                        registerFromMenu("Department");
                        break;
                    case 2:
                        registerFromMenu("Student");
                        break;
                    case 3:
                        registerFromMenu("Teacher");
                        break;
                    case 4:
                        registerFromMenu("Administrator");
                        break;
                    case 5:
                        registerFromMenu("Course");
                        break;
                    case 6:
                        registerFromMenu("Classroom");
                        break;
                    case 7:
                        registerFromMenu("Enrollment");
                        break;
                    case 8:
                        registerFromMenu("Assessment");
                        break;
                    case 9:
                        registerFromMenu("FeePayment");
                        break;
                    case 10:
                        registerFromMenu("LibraryBook");
                        break;
                    case 11:
                        registerFromMenu("BookLoan");
                        break;
                    case 12:
                        returnBook();
                        break;
                    case 13:
                        viewAllRecords();
                        break;
                    case 14:
                        searchById();
                        break;
                    case 15:
                        updateRecord();
                        break;
                    case 16:
                        studentAcademicSummary();
                        break;
                    case 17:
                        studentPaymentHistory();
                        break;
                    case 18:
                        showAvailableBooks();
                        break;
                    case 19:
                        demonstratePolymorphism();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using AUCA_ID. Goodbye.");
                        break;
                    default:
                        System.out.println("! Invalid option. Please choose a number from 0 to 19.");
                        break;
                }
            } catch (RecordNotFoundException e) {
                System.out.println("! Not found: " + e.getMessage());
            } catch (PrerequisiteMissingException e) {
                System.out.println("! Missing prerequisite: " + e.getMessage());
            } catch (DuplicateRecordException e) {
                System.out.println("! Duplicate: " + e.getMessage());
            } catch (BookUnavailableException e) {
                System.out.println("! Book unavailable: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("! Something went wrong: " + e.getMessage());
                System.out.println("  Returning to the main menu.");
            }
        }
    }

    private static void printMenu() {
        String[] left = {"Register Department", "Register Student", "Register Teacher", "Register Administrator",
                "Register Course", "Register Classroom", "Enroll Student", "Record Assessment",
                "Record Fee Payment", "Register Library Book"};
        String[] right = {"Borrow Library Book", "Return Library Book", "View All Records", "Search Record by ID",
                "Update Existing Record", "Student Academic Summary", "Student Payment History",
                "View Available Books", "Demonstrate Polymorphism", "Exit"};
        System.out.println();
        System.out.println("============== AUCA_ID SCHOOL MANAGEMENT ==============");
        for (int i = 0; i < 10; i++) {
            int rightNumber = i == 9 ? 0 : 11 + i;
            System.out.printf("%2d. %-26s %2d. %s%n", i + 1, left[i], rightNumber, right[i]);
        }
    }

    private static String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Input ended. Closing application.");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static int readMenuChoice() {
        System.out.print("Select an option: ");
        String input = readLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("! Please type a number from the menu (0-19).");
            return -1;
        }
    }

    private static String displayName(String entity) {
        return entity.replaceAll("([a-z])([A-Z])", "$1 $2");
    }

    private static void registerFromMenu(String entity) {
        boolean again = true;
        while (again) {
            Object saved = runForm(entity);
            if (saved == null) {
                return;
            }
            System.out.println("  [1] Register another   [2] View " + displayName(entity).toLowerCase() + "   [0] Main menu");
            boolean waiting = true;
            while (waiting) {
                System.out.print("Select option: ");
                String choice = readLine();
                if (choice.equals("1")) {
                    waiting = false;
                } else if (choice.equals("2")) {
                    ((Registrable) saved).printDetails();
                } else if (choice.equals("0")) {
                    waiting = false;
                    again = false;
                } else {
                    System.out.println("! Choose 1, 2 or 0.");
                }
            }
        }
    }

    private static Object runForm(String entity) {
        String[][] spec = specOf(entity);
        int[] bounds = boundsOf(entity);
        String[] titles = titlesOf(entity);
        Object[] vals = new Object[spec.length];
        try {
            int step = 0;
            while (step < 3) {
                int start = step == 0 ? 0 : bounds[step - 1];
                printHeader(entity, step, titles[step]);
                boolean goBack = false;
                for (int i = start; i < bounds[step]; i++) {
                    if (spec[i][2].equals("derived")) {
                        vals[i] = null;
                        continue;
                    }
                    Object result = askField(entity, spec, i, vals, null);
                    if (result == CANCEL) {
                        System.out.println("- " + displayName(entity) + " registration cancelled. Nothing was saved.");
                        return null;
                    }
                    if (result == BACK) {
                        goBack = true;
                        break;
                    }
                    vals[i] = result;
                }
                if (goBack) {
                    if (step > 0) {
                        step--;
                    } else {
                        System.out.println("! You are already at the first step.");
                    }
                } else {
                    step++;
                }
            }
            return reviewLoop(entity, spec, bounds, titles, vals);
        } catch (PrerequisiteMissingException e) {
            System.out.println("! " + e.getMessage());
            System.out.println("- " + displayName(entity) + " registration cancelled. Nothing was saved.");
            return null;
        }
    }

    private static void printHeader(String entity, int step, String title) {
        System.out.println();
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| AUCA_ID / SCHOOL MANAGEMENT");
        System.out.println("| " + displayName(entity).toUpperCase() + " REGISTRATION        Step " + (step + 1) + " of 4");
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| Enter the requested value. Type BACK to return to the");
        System.out.println("| previous step; CANCEL to discard the unsaved record.");
        System.out.println("+------------------------------------------------------------+");
        System.out.println("[STEP " + (step + 1) + "/4] " + title);
    }

    private static Object reviewLoop(String entity, String[][] spec, int[] bounds, String[] titles, Object[] vals) {
        while (true) {
            printReview(entity, spec, bounds, titles, vals);
            System.out.println("| [1] Save " + displayName(entity).toLowerCase() + "   [2] Edit field   [0] Cancel");
            System.out.print("Select option: ");
            String choice = readLine();
            if (choice.equals("1")) {
                try {
                    Object saved = buildAndSave(entity, spec, vals);
                    System.out.println("+ " + displayName(entity) + " " + ((Identifiable) saved).getId()
                            + " registered successfully.");
                    return saved;
                } catch (ValidationException | DuplicateRecordException | BookUnavailableException e) {
                    System.out.println("! Cannot save: " + e.getMessage());
                    System.out.println("  Use option 2 to correct the field.");
                }
            } else if (choice.equals("2")) {
                editInReview(entity, spec, vals);
            } else if (choice.equals("0") || choice.equalsIgnoreCase("CANCEL")) {
                System.out.println("- " + displayName(entity) + " registration cancelled. Nothing was saved.");
                return null;
            } else {
                System.out.println("! Choose 1, 2 or 0.");
            }
        }
    }

    private static void printReview(String entity, String[][] spec, int[] bounds, String[] titles, Object[] vals) {
        System.out.println();
        System.out.println("+---------------- REVIEW " + displayName(entity).toUpperCase() + " ----------------+");
        System.out.println("[STEP 4/4] REVIEW & CONFIRM");
        for (int step = 0; step < 3; step++) {
            int start = step == 0 ? 0 : bounds[step - 1];
            System.out.println("| " + titles[step]);
            for (int i = start; i < bounds[step]; i++) {
                System.out.printf("| %02d %-20s : %s%n", i + 1, spec[i][0], display(vals[i], spec[i][2]));
            }
        }
        System.out.println("+------------------------------------------------------------+");
    }

    private static String display(Object value, String kind) {
        if (value == null) {
            return kind.equals("derived") ? "(not returned yet)" : "(none)";
        }
        if (value instanceof Registrable) {
            return ((Registrable) value).getRegistrationSummary();
        }
        return String.valueOf(value);
    }

    private static void editInReview(String entity, String[][] spec, Object[] vals) {
        System.out.print("Field number to edit (01-" + String.format("%02d", spec.length) + "): ");
        int number;
        try {
            number = Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            System.out.println("! Please enter a valid field number.");
            return;
        }
        if (number < 1 || number > spec.length) {
            System.out.println("! Field number must be between 1 and " + spec.length + ".");
            return;
        }
        int idx = number - 1;
        if (spec[idx][2].equals("derived")) {
            System.out.println("! This field is set automatically and cannot be edited.");
            return;
        }
        System.out.println("Current value: " + display(vals[idx], spec[idx][2]));
        try {
            Object result = askField(entity, spec, idx, vals, null);
            if (result == CANCEL || result == BACK) {
                System.out.println("Edit cancelled. The old value was kept.");
            } else {
                vals[idx] = result;
            }
        } catch (PrerequisiteMissingException e) {
            System.out.println("! " + e.getMessage());
        }
    }

    private static Object buildAndSave(String entity, String[][] spec, Object[] vals) throws ValidationException {
        validateAll(entity, spec, vals, null);
        Object created = build(entity, vals);
        ((Registrable) created).validate();
        if (created instanceof Assessment) {
            if (!((Assessment) created).getEnrollment().isActive()) {
                throw new ValidationException("Only an active enrollment can be assessed.");
            }
        }
        if (created instanceof BookLoan) {
            LibraryBook book = ((BookLoan) created).getBook();
            if (book.getAvailableCopies() <= 0) {
                throw new BookUnavailableException("No copy of '" + book.getTitle() + "' is available.");
            }
        }
        store(created);
        return created;
    }

    private static void store(Object created) {
        if (created instanceof Department) {
            departments.add((Department) created);
        } else if (created instanceof Student) {
            students.add((Student) created);
        } else if (created instanceof Teacher) {
            teachers.add((Teacher) created);
        } else if (created instanceof Administrator) {
            administrators.add((Administrator) created);
        } else if (created instanceof Course) {
            courses.add((Course) created);
        } else if (created instanceof Classroom) {
            classrooms.add((Classroom) created);
        } else if (created instanceof Enrollment) {
            enrollments.add((Enrollment) created);
        } else if (created instanceof Assessment) {
            assessments.add((Assessment) created);
        } else if (created instanceof FeePayment) {
            payments.add((FeePayment) created);
        } else if (created instanceof LibraryBook) {
            books.add((LibraryBook) created);
        } else if (created instanceof BookLoan) {
            BookLoan loan = (BookLoan) created;
            loans.add(loan);
            loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() - 1);
        }
    }

    private static Object askField(String entity, String[][] spec, int idx, Object[] vals, Object self) {
        String[] field = spec[idx];
        String[] parts = field[2].split(":");
        if (parts[0].equals("ref")) {
            boolean optional = parts.length > 2;
            System.out.println(String.format("%02d %s [%s]", idx + 1, field[0], field[1]));
            while (true) {
                try {
                    return pickRef(parts[1], optional, true);
                } catch (BookUnavailableException e) {
                    System.out.println("! " + e.getMessage() + " Please choose another book.");
                }
            }
        }
        while (true) {
            System.out.print(String.format("%02d %-20s [%s] : ", idx + 1, field[0], hintOf(field)));
            String input = readLine();
            if (input.equalsIgnoreCase("CANCEL")) {
                return CANCEL;
            }
            if (input.equalsIgnoreCase("BACK")) {
                return BACK;
            }
            try {
                return checkField(entity, spec, idx, vals, self, input);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
                System.out.println("  Please re-enter field " + String.format("%02d", idx + 1) + ".");
            } catch (DuplicateRecordException e) {
                System.out.println("! " + e.getMessage());
                System.out.println("  Please enter a different value for field " + String.format("%02d", idx + 1) + ".");
            }
        }
    }

    private static String hintOf(String[] field) {
        String[] parts = field[2].split(":");
        if (parts[0].equals("choice")) {
            String[] options = parts[1].split(",");
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                if (i > 0) {
                    text.append(" / ");
                }
                text.append(i + 1).append(" ").append(options[i]);
            }
            return text.toString();
        }
        if (parts[0].equals("yesno")) {
            return "1 YES / 2 NO";
        }
        return field[1];
    }

    private static String refLabel(String type) {
        switch (type) {
            case "department":
                return "Department";
            case "teacher":
                return "Teacher";
            case "student":
                return "Student";
            case "course":
                return "Course";
            case "enrollment":
                return "Active Enrollment";
            case "book":
                return "Library Book";
            default:
                return type;
        }
    }

    private static String entityOf(String type) {
        switch (type) {
            case "department":
                return "Department";
            case "teacher":
                return "Teacher";
            case "student":
                return "Student";
            case "course":
                return "Course";
            case "enrollment":
                return "Enrollment";
            default:
                return "LibraryBook";
        }
    }

    private static List<Registrable> options(String type) {
        List<Registrable> list = new ArrayList<>();
        switch (type) {
            case "department":
                list.addAll(departments);
                break;
            case "teacher":
                list.addAll(teachers);
                break;
            case "student":
                list.addAll(students);
                break;
            case "course":
                list.addAll(courses);
                break;
            case "enrollment":
                for (Enrollment e : enrollments) {
                    if (e.isActive()) {
                        list.add(e);
                    }
                }
                break;
            case "book":
                list.addAll(books);
                break;
            default:
                break;
        }
        return list;
    }

    private static Object pickRef(String type, boolean optional, boolean allowRegister) {
        String name = refLabel(type);
        while (true) {
            List<Registrable> items = options(type);
            if (items.isEmpty()) {
                if (optional) {
                    System.out.println("  No " + name + " is registered yet. This field stays empty and can be assigned later with Update.");
                    return null;
                }
                if (!allowRegister) {
                    System.out.println("No " + name + " is registered.");
                    return CANCEL;
                }
                System.out.println();
                System.out.println("No " + name + " is registered. Register a " + name.toLowerCase() + " before continuing.");
                System.out.println("  1 Register " + name + " now");
                System.out.println("  0 Cancel");
                System.out.print("Select: ");
                String choice = readLine();
                if (choice.equals("1")) {
                    runForm(entityOf(type));
                    System.out.println("Returning to the interrupted form...");
                } else if (choice.equalsIgnoreCase("BACK")) {
                    return BACK;
                } else if (choice.equals("0") || choice.equalsIgnoreCase("CANCEL")) {
                    throw new PrerequisiteMissingException(name + " is required but none was registered.");
                } else {
                    System.out.println("! Choose 1 or 0.");
                }
                continue;
            }
            int size = items.size();
            System.out.println("+----------------- CHOOSE " + name.toUpperCase() + " -----------------+");
            for (int i = 0; i < size; i++) {
                System.out.println("| " + (i + 1) + " " + items.get(i).getRegistrationSummary());
            }
            if (optional) {
                System.out.println("| " + (size + 1) + " Leave empty (assign later)");
            } else if (allowRegister) {
                System.out.println("| " + (size + 1) + " [+] Register a new " + name);
            }
            System.out.println("| 0 Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            String input = readLine();
            if (input.equalsIgnoreCase("BACK")) {
                return BACK;
            }
            if (input.equalsIgnoreCase("CANCEL") || input.equals("0")) {
                return CANCEL;
            }
            int number;
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number from the list.");
                continue;
            }
            if (number >= 1 && number <= size) {
                Registrable chosen = items.get(number - 1);
                if (type.equals("book") && ((LibraryBook) chosen).getAvailableCopies() <= 0) {
                    throw new BookUnavailableException("No copy of '" + ((LibraryBook) chosen).getTitle() + "' is available.");
                }
                return chosen;
            }
            if (number == size + 1 && optional) {
                return null;
            }
            if (number == size + 1 && allowRegister) {
                runForm(entityOf(type));
                System.out.println("Returning to the interrupted form...");
                continue;
            }
            System.out.println("! Choose a number between 0 and " + (optional || allowRegister ? size + 1 : size) + ".");
        }
    }

    private static String checkField(String entity, String[][] spec, int idx, Object[] vals, Object self,
                                     String input) throws ValidationException {
        String label = spec[idx][0];
        String[] p = spec[idx][2].split(":");
        String value = input == null ? "" : input.trim();
        switch (p[0]) {
            case "text": {
                Registrable.requireText(value, label, Integer.parseInt(p[1]), Integer.parseInt(p[2]));
                break;
            }
            case "email": {
                Registrable.requireEmail(value, label);
                break;
            }
            case "phone": {
                Registrable.requirePhone(value, label);
                break;
            }
            case "date": {
                LocalDate date = parseDate(value);
                if (p[1].equals("past") && !date.isBefore(LocalDate.now())) {
                    throw new ValidationException(label + " must be in the past.");
                }
                if (p[1].equals("notfuture") && date.isAfter(LocalDate.now())) {
                    throw new ValidationException(label + " must not be in the future.");
                }
                value = date.toString();
                break;
            }
            case "int": {
                int number = parseWhole(value, label);
                int min = Integer.parseInt(p[1]);
                int max = Integer.parseInt(p[2]);
                if (number < min || number > max) {
                    throw new ValidationException(label + " must be between " + min + " and " + max + ".");
                }
                value = String.valueOf(number);
                break;
            }
            case "money": {
                BigDecimal amount = parseDecimal(value, label);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ValidationException(label + " must be greater than zero.");
                }
                value = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
                break;
            }
            case "double": {
                double number = parseDecimal(value, label).doubleValue();
                if (p[1].equals("pos") && number <= 0) {
                    throw new ValidationException(label + " must be greater than zero.");
                }
                if (p[1].equals("nonneg") && number < 0) {
                    throw new ValidationException(label + " must not be negative.");
                }
                value = String.valueOf(number);
                break;
            }
            case "choice": {
                value = pickChoice(value, p[1].split(","), label);
                break;
            }
            case "yesno": {
                value = pickChoice(value, new String[]{"YES", "NO"}, label);
                break;
            }
            default:
                break;
        }
        if (!spec[idx][3].isEmpty()) {
            checkUnique(label, spec[idx][3], value, self);
        }
        extra(entity, idx, vals, value, self);
        return value;
    }

    private static LocalDate parseDate(String value) throws ValidationException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date: use YYYY-MM-DD (example: 2004-05-16).");
        }
    }

    private static int parseWhole(String value, String label) throws ValidationException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a whole number.");
        }
    }

    private static BigDecimal parseDecimal(String value, String label) throws ValidationException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a valid number (example: 1500.50).");
        }
    }

    private static String pickChoice(String value, String[] options, String label) throws ValidationException {
        for (String option : options) {
            if (option.equalsIgnoreCase(value)) {
                return option;
            }
        }
        if (value.matches("[0-9]{1,3}")) {
            int number = Integer.parseInt(value);
            if (number >= 1 && number <= options.length) {
                return options[number - 1];
            }
        }
        throw new ValidationException(label + " must be one of: " + String.join(", ", options) + " (or its number).");
    }

    private static void extra(String entity, int idx, Object[] vals, String value, Object self) throws ValidationException {
        if (entity.equals("Assessment") && idx == 4 && vals[3] != null) {
            double max = Double.parseDouble((String) vals[3]);
            if (Double.parseDouble(value) > max) {
                throw new ValidationException("Obtained score cannot exceed the maximum score (" + max + ").");
            }
        }
        if (entity.equals("LibraryBook") && idx == 5 && vals[4] != null) {
            if (Integer.parseInt(value) > Integer.parseInt((String) vals[4])) {
                throw new ValidationException("Available copies cannot exceed total copies (" + vals[4] + ").");
            }
        }
        if (entity.equals("BookLoan") && idx == 4 && vals[3] != null) {
            if (!LocalDate.parse(value).isAfter(LocalDate.parse((String) vals[3]))) {
                throw new ValidationException("Due date must be after the borrowed date (" + vals[3] + ").");
            }
        }
        if (entity.equals("Enrollment") && (idx == 4 || idx == 5)) {
            Object student = vals[1];
            Object course = vals[2];
            String semester = idx == 4 ? value : (String) vals[4];
            String status = idx == 5 ? value : (vals[5] == null ? "ACTIVE" : (String) vals[5]);
            if (student != null && course != null && semester != null && status.equals("ACTIVE")) {
                for (Enrollment e : enrollments) {
                    if (e != self && e.isActive() && e.getStudent() == student && e.getCourse() == course
                            && e.getSemester().equals(semester)) {
                        throw new DuplicateRecordException("This student already has an active enrollment in this course for "
                                + semester + ".");
                    }
                }
            }
        }
    }

    private static List<Person> allPeople() {
        List<Person> list = new ArrayList<>();
        list.addAll(students);
        list.addAll(teachers);
        list.addAll(administrators);
        return list;
    }

    private static void checkUnique(String label, String set, String value, Object self) {
        List<String> used = new ArrayList<>();
        switch (set) {
            case "personid":
                for (Person p : allPeople()) {
                    if (p != self) {
                        used.add(p.getPersonId());
                    }
                }
                break;
            case "email":
                for (Person p : allPeople()) {
                    if (p != self) {
                        used.add(p.getEmail());
                    }
                }
                break;
            case "member":
                for (Person p : allPeople()) {
                    if (p != self && p instanceof AcademicMember) {
                        used.add(((AcademicMember) p).getMemberNumber());
                    }
                }
                break;
            case "instemail":
                for (Person p : allPeople()) {
                    if (p != self && p instanceof AcademicMember) {
                        used.add(((AcademicMember) p).getInstitutionalEmail());
                    }
                }
                break;
            case "student":
                for (Student s : students) {
                    if (s != self) {
                        used.add(s.getStudentNumber());
                    }
                }
                break;
            case "employee":
                for (Teacher t : teachers) {
                    if (t != self) {
                        used.add(t.getEmployeeNumber());
                    }
                }
                break;
            case "staff":
                for (Administrator a : administrators) {
                    if (a != self) {
                        used.add(a.getStaffNumber());
                    }
                }
                break;
            case "department":
                for (Department d : departments) {
                    if (d != self) {
                        used.add(d.getDepartmentId());
                    }
                }
                break;
            case "deptcode":
                for (Department d : departments) {
                    if (d != self) {
                        used.add(d.getDepartmentCode());
                    }
                }
                break;
            case "course":
                for (Course c : courses) {
                    if (c != self) {
                        used.add(c.getCourseId());
                    }
                }
                break;
            case "coursecode":
                for (Course c : courses) {
                    if (c != self) {
                        used.add(c.getCourseCode());
                    }
                }
                break;
            case "classroom":
                for (Classroom c : classrooms) {
                    if (c != self) {
                        used.add(c.getClassroomId());
                    }
                }
                break;
            case "enrollment":
                for (Enrollment e : enrollments) {
                    if (e != self) {
                        used.add(e.getEnrollmentId());
                    }
                }
                break;
            case "assessment":
                for (Assessment a : assessments) {
                    if (a != self) {
                        used.add(a.getAssessmentId());
                    }
                }
                break;
            case "payment":
                for (FeePayment f : payments) {
                    if (f != self) {
                        used.add(f.getPaymentId());
                    }
                }
                break;
            case "receipt":
                for (FeePayment f : payments) {
                    if (f != self) {
                        used.add(f.getReceiptNumber());
                    }
                }
                break;
            case "book":
                for (LibraryBook b : books) {
                    if (b != self) {
                        used.add(b.getBookId());
                    }
                }
                break;
            case "isbn":
                for (LibraryBook b : books) {
                    if (b != self) {
                        used.add(b.getIsbn());
                    }
                }
                break;
            case "loan":
                for (BookLoan l : loans) {
                    if (l != self) {
                        used.add(l.getLoanId());
                    }
                }
                break;
            default:
                break;
        }
        for (String existing : used) {
            if (existing != null && existing.equalsIgnoreCase(value)) {
                throw new DuplicateRecordException(label + " " + value + " already exists.");
            }
        }
    }

    private static void validateAll(String entity, String[][] spec, Object[] vals, Object self) throws ValidationException {
        for (int i = 0; i < spec.length; i++) {
            String[] parts = spec[i][2].split(":");
            if (parts[0].equals("derived")) {
                continue;
            }
            if (parts[0].equals("ref")) {
                if (vals[i] == null && parts.length < 3) {
                    throw new ValidationException(spec[i][0] + " must be selected.");
                }
                continue;
            }
            if (vals[i] == null) {
                throw new ValidationException(spec[i][0] + " must not be blank.");
            }
            checkField(entity, spec, i, vals, self, (String) vals[i]);
        }
    }

    private static String[] f(String label, String hint, String kind, String unique, String mutable) {
        return new String[]{label, hint, kind, unique, mutable};
    }

    private static String[][] join(String[][]... parts) {
        int total = 0;
        for (String[][] part : parts) {
            total += part.length;
        }
        String[][] result = new String[total][];
        int position = 0;
        for (String[][] part : parts) {
            for (String[] row : part) {
                result[position++] = row;
            }
        }
        return result;
    }

    private static String[][] personSpec() {
        return new String[][]{
                f("Person ID", "String | unique | 4-30 chars", "text:4:30", "personid", "N"),
                f("First name", "String | 2-60 chars", "text:2:60", "", "Y"),
                f("Last name", "String | 2-60 chars", "text:2:60", "", "Y"),
                f("Email", "String | unique | email", "email", "email", "Y"),
                f("Phone", "String | 10-15 digits", "phone", "", "Y"),
                f("Date of birth", "LocalDate | YYYY-MM-DD", "date:past", "", "Y")};
    }

    private static String[][] memberSpec() {
        return new String[][]{
                f("Member number", "String | unique | 4-30", "text:4:30", "member", "N"),
                f("Department", "Department | select", "ref:department", "", "Y"),
                f("Join date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                f("Academic status", "", "choice:ACTIVE,INACTIVE", "", "Y"),
                f("Institutional email", "String | unique | email", "email", "instemail", "Y"),
                f("Advisor name", "String | 2-120 chars", "text:2:120", "", "Y")};
    }

    private static String[][] specOf(String entity) {
        switch (entity) {
            case "Department":
                return new String[][]{
                        f("Department ID", "String | unique | 4-30", "text:4:30", "department", "N"),
                        f("Department name", "String | 2-60 chars", "text:2:60", "", "Y"),
                        f("Department code", "String | unique | 2-10", "text:2:10", "deptcode", "Y"),
                        f("Office location", "String | 2-60 chars", "text:2:60", "", "Y"),
                        f("Contact email", "String | email", "email", "", "Y"),
                        f("Head teacher", "Teacher | optional select", "ref:teacher:optional", "", "Y")};
            case "Student":
                return join(personSpec(), memberSpec(), new String[][]{
                        f("Student number", "String | unique | 4-30", "text:4:30", "student", "N"),
                        f("Program name", "String | 2-120 chars", "text:2:120", "", "Y"),
                        f("Admission date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                        f("Study year", "int | 1-6", "int:1:6", "", "Y"),
                        f("Guardian phone", "String | 10-15 digits", "phone", "", "Y"),
                        f("Scholarship", "", "yesno", "", "Y")});
            case "Teacher":
                return join(personSpec(), memberSpec(), new String[][]{
                        f("Employee number", "String | unique | 4-30", "text:4:30", "employee", "N"),
                        f("Specialization", "String | 2-120 chars", "text:2:120", "", "Y"),
                        f("Hire date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                        f("Salary", "BigDecimal | > 0", "money", "", "Y"),
                        f("Office number", "String | 1-20 chars", "text:1:20", "", "Y"),
                        f("Employment type", "", "choice:FULL_TIME,PART_TIME,CONTRACT", "", "Y")});
            case "Administrator":
                return join(personSpec(), new String[][]{
                        f("Staff number", "String | unique | 4-30", "text:4:30", "staff", "N"),
                        f("Job title", "String | 2-60 chars", "text:2:60", "", "Y"),
                        f("Assigned office", "String | 1-30 chars", "text:1:30", "", "Y"),
                        f("Employment date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                        f("Access level", "int | 1-5", "int:1:5", "", "Y"),
                        f("Active", "", "yesno", "", "Y")});
            case "Course":
                return new String[][]{
                        f("Course ID", "String | unique | 4-30", "text:4:30", "course", "N"),
                        f("Course code", "String | unique | 2-10", "text:2:10", "coursecode", "Y"),
                        f("Course title", "String | 2-120 chars", "text:2:120", "", "Y"),
                        f("Credit hours", "int | 1-6", "int:1:6", "", "Y"),
                        f("Department", "Department | select", "ref:department", "", "Y"),
                        f("Assigned teacher", "Teacher | select", "ref:teacher", "", "Y")};
            case "Classroom":
                return new String[][]{
                        f("Classroom ID", "String | unique | 4-30", "text:4:30", "classroom", "N"),
                        f("Building name", "String | 2-60 chars", "text:2:60", "", "Y"),
                        f("Room number", "String | 1-20 chars", "text:1:20", "", "Y"),
                        f("Capacity", "int | > 0", "int:1:5000", "", "Y"),
                        f("Floor number", "int | 0 or more", "int:0:200", "", "Y"),
                        f("Available", "", "yesno", "", "Y")};
            case "Enrollment":
                return new String[][]{
                        f("Enrollment ID", "String | unique | 4-30", "text:4:30", "enrollment", "N"),
                        f("Student", "Student | select", "ref:student", "", "N"),
                        f("Course", "Course | select", "ref:course", "", "N"),
                        f("Enrollment date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                        f("Semester", "", "choice:FALL,SPRING,SUMMER", "", "Y"),
                        f("Enrollment status", "", "choice:ACTIVE,COMPLETED,DROPPED", "", "Y")};
            case "Assessment":
                return new String[][]{
                        f("Assessment ID", "String | unique | 4-30", "text:4:30", "assessment", "N"),
                        f("Enrollment", "Active enrollment | select", "ref:enrollment", "", "N"),
                        f("Assessment type", "", "choice:QUIZ,ASSIGNMENT,MIDTERM,FINAL", "", "Y"),
                        f("Maximum score", "double | > 0", "double:pos", "", "Y"),
                        f("Obtained score", "double | 0 to maximum", "double:nonneg", "", "Y"),
                        f("Assessment date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y")};
            case "FeePayment":
                return new String[][]{
                        f("Payment ID", "String | unique | 4-30", "text:4:30", "payment", "N"),
                        f("Student", "Student | select", "ref:student", "", "N"),
                        f("Amount", "BigDecimal | > 0", "money", "", "Y"),
                        f("Payment date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "Y"),
                        f("Payment method", "", "choice:CASH,MOBILE_MONEY,BANK_TRANSFER,CARD", "", "Y"),
                        f("Receipt number", "String | unique | 1-30", "text:1:30", "receipt", "N")};
            case "LibraryBook":
                return new String[][]{
                        f("Book ID", "String | unique | 4-30", "text:4:30", "book", "N"),
                        f("ISBN", "String | unique | 10-17", "text:10:17", "isbn", "Y"),
                        f("Title", "String | 2-120 chars", "text:2:120", "", "Y"),
                        f("Author", "String | 2-60 chars", "text:2:60", "", "Y"),
                        f("Total copies", "int | 0 or more", "int:0:100000", "", "Y"),
                        f("Available copies", "int | 0 to total", "int:0:100000", "", "Y")};
            case "BookLoan":
                return new String[][]{
                        f("Loan ID", "String | unique | 4-30", "text:4:30", "loan", "N"),
                        f("Student", "Student | select", "ref:student", "", "N"),
                        f("Library book", "LibraryBook | select", "ref:book", "", "N"),
                        f("Borrowed date", "LocalDate | YYYY-MM-DD", "date:notfuture", "", "N"),
                        f("Due date", "LocalDate | YYYY-MM-DD", "date:any", "", "Y"),
                        f("Returned date", "LocalDate | set on return", "derived", "", "N")};
            default:
                return new String[0][];
        }
    }

    private static int[] boundsOf(String entity) {
        if (entity.equals("Student") || entity.equals("Teacher")) {
            return new int[]{6, 12, 18};
        }
        if (entity.equals("Administrator")) {
            return new int[]{6, 9, 12};
        }
        return new int[]{2, 4, 6};
    }

    private static String[] titlesOf(String entity) {
        if (entity.equals("Student")) {
            return new String[]{"PERSONAL DETAILS", "ACADEMIC DETAILS", "STUDENT DETAILS"};
        }
        if (entity.equals("Teacher")) {
            return new String[]{"PERSONAL DETAILS", "ACADEMIC DETAILS", "TEACHER DETAILS"};
        }
        if (entity.equals("Administrator")) {
            return new String[]{"PERSONAL DETAILS", "ADMINISTRATOR DETAILS", "ADMINISTRATOR ACCESS"};
        }
        return new String[]{"DETAILS (PART 1)", "DETAILS (PART 2)", "DETAILS (PART 3)"};
    }

    private static String s(Object[] v, int i) {
        return (String) v[i];
    }

    private static LocalDate d(Object[] v, int i) {
        return LocalDate.parse((String) v[i]);
    }

    private static int n(Object[] v, int i) {
        return Integer.parseInt((String) v[i]);
    }

    private static double dbl(Object[] v, int i) {
        return Double.parseDouble((String) v[i]);
    }

    private static BigDecimal money(Object[] v, int i) {
        return new BigDecimal((String) v[i]);
    }

    private static boolean yes(Object[] v, int i) {
        return "YES".equals(v[i]);
    }

    private static String yn(boolean value) {
        return value ? "YES" : "NO";
    }

    private static Object build(String entity, Object[] v) {
        switch (entity) {
            case "Department":
                return new Department(s(v, 0), s(v, 1), s(v, 2), s(v, 3), s(v, 4), (Teacher) v[5]);
            case "Student":
                return new Student(s(v, 0), s(v, 1), s(v, 2), s(v, 3), s(v, 4), d(v, 5), s(v, 6),
                        (Department) v[7], d(v, 8), s(v, 9), s(v, 10), s(v, 11), s(v, 12), s(v, 13),
                        d(v, 14), n(v, 15), s(v, 16), yes(v, 17));
            case "Teacher":
                return new Teacher(s(v, 0), s(v, 1), s(v, 2), s(v, 3), s(v, 4), d(v, 5), s(v, 6),
                        (Department) v[7], d(v, 8), s(v, 9), s(v, 10), s(v, 11), s(v, 12), s(v, 13),
                        d(v, 14), money(v, 15), s(v, 16), s(v, 17));
            case "Administrator":
                return new Administrator(s(v, 0), s(v, 1), s(v, 2), s(v, 3), s(v, 4), d(v, 5), s(v, 6),
                        s(v, 7), s(v, 8), d(v, 9), n(v, 10), yes(v, 11));
            case "Course":
                return new Course(s(v, 0), s(v, 1), s(v, 2), n(v, 3), (Department) v[4], (Teacher) v[5]);
            case "Classroom":
                return new Classroom(s(v, 0), s(v, 1), s(v, 2), n(v, 3), n(v, 4), yes(v, 5));
            case "Enrollment":
                return new Enrollment(s(v, 0), (Student) v[1], (Course) v[2], d(v, 3), s(v, 4), s(v, 5));
            case "Assessment":
                return new Assessment(s(v, 0), (Enrollment) v[1], s(v, 2), dbl(v, 3), dbl(v, 4), d(v, 5));
            case "FeePayment":
                return new FeePayment(s(v, 0), (Student) v[1], money(v, 2), d(v, 3), s(v, 4), s(v, 5));
            case "LibraryBook":
                return new LibraryBook(s(v, 0), s(v, 1), s(v, 2), s(v, 3), n(v, 4), n(v, 5));
            default:
                return new BookLoan(s(v, 0), (Student) v[1], (LibraryBook) v[2], d(v, 3), d(v, 4), null);
        }
    }

    private static Object[] joinValues(Object[] first, Object[] second) {
        Object[] result = new Object[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static Object[] personValues(Person p) {
        return new Object[]{p.getPersonId(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getPhoneNumber(),
                p.getDateOfBirth().toString()};
    }

    private static Object[] memberValues(AcademicMember m) {
        return joinValues(personValues(m), new Object[]{m.getMemberNumber(), m.getDepartment(), m.getJoinDate().toString(),
                m.getAcademicStatus(), m.getInstitutionalEmail(), m.getAdvisorName()});
    }

    private static Object[] toValues(Object record) {
        if (record instanceof Department) {
            Department x = (Department) record;
            return new Object[]{x.getDepartmentId(), x.getDepartmentName(), x.getDepartmentCode(),
                    x.getOfficeLocation(), x.getContactEmail(), x.getHeadTeacher()};
        }
        if (record instanceof Student) {
            Student x = (Student) record;
            return joinValues(memberValues(x), new Object[]{x.getStudentNumber(), x.getProgramName(),
                    x.getAdmissionDate().toString(), String.valueOf(x.getStudyYear()), x.getGuardianPhone(),
                    yn(x.isScholarshipStatus())});
        }
        if (record instanceof Teacher) {
            Teacher x = (Teacher) record;
            return joinValues(memberValues(x), new Object[]{x.getEmployeeNumber(), x.getSpecialization(),
                    x.getHireDate().toString(), x.getSalary().toPlainString(), x.getOfficeNumber(),
                    x.getEmploymentType()});
        }
        if (record instanceof Administrator) {
            Administrator x = (Administrator) record;
            return joinValues(personValues(x), new Object[]{x.getStaffNumber(), x.getJobTitle(), x.getAssignedOffice(),
                    x.getEmploymentDate().toString(), String.valueOf(x.getAccessLevel()), yn(x.isActive())});
        }
        if (record instanceof Course) {
            Course x = (Course) record;
            return new Object[]{x.getCourseId(), x.getCourseCode(), x.getCourseTitle(),
                    String.valueOf(x.getCreditHours()), x.getDepartment(), x.getAssignedTeacher()};
        }
        if (record instanceof Classroom) {
            Classroom x = (Classroom) record;
            return new Object[]{x.getClassroomId(), x.getBuildingName(), x.getRoomNumber(),
                    String.valueOf(x.getCapacity()), String.valueOf(x.getFloorNumber()), yn(x.isAvailable())};
        }
        if (record instanceof Enrollment) {
            Enrollment x = (Enrollment) record;
            return new Object[]{x.getEnrollmentId(), x.getStudent(), x.getCourse(), x.getEnrollmentDate().toString(),
                    x.getSemester(), x.getEnrollmentStatus()};
        }
        if (record instanceof Assessment) {
            Assessment x = (Assessment) record;
            return new Object[]{x.getAssessmentId(), x.getEnrollment(), x.getAssessmentType(),
                    String.valueOf(x.getMaximumScore()), String.valueOf(x.getObtainedScore()),
                    x.getAssessmentDate().toString()};
        }
        if (record instanceof FeePayment) {
            FeePayment x = (FeePayment) record;
            return new Object[]{x.getPaymentId(), x.getStudent(), x.getAmount().toPlainString(),
                    x.getPaymentDate().toString(), x.getPaymentMethod(), x.getReceiptNumber()};
        }
        if (record instanceof LibraryBook) {
            LibraryBook x = (LibraryBook) record;
            return new Object[]{x.getBookId(), x.getIsbn(), x.getTitle(), x.getAuthor(),
                    String.valueOf(x.getTotalCopies()), String.valueOf(x.getAvailableCopies())};
        }
        BookLoan x = (BookLoan) record;
        return new Object[]{x.getLoanId(), x.getStudent(), x.getBook(), x.getBorrowedDate().toString(),
                x.getDueDate().toString(), x.getReturnedDate() == null ? null : x.getReturnedDate().toString()};
    }

    private static void applyPerson(Person p, Object[] v) {
        p.setFirstName(s(v, 1));
        p.setLastName(s(v, 2));
        p.setEmail(s(v, 3));
        p.setPhoneNumber(s(v, 4));
        p.setDateOfBirth(d(v, 5));
    }

    private static void applyMember(AcademicMember m, Object[] v) {
        applyPerson(m, v);
        m.setDepartment((Department) v[7]);
        m.setJoinDate(d(v, 8));
        m.setAcademicStatus(s(v, 9));
        m.setInstitutionalEmail(s(v, 10));
        m.setAdvisorName(s(v, 11));
    }

    private static void apply(Object record, Object[] v) {
        if (record instanceof Department) {
            Department x = (Department) record;
            x.setDepartmentName(s(v, 1));
            x.setDepartmentCode(s(v, 2));
            x.setOfficeLocation(s(v, 3));
            x.setContactEmail(s(v, 4));
            x.setHeadTeacher((Teacher) v[5]);
        } else if (record instanceof Student) {
            Student x = (Student) record;
            applyMember(x, v);
            x.setProgramName(s(v, 13));
            x.setAdmissionDate(d(v, 14));
            x.setStudyYear(n(v, 15));
            x.setGuardianPhone(s(v, 16));
            x.setScholarshipStatus(yes(v, 17));
        } else if (record instanceof Teacher) {
            Teacher x = (Teacher) record;
            applyMember(x, v);
            x.setSpecialization(s(v, 13));
            x.setHireDate(d(v, 14));
            x.setSalary(money(v, 15));
            x.setOfficeNumber(s(v, 16));
            x.setEmploymentType(s(v, 17));
        } else if (record instanceof Administrator) {
            Administrator x = (Administrator) record;
            applyPerson(x, v);
            x.setJobTitle(s(v, 7));
            x.setAssignedOffice(s(v, 8));
            x.setEmploymentDate(d(v, 9));
            x.setAccessLevel(n(v, 10));
            x.setActive(yes(v, 11));
        } else if (record instanceof Course) {
            Course x = (Course) record;
            x.setCourseCode(s(v, 1));
            x.setCourseTitle(s(v, 2));
            x.setCreditHours(n(v, 3));
            x.setDepartment((Department) v[4]);
            x.setAssignedTeacher((Teacher) v[5]);
        } else if (record instanceof Classroom) {
            Classroom x = (Classroom) record;
            x.setBuildingName(s(v, 1));
            x.setRoomNumber(s(v, 2));
            x.setCapacity(n(v, 3));
            x.setFloorNumber(n(v, 4));
            x.setAvailable(yes(v, 5));
        } else if (record instanceof Enrollment) {
            Enrollment x = (Enrollment) record;
            x.setEnrollmentDate(d(v, 3));
            x.setSemester(s(v, 4));
            x.setEnrollmentStatus(s(v, 5));
        } else if (record instanceof Assessment) {
            Assessment x = (Assessment) record;
            x.setAssessmentType(s(v, 2));
            x.setMaximumScore(dbl(v, 3));
            x.setObtainedScore(dbl(v, 4));
            x.setAssessmentDate(d(v, 5));
        } else if (record instanceof FeePayment) {
            FeePayment x = (FeePayment) record;
            x.setAmount(money(v, 2));
            x.setPaymentDate(d(v, 3));
            x.setPaymentMethod(s(v, 4));
        } else if (record instanceof LibraryBook) {
            LibraryBook x = (LibraryBook) record;
            x.setIsbn(s(v, 1));
            x.setTitle(s(v, 2));
            x.setAuthor(s(v, 3));
            x.setTotalCopies(n(v, 4));
            x.setAvailableCopies(n(v, 5));
        } else if (record instanceof BookLoan) {
            BookLoan x = (BookLoan) record;
            x.setDueDate(d(v, 4));
        }
    }

    private static List<Registrable> recordsOf(String entity) {
        List<Registrable> list = new ArrayList<>();
        switch (entity) {
            case "Department":
                list.addAll(departments);
                break;
            case "Student":
                list.addAll(students);
                break;
            case "Teacher":
                list.addAll(teachers);
                break;
            case "Administrator":
                list.addAll(administrators);
                break;
            case "Course":
                list.addAll(courses);
                break;
            case "Classroom":
                list.addAll(classrooms);
                break;
            case "Enrollment":
                list.addAll(enrollments);
                break;
            case "Assessment":
                list.addAll(assessments);
                break;
            case "FeePayment":
                list.addAll(payments);
                break;
            case "LibraryBook":
                list.addAll(books);
                break;
            case "BookLoan":
                list.addAll(loans);
                break;
            default:
                break;
        }
        return list;
    }

    private static String chooseEntity(String title, boolean allowAll) {
        while (true) {
            System.out.println();
            System.out.println("---------- " + title + " ----------");
            for (int i = 0; i < ENTITIES.length; i++) {
                System.out.println(" " + (i + 1) + ". " + displayName(ENTITIES[i]));
            }
            if (allowAll) {
                System.out.println(" " + (ENTITIES.length + 1) + ". All record types");
            }
            System.out.println(" 0. Back to main menu");
            System.out.print("Select: ");
            String input = readLine();
            int number;
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number from the list.");
                continue;
            }
            if (number == 0) {
                return null;
            }
            if (number >= 1 && number <= ENTITIES.length) {
                return ENTITIES[number - 1];
            }
            if (allowAll && number == ENTITIES.length + 1) {
                return "ALL";
            }
            System.out.println("! Invalid selection.");
        }
    }

    private static Registrable chooseFromList(List<Registrable> records, String title) {
        while (true) {
            System.out.println("---------- SELECT " + title.toUpperCase() + " ----------");
            for (int i = 0; i < records.size(); i++) {
                System.out.println(" " + (i + 1) + " " + records.get(i).getRegistrationSummary());
            }
            System.out.println(" 0 Cancel");
            System.out.print("Select: ");
            String input = readLine();
            try {
                int number = Integer.parseInt(input);
                if (number == 0) {
                    return null;
                }
                if (number >= 1 && number <= records.size()) {
                    return records.get(number - 1);
                }
                System.out.println("! Choose a number between 0 and " + records.size() + ".");
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number from the list.");
            }
        }
    }

    private static void showRecords(String entity) {
        List<Registrable> records = recordsOf(entity);
        System.out.println();
        System.out.println("=== " + displayName(entity) + " records (" + records.size() + ") ===");
        if (records.isEmpty()) {
            System.out.println("No " + displayName(entity) + " records have been registered yet.");
            return;
        }
        for (int i = 0; i < records.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + records.get(i).getRegistrationSummary());
        }
    }

    private static void viewAllRecords() {
        String entity = chooseEntity("VIEW ALL RECORDS", true);
        if (entity == null) {
            return;
        }
        if (entity.equals("ALL")) {
            for (String name : ENTITIES) {
                showRecords(name);
            }
        } else {
            showRecords(entity);
        }
        System.out.print("Press ENTER to return to the main menu...");
        readLine();
    }

    private static void searchById() {
        System.out.print("Enter the ID to search for (0 to cancel): ");
        String id = readLine();
        if (id.isEmpty()) {
            System.out.println("! The ID must not be blank.");
            return;
        }
        if (id.equals("0")) {
            return;
        }
        boolean found = false;
        for (String entity : ENTITIES) {
            for (Registrable record : recordsOf(entity)) {
                if (((Identifiable) record).getId().equalsIgnoreCase(id)) {
                    System.out.println();
                    System.out.println("--- " + displayName(entity) + " found ---");
                    record.printDetails();
                    found = true;
                }
            }
        }
        if (!found) {
            throw new RecordNotFoundException("No record with ID '" + id + "' exists.");
        }
    }

    private static void updateRecord() {
        String entity = chooseEntity("UPDATE EXISTING RECORD", false);
        if (entity == null) {
            return;
        }
        List<Registrable> records = recordsOf(entity);
        if (records.isEmpty()) {
            System.out.println("No " + displayName(entity) + " records exist, so there is nothing to update.");
            return;
        }
        Registrable record = chooseFromList(records, displayName(entity));
        if (record == null) {
            return;
        }
        String[][] spec = specOf(entity);
        Object[] vals = toValues(record);
        boolean editing = true;
        while (editing) {
            System.out.println();
            System.out.println("--- UPDATE " + displayName(entity).toUpperCase() + " ---");
            for (int i = 0; i < spec.length; i++) {
                String lock = spec[i][4].equals("N") ? " (locked)" : "";
                System.out.printf(" %02d %-20s : %s%s%n", i + 1, spec[i][0], display(vals[i], spec[i][2]), lock);
            }
            System.out.print("Field number to edit (0 to finish): ");
            int number;
            try {
                number = Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a valid field number.");
                continue;
            }
            if (number == 0) {
                editing = false;
                continue;
            }
            if (number < 1 || number > spec.length) {
                System.out.println("! Field number must be between 1 and " + spec.length + ".");
                continue;
            }
            int idx = number - 1;
            if (spec[idx][4].equals("N")) {
                System.out.println("! This field cannot be changed.");
                continue;
            }
            Object result;
            try {
                result = askField(entity, spec, idx, vals, record);
            } catch (PrerequisiteMissingException e) {
                System.out.println("! " + e.getMessage());
                continue;
            }
            if (result == CANCEL || result == BACK) {
                System.out.println("Edit cancelled. Nothing was changed.");
                continue;
            }
            Object[] work = vals.clone();
            work[idx] = result;
            try {
                validateAll(entity, spec, work, record);
            } catch (ValidationException e) {
                System.out.println("! Change rejected: " + e.getMessage());
                continue;
            } catch (DuplicateRecordException e) {
                System.out.println("! Change rejected: " + e.getMessage());
                continue;
            }
            System.out.println("Old value: " + display(vals[idx], spec[idx][2]));
            System.out.println("New value: " + display(result, spec[idx][2]));
            System.out.print("Confirm this change? 1 Yes / 0 No: ");
            if (readLine().equals("1")) {
                apply(record, work);
                try {
                    record.validate();
                    vals = work;
                    System.out.println("+ " + displayName(entity) + " " + ((Identifiable) record).getId() + " updated successfully.");
                } catch (ValidationException e) {
                    apply(record, vals);
                    System.out.println("! Change rolled back: " + e.getMessage());
                }
            } else {
                System.out.println("Change discarded.");
            }
        }
    }

    private static void returnBook() {
        List<BookLoan> active = new ArrayList<>();
        for (BookLoan loan : loans) {
            if (loan.getReturnedDate() == null) {
                active.add(loan);
            }
        }
        if (active.isEmpty()) {
            System.out.println("No borrowed book is waiting to be returned. Use option 11 to borrow a book first.");
            return;
        }
        List<Registrable> options = new ArrayList<>(active);
        BookLoan loan = (BookLoan) chooseFromList(options, "loan to return");
        if (loan == null) {
            System.out.println("- Return cancelled. Nothing was changed.");
            return;
        }
        LocalDate returned = null;
        while (returned == null) {
            System.out.print("Returned date [LocalDate | YYYY-MM-DD] (CANCEL to stop): ");
            String input = readLine();
            if (input.equalsIgnoreCase("CANCEL")) {
                System.out.println("- Return cancelled. Nothing was changed.");
                return;
            }
            try {
                LocalDate date = parseDate(input);
                if (date.isBefore(loan.getBorrowedDate())) {
                    System.out.println("! Returned date cannot be before the borrowed date (" + loan.getBorrowedDate() + ").");
                } else if (date.isAfter(LocalDate.now())) {
                    System.out.println("! Returned date cannot be in the future.");
                } else {
                    returned = date;
                }
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
        System.out.println("Loan     : " + loan.getRegistrationSummary());
        System.out.println("Returned : " + returned);
        System.out.print("1 Confirm return   0 Cancel: ");
        if (!readLine().equals("1")) {
            System.out.println("- Return cancelled. Nothing was changed.");
            return;
        }
        loan.setReturnedDate(returned);
        try {
            loan.validate();
            LibraryBook book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            System.out.println("+ Book '" + book.getTitle() + "' returned. Available copies: "
                    + book.getAvailableCopies() + "/" + book.getTotalCopies() + ".");
        } catch (ValidationException e) {
            loan.setReturnedDate(null);
            System.out.println("! Return rejected: " + e.getMessage());
        }
    }

    private static Student chooseStudentForReport() {
        Object chosen = pickRef("student", false, false);
        if (chosen instanceof Student) {
            return (Student) chosen;
        }
        return null;
    }

    private static void studentAcademicSummary() {
        Student student = chooseStudentForReport();
        if (student == null) {
            return;
        }
        System.out.println();
        System.out.println("=========== STUDENT ACADEMIC SUMMARY ===========");
        student.printDetails();
        System.out.println("--- Enrollments and assessments ---");
        int enrollmentCount = 0;
        int activeCredits = 0;
        double percentageTotal = 0;
        int assessmentCount = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent() != student) {
                continue;
            }
            enrollmentCount++;
            if (enrollment.isActive()) {
                activeCredits += enrollment.getCourse().getCreditHours();
            }
            System.out.println(" " + enrollment.getCourse().getCourseCode() + " " + enrollment.getCourse().getCourseTitle()
                    + " | " + enrollment.getSemester() + " | " + enrollment.getEnrollmentStatus());
            boolean hasAssessment = false;
            for (Assessment assessment : assessments) {
                if (assessment.getEnrollment() == enrollment) {
                    hasAssessment = true;
                    assessmentCount++;
                    percentageTotal += assessment.getPercentage();
                    System.out.printf("     %s: %.1f / %.1f (%.1f%%) on %s%n", assessment.getAssessmentType(),
                            assessment.getObtainedScore(), assessment.getMaximumScore(),
                            assessment.getPercentage(), assessment.getAssessmentDate());
                }
            }
            if (!hasAssessment) {
                System.out.println("     No assessment recorded yet.");
            }
        }
        if (enrollmentCount == 0) {
            System.out.println(" This student has no enrollments yet.");
        }
        System.out.println("Active credit hours : " + activeCredits);
        if (assessmentCount > 0) {
            System.out.printf("Average score       : %.2f%%%n", percentageTotal / assessmentCount);
        } else {
            System.out.println("Average score       : not available (no assessments)");
        }
        System.out.print("Press ENTER to return to the main menu...");
        readLine();
    }

    private static void studentPaymentHistory() {
        Student student = chooseStudentForReport();
        if (student == null) {
            return;
        }
        System.out.println();
        System.out.println("=========== PAYMENT HISTORY: " + student.getDisplayName() + " ===========");
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (FeePayment payment : payments) {
            if (payment.getStudent() == student) {
                count++;
                total = total.add(payment.getAmount());
                System.out.println(" " + count + ". " + payment.getPaymentDate() + " | " + payment.getAmount() + " | "
                        + payment.getPaymentMethod() + " | receipt " + payment.getReceiptNumber());
            }
        }
        if (count == 0) {
            System.out.println(" No payments have been recorded for this student.");
        } else {
            System.out.println("Total paid: " + total);
        }
        System.out.print("Press ENTER to return to the main menu...");
        readLine();
    }

    private static void showAvailableBooks() {
        System.out.println();
        System.out.println("=========== AVAILABLE BOOKS ===========");
        int count = 0;
        for (LibraryBook book : books) {
            if (book.getAvailableCopies() > 0) {
                count++;
                System.out.println(" " + count + ". " + book.getRegistrationSummary());
            }
        }
        if (count == 0) {
            System.out.println(" No book is currently available.");
        }
        System.out.print("Press ENTER to return to the main menu...");
        readLine();
    }

    private static void demonstratePolymorphism() {
        System.out.println();
        System.out.println("=========== POLYMORPHISM DEMONSTRATION ===========");
        System.out.println("1) Superclass polymorphism: List<Person> holding Student, Teacher and Administrator");
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);
        if (people.isEmpty()) {
            System.out.println("   No Student, Teacher or Administrator is registered yet.");
        }
        for (Person person : people) {
            StringBuilder chain = new StringBuilder();
            Class<?> type = person.getClass();
            while (type != null && type != Object.class) {
                if (chain.length() > 0) {
                    chain.append(" -> ");
                }
                chain.append(type.getSimpleName());
                type = type.getSuperclass();
            }
            System.out.println("   Object type    : " + chain);
            System.out.println("   getRoleDescription() : " + person.getRoleDescription());
            if (person instanceof AcademicMember) {
                System.out.println("   getAcademicCategory(): " + ((AcademicMember) person).getAcademicCategory());
            }
            System.out.println("   getId()/isActive()   : " + person.getId() + " / " + person.isActive());
            System.out.println("   Summary        : " + person.getRegistrationSummary());
            System.out.println();
        }
        System.out.println("2) Interface polymorphism: List<Identifiable> holding unrelated entities");
        List<Identifiable> things = new ArrayList<>();
        things.addAll(departments);
        things.addAll(courses);
        things.addAll(classrooms);
        things.addAll(enrollments);
        things.addAll(assessments);
        things.addAll(payments);
        things.addAll(books);
        things.addAll(loans);
        if (things.isEmpty()) {
            System.out.println("   No Department, Course, Classroom, Enrollment, Assessment, Payment, Book or Loan yet.");
        }
        for (Identifiable thing : things) {
            System.out.println("   " + thing.getClass().getSimpleName() + " | id=" + thing.getId()
                    + " | name=" + thing.getDisplayName() + " | active=" + thing.isActive());
        }
        System.out.println();
        System.out.println("Why Person and AcademicMember cannot be instantiated:");
        System.out.println("   They are abstract classes. They leave methods such as getRoleDescription(),");
        System.out.println("   getAcademicCategory() and isActive() without a body, so an object of them would be");
        System.out.println("   incomplete. Only the concrete subclasses Student, Teacher and Administrator can be created.");
        System.out.print("Press ENTER to return to the main menu...");
        readLine();
    }
}
