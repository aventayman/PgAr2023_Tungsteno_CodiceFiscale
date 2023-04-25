package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        Persona ayman = new Persona("Ayman", "Marpicati", "M", "Brescia", "2003-03-22");
        Persona fede = new Persona("Federico", "Serafini", "M", "Ghedi", "2003-08-11");
        Persona mirko = new Persona("Mirko", "Tedoldi", "M", "Gardone Val Trompia", "2003-07-24");

        XMLInputFactory xmlif;
        XMLStreamReader xmlr = null;
        String pathComuni = "./TestFiles/Comuni.xml";
        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader(pathComuni, new FileInputStream(pathComuni));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        CodiceFiscale.creaMappaComuni(xmlr);

        CodiceFiscale.creaCodice(ayman);
        CodiceFiscale.creaCodice(mirko);
        CodiceFiscale.creaCodice(fede);

        System.out.println(ayman.getCodiceFiscale());
        System.out.println(mirko.getCodiceFiscale());
        System.out.println(fede.getCodiceFiscale());
    }
}
