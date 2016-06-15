#Introduction

[![Build Status](https://travis-ci.org/CloudBoost/JavaSDK.png)](https://travis-ci.org/CloudBoost/JavaSDK)

This is the Java SDK for CloudBoost. It is available as a [JAR on Git](https://github.com/CloudBoost/JavaSDK/blob/master/target/JavaSDK-1.0.2.jar) and as a [maven dependency](http://mvnrepository.com/artifact/io.cloudboost/JavaSDK). If you want to have a look into documentation, you can check them out here : [https://tutorials.cloudboost.io](https://tutorials.cloudboost.io) and API reference is available here : [https://docs.cloudboost.io](https://docs.cloudboost.io)

###Dependencies
####The following dependencies are included in the repo in the <code>libs</code> folder. Add them to your project path to be able to use the SDK.
<ul>
<li>Javasdk for cloudboost Javasdk-1.0.1.jar </li>
<li>Socket-client.jar</li>
<li>Okhttp-2.4.0.jar</li>
<li>Okhttp-ws-2.4.0.jar</li>
<li>Okio-1.4.0.jar</li>
</ul>

### JAR Usage

``` java
//Add cloudboost java sdk jar file in your project
import io.cloudboost.*;
```

### Sample Code

``` java

// AppID and AppKey are your App ID and key of the application created in CloudBoost Dashboard.

//Init your Application
CloudApp.init("YourAppId","YourAppKey");

//Data Storage : Create a CloudObject of type 'Custom' (Note: You need to create a table 'Custom' on CloudBoost Dashboard)

CloudObject obj = new CloudObject("Custom");

//Set the property 'name' (Note: Create a column 'name' of type text on CloudBoost Dashboard)
obj.set("name","CloudBoost");

//Save the object
obj.save(new CloudObjectCallback(){
	@Override
	public void done(CloudObject object, CloudException err) {
		if(err != null){
			//save operation failed
		}
		if(object != null){
		  //object saved successfully
		}
	}
});

```

<img align="right" height="150" src="https://cloud.githubusercontent.com/assets/5427704/7724257/b7f45d6c-ff0d-11e4-8f60-06024eaa1508.png">
#### Documentation

Visit the [CloudBoost Docs](http://docs.cloudboost.io) for documentation.

