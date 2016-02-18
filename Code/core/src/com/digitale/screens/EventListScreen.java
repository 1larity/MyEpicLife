package com.digitale.screens;

	import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.MELEvent;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;


	public class EventListScreen extends MyEpicLifeScreen {
		private String TAG = "EVENTLISTSCREEN: ";
		protected boolean doneflag=false;
		Stage stage;
		private EventTable tableEventlist;
		int screenX = Gdx.graphics.getWidth();
		int screenY = Gdx.graphics.getHeight();
		MELCalendar melClaendar=new MELCalendar();
		public EventListScreen() {
			
			// stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, new PolygonSpriteBatch());
			stage = new Stage();
			Gdx.input.setInputProcessor(stage);
			DesktopTimer.setStage(stage);
			/**icons **/
			ImageButton buttonDone = null;
			ImageButton buttonHelp = null;
			ScreenTable buttontable = new ScreenTable();
			try {
				buttonDone = MyEpicLife.buttons.getButton("doneholo");
				buttonHelp = MyEpicLife.buttons.getButton("helpholo");
			} catch (ButtonNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tableEventlist = new EventTable(stage);

			ScrollPane scrollPane2 = new ScrollPane(tableEventlist,
					MyEpicLife.uiSkin);
			scrollPane2.setVariableSizeKnobs(false);


			Window window=new ScreenWindow("Pending Events");
			buttontable.row().fill().expandX().expandY();
			buttontable.add(scrollPane2).colspan(4);
			buttontable.row().fill().expandX();
			buttontable.add();
			buttontable.add();
			buttontable.add(buttonHelp);
			buttontable.add(buttonDone);
			buttontable.layout();
			
			window.add(buttontable).fill().expandX().expandY().align(Align.bottom);
			window.pack();

			stage.addActor(window);



			
			buttonDone.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					MyEpicLife.gameMode=2;
					doneflag=true;
				}
			});
			
			buttonHelp.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
					MyEpicLife.gameMode = 8;
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
				System.out
						.println(TAG
								+ "No missions available.\nUse the new mission button to create some.");
				TextArea textHelp = new TextArea

				(
						"No missions available. Use the new mission button to create some.",
						MyEpicLife.uiSkin);
				textHelp.setDisabled(true);
				tableEventlist.row().fill().expandX().expandY();
				tableEventlist.add(textHelp);

			} else {
				// eventlist is not empty so populate summary
				System.out.println(TAG + "Data found in eventlist");
				for (final MELEvent melEvent : MyEpicLife.eventList) {
					System.out.println(TAG + "Building button "
							+ melEvent.getEventName());
					System.out.println(TAG + "Building button "
							+ melEvent.getFirstOccurance());
					TextButton buttonEvent = new TextButton(
							melEvent.getEventName(), MyEpicLife.uiSkin);
					TextButton buttonEventTime = new TextButton(
							melClaendar.MELshortTimeFromLong(melEvent.getAlarmTime()) + " "
									+ melEvent.getFirstOccuranceDate(),
							MyEpicLife.uiSkin);
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
							CustomDialog saveDialog = new CustomDialog(
									"View " + MyEpicLife.eventName+"?") {

								protected void result(Object object) {
									System.out.println(TAG + "Chosen: " + object);
									if (("" + object).equals("true")) {
										System.out.println(TAG
												+ "Launch View Event Details "
												+ melEvent.getEventName());
									}
								}
							};
							if(MyEpicLife.DEBUG)saveDialog.debugAll();
							saveDialog.text("not implemented yet ");
							saveDialog.setSize(screenX / 3, screenY / 3);
							
							saveDialog.button("..Yes..", true).setSize(300, 300);
							saveDialog.button("..No..", false).setSize(300, 300);
							saveDialog.key(Keys.ENTER, true);
							saveDialog.key(Keys.ESCAPE, false);
							saveDialog.getContentTable().layout();
							saveDialog.getButtonTable().layout();
							saveDialog.pack();
							saveDialog.show(stage);

						}

					});

					System.out.println(TAG + "Populating Event table with "
							+ melEvent.getEventName());
				}

			}

		}
		@Override
		public void render (float delta) {
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


			stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
			stage.draw();
			FrameLimit.sleep();
		}

		@Override
		public void resize (int width, int height) {
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
		public void dispose () {
			stage.dispose();
			
		}
		@Override
		public void setDone(boolean flag) {
			doneflag=flag;
			
		}
}