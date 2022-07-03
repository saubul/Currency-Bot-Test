package ru.saubulprojects.currencybot.service;

import ru.saubulprojects.currencybot.entity.Currency;

public interface CurrencyModeService {
	
	Currency getOriginalCurrency(long chatId);
	
	Currency getTargetCurrency(long chatId);
	
	void setOriginalCurrency(long chatId, Currency currency);
	
	void setTargetCurrency(long chatId, Currency currency);
	
}

