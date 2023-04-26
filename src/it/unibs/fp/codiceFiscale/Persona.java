package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.List;

public class Persona {
    private final String nome, cognome, sesso, comune, dataNascita;
    private CodiceFiscale codiceFiscale;

    //Attributo che stabilisce se il codice della persona è presente nella lista del file fornito
    private boolean presente = false;

    public Persona(String nome, String cognome, String sesso, String comune, String data)
    {
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.comune = comune;
        this.dataNascita = data;
    }


    /**
     * Metodo per aggiungere un nuovo oggetto persona all'interno della lista di persone
     * @param xmlr oggetto XMLStreamReader per decodificare il file XML
     * @param persone la lista a cui aggiungere la nuova persona
     * @throws XMLStreamException
     */
    public static void aggiungiPersona(XMLStreamReader xmlr, List<Persona> persone) throws XMLStreamException
    {
        //Creazione di variabili temporanee da inserire alla fine in un nuovo oggetto persona
        String nome, cognome, sesso, comune, dataNascita;
        nome = cognome = sesso = comune = dataNascita = null;

        boolean running = true;
        //While che permette di posizionarsi nelle caselle di testo automaticamente
        while (running) {
            switch (xmlr.getEventType()) {
                case (XMLEvent.START_ELEMENT) -> {
                    String tag = xmlr.getLocalName();
                    xmlr.next();
                    String testo = xmlr.getText();
                    //Se si tratta di uno START_ELEMENT controlla tutti i possibili casi e assegna il caso al testo corrispettivo
                    switch (tag) {
                        case "nome" -> nome = testo;
                        case "cognome" -> cognome = testo;
                        case "sesso" -> sesso = testo;
                        case "data_nascita" -> dataNascita = testo;
                        case "comune_nascita" -> comune = testo;
                    }
                }

                //Se si tratta di un END_ELEMENT, termina il controllo
                case (XMLEvent.END_ELEMENT) -> {
                    if (xmlr.getLocalName().equals("persona"))
                        running = false;
                    xmlr.next();
                }
            }

            //In ogni caso, se è presente un successivo, vi ci si posiziona
            if (xmlr.hasNext())
                xmlr.next();
        }

        //Creazione del nuovo oggetto persona e del relativo codice fiscale
        Persona persona = new Persona(nome, cognome, sesso, comune, dataNascita);
        CodiceFiscale.creaCodice(persona);

        //Aggiunta del nuovo oggetto Persona nella lista di persone
        persone.add(persona);
    }


    /**
     * Metodo per creare una popolazione dai dati forninti dall'xml
     * @param popolazione lista vuota da riempire con la popolazione
     * @param xmlrPersone xmlr relativo alle persone
     * @throws XMLStreamException
     */
    public static void creaPopolazione (List<Persona> popolazione, XMLStreamReader xmlrPersone) throws XMLStreamException {
        xmlrPersone.next();

        //Il numero di persone è dato dall'inidice iniziale
        final int NUMERO_PERSONE = Integer.parseInt(xmlrPersone.getAttributeValue(0));

        //Aggiunge tutte le persone alla lista, anche con il relativo codice fiscale
        for (int i = 0; i < NUMERO_PERSONE; i++)
            Persona.aggiungiPersona(xmlrPersone, popolazione);
    }

    /**
     * Modifica lo stato di presente della persona
     * Se il codice fiscale della persona è presente nella lista inserita, allora presente diventa true
     * @param listaCodici lista dei codici per il confronto
     * @param popolazione popolazione a cui modificare la varianile presente
     */
    public static void controllaPresenza (List<CodiceFiscale> listaCodici, List<Persona> popolazione){
        for (CodiceFiscale codice: listaCodici){
            for (Persona persona: popolazione){
                //Se i codici sono uguali, la variabile presente diventa true
                if(persona.getCodiceFiscale().equals(codice))
                    persona.setPresente(true);
            }
        }
    }


    //Getter e Setter per la classe Persona

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSesso() {
        return sesso;
    }

    public String getComune() {
        return comune;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public CodiceFiscale getCodiceFiscale() {
        return codiceFiscale;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setCodiceFiscale(CodiceFiscale codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", sesso='" + sesso + '\'' +
                ", comune='" + comune + '\'' +
                ", dataNascita='" + dataNascita + '\'' +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                '}';
    }
}
