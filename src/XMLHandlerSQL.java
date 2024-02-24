import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class XMLHandlerSQL extends XMLHandler {
    private Voter voter;
    private int batchSize = 0;
    private static final int MAX_BATCH_SIZE = 1000;
    private List<Voter> votersBatch = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter")) {
                voter = new Voter(
                        attributes.getValue("name"),
                        Voter.birthDayFormat.parse(attributes.getValue("birthDay"))
                );
                votersBatch.add(voter);
                batchSize++;

                if (batchSize == MAX_BATCH_SIZE) {
                    DBConnection.multiInsertVoters(votersBatch);
                    votersBatch.clear();
                    batchSize = 0;
                }
            }
        } catch (ParseException | SQLException e) {
            throw new SAXException(e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("voter")) {
            voter = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        if (!votersBatch.isEmpty()) {
            try {
                DBConnection.multiInsertVoters(votersBatch);
            } catch (SQLException e) {
                throw new SAXException(e);
            }
        }
    }

    @Override
    public void printDuplicatedVoters() {
        try {
            DBConnection.printVoterCounts();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void printStationWorkTimes() {
    }
}
