import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class WelcomeWindow extends VBox {

	private Stage window;
	public WelcomeWindow(Stage window) {
		super();
		getWelcomeBox();
		this.window = window;
	}
	
	public void getWelcomeBox() {
		super.setAlignment(Pos.CENTER);
		super.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;-fx-font-weight:bold;");
		Label titleLbl = getTitleLbl();
		
		HBox fontBox = new HBox(10);
		fontBox.setAlignment(Pos.CENTER);
		
		Label fontLbl = new Label("Font Files");
		fontLbl.setStyle("-fx-font-size: 22px; -fx-font-weight:bold");
		fontLbl.setTextFill(Color.WHITE);
		
		Button newFontBtn = new Button("  Create\n  Font");
		InitMethods.initBtn(newFontBtn, "add", "New Font File", 45,45,160,100);
		newFontBtn.setOnAction(e->{
			NewFileWindow fW = new NewFileWindow(0, window);
			fW.display();
		});
		
		Button editFontBtn = new Button("  Edit\n  Font");
		InitMethods.initBtn(editFontBtn, "edit", "Edit Font File", 45,45,160,100);
		editFontBtn.setOnAction(e->{
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Font", "*.font"));
			File selectedFile = chooser.showOpenDialog(window);
			try {
				if (selectedFile.exists()) {
					boolean valid = checkValidity(selectedFile);
					if (valid) {
						FontWindow fW = new FontWindow(selectedFile.getAbsolutePath(), window);
						fW.display();
						window.close();
					} else {
						new ErrorAlert("File Not Valid");
					}
				} else {}
			} catch (NullPointerException ex) {}
		});
		
		VBox.setMargin(fontBox, new Insets(0,0,15,0));
		fontBox.getChildren().addAll(newFontBtn, editFontBtn);
		
		HBox txtBox = new HBox(10);
		txtBox.setAlignment(Pos.CENTER);
		
		Label txtLbl = new Label("Text Files");
		txtLbl.setStyle("-fx-font-size: 22px; -fx-font-weight:bold");
		txtLbl.setTextFill(Color.WHITE);
		
		Button newTxtBtn = new Button("  Create\n  Text");
		InitMethods.initBtn(newTxtBtn, "add","New Text File", 45,45,160,100);
		newTxtBtn.setOnAction(e-> {
			NewFileWindow nW = new NewFileWindow(1, window);
			nW.display();
		});
		
		Button editTxtBtn = new Button("  Edit\n  Text");
		InitMethods.initBtn(editTxtBtn, "edit", "Edit Text File", 45,45,160,100);
		editTxtBtn.setOnAction(e-> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Text", "*.text"));
			File selectedFile = chooser.showOpenDialog(window);
			try {
				if (selectedFile.exists()) {
					SelFontWindow fW = new SelFontWindow("");
					fW.display();
					if (!fW.getDir().isEmpty()) {
						TextWindow tW = new TextWindow(selectedFile.getAbsolutePath(), fW.getDir(), window);
						tW.display();
						window.close();
					}
				} else {}
			} catch (NullPointerException ex) {}
		});
		
		txtBox.getChildren().addAll(newTxtBtn, editTxtBtn);
		
		super.getChildren().addAll(titleLbl, fontLbl, fontBox, txtLbl, txtBox);
	}
	
	public boolean checkValidity (File file) {
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
	
	public Label getTitleLbl() {
		Label titleLbl = new Label("Welcome to Font Maker");
		titleLbl.setStyle("-fx-font-size:28px; -fx-font-weight:bold;");
		titleLbl.setAlignment(Pos.CENTER);
		VBox.setMargin(titleLbl, new Insets(0, 0, 50, 0));
		titleLbl.setTextFill(Color.WHITE);
		return titleLbl;
	}
}
