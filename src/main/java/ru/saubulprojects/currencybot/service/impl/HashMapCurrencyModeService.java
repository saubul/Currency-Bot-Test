package ru.saubulprojects.currencybot.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import ru.saubulprojects.currencybot.entity.Currency;
import ru.saubulprojects.currencybot.service.CurrencyModeService;

@Service
public class HashMapCurrencyModeService implements CurrencyModeService {

	private final Map<Long, Currency> originalCurrency = new HashMap<>();
	private final Map<Long, Currency> targetCurrency = new HashMap<>();
	
	@Override
	public Currency getOriginalCurrency(long chatId) {
		return originalCurrency.getOrDefault(chatId, Currency.USD);
	}

	@Override
	public Currency getTargetCurrency(long chatId) {
		return targetCurrency.getOrDefault(chatId, Currency.USD);
	}

	@Override
	public void setOriginalCurrency(long chatId, Currency currency) {
		originalCurrency.put(chatId, currency);
	}

	@Override
	public void setTargetCurrency(long chatId, Currency currency) {
		targetCurrency.put(chatId, currency);
	}

}
