package net.onima.onimaapi.commands.note;

import net.onima.onimaapi.commands.note.arguments.NoteExpireArgument;
import net.onima.onimaapi.commands.note.arguments.NoteListArgument;
import net.onima.onimaapi.commands.note.arguments.NotePriorityArgument;
import net.onima.onimaapi.commands.note.arguments.NoteRemoveArgument;
import net.onima.onimaapi.commands.note.arguments.NoteWriteArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class NoteExecutor extends ArgumentExecutor {

	public NoteExecutor() {
		super("note", OnimaPerm.ONIMAAPI_NOTE_COMMAND);
		
		addArgument(new NoteExpireArgument());
		addArgument(new NoteListArgument());
		addArgument(new NotePriorityArgument());
		addArgument(new NoteRemoveArgument());
		addArgument(new NoteWriteArgument());
		
	}

}
