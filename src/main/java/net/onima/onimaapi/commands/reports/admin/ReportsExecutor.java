package net.onima.onimaapi.commands.reports.admin;

import net.onima.onimaapi.commands.reports.admin.arguments.ReportsUserArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class ReportsExecutor extends ArgumentExecutor {

	public ReportsExecutor(String label, OnimaPerm permission) {
		super("reports", OnimaPerm.REPORTS_COMMAND);

		addArgument(new ReportsUserArgument());
	}
	
}
