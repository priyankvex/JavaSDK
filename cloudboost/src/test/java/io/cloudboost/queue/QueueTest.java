package io.cloudboost.queue;

import io.cloudboost.CloudException;
import io.cloudboost.CloudQueue;
import io.cloudboost.CloudQueueArrayCallback;
import io.cloudboost.CloudQueueCallback;
import io.cloudboost.CloudQueueMessageCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.QueueMessage;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.util.UUID;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class QueueTest {
	void initialize() {
		UTIL.init();
	}

	@Test(timeout = 50000)
	public void shouldCreateAndDeleteQueue(){
		UTIL.init();
		CloudQueue q=new CloudQueue("newqueue", null);
		q.create(new CloudQueueCallback() {
			
			@Override
			public void done(CloudQueue q, CloudException e) {
				if(e!=null){
					Assert.fail(e.getMessage());
					
				}
				else{
					q.delete(new CloudQueueCallback() {
						
						@Override
						public void done(CloudQueue q, CloudException e) {
							if(e!=null)
								Assert.fail(e.getMessage());
							else{
								Assert.assertTrue(q.getAttribute("name").equals("newqueue"));
							}
							
						}
					});
				}
				
			}
		});
	}
//	@Test(timeout = 50000)
//	public void shouldReturnNoQueueObjectsWhenNoneInDB(){
//		UTIL.initKisenyi();
//		CloudQueue.getAll(new CloudQueueArrayCallback() {
//			
//			@Override
//			public void done(CloudQueue[] queues, CloudException e) {
//				if(queues!=null)
//					Assert.fail("Should not get queues when non exists in DB");
//				else{
//					Assert.assertTrue(e.getMessage().equals("No queues found"));
//				}
//			}
//		});
//	}
	@Test(timeout = 50000)
	public void shouldAddExpiresIntoQueueMessage(){
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		QueueMessage ms=new QueueMessage();
		ms.setMessage("sample");
		ms.setExpires("2017-03-02T09:11:15.621Z");
		que.addMessage(ms, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msg != null) {
					Assert.assertEquals(msg[0].getElement("expires"),"2017-03-02T09:11:15.621Z");
				}

			}
		});
	}
	@Test(timeout = 50000)
	public void shouldGetMessageWhenExpiresIsSetToFutureDate(){
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		QueueMessage ms=new QueueMessage();
		ms.setMessage("sample");
		ms.setExpires("2017-03-02T09:11:15.621Z");
		que.addMessage(ms, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msg != null) {

					String mg = msg[0].getMessage();
					if(mg.equals("sample")){
						que.getMessage(1, new CloudQueueMessageCallback() {
							
							@Override
							public void done(QueueMessage[] msgs, CloudException e) {
								
								
							}
						});
					}
				}

			}
		});
	}
	@Test(timeout = 50000)
	// should push data into the queue
	public void push() {
		initialize();
		CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msg != null) {

					String mg = msg[0].getMessage();
					Assert.assertEquals(mg, "sample");
				}

			}
		});
	}
	@Test(timeout = 50000)
	// should push data into the queue
	public void shouldUpdateMessage() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		QueueMessage ms=new QueueMessage();
		ms.setMessage("sample");
		que.addMessage(ms, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msg != null) {
					QueueMessage mss=msg[0];
					
					if(mss.getMessage().equals("sample")){
						mss.setMessage("not sample");
						que.updateMessage(new QueueMessage[]{mss}, new CloudQueueMessageCallback() {
							
							@Override
							public void done(QueueMessage[] msgs, CloudException e) {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								else Assert.assertEquals(msgs[0].getMessage(), "not sample");
								
							}
						});
						
					}else Assert.fail("wrong message returned");
				}

			}
		});
	}
	@Test(timeout = 50000)
	// should push data into the queue
	public void shouldNotUpdateUnsavedDataIntoQueue() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		QueueMessage ms=new QueueMessage();
		ms.setMessage("sample");
		que.updateMessage(new QueueMessage[]{ms}, new CloudQueueMessageCallback() {
			
			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.assertEquals(e.getMessage(),"The message cannot be updated because it has never been saved");
				}
				else Assert.fail("should not update unsaved data");
				
			}
		});
	}
	@Test(timeout = 50000)
	// should push data array into the queue
	public void pushArray() {
		initialize();
		CloudQueue que = new CloudQueue("follow", null);
		que.push(new String[] { "sample", "egima" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msg, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msg != null) {
							Assert.assertTrue(msg.length == 2
									&& msg[0].getMessage().equals("sample")
									&& msg[1].getMessage().equals("egima"));
						}

					}
				});
	}

	@Test(timeout = 50000)
	// should push message array into the queue
	public void pushMessageArray() {
		initialize();
		CloudQueue que = new CloudQueue("follow", null);
		QueueMessage msg1 = new QueueMessage();
		msg1.push("sample1");
		QueueMessage msg2 = new QueueMessage();
		msg2.push("sample2");
		QueueMessage[] msgs = { msg1, msg2 };
		que.push(msgs, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msg != null) {
					List<String> msgs=Arrays.asList(new String[]{"sample1","sample2"});
					Assert.assertTrue(msg.length == 2
							&& msgs.contains(msg[0].getMessage())
							&& msgs.contains(msg[1].getMessage()));
				}

			}
		});
	}

	@Test(timeout = 50000)
	// can push duplicate messages into same queue
	public void pushDuplicateMessages() {
		initialize();
		final CloudQueue que = new CloudQueue("follow", null);
		que.push(new String[] { "sample", "egima" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msgs, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msgs != null) {
							que.push(msgs, new CloudQueueMessageCallback() {

								@Override
								public void done(QueueMessage[] msg,
										CloudException e) {
									if (e != null)
										Assert.fail(e.getMessage());
									for (QueueMessage ms : msg) {
										if (ms.getMessage().equals("sample")
												|| ms.getMessage().equals(
														"egima"))
											continue;
										else
											Assert.fail();
									}

								}
							});
						}

					}
				});
	}

	@Test(timeout = 50000)
	// should not push null data into queue
	public void noPushNull() {
		initialize();
		final CloudQueue que = new CloudQueue("follow", null);
		que.push(null, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.assertEquals(e.getMessage(),
							"Cannot push null into queue");
				}
				if (msgs != null) {
					Assert.fail("Should not allow null messages into queue");

				}

			}
		});
	}

	@Test(timeout = 50000)
	// Should not allow null empty queue name
	public void dontAllowEmptyQueueName() {
		initialize();
		final CloudQueue que = new CloudQueue("", null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.assertEquals(e.getMessage(),
							"Object Validation Failure");
				}
				if (msgs != null) {
					Assert.fail("Should not allow null empty queue name");

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldAddAndGetSameMessage() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					QueueMessage msg = msgs[0];
					if (msg.getMessage().equals("sample")) {
						que.getMessage(1, new CloudQueueMessageCallback() {

							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null) {
									Assert.assertEquals(e.getMessage(),
											"Object Validation Failure");
								}
								if (msgs != null) {
									Assert.assertTrue(msgs.length == 1
											&& msgs[0].getMessage().equals(
													"sample"));
								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldGetMessageById() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					QueueMessage msg = msgs[0];
					String id = msg.getId();
					que.getMessageById(id, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							if (e != null) {
								Assert.fail(e.getMessage());
							}
							if (msgs != null) {
								if (msgs.length == 1) {
									Assert.assertTrue(msgs.length == 1
											&& msgs[0].getMessage().equals(
													"sample"));
								} else
									Assert.fail("Should get only 1 message");
							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldGetMessage() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.assertEquals(e.getMessage(),
							"Object Validation Failure");
				}
				if (msgs != null) {
					que.getMessage(1, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							if (e != null) {
								Assert.assertEquals(e.getMessage(),
										"Object Validation Failure");
							}
							if (msgs != null) {
								Assert.assertTrue(msgs.length == 1
										&& msgs[0].getMessage()
												.equals("sample"));
							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void getMessagesInFIFO() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample1", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					if (msgs[0].getMessage().equals("sample1")) {
						que.addMessage("sample2", new CloudQueueMessageCallback() {

							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								if (msgs != null) {
									if (msgs[0].getMessage().equals("sample2")) {
										que.getMessage(
												1,
												new CloudQueueMessageCallback() {

													@Override
													public void done(
															QueueMessage[] msgs,
															CloudException e) {
														if (e != null) {
															Assert.fail(e
																	.getMessage());
														}
														if (msgs != null) {
															if (msgs[0]
																	.getMessage()
																	.equals("sample1")) {
																que.getMessage(
																		1,
																		new CloudQueueMessageCallback() {

																			@Override
																			public void done(
																					QueueMessage[] msgs,
																					CloudException e) {
																				if (e != null) {
																					Assert.fail(e
																							.getMessage());
																				}
																				if (msgs != null) {
																					Assert.assertEquals(
																							msgs[0].getMessage(),
																							"sample2");
																				}
																			}
																		});
															}
														}

													}
												});
									}
								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shouldGetTwoMessages() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample1", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					if (msgs[0].getMessage().equals("sample1")) {
						que.addMessage("sample2", new CloudQueueMessageCallback() {

							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								if (msgs != null) {
									if (msgs[0].getMessage().equals("sample2")) {
										que.getMessage(
												2,
												new CloudQueueMessageCallback() {

													@Override
													public void done(
															QueueMessage[] msgs,
															CloudException e) {
														if (e != null) {
															Assert.assertEquals(
																	e.getMessage(),
																	"Object Validation Failure");
														}
														if (msgs != null) {
															Assert.assertTrue(msgs.length == 2
																	&& msgs[0]
																			.getMessage()
																			.equals("sample1")
																	&& msgs[1]
																			.getMessage()
																			.equals("sample2"));
														}

													}
												});
									}
								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldGet$TwoMessages() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample1", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.addMessage("sample2", new CloudQueueMessageCallback() {
						
						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							que.getMessage(2, new CloudQueueMessageCallback() {

								@Override
								public void done(QueueMessage[] msgs, CloudException e) {
									if (e != null) {
										Assert.fail(e.getMessage());
									}
									if (msgs != null) {
										Assert.assertTrue(msgs.length == 2
												&& msgs[0].getMessage()
														.equals("sample1"));
									}

								}
							});
						}
					});
					

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shouldPullTwo() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample1", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					if (msgs[0].getMessage().equals("sample1")) {
						que.addMessage("sample2", new CloudQueueMessageCallback() {

							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								if (msgs != null) {
									if (msgs[0].getMessage().equals("sample2")) {
										que.getMessage(
												2,
												new CloudQueueMessageCallback() {

													@Override
													public void done(
															QueueMessage[] msgs,
															CloudException e) {
														if (e != null) {
															Assert.fail(
																	e.getMessage());
														}
														if (msgs != null) {
															Assert.assertTrue(msgs.length == 2
																	&& msgs[0]
																			.getMessage()
																			.equals("sample1")
																	&& msgs[1]
																			.getMessage()
																			.equals("sample2"));
														}

													}
												});
									}
								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shouldNotPullMessageWithDelay() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		QueueMessage msg = new QueueMessage();
		msg.setDelay(3000);
		msg.setMessage("sample1");
		QueueMessage[] msgs = { msg };
		que.push(msgs, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {

					que.pull(1, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							Assert.assertTrue(e != null);
							if (msgs != null) {
								Assert.fail("Pulled message despite delay");

							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shouldNotPullFromInexistentQueue() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.pull(1, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				Assert.assertTrue(e != null);
				if (msgs != null) {
					Assert.fail("Should not pull from inexistent queue");

				}

			}
		});
	}

	@Test(timeout = 500000)
	public void shoulNotPullSameMessageTwice() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample1", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {

				if (msgs != null) {
					if (msgs[0].getMessage().equals("sample1")) {
						que.getMessage(1, new CloudQueueMessageCallback() {

							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								if (msgs != null) {
									if (msgs[0].getMessage().equals("sample1")) {
										que.getMessage(
												1,
												new CloudQueueMessageCallback() {

													@Override
													public void done(
															QueueMessage[] msgs,
															CloudException e) {
														Assert.assertTrue(msgs == null);
													}
												});
									}
								}

							}
						});
					}
				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldGetNullForWrongMessageById() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.getMessageById("sample",
							new CloudQueueMessageCallback() {

								@Override
								public void done(QueueMessage[] msgs,
										CloudException e) {
									Assert.assertTrue(msgs.length==0);

								}
							});

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void deleteById() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					final String id = msgs[0].getId();
					que.deleteMessage(id, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {

							Assert.assertTrue(msgs != null
									&& msgs[0].getId().equals(id));

						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void deleteByMessageObject() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					final String id = msgs[0].getId();
					que.deleteMessage(msgs[0], new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {

							Assert.assertTrue(msgs != null
									&& msgs[0].getId().equals(id));

						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldNotGetAfterDelete() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					final String id = msgs[0].getId();
					que.deleteMessage(id, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {

							if (msgs != null && msgs[0].getId().equals(id)) {
								que.getMessageById(id,
										new CloudQueueMessageCallback() {

											@Override
											public void done(
													QueueMessage[] msgs,
													CloudException e) {
												Assert.assertTrue(msgs.length==0);

											}
										});
							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	// should push data into the queue
	public void shouldAddSubscriberToQueue() {
		initialize();
		CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		que.addSubscriber("http://egima.blogspot.com",
				new CloudQueueCallback() {

					@Override
					public void done(CloudQueue q, CloudException e) {
						if (q != null) {
							try {
								JSONArray array = q.getDocument()
										.getJSONArray("subscribers");
								String subscribers = array.toString()
										.replace("\"\"", "").replace("[", "")
										.replace("]", "");
								String[] arr = subscribers.split(",");
								Assert.assertTrue(arr.length > 0);
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}
						if (e != null) {
							Assert.fail(e.getMessage());
						}
					}
				});
	}

	@Test(timeout = 60000)
	public void shouldAddMultiplerSubscribers() {
		initialize();
		CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		String[] arr = { "http://egima.blogspot.com", "http://nandala.com" };
		que.addSubscriber(arr, new CloudQueueCallback() {

			@Override
			public void done(CloudQueue q, CloudException e) {
				if (q != null) {
					try {
						JSONArray array = q.getDocument()
								.getJSONArray("subscribers");
						String subscribers = array.toString()
								.replace("\"\"", "").replace("[", "")
								.replace("]", "");
						String[] arr = subscribers.split(",");
						Assert.assertTrue(arr.length == 2);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
				if (e != null) {
					Assert.fail(e.getMessage());
				}
			}
		});
	}

	@Test(timeout = 50000)
	// should push data into the queue
	public void shouldDeleteSubscriber() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		String[] arr = { "http://egima.blogspot.com", "http://nandala.com" };
		que.addSubscriber(arr, new CloudQueueCallback() {

			@Override
			public void done(CloudQueue q, CloudException e) {
				if (q != null) {
					que.deleteSubscriber("http://egima.blogspot.com",
							new CloudQueueCallback() {

								@Override
								public void done(CloudQueue q, CloudException e) {
									try {
										JSONArray array = q.getDocument()
												.getJSONArray("subscribers");
										String subscribers = array.toString()
												.replace("\"\"", "")
												.replace("[", "")
												.replace("]", "");
										String[] arr = subscribers.split(",");

										Assert.assertTrue(arr.length == 1);
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
								}
							});

				}
				if (e != null) {
					Assert.fail(e.getMessage());
				}
			}
		});
	}

	@Test(timeout = 50000)
	// should push data into the queue
	public void shouldDeleteMultipleSubscribers() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		String[] arr = { "http://egima.blogspot.com", "http://nandala.com",
				"http://sample.com" };
		final String[] del = { arr[0], arr[1] };
		que.addSubscriber(arr, new CloudQueueCallback() {

			@Override
			public void done(CloudQueue q, CloudException e) {
				if (q != null) {
					que.deleteSubscriber(del, new CloudQueueCallback() {

						@Override
						public void done(CloudQueue q, CloudException e) {
							try {
								JSONArray array = q.getDocument()
										.getJSONArray("subscribers");
								String subscribers = array.toString()
										.replace("\"\"", "").replace("[", "")
										.replace("]", "");
								String[] arr = subscribers.split(",");
								Assert.assertTrue(arr.length == 1);
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}
					});

				}
				if (e != null) {
					Assert.fail(e.getMessage());
				}
			}
		});
	}

	@Test(timeout = 50000)
	// should push data into the queue
	public void shouldNotSubscribeInvalidUrl() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		que.addSubscriber("sample/sample",
				new CloudQueueCallback() {

					@Override
					public void done(CloudQueue q, CloudException e) {

						if (q != null)
							Assert.fail("Should not save subscriber with invalid url");

						if (e != null) {
							Assert.assertTrue(true);
						}
					}
				});
	}

	@Test(timeout = 50000)
	public void shouldAddandRemoveSubscriber() {
		initialize();
		final CloudQueue que = new CloudQueue(PrivateMethod._makeString(), null);
		que.addSubscriber("http://thermodynamiccomputing.com/",
				new CloudQueueCallback() {

					@Override
					public void done(CloudQueue q, CloudException e) {

						if (q != null) {
							if (q.getSubscribers().size() == 1) {
								que.removeSubscriber(
										"http://thermodynamiccomputing.com/",
										new CloudQueueCallback() {

											@Override
											public void done(CloudQueue q,
													CloudException e) {
												if (e != null)
													Assert.fail(e.getMessage());
												if (q != null) {
													try {
														JSONArray arr = q.getDocument()
																.getJSONArray("subscribers");
														Assert.assertTrue(arr
																.length() == 0);
													} catch (JSONException e1) {
	
														e1.printStackTrace();
													}

												}
											}
										});
							}
						}

						if (e != null) {
							Assert.fail(e.getMessage());
						}
					}
				});
	}

	@Test(timeout = 60000)
	public void shouldDeleteQueueByName() {
		initialize();
		final String queueName = UUID.uuid(8);
		final CloudQueue que = new CloudQueue(queueName, null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.deleteQueue(queueName, new CloudQueueCallback() {

						@Override
						public void done(CloudQueue q, CloudException e) {
							if (e != null)
								Assert.fail(e.getMessage());
							if (q != null) {
								que.pull(1, new CloudQueueMessageCallback() {

									@Override
									public void done(QueueMessage[] msgs,
											CloudException e) {
										Assert.assertTrue(msgs == null);

									}
								});
							}
						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	public void shouldDeleteTheQueue() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.deleteQueue(new CloudQueueCallback() {

						@Override
						public void done(CloudQueue q, CloudException e) {
							if (e != null)
								Assert.fail(e.getMessage());
							if (q != null) {
								que.pull(1, new CloudQueueMessageCallback() {

									@Override
									public void done(QueueMessage[] msgs,
											CloudException e) {
										Assert.assertTrue(msgs == null);

									}
								});
							}
						}
					});

				}

			}
		});
	}

	@Test(timeout = 60000)
	// should clear queue
	public void shouldClearQueue() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push(new String[] { "sample", "egima", "ayiko", "ben" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msg, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msg != null) {
							que.clear(new CloudQueueCallback() {

								@Override
								public void done(CloudQueue q, CloudException e) {
									if (e != null)
										Assert.fail(e.getMessage());
									if (q != null) {
										q.pull(1,
												new CloudQueueMessageCallback() {

													@Override
													public void done(
															QueueMessage[] msgs,
															CloudException e) {
														Assert.assertTrue(msgs == null);

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
	// should push data array into the queue
	public void shouldGetQueue() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push(new String[] { "sample", "egima" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msg, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msg != null) {
							que.get(new CloudQueueCallback() {

								@Override
								public void done(CloudQueue q, CloudException e) {
									if (e != null)
										Assert.fail(e.getMessage());
									if (q != null) {
										try {
											int msgCount = q.getDocument()
													.getInt("totalMessages");
											Assert.assertTrue(msgCount == 2);
										} catch (JSONException e1) {
											e1.printStackTrace();
										}

									}

								}
							});

						}

					}
				});
	}

	@Test(timeout = 50000)
	// should get queue by name
	public void shouldGetQueueByName() {
		initialize();
		final String queueName = UUID.uuid(8);
		final CloudQueue que = new CloudQueue(queueName, null);
		que.push(new String[] { "sample", "egima" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msg, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msg != null) {
							que.get(queueName, new CloudQueueCallback() {

								@Override
								public void done(CloudQueue q, CloudException e) {
									if (e != null)
										Assert.fail(e.getMessage());
									if (q != null) {
										try {
											int msgCount = q.getDocument()
													.getInt("totalMessages");
											Assert.assertTrue(msgCount == 2);
										} catch (JSONException e1) {
											
											e1.printStackTrace();
										}

									}

								}
							});

						}

					}
				});
	}

	@Test(timeout = 50000)
	// should get queue by name
	public void shouldNotGetTheQueueWithNullName() {
		initialize();
		final String queueName = UUID.uuid(8);
		final CloudQueue que = new CloudQueue(queueName, null);
		que.addMessage(new String[] { "sample", "egima" },
				new CloudQueueMessageCallback() {

					@Override
					public void done(QueueMessage[] msg, CloudException e) {
						if (e != null) {
							Assert.fail(e.getMessage());
						}
						if (msg != null) {
							que.get(null, new CloudQueueCallback() {

								@Override
								public void done(CloudQueue q, CloudException e) {
									Assert.assertTrue(e!=null);
								}
							});

						}

					}
				});
	}

	@Test(timeout = 500000)
	// should get queue by name
	public void shoulGetAll() {
		initialize();
		CloudQueue.getAll(new CloudQueueArrayCallback() {

			@Override
			public void done(CloudQueue[] q, CloudException e) {
				if (e != null)
					Assert.fail(e.getMessage());
				if (q != null) {
					Assert.assertTrue(q.length > 0);

				}

			}
		});
	}

	@Test(timeout = 500000)
	// should get queue by name
	public void shouldNotGetInexistentQueue() {
		initialize();
		final String queueName = UUID.uuid(8);
		final CloudQueue que = new CloudQueue(queueName, null);
		que.get(new CloudQueueCallback() {

			@Override
			public void done(CloudQueue q, CloudException e) {

				Assert.assertTrue(e != null);
			}
		});
	}

	@Test(timeout = 50000)
	public void pullMessageAfterDelay() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		QueueMessage msg = new QueueMessage();
		msg.setMessage("sample");
		msg.setDelay(2000);
		QueueMessage[] msgs={msg};
		que.addMessage(msgs, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {

				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					QueueMessage msg = msgs[0];
					if (msg.getMessage().equals("sample")) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						que.getMessage(1, new CloudQueueMessageCallback() {
							@Override
							public void done(QueueMessage[] msgs,
									CloudException e) {
								if (e != null)
									Assert.fail(e.getMessage());
								if (msgs != null) {
									Assert.assertEquals(msgs[0].getMessage(),
											"sample");
								}
							}
						});
					}
				}

			}

		});
	}

	@Test(timeout = 50000)
	public void pullMessageAfterTimeout() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		QueueMessage msg = new QueueMessage();
		msg.setMessage("sample");
		msg.setTimeout(3);
		QueueMessage[] msgs={msg};
		que.addMessage(msgs, new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.getMessage(1, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							if (e != null) {
								Assert.fail(e.getMessage());
							}
							if (msgs != null) {
								QueueMessage msg = msgs[0];
								
								if (msg.getMessage().equals("sample")) {
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e1) {
										
										e1.printStackTrace();
									}
									que.getMessage(1,
											new CloudQueueMessageCallback() {
												@Override
												public void done(
														QueueMessage[] msgs,
														CloudException e) {
													if (e != null)
														Assert.fail(e
																.getMessage());
													if (msgs != null) {
														Assert.assertEquals(
																msgs[0].getMessage(),
																"sample");
													}
												}
											});
								}
							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 50000)
	public void refreshMessageTimeoutPulledMessage() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.addMessage("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					que.getMessage(1, new CloudQueueMessageCallback() {

						@Override
						public void done(QueueMessage[] msgs, CloudException e) {
							QueueMessage msg = msgs[0];
							que.refreshMessageTimeout(msg,
									new CloudQueueMessageCallback() {

										@Override
										public void done(QueueMessage[] msgs,
												CloudException e) {
											Assert.assertTrue(msgs == null);

										}
									});

						}
					});

				}

			}
		});
	}

	@Test(timeout = 50000)
	public void refreshMessageTimeoutUnSpecifiedTimeout() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					QueueMessage msg = msgs[0];
					que.refreshMessageTimeout(msg,
							new CloudQueueMessageCallback() {

								@Override
								public void done(QueueMessage[] msgs,
										CloudException e) {
									if (e != null) {
										Assert.fail(e.getMessage());
									}
									if (msgs != null) {
										QueueMessage msg = msgs[0];
										int timeout = (int) msg
												.getElement("timeout");
										Assert.assertEquals(timeout, 1800);
									}

								}
							});

				}

			}
		});
	}

	@Test(timeout = 50000)
	public void refreshMessageTimeoutSpecifiedTimeout() {
		initialize();
		final CloudQueue que = new CloudQueue(UUID.uuid(8), null);
		que.push("sample", new CloudQueueMessageCallback() {

			@Override
			public void done(QueueMessage[] msgs, CloudException e) {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (msgs != null) {
					QueueMessage msg = msgs[0];
					que.refreshMessageTimeout(msg, 3600,
							new CloudQueueMessageCallback() {

								@Override
								public void done(QueueMessage[] msgs,
										CloudException e) {
									if (e != null) {
										Assert.fail(e.getMessage());
									}
									if (msgs != null) {
										QueueMessage msg = msgs[0];
										int timeout = (int) msg
												.getElement("timeout");
										Assert.assertEquals(timeout, 3600);
									}

								}
							});

				}

			}
		});
	}
	@Test(timeout=60000)//should get queue by name
	public void shouldUpdateQueue(){
		initialize();
		final String queueName=UUID.uuid(8);
		final CloudQueue que=new CloudQueue(queueName,null);
		que.push(new String[]{"sample"},  new CloudQueueMessageCallback() {
			
			@Override
			public void done(QueueMessage[] msg, CloudException e) {
				if(e!=null){
					
					Assert.fail(e.getMessage());
				}
				if(msg!=null){
					que.addSubscriber("http://www.google.com",new CloudQueueCallback() {
						
						@Override
						public void done(CloudQueue qu, CloudException e) {
							if(e!=null)
								Assert.fail(e.getMessage());
							if(qu!=null){
								que.setAttribute(CloudQueue.QUEUE_TYPE, "push");
								que.update(new CloudQueueCallback() {
									
									@Override
									public void done(CloudQueue q, CloudException e) {
										
										if(q!=null){
										String queueType=(String) q.getAttribute(CloudQueue.QUEUE_TYPE);
										Assert.assertEquals(queueType, "push");}
										else if(e!=null){
											Assert.fail(e.getMessage());}
										
									}
								});
								
								
							}
							
						}
					});

				}
				
			}
		});
	}
}
