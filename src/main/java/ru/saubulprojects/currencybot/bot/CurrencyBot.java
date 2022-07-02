package ru.saubulprojects.currencybot.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.SneakyThrows;
import ru.saubulprojects.currencybot.entity.Currency;

public class CurrencyBot extends TelegramLongPollingBot{

	@Value("${bot.username}")
	private String username;
	
	@Value("${bot.token}")
	private String token;
	
	public CurrencyBot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public String getBotToken() {
		return this.token;
	}	
	
	@Override
	public String getBotUsername() {
		return this.username;
	}
	

	@Override
	@SneakyThrows
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()) {
			handleMessage(update.getMessage());
		}
	}

	@SneakyThrows
	private void handleMessage(Message message) {
		if(message.hasText() && message.hasEntities()) {
			Optional<MessageEntity> commandEntity =  message.getEntities().stream()
																			  .filter(e -> "bot_command".equals(e.getType()))
																			  .findFirst();
			if(commandEntity.isPresent()) {
				String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
				switch(command) {
					case "/set_currency":
						List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
						for(Currency currency : Currency.values()) {
							buttons.add(Arrays.asList(
													  InlineKeyboardButton.builder()
													  						  .text(currency.name())
													  						  .callbackData("ORIGINAL: " + currency)
													  					  .build(),
													  InlineKeyboardButton.builder()
													  						  .text(currency.name())
													  						  .callbackData("TARGET: " + currency)
													  					  .build()));
						}
						execute(SendMessage.builder()
										       .chatId(message.getChatId())
										       .text("Please choose Original and Target currencies")
										       .replyMarkup(InlineKeyboardMarkup.builder()
										    		   								.keyboard(buttons)
										    		   							.build())
										   .build());	
						return;
				}
			}
			
				
		}
	}


}
