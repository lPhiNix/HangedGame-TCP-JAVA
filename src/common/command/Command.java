package common.command;

import server.thread.ClientHandler;

public interface Command {
    void execute(String[] args, ClientHandler clientHandler);
}
