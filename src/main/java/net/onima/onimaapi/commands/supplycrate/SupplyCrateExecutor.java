package net.onima.onimaapi.commands.supplycrate;

import net.onima.onimaapi.commands.supplycrate.arguments.staff.SupplyCrateFirstScheduleArgument;
import net.onima.onimaapi.commands.supplycrate.arguments.staff.SupplyCrateScheduleArgument;
import net.onima.onimaapi.commands.supplycrate.arguments.staff.SupplyCrateSchedulerEnableArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class SupplyCrateExecutor extends ArgumentExecutor {

	public SupplyCrateExecutor() {
		super("supplycrate", OnimaPerm.ONIMAAPI_COMMAND);
		
		addArgument(new SupplyCrateFirstScheduleArgument());
		addArgument(new SupplyCrateScheduleArgument());
		addArgument(new SupplyCrateSchedulerEnableArgument());
	}


}
