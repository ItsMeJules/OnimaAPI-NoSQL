package net.onima.onimaapi.commands.region;

import net.onima.onimaapi.commands.region.arguments.RegionCreateArgument;
import net.onima.onimaapi.commands.region.arguments.RegionDTRLossArgument;
import net.onima.onimaapi.commands.region.arguments.RegionDeathbanArgument;
import net.onima.onimaapi.commands.region.arguments.RegionDeathbanMultiplierArgument;
import net.onima.onimaapi.commands.region.arguments.RegionDisplayNameArgument;
import net.onima.onimaapi.commands.region.arguments.RegionFlagArgument;
import net.onima.onimaapi.commands.region.arguments.RegionListArgument;
import net.onima.onimaapi.commands.region.arguments.RegionLocationArgument;
import net.onima.onimaapi.commands.region.arguments.RegionNameArgument;
import net.onima.onimaapi.commands.region.arguments.RegionPriorityArgument;
import net.onima.onimaapi.commands.region.arguments.RegionRankArgument;
import net.onima.onimaapi.commands.region.arguments.RegionRemoveArgument;
import net.onima.onimaapi.commands.region.arguments.RegionShowArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class RegionExecutor extends ArgumentExecutor {

	public RegionExecutor() {
		super("region", OnimaPerm.ONIMAAPI_REGION_COMMAND);
		
		addArgument(new RegionCreateArgument());
		addArgument(new RegionDeathbanArgument());
		addArgument(new RegionDeathbanMultiplierArgument());
		addArgument(new RegionDisplayNameArgument());
		addArgument(new RegionDTRLossArgument());
		addArgument(new RegionFlagArgument());
		addArgument(new RegionShowArgument());
		addArgument(new RegionListArgument());
		addArgument(new RegionLocationArgument());
		addArgument(new RegionNameArgument());
		addArgument(new RegionRankArgument());
		addArgument(new RegionRemoveArgument());
		addArgument(new RegionPriorityArgument());
	}

}
