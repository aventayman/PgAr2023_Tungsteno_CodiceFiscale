package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;


public class CodiceFiscale {

    HashMap<String, String> comuni = new HashMap<>();
    XMLStreamReader xmlr;

    //Variabile final che memorizza il numero di comuni presenti nel documento XML
    private final int NUMERO_COMUNI = Integer.parseInt(xmlr.getAttributeValue(XMLStreamReader.START_DOCUMENT));


    /**
     * Metodo che aggiunge i metodi presi dal file XML in una Hashmap
     * @param xmlr oggetto XMLStreamReader per decodificare il file XML
     * @throws XMLStreamException
     */
    public void aggiungiComune(XMLStreamReader xmlr) throws XMLStreamException {
        String codice, nome;

        xmlr.next();
        nome = xmlr.getText();
        xmlr.next();
        codice = xmlr.getText();
        xmlr.next();
        xmlr.next();

        comuni.put(nome, codice);
    }

    /**
     * Creo la mappa che associa ai comuni i codici identificativi relativi
     * @throws XMLStreamException
     */
    public void creaMappaComuni() throws XMLStreamException {
        for (int i=0; i < NUMERO_COMUNI; i++){
            xmlr.next();
            aggiungiComune(xmlr);
        }
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa all'anno di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa all'anno di nascita
     * @param persona persona a cui è assegnato il codice fiscale
     */
    public void addAnnoNascita(StringBuilder codice, Persona persona) {

        String data = persona.getDataNascita();
        StringBuilder anno = new StringBuilder();
        anno.append(data.charAt(2));
        anno.append(data.charAt(3));
        codice.append(anno);
    }


    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al mese di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al mese di nascita
     * @param persona persona a cui è assegnato il codice fiscale
     */
    public void addMeseNascita(StringBuilder codice, Persona persona ) {

        String data = persona.getDataNascita();
        StringBuilder mese = new StringBuilder();
        mese.append(data.charAt(5));
        mese.append(data.charAt(6));

        String meseString = mese.toString();
        String letteraMese = switch (meseString) {

            case "01" -> "A";
            case "02" -> "B";
            case "03" -> "C";
            case "04" -> "D";
            case "05" -> "E";
            case "06" -> "H";
            case "07" -> "L";
            case "08" -> "M";
            case "09" -> "P";
            case "10" -> "R";
            case "11" -> "S";

            default -> "T";

        };

        codice.append(letteraMese);

    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al giorno di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al giorno di nascita
     * @param persona persona a cui è assegnato il codice fiscale
     */
    public void addGiornoNascita(StringBuilder codice, Persona persona) {

        String giorno = persona.getDataNascita().substring(persona.getDataNascita().length() - 2);
        if (persona.getSesso().equals("M") ){
            codice.append(giorno);
        }
        else {
            int n = Integer.parseInt(giorno);
            n += 40;
            codice.append(n);
        }
    }

    /**
     * Metodo che restituisce il codice relativo al comune di nascita della persona inserita
     * @param persona persona di cui è richiesto il codice del comune di nascita
     * @return codice alfanumerico del comune di nascita
     */
    public String codiceComune (Persona persona) {
        String comune = persona.getComune();
        return comuni.get(persona.getComune());
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al comune di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al comune di nascita
     * @param persona persona a cui è assegnato il codice fiscale
     */
    public void addCodiceComune(StringBuilder codice, Persona persona){
        codice.append(codiceComune(persona));
    }
}
