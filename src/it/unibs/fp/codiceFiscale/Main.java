package it.unibs.fp.codiceFiscale;

public class Main {
    public static void main(String[] args) {
        Persona ayman = new Persona("Ayman", "Marpicati", "M", "Bagnolo Mella", "2003-03-22");
        Persona fede = new Persona("Federico", "Serafini", "M", "Ghedi", "2003-08-11");
        Persona mirko = new Persona("Mirko", "Tedoldi", "M", "Gardone Val Trompia", "2003-07-24");
        StringBuilder codice = new StringBuilder();

        CodiceFiscale.creaCodice(ayman);
        CodiceFiscale.creaCodice(mirko);
        CodiceFiscale.creaCodice(fede);

        System.out.println(ayman.getCodiceFiscale());
        System.out.println(mirko.getCodiceFiscale());
        System.out.println(fede.getCodiceFiscale());
    }
}
