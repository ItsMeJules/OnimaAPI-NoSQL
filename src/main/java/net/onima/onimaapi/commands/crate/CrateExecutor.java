package net.onima.onimaapi.commands.crate;

import net.onima.onimaapi.commands.crate.arguments.CrateAddCommandArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateAddItemArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateCreateArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateDisplayNameArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateDropAmountArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateFireworkArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateInfoArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateKeyArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateListArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateLocationArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateRemoveArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateRemoveItemArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateRoomTPArgument;
import net.onima.onimaapi.commands.crate.arguments.CrateRoomforArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class CrateExecutor extends ArgumentExecutor {

	public CrateExecutor() {
		super("crate", OnimaPerm.ONIMAAPI_CRATE_COMMAND);
		
		addArgument(new CrateAddCommandArgument());
		addArgument(new CrateAddItemArgument());
		addArgument(new CrateCreateArgument());
		addArgument(new CrateDisplayNameArgument());
		addArgument(new CrateDropAmountArgument());
		addArgument(new CrateFireworkArgument());
		addArgument(new CrateInfoArgument());
		addArgument(new CrateKeyArgument());
		addArgument(new CrateListArgument());
		addArgument(new CrateLocationArgument());
		addArgument(new CrateRemoveArgument());
		addArgument(new CrateRemoveItemArgument());
		addArgument(new CrateRoomforArgument());
		addArgument(new CrateRoomTPArgument());
	}
	
}
