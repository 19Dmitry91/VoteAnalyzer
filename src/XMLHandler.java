import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.SimpleDateFormat;

public abstract class XMLHandler extends DefaultHandler {
    protected static final SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    protected static final SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Override
    public abstract void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

    @Override
    public abstract void endElement(String uri, String localName, String qName) throws SAXException;

    public abstract void printDuplicatedVoters();

    public abstract void printStationWorkTimes();
}