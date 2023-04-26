package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        //Inizializzazione degli XMLStreamreader
        XMLInputFactory xmlif;
        XMLStreamReader xmlrComuni = null;
        XMLStreamReader xmlrPersone = null;
        XMLStreamReader xmlrCodice = null;

        //Path dei file
        String pathComuni = "./TestFiles/Comuni.xml";
        String pathPersone = "./TestFiles/InputPersone.xml";
        String pathCodici = "./TestFiles/CodiciFiscali.xml";


        //Inizializzazione per l'input
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


        //Creo le raccolte degli elementi forniti dai file xml
        Comune.creaMappaComuni(xmlrComuni);
        List<Persona> popolazione = new ArrayList<>();
        List<CodiceFiscale> codiciFiscali = new ArrayList<>();

        Persona.creaPopolazione(popolazione, xmlrPersone);
        CodiceFiscale.creaListaCodici(xmlrCodice, codiciFiscali);

        //Eseguo i controlli necessari cambiando le variabili corrispondenti
        CodiceFiscale.codiciInvalidi(codiciFiscali);
        CodiceFiscale.codiciSpaiati(codiciFiscali, popolazione);
        Persona.controllaPresenza(codiciFiscali, popolazione);

        //Inizializzazione
        XMLOutputFactory xmlof;
        XMLStreamWriter xmlw = null;

        //Percorso del file di output
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
