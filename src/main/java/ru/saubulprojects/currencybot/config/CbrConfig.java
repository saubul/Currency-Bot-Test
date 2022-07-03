package ru.saubulprojects.currencybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "cbr")
@Getter
@Setter
public class CbrConfig {

	private String url;
	
}
