package com.digitale.myepiclife;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.digitale.connex.News;
import com.digitale.database.AssetDef;
import com.digitale.database.AssetsData;
import com.digitale.database.AwardDef;
import com.digitale.database.AwardsData;
import com.digitale.database.MELEvent;
import com.digitale.database.EventTemplateData;
import com.digitale.database.MELEventList;
import com.digitale.database.MELEventLoader;
import com.digitale.database.TestDb;
import com.digitale.screens.AwardScreen;
import com.digitale.screens.CalendarScreen;
import com.digitale.screens.EventDetailScreen;
import com.digitale.screens.EventListScreen;
import com.digitale.screens.HelpScreen;
import com.digitale.screens.MELButton;
import com.digitale.screens.MainMenuScreen;
import com.digitale.screens.MyEpicLifeScreen;
import com.digitale.screens.NewEventScreen;
import com.digitale.screens.OptionsScreen;
import com.digitale.screens.RepeatingEventScreen;
import com.digitale.screens.Splash;
import com.digitale.utils.MELDebug;

public class MyEpicLife implements ApplicationListener, InputProcessor {

	public static boolean selectScreen;
	public static DesktopTimer desktopTimer;
	/**Start of variables **/
	/**Debug flags**/
	private static String TAG="MAIN: ";
	// debug for spammy outputs
	public static  boolean DEEPDEBUG = false;
	// system debug flag
	public static  boolean DEBUG = true;
	SpriteBatch batch;
	SoundManager soundManager;
	Texture img;
	// **Version number**//*
	public static String version = "0.1.29.06.15";
	static String helptext = "\nVersion:"
			+ version
			+ "\n\n"
			+ "There is no help text yet:-\n"
			+ "I really need to put some time into writing all the help screens.\n"
			+ "Which would be here if I wasnt so lazy.\n"
			+ ".\n"
			+ ".\n"
			+ ".\n"
			+ ".\n"
			+ ".\n"
			+ "\nCredits:\n Code and Graphics:Richard Beech\nMusic:Kevin MacLeod http://incompetech.com/\n"
			+ "Sound Effects:Stefan Persson  http://soundtrack.imphenzia.com\n\n"
			+ "Special thanks to Testers: .";

	// **data operations manager for assets**//
	public static AssetsData assetsData = new AssetsData();
	// Tint all buttons with this colour
	public static Color uiColour=Color.CYAN;
	// **data operations manager for event templates**//
	public static EventTemplateData eventTemplateData = new EventTemplateData();
	//**data operations manager for event templates**//
//	public static ArrayList<MELEventInterface> eventListData;
	// **container for app news**//
	private static String mnewslines;
	// **list of last few news items **//
	public static News news[] = new News[5];
	// ** the current mode the game is in **//
	public static int gameMode = 1;
	// **Audio volume controls**//
	public static float sfxVolume = 0.5f;
	public static float musicVolume = 0.5f;
	//**Use tiny font flag**/
	public static boolean tinyfont;
	public static Skin uiSkin;
	/**Asset manager for loading and disposing of app assets**/
	public static AssetManager manager = new AssetManager();
	/**Screen Object**/
	public static  MyEpicLifeScreen screen;
	/**Flag indicating whether we were initialised already **/
	private static boolean isInitialized = false;
	/**List of assets to be loaded**/
	public static List<AssetDef> assetList = new ArrayList<AssetDef>();
	/**List of event templates**/
	public static List<EventTemplate> eventTemplateList = new ArrayList<EventTemplate>();
	/**List of events**/
	public static MELEventList eventList=new MELEventList();
	/**list of award defs**/
	public static List<AwardDef> awardList=new ArrayList<AwardDef>();
	public static AwardsData awardsData = new AwardsData();
	/**Accelerometer settings**/
	public static float accelXsensitivity = 1;
	public static float accelYsensitivity = 1;
	public static float deadzoneX = .5f;
	public static float deadzoneY = .5f;
	/** error flags and constants**/
	public static final int ALL_OK = 0;
	public static int Error = ALL_OK;
	public static final int NETWORK_DOWN = 1;
	public static final int SERVER_DOWN = 2;
	public static final int TEST_ERROR = 3;
	/**End of variables **/
	public static Model shipMesh;
	public static boolean mloginOK;
	public static BitmapFont globalFont;
	public static BitmapFont titleFont;
	public static BitmapFont microFont;
	//synonym for event, for ex. "quest","activity,"mission","event"
	public static String eventName= "event";
	TestDb testDB=new TestDb();
	//MELEvent workspace for building new events
	public static MELEvent workingEvent;
	public static MELButton buttons;
	private boolean localDebug=false;

