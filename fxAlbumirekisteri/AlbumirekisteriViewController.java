package fxAlbumirekisteri;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static fxAlbumirekisteri.AlbumiController.getFieldId;
import albumirekisteri.Albumi;
import albumirekisteri.Kappale;
import albumirekisteri.Omistaja;
import albumirekisteri.SailoException;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * 
 * @author Alexander SˆdergÂrd
 * @version 18.2.2019
 *
 */
public class AlbumirekisteriViewController implements Initializable {
    
    @FXML private ScrollPane panelAlbumi;
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Albumi> chooserAlbumit;
    @FXML private StringGrid<Kappale> tableKappaleet;
    @FXML private GridPane gridAlbumi;
    
    @FXML private TextField editArtisti;
    @FXML private TextField editAlbumi;
    @FXML private TextField editJvuosi;
    @FXML private TextField editYhtio;
    @FXML private TextField editGenre;
    @FXML private TextField editKunto;
    @FXML private TextField editHinta;
    @FXML private TextField editFormaatti;
    
    @FXML private TextField textArvo; 
   
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
   
    // Toiminto joka tulee kun painetaan yl‰palkista "Avaa".
    @FXML void handleAvaa() {
    avaa();
    }
    
    

    // Toiminto joka tulee kun painetaan yl‰palkista "Tallenna".
    @FXML void handleTallenna() {   
        tallenna();
    }
    
    

   // Toiminto joka ilmestyy kun halutaan tulostaa rekisterin tietoja.
   @FXML void handleTulosta() {
       TulostusController tulostusCtrl = TulostusController.tulosta(null); 
       tulostaValitut(tulostusCtrl.getTextArea()); 
   }
   
   // Toiminto hakukentt‰‰n kirjoittaessa
   @FXML private void handleHakuehto() {
       if ( albumiKohdalla != null )
       hae(albumiKohdalla.getTunnusNro());
   }


   // Toiminto suljettaessa ohjelmistoa.
   @FXML void handleSulje() {
       boolean vastaus = Dialogs.showQuestionDialog("Tallennataanko?",
       "Tallennetaanko ennen sulkemista?", "Kyll‰", "Ei");
           if (vastaus) tallenna();
           Platform.exit();
   }
   

    // Toiminto joka tulee kun lis‰t‰‰n uusi albumi.
    @FXML void handleLisaaAlbumi() {
    uusiAlbumi();
    }
    
    
    // Ohjaa kappaleen luomiseen
    @FXML void handleLisaaKappale() {        
    uusiKappale();
    }
    
 
    // Toiminto joka tulee kun muokataan albumia.
    @FXML void handleMuokkaaAlbumi() {
    muokkaa(kentta);
    }
    

    // Toiminto joka tulee kun poistetaan albumia.
    @FXML void handlePoistaAlbumi() {
    poistaAlbumi();
    }
    
    
    // Toiminto joka tulee tulostaessa tietoja. Aliohjelma johon siirryt‰‰n avaa selaimen.
    @FXML void handleOhje() {
    tietoAvustus();
    }
    
    
    // Toiminto joka ilmestyy kun halutaan lukea lis‰tietoja ohjelmasta. 
    @FXML void handleLisatietoja() {
    ModalController.showModal(AlbumirekisteriViewController.class.getResource("AboutView.fxml"), "Albumirekisteri", null, "");
    }
    
    
    @FXML void handlePoistaKappale() {        
    poistaKappale();
    }
    
    
    
    
 // =================================================================================================
 // K‰yttˆliittym‰n ulkopuolinen koodi  
    
