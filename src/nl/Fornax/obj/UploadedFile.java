package nl.Fornax.obj;

/**
 * @author Fornax
 */
public class UploadedFile {
	private final String name;
	private final String url;
	private final String fileType;
	private final long uploadTime;
	
	public UploadedFile(String name, String url, String fileType, long uploadTime){
		this.url = url;
		this.name = name;
		this.fileType = fileType;
		this.uploadTime = uploadTime;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}

	public String getFileType() {
		return fileType;
	}

	public long getUploadTime() {
		return uploadTime;
	}
}
