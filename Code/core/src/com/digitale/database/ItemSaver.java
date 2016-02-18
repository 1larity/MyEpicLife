package com.digitale.database;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;


public class ItemSaver {
	/**save supplied arraylist as Json with a given filename **/

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
	        
	        file.writeString(json.prettyPrint(data), false);
	       
	    }
}