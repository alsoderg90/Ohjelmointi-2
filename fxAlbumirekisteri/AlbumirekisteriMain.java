package fxAlbumirekisteri;
	
import albumirekisteri.Omistaja;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/***
 * 
 * @author Alexander Södergård
 * @version 5.3.2019
 *
 */

public class AlbumirekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("AlbumirekisteriGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final AlbumirekisteriViewController AlbumirekisteriCtrl = (AlbumirekisteriViewController)ldr.getController();
         
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("albumirekisteri.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Albumirekisteri");
           
            // Platform.setImplicitExit(false); // tätä ei kai saa laittaa
            primaryStage.setOnCloseRequest((event) -> {
                    if ( !AlbumirekisteriCtrl.voikoSulkea() ) event.consume();
                });
           
            Omistaja mina = new Omistaja(); 
            AlbumirekisteriCtrl.setOmistaja(mina); 
           
            primaryStage.show();
            
            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                AlbumirekisteriCtrl.lueTiedosto(params.getRaw().get(0)); 
            else
                if ( !AlbumirekisteriCtrl.avaa() ) Platform.exit();
                        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	/***
	 * 
	 * @param args Ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
