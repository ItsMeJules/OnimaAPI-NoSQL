package net.onima.onimaapi.commands.ignore;

import net.onima.onimaapi.commands.ignore.arguments.IgnoreAddArgument;
import net.onima.onimaapi.commands.ignore.arguments.IgnoreListArgument;
import net.onima.onimaapi.commands.ignore.arguments.IgnoreRemoveArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class IgnoreExecutor extends ArgumentExecutor {

	public IgnoreExecutor() {
		super("ignore", OnimaPerm.ONIMAAPI_IGNORE_COMMAND);
		
		addArgument(new IgnoreAddArgument());
		addArgument(new IgnoreListArgument());
		addArgument(new IgnoreRemoveArgument());
	}


}
