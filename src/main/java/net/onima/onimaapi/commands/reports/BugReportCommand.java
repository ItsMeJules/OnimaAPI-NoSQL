package net.onima.onimaapi.commands.reports;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class BugReportCommand implements CommandExecutor {
	
	/*
	 *Suite de gui:
	 *
	 *1er menu : Explique les étapes de report de bug.
	 * 1 bouton pour passer
	 * 1 bouton avec 1ere étape decrivez le bug -> panneau ou livre
	 * 1 bouton avec 2eme étape que faisiez-vous -> panneau ou livre
	 * 1 bouton avec 3eme étape quelle heure exacte était-il lors que ce bug a eu lieu -> panneau ou livre ou enclume
	 * 1 bouton avec 4ème étape ajoutez un lien vers une photo/vidéo montrant le bug si vous pouvez. -> livre
	 * 1 bouton avec 5ème étape attendez quelque temps et vous saurez l'état de votre signalement (en attente, en cours d'investigation, validé/refusé). -> gui avec tous les reports de bug
	 * 1 bouton avec 6ème étape récompense ! -> gui avec la récompense.
	 *
	 *Ouvre un gui avec un bouton: Decrivez le bug
	 *ouvre un panneau ou un livre dans lequel le joueur pourra écrire.
	 * 
	 */

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.BUG_REPORT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer reporter = APIPlayer.getPlayer((Player) sender);
		
		return true;
	}
	
}
