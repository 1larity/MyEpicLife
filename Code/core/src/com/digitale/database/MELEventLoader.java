package com.digitale.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.digitale.myepiclife.MyEpicLife;
import com.digitale.utils.MELDebug;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MELEventLoader {
	private String TAG = "MELEVENTLOADER: ";
	private boolean localDebug=false;
	/** load Json from given filename **/
	public MELEventList load(String db) {
		MELDebug.log(TAG + "MELEvents loading from file: " + db,localDebug);
		// Persist JSON object in a Collection, in my case an ArrayList
		MELEventList itemArrayList = new MELEventList();

		// Setup JSON requirements
		Json json = new Json();
		json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(JsonWriter.OutputType.json);
		// Convert each JSON obj to an MELEvent obj
		//String localFiles=Gdx.files.getLocalStoragePath();
		//if (MyEpicLife.DEBUG)System.out.println(TAG+"Loading events list from :- "+localFiles+" file:-"+db);
		
		try {
			FileHandle file = Gdx.files.local(db);
			for (Object obj : json.fromJson(ArrayList.class, MELEvent.class,
					file)) {
				itemArrayList.add((MELEvent) obj);

			}

		} catch (com.badlogic.gdx.utils.SerializationException e) {
			String subCause = "" + e.getCause().getCause();
			if (subCause.contains("File not found")) {
				System.out
						.println(TAG+"No Event data file present (probable first run).");
			} else {
				e.printStackTrace();
			}
		}
		if(MyEpicLife.DEBUG){
			//for(MELEvent event:itemArrayList){
				
			//}
		}
		itemArrayList.notificationSort();
		return itemArrayList;
	}
}