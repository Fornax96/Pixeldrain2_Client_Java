package nl.Fornax.ui;

import java.util.Collections;
import java.util.List;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import nl.Fornax.ClipBoardManager;
import nl.Fornax.Database;
import nl.Fornax.FileManager;
import nl.Fornax.Notification;
import nl.Fornax.UploadedFileComparator;
import nl.Fornax.obj.UploadedFile;
import nl.Fornax.res.Resources;

/**
 * @author Fornax
 */
public class UploadsTilePane extends TilePane {
	private final int PREVIEW_SIZE = 200;
	private static UploadsTilePane instance;
	
	@SuppressWarnings("LeakingThisInConstructor")
	public UploadsTilePane() {
		reloadUploadHistory();
		
		instance = this;
	}
	
	public static UploadsTilePane getInstance(){
		return instance;
	}

	public final void reloadUploadHistory() {
		getChildren().clear();
		
		String uploadsPath = new FileManager().getUploadsDir();
		
		Database db = new Database();
		List<UploadedFile> fileList = db.getUploadHistory();
		
		Collections.sort(fileList, new UploadedFileComparator());
		
		for(int i = fileList.size() - 1; i >= 0; i--){
			UploadedFile f = fileList.get(i);
			
			switch (f.getFileType()) {
				case "image/png":
				case "image/bmp":
				case "image/gif":
				case "image/jpeg":
					addImageToPane(
						new Image("file://" + uploadsPath + f.getName(),
							PREVIEW_SIZE, 0, true, true),
						f.getUrl()
					);	break;
				default:
					addImageToPane(new Resources().getImgArchive(), f.getUrl());
					break;
			}
		}
	}
	
	private void addImageToPane(Image img, String imageUrl){
		Pane pane = new Pane();
		pane.getStyleClass().add("historyTile");
		pane.setCenterShape(true);
		pane.setPrefSize(PREVIEW_SIZE + 20, PREVIEW_SIZE + 20);
		
		ImageView iv = new ImageView();
		iv.setImage(img);
		if(img.getWidth() > img.getHeight()){
			iv.setFitWidth(PREVIEW_SIZE);
		}else{
			iv.setFitHeight(PREVIEW_SIZE);
		}
		iv.setPreserveRatio(true);
		iv.setX(10);
		iv.setY(10);
		iv.getStyleClass().add("uploadTileImage");

		ImageView copy = new ImageView();
		copy.setImage(new Resources().getImgClipboard());
		copy.setLayoutX(PREVIEW_SIZE - 8);
		copy.setLayoutY(PREVIEW_SIZE - 15);
		copy.setOnMouseClicked((MouseEvent event) -> {
			onCopyBtnClick(imageUrl, event);
		});

		pane.getChildren().add(iv);
		pane.getChildren().add(copy);
		getChildren().add(pane);
	}

	public void onCopyBtnClick(String imageURL, Event event) {
		ClipBoardManager cbm = new ClipBoardManager(imageURL);
		cbm.copy();
		Notification.addNotification("URL copied to clipboard", imageURL);
	}
}
