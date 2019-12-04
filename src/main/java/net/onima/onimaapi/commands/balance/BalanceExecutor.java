package net.onima.onimaapi.commands.balance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.commands.balance.arguments.BalanceCheckArgument;
import net.onima.onimaapi.commands.balance.arguments.BalancePayArgument;
import net.onima.onimaapi.commands.balance.arguments.BalanceTopArgument;
import net.onima.onimaapi.commands.balance.arguments.staff.BalanceAddArgument;
import net.onima.onimaapi.commands.balance.arguments.staff.BalanceBanArgument;
import net.onima.onimaapi.commands.balance.arguments.staff.BalanceRemoveArgument;
import net.onima.onimaapi.commands.balance.arguments.staff.BalanceSetArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class BalanceExecutor extends ArgumentExecutor {

	public BalanceExecutor() {
		super("balance", OnimaPerm.ONIMAAPI_BALANCE_COMMAND);
		
		addArgument(new BalanceAddArgument());
		addArgument(new BalanceBanArgument());
		addArgument(new BalanceRemoveArgument());
		addArgument(new BalanceSetArgument());
		addArgument(new BalanceCheckArgument());
		addArgument(new BalancePayArgument());
		addArgument(new BalanceTopArgument());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1)
			return getArgument("check").onCommand(sender, command, label, new String[] {"check"});
		else if (args[0].equalsIgnoreCase("help"))
			return super.onCommand(sender, command, label, new String[0]);
		else
			return super.onCommand(sender, command, label, args);
	}

}
