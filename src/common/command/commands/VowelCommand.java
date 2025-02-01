package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.logger.CustomLogger;

import java.util.logging.Logger;

public class VowelCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(VowelCommand.class.getName());
    private static final String COMMAND_NAME = "vowel";
    @Override
    public void execute(String[] args, CommandContext context) {

    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
