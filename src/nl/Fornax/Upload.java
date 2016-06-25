package nl.Fornax;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import nl.Fornax.ui.ProgressBar;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Fornax
 */
public class Upload {

	private final File file;
	private ProgressBar progressbar;
	private final String type;

	public Upload(File file){
		this.file = file;
		
		String typef;
		try {
			typef = Files.probeContentType(file.toPath());
		} catch (IOException ex) {
			Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
			typef = "unknown";
		}
		
		type = typef;
	}

	public boolean post(ProgressBar pb) {
		this.progressbar = pb;
		
		if (progressbar == null) {
			progressbar = new ProgressBar("Uploading");
		} else {
			progressbar.setTitle("Uploading");
		}

		UploadThread t = new UploadThread(file, progressbar, this);
		t.start();

		return true;
	}

	public void uploadDone(String msg) {
		Object obj = JSONValue.parse(msg);
		JSONObject json = (JSONObject) obj;

		//The json parsing will return null if there was no valid json in the string
		if (json == null) {
			progressbar.setTitle("Invalid response from server");
			progressbar.setAction(msg);
			progressbar.destroy(100000);
			return;
		}

		//Every message should contain a type
		if (json.containsKey("type") == false) {
			progressbar.setTitle("Invalid response from server");
			progressbar.setAction(msg);
			progressbar.destroy(100000);
			return;
		}
		
		if(json.get("type").equals("error")){
			progressbar.setTitle("And error occured:");
			progressbar.setAction(json.get("message").toString());
		}
		
		if(json.get("type").equals("success")){
			ClipBoardManager cbm = new ClipBoardManager(json.get("url").toString());
			cbm.copy();
			
			new Database().addNewUpload(
				file.getName(),
				json.get("url").toString(), 
				type);
			
			progressbar.setTitle("Upload successful!");
			progressbar.setAction("Link has been copied to your clipboard");
			progressbar.destroy(10000);
		}
		
		System.gc();
	}
}

class UploadThread extends Thread {

	private final File file;
	private final ProgressBar progressBar;
	private final Upload parent;

	public UploadThread(File f, ProgressBar bar, Upload parent) {
		file = f;
		progressBar = bar;
		this.parent = parent;
	}

	@Override
	public void run() {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpPost httppost = new HttpPost("http://pixeldra.in/api/upload");

			ProgressHttpEntityWrapper.ProgressCallback progressCallback = (float progress) -> {
				progressBar.setProgress(progress / 100F);
				progressBar.setAction("Uploading... " + progress + "%");
			};

			MultipartEntityBuilder mpEnt = MultipartEntityBuilder.create();
			mpEnt.addBinaryBody("file", file);
			mpEnt.addTextBody("fileName", file.getName());

			HttpEntity reqEntity = mpEnt.build();
			httppost.setEntity(new ProgressHttpEntityWrapper(reqEntity, progressCallback));

			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			
			String responseText = "no response";
			
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				System.out.println("Response content length: " + resEntity.getContentLength());
				responseText = EntityUtils.toString(resEntity);
				System.out.println(responseText);
			}
			
			EntityUtils.consume(resEntity);

			response.close();

			httpclient.close();
			
			parent.uploadDone(responseText);

		} catch (IOException ex) {
			progressBar.setTitle("An error occurred during upload");
			progressBar.setAction("Check the logs for more details");
			Platform.runLater(() -> {showNoConnectionError();});
		}

	}
	
	//TODO: Move to own FXML / Controller files
	//Urgent!
	private void showNoConnectionError(){
		final Stage stage = new Stage();
		stage.setWidth(350);
		stage.setHeight(100);
		stage.setTitle("Upload failed");
		stage.setResizable(false);
		
		FlowPane pane = new FlowPane();

		Label lbl = new Label();
		String text = "PixelDrain could not connect to the file server";
		lbl.setText(text);
		
		Button btnOk = new Button("Cancel upload");
		btnOk.setOnAction((ActionEvent arg0) -> {
			progressBar.destroy(1000);
			stage.close();
		});
		
		Button btnContinue = new Button("Try again");
		btnContinue.setOnAction((ActionEvent arg0) -> {
			new UploadThread(file, progressBar, parent).start();
			stage.close();
		});
		
		pane.getChildren().add(lbl);
		pane.getChildren().add(btnOk);
		pane.getChildren().add(btnContinue);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("/nl/Fornax/res/style.css").toString());
		
		stage.setScene(scene);
		
		stage.show();
	}
}
