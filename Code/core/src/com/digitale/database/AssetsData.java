package com.digitale.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.digitale.myepiclife.MyEpicLife;
import com.digitale.utils.MELDebug;

//** manage app assets database **//*
public class AssetsData {
	private String TAG="ASSETMANAGER: ";
	private boolean localDebug=false;
	// ** retrieve asset catalogue from database **//*
	public void getAssetDefinitions() {
		Resolution _568x1136 = new Resolution(568, 1136, "568x1136");
		Resolution _1200x1920 = new Resolution(568, 1136, "1200x1920");
		ResolutionFileResolver resolver = new ResolutionFileResolver(
				new InternalFileHandleResolver(), _568x1136, _1200x1920);
		FileHandle file = Gdx.files.internal("assets.csv");
		String fileData = file.readString();
		MELDebug.log(TAG + "Filedata " + fileData,localDebug);
		String[] row = fileData.split("\n");
		int i = 0;

		while (i < row.length) {

			String[] rowData = row[i].split(",");
			MELDebug.log(TAG+"rowdata  " + row[i],localDebug);
			AssetDef thisAssetDef = new AssetDef();
			thisAssetDef.setUid(Integer.valueOf(rowData[0]));
			thisAssetDef.setAssetName(rowData[1]);
			thisAssetDef.setFilename(rowData[2]);
			thisAssetDef.setExt(rowData[3]);
			thisAssetDef.setType(rowData[4].trim());
			MyEpicLife.assetList.add(thisAssetDef);
			i++;

			MELDebug.log(TAG+"saving asset " + thisAssetDef.getUid()
						+ ":" + thisAssetDef.getAssetName() + ";"
						+ thisAssetDef.getFilename() + ";"
						+ thisAssetDef.getExt() + ";" + thisAssetDef.getType(),localDebug);


		}
		if (MyEpicLife.DEBUG) {
			for (int j = 0; j < MyEpicLife.assetList.size(); j++) {
				MELDebug.log(TAG + "assetlist " + i
						+ MyEpicLife.assetList.get(j).getUid() + ":"
						+ MyEpicLife.assetList.get(j).getAssetName() + ";"
						+ MyEpicLife.assetList.get(j).getFilename() + ";"
						+ MyEpicLife.assetList.get(j).getExt() + ";"
						+ MyEpicLife.assetList.get(j).getType(),localDebug);
			}
		}
	}

	// Queue assets to be loaded

	public void loadAssets() {
		String fileName;
		TextureParameter textureLoadParam = new TextureParameter();
		textureLoadParam.minFilter = TextureFilter.Linear;
		textureLoadParam.magFilter = TextureFilter.Linear;
		textureLoadParam.genMipMaps = true;

		BitmapFontParameter fontLoadParam = new BitmapFontParameter();
		fontLoadParam.minFilter = TextureFilter.Linear;
		fontLoadParam.magFilter = TextureFilter.Linear;
		fontLoadParam.genMipMaps = true;

		for (int i = 0; i < MyEpicLife.assetList.size(); i++) {
			String assetType = MyEpicLife.assetList.get(i).getType();
			fileName = (MyEpicLife.assetList.get(i).getFilename() + "." + MyEpicLife.assetList
					.get(i).getExt());

			MELDebug.log(TAG+"enqueue " + fileName + " type " + assetType,localDebug);
			if (assetType.equals("ttf")) {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
						Gdx.files.internal("data/ui/" + fileName));
				FreeTypeFontParameter parameter = new FreeTypeFontParameter();
				float textScale = Gdx.graphics.getDensity();
				parameter.size = (Gdx.graphics.getHeight() / 20);

				MyEpicLife.globalFont = generator.generateFont(parameter);

			} else if ((MyEpicLife.assetList.get(i).getType()).equals("font")) {
				MyEpicLife.manager.load("data/ui/" + fileName,
						BitmapFont.class, fontLoadParam);

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
				//MyEpicLife.manager.load("data/ui/" + fileName, Texture.class,
				//		textureLoadParam);
				MELDebug.log(TAG+"icon not loaded, using button atlas " + fileName,localDebug);
				//load skins
			} else if (assetType.equals("skin")) {
				MyEpicLife.manager.load("data/ui/" + fileName, Skin.class);
				MELDebug.log(TAG + "loaded " + fileName,localDebug);
			//load texture atlasses
			}else if (assetType.equals("atlas")) {
				MyEpicLife.manager.load("data/ui/" + fileName, TextureAtlas.class);
				MELDebug.log(TAG + "loaded " + fileName,localDebug);
			} else {

				System.out.println(TAG+"Unrecognised asset type " + fileName
						+ " type " + assetType + "@");
			}
		}
	}
}
