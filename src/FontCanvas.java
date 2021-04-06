import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FontCanvas extends Canvas {
	
	private int mode = 0; // 0-> Draw 1->Erase
	private boolean enabled = false; //Drawing enabled?
	public FontCanvas(double width, double height) {
		super(width, height);
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}
	
	public void init() {
		GraphicsContext CG = super.getGraphicsContext2D();
		CG.setLineWidth(0.5);
		int linesCount = 0;
		for (double i=30; i<=495; i+=30) {
			if (linesCount==15) {
				break;
			}
			if (linesCount==7) {
				CG.setStroke(Color.web("#ee2211"));
			} else {
				CG.setStroke(Color.web("#555"));
			}
			if (linesCount==7) {
				CG.setStroke(Color.web("#ee2211"));
			} else {
				CG.setStroke(Color.web("#555"));
			}
			CG.setLineWidth(0.5);
			CG.strokeLine(i, 0, i, 495);
			if (linesCount==9) {
				CG.setLineWidth(1);
				CG.setStroke(Color.web("#ee2211"));
				CG.strokeLine(0, i, 495, i);
			} else {
				CG.setLineWidth(0.5);
				CG.strokeLine(0, i, 495, i);
			}
			linesCount++;
		}
		super.setOnMousePressed(e-> {
			if (enabled) {
				int[] loc = getLoc(e.getX(), e.getY());
				if (mode==0) {
					CG.setFill(Color.web("#333"));
					CG.fillRect(loc[0]+1.5, loc[1]+1.5, 27.5, 27.5);
				} else {
					CG.clearRect(loc[0]+1.5, loc[1]+1.5, 27.5, 27.5);
				}
			} else {
				new ErrorAlert("Add Character to start drawing!");
			}
		});
		super.setOnMouseDragged(e-> {
			if (enabled) {
				int[] loc = getLoc(e.getX(), e.getY());
				if (mode==0) {
					CG.setFill(Color.web("#333"));
					CG.fillRect(loc[0]+1.5, loc[1]+1.5, 27.5, 27.5);
				} else {
					CG.clearRect(loc[0]+1.5, loc[1]+1.5, 27.5, 27.5);
				}
			} else {
				new ErrorAlert("Add Character to start drawing!");
			}
		});
	}
	
	private int[] getLoc(double xVal, double yVal) {
		int[] loc = {0,0};
		int i=0;
		boolean xFound = false;
		boolean yFound = false;
		while (!(xFound&&yFound)) {
			if (xVal>=i&&xVal<i+30) {
				loc[0] = i;
				xFound = true;
			}
			if (yVal>=i&&yVal<i+30) {
				loc[1] = i;
				yFound = true;
			}
			i+=30;
		}
		return loc;
	}
	
	public void clear() {
		super.getGraphicsContext2D().clearRect(0, 0, 480, 480);
		init();
	}
}
