package com.digitale.database;

import com.digitale.database.Item;
import com.digitale.database.ItemLoader;
import com.digitale.database.Weapon;

import java.io.IOException;
import java.util.ArrayList;

/*Simple Test Case*/
public class TestDb {
	ArrayList<Item> database;

	public void loadDb() throws IOException {
		ItemLoader itemLoader = new ItemLoader();
		database = itemLoader.load("weaponDb.json");
		for (Item item : database) {
			assert item.getName() != null;
			System.out.println("LOADER TEST" + item.getName());

		}

	}

	public void saveDb() throws IOException {
		ItemSaver itemSaver = new ItemSaver();
		for (Item item : database) {
			assert item.getName() != null;
			item.setName(item.getName() + "TEST");

		}
		itemSaver.save("itemtestDb.json", database);

	}

	public void loadnewDb() throws IOException {
		ItemLoader itemLoader = new ItemLoader();
		database = itemLoader.load("itemtestDb.json");
		for (Item item : database) {
			assert item.getName() != null;
			System.out.println("LOADER TEST" + item.getName());

		}

	}

}