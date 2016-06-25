package nl.Fornax;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Fornax
 */
public class FileManager {

	private final String configDir;
	private final String uploadsDir;

	public FileManager() {

		switch (PixelDrain.system) {
			case "Linux":
				configDir = System.getProperty("user.home") + "/.config/PixelDrain/";
				uploadsDir = System.getProperty("user.home") + "/.config/PixelDrain/uploads/";
				break;
			case "Windows":
				configDir = System.getProperty("user.home") + "\\AppData\\Roaming\\PixelDrain\\";
				uploadsDir = System.getProperty("user.home") + "\\AppData\\Roaming\\PixelDrain\\uploads\\";
				break;
			default:
				configDir = System.getProperty("user.home") + "/PixelDrain/";
				uploadsDir = System.getProperty("user.home") + "/PixelDrain/uploads/";
				break;
		}
		
		File dir = new File(uploadsDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}

	public ArrayList<File> getUploads() {
		File[] files = new File(uploadsDir).listFiles();

		ArrayList<File> alFiles = new ArrayList();
		alFiles.addAll(Arrays.asList(files));

		return alFiles;
	}

	public String getConfigDir() {
		File dir = new File(configDir);

		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("Config directory created");
		}

		return configDir;
	}

	public String getUploadsDir() {
		File dir = new File(uploadsDir);

		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("Uploads directory created");
		}

		return uploadsDir;
	}
}
