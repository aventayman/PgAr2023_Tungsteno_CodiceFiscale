package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;


public class CodiceFiscale {

    HashMap<String, String> comuni = new HashMap<>();
    XMLStreamReader xmlr;
    private final int NUMERO_COMUNI = Integer.parseInt(xmlr.getAttributeValue(XMLStreamReader.START_DOCUMENT));

    public void aggiungiComune(XMLStreamReader xmlr) throws XMLStreamException
    {
        String codice, nome;

        xmlr.next();
        nome = xmlr.getText();
        xmlr.next();
        codice = xmlr.getText();
        xmlr.next();
        xmlr.next();

        comuni.put(nome, codice);
    }

    public void creaMappaComuni() throws XMLStreamException
    {
        for (int i=0; i < NUMERO_COMUNI; i++){
            xmlr.next();
            aggiungiComune(xmlr);
        }
    }
}
