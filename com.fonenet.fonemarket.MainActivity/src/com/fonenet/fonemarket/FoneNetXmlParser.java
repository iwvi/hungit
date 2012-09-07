package com.fonenet.fonemarket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.util.Xml;

public class FoneNetXmlParser {
	// We don't use namespaces
	private static final String ns = null;

	public class Person {
		private Integer id;
		private String name;
		private Short age;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Short getAge() {
			return age;
		}

		public void setAge(Short age) {
			this.age = age;
		}
	}

	@SuppressLint("UseValueOf")
	public List<Person> readXML(InputStream inStream) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
			Person currentPerson = null;
			List<Person> persons = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					persons = new ArrayList<Person>();
					break;
				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase("person")) {
						currentPerson = new Person();
						currentPerson.setId(new Integer(parser
								.getAttributeValue(null, "id")));
					} else if (currentPerson != null) {
						if (name.equalsIgnoreCase("name")) {
							currentPerson.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
						} else if (name.equalsIgnoreCase("age")) {
							currentPerson.setAge(new Short(parser.nextText()));
						}
					}
					break;

				case XmlPullParser.END_TAG:// 结束元素事件
					if (parser.getName().equalsIgnoreCase("person")
							&& currentPerson != null) {
						persons.add(currentPerson);
						currentPerson = null;
					}
					break;
				}
				eventType = parser.next();
			}
			inStream.close();
			return persons;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}