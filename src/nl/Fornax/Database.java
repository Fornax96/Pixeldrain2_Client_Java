package nl.Fornax;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import nl.Fornax.obj.UploadedFile;
import nl.Fornax.ui.UploadsTilePane;

/**
 * @author Fornax
 */
public class Database {

	File dbFile;

	public Database() {
		FileManager fm = new FileManager();
		dbFile = new File(fm.getConfigDir() + "pixeldrain.db");

		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.SEVERE);
		SQLiteConnection db = new SQLiteConnection(dbFile);

		if (!dbFile.exists()) {
			try {
				db.open(true);
				db.exec("CREATE TABLE uploads ("
					+ "fileName VARCHAR, "
					+ "url VARCHAR, "
					+ "type VARCHAR, "
					+ "uploadTime long)");
				db.exec("CREATE TABLE settings ("
					+ "property VARCHAR, "
					+ "value VARCHAR)");
				db.dispose();
				System.out.println("Database initiated");
			} catch (SQLiteException ex) {
				Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		db.dispose();
	}

	public void addNewUpload(String fileName, String url, String type) {
		try {
			Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.SEVERE);
			SQLiteConnection db = new SQLiteConnection(dbFile);
			db.open(true);

			SQLiteStatement st = db.prepare("INSERT INTO uploads ("
				+ "fileName, "
				+ "url, "
				+ "type, "
				+ "uploadTime) "
				+ "VALUES (?, ?, ?, ?)");
			st.bind(1, fileName);
			st.bind(2, url);
			st.bind(3, type);
			st.bind(4, System.currentTimeMillis());
			st.stepThrough();
			st.dispose();
			System.out.println("Stuff inserted into database");
			db.dispose();
		} catch (SQLiteException ex) {
			Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Reload the tiles in the JFX interface
		Platform.runLater(() -> {
			UploadsTilePane.getInstance().reloadUploadHistory();
		});
	}
	
	public void deleteUpload(String column, String value) {
		try {
			Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.SEVERE);
			SQLiteConnection db = new SQLiteConnection(dbFile);
			db.open(true);

			SQLiteStatement st = db.prepare("DELETE FROM uploads WHERE ? = ?");
			st.bind(1, column);
			st.bind(2, value);
			st.stepThrough();
			st.dispose();
			System.out.println("Stuff removed from database");
			db.dispose();
		} catch (SQLiteException ex) {
			Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Reload the tiles in the JFX interface
		Platform.runLater(() -> {
			UploadsTilePane.getInstance().reloadUploadHistory();
		});
	}

	public List<UploadedFile> getUploadHistory() {
		try {
			Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.SEVERE);
			SQLiteConnection db = new SQLiteConnection(dbFile);
			db.open(true);
			
			SQLiteStatement st = db.prepare("SELECT * FROM uploads");
			
			List<UploadedFile> list = new ArrayList();
			while (st.step()) {
				list.add(new UploadedFile(
					st.columnString(0),
					st.columnString(1),
					st.columnString(2),
					st.columnLong(3)
				));
			}
			st.dispose();
			db.dispose();
			
			return list;
		} catch (SQLiteException ex) {
			Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
