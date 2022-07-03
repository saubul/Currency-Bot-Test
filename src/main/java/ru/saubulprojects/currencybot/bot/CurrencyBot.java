package ru.saubulprojects.currencybot.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.saubulprojects.currencybot.config.CurrencyBotConfig;
import ru.saubulprojects.currencybot.entity.Currency;
import ru.saubulprojects.currencybot.service.CurrencyConversionService;
import ru.saubulprojects.currencybot.service.CurrencyModeService;

@Component
public class CurrencyBot extends TelegramLongPollingBot{

	private final CurrencyBotConfig botConfig;
	
	private final CurrencyModeService currencyModeService;
	private final CurrencyConversionService currencyConversionService;
	
	public CurrencyBot(DefaultBotOptions options, 
					   CurrencyModeService currencyModeService,
					   CurrencyConversionService currencyConversionService,
					   CurrencyBotConfig botConfig) {
		super(options);
		this.currencyModeService = currencyModeService;
		this.currencyConversionService = currencyConversionService;
		this.botConfig = botConfig;
	}

	@Override
	public String getBotToken() {
		return botConfig.getToken();
	}	
	
	@Override
	public String getBotUsername() {
		return botConfig.getUsername();
	}
	

	@Override
	@SneakyThrows
	public void onUpdateReceived(Update update) {
		if(update.hasCallbackQuery()) {
			handleCallback(update.getCallbackQuery());
		} else if(update.hasMessage()) {
			handleMessage(update.getMessage());
		}
	}

	@SneakyThrows
	private void handleCallback(CallbackQuery callbackQuery) {
		Message message = callbackQuery.getMessage();
		String[] param = callbackQuery.getData().split(":");
		String action = param[0];
		Currency currency = Currency.valueOf(param[1]);
		switch (action) {
			
			case "ORIGINAL":
				currencyModeService.setOriginalCurrency(message.getChatId(), currency);
				break;
			case "TARGET":
				currencyModeService.setTargetCurrency(message.getChatId(), currency);
				break;
		}
		execute(EditMessageReplyMarkup.builder()
										  .chatId(message.getChatId())
										  .messageId(message.getMessageId())
										  .replyMarkup(InlineKeyboardMarkup.builder()
												  							   .keyboard(getButtons(message))
												  						   .build())
									  .build());
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
						execute(SendMessage.builder()
										       .chatId(message.getChatId())
										       .text("Please choose Original and Target currencies")
										       .replyMarkup(InlineKeyboardMarkup.builder()
										    		   								.keyboard(getButtons(message))
										    		   							.build())
										   .build());	
						return;
				}
			}
		}else if(message.hasText()) {
			String messageText = message.getText();
			Optional<Double> value = parseDouble(messageText);
			if(value.isPresent()) {
				Currency original = currencyModeService.getOriginalCurrency(message.getChatId());
				Currency target = currencyModeService.getTargetCurrency(message.getChatId());
				execute(SendMessage.builder()
								       .chatId(message.getChatId())
								       .text(String.format("%4.2f %s is %4.2f %s", value.get(), 
								    		   									   original,
								    		   									   value.get()*currencyConversionService.convert(original, target),
								    		   									   target))
								   .build());
			}
		}
		
	}
	
	private Optional<Double> parseDouble(String messageText) {
		try {
			return Optional.of(Double.parseDouble(messageText));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	private List<List<InlineKeyboardButton>> getButtons(Message message) {
		List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
		Currency original = currencyModeService.getOriginalCurrency(message.getChatId());
		Currency target = currencyModeService.getTargetCurrency(message.getChatId());
		for(Currency currency : Currency.values()) {
			buttons.add(Arrays.asList(
									  InlineKeyboardButton.builder()
									  						  .text(getCurrencyButton(original, currency))
									  						  .callbackData("ORIGINAL:" + currency)
									  					  .build(),
									  InlineKeyboardButton.builder()
									  						  .text(getCurrencyButton(target, currency))
									  						  .callbackData("TARGET:" + currency)
									  					  .build()));
		}
		return buttons;
	}
	
	private String getCurrencyButton(Currency saved, Currency current) {
		return saved == current? current + "\u26BD" : current.name();
	}

}
