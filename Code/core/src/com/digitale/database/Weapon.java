package com.digitale.database;

public class Weapon implements Item {

    private int id;
    private String name;
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	@Override
	public void setName(String name) {
		this.name=name;
		
	}
    
    
}