import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TextWindow {

	private String inputDir;
	private Stage window;
	private Stage mainWindow;
	private String fontDir;
	private ArrayList<CustomChar> chList;
	private ArrayList<TextChar> chars;
	private boolean bold = false;
	private boolean underlined = false;
	private int currentWord = 0;
	private int charSize = 16;
	
	public TextWindow(String inputDir, String fontDir, Stage mainWindow) {
		window = new Stage();
		chList = new ArrayList<>();
		chars = new ArrayList<>();
		
		this.inputDir = inputDir;
		this.mainWindow = mainWindow;
		this.fontDir = fontDir;
		
		VBox root = getRoot();
		
		window.setOnCloseRequest(e-> {
			e.consume();
			close();
		});
		Scene scene = new Scene(root, 1025, 875);
		scene.setOnKeyPressed(e-> {
			keyPressed(e.getCode());
		});
		scene.setOnKeyTyped(e-> {
			if (e.getCharacter().charAt(0)==' ') {
				textCanv.incX();
				chars.add(new TextChar(' ', false, false, getHex(picker.getValue())));
				currentWord = 0;
			} else {
				drawChar(e.getCharacter().charAt(0), bold, underlined, getHex(picker.getValue()));
			}
		});
		window.setScene(scene);
		window.setTitle("Text File");
		window.setResizable(false);
	}
	
	private boolean close() {
		ConfirmAlert quitConfirm = new ConfirmAlert("Are you sure you want to quit? (Any unsaved changes will be lost)");
		boolean confirm = quitConfirm.showAlert();
		if (confirm) {
			window.close();
			mainWindow.show();
		}
		return confirm;
	}
	
	private void keyPressed(KeyCode code) {
		if (code == KeyCode.ENTER) {
			textCanv.startNewLine();
			chars.add(new TextChar('\n', false, false, getHex(picker.getValue())));
			currentWord = 0;
		} else if (code == KeyCode.BACK_SPACE) {
			boolean empty = detectEmpty(textCanv.getX(), textCanv.getY());
			do {
				try {
					textCanv.backspace();
					if ((empty&&(chars.get(chars.size()-1).getCh()==' '||chars.get(chars.size()-1).getCh()=='\n'))||!empty) {
						if (chars.size()>0) {
							chars.remove(chars.size()-1);
						}
						currentWord--;
						if (currentWord<0) {
							currentWord=0;
						}
					}
					empty = detectEmpty(textCanv.getX(), textCanv.getY());
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
			} while (empty&&!textCanv.atBeginning());
		}
	}
	
	private static boolean compareColors(Color color1, Color color2) {
		if (color1.getHue()==color2.getHue() && color1.getSaturation()==color2.getSaturation() && color1.getBrightness()==color2.getBrightness()) {
			return true;
		}
		return false;
	}

	private boolean detectEmpty(int x, int y) {
		WritableImage img = ScanMethods.canvToImg(textCanv, 1);
		PixelReader PR = img.getPixelReader();
		Color bgColor = Color.hsb(0, 0, 1);
		for (int cY = y; cY<y+((charSize/16)*10-1); cY++) {
			for (int cX = x; cX>x-charSize; cX--) {
				try {
					Color c = PR.getColor(cX, cY);
					if (!compareColors(c, bgColor)) {
						return false;
					}
				} catch (IndexOutOfBoundsException ex) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void init() {
		extractFont();
		if (!this.inputDir.isEmpty()) {
			try {
				FileInputStream fis = new FileInputStream(inputDir);
				int ch = 0;
				while ((ch=fis.read())!=-1) {
					int ul = fis.read();
					int b = fis.read();
					if (ul==-1||b==-1) {
						new ErrorAlert("Invalid Input File!");
						window.close();
						break;
					}
					if ((ul!='0'&&ul!='1')||(b!='0'&&b!='1')) {
						new ErrorAlert("Invalid Input File!");
						window.close();
						break;
					}
					boolean isUnderlined = ul=='0'?false:true;
					boolean isBold = b=='0'?false:true;
					String hexVal = "";
					for (int i=0; i<6; i++) {
						hexVal+=(char)fis.read();
					}
					if ((char)ch==' ') {
						currentWord=0;
						textCanv.incX();
						chars.add(new TextChar(' ', false, false, hexVal));
					} else if ((char)ch=='\n') {
						currentWord=0;
						textCanv.startNewLine();
						chars.add(new TextChar('\n', false, false, hexVal));
					} else {
						drawChar((char)ch, isBold, isUnderlined, hexVal);
					}
				}
				fis.close();
			} catch (FileNotFoundException ex) {
				System.out.println(ex);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}
	
	private void drawChar(char ch, boolean isBold, boolean isUnderlined, String hexColor) {
		int chIndex = charIndex(Character.toUpperCase(ch));
		if (chIndex!=-1) {
			if (textCanv.getX()==textCanv.getXStart()&&currentWord>0) {
				ArrayList<TextChar> wordVal = new ArrayList<>();
				textCanv.backspace();
				for (int i=0; i<currentWord; i++) {
					textCanv.backspace();
					if (chars.size()>0) {
						wordVal.add(chars.get(chars.size()-i-1));
					}
					
				}
				textCanv.resetX();
				textCanv.incY();
				for (int i=wordVal.size()-1; i>=0; i--) {
					int ind = charIndex(Character.toUpperCase(wordVal.get(i).getCh()));
					if (ind!=-1) {
						if (Character.isLowerCase(wordVal.get(i).getCh())||!Character.isLetter(wordVal.get(i).getCh())) {
							ScanMethods.drawChar(chList.get(ind).getLcCode(), textCanv, textCanv.getX(), textCanv.getY(), wordVal.get(i).isBold(), wordVal.get(i).isUnderLined(), wordVal.get(i).getHexColor(), charSize/16);
							textCanv.incX();
						} else {
							ScanMethods.drawChar(chList.get(ind).getUlCode(), textCanv, textCanv.getX(), textCanv.getY(), wordVal.get(i).isBold(), wordVal.get(i).isUnderLined(), wordVal.get(i).getHexColor(), charSize/16);
							textCanv.incX();
						}
					}
				}
			}
			if (Character.isLowerCase(ch)||!Character.isLetter(ch)) {
				String code = chList.get(chIndex).getLcCode();
				if (!code.equals("0")) {
					ScanMethods.drawChar(code, textCanv, textCanv.getX(), textCanv.getY(), isBold, isUnderlined, hexColor, charSize/16);
					textCanv.incX();
				}
			} else {
				String code = chList.get(chIndex).getUlCode();
				if (!code.equals("0")) {
					ScanMethods.drawChar(code, textCanv, textCanv.getX(), textCanv.getY(), isBold, isUnderlined, hexColor, charSize/16);
					textCanv.incX();
				}
			}
			chars.add(new TextChar(ch, isUnderlined, isBold, hexColor));
			currentWord++;
		}
	}

	private TextCanvas textCanv;
	public VBox getRoot() {
		VBox root = new VBox(10);
		root.setStyle("-fx-font-family: \"Courier New\"; -fx-font-size:18px; -fx-background-color: #333;-fx-font-weight:bold;");
		
		HBox topBox = new HBox(10);
		VBox.setMargin(topBox, new Insets(15,0,0,0));
		topBox.setAlignment(Pos.CENTER);
		BtnsBox fileBtns = getFileBtns();
		BtnsBox styleBtns = getStyleBtns();
		topBox.getChildren().addAll(fileBtns, styleBtns);
		
		ScrollPane textScroll = new ScrollPane();
		textScroll.setOnKeyPressed(e-> {
			if (e.getCode() == KeyCode.SPACE) {
				e.consume();
			}
		});
		textScroll.setPrefHeight(775);
		textScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		textCanv = new TextCanvas(charSize);
		textScroll.setContent(textCanv);
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	textCanv.requestFocus();
		    }
		});
		
		root.getChildren().addAll(topBox, textScroll);
		return root;
	}
	
	public BtnsBox getFileBtns() {
		BtnsBox fileBtns = new BtnsBox("File");
		Button newFileBtn = new Button(" New");
		InitMethods.initBtn(newFileBtn, "file", "Create New Text File", 15, 25, 95, 40);
		newFileBtn.setOnAction(e-> {
			if (close()) {
				NewFileWindow fW = new NewFileWindow(0, window);
				fW.display();
			}
		});
		
		Button openFileBtn = new Button(" Open");
		InitMethods.initBtn(openFileBtn, "browse", "Open Text File", 15, 25, 100, 40);
		openFileBtn.setOnAction(e-> {
			if (close()) {
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
			}
		});
		Button saveFileBtn = new Button(" Save");
		InitMethods.initBtn(saveFileBtn, "browse", "Save Text File", 15, 25, 100, 40);
		saveFileBtn.setOnAction(e-> {
			save();
		});
		
		Button quitFileBtn = new Button(" Quit");
		InitMethods.initBtn(quitFileBtn, "clear", "Quit Without Saving", 15, 25, 100, 40);
		quitFileBtn.setOnAction(e-> {
			close();
		});
		
		fileBtns.getRow().getChildren().addAll(newFileBtn, openFileBtn, saveFileBtn, quitFileBtn);
		return fileBtns;
	}
	private ColorPicker picker;
	private ComboBox<Integer> fontSizeCB;
	public BtnsBox getStyleBtns() {
		BtnsBox styleBtns = new BtnsBox("Style");
		picker = new ColorPicker();
		picker.setValue(Color.BLACK);
		picker.setPrefWidth(75);
		picker.setOnAction(e->textCanv.requestFocus());
		Button fontBtn = new Button(" Font");
		InitMethods.initBtn(fontBtn, "browse", "Change Font", 15, 25, 105, 40);
		fontBtn.setOnAction(e-> {
			SelFontWindow fW = new SelFontWindow(fontDir);
			fW.display();
			if (!fontDir.equals(fW.getDir())) {
				redraw(fW.getDir());
			}
			textCanv.requestFocus();
		});
		
		fontSizeCB = new ComboBox<>();
		fontSizeCB.setValue(16);
		fontSizeCB.setPrefWidth(100);
		fontSizeCB.getItems().addAll(16,32,48);
		fontSizeCB.setOnAction(e-> {
			charSize = fontSizeCB.getValue();
			redraw(fontDir);
		});
		
		CheckBox boldCB = new CheckBox("B");
		boldCB.setOnAction(e-> {
			bold = boldCB.isSelected();
			textCanv.requestFocus();
		});
		boldCB.setTextFill(Color.WHITE);
		boldCB.setPrefHeight(40);
		CheckBox underLineCB = new CheckBox("U");
		underLineCB.setOnAction(e-> {
			underlined = underLineCB.isSelected();
			textCanv.requestFocus();
		});
		underLineCB.setTextFill(Color.WHITE);
		underLineCB.setPrefHeight(40);
		styleBtns.getRow().getChildren().addAll(fontBtn, picker, fontSizeCB, boldCB, underLineCB);
		return styleBtns;
	}
	
	private void redraw (String dir) {
		chList.clear();
		chList = new ArrayList<>();
		fontDir = dir;
		extractFont();
		textCanv.setSize(charSize);
		textCanv.reset();
		currentWord=0;
		ArrayList<TextChar> textCopy = new ArrayList<>(chars);
		chars.clear();
		for (int i=0; i<textCopy.size(); i++) {
			if (textCopy.get(i).getCh()==' ') {
				textCanv.incX();
				currentWord=0;
				chars.add(new TextChar(' ', false, false, getHex(picker.getValue())));
			} else if (textCopy.get(i).getCh()=='\n') {
				textCanv.startNewLine();
				currentWord=0;
				chars.add(new TextChar('\n', false, false, getHex(picker.getValue())));
			} else {
				TextChar ch = textCopy.get(i);
				drawChar(ch.getCh(), ch.isBold(), ch.isUnderLined(), ch.getHexColor());
			}
		}
	}
	
	private void extractFont() {
		try {
			Scanner in = new Scanner(new File(fontDir));
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
			in.close();
		} catch (FileNotFoundException ex) {
			new ErrorAlert("File Not Found");
		}
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
	
	public void save() {
		try {
			PrintWriter writer = new PrintWriter(inputDir);
			for (int i=0; i<chars.size(); i++) {
				writer.print(chars.get(i).getFileCode());
			}
			writer.close();
			Alert alert = new Alert(AlertType.INFORMATION, "File Saved Successfully", ButtonType.OK);
			alert.show();
		} catch (FileNotFoundException ex) {
			new ErrorAlert("File Not Found!");
		}
	}

	public void display() {
		window.show();
		init();
	}
	
	private String getHex(Color color){
		int r = (int)(color.getRed()*255);
		int g = (int)(color.getGreen()*255);
		int b = (int)(color.getBlue()*255);
        return String.format("%02X%02X%02X", r, g, b);
    }
	
}
