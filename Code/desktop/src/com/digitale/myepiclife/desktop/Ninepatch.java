package com.digitale.myepiclife.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ninepatch implements ApplicationListener {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TextureAtlas buttonsAtlas; //** Holds the entire image **//
    private TextureRegion button1; //** Will Point to button1 (a TextureRegion) **//
    private NinePatch button2; //** Will Point to button2 (a NinePatch) **//
    private BitmapFont font;
    private NinePatch loadingBackground;
    public static void main (String[] arg) {
    	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ninepatch";
		cfg.height = 480;
		cfg.width = 800;
		new LwjglApplication(new Ninepatch(), cfg);
    }
    @Override
    public void create() {       
       
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); //** w/h ratio = 1.66 **//
       
        batch = new SpriteBatch();
        buttonsAtlas = new TextureAtlas("data/misc/misc.atlas"); //** buttonsAtlas has both buttons **//
     //   button1 = buttonsAtlas.findRegion("button1"); //** button1 - not 9 patch **//
        loadingBackground = new NinePatch(new Texture(Gdx.files.internal("data/misc/misc.png")), 10, 10, 10, 9);

        button2 = buttonsAtlas.createPatch("button2"); //** button2 - 9 patch **//
        font = new BitmapFont(); //** default font **//
        font.setColor(0, 0, 1, 1); //** blue font **//
        //font.setScale(2); //** 2 times size **//
    }

    @Override
    public void dispose() {
        batch.dispose();
        buttonsAtlas.dispose();
        font.dispose();
    }

    @Override
    public void render() {       
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
       
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
       // batch.draw(button1, 50, 0, 100, 100); //** not a nine patch **//
       // font.draw(batch, "Not a nine patch", 180, 80);
      //  button2.draw(batch, 50, 200, 100, 100); //** is a nine patch **//
        loadingBackground.draw(batch,50, 200, 200, 200);
        font.draw(batch, "Nine patch", 180, 280);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}