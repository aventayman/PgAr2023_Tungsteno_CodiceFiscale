package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.util.ArrayList;

public class Parsing {
    XMLStreamReader xmlr;
    ArrayList<Persona> listaPersone;

    //File xmlfile = new File();

    private final int NUMERO_PERSONE = Integer.parseInt(xmlr.getAttributeValue(XMLStreamReader.START_DOCUMENT));
    public void creaListaPersone() throws XMLStreamException {
        for (int i = 0; i < NUMERO_PERSONE; i++) {
            xmlr.next();
            Persona.aggiungiPersona(xmlr, listaPersone);
        }
    }
}
