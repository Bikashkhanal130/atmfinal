import java.sql.*;
import java.util.*;
public class ATMSystem{
	public static void main(String[] args)throws SQLException{
		DatabaseConnection dc = new DatabaseConnection();
		Connection cn = dc.getConnection();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println(" ******** Hello Welcome to Hamro Development Bank ATM ********");
		System.out.println("1. Login");
		System.out.println("2. User details");
		
		System.out.println("Enter your choice: ");
		int choice = Integer.parseInt(sc.nextLine());
	
		switch(choice){
			case 1:
			displayLogin(cn, sc);
			break;
			
			case 2:
			displayUserDetail(cn, sc);
			break;
		}
		
		 // displayInsert(cn);
		
	}
	
	
	 private static int currentCardNumber;
	 private static void displayLogin(Connection cn, Scanner sc)throws SQLException{
		//System.out.println("hello");
		
		System.out.println("Enter your Card number: ");
		int intValue = sc.nextInt();
		Statement stat = cn.createStatement();
		String sql = "SELECT card_number FROM user";
		ResultSet rs = stat.executeQuery(sql);
		//int card = rs.getInt("card_number");
		 if (rs.next()) {
        int card = rs.getInt("card_number");
        if(card == intValue){
			
			processAfterValidCard(cn, sc);
		}else{
			System.out.println("Invalid Card Number!");
		}
		} else {
        System.out.println("No records found!");
    }	
	}

	
	private static void processAfterValidCard(Connection cn, Scanner sc)throws SQLException{

		Statement stat = cn.createStatement();
		String sql= "SELECT pin_number FROM user";
		ResultSet rs = stat.executeQuery(sql);
		//int pin = -1;
		if (rs.next()) {
		int pin = rs.getInt("pin_number");
					System.out.print("Enter your PIN Number: ");
					int intValue =  sc.nextInt();
			
					if(intValue == pin){
						displayMenu(cn,sc);
					}else{
					//attempts[i] = intValue;
					//System.out.println("Invalid PIN!");
					//System.out.println("Remaining attempts: " + (maxAttempts - i - 1));
					System.out.println("Invalid PIN!");
				}
			 //}
		}else{
		System.out.println("No records found!");
		}
}
		/*int maxAttempts = 3;
			int[] attempts = new int[maxAttempts];
			//for(int i=0; i<maxAttempts; i++ ){
				try{
					System.out.print("Enter your PIN Number: ");
					int intValue =  sc.nextInt();
			
					if(intValue == pin){
						displayMenu(cn,sc);
					}else{
					//attempts[i] = intValue;
					//System.out.println("Invalid PIN!");
					//System.out.println("Remaining attempts: " + (maxAttempts - i - 1));
				}
			 }catch(NumberFormatException e){
				 //e.printStackTrace();
				 System.out.println("Invalid input. Please put Valid PIN Number.");
			 }
			//}
			System.out.println("**** Your card has been blocked. Please contact your respective Bank. ****");
	}*/
	
	private static void displayMenu(Connection cn, Scanner sc)throws SQLException{
		Statement stat = cn.createStatement();
		String sql = "SELECT name FROM user";
		ResultSet rs = stat.executeQuery(sql);
		if (rs.next()){
		String name = rs.getString("name");
		System.out.println(" ******** Welcome to our ATM " +name+ " ********");
		}else{
		System.out.println("No records found!");
		}
		//System.out.println("***** Welcome to Hamro Development ATM Machine! "+Name+" ******");
        System.out.println("1. Balance Enquiry");
        System.out.println("2. Mini Statement");
		System.out.println("3. Cash Out");
		System.out.println("4. Change PIN number");
        System.out.println("5. Exit");
		try{
		int i = 1;
		while(i>0){
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1:
                displayBalance(cn,sc);
                break;
			
			case 2:
    // miniStatement(cn, card, sc);
    break;

				
			case 3:
				cashOut(cn,sc);
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
		displayMenu(cn,sc);
		}
		} catch(NumberFormatException e) {
			System.out.println("Invalid input! Please enter a valid number.");
        }catch(InputMismatchException e){
			System.out.println("Invalid input! Please enter a valid number.");
		}
		}

	private static void displayBalance(Connection cn, Scanner sc)throws SQLException {
		Statement stat = cn.createStatement();
		String sql = "SELECT balance FROM user";
		ResultSet rs = stat.executeQuery(sql);
		if (rs.next()){
		int balance = rs.getInt("balance");
		System.out.println("Your account balance is: " +balance);
		}else{
		System.out.println("No records found!");
		}
		
    }
	
	
	//Withdrawal Code 
	
	private static void cashOut( Connection cn, Scanner sc)throws  SQLException{
		System.out.println("Enter the amount you want to withdrawal: ");
		double withdraw = sc.nextDouble();
		if(withdraw%500 == 0){
			Statement stat = cn.createStatement();
			String sql = "SELECT balance FROM user";
			ResultSet rs = stat.executeQuery(sql);
			if (rs.next()){
				double balance = rs.getDouble("balance");
				System.out.println("Your account balance is: " +balance);
				if(balance >= withdraw){
				balance =balance - withdraw;
				
				String query = "UPDATE user SET balance="+withdraw+"";
				stat.executeUpdate(query);
				System.out.println("Withdrawal Amount: " +withdraw);
				
				String qy = "UPDATE user SET balance="+balance+"";
				stat.executeUpdate(qy);
				System.out.println("Your remaining balance is: " +balance);
				}else{
				System.out.println("Insufficient Funds!");
			}
			}else{
				System.out.println("No records found!");
			}
				}else{
			System.out.println("Withdraw amount should be 500 or Multiple of it.");
		}
			
			
			
				/*LocalDateTime withdrawDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = withdrawDateTime.format(formatter);
				
				System.out.println("Withdrawal Date and Time: " + formattedDateTime);
				System.out.println("Please collect your money"); */	
    }
    
	
	//ATM  pin 
	
