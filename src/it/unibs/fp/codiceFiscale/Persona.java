package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

public class Persona {
    private String nome, cognome, sesso, comune, dataNascita, codiceFiscale;

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
        xmlr.next();
        nome = xmlr.getText();
        xmlr.next();
        xmlr.next();
        cognome = xmlr.getText();
        xmlr.next();
        xmlr.next();
        sesso = xmlr.getText();
        xmlr.next();
        xmlr.next();
        comune = xmlr.getText();
        xmlr.next();
        xmlr.next();
        dataNascita = xmlr.getText();
        xmlr.next();
        xmlr.next();

        //Creazione del nuovo oggetto persona e del relativo codice fiscale
        Persona persona = new Persona(nome, cognome, sesso, comune, dataNascita);
        //odiceFiscale.creaCodice(persona);

        //Aggiunta del nuovo oggetto Persona nella lista di persone
        persone.add(persona);
    }


    //Getter e Setter per la classe Persona

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

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
}
