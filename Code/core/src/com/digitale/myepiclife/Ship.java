package com.digitale.myepiclife;


	import com.badlogic.gdx.graphics.g3d.Model;
	import com.badlogic.gdx.graphics.g3d.ModelInstance;
	import com.badlogic.gdx.math.Vector3;

	public class Ship extends ModelInstance {
		public static final float SHIP_RADIUS = 1;
		public static final float SHIP_VELOCITY = 20;
		public int lives = 3;
		public boolean isExploding = false;
		public float explodeTime = 0;

		public Ship (Model model) {
			super(model);
		}

		public void update (float delta) {
/*			if (isExploding) {
				explodeTime += delta;
				if (explodeTime > Explosion.EXPLOSION_LIVE_TIME) {
					isExploding = false;
					explodeTime = 0;
				}
			}*/
		}
	}

