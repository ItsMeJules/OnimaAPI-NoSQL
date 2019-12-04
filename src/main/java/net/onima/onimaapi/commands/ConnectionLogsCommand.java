package net.onima.onimaapi.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public class ConnectionLogsCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage usagePlayer;
	private JSONMessage usageIp;
	private JSONMessage usageBetween;
	
	{
		usagePlayer = new JSONMessage("§7/clg player <player>", "§d§oAffiche tous les logs de connexion pour ce joueur.");
		usageIp = new JSONMessage("§7/clg ip <player>", "§d§oAffiche tous les logs de connexion pour cette ip.");
		usageBetween = new JSONMessage("§7/clg date <dd-MM-yyyy:HH-mm> <dd-MM-yyyy:HH-mm>", "§d§oAffiche tous les logs de connexion dans cette tranche de date.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.CONNECTION_LOGS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (args.length < 2) {
			sendHelp(sender);
			return false;
		}

		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		VoidCallback<MongoCollection<Document>> callback = null;
		
		if (args[0].equalsIgnoreCase("player")) {
			UUID uuid = UUIDCache.getUUID(args[1]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas ! Essayez une recherche par ip.");
				return false;
			}
			
			callback = collection -> new ConnectionLogMenu(collection.find(Filters.eq("uuid", uuid.toString()))).open(apiPlayer);
			
		} else if (args[0].equalsIgnoreCase("ip"))
			callback = collection -> new ConnectionLogMenu(collection.find(Filters.eq("ip", args[1]))).open(apiPlayer);
		else if (args[0].equalsIgnoreCase("date") && args.length > 2) {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy:HH-mm");
			
			try {
				long firstDate = format.parse(args[1]).getTime();
				long secondDate = format.parse(args[2]).getTime();
				
				callback = collection -> new ConnectionLogMenu(collection.find(Filters.and(Filters.gte("time", firstDate), Filters.lte("time", secondDate)))).open(apiPlayer);
			} catch (ParseException e) {
				sender.sendMessage("§cImpossible de convertir vos dates ! Veuillez réessayer");
				return false;
			}
			
		} else {
			sendHelp(sender);
			return false;
		}
		
		OnimaMongo.executeAsync(OnimaCollection.CONNECTION_LOGS, callback, true);
		return true;
			
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.CONNECTION_LOGS_COMMAND.has(sender) && args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		if (StringUtil.startsWithIgnoreCase("ip", args[0]))
			completions.add("ip");
		
		if (StringUtil.startsWithIgnoreCase("player", args[0]))
			completions.add("player");
		
		if (StringUtil.startsWithIgnoreCase("date", args[0]))
			completions.add("date");
		
		return completions;
	}
	
	private void sendHelp(CommandSender sender) {
		sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
		usagePlayer.send(sender);
		usageIp.send(sender);
		usageBetween.send(sender);
		sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
	}
	
	private class ConnectionLogMenu extends PageMenu {
		
		private Iterable<Document> iterable;

		public ConnectionLogMenu(Iterable<Document> iterable) {
			super("lol", "§cLogs de connexion", MAX_SIZE, false);
			
			this.iterable = iterable;
		}

		@Override
		public Map<Integer, Button> getAllPagesItems() {
			HashMap<Integer, Button> map = new HashMap<>();
			Iterator<Document> iterator = iterable.iterator();
			int index = 0;
			
			while (iterator.hasNext()) {
				Document document = iterator.next();
				BetterItem item = new BetterItem(Material.BOOK, 1, 0, Methods.toFormatDate(document.getLong("time"), ConfigurationService.DATE_FORMAT_HOURS));
				UUID uuid = UUID.fromString(document.getString("uuid"));
				
				item.addLore("§6Adresse IP : §e" + document.getString("ip"));
				item.addLore("§6UUID : §e" + uuid.toString() + " §6(nom=§e" + Methods.getRealName(Bukkit.getOfflinePlayer(uuid)) + "§6)");
				item.addLore("§6Résultat de connexion : " + document.getString("connection_result"));
				
				String kickMessage = document.getString("result_reason");
				
				if (kickMessage != null && !kickMessage.isEmpty()) {
					item.addLore("§6Message de kick : ");
					
					for (String str : kickMessage.split("\n"))
						item.addLore(str);
				}
				
				map.put(index, new DisplayButton(item));
				index++;
			}
			
			return map;
		}

		@Override
		public int getMaxItemsPerPage() {
			return 52;
		}

		@Override
		public PageMenu getPage(int page) {
			ConnectionLogMenu menu = new ConnectionLogMenu(iterable);
			menu.currentPage = page;
			
			return menu;
		}

		@Override
		public boolean changePage(APIPlayer apiPlayer, int toAdd) {
			ConnectionLogMenu menu = new ConnectionLogMenu(iterable);
			menu.currentPage += currentPage;
			
			menu.open(apiPlayer);
			return true;
		}

		@Override
		public boolean openPage(APIPlayer apiPlayer, int page) {
			ConnectionLogMenu menu = new ConnectionLogMenu(iterable);
			menu.currentPage = currentPage;
			
			menu.open(apiPlayer);
			return true;
		}
		
	}

}
