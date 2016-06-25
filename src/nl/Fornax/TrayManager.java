package nl.Fornax;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Fornax
 */
public class TrayManager implements MouseListener{
	private final PixelDrain parent;
	
	public TrayManager(PixelDrain parent){
		this.parent = parent;
	}

	public void init() {
		if (SystemTray.isSupported()) {
			try {
				SystemTray tray = SystemTray.getSystemTray();
				Image trayImage = ImageIO.read(getClass().getResourceAsStream("/nl/Fornax/res/tray32.png"));
				TrayIcon trayIcon = new TrayIcon(trayImage);
				trayIcon.setImageAutoSize(true);
				tray.add(trayIcon);
				
				trayIcon.addMouseListener(this);
				
			} catch (IOException | AWTException ex) {
				Logger.getLogger(TrayManager.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			System.err.println("SystemTray not supported");
		}
	}

	// Show the UI when tray icon is clicked
	@Override
	public void mouseClicked(MouseEvent e) {parent.showGui();}

	// Unused implemented methods
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
