package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.util.ArrayList;

public class Parsing {
    XMLStreamReader xmlr = null;
    ArrayList<Persona> listaPersone;

    //File xmlfile = new File();

    private final int NUMERO_PERSONE = 1000;
    public void creaListaPersone() throws XMLStreamException {
        for (int i = 0; i < NUMERO_PERSONE; i++) {
            xmlr.next();
            Persona.aggiungiPersona(xmlr, listaPersone);
        }
    }
}
