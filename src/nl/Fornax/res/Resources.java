package nl.Fornax.res;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

/**
 * @author Fornax
 */
public class Resources {
	private static String styleSheet;
	
	private static Parent fxEditor;
	private static Parent fxHome;
	
	private static Image imgDropTexture;
	private static Image imgClipboard;
	private static Image imgArchive;
	
	public Resources(){
//		try {
//			styleSheet = getClass().getResource("/nl/Fornax/res/style.css").toString();
//			
//			fxEditor = FXMLLoader.load(getClass().getResource("/nl/Fornax/ui/Editor.fxml"));
//			fxHome = FXMLLoader.load(getClass().getResource("/nl/Fornax/ui/Home.fxml"));
//			
//			imgDropTexture = new Image(getClass().getResource("/nl/Fornax/res/dropTexture.png").getFile());
//			imgClipboard = new Image(getClass().getResource("/nl/Fornax/res/clipboard.png").getFile());
//			imgArchive = new Image(getClass().getResource("/nl/Fornax/res/archive.png").getFile());
//		} catch (IOException ex) {
//			Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
//		}
//		
//		System.out.println("Resources initialized");
	}
	
	public String getStyleSheet(){
		if(styleSheet == null){
			styleSheet = getClass().getResource("/nl/Fornax/res/style.css").toString();
		}
		return styleSheet;
	}

	public Parent getFxEditor() {
		if(fxEditor == null){
			try {
				fxEditor = FXMLLoader.load(getClass().getResource("/nl/Fornax/ui/Editor.fxml"));
			} catch (IOException ex) {
				Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return fxEditor;
	}

	public Parent getFxHome() {
		if(fxHome == null){
			try {
				fxHome = FXMLLoader.load(getClass().getResource("/nl/Fornax/ui/Home.fxml"));
			} catch (IOException ex) {
				Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return fxHome;
	}

	public Image getImgDropTexture() {
		if(imgDropTexture == null){
			imgDropTexture = new Image(getClass().getResourceAsStream("/nl/Fornax/res/dropTexture.png"));
		}
		return imgDropTexture;
	}

	public Image getImgClipboard() {
		if(imgClipboard == null){
			imgClipboard = new Image(getClass().getResourceAsStream("/nl/Fornax/res/clipboard.png"));
		}
		return imgClipboard;
	}

	public Image getImgArchive() {
		if(imgArchive == null){
			imgArchive = new Image(getClass().getResourceAsStream("/nl/Fornax/res/archive.png"));
		}
		return imgArchive;
	}
	
}
