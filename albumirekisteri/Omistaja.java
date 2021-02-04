package albumirekisteri;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

 /** (Välittäjä)Luokka, joka huolehtii albumeista ja kappaleista.
 * @author Alexander Södergård
 * @version 20.2.2019
 *
 */
public class Omistaja {

    private Albumit albumit = new Albumit();
    private Kappaleet kappaleet = new Kappaleet(); 

    
    /**
     * Lisää albumirekisteriin uuden albumin
     * @param albumi lisatta albumi
     * @throws SailoException jos tietorakenne taynna
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Omistaja omistaja = new Omistaja();
     * Albumi eka = new Albumi(), toka = new Albumi();
     * eka.rekisteroi(); toka.rekisteroi();
     * omistaja.getAlbumeita() === 0;
     * omistaja.lisaa(eka); omistaja.getAlbumeita() === 1;
     * omistaja.lisaa(toka); omistaja.getAlbumeita() === 2;
     * omistaja.lisaa(eka); omistaja.getAlbumeita() === 3;
     * omistaja.annaAlbumi(0) === eka;
     * omistaja.annaAlbumi(1) === toka;
     * omistaja.annaAlbumi(2) === eka;
     * omistaja.annaAlbumi(1) == eka === false;
     * omistaja.annaAlbumi(1) == toka === true;
     * omistaja.annaAlbumi(3) === eka; #THROWS IndexOutOfBoundsException
     * omistaja.lisaa(eka); omistaja.getAlbumeita() === 4;
     * omistaja.lisaa(eka); omistaja.getAlbumeita() === 5;
     * </pre>
     */   
    public void lisaa(Albumi albumi) throws SailoException {
        albumit.lisaa(albumi);
    }
    
    /**
     * @param kap kappale joka lisataan 
     */
    public void lisaa(Kappale kap) {
        kappaleet.lisaa(kap);
    }
    
    /**
     * palauttaa kokoelman koon
     * @return koko
     */
    public int getAlbumeita() {
        return albumit.getLkm();
    }
    
    
    /**
     * @param i monesko albumi palautetaan
     * @return viite albumiin i
     * @throws IndexOutOfBoundsException laiton indeksi
     */
    public Albumi annaAlbumi(int i) throws IndexOutOfBoundsException {
        return albumit.anna(i);
    }
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien albumien viitteet
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä albumeista
     * @throws SailoException Jos jotakin menee väärin
     */ 
    public Collection<Albumi> etsi(String hakuehto, int k) throws SailoException { 
        return albumit.etsi(hakuehto, k); 
    } 

    
    /**
     * @param nimi Tiedostonnimi jota luetaan
     * @throws SailoException poikkeus jos lukeminen ei onnistu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        albumit = new Albumit(); // 
        kappaleet = new Kappaleet();
        setTiedosto(nimi);
        albumit.lueTiedostosta();
        kappaleet.lueTiedostosta();
    }
    
    
    
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        albumit.setTiedostonPerusNimi(hakemistonNimi + "albumit");
        kappaleet.setTiedostonPerusNimi(hakemistonNimi + "kappaleet");
    }
    
    
    /**
     * Tallenttaa kerhon tiedot tiedostoon. 
     * Vaikka albumien tallettamien epäonistuisi, niin yritetään silti tallettaa
     * kappaleita ennen poikkeuksen heittämistä.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            albumit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        try {
            kappaleet.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    

    /**
     * Haetaan kaikki albumin kappaleet
     * @param albumi albumi jolle kappaleita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin kappaleisiin
     * @example
     */
    public ArrayList<Kappale> annaKappaleet(Albumi albumi)  {
        return kappaleet.annaKappaleet(albumi.getTunnusNro());
    }
    
    /**
     * Korvaa albumin tietorakenteessa.  Ottaa albumin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva albumi.  Jos ei löydy,
     * niin lisätään uutena albumi.
     * @param albumi lisätäävän albumiin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */ 
    public void korvaaTaiLisaa(Albumi albumi) throws SailoException { 
        albumit.korvaaTaiLisaa(albumi); 
    } 
    
    
    /** Poistetaan albumi ja sen kappaleet
     * @param albumi poistettava
     * @return 1 jos onnistui
     */
    public int poista(Albumi albumi) {
        if ( albumi == null ) return 0;
        int ret = albumit.poista(albumi.getTunnusNro()); 
        kappaleet.poistaAlbuminKappaleet(albumi.getTunnusNro()); 
        return ret; 
    }
    
    
    /** Poistaa kappaleen
     * @param kappale poisttava
     */
    public void poistaKappale(Kappale kappale) { 
        kappaleet.poista(kappale); 
    } 
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
    Omistaja mina = new Omistaja();
    
    Albumi II = new Albumi(), PakoLaine = new Albumi();
    
    II.rekisteroi(); 
    II.taytaTiedot(); 
    PakoLaine.rekisteroi();
    PakoLaine.taytaTiedot();
    
    try {
        mina.lisaa(II);
        mina.lisaa(PakoLaine);
    } catch (SailoException e) {
        System.out.println("Virhe:" + e.getMessage());
    }

    
    System.out.println("============= Kokoelmasi levyt =================");
    
    for (int i = 0; i < mina.getAlbumeita(); i++) {
        Albumi albumi = mina.annaAlbumi(i);
        System.out.println("albumi nro: " + i);
        albumi.tulosta(System.out);
    }
    }

}
