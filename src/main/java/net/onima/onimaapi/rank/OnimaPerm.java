package net.onima.onimaapi.rank;

import org.bukkit.permissions.Permissible;

public enum OnimaPerm {
	
	/**
	 * Switching gamemode (survival, creative, adventure).
	 */
	CORE_GAMEMODE("onimacore.core.gamemode", "Permet de changer de gamemode."),
	
	/**
	 * Edits the reward menu for a game.
	 */
	GAME_REWARD_MENU_EDIT("onimaapi.game.reward-menu-edit", "Permet d'edit le menu des récompenses."),
	
	/**
	 * Stops a running game.
	 */
	GAME_STOP_ARGUMENT("onimagame.game.stop", "Permet d'arrêter un event en cours."),
	
	/**
	 * Creates a new game.
	 */
	GAME_CREATE_ARGUMENT("onimagame.game.create", "Permet de créer un nouvel event."),
	
	/**
	 * Removes a created game.
	 */
	GAME_REMOVE_ARGUMENT("onimagame.game.remove", "Permer de supprimer un event."),
	
	/**
	 * Rename a created game.
	 */
	GAME_NAME_ARGUMENT("onimagame.game.name", "Permet de renommer un event existant."),
	
	/**
	 * Sets the area of a game.
	 */
	GAME_AREA_ARGUMENT("onimagame.game.area", "Permet de définir l'arène d'un event."),
	
	/**
	 * Starts a game.
	 */
	GAME_START_ARGUMENT("onimagame.game.start", "Permet de lancer un event."),
	
	/**
	 * Checks the up time of an event.
	 */
	GAME_UPTIME_ARGUMENT("onimagame.game.uptime", "Permet d'afficher la durée d'un event."),
	
	/**
	 * Displays all the scheduled games.
	 */
	GAME_NEXT_ARGUMENT("onimagame.game.next", "Permet d'afficher la programmation des events."),
	
	/**
	 * Allows to show who's capping the event on show
	 */
	GAME_SHOW_MOD("onimagame.game.show-mod", "Permet d'afficher toutes les informations lors d'un /game show."),
	
	/**
	 * Displays all useful informations of a game.
	 */
	GAME_SHOW_ARGUMENT("onimagame.game.show", "Permet de voir les infos d'un event."),
	
	/**
	 * Displays the game help.
	 */
	GAME_HELP_ARGUMENT("onimagame.game.help", "Permet d'afficher l'aide pour les events"),
	
	/**
	 * Schedules a game.
	 */
	GAME_SCHEDULE_ARGUMENT("onimagame.game.schedule", "Permet de programmer un event."),
	
	/**
	 * Defines the first time the game will start.
	 */
	GAME_FIRSTSCHEDULE_ARGUMENT("onimagame.game.firstschedule", "Permet de définir le jour ou l'event commencera pour la première fois."),
	
	/**
	 * Enable/Disable a game's scheduler.
	 */
	GAME_ENABLESCHEDULER_ARGUMENT("onimagame.game.enablescheduler", "Permet d'activer/désactiver le scheduler d'un event."),
	
	/**
	 * Shows the list of the games.
	 */
	GAME_LIST_ARGUMENT("onimagame.game.list", "Permet d'afficher la liste des event."),
	
	/**
	 * Sets/unset the koth capzone
	 */
	KOTH_CAPZONE_ARGUMENT("onimagame.koth.capzone", "Permet de définir la cap zone d'un koth."),
	
	/**
	 * Sets the cap time for the koth.
	 */
	KOTH_SETCAPTIME_ARGUMENT("onimagame.koth.setcaptime", "Permet de définir le temps de cap d'un koth."),
	
	/**
	 * Sets the cap zone for the conquest.
	 */
	CONQUEST_CAPZONE_ARGUMENT("onimagame.conquest.capzone", "Permet de définir la cap zone d'une conquest."),
	
	/**
	 * Sets the cap time for a conquest.
	 */
	CONQUEST_SETCAPTIME_ARGUMENT("onimagame.conquest.setcaptime", "Permet de définir le temps de cap d'une zone de conquest."),
	
	/**
	 * Sets the points per cap for a conquest
	 */
	CONQUEST_SETPOINTSPERCAP_ARGUMENT("onimagame.conquest.setpointspercap", "Permet de définir le nombre de points de cap pour une zone de conquest."),
	
	/**
	 * Sets the points to win a conquest.
	 */
	CONQUEST_SETPOINTSTOWIN_ARGUMENT("onimagame.conquest.setpointstowin", "Permet de définir le nombre de points nécessaire pour gagner une conquest."),
	
	/**
	 * Sets/unset the citadel capzone
	 */
	CITADEL_CAPZONE_ARGUMENT("onimagame.citadel.capzone", "Permet de définir la cap zone d'une citadel."),
	
	/**
	 * Sets the cap time for the citadel.
	 */
	CITADEL_SETCAPTIME_ARGUMENT("onimagame.citadel.setcaptime", "Permet de définir le temps de cap d'une citadel."),

	/**
	 * Sets the block of a DTC.
	 */
	DTC_SETBLOCK_ARGUMENT("onimagame.dtc.setblock", "Permet de définir le block d'un DTC."),
	
	/**
	 * Sets the amount needed to break the DTC.
	 */
	DTC_SETPOINTS_ARGUMENTS("onimagame.dtc.setpoints", "Permet de définir le nombre de fois qu'il faut casser un DTC."),
	
	/**
	 * Manages the dragon's effects.
	 */
	DRAGON_EFFECT_ARGUMENT("onimagame.dragon.effects", "Permet de gérer les effets du dragon"),
	
	/**
	 * Sets the dragon's health whether before or after spawning.
	 */
	DRAGON_HEALTH_ARGUMENT("onimagame.dragon.health", "Permet de définir la vie du dragon avant ou après avoir spawn"),
	
	/**
	 * Sets the boss bar name
	 */
	DRAGON_NAME_ARGUMENT("onimagame.dragon.name", "Permet de définir le nom du dragon dans la boss bar"),
	
