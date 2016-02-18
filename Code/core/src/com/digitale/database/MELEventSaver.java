package com.digitale.database;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.digitale.myepiclife.MyEpicLife;

public class MELEventSaver {
	/**save supplied arraylist as Json with a given filename **/
	public static final String TAG = "MELEVENTSAVER ";
	  public  void save(String db,ArrayList data) {
	        //Persist JSON object in a Collection, in my case an ArrayList
	        ArrayList itemArrayList = new ArrayList();
	        FileHandle file = Gdx.files.local(db);
	        
	        //Setup JSON requirements
	        Json json = new Json();
	        json.setTypeName(null);
	        json.setUsePrototypes(false);
	        json.setIgnoreUnknownFields(true);
	        json.setOutputType(JsonWriter.OutputType.json);
	        String localFiles=Gdx.files.getLocalStoragePath();
	        
	        if (MyEpicLife.DEBUG)System.out.println(TAG+"Saving events list to :- "+localFiles+" file:-"+db);
	        file.writeString(json.prettyPrint(data), false);
	       
	    }
}