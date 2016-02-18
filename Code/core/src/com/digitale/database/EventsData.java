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

//** manage user created events data **//
public class EventsData {

	// ** retrieve asset catalogue from database **//*
	public void getEventList() {
		FileHandle file = Gdx.files.local("MyEpicLifeData.csv");
		
		String fileData = file.readString();
		System.out.println("Filedata " + fileData);
		String[] row = fileData.split("\n");
		int i = 0;

		while (i < row.length) {

			String[] rowData = row[i].split(",");
			System.out.println("rowdata  " + row[i]);
			AssetDef thisAssetDef = new AssetDef();
			thisAssetDef.setUid(Integer.valueOf(rowData[0]));
			thisAssetDef.setAssetName(rowData[1]);
			thisAssetDef.setFilename(rowData[2]);
			thisAssetDef.setExt(rowData[3]);
			thisAssetDef.setType(rowData[4].trim());
			MyEpicLife.assetList.add(thisAssetDef);
			i++;

			if (MyEpicLife.DEBUG) {
				System.out.println("saving asset " + thisAssetDef.getUid()
						+ ":" + thisAssetDef.getAssetName() + ";"
						+ thisAssetDef.getFilename() + ";"
						+ thisAssetDef.getExt() + ";" + thisAssetDef.getType());
			}

		}
		if (MyEpicLife.DEBUG) {
			for (int j = 0; j < MyEpicLife.assetList.size(); j++) {
				System.out.println("assetlist " + i
						+ MyEpicLife.assetList.get(j).getUid() + ":"
						+ MyEpicLife.assetList.get(j).getAssetName() + ";"
						+ MyEpicLife.assetList.get(j).getFilename() + ";"
						+ MyEpicLife.assetList.get(j).getExt() + ";"
						+ MyEpicLife.assetList.get(j).getType());
			}
		}
	}

	// Queue assets to be loaded

	public void enqueueAssets() {
		String fileName;
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMap;
		param.magFilter = TextureFilter.MipMapNearestLinear;
		param.genMipMaps = true;
		for (int i = 0; i < MyEpicLife.assetList.size(); i++) {
			String assetType = MyEpicLife.assetList.get(i).getType();
			fileName = (MyEpicLife.assetList.get(i).getFilename() + "." + MyEpicLife.assetList
					.get(i).getExt());
			if (MyEpicLife.DEBUG)
				System.out
						.println("enqueue " + fileName + " type " + assetType);

			if ((MyEpicLife.assetList.get(i).getType()).equals("font")) {
				MyEpicLife.manager.load("data/ui/" + fileName, BitmapFont.class);

			} else if ((MyEpicLife.assetList.get(i).getType()).equals("3dtex")
					|| (MyEpicLife.assetList.get(i).getType()).equals("2dtex")) {
				MyEpicLife.manager.load("data/" + fileName, Texture.class);

			} else if ((MyEpicLife.assetList.get(i).getType()).equals("sound")) {
				MyEpicLife.manager.load("sound/" + fileName, Sound.class);
			} else if ((MyEpicLife.assetList.get(i).getType()).equals("music")) {
				MyEpicLife.manager.load("sound/" + fileName, Music.class);
			} else if ((MyEpicLife.assetList.get(i).getType())
					.equals("itemicon")) {
				MyEpicLife.manager.load("data/" + fileName, Pixmap.class);
			} else if (assetType.equals("icon")) {
				MyEpicLife.manager.load("data/ui/" + fileName, Texture.class);
				if (MyEpicLife.DEBUG)
					System.out.println("loaded " + fileName);
			} else {
				System.out.println("Unrecognised asset type " +
				fileName + " type " + assetType + "@");
			}
		}
	}
}