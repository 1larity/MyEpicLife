package com.digitale.screens;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class ImageButtonHolder {
	private String buttonName;
	private ImageButton buttonDef;
	
	/** Create default button entry**/
	public ImageButtonHolder(){
		
	}
	
	/** Create populated button **/
public ImageButtonHolder(String buttonName,ImageButton buttonDef){
		setButtonDef(buttonDef);
		setButtonName(buttonName);
	}

	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public ImageButton getButtonDef() {
		return buttonDef;
	}
	public void setButtonDef(ImageButton buttonDef) {
		this.buttonDef = buttonDef;
	}
}
