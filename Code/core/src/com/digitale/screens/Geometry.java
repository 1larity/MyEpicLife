package com.digitale.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Geometry {
	void drawRect(int x, int y, int width, int height, int thickness,Batch batch) {
	   
		Texture rect = null;
		batch.draw(rect, x, y, width, thickness);
	    batch.draw(rect, x, y, thickness, height);
	    batch.draw(rect, x, y+height-thickness, width, thickness);
	    batch.draw(rect, x+width-thickness, y, thickness, height);
	}

	void drawLine(int x1, int y1, int x2, int y2, int thickness, Batch batch) {
	    int dx = x2-x1;
	    int dy = y2-y1;
	    float dist = (int)Math.sqrt(dx*dx + dy*dy);
	    float rad = (int)Math.atan2(dy, dx);
	    Texture rect;
		//batch.draw(rect, x1, y1, dist, thickness, 0, 0, rad); 
	}
}
