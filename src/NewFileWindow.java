import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewFileWindow {

	private Stage window;
	private Stage srcWindow;
	private int mode = 0; // 0-> New Font File 1-> New Text File
	
	public NewFileWindow(int mode, Stage srcWindow) {
		this.srcWindow = srcWindow;
		window = new Stage();
		this.mode = mode;
		
		VBox root = getRoot();
		
		Scene scene = new Scene(root, 650, 300);
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("New File");
		window.initModality(Modality.APPLICATION_MODAL);
	}
	
	public void display() {
		window.showAndWait();
	}
	
	String fileDir = "";
	public VBox getRoot() {
		VBox root = new VBox(15);
		root.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;-fx-font-weight:bold;");
		root.setAlignment(Pos.CENTER);
		
		Label titleLbl = new Label("Create New "+(mode==0?"Font":"Text") +" File");
		titleLbl.setPadding(new Insets(0,0,25,0));
		titleLbl.setStyle("-fx-font-size:22px");
		titleLbl.setTextFill(Color.WHITE);
		
		HBox nameBox = new HBox(10);
		VBox.setMargin(nameBox, new Insets(0,0,0,75));
		nameBox.setPrefWidth(500);
		nameBox.setAlignment(Pos.CENTER_LEFT);
		
		Label nameLbl = new Label("Name");
		nameLbl.setPrefWidth(100);
		nameLbl.setTextFill(Color.WHITE);
		
		TextField nameField = new TextField();
		nameField.setPrefWidth(300);
		nameField.setPromptText("Enter File Name");
		
		nameBox.getChildren().addAll(nameLbl, nameField);
		
		HBox dirBox = new HBox(10);
		VBox.setMargin(dirBox, new Insets(0,0,0,75));
		dirBox.setPrefWidth(500);
		dirBox.setAlignment(Pos.CENTER_LEFT);
		
		Label dirLbl = new Label("Save Dir");
		dirLbl.setPrefWidth(100);
		dirLbl.setTextFill(Color.WHITE);
		
		TextField dirField = new TextField();
		dirField.setPrefWidth(300);
		dirField.setEditable(false);
		dirField.setPromptText("Select Save Directory");
		
		Button browseBtn = new Button(" Browse");
		InitMethods.initBtn(browseBtn, "browse", "Select File", 35, 30, 140, 40);
		browseBtn.setOnAction(e-> {
			String name = nameField.getText();
			if (!name.isEmpty()&&!name.trim().isEmpty()) {
				DirectoryChooser chooser = new DirectoryChooser();
				try {
					File dirFile = chooser.showDialog(window);
					if (dirFile.exists()) {
						String dirStr = dirFile.getAbsolutePath();
						this.fileDir = dirStr+"/"+name+(mode==0?".font":".text");
						dirField.setText(fileDir);
					}
				} catch (NullPointerException ex) {
				}
			} else {
				new ErrorAlert("Enter File Name");
			}
		});
		
		dirBox.getChildren().addAll(dirLbl, dirField, browseBtn);
		
		Button saveBtn = new Button("Create File");
		InitMethods.initHover(saveBtn);
		saveBtn.setAlignment(Pos.CENTER);
		saveBtn.setOnAction(e->{
			if (!fileDir.isEmpty()) {
				try {
					PrintWriter out = new PrintWriter(fileDir);
					out.print("");
					out.close();
					if (mode==0) {
						FontWindow fW = new FontWindow(fileDir, srcWindow);
						fW.display();
						srcWindow.close();
					} else {
						SelFontWindow fW = new SelFontWindow("");
						fW.display();
						if (!fW.getDir().isEmpty()) {
							TextWindow tW = new TextWindow(fileDir, fW.getDir(), srcWindow);
							tW.display();
							window.close();
							srcWindow.close();
						} else {
							srcWindow.show();
						}
					}
					window.close();
				} catch (FileNotFoundException ex) {
					new ErrorAlert("Directory Not Found!");
				}
			} else {
				new ErrorAlert("Select Directory!");
			}
		});
		
		root.getChildren().addAll(titleLbl, nameBox, dirBox, saveBtn);
		
		return root;
	}
		
}
