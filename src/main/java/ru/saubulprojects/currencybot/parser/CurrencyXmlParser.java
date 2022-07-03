package ru.saubulprojects.currencybot.parser;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ru.saubulprojects.currencybot.entity.Currency;

@Service
public class CurrencyXmlParser {
	
	public double parse(String xml, Currency currency) {
		
		if(currency == Currency.RUB) {
			return 1.0;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			try(StringReader sReader = new StringReader(xml)) {
				Document doc = db.parse(new InputSource(sReader));
				doc.getDocumentElement().normalize();
				NodeList nodes = doc.getElementsByTagName("ValuteCursOnDate");
				for(int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element el = (Element) node;
						if(el.getElementsByTagName("VchCode").item(0).getTextContent().equalsIgnoreCase(currency.toString())) {
							System.out.println(el.getElementsByTagName("Vcurs").item(0).getTextContent());
							return Double.parseDouble(el.getElementsByTagName("Vcurs").item(0).getTextContent());
						}
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return 1.0;
	}
	
}
