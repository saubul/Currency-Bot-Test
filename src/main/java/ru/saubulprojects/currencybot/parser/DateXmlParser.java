package ru.saubulprojects.currencybot.parser;


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


@Component
public class DateXmlParser {
	
	public String parse(String xml) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		String date = "";
		try {
			db = dbf.newDocumentBuilder();
		
			try(StringReader reader = new StringReader(xml)) {
				Document doc = db.parse(new InputSource(reader));
				doc.getDocumentElement().normalize();
				date = doc.getElementsByTagName("GetLatestDateTimeResult").item(0).getTextContent();
				date = date.substring(0, 10);
			} 
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return date;
	}
	
}
