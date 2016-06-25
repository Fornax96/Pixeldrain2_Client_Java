/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.Fornax.ui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import nl.Fornax.PixelDrain;
import nl.Fornax.Upload;
import nl.Fornax.Zipper;

/**
 *
 * @author Fornax
 */
public class HomeController implements Initializable {

	@FXML
	private ImageView dropTexture;
	@FXML
	private ScrollPane contentScrollPane;

	@FXML
	private void btnUploads(ActionEvent event) {
		contentScrollPane.setContent(new UploadsTilePane());
	}

	@FXML
	private void btnSettings(ActionEvent event) {
		contentScrollPane.setContent(new SettingsVBox());
	}

	@FXML
	private void btnInstructions(ActionEvent event) {
		contentScrollPane.setContent(new InstructionsTextFlow());
	}

	@FXML
	private void btnShutdown(ActionEvent event) {
		PixelDrain.shutdown();
	}

	@FXML
	private void fileOnDragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY);
		} else {
			event.consume();
		}
	}

	@FXML
	private void fileOnDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasFiles()) {
			success = true;
			
			final int numFiles = db.getFiles().size();
			final List<File> fileList = db.getFiles();

			Runnable uploadTask = () -> {
				File result;
				ProgressBar progressbar = new ProgressBar("Working...");

				if (numFiles == 1) {
					result = fileList.get(0);

					if (result.isDirectory()) {
						result = new Zipper(progressbar).zipFiles(fileList);
					}
				} else if (numFiles > 1) {
					result = new Zipper(progressbar).zipFiles(fileList);
				} else {
					//event.setDropCompleted(success);
					//event.consume();
					return;
				}

				System.out.println(result);

				final File finalResult = result;
				new Upload(finalResult).post(progressbar);
			};
			new Thread(uploadTask).start();

		}
		event.setDropCompleted(success);
		event.consume();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dropTexture.setImage(new Image(
			getClass().getResourceAsStream("/nl/Fornax/res/dropTexture.png")
		));

		contentScrollPane.setContent(new UploadsTilePane());
	}
}
