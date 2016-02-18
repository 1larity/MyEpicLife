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
import com.digitale.myepiclife.MyEpicLife;

//** manage app assets database **//*
public class AwardsData {
	private String TAG = "AWARDSDATA: ";
	// ** retrieve asset catalogue from database **//*
	public void getAwardDefinitions() {
		FileHandle file = Gdx.files.internal("awarddefs.csv");
		String fileData = file.readString();
		if (MyEpicLife.DEBUG)System.out.println(TAG+"Filedata " + fileData);
		String[] row = fileData.split("\n");
		int i = 0;

		while (i < row.length) {

			String[] rowData = row[i].split(",");
			if (MyEpicLife.DEBUG)System.out.println(TAG+"rowdata  " + row[i]);
			AwardDef thisAwardDef = new AwardDef();
			thisAwardDef.setUid(Integer.valueOf(rowData[0]));
			thisAwardDef.setQuality(rowData[1]);
			thisAwardDef.setTitle(rowData[2]);
			thisAwardDef.setText(rowData[3]);
			thisAwardDef.setRule(rowData[4]);
			thisAwardDef.setPointValue(Integer.valueOf(rowData[5].trim()));
			MyEpicLife.awardList.add(thisAwardDef);
			i++;

			if (MyEpicLife.DEBUG) {
				System.out.println(TAG+"saving asset " + thisAwardDef.getUid()
						+ ":" + thisAwardDef.getTitle() + ";"
						+ thisAwardDef.getRule() + ";");
			}

		}
		if (MyEpicLife.DEBUG) {
			for (int j = 0; j < MyEpicLife.assetList.size(); j++) {
				System.out.println(TAG+"assetlist " + i
						+ MyEpicLife.assetList.get(j).getUid() + ":"
						+ MyEpicLife.assetList.get(j).getAssetName() + ";"
						+ MyEpicLife.assetList.get(j).getFilename() + ";"
						+ MyEpicLife.assetList.get(j).getExt() + ";"
						+ MyEpicLife.assetList.get(j).getType());
			}
		}
	}


}