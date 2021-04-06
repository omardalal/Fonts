import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TextCanvas extends Canvas {
	
	private int lineY = 50;
	private int currentY;
	private int currentX;
	
	private int xStart = 0;
	private int yStart;
	private int charSize;
	public TextCanvas(int charSize) {
		super(1000, 750);
		this.charSize = charSize;
		yStart = lineY-(charSize/16*10);
		init();
	}
	
	public void setSize(int size) {
		this.charSize = size;
	}
	
	public int getXStart() {
		return xStart;
	}
	
	public void backspace() {
		if (currentX<=xStart) {
			if (currentY != yStart) {
				currentX = 960+charSize;
			}
			decY();
		}
		GraphicsContext GC = super.getGraphicsContext2D();
		GC.clearRect(currentX-charSize, currentY, charSize, 50);
		GC.setStroke(Color.web("81a7e3"));
		GC.strokeLine(currentX-charSize, currentY+((charSize/16)*10), currentX, currentY+((charSize/16)*10));
		decX();
	}
	
	public boolean atBeginning() {
		if (currentX == xStart && currentY == yStart) {
			return true;
		}
		return false;
	}
	
	public void init() {
		for (int i=0; i<14; i++) {
			newLine();
		}
		currentX = xStart;
		currentY = yStart;
	}
	
	public void startNewLine( ) {
		incY();
		currentX = xStart;
	}
	
	public void newLine() {
		GraphicsContext GC = super.getGraphicsContext2D();
		GC.setStroke(Color.web("81a7e3"));
		GC.strokeLine(15, lineY, 985, lineY);
		lineY+=50;
	}
	
	public void clear() {
		super.getGraphicsContext2D().clearRect(0, 0, super.getWidth(), super.getHeight());
		super.setHeight(750);
		lineY = 50;
		currentY = yStart;
		currentX = xStart;
		init();
	}
	
	public void incX() {
		if (currentX>=960) {
			currentX = xStart;
			incY();
		} else {
			currentX+=charSize;
		}
	}
	
	public void incY() {
		currentY+=50;
		if (currentY>=super.getHeight()-200) {
			newLine();
			super.setHeight(super.getHeight()+50);
		}
	}
	
	public void decX() {
		currentX-=charSize;
		if (currentX<xStart) {
			currentX=xStart;
		}
	}
	
	public void decY() {
		currentY-=50;
		if (currentY<yStart) {
			currentY=yStart;
		}
	}
	
	public void reset() {
		clear();
		resetLine();
		resetYstart();
		resetX();
		resetY();
	}
	
	public int getX() {
		return this.currentX;
	}
	
	public void resetX() {
		this.currentX = xStart;
	}
	
	public void resetLine() {
		lineY = 50;
	}
	
	public void resetYstart() {
		yStart = lineY-(charSize/16*10);
	}
	
	public void resetY() {
		this.currentY = yStart;
	}
	
	public int getY() {
		return this.currentY;
	}
}
