package nl.Fornax.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nl.Fornax.FileManager;
import nl.Fornax.Screenshot;
import nl.Fornax.Upload;

/**
 * FXML Controller class
 *
 * @author Fornax
 */
public class EditorController implements Initializable {

	@FXML private ToggleButton btnCrop;
	@FXML private ToggleButton btnAddText;
	@FXML private ToggleButton btnDraw;
	@FXML private ColorPicker colorPicker;
	@FXML private Button btnZoomOut;
	@FXML private Button btnZoomIn;
	@FXML private Button btnUpload;
	@FXML private Canvas canvas;
	@FXML private ScrollPane scrollPane;
	@FXML private AnchorPane anchorPane;
	@FXML private Group group;

	private Image currentImage;
	private double mouseDragStartX = 0;
	private double mouseDragStartY = 0;

	private float zoomScale = 1;

	/**
	 * Initializes the controller class.
	 *
	 * @param url idk
	 * @param rb idk
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		btnUpload.setOnAction(btnUploadHandler);
		btnZoomOut.setOnAction((event) -> {
			zoom(-0.1F);
		});
		btnZoomIn.setOnAction((event) -> {
			zoom(0.1F);
		});
		
		btnCrop.setOnAction((event) -> {
			btnDraw.setSelected(false);
			scrollPane.setPannable(!btnCrop.isSelected());
			canvas.setCursor(Cursor.CROSSHAIR);
		});
		btnDraw.setOnAction((event) -> {
			btnCrop.setSelected(false);
		});

		canvas.setOnScroll((event) -> {
			if (event.getDeltaY() > 0) {
				zoom(0.1F);
			} else {
				zoom(-0.1F);
			}
			event.consume();
		});

		canvas.setOnMousePressed(startDragEventHandler);
		canvas.setOnMouseReleased(finishDragEventHandler);
		canvas.setOnMouseDragged(midDragEventHandler);
		
		colorPicker.setValue(Color.GREEN);
		
		btnAddText.setDisable(true);

		scrollPane.setPannable(true);
	}

	public void setImage(Image img) {
		currentImage = img;
		
		canvas.setWidth(img.getWidth());
		canvas.setHeight(img.getHeight());

		GraphicsContext gfx = canvas.getGraphicsContext2D();
		gfx.setGlobalAlpha(1);
		gfx.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
		
		group.resize(currentImage.getWidth() * zoomScale, currentImage.getHeight() * zoomScale);
		group.relocate(0, 0);
		//TODO: Position the image in the center of the window
	}

	private void zoom(float v) {
		// Make sure the user doesn't zoom in too far
		if (zoomScale < 0.2 && v < 0) {
			return;
		}
		if (zoomScale > 2 && v > 0) {
			return;
		}

		zoomScale = zoomScale + v;

		canvas.setScaleX(zoomScale);
		canvas.setScaleY(zoomScale);
		System.out.println("width: " + canvas.getWidth() + " height: " + canvas.getHeight());
		group.resize(currentImage.getWidth() * zoomScale, currentImage.getHeight() * zoomScale);
		group.relocate(0, 0);
	}
	
	private Image snapshot(){
		canvas.setScaleX(1);
		canvas.setScaleY(1);
		Image img = canvas.snapshot(null, null);
		canvas.setScaleX(zoomScale);
		canvas.setScaleY(zoomScale);
		
		return img;
	}

	/**
	 * ****************
	 * EVENT HANDLERS *
	 *****************
	 */
	private double prevX = -1;
	private double prevY = -1;
	private final EventHandler<MouseEvent> midDragEventHandler = (event) -> {
		if (btnCrop.isSelected()) {
			double x = Math.min(mouseDragStartX, event.getX());
			double y = Math.min(mouseDragStartY, event.getY());
			double w = Math.abs(event.getX() - mouseDragStartX);
			double h = Math.abs(event.getY() - mouseDragStartY);

			GraphicsContext gfx = canvas.getGraphicsContext2D();
			gfx.setGlobalAlpha(1);
			gfx.drawImage(currentImage, 0, 0, canvas.getWidth(), canvas.getHeight());
			gfx.setGlobalAlpha(0.5);
			gfx.setFill(Color.AZURE);
			gfx.setLineWidth(3);
			gfx.fillRect(x, y, w, h);
			gfx.setGlobalAlpha(1);
		} else if (btnDraw.isSelected()) {
			event.consume();
			
			GraphicsContext gfx = canvas.getGraphicsContext2D();
			gfx.setStroke(colorPicker.getValue());
			if(prevX == -1 || prevY == -1){
				prevX = event.getX();
				prevY = event.getY();
			}
			gfx.setLineWidth(5);
			gfx.strokeLine(prevX, prevY, event.getX(), event.getY());
			prevX = event.getX();
			prevY = event.getY();
		}
	};

	private final EventHandler<MouseEvent> startDragEventHandler = (event) -> {
		if (btnCrop.isSelected()) {
			event.consume();
			canvas.setCursor(Cursor.CROSSHAIR);
		} else if (btnDraw.isSelected()) {
			event.consume();
		}

		mouseDragStartX = event.getX();
		mouseDragStartY = event.getY();
	};

	private final EventHandler<MouseEvent> finishDragEventHandler = (event) -> {

		double x = Math.min(mouseDragStartX, event.getX());
		double y = Math.min(mouseDragStartY, event.getY());
		double w = Math.abs(event.getX() - mouseDragStartX);
		double h = Math.abs(event.getY() - mouseDragStartY);

		canvas.setCursor(Cursor.DEFAULT);

		if (btnCrop.isSelected()) {
			event.consume();
			
			if(x < 0){x = 0;}
			if(y < 0){y = 0;}
			if(w > currentImage.getWidth()){w = currentImage.getWidth();}
			if(h > currentImage.getHeight()){w = currentImage.getHeight();}
			
			WritableImage croppedImage = new WritableImage(
				currentImage.getPixelReader(),
				(int) x,
				(int) y,
				(int) w,
				(int) h
			);

			setImage(croppedImage);
			btnCrop.setSelected(false);

			scrollPane.setPannable(true);
		} else if (btnDraw.isSelected()) {
			setImage(snapshot());
			
			prevX = -1;
			prevY = -1;
			event.consume();
		}

	};

	private final EventHandler<ActionEvent> btnUploadHandler = (event) -> {
		String uploadsPath = new FileManager().getUploadsDir();

		String fileName = System.currentTimeMillis() + ".png";
		File file = new File(uploadsPath + fileName);
		
		zoomScale = 1;
		zoom(0);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(currentImage, null), "png", file);

			Runnable uploadTask = () -> {
				new Upload(file).post(null);
			};
			new Thread(uploadTask).start();

			Stage stage = (Stage) btnUpload.getScene().getWindow();
			stage.close();
		} catch (IOException ex) {
			Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
		}
	};
}
