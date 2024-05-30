import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLfile {
    private ArrayList<Nod> listOfNodes=new ArrayList<>();
    private ArrayList<Arc> listOfArches=new ArrayList<>();
    public ArrayList<Nod> getListOfNodes() {
        return listOfNodes;
    }
    public ArrayList<Arc> getListOfArches() {
        return listOfArches;
    }

    public ReadXMLfile(){
        try {
            File readMap = new File("D:\\Facultate\\Anul II\\AG\\Teme\\Tema 6\\Tema6\\src\\hartaLuxembourg.xml");

            //aceste linii initializeaza un parser SAX,
            // care este o unealta pentru citirea si procesarea fișierelor XML
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            //creeaza o instanta a clasei UserHandler, care este definita pentru a
            //gestiona evenimentele de analiza XML (cum ar fi intalnirea unui nou element).
            UserHandler userhandler = new UserHandler();
            saxParser.parse(readMap, userhandler);


//dupa finalizarea analizei, informatiile extrase (nodurile si arcele)
// sunt accesate prin metodele getNodes() si getArches() ale userhandler
            this.listOfNodes=userhandler.getNodes();
            this.listOfArches=userhandler.getArches();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

//aceasta clasa permite conversia eficienta a datelor XML in structuri de date pentru noduri si arce
class UserHandler extends DefaultHandler {
    private ArrayList<Nod> nodes =new ArrayList<>();
    private ArrayList<Arc> arches =new ArrayList<>();

    public ArrayList<Arc> getArches() {
        return arches;
    }

    public ArrayList<Nod> getNodes() {
        return nodes;
    }


    //aceasta metoda este apelata automat de parser-ul SAX de fiecare data
    // cand acesta intalneste un nou element in fisierul XML.
    @Override
    public void startElement(
            // parametrii conțin informatii despre elementul curent.
            //qName este numele elementului(sau tag-ului) (cum ar fi "node" sau "arc"),
            // iar attributes contine atributele elementului.
            String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("node")) {

            String id = attributes.getValue("id");
            String longitude = attributes.getValue("longitude");
            String latitude = attributes.getValue("latitude");

            Nod n = new Nod(Integer.parseInt(id), Integer.parseInt(latitude), Integer.parseInt(longitude));
            nodes.add(n);
        } else if (qName.equalsIgnoreCase("arc")) {


            String from = attributes.getValue("from");
            String to = attributes.getValue("to");
            String length = attributes.getValue("length");

            Arc arc = new Arc(Integer.parseInt(from), Integer.parseInt(to), Integer.parseInt(length));
            arches.add(arc);
        }

    }
}
