package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        /*
        Persona ayman = new Persona("Ayman", "Marpicati", "M", "Brescia", "2003-03-22");
        Persona fede = new Persona("Federico", "Serafini", "M", "Ghedi", "2003-08-11");
        Persona mirko = new Persona("Mirko", "Tedoldi", "M", "Gardone Val Trompia", "2003-08-24");
        */

        XMLInputFactory xmlif = null;
        XMLStreamReader xmlrComuni = null;
        XMLStreamReader xmlrPersone = null;
        XMLStreamReader xmlrCodice = null;
        String pathComuni = "./TestFiles/Comuni.xml";
        String pathPersone = "./TestFiles/InputPersone.xml";
        String pathCodici = "./TestFiles/CodiciFiscali.xml";

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

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlrCodice = xmlif.createXMLStreamReader(pathCodici, new FileInputStream(pathCodici));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        /*
        CodiceFiscale.creaCodice(ayman);
        CodiceFiscale.creaCodice(mirko);
        CodiceFiscale.creaCodice(fede);

        System.out.println(ayman.getCodiceFiscale());
        System.out.println(mirko.getCodiceFiscale());
        System.out.println(fede.getCodiceFiscale());
        */

       // Parsing.inizializzazione(pathCodici, xmlrCodice, xmlif);

        assert xmlrComuni != null;
        Comune.creaMappaComuni(xmlrComuni);
        List<Persona> popolazione = new ArrayList<>();
        List<CodiceFiscale> codiciFiscali = new ArrayList<>();

        assert xmlrPersone != null;
        Persona.creaPopolazione(popolazione, xmlrPersone);
        assert xmlrCodice != null;
        CodiceFiscale.creaListaCodici(xmlrCodice, codiciFiscali);

        CodiceFiscale.codiciInvalidi(codiciFiscali);
        CodiceFiscale.codiciSpaiati(codiciFiscali, popolazione);
        Persona.isPresente(codiciFiscali, popolazione);



        XMLOutputFactory xmlof = null;
        XMLStreamWriter xmlw = null;

        String pathOutput = "./TestFiles/CodiciPersone.xml";

        try {
            xmlof = XMLOutputFactory.newInstance();
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(pathOutput), "utf-8");
            xmlw.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }

        Parsing.Output(popolazione, codiciFiscali, xmlw);
    }
}