	/**
	 * Sets the dragon spawn location
	 */
	DRAGON_SPAWNLOCATION_ARGUMENT("onimagame.dragon.argument", "Permet de définir la location du spawn du dragon"),

	/**
	 * Ability to use and give a wand.
	 */
	ONIMAAPI_WAND("onimaapi.wand.command", "Permet de se give une wand"),
	
	/**
	 * Ability to display the faction help.
	 */
	ONIMAFACTION_HELP_ARGUMENT("onimafaction.help.argument", "Permet d'afficher le faction help"),
	
	/**
	 * Ability to create a faction.
	 */
	ONIMAFACTION_CREATE_ARGUMENT("onimafaction.create.argument", "Permet de créer une faction"),
	
	/**
	 * Ability to invite a player.
	 */
	ONIMAFACTION_INVITE_ARGUMENT("onimafaction.invite.argument", "Permet d'inviter un joueur dans sa faction."), 
	
	/**
	 * Ability to go past the border.
	 */
	WORLD_BORDER_BYPASS("onimaapi.world-border.bypass", "Permet d'aller au-delà de la border."),
	
	/**
	 * Ability to change of tab.
	 */
	ONIMABOARD_TAB_COMMAND("onimaboard.tab.command", "Permet de changer de tab"),
	
	/**
	 * Ability to join a faction.
	 */
	ONIMAFACTION_JOIN_ARGUMENT("onimafaction.join.argument", "Permet de rejoindre une faction."),
	
	/**
	 * Ability to change of chat.
	 */
	ONIMAFACTION_CHAT_ARGUMENT("onimafaction.chat.argument", "Permet de changer de chat."),
	
	/**
	 * Ability to kick a player.
	 */
	ONIMAFACTION_KICK_ARGUMENT("onimafaction.kick.argument", "Permet de kick un joueur de la faction"),

	/**
	 * Ability to get the claiming wand.
	 */
	ONIMAFACTION_CLAIM_ARGUMENT("onimafaction.claim.argument", "Permet de claim un territoire."),
	
	/**
	 * Ability to ally with a faction.
	 */
	ONIMAFACTION_ALLIANCE_ARGUMENT("onimafaction.ally.argument", "Permet de s'allier à une faction"),

	/**
	 * Ability to unally with a faction.
	 */
	ONIMAFACTION_UNALLIANCE_ARGUMENT("onimafaction.unally.argument", "Permet d'enlever l'alliance avec une faction"),

	/**
	 * Ability to disband a faction
	 */
	ONIMAFACTION_DISBAND_ARGUMENT("onimafaction.disband.argument", "Permet de dissoudre une faction"),
	
	/**
	 * Ability to leave a faction
	 */
	ONIMAFACTION_LEAVE_ARGUMENT("onimafaction.leave.argument", "Permet de quitter une faction"),
	
	/**
	 * Ability to set a leader.
	 */
	ONIMAFACTION_LEADER_ARGUMENT("onimafaction.leader.argument", "Permet de définir le leader d'une faction"), 
	
	/**
	 * Ability to display the faction list.
	 */
	ONIMAFACTION_LIST_ARGUMENT("onimafaction.list.argument", "Permet d'afficher la liste des factions"),
	
	/**
	 * Ability to claim a chunk
	 */
	ONIMAFACTION_CLAIMCHUNK_ARGUMENT("onimafaction.claimchunk.argument", "Permet de claim un chunk"),
	
	/**
	 * Ability to open your faction.
	 */
	ONIMAFACTION_OPEN_ARGUMENT("onimafaction.open.argument", "Permet d'ouvrir une faction"),
	
	/**
	 * Ability to set your faction's home.
	 */
	ONIMAFACTION_SETHOME_ARGUMENT("onimafaction.sethome.argument", "Permet de définir l'home d'une faction"),
	
	/**
	 * Ability to teleport to the faction's home.
	 */
	ONIMAFACTION_HOME_ARGUMENT("onimafaction.home.argument", "Permet de se téléporter au home d'une faction"),
	
	/**
	 * Ability to show details about a faction.
	 */
	ONIMAFACTION_SHOW_ARGUMENT("onimafaction.show.argument", "Permet d'afficher les détails d'une fation"),
	
	/**
	 * Ability to promote someone as officer.
	 */
	ONIMAFACTION_PROMOTE_ARGUMENT("onimafaction.promote.argument", "Permet de promote un joueur dans une faction"),
	
	/**
	 * Ability to demote a player in the faction.
	 */
	ONIMAFACTION_DEMOTE_ARGUMENT("onimafaction.demote.argument", "Permet de demote un joueur dans une faction"),
	
	/**
	 * Ability to deinvite a player from the faction.
	 */
	ONIMAFACTION_DEINVITE_ARGUMENT("onimafaction.deinvite.argument", "Permet de supprimer l'invitation d'un joueur dans une faction"),
	
	/**
	 * Ability to open a gui to manage the faction's commands.
	 */
	ONIMAFACTION_GUI_ARGUMENT("onimafaction.gui.argument", "Permet d'ouvrir un gui pour gérer la faction."),

	/**
	 * Ability to deposit an amount of money.
	 */
	ONIMAFACTION_DEPOSIT_ARGUMENT("onimafaction.deposit.argument", "Permet de déposer de l'argent dans la banque de la faction."),
	
	/**
	 * Ability to unclaim a land.
	 */
	ONIMAFACTION_UNCLAIM_ARGUMENT("onimafaction.unclaim.argument", "Permet de déposer d'unclaim un territoire."),
	
	/**
	 * Ability to f stuck.
	 */
	ONIMAFACTION_STUCK_ARGUMENT("onimafaction.stuck.argument", "Permet de se téléporter dans un endroit sauf."),
	
	/**
	 * Ability to see who invited you/which players are invited.
	 */
	ONIMAFACTION_INVITES_ARGUMENT("onimafaction.invites.argument", "Permet d'afficher toutes les invitations."),
	
