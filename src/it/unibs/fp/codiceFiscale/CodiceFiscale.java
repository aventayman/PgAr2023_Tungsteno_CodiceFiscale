package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.*;


public class CodiceFiscale {

    public static Map<String, String> comuni = new HashMap<>();

    /**
     * Metodo che aggiunge i metodi presi dal file XML in una Hashmap
     * @param xmlr oggetto XMLStreamReader per decodificare il file XML
     * @throws XMLStreamException
     */
    private static void aggiungiComune(XMLStreamReader xmlr) throws XMLStreamException {
        String codice, nome;
        codice = nome = null;

        xmlr.next();
        switch (xmlr.getLocalName()) {
            case ("nome") -> nome = xmlr.getText().toUpperCase();
            case ("codice") -> codice = xmlr.getText().toUpperCase();
        }
        comuni.put(nome, codice);
        xmlr.next();
    }

    /**
     * Creo la mappa che associa ai comuni i codici identificativi relativi
     * @param xmlr oggetto XMLStreamReader per decodificare il file dei comuni
     * @throws XMLStreamException
     */
    public static void creaMappaComuni(XMLStreamReader xmlr) throws XMLStreamException {
        final int NUMERO_COMUNI = Integer.parseInt(xmlr.getAttributeValue(XMLStreamReader.START_DOCUMENT));
        xmlr.next();
        for (int i=0; i < NUMERO_COMUNI; i++){
            aggiungiComune(xmlr);
        }
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa all'anno di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa all'anno di nascita
     * @param data data di nascita della persona
     */
    private static void aggiungiAnnoNascita(StringBuilder codice, String data) {
        String anno = data.substring(2, 4);
        codice.append(anno);
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al mese di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al mese di nascita
     * @param data data di nascita della persona
     */
    private static void aggiungiMeseNascita(StringBuilder codice, String data) {
        String mese = data.substring(5, 7);

        String letteraMese = switch (mese) {
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
            case "12" -> "T";
            //In caso il mese sia sbagliato inseriamo una lettera di controllo
            default -> "Z";
        };
        codice.append(letteraMese);
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al giorno di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al giorno di nascita
     * @param persona persona a cui è assegnato il codice fiscale
     */
    private static void aggiungiGiornoNascita(StringBuilder codice, Persona persona) {
        String giorno = persona.getDataNascita().substring(persona.getDataNascita().length() - 2);
        if (persona.getSesso().equals("M"))
            codice.append(giorno);
        else {
            int n = Integer.parseInt(giorno);
            n += 40;
            codice.append(n);
        }
    }

    /**
     * Metodo che aggiunge al codice inserito la parte di codice fiscale relativa al comune di nascita della persona
     * @param codice codice a cui aggiungere la parte relativa al comune di nascita
     * @param comune comune di residenza della persona
     */
    private static void aggiungiCodiceComune(StringBuilder codice, String comune){
        comune = comune.toUpperCase();
        String codiceComune = comuni.get(comune);
        codice.append(codiceComune);
    }

    private static void aggiungiNome(StringBuilder codiceFiscale, String nome) {
        nome = nome.toUpperCase();

        StringBuilder nomeConsonanti = new StringBuilder();
        StringBuilder nomeVocali = new StringBuilder();

        List<Character> vocali = new ArrayList<>(Arrays.asList('A', 'E', 'I', 'O', 'U'));

        char [] lettere = nome.toCharArray();

        for (char lettera : lettere) {
            if (vocali.contains(lettera))
                nomeVocali.append(lettera);
            else if (lettera >= 'A' && lettera <= 'Z')
                nomeConsonanti.append(lettera);
        }

        StringBuilder tempCodice = new StringBuilder();

        //Aggiungo al codice le consonanti del nome
        tempCodice.append(nomeConsonanti);

        //Se le consonanti non sono sufficienti aggiungo le vocali
        if (tempCodice.length() < 3)
            tempCodice.append(nomeVocali);

        //Se le vocali non sono sufficienti aggiungo X
        if (tempCodice.length() < 3)
            for (int i = 0; i < 3 - tempCodice.length(); i++)
                tempCodice.append("X");

        codiceFiscale.append(tempCodice.substring(0, 3));
    }

    public static void creaCodice(Persona persona) {
        StringBuilder codiceFiscale = new StringBuilder();

        //Aggiunta dei caratteri del cognome nel codice fiscale
        aggiungiNome(codiceFiscale, persona.getCognome());

        //Aggiunta dei caratteri del nome nel codice fiscale
        aggiungiNome(codiceFiscale, persona.getNome());

        //Aggiunta dei caratteri dell'anno di nascita
        aggiungiAnnoNascita(codiceFiscale, persona.getDataNascita());

        //Aggiunta dei caratteri del mese di nascita
        aggiungiMeseNascita(codiceFiscale, persona.getDataNascita());

        //Aggiunta dei caratteri del giorno di nascita
        aggiungiGiornoNascita(codiceFiscale, persona);

        //Aggiunta dei caratteri del comune di nascita
        aggiungiCodiceComune(codiceFiscale, persona.getComune());

        //TO-DO: Aggiunta del carattere di controllo

        //Assegnamento del codice fiscale alla persona
        persona.setCodiceFiscale(codiceFiscale.toString());
    }
}
