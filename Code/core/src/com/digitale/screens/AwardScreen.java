package com.digitale.screens;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.MELEvent;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MyEpicLife;

public class AwardScreen extends MyEpicLifeScreen {

	Stage stage;
	int screenX = Gdx.graphics.getWidth();
	int screenY = Gdx.graphics.getHeight();
	protected boolean doneflag = false;
	Calendar m_calendar = Calendar.getInstance();
	private Label timeLabel;
	private Label dateLabel;
	private Label infoLabel;
	private String TAG = "AWARDSCREEN: ";

	public AwardScreen() {

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		DesktopTimer.setStage(stage);
		ImageButton buttonHelp = null;
		ImageButton buttonDone = null;

		try {

			buttonHelp = MyEpicLife.buttons.getButton("helpholo");
			buttonDone = MyEpicLife.buttons.getButton("doneholo");

		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** set UI root table using custom table template **/
		ScreenTable buttontable = new ScreenTable();

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

		Table tableTimeDate = new Table();
		if (MyEpicLife.DEBUG)
			tableTimeDate.debugAll();
		tableTimeDate.row().fill().expandX().expandY();
		tableTimeDate.add(timeLabel).size(buttontable.getWidth() / 3, buttontable.getHeight() / 3)
				.align(Align.center);
		tableTimeDate.row().fill().expandX().expandY();
		;
		tableTimeDate.add(dateLabel).size(buttontable.getWidth() / 3, buttontable.getHeight() / 3)
				.align(Align.center);

		// info panel
		infoLabel = new Label("Null", MyEpicLife.uiSkin);
		infoLabel.setStyle(labelStyle);
		infoLabel.setFontScale((screenX / 7.25f) / 100f);

		/* list of events summaries for top right data panel * */
		Table tableEventlist = new Table(MyEpicLife.uiSkin);
		populateEventSummaryTable(tableEventlist);

		ScrollPane paneTimeDate = new ScrollPane(tableTimeDate);
		paneTimeDate.setVariableSizeKnobs(false);

		ScrollPane paneEventList = new ScrollPane(tableEventlist, MyEpicLife.uiSkin);
		paneEventList.setFlickScroll(false);

		SplitPane splitPane = new SplitPane(paneTimeDate, paneEventList, false, MyEpicLife.uiSkin,
				"default-horizontal");
		splitPane.setSplitAmount(.33444f);

		/** construct table **/
		// time/date row
		buttontable.row().fill().expandY();
		buttontable.add(splitPane).colspan(3);
		// row2
		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add();
		buttontable.add(buttonHelp);
		buttontable.add(buttonDone);

		buttontable.layout();
		tableTimeDate.layout();
		tableEventlist.layout();
		ScreenWindow window = new ScreenWindow("Awards");
		window.add(buttontable).fill().expandX().align(Align.bottom);
		window.pack();

		stage.addActor(window);

		buttonHelp.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 8;
				doneflag = true;
			}
		});

		buttonDone.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 2;
				doneflag = true;
			}
		});

	}

	private void populateEventSummaryTable(Table tableEventlist) {
		// Set up table defaults
		if (MyEpicLife.DEBUG)
			tableEventlist.debugAll();
		tableEventlist.row().fill().expandX().expandY();
		// if eventlist is empty, set event table entries to helpful text
		if (MyEpicLife.eventList.isEmpty()) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG
						+ "No missions available.\nUse the new mission button to create some.");
			TextArea textHelp = new TextArea

			("No missions available. Use the new mission button to create some.", MyEpicLife.uiSkin);
			textHelp.setDisabled(true);
			tableEventlist.row().fill().expandX().expandY();
			tableEventlist.add(textHelp);

		} else {
			// eventlist is not empty so populate summary
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Data found in eventlist");
			for (final MELEvent melEvent : MyEpicLife.eventList) {
				if (MyEpicLife.DEBUG) {
					System.out.println(TAG + "Building button " + melEvent.getEventName());
					System.out.println(TAG + "Building button " + melEvent.getFirstOccurance());
				}
				TextButton buttonEvent = new TextButton(melEvent.getEventName(), MyEpicLife.uiSkin);
				TextButton buttonEventTime = new TextButton(melEvent.getFirstOccuranceTime() + " "
						+ melEvent.getFirstOccuranceDate(), MyEpicLife.uiSkin);
				TextButtonStyle buttonStyle = new TextButtonStyle(
						MyEpicLife.uiSkin.get(TextButtonStyle.class));
				buttonStyle.up = MyEpicLife.uiSkin.newDrawable("default-rect");
				if (MyEpicLife.DEBUG)
					buttonEvent.debugAll();
				buttonEvent.setStyle(buttonStyle);
				buttonEvent.getLabel().setAlignment(Align.left);
				buttonEventTime.setStyle(buttonStyle);
				buttonEventTime.getLabel().setAlignment(Align.right);
				tableEventlist.row().fill().expandX().expandY();
				tableEventlist.add(buttonEvent);
				tableEventlist.add(buttonEventTime);

				buttonEvent.addListener(new ChangeListener() {

					public void changed(ChangeEvent event, Actor actor) {
						CustomDialog saveDialog = new CustomDialog("View Mission? "
								+ MyEpicLife.eventName) {

							protected void result(Object object) {
								if (MyEpicLife.DEBUG)System.out.println(TAG + "Chosen: " + object);
								if (("" + object).equals("true")) {
									if (MyEpicLife.DEBUG)System.out.println(TAG + "Launch View Event Details "
											+ melEvent.getEventName());
								}
							}
						};

						if (MyEpicLife.DEBUG)
							saveDialog.debugAll();
						saveDialog.text("Save new " + MyEpicLife.eventName);
						saveDialog.setSize(screenX / 3, screenY / 3);
						saveDialog.button("Yes", true);
						saveDialog.button("No", false).setSize(300, 100);
						saveDialog.key(Keys.ENTER, true);
						saveDialog.key(Keys.ESCAPE, false);
						saveDialog.getContentTable().layout();
						saveDialog.getButtonTable().layout();
						saveDialog.pack();
						saveDialog.show(stage);

					}

				});

				if (MyEpicLife.DEBUG)System.out.println(TAG + "Populating Event table with " + melEvent.getEventName());
			}

		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		m_calendar = Calendar.getInstance();

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
	public void dispose() {
		stage.dispose();

	}

	@Override
	public void pause() {
		// super.pause();
		if (MyEpicLife.DEBUG)System.out.println(TAG + "screen.paused called");
	}

	@Override
	public void setDone(boolean b) {
		// TODO Auto-generated method stub

	}
}