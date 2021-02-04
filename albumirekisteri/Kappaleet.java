package albumirekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;


/** Luokka kappaleille
 * Kappaleiden lukeminen tiedostosta ja tallennus, poistaminen, muokkaaminen ja lisäys
 * @author Alexander Södergård
 * @version 8.3.2019
 *
 */
public class Kappaleet implements Iterable<Kappale>  {
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = " ";
    private final ArrayList<Kappale> alkiot =    new ArrayList<Kappale>();
    
    
    /** Kappaleen alustus
     * 
     */
    public Kappaleet() {
       // 
    }
    
    /**
     * @param kap kappale joka lisataan tietokantaan
     */
    public void lisaa(Kappale kap) {
       this.alkiot.add(kap);
       muutettu = true;
    }
    
    /**
     * Poistaa kaikki tietyn tietyn albumin kappaleet
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin
     * @example
     * <pre name="test">
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale eka = new Kappale(); eka.taytaTiedot(2);
     *  Kappale toka = new Kappale(); toka.taytaTiedot(1);
     *  Kappale kolmas = new Kappale(); kolmas.taytaTiedot(2);
     *  Kappale neljas = new Kappale(); neljas.taytaTiedot(1);
     *  Kappale viides = new Kappale(); viides.taytaTiedot(2);
     *  kappaleet.lisaa(eka);
     *  kappaleet.lisaa(toka);
     *  kappaleet.lisaa(kolmas);
     *  kappaleet.lisaa(neljas);
     *  kappaleet.poistaAlbuminKappaleet(2) === 2;  kappaleet.getLkm() === 2;
     *  kappaleet.poistaAlbuminKappaleet(3) === 0;  kappaleet.getLkm() === 2;
     *  List<Kappale> kap = kappaleet.annaKappaleet(2);
     *  kap.size() === 0;
     *  kap = kappaleet.annaKappaleet(1);
     *  kap.get(0) === toka;
     *  kap.get(1) === neljas;
     * </pre>
     */
    public int poistaAlbuminKappaleet(int tunnusNro) {
        int n = 0;
        for (Iterator<Kappale> it = alkiot.iterator(); it.hasNext();) {
            Kappale kap = it.next();
            if ( kap.getAlbumiNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    
    /**
     * Lukee kappaleet tiedostosta. 
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale eka = new Kappale(); eka.taytaTiedot(2);
     *  Kappale toka = new Kappale(); toka.taytaTiedot(1);
     *  Kappale kolmas = new Kappale(); kolmas.taytaTiedot(2);
     *  Kappale neljas = new Kappale(); neljas.taytaTiedot(1);
     *  Kappale viides = new Kappale(); viides.taytaTiedot(2);
     *  String tiedNimi = "testialexander";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  kappaleet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  kappaleet.lisaa(eka);
     *  kappaleet.lisaa(toka);
     *  kappaleet.lisaa(kolmas);
     *  kappaleet.lisaa(neljas);
     *  kappaleet.lisaa(viides);
     *  kappaleet.tallenna();
     *  kappaleet = new Kappaleet();
     *  kappaleet.lueTiedostosta(tiedNimi);
     *  Iterator<Kappale> i = kappaleet.iterator();
     *  i.next().toString() === eka.toString();
     *  i.next().toString() === toka.toString();
     *  i.next().toString() === kolmas.toString();
     *  i.next().toString() === neljas.toString();
     *  i.next().toString() === viides.toString();
     *  i.hasNext() === false;
     *  kappaleet.lisaa(viides);
     *  kappaleet.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        setTiedostonPerusNimi(hakemisto);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi; 
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kappale kap = new Kappale();
                kap.parse(rivi); // voisi olla virhekäsittely
                lisaa(kap);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
       } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage()); 
        }
    }
    
    /**
     * Poistaa valitun kappaleen
     * @param kappale poistettava kappale
     * @return tosi jos löytyi poistettava tietue
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale eka = new Kappale(); eka.taytaTiedot(2);
     *  Kappale toka = new Kappale(); toka.taytaTiedot(1);
     *  Kappale kolmas = new Kappale(); kolmas.taytaTiedot(2);
     *  Kappale neljas = new Kappale(); neljas.taytaTiedot(1);
     *  Kappale viides = new Kappale(); viides.taytaTiedot(2);
     *  kappaleet.lisaa(eka);
     *  kappaleet.lisaa(toka);
     *  kappaleet.lisaa(kolmas);
     *  kappaleet.lisaa(neljas);
     *  kappaleet.poista(viides) === false ; kappaleet.getLkm() === 4;
     *  kappaleet.poista(toka) === true;   kappaleet.getLkm() === 3;
     *  List<Kappale> kap = kappaleet.annaKappaleet(1);
     *  kap.size() === 1;
     *  kap.get(0) === neljas;
     * </pre>
     */
    public boolean poista(Kappale kappale) {
        boolean ret = alkiot.remove(kappale);
        if (ret) muutettu = true;
        return ret;
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    
    /**
     * Tallentaa kappaleet tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");
        
        try ( PrintStream fo = new PrintStream(new FileOutputStream(ftied.getCanonicalPath())) ) {
            for (Kappale kap : this) {
                fo.println(kap.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    private String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Palauttaa kerhon Kappaleten lukumäärän
     * @return Kappaleten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }

    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
    * Palauttaa tiedoston nimen, jota käytetään tallennukseen
    * @return tallennustiedoston nimi
    */
   public String getTiedostonNimi() {
       return tiedostonPerusNimi + ".dat";
   }
    
    
    /**
     * Haetaan kaikki albumin kappaleet
     * @param tunnusnro albumin tunnusnumero jolle kappaleet haetaan
     * @return tietorakenne jossa viiteet löydetteyihin kappaleisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     *
     *  Kappaleet biisit = new Kappaleet();
     *  Kappale pitsi21 = new Kappale(2); biisit.lisaa(pitsi21);
     *  Kappale pitsi11 = new Kappale(1); biisit.lisaa(pitsi11);
     *  Kappale pitsi22 = new Kappale(2); biisit.lisaa(pitsi22);
     *  Kappale pitsi12 = new Kappale(1); biisit.lisaa(pitsi12);
     *  Kappale pitsi23 = new Kappale(2); biisit.lisaa(pitsi23);
     *  Kappale pitsi51 = new Kappale(5); biisit.lisaa(pitsi51);
     * 
     *  List<Kappale> loytyneet;
     *  loytyneet = biisit.annaKappaleet(3);
     *  loytyneet.size() === 0;
     *  loytyneet = biisit.annaKappaleet(1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = biisit.annaKappaleet(5);
     *  loytyneet.size() === 1;
     *  loytyneet.get(0) == pitsi51 === true;
     * </pre>
     */ 
    public ArrayList<Kappale> annaKappaleet(int tunnusnro) {
        ArrayList<Kappale> loydetyt = new ArrayList<Kappale>();
        for (Kappale kap : alkiot)
            if (kap.getAlbumiNro() == tunnusnro) loydetyt.add(kap);
        return loydetyt;
    }
    
    
    /**
     * Iteraattori kaikkien Kappaleten läpikäymiseen
     * @return Kappale iteraattori
     *
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale eka = new Kappale(2); kappaleet.lisaa(eka);
     *  Kappale toka = new Kappale(1); kappaleet.lisaa(toka);
     *  Kappale kolmas = new Kappale(2); kappaleet.lisaa(kolmas);
     *  Kappale neljas = new Kappale(1); kappaleet.lisaa(neljas);
     *  Kappale viides = new Kappale(2); kappaleet.lisaa(viides);
     *
     *  Iterator<Kappale> i2=kappaleet.iterator();
     *  i2.next() === eka;
     *  i2.next() === toka;
     *  i2.next() === kolmas;
     *  i2.next() === neljas;
     *  i2.next() === viides;
     *  i2.next() === neljas;  #THROWS NoSuchElementException 
     * 
     *  int n = 0;
     *  int anrot[] = {2,1,2,1,2};
     * 
     *  for ( Kappale kap:kappaleet ) {
     *    kap.getAlbumiNro() === anrot[n]; n++; 
     *  }
     * 
     *  n === 5;
     * 
     * </pre>
     */
    @Override
    public Iterator<Kappale> iterator() {
        return alkiot.iterator();
    }
    
    /** Testiohjelma kappaleilla
     * @param args eii käytössä
     */
    public static void main(String[] args) {
        Kappaleet biisit = new Kappaleet();
        Kappale eka = new Kappale();
        eka.taytaTiedot(2);
        Kappale toka = new Kappale();
        toka.taytaTiedot(1);
        Kappale kolmas = new Kappale();
        kolmas.taytaTiedot(2);
        Kappale neljas = new Kappale();
        neljas.taytaTiedot(2);
        biisit.lisaa(eka);
        biisit.lisaa(toka);
        biisit.lisaa(kolmas);
        biisit.lisaa(toka);
        biisit.lisaa(neljas);
        System.out.println("============= Kappaleet testi =================");
        ArrayList<Kappale> biisit2 = biisit.annaKappaleet(2);
        for (Kappale kap : biisit2) {
            System.out.print(kap.getAlbumiNro() + " ");
            kap.tulosta(System.out);
        }
    }
   
}