    private String kokoelmanomistaja = "alexander";
    private Omistaja omistaja;
    private Albumi albumiKohdalla;
    private TextField[] edits;
    private int kentta = 0; 
    private static double arvo = 0; 
    private static Kappale apukappale = new Kappale(); 
    
      
    /** Alustetaan loota (fontin asetus), listan tyhjennys. 
     *  Jos albumi valitaan hiirell‰ siirryt‰‰n aliohjelmaan
     *  joka n‰ytt‰‰ albumin tiedot
     */
    protected void alusta() {
        chooserAlbumit.clear();
        chooserAlbumit.addSelectionListener(e -> naytaAlbumi());
        edits = TietueDialogController.luoKentat(gridAlbumi, new Albumi()); 
        for (TextField edit: edits) 
            if ( edit != null ) { 
                edit.setEditable(false); 
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); }); 
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta)); 
            } 
        
        int eka = apukappale.ekaKentta(); 
        int lkm = apukappale.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apukappale.getKysymys(k); 
        tableKappaleet.initTable(headings); 
        tableKappaleet.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableKappaleet.setEditable(false); 
        tableKappaleet.setPlaceholder(new Label("Ei viel‰ kappaleita")); 
         
        // T‰m‰ on viel‰ huono, ei automaattisesti muutu jos kentti‰ muutetaan.
        tableKappaleet.setColumnSortOrderNumber(1); 
        tableKappaleet.setColumnSortOrderNumber(2); 
        tableKappaleet.setColumnWidth(1, 60); 
 
    }
        /*
        panelAlbumi.setFitToHeight(true);
        chooserAlbumit.clear();
        chooserAlbumit.addSelectionListener(e -> naytaAlbumi());        
        edits = new TextField[] {  editArtisti, editAlbumi, editJvuosi, editYhtio, editGenre, editFormaatti,
                                  editKunto, editHinta     } ; */
    
    
    
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
    /**muokkaa valitun albumin tietoja
     * @param k kentan id
     */
    private void muokkaa(int k) {   
        if (albumiKohdalla == null) return;
        try { 
            Albumi albumi; 
            albumi = AlbumiController.kysyAlbumi(null, albumiKohdalla.clone(), k); 
            if ( albumi == null ) return; 
            omistaja.korvaaTaiLisaa(albumi); 
            hae(albumi.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            //
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }

      
   // Luo uuden albumin jota aletaan editoimaan
    private void uusiAlbumi() {
        Albumi uusi = new Albumi();
        uusi = AlbumiController.kysyAlbumi(null, uusi, 1);
        if (uusi == null) return;
        uusi.rekisteroi();
       // uusi.taytaTiedot();
        try {
            omistaja.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Virhe albumin luonnissa " + e.getMessage());
            e.printStackTrace();
            return;
        }
        hae(uusi.getTunnusNro());
    }
    
    
    /**
     * Tekee uuden tyhj‰n ikkunan kappaleen editointia varten
     */   
    private void uusiKappale() {
          if ( albumiKohdalla == null ) Dialogs.showMessageDialog("Albumia ei ole valittu");; 
            Kappale uusi = new Kappale(albumiKohdalla.getTunnusNro());
            uusi = TietueDialogController.kysyTietue(null, uusi, 0); 
            if (uusi == null) return;
            uusi.rekisteroi(); 
            omistaja.lisaa(uusi);
            /*uusi.taytaTiedot(albumiKohdalla.getTunnusNro());   */
            naytaKappaleet(albumiKohdalla);
            tableKappaleet.selectRow(1000);
            ///hae(albumiKohdalla.getTunnusNro());        
        } 
    
    
    /** Hakee albumit listaan n‰kyville.
     * @param tunnusNro valitsee luodun albumin n‰kyv‰mm‰ksi
     */
    protected void hae(int tunnusNro) {  
        arvo = 0;
        int k = cbKentat.getSelectionModel().getSelectedIndex();
        String ehto = hakuehto.getText(); 
       /* if (k > 0 || ehto.length() > 0)
            naytaVirhe(String.format("Ei osata hakea (kentt‰: %d, ehto: %s)", k, ehto));
            // omistaja.hakuehtoAli(int kentta, String ehto) 
        else naytaVirhe(null); */ 
        chooserAlbumit.clear();
        int index = 0;
        try {
            Collection<Albumi> albumit = omistaja.etsi(ehto, k);
            int i = 0;
            for (Albumi albumi:albumit) {
                if (albumi.getTunnusNro() == tunnusNro) index = i;
                chooserAlbumit.add(albumi.getArtisti(), albumi);
                arvo += Double.parseDouble(albumi.getHinta()); 
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Albumin hakemisessa ongelmia! " + ex.getMessage());
        }
        laitaArvo(textArvo, arvo); 
        chooserAlbumit.setSelectedIndex(index);   
    }
    
    /** Laskee kokoelman arvon
     * @param teksti tekstikentt‰ johon arvo syˆtet‰‰n
     * @param hinta yhteenlaskettu arvo
     */
    public static void laitaArvo(TextField teksti, double hinta) { 
        String tulos = String.format("%.2f",hinta);
        tulos = tulos.replace(',', '.');
        teksti.setText(tulos);
    }
    
    /**
     * Kysyt‰‰n tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = KaynnistysViewController.kysyNimi(null, kokoelmanomistaja);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        hae(0);
        return true;
    }
    
    
    
    /**
     * Alustaa omistajan kokoelman tiedot lukemalla sen valitun nimisest‰ tiedostosta
     * @param nimi tiedosto josta kokoelman tiedot luetaan
     * @return null jos onnistuu, muuten virhe
     */
    protected String lueTiedosto(String nimi) {
        kokoelmanomistaja = nimi;
        setTitle("Albumirekisteri - " + kokoelmanomistaja);
        try {
            omistaja.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
   
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstin‰
     */
    private String tallenna() {
        try {
            omistaja.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
        
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    
    
    /**
     * @param omistaja kokoelma jota k‰ytet‰‰n t‰ss‰ k‰yttˆliittym‰ss‰
     */    
    public void setOmistaja(Omistaja omistaja) {
        this.omistaja = omistaja;
        naytaAlbumi();
    }  
    
    /**
     * etsii ja n‰ytt‰‰ tietyn albumin kappaleet
     * @param albumi albumi jonka kappaleet etsit‰‰‰n
     */
    private void naytaKappaleet(Albumi albumi) {
        tableKappaleet.clear();
        if ( albumi == null ) return;    
            List<Kappale> kappaleet = omistaja.annaKappaleet(albumi);
            if ( kappaleet.size() == 0 ) return;
            for (Kappale kap: kappaleet)
                naytaKappaleet(kap);    
    }
   
    /**
     * N‰ytt‰‰ kappaleen taulukossa
     */
    private void naytaKappaleet(Kappale kap) {
        int kenttia = kap.getKenttia();
        String[] rivi = new String[kenttia - kap.ekaKentta()];
        for (int i =0, k=kap.ekaKentta(); k<kenttia; k++, i++) 
            rivi[i] = kap.anna(k);
            tableKappaleet.add(kap,rivi);
        }
        /*String[] rivi = kap.toString().split("\\|"); // 
        tableKappaleet.add(kap,rivi[2],rivi[3]); //
    

    
    /**
     * N‰ytt‰‰ listasta valitun albumin tiedot.
     * albumiKohdalla = valittu albumi albumilistalta => tulosta metodi toimii t‰lle
     */
    private void naytaAlbumi() {
        albumiKohdalla = chooserAlbumit.getSelectedObject();
        if (albumiKohdalla == null) {
           // areaAlbumi.clear();
            return;
        }
        
        AlbumiController.naytaAlbumi(edits, albumiKohdalla);
        /* Tyhm‰sti :
        editArtisti.setText(albumiKohdalla.getNimi());
        editAlbumi.setText(albumiKohdalla.getArtisti());
        editJvuosi.setText(albumiKohdalla.getJvuosi());
        editYhtio.setText(albumiKohdalla.getYhtio());
        editGenre.setText(albumiKohdalla.getGenre());
        editFormaatti.setText(albumiKohdalla.getFormaatti());
        editKunto.setText(albumiKohdalla.getKunto());
        editHinta.setText(albumiKohdalla.getHinta());
        */
        naytaKappaleet(albumiKohdalla);
    }
    

    
    
    /**
     * Tulostaa albumin tiedot
     * @param os tietovirta johon tulostetaan
     * @param albumi tulostettava albumi
     */
    public void tulosta(PrintStream os, final Albumi albumi) {
        os.println("----------------------------------------------");
        albumi.tulosta(os);
        os.println("----------------------------------------------");
        ArrayList<Kappale> kappaleet = omistaja.annaKappaleet(albumi);   
        for (Kappale kap:kappaleet)
            kap.tulosta(os); 
    }

    /**
     *  Aliohjelma k‰ynnist‰‰ selaimen, jolla siirryt‰‰n ohjeet sis‰lt‰v‰‰n sivuun
     */
    private void tietoAvustus() {
        
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/alsoderg");
            desktop.browse(uri);
        } catch (URISyntaxException e) {        
            return;
        } catch (IOException e) {
            return;
        }
    }     
    
    /**
     * Tulostaa listassa olevat albumit tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki albumit");
            for (Albumi albumi: chooserAlbumit.getObjects()) { 
                tulosta(os, albumi);
                os.println("\n\n");
            }
        }
    }
    
    /**
     * Poistetaan listalta valittu albumi
     */
    private void poistaAlbumi() {
        Albumi albumi = albumiKohdalla;
        if ( albumi == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko albumi: " + albumi.getNimi(), "Kyll‰", "Ei") )
            return;
        omistaja.poista(albumi);
        int index = chooserAlbumit.getSelectedIndex();
        hae(0);
        chooserAlbumit.setSelectedIndex(index);
    }
    
    /**
     * Poistetaan kappaletaulukosta valitulla kohdalla oleva kappale.
     */
    private void poistaKappale() {
        int rivi = tableKappaleet.getRowNr();
        if ( rivi < 0 ) return;
        Kappale kappale = tableKappaleet.getObject();
        if ( kappale == null ) return;
        omistaja.poistaKappale(kappale);
        naytaKappaleet(albumiKohdalla);
        int kappaleita = tableKappaleet.getItems().size(); 
        if ( rivi >= kappaleita ) rivi = kappaleita -1;
        tableKappaleet.getFocusModel().focus(rivi);
        tableKappaleet.getSelectionModel().select(rivi);
    }
}