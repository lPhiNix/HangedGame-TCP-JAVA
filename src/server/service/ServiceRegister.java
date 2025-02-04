package server.service;

import common.logger.CustomLogger;
import server.service.services.CommandProcessor;
import server.service.services.ProverbManager;
import server.service.services.RoomManager;
import server.service.services.UserManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceRegister {
    private static final Logger logger = CustomLogger.getLogger(ServiceRegister.class.getName());
    private final ConcurrentMap<Class<? extends Service>, Service> services;

    public ServiceRegister() {
        services = new ConcurrentHashMap<>();

        registerService(UserManager.class, new UserManager());
        registerService(ProverbManager.class, new ProverbManager());
        registerService(CommandProcessor.class, new CommandProcessor());
        registerService(RoomManager.class, new RoomManager());

        logger.log(Level.CONFIG, "Iniciando servicios del servidor");
    }

    private void registerService(Class<? extends Service> classService, Service service) {
        services.put(classService, service);
    }

    public synchronized <T extends Service> T getService(Class<T> classService) {
        return classService.cast(services.get(classService));
    }
}
