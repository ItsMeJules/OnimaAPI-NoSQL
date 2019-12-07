package net.onima.onimaapi.players.notes;

import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.mongo.saver.MongoSerializer;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class Note implements MongoSerializer {

	public static final int NEVER_EXPIRE;
	
	static {
		NEVER_EXPIRE = -1;
	}
	
	private UUID sender;
	private String note;
	private NotePriority priority;
	private long time;
	private long expire;
	
	{
		time = System.currentTimeMillis();
		expire = NEVER_EXPIRE;
	}
	
	public Note(UUID sender, String note, NotePriority priority) {
		this.sender = sender;
		this.note = note;
		this.priority = priority;
	}

	public Note(UUID sender, String note) {
		this(sender, note, NotePriority.INFORMATIVE);
	}
	
	public void display(APIPlayer apiPlayer, APIPlayer target) {
		apiPlayer.sendMessage("§e[]§6" + ConfigurationService.STAIGHT_LINE + "§e[]");
		apiPlayer.sendMessage(getComponNote(-1, null));
	}
	
	public BaseComponent[] getComponNote(int pos, String targetName) {
		JSONMessage json = new JSONMessage(
				(pos == -1 ? "" : "§e" + pos + ". ") + priority.getColor() + note,
						"§eÉcrite par : §6" + (sender.equals(ConfigurationService.CONSOLE_UUID) ? "Console" : Methods.getRealName(Bukkit.getOfflinePlayer(sender)))
						+ "\n§ePriorité : " + priority.getColor() + priority.name()
						+ "\n§eÉcrite le : §6" + Methods.toFormatDate(time, ConfigurationService.DATE_FORMAT_HOURS)
						+ (expire == -1 ? "" : "\n§eExpire le : §6" + Methods.toFormatDate(expire, ConfigurationService.DATE_FORMAT_HOURS))
							+ (isExpired() ? "§7(§oexpirera à la prochaine sauvegarde.§7)" : ""));

		if (targetName != null) {
			json.setClickAction(ClickEvent.Action.SUGGEST_COMMAND);
			json.setClickString("/note remove " + targetName);
		}
		
		return json.build();
	}
	
	public UUID getSender() {
		return sender;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public NotePriority getPriority() {
		return priority;
	}

	public void setPriority(NotePriority priority) {
		this.priority = priority;
	}

	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

	public long getExpire() {
		return expire;
	}
	
	public boolean isExpired() {
		if (expire == NEVER_EXPIRE)
			return false;
		else
			return expire <= System.currentTimeMillis();
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("sender", sender.toString()).append("note", note)
				.append("priority", priority.name()).append("created", time)
				.append("expire", expire);
	}

}