	private static void changePIN(Connection cn, Scanner sc)throws SQLException{
		Statement stat = cn.createStatement();
		String sql = "SELECT pin_number FROM user";
		ResultSet rs = stat.executeQuery(sql);
		if (rs.next()){
		int PIN = rs.getInt("pin_number");
		
		System.out.println("Enter your previous PIN: ");
		int currentPIN = sc.nextInt();
		
		if(currentPIN == PIN){
		System.out.println("Enter your new PIN: ");
		int newPIN = sc.nextInt();
		
		System.out.println("Confirm your new PIN: ");
        int changePIN = sc.nextInt();
		
	
			if(newPIN == changePIN){
				PIN = changePIN;
				String qls = "UPDATE user SET pin_number="+changePIN+"";
				stat.executeUpdate(qls);
				System.out.println("***** PIN changed successfully! *****");
				System.out.println("Your new PIN is: "+newPIN);
			}else {
                System.out.println("PIN confirmation failed. Please try Again.");
            }
		}else{
		System.out.println("Incorrect PIN! PIN change failed.");
	}
		}else{
		System.out.println("No records found!");
		}
	}
	
	
	private static void exit(){
		System.out.println("* Thank You! For using our ATM. Goodbye");
		System.exit(0);
	}


	
	private static void displayUserDetail(Connection cn, Scanner sc){
		System.out.println("");
		System.out.println("");
		try {
            Statement stat = cn.createStatement();
            String sql = "SELECT * FROM `user`";
			ResultSet rs = stat.executeQuery(sql);
			System.out.println("------------------------------------------------");
			System.out.printf("| %-10s | %-10s | %-10s | %-10s |\n", "Name", "card_number", "pin_number", "balance");
			System.out.println("------------------------------------------------");
            
			while(rs.next()){
            String name = rs.getString("name"); 
            int card = rs.getInt("card_number"); 
			int pin = rs.getInt("pin_number");
			double balance = rs.getDouble("balance");
			System.out.printf("| %-10s | %-10s | %-10s | %-10s |\n", name, card, pin, balance);
            }
            System.out.println("------------------------------------------------");
			}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	// mini statement code 
	/*
	private static void miniStatement(Connection cn, int card, Scanner sc)throws SQLException{
			Statement stat = cn.createStatement();
			String sql = "SELECT FROM user WHERE card_number= '"+card+"'";
			String select = "SELECT deposit, withdraw, balance, date, description FROM transactions WHERE card_number ='"+card+"' LIMIT 10";
			ResultSet rs = stat.executeQuery(sql);
			if (rs.next()){
			String name = rs.getString("name");
			System.out.println(" ******** Transaction Details of  Mr." +name+ " ********");
			ResultSet transactionRs = stat.executeQuery(select);
			boolean isFirstRow = true; 
			while (transactionRs.next()) {
			double balance = transactionRs.getDouble("balance");
			double withdraw = transactionRs.getDouble("withdraw");
			double deposit = transactionRs.getDouble("deposit");
			String date = transactionRs.getString("date");
			String description = transactionRs.getString("description");
				if (isFirstRow) {
                System.out.println("***** Card Number ='"+card+"' ***** Balance = '"+balance+"' ***** Withdrawal amount = '"+withdraw+"' ***** Deposited amount = '"+deposit+"' ***** Date = '"+date+"', ***** Description = '"+description+"'");
                isFirstRow = false;
            } else {
                System.out.println("------------------------------------------------------------------------------------------------------");
                System.out.println("***** Remaining Balance = '"+balance+"' ***** Withdrawal amount = '"+withdraw+"'  ***** Deposited amount = '"+deposit+"' ***** Date = '"+date+"',***** Description = '"+description+"'");
            }
        }
        transactionRs.close();
    } else {
        System.out.println("No records found!");
    }
}
	
	*/
	 private static void miniStatement(Connection cn, int card, Scanner sc) throws SQLException {
    Statement stat = cn.createStatement();
    String select = "SELECT deposit, withdraw, balance, date, description FROM transactions WHERE card_number = '"+card+"' ORDER BY date DESC LIMIT 10";
    ResultSet transactionRs = stat.executeQuery(select);
    
    System.out.println(" ******** Mini Statement ********");
    System.out.println("|    Date    | Description  | Deposit  | Withdraw | Balance  |");
    System.out.println("----------------------------------");
    
    while (transactionRs.next()) {
        String date = transactionRs.getString("date");
        String description = transactionRs.getString("description");
        double deposit = transactionRs.getDouble("deposit");
        double withdraw = transactionRs.getDouble("withdraw");
        double balance = transactionRs.getDouble("balance");
        
        System.out.printf("| %-10s | %-12s | %-8.2f | %-8.2f | %-8.2f |\n", date, description, deposit, withdraw, balance);
    }
    
    transactionRs.close();
}

}