package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        Persona ayman = new Persona("Ayman", "Marpicati", "M", "Brescia", "2003-03-22");
        Persona fede = new Persona("Federico", "Serafini", "M", "Ghedi", "2003-08-11");
        Persona mirko = new Persona("Mirko", "Tedoldi", "M", "Gardone Val Trompia", "2003-07-24");

        XMLInputFactory xmlif;
        XMLStreamReader xmlrComuni = null;
        XMLStreamReader xmlrPersone = null;
        String pathComuni = "./TestFiles/Comuni.xml";
        String pathPersone = "./TestFiles/InputPersone.xml";

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlrComuni = xmlif.createXMLStreamReader(pathComuni, new FileInputStream(pathComuni));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlrPersone = xmlif.createXMLStreamReader(pathPersone, new FileInputStream(pathPersone));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        CodiceFiscale.creaMappaComuni(xmlrComuni);

        CodiceFiscale.creaCodice(ayman);
        CodiceFiscale.creaCodice(mirko);
        CodiceFiscale.creaCodice(fede);

        System.out.println(ayman.getCodiceFiscale());
        System.out.println(mirko.getCodiceFiscale());
        System.out.println(fede.getCodiceFiscale());

        List<Persona> popolazione = new ArrayList<>();
        xmlrPersone.next();
        final int NUMERO_PERSONE = Integer.parseInt(xmlrPersone.getAttributeValue(0));
        System.out.println(xmlrPersone.getEventType());
        System.out.println(xmlrPersone.getLocalName());
        for (int i = 0; i < NUMERO_PERSONE; i++)
            Persona.aggiungiPersona(xmlrPersone, popolazione);

        System.out.println(popolazione);
    }
}
