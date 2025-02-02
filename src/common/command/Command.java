package common.command;

import server.ClientHandler;

public interface Command {
    void execute(String[] args, ClientHandler clientHandler);
}
