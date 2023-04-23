package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CodiceFiscale {

    ArrayList<Comune> comuni;
    private final int NUMERO_COMUNI = 8092;
    XMLStreamReader xmlr = null;

    public static void aggiungiComune(XMLStreamReader xmlr, List<Comune> comuni) throws XMLStreamException
    {
        String codice, nome;

        xmlr.next();
        nome = xmlr.getText();
        xmlr.next();
        codice = xmlr.getText();
        xmlr.next();
        xmlr.next();

        Comune comune = new Comune(nome, codice);
        comuni.add(comune);
    }

    public void creaListaComuni() throws XMLStreamException
    {
        for (int i=0; i < NUMERO_COMUNI; i++){
            xmlr.next();
            aggiungiComune(xmlr, comuni);
        }
    }
}
