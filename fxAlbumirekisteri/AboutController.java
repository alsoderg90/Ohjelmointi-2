package fxAlbumirekisteri;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**Luokka lisätietoaikkunalle
 * 
 * @author Alexander Södergård
 * @version 20.2.2019
 *
 */
public class AboutController implements ModalControllerInterface<String> {

    @FXML
    private BorderPane borderPane;
    
    
    @FXML
    void handleOK() {
        ModalController.closeStage(borderPane);
    }
    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void setDefault(String oletus) {
        // TODO Auto-generated method stub
        
    }

}



