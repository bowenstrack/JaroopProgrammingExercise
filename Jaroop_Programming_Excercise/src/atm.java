import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class atm {

    /**
     * run method:
     * The only method the main method runs.
     * Runs a scanner and a do-while loop in order to grab the user input in real time.
     * Displays messages to help the user give input as well.
     */
    public static void run(){
        boolean running = true;
        boolean isValid = true;
        Scanner kbd = new Scanner(System.in);
        File file = new File("log.html");
        HTMLLog htmlLog = new HTMLLog(file);
        
        do
            {
                System.out.println("Please enter in a command (Deposit, Withdraw, Balance, Exit)");
                String command = kbd.next();
                String userInput = null;
                double depositAmount = 0;
                double withdrawalAmount = 0;
                double sumAmount = 0;

                /**
                 * Deposit command:
                 * Runs only if the user give the input 'deposit' (ignores case)
                 * uses the helper method checkNumb in order to make sure the amount the user gives is valid.
                 * if it is not, it is prompted to give another amount.
                 * It then attempt to add the amount to the log.html file using the logDeposit method (located in the HTMLLog class).
                 * If it cannot, throws an IOException
                 * @throws IOException This method throws an IOException just in case the file is un-writable.
                 */
                if (command.equalsIgnoreCase("deposit")) {
                    System.out.println("Please enter an amount to deposit:");
                    while(isValid) {
                        userInput = kbd.next();
                        if(!checkNumb(userInput)) {
                            System.out.println("Invalid amount, please enter a positive dollar amount with only 2 decimal points. ");
                            System.out.println("Please input another value:");
                        }
                        else
                            isValid = false;
                    }
                    isValid = true;
                    depositAmount = Double.parseDouble(userInput);
                    try{
                        htmlLog.logWithdrawal(depositAmount);
                    }
                    catch(IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }
                /**
                 * Withdraw command:
                 * Runs only if the user give the input 'withdraw' (ignores case)
                 * uses the helper method checkNumb in order to make sure the amount the user gives is valid.
                 * if it is not, it is prompted to give another amount.
                 * It then attempt to add the amount to the log.html file using the logWithdrawal method (located in the HTMLLog class).
                 * If it cannot, throws an IOException
                 * @throws IOException This method throws an IOException just in case the file is un-writable.
                 */
                else if(command.equalsIgnoreCase("withdraw")) {
                    System.out.println("Please enter an amount to withdraw:");
                    while(isValid) {
                        userInput = kbd.next();
                        if(!checkNumb(userInput)) {
                            System.out.println("Invalid amount, please enter a positive dollar amount with only 2 decimal points. ");
                            System.out.println("Please input another value:");
                        }
                        else
                            isValid = false;
                    }
                    isValid = true;
                    withdrawalAmount = Double.parseDouble(userInput);
                    try{
                        htmlLog.logWithdrawal(withdrawalAmount);
                    }
                    catch(IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }
                /**
                 * Balance command:
                 * Runs only if the user gives the input 'balance' (ignores case)
                 * Then attempts to run the method sumTransactions (pulled from the HTMLLog class) in order to sum all the transactions in the log.html file.
                 * If it cannot, throws an IOException.
                 * If it is successful displays the current balance of the user after adding/subtracting (deposit/withdraw) all values in the html table.
                 * @throws IOException This method throws an IOException just in case the file is un-readable.
                 */
                else if(command.equalsIgnoreCase("balance")){
                    try{
                        sumAmount = htmlLog.sumTransactions();
                    }
                    catch(IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                    System.out.println("The current balance is: $" + sumAmount);
                }
                /**
                 * exit input:
                 * Exits the program by breaking the loop.
                 * Sets the boolean running value to false, knocking the user out of the loop.
                 */
                else if(command.equalsIgnoreCase("exit")){
                    running = false;
                }
                /**
                 * Checks to make sure the only input is either deposit, withdraw, balance, or exit.
                 */
                else
                    System.out.println("Invalid command, please enter Deposit, Withdraw, Balance or Exit.");
            }
        while(running);
    }

    /**
     * checkNumb method:
     * Used to make sure that the input the user gives after declaring to make a deposit or withdrawal is correct.
     * First, checks to make sure the input is a double.
     * Then checks the number of decimal places the double has.
     * Lastly, makes sure the double is positive.
     * If any of these fail, then the method returns false, and the while loop located under the deposit command and withdraw command sections will display an error.
     * @param userInput The string that the user inputs, tested if possible to convert to a double, then converted mid method.
     * @return returns either true or false, whether or not the check fails or passes.
     */
    private static boolean checkNumb(String userInput){
        try {
            Double.parseDouble(userInput);
        } catch (NumberFormatException e) {
            return false;
        }
        double amount = Double.parseDouble(userInput);
        if (BigDecimal.valueOf(amount).scale() > 2)
            return false;
        else if(amount < 0)
            return false;
        else
            return true;
    }
}
