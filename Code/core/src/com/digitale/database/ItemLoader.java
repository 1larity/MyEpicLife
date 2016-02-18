package com.digitale.database;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

public class ItemLoader {
/**load Json from given filename **/
    public ArrayList load(String db) {
        //Persist JSON object in a Collection, in my case an ArrayList
        ArrayList itemArrayList = new ArrayList();
        
        //Setup JSON requirements
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        //Convert each JSON obj to an Weapon obj
        for (Object obj : json.fromJson(ArrayList.class, Weapon.class, new FileHandle(db))) {
            itemArrayList.add((Weapon) obj);
        }

        return itemArrayList;
    }
}