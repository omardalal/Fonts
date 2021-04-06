//Omar Dalal 1180171 Section 2

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

//Will be used to display an error message to user when needed
public class ErrorAlert extends Alert {
	public ErrorAlert(String msg) {
		super(AlertType.ERROR, msg, ButtonType.OK);
		super.show();
	}
}
