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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MyEpicLife;
import com.digitale.myepiclife.SoundManager;

/**
 * The main menu screen showing a background, the logo of the game and a label
 * telling the user to touch the screen to start the game. Waits for the touch
 * and returns isDone() == true when it's done so that the ochestrating
 * GdxInvaders class can switch to the next screen.
 * 
 */
public class HelpScreen extends MyEpicLifeScreen {
	String[] listEntries = { null, null, null, null, null, null, null, null,
			null, null };
	public static boolean fadeOut;
	public static boolean fadeIn;
	public static float fadeTimer;
	public static int i = 0;
	private Timer timer = new Timer();

	Stage stage;
	SpriteBatch batch;
	/** the cross fade texture **/
	private Texture xfadeTexture;
	Texture background;
	Actor root;
	private String ac;
	protected boolean doneflag = false;

	public HelpScreen(String newslines) {
		OrthographicCamera camera;
		batch = new SpriteBatch();
		ImageButton buttonDone = null;
		try {
			buttonDone = MyEpicLife.buttons.getButton("doneholo");
		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);
		DesktopTimer.setStage(stage);
		Label labelStories = new Label(newslines, MyEpicLife.uiSkin);
		labelStories.setWrap(true);
		ScrollPane scrollPane = new ScrollPane(labelStories, MyEpicLife.uiSkin);
		scrollPane.setFadeScrollBars(false);
		ScreenTable buttontable = new ScreenTable();
		Window window = new ScreenWindow("Help");
		
		buttontable.row().fill().expandX();
		buttontable.add(scrollPane).fill().expand().colspan(3);
		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add();
		buttontable.add();
		buttontable.add(buttonDone);


		window.row().fill().expandX();
		window.add(buttontable);

		buttontable.layout();
		window.pack();
		stage.addActor(window);

		buttonDone.addListener(new ChangeListener() {

			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("help Close pressed");
				// SoundManager.playuiclick();

				MyEpicLife.gameMode = 2;
				doneflag = true;

			}
		});

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		FrameLimit.sleep();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();

		// render_toast.trash_all();

	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		return doneflag;

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
