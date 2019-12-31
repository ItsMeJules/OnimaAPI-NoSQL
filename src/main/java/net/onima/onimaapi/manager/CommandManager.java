package net.onima.onimaapi.manager;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.commands.AltsCommand;
import net.onima.onimaapi.commands.CondenseCommand;
import net.onima.onimaapi.commands.ConnectionLogsCommand;
import net.onima.onimaapi.commands.CoordsCommand;
import net.onima.onimaapi.commands.CrowbarCommand;
import net.onima.onimaapi.commands.DisguiseCommand;
import net.onima.onimaapi.commands.FreezeCommand;
import net.onima.onimaapi.commands.InvrestoredCommand;
import net.onima.onimaapi.commands.InvseeCommand;
import net.onima.onimaapi.commands.IpHistoryCommand;
import net.onima.onimaapi.commands.LagCommand;
import net.onima.onimaapi.commands.MenuCommand;
import net.onima.onimaapi.commands.MessageCommand;
import net.onima.onimaapi.commands.ModCommand;
import net.onima.onimaapi.commands.OniwandCommand;
import net.onima.onimaapi.commands.OresCommand;
import net.onima.onimaapi.commands.PayCommand;
import net.onima.onimaapi.commands.PunishmentCommand;
import net.onima.onimaapi.commands.ReplyCommand;
import net.onima.onimaapi.commands.RestoreCommand;
import net.onima.onimaapi.commands.SocialSpyCommand;
import net.onima.onimaapi.commands.SpawnerCommand;
import net.onima.onimaapi.commands.VanishCommand;
import net.onima.onimaapi.commands.VirtualKeysCommand;
import net.onima.onimaapi.commands.WorldChangerCommand;
import net.onima.onimaapi.commands.balance.BalanceExecutor;
import net.onima.onimaapi.commands.chat.ChatExecutor;
import net.onima.onimaapi.commands.cooldown.CooldownExecutor;
import net.onima.onimaapi.commands.crate.CrateExecutor;
import net.onima.onimaapi.commands.essentials.ClearCommand;
import net.onima.onimaapi.commands.essentials.CraftCommand;
import net.onima.onimaapi.commands.essentials.EnchantCommand;
import net.onima.onimaapi.commands.essentials.FlyCommand;
import net.onima.onimaapi.commands.essentials.GmcCommand;
import net.onima.onimaapi.commands.essentials.ReloreCommand;
import net.onima.onimaapi.commands.essentials.RenameCommand;
import net.onima.onimaapi.commands.essentials.SkullCommand;
import net.onima.onimaapi.commands.essentials.SudoCommand;
import net.onima.onimaapi.commands.essentials.TeleportAllCommand;
import net.onima.onimaapi.commands.essentials.TeleportHereCommand;
import net.onima.onimaapi.commands.essentials.TopCommand;
import net.onima.onimaapi.commands.essentials.WhoisCommand;
import net.onima.onimaapi.commands.essentials.WorldCommand;
import net.onima.onimaapi.commands.ignore.IgnoreExecutor;
import net.onima.onimaapi.commands.mountain.MountainExecutor;
import net.onima.onimaapi.commands.note.NoteExecutor;
import net.onima.onimaapi.commands.punishment.BanCommand;
import net.onima.onimaapi.commands.punishment.BlacklistCommand;
import net.onima.onimaapi.commands.punishment.KickCommand;
import net.onima.onimaapi.commands.punishment.MuteCommand;
import net.onima.onimaapi.commands.punishment.TempbanCommand;
import net.onima.onimaapi.commands.punishment.WarnCommand;
import net.onima.onimaapi.commands.punishment.remover.UnbanCommand;
import net.onima.onimaapi.commands.punishment.remover.UnblacklistCommand;
import net.onima.onimaapi.commands.punishment.remover.UnmuteCommand;
import net.onima.onimaapi.commands.rank.RankExecutor;
import net.onima.onimaapi.commands.region.RegionExecutor;
import net.onima.onimaapi.commands.supplycrate.SupplyCrateExecutor;
import net.onima.onimaapi.commands.warp.WarpExecutor;

public class CommandManager {
	
	private OnimaAPI plugin;
	private BalanceExecutor balanceExecutor;
	
