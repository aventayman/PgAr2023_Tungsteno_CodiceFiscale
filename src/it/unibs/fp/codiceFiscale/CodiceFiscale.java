package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
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
            tempCodice.append("X".repeat(Math.max(0, 3 - tempCodice.length())));

        codiceFiscale.append(tempCodice.substring(0, 3));
    }

    private static final Map<Character, Integer> tabellaDispari = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('0', 1),
            new AbstractMap.SimpleEntry<>('1', 0),
            new AbstractMap.SimpleEntry<>('2', 5),
            new AbstractMap.SimpleEntry<>('3', 7),
            new AbstractMap.SimpleEntry<>('4', 9),
            new AbstractMap.SimpleEntry<>('5', 13),
            new AbstractMap.SimpleEntry<>('6', 15),
            new AbstractMap.SimpleEntry<>('7', 17),
            new AbstractMap.SimpleEntry<>('8', 19),
            new AbstractMap.SimpleEntry<>('9', 21),
            new AbstractMap.SimpleEntry<>('A', 1),
            new AbstractMap.SimpleEntry<>('B', 0),
            new AbstractMap.SimpleEntry<>('C', 5),
            new AbstractMap.SimpleEntry<>('D', 7),
            new AbstractMap.SimpleEntry<>('E', 9),
            new AbstractMap.SimpleEntry<>('F', 13),
            new AbstractMap.SimpleEntry<>('G', 15),
            new AbstractMap.SimpleEntry<>('H', 17),
            new AbstractMap.SimpleEntry<>('I', 19),
            new AbstractMap.SimpleEntry<>('J', 21),
            new AbstractMap.SimpleEntry<>('K', 2),
            new AbstractMap.SimpleEntry<>('L', 4),
            new AbstractMap.SimpleEntry<>('M', 18),
            new AbstractMap.SimpleEntry<>('N', 20),
            new AbstractMap.SimpleEntry<>('O', 11),
            new AbstractMap.SimpleEntry<>('P', 3),
            new AbstractMap.SimpleEntry<>('Q', 6),
            new AbstractMap.SimpleEntry<>('R', 8),
            new AbstractMap.SimpleEntry<>('S', 12),
            new AbstractMap.SimpleEntry<>('T', 14),
            new AbstractMap.SimpleEntry<>('U', 16),
            new AbstractMap.SimpleEntry<>('V', 10),
            new AbstractMap.SimpleEntry<>('W', 22),
            new AbstractMap.SimpleEntry<>('X', 25),
            new AbstractMap.SimpleEntry<>('Y', 24),
            new AbstractMap.SimpleEntry<>('Z', 23)
    );

    private static final Map<Character, Integer> tabellaPari = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('0', 0),
            new AbstractMap.SimpleEntry<>('1', 1),
            new AbstractMap.SimpleEntry<>('2', 2),
            new AbstractMap.SimpleEntry<>('3', 3),
            new AbstractMap.SimpleEntry<>('4', 4),
            new AbstractMap.SimpleEntry<>('5', 5),
            new AbstractMap.SimpleEntry<>('6', 6),
            new AbstractMap.SimpleEntry<>('7', 7),
            new AbstractMap.SimpleEntry<>('8', 8),
            new AbstractMap.SimpleEntry<>('9', 9),
            new AbstractMap.SimpleEntry<>('A', 0),
            new AbstractMap.SimpleEntry<>('B', 1),
            new AbstractMap.SimpleEntry<>('C', 2),
            new AbstractMap.SimpleEntry<>('D', 3),
            new AbstractMap.SimpleEntry<>('E', 4),
            new AbstractMap.SimpleEntry<>('F', 5),
            new AbstractMap.SimpleEntry<>('G', 6),
            new AbstractMap.SimpleEntry<>('H', 7),
            new AbstractMap.SimpleEntry<>('I', 8),
            new AbstractMap.SimpleEntry<>('J', 9),
            new AbstractMap.SimpleEntry<>('K', 10),
            new AbstractMap.SimpleEntry<>('L', 11),
            new AbstractMap.SimpleEntry<>('M', 12),
            new AbstractMap.SimpleEntry<>('N', 13),
            new AbstractMap.SimpleEntry<>('O', 14),
            new AbstractMap.SimpleEntry<>('P', 15),
            new AbstractMap.SimpleEntry<>('Q', 16),
            new AbstractMap.SimpleEntry<>('R', 17),
            new AbstractMap.SimpleEntry<>('S', 18),
            new AbstractMap.SimpleEntry<>('T', 19),
            new AbstractMap.SimpleEntry<>('U', 20),
            new AbstractMap.SimpleEntry<>('V', 21),
            new AbstractMap.SimpleEntry<>('W', 22),
            new AbstractMap.SimpleEntry<>('X', 23),
            new AbstractMap.SimpleEntry<>('Y', 24),
            new AbstractMap.SimpleEntry<>('Z', 25)
    );

    private static void aggiungiCarattereControllo(StringBuilder codiceFiscale) {
        StringBuilder codicePari = new StringBuilder();
        StringBuilder codiceDispari = new StringBuilder();

        for (int i = 0; i < codiceFiscale.length(); i++) {
            //Se il carattere è in posizione pari, partendo da uno, aggiungere al codicePari
            if ((i+1) % 2 == 0)
                codicePari.append(codiceFiscale.charAt(i));
            //Se il carattere è in posizione dispari aggiungere a codiceDispari
            else
                codiceDispari.append(codiceFiscale.charAt(i));
        }

        char [] caratteriPari = codicePari.toString().toCharArray();
        char [] caratteriDispari = codiceDispari.toString().toCharArray();

        int somma = 0;

        for (char carattere : caratteriPari) {
            try {
                somma += tabellaPari.get(carattere);
            } catch (Exception ignored) {}
        }


        for (char carattere : caratteriDispari) {
            try {
                somma += tabellaDispari.get(carattere);
            } catch (Exception ignored) {}
        }

        int resto = somma % 26;

        codiceFiscale.append((char)('A' + resto));
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

        //Aggiunta del carattere di controllo
        aggiungiCarattereControllo(codiceFiscale);

        //Assegnamento del codice fiscale alla persona
        persona.setCodiceFiscale(codiceFiscale.toString());
    }
}