	/**
	 * Ability to withdraw money from your faction.
	 */
	ONIMAFACTION_WITHDRAW_ARGUMENT("onimafaction.withdraw.argument", "Permet de retirer de l'argent de la faction."),
	
	/**
	 * Ability to see the claims surrounding you.
	 */
	ONIMAFACTION_MAP_ARGUMENT("onimafaction.map.argument", "Permet de voir les claims autour."),
	
	/**
	 * Ability to spy some faction chat.
	 */
	ONIMAFACTION_CHATSPY_ARGUMENT("onimafaction.chatspy.argument", "Permet d'espionner les chats de faction."),
	
	/**
	 * Ability to modify a faction dtr.
	 */
	ONIMAFACTION_DTR_ARGUMENT("onimafaction.dtr.argument", "Permet de changer le dtr d'une faction."),
	
	/**
	 * Ability to set if you lose dtr in a faction.
	 */
	ONIMAFACTION_DTRLOSS_ARGUMENT("onimafaction.dtrloss.argument", "Permet de changer si l'on perd ou non du dtr quand on meurt dans une faction."),
	
	/**
	 * Ability to set whether yes or no a deathbannable faction.
	 */
	ONIMAFACTION_DEATHBAN_ARGUMENT("onimafaction.deathban.argument", "Permet de définir si une faction est deathbannable."),
	
	/**
	 * Ability to forcedisband a faction.
	 */
	ONIMAFACTION_FORCEDISBAND_ARGUMENT("onimafaction.forcedisband.argument", "Permet de dissoudre de force une faction."),
	
	/**
	 * Ability to forcejoin a faction.
	 */
	ONIMAFACTION_FORCEJOIN_ARGUMENT("onimafaction.forcejoin.argument", "Permet de rejoindre de force une faction."),
	
	/**
	 * Ability to set someone co leader of the faction.
	 */
	ONIMAFACTION_COLEADER_ARGUMENT("onimafaction.coleader.argument", "Permet de définir un co leader de la faction."),
	
	/**
	 * Ability to force set a leader in a faction.
	 */
	ONIMAFACTION_FORCELEADER_ARGUMENT("onimafaction.forceleader.argument", "Permet de définir de force un leader d'une faction"),
	
	/**
	 * Ability to force set an officer in a faction.
	 */
	ONIMAFACTION_FORCEOFFICER_ARGUMENT("onimafaction.forceofficer.argument", "Permet de définir de force un officier d'une faction"),
	
	/**
	 * Ability to force set a co-leader in a faction.
	 */
	ONIMAFACTION_FORCECOLEADER_ARGUMENT("onimafaction.forcecoleader.argument", "Permet de définir de force un coleader d'une faction"),
	
	/**
	 * Ability to force set a member in a faction.
	 */
	ONIMAFACTION_FORCEMEMBER_ARGUMENT("onimafaction.forcemember.argument", "Permet de définir de force un membre d'une faction"),
	
	/**
	 * Ability to give money to a faction.
	 */
	ONIMAFACTION_GIVEMONEY_ARGUMENT("onimafaction.givemoney.argument", "Permet de donner de l'argent à une faction."),
	
	/**
	 * Ability to force kick a player from a faction.
	 */
	ONIMAFACTION_FORCEKICK_ARGUMENT("onimafaction.forcekick.argument", "Permet de kick de force un joueur d'une faction."),
	
	/**
	 * Ability to claim a land for a faction.
	 */
	ONIMAFACTION_CLAIMFOR_ARGUMENT("onimafaction.claimfor.argument", "Permet de claim pour une faction."),
	
	/**
	 * Ability to forceunclaim a land for a faction.
	 */
	ONIMAFACTION_FORCEUNCLAIM_ARGUMENT("onimafaction.forceunclaim.argument", "Permet d'unclaim de force un territoire pour une faction."),
	
	/**
	 * Ability to clear all the claims of a faction
	 */
	ONIMAFACTION_CLEARCLAIMS_ARGUMENT("onimafaction.clearclaims.argument", "Permet de clear tous les claims d'une faction"),
	
	/**
	 * Ability to clear all the claims of the server.
	 */
	ONIMAFACTION_CLEARCLAIMS_ALL("onimafaction.clearclaims.all", "Permet de clear tous les claims du serveur."),
	
	/**
	 * Ability to interact in a claim.
	 */
	ONIMAFACTION_BYPASS_ARGUMENT("onimafaction.bypass.argument", "Permet d'intéragir avec l'environnement dans un claim."),
	
	/**
	 * Ability to manage the flag of a faction.
	 */
	ONIMAFACTION_FLAG_ARGUMENT("onimafaction.flag.argument", "Permet de gérer les flags d'une faction."),
	
	/**
	 * Ability to make a faction permanent.
	 */
	ONIMAFACTION_PERMANENT_ARGUMENT("onimafaction.permanent.argument", "Permet de rendre une faction permanente."),
	
	/**
	 * Ability to restrain some actions with the factions.
	 */
	ONIMAFACTION_LOCK_ARGUMENT("onimafaction.lock.argument", "Permet de vérouiller plusieurs actions avec les factions."),
	
	/**
	 * Ability to modify the deathbanmultiplier of a faction.
	 */
	ONIMAFACTION_DEATHBANMULTIPLIER_ARGUMENT("onimafaction.deathbanmultiplier.argument", "Permet de modifier le deathban multiplier d'une faction"),
	
	/**
	 * Ability to rename your faction.
	 */
	ONIMAFACTION_NAME_ARGUMENT("onimafaction.name.argument", "Permet de changer la faction de nom"),
	
	/**
	 * Ability to bypass the annoying cooldown.
	 */
	ONIMAAPI_COOLDOWN_BYPASS("onimaapi.cooldown.bypass", "Permet de bypass les coolodowns"),
	
	/**
	 * Ability to set the announcement of the faction.
	 */
	ONIMAFACTION_ANNOUNCEMENT_ARGUMENT("onimafaction.announcement.argument", "Permet de modifier l'annonce de la faction."),
	
