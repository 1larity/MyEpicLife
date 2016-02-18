package com.digitale.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;


public class Scene2dConverters {

public static final TypeConverter<Actor> actorPositionTypeConverter = new TypeConverter<Actor>() {
@Override
public int variables() {
return 2;
}

@Override
public float[] copyFromObject(Actor object, float[] x) {
if (x == null)
x = new float[variables()];
x[0] = object.getX();
x[1] = object.getY();
return x;
}

@Override
public Actor copyToObject(Actor object, float[] x) {
object.setX(x[0]);
object.setY(x[1]);
return object;
}
};

}