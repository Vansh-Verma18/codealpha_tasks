import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {
    private ArrayList<String> studentNames;
    private ArrayList<Double> studentGrades;
    private Scanner scanner;

    public StudentGradeTracker() {
        studentNames = new ArrayList<>();
        studentGrades = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        
        while (true) {
            try {
                System.out.print("Enter " + name + "'s grade (or -1 to finish): ");
                double grade = Double.parseDouble(scanner.nextLine());
                
                if (grade == -1) break;
                
                if (grade < 0 || grade > 100) {
                    System.out.println("Invalid grade. Please enter a grade between 0 and 100.");
                    continue;
                }
                
                studentNames.add(name);
                studentGrades.add(grade);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric grade.");
            }
        }
    }

    public void displayGradeStatistics() {
        if (studentGrades.isEmpty()) {
            System.out.println("No grades entered.");
            return;
        }

        System.out.println("\n--- Grade Statistics ---");
        
        for (int i = 0; i < studentNames.size(); i++) {
            System.out.printf("%s: %.2f\n", studentNames.get(i), studentGrades.get(i));
        }

        double average = calculateAverage();
        double highest = findHighestScore();
        double lowest = findLowestScore();

        System.out.printf("\nClass Average: %.2f\n", average);
        System.out.printf("Highest Score: %.2f\n", highest);
        System.out.printf("Lowest Score: %.2f\n", lowest);
    }

    private double calculateAverage() {
        if (studentGrades.isEmpty()) return 0.0;
        
        double sum = 0;
        for (double grade : studentGrades) {
            sum += grade;
        }
        return sum / studentGrades.size();
    }

    private double findHighestScore() {
        if (studentGrades.isEmpty()) return 0.0;
        
        double highest = studentGrades.get(0);
        for (double grade : studentGrades) {
            if (grade > highest) {
                highest = grade;
            }
        }
        return highest;
    }

    private double findLowestScore() {
        if (studentGrades.isEmpty()) return 0.0;
        
        double lowest = studentGrades.get(0);
        for (double grade : studentGrades) {
            if (grade < lowest) {
                lowest = grade;
            }
        }
        return lowest;
    }

    public void run() {
        while (true) {
            System.out.println("\n--- Student Grade Tracker ---");
            System.out.println("1. Add Student Grades");
            System.out.println("2. Display Grade Statistics");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    displayGradeStatistics();
                    break;
                case "3":
                    System.out.println("Exiting the program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        StudentGradeTracker tracker = new StudentGradeTracker();
        tracker.run();
    }
}
