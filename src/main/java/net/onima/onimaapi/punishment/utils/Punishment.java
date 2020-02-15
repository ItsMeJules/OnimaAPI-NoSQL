package net.onima.onimaapi.punishment.utils;

import java.util.UUID;

import org.bson.Document;

import net.onima.onimaapi.mongo.saver.MongoSerializer;
import net.onima.onimaapi.punishment.Ban;
import net.onima.onimaapi.punishment.BlackList;
import net.onima.onimaapi.punishment.Kick;
import net.onima.onimaapi.punishment.Mute;
import net.onima.onimaapi.punishment.TempBan;
import net.onima.onimaapi.punishment.Warn;

public abstract class Punishment implements MongoSerializer {

	public static final int CANT_EXPIRE_TIME;
	
	public static Integer ID;
	
	static {
		CANT_EXPIRE_TIME = -2;
	}
	
	protected int id;
	protected PunishmentType type;
	protected UUID sender, receiver, remover;
	protected String reason, removeReason;
	protected boolean silent;
	protected long issued, expire;
	
	{
		issued = System.currentTimeMillis();
	}
	
	public Punishment(PunishmentType type, UUID sender, UUID receiver) {
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public abstract String errorMessage();
	public abstract boolean execute();
	
	public int getID() {
		return id;
	}
	
	public void initID() {
		id = ID++;
	}

	public PunishmentType getType() {
		return type;
	}

	public UUID getSender() {
		return sender;
	}

	public UUID getReceiver() {
		return receiver;
	}
	
	public UUID getRemover() {
		return remover;
	}

	public void setRemover(UUID remover) {
		this.remover = remover;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getRemoveReason() {
		return removeReason;
	}
	
	public void setRemoveReason(String removeReason) {
		this.removeReason = removeReason;
	}

	public boolean isSilent() {
		return silent;
	}
	
	public void setSilent(boolean silent) {
		this.silent = silent;
	}
	
	public long getIssued() {
		return issued;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
	public long getDuration() {
		return expire - System.currentTimeMillis();
	}
	
	public void setDuration(long duration) {
		expire = duration + System.currentTimeMillis();
	}
	
	public boolean isRemoved() {
		return removeReason != null;
	}
	
	public boolean isActive() {
		if (isRemoved())
			return false;
		else if (expire == CANT_EXPIRE_TIME)
			return true;
		else if (expire == -1)
			return false;
		else
			return expire >= System.currentTimeMillis();
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("id", id).append("punishment_type", type.name())
				.append("reason", reason).append("remove_reason", removeReason)
				.append("silent", silent).append("issued", issued).append("expire", expire)
				.append("remover_uuid", remover != null ? remover.toString() : null)
				.append("sender_uuid", sender.toString());
	}
	
	public static Punishment fromDB(int id, PunishmentType type, UUID sender, UUID receiver, UUID remover, String reason, String removeReason, boolean silent, long issued, long expire) {
		Punishment punishment = null;
		
		switch (type) {
		case BAN:
			punishment = new Ban(sender, receiver);
			break;
		case BLACKLIST:
			punishment = new BlackList(null, sender, receiver);
			break;
		case KICK:
			punishment = new Kick(sender, receiver);
			break;
		case MUTE:
			punishment = new Mute(sender, receiver, 0);
			break;
		case TEMPBAN:
			punishment = new TempBan(sender, receiver, 0);
			break;
		case WARN:
			punishment = new Warn(sender, receiver);
			break;
		default:
			break;
		}
		
		punishment.id = id;
		punishment.remover = remover;
		punishment.reason = reason;
		punishment.removeReason = removeReason;
		punishment.silent = silent;
		punishment.issued = issued;
		punishment.expire = expire;
		
		return punishment;
	}
	
}
