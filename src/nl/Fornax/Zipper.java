package nl.Fornax;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;
import nl.Fornax.ui.ProgressBar;

/**
 * @author Fornax
 */
public class Zipper {
	private final String uploadsDir;
	private final ProgressBar progressbar;
	
	public Zipper(ProgressBar pgb){
		uploadsDir = new FileManager().getUploadsDir();
		progressbar = pgb;
		progressbar.setTitle("Zipping");
	}
	
	public Zipper(){
		uploadsDir = new FileManager().getUploadsDir();
		progressbar = new ProgressBar("Zipping");
	}

	public File zipFiles(List<File> files) {
		try {

			String outLocation = uploadsDir + System.currentTimeMillis() + ".zip";

			ZipFile zipfile = new ZipFile(outLocation);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

			ProgressMonitor progressMonitor = zipfile.getProgressMonitor();
			startMonitoringThread(progressMonitor);

			//This method does not add files in folders
			//zipfile.addFiles((ArrayList) files, parameters);
			progressbar.setIndeterminate(true);
			for (File f : files) {
				if (f.isDirectory()) {
					zipfile.addFolder(f, parameters);
				} else if (f.isFile()) {
					zipfile.addFile(f, parameters);
				}
			}
			
			progressbar.setIndeterminate(false);

			return zipfile.getFile();
		} catch (ZipException ex) {
			Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
	
	private void startMonitoringThread(ProgressMonitor monitor){
		Timer timer = new Timer("ZipperMonitor");
		
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				if(monitor.getState() == ProgressMonitor.STATE_BUSY){
					progressbar.setAction(monitor.getFileName());
				}else{
					timer.cancel();
				}
			}
		}, 100, 100);
	}
}
