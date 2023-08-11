import java.sql.*;
import java.util.*;
public class demo {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection dc = new DatabaseConnection();
		Connection cn = dc.getConnection();
		
		Scanner sc = new Scanner(System.in);
		

        System.out.println("Welcome to Development Bank ATM");
        System.out.println("1. Login ");
        System.out.println("2. Details ");

        System.out.print("Please Select Your Option: ");
        int choice = Integer.parseInt(sc.nextLine());

        switch (choice) {
            case 1:
                displayLogin(cn, sc);
                break;
            case 2:
                displayUserDetail(cn, sc);
                break;
        }
    }

    private static void displayLogin(Connection cn, Scanner sc) throws SQLException {
        System.out.print("Enter your Card number: ");
        int cardNumber = sc.nextInt();
        Statement stat = cn.createStatement();
        String sql = "SELECT card_number FROM user";
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            int card = rs.getInt("card_number");
            if (card == cardNumber) {
                processAfterValidCard(cn, sc);
            } else {
                System.out.println("Invalid Card Number!");
            }
        } else {
            System.out.println("No records found!");
        }
    }

    private static void processAfterValidCard(Connection cn, Scanner sc) throws SQLException {
        Statement stat = cn.createStatement();
        String sql = "SELECT pin_number FROM user";
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            int pin = rs.getInt("pin_number");
            System.out.print("Enter your PIN Number: ");
            int enteredPIN = sc.nextInt();

            if (enteredPIN == pin) {
                displayMenu(cn, sc);
            } else {
                System.out.println("Invalid PIN!");
            }
        } else {
            System.out.println("No records found!");
        }
    }

    private static void displayMenu(Connection cn, Scanner sc) throws SQLException {
        Statement stat = cn.createStatement();
        String sql = "SELECT name FROM user";
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            String name = rs.getString("name");
            System.out.println(" *** Welcome to Development Bank ATM " + name + " ********");
        } else {
            System.out.println("No records found!");
        }

        try {
            while (true) {
                System.out.println("1. Balance Enquiry");
                System.out.println("2. Statement");
                System.out.println("3. Withdraw");
                System.out.println("4. Change PIN");
                System.out.println("5. Exit");

                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        displayBalance(cn, sc);
                        break;
                    case 2:
                        // miniStatement();
                        break;
                    case 3:
                        cashOut(cn, sc);
                        break;
                    case 4:
                        changePIN(cn, sc);
                        break;
                    case 5:
                        exit();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    private static void displayBalance(Connection cn, Scanner sc) throws SQLException {
        Statement stat = cn.createStatement();
        String sql = "SELECT balance FROM user";
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            System.out.println("Your account balance is: " + balance);
        } else {
            System.out.println("No records found!");
        }
    }

    private static void cashOut(Connection cn, Scanner sc) throws SQLException {
        System.out.println("Enter the amount you want to withdraw: ");
        double withdraw = sc.nextDouble();

        if (withdraw % 500 == 0) {
            Statement stat = cn.createStatement();
            String sql = "SELECT balance FROM user";
            ResultSet rs = stat.executeQuery(sql);

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Your account balance is: " + balance);

                if (balance >= withdraw) {
                    balance = balance - withdraw;
                    String query = "UPDATE record SET withdraw=" + withdraw;
                    stat.executeUpdate(query);
                    System.out.println("Withdrawal Amount: " + withdraw);

                    String qy = "UPDATE record SET balance=" + balance;
                    stat.executeUpdate(qy);
                    System.out.println("Your remaining balance is: " + balance);
                } else {
                    System.out.println("Insufficient Funds!");
                }
            } else {
                System.out.println("No records found!");
            }
        } else {
            System.out.println("Withdrawal amount should be 500 or multiple of it.");
        }
    }


			   public static void main(String[] args) {
        try (Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pin", "username", "password");
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter card number: ");
            int accNum = scanner.nextInt();

            System.out.print("Enter current PIN: ");
            int currentPIN = scanner.nextInt();

            System.out.print("Enter new PIN: ");
            int newPIN = scanner.nextInt();

            if (validateAndChangePIN(cn, accNum, currentPIN, newPIN)) {
                System.out.println("PIN changed successfully.");
            } else {
                System.out.println("Invalid account number or PIN.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean validateAndChangePIN(Connection cn, int accNum, int currentPIN, int newPIN) throws SQLException {
        String query = "UPDATE accounts SET pin = ? WHERE account_number = ? AND pin = ?";
        try (PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setInt(1, newPIN);
            statement.setInt(2, accNum);
            statement.setInt(3, currentPIN);
            return statement.executeUpdate() > 0;
        }
    }
}





/*private static void changePIN(Connection cn, Scanner sc) throws SQLException {
        Statement stat = cn.createStatement();
        String sqlQuery = "SELECT * FROM pin_number";
		ResultSet resultSet = statement.executeQuery(sqlQuery);


	private static void changePIN(Connection cn, Scanner sc) throws SQLException {
    Statement stat = cn.createStatement();
    String sqlQuery = "SELECT * FROM pin_number";
    ResultSet resultSet = stat.executeQuery(sqlQuery);
	
	if (resultSet.next()) {
		int PIN = resultSet.getInt("pin_number");
	}


       /* if (rs.next()) {
            int PIN = rs.getInt("pin_number");

            System.out.print("Enter your previous PIN: ");
            int current_PIN = sc.nextInt();

            if (current_PIN == PIN) {
                System.out.print("Enter your new PIN: ");
                int new_PIN = sc.nextInt();

                System.out.print("Confirm your new PIN: ");
                int confirm_PIN = sc.nextInt();

                if (new_PIN == confirm_PIN) {
                    PIN = confirm_PIN;
                    String qls = "UPDATE user SET pin_number=" + confirm_PIN;
                    stat.executeUpdate(qls);
                    System.out.println("Your new PIN is: " + new_PIN);
                } else {
                    System.out.println("PIN confirmation failed. Please try again.");
                }
            } else {
                System.out.println("Incorrect PIN! PIN change failed.");
            }
        } else {
            System.out.println("No records found!");
        }
    }
	
* */




    private static void exit() {
        System.out.println(" Thank You For using our ATM. ");
        System.exit(0);
    }

    private static void displayUserDetail(Connection cn, Scanner sc) {
        
        System.out.println("");
        try {
            Statement stat = cn.createStatement();
            String sql = "SELECT * FROM `user`";
            ResultSet rs = stat.executeQuery(sql);
            
            System.out.printf("| %-5s | %-10s | %-5s | %-10s |\n", "Name", "card_number", "pin_number", "balance");
            System.out.println("...............................................");

            while (rs.next()) {
                String name = rs.getString("name");
                int card = rs.getInt("card_number");
                int pin = rs.getInt("pin_number");
                double balance = rs.getDouble("balance");
                System.out.printf("| %-5s | %-10s | %-5s | %-10s |\n", name, card, pin, balance);
            }
            System.out.println("....................................................");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayInsert(Connection cn) {
        try {
            Statement stat = cn.createStatement();
            String sql = "INSERT into crud(name) values('ram')";
            stat.executeUpdate(sql);
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
