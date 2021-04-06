import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch();
	}

	Stage window;
	@Override
	public void start(Stage pS) {
		window = pS;
		WelcomeWindow root = new WelcomeWindow(window);
		Scene scene = new Scene(root, 600, 500);
		pS.setScene(scene);
		pS.setResizable(false);
		pS.setTitle("Font Maker");
		pS.show();
	}
}
