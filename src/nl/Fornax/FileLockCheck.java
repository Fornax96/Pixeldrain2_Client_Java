package nl.Fornax;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * @author Fornax
 */
public class FileLockCheck {

	private File file;
	private FileChannel channel;
	private FileLock lock;
	private final FileManager fm;
	private final PixelDrain parent;

	public FileLockCheck(PixelDrain parent) {
		fm = new FileManager();
		this.parent = parent;
	}

	public boolean isAppActive() {
		try {
			file = new File(fm.getConfigDir() + "pixel.lock");
			channel = new RandomAccessFile(file, "rw").getChannel();

			try {
				lock = channel.tryLock();
			} catch (OverlappingFileLockException e) {
				// already locked
				closeLock();
				return true;
			}

			if (lock == null) {
				closeLock();
				return true;
			}

			Runtime.getRuntime().addShutdownHook(new Thread() {
				// destroy the lock when the JVM is closing
				@Override
				public void run() {
					closeLock();
					deleteFile();
				}
			});
			return false;
		} catch (Exception e) {
			closeLock();
			return true;
		}
	}

	private void closeLock() {
		try {
			lock.release();
		} catch (Exception e) {
		}
		try {
			channel.close();
		} catch (Exception e) {
		}
	}

	private void deleteFile() {
		try {
			file.delete();
		} catch (Exception e) {
		}
	}

	public void showWarning() {
		final Stage warnStage = new Stage();
		warnStage.setWidth(300);
		warnStage.setHeight(200);
		warnStage.setTitle("PixelDrain is already running!");
		
		FlowPane pane = new FlowPane();

		Label lbl = new Label();
		String text = "It seems like PixelDrain is already running. \n"
			+ "You should know that system crashes can \n"
			+ "occur when PixelDrain is started twice.\n"
			+ "You can press CTRL + ALT + 4 to bring up \nthe PixelDrain interface.\n"
			+ "If you're absolutely sure that PixelDrain is \nnot already running you "
			+ "can click \"No it's \nnot running\" below to continue.";
		lbl.setText(text);
		
		Button btnOk = new Button("Okay");
		btnOk.setOnAction((ActionEvent arg0) -> {
			warnStage.close();
			System.exit(0);
		});
		
		Button btnContinue = new Button("No it's not running");
		btnContinue.setOnAction((ActionEvent arg0) -> {
			deleteFile();
			try {
				parent.start(new Stage());
			} catch (Exception ex) {
				Logger.getLogger(FileLockCheck.class.getName()).log(Level.SEVERE, null, ex);
			}
			warnStage.close();
		});
		
		pane.getChildren().add(lbl);
		pane.getChildren().add(btnContinue);
		pane.getChildren().add(btnOk);
		
		Scene scene = new Scene(pane);
		
		warnStage.setScene(scene);
		
		warnStage.show();
	}
}
