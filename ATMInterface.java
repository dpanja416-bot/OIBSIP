import java.util.ArrayList;
import java.util.Scanner;
public class ATMInterface {
    static class User {
        private String userId;
        private String pin;
        User(String userId, String pin) {
            this.userId = userId;
            this.pin = pin;
        }
        boolean validate(String uid, String pin) {
            return this.userId.equals(uid) && this.pin.equals(pin);
        }
    }
    static class Transaction {
        ArrayList<String> history = new ArrayList<>();

        void add(String record) {
            history.add(record);
        }
        void show() {
            if (history.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                System.out.println("----- Transaction History -----");
                for (String h : history) {
                    System.out.println(h);
                }
            }
        }
    }
    static class Account {
        double balance = 10000;
        Transaction transaction = new Transaction();
        void deposit(double amount) {
            balance += amount;
            transaction.add("Deposited: ₹" + amount);
            System.out.println("Deposit successful.");
        }
        void withdraw(double amount) {
            if (amount <= balance) {
                balance -= amount;
                transaction.add("Withdrawn: ₹" + amount);
                System.out.println("Please collect your cash.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
        void transfer(double amount) {
            if (amount <= balance) {
                balance -= amount;
                transaction.add("Transferred: ₹" + amount);
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
    }
    static class ATMOperations {
        Account account = new Account();
        Scanner sc = new Scanner(System.in);
        void start() {
            int choice;
            do {
                System.out.println("\n===== ATM MENU =====");
                System.out.println("1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        account.transaction.show();
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        account.withdraw(sc.nextDouble());
                        break;
                    case 3:
                        System.out.print("Enter amount to deposit: ");
                        account.deposit(sc.nextDouble());
                        break;
                    case 4:
                        System.out.print("Enter amount to transfer: ");
                        account.transfer(sc.nextDouble());
                        break;
                    case 5:
                        System.out.println("Thank you for using ATM.");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } while (choice != 5);
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User user = new User("user123", "1234");
        System.out.println("===== WELCOME TO ATM =====");
        System.out.print("Enter User ID: ");
        String uid = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();
        if (user.validate(uid, pin)) {
            System.out.println("Login Successful!");
            ATMOperations atm = new ATMOperations();
            atm.start();
        } else {
            System.out.println("Invalid User ID or PIN.");
        }
    }
}
