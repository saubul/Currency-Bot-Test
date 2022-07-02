package ru.saubulprojects.currencybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public class CurrencyBotConfig {
	
	private String username;
	private String token;
	
}
