package com.digitale.myepiclife.android;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class WidgetMELEventSaver {
	/**save supplied arraylist as Json with a given filename **/
	public static final String TAG = "MELEVENTSAVER";
	  public  void save(String db,ArrayList data) {
		  
		 /* FileHandle[] filesList = Gdx.files.external("").list();
		  for(FileHandle file: filesList) {
			  System.out.println(TAG+"external dir :- "+file);
		  }*/
	        //Persist JSON object in a Collection, in my case an ArrayList
	        ArrayList itemArrayList = new ArrayList();
	        FileHandle file = Gdx.files.local(db);
	        FileHandle debugfile = Gdx.files.external(""+db);
	        String extFiles=Gdx.files.getExternalStoragePath();
	        //Setup JSON requirements
	        Json json = new Json();
	        json.setTypeName(null);
	        json.setUsePrototypes(false);
	        json.setIgnoreUnknownFields(true);
	        json.setOutputType(JsonWriter.OutputType.json);
	        String localFiles=Gdx.files.getLocalStoragePath();
	        
			System.out.println(TAG+"Saving evnts list to :- "+localFiles+" file:-"+db);
	        file.writeString(json.prettyPrint(data), false);
	       // if(MyService.DEBUG){
	        	debugfile.writeString(json.prettyPrint(data), false);
	        	System.out.println(TAG+"Saving debug event list to :- "+extFiles+" file:-"+db);
	      // }
	    }
}