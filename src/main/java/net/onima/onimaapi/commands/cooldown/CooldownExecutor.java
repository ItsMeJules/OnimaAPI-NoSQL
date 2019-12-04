package net.onima.onimaapi.commands.cooldown;

import net.onima.onimaapi.commands.cooldown.arguments.CooldownAddArgument;
import net.onima.onimaapi.commands.cooldown.arguments.CooldownCheckArgument;
import net.onima.onimaapi.commands.cooldown.arguments.CooldownPauseArgument;
import net.onima.onimaapi.commands.cooldown.arguments.CooldownRemoveArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class CooldownExecutor extends ArgumentExecutor {

	public CooldownExecutor() {
		super("cooldown", OnimaPerm.ONIMAAPI_COOLDOWN_COMMAND);

		addArgument(new CooldownAddArgument());
		addArgument(new CooldownCheckArgument());
		addArgument(new CooldownPauseArgument());
		addArgument(new CooldownRemoveArgument());
	}

}
