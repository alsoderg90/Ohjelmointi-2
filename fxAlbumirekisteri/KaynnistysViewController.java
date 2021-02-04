package fxAlbumirekisteri;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * käynnistysvalikon hoitava luokka
 *
 * @author Alexander Södergård
 * @version 20.2.2019
 */
public class KaynnistysViewController implements ModalControllerInterface<String> {


    @FXML private TextField textVastaus;
    private String vastaus = null;
    
    @FXML
    void handleOK() {
    vastaus = textVastaus.getText();
    ModalController.closeStage(textVastaus); 
    }

    @FXML
    void handlePeruuta() {      
        ModalController.closeStage(textVastaus);
    }

    @Override
    public String getResult() {
        return vastaus;
    }

    @Override
    public void handleShown() {
        textVastaus.requestFocus();
        
    }

    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
        
    }

    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä nimeä näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                KaynnistysViewController.class.getResource("KaynnistysView.fxml"),
                "Albumirekisteri",
                modalityStage, oletus);
    }
    
}
