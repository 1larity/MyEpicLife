package com.digitale.screens;

import com.digitale.myepiclife.MyEpicLife;

/**framelimit rendering to reduce battery usage**/
public class FrameLimit {
	private static long diff;
	private static long start = System.currentTimeMillis();
	/**limit FPS using parameter
	 * @param fps Desired frames per second to limit rendering to
	 * **/
	public static void sleep(int fps) {
	    if(fps>0){
	      diff = System.currentTimeMillis() - start;
	      long targetDelay = 1000/fps;
	      if (diff < targetDelay) {
	        try{
	            Thread.sleep(targetDelay - diff);
	          } catch (InterruptedException e) {}
	        }   
	      start = System.currentTimeMillis();
	    }
	}
	/**limit FPS using global variable**/
	public static void sleep() {
		int fps=MyEpicLife.selectedFPS;
	    if(fps>0){
	      diff = System.currentTimeMillis() - start;
	      long targetDelay = 1000/fps;
	      if (diff < targetDelay) {
	        try{
	            Thread.sleep(targetDelay - diff);
	          } catch (InterruptedException e) {}
	        }   
	      start = System.currentTimeMillis();
	    }
	}
}