	/**
	 * Main command for ranks.
	 */
	RANK_COMMAND("onimaapi.rank.command", "Commande principale pour les ranks"),
	
	/**
	 * Ability to set the rank of a player.
	 */
	RANK_COMMAND_SET("onimaapi.rank.command.set", "Permet de définir le rank d'un joueur."),
	
	/**
	 * Ability to focus a player.
	 */
	ONIMAFACTION_FOCUS_COMMAND("onimafaction.focus.command", "Permet de focus un joueur"),
	
	/**
	 * Ability to send your location to your faction
	 */
	ONIMAFACTION_TEAMLOCATION_COMMAND("onimafaction.teamlocation.command", "Permet d'envoyer sa location."), 
	
	/**
	 * Ability to set in vanish
	 */
	ONIMAAPI_VANISH_COMMAND("onimaapi.vanish.command", "Permet de se mettre en vanish."),
	
	/**
	 * Ability to see informations about a player's rank.
	 */
	RANK_COMMAND_INFO("onimaapi.rank.command.info", "Permet d'obtenir des informations sur le rank d'un joueur"),
	
	/**
	 * Ability to see the ranks list.
	 */
	RANK_COMMAND_LIST("onimaapi.rank.command.list", "Permet de voir la liste des ranks"),
	
	/**
	 * Ability to set a rank with a specific time of reset.
	 */
	RANK_COMMAND_TEMPSET("onimaapi.rank.command.tempset", "Permet de mettre un rank avec un temps défini."),
	
	/**
	 * Allows not to be picked up for a random teleport.
	 */
	MOD_RANDOM_TELEPORT_BYPASS("onimaapi.mod.randomteleport-byass", "Permet de ne pas être choisit lors d'une téléportation aléatoire."),
	
	/**
	 * Allows to be counted as a staff in mod mode.
	 */
	STAFF_COUNTER_COUNT("onimaapi.mod.staff-counter-count", "Permet de d'être compté en tant que staff en mod mode."),
	
	/**
	 * Allows players to open this menu
	 */
	ONIMAAPI_GUI_STAFF_MENU("onimaapi.gui.staff-menu", "Permet d'ouvrir le menu du staff."),
	
	/**
	 * Allows a player to go in moderator mode.
	 */
	MOD_COMMAND("onimaapi.mod.command", "Permet de se mettre en mod mode."),
	
	/**
	 * Allows a player to receive a warning when another player uses a forbidden potion.
	 */
	FORBIDDEN_POTION_WARN("onimaapi.forbidden-potion.warn", "Permet de recevoir une notif quand une potion interdite est utilisée."),
	
	FORBIDDEN_POTION_BYPASS("onimaapi.forbidden-potion.bypass", "Permet de bypass les potions interdite."),
	
	ONIMAAPI_CHAT_CLEAR_ARGUMENT("onimaapi.chat.clear.argument", "Permet de clear le chat"),
	
	ONIMAAPI_CHAT_MUTE_ARGUMENT("onimaapi.chat.mute.argument", "Permet de mute le chat"), 
	
	ONIMAAPI_CHAT_SLOW_ARGUMENT("onimaapi.chat.slow.argument", "Permet de ralentir le chat"),
	
	ONIMAAPI_MOUNTAIN_CREATE_ARGUMENT("onimaapi.mountain.create.argument", "Permet de créer une montagne"),
	
	ONIMAAPI_MOUNTAIN_FIRSTSCHEDULE_ARGUMENT("onimaapi.mountain.firstschedule.argument", "Permet de définir le jour où la montagne commencera pour la première fois."),
	
	ONIMAAPI_MOUNTAIN_SCHEDULE_ARGUMENT("onimaapi.mountain.schedule.argument", "Permet de programmer une montagne."),
	
	ONIMAAPI_MOUNTAIN_ENABLESCHEDULER_ARGUMENT("onimaapi.mountain.enablescheduler.argument", "Permet d'activer/désactiver le scheduler d'une montagne."),
	
	ONIMAAPI_MOUNTAIN_REGISTERBLOCKS_ARGUMENT("onimaapi.mountain.registerblocks.argument", "Permet d'enregistrer les blocks à régénérer d'une montagne."),
	
	ONIMAAPI_MOUNTAIN_REMOVE_ARGUMENT("onimaapi.mountain.remove.argument", "Permet de supprimer une montagne."),
	
	ONIMAAPI_MOUNTAIN_SHOW_ARGUMENT("onimaapi.mountain.show.argument", "Permet d'afficher les informations d'une montagne"),
	
	ONIMAAPI_MOUNTAIN_LIST_ARGUMENT("onimaapi.mountain.list.argument", "Permet d'afficher la liste des montagnes"),
	
	ONIMAAPI_MOUNTAIN_NAME_ARGUMENT("onimaapi.mountain.name.argument", "Permet de changer le nom d'une montagne"),
	
	ONIMAAPI_MOUNTAIN_LOCATION_ARGUMENT("onimaapi.mountain.location.argument", "Permet de redéfinir la location d'une montagne"), 
	
	ONIMAAPI_REGION_CREATE_ARGUMENT("onimaapi.region.create.argument", "Permet de créer une région"),
	
	ONIMAAPI_REGION_REMOVE_ARGUMENT("onimaapi.region.remove.argument", "Permet de supprimer une région"),
	
	ONIMAAPI_REGION_DEATHBAN_ARGUMENT("onimaapi.region.deathban.argument", "Permet d'activer/désactiver le deathban une région"),
	
	ONIMAAPI_REGION_DTRLOSS_ARGUMENT("onimaapi.region.dtrloss.argument", "Permet d'activer ou non la perte de DTR"),
	
	ONIMAAPI_REGION_PRIORITY_ARGUMENT("onimaapi.region.priority.argument", "Permet de définir la priorité d'une région."),
	
	ONIMAAPI_REGION_LOCATION_ARGUMENT("onimaapi.region.location.argument", "Permet de définir la location d'une région."),
	
	ONIMAAPI_REGION_DEATHBANMULTIPLIER_ARGUMENT("onimaapi.region.deathbanmultiplier.argument", "Permet de définir le multiplicateur de perte de dathban"),
	
	ONIMAAPI_REGION_NAME_ARGUMENT("onimaapi.region.name.argument", "Permet de définir le nom d'une région"),
	
	ONIMAAPI_REGION_FLAG_ARGUMENT("onimaapi.region.flag.argument", "Permet de gérer les flags d'une région"),
	
	ONIMAAPI_REGION_SHOW_ARGUMENT("onimaapi.region.show.argument", "Permet de montrer les infos d'une région"),
	
	ONIMAAPI_REGION_RANK_ARGUMENT("onimaapi.region.rank.argument", "Permet de montrer définir le rank minimum pour rentrer dans une région"),
	
	ONIMAAPI_REGION_LIST_ARGUMENT("onimaapi.region.list.argument", "Permet d'afficher la liste des régions"),
	
	ONIMAAPI_REGION_DISPLAYNAME_ARGUMENT("onimaapi.region.displayname.argument", "Permet de définir le display name d'une région"),
	
	ONIMAAPI_COOLDOWN_ADD_ARGUMENT("onimaapi.cooldown.add.argument", "Permet d'ajouter un cooldown"),
	
	ONIMAAPI_COOLDOWN_REMOVE_ARGUMENT("onimaapi.cooldown.remove.argument", "Permet d'enlever un cooldown"),
	
	ONIMAAPI_COOLDOWN_CHECK_ARGUMENT("onimaapi.cooldown.check.argument", "Permet de voir tous les cooldowns d'un joueur"),
	
	ONIMAAPI_COOLDOWN_PAUSE_ARGUMENT("onimaapi.cooldown.pause.argument", "Permet de pauser/play un cooldown d'un joueur"),
	
	PROTECTION_BYPASS("protection.bypass", "Permet de bypass toutes sortes de protection du serveur."),
	
	CHAT_FILTER_BYPASS("chat.filter.bypass", "Permet de bypass le filtre du chat."),
	
	ONIMAAPI_COMMAND_SOCIALSPY("onimaapi.socialspy.command", "Permet d'espionner les joueurs"),
	
	ONIMAAPI_COMMAND_INVSEE("onimaapi.invsee.command", "Permet d'inspecter l'inventaire d'un joueur"),
	
	ONIMAAPI_CRATE_CREATE_ARGUMENT("onimaapi.crate.create.argument", "Permet de créer une crate"),
	
	ONIMAAPI_COMMAND_ORES("onimaapi.ores.command", "Permet de voir les minerais miné par un joueur"),
	
	ONIMAAPI_CRATE_REMOVE_ARGUMENT("onimaapi.crate.remove.argument", "Permet de supprimer une crate"),
	
	ONIMAAPI_CRATE_KEY_ARGUMENT("onimaapi.crate.key.argument", "Permet d'avoir la clef de la crate"),
	
	ONIMAAPI_CRATE_LIST_ARGUMENT("onimaapi.crate.lsit.argument", "Permet de voir la liste des crates"),
	
	ONIMAAPI_CRATE_ROOMFOR_ARGUMENT("onimaapi.crate.roomfor.argument", "Définit la salle pour les crates avec hologrammes"),
	
	ONIMAAPI_CRATE_LOCATION_ARGUMENT("onimaapi.crate.location.argument", "Définit la location d'une crate physique"),
	
	ONIMAAPI_CRATE_DROPAMOUNT_ARGUMENT("onimaapi.crate.dropamount.argument", "Définit le nombre d'items que cette crate va drop"),
	
	ONIMAAPI_CRATE_ADDITEM_ARGUMENT("onimaapi.crate.additem.argument", "Ajoute un item dans la crate"),
	
	ONIMAAPI_CRATE_ADDCOMMAND_ARGUMENT("onimaapi.crate.addcommand.argument", "Ajoute une commande dans la crate"),
	
	ONIMAAPI_CRATE_ROOMTP_ARGUMENT("onimaapi.crate.roomtp.argument", "Définit la location de téléportation pour les ROOM crate"),
	
	ONIMAAPI_CRATE_REMOVEITEM_ARGUMENT("onimaapi.crate.removeitem.argument", "Supprime un item d'une crate"),
	
	ONIMAAPI_CRATE_FIREWORK_ARGUMENT("onimaapi.crate.firework.argument", "Met un feu d'artifice lorsque on recoit le prix"),
	
	ONIMAAPI_CRATE_DISPLAYNAME_ARGUMENT("onimaapi.crate.displayname.argument", "Définit le nom à afficher pour la crate"),
	
	ONIMAAPI_CRATE_INFO_ARGUMENT("onimaapi.crate.info.argument", "Affiche les informatons d'une crate"),
	
	VIRTUAL_KEYS_COMMAND("onimaapi.virtualkeys.command", "Affiche les clefs virtuelles."),
	
	VIRTUAL_KEYS_COMMAND_AS_ADMIN("onimaapi.virtualkeys.asadmin.command", "Affiche les clefs virtuelles d'un joueur."),
	
	EOTW_COMMAND("onimafaction.eotw.command", "Démarre ou stoppe l'EOTW."),
	
	SOTW_COMMAND("onimafaction.sotw.command", "Démarre ou stoppe le SOTW."),
	
	RAIDABLE_COMMAND("onimafaction.raidable.command", "Gère l'event raidable."),
	
	CREATE_SIGN_SHOP("onimaapi.sign-shop.create", "Créé un sign shop."),
	
	CONDENSE_COMMAND("onimaapi.condense.command", "Stack les items de l'inventaire entre eux."),
	
	MENU_COMMAND("onimaapi.menu.command", "Permet d'ouvrir des menus"),
	
	MESSAGE_COMMAND("onimaapi.message.command", "Permet d'envoyer des messages privés"),
	
