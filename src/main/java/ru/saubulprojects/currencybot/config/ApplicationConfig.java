package ru.saubulprojects.currencybot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import ru.saubulprojects.currencybot.bot.CurrencyBot;

@Configuration
public class ApplicationConfig {

	@Bean
	public CurrencyBot bot() {
		return new CurrencyBot(new DefaultBotOptions());
	}
	
}
