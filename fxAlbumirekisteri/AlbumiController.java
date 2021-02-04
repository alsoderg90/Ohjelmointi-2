package fxAlbumirekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import albumirekisteri.Albumi;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Luokka albumin muokkaamiselle
 * 
 * @author Alexander S�derg�rd
 * @version 20.2.2019
 *
 */
public class AlbumiController implements ModalControllerInterface<Albumi>, Initializable  {

    @FXML private ScrollPane panelAlbum;
    @FXML private GridPane gridAlbumi;
    @FXML private Label labelVirhe;
    
    @FXML private BorderPane borderPane;
    @FXML private TextField editArtisti;
    @FXML private TextField editAlbumi;
    @FXML private TextField editJvuosi;
    @FXML private TextField editYhtio;
    @FXML private TextField editGenre;
    @FXML private TextField editKunto;
    @FXML private TextField editHinta;
    @FXML private TextField editFormaatti;

    
    @FXML void handlePeruuta() {
        albumiKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    @FXML
    void handleTallenna() {
        if ( albumiKohdalla != null && albumiKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhj�");
            return;
        }
        ModalController.closeStage(labelVirhe);
    } 


 //----------------------------------------------------------------------
    private TextField[] edits;
    private Albumi albumiKohdalla;
    private static Albumi apuAlbumi = new Albumi();
    private int kentta = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();     
    }
    
    
    /**
     *  Tekee alustuksen
     */
    public void alusta() { 
        
        edits = luoKentat(gridAlbumi);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosAlbumiin((TextField)(e.getSource())));
        panelAlbum.setFitToHeight(true);
    }
        /*
        edits = luoKentat(gridAlbumi);
        edits = new TextField[] { editArtisti, editAlbumi, editJvuosi, editYhtio, editGenre, editFormaatti,
                editKunto, editHinta     } ;
        
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleMuutosAlbumiin(k, (TextField)(e.getSource()))); */
        
    /**
     * Tyhjent��n tekstikent�t
     * @param edits tyhjennett�v�t kent�t
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit: edits) 
            if ( edit != null ) edit.setText(""); 
    }
    
    /**
     * Luodaan GridPaneen albumin tiedot
     * @param gridAlbumi mihin tiedot luodaan
     * @return luodut tekstikent�t
     */
    public static TextField[] luoKentat(GridPane gridAlbumi) {
        gridAlbumi.getChildren().clear();
        TextField[] edits = new TextField[apuAlbumi.getKenttia()];
       
        for (int i=0, k = apuAlbumi.ekaKentta(); k < apuAlbumi.getKenttia(); k++, i++) {
            Label label = new Label(apuAlbumi.getKysymys(k));
            gridAlbumi.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridAlbumi.add(edit, 1, i);
        }
        return edits;
    }
    
    /**
     * palautetaan dialogin tulos()
     */
    @Override
    public Albumi getResult() {
        return albumiKohdalla;
    }

    
    /**
     * Mit� tehd��n kun dialogi on n�ytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuAlbumi.ekaKentta(), Math.min(kentta, apuAlbumi.getKenttia()-1));
        edits[kentta].requestFocus();
        
    }

    @Override
    public void setDefault(Albumi oletus) {
        albumiKohdalla = oletus;
        naytaAlbumi(edits, albumiKohdalla);       
    }
    

    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /**
     * Palautetaan komponentin id:st� saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mik� arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
    
   
    /**
     * K�sitell��n albumiin tullut muutos
     * @param k mik� kentt� muuttui
     * @param edit muuttunut kentt�
     */
    private void kasitteleMuutosAlbumiin(TextField edit) {
        if (albumiKohdalla == null) return;
        int k = getFieldId(edit,apuAlbumi.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = albumiKohdalla.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
        /*if (albumiKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
           case 1 : virhe = albumiKohdalla.setArtisti(s); break;
           case 2 : virhe = albumiKohdalla.setNimi(s); break;
           case 3 : virhe = albumiKohdalla.setJvuosi(s); break;
           case 4 : virhe = albumiKohdalla.setYhtio(s); break;
           case 5 : virhe = albumiKohdalla.setGenre(s); break;
           case 6 : virhe = albumiKohdalla.setFormaatti(s); break;
           case 7 : virhe = albumiKohdalla.setKunto(s); break;
           case 8 : virhe = albumiKohdalla.setHinta(s); break;
           default:
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
   */ 
   
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    
    /**
     * N�ytet��n albumin tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeist� johon n�ytet��n
     * @param albumi n�ytett�v� albumi
     */
    public static void naytaAlbumi(TextField[] edits, Albumi albumi) {
        if (albumi == null) return;
        for (int k = albumi.ekaKentta(); k < albumi.getKenttia(); k++) {
            edits[k].setText(albumi.anna(k));
        }
    }
    
    /*
     * N�ytet��n albumin tiedot TextField komponentteihin
     * @param edits taulukko jossa tekstikentti�
     * @param albumi n�ytett�v� albumi

    public static void naytaAlbumi(TextField[] edits, Albumi albumi) {
        edits[0].setText(albumi.getArtisti());
        edits[1].setText(albumi.getNimi());
        edits[2].setText(albumi.getJvuosi());
        edits[3].setText(albumi.getYhtio());
        edits[4].setText(albumi.getGenre());
        edits[5].setText(albumi.getFormaatti());
        edits[6].setText(albumi.getKunto());
        edits[7].setText(albumi.getHinta());
    }
    
   
    public void naytaAlbumi(Albumi albumi) {
        if (albumi == null) return;
        naytaAlbumi(edits,albumi);
        editArtisti.setText(albumi.getArtisti());
        editAlbumi.setText(albumi.getNimi());
        editJvuosi.setText(albumi.getJvuosi());
        editYhtio.setText(albumi.getYhtio());
        editGenre.setText(albumi.getGenre());
        editFormaatti.setText(albumi.getFormaatti());
        editKunto.setText(albumi.getKunto());
        editHinta.setText(albumi.getHinta());
    }    
    */
    
    /**
     * Luodaan albumin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit� dataan n�ytet��n oletuksena
     * @param kentta klikattu kentt�
     * @return null jos painetaan Cancel, muuten t�ytetty tietue
     */
    public static Albumi kysyAlbumi(Stage modalityStage, Albumi oletus, int kentta) {
        return ModalController.<Albumi, AlbumiController>showModal(
                    AlbumiController.class.getResource("AlbumiDialogView.fxml"),
                    "Albumirekisteri",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta)
                );
    }


}