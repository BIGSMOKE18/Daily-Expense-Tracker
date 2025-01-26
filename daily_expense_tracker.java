import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Expense {
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Expense(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + " | " + category + " | " + amount + " | " + description;
    }
}

class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private static final String FILE_NAME = "expenses.txt";


    public void addExpense(double amount, String category, String description) {
        Expense expense = new Expense(amount, category, description, LocalDate.now());
        expenses.add(expense);
        System.out.println("Expense added successfully!");
    }


    public void viewSummary(LocalDate startDate, LocalDate endDate) {
        double total = 0;
        System.out.println("Date       | Category   | Amount   | Description");
        System.out.println("------------------------------------------------");
        for (Expense expense : expenses) {
            if (!expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate)) {
                System.out.println(expense);
                total += expense.getAmount();
            }
        }
        System.out.println("------------------------------------------------");
        System.out.println("Total Expenses: " + total);
    }


    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.toString());
                writer.newLine();
            }
        }
        System.out.println("Expenses saved to file.");
    }


    public void loadFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                LocalDate date = LocalDate.parse(parts[0].trim());
                String category = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                String description = parts[3].trim();
                expenses.add(new Expense(amount, category, description, date));
            }
        }
        System.out.println("Expenses loaded from file.");
    }
}


public class daily_expense_tracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        try {
            manager.loadFromFile();
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }

        while (true) {
            System.out.println("\n--- Daily Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View Summary (Daily)");
            System.out.println("3. View Summary (Weekly)");
            System.out.println("4. View Summary (Monthly)");
            System.out.println("5. Save & Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    manager.addExpense(amount, category, description);
                    break;
                case 2:
                    manager.viewSummary(LocalDate.now(), LocalDate.now());
                    break;
                case 3:
                    manager.viewSummary(LocalDate.now().minusDays(7), LocalDate.now());
                    break;
                case 4:
                    manager.viewSummary(LocalDate.now().minusDays(30), LocalDate.now());
                    break;
                case 5:
                    try {
                        manager.saveToFile();
                    } catch (IOException e) {
                        System.out.println("Error saving data: " + e.getMessage());
                    }
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
