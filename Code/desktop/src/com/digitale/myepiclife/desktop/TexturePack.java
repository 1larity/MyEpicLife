package com.digitale.myepiclife.desktop;
//pack textures for ui
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class TexturePack {
    public static void main(String[] args) throws Exception {
    	//holo skin
        TexturePacker.process(
                "assets-raw/skin/holo", 
                "../android/assets/data/ui", 
                "uiskinholo.atlas");
        //libgdx skin
        TexturePacker.process(
                "assets-raw/skin/default", 
                "../android/assets/data/ui", 
                "uiskin.atlas");
        //hifi skin
        TexturePacker.process(
                "assets-raw/skin/hifi", 
                "../android/assets/data/ui", 
                "uiskinhifi.atlas");
        TexturePacker.process(
                "assets-raw/skin/misc", 
                "../android/assets/data/misc", 
                "misc.atlas");
        //hifibuttons
        TexturePacker.process(
                "assets-raw/skin/hifibuttons", 
                "../android/assets/data/ui", 
                "hifibuttons.atlas");
    }
}