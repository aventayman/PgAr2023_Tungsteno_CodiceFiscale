package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

public class Persona {
    private final String nome, cognome, sesso, comune, dataNascita;
    private CodiceFiscale codiceFiscale;
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
        while (running) {
            switch (xmlr.getEventType()) {
                case (XMLEvent.START_ELEMENT) -> {
                    String tag = xmlr.getLocalName();
                    xmlr.next();
                    String testo = xmlr.getText();
                    switch (tag) {
                        case "nome" -> nome = testo;
                        case "cognome" -> cognome = testo;
                        case "sesso" -> sesso = testo;
                        case "data_nascita" -> dataNascita = testo;
                        case "comune_nascita" -> comune = testo;
                    }
                }
                case (XMLEvent.END_ELEMENT) -> {
                    if (xmlr.getLocalName().equals("persona"))
                        running = false;

                    xmlr.next();
                }
            }

            if (xmlr.hasNext())
                xmlr.next();
        }

        //Creazione del nuovo oggetto persona e del relativo codice fiscale
        Persona persona = new Persona(nome, cognome, sesso, comune, dataNascita);
        CodiceFiscale.creaCodice(persona);

        //Aggiunta del nuovo oggetto Persona nella lista di persone
        persone.add(persona);
    }


    public static void creaPopolazione (List<Persona> popolazione, XMLStreamReader xmlrPersone) throws XMLStreamException {
        xmlrPersone.next();

        final int NUMERO_PERSONE = Integer.parseInt(xmlrPersone.getAttributeValue(0));
        for (int i = 0; i < NUMERO_PERSONE; i++)
            Persona.aggiungiPersona(xmlrPersone, popolazione);
    }

    /**
     * Modifica lo stato di presente della persona
     * Se il codice fiscale della persona è presente nella lista inserita, allora presente diventa true
     * @param listaCodici
     * @param popolazione
     */
    public static void isPresente (List<CodiceFiscale> listaCodici, List<Persona> popolazione){
        for (CodiceFiscale codice: listaCodici){
            for (Persona persona: popolazione){
                if(persona.getCodiceFiscale().equals(codice)){
                    persona.presente = true;
                }
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
