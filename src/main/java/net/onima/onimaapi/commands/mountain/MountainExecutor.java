package net.onima.onimaapi.commands.mountain;

import net.onima.onimaapi.commands.mountain.arguments.MountainShowArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainCreateArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainFirstScheduleArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainListArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainLocationArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainNameArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainRegisterBlocksArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainRemoveArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainScheduleArgument;
import net.onima.onimaapi.commands.mountain.arguments.staff.MountainSchedulerEnableArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class MountainExecutor extends ArgumentExecutor {

	public MountainExecutor() {
		super("mountain", OnimaPerm.ONIMAAPI_MOUNTAIN_COMMAND);
		
		addArgument(new MountainCreateArgument());
		addArgument(new MountainFirstScheduleArgument());
		addArgument(new MountainListArgument());
		addArgument(new MountainLocationArgument());
		addArgument(new MountainNameArgument());
		addArgument(new MountainRegisterBlocksArgument());
		addArgument(new MountainRemoveArgument());
		addArgument(new MountainScheduleArgument());
		addArgument(new MountainSchedulerEnableArgument());
		addArgument(new MountainShowArgument());
	}

}
