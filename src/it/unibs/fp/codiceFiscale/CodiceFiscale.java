package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.*;


public class CodiceFiscale {
    private final String codice;
    private boolean valido = true;
    private boolean spaiato = true;

    public CodiceFiscale(String codice) {
        this.codice = codice;
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
            //che in caso di verifica darà un codice invalido
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
        String codiceComune = Comune.comuni.get(comune);
        codice.append(codiceComune);
    }

    /**
     * Metodo per aggiungere al codice la parte relativa al nome della persona
     * @param codiceFiscale codice da modificare
     * @param nome nome della persona
     */
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

    private static char calcoloCarattereControllo(String codiceFiscale) {
        StringBuilder codicePari = new StringBuilder();
        StringBuilder codiceDispari = new StringBuilder();

        for (int i = 0; i < 15; i++) {
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

        return (char) ('A' + resto);
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
        codiceFiscale.append(calcoloCarattereControllo(codiceFiscale.toString()));

        //Assegnamento del codice fiscale alla persona
        persona.setCodiceFiscale(new CodiceFiscale(codiceFiscale.toString()));
    }

    /**
     * Metodo per verificare la validità del codice fiscale inserito
     * @param codiceFiscale di cui si vuole fare una verifica
     */
    public static void codiceValido(CodiceFiscale codiceFiscale) {
        char [] codice = codiceFiscale.codice.toCharArray();
        int [] posizioneLettere = {0, 1, 2, 3, 4, 5, 8, 11, 15};
        int [] posizioneCifre = {6, 7, 9, 10, 12, 13, 14};
        char [] mesi31 = {'A', 'C', 'E', 'L', 'M', 'R', 'T'};
        char[] mesi30 = {'S', 'D', 'H', 'P'};

        //Se il codice contiene un numero di caratteri diverso da 16 il codice è invalido
        if (codice.length != 16) {
            codiceFiscale.setValido(false);
            return;
        }

        //Controllo le posizioni di ogni carattere del codice fiscale
        for (int i = 0; i < 16; i++) {
            //Se si tratta di una lettera
            if (codice[i] >= 'A' && codice[i] <= 'Z') {
                for (int posizioneCifra : posizioneCifre)
                    //Se la lettera si trova in posizione di una cifra il codice è invalido
                    if (i == posizioneCifra) {
                        codiceFiscale.setValido(false);
                        return;
                    }
            }
            //Se si tratta di una cifra
            else if (codice[i] >= '0' && codice[i] <= '9') {
                for (int posizioneLettera : posizioneLettere)
                    //Se la cifra si trova in posizione di una lettera il codice è invalido
                    if (i == posizioneLettera) {
                        codiceFiscale.setValido(false);
                        return;
                    }
            }
            //Se si tratta di un qualsiasi altro carattere il codice è invalido
            else {
                codiceFiscale.setValido(false);
                return;
            }
        }

        //Controllo dell'esistenza del mese e della correttezza dei giorni
        int giornoNascita = Integer.parseInt(codiceFiscale.codice.substring(9, 11));
        int annoNascita = Integer.parseInt(codiceFiscale.codice.substring(6, 8));
        char meseNascita = codice[8];

        boolean meseEsistente = false;

        //Controllo se si tratta di un mese da 30 giorni
        for (char mese : mesi30) {
            if (meseNascita == mese) {
                if (giornoNascita < 1 || (giornoNascita > 30 && giornoNascita < 41) || giornoNascita > 70) {
                    codiceFiscale.setValido(false);
                    return;
                }
                meseEsistente = true;
            }
        }

        //Controllo se si tratta di un mese da 31 giorni
        for (char mese : mesi31) {
            if (meseNascita == mese) {
                if (giornoNascita < 1 || (giornoNascita > 31 && giornoNascita < 41) || giornoNascita > 71) {
                    codiceFiscale.setValido(false);
                    return;
                }
                meseEsistente = true;
            }
        }

        //Controllo se si tratta di un anno bisestile
        boolean bisestile;
        if (annoNascita % 4 == 0) {
            if (annoNascita % 100 == 0)
                bisestile = annoNascita % 400 != 0;
            else
                bisestile = true;
        }
        else
            bisestile = false;

        //Controllo se si tratta di febbraio
        if (meseNascita == 'B') {
            if (bisestile) {
                if (giornoNascita < 1 || (giornoNascita > 29 && giornoNascita < 41) || giornoNascita > 69) {
                    codiceFiscale.setValido(false);
                    return;
                }
            }
            else {
                if (giornoNascita < 1 || (giornoNascita > 28 && giornoNascita < 41) || giornoNascita > 68) {
                    codiceFiscale.setValido(false);
                    return;
                }
            }
            meseEsistente = true;
        }

        //Se non è stato trovato il mese allora il codice è invalido
        if (!meseEsistente) {
            codiceFiscale.setValido(false);
            return;
        }

        //Controllo del carattere di controllo
        char carattereControllo = calcoloCarattereControllo(codiceFiscale.codice);

        if (codice[15] != carattereControllo) {
            codiceFiscale.setValido(false);
            return;
        }

        //Se ha superato tutti i controlli ritorna true
        codiceFiscale.setValido(true);
    }

    /**
     * Metodo che inserisce i codici fiscali del file nella lista data come attributo
     * @param xmlrCodice oggetto riferito al file di codici
     * @param listaCodici lista da riempire con i codici fiscali
     * @throws XMLStreamException
     */
    public static void creaListaCodici (XMLStreamReader xmlrCodice, List<CodiceFiscale> listaCodici)throws XMLStreamException
    {
        xmlrCodice.next();
        final int NUMERO_CODICI = Integer.parseInt(xmlrCodice.getAttributeValue(0));
        int counter = 0;

        while (counter < NUMERO_CODICI) {
           boolean running = true;
           while (running) {
               switch (xmlrCodice.getEventType()) {
                   case (XMLEvent.START_ELEMENT) -> {
                       String tag = xmlrCodice.getLocalName();
                       if (tag.equals("codice")) {
                           xmlrCodice.next();
                           CodiceFiscale codice = new CodiceFiscale(xmlrCodice.getText());
                           listaCodici.add(codice);
                           counter++;
                       }

                   }
                   case (XMLEvent.END_ELEMENT) -> {
                       if (xmlrCodice.getLocalName().equals("codice"))
                           running = false;

                       xmlrCodice.next();
                   }
               }

               if (xmlrCodice.hasNext())
                   xmlrCodice.next();
           }
       }

    }

    /**
     * Metodo che modifica l'attributo valido della lista di codici secondo i criteri stabiliti
     * @param listaCodici lista di cui si vuol modificare lo stato di validità
     */
    public static void codiciInvalidi (List<CodiceFiscale> listaCodici) {
        for(CodiceFiscale codice: listaCodici){
            codiceValido(codice);
        }
    }

    /**
     * Metodo che modifica l'attributo spaiato di ogni codice nella lista inserita
     * Se il codice è presente nel file e appartiene a una persona della popolazione, allora spaiato diventa false
     * @param listaCodici di codici di cui modificare l'attributo spaiato
     * @param popolazione i cui codici fiscali vanno confrontati con la lista inserita
     */
    public static void codiciSpaiati (List<CodiceFiscale> listaCodici, List<Persona> popolazione){
        for (CodiceFiscale codice: listaCodici){
            for (Persona persona: popolazione){
                if (codice.equals(persona.getCodiceFiscale())) {
                    codice.setSpaiato(false);
                }
            }
        }
    }

    public boolean isValido() {
        return valido;
    }

    public boolean isSpaiato() {
        return spaiato;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public void setSpaiato(boolean spaiato) {
        this.spaiato = spaiato;
    }

    @Override
    public String toString() {
        return codice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodiceFiscale that = (CodiceFiscale) o;
        return Objects.equals(codice, that.codice);
    }
}
