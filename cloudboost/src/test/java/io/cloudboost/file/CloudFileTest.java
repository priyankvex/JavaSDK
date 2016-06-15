package io.cloudboost.file;

import io.cloudboost.CloudException;
import io.cloudboost.CloudFile;
import io.cloudboost.CloudFileArrayCallback;
import io.cloudboost.CloudFileCallback;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.FileUploadCallback;
import io.cloudboost.ObjectCallback;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author cloudboost
 * 
 */
public class CloudFileTest {
	void initialize() {
		UTIL.init();
			}

	@Test(timeout = 50000)
	public void shouldSaveFileGetFromRelation() throws IOException,
			CloudException, JSONException {
		initialize();
		final CloudFile cf = new CloudFile("myFile", "sampletest", "txt");
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				

				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x1.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("No url");
				else {
					CloudObject object = new CloudObject("Employee");
					CloudObject object2 = new CloudObject("Company");
					object2.set("File", x1);
					object.set("Company", object2);

					object.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.assertTrue(t!=null);
							else {
								String objId = x.getId();
								CloudQuery query = new CloudQuery("Employee");
								query.equalTo("id", objId);
								query.include("Company.File");
								query.find(new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t)
											throws CloudException {

									}
								});
							}

						}
					});
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldIncludeOverFile() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		final CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x1.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("No url");
				else {
					CloudObject object = new CloudObject("Company");
					object.set("File", x1);
					object.set("Name", "egima");
					object.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							else {
								String objId = x.getId();
								CloudQuery query = new CloudQuery("Company");
								query.equalTo("id", objId);
								query.include("File");
								query.find(new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t)
											throws CloudException {
										Assert.assertTrue(x!=null);

									}
								});
							}

						}
					});
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldSaveFileAndFetchGetFileContent() throws IOException,
			CloudException, JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		final CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x1.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("No url");
				else {
					CloudFile file = x1;
					file.getFileContent(new ObjectCallback() {

						@Override
						public void done(Object x, CloudException t)
								throws CloudException {
							Assert.assertTrue(x != null);

						}
					});
				}

			}
		});
	}
	@Test(timeout = 50000)
	public void shouldCountProgress() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		final CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				

			}
		},new FileUploadCallback() {
			
			@Override
			public void setProgress(int percent) {
				if(percent==100)
					Assert.assertTrue(true);
				
			}
		});
	}
	@Test(timeout = 50000)
	public void shouldSaveFileAndFetchIt() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		final CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x1.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("No url");
				else {
					x1.fetch(new CloudFileArrayCallback() {

						@Override
						public void done(CloudFile[] x, CloudException t)
								throws CloudException {
							if (t != null) {
								Assert.fail(t.getMessage());
							}

						}
					});
				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shouldSaveFileList() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		final CloudFile cf = new CloudFile(f);
		final CloudFile cf2 = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(final CloudFile x1, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x1.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("No url");
				else {
					try {
						cf2.save(new CloudFileCallback() {

							@Override
							public void done(final CloudFile x2,
									CloudException t) throws CloudException {
								if (t != null) {
									Assert.fail(t.getMessage());
								}
								String url = x2.getFileUrl();
								if ("".equals(url) && null == url
										&& "null".equals(url))
									Assert.fail("No url");
								else {
									CloudObject ob = new CloudObject("Company");
									ob.set("Name", "Egima");
									ob.set("fileList",
											new CloudFile[] { x1, x2 });
									ob.save(new CloudObjectCallback() {

										@Override
										public void done(CloudObject x,
												CloudException t)
												throws CloudException {
											if (t != null) {
												Assert.fail(t.getMessage());
											} else {
												Object[] filelist = x
														.getArray("fileList");
												JSONObject file = (JSONObject) filelist[0];
												JSONObject file2 = (JSONObject) filelist[1];
												try {
													Assert.assertTrue(file
															.getString("id")
															.equals(x1.getId())
															&& file2.getString(
																	"id")
																	.equals(x2
																			.getId()));
												} catch (JSONException e) {
													
													e.printStackTrace();
												}
											}

										}
									});
								}

							}
						});
					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldDeleteNewFile() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(CloudFile x, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x.getFileUrl();
				if ("".equals(url) && null == url && "null".equals(url))
					Assert.fail("Failed to get url");
				else {
					x.delete(new CloudStringCallback() {

						@Override
						public void done(String x, CloudException e)
								throws CloudException {
							
							if (e != null) {
								Assert.fail(e.getMessage());
							}
							Assert.assertEquals("OK", x);
						}
					});
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldSaveNewFile() throws IOException, CloudException,
			JSONException {
		URL url = new URL("http://hmkcode.appspot.com/rest/controller/get.json");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		File f = new File("json.json");
		FileOutputStream fos = new FileOutputStream(f);
		byte[] buf = new byte[512];
		while (true) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}
			fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		initialize();
		CloudFile cf = new CloudFile(f);
		cf.save(new CloudFileCallback() {

			@Override
			public void done(CloudFile x, CloudException t)
					throws CloudException {
				
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				String url = x.getFileUrl();
				Assert.assertFalse("".equals(url) && null == url
						&& "null".equals(url));

			}
		});
	}

	@Test(timeout = 50000)
	public void deleteFile() throws IOException, CloudException, JSONException {
		initialize();
		CloudFile file = new CloudFile("abc.txt", "Hello World", "txt");
		file.save(new CloudFileCallback() {
			@Override
			public void done(CloudFile x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				String url = x.getFileUrl();
				Assert.assertFalse("".equals(url) && null == url
						&& "null".equals(url));
				x.delete(new CloudStringCallback() {

					@Override
					public void done(String x, CloudException t)
							throws CloudException {
						if (t != null) {
							Assert.fail(t.getMessage());
						}
						Assert.assertEquals("OK", x);

					}
				});
			}
		});
	}

	@Test(timeout = 50000)
	public void saveFileAndGiveUrl() throws IOException, CloudException,
			JSONException {
		initialize();
		CloudFile file = new CloudFile("abc.txt", "Hello World", "txt");
		file.save(new CloudFileCallback() {
			@Override
			public void done(CloudFile x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				String url = x.getFileUrl();
				Assert.assertFalse("".equals(url) && null == url
						&& "null".equals(url));
			}
		});
	}

	@Test(timeout = 50000)
	public void saveFileWithDataAndName() throws IOException, CloudException,
			JSONException {
		initialize();
		CloudFile file = new CloudFile("abc.txt", "Hello World", "txt");
		file.save(new CloudFileCallback() {
			@Override
			public void done(CloudFile x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				String url = x.getFileUrl();
				Assert.assertFalse("".equals(url) && null == url
						&& "null".equals(url));
			}
		});
	}

	@Test(timeout = 50000)
	public void returnFileWithCloudObject() throws IOException, CloudException,
			JSONException {
		initialize();
		CloudFile file = new CloudFile("abc.txt", "Hello World", "txt");
		file.save(new CloudFileCallback() {
			@Override
			public void done(final CloudFile x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				String url = x.getFileUrl();
				if (!("".equals(url) && null == url && "null".equals(url))) {
					CloudObject ob = new CloudObject("Company");
					JSONObject json = x.getDocument();
					json.remove("ACL");
					ob.set("File", json);
					ob.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject o, CloudException t)
								throws CloudException {
							if (t != null) {
								Assert.fail(t.getMessage());
							}
							JSONObject obj = (JSONObject) o.get("File");
							String url = null;
							try {
								url = obj.getString("url");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Assert.assertFalse("".equals(url) && null == url
									&& "null".equals(url));

						}
					});

				}
			}
		});
	}

}