	public CommandManager(OnimaAPI plugin) {
		this.plugin = plugin;
	}
	
	
	public void registerCommands() {
		plugin.getCommand("oniwand").setExecutor(new OniwandCommand());
		plugin.getCommand("rank").setExecutor(new RankExecutor());
		plugin.getCommand("menu").setExecutor(new MenuCommand());
		plugin.getCommand("mod").setExecutor(new ModCommand());
		plugin.getCommand("chat").setExecutor(new ChatExecutor());
		plugin.getCommand("mountain").setExecutor(new MountainExecutor());
		plugin.getCommand("region").setExecutor(new RegionExecutor());
		plugin.getCommand("cooldown").setExecutor(new CooldownExecutor());
		plugin.getCommand("message").setExecutor(new MessageCommand());
		plugin.getCommand("socialspy").setExecutor(new SocialSpyCommand());
		plugin.getCommand("reply").setExecutor(new ReplyCommand());
		plugin.getCommand("crate").setExecutor(new CrateExecutor());
		plugin.getCommand("virtualkeys").setExecutor(new VirtualKeysCommand());
		plugin.getCommand("condense").setExecutor(new CondenseCommand());
		plugin.getCommand("freeze").setExecutor(new FreezeCommand());
		plugin.getCommand("spawner").setExecutor(new SpawnerCommand());
		plugin.getCommand("crowbar").setExecutor(new CrowbarCommand());
		plugin.getCommand("lag").setExecutor(new LagCommand());
		plugin.getCommand("invsee").setExecutor(new InvseeCommand());
		plugin.getCommand("restore").setExecutor(new RestoreCommand());
		plugin.getCommand("invrestored").setExecutor(new InvrestoredCommand());
		plugin.getCommand("coords").setExecutor(new CoordsCommand());
		plugin.getCommand("vanish").setExecutor(new VanishCommand());
		plugin.getCommand("balance").setExecutor(balanceExecutor = new BalanceExecutor());
		plugin.getCommand("pay").setExecutor(new PayCommand());
		plugin.getCommand("whois").setExecutor(new WhoisCommand());
		plugin.getCommand("craft").setExecutor(new CraftCommand());
		plugin.getCommand("rename").setExecutor(new RenameCommand());
		plugin.getCommand("relore").setExecutor(new ReloreCommand());
		plugin.getCommand("enchant").setExecutor(new EnchantCommand());
		plugin.getCommand("worldchanger").setExecutor(new WorldChangerCommand());
		plugin.getCommand("world").setExecutor(new WorldCommand());
		plugin.getCommand("fly").setExecutor(new FlyCommand());
		plugin.getCommand("supplycrate").setExecutor(new SupplyCrateExecutor());
		plugin.getCommand("ban").setExecutor(new BanCommand());
		plugin.getCommand("blacklist").setExecutor(new BlacklistCommand());
		plugin.getCommand("kick").setExecutor(new KickCommand());
		plugin.getCommand("mute").setExecutor(new MuteCommand());
		plugin.getCommand("tempban").setExecutor(new TempbanCommand());
		plugin.getCommand("warn").setExecutor(new WarnCommand());
		plugin.getCommand("warp").setExecutor(new WarpExecutor());
		plugin.getCommand("top").setExecutor(new TopCommand());
		plugin.getCommand("punishment").setExecutor(new PunishmentCommand());
		plugin.getCommand("unban").setExecutor(new UnbanCommand());
		plugin.getCommand("unblacklist").setExecutor(new UnblacklistCommand());
		plugin.getCommand("unmute").setExecutor(new UnmuteCommand());
		plugin.getCommand("ores").setExecutor(new OresCommand());
		plugin.getCommand("sudo").setExecutor(new SudoCommand());
		plugin.getCommand("gmc").setExecutor(new GmcCommand());
		plugin.getCommand("connectionlogs").setExecutor(new ConnectionLogsCommand());
		plugin.getCommand("alts").setExecutor(new AltsCommand());
		plugin.getCommand("note").setExecutor(new NoteExecutor());
		plugin.getCommand("iphistory").setExecutor(new IpHistoryCommand());
		plugin.getCommand("disguise").setExecutor(new DisguiseCommand());
		plugin.getCommand("tphere").setExecutor(new TeleportHereCommand());
		plugin.getCommand("tpall").setExecutor(new TeleportAllCommand());
		plugin.getCommand("skull").setExecutor(new SkullCommand());
		plugin.getCommand("clear").setExecutor(new ClearCommand());
		plugin.getCommand("ignore").setExecutor(new IgnoreExecutor());
	}


	public BalanceExecutor getBalanceExecutor() {
		return balanceExecutor;
	}

}
