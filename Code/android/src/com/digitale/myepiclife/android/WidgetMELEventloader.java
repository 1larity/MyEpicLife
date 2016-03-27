package com.digitale.myepiclife.android;


import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventList;


public class WidgetMELEventloader {
	private String TAG = "WIDGETMELEVENTLOADER: ";
	/** load Json from given filename **/
	public ArrayList load(String db) {
		if (MyService.DEBUG)
			System.out.println(TAG+"MELEvents loading from file: " + db);
		String path="/data/data/com.digitale.myepiclife.android/files/";
		
		// Persist JSON object in a Collection, in my case an ArrayList
		ArrayList itemArrayList = new MELEventList();

		// Setup JSON requirements
		Json json = new Json();
		json.setTypeName(null);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(JsonWriter.OutputType.json);
		// Convert each JSON obj to an MELEvent obj


		//String localFiles=Gdx.files.getLocalStoragePath();
	
		System.out.println(TAG+"Loading events list from :- "+path+" file:-"+db);
		
		try {
			FileHandle datafile =new FileHandle(path+db);
			for (Object obj : json.fromJson(ArrayList.class, MELEvent.class,
					datafile)) {
				itemArrayList.add((MELEvent) obj);

			}

		} catch (com.badlogic.gdx.utils.SerializationException e) {
			String subCause = "" + e.getCause().getCause().getCause();
			if (subCause.contains("The system cannot find the file specified")) {
				System.out
						.println(TAG+"No Event data file present (probable first run).");
			} else {
				e.printStackTrace();
			}
		}
		if(MyService.DEBUG){
			//for(MELEvent event:itemArrayList){
				
			//}
		}
		return itemArrayList;
	}
}