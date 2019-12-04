package net.onima.onimaapi.commands.rank;

import net.onima.onimaapi.commands.rank.arguments.RankInfoArgument;
import net.onima.onimaapi.commands.rank.arguments.RankListArgument;
import net.onima.onimaapi.commands.rank.arguments.staff.RankSetArgument;
import net.onima.onimaapi.commands.rank.arguments.staff.RankTempSetArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class RankExecutor extends ArgumentExecutor {

	public RankExecutor() {
		super("rank", OnimaPerm.RANK_COMMAND);

		addArgument(new RankInfoArgument());
		addArgument(new RankListArgument());
		addArgument(new RankSetArgument());
		addArgument(new RankTempSetArgument());
	}

}
