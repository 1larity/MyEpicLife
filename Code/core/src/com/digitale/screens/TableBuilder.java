package com.digitale.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.digitale.database.CompletionDef;
import com.digitale.database.MELEvent;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

/**Buids various tables for UI**/
public class TableBuilder {
	private String TAG = "TABLEBUILDER: ";
	public TableBuilder() {
		// TODO Auto-generated constructor stub
	}
	/** build a table to display the completions of the supplied event**/
	public void  populateEventCompletionTable(Table tableEventlist,MELEvent currentEvent) {
		// Set up table defaults
		if (MyEpicLife.DEBUG)
			tableEventlist.debugAll();
		tableEventlist.row().fill().expandX().expandY();
		// if eventlist is empty, set event table entries to helpful text
		if (currentEvent.getCompletionList().isEmpty()) {
			if (MyEpicLife.DEBUG)System.out.println(TAG + "No missions completed");
			TextArea textHelp = new TextArea
			("No missions completed.", MyEpicLife.uiSkin);
			textHelp.setDisabled(true);
			tableEventlist.row().fill().expandX().expandY();
			tableEventlist.add(textHelp);

		} else {
			// eventlist is not empty so populate summary
			if (MyEpicLife.DEBUG)System.out.println(TAG + "Data found in eventlist");
			for (final CompletionDef currentCompletion : currentEvent
					.getCompletionList()) {
				if (MyEpicLife.DEBUG)System.out.println(TAG + "Building button "
						+ currentCompletion.getCompletionStatus());
				TextButton buttonEvent = new TextButton(
						currentCompletion.getCompletionStatus()
								+ "   "
								+ MELCalendar.MELshortTimeAndDateFromLong(currentCompletion
										.getCompletionDate()),
						MyEpicLife.uiSkin);
				buttonEvent.setDisabled(true);
				TextButtonStyle buttonStyle = new TextButtonStyle(
						MyEpicLife.uiSkin.get(TextButtonStyle.class));
				buttonStyle.up = MyEpicLife.uiSkin.newDrawable("default-rect");
				if (MyEpicLife.DEBUG)
					buttonEvent.debugAll();
				buttonEvent.setStyle(buttonStyle);
				buttonEvent.getLabel().setAlignment(Align.left);
				tableEventlist.row().fill().expandX().expandY();
				tableEventlist.add(buttonEvent);
				if (MyEpicLife.DEBUG)System.out.println(TAG + "Populating event completion table with "
						+ currentCompletion.getCompletionStatus());
			}

		}


	}
}
