package com.digitale.screens;

import java.util.ArrayList;

public class BarList extends ArrayList<Bar> implements BarListInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6081673211199699650L;

	public BarList(BarList barList) {
		this.addAll(barList);
	}

	public BarList() {
		// TODO Auto-generated constructor stub
	}

	public int getMax(BarList barList) {
		int maxValue =0;
		for(Bar currentBar:barList ){
			if (currentBar.getValue()>= maxValue){
				maxValue=currentBar.getValue();
				
			}
		}

		return maxValue;
	}

	@Override
	public int getMax() {
		int maxValue =0;
		for(Bar currentBar:this ){
			if (currentBar.getValue()>= maxValue){
				maxValue=currentBar.getValue();
				
			}
		}

		return maxValue;
	}

}
