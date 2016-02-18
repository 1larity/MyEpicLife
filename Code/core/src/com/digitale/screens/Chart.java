package com.digitale.screens;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.digitale.database.CompletionDef;
import com.digitale.database.MELEvent;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class Chart {
	private String TAG = "CHART: ";
	public ShapeRenderer renderer;
	private int chartWidth = 256;
	private int chartHeight = 256;
	private SpriteBatch chartBatch;

	public Chart() {
	}

	/**
	 * render chart to texture
	 * 
	 * @param charttype
	 **/
	public TextureRegion chartTexture(MELEvent currentEvent, String charttype) {
		Slice[] streakSlices = {
				new Slice(
						currentEvent.getCompletionList().size() - currentEvent.getLongestStreak(),
						Color.BLACK), new Slice(currentEvent.getLongestStreak(), Color.CYAN) };
		Vector2 origin = new Vector2(chartWidth / 2, chartHeight / 2);

		Slice[] slices = { new Slice(currentEvent.getCompletedEventCount(), Color.GREEN),
				new Slice(currentEvent.getCancelledEventCount(), Color.RED) };
		int radius = Gdx.graphics.getWidth() / 8;

		TextureRegion bufferTexture;
		FrameBuffer chartBuffer = new FrameBuffer(Format.RGBA8888, chartWidth, chartHeight, false);
		OrthographicCamera cam = new OrthographicCamera(chartWidth, chartHeight);
		cam.setToOrtho(true, chartWidth, chartHeight);
		chartBatch = new SpriteBatch();
		chartBatch.enableBlending();
		cam.update();
		chartBatch.setProjectionMatrix(cam.combined);
		chartBuffer.begin();

		if (charttype.equals("piechart")) {
			drawPie(chartBatch, origin, radius, slices, currentEvent);
			radius = radius / 2;
			drawPie(chartBatch, origin, radius, streakSlices, currentEvent);
		} else if (charttype.equals("weeklybarchart")) {
			drawBarWeekly(chartBatch, currentEvent.getCompletionList());
		} else if (charttype.equals("dailybarchart")) {
			drawBarDaily(chartBatch, currentEvent.getCompletionList());
		}
		chartBuffer.end();
		bufferTexture = new TextureRegion(chartBuffer.getColorBufferTexture());
		return bufferTexture;
	}

	/** draws bar chart of completions per day **/
	private void drawBarDaily(SpriteBatch batch, ArrayList<CompletionDef> completions) {
		chartBatch.begin();
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		SimpleDateFormat dayFormat = new SimpleDateFormat("HH:MM dd/mm/yy");
		NinePatch barTexture = new NinePatch(MyEpicLife.uiSkin.getRegion("default-round-large"));
		NinePatchDrawable barPatch = new NinePatchDrawable(barTexture);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// major axes
		renderer.rect(chartWidth / 16, chartHeight / 16, chartWidth, 2); // x
																			// axis
		renderer.rect(chartWidth / 16, chartHeight / 16, 2, chartHeight); // yaxis
		// graduations
		BarList data = new BarList(createStats(completions, 1));
		System.out.println(TAG + "Barlist contains " + data.size() + " max is " + data.getMax());
		// calc origin X for each bar
		int barWidth = chartWidth / 20;
		renderer.setColor(Color.BLUE);

		String firstDate = "";
		// get long time for end of today
		Date today = new Date(MELCalendar.MELGetDate());
		int x = 20;

		// only draw graph if the max value is >0 (there is some data)
		if (data.getMax() >= 1) {
			for (Bar currentBar : data) {
				renderer.setColor(currentBar.getColor());
				renderer.rect(x * barWidth, (chartHeight / 16) + 2, barWidth, currentBar.getValue()
						* (chartHeight / data.getMax()));
				// draw graph with texture
				/*
				 * barPatch.draw(chartBatch,x * barWidth, (chartHeight / 16) +
				 * 2, barWidth, currentBar.getValue() (chartHeight /
				 * data.getMax()));
				 */
				System.out.println(TAG + "Bar " + x + " value " + currentBar.getValue());
				x--;
			}
		}

		renderer.end();
		chartBatch.end();
		chartBatch.begin();
		// chartBatch.draw(barTexture, 50, 50);
		MyEpicLife.globalFont.setColor(Color.WHITE);
		// legend x axis maximum
		MyEpicLife.microFont.draw(chartBatch, MELCalendar.MELshortDate(today).substring(0, 5),
				chartWidth - (chartWidth / 4.25f), chartHeight / 18);
		// legend x axis minimum
		Calendar cal=Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_YEAR,-17);
		Date twentyDaysAgo=cal.getTime();
		MyEpicLife.microFont.draw(chartBatch, MELCalendar.MELshortDate(twentyDaysAgo).substring(0, 5),
				chartWidth / 18f, chartHeight / 18);
		// y=0 datum
		MyEpicLife.microFont.draw(chartBatch, "0", 0, chartHeight / 9);
		// y= median value of data
		MyEpicLife.microFont.draw(chartBatch, "" + (data.getMax() / 2f), 0, (chartHeight / 2)
				+ chartHeight / 18);
		// y=maxvalue of dataset datum
		MyEpicLife.microFont.draw(chartBatch, "" + data.getMax(), 0, chartHeight);
		chartBatch.end();
	}

	/**
	 * draws bar chart of completions per week.!!! This is a load of bollox!!!, simply
	 * multiply daily function by 7 for weeks and month by 30
	 **/
	private void drawBarWeekly(SpriteBatch batch, ArrayList<CompletionDef> completions) {
		chartBatch.begin();
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		SimpleDateFormat dayFormat = new SimpleDateFormat("HH:MM dd/mm/yy");
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// major axes
		renderer.rect(chartWidth / 16, chartHeight / 16, chartWidth, 2); // x
																			// axis
		renderer.rect(chartWidth / 16, chartHeight / 16, 2, chartHeight); // yaxis
		// graduations
		// bars
		// calc origin X for each bar
		int barWidth = chartWidth / 20;
		renderer.setColor(Color.BLUE);
		String firstDate = "";
		// get long time for end of today
		Date today = new Date(MELCalendar.MELGetDate());
		Calendar l_calendar = new GregorianCalendar();
		long dayend = MELCalendar.MELlongTimeAndDateFromString("23:59 "
				+ MELCalendar.MELshortDate(today));
		l_calendar.setTime(new Date(dayend));
		Date weekEndTime = null, weekStartTime = null;
		for (int x = 19; x > -1; x -= 1) {
			if (x % 2 == 0) {
				renderer.setColor(Color.BLUE);
			} else {
				renderer.setColor(Color.CYAN);
			}
			weekEndTime = l_calendar.getTime();
			l_calendar.add(MELCalendar.DAY_OF_YEAR, -7);
			weekStartTime = l_calendar.getTime();

			int counter = 0;
			for (CompletionDef currentCompletion : completions) {
				// count how many completions in the last week
				if (currentCompletion.getCompletionStatus().equals("completed")) {

					if (MyEpicLife.DEBUG)
						System.out.println(TAG
								+ "Is completion "
								+ MELCalendar.MELshortTimeAndDateFromLong(currentCompletion
										.getCompletionDate()) + " between "
								+ MELCalendar.MELshortDate(weekStartTime) + " and "
								+ MELCalendar.MELshortDate(weekEndTime));
					Date completion = new Date(currentCompletion.getCompletionDate());
					if (between(completion, weekStartTime, weekEndTime)) {
						counter = counter + 1;
						firstDate = MELCalendar.MELshortDate(weekStartTime);
					}
				}
			}
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + counter + " completions for the  week " + (19 - x)
						+ " starting " + MELCalendar.MELshortDate(weekStartTime));
			renderer.rect(x * barWidth, (chartHeight / 16) + 2, (x * barWidth) + barWidth, counter
					* (chartHeight / 8));

		}

		renderer.end();
		chartBatch.end();
		chartBatch.begin();
		MyEpicLife.globalFont.setColor(Color.WHITE);
		// legend x axis minimum
		MyEpicLife.microFont.draw(chartBatch, firstDate, 0, chartHeight / 18);
		// legend x axis maximum
		MyEpicLife.microFont.draw(chartBatch, MELCalendar.MELshortDate(today), chartWidth
				- (chartWidth / 2), chartHeight / 18);
		chartBatch.end();
	}

	/**
	 * creates array of bar definitions containing colour and value per sample
	 * period measured in days
	 **/
	private BarList createStats(ArrayList<CompletionDef> completions, int samplePeriod) {
		BarList bars = new BarList();
		Bar currentBar = new Bar();
	

		// we want to draw 20 bars counting backwards (right to left)
		// so examine today and the previous 19 days.
		// create empty array of 20 bars and set colours
		for (int x = 18; x > -1; x -= 1) {
			// colour even bars blue, odd bars cyan
			if (x % 2 == 0) {
				currentBar.setColor(Color.BLUE);
			} else {
				currentBar.setColor(Color.CYAN);
			}
			bars.add(new Bar(currentBar));
		}
	
	//iterate through completions and increment each bar value by 1
		//if an event was completed on that bar's day
		for (CompletionDef currentCompletion : completions) {
			// if this completion status is "completed"
			if (currentCompletion.getCompletionStatus().equals("completed")) {
				//calculate number of days between today and the completion date
				long diff = MELCalendar.MELGetDate() - currentCompletion.getCompletionDate();
				int days = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
				System.out.println("Days: " + days);
				//ignore completions more than 20 days ago
				if (days < 19) {
					//use days as index to update bars
					bars.get(days).value = bars.get(days).value + 1;
				}
			}
		}
		return bars;
	}

	/** create string for pie chart titles **/
	private String chartTitle(MELEvent currentEvent) {
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		int completed = currentEvent.getCompletedEventCount();
		int total = currentEvent.getCompletionList().size();
		String sucessRate = myFormatter.format(((float) completed / (float) total) * 100);
		String chartTitle = "Sucess Rate " + sucessRate + "%.\nLongest streak "
				+ currentEvent.getLongestStreak();
		return chartTitle;
	}

	public static boolean between(Date date, Date dateStart, Date dateEnd) {
		if (date != null && dateStart != null && dateEnd != null) {
			if (date.after(dateStart) && date.before(dateEnd)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void PieChart() {

	}

	/** draw pie chart **/
	void drawPie(SpriteBatch batch, Vector2 origin, int radius, Slice[] slices,
			MELEvent currentEvent) {
		chartBatch.begin();
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.begin(ShapeType.Filled);
		double total = 0.0D;
		for (int i = 0; i < slices.length; i++) {
			total += slices[i].value;
		}
		double curValue = 0.0D;
		float startAngle = 0;
		for (int i = 0; i < slices.length; i++) {
			startAngle = (float) (curValue * 360 / total);
			float arcAngle = (float) (slices[i].value * 360 / total);
			renderer.setColor(slices[i].color);

			renderer.arc(origin.x, origin.y, radius, startAngle, arcAngle, 40);

			curValue += slices[i].value;
		}
		renderer.end();
		chartBatch.end();
		chartBatch.begin();
		MyEpicLife.globalFont.setColor(Color.WHITE);

		MyEpicLife.globalFont.draw(chartBatch, chartTitle(currentEvent), 0, 256);
		chartBatch.end();

	}

}