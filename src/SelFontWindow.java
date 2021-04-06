import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class SelFontWindow {

	private String fileDir="";
	private Stage window;
	public SelFontWindow(String fileDir) {
		this.fileDir = fileDir;
		
		window = new Stage();
		VBox root = getRoot();
		Scene scene = new Scene(root, 700, 200);
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("Select Font File");
		window.initModality(Modality.APPLICATION_MODAL);
	}
	
	public void display() {
		window.showAndWait();
	}
	
	String newFileDir = "";
	public VBox getRoot() {
		VBox root = new VBox(20);
		root.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;-fx-font-weight:bold;");
		root.setAlignment(Pos.CENTER);
		
		Label titleLbl = new Label("Font File");
		titleLbl.setTextFill(Color.WHITE);
		titleLbl.setStyle("-fx-font-size:24px");
		
		HBox fontBox = new HBox(15);
		fontBox.setAlignment(Pos.CENTER);
		
		Label fontLbl = new Label("Font Dir");
		fontLbl.setTextFill(Color.WHITE);
		TextField fontField = new TextField(fileDir);
		fontField.setPromptText("Select Font File");
		fontField.setPrefWidth(300);
		fontField.setEditable(false);
		
		Button browseBtn = new Button(" Browse");
		InitMethods.initBtn(browseBtn, "browse", "Select Font File", 35, 30, 160, 40);
		browseBtn.setOnAction(e-> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Font", "*.font"));
			File fontFile = chooser.showOpenDialog(window);
			try {
				if (fontFile.exists()) {
					boolean valid = checkValidity(fontFile);
					if (valid) {
						newFileDir = fontFile.getAbsolutePath(); 
						fontField.setText(newFileDir);
					} else {
						new ErrorAlert("File not valid!");
					}
				} else {
					new ErrorAlert("File Not Found!");
				}
			} catch (NullPointerException ex) {
				new ErrorAlert("File Not Found!");
			}
		});
		
		fontBox.getChildren().addAll(fontLbl, fontField, browseBtn);
		
		Button confirmBtn = new Button(" Confirm");
		InitMethods.initBtn(confirmBtn, "Confirm", "Confirm Font File", 35, 30, 160, 40);
		confirmBtn.setOnAction(e-> {
			if (!newFileDir.isEmpty()) {
				fileDir = newFileDir;
				window.close();
			} else {
				new ErrorAlert("Select a file first!");
			}
		});
		
		root.getChildren().addAll(titleLbl, fontBox, confirmBtn);
		return root;
	}
	public boolean checkValidity(File file) {
		boolean valid = true;
		try {
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) {
				String[] ch = in.nextLine().split(" ");
				if (ch.length!=3) {
					valid = false;
					break;
				} else {
					for (int i=1; i<3; i++) {
						if (ch[i].length()!=64&&ch[i].charAt(0)!='0') {
							valid = false;
							break;
						}
					}
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			new ErrorAlert("File Not Found");
		}
		return valid;
	}
	public String getDir() {
		return this.fileDir;
	}
}
