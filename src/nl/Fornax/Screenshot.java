package nl.Fornax;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 * @author Fornax
 */
public class Screenshot {
	private BufferedImage img;
	private boolean saved = false;
	private File savedFile;
	
	public boolean grab(){
		try {
			img = new Robot().createScreenCapture(
				new Rectangle(
					Toolkit.getDefaultToolkit().getScreenSize()
				)
			);
			
			return true;
		} catch (AWTException ex) {
			Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return false;
	}
	
	public void uploadImage(){
		if(saved){
			new Upload(savedFile).post(null);
		}else{
			saveImage();
			uploadImage();
		}
	}
	
	public File saveImage(){
		String uploadsPath = new FileManager().getUploadsDir();
		
		String fileName = System.currentTimeMillis() + ".png";
		File file = new File(uploadsPath + fileName);
		
		try {
			ImageIO.write(img, "png", file);
			savedFile = file;
			saved = true;
			
			return file;
		} catch (IOException ex) {
			Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	public BufferedImage getBufferedImage(){
		return img;
	}
	
	public Image getFxImage(){
		return SwingFXUtils.toFXImage(img, null);
	}
}
