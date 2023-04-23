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

    public static void aggiungiPersona(XMLStreamReader xmlr, List<Persona> persone) throws XMLStreamException {
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

        Persona persona = new Persona(nome, cognome, sesso, comune, dataNascita);
        //odiceFiscale.creaCodice(persona);

        persone.add(persona);
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }
}
