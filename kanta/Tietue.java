package kanta;
/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa
 * "attribuutit".
 * @author Alexander Södergård
 * @version 18.4.2019
 * @example
 */
public interface Tietue {
   
    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import albumirekisteri.Kappale;
     *   Kappale kap = new Kappale();
     *   kap.getKenttia() === 4;
     * </pre>
     */
    
    public abstract int getKenttia();
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     * @example
     * <pre name="test">
     *   Kappale kap = new Kappale();
     *   kap.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Kappale kap = new Kappale();
     *   kap.getKysymys(2) === "ala";
     * </pre>
     */
    public abstract String getKysymys(int k);
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Kappale kap = new Kappale();
     *   kap.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   kap.anna(0) === "2";   
     *   kap.anna(1) === "10";   
     *   kap.anna(2) === "Kalastus";   
     *   kap.anna(3) === "1949";   
     *   kap.anna(4) === "22";   
     * </pre>
     */
    public abstract String anna(int k);
   
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Kappale kap = new Kappale();
     *   kap.aseta(3,"kissa") === "aloitusvuosi: Ei kokonaisluku (kissa)";
     *   kap.aseta(3,"1940")  === null;
     *   kap.aseta(4,"kissa") === "h/vko: Ei kokonaisluku (kissa)";
     *   kap.aseta(4,"20")    === null;
     * </pre>
     */
    public abstract String aseta(int k, String s);
    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Kappale kap = new Kappale();
     *   kap.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   Object kopio = kap.clone();
     *   kopio.toString() === kap.toString();
     *   kap.parse("   1   |  11  |   Uinti  | 1949 | 22 t ");
     *   kopio.toString().equals(kap.toString()) === false;
     *   kopio instanceof Kappale === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;
    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Kappale kappale = new Kappale();
     *   kappale.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   kappale.toString()    =R= "2\\|10\\|Kalastus\\|1949\\|22.*";
     * </pre>
     */
    @Override
    public abstract String toString();
}