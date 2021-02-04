package albumirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/** Luokka albumeille
 * Albumin tiedot , tunnusnumeron ylläpito, albumin tiedot merkkijonosta ja albumin tiedot merkkijonoksi
 * @author Alexander Södergård
 * @version 19.2.2019
 *
 */
public class Albumi implements Cloneable, Tietue {
    
    private int tunnusNro;
    private static int seuraavaNro = 1;
    
    private String  albumi          = "";
    private String  artisti         = "";
    private int     julkaisuvuosi   = 0;
    private String  levyYhtio       = "";
    private String  genre           = "";
    private String  formaatti       = "";
    private String  kunto           = "";
    private double  hinta           = 0.0;

    
    /**
     * Antaa albumille seuraavan rekisterinumeron.
     * @return albumin uusi tunnusNro
     * @example
     * <pre name="test">
     *   Albumi II = new Albumi();
     *   II.getTunnusNro() === 0;
     *   II.rekisteroi();
     *   Albumi PakoLaine = new Albumi();
     *   PakoLaine.rekisteroi();
     *   int n1 = II.getTunnusNro();
     *   int n2 = PakoLaine.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if (tunnusNro != 0) return tunnusNro;
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    /**
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 9;
    }
    
    
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Palauttaa k:tta albumin kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "Albumi";
        case 2: return "Artisti";
        case 3: return "Julkaisuvuosi";
        case 4: return "Levy-yhtiö";
        case 5: return "Genre";
        case 6: return "Formaatti";
        case 7: return "Kunto";
        case 8: return "Hinta";
        default: return "";
        }
    }
    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    /** Palautetaan albumin nimi
     * @return nimi
     */
    public String getNimi() {
        return albumi;
    }
    
    /**
     * @param s albumille laitettava nimi
     * @return virheilmoitus, null jos ok
     */
    public String setNimi(String s) {
        albumi = s;
        return null;
    }
    
    /**
     * @return artistin nimi
     */
    public String getArtisti() {
        return artisti;
    }
    
    /**
     * @param s artistin nimi
     * @return virheilmoitus, null jos ok
     */
    public String setArtisti(String s) {
        artisti = s;
        return null;
    }
    
    /**
     * @return albumin julkaisuvuosi
     */
    public String getJvuosi() {
        return ""+ julkaisuvuosi;
    }
    
    /**
     * @param s julkaisuvuosi
     * @return null jos julkaisuvuodessa ei virhettä, muutoin virheteksti
     */
    public String setJvuosi(String s) {
        if (!s.matches("[0-9]*")) return "Vuosiluku voi sisältää vain numeroita!" ;
        julkaisuvuosi = (int)Double.parseDouble(s);
        return null;
    }
    
    /**
     * @return levy-yhtiön nimi
     */
    public String getYhtio() {
        return levyYhtio ;
    }
    
    /** Asetetaan levy-yhtiön nimi
     * @param s levy-yhtiön nimi
     * @return null
     */
    public String setYhtio(String s) {
        levyYhtio = s;
        return null;
    }
    
    /**
     * @return albumin genre
     */
    public String getGenre() {
        return genre ;
    }
    
    /** asetetaan genre
     * @param s genre
     * @return null
     */
    public String setGenre(String s) {
        genre = s;
        return null;
    }
    
    /**
     * @return albumin formaatti
     */
    public String getFormaatti() {
        return formaatti ;
    }
    
    /** Asetetaan formaatti
     * @param s formaatti
     * @return null
     */
    public String setFormaatti(String s) {
        formaatti = s;
        return null;
    }
    
    /** 
     * @return albumin kunto
     */
    public String getKunto() {
        return kunto ;
    }
    
    /**
     * @param s asetetaan albumin kunto
     * @return null
     */
    public String setKunto(String s) {
        kunto = s;
        return null;
    }
    
    /**
     * @return hinta
     */
    public String getHinta() {
        return hinta +"" ;
    }
    
    /**
     * @param s asetetaan albumin hinta
     * @return null jos hinta oikessa muodossa, muutoin virheteksti
     */
    public String setHinta(String s) {
        if (!s.matches("[0-9]*[.][0-9]*") && !s.matches("[0-9]+")) return "Esitä hinta pelkästään numeroina!";
        hinta = Double.parseDouble(s);
        return null;
    }
    