	REPLY_COMMAND("onimaapi.reply.command", "Permet de répondre à des messages privés"),
	
	FREEZE_COMMAND("onimaapi.freeze.command", "Permet de freeze un joueur"),
	
	SPAWNER_COMMAND("onimaapi.spawner.command", "Permet de se donner un spawner"),
	
	CROWBAR_COMMAND("onimaapi.crowbar.command", "Permet de donner une crowbar"),
	
	LAG_COMMAND("onimaapi.lag.command", "Permet de donner des informations sur le serveur"),
	
	LAG_COMMAND_MEMORY("onimaapi.lag.command", "Permet de donner des informations sur la mémoire"),
	
	ONIMAAPI_COMMAND_INVSEE_EDIT("onimaapi.command.invsee-edit", "Permet d'éditer l'inventaire d'un joueur."),
	
	RESTORE_COMMAND("onimaapi.restore.command", "Affiche toutes les morts du joueur et le contenu de son inventaire."),
	
	REVIVE_COMMAND("onimaapi.revive.command", "Permet de ressuciter un joueur deathban."),
	
	INVRESTORED_COMMAND("onimaapi.invrestored.command", "Permet d'afficher les inventaires en attente."),
	
	INVRESTORED_COMMAND_OTHER("onimaapi.invrestored-other.subcommand", "Permet d'afficher les inventaires en attente d'un joueur."),
	
	VANISH_COMMAND("onimaapi.vanish.command", "Permet de se mettre en vanish."),
	
	LOGOUT_COMMAND("onimaapi.logout.command", "Permet de se déconnecter en sécurité."),
	
	ONIMAFACTION_LIVES_CHECK_ARGUMENT("onimafaction.lives.check.argument", "Permet de regarder son nombre de vie."),
	
	ONIMAFACTION_LIVES_SEND_ARGUMENT("onimafaction.lives.send.argument", "Permet d'envoyer des vies."),
	
	ONIMAFACTION_LIVES_REMOVE_ARGUMENT("onimafaction.lives.remove.argument", "Permet d'enlever des vies."),
	
	ONIMAFACTION_LIVES_GIVE_ARGUMENT("onimafaction.lives.give.argument", "Permet de donner des vies."),
	
	ONIMAFACTION_LIVES_SET_ARGUMENT("onimafaction.lives.set.argument", "Permet de définir un nombre de vies."),
	
	ONIMAAPI_BALANCE_ADD_ARGUMENT("onimaapi.balance.add.argument", "Permet de give de l'argent."),
	
	ONIMAAPI_BALANCE_BAN_ARGUMENT("onimaapi.balance.ban.argument", "Permet de bloquer l'argent d'un joueur."),
	
	ONIMAAPI_BALANCE_REMOVE_ARGUMENT("onimaapi.balance.remove.argument", "Permet de retirer de l'argent à un joueur."),
	
	ONIMAAPI_BALANCE_SET_ARGUMENT("onimaapi.balance.set.argument", "Permet de définir de l'argent à un joueur."),
	
	ONIMAAPI_BALANCE_CHECK_ARGUMENT("onimaapi.balance.check.argument", "Affiche le solde d'un joueur."),
	
	ONIMAAPI_BALANCE_PAY_ARGUMENT("onimaapi.balance.pay.argument", "Paye un joueur."),
	
	ONIMAAPI_BALANCE_TOP_ARGUMENT("onimaapi.balance.top.argument", "Affiche les joueurs les plus riche du serveur."),
	
	ONIMAFACTION_STAFFCHAT_COMMAND("onimafaction.staffchat.command", "Vous met en staffchat."),
	
	ONIMAAPI_WHOIS_COMMAND("onimaapi.whois.command", "Affiche des informations sur le joueur."),
	
	ONIMAAPI_CRAFT_COMMAND("onimaapi.craft.command", "Ouvre une table de craft."),
	
	ONIMAAPI_RENAME_COMMAND("onimaapi.rename.command", "Renomme un item."),
	
	ONIMAAPI_RENAME_COMMAND_BYPASS("onimaapi.rename.command.bypass", "Renomme n'importe quel item."),
	
	ONIMAFACTION_PVP_COMMAND("onimafaction.pvp.command", "Permet de désactiver son pvp timer"),
	
	ONIMAAPI_RELORE_COMMAND("onimaapi.relore.command", "Change le lore d'un item"),
	
	ONIMAAPI_ENCHANT_COMMAND("onimaapi.enchant.command", "Enchante un item"),
	
	ONIMAAPI_WORLDCHANGER_COMMAND("onimaapi.worldchanger.command", "Gère le changement de monde."),
	
	ONIMAAPI_WORLD_COMMAND("onimaapi.world.command", "Permet de se téléporter de monde en monde."),
	
	ONIMAAPI_FLY_COMMAND("onimaapi.fly.command", "Permet d'activer/désactiver le fly."),
	
	ONIMAAPI_FLY_OTHERS_COMMAND("onimaapi.fly-others.subcommand", "Permet d'activer/désactiver le fly d'un joueur."),
	
	ONIMAAPI_SUPPLYCRATE_SCHEDULE_ARGUMENT("onimaapi.supplycrate.schedule.argument", "Permet de programmer des supply crates."),
	
	ONIMAAPI_SUPPLYCRATE_FIRSTSCHEDULE_ARGUMENT("onimaapi.supplycrate.firstschedule.argument", "Permet de définir le jour ou l'event commencera pour la première fois."),
	
	ONIMAAPI_SUPPLYCRATE_ENABLESCHEDULER_ARGUMENT("onimaapi.supplycrate.enablescheduler.argument", "Permet d'activer/désactiver un scheduler."),
	
	ONIMAAPI_SUPPLYCRATE_ADDITEM_ARGUMENT("onimaapi.supplycrate.additem.argument", "Permet d'ajouter des items à drop dans le drop."),
	
	ONIMAAPI_BAN_COMMAND("onimaapi.ban.command", "Permet de bannir un joueur."),
	
