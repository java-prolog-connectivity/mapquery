package org.jpc.examples.osm.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Apparently simple dialogs are not supported in Javafx 2.2 (although they seemed to be scheduled for a future release).
 * http://stackoverflow.com/questions/11794987/what-is-replacement-of-alert-in-javafx-2-1-alert-is-in-javafx-1-3-but-not-in-j
 * http://stackoverflow.com/questions/8309981/how-to-create-and-show-common-dialog-error-warning-confirmation-in-javafx-2
 * 
 * In the meanwhile this class creates a simple dialog. It should be replaced when a simple native javafx alternative is available in the near future
 * @author sergioc
 *
 */
public class SimpleDialog extends Stage {

	public static final Font MESSAGE_FONT = Font.font("Helvetica", FontWeight.NORMAL, 14);
	public static final Color BACKGROUND_COLOR =  Color.web("#AAC1DF"); //TODO change to use style sheets
	
	public static final String WARNING_TITLE = "Warning";
	public static final String ERROR_TITLE = "Error";
	
	public static SimpleDialog warning(String message) {
		return new SimpleDialog(WARNING_TITLE, message);
	}
	
	public static SimpleDialog error(String message) {
		return new SimpleDialog(ERROR_TITLE, message);
	}
	
	public SimpleDialog(String title, String message) {
		
		initModality(Modality.WINDOW_MODAL);
		initStyle(StageStyle.UTILITY );
        initModality( Modality.APPLICATION_MODAL );
        setResizable( false );
        this.setTitle(title);
		Button okButton = new Button("Ok");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle( ActionEvent e ) {
				close();
			}
		});
		Text textMessage = new Text(message);
		Scene scene = new Scene(VBoxBuilder.create().children(textMessage, okButton).alignment(Pos.CENTER).spacing(10).padding(new Insets(10)).build());
		scene.setFill(BACKGROUND_COLOR);
		textMessage.setFont(MESSAGE_FONT);
		setScene(scene);
	}
	
	public void showDialog() {
        sizeToScene();
        centerOnScreen();
        showAndWait();
    }
	
}
