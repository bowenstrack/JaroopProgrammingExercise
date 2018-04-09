import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class HTMLLog {

    private File file = new File("log.html");

    /**
     * Constructor so I can pull the file whenever I call these methods.
     * @param fileName Name of the file I want to use with the methods below.
     */
    public HTMLLog(File fileName){
        fileName = file;
    }

    /**
     * logDeposit method:
     * the logDeposit method goes about adding the new deposit to the log.html file.
     * It uses the JSoup library to quickly parse through the html file and find the end of the transaction table, where we want to add a new entry.
     * Finds the body of the table, then appends a new row and new cell to the bottom of the table, along with the amount parameter.
     * @param amount The amount parameter allows the method to take a double, which it then uses that value to add to the table.
     *               This way I can insert the value the user gives on the command line with ease.
     * @throws IOException This method throws an IOException just in case the file is un-writable.
     */
    public void logDeposit(double amount) throws IOException{
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements dom = doc.children();

        dom.select("#transactions tbody").append("<tr><td>" + amount + "</td></tr>");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(dom.toString());
            bw.close();
        }
        catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /**
     * logWithdrawal method:
     * Works exactly the same as the logDeposit method, except it converts the amount to a negative number before adding it to the table.
     * @param amount The amount parameter allows the method to take a double, which it then uses that value to add to the table.
     *               This way I can insert the value the user gives on the command line with ease.
     * @throws IOException This method throws an IOException just in case the file is un-writable.
     */
    public void logWithdrawal(double amount) throws IOException{
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements dom = doc.children();
        amount = amount*-1;

        dom.select("#transactions tbody").append("<tr><td>" + amount + "</td></tr>");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(dom.toString()); //toString will give all the elements as a big string
            bw.close();  //Close to apply the changes
        }
        catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /**
     * sumTransactions method:
     * Parses the log.html file exactly like the logDeposit and logWithdrawal methods do.
     * Searches and finds the first row of the table, then using this location, goes through the rows one at a time.
     * Each time it iterates through the for loop, it selects the cell inside of the row and then adds/subtracts (deposit/withdraw) the contents of the cell to a variable called sum.
     * It parses the contents of the table into a double, since the user can only give a double as an input.
     * @throws IOException This method throws an IOException just in case the file is un-writable.
     * @return Returns the calculated sum, to be used to print in the atm class.
     */
    public double sumTransactions() throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements dom = doc.children();
        Elements table = dom.select("#transactions tbody");
        Elements rows = table.select("tr");
        double sum = 0;

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            sum += Double.parseDouble(cols.get(0).text());
        }
        sum = round(sum, 2);
        return sum;
    }

    /**
     * round method:
     * Helper function I added to round the balance up to 2 decimal places.
     * I found that sometimes I would get a very long repeating decimal after adding, so I added this to keep everything consistent.
     * @param value The number that will be rounded.
     * @param places Number of decimal places I want to round to.
     * @return Returns the rounded number once completed.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
