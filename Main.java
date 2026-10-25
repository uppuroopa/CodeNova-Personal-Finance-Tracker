
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Transaction> transactions = new ArrayList<>();
private static final String DATA_FILE = "transactions.csv";
    
public static void main(String[] args) {
    loadTransactions();
    boolean running = true;


        while (running) {
            System.out.println("\n================================");
            System.out.println("    PERSONAL FINANCE TRACKER");
            System.out.println("================================");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Monthly Summary");
            
System.out.println("5. Filter by Date Range");
System.out.println("6. Export Report");
System.out.println("7. Filter by Category");
System.out.println("8. Exit");

            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTransaction("INCOME");
                    break;
                case "2":
                    addTransaction("EXPENSE");
                    break;
                case "3":
                    viewTransactions();
                    break;
                case "4":
                    showBalance();
                    break;
                case "5":
                    filterTransactions();
                    break;
                case "6":
    exportReport();
    break;
                
case "7":
    filterByCategory();
    break;
case "8":
    running = false;
    System.out.println("Thank you for using Personal Finance Tracker!");
    break;

            }
        }

        scanner.close();
    }

    private static void addTransaction(String type) {
        try {
            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter remarks: ");
            String remarks = scanner.nextLine().trim();

            if (category.isEmpty()) {
                System.out.println("Category cannot be empty.");
                return;
            }

            transactions.add(new Transaction(type, amount, category, remarks));
            saveTransactions();
            System.out.println(type + " recorded successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }

    private static void viewTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.println((i + 1) + ". " + transactions.get(i));
        }
    }

    
private static void showBalance() {
    java.time.YearMonth selectedMonth;

    try {
        System.out.print("Enter month (YYYY-MM), for example 2026-10: ");
        selectedMonth = java.time.YearMonth.parse(scanner.nextLine().trim());
    } catch (java.time.format.DateTimeParseException e) {
        System.out.println("Invalid month format. Please use YYYY-MM.");
        return;
    }

    BigDecimal income = BigDecimal.ZERO;
    BigDecimal expenses = BigDecimal.ZERO;

    for (Transaction transaction : transactions) {
        java.time.YearMonth transactionMonth =
                java.time.YearMonth.from(transaction.getTimestamp());

        if (transactionMonth.equals(selectedMonth)) {
            if (transaction.getType().equals("INCOME")) {
                income = income.add(transaction.getAmount());
            } else {
                expenses = expenses.add(transaction.getAmount());
            }
        }
    }

    System.out.println("\n===== MONTHLY SUMMARY: " + selectedMonth + " =====");
    System.out.println("Total income:   " + income);
    System.out.println("Total expenses: " + expenses);
    System.out.println("Balance:        " + income.subtract(expenses));
}


    
private static void filterTransactions() {
    java.time.LocalDate startDate;
    java.time.LocalDate endDate;

    try {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        startDate = java.time.LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Enter end date (YYYY-MM-DD): ");
        endDate = java.time.LocalDate.parse(scanner.nextLine().trim());

        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date.");
            return;
        }
    } catch (java.time.format.DateTimeParseException e) {
        System.out.println("Invalid date. Use YYYY-MM-DD, for example 2026-10-25.");
        return;
    }

    boolean found = false;

    for (Transaction transaction : transactions) {
        java.time.LocalDate transactionDate =
                transaction.getTimestamp().toLocalDate();

        if (!transactionDate.isBefore(startDate)
                && !transactionDate.isAfter(endDate)) {
            System.out.println(transaction);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No transactions found in that date range.");
    }
}

    
private static void exportReport() {
    if (transactions.isEmpty()) {
        System.out.println("No transactions to export.");
        return;
    }

    try (java.io.PrintWriter writer =
            new java.io.PrintWriter("finance_report.csv")) {

        writer.println("Timestamp,Type,Amount,Category,Remarks");

        for (Transaction transaction : transactions) {
            String remarks = transaction.getRemarks().replace("\"", "\"\"");
            String category = transaction.getCategory().replace("\"", "\"\"");

            writer.println(
                    "\"" + transaction.getTimestamp() + "\","
                    + "\"" + transaction.getType() + "\","
                    + transaction.getAmount() + ","
                    + "\"" + category + "\","
                    + "\"" + remarks + "\""
            );
        }

        System.out.println("CSV report exported successfully!");
        System.out.println("File location: finance_report.csv");

    } catch (java.io.IOException e) {
        System.out.println("Error exporting report: " + e.getMessage());
    }
}

private static void saveTransactions() {
    try (java.io.PrintWriter writer = new java.io.PrintWriter(
            new java.io.FileWriter(DATA_FILE))) {

        for (Transaction t : transactions) {
            writer.println(
                t.getTimestamp() + "," +
                t.getType() + "," +
                t.getAmount().toPlainString() + "," +
                t.getCategory().replace(",", " ") + "," +
                t.getRemarks().replace(",", " ")
            );
        }

    } catch (java.io.IOException e) {
        System.out.println("Error saving transactions: " + e.getMessage());
    }
}

private static void loadTransactions() {
    java.io.File file = new java.io.File(DATA_FILE);

    if (!file.exists()) {
        return;
    }

    try (java.util.Scanner fileScanner = new java.util.Scanner(file)) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",", 5);

            if (parts.length == 5) {
                transactions.add(new Transaction(
                    parts[1],
                    new BigDecimal(parts[2]),
                    parts[3],
                    parts[4],
                    LocalDateTime.parse(parts[0])
                ));
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading transactions: " + e.getMessage());
    }
}

private static void filterByCategory() {
    System.out.print("Enter category to search: ");
    String category = scanner.nextLine().trim();

    boolean found = false;

    for (Transaction transaction : transactions) {
        if (transaction.getCategory().equalsIgnoreCase(category)) {
            System.out.println(transaction);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No transactions found for that category.");
    }
}

}
