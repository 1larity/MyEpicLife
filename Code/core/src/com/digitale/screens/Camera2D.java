package com.digitale.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Camera2D {
	/** set screen to preferred viewport**/
	public float appWidth;
	/** set screen to preferred viewport**/

	public float appHeight;

	public OrthographicCamera camera;

	public Stage stage;

	public Camera2D(int appWidth, int appHeight) {
		camera= new OrthographicCamera();
		camera.setToOrtho(false, appWidth, appHeight);
		stage = new Stage(new StretchViewport(appWidth, appHeight,camera));
		Gdx.input.setInputProcessor(stage);
	}
	public void init(){
		
	
	}
	
}