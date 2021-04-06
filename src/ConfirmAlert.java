import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConfirmAlert {

	private Alert alert;
	public ConfirmAlert(String msg) {
		alert = new Alert(AlertType.CONFIRMATION, msg);
		alert.setHeaderText("Confirm");
	}
	
	public boolean showAlert() {
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
}
