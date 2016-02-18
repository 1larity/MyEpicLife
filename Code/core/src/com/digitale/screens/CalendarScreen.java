package com.digitale.screens;

import java.util.ArrayList;
import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class CalendarScreen extends MyEpicLifeScreen {


	ArrayList<ImageTextButton> dayButtonList = new ArrayList<ImageTextButton>();
	Stage stage;
	/** icons **/
	Texture textureHelp;
	Texture textureHelpPressed;

	Texture textureDone;
	Texture textureDonePressed;

	Texture textureEventList;
	Texture textureEventListPressed;
	MELCalendar localCalendar;
	Label fpsLabel;
	int screenX=Gdx.graphics.getWidth();
	int screenY=Gdx.graphics.getHeight();
	Calendar m_calendar = Calendar.getInstance();
	private int m_month= m_calendar.get(Calendar.MONTH);
	private int m_year = m_calendar.get(Calendar.YEAR);
	protected boolean doneflag = false;

	public CalendarScreen() {
		localCalendar=new MELCalendar();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		DesktopTimer.setStage(stage);
		ImageButton buttonDone = null;
		ImageButton buttonViewEventsList=null;
		ImageButton buttonHelp=null;
		try {
		 buttonViewEventsList = MyEpicLife.buttons.getButton("eventholo");
		buttonDone = MyEpicLife.buttons.getButton("doneholo");
		buttonHelp = MyEpicLife.buttons.getButton("helpholo");
	} catch (ButtonNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		Label myLabel = new Label("this is some text.", MyEpicLife.uiSkin);
		myLabel.setWrap(true);

		/** set UI root table using custom table template **/
		ScreenTable buttontable = new ScreenTable();
;

		Table calendarTable = composeCalendar();
		if (MyEpicLife.DEBUG)
			calendarTable.debugAll();
		if (MyEpicLife.DEBUG)
			buttontable.debugAll();
		buttontable.row().fillX().expandX();
		buttontable.add(calendarTable).colspan(3);
		buttontable.row().fillX().expandX();
		buttontable.add(buttonHelp);
		buttontable.add(buttonViewEventsList);
		buttontable.add(buttonDone);

		
		calendarTable.layout();
		buttontable.layout();
		
		Window window=new ScreenWindow("Calendar");
		window.add(buttontable).fill().expandX().align(Align.bottom);
		window.pack();
		stage.addActor(window);



		buttonViewEventsList.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				new Dialog("Some Dialog", MyEpicLife.uiSkin, "dialog") {
					protected void result(Object object) {
						if (MyEpicLife.DEBUG)System.out.println("Chosen: " + object);
					}
				}.text("Are you enjoying this demo?").button("Yes", true)
						.button("No", false).key(Keys.ENTER, true)
						.key(Keys.ESCAPE, false).show(stage);
			}
		});


		buttonDone.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 2;
				doneflag = true;
			}
		});

		buttonHelp.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				new Dialog("Some Dialog", MyEpicLife.uiSkin, "dialog") {
					protected void result(Object object) {
						if (MyEpicLife.DEBUG)System.out.println("Chosen: " + object);
					}
				}.text("Are you enjoying this demo?").button("Yes", true)
						.button("No", false).key(Keys.ENTER, true)
						.key(Keys.ESCAPE, false).show(stage);
			}
		});

	}

	private Table composeCalendar() {
		/** compose calendar grid **/
		Table calendarTable = new Table();
		if (MyEpicLife.DEBUG)
			calendarTable.debugAll();
		calendarTable.align(Align.center);
		calendarTable.padTop(50);
		calendarTable.padBottom(0);
		calendarTable.setFillParent(true);
		localCalendar.monthList(m_month, m_year);
		// set style for calendar buttons
		ImageTextButtonStyle styleDay = new ImageTextButtonStyle();

		styleDay.font = MyEpicLife.uiSkin.getFont("default-font");
		styleDay.up = new TextureRegionDrawable(MyEpicLife.uiSkin.getRegion("default-round"));
		styleDay.down = new TextureRegionDrawable(
				MyEpicLife.uiSkin.getRegion("default-round-down"));

		for (int dayNumber = 0; dayNumber < localCalendar.m_dayList.size(); dayNumber++) {
			final String dayData=localCalendar.m_dayList.get(dayNumber).getDateString();
			String[] daydate = dayData.split("-");
			if (MyEpicLife.DEBUG)System.out.println("dayNumber is: " + dayNumber);
			dayButtonList.add(new ImageTextButton("" + daydate[0], styleDay));

			// reset imagetextbutton so you can add text and image in custom
			// arrangement
		

		}

		int column = 1;
		for (ImageTextButton itb : dayButtonList) {
			itb.pad(10);
			calendarTable.add(itb);//.size(Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/11).space(Gdx.graphics.getHeight()/100);
			final int dayNumber=dayButtonList.indexOf(itb)+1;
			
			itb.getLabel().setColor(localCalendar.m_dayList.get(dayNumber-1).getDayColour());
			final String dayData=localCalendar.m_dayList.get(dayNumber-1).getDateString();
			//add daybutton click listeners
			itb.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
					new Dialog("", MyEpicLife.uiSkin, "dialog") {
						
						protected void result(Object object) {
							
							if (MyEpicLife.DEBUG)System.out.println("Chosen: " + object);
						}
					}.text("See events for "+dayData+" ?").button("    Yes    ", true)
							.button("    No    ", false).key(Keys.ENTER, true)
							.key(Keys.ESCAPE, false).show(stage);
				}
			});
			
			column++;
			if (column == 8) {
				column = 1;
				if (dayNumber<39)calendarTable.row().fill();
			}

		}
		calendarTable.layout();
		return calendarTable;
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
	public void update(float delta) {
		// TODO Auto-generated method stub

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
	public void setDone(boolean b) {
		// TODO Auto-generated method stub
		
	}
}