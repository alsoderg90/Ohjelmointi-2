package albumirekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import albumirekisteri.SailoException;

import fi.jyu.mit.fxgui.Dialogs;

/** 
 * Luokka albumeille
 * Albumin lisäys kokoelmaan, albumin poisto ja muokkaus, tiedostosta lukeminen ja tallennus
 * @author Alexander Södergård
 * @version 19.2.2019
 *
 */
public class Albumit implements Iterable<Albumi> {
    

    private String kokoNimi  = "";
    private boolean muutettu = false;
    private static final int MAX_ALBUMEITA       = 10;
    private int              lkm                 = 0;
    private String           tiedostonPerusNimi  = "albumit";
    private Albumi           alkiot[]              = new Albumi[MAX_ALBUMEITA];
    
    
    /**
     * Oletusmuodostaja
     */
    public Albumit() {
        // Attribuuttien oma alustus riittää
    }
    
    
    /**
     * Lisää uuden albumin tietorakenteeseen.  Ottaa albumin omistukseensa.
     * @param albumi lisättävän albumin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException asd
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Albumit albumit = new Albumit();
     * Albumi eka = new Albumi(), toka = new Albumi();
     * albumit.getLkm() === 0;
     * albumit.lisaa(eka); albumit.getLkm() === 1;
     * albumit.lisaa(toka); albumit.getLkm() === 2;
     * albumit.lisaa(eka); albumit.getLkm() === 3;
     * albumit.anna(0) === eka;
     * albumit.anna(1) === toka;
     * albumit.anna(2) === eka;
     * albumit.anna(1) == eka === false;
     * albumit.anna(1) == toka === true;
     * albumit.anna(3) === eka; #THROWS IndexOutOfBoundsException
     * albumit.lisaa(eka); albumit.getLkm() === 4;
     * albumit.lisaa(eka); albumit.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Albumi albumi) throws SailoException {
        if (albumi == null) return;
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
            alkiot[lkm] = albumi;
            lkm++;
            muutettu = true;
            
            /*        
            Albumi[] uusi =  new Albumi[alkiot.length + 20];
            for (int i = 0; i < this.lkm; i++) {
            uusi[i] = alkiot[i];
            uusi[lkm++] = alkio;
            this.alkiot = uusi;
            uusi.alkiot[i] = alkio; }     */
            
        }

    /**
     * Palauttaa rekisterin  albumien lukumäärän
     * @return lukumäärä
     */
    public int getLkm() {
       return lkm;
    }
    
    /** Asettaa albumien lukumäärän
     * @param lkm albumien lkm
     */
    public void setLkm(int lkm) {
        this.lkm = lkm;
    }
    
    /**
     * Palauttaa viitteen i:teen albumi.
     * @param i monennenko albumin viite halutaan
     * @return viite albumiin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella 
     */
    public Albumi anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }

    
    /** 
     * Lukee albumilistan tiedostosta.
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine(); // 
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ' ' || rivi.charAt(0) == ';' ) continue;
                Albumi albumi = new Albumi();
                albumi.parse(rivi); // voisi olla virhekäsittely
                lisaa(albumi);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage()); 
        }
        
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien albumien viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä albumeista
     */ 
    public Collection<Albumi> etsi(String hakuehto, int k) { 
    List<Albumi> loytyneet = new ArrayList<Albumi>(); 
    for (Albumi albumi : this) { 
        if (etsiOikeat(albumi, hakuehto, k))
        loytyneet.add(albumi); 
        } 
        return loytyneet; 
    }

    /**
     * @param albumi albumi jota käsitellään
     * @param haku hakuehto
     * @param kentta hakuehtan kenttä
     * @return toteuttaako hakuehdon
     */
    public static boolean etsiOikeat(Albumi albumi, String haku, int kentta) {
      if (haku.length() == 0) return true;
        boolean onko = false;
        String ehto = haku.toLowerCase();
        int hakukentta = kentta + 1;
        switch (hakukentta) {
        case 1 : if (albumi.getArtisti().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 2 : if (albumi.getNimi().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 3 : if (albumi.getJvuosi().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 4 : if (albumi.getYhtio().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 5 : if (albumi.getGenre().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 6 : if (albumi.getFormaatti().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 7 : if (albumi.getKunto().toLowerCase().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        case 8 : if (albumi.getHinta().matches("(" +ehto+ ")"+"{1}.*")) onko = true; break;
        default:
        }
        return onko;
    }


    /**
     * Tallentaa Albumilistan tiedostoon. 
     * Tiedoston muoto:
     * <pre>
     * Alexander
     * 20
     * ; kommenttirivi
     * 2|Kingston Wall|II|2015|Svart Records|Rock|LP|VG+|60.0
     * 3|Laineen Kasperi|Pako-Laine|2012|Ähky Records|Rap|VG+|60.0
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");
        try ( PrintStream fo = new PrintStream(new FileOutputStream(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Albumi albumi : this) {
                fo.println(albumi.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
        Dialogs.showMessageDialog("Tallennus onnistui!");
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Palauttaa Kokoelman koko nimen
     * @return Kokoelman omistajan koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    /** 
     * Luokka albumien iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     * Albumit albumit = new Albumit();
     * Albumi eka = new Albumi(), toka = new Albumi();
     * eka.rekisteroi(); toka.rekisteroi();
     *
     * albumit.lisaa(eka);
     * albumit.lisaa(toka);
     * albumit.lisaa(eka);
     *
     * StringBuffer ids = new StringBuffer(30);
     * for (Albumi albumi:albumit)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+ albumi.getTunnusNro());           
     *
     * String tulos = " " + eka.getTunnusNro() + " " + toka.getTunnusNro() + " " + eka.getTunnusNro();
     *
     * ids.toString() === tulos;
     *
     * ids = new StringBuffer(30);
     * for (Iterator<Albumi>  i=albumit.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Albumi albumi = i.next();
     *   ids.append(" "+ albumi.getTunnusNro());           
     * }
     *
     * ids.toString() === tulos;
     *
     * Iterator<Albumi>  i=albumit.iterator();
     * i.next() == eka  === true;
     * i.next() == toka  === true;
     * i.next() == eka  === true;
     *
     * i.next();  #THROWS NoSuchElementException
     * 
     * </pre>
     */
    public class AlbumitIterator implements Iterator<Albumi> {
        private int kohdalla = 0;
        /**
         * Onko olemassa vielä seuraavaa albumia
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä albumeita
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        /**
         * Annetaan seuraava albumi
         * @return seuraava albumi
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Albumi next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei löydy seuraavaa");
            return anna(kohdalla++);
        }
        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    /**
     * Palautetaan iteraattori albumeistaan.
     * @return albumi iteraattori
     */
    @Override
    public Iterator<Albumi> iterator() {
      return new AlbumitIterator();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
        Albumit alexander = new Albumit();
        
        Albumi II = new Albumi(), PakoLaine = new Albumi(),PakoLaine1 = new Albumi(), kolmas = new Albumi();
        II.rekisteroi(); 
        II.taytaTiedot(); 
        PakoLaine.rekisteroi();
        PakoLaine.taytaTiedot();
        kolmas.rekisteroi();
        kolmas.taytaTiedot();
        PakoLaine1.rekisteroi();
        PakoLaine1.taytaTiedot();
        
          
          
            try {
                alexander.lisaa(II);
                alexander.lisaa(PakoLaine);
                alexander.lisaa(kolmas);
                alexander.lisaa(PakoLaine1);
            } catch (SailoException e) {
                System.err.println("Virhe: " + e.getMessage());
                e.printStackTrace();
            }
        
            System.out.println("============= Albumit testi =================");

            for (int i = 0; i < alexander.getLkm(); i++) {
                Albumi albumi = alexander.anna(i);
                System.out.println("albumi nro: " + i);
                albumi.tulosta(System.out);
            }
        
        }



    /**
     * Korvaa albumin tietorakenteessa.  Ottaa albumin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva albumi.  Jos ei löydy,
     * niin lisätään uutena albumi.
     * @param albumi lisätäävän albumiin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test"> 
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Albumit albumit = new Albumit();
     * Albumi eka = new Albumi(), toka = new Albumi();
     * eka.rekisteroi(); toka.rekisteroi();
     * albumit.getLkm() === 0;
     * albumit.korvaaTaiLisaa(eka); albumit.getLkm() === 1;
     * albumit.korvaaTaiLisaa(toka); albumit.getLkm() === 2;
     * Albumi kolmas = eka.clone();
     * kolmas.setJvuosi("2015");
     * Iterator<Albumi> it = albumit.iterator();
     * it.next() == eka === true;
     * albumit.korvaaTaiLisaa(kolmas); albumit.getLkm() === 2;
     * it = albumit.iterator();
     * Albumi j0 = it.next();
     * j0 === kolmas;
     * j0 == kolmas === true;
     * j0 == eka === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Albumi albumi)  throws SailoException {
        int id = albumi.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = albumi;
                muutettu = true;
                return;
            }
        }
        lisaa(albumi);
    }
    
    /**
     * Poistaa albumin jolla on valittu tunnusnumero 
     * @param id poistettavan albumin tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei löydy
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Albumit albumit = new Albumit();
     * Albumi eka = new Albumi(), toka = new Albumi(), kolmas = new Albumi();
     * eka.rekisteroi(); toka.rekisteroi(); kolmas.rekisteroi();
     * int id1 = eka.getTunnusNro();
     * albumit.lisaa(eka); albumit.lisaa(toka); albumit.lisaa(kolmas);
     * albumit.poista(id1+1) === 1;
     * albumit.annaId(id1+1) === null; albumit.getLkm() === 2;
     * albumit.poista(id1) === 1; albumit.getLkm() === 1;
     * albumit.poista(id1+3) === 0; albumit.getLkm() === 1;
     * </pre>
     * 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    /**
     * Etsii albumin id:n perusteella
     * @param id tunnusnumero, jonka mukaan etsitään
     * @return albumi jolla etsittävä id tai null
     * <pre name="test">
     * #THROWS SailoException 
     * Albumit albumit = new Albumit();
     * Albumi eka = new Albumi(), toka = new Albumi(), kolmas = new Albumi();
     * eka.rekisteroi(); toka.rekisteroi(); kolmas.rekisteroi();
     * int id1 = eka.getTunnusNro();
     * albumit.lisaa(eka); albumit.lisaa(toka); albumit.lisaa(kolmas);
     * albumit.annaId(id1  ) == eka === true;
     * albumit.annaId(id1+1) == toka === true;
     * albumit.annaId(id1+2) == kolmas === true;
     * </pre>
     */ 
    public Albumi annaId(int id) { 
        for (Albumi albumi : this) { 
            if (id == albumi.getTunnusNro()) return albumi; 
        } 
        return null; 
    } 
    
    
    /**
     * @param id etsitään albumi id:n perustella
     * @return jäsenen indeksi tai -1, jos ei löydy
     */
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 
}  
