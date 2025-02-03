package common.command;

import server.service.ClientHandler;

public interface Command {
    void execute(String[] args, ClientHandler clientHandler);
}
