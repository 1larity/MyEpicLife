package com.digitale.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.digitale.database.Filter;
import com.digitale.database.FilterList;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventSaver;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.EventTemplate;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class RepeatingEventScreen extends MyEpicLifeScreen {
	private String TAG = "REPEATINGEVENTSCREEN: ";
	Stage stage;
	private int screenX = Gdx.graphics.getWidth();
	private int screenY = Gdx.graphics.getHeight();
	protected boolean doneflag = false;
	private Label infoLabel;
	private Label labelStartDate = new Label("Date", MyEpicLife.uiSkin);
	private Label labelTimeRepeat = new Label("Time", MyEpicLife.uiSkin);
	private Label labelNextNotification = new Label("Next Due ...", MyEpicLife.uiSkin);
	private TextField questionTextBox = new TextField("Report Question",
			MyEpicLife.uiSkin);
	private TextArea descriptionLabel = new TextArea("Description ",
			MyEpicLife.uiSkin);
	private TextButton repeatTypeButton = new TextButton("Toggle Repeat Type",
			MyEpicLife.uiSkin, "toggle");
	Table tableWeekButton;
	Table tableDayRepeat;
	ScreenTable buttontable = new ScreenTable();
	//the unit of time the repeat uses, ie day, month, year
	private String repeatUnit;
	//how many repeat units between repeats
	private int repeatDuration;
	//type of repeat arbitrary, weekday (mon-tue &tc)
	private String repeatType;
	//repeat data
	private String repeat;
	private Label labelRepeatInformation= new Label("Repeat information ", MyEpicLife.uiSkin);;
	private boolean initialDataLoad=true;
	final MELCalendar melCalendar = new MELCalendar();
	
	public RepeatingEventScreen() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		DesktopTimer.setStage(stage);
		decodeRepeat();
		tableDayRepeat = setupDayRepeat();
		tableWeekButton = setupWeekButtons();
		
		if (repeatType.equals("arbitary")) {
			System.out.println(TAG+"setting repeat type button");
			repeatTypeButton.setChecked(true);
		} 
	
		ImageButton buttonDoneRepeat = null;
		ImageButton buttonHelp = null;
		ImageButton buttonCancelRepeat = null;
		try {
			buttonDoneRepeat = MyEpicLife.buttons.getButton("doneholo");
			buttonHelp = MyEpicLife.buttons.getButton("helpholo");
			buttonCancelRepeat = MyEpicLife.buttons.getButton("cancelholo");

		} catch (ButtonNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** set UI root table using custom table template **/

		// ** title label for event catagory **//*
		Label dateLabel = new Label("Start date", MyEpicLife.uiSkin);
		dateLabel.setWrap(true);
		dateLabel.setAlignment(Align.left);
		dateLabel.setColor(Color.CYAN);
		// ** title label for event name **//*
		Label labelTimeTitle = new Label("Notification time", MyEpicLife.uiSkin);
		labelTimeTitle.setWrap(true);
		labelTimeTitle.setAlignment(Align.left);
		labelTimeTitle.setColor(Color.CYAN);

		LabelStyle labelStyle = new LabelStyle(MyEpicLife.globalFont,
				Color.WHITE);
		labelStyle.background = MyEpicLife.uiSkin.newDrawable("selection");
		//set up repeat information label
		labelRepeatInformation.setWrap(true);
		labelRepeatInformation.setColor(Color.CYAN);
		labelRepeatInformation.setAlignment(Align.left);
		// **  label for next notification **//*
		labelNextNotification.setWrap(true);
		labelNextNotification.setAlignment(Align.left);
		labelNextNotification.setColor(Color.CYAN);
		/** set up time and date text fields **/
		//if no alarm time has been set by template, set alarm time to now
		if (MyEpicLife.DEBUG)System.out.println(TAG +"Alarmtime of this event is "+MyEpicLife.workingEvent.getAlarmTime() );
		if(MyEpicLife.workingEvent.getAlarmTime()==0){
			
			labelTimeRepeat.setText(melCalendar.MELgetShortNowTime());
		}else{
			//set alarm time label to default stored in template
			labelTimeRepeat.setText(MELCalendar.MELshortTime(new Date(MyEpicLife.workingEvent.getAlarmTime())));
		}
	
		labelStartDate.setText(MyEpicLife.workingEvent.getFirstOccuranceDate());
		labelStartDate.setStyle(labelStyle);
		labelTimeRepeat.setStyle(labelStyle);
		/** description text area **/
		descriptionLabel.setColor(Color.CYAN);
		descriptionLabel.setAlignment(Align.topLeft);

		// info panel
		infoLabel = new Label("Null", MyEpicLife.uiSkin);
		infoLabel.setStyle(labelStyle);
		infoLabel.setFontScale((screenX / 7.25f) / 100f);
		updateEventRepeat();
		/* list of events summaries for top  data panel * */
		final Table tableInfo = new Table(MyEpicLife.uiSkin);

		/** construct info table **/
		// row startdate and alarm time labels
		if (MyEpicLife.DEBUG)
			tableInfo.debugAll();
		tableInfo.row().fill().expandX();
		tableInfo.add(dateLabel).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		tableInfo.add(labelTimeTitle).align(Align.left)
				.width((screenX - (int) (screenY * (4 / 100.0f))) / 2);
		// row startdate and alarm buttons
		tableInfo.row().fill().expandX().height(screenY / 6);
		tableInfo.add(labelStartDate);
		tableInfo.add(labelTimeRepeat);
		// row3 repeat parameters label
		tableInfo.row().fill().expandX();
		tableInfo.add(labelRepeatInformation).colspan(3);
		// row4 the report question text
		tableInfo.row().fill().expandX();
		tableInfo.add(labelNextNotification).colspan(3);
		tableInfo.row().fill().expandX();
		tableInfo.add(repeatTypeButton).colspan(3);
		// repeat perameters
		tableInfo.row().fill().expandX().expandY();
		//add appropriate repeat params table
		if (repeatType.equals("arbitary")) {
			tableInfo.add(tableDayRepeat).colspan(3);
		}else{
			tableInfo.add(tableWeekButton).colspan(3);
		}
		/** construct root window **/
		buttontable.row().fill().expandX().expandY();
		buttontable.add(tableInfo).fill().expandX().colspan(3);

		buttontable.row().fill().expandX().height(stage.getHeight() / 5);
		buttontable.add(buttonHelp);
		buttontable.add(buttonCancelRepeat);
		buttontable.add(buttonDoneRepeat);

		buttontable.layout();
		tableInfo.layout();
		ScreenWindow window = new ScreenWindow("Set " + MyEpicLife.eventName
				+ " repeat");

		window.row().fill().expandX();
		window.add(buttontable).fill().expandX().expandY().align(Align.bottom);
		window.pack();

		stage.addActor(window);

		labelStartDate.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
			}
		});
		// set the repeat time
		labelTimeRepeat.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				String inputTime = melCalendar.MELgetShortNowTime();
				if (!labelTimeRepeat.getText().equals(
						melCalendar.MELgetShortNowTime())) {
					inputTime = "" + labelTimeRepeat.getText();
				}
				final TimePickerDialog timePickDialog = new TimePickerDialog(
						"Pick repeat time", inputTime) {
					protected void result(Object object) {
						System.out.println(TAG + "time chosen: " + object);
					}
				};
				TextButton setTimeButton = new TextButton("Set time",
						MyEpicLife.uiSkin);
				setTimeButton.setColor(MyEpicLife.uiColour);
				timePickDialog.getButtonTable().add(setTimeButton).minWidth(screenX/4).minHeight(screenY/6);
				timePickDialog.button("Cancel", false);
				timePickDialog.key(Keys.ESCAPE, false);
				timePickDialog.getContentTable().layout();
				timePickDialog.getButtonTable().layout();
				timePickDialog.setMovable(false);
				timePickDialog.pack();
				timePickDialog.show(stage);

				setTimeButton.addListener(new ChangeListener() {
					public void changed(ChangeEvent event, Actor actor) {
						String userAlarmString=timePickDialog.hourString
								+ ":" + timePickDialog.minuteString;
						long epocTime=MELCalendar.MELTimeAsLong(userAlarmString);
						if (MyEpicLife.DEBUG)System.out.println(TAG +"User picked"+ userAlarmString+" long representation "+ epocTime);
						labelTimeRepeat.setText(userAlarmString);
						
						updateEventRepeat();
						timePickDialog.hide();
					}
				});
			}
		});
		// set the start date
		labelStartDate.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				//set input date for date picker to today
				String inputTime = melCalendar.MELgetShortNowDate();
				//if the start date label is not set to today
				if (!labelStartDate.getText().equals(
						melCalendar.MELgetShortNowDate())) {
					//set input date for date picker to date specified by label
					inputTime = "" + labelStartDate.getText();
				}
				final DatePickerDialog datePickDialog = new DatePickerDialog(
						"Pick repeat time", inputTime) {
					protected void result(Object object) {
						System.out.println(TAG + "time chosen: " + object);
					}
				};
				TextButton setTimeButton = new TextButton("Set date",
						MyEpicLife.uiSkin);
				setTimeButton.setColor(MyEpicLife.uiColour);
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
						//set start date label to picked date
						labelStartDate.setText(datePickDialog.dayString + "/"
								+ datePickDialog.monthString + "/"
								+ datePickDialog.yearString);
						updateEventRepeat();
						datePickDialog.hide();
					}
				});
			}
		});

		buttonCancelRepeat.addListener(new ChangeListener() {
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

		buttonDoneRepeat.addListener(new ChangeListener() {

			public void changed(ChangeEvent event, Actor actor) {
				CustomDialog saveDialog = new CustomDialog("Save "
						+ MyEpicLife.eventName) {

					protected void result(Object object) {
						System.out.println(TAG + "Chosen: " + object);
						if (("" + object).equals("true")) {
						
							//TODO save repeat settings to working event

							System.out.println(TAG + "New Selected Event:"
									+ MyEpicLife.workingEvent.getCatagoryName()
									+ "@"
									+ MyEpicLife.workingEvent.getEventName());
							updateEventRepeat();
					
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
				if (MyEpicLife.DEBUG)saveDialog.debugAll();
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
//button to switch lower panel between repeat types
		repeatTypeButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				SnapshotArray<Actor> children = tableInfo.getChildren();
				children.ordered = false;

				if (repeatTypeButton.isChecked()) {
					// weekButtonTable.remove();
					tableInfo.removeActor(children.get(children.size - 1));
					tableInfo.add(tableDayRepeat).colspan(3);
					//update repeat info label to reflect change
					setArbitraryInfoLabel(repeatUnit, repeatDuration);
				} else {
					tableInfo.removeActor(children.get(children.size - 1));
					tableInfo.add(tableWeekButton).colspan(3);
				}
				updateEventRepeat();
			}
		});

	}

	
	private void updateEventRepeat() {
		//set repeat type by checking state of repeat type button
		String repeatType;
		if (repeatTypeButton.isChecked()){
			repeatType="arbitary@";
			 MyEpicLife.workingEvent.setFrequency(repeatType+
					 repeatDuration+ "-"+repeatUnit);
			 
		}else {
			repeatType="Weekly@";
			MyEpicLife.workingEvent.setFrequency(repeatType+repeat);
		}
		String timeString=""+labelTimeRepeat.getText();
		String firstOccurancString=""+labelStartDate.getText();
		MyEpicLife.workingEvent.setAlarmTime(MELCalendar.MELTimeAsLong(timeString));
		MyEpicLife.workingEvent.setFirstOccurance(MELCalendar.MELlongDateFromString(firstOccurancString));
		MyEpicLife.workingEvent.calculateNextNotification();
	}
	
	
	private void decodeRepeat() {
		 repeatUnit = null;
		 repeatDuration = 0;
		// retrieve repeat setting for this template
		// first letter A=arbitary period, W=weekly, ie every monday
		// repeat for arbitaries is:-
		// N-day (or month, year where n is the number of days
		// repeat for Weeklies is 
		//2 letter codes representing the days the occurance is on  (mo,tu,we,th,fr,sa,su)
		
		String[] row = MyEpicLife.workingEvent.getFrequency().split("@");
		repeatType = row[0];
		if (MyEpicLife.DEBUG)
			System.out.println(TAG+"Decoded "+repeatType+" repeat.");
		 repeat = row[1];
		if (MyEpicLife.DEBUG)
			System.out.println(TAG+"Repeat detail data "+repeat);
		if (repeat.contains("-")) {
			String[] abitaryRepeat = repeat.split("-");
			repeatDuration = Integer.valueOf(abitaryRepeat[0]);
			repeatUnit = abitaryRepeat[1];
			if (MyEpicLife.DEBUG)
				System.out.println(TAG+"Repeat every "+repeatDuration+":"+repeatUnit);
		}
		
		
	}

	/** set up buttons for "every Monday" style repeats **/
	private Table setupWeekButtons() {
		
		Table weekDayButtonTable = new Table();
		if (MyEpicLife.DEBUG)
			weekDayButtonTable.debugAll();
		Table toprowTable = new Table();
		Table bottomrowTable = new Table();
		if (MyEpicLife.DEBUG)
			toprowTable.debugAll();
		if (MyEpicLife.DEBUG)
			bottomrowTable.debugAll();
		final TextButton mondayToggleButton = new TextButton("Monday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton tuesdayToggleButton = new TextButton("Tuesday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton wednesdayToggleButton = new TextButton("Wednesday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton thursdayToggleButton = new TextButton("Thursday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton fridayToggleButton = new TextButton("Friday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton saturdayToggleButton = new TextButton("Saturday",
				MyEpicLife.uiSkin, "toggle");
		final TextButton sundayToggleButton = new TextButton("Sunday",
				MyEpicLife.uiSkin, "toggle");

		toprowTable.row().expandX().fill();
		toprowTable.add(mondayToggleButton).width(toprowTable.getWidth() / 3);
		toprowTable.add(tuesdayToggleButton).width(toprowTable.getWidth() / 3);
		toprowTable.add(wednesdayToggleButton)
				.width(toprowTable.getWidth() / 3);

		bottomrowTable.row().expandX().fill();
		bottomrowTable.add(thursdayToggleButton).width(
				bottomrowTable.getWidth() / 4);
		bottomrowTable.add(fridayToggleButton).width(
				bottomrowTable.getWidth() / 4);
		bottomrowTable.add(saturdayToggleButton).width(
				bottomrowTable.getWidth() / 4);
		bottomrowTable.add(sundayToggleButton).width(
				bottomrowTable.getWidth() / 4);

		weekDayButtonTable.row().expandX().fill();
		weekDayButtonTable.add(toprowTable);
		weekDayButtonTable.row().expandX().fill();
		weekDayButtonTable.add(bottomrowTable);
		EventListener daybuttonListener=
		new ChangeListener() {

			public void changed(ChangeEvent event, Actor actor) {
				//update repeat information to reflect buttons
				if (!initialDataLoad) concatinateRepeatFromButtons();
				labelRepeatInformation.setText("Repeat every "+MyEpicLife.workingEvent.concatinateRequiredDays());
			}
			private void concatinateRepeatFromButtons() {
				repeat="";
				if (mondayToggleButton.isChecked())repeat=repeat+"mo";
				if (tuesdayToggleButton.isChecked())repeat=repeat+"tu";
				if (wednesdayToggleButton.isChecked())repeat=repeat+"we";
				if (thursdayToggleButton.isChecked())repeat=repeat+"th";
				if (fridayToggleButton.isChecked())repeat=repeat+"fr";
				if (saturdayToggleButton.isChecked())repeat=repeat+"sa";
				if (sundayToggleButton.isChecked())repeat=repeat+"su";
				MyEpicLife.workingEvent.setFrequency("Weekly@"+repeat);
				if (MyEpicLife.DEBUG) System.out.println(TAG + "repeat data updated: "
						+ repeat);
				//MyEpicLife.workingEvent.setFirstOccurance(firstOccurance);
				updateEventRepeat();
			}
		};
		
		//listeners for weekday buttons
		mondayToggleButton.addListener(daybuttonListener);
		tuesdayToggleButton.addListener(daybuttonListener);
		wednesdayToggleButton.addListener(daybuttonListener);
		thursdayToggleButton.addListener(daybuttonListener);
		fridayToggleButton.addListener(daybuttonListener);
		saturdayToggleButton.addListener(daybuttonListener);
		sundayToggleButton.addListener(daybuttonListener);
		/** set state of day buttons if this is a "weekly" event **/
		if (repeatType.equals("Weekly")) {

			labelRepeatInformation.setText("Repeat every ");
			if (repeat.contains("mo")){
				mondayToggleButton.setChecked(true);

			}
			if (repeat.contains("tu")){
				tuesdayToggleButton.setChecked(true);

			}
			if (repeat.contains("we")){
				wednesdayToggleButton.setChecked(true);

			}
			if (repeat.contains("th")){
				thursdayToggleButton.setChecked(true);

			}
			if (repeat.contains("fr")){
				fridayToggleButton.setChecked(true);

			}
			if (repeat.contains("sa"))	{
				saturdayToggleButton.setChecked(true);

			}
			if (repeat.contains("su")){
				sundayToggleButton.setChecked(true);

			}
			
			labelRepeatInformation.setText("Repeat every "+MyEpicLife.workingEvent.concatinateRequiredDays());
		}
		initialDataLoad=false;
		return weekDayButtonTable;
	}

	/** set up table for day based repeats **/
	private Table setupDayRepeat() {
		Table tableWeekDayRepeat = new Table();
		// dropdown for unit size ie week/year
		final SelectBox<String> dropdownRepeatUnits = new SelectBox<String>(MyEpicLife.uiSkin);
		dropdownRepeatUnits.setItems("day", "week", "month", "year");

		// textbox for unit count
		final TextField textFieldUnitCount = new TextField("1",
				MyEpicLife.uiSkin);
		textFieldUnitCount
				.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		textFieldUnitCount.setMaxLength(3);

		//if appropriate type of repeat set units and duration
		if (repeatType.equals("arbitary")) {
			dropdownRepeatUnits.setSelected(repeatUnit);
			textFieldUnitCount.setText("" + repeatDuration);
		} else {
			dropdownRepeatUnits.setSelected("day");
		}

		if (MyEpicLife.DEBUG)
			tableWeekDayRepeat.debugAll();
		// compose table
		tableWeekDayRepeat.row().expandX();
		tableWeekDayRepeat.add(textFieldUnitCount);
		tableWeekDayRepeat.add(dropdownRepeatUnits);
		tableWeekDayRepeat.row().expandX();
		// set repeat information label with settings from inputs for arbitrary repeat
		setArbitraryInfoLabel(repeatUnit, repeatDuration);
		
		//listener for arbitrary unit count to update info
		textFieldUnitCount.setTextFieldListener(new TextFieldListener() {
			public void keyTyped(TextField textField, char key) {
				if (MyEpicLife.DEBUG) System.out.println(TAG + "key pressed: "+key);
				//make sure something is in the textfield
				if (textField.getText().length()>0 && textField.getText().length()<4){
					textField.getOnscreenKeyboard().show(false);
				repeatDuration=Integer.valueOf(textField.getText());
			
				setArbitraryInfoLabel(repeatUnit, repeatDuration);
				//if input is too short set to minimum
			}else if(textField.getText().length()<1){
				textField.setText("1");
			}
				updateEventRepeat();
			}
		});

		dropdownRepeatUnits.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				repeatUnit=dropdownRepeatUnits.getSelected();
				setArbitraryInfoLabel(repeatUnit, repeatDuration);
				updateEventRepeat();
			}
		});

		return tableWeekDayRepeat;
	}
	// set repeat information label with settings from inputs for arbitrary repeat
	private void setArbitraryInfoLabel(String unitType,	int unitCount) {
		if (MyEpicLife.DEBUG) System.out.println(TAG + "repeat info updated: "
				+ unitCount+" "+unitType);
		if (unitCount > 0) {
			if (unitCount < 2) {
				labelRepeatInformation.setText("Repeat every "
						+ unitType + ".");
			} else {
				labelRepeatInformation.setText("Repeat every "
						+ unitCount + " "
						+ unitType + "s.");
			}
		}
	}

	public void saveEventDb() throws IOException {
		MELEventSaver EventSaver = new MELEventSaver();
		//update next alarmtime
		
		MyEpicLife.workingEvent.setUid(MELCalendar.MELGetDate());
		
		MyEpicLife.eventList.add(MyEpicLife.workingEvent);

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
		labelNextNotification.setText("Next Due "+MELCalendar.MELshortTimeAndDateFromLong(MyEpicLife.workingEvent.getNextNotification()));
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