	// Define an interface for various callbacks to the android launcher
    public interface MyGameCallback {
        public void onStartActivityA();
        public void onStartActivityB();
        public void onStartSomeActivity(int someParameter, String someOtherParameter);
    }

    // Local variable to hold the callback implementation
    private static MyGameCallback myGameCallback;
    //event identifier for passing user selected events around
	public static long eventintent;
	public static int selectedFPS=30;
	
	

    // ** Additional **
    // Setter for the callback
    public void setMyGameCallback(MyGameCallback callback) {
        myGameCallback = callback;
    }

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);

		workingEvent=new MELEvent();
		if(MyEpicLife.DEBUG){
		String externalFiles=Gdx.files.getExternalStoragePath();
		System.out.println("External files:-"+externalFiles);
		String localFiles=Gdx.files.getLocalStoragePath();
		System.out.println("Local files:-"+localFiles);
		}
		if (!isInitialized) {
			//load award definitions
			awardsData.getAwardDefinitions();
			//load assetlist
			assetsData.getAssetDefinitions();
			//load assets
			assetsData.loadAssets();
			eventTemplateData.getEventTemplateDefinitions();
			MELEventLoader MELLoader = new MELEventLoader();
			eventList=(MELEventList) MELLoader.load("eventDb.json");
			for(MELEvent event:eventList) {
                MELDebug.log(TAG + "Eventlist: " + event.getEventName(), localDebug);
            }loadPrefs();
			// soundManager= new SoundManager();
			// soundManager.init();
			setScreen(new Splash());
			isInitialized = true;
			//start android timer service
			callEventIntent("STARTSERVICE");
		}
	}

	@Override
	public void pause() {
		if (DEBUG)System.out.println("****APP PAUSE****");
		screen.pause();
	}

	@Override
	public void resume() {
		screen.resume();
	}

	@Override
	public void resize(int width, int height) {
		if (DEBUG)System.out.println("****APP RESIZE****");
	}

	/**
	 * 
	 * For this game each of our screens is an instance of StardustScreen.
	 * 
	 * @return the currently active {@link MyEpicLifeScreen}.
	 */
	public MyEpicLifeScreen getScreen() {
		return this.screen;
	}

	// demo render
	@Override
	public void render() {

		MyEpicLifeScreen currentScreen = getScreen();
		// update the screen
		// screen.update(Gdx.graphics.getDeltaTime());
		// render the screen
		if(currentScreen!=null){
		currentScreen.render(Gdx.graphics.getDeltaTime());
		}
		if(currentScreen==null){
			setScreen(new MainMenuScreen());
		}
		// when the screen is done we change to the
		// next screen
		if (selectScreen) {
			// coming from a screen we dont want to dispose ie main sim
			switch (gameMode) {

			case 4:
				MELDebug.log("run game",localDebug);
				// setScreen(new GameLoop());
				break;
			case 8:
				MELDebug.log("run solar sys info",localDebug);
				// setScreen(new Solar());
				break;
			case 9:
				MELDebug.log("run help",localDebug);
				// setScreen(new Help(helptext));
				break;
			case 13:
				MELDebug.log("run market info dep",localDebug);
				// setScreen(new Market());
				break;

			}
			// reset selectscreen so we only call once
			selectScreen = false;
		}
		if(currentScreen!=null){
		if (screen.isDone()) {
			// dispose the current screen
			// may be some screens we dont want to dispose main sim
			if (screen!=null){
			screen.dispose();}
			// switch to requested screen
			// the game loop
			MELDebug.log("running mode" + gameMode, localDebug);
			switch (gameMode) {
			case 2:
				MELDebug.log("run main screen", localDebug);
				setScreen(new MainMenuScreen());

				break;
			case 3:
				MELDebug.log("run calendar screen",localDebug);
				setScreen(new CalendarScreen());
				break;
			case 4:
				MELDebug.log("run award screen",localDebug);
				setScreen(new AwardScreen());
				break;
			case 5:
				MELDebug.log("run event list",localDebug);
				setScreen(new EventListScreen());
				break;

			case 6:
				MELDebug.log("run new event wizard screen",localDebug);
				setScreen(new NewEventScreen());
				break;

			case 7:
				MELDebug.log("run options screen",localDebug);
				setScreen(new OptionsScreen());
				break;
			case 8:
				MELDebug.log("run help screen",localDebug);
				//setScreen(new TestScreen());
				setScreen(new HelpScreen(helptext));
				break;
			case 9:
				MELDebug.log("run repeat setup screen",localDebug);
				setScreen(new RepeatingEventScreen());
				break;
			case 10:
				MELDebug.log("run event details screen",localDebug);
				setScreen(new EventDetailScreen(eventintent));
				break;
			}
		}
		}

	}

	/** Sets the current screen. { Screen#hide()} is called on any old
	screen, and { Screen#show()} is called on the new
	screen, if any.
	@param screen may be {@code null} **/

	public void setScreen(MyEpicLifeScreen screen) {
		if (this.screen != null)
			this.screen.hide();
		this.screen = screen;
		if (this.screen != null) {
			this.screen.show();
			this.screen.resize(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		}
	}

	@Override
	public void dispose() {
		desktopTimer.dispose();
		screen.dispose();
		microFont.dispose();
		globalFont.dispose();
		titleFont.dispose();
	}

	// Load application preferences

	private void loadPrefs() {
		/** get prefs, if no prefs (-1) use default settings **/
		Preferences prefs = Gdx.app.getPreferences("myepiclifetpreferences");

		float tempFloat;
		int tempInt = -1;
		boolean tempBoolean;
		tempBoolean = prefs.getBoolean("fontsize", false);
		tinyfont = tempBoolean;
		tempFloat = prefs.getFloat("sfxvolume", -1);
		if (tempFloat != -1)
			sfxVolume = tempFloat;
		// get music volume from prefs
		tempFloat = prefs.getFloat("musicvolume", -1);
		if (tempFloat != -1)
			musicVolume = tempFloat;

		// get horiz dead zone from prefs
		tempFloat = prefs.getFloat("dzx", 0.5f);
		if (tempFloat != .5)
			deadzoneX = tempFloat;
		// get vert dead zone from prefs
		tempFloat = prefs.getFloat("dzy", 0.5f);
		if (tempFloat != .5)
			deadzoneY = tempFloat;
		// get horiz sensitivity from prefs
		tempFloat = prefs.getFloat("sx", 1f);
		if (tempFloat != 1)
			accelXsensitivity = tempFloat;
		// get very dead zone from prefs
		tempFloat = prefs.getFloat("sy", 1f);
		if (tempFloat != 1)
			accelYsensitivity = tempFloat;
	}

	public static void callEventIntent(String activityId) {
		
		   // check the calling class has actually implemented MyGameCallback
        if (myGameCallback != null) {

            // initiate which ever callback method you need.
            if (activityId.equals("NEWEVENT")) {
                myGameCallback.onStartActivityA();
            } else if (activityId.equals("STARTSERVICE")) {
                myGameCallback.onStartActivityB();
            } else {
             //   myGameCallback.onStartSomeActivity(someInteger, someString);
            }

        } else {
            System.out.println(TAG+"To use this class you must implement MyGameCallback!");
        }
    }
    @Override
	public boolean keyDown(int keycode) {
    	MELDebug.log(TAG+"key pressed"+keycode,localDebug);
	      // TODO Auto-generated method stub
	       if(keycode == Keys.BACK){
	    	   MELDebug.log(TAG + "back key pressed", localDebug);
	             // Do back button handling (show pause menu?)
	             Gdx.app.exit(); //This will exit the app but you can add other options here as well
	       }
	      return false;
	 }
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
		
	}

	/*
	 * public static void loadstage01() {
	 * 
	 * }
	 * 
	 * public static void loadstage02() { MyDataOp.getAvatarDefs(); if
	 * (Stardust3d.DEBUG) { for (int i = 0; i < avatarList.size(); i++) {
	 * System.out.println("avatardef " + i + " " +
	 * avatarList.get(i).getDescription()); } } for (int i = 0; i <
	 * charList.length; i++) { charList[i] = new Avatar();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public static void loadstage03() { // copy to new array without nulls
	 * tempchar = new Avatar[numberOfCharacters]; for (int i = 0; i <
	 * tempchar.length; i++) { tempchar[i] = charList[i]; } // convert array to
	 * list List<Avatar> charRealList = Arrays.asList(tempchar);
	 * 
	 * for (int i = 0; i < npcList.length; i++) { npcList[i] = new NPC(); }
	 * 
	 * for (int i = 0; i < LandTypes.length; i++) { LandTypes[i] = new
	 * GroundItem(); } for (int x = 0; x < 64; x++) { for (int y = 0; y < 64;
	 * y++) { LandScape[x][y] = new Location(); } }
	 * 
	 * }
	 * 
	 * public static void loadstage04() { MyDataOp.getShipDefs(); if
	 * (Stardust3d.DEBUG) { for (int i = 0; i < shipdefs.size(); i++) {
	 * System.out.println("shipdef " + i + " " + shipdefs.get(i).getModel()); }
	 * } for (int i = 0; i < solarSystem.length; i++) { solarSystem[i] = new
	 * celestial(); } String returnString = "test";
	 * 
	 * 
	 * }
	 * 
	 * public static void loadstage05() { String returnString = "test";
	 * 
	 * 
	 * }
	 * 
	 * public static void loadstage06() {
	 * 
	 * MyDataOp.getInventory(Integer.valueOf(currencharacteruid),true); for (int
	 * i = 0; i < news.length; i++) { news[i] = new News(); }
	 * 
	 * }
	 * 
	 * public static void loadstage07() { MyDataOp.getNews();
	 * 
	 * mnewslines = " \n"; // insert news text into newsTextView for (int i = 0;
	 * i < news.length; i++) { // check to see that there is news to add if
	 * (news[i].get_title() != null) { // Create news string nicely formatted
	 * 
	 * mnewslines = mnewslines + news[i].get_title() + "\n" + news[i].get_date()
	 * + "\n" + news[i].get_text() + " \n"; mnewslines = mnewslines +
	 * "________________________________________\n"; } }
	 * 
	 * }
	 * 
	 * public static void loadstage08() { MyDataOp.getChatDefs(); for (int i =
	 * 0; i < channelList.size(); i++) { if (Stardust3d.DEBUG)
	 * System.out.println("channeldef " + i + " " +
	 * channelList.get(i).getChannelname()); }
	 * 
	 * for (int i = 0; i < charList.length; i++) { charList[i] = new Avatar(); }
	 * }
	 * 
	 * public static void loadstage09() { // TODO Auto-generated method stub
	 * chatChannelName = channelList.get(chatChannel).getChannelname();
	 * chatListReset = true; refreshChat(chatChannel, false); chatListReset =
	 * true; refreshChat(1, true); }
	 * 
	 * public static void loadstage10() { // TODO Auto-generated method stub
	 * 
	 * }
	 */
