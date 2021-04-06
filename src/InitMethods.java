import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class InitMethods {
	
	public static void initHover(Button btn) {
		btn.setTextFill(Color.WHITE);
		btn.setStyle("-fx-background-color:#333;");
		btn.setOnMouseEntered(e-> {
			btn.setStyle("-fx-background-color:#555;");
		});
		btn.setOnMouseExited(e-> {
			btn.setStyle("-fx-background-color:#333;");
		});
	}
	
	public static void initBtn(Button btn, String imgName, String tooltip, double imageWidth, double imageHeight, double btnWidth, double btnHeight) {
		Image img = new Image("rcs/"+imgName+".png");
		ImageView imgView = new ImageView(img);
		imgView.setPreserveRatio(true);
		imgView.setFitWidth(imageWidth);
		imgView.setFitHeight(imageHeight);
		btn.setGraphic(imgView);
		btn.setTextFill(Color.WHITE);
		btn.setPrefSize(btnWidth, btnHeight);
		btn.setTooltip(new Tooltip(tooltip));
		InitMethods.initHover(btn);;
		initHover(btn);
	}
}
