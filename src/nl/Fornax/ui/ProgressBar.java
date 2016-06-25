package nl.Fornax.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Fornax
 */
public class ProgressBar {

	private final JFrame frame;
	private final JProgressBar progressbar;
	private String title;

	public ProgressBar(String title) {
		this.title = title;

		//Set the look and feel to GTK when it's available
		try {

			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				} else {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
			}
		} catch (ClassNotFoundException |
			InstantiationException |
			IllegalAccessException |
			UnsupportedLookAndFeelException ex) {
			Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
		}

		frame = new JFrame(title);
		frame.setSize(600, 50);
		frame.setLayout(new BorderLayout());
		frame.setLocation(10, 10);
		frame.setAlwaysOnTop(true);
		frame.setFocusable(false);

		progressbar = new JProgressBar();
		progressbar.setMaximum(1000);
		progressbar.setMinimum(0);
		progressbar.setPreferredSize(new Dimension(600, 50));
		progressbar.setStringPainted(true);

		frame.add(progressbar, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	private long lastTitleUpdate = System.currentTimeMillis();

	public void setProgress(float progress) {
		progressbar.setValue(Math.round(progress * 1000));

		if (lastTitleUpdate < (System.currentTimeMillis() - 1000)) {
			frame.setTitle(title + " ~ " + Math.round(progress * 100) + "%");
			lastTitleUpdate = System.currentTimeMillis();
		}
	}

	public void setTitle(String title) {
		this.title = title;
		frame.setTitle(title);
	}

	public void setAction(String action) {
		progressbar.setString(action);
	}
	
	public void setIndeterminate(boolean value){
		progressbar.setIndeterminate(value);
	}

	/**
	 * Destroys the ProgressBar window
	 * 
	 * @param timeout Time in milliseconds until the window is destroyed, 
	 * set to null for no timeout
	 */
	public void destroy(Integer timeout) {
		if (timeout == null) {
			frame.dispose();
		} else {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					frame.dispose();
				}
			}, timeout);
		}

	}
}
