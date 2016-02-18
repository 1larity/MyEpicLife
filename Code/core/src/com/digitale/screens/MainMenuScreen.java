package com.digitale.screens;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.Item;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventList;
import com.digitale.database.MELEventLoader;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.EventTemplate;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class MainMenuScreen extends MyEpicLifeScreen {

	private String TAG = "MAINSCREEN: ";
	Stage stage = new Stage();
	int screenX = Gdx.graphics.getWidth();
	int screenY = Gdx.graphics.getHeight();
	protected boolean doneflag = false;
	Calendar m_calendar = Calendar.getInstance();
	MELCalendar melCacendar = new MELCalendar();
	private Label timeLabel;
	private Label dateLabel;
	private Label infoLabel;
	private EventTable tableEventlist;
	private ScrollPane scrollPane2;
	private ScreenTable buttontable;
	private Table timeDatePanel;
	private ScreenWindow window;
	Label fpsLabel;

	public MainMenuScreen() {
		MELEventLoader MELLoader = new MELEventLoader();
		MyEpicLife.eventList = (MELEventList) MELLoader.load("eventDb.json");
		MyEpicLife.eventList.notificationSort();

		DesktopTimer.setStage(stage);
		Gdx.input.setInputProcessor(stage);
		ImageButton buttonViewCalendar = null;
		ImageButton buttonNewEventWizard = null;
		ImageButton buttonViewEventsList = null;
		ImageButton buttonOptions = null;
		ImageButton buttonViewAchievement = null;
		ImageButton buttonHelp = null;

		try {
			buttonViewCalendar = MyEpicLife.buttons.getButton("calendarholo");
			buttonNewEventWizard = MyEpicLife.buttons.getButton("neweventholo");
			buttonViewEventsList = MyEpicLife.buttons.getButton("eventholo");
			buttonOptions = MyEpicLife.buttons.getButton("optionsholo");
			buttonViewAchievement = MyEpicLife.buttons.getButton("achieholo");
			buttonHelp = MyEpicLife.buttons.getButton("helpholo");

		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** set UI root table using custom table template **/
		buttontable = new ScreenTable();
		fpsLabel = new Label("fps:", MyEpicLife.uiSkin);
		LabelStyle labelStyle = new LabelStyle(MyEpicLife.titleFont, Color.CYAN);
		labelStyle.background = MyEpicLife.uiSkin.newDrawable("default-rect");
		timeLabel = new Label("Null", MyEpicLife.uiSkin);
		timeLabel.setStyle(labelStyle);
		timeLabel.setFontScale((screenX / 7.25f) / 100f);
		timeLabel.setAlignment(Align.center);
		// timeLabel.

		dateLabel = new Label("Null", MyEpicLife.uiSkin);
		dateLabel.setFontScale((screenX / 9f) / 100f);
		dateLabel.setStyle(labelStyle);
		dateLabel.setAlignment(Align.center);
		
		Label awardPointsLabel=new Label("Award points "+MyEpicLife.eventList.getAwardPoints(),  MyEpicLife.uiSkin);
		Label ExpPointsLabel=new Label("Exp points "+MyEpicLife.eventList.getExpPoints(),  MyEpicLife.uiSkin);
		timeDatePanel = new Table();
		if (MyEpicLife.DEBUG)
			timeDatePanel.debugAll();
		timeDatePanel.row().fill().expandX().expandY();
		timeDatePanel.add(timeLabel).size(buttontable.getWidth() / 3, buttontable.getHeight() / 3)
				.align(Align.center);
		timeDatePanel.row().fill().expandX().expandY();
		;
		timeDatePanel.add(dateLabel).size(buttontable.getWidth() / 3, buttontable.getHeight() / 3)
				.align(Align.center);
		timeDatePanel.row().fill().expandX();
		timeDatePanel.add(awardPointsLabel);
		timeDatePanel.row().fill().expandX();
		timeDatePanel.add(ExpPointsLabel);
		
		// info panel
		infoLabel = new Label("Null", MyEpicLife.uiSkin);
		infoLabel.setStyle(labelStyle);
		infoLabel.setFontScale((screenX / 7.25f) / 100f);

		/* list of events summaries for top right data panel * */
		tableEventlist = new EventTable(stage);
		tableEventlist.layout();

		ScrollPane scrollPane = new ScrollPane(timeDatePanel);
		scrollPane.setVariableSizeKnobs(false);
		scrollPane2 = new ScrollPane(tableEventlist, MyEpicLife.uiSkin);
		scrollPane2.setVariableSizeKnobs(false);
		scrollPane2.setScrollingDisabled(true, false);
		SplitPane splitPane = new SplitPane(scrollPane, scrollPane2, false, MyEpicLife.uiSkin,
				"default-horizontal");
		splitPane.setSplitAmount(.33444f);
		/** construct table **/
		// time/date row
		buttontable.row().fill().expandY();
		buttontable.add(splitPane).colspan(3);
		// row1
		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add(buttonViewCalendar);
		buttontable.add(buttonViewAchievement);
		buttontable.add(buttonViewEventsList);
		// row2
		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add(buttonNewEventWizard);
		buttontable.add(buttonOptions);
		buttontable.add(buttonHelp);
		if (MyEpicLife.DEBUG == true) {
			buttontable.row().fill().expandX().height(stage.getHeight() / 15);
			buttontable.add(fpsLabel).colspan(2);
		}
		buttontable.layout();
		timeDatePanel.layout();
		window = new ScreenWindow("Main Menu");
		window.add(buttontable).fill().expandX().expandY().align(Align.bottom);
		window.pack();

		stage.addActor(window);

		buttonViewCalendar.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 3;
				doneflag = true;
			}
		});

		buttonViewAchievement.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 4;
				doneflag = true;
			}
		});

		buttonViewEventsList.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 5;
				doneflag = true;
			}
		});

		buttonNewEventWizard.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 6;
				doneflag = true;
			}
		});

		buttonOptions.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 7;
				doneflag = true;
			}
		});

		buttonHelp.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 8;
				doneflag = true;
			}
		});

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		m_calendar = Calendar.getInstance();
		// if event list data is out of date reload mainscreen with new data
		if (MyEpicLife.desktopTimer.dataInvalidated == true) {

			MyEpicLife.desktopTimer.dataInvalidated = false;
			MyEpicLife.gameMode = 2;
			doneflag = true;

		}
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			System.out.println("back key pressed");
			this.pause();
			// change screen to MainMenu
		}
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		timeLabel.setText(""
				+ Integer.toString(m_calendar.get(Calendar.HOUR_OF_DAY) + 100).substring(1) + ":"
				+ Integer.toString(m_calendar.get(Calendar.MINUTE) + 100).substring(1) + ":"
				+ Integer.toString(m_calendar.get(Calendar.SECOND) + 100).substring(1));
		dateLabel.setText(""
				+ Integer.toString(m_calendar.get(Calendar.DAY_OF_MONTH) + 100).substring(1) + ":"
				+ Integer.toString(m_calendar.get(Calendar.MONTH) + 100).substring(1) + ":"
				+ Integer.toString(m_calendar.get(Calendar.YEAR) + 10000).substring(1));
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		FrameLimit.sleep();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		return doneflag;

	}

	@Override
	public void setDone(boolean flag) {
		doneflag = flag;

	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {
		super.pause();
		if (MyEpicLife.DEBUG)
			System.out.println(TAG + "Screen.paused called");
	}

}