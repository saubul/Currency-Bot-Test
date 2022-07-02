package ru.saubulprojects.currencybot.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.SneakyThrows;

public class CurrencyBot extends TelegramLongPollingBot{

	public CurrencyBot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public String getBotToken() {
		return "5279326899:AAEJtRhXCurEeKk1pTHY0Swz6t-Ef8yp_Fk";
	}

	@Override
	@SneakyThrows
	public void onUpdateReceived(Update update) {
		this.execute(SendMessage.builder()
									.chatId(update.getMessage().getChatId())
									.text("HI!")
								.build());
	}

	@Override
	public String getBotUsername() {
		return "SaubulTestTelegramBot";
	}
	
}
