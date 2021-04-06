import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FontWindow {

	private Stage window;
	private Stage mainWindow;
	private ArrayList<CustomChar> chList;
	private String fileDir;
	private CustomChar selectedChar;
	
	public FontWindow(String fileDir, Stage mainWindow) {
		window = new Stage();
		this.mainWindow = mainWindow;
		this.fileDir = fileDir;
		
		chList = new ArrayList<>();
		
		HBox root = getRoot();
		
		extractContent();
		fillList();

		window.setOnCloseRequest(e->{
			e.consume();
			reqClose();
		});
		
		Scene scene = new Scene(root, 900, 750);
		window.setScene(scene);
		window.setTitle("Font Editor");
		window.setResizable(false);
	}
	
	public void reqClose() {
		ConfirmAlert alert = new ConfirmAlert("Are you sure you want to quit? (Any unsaved changes will be lost)");
		boolean confirm = alert.showAlert();
		if (confirm) {
			window.close();
			mainWindow.show();
		}
	}
	
	public void extractContent() {
		try {
			Scanner in = new Scanner(new File(fileDir));
			while (in.hasNextLine()) {
				String[] ch = in.nextLine().split(" ");
				if (ch.length==3) {
					chList.add(new CustomChar(ch[0].charAt(0), ch[1], ch[2]));
				} else {
					window.close();
					mainWindow.show();
					new ErrorAlert("Invalid Font File");
					break;
				}
			}
			if (chList.size()>0) {
				selectedChar = chList.get(0);
				chListView.getSelectionModel().select(0);
				ScanMethods.drawChar(chList.get(0).getLcCode(), canvas);
				canvas.setEnabled(true);
			}
			in.close();
		} catch (FileNotFoundException ex) {
			new ErrorAlert("File Not Found");
		}
	}
	
	public void fillList() {
		chListView.getItems().clear();
		for (int i=0; i<chList.size(); i++) {
			chListView.getItems().add(chList.get(i).getCh());
		}
	}
	
	public void display() {
		window.show();
	}
	
	private HBox getRoot() {
		HBox root = new HBox();
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;");
		
		VBox leftBox = getLeftBox();
		
		VBox centerBox = getCenterBox();
		
		VBox rightBox = getRightBox();
		
		root.getChildren().addAll(leftBox, centerBox, rightBox);
		return root;
	}
	
	RadioButton lcRadio;
	RadioButton ucRadio;
	FontCanvas canvas;
	private VBox getLeftBox() {
		VBox root = new VBox(15);
		HBox.setMargin(root, new Insets(50,0,0,0));
		
		Label titleLbl = new Label("Draw Character");
		titleLbl.setStyle("-fx-font-size: 22px; -fx-font-weight:bold");
		titleLbl.setTextFill(Color.WHITE);
		
		StackPane canvBg = new StackPane();
		canvBg.setStyle("-fx-background-color:#eee");
		canvas = new FontCanvas(480, 480);
		canvas.init();
		canvBg.getChildren().add(canvas);
		
		HBox caseBox = new HBox(20);
		lcRadio = new RadioButton("Lower Case");
		ucRadio = new RadioButton("Upper Case");
		ToggleGroup caseGroup = new ToggleGroup();
		lcRadio.setToggleGroup(caseGroup);
		lcRadio.setSelected(true);
		ucRadio.setToggleGroup(caseGroup);
		lcRadio.setTextFill(Color.WHITE);
		ucRadio.setTextFill(Color.WHITE);
		caseBox.getChildren().addAll(lcRadio, ucRadio);
		
		lcRadio.setOnAction(e->{ 
			if (canvas.getEnabled()) {
			if (lcRadio.isSelected()) {
					ConfirmAlert alert = new ConfirmAlert("Confirm Upper Case Letter?");
					boolean confirmed = alert.showAlert();
					if (confirmed) {
						String charHex = ScanMethods.getHexPixelVals(canvas);
						selectedChar.setUcCode(charHex);
					}
					canvas.clear();
					if (!selectedChar.getLcCode().equals("0")) {
						ScanMethods.drawChar(selectedChar.getLcCode(), canvas);
					}
				}
			} else {
				lcRadio.setSelected(false);
				ucRadio.setSelected(true);
				new ErrorAlert("Add Character First");
			}
		});
		
		ucRadio.setOnAction(e->{ 
			if (canvas.getEnabled()) {
				if (ucRadio.isSelected()) {
					ConfirmAlert alert = new ConfirmAlert("Confirm Lower Case Letter?");
					boolean confirmed = alert.showAlert();
					if (confirmed) {
						String charHex = ScanMethods.getHexPixelVals(canvas);
						selectedChar.setLcCode(charHex);
					}
					canvas.clear();
					if (!selectedChar.getUlCode().equals("0")) {
						ScanMethods.drawChar(selectedChar.getUlCode(), canvas);
					}
				}
			} else {
				lcRadio.setSelected(true);
				ucRadio.setSelected(false);
				new ErrorAlert("Add Character First");
			}
		});
		
		Button confirmBtn = new Button(" Confirm", new ImageView(new Image("rcs/confirm.png")));
		confirmBtn.setStyle("-fx-font-weight:bold");
		confirmBtn.setTextFill(Color.WHITE);
		InitMethods.initHover(confirmBtn);
		
		confirmBtn.setOnAction(e-> {
			if (canvas.getEnabled()) {
				String sel = "";
				if (lcRadio.isSelected()) {
					sel = "Lower Case";
				} else {
					sel = "Upper Case";
				}
				ConfirmAlert alert = new ConfirmAlert("Confirm "+sel+" Letter?");
				boolean confirmed = alert.showAlert();
				if (confirmed) {
					String charHex = ScanMethods.getHexPixelVals(canvas);
					if (lcRadio.isSelected()) {
						selectedChar.setLcCode(charHex);
					} else {
						selectedChar.setUcCode(charHex);
					}
				}
			} else {
				new ErrorAlert("Add Character First");
			}
		});
		
		VBox.setMargin(confirmBtn, new Insets(15,0,0,0));
		
		root.getChildren().addAll(titleLbl, canvBg, caseBox, confirmBtn);
		return root;
	}
	
	//exists -> return index / else return -1
	private int charIndex(char ch) {
		int index = -1;
		for (int i=0; i<chList.size(); i++) {
			if (chList.get(i).getCh()==ch) {
				index = i;
				break;
			}
		}
		return index;
	}

	private VBox getCenterBox() {
		VBox root = new VBox(10);
		HBox.setMargin(root, new Insets(91,0,0,0));
		
		Button penBtn = new Button();
		InitMethods.initBtn(penBtn, "pen", "Pen",30,30,40,40);
		
		Button eraserBtn = new Button();
		InitMethods.initBtn(eraserBtn, "eraser", "Eraser",30,30,40,40);
		eraserBtn.setOpacity(0.5);
		
		penBtn.setOnAction(e-> {
			penBtn.setOpacity(1);
			eraserBtn.setOpacity(0.5);
			canvas.setMode(0);
		});
		eraserBtn.setOnAction(e-> {
			penBtn.setOpacity(0.5);
			eraserBtn.setOpacity(1);
			canvas.setMode(1);
		});
		
		Button clearBtn = new Button();
		InitMethods.initBtn(clearBtn, "clear", "Clear",30,30,40,40);
		clearBtn.setOnAction(e-> {
			ConfirmAlert alert = new ConfirmAlert("Clear Canvas?");
			boolean confirmed = alert.showAlert();
			if (confirmed) {
				canvas.clear();
				penBtn.setOpacity(1);
				eraserBtn.setOpacity(0.5);
				canvas.setMode(0);
			}
		});
		
		root.getChildren().addAll(penBtn, eraserBtn, clearBtn);
		return root;
	}
	
	ListView<Character> chListView;
	private VBox getRightBox() {
		VBox root = new VBox(10);
		HBox.setMargin(root, new Insets(50,0,0,50));
		
		Label titleLbl = new Label("Characters");
		titleLbl.setStyle("-fx-font-size: 22px; -fx-font-weight:bold");
		titleLbl.setTextFill(Color.WHITE);
		
		chListView = new ListView<>();
		chListView.setPrefHeight(500);
		chListView.setStyle("-fx-background-color:#555");
		chListView.getSelectionModel().selectedItemProperty().addListener(e-> {
			if (chListView.getSelectionModel().getSelectedItem()!=null) {
				canvas.clear();
				int index = charIndex(chListView.getSelectionModel().getSelectedItem());
				selectedChar = chList.get(index);
				if (!selectedChar.getLcCode().equals("0")) {
					lcRadio.setSelected(true);
					ucRadio.setSelected(false);
					ScanMethods.drawChar(selectedChar.getLcCode(), canvas);
				} else if (!selectedChar.getUlCode().equals("0")) {
					lcRadio.setSelected(false);
					ucRadio.setSelected(true);
					ScanMethods.drawChar(selectedChar.getUlCode(), canvas);
				} else {
					lcRadio.setSelected(true);
					ucRadio.setSelected(false);
				}
			}
		});
		
		HBox btnsBox = new HBox(10);
		Button addBtn = new Button();
		InitMethods.initBtn(addBtn, "add", "New Character",30,30,40,40);
		addBtn.setOnAction(e-> {
			GetCharWindow charWindow = new GetCharWindow();
			char ch = charWindow.getChar();
			if (ch!='\n') {
				if (charIndex(ch)==-1) {
					CustomChar newChar = new CustomChar(ch);
					selectedChar = newChar;
					canvas.setEnabled(true);
					canvas.clear();
					chList.add(newChar);
					fillList();
					chListView.getSelectionModel().select(chList.size()-1);
				} else {
					new ErrorAlert("Character Already Exists!");
				}
			}
		});
		
		Button removeBtn = new Button();
		InitMethods.initBtn(removeBtn, "remove", "Remove Character",30,10,40,40);
		removeBtn.setOnAction(e-> {
			ConfirmAlert removeAlert = new ConfirmAlert("Are you sure you want to remove "+selectedChar.getCh()+"?");
			boolean confirmed = removeAlert.showAlert();
			if (confirmed) {
				int index = charIndex(selectedChar.getCh());
				chList.remove(index);
				fillList();
				canvas.clear();
				if (chList.size()>0) {
					index-=1;
					if (index<0) {
						index = 0;
					}
					chListView.getSelectionModel().select(index);
					selectedChar = chList.get(index);
					if (!selectedChar.getLcCode().equals("0")) {
						lcRadio.setSelected(true);
						ucRadio.setSelected(false);
						ScanMethods.drawChar(selectedChar.getLcCode(), canvas);
					} else if (!selectedChar.getUlCode().equals("0")) {
						lcRadio.setSelected(false);
						ucRadio.setSelected(true);
						ScanMethods.drawChar(selectedChar.getUlCode(), canvas);
					} else {
						lcRadio.setSelected(true);
						ucRadio.setSelected(false);
					}
				} else {
					canvas.setEnabled(false);
				}
			}
		});
		
		Button resetBtn = new Button();
		HBox.setMargin(resetBtn, new Insets(0,0,0,65));
		InitMethods.initBtn(resetBtn, "clear", "Reset Characters",30,30,40,40);
		resetBtn.setOnAction(e-> {
			ConfirmAlert resetAlert = new ConfirmAlert("Are you sure you want to delete all assigned characters?");
			boolean confirmed = resetAlert.showAlert();
			if (confirmed) {
				chList.clear();
				fillList();
				canvas.clear();
				canvas.setEnabled(false);
			}
		});
		
		btnsBox.getChildren().addAll(addBtn, removeBtn, resetBtn);
		
		File f = new File(fileDir);
		Label nameLbl = new Label(f.getName());
		nameLbl.setWrapText(true);
		nameLbl.setStyle("-fx-font-size: 22px; -fx-font-weight:bold");
		nameLbl.setTextFill(Color.WHITE);
		
		Button saveBtn = new Button(" Save");
		InitMethods.initBtn(saveBtn, "browse", "Save Font File", 35, 30, 120, 40);
		saveBtn.setOnAction(e-> {
			 saveFile();
		});
		
		root.getChildren().addAll(titleLbl, chListView, btnsBox, nameLbl, saveBtn);
		return root;
	}
	
	public void saveFile() {
		try {
			PrintWriter writer = new PrintWriter(fileDir);
			for (int i=0; i<chList.size(); i++) {
				CustomChar ch = chList.get(i);
				writer.print(ch.getCh()+" "+ch.getLcCode()+" "+ch.getUlCode()+"\n");
			}
			writer.close();
			Alert successAlert = new Alert(AlertType.INFORMATION, "File Saved Successfully", ButtonType.OK);
			successAlert.show();
		} catch (FileNotFoundException ex) {
			
		}
	}
	
}
