package com.fonenet.fonemarket.xmltools;

import java.util.ArrayList;

public class Page {

	String name;
	Integer itemNum;
	ArrayList<Item> items;

	public class Item {
		public String getAttr() {
			return attr;
		}
		public String getBinName() {
			return binName;
		}
		public Integer getAppSize() {
			return appSize;
		}
		public String getFee() {
			return fee;
		}
		public String getAppId() {
			return appId;
		}
		public String getIconName() {
			return iconName;
		}
		public String getIntro() {
			return intro;
		}
		public String getName() {
			return name;
		}
		public String getStart() {
			return start;
		}
		public String getTime() {
			return time;
		}
		public String getType() {
			return type;
		}
		public String getVersion() {
			return version;
		}
		String attr;
		String binName;
		Integer appSize;
		String fee; // no use
		String appId;
		String iconName;
		String intro;
		String name; // item name
		String start; // no use
		String time;
		String type;
		String version;
	};

	public Page() {
		name = null;
		itemNum = 0;
		items = new ArrayList<Item>();
	}

	public void setName(String setName) {
		name = setName;
	}

	public String getName() {
		return name;
	}

	public Integer getItemNum() {
		return itemNum;
	}

	public ArrayList<Item> getItems() {
		return items;
	}
	public Integer addItem(Item item) {
		itemNum++;
		items.add(item);
		return itemNum;
	}

};
