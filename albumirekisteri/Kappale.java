package albumirekisteri;
import static kanta.HintaTarkistus.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/**
 * Luokka kappaleelle
 * Lukeminen merkkijonosta ja merkkijonoksi muuttaminen
 * @author Alexander Södergård
 * @version 8.3.2019
 *
 */
public class Kappale implements Tietue {

    int tunnusNro;
    int albumiNro;
    String nimi;
    String kesto;
    
    private static int seuraavaNro = 1;
    
    

    /** Oletusmuodostaja
     * 
     */
    public Kappale() {
        // ass
    }
    
    
    /** Kappaleen muodostaja
     * @param albuminro albumi johon kappale kuuluu
     */
    public Kappale(int albuminro) {
        this.albumiNro = albuminro;
    }    
    
    /**
     * Selvitää kappaleen tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta kappaleet tiedot otetaan
     * @example
     * <pre name="test">
     *   Kappale kappale = new Kappale();
     *   kappale.parse("  1  |  3  |  We Cannot Move   |  4:38");
     *   kappale.getAlbumiNro() === 3;
     *   kappale.toString()    === "1|3|We Cannot Move|4:38";
     *   
     *   kappale.rekisteroi();
     *   int n = kappale.getTunnusNro();
     *   kappale.parse(""+(n+20));
     *   kappale.rekisteroi();
     *   kappale.getTunnusNro() === n+20+1;
     *   kappale.toString()     === "" + (n+20+1) + "|3|We Cannot Move|4:38";
     * </pre> */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        albumiNro = Mjonot.erota(sb, '|', albumiNro);
        nimi = Mjonot.erota(sb, '|', nimi);
        kesto = Mjonot.erota(sb, '|', kesto);
    }
    
    /** 
     * Apumetodi, jolla täytetään toimivuuden testaamisen vuoksi albumin kappaletietoja
     * @param nro albumin johon kappale kuuluu
     */
    public void taytaTiedot(int nro) {
        this.albumiNro = nro;
        this.nimi = "We Cannot Move";
        this.kesto = "4:" + rand(1,59);   
    }
     
    
    /**
     * Palauttaa kappaleen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return kappaleen tiedot tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Kappale kappale = new Kappale();
     *   kappale.parse("1  | 1 |We Cannot Move   |  4:42 ");
     *   kappale.toString()    === "1|1|We Cannot Move|4:42";
     * </pre>
     */
    @Override
    public String toString() {
        return "" 
    + getTunnusNro() 
    + "|" + albumiNro 
    + "|" + nimi 
    + "|" + kesto;
    }
    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    
    /** Asetetaan kappaleen nimi
     * @param s kappaleen nimi
     */
    public void setNimi(String s) {
    this.nimi = s;
    }
    
    /** Asetetaan kappaleen kesto
     * @param s kappaleen kesto
     */
    public void setKesto(String s) {
    this.kesto = s;
    }
    
    
    /**
     * Tulostetaan kappaleen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(this.nimi + ": " + this.kesto);
    }
    
    
    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
        
    /**
     * Antaa kappaleelle seuraavan rekisterinumeron.
     * @return kappaleen uusi tunnusNro
     * @example
     * <pre name="test">
     *   Kappale eka = new Kappale();
     *   eka.getTunnusNro() === 0;
     *   eka.rekisteroi();
     *   Kappale toka = new Kappale();
     *   toka.rekisteroi();
     *   int n1 = eka.getTunnusNro();
     *   int n2 = toka.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
        
    /**
     * Palautetaan kappaleen oma id
     * @return kappaleen id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    /**
     * Palautetaan mille albumille kappale kuuluu
     * @return albumin id
     */
    public int getAlbumiNro() {
        return albumiNro;
    }
    /**
     * Testiohjelma kappaleelle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kappale har = new Kappale();
        har.taytaTiedot(2);
        har.tulosta(System.out);
    }


    /**
     * @return kappaleen kentien lkm
     */
    @Override
    public int getKenttia() {
        return 4;
    }
    
    /**
     * @return ekan kentan indeksi joka näkyy käyttöliittymässä
     */
    @Override
    public int ekaKentta() {
        return 2;
    }

    
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test"> 
     *   Kappale kap = new Kappale();
     *   kap.parse("   2   |  10  |   Jou  | 4:39  ");
     *   kap.anna(0) === "2";   
     *   kap.anna(1) === "10";   
     *   kap.anna(2) === "Jou";   
     *   kap.anna(3) === "4:39";      
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0: return "" + tunnusNro;
            case 1: return "" + albumiNro;
            case 2: return nimi;
            case 3: return "" + kesto;
            default: return "";
        }
    }
    
    /**
    * @param k minkä kentän kysymys halutaan
    * @return valitun kentän kysymysteksti
    */
   @Override
   public String getKysymys(int k) {
       switch (k) {
           case 0: return "id";
           case 1: return "albumiId";
           case 2: return "nimi";
           case 3: return "kesto";
           default: return "";
       }
   }
   
   /**
    * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
    * palautetaan null, muutoin virheteksti. 
    * @param k minkä kentän sisältö asetetaan
    * @param s asetettava sisältö merkkijonona
    * @return null jos ok, muuten virheteksti
    * @example
    * <pre name="test">
    *   Kappale har = new Kappale();
    *   har.aseta(2,"kissa") === null;
    *   har.aseta(3,"1940")  === null;
    *   har.aseta(4,"04:39") === "Väärä kentän indeksi";
    * </pre>
    */
   @Override
   public String aseta(int k, String s) {
       String st = s.trim();
       StringBuffer sb = new StringBuffer(st);
       switch (k) {
           case 0: setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                   return null;
           case 1: albumiNro = Mjonot.erota(sb, '$', albumiNro);
                   return null;
           case 2: nimi = st;
                   return null;
           case 3: kesto = st;
                   return null;
           default:return "Väärä kentän indeksi";
       }
   }
   
   /**
    * Tehdään identtinen klooni albumista
    * @return Object kloonattu albumi
    */
   @Override
   public Kappale clone() throws CloneNotSupportedException { 
       return (Kappale)super.clone();
   }

}
