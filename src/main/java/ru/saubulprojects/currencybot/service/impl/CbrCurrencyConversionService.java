package ru.saubulprojects.currencybot.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.saubulprojects.currencybot.config.CbrConfig;
import ru.saubulprojects.currencybot.entity.Currency;
import ru.saubulprojects.currencybot.parser.CurrencyXmlParser;
import ru.saubulprojects.currencybot.parser.DateXmlParser;
import ru.saubulprojects.currencybot.service.CurrencyConversionService;

@Service
@RequiredArgsConstructor
public class CbrCurrencyConversionService implements CurrencyConversionService{

	private final CbrConfig cbrConfig;
	private final DateXmlParser dateXmlParser;
	private final CurrencyXmlParser currencyXmlParser;
	
	@Override
	public double convert(Currency original, Currency target) {
		double originalRatio = getCurrencyRatioToRub(original);
		double targetRatio = getCurrencyRatioToRub(target);
		double resultRatio = originalRatio/targetRatio;
		return resultRatio;
	}
	
	@SneakyThrows
	private String getLatestDate() {
		//установка соединения
		URL url = new URL(cbrConfig.getUrl());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		//настройка параметров
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/xml");
		
		//тело запроса
		String requestForDate = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
				+ "  <soap:Body>\r\n"
				+ "    <GetLatestDateTime xmlns=\"http://web.cbr.ru/\" />\r\n"
				+ "  </soap:Body>\r\n"
				+ "</soap:Envelope>";
		
		//флаг на отправку данных
		con.setDoOutput(true);
		
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestForDate);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return dateXmlParser.parse(response.toString());
	}
	
	@SneakyThrows
	private double getCurrencyRatioToRub(Currency currency) {

		//установка соединения
		URL url = new URL(cbrConfig.getUrl());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
				
		//настройка параметров
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/xml");
		
		//флаг на отправку данных
		con.setDoOutput(true);
				
		String date = getLatestDate();
		String requestForCurrencyXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
				+ "  <soap:Body>\r\n"
				+ "    <GetCursOnDateXML xmlns=\"http://web.cbr.ru/\">\r\n"
				+ "      <On_date>" + date + "</On_date>\r\n"
				+ "    </GetCursOnDateXML>\r\n"
				+ "  </soap:Body>\r\n"
				+ "</soap:Envelope>";
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestForCurrencyXml);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return currencyXmlParser.parse(response.toString(), currency);
	}
}
