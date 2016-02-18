package com.digitale.myepiclife.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.digitale.database.MELEventList;
import com.digitale.database.MELEventLoader;
import com.digitale.myepiclife.MyEpicLife;

public class DesktopLauncher {
	public static void main (String[] arg) {
	
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=480;
		config.width=800;
		new LwjglApplication(new MyEpicLife(), config);
		MELEventLoader MELLoader = new MELEventLoader();
		MyEpicLife.eventList=(MELEventList) MELLoader.load("eventDb.json");
	}
}
