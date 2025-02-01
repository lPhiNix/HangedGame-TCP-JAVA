package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.logger.CustomLogger;

import java.util.logging.Logger;

public class RegisterCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(RegisterCommand.class.getName());
    private static final String COMMNAD_NAME = "register";
    @Override
    public void execute(String[] args, CommandContext context) {

    }

    public static String getCommnadName() {
        return COMMNAD_NAME;
    }
}
