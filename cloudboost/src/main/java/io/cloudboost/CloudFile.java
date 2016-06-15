package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.regex.Pattern;

/** 
 * <p>Title: Abstract wrapper for File objects</p>
 * <p>CloudFile is an object that represents an arbitrary file format, can save and retrieve these from CloudBoost</p>
 * 
 * @author cloudboost
 *
 */
public class CloudFile{
	public void setDocument(JSONObject document) {
		this.document = document;
	}
	private JSONObject document;
	
	/**
	 * 
	 * @param file
	 * @throws CloudException
	 */
	private File file = null;
	private Blob blobFile = null;
	private Object data = null;
	/**
	 * Constructor that builds a CloudFile object from {@link java.io.File } and content type of the file
	 * @param fileObj a Java file object
	 * @param contentType String representing content/mime type of this file
	 * @throws CloudException
	 */
	public CloudFile(File fileObj,String contentType) throws CloudException{
		
		if(fileObj == null){
			throw new CloudException("File is null");
		}
		
		this.file = fileObj;
		this.document = new JSONObject();
		try {
			this.document.put("_id", JSONObject.NULL);

			this.document.put("_type", "file");
			this.document.put("ACL", (new ACL()).getACL());

		this.document.put("name", file.getName());
		this.document.put("size", file.length());
		this.document.put("url", JSONObject.NULL);
		this.document.put("expires", JSONObject.NULL);
		this.document.put("contentType", contentType);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * returns the Id assigned to this file after saving, an unsaved CloudFile object has no Id
	 * @return _id
	 */
	public String getId(){
		try {
			return document.getString("_id");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Constructor that builds a CloudFile object from {@link java.io.File} and tries to guess the mimetype
	 * @param fileObj a java file object
	 * @throws CloudException
	 */
	public CloudFile(File fileObj) throws CloudException{
		
		if(fileObj == null){
			throw new CloudException("File is null");
		}
		
		this.file = fileObj;
		this.document = new JSONObject();
		try {
			this.document.put("_id", JSONObject.NULL);

			this.document.put("_type", "file");
			this.document.put("ACL", (new ACL()).getACL());

		this.document.put("name", file.getName());
		this.document.put("size", file.length());
		this.document.put("url", JSONObject.NULL);
		this.document.put("expires", JSONObject.NULL);
		this.document.put("contentType", this.getFileExtension(file));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor that builds CloudFile Object from a {@link java.sql.Blob}
	 * @param fileObj
	 * @throws CloudException
	 */
	public CloudFile(Blob fileObj) throws CloudException{
		
		if(fileObj == null){
			throw new CloudException("File is null");
		}
		
		this.blobFile = fileObj;
		this.document = new JSONObject();
		try {
			this.document.put("_type", "file");
		
		this.document.put("name", ((File) this.blobFile).getName());
		
		try {
			this.document.put("size", file.length());
		} catch (JSONException e) {
			this.document.put("size", (Object)null);
			e.printStackTrace();
		}
		
		this.document.put("url", (Object)null);
		this.document.put("contentType", (Object)null);} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
	}
	
	/**
	 * A Constructor that takes the URL of a file and downloads it to create a CloudFile Object
	 * @param url
	 * @throws CloudException
	 */
	public CloudFile(String url) throws CloudException{
		
		if(url.isEmpty()){
			throw new CloudException("Enter valid URL");
		}
		this.document = new JSONObject();
		Pattern pattern = Pattern.compile("https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,}");
		if(pattern.matcher(url).find()){
			try {
				this.document.put("_type", "file");
			
			this.document.put("expires", JSONObject.NULL);
			this.document.put("ACL", new ACL());
			this.document.put("name", JSONObject.NULL);
			this.document.put("size", JSONObject.NULL);
			this.document.put("url", url);
			this.document.put("contentType", JSONObject.NULL);
			
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}else{
			throw new CloudException("Invalid URL");
		}
		
	}
	/**
	 * Constructor that builds a CloudFile object out of arbitrary data
	 * @param fileName the name under which the file should be stored
	 * @param data the content of the file
	 * @param type mimetype of the file
	 * @throws CloudException
	 */
	public CloudFile(String fileName, Object data, String type) throws CloudException{
		this.document = new JSONObject();
		if(fileName.isEmpty()){
			throw new CloudException("file name is required");
		}
		if(type.isEmpty()){
			throw new CloudException("file type is required");
		}
		
		this.data = data;
		try {
			this.document.put("_id", JSONObject.NULL);
		
		this.document.put("_type", "file");
		this.document.put("expires", JSONObject.NULL);
		this.document.put("ACL", (new ACL()).getACL());
		this.document.put("name", fileName);
		this.document.put("size", (Object)null);
		this.document.put("url", JSONObject.NULL);
		this.document.put("contentType", type);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	public CloudFile(JSONObject jsonObject) {
		setDocument(jsonObject);
	}
	/**
	 * Changes the type of the file to the given String
	 * @param type mime type of file
	 */
	public void setFileType(String type){
		try {
			this.document.put("_type", type);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the mime type of the file as a {@link java.lang.String}
	 * @return fileType
	 */
	public String getFileType(){
		try {
			return this.document.getString("_type");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * returns the URL of the file as saved in cloudboost, unsaved files have no URL's
	 * @return file url
	 */
	public String getFileUrl(){
		try {
			return this.document.getString("url");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gives the file a name as should be saved in CloudBoost
	 * @param name the name the file should be saved under
	 */
	public void setFileName(String name){
		try {
			this.document.put("name", name);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the name of the file
	 * @return fileName
	 */
	public String getFileName(){
		try {
			return this.document.getString("name");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the size of the file in Bytes
	 * @return size
	 */
	public int getFileSize(){
		try {
			return this.document.getInt("size");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * Returns a String of the file extension e.g. .json, .txt
	 * @param file
	 * @return file extension
	 */
	private String getFileExtension(File file){
		 String name = file.getName();
		 int lastIndexOf = name.lastIndexOf(".");
		 if (lastIndexOf == -1) {
			 return null;
		 }
		 return name.substring(lastIndexOf);
	}
	/**
	 * Makes an HTTP POST request to upload the file and save all attributes to CloudBoost
	 * @param callbackObject callback to be triggered when the call has completed
	 * @param uploadCallback callback interface that returns in realtime the percent of file uploaded
	 * @throws CloudException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void save(CloudFileCallback callbackObject,FileUploadCallback uploadCallback) throws CloudException, IOException, JSONException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is null");
		}
		
		String url = CloudApp.getApiUrl()+"/file/"+CloudApp.getAppId();
		CBResponse response=null;
		if(data==null)
		response=CBParser.postFormData(url, "POST", document, file,uploadCallback);
		
		else {
			JSONObject params=new JSONObject();
			params.put("key", CloudApp.getAppKey());
			
			params.put("fileObj", document);
			params.put("data", data);
			response=CBParser.callJson(url, "POST", params);}
		if (response.getStatusCode() == 200) {
			String responseBody = response.getResponseBody();
			JSONObject body=null;
			try {
				body = new JSONObject(responseBody);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			document = body;
			callbackObject.done(this, null);
		} else {
			CloudException e = new CloudException(
					response.getStatusMessage());
			callbackObject.done(null, e);
		}
		
	}
	/**
	 * Makes an HTTP POST request to upload the file and save all attributes to CloudBoost
	 * @param callbackObject callback to be triggered when the call has completed
	 * @throws CloudException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void save(CloudFileCallback callbackObject) throws CloudException, IOException, JSONException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is null");
		}
		
		String url = CloudApp.getApiUrl()+"/file/"+CloudApp.getAppId();
		CBResponse response=null;
		if(data==null)
		response=CBParser.postFormData(url, "POST", document, new FileInputStream(file));
		
		else {
			JSONObject params=new JSONObject();
			params.put("key", CloudApp.getAppKey());
			
			params.put("fileObj", document);
			params.put("data", data);
			response=CBParser.callJson(url, "POST", params);}
		if (response.getStatusCode() == 200) {
			String responseBody = response.getResponseBody();
			JSONObject body=null;
			try {
				body = new JSONObject(responseBody);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			document = body;
			callbackObject.done(this, null);
		} else {
			CloudException e = new CloudException(
					response.getStatusMessage());
			callbackObject.done(null, e);
		}
		
	}
	/**
	 * returns the raw JSONObject wrapped by this class, only make direct changes to the underlying JSONObject
	 * if you absolutely know what you are doing
	 * @return document
	 */
	public JSONObject getDocument() {
		return document;
	}
	/**
	 * deletes a saved CloudFile
	 * @param callbackObject callback triggered when the call completes
	 * @throws CloudException
	 */
	public void delete(CloudStringCallback callbackObject) throws CloudException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is null");
		}
		
		try {
			if(this.document.getString("url") == null){
				throw new CloudException("You cannot delete a file which does not have an URL");
			}
		} catch (JSONException e2) {
			
			e2.printStackTrace();
		}
		
		JSONObject params  = new JSONObject();
		try {
			params.put("fileObj", this.document);
				
		params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl()+"/file/"+CloudApp.getAppId()+"/"+getId();
		CBResponse response=CBParser.callJson(url, "DELETE", params);
	if (response.getStatusCode() == 200) {
		callbackObject.done(response.getStatusMessage(), null);
	} else {
		CloudException e = new CloudException(
				response.getStatusMessage());
		callbackObject.done(null, e);
	}
	}
	/**
	 * returns the file content as an object
	 * @param callback
	 */
	public void getFileContent(ObjectCallback callback){
		String url=CloudApp.getServerUrl()+"/file/" + CloudApp.getAppId() + "/" + getId()  ;
		JSONObject params=new JSONObject();
		try {
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		CBResponse response=CBParser.callJson(url, "POST", params);
		if(response.getStatusCode()==200){
			try {
				callback.done(response.getResponseBody(), null);
			} catch (CloudException e) {
				e.printStackTrace();
			}
			
		} else
			try {
				callback.done(null, new CloudException(response.getStatusMessage()));
			} catch (CloudException e) {
				
				e.printStackTrace();
			}
	}
	/**
	 * returns the ACL object representing the access rights on this file
	 * @return
	 */
	public ACL getAcl(){
		try {
			JSONObject ob=(JSONObject) document.get("ACL");
			ACL acl=new ACL();
			acl.acl=ob;
			return acl;
		} catch (JSONException e) {
			return null;
		}
	}
	/**
	 * replaces the existing ACL object
	 * @param acl new ACL object
	 */
	public void setAcl(ACL acl){
		try {
			document.put("ACL", acl.getACL());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * fetches this file from the database
	 * @param callback
	 * @throws CloudException
	 */
	public void fetch(final CloudFileArrayCallback callback) throws CloudException{
		CloudQuery query=new CloudQuery("_File");
		query.equalTo("id", getId());
		query.setLimit(1);
		query.find(new CloudFileArrayCallback() {
			
			@Override
			public void done(CloudFile[] x, CloudException t) throws CloudException {
				callback.done(x, t);
				
			}
		});
		
	}
	public static void main(String[] args) throws CloudException, JSONException, IOException {
		CloudApp.init("cpnbzclvxjts", "67d3f4e4-97e6-4f2b-91af-d553b9160e20");
		CloudFile file=new CloudFile(new File("smack.jpg"));
		file.save(new CloudFileCallback() {
			
			@Override
			public void done(CloudFile x, CloudException t) throws CloudException {
				x.getFileContent(new ObjectCallback() {
					
					@Override
					public void done(Object x, CloudException t) throws CloudException {
						System.out.println("file content: "+x.toString());
						
					}
				});
				
			}
		});
	}
}