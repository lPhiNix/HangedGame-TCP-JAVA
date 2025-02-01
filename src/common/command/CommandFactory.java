package common.command;

import common.command.commands.*;
import common.model.User;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final String COMMAND_SYMBOL = "/";
    private final Map<String, Class<? extends Command>> commands;

    public CommandFactory() {
        commands = new HashMap<>();

        registerCommand(UserCommand.getCommandName(), UserCommand.class);
        registerCommand(PasswordCommand.getCommandName(), PasswordCommand.class);
        registerCommand(RegisterCommand.getCommnadName(), RegisterCommand.class);
        registerCommand(StartSingleGameCommand.getCommnadName(), StartSingleGameCommand.class);
        registerCommand(ConsonantCommand.getCommandName(), ConsonantCommand.class);
        registerCommand(VowelCommand.getCommandName(), VowelCommand.class);
        registerCommand(SolveCommand.getCommnadName(), SolveCommand.class);
        registerCommand(ExitCommand.getCommandName(), ExitCommand.class);

    }

    public void registerCommand(String commandName, Class<? extends Command> commandClass) {
        commands.put(commandName, commandClass);
    }

    public Class<? extends Command> getCommand(String command) {
        return commands.get(command);
    }

    public Command createCommand(String commandName) throws Exception {
        if (commandName.startsWith(COMMAND_SYMBOL)) {
            commandName = commandName.substring(1);
        } else {
            return null;
        }

        Class<? extends Command> commandClass = commands.get(commandName);
        if (commandClass != null) {
            return commandClass.getConstructor().newInstance();
        }
        throw new IllegalArgumentException();
    }
}
