package ru.saubulprojects.currencybot.service;

import ru.saubulprojects.currencybot.entity.Currency;

public interface CurrencyConversionService {
	
	double convert(Currency original, Currency target);
	
}
