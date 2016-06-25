package nl.Fornax;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.Fornax.res.Resources;
import nl.Fornax.ui.EditorController;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * @author Fornax
 */
public class KeyEventListener implements NativeKeyListener {

	private boolean MOD_1_PRESSED = false;
	private boolean MOD_2_PRESSED = false;
	private boolean KEY_DIRECT_PRESSED = false;
	private boolean KEY_EDIT_PRESSED = false;
	private boolean KEY_WEBCAM_PRESSED = false;
	private boolean KEY_INTERFACE_PRESSED = false;

	private final int MOD_1_CODE = 29;
	private final int MOD_2_CODE = 56;
	private final int KEY_DIRECT_CODE = 2;
	private final int KEY_EDIT_CODE = 3;
	private final int KEY_WEBCAM_CODE = 4;
	private final int KEY_INTERFACE__CODE = 5;
	
	private final PixelDrain parent;

	public KeyEventListener(PixelDrain parent) {
		this.parent = parent;
		
		//TODO: Make keys configurable
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		
		if(GlobalScreen.isNativeHookRegistered()){
			System.err.println("The native hook has already been registered");
			System.err.println("Exiting...");
			PixelDrain.shutdown();
		}

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(this);
		System.out.println("Native hook registered");
	}

	private void updateKeyState(int keycode, boolean pressed) {
		if (keycode == MOD_1_CODE) {
			MOD_1_PRESSED = pressed;
		} else if (keycode == MOD_2_CODE) {
			MOD_2_PRESSED = pressed;
		} else if (keycode == KEY_DIRECT_CODE) {
			KEY_DIRECT_PRESSED = pressed;
		} else if (keycode == KEY_EDIT_CODE) {
			KEY_EDIT_PRESSED = pressed;
		} else if (keycode == KEY_WEBCAM_CODE) {
			KEY_WEBCAM_PRESSED = pressed;
		} else if (keycode == KEY_INTERFACE__CODE) {
			KEY_INTERFACE_PRESSED = pressed;
		}

		if (MOD_1_PRESSED && MOD_2_PRESSED) {
			if (KEY_DIRECT_PRESSED) {
				Screenshot sc = new Screenshot();
				sc.grab();
				sc.uploadImage();
			}else if (KEY_EDIT_PRESSED) {
				System.out.println("Opening editor");
				
				final Screenshot sc = new Screenshot();
				sc.grab();
				
				Platform.runLater(() -> {
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/nl/Fornax/ui/Editor.fxml"));
						Scene scene = new Scene(loader.load());
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setTitle("PixelDrain Image Editor");
						stage.getIcons().add(new Resources().getImgDropTexture());
						stage.setMaximized(true);
						scene.getStylesheets().add(new Resources().getStyleSheet());
						EditorController controller = loader.getController();
						controller.setImage(sc.getFxImage());
						stage.show();
					} catch (IOException ex) {
						Logger.getLogger(KeyEventListener.class.getName()).log(Level.SEVERE, null, ex);
					}
				});
			}else if (KEY_WEBCAM_PRESSED) {
				System.out.println("GUI hidden");
				parent.hideGui();
			}else if (KEY_INTERFACE_PRESSED) {
				System.out.println("GUI opened");
				parent.showGui();
			}
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		updateKeyState(e.getKeyCode(), true);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		updateKeyState(e.getKeyCode(), false);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}