    /**
     * Tulostetaan albumin tiedot
     * @param out tietovirta, johon tulostus tapahtuu
     */
    public void tulosta(PrintStream out) {
        
        out.println(String.format("%03d", this.tunnusNro) + "\n" + artisti + ": "
                + albumi);
        out.println("Julkaisuvuosi: " + julkaisuvuosi + " \nLevy-yhtiö: " 
                + levyYhtio + " \nGenre:" + genre);
        out.println("Formaatti: " + formaatti +
                "\nKunto:" + kunto +
                "\nHinta: " + hinta + "\n");
    }
    
    
    /**
     * Tulostetaan albumin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Testimetodi jolla täytetään albumintiedot
     */
    public void taytaTiedot() {
    String apuArtisti = kanta.HintaTarkistus.arvoArtisti();
    taytaTiedot(apuArtisti);
    }
    
    
    /**
     * Taytttaa albumin tiedot automaattisesti /TESTIMETODI
     * @param apuNimi arvottu nimi
     */
    public void taytaTiedot(String apuNimi) {
        
       artisti         = "Kingston Wall"+ kanta.HintaTarkistus.rand(8,999);
       albumi          = "II" ;
       julkaisuvuosi   = 2015;
       levyYhtio       = apuNimi; //"Svart Records"; 
       genre           = "Rock";
       formaatti       = "LP";
       kunto           = "VG+";
       hinta           = 60.0 ; 
    }
    

    
    /**
     * Selvitää albumin tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta albumin tiedot otetaan
     *
     * @example
     * <pre name="test">
     *   Albumi albumi = new Albumi();
     *   albumi.parse("   3  |  Asa   | Via Karelia");
     *   albumi.getTunnusNro() === 3;
     *   albumi.toString().startsWith("3|Asa|Via Karelia|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   albumi.rekisteroi();
     *   int n = albumi.getTunnusNro();
     *   albumi.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   albumi.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        artisti = Mjonot.erota(sb, '|', artisti);
        albumi = Mjonot.erota(sb, '|', albumi);
        julkaisuvuosi = Mjonot.erota(sb, '|', julkaisuvuosi);
        levyYhtio = Mjonot.erota(sb, '|', levyYhtio);
        genre = Mjonot.erota(sb, '|', genre);
        formaatti = Mjonot.erota(sb, '|', formaatti);
        kunto = Mjonot.erota(sb, '|', kunto);
        hinta = Mjonot.erota(sb, '|', hinta);
    }
    
    
    /**
     * Palauttaa albumin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return albumi tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Albumi albumi = new Albumi();
     *   albumi.parse("   2  |  Kingston Wall   | 2015");
     *   albumi.toString().startsWith("2|Kingston Wall|2015|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre> 
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                artisti + "|" +
                albumi + "|" +
                julkaisuvuosi + "|" +
                levyYhtio + "|" +
                genre + "|" +
                formaatti + "|" +
                kunto + "|" +
                hinta + "|";
    }
    
    
    /**
     *  Palauttaa albumin tunnusnro:n
     * @return tunnusnro.
     */
    public int getTunnusNro() {
        return tunnusNro;   
    }
    
    /**
     * Tehdään identtinen klooni albumista
     * @return Object kloonattu albumi
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Albumi albumi = new Albumi();
     *   albumi.parse("   2  | II |  Kingston Wall ");
     *   Albumi kopio = albumi.clone();
     *   kopio.toString() === albumi.toString();
     *   albumi.parse("  2  |  II  |  Kingston Wall    ");
     *   kopio.toString().equals(albumi.toString()) === true;
     * </pre>
     */
    @Override
    public Albumi clone() throws CloneNotSupportedException {
        Albumi uusi = (Albumi) super.clone();
        return uusi;
    }
    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test"> 
     *   Albumi albumi = new Albumi();
     *   albumi.aseta(1,"Artisti") === null;
     *   albumi.aseta(2,"Albumi") === null
     *   albumi.aseta(8,"kymmynen") === "Esitä hinta pelkästään numeroina!";
     *   albumi.aseta(8,"10.10") === null
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            artisti = tjono;
            return null;
        case 2:;
            albumi = tjono;
            return null;
        case 3:
            if (!jono.matches("[0-9]*")) return "Vuosiluku voi sisältää vain numeroita!" ;
            julkaisuvuosi = Integer.parseInt(tjono);
            return null;
        case 4:
            levyYhtio = tjono;
            return null;
        case 5:
            genre = tjono;
            return null;
        case 6:
            formaatti = tjono;
            return null;
        case 7:
            kunto = tjono;
            return null;
        case 8:
            if (!jono.matches("[0-9]*[.][0-9]*") && !jono.matches("[0-9]+")) return "Esitä hinta pelkästään numeroina!";
            hinta = Double.parseDouble(tjono);
            return null;
        default:
            return "";
        }
    }
    
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + artisti;
        case 2: return "" + albumi;
        case 3: return "" + julkaisuvuosi;
        case 4: return "" + levyYhtio;
        case 5: return "" + genre;
        case 6: return "" + formaatti;
        case 7: return "" + kunto;
        case 8: return "" + hinta;
        default: return "";
        }
    }
    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        
        Albumi II = new Albumi ();
        Albumi PakoLaine = new Albumi ();
        Albumi kolmas = new Albumi ();
        
        II.rekisteroi();
        PakoLaine.rekisteroi();
        kolmas.rekisteroi();

        II.tulosta(System.out);
        PakoLaine.tulosta(System.out);
        kolmas.tulosta(System.out);
        
        II.taytaTiedot();
        PakoLaine.taytaTiedot();
        kolmas.taytaTiedot();
        
        II.tulosta(System.out);
        PakoLaine.tulosta(System.out);
        kolmas.tulosta(System.out);
    }

}
