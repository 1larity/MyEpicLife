package com.digitale.screens;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.CompletionDef;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventSaver;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class EventDetailScreen extends MyEpicLifeScreen {

	public Stage stage;
	private int screenX = Gdx.graphics.getWidth();
	private int screenY = Gdx.graphics.getHeight();
	protected boolean doneflag = false;
	private MELEvent currentEvent = new MELEvent();
	private Label infoLabel;
	private String TAG = "EVENTDETAILSCREEN: ";
	private SpriteBatch batch = new SpriteBatch();
	private Chart piechart = new Chart();
	private TextField questionTextBox = new TextField("Report Question",
			MyEpicLife.uiSkin);
	private TextArea descriptionLabel = new TextArea("Description ",
			MyEpicLife.uiSkin);
	private TextArea textGraphBackground;
	private TableBuilder tablebuilder=new TableBuilder();
	
	public EventDetailScreen(long eventintent) {
		stage = new Stage();
		DesktopTimer.setStage(stage);
		Gdx.input.setInputProcessor(stage);
		//populate local event data using supplied id
		getEventData(eventintent);
		ImageButton buttonDoneEvent = null;
		ImageButton buttonHelp = null;
		ImageButton buttonDeleteEvent = null;
		ImageButton buttonEditEvent = null;
		TextButton buttonAddCompletionDate=new TextButton("Add completion date", MyEpicLife.uiSkin);
		TextButton buttonAddCompletion=new TextButton("Add completion now", MyEpicLife.uiSkin);
		buttonAddCompletion.setColor(MyEpicLife.uiColour);
		try {
			buttonDoneEvent = MyEpicLife.buttons.getButton("doneholo");
			buttonHelp = MyEpicLife.buttons.getButton("helpholo");
			buttonDeleteEvent = MyEpicLife.buttons.getButton("deleteholo");
			buttonEditEvent=MyEpicLife.buttons.getButton("editholo");

		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	


		/** set UI root table using custom table template **/
		ScreenTable buttontable = new ScreenTable();
		// ** title label for event catagory **//*
		Label catagoryLabel = new Label("Catagory: "
				+ currentEvent.getCatagoryName(), MyEpicLife.uiSkin);
		catagoryLabel.setWrap(true);
		catagoryLabel.setAlignment(Align.left);
		catagoryLabel.setColor(Color.CYAN);
		// ** title label for event name **//*
		Label eventTitleLabel = new Label(StringOperation.firstLetterToUpper(MyEpicLife.eventName) + ": "
				+ currentEvent.getEventName(), MyEpicLife.uiSkin);
		eventTitleLabel.setWrap(true);
		eventTitleLabel.setAlignment(Align.left);
		eventTitleLabel.setColor(Color.CYAN);

		LabelStyle labelStyle = new LabelStyle(MyEpicLife.titleFont, Color.CYAN);
		labelStyle.background = MyEpicLife.uiSkin.newDrawable("default-rect");

		/** Description Label **/
		Label eventTimeTitle = new Label(StringOperation.firstLetterToUpper(MyEpicLife.eventName)
				+ " next occurance "
				+ MELCalendar.MELshortTimeAndDateFromLong(currentEvent
						.getNextNotification()), MyEpicLife.uiSkin);
		eventTimeTitle.setWrap(true);
		eventTimeTitle.setColor(Color.CYAN);
		eventTimeTitle.setAlignment(Align.left);

		/* list of events summaries for top right data panel * */
		Table tableEventlist = new Table(MyEpicLife.uiSkin);
		tablebuilder.populateEventCompletionTable(tableEventlist,currentEvent);
		ScrollPane scrollPane2 = new ScrollPane(tableEventlist,
				MyEpicLife.uiSkin);
		scrollPane2.setVariableSizeKnobs(false);
		scrollPane2.setScrollingDisabled(true, false);

		/** description text area **/
		descriptionLabel.setColor(Color.CYAN);
		descriptionLabel.setAlignment(Align.topLeft);

		// info panel
		infoLabel = new Label("Null", MyEpicLife.uiSkin);
		infoLabel.setStyle(labelStyle);
		infoLabel.setFontScale((screenX / 7.25f) / 100f);

		/* list of events summaries for top right data panel * */
		final Table tableInfo = new Table(MyEpicLife.uiSkin);
		
		Image pieImage= new Image(piechart.chartTexture(currentEvent, "piechart"));
		Image barImage= new Image(piechart.chartTexture(currentEvent,"dailybarchart"));
		Table imageTable=new Table();
		imageTable.pad(5);
		imageTable.add(pieImage);
		imageTable.add(barImage);
		imageTable.add(pieImage);
		imageTable.layout();
		ScrollPane scrollPane1 = new ScrollPane(imageTable,
				MyEpicLife.uiSkin);
		scrollPane1.setScrollingDisabled(false, true);
		SplitPane splitPane = new SplitPane(scrollPane2, scrollPane1, false,
				MyEpicLife.uiSkin, "default-horizontal");
		splitPane.setSplitAmount(.666666f);
		/** construct info table **/
		if (MyEpicLife.DEBUG)
			tableInfo.debugAll();
		tableInfo.row().fill().expandX();
		// row1 event name/catagory labels
		tableInfo.add(catagoryLabel).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		tableInfo.add(eventTitleLabel).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		//construct event data table
		tableInfo.row().fill().expandX();
		tableInfo.add(eventTimeTitle).colspan(4);
		tableInfo.row().fill().expandX().expandY();
		tableInfo.add(splitPane).colspan(4);
		/** construct root window **/
		buttontable.row().fill().expandX().expandY();
		buttontable.add(tableInfo).fill().expandX().colspan(4);
		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add(buttonHelp);
		buttontable.add(buttonDeleteEvent);
		buttontable.add(buttonEditEvent);
		buttontable.add(buttonDoneEvent);
		buttontable.row().fill().expandX().height(stage.getHeight() /10);
		if(MyEpicLife.DEBUG)buttontable.add(buttonAddCompletionDate);
		if(currentEvent.getRepeat().getRepeatType().equals("log"))buttontable.add(buttonAddCompletion);
		buttontable.layout();
		tableInfo.layout();
		ScreenWindow window = new ScreenWindow(MyEpicLife.eventintent
				+ "Details.");

		window.row().fill().expandX();
		window.add(buttontable).fill().expandX().expandY().align(Align.bottom).colspan(2);
		window.pack();

		stage.addActor(window);
		
		
		buttonDeleteEvent.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				CustomDialog saveDialog = new CustomDialog(
						"Delete " + currentEvent.getEventName()) {

					protected void result(Object object) {
						System.out.println(TAG + "Chosen: " + object);
						if (("" + object).equals("true")) {
							// delete this event
							System.out.println(TAG + "Delete event pressed, UID:"
									+ currentEvent.getUid());

						MyEpicLife.eventList.deleteEvent(currentEvent.getUid());
						try {
							saveEventDb();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MyEpicLife.gameMode = 2;
						doneflag = true;
						}
					}
				};
				if(MyEpicLife.DEBUG)saveDialog.debugAll();
				saveDialog.text("Are you sure you wish to delete this "+MyEpicLife.eventName+"?");
				saveDialog.setSize(screenX / 3, screenY / 3);
				
				saveDialog.button("Yes", true).setSize(300, 300);
				saveDialog.button("No", false).setSize(300, 300);
				saveDialog.key(Keys.ENTER, true);
				saveDialog.key(Keys.ESCAPE, false);
				saveDialog.getContentTable().layout();
				saveDialog.getButtonTable().layout();
				saveDialog.pack();
				saveDialog.show(stage);
				
			
			}
		});
		buttonEditEvent.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				CustomDialog saveDialog = new CustomDialog(
						"Edit " + currentEvent.getEventName()) {

					protected void result(Object object) {
						System.out.println(TAG + "Chosen: " + object);
						if (("" + object).equals("true")) {
							// delete this event
							System.out.println(TAG + "Edit event pressed, UID:"
									+ currentEvent.getUid());

						//MyEpicLife.eventList.deleteEvent(currentEvent.getUid());
						MyEpicLife.gameMode = 2;
						doneflag = true;
						}
					}
				};
				if(MyEpicLife.DEBUG)saveDialog.debugAll();
				
				saveDialog.text("Are you sure you wish to edit this "+MyEpicLife.eventName+"?");
				saveDialog.setSize(screenX / 3, screenY / 3);
				
				saveDialog.button("Yes", true);
				saveDialog.button("No", false);
				saveDialog.key(Keys.ENTER, true);
				saveDialog.key(Keys.ESCAPE, false);
				saveDialog.getContentTable().layout();
				saveDialog.getButtonTable().layout();
				saveDialog.pack();
				saveDialog.show(stage);
				
			
			}
		});
		buttonDoneEvent.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 2;
				doneflag = true;
			}
		});
		
		buttonAddCompletionDate.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
			//	
				final DatePickerDialog datePickDialog = new DatePickerDialog(
						"Enter completion date", MELCalendar.MELshortDateFromLong(MELCalendar.MELGetDate())) {
					protected void result(Object object) {
						System.out.println(TAG + "time chosen: " + object);
					}
				};
				TextButton setTimeButton = new TextButton("Set date",
						MyEpicLife.uiSkin);
				datePickDialog.getButtonTable().add(setTimeButton).minWidth(screenX/4).minHeight(screenY/6);
				datePickDialog.button("Cancel", false);
				datePickDialog.key(Keys.ESCAPE, false);
				datePickDialog.getContentTable().layout();
				datePickDialog.getButtonTable().layout();
				datePickDialog.setMovable(false);
				datePickDialog.pack();
				datePickDialog.show(stage);

				setTimeButton.addListener(new ChangeListener() {
					public void changed(ChangeEvent event, Actor actor) {
						System.out.println(TAG + datePickDialog.dayString + "/"
								+ datePickDialog.monthString + "/"
								+ datePickDialog.yearString);
						//add completion this event's completion list
						long completionDate=MELCalendar.MELlongDateFromString(datePickDialog.dayString + "/"
								+ datePickDialog.monthString + "/"
								+ datePickDialog.yearString);
					
						MyEpicLife.eventList.addCompletion(currentEvent.getUid(),new CompletionDef(currentEvent.getCompletionList().size()+1,
								completionDate,"completed"));
						datePickDialog.hide();
					}
				});
			//	
			}
		}); 
		buttonAddCompletion.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {				
						MyEpicLife.eventList.addCompletion(currentEvent.getUid(),new CompletionDef(currentEvent.getCompletionList().size()+1,
								MELCalendar.MELGetDate(),"completed"));
			
			}
		}); 
		buttonHelp.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 8;
				doneflag = true;
			}
		});

	}

	private void getEventData(long eventintent) {
		for (MELEvent cursor : MyEpicLife.eventList) {
			if (cursor.getUid() == eventintent) {
				currentEvent = cursor;
			}

		}

	}

	public void saveEventDb() throws IOException {
		MELEventSaver EventSaver = new MELEventSaver();

		for (MELEvent i : MyEpicLife.eventList) {
			System.out.println(TAG + "Event List prior to save:"
					+ i.getEventName());
		}

		EventSaver.save("eventDb.json", MyEpicLife.eventList);

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
		System.out.println(TAG + "screen.paused called");
	}

	@Override
	public void setDone(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
