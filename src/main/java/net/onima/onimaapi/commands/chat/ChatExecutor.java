package net.onima.onimaapi.commands.chat;

import net.onima.onimaapi.commands.chat.arguments.ChatClearArgument;
import net.onima.onimaapi.commands.chat.arguments.ChatMuteArgument;
import net.onima.onimaapi.commands.chat.arguments.ChatSlowArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class ChatExecutor extends ArgumentExecutor {

	public ChatExecutor() {
		super("chat", OnimaPerm.ONIMAAPI_CHAT_COMMAND);
		
		addArgument(new ChatClearArgument());
		addArgument(new ChatMuteArgument());
		addArgument(new ChatSlowArgument());
	}

}
