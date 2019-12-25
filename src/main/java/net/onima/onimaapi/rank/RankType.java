package net.onima.onimaapi.rank;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.time.Time;

public enum RankType { //TODO Classification rank en json. Liste de permission pour les ranks. Quand on passe la souris sur le rank ca affiche les perms et si on clique ???
	
	//Ranks sorted from bigger to smaller.
	
	CONSOLE("", "", 1000000, Integer.MAX_VALUE, ChatColor.DARK_RED, ChatColor.DARK_RED) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return null;
		}
		
		@Override
		public String getDescription() {
			return null;
		}
		
	},
	
	OWNER("§4Owner", "§f[§4Owner§f]§r", 100, Integer.MAX_VALUE , ChatColor.DARK_RED, ChatColor.DARK_RED) {
	
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                           §f[§4Owner§f]§r\n\n"
					+ ""
					+ "§fA l'origine du serveur, il a tous les droits et a accès à la console\n"
					+ "§fIl peut faire ce qu'il souhaite dans l'impunité la plus totale.\n"
					+ "Lui demander de l'aide pour des choses inutiles peut résoudre à un mute.\n\n"
					+ ""
					+ "Il est aussi connecté pour profiter de son serveur\n"
					+ "tout comme n'importe quel joueur. Donc s'il ne répond pas\n"
					+ "il est grandement inutile de l'harceler.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	HEAD_ADMIN("§cHead-Admin", "§f[§cHead-Admin§f]§r", 17, Integer.MAX_VALUE, ChatColor.RED, ChatColor.RED) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                  §f[§cHead-Admin§f]§r\n\n"
					+ ""
					+ "§fA gagné la confiance totale des owners par son travail fourni\n"
					+ "sa bienveillance. Sa position n'est en aucun cas à remettre\n"
					+ "en question. Il détient aussi toutes les permissions mais a\n"
					+ "un accès restreint à la console.\n\n"
					+ ""
					+ "Il est connecté pour s'assurer que tout se passe bien\n"
					+ "sur le serveur et dans le staff. Donc s'il ne répond pas\n"
					+ "il est grandement inutile de l'harceler.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	ADMIN("§cAdmin", "§8[§cAdmin§8]§r", 16, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                             §8[§cAdmin§8]§r\n\n"
					+ ""
					+ "§fA gagné la confiance des owners et des head-admins par son travail fourni\n"
					+ "et sa bienveillance. Sa position n'est en aucun cas à remettre\n"
					+ "en question. Il détient aussi toutes les permissions mais n'a\n"
					+ "aucn accès à la console.\n\n"
					+ ""
					+ "Il est connecté pour s'assurer que tout se passe bien\n"
					+ "sur le serveur, dans le staff et pour régler des problèmes §nmajeurs§r.\n"
					+ "Donc s'il ne répond pas il est grandement inutile de l'harceler.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	CHEF_DEVELOPPEUR("§9Chef-Dév", "§8[§9Chef-Dév§8]§r", 15, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {

		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                                  §8[§9Chef-Dév§8]§r\n\n"
					+ ""
					+ "§fResponsable du développement des plugins et de la stabilité de ces derniers.\n"
					+ "Il a des connaisances solides en Java, SQL, Redis... Il s'occupe des développeurs\n"
					+ "et de leur travail. Tout ce que les devs font est analysé et validé par lui.\n\n"
					+ ""
					+ "Si vous êtes témoin d'un bug merci de ne pas lui reporter.\n"
					+ "Executez la commande §o/bug§r et suivez les informations écrites.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	DEVELOPPEUR("§9§oDév", "§8[§9§oDév§8]§r", 14, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                                  §8[§9§oDév§8]§r\n\n"
					+ ""
					+ "§fResponsable du développement de certains plugins et de la stabilité de ces derniers.\n"
					+ "Il a des connaisances solides en Java. Il s'occupe de ce que le chef dév lui ordonne\n"
					+ "et le code au maximum de ses capacités. Tout son travail est important car il\n"
					+ "permet de garder le serveur à jour et de régler les bugs existants\n\n"
					+ ""
					+ "Si vous êtes témoin d'un bug merci de ne pas lui reporter.\n"
					+ "Executez la commande §o/bug§r et suivez les informations écrites.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	BUILDER("§bBuilder", "§8[§bBuilder§8]§r", 13, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                                  §8[§bBuilder§8]§r\n\n"
					+ ""
					+ "§fResponsable des builds (Spawn, Events, WarZone...). Il travaille en équipe\n"
					+ "pour un résultat parfait et rapide. Ses connaissances excelle en architecture\n"
					+ "HCF pour fournir une qualité et une expérience de jeu supérieure.\n\n"
					+ ""
					+ "Son rôle n'est pas de s'occuper des joueurs ou de leurs problèmes,\n"
					+ "il peut tout de même accepter ou refuser vos propositions de builds.\n"
					+ "Si vous êtes témoins d'un problème dans des sur son travail, il est\n"
					+ "fortement recommandé de lui signaler. S'il juge le signalement important\n"
					+ "un administrateur sera dans la capacité de vous récompenser.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	MOD("§aMod", "§8[§aMod§8]§r", 11, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                       §8[§aMod§8]§r\n\n"
					+ ""
					+ "§fResponsable de la communauté du serveur, il s'assure\n"
					+ "qu'il n'y ait aucun soucis majeur chez les joueurs (cheat, \n"
					+ "comportement, langage, malhônnêteté...).\n\n"
					+ ""
					+ "Son rôle est de s'occuper des joueurs ou de leurs problèmes.\n"
					+ "Si vous avez une question, merci de s'adresser à lui\n"
					+ "directement. Il sera capable de vous aider et diriger\n"
					+ "au mieux possible. Vous pouvez aussi lui signaler un\n"
					+ "problème quelquoncque et il sera en mesure de le régler.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	TRIAL_MOD("§a§oTrial-Mod", "§8[§a§oTrial-Mod§8]§r", 10, Time.WEEK, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                            §8[§a§oTrial-Mod§8]§r\n\n"
					+ ""
					+ "§fExactement le même rôle qu'un modérateur, avec\n"
					+ "avec quelques permissions en moins. Il est ici pour\n"
					+ "apprendre à modérer sur le serveur Onima.\n\n"
					+ ""
					+ "Sa période d'essai dure une semaine.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	FAMOUS("§d§oFamous", "§8[§d§oFamous§8]§r", 9, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                         §8[§dFamous§8]§r\n\n"
					+ ""
					+ "§fYoutuber qui permet de gérer la pub sur le serveur Onima.\n"
					+ "Il met en ligne au moins 2 vidéos/semaine concernant le\n"
					+ "serveur. Ces dernières comptabilise ~§o1000vues§r pour être\n"
					+ "éligible à ce grade qui donne des permissions spéciales.\n\n"
					+ ""
					+ "Son rôle est très important pour Onima, sa parole et sa\n"
					+ "présence sont donc priorisé comparé à un joueur.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	YOUTUBE("§f§lYoutube", "§8[§f§lYouTube§8]§r", 8, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                    §8[§f§lYouTube§8]§r\n\n"
					+ ""
					+ "§fYoutuber qui permet de gérer la pub sur le serveur Onima.\n"
					+ "Il met en ligne au moins 2 vidéos/semaine concernant le\n"
					+ "serveur. Ces dernières comptabilise ~§o500vues§r pour être\n"
					+ "éligible à ce grade qui donne des permissions spéciales.\n\n"
					+ ""
					+ "Son rôle est très important pour Onima, sa\n"
					+ "présence sont donc priorisé comparé à un joueur.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	FRIEND("§eAmi", "§8[§eAmi§8]§r", 7, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                        §8[§eAmi§8]§r\n\n"
					+ ""
					+ "§fAmi d'un des fondateurs/head-admin.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	SHOGUN("§6Shogun", "§8[§6Shogun§8]§r", 6, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                              §8[§3Shogun§8]§r\n\n"
					+ ""
					+ "§fPlus haut rank du serveur, il a accès à toutes\n"
					+ "sortes de permissions respectant la limite du P2W.\n\n"
					+ ""
					+ "Vu de son financement pour le serveur, il est très\n"
					+ "apprécié. L'achat de son rang nous permet de faire\n"
					+ "perdurer le serveur et de payer les équipes de staff.\n\n"
					+ ""
					+ "Pour remercier de l'achat la durée de ce rang est à vie.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	KACHI("§5Kachi", "§8[§5Kachi§8]§r", 5, Integer.MAX_VALUE, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                   §8[§5Kachi§8]§r\n\n"
					+ ""
					+ "§fSecond plus haut rank du serveur, il a accès à certaines\n"
					+ "sortes de permissions respectant la limite du P2W.\n\n"
					+ ""
					+ "Vu de son financement pour le serveur, il est très\n"
					+ "apprécié. L'achat de son rang nous permet de contribuer\n"
					+ "au paiement de la machine et du staff.\n\n"
					+ ""
					+ "Pour remercier de l'achat la durée de ce rang est à vie.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	KOMONO("§2Komono", "§8[§2Komono§8]§r", 4, Time.MONTH, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                                       §8[§2Komono§8]§r\n\n"
					+ ""
					+ "§fTroisième plus haut rank du serveur, il a accès à certaines\n"
					+ "sortes de permissions respectant la limite du P2W.\n\n"
					+ ""
					+ "Vu de son financement pour le serveur, il est\n"
					+ "apprécié. L'achat de son rang contribue au paiement de\n"
					+ "la machine qui permet une stabilité optimale.\n\n"
					+ ""
					+ "Pour remercier de l'achat la durée de ce rang est de\n"
					+ "1 mois.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	RONIN("§3Ronin", "§8[§3Ronin§8]§r", 3, 2 * Time.WEEK, ChatColor.WHITE, ChatColor.WHITE) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                               §8[§3Ronin§8]§r\n\n"
					+ ""
					+ "§fDeuxième rank du serveur, il a accès à certaines\n"
					+ "sortes de permissions respectant la limite du P2W.\n\n"
					+ ""
					+ "Vu de son financement pour le serveur, il est\n"
					+ "apprécié. L'achat de son rang nous permet de\n"
					+ "payer quelques membres du staff pour pouvoir\n"
					+ "améliorer le serveur.\n\n"
					+ ""
					+ "Pour remercier de l'achat la durée de ce rang est de\n"
					+ "2 semaines.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},

	NINJA("§7§lNinja", "§8[§7§lNinja§8]§r", 2, 2 * Time.WEEK, ChatColor.WHITE, ChatColor.GRAY) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                             §8[§6Ninja§8]§r\n\n"
					+ ""
					+ "§fPremier rank du serveur, il a accès à certaines\n"
					+ "sortes de permissions respectant la limite du P2W.\n\n"
					+ ""
					+ "Vu de son financement pour le serveur, il est\n"
					+ "apprécié. L'achat de son rang nous permet de\n"
					+ "payer un membre du staff pour l'aide qu'il apporte\n\n"
					+ ""
					+ "Pour remercier de l'achat la durée de ce rang est de\n"
					+ "2 semaines.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	DEFAULT("§7Défaut", "", 1, Integer.MAX_VALUE, ChatColor.GRAY, ChatColor.GRAY) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return Lists.newArrayList(OnimaPerm.ALTS_COMMAND, OnimaPerm.ONIMAFACTION_FACTION_COMMAND, OnimaPerm.SOTW_COMMAND);
		}
		
		@Override
		public String getDescription() {
			return "§fRang de base sur le serveur Onima.\n"
					+ "avec ce dernier, vous mettrez plus de temps\n"
					+ "à vous lancer dans le bain du HCF.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	},
	
	BOT("Bot", "§8[BOT§8]§r", 0, Integer.MAX_VALUE, ChatColor.DARK_GRAY, ChatColor.DARK_GRAY) {
		
		@Override
		public List<OnimaPerm> getPermissions() {
			return ImmutableList.copyOf(OnimaPerm.values());
		}
		
		@Override
		public String getDescription() {
			return "                          §8[BOT§8]§r\n\n"
					+ ""
					+ "Dénué d'intelligence il est a mépriser.\n"
					+ "Dès qu'il parle le roast est encouragé et ne\n"
					+ "sera pas sanctionné.\n\n\n"
					+ ""
					+ ""
					+ "§7§oPour plus d'informations cliquez sur ce message.";
		}
		
	};
	
	private String name, prefix;
	private JSONMessage denyMsg;
	private int value;
	private long time;
	private ChatColor speakingColor, nameColor;

	private RankType(String name, String prefix, int value, long time, ChatColor nameColor, ChatColor speakingColor) {
		this.name = name;
		this.prefix = prefix;
		this.denyMsg = new JSONMessage("§cVous avez besoin du rang " + name + " pour pouvoir faire ceci.", getDescription(), true, "/menu ranks");
		this.value = value;
		this.time = time;
		this.nameColor = nameColor;
		this.speakingColor = speakingColor;
	}
	
	public abstract List<OnimaPerm> getPermissions();
	public abstract String getDescription();
	
	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public JSONMessage getDenyMsg() {
		return denyMsg;
	}

	public int getValue() {
		return value;
	}

	public long getTime() {
		return time;
	}

	public ChatColor getNameColor() {
		return nameColor;
	}
	
	public ChatColor getSpeakingColor() {
		return speakingColor;
	}
	
	public boolean isAtLeast(RankType rank) {
		return value >= rank.value;
	}
	
	public boolean isAtMost(RankType rank) {
		return value <= rank.value;
	}
	
	public boolean hasPermisssion(OnimaPerm perm) {
		return perm == null ? true : getPermissions().contains(perm);
	}
	
	public static RankType fromString(String name) {
		switch (name) {
		case "OWNER":
			return OWNER;
		case "HEAD_ADMIN":
			return HEAD_ADMIN;
		case "ADMIN":
		    return ADMIN;
		case "CHEF_DEVELOPPEUR":
		    return CHEF_DEVELOPPEUR;
		case "DEVELOPPEUR":
		    return DEVELOPPEUR;
		case "BUILDER":
		    return BUILDER;
		case "MOD":
		    return MOD;
		case "TRIAL_MOD":
		    return TRIAL_MOD;
		case "FAMOUS":
		    return FAMOUS;
		case "YOUTUBE":
		    return YOUTUBE;
		case "FRIEND":
		    return FRIEND;
		case "SHOGUN":
		    return SHOGUN;
		case "KACHI":
		    return KACHI;
		case "KOMONO":
		    return KOMONO;
		case "RONIN":
		    return RONIN;
		case "NINJA":
		    return NINJA;
		case "DEFAULT":
		    return DEFAULT;
		case "BOT":
		    return BOT;
		default:
			return null;
		}
	}

	public static RankType getRank(CommandSender receiver) {
		if (receiver instanceof ConsoleCommandSender)
			return CONSOLE;
		else
			return APIPlayer.getPlayer((Player) receiver).getRank().getRankType();
	}

}
