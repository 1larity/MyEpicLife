package com.digitale.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.digitale.myepiclife.MyEpicLife;
/** creates default MEL-styled table**/
public class ScreenTable extends Table{

	public Stage stage;
	private int screenX=Gdx.graphics.getWidth();
	private int screenY=Gdx.graphics.getHeight();
	public ScreenTable() {
		super(MyEpicLife.uiSkin);

		//set up table
		if (MyEpicLife.DEBUG) this.debugAll();
		this.setFillParent(true);
		//padding
		this.padTop((int)(screenY*(2/100.0f))).padBottom((int)(screenY*(2/100.0f)));
		this.padLeft((int)(screenX*(2/100.0f))).padRight((int)(screenX*(2/100.0f)));
		//padding row for window title divider
		this.row().fill().height(40);
		this.add();
		this.row().fill().expandX();
	
		
	}
}