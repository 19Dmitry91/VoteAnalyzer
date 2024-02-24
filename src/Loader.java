import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

public class Loader {

    private static final String FILE_NAME = "YOUR_PATH";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println("Start time of work: " + LocalTime.now().toString());
        long startUp = System.currentTimeMillis();

        try {
            parseXMLHandler(FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long startEnd = System.currentTimeMillis();
        System.out.println("Parsing duration: " + getTime(startUp, startEnd) + ".");

        try {
            DBConnection.printVoterCounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + " milliseconds");

        try {
            DBConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void parseXMLHandler(String fileName) throws ParserConfigurationException, SAXException, IOException, SQLException {
        SAXParserFactory factoryToMySQL = SAXParserFactory.newInstance();
        SAXParser parserToMySQL = factoryToMySQL.newSAXParser();
        parserToMySQL.parse(new File(fileName), new XMLHandlerSQL());
    }

    public static double getTime(long start, long end) {
        return (double) (end - start) / 1000 / 60;
    }
}