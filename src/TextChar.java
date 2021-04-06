
public class TextChar {

	private char ch;
	private boolean underLined;
	private boolean bold;
	private String hexColor;
	
	public TextChar(char ch, boolean underLined, boolean bold, String hexColor) {
		super();
		this.ch = ch;
		this.underLined = underLined;
		this.bold = bold;
		this.hexColor = hexColor;
	}

	public char getCh() {
		return ch;
	}

	public void setCh(char ch) {
		this.ch = ch;
	}

	public boolean isUnderLined() {
		return underLined;
	}

	public void setUnderLined(boolean underLined) {
		this.underLined = underLined;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}

	public String getFileCode() {
		return ch+(underLined==true?"1":"0")+(bold==true?"1":"0")+hexColor;
	}
}
