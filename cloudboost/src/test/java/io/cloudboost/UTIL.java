package io.cloudboost;

import io.cloudboost.Column.DataType;

import java.io.IOException;

public class UTIL {

	public static void init() {
//		CloudApp.init("bengi123", "mLiJB380x9fhPRCjCGmGRg==");
		CloudApp.init("jcpcjpzsgbtt", "4c9c7510-7c6b-459b-87e5-c3617f2daaf0");
	}

	public static void initMaster() {
		CloudApp.init("jcpcjpzsgbtt",
				"bf410abd-aa7e-45fb-8f55-03251d7886f4");
	}

	public static void initKisenyiMaster() {
		CloudApp.init("kisenyi", "3cau5aq6i85zzLiIpbZ84Y+NDkS7Ojlx4Mj26+oSsMg=");
	}

	public static void initKisenyi() {
		CloudApp.init("kisenyi", "yW0nFG/XF1GCfgaRdbj4KA==");
	}


    public static void main(String[] args) throws CloudException, IOException {
    	initMaster();
    	for(int i=0;i<34;i++){
    		CloudTable table=new CloudTable("NOTIFICATION_QUERY_"+i);
    		Column col1=new Column("name", DataType.Text);
    		Column col2=new Column("age", DataType.Number);
    		table.addColumn(new Column[]{col1,col2});
    		table.save(new CloudTableCallback() {
				
				@Override
				public void done(CloudTable table, CloudException e) throws CloudException {
					if(e!=null)
						System.out.println("error:"+e.getMessage());
					if(table!=null){
						System.out.println("success:");
					}
					
				}
			});

    	}

		
	}
}
