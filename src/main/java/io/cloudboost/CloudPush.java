package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.util.HashMap;
import java.util.Map;

public class CloudPush {
	public CloudPush() {

	}

	public void send(PushData pushData,Object query,
			CloudPushCallback callback) throws CloudException {
		String _tableName = "Device";
		CloudQuery pushQuery = new CloudQuery(_tableName);
		if (CloudApp.getAppId() == null) {
			callback.done(null, new CloudException("CloudApp.appId is null"));
			return;
		}
		if (pushData == null) {
			callback.done(null, new CloudException("pushData object is null"));
			return;
		}
		if (pushData.getMessage() == null) {
			callback.done(null, new CloudException(
					"message is not set in pushData"));
			return;
		}
		if(query instanceof CloudQuery){
			pushQuery=(CloudQuery) query;
			pushQuery.setTableName(_tableName);
		}
		if(query!=null&&query instanceof String[]){
			pushQuery.containedIn("channels", (String[])query);
		}
		if(query instanceof String){
			pushQuery.containedIn("channels", new String[]{(String) query});
		}
		Map<String,Object> _params=new HashMap<>();
		_params.put("query", pushQuery.getQuery());
		_params.put("sort", pushQuery.getSort());
		_params.put("limit", pushQuery.getLimit());
		_params.put("skip", pushQuery.getSkip());
		_params.put("data", pushData);
		_params.put("key", CloudApp.getAppKey());
		JSONObject _jsonParams=new JSONObject(_params);
		String _url=CloudApp.getServerUrl()+"/push/"+CloudApp.getAppId()+"/send";
		CBResponse response=CBParser.callJson(_url, "POST", _jsonParams);
		if(response.getStatusCode()==200){
			System.out.println("response: "+response.toString());
		}
		else{
			callback.done(null, new CloudException(response.getError()));
		}

	}
}
