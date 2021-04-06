import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GetCharWindow {

	private char ch='\n';
	public GetCharWindow() {
		Stage window = new Stage();
		HBox root = new HBox(5);
		root.setAlignment(Pos.CENTER);
		HBox.setMargin(root, new Insets(50,0,0,0));
		
		root.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;-fx-font-weight:bold;");
		
		Label lbl = new Label("Type Character");
		lbl.setTextFill(Color.WHITE);
		
		TextField field = new TextField();
		field.setPrefColumnCount(3);
		field.textProperty().addListener((observable, oldValue, newValue)-> {
			if (!newValue.isEmpty()) {
				field.setText(newValue.substring(0,1).toUpperCase());
			}
		});
		
		Button confirmBtn = new Button(" Confirm");
		InitMethods.initBtn(confirmBtn, "confirm", "Confirm Character", 35, 15, 135, 40);
		confirmBtn.setOnAction(e-> {
			if (!field.getText().trim().isEmpty()) {
				this.ch = field.getText().charAt(0);
				window.close();
			}
		});
		
		root.getChildren().addAll(lbl, field, confirmBtn);
		Scene scene = new Scene(root, 500, 200);
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("Enter Character");
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();
	}
	
	public char getChar() {
		return this.ch;
	}
	
}