	ONIMAAPI_MUTE_COMMAND("onimaapi.mute.command", "Permet de muter un joueur."),
	
	ONIMAAPI_KICK_COMMAND("onimaapi.kick.command", "Permet de kicker un joueur."),
	
	ONIMAAPI_WARN_COMMAND("onimaapi.warn.command", "Permet de warn un joueur."),
	
	ONIMAAPI_TEMPBAN_COMMAND("onimaapi.tempban.command", "Permet de tempban un joueur."),
	
	ONIMAAPI_BLACKLIST_COMMAND("onimaapi.blacklist.command", "Permet de blacklist un joueur."),
	
	ONIMAAPI_WARP_CREATE_ARGUMENT("onimaapi.warp.create.argument", "Permet de créer un warp."),
	
	ONIMAAPI_WARP_REMOVE_ARGUMENT("onimaapi.warp.remove.argument", "Permet de supprimer un warp."),
	
	ONIMAAPI_LIST_REMOVE_ARGUMENT("onimaapi.warp.list.argument", "Permet d'afficer la liste des warps."),
	
	TOP_COMMAND("onimaapi.top.command", "Permet de se téléporter au block le plus haut."),
	
	ONIMAAPI_PUNISHMENT_COMMAND("onimaapi.punishment.command", "Permet de voir les punitions d'un joueur"),
	
	ONIMAAPI_PUNISHMENT_SELF_COMMAND("onimaapi.punishment.self.command", "Permet de voir ses punitions"),
	
	ONIMAAPI_UNBAN_COMMAND("onimaapi.unban.command", "Permet d'unban un joueur"),
	
	ONIMAAPI_UNMUTE_COMMAND("onimaapi.unmute.command", "Permet de démuter un joueur"),
	
	ONIMAAPI_UNBLACKLIST_COMMAND("onimaapi.unblacklist.command", "Permet de déblacklist un joueur"),
	
	COMBAT_LOGGER_BYPASS("combat_logger.bypass", "Permet de ne pas spawn de combat logger à la déconnexion."),
	
	SUDO_COMMAND("onimaapi.sudo.command", "Permet de forcer un joueur à exécuter une commande."),
	
	GMC_COMMAND("onimaapi.gmc.command", "Permet de se mettre en gamemode."),
	
	CONNECTION_LOGS_COMMAND("onimaapi.connectionlogs.command", "Permet d'afficher les logs de connexion."),
	
	ALTS_COMMAND("onimaapi.alts.command", "Permet d'afficher les alts d'un joueur"),
	
	ONIMAAPI_BALANCE_COMMAND("onimaapi.balance.command", "Commande principale pour les balance."),
	
	ONIMAAPI_CHAT_COMMAND("onimaapi.chat.command", "Commande principale pour le chat."),
	
	ONIMAAPI_REGION_COMMAND("onimaapi.region.command", "Commande principale pour les régions."),
	
	ONIMAAPI_COOLDOWN_COMMAND("onimaapi.cooldown.command", "Commande principale pour les cooldowns."),
	
	ONIMAAPI_CRATE_COMMAND("onimaapi.crate.command", "Commande principale pour les crates."),
	
	ONIMAAPI_MOUNTAIN_COMMAND("onimaapi.mountain.command", "Commande principale pour les montagnes."),
	
	ONIMAAPI_COMMAND("onimaapi.supplycrate.command", "Commande principale pour les supply crates."),
	
	ONIMAAPI_WARP_COMMAND("onimaapi.warp.command", "Commande principale pour les warps."),
	
	ONIMABOARD_STAFFBOARD("onimaboard.staffboard.command", "Permet d'activer/désactiver le staffboard."),
	
	ONIMAFACTION_LIVES_COMMAND("onimafaction.lives.command", "Commande principale pour les vies."),
	
	GAME_COMMAND("onimagames.game.command", "Commande principale pour les games."),
	
	KOTH_COMMAND("onimagames.koth.command", "Commande principale pour les koth."),
	
	CITADEL_COMMAND("onimagames.citadel.command", "Commande principale pour les citadelles."),
	
	CONQUEST_COMMAND("onimagames.conquest.command", "Commande principale pour les conquests."),
	
	DRAGON_COMMAND("onimagames.dragon.command", "Commande principale pour les events dragon."), 
	
	DTC_COMMAND("onimagames.dtc.command", "Commande principale pour les dtc."),
	
	ONIMAAPI_NOTE_COMMAND("onimaapi.note.command", "Commande principale pour les notes."),
	
	ONIMAAPI_NOTE_WRITE_ARGUMENT("onimaapi.note.write.argument", "Permet d'écrire une note."),
	
	ONIMAAPI_NOTE_REMOVE_ARGUMENT("onimaapi.note.remove.argument", "Permet de supprimer une note."),
	
	ONIMAAPI_NOTE_LIST_ARGUMENT("onimaapi.note.list.agument", "Permet d'afficher toutes les notes d'un joueur."),
	
	ONIMAAPI_NOTE_PRIORITY_ARGUMENT("onimaapi.note.priority.agument", "Permet de changer la priorité d'une note."),
	
	ONIMAAPI_NOTE_EXPIRE_ARGUMENT("onimaapi.note.expire.agument", "Permet de définir quand une note sera supprimée."),
	
	ONIMAAPI_IPHISTORY_COMMAND("onimaapi.iphistory.command", "Permet de voir l'historique des ip d'un joueur."),
	
	ONIMAAPI_COORDS_COMMAND("onimaapi.coords.command", "Permet d'afficher les coordonées importantes."),
	
	ONIMABOARD_SIDEBAR_COMMAND("onimaboard.sidebar.command", "Permet d'activer/désactiver le scoreboard."),
	
	ONIMAFACTION_FACTION_COMMAND("onimafaction.faction.command", "Commande principale pour les faction."),
	
	ONIMAAPI_COLORED_SIGN("onimaapi.colored.sign", "Permet d'écrire en coloré sur un panneau."),
	
	ONIMAAPI_COLORED_CHAT("onimaapi.colored.chat", "Permet d'écrire en coloré dans le chat."),
	
