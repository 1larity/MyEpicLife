package com.digitale.screens;

import com.badlogic.gdx.graphics.Color;

class Bar {
	int value;
	Color color;

	public Bar(int value, Color color) {
		this.value = value;
		this.color = color;
	}
	//**default constructor
	public Bar() {
	}
	public Bar(Bar currentBar) {
		this.value = currentBar.value;
		this.color = currentBar.color;
	}
	//**set this bars colour*/
	public void setColor(Color color){
		this.color=color;
	}
	//**get this bars colour*/
	public Color getColor(){
		return this.color;
	}

	//**set this bars value*/
	public void setValue(int value){
		this.value=value;
	}
	//**get this bars colour*/
	public int getValue(){
		return this.value;
	}
}