package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.Map;

public class Comune {

    public static Map<String, String> comuni = new HashMap<>();

    /**
     * Metodo che aggiunge i metodi presi dal file XML in una Hashmap
     * @param xmlr oggetto XMLStreamReader per decodificare il file XML
     * @throws XMLStreamException
     */
    private static void aggiungiComune(XMLStreamReader xmlr) throws XMLStreamException {
        String codice, nome;
        codice = nome = null;

        boolean running = true;
        while (running) {
            switch (xmlr.getEventType()) {
                //Se si tratta di un tag di apertura
                case (XMLEvent.START_ELEMENT) -> {
                    switch (xmlr.getLocalName()) {
                        case ("nome") -> {
                            //Avanzare di un posto per salvare il nome del paese
                            xmlr.next();
                            nome = xmlr.getText().toUpperCase();
                        }
                        case ("codice") -> {
                            //Avanzare di un posto per salvare il codice del paese
                            xmlr.next();
                            codice = xmlr.getText().toUpperCase();
                        }
                    }
                }
                //Se si tratta di un tag di chiusura
                case (XMLEvent.END_ELEMENT) -> {
                    //Se è il closing-tag del comune allora termina di controllare per lo specifico comune
                    if (xmlr.getLocalName().equals("comune"))
                        running = false;

                    //In ogni caso se è un closing-tag andare al successivo
                    xmlr.next();
                }
            }
            //Questa condizione serve in modo che non dia un errore perché ci si trova alla fine del file
            if (xmlr.hasNext())
                xmlr.next();
        }
        comuni.put(nome, codice);
    }


    /**
     * Creo la mappa che associa ai comuni i codici identificativi relativi
     * @param xmlr oggetto XMLStreamReader per decodificare il file dei comuni
     * @throws XMLStreamException
     */
    public static void creaMappaComuni(XMLStreamReader xmlr) throws XMLStreamException {
        xmlr.next();
        final int NUMERO_COMUNI = Integer.parseInt(xmlr.getAttributeValue(0));
        xmlr.next();
        xmlr.next();
        for (int i = 0; i < NUMERO_COMUNI; i++){
            aggiungiComune(xmlr);
        }
    }
}
