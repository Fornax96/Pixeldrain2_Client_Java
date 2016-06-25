package nl.Fornax.ui;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * @author Fornax
 */
public class SettingsVBox extends GridPane {

	public SettingsVBox() {
		getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE));
		getColumnConstraints().add(new ColumnConstraints(200));
		
		setHgap(10);
		setVgap(10);
		
		CheckBox chkOpenBrowser = new CheckBox();
		chkOpenBrowser.addEventHandler(EventType.ROOT, chkOpenBrowserHandler);
		
		CheckBox chkMinimizeToTray = new CheckBox();
		chkOpenBrowser.addEventHandler(EventType.ROOT, chkMinimizeToTrayHandler);
		
		ComboBox cmbUploadServer = new ComboBox();
		chkOpenBrowser.addEventHandler(EventType.ROOT, cmbUploadServerHandler);
		
		cmbUploadServer.getItems().addAll("Fornax96.me", "Imgur");

		add(new Label("Open browser after upload:"), 0, 0);
		add(chkOpenBrowser, 1, 0);
		add(new Label("Minimize to tray instead of task bar:"), 0, 1);
		add(chkMinimizeToTray, 1, 1);
		add(new Label("Server to upload files to:"), 0, 2);
		add(cmbUploadServer, 1, 2);
	}
	
	// Handlers, might need a simpler system for this
	private final EventHandler chkOpenBrowserHandler = (event) -> {
		
	};
	
	private final EventHandler chkMinimizeToTrayHandler = (event) -> {
		
	};
	
	private final EventHandler cmbUploadServerHandler = (event) -> {
		
	};
}
