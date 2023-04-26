package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLStreamWriter;
import java.util.List;

public class Parsing {

    /**
     * Metodo per la generazione del file xml con in dati forniti
     * @param popolazione lista di persone di cui generare il file xml
     * @param codici lista di codici per i confronti
     * @param xmlw XMLStreamwriter per la generazione del file
     */
    public static void Output(List<Persona> popolazione, List<CodiceFiscale> codici, XMLStreamWriter xmlw) {
        try { // blocco try per raccogliere eccezioni
            xmlw.writeStartElement("output");//Scrittura del tag dell'xml
            xmlw.writeStartElement("Persone");
            xmlw.writeAttribute("numero = ", Integer.toString(popolazione.size()));


            //Variabile per riportare correttamente gli id necessari
            int id = 0;

            for (Persona persona: popolazione) {
                //Scrittura di tutti gli elementi richiesti
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
                //Se l'attributo presente è true allora restituisce il suo codice fiscale
                if(persona.isPresente())
                    xmlw.writeCharacters(persona.getCodiceFiscale().toString());
                //altrimenti riporta la dicitura "ASSENTE"
                else
                    xmlw.writeCharacters("ASSENTE");
                xmlw.writeEndElement();
                xmlw.writeEndElement();

                //Aumento l'indice per la prossima persona
                id++;
            }
            //Chiusura del tag persone
            xmlw.writeEndElement();

            xmlw.writeStartElement("codici"); //apertura del tag codici
            xmlw.writeStartElement("invalidi");

            //Variabile per contare il numero di codici invalidi
            int invalidi = 0;
            for (CodiceFiscale codice: codici) {
                if(!codice.isValido())
                    invalidi++;
            }

            xmlw.writeAttribute("numero = ", Integer.toString(invalidi));

            for (CodiceFiscale codice: codici) {
                //Se il codice non è valido viene stampato
                if (!codice.isValido()){
                    xmlw.writeStartElement("codice");
                    xmlw.writeCharacters(codice.toString());
                    xmlw.writeEndElement();
                }
            }
            xmlw.writeEndElement();//chiusura del tag invalidi

            xmlw.writeStartElement("spaiati");//apertura del tag spaiati

            //variabile per contare il numero di codici spaiati
            int spaiati = 0;
            for (CodiceFiscale codice: codici) {
                if(codice.isSpaiato())
                    spaiati++;
            }

            xmlw.writeAttribute("numero = ", Integer.toString(spaiati));

            for (CodiceFiscale codice: codici) {
                //Se il codice è spaiato e valido allora viene stampato
                if (codice.isSpaiato() && codice.isValido()){
                    xmlw.writeStartElement("codice");
                    xmlw.writeCharacters(codice.toString());
                    xmlw.writeEndElement();
                }
            }
            xmlw.writeEndElement();//chiusura del tag spaiati
            xmlw.writeEndElement();//chiusura del tag codici

            xmlw.writeEndDocument();//chiusura di output

            xmlw.flush(); // svuota il buffer
            xmlw.close(); // chiusura del documento e delle risorse impiegate
        } catch (Exception e) { // se c’è un errore viene eseguita questa parte
            System.out.println("Errore nella scrittura");
        }
    }
}
