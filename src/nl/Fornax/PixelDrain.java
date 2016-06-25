/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.Fornax;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.Fornax.res.Resources;

/**
 *
 * @author Fornax
 */
public class PixelDrain extends Application {

	public static final String VERSION = "2.0";
	public static final String system = System.getProperty("os.name");
	public static final String java_version = System.getProperty("java.version");

	private KeyEventListener kev;

	private Stage mainStage;

	@Override
	public void start(Stage stage) throws Exception {
		// Initialize the resources for other classes to use
		//Resources res = new Resources();
		//res.init();

		// File Locking system to check if PixelDrain has already been started
		// The user is presented with a pop-up asking if they still want to proceed
		FileLockCheck flc = new FileLockCheck(this);
		if(flc.isAppActive()){
			flc.showWarning();
			System.out.println("Application is already running!");
			System.out.println("Saved you from a nasty Xorg crash right there");
			return;
		}

		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.SEVERE);

		// Your Xorg server will freeze when two KeyEventListeners are registered
		// That's why I installed the File Lock system
		kev = new KeyEventListener(this);

		//VERY IMPORTANT! You can not show the GUI a second time if this property isn't set
		Platform.setImplicitExit(false);
		mainStage = stage;

		Parent root = new Resources().getFxHome();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(new Resources().getStyleSheet());

		stage.setTitle("PixelDrain " + VERSION);
		stage.getIcons().add(new Resources().getImgDropTexture());
		stage.setMinHeight(350);
		stage.setMinWidth(400);
		stage.setScene(scene);
		stage.setOnCloseRequest((WindowEvent evt) -> {
			hideGui();
		});

		if(!getParameters().getUnnamed().contains("m")){
			showGui();
		}

		new TrayManager(this).init();
	}

	public void showGui() {
		Platform.runLater(() -> {
			mainStage.show();
			mainStage.toFront();
		});
	}

	public void hideGui() {
		Platform.runLater(() -> {
			mainStage.hide();
		});
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public static void shutdown() {
		System.exit(0);
	}
}
