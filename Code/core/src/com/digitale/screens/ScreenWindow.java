package com.digitale.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.digitale.myepiclife.MyEpicLife;
/** creates default MEL-styled window**/
public class ScreenWindow extends Window{

	public Stage stage;
	private int screenX=Gdx.graphics.getWidth();
	private int screenY=Gdx.graphics.getHeight();
	public ScreenWindow( String title) {
		super(title,MyEpicLife.uiSkin);

		//set up title table
		this.getTitleTable().setSkin(MyEpicLife.uiSkin);
		this.getTitleTable().pad(5);
		//this.getTitleTable().setBackground("tree-minus");
	
		//set up title label
		this.getTitleLabel().setAlignment(Align.topLeft);
		LabelStyle labelStyle = new LabelStyle(MyEpicLife.titleFont,Color.CYAN);
		this.getTitleLabel().setStyle(labelStyle);
		
	
		//set up window
		if (MyEpicLife.DEBUG) this.debugAll();
		this.pad(0);
		this.padTop(30);
		this.setFillParent(true);
		this.setPosition(0, 0);
		//this.pad(0);
	}
}