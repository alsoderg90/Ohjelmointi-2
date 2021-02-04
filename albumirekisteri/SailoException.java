package albumirekisteri;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Alexander S�derg�rd
 * @version 1.0, 22.02.2019
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * k�ytett�v� viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}