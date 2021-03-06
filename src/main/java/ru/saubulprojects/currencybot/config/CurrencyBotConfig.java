package ru.saubulprojects.currencybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "bot")
@Getter
@Setter
public class CurrencyBotConfig {
	
	private String username;
	private String token;
	
}
