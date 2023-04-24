package it.unibs.fp.codiceFiscale;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;


public class CodiceFiscale {

    HashMap<String, String> comuni = new HashMap<>();
    XMLStreamReader xmlr;
    private final int NUMERO_COMUNI = Integer.parseInt(xmlr.getAttributeValue(XMLStreamReader.START_DOCUMENT));

    public void aggiungiComune(XMLStreamReader xmlr) throws XMLStreamException
    {
        String codice, nome;

        xmlr.next();
        nome = xmlr.getText();
        xmlr.next();
        codice = xmlr.getText();
        xmlr.next();
        xmlr.next();

        comuni.put(nome, codice);
    }

    public void creaMappaComuni() throws XMLStreamException
    {
        for (int i=0; i < NUMERO_COMUNI; i++){
            xmlr.next();
            aggiungiComune(xmlr);
        }
    }

    public void annoNascita(StringBuilder codice, Persona persona) {

        String data = persona.getDataNascita();
        StringBuilder anno = new StringBuilder();
        anno.append(data.charAt(2));
        anno.append(data.charAt(3));
        codice.append(anno);
    }

    public void meseNascita(StringBuilder codice, Persona persona ) {

        String data = persona.getDataNascita();
        StringBuilder mese = new StringBuilder();
        mese.append(data.charAt(5));
        mese.append(data.charAt(6));

        String meseString = mese.toString();
        String letteraMese = switch (meseString) {

            case "01" -> "A";
            case "02" -> "B";
            case "03" -> "C";
            case "04" -> "D";
            case "05" -> "E";
            case "06" -> "H";
            case "07" -> "L";
            case "08" -> "M";
            case "09" -> "P";
            case "10" -> "R";
            case "11" -> "S";

            default -> "T";

        };

        codice.append(letteraMese);

    }

    public void giornoNascita(StringBuilder codice, Persona persona) {

        String giorno = persona.getDataNascita().substring(persona.getDataNascita().length() - 2);
        if (persona.getSesso().equals("M") ){
            codice.append(giorno);
        }
        else {
            int n = Integer.parseInt(giorno);
            n += 40;
            codice.append(n);
        }
    }
}
