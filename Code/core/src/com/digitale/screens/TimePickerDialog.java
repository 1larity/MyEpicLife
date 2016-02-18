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

public class TimePickerDialog extends Dialog {
	private String TAG = "TIMEPICKERDIALOG: ";
	String hourString="23";
	String minuteString="30";
	Label hourPicked = new Label(hourString, MyEpicLife.uiSkin);
	Label minutePicked = new Label(minuteString, MyEpicLife.uiSkin);
	int screenX = Gdx.graphics.getWidth();
	int screenY = Gdx.graphics.getHeight();
	protected Date pickedTime;
	
	public TimePickerDialog(String title, String timeString) {
		super(title, MyEpicLife.uiSkin);
		initialize(timeString);
	}

	private void initialize(String timeString) {
	//	setHeight(screenY/3);
		
		System.out.println(TAG+"timestring"+timeString);
		padTop(50); // set padding on top of the dialog title
		Table hourList = new Table(MyEpicLife.uiSkin);
		Table minuteList = new Table(MyEpicLife.uiSkin);
		Label timeDivider = new Label(":", MyEpicLife.uiSkin);
		minuteString=(timeString.substring(3,timeString.length()));
		hourString=(timeString.substring(0,2));
		hourPicked.setText(hourString);
		minutePicked.setText(minuteString);
		setTime();
		populateHourList(hourList);
		populateMinuteList(minuteList);
		ScrollPane hourPane = new ScrollPane(hourList);
		ScrollPane minutePane = new ScrollPane(minuteList);
		hourPane.setVariableSizeKnobs(false);
		minutePane.setVariableSizeKnobs(false);
		this.getContentTable().setHeight(this.getHeight()/1.5f);
		this.getContentTable().add(hourPane);
		this.getContentTable().add(timeDivider);
		this.getContentTable().add(minutePane);
		this.getContentTable().row();
		this.getContentTable().add(hourPicked);
		this.getContentTable().add(timeDivider);
		this.getContentTable().add(minutePicked);
		if (MyEpicLife.DEBUG)this.debugAll();


	}

	private void populateHourList(Table tableEventlist) {
		for (int hours = 0; hours < 24; hours++) {
			final TextButton buttonEvent = new TextButton(Integer.toString(
					hours + 100).substring(1), MyEpicLife.uiSkin);
			
			buttonEvent.addListener(new ChangeListener() {

				public void changed(ChangeEvent event, Actor actor) {
					hourString=buttonEvent.getLabel().getText().toString();
					hourPicked.setText(hourString);
					setTime();
				}
			});

			tableEventlist.add(buttonEvent).width(screenX/7).height(screenY/6);
			tableEventlist.row();
		}
	}

	private void populateMinuteList(Table tableEventlist) {
		for (int minutes = 0; minutes < 60; minutes++) {
			final TextButton buttonEvent = new TextButton(Integer.toString(
					minutes + 100).substring(1), MyEpicLife.uiSkin);
			buttonEvent.addListener(new ChangeListener() {

				public void changed(ChangeEvent event, Actor actor) {
					minuteString=buttonEvent.getLabel().getText().toString();
					minutePicked.setText(minuteString);
					setTime();
				}
			});
			tableEventlist.add(buttonEvent).width(screenX/7).height(screenY/6);
			tableEventlist.row();
		}
	}
private void setTime(){
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
	try {
		pickedTime=formatter.parse(hourString+":"+minuteString);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	@Override
	public TimePickerDialog text(String text) {
		super.text(new Label(text, MyEpicLife.uiSkin));
		return this;
	}

	/**
	 * Adds a text button to the button table.
	 * 
	 * @param listener
	 *            the input listener that will be attached to the button.
	 */
	public TimePickerDialog button(String buttonText, InputListener listener) {
		TextButton button = new TextButton(buttonText, MyEpicLife.uiSkin);
		button.addListener(listener);
		button(button);
		return this;
	}

	@Override
	public float getPrefWidth() {
		// force dialog width
		return screenX / 2f;
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
	    	this.getButtonTable().add(button).minWidth(Gdx.graphics.getWidth()/4).minHeight(Gdx.graphics.getHeight()/6);
	    
			setObject(button, object);
			return this;
		}
}