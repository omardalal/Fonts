import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BtnsBox extends Pane{

	private HBox btnsRow;
	public BtnsBox(String title) {
		super();

		Rectangle rect = getRect();
		Label lbl = getLabel(title);
		
		btnsRow = getBtnsBox();
		
		super.getChildren().addAll(rect, lbl, btnsRow);
	}
	
	public HBox getRow() {
		return btnsRow;
	}
	
	private Rectangle getRect() {
		Rectangle rect = new Rectangle(450, 70);
		rect.setStroke(Color.WHITE);
		rect.setFill(Color.web("333"));
		rect.setLayoutX(10);
		rect.setLayoutY(10);
		return rect;
	}
	
	private HBox getBtnsBox() {
		HBox row = new HBox(10);
		row.setLayoutX(20);
		row.setLayoutY(30);
		
		return row;
	}
	
	private Label getLabel(String title) {
		Label lbl = new Label(title);
		lbl.setPadding(new Insets(0,5,0,5));
		lbl.setStyle("-fx-background-color: #333");
		lbl.setTextFill(Color.WHITE);
		lbl.setLayoutX(20);
		lbl.setLayoutY(0);
		return lbl;
	}
}
