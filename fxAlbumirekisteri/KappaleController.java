package fxAlbumirekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import albumirekisteri.Kappale;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**kappaleen muokkaamisen hoitava luokka
 * 
 * @author Alexander Södergård
 * @version 20.2.2019
 *
 */
public class KappaleController implements ModalControllerInterface<Kappale> , Initializable  {

    @FXML private TextField editKappale;
    @FXML private TextField kesto;

    @FXML void handlePeruuta() {
        ModalController.closeStage(editKappale);
    }

    @FXML void handleTallenna() {
        ModalController.closeStage(editKappale);
    }
///------------------------------------------------------------------------------------------
    
    
    @Override public void handleShown() {
        // 
    }

    @Override 
    public void initialize(URL url, ResourceBundle bundle) {
        //alusta();    
    }

    @Override 
    public Kappale getResult() {
        return null;
    }

    @Override 
    public void setDefault(Kappale oletus) {
//        kappaleKohdalla = oletus;
  //      lisaaKappale(kappaleKohdalla);
    }


    /**
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataa näytetään oletuksena
     * @return null jos painetaan cancel, muuten kappale tiedoilla
     */
    public static Kappale kysyKappale(Stage modalityStage, Kappale oletus) {
        return ModalController.showModal(
                AlbumiController.class.getResource("KappaleDialogView.fxml"),
                "Albumirekisteri",
                modalityStage, oletus, null 
            );
    }

}
