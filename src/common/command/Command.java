package common.command;

public interface Command {
    void execute(String[] args, CommandContext contex);
}
