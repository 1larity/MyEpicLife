/*
 * Copyright 2012 Richard Beech rp.beech@gmail.com
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.digitale.screens;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MyEpicLife;
import com.digitale.utils.MELDebug;

public class Splash extends MyEpicLifeScreen{
	public boolean localDebug=true;
	OrthographicCamera camera;
	SplashData data = new SplashData(1);
	/** the renderer **/
	// private final SplashRenderer renderer;
	/** sprite batch to draw text **/
	private SpriteBatch spriteBatch;
	public static int i = 0;
	public static com.badlogic.gdx.math.Vector3 touchPoint = new com.badlogic.gdx.math.Vector3();
	private Texture progressBarTexture;
	public static float cameraHorizAngle = -180;
	public static int cameraVertAngle = 15;
	private Timer timer = new Timer();
	private float planetspeed = 125;
	private float rotationspeed = 20.0f;
	public static boolean fadeOut;
	public static boolean fadeIn;
	public static float fadeTimer;
	private BitmapFont loadingfont;
	private BitmapFont versionfont;

	/** the cross fade texture **/
	private Texture xfadeTexture;
	private boolean netErrorShown = false;
	public static float planetmove;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
    public ModelBatch shadowBatch;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Environment environment;
	public boolean loading;
	private float progress;
	protected boolean doneflag=false;
    DirectionalShadowLight shadowLight;
	Stage stage;
	int screenX= Gdx.graphics.getWidth();
	int screenY=Gdx.graphics.getHeight();
	ProgressBar loadProgressBar;
	Label labelProgressPercent;
	public Splash() {
		SkinManager.skinSetup();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		composeTable();
		xfadeTexture = new Texture("data/black.png");
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();
		environment = new Environment();

		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
		environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 30f, 30f, 1f, 100f)).set(0.8f, 0.8f, 0.8f, 0f, 0f,
				-30f));
		environment.shadowMap = shadowLight;

		progressBarTexture = new Texture(
				Gdx.files.internal("data/progbar.png"), Pixmap.Format.RGB565, true);
		progressBarTexture
				.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Linear);
		cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(00f, -00f, 200f);
		cam.lookAt(0, 0, 1);
		cam.near = .5f;
		cam.far = 250f;
		cam.update();
		/**splash screen assets**/
		assets = new AssetManager();
		assets.load("data/ground.g3db", Model.class);
		assets.load("data/wall.g3db", Model.class);
		assets.load("data/newtitle.g3db", Model.class);
        shadowBatch = new ModelBatch(new DepthShaderProvider());
		loading = true;

		timer.scheduleAtFixedRate(new TimerTask() {


			public void run() {
				i++;

				// detect and set black fade in
				if (i < 10 && fadeIn == false) {
					fadeIn = true;
					fadeTimer = 1;
				}
				if (fadeIn) {
					fadeTimer = fadeTimer - 0.1f;
					MELDebug.log("fade " + fadeTimer, localDebug);
				}
				if (fadeIn && fadeTimer < 0) {
					fadeIn = false;
					fadeTimer = 0;
				}
				// detect and set white fade in
				if (i > 90 && fadeOut == false) {
					fadeTimer = 0;
					fadeOut = true;
				}
				if (fadeOut) {
					fadeTimer = fadeTimer + 0.1f;
					MELDebug.log("fade " + fadeTimer,localDebug);
				}
				if (i > 100) {
					fadeTimer = 1;
				}

				if (i % 10 == 0 && data.loadstate == (int) i / 10
						&& MyEpicLife.Error == MyEpicLife.ALL_OK) {
					MELDebug.log("loading stage " + (int) i / 10,localDebug);
					incrementalloader((int) i / 10);

				}
				if (i == 1) {
					// SoundManager.playmusic();
				}
				MELDebug.log("timer " + i % 10 + "  div" + (int) i
						/ 10 + " loadstate " + data.loadstate,localDebug);
			}
		}, 0, 500);

	}

	private void composeTable() {
		loadingfont = MyEpicLife.titleFont;
		versionfont = MyEpicLife.globalFont;
		labelProgressPercent = new Label("Loading ", MyEpicLife.uiSkin);
		labelProgressPercent.setStyle(new LabelStyle(MyEpicLife.titleFont,Color.CYAN));
		labelProgressPercent.setAlignment(Align.left);
		labelProgressPercent.setFontScale((1f/screenX)*500);
		Stack stackProgress=new Stack();
		Label labelVersion = new Label("Version "+MyEpicLife.version, MyEpicLife.uiSkin);
		labelVersion.setFontScale((10f/screenX)*100);
		labelVersion.setAlignment(Align.center);
		TextureRegionDrawable textureBar = new TextureRegionDrawable(new TextureRegion(MyEpicLife.uiSkin.getRegion("selection")));
		ProgressBarStyle barStyle = new ProgressBarStyle(MyEpicLife.uiSkin.newDrawable("default-rect"), null);
		barStyle.knobBefore =(MyEpicLife.uiSkin.newDrawable(("selection")));

		loadProgressBar = new ProgressBar(0, 100, 1f, false, MyEpicLife.uiSkin);
		loadProgressBar.setStyle(barStyle);
		stackProgress.add(labelProgressPercent);
		stackProgress.add(loadProgressBar);

		stackProgress.layout();
		Table loadScreenTable = new Table();
		//loadScreenTable.debugAll();
		loadScreenTable.setFillParent(true);
		//pad table to bottom of screen
		loadScreenTable.row().fill().expandY();
		loadScreenTable.add();
		loadScreenTable.row().fill().expandX();
		loadScreenTable.add(stackProgress).width(screenX/2);
		loadScreenTable.row().fill().expandX();
		loadScreenTable.add(labelProgressPercent).width(screenX/2);
		loadScreenTable.row().fill().expandX();
		loadScreenTable.add(labelVersion).width(screenX/2);
		loadScreenTable.layout();

		stage.addActor(loadScreenTable);
	}

	@Override
	public void dispose() {
		progressBarTexture.dispose();
		xfadeTexture.dispose();
		spriteBatch.dispose();
		modelBatch.dispose();
		instances.clear();
		assets.dispose();

	}

	@Override
	public boolean isDone() {
		//	MyEpicLife.gameMode = 2;
			return false;//doneflag;
	}

	@Override
	public void render(float delta) {
		/** If splash assets are not loaded call assetloader **/
		if (loading && assets.update()) {
			doneLoadingSplash();
		}

		if (assets.isLoaded("data/newtitle.g3db")){
			// camController.update();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                shadowLight.set(1f, 1f, 1f,-( Gdx.input.getX()-600),
                        Gdx.input.getY()-400, -30f);
                MELDebug.log("x:" + Gdx.input.getX() + "  y:"
                        + Gdx.input.getY(),localDebug);
            }
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            shadowLight.begin(Vector3.Zero, cam.direction);
            shadowBatch.begin(shadowLight.getCamera());
            shadowBatch.render(instances);
            shadowBatch.end();
            shadowLight.end();

			modelBatch.begin(cam);
			modelBatch.render(instances, environment);
			modelBatch.end();
			MyEpicLife.manager.update();
			progress = MyEpicLife.manager.getProgress();
			if (i > 30 && progress >= 1.0) {
				timer.cancel();
				doneflag = true;
				MyEpicLife.buttons=new MELButton();

			//	MyEpicLife.desktopTimer =new DesktopTimer();


			//		MyEpicLife.desktopTimer.start();

			}

			//MELDebug.log("loading progress " + progress,localDebug);
			renderHud();
		}
	}


	/** render 2d elements in front of 3d scene**/
	private void renderHud() {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		/** calculate and update progress bar and label**/
		loadProgressBar.setValue(progress*100);
		labelProgressPercent.setText("Loading "+ (int)(progress*100));
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		/** draw black foreground to fade in and out the splash**/
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,
				GL20.GL_ONE_MINUS_SRC_ALPHA);
		if (Splash.fadeIn) {
			spriteBatch.setColor(1, 1, 1, Splash.fadeTimer);
			spriteBatch.draw(xfadeTexture, 0, 0, screenX, screenY);
		}
		if (Splash.fadeOut) {
			spriteBatch.setColor(1, 1, 1, Splash.fadeTimer);
			spriteBatch.draw(xfadeTexture, 0, 0, screenX,screenY);
		}


		spriteBatch.end();
		/*
		 * if (MyEpicLife.Error==MyEpicLife.NETWORK_DOWN &&
		 * netErrorShown==false){ stage.addActor(Actors.bottomToast(
		 * "Connection to internet failed, Please check your network is working."
		 * , 5, skin));
		 *
		 * netErrorShown=true; }
		 */

	}
	/**Instance splash screen assets**/
	private void doneLoadingSplash() {
		Model ground = assets.get("data/ground.g3db", Model.class);
		ModelInstance groundInstance = new ModelInstance(ground);
		groundInstance.transform.setToTranslation(0, 0, 0);
		instances.add(groundInstance);

		Model wall = assets.get("data/wall.g3db", Model.class);
		ModelInstance wallInstance = new ModelInstance(wall);
		wallInstance.transform.setToTranslation(0, 0, 0);
		instances.add(wallInstance);

    	Model title = assets.get("data/newtitle.g3db", Model.class);
    	ModelInstance titleInstance = new ModelInstance(title);
		titleInstance.transform.setToTranslation(0, 0, 0);
		instances.add(titleInstance);
		/**done preparing splash assets**/

		loading = false;
	}

	private void incrementalloader(int time) {
		/*
		 * switch (time){ case 1: //load some stuff MyEpicLife.loadstage01();
		 * //increment loaded flag loadstate=2; break; case 2: //load some stuff
		 * MyEpicLife.loadstage02(); //increment loaded flag loadstate=3; break;
		 * case 3: //load some stuff MyEpicLife.loadstage03(); //increment
		 * loaded flag loadstate=4; break; case 4: //load some stuff
		 * MyEpicLife.loadstage04(); //increment loaded flag loadstate=5; break;
		 *
		 * case 5: //load some stuff MyEpicLife.loadstage05(); //increment
		 * loaded flag loadstate=6; break; case 6: //load some stuff
		 * MyEpicLife.loadstage06(); //increment loaded flag loadstate=7; break;
		 * case 7: //load some stuff MyEpicLife.loadstage07(); //increment
		 * loaded flag loadstate=8; break; case 8: //load some stuff
		 * MyEpicLife.loadstage08(); //increment loaded flag loadstate=9; break;
		 * case 9: //load some stuff MyEpicLife.loadstage09(); //increment
		 * loaded flag loadstate=10; break; case 10: //load some stuff
		 * MyEpicLife.loadstage10(); //increment loaded flag loadstate=11;
		 * break; }
		 */

	}

	@Override
	public void update(float delta) {

		cameraHorizAngle = cameraHorizAngle + (rotationspeed * delta);
		planetmove = planetmove + (delta * planetspeed);
		// System.out.println("planet x="+planetmove);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub


	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDone(boolean b) {
		// TODO Auto-generated method stub

	}

}