package com.digitale.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.digitale.myepiclife.EventTemplate;
import com.digitale.myepiclife.MyEpicLife;

//** manage app event template database **//*
public class EventTemplateData {
	private String TAG="EVENTTEMPLATEDATA: ";
	// ** retrieve Event template catalogue from database **//*
	public void getEventTemplateDefinitions() {

		FileHandle file = Gdx.files.internal("eventtemplates.csv");
		String fileData = file.readString();
	
		String[] row = fileData.split("\n");
		int i = 0;

		while (i < row.length) {

			String[] rowData = row[i].split(";");
			if(MyEpicLife.DEBUG)
			System.out.println(TAG+"rowdata  " + row[i]);
			EventTemplate thisEventTemplate = new EventTemplate();
			try{
			thisEventTemplate.setUid(Integer.valueOf(rowData[0]));
			thisEventTemplate.setName(rowData[1]);
			thisEventTemplate.setGoodBad(Integer.valueOf(rowData[2]));
			thisEventTemplate.setUnitType(rowData[3]);
			thisEventTemplate.setDefaultAlarmTime(Long.valueOf(rowData[4]));
			thisEventTemplate.setDefaultReminderPeriod(rowData[5]);
			thisEventTemplate.setCategory(rowData[6]);
			thisEventTemplate.setReportQuestion(rowData[7]);
			thisEventTemplate.setDefaultFrequency(rowData[8]);
			thisEventTemplate.setDescription(rowData[9].trim());
			//thisEventTemplate.setRepeat(thisEventTemplate.getDefaultFrequency);
			MyEpicLife.eventTemplateList.add(thisEventTemplate);
			}catch (java.lang.NumberFormatException e) {
				System.out.println(TAG+"Corrupt Event Template Data " + thisEventTemplate.getUid()
						+ ":" + thisEventTemplate.getName() + ";"
						+ ":" + thisEventTemplate.getGoodBad() + ";"
						+ ":" + thisEventTemplate.getUnitType() + ";"
						+ ":" + thisEventTemplate.getCategory() + ";"
						+ ":" +thisEventTemplate.getReportQuestion() + ";"
						+ ":" +thisEventTemplate.getDefaultFrequency() + ";"
						+ ":" +thisEventTemplate.getDescription()
						);
	            e.printStackTrace();
	        }
			i++;
			
			if (MyEpicLife.DEBUG) {
				System.out.println(TAG+"saving template " + thisEventTemplate.getUid()
						+ ":" + thisEventTemplate.getName() + ";"
						+ thisEventTemplate.getGoodBad() + ";"
						+ thisEventTemplate.getUnitType());
			}

		}
	/*	if (MyEpicLife.DEBUG) {
			for (int j = 0; j < MyEpicLife.assetList.size(); j++) {
				System.out.println("assetlist " + i
						+ MyEpicLife.assetList.get(j).getUid() + ":"
						+ MyEpicLife.assetList.get(j).getAssetName() + ";"
						+ MyEpicLife.assetList.get(j).getFilename() + ";"
						+ MyEpicLife.assetList.get(j).getExt() + ";"
						+ MyEpicLife.assetList.get(j).getType());
			}
		}*/
	}


}