package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.logger.CustomLogger;

import java.util.logging.Logger;

public class StartSingleGameCommand implements Command {
    private static final Logger logger = CustomLogger.getLogger(StartSingleGameCommand.class.getName());
    private static final String COMMNAD_NAME = "singlegame";
    @Override
    public void execute(String[] args, CommandContext context) {

    }

    public static String getCommnadName() {
        return COMMNAD_NAME;
    }
}
