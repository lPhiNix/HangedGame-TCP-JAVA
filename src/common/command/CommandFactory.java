package common.command;

import common.command.commands.*;
import common.logger.CustomLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandFactory {
    private static final Logger logger = CustomLogger.getLogger(CommandFactory.class.getName());
    private static final String COMMAND_SYMBOL = "/";
    private final Map<String, Class<? extends Command>> commands;

    public CommandFactory() {
        commands = new HashMap<>();

        registerCommand(LoginCommand.getCommandName(), LoginCommand.class);
        registerCommand(RegisterCommand.getCommnadName(), RegisterCommand.class);
        registerCommand(UserCommand.getCommandName(), UserCommand.class);
        registerCommand(StartSingleGameCommand.getCommandName(), StartSingleGameCommand.class);
        registerCommand(ConsonantCommand.getCommandName(), ConsonantCommand.class);
        registerCommand(VowelCommand.getCommandName(), VowelCommand.class);
        registerCommand(SolveCommand.getCommandName(), SolveCommand.class);
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
            logger.log(Level.WARNING, "Command not found: {0}", commandName);
            return null;
        }

        Class<? extends Command> commandClass = commands.get(commandName);
        if (commandClass != null) {
            return commandClass.getConstructor().newInstance();
        }
        logger.log(Level.WARNING, "Command not found: {0}", commandName);
        return null;
    }

    public static String getCommandSymbol() {
        return COMMAND_SYMBOL;
    }
}
