package net.onima.onimaapi.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class ChatManager implements FileSaver {
	
	private static final Pattern URL_REGEX = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
	private static final Pattern IP_REGEX = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
	private static final List<String> LINK_WHITELIST = Arrays.asList("noelshack.com", "youtube.com", "youtu.be", "discord.gg", "twitter.com",
			"prnt.sc", "gyazo.com", "imgur.com"
	);

	private boolean mute, slow;
	private int delay;
	private String muter, slower;
	private Set<String> messagesToRegister;
	private List<String> filteredWords, filteredPhrases;
	
	{
		delay = 0;
		messagesToRegister = new HashSet<>();
		filteredWords = Arrays.asList("nigger", "faggot", "queer", "paki", "slut", "nègre", "pute", "enculé", "enculer", "ddos", "pd", "salope", "connard", "mongolien", "boot");
		filteredPhrases = Arrays.asList("adviser is fat", "adviser fat", "server de merde", "serveur de merde", "serveur pourri");
		save();
	}
	
	public void mute(boolean mute, String muter) {
		this.mute = mute;
		this.muter = mute == false ? null : muter;
	}
	
	public void slow(boolean slow, int delay, String slower) {
		this.slow = slow;
		this.delay = delay;
		this.slower = slow == false ? null : slower;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void clear(int lines) {
		for (int i = 0; i < lines; i++)
			Bukkit.broadcastMessage(" ");
	}
	
	public void clear(int lines, CommandSender sender) {
		for (int i = 0; i < lines; i++)
			sender.sendMessage("");
	}
	
	public int getDelay() {
		return delay;
	}
	
	public boolean isSlowed() {
		return slow;
	}
	
	public boolean isMuted() {
		return mute;
	}

	public String getMuter() {
		return muter;
	}

	public void setMuter(String muter) {
		this.muter = muter;
	}

	public String getSlower() {
		return slower;
	}

	public void setSlower(String slower) {
		this.slower = slower;
	}

	public void addMessage(String sender, String message) {
		messagesToRegister.add("[" + Methods.toFormatDate(System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_HOURS) + "] " + sender + ": " + message);
	}
	
	public boolean shouldFilter(String message) {
		String msg = message.toLowerCase()
		                    .replace("3", "e")
		                    .replace("1", "i")
		                    .replace("!", "i")
		                    .replace("@", "a")
		                    .replace("7", "t")
		                    .replace("0", "o")
		                    .replace("5", "s")
		                    .replace("8", "b")
		                    .replaceAll("\\p{Punct}|\\d", "").trim();

		for (String word : msg.trim().split(" ")) {
			for (String filteredWord : filteredWords) {
				if (word.contains(filteredWord))
					return true;
			}
		}

		for (String word : message.replace("(dot)", ".").replace("[dot]", ".").replace("(point)", ".").replace("[point]", ".").replace("(.)", ".").replace("[.]", ".").trim().split(" ")) {
			boolean continueIt = false;

			for (String phrase : ChatManager.LINK_WHITELIST) {
				if (word.toLowerCase().contains(phrase)) {
					continueIt = true;
					break;
				}
			}

			if (continueIt)
				continue;

			Matcher matcher = ChatManager.IP_REGEX.matcher(word);

			if (matcher.matches())
				return true;

			matcher = ChatManager.URL_REGEX.matcher(word);

			if (matcher.matches())
				return true;
		}

		return filteredPhrases.parallelStream().filter(msg::contains).findFirst().isPresent();
	}

	@Override
	public void save() {
		OnimaAPI.getSavers().add(this);
	}

	@Override
	public void remove() {
		OnimaAPI.getSavers().remove(this);
	}

	@Override
	public boolean isSaved() {
		return OnimaAPI.getSavers().contains(this);
	}

	@Override
	public void serialize() {
		try {
			File file = new File(Config.DEFAULT_PATH, "chat-register.txt");
			
			if (!file.exists())
				file.createNewFile();
			
			PrintWriter pw = new PrintWriter(new FileWriter(file, true));
			
			for (String message : messagesToRegister)
				pw.println(message);
			
			messagesToRegister.clear();
			
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Impossible d'enregistrer un message, veuillez contacter un développeur.");
		}		
	}

	@Override
	public void refreshFile() {
		
	}
	
}
