package nl.Fornax.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author Fornax
 */
public class InstructionsTextFlow extends HBox{
	private final String instuctionText = ""
		+ "Hello, and welcome to PixelDrain\n"
		+ "\n"
		+ "Controls:\n"
		+ " • CTRL + ALT + 1: Take a screenshot of the entire screen and upload it.\n"
		+ " • CTRL + ALT + 2: Take a screenshot and open it in the editor. You can use the "
		+ "editor to crop the image and add drawings to it.\n"
		+ " • CTRL + ALT + 3: NOT IMPLEMENTED. Take a photo with your webcam and upload it.\n"
		+ " • CRTL + ALT + 4: Open the PixelDrain Home interface. Here you can upload files, "
		+ "change your settings and view your previously uploaded files.\n"
		+ "\n"
		+ "\n"
		+ "Planned Features:\n"
		+ " • Make the settings panel work.\n"
		+ " • Add system tray icon.\n"
		+ " • Button to save images locally\n"
		+ " • Option to upload to other websites\n"
		+ " • More image editor features: arrows, text, blur, etc";
	
	public InstructionsTextFlow(){
		setPadding(new Insets(10));
		Label text = new Label(instuctionText);
		text.setWrapText(true);
		getChildren().add(text);
	}
}
