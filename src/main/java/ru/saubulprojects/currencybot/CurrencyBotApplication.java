package ru.saubulprojects.currencybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.SneakyThrows;
import ru.saubulprojects.currencybot.bot.CurrencyBot;

@SpringBootApplication
public class CurrencyBotApplication {

	
	@SneakyThrows
	public CurrencyBotApplication(CurrencyBot currencyBot) {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(currencyBot);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CurrencyBotApplication.class, args);
	}
	
}