	ONIMAAPI_DISGUISE_COMMAND("onimaapi.disguise.command", "Permet de se déguiser en un autre joueur."),
	
	ONIMAAPI_DISGUISE_COMMAND_LIST("onimaapi.disguise.command.list", "Permet de gérer les disguise d'autres joueurs."),
	
	ONIMAFACTION_DTR_COOLDOWN_ARGUMENT("onimafaction.dtr.cooldown.argument", "Permet de supprimer le dtr freeze d'une faction."), 
	
	ONIMAAPI_SEE_INVISIBLE("onimaapi.see.invisible", "Permet de voir les joueurs invisibles"),
	
	ONIMAAPI_TPHERE_COMMAND("onimaapi.tphere.command", "Permet de téléporter un joueur à votre position."),
	
	ONIMAAPI_TPALL_COMMAND("onimaapi.tpall.command", "Permet de téléporter tous les joueurs à votre position."),
	
	ONIMAAPI_SKULL_COMMAND("onimaapi.skull.command", "Permet de donner un type de tête/la tête d'un joueur."),
	
	ONIMAAPI_CLEAR_COMMAND("onimaapi.clear.command", "Permet de clear l'inventaire d'un joueur."),
	
	ONIMAAPI_IGNORE_COMMAND("onimaapi.ignore.command", "Commande principale pour ignorer des joueurs."),
	
	ONIMAAPI_IGNORE_ADD_ARGUMENT("onimaapi.ignore.add.argument", "Ajoute un joueur à la liste des joueurs ignorés."),
	
	ONIMAAPI_IGNORE_EXEMPT("onimaapi.ignore.exempt", "Empêche un joueur d'être ignoré."),
	
	ONIMAAPI_IGNORE_REMOVE_ARGUMENT("onimaapi.ignore.remove.argument", "Supprime un joueur de la liste des joueurs ignorés."),
	
	ONIMAAPI_IGNORE_LIST_ARGUMENT("onimaapi.ignore.list.argument", "Affiche la liste des joueurs ignorés."),
	
	KILL_COMMAND("onimaapi.kill.command", "Tue un joueur."),
	
	GMS_COMMAND("onimaapi.gms.command", "Permet de se mettre en survie."),
	
	SPEED_COMMAND("onimaapi.speed.command", "Permet de définir sa vitesse."),
	
	HEAL_COMMAND("onimaapi.heal.command", "Permet de se heal."),
	
	FEED_COMMAND("onimaapi.feed.command", "Permet de se nourrir."),
	
	ENDEXIT_COMMAND("onimaapi.endexit.command", "Permet de définir la zone de sortie de l'end."),
	
	REPAIR_COMMAND("onimaapi.repair.command", "Permet de réparer n'importe quel item."),
	
	HAT_COMMAND("onimaapi.hat.command", "Permet de mettre n'importe quel item sur sa tête."),
	
	STOPLAG_COMMAND("onimaapi.stoplag.command", "Permet d'arrêter toutes les activitées qui font lagger le serveur."),
	
	ENDERCHEST_COMMAND("onimaapi.enderchest.command", "Permet d'ouvrir un enderchest."),
	
	ONIMAFACTION_FASTPLANT_COMMAND("onimafaction.fastplant.command", "Commande principale pour les fast plant."),
	
	ONIMAFACTION_FASTPLANT_CHECK_COMMAND("onimafaction.fastplant.check.argument", "Affiche l'état du fast plant visé."),
	
	BROADCAST_COMMAND("onimaapi.broadcast.command", "Broadcast un message dans tout le serveur."),
	
	SETTINGS_COMMAND("onimaapi.settings.command", "Gère ses options."),
	
	REPORT_COMMAND("onimaapi.report.command", "Report un joueur."),
	
	REPORTS_COMMAND("onimaapi.reports.command", "Commande principale pour gérer les reports."),
	
	REPORT_EXEMPT("onimaapi.report.exempt", "Permet de ne pas se faire report."),
	
	BUG_REPORT_COMMAND("onimaapi.bugreport.command", "Signale un bug."),
	
	BUG_REPORTS_COMMAND("onimaapi.bugreports.command", "Commande principale pour gérer les signalement de bugs."),
	
	REPORT_STATS_CHECK("onimaapi.reports.check", "Permet de voir ses statistiques de report."),
	
	REPORTS_USER_ARGUMENT("onimaapi.reports.user.argument", "Permet d'afficher les reports d'un joueur."),
	
	REPORTS_DONE_ARGUMENT("onimaapi.reports.done.argument", "Permet d'afficher les reports traités."),
	
	REPORTS_HASHTAG_ARGUMENT("onimaapi.reports.hashtag.argument", "Permet d'afficher les infos d'un report."),
	
	REPORTS_DATA_ARGUMENT("onimaapi.reports.data.argument", "Permet d'afficher les données d'un joueur."),
	
	MYREPORTS_COMMAND("onimaapi.myreports.command", "Affiche l'historique de ses reports."),
	
	CHAT_RESTRICTION_BYPASS("onimaapi.chat-restriction.bypass", "Permet de passer les restrictions de chat.");
	
	private String permission, description;
	
	private OnimaPerm(String permission, String description) {
		this.permission = permission;
		this.description = description;
	}

	/**
	 * This method returns the permission as a string.
	 * 
	 * @return The permission.
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * This method returns the description of this permission.
	 * 
	 * @return The permission description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method returns the matching OnimaPerm with the given string.
	 * 
	 * @param str - Permission as a string.
	 * @return The OnimaPerm.
	 */
	public static OnimaPerm fromString(String str) {
		for (OnimaPerm permission : values()) {
			if (permission.getPermission().equalsIgnoreCase(str))
				return permission;
		}
		return null;
	}

	/**
	 * This methods checks if the Permissible has the permission 
	 * @param sender
	 * @return
	 */
	public boolean has(Permissible sender) {
		return sender.hasPermission(permission);
	}
	
}
