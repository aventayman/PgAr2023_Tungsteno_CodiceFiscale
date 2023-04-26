package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Parsing {

    public static void  inizializzazione (String path, XMLStreamReader xmlr, XMLInputFactory xmlif) {
        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader(path, new FileInputStream(path));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }
    }

    public static void Output(List<Persona> popolazione, List<CodiceFiscale> codici, XMLStreamWriter xmlw)
            throws XMLStreamException {
        try { // blocco try per raccogliere eccezioni
            xmlw.writeStartElement("output"); // scrittura del tag radice <programmaArnaldo>
            xmlw.writeStartElement("Persone");
            xmlw.writeAttribute("numero = ", Integer.toString(popolazione.size()));

            int id = 0;

            for (Persona persona: popolazione) {
                xmlw.writeStartElement("persona");
                xmlw.writeAttribute("id = ", Integer.toString(id));
                xmlw.writeStartElement("nome");
                xmlw.writeCharacters(persona.getNome());
                xmlw.writeEndElement();
                xmlw.writeStartElement("cognome");
                xmlw.writeCharacters(persona.getCognome());
                xmlw.writeEndElement();
                xmlw.writeStartElement("sesso");
                xmlw.writeCharacters(persona.getSesso());
                xmlw.writeEndElement();
                xmlw.writeStartElement("Comune_nascita");
                xmlw.writeCharacters(persona.getComune());
                xmlw.writeEndElement();
                xmlw.writeStartElement("Data_Nascita");
                xmlw.writeCharacters(persona.getDataNascita());
                xmlw.writeEndElement();
                xmlw.writeStartElement("Codice_fiscale");
                if(persona.isPresente())
                    xmlw.writeCharacters(persona.getCodiceFiscale().toString());
                else
                    xmlw.writeCharacters("ASSENTE");
                xmlw.writeEndElement();
                xmlw.writeEndElement();

                id++;
            }
            xmlw.writeEndElement();

            xmlw.writeStartElement("codici");
            xmlw.writeStartElement("invalidi");

            int invalidi = 0;
            for (CodiceFiscale codice: codici) {
                if(!codice.isValido())
                    invalidi++;
            }

            xmlw.writeAttribute("numero = ", Integer.toString(invalidi));

            for (CodiceFiscale codice: codici) {
                if (!codice.isValido()){
                    xmlw.writeStartElement("codice");
                    xmlw.writeCharacters(codice.toString());
                    xmlw.writeEndElement();
                }
            }
            xmlw.writeEndElement();

            xmlw.writeStartElement("invalidi");

            int spaiati = 0;
            for (CodiceFiscale codice: codici) {
                if(!codice.isValido())
                    spaiati++;
            }

            xmlw.writeAttribute("numero = ", Integer.toString(spaiati));

            for (CodiceFiscale codice: codici) {
                if (codice.isSpaiato() && codice.isValido()){
                    xmlw.writeStartElement("codice");
                    xmlw.writeCharacters(codice.toString());
                    xmlw.writeEndElement();
                }
            }
            xmlw.writeEndElement();
            xmlw.writeEndElement();

            xmlw.writeEndDocument();

            xmlw.flush(); // svuota il buffer
            xmlw.close(); // chiusura del documento e delle risorse impiegate
        } catch (Exception e) { // se c’è un errore viene eseguita questa parte
            System.out.println("Errore nella scrittura");
        }

    }
}
