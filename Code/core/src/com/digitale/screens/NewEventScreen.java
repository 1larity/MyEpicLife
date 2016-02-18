package com.digitale.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.Filter;
import com.digitale.database.FilterList;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventSaver;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.EventTemplate;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class NewEventScreen extends MyEpicLifeScreen {

	Stage stage;
	private int screenX = Gdx.graphics.getWidth();
	private int screenY = Gdx.graphics.getHeight();
	protected boolean doneflag = false;
	private ArrayList<EventTemplate> globalfilteredCatagoryList = new ArrayList<EventTemplate>();
	private ArrayList<EventTemplate> globalfilteredEventList = new ArrayList<EventTemplate>();
	private Label infoLabel;
	private String TAG = "NEWEVENTSCREEN: ";
	private SelectBox<String> catagoryDropdown = new SelectBox<String>(MyEpicLife.uiSkin);
	private SelectBox<String> eventDropdown = new SelectBox<String>(MyEpicLife.uiSkin);
	private TextField questionTextBox = new TextField("Report Question", MyEpicLife.uiSkin);
	private TextArea descriptionLabel = new TextArea("Description ", MyEpicLife.uiSkin);

	public NewEventScreen() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		DesktopTimer.setStage(stage);
		ImageButton buttonDoneEvent = null;
		ImageButton buttonHelp = null;
		ImageButton buttonCancelNewEvent = null;

		try {
			buttonDoneEvent = MyEpicLife.buttons.getButton("doneholo");
			buttonHelp = MyEpicLife.buttons.getButton("helpholo");
			buttonCancelNewEvent = MyEpicLife.buttons.getButton("cancelholo");

		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** set UI root table using custom table template **/
		ScreenTable buttontable = new ScreenTable();
		// ** title label for event catagory **//*
		Label catagoryLabel = new Label("Catagory", MyEpicLife.uiSkin);
		catagoryLabel.setWrap(true);
		catagoryLabel.setAlignment(Align.left);
		catagoryLabel.setColor(Color.CYAN);
		// ** title label for event name **//*
		Label eventTitleLabel = new Label(MyEpicLife.eventName, MyEpicLife.uiSkin);
		eventTitleLabel.setWrap(true);
		eventTitleLabel.setAlignment(Align.left);
		eventTitleLabel.setColor(Color.CYAN);

		LabelStyle labelStyle = new LabelStyle(MyEpicLife.titleFont, Color.CYAN);
		labelStyle.background = MyEpicLife.uiSkin.newDrawable("default-rect");

		/** Description Label **/
		Label reportDescriptionTitle = new Label(MyEpicLife.eventName + " Description ",
				MyEpicLife.uiSkin);
		reportDescriptionTitle.setWrap(true);
		reportDescriptionTitle.setColor(Color.CYAN);
		reportDescriptionTitle.setAlignment(Align.left);

		/** description text area **/
		descriptionLabel.setColor(Color.CYAN);
		descriptionLabel.setAlignment(Align.topLeft);
		// setup catagory dropdown
		catagoryDropdown.setItems(populateCatagories());
		catagoryDropdown.setSelected("Health and Fitness");
		catagoryDropdown.getScrollPane().setVariableSizeKnobs(false);
		catagoryDropdown.getScrollPane().setScrollbarsOnTop(true);
		// setup eventdropdown
		eventDropdown.getScrollPane().setVariableSizeKnobs(false);
		eventDropdown.setItems(populateEventNames(catagoryDropdown.getSelected()));
		eventDropdown.getScrollPane().setWidth(eventDropdown.getWidth());

		// info panel
		infoLabel = new Label("Null", MyEpicLife.uiSkin);
		infoLabel.setStyle(labelStyle);
		infoLabel.setFontScale((screenX / 7.25f) / 100f);

		/* list of events summaries for top right data panel * */
		final Table tableInfo = new Table(MyEpicLife.uiSkin);

		/** construct info table **/
		if (MyEpicLife.DEBUG)
			tableInfo.debugAll();
		tableInfo.row().fill().expandX();
		// row1 dropdown labels
		tableInfo.add(catagoryLabel).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		tableInfo.add(eventTitleLabel).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		// row1
		tableInfo.row().fill().expandX();
		tableInfo.add(catagoryDropdown);
		// window.add(buttonEditCatagory).size(32,32);
		tableInfo.add(eventDropdown);
		// window.add(buttonEditName).size(32,32);
		// row3 report question title label
		tableInfo.row().fill().expandX();
		tableInfo.add(reportDescriptionTitle).colspan(3);
		// row4 the report question text
		tableInfo.row().fill().expandX().expandY();
		tableInfo.add(descriptionLabel).colspan(3);
		// window.add(buttonEditQuestion).size(32,32);
		/** construct root window **/
		buttontable.row().fill().expandX().expandY();
		buttontable.add(tableInfo).fill().expandX().colspan(3);

		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add(buttonHelp);
		buttontable.add(buttonCancelNewEvent);
		buttontable.add(buttonDoneEvent);

		buttontable.layout();
		tableInfo.layout();
		ScreenWindow window = new ScreenWindow("Create New " + MyEpicLife.eventName);

		window.row().fill().expandX();
		window.add(buttontable).fill().expandX().expandY().align(Align.bottom);
		window.pack();
		populateEventUI();
		stage.addActor(window);

		catagoryDropdown.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				populateEventUI();
				// the catagory has changed so we need to rebuild Events list
				eventDropdown.setItems(populateEventNames(catagoryDropdown.getSelected()));
				tableInfo.layout();
			}
		});
		eventDropdown.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				populateEventUI();
				tableInfo.layout();
				// the event has changed so we need to rebuild Events details
			}
		});

		buttonCancelNewEvent.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 2;
				doneflag = true;
			}
		});

		buttonHelp.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				MyEpicLife.gameMode = 8;
				doneflag = true;
			}
		});

		buttonDoneEvent.addListener(new ChangeListener() {

			public void changed(ChangeEvent event, Actor actor) {
				CustomDialog saveDialog = new CustomDialog("Create " + MyEpicLife.eventName) {

					protected void result(Object object) {
						if (MyEpicLife.DEBUG)System.out.println(TAG + "Chosen: " + object);
						if (("" + object).equals("true")) {
							// String
							// selectedCatagoryName=globalfilteredCatagoryList.get(catagoryDropdown.getSelectedIndex()).getName();
							String selectedEventName = globalfilteredEventList.get(
									eventDropdown.getSelectedIndex()).getName();
							if (MyEpicLife.DEBUG)System.out.println(TAG + "Selected Event " + selectedEventName);
							MyEpicLife.workingEvent = new MELEvent(selectedEventName);

							if (MyEpicLife.DEBUG)System.out.println(TAG + "New Selected Event:"
									+ MyEpicLife.workingEvent.getCatagoryName() + "@"
									+ MyEpicLife.workingEvent.getEventName());
							// if this is a logging event save and return to
							// main
							if (MyEpicLife.workingEvent.getRepeat().getRepeatType().equals("log")) {
								try {
									saveEventDb();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								MyEpicLife.gameMode = 2;
								doneflag = true;
								// else this is a repeating event and we need to
								// show repeat setup screen
							} else {
								MyEpicLife.gameMode = 9;
								doneflag = true;
							}
						}
					}
				};
				if (MyEpicLife.DEBUG)
					saveDialog.debugAll();
				saveDialog.text("Save new " + MyEpicLife.eventName);
				saveDialog.setSize(screenX / 3, screenY / 3);
				saveDialog.button("Yes", true);
				saveDialog.button("No", false).setSize(100, 100);
				saveDialog.key(Keys.ENTER, true);
				saveDialog.key(Keys.ESCAPE, false);
				saveDialog.getContentTable().layout();
				saveDialog.getButtonTable().layout();
				saveDialog.pack();
				saveDialog.show(stage);

			}

		});

	}

	public void saveEventDb() throws IOException {
		MELEventSaver EventSaver = new MELEventSaver();
MyEpicLife.workingEvent.setUid(MELCalendar.MELGetDate());
		
		MyEpicLife.eventList.add(MyEpicLife.workingEvent);
		for (MELEvent i : MyEpicLife.eventList) {
			if (MyEpicLife.DEBUG)System.out.println(TAG + "Event List prior to save:" + i.getEventName());
		}

		EventSaver.save("eventDb.json", MyEpicLife.eventList);

	}

	/**
	 * populate categories list dropdown array from event templates
	 * 
	 * @return
	 **/
	private String[] populateCatagories() {

		/** make local copy of master event templates list **/
		ArrayList<EventTemplate> eventTemplateList = new ArrayList<EventTemplate>(
				MyEpicLife.eventTemplateList);
		/** dropdown string array workspace **/
		ArrayList<String> catagoryList = new ArrayList<String>();
		String currentCatagory;

		/** iterate through event template list **/
		for (Iterator<EventTemplate> i = eventTemplateList.iterator(); i.hasNext();) {
			currentCatagory = i.next().getCategory();
			if (MyEpicLife.DEBUG)System.out.println(TAG + "current catagory: " + currentCatagory);

			/** if category is not already in the list, add it **/
			if (!catagoryList.contains(currentCatagory)) {
				catagoryList.add(currentCatagory);
				globalfilteredCatagoryList.add(i.next());
			}

		}
		for (int k = 0; k < catagoryList.size(); k++) {
			if (MyEpicLife.DEBUG)System.out.println(TAG + "new catagory list added: " + catagoryList.get(k) + " gfc "
					+ globalfilteredCatagoryList.get(k).getCategory());
		}
		String[] stringArray = catagoryList.toArray(new String[catagoryList.size()]);
		return stringArray;

	}

	/**
	 * populated event dropdown with only events appropriate for the current
	 * catagory
	 * 
	 * @return
	 **/
	private String[] populateEventNames(String currentCatagory) {
		/** clear global filtered event list **/
		globalfilteredEventList.clear();
		/** make local copy of master event templates list **/
		ArrayList<EventTemplate> eventTemplateList = new ArrayList<EventTemplate>(
				MyEpicLife.eventTemplateList);
		/** create filter logic **/
		Filter<EventTemplate, String> filter = new Filter<EventTemplate, String>() {
			public boolean isMatched(EventTemplate object, String text) {
				return object.getCategory().startsWith(String.valueOf(text));
			}
		};

		/**
		 * Pass filter to filter the original list using search term derived
		 * from categories dropdown
		 **/
		ArrayList<EventTemplate> filteredEventList = (ArrayList<EventTemplate>) new FilterList<Object>()
				.filterList(eventTemplateList, filter, "" + currentCatagory);

		/** copy event title in this catagory to string array **/
		ArrayList<String> eventTitles = new ArrayList<String>();
		for (EventTemplate et : filteredEventList) {
			eventTitles.add(et.getName());
		}
		/** add event names in this catagory to dropdown **/

		/**
		 * copy local filtered event list to class wide event list for future
		 * interface updates
		 **/
		globalfilteredEventList.addAll(filteredEventList);
		// populateEventUI();
		String[] stringArray = eventTitles.toArray(new String[eventTitles.size()]);
		return stringArray;
	}

	/** update UI due to event template changes **/
	private void populateEventUI() {

		/** only set question label if global filtered events is not empty **/
		if (!globalfilteredEventList.isEmpty()) {
			/** replace all occurances of D% in question text with todays date **/
			Calendar m_calendar = Calendar.getInstance();
			String parsedQuestion = globalfilteredEventList.get(eventDropdown.getSelectedIndex())
					.getReportQuestion();
			questionTextBox.setText(parsedQuestion.replaceAll("%D",
					MELCalendar.MELshortDate(m_calendar.getTime())));
			String repeatText = globalfilteredEventList.get(eventDropdown.getSelectedIndex())
					.concatinateRequiredDays();

			String description = globalfilteredEventList.get(eventDropdown.getSelectedIndex())
					.getDescription();
			String alarmText = MELCalendar.MELshortTime(new Date(globalfilteredEventList.get(
					eventDropdown.getSelectedIndex()).getDefaultAlarmTime()));
			descriptionLabel.setText(description + "\nRepeats every " + repeatText + ", at "
					+ alarmText);

		}
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
		if (MyEpicLife.DEBUG)System.out.println(TAG + "screen.paused called");
	}

	@Override
	public void setDone(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
