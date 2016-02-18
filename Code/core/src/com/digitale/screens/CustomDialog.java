package com.digitale.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.digitale.myepiclife.MyEpicLife;

public class CustomDialog extends Dialog {  
	   
    public CustomDialog (String title) {  
        super(title, MyEpicLife.uiSkin);  
        initialize();  
    }  
      
    private void initialize() {  
        padTop(35); // set padding on top of the dialog title  
        getButtonTable().defaults().height(Gdx.graphics.getHeight()/5); // set buttons height 
        getButtonTable().defaults().fill().expandX();
        getButtonTable().padBottom(Gdx.graphics.getHeight()/50);
        getButtonTable().padLeft(Gdx.graphics.getHeight()/50).padRight(Gdx.graphics.getHeight()/50);
        setModal(true);  
        setMovable(true);  
        setResizable(false);  
    }  
  
    @Override  
    public CustomDialog text(String text) {  
       text(new Label(text, MyEpicLife.uiSkin)); 
       
        return this;  
    }  
	@Override
    /** Adds the given Label to the content table */
	public Dialog text (Label label) {
		 label.setWrap(true);
		 label.setWidth(100);
		 label.setAlignment(Align.center);
		this.getContentTable().add(label).width(Gdx.graphics.getWidth()/2.5f);
		return this;
	}
      
    /**  
     * Adds a text button to the button table.  
     * @param listener the input listener that will be attached to the button.  
     */  
    public Dialog button(String buttonText, InputListener listener) {  
        TextButton button = new TextButton(buttonText, MyEpicLife.uiSkin);  
        button.addListener(listener);  
        button.setColor(MyEpicLife.uiColour);
        button(button);  
        return this;  
    }  
  
    @Override  
    public float getPrefWidth() {  
        // force dialog width  
        return Gdx.graphics.getWidth()/2f;  
    }  
  
    @Override  
    public float getPrefHeight() {  
        // force dialog height  
        return Gdx.graphics.getHeight()/2f;  
    }
    @Override
	/** Adds the given button to the button table.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Dialog button (Button button, Object object) {
    	button.setColor(MyEpicLife.uiColour);
    	this.getButtonTable().add(button).minWidth(Gdx.graphics.getWidth()/4).minHeight(Gdx.graphics.getHeight()/6);
    
		setObject(button, object);
		return this;
	}
} 