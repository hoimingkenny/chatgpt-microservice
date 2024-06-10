package com.openai.chatgpt;

import com.openai.chatgpt.domain.telegram.TestBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ChatgptApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatgptApplication.class, args);

//		String botToken = "7024395537:AAHdQVuCxZicHH7ADuE2irYn9H_iMYb6dTo";
//		try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
//			botsApplication.registerBot(botToken, new TestBot(botToken));
//			System.out.println("MyAmazingBot successfully started!");
//			Thread.currentThread().join();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
