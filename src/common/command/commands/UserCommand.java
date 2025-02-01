package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.logger.CustomLogger;

import java.util.logging.Logger;

public class UserCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(UserCommand.class.getName());
    private static final String COMMAND_NAME = "user";
    @Override
    public void execute(String[] args, CommandContext context) {

    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
