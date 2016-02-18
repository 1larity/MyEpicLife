package com.digitale.screens;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;
import com.digitale.myepiclife.MyEpicLife;

public class DatePickerDialog extends Dialog {
	private String TAG = "DATEPICKERDIALOG: ";
	String dayString="1";
	String monthString="1";
	String yearString="2015";
	Label dayPicked = new Label(dayString, MyEpicLife.uiSkin);
	Label monthPicked = new Label(dayString, MyEpicLife.uiSkin);	
	Label yearPicked = new Label(yearString, MyEpicLife.uiSkin);
	int screenX = Gdx.graphics.getWidth();
	int screenY = Gdx.graphics.getHeight();
	protected Date pickedTime;
	
	public DatePickerDialog(String title, String timeString) {
		super(title, MyEpicLife.uiSkin);
		initialize(timeString);
	}

	private void initialize(String timeString) {
	//	setHeight(screenY/3);
		
		System.out.println(TAG+"datestring "+timeString);
		padTop(50); // set padding on top of the dialog title
		Table dayList = new Table(MyEpicLife.uiSkin);
		Table monthList = new Table(MyEpicLife.uiSkin);
		Table yearList = new Table(MyEpicLife.uiSkin);
		Label timeDivider = new Label("/", MyEpicLife.uiSkin);
		Label timeDivider2 = new Label("/", MyEpicLife.uiSkin);
		dayString=(timeString.substring(0,2));
		System.out.println(TAG+"daystring "+timeString.substring(0,2));
		monthString=(timeString.substring(3,timeString.length()-3));
		System.out.println(TAG+"monthtring "+timeString.substring(3,timeString.length()-3));
		yearString=(timeString.substring(6,timeString.length()));
		System.out.println(TAG+"yearstring "+timeString.substring(6,timeString.length()));
		dayPicked.setText(dayString);
		monthPicked.setText(monthString);
		yearPicked.setText(yearString);
		setTime();
		populateDateList(dayList);
		populateMonthList(monthList);
		populateYearList(yearList);
		ScrollPane dayPane = new ScrollPane(dayList);
		ScrollPane monthPane = new ScrollPane(monthList);
		ScrollPane yearPane = new ScrollPane(yearList);
		dayPane.setVariableSizeKnobs(false);
		monthPane.setVariableSizeKnobs(false);
		yearPane.setVariableSizeKnobs(false);
		this.getContentTable().setHeight(this.getHeight()/1.5f);		
		this.getContentTable().add(dayPane);
		this.getContentTable().add(timeDivider);
		this.getContentTable().add(monthPane);
		this.getContentTable().add(timeDivider);
		this.getContentTable().add(yearPane);
		this.getContentTable().row();

		this.getContentTable().add(dayPicked);
		this.getContentTable().add(timeDivider);
		this.getContentTable().add(monthPicked);
		this.getContentTable().add(timeDivider2);
		this.getContentTable().add(yearPicked);

		if (MyEpicLife.DEBUG)this.debugAll();


	}

	private void populateDateList(Table tableEventlist) {
		for (int days = 1; days < 31; days++) {
			final TextButton buttonEvent = new TextButton(Integer.toString(
					days + 100).substring(1), MyEpicLife.uiSkin);
			buttonEvent.setColor(MyEpicLife.uiColour);
			buttonEvent.addListener(new ChangeListener() {

				public void changed(ChangeEvent event, Actor actor) {
					dayString=buttonEvent.getLabel().getText().toString();
					dayPicked.setText(dayString);
					setTime();
				}
			});

			tableEventlist.add(buttonEvent).width(screenX/7).height(screenY/6);
			tableEventlist.row();
		}
	}

	private void populateMonthList(Table tableEventlist) {
		for (int months = 1; months < 13; months++) {
			final TextButton buttonEvent = new TextButton(Integer.toString(
					months + 100).substring(1), MyEpicLife.uiSkin);
			buttonEvent.setColor(MyEpicLife.uiColour);
			buttonEvent.addListener(new ChangeListener() {

				public void changed(ChangeEvent event, Actor actor) {
					monthString=buttonEvent.getLabel().getText().toString();
					monthPicked.setText(monthString);
					setTime();
				}
			});
			tableEventlist.add(buttonEvent).width(screenX/7).height(screenY/6);
			tableEventlist.row();
		}
	}
	private void populateYearList(Table tableEventlist) {
		for (int years = 15; years < 20; years++) {
			final TextButton buttonEvent = new TextButton(Integer.toString(
					years + 100).substring(1), MyEpicLife.uiSkin);
			buttonEvent.setColor(MyEpicLife.uiColour);
			buttonEvent.addListener(new ChangeListener() {

				public void changed(ChangeEvent event, Actor actor) {
					yearString=buttonEvent.getLabel().getText().toString();
					yearPicked.setText(yearString);
					setTime();
				}
			});
			tableEventlist.add(buttonEvent).width(screenX/7).height(screenY/6);
			tableEventlist.row();
		}
	}
private void setTime(){
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
	try {
		pickedTime=formatter.parse(dayString+"/"+monthString+"/"+yearString);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	@Override
	public DatePickerDialog text(String text) {
		super.text(new Label(text, MyEpicLife.uiSkin));
		return this;
	}

	/**
	 * Adds a text button to the button table.
	 * 
	 * @param listener
	 *            the input listener that will be attached to the button.
	 */
	public DatePickerDialog button(String buttonText, InputListener listener) {
		TextButton button = new TextButton(buttonText, MyEpicLife.uiSkin);
		button.setColor(MyEpicLife.uiColour);
		button.addListener(listener);
		button(button);
		return this;
	}

	@Override
	public float getPrefWidth() {
		// force dialog width
		return screenX / 1.90f;
	}

	@Override
	public float getPrefHeight() {
		// force dialog height
		return screenY / 1.5f;
	}
	 @Override
		/** Adds the given button to the button table.
		 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
		public Dialog button (Button button, Object object) {
		 button.setColor(MyEpicLife.uiColour);
	    	this.getButtonTable().add(button).minWidth(screenX/4).minHeight(screenY/6);
	    
			setObject(button, object);
			return this;
		}
}