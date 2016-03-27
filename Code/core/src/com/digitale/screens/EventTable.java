package com.digitale.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.CompletionDef;
import com.digitale.database.MELEvent;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;
import com.digitale.utils.MELDebug;

public class EventTable extends Table{
	private static final int BAD = 1;
	private static final int GOOD = 0;
	private final boolean localDebug=false;
	private String TAG = "EVENTTABLE: ";
	int screenX = Gdx.graphics.getWidth();
	int screenY = Gdx.graphics.getHeight();
	public Stage stage;
	public EventTable(){
	}
	
	public EventTable(final Stage stage) {
		
			// Set up table defaults
			if (MyEpicLife.DEBUG)
				this.debugAll();
			this.row().fill().expandX().expandY();
			// if eventlist is empty, set event table entries to helpful text
			if (MyEpicLife.eventList.isEmpty()) {
				MELDebug.log(TAG
						+ "No missions available.\nUse the new mission button to create some.",localDebug);
				TextArea textHelp = new TextArea

				(
						"No missions available. Use the new mission button to create some.",
						MyEpicLife.uiSkin);
				textHelp.setDisabled(true);
				this.row().fill().expandX().expandY();
				this.add(textHelp);

			} else {
				// eventlist is not empty so populate summary
				MELDebug.log(TAG + "Data found in eventlist",localDebug);
				for (final MELEvent melEvent : MyEpicLife.eventList) {
					ImageButton buttonAddItem=null;
					MELDebug.log(TAG + "Building button "
							+ melEvent.getEventName()+"UID "+ melEvent.getUid(),localDebug);
					TextButton buttonEvent = new TextButton(
							melEvent.getEventName(), MyEpicLife.uiSkin);
					buttonEvent.setColor(MyEpicLife.uiColour);
					try {
						 buttonAddItem = MyEpicLife.buttons.getButton("additemholo");
					} catch (ButtonNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					buttonAddItem.getImageCell().height(30).width(30);
					
					TextButtonStyle buttonStyle = new TextButtonStyle(
							MyEpicLife.uiSkin.get(TextButtonStyle.class));
					//buttonStyle.up = MyEpicLife.uiSkin.newDrawable("default-rect");
					//add 2 labels to the event button to allow correct alignment of event text and time
					Table tableButton=buttonEvent.getLabelCell().getTable();
					//clear any exiting actors in the button
					tableButton.clearChildren();
					
					Label labelEventTitle=new Label(melEvent.getEventName(), MyEpicLife.uiSkin);
					Label labelEventTime=new Label(MELCalendar.MELshortTimeAndDateFromLong(melEvent.getNextNotification()), MyEpicLife.uiSkin);
					labelEventTime.setAlignment(Align.right);
					tableButton.row().fill().expandX().height(screenY/10);
					tableButton.pad(0);
					tableButton.padLeft(10).padRight(10);
					tableButton.add(labelEventTitle).align(Align.left);
				
					if (melEvent.getRepeat().getRepeatType().equals("log")){
					
					}else{
						tableButton.add(labelEventTime);
					}
			
				
					//add created button to list
					if (MyEpicLife.DEBUG)
						buttonEvent.debugAll();
					buttonEvent.setStyle(buttonStyle);
					this.row().fill();
					
					if (melEvent.getRepeat().getRepeatType().equals("log")){
						this.add(buttonEvent).expandX().fill();
						if(melEvent.getGoodBad()==BAD)buttonAddItem.setColor(Color.RED);
						if(melEvent.getGoodBad()==GOOD)buttonAddItem.setColor(Color.GREEN);
						this.add(buttonAddItem).width(screenX/5).align(Align.right);
					}else{
						this.add(buttonEvent).colspan(2);
					}
						buttonEvent.addListener(new ChangeListener() {

						public void changed(ChangeEvent event, Actor actor) {
							CustomDialog saveDialog = new CustomDialog(
									"View " + MyEpicLife.eventName+"?") {

								protected void result(Object object) {
									MELDebug.log(TAG + "Chosen: " + object,localDebug);
									if (("" + object).equals("true")) {
										MELDebug.log(TAG
												+ "Launch View Event Details "
												+ melEvent.getEventName()+" "+melEvent.getUid(),localDebug);
										MyEpicLife.eventintent=melEvent.getUid();
										MyEpicLife.gameMode = 10;
										MyEpicLife.screen.setDone(true);
									}
								}
							};
							if(MyEpicLife.DEBUG)saveDialog.debugAll();
							saveDialog.text("Show "+MyEpicLife.eventName+" details?");
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

					buttonAddItem.addListener(new ChangeListener() {
						
						public void changed(ChangeEvent event, Actor actor) {	
							CustomDialog consumptionDialog = new CustomDialog(
									"Update log for "+melEvent.getEventName()) {

								protected void result(Object object) {
									MELDebug.log(TAG + "Chosen: " + object,localDebug);
									if (("" + object).equals("true")) {
										MyEpicLife.eventList.addCompletion(melEvent.getUid(), new CompletionDef(melEvent.getCompletionList().size() + 1,
												MELCalendar.MELGetDate(), "completed"));
									}
								}
							};
							if(MyEpicLife.DEBUG)consumptionDialog.debugAll();
							consumptionDialog.text("Add a "+melEvent.getUnitType()+" to your total?");
							consumptionDialog.setSize(screenX / 3, screenY / 3);
							
							consumptionDialog.button("Yes", true).setSize(300, 300);
							consumptionDialog.button("No", false).setSize(300, 300);
							consumptionDialog.key(Keys.ENTER, true);
							consumptionDialog.key(Keys.ESCAPE, false);
							consumptionDialog.getContentTable().layout();
							consumptionDialog.getButtonTable().layout();
							consumptionDialog.pack();
							consumptionDialog.show(stage);

						
						}
					});

					MELDebug.log(TAG + "Populating Event table with "
						+ melEvent.getEventName(),localDebug);
				}

			}
			

	
	}
}