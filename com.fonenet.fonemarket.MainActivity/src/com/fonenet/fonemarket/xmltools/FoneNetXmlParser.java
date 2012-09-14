package com.fonenet.fonemarket.xmltools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import com.fonenet.fonemarket.xmltools.Page;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class FoneNetXmlParser {
	// We don't use namespaces
	private static final String ns = null;
	String fName;
	Context mContext;
	public FoneNetXmlParser(Context context, String fileName) {
		mContext = context;
		fName = fileName;
	}

	Integer pageNum;
	ArrayList<Page> pages;

	public ArrayList<Page> getPages() {
		return pages;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}
	
	public ArrayList<Page> readXML(String fileName) {
		FileInputStream inFS;
		try {
			inFS = new FileInputStream(fileName);
			pages = readXML(inFS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("FoneError", fileName+"open error!");
			pages = null;
		}
		return pages;
	}
	
	@SuppressLint("UseValueOf")
	public ArrayList<Page> readXML(InputStream inStream) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
			Page currentPage = null;
			com.fonenet.fonemarket.xmltools.Page.Item curItem = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					pages = new ArrayList<Page>();
					pageNum = 0;
					break;
				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase("page")) {
						currentPage = new Page();
						currentPage.setName(new String(parser.getAttributeValue(null, "name")));
					} else if (currentPage != null) {
						if (name.equalsIgnoreCase("item")) {
							curItem = currentPage.new Item();
							curItem.attr = new String(parser.getAttributeValue(null, "attr"));
							curItem.binName = new String(parser.getAttributeValue(null, "bin"));
							curItem.appSize = new Integer(parser.getAttributeValue(null, "binsize"));
							curItem.fee = new String(parser.getAttributeValue(null, "fee"));
							curItem.appId = new String(parser.getAttributeValue(null, "id"));
							curItem.iconName = new String(parser.getAttributeValue(null, "image"));
							curItem.intro = new String(parser.getAttributeValue(null, "intro"));
							curItem.name = new String(parser.getAttributeValue(null, "name"));
							curItem.start = new String(parser.getAttributeValue(null, "start"));
							curItem.time = new String(parser.getAttributeValue(null, "time"));
							curItem.type = new String(parser.getAttributeValue(null, "type"));
							curItem.version = new String(parser.getAttributeValue(null, "version"));
						}
					}
					break;

				case XmlPullParser.END_TAG:// 结束元素事件
					if (parser.getName().equalsIgnoreCase("page")
							&& currentPage != null) {
						pages.add(currentPage);
						currentPage = null;
					}
					else if (parser.getName().equalsIgnoreCase("item")
							&& currentPage != null) {
						currentPage.addItem(curItem);
						curItem = null;
					}
					break;
				}
				eventType = parser.next();
			}
			inStream.close();
			return pages;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}