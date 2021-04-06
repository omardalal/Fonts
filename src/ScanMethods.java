import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class ScanMethods {

	private static String[] scan(Canvas canvas) {
		String[] pixelVals = new String[16];
		for (int i=0; i<pixelVals.length; i++) {
			pixelVals[i] = "";
		}

		WritableImage img = canvToImg(canvas, 1);
		PixelReader PR = img.getPixelReader();
		
		int row = 0;
		int y = 15;
		while (row<16) {
			int column = 0;
			int x = 15;
			while (column<16) {
				if (PR.getColor(x, y).equals(Color.WHITE)) {
					pixelVals[row]+=0;
				} else {
					pixelVals[row]+=1;
				}
				x+=30;
				column++;
			}
			y+=30;
			row++;
		}
		return pixelVals;
	}
	
	public static String getHexPixelVals(Canvas canvas) {
		String[] binaryPixelVals = scan(canvas);
		String hexPixelVals = "";
		
		for (int i=0; i<binaryPixelVals.length; i++) {
			String binary = "";
			for (int j=0; j<binaryPixelVals[i].length(); j++) {
				binary+=binaryPixelVals[i].charAt(j);
			}
			hexPixelVals += lineHexVal(binary);
		}
		return hexPixelVals;
	}
	
	public static void drawChar(String hex, FontCanvas canvas) {
		GraphicsContext CG = canvas.getGraphicsContext2D();
		String[] binary = charBinaryVal(hex);
		for (int i=0; i<binary.length; i++) {
			for (int j=0; j<binary[i].length(); j++) {
				if (binary[i].charAt(j)=='1') {
					CG.setFill(Color.web("#333"));
					CG.fillRect(j*30+1.5, i*30+1.5, 27.5, 27.5);
				}
			}
		}
	}
	
	private static void putPixel(int x, int y, TextCanvas canvas, Color color, int size) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(color);
		gc.fillRect(x, y, size, size);
	}
	
	public static void drawChar(String hex, TextCanvas canvas, int x, int y, boolean bold, boolean underlined, String hexColor, int size) {
		String[] binary = charBinaryVal(hex);
		int cX=x;
		int cY=y;
		Color color = Color.web("#"+hexColor);
		for (int i=0; i<binary.length; i++) {
			cX=x;
			for (int j=0; j<binary[i].length(); j++) {
				if (binary[i].charAt(j)=='1') {
					putPixel(cX, cY, canvas, color, size);
					if (bold) {
						putPixel(cX-1, cY, canvas, color, size);
						putPixel(cX, cY-1, canvas, color, size);
					}
				}
				cX+=size;
			}
			cY+=size;
		}
		if (underlined) {
			GraphicsContext CG = canvas.getGraphicsContext2D();
			CG.setStroke(Color.web("#444"));
			CG.strokeLine(x, y+(size*10+2), x+size*16, y+(size*10+2));
		}
	}
	
	private static String[] charBinaryVal(String hex) {
		String[] hexArr = new String[16];
		int count = 0;
		for (int i=1; i<=hex.length(); i++) {
			if (i%4==0) {
				hexArr[count++] = hex.substring(i-4, i);
			}
		}
		String[] binaryArr = new String[16];
		for (int i=0; i<hexArr.length; i++) {
			for (int j=0; j<hexArr[i].length(); j++) {
				binaryArr[i] = binaryVal(hexArr[i]);
			}
		}
		return binaryArr;
	}
	
	private static String binaryVal(String hex) {
		String binaryVal = "";
		for (int i=0; i<hex.length(); i++) {
			String bin = Integer.toBinaryString(Integer.valueOf(hex.charAt(i)+"", 16));
			while (bin.length()<4) {
				bin="0"+bin;
			}
			binaryVal += bin;
		}
		return binaryVal;
	}
	
	private static String lineHexVal(String binary) {
		String hex = "";
		for (int i=0; i<=12; i+=4) {
			hex+=Integer.toHexString(Integer.valueOf(binary.substring(i, i+4), 2));
		}
		return hex;
	}
	
	public static WritableImage canvToImg(Canvas canvas, double pixelScale) {
	    WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
	    SnapshotParameters spa = new SnapshotParameters();
	    spa.setTransform(Transform.scale(pixelScale, pixelScale));
	    return canvas.snapshot(spa, writableImage);     
	}
	
}
