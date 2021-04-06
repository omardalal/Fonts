
public class CustomChar {

	private char ch;
	private String lcCode="0";
	private String ucCode="0";
	
	public CustomChar(char ch) {
		super();
		this.ch = ch;
	}
	public CustomChar(char ch, String lcCode, String ucCode) {
		super();
		this.ch = Character.toUpperCase(ch);
		this.lcCode = lcCode;
		this.ucCode = ucCode;
	}
	
	public char getCh() {
		return ch;
	}
	public void setCh(char ch) {
		this.ch = Character.toUpperCase(ch);
	}
	public String getLcCode() {
		return lcCode;
	}
	public void setLcCode(String lcCode) {
		this.lcCode = lcCode;
	}
	public String getUlCode() {
		return ucCode;
	}
	public void setUcCode(String ucCode) {
		this.ucCode = ucCode;
	}
	@Override
	public String toString() {
		return "CustomChar [ch=" + ch + ", lcCode=" + lcCode + ", ucCode=" + ucCode + "]";
	}
}
