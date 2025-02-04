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

/**
 * Registro central de servicios del servidor.
 * <p>
 * Esta clase actúa como un contenedor para administrar y acceder a las diferentes
 * instancias de servicios en el servidor. Permite la recuperación y almacenamiento de
 * servicios de manera concurrente y segura.
 * </p>
 *
 * @see Service
 * @see UserManager
 * @see ProverbManager
 * @see CommandProcessor
 * @see RoomManager
 */
public class ServiceRegister {
    private static final Logger logger = CustomLogger.getLogger(ServiceRegister.class.getName());

    /** Mapa concurrente que almacena las instancias de los servicios registrados. */
    private final ConcurrentMap<Class<? extends Service>, Service> services;

    public ServiceRegister() {
        services = new ConcurrentHashMap<>();

        // Registro de los servicios principales utilizados en el servidor
        registerService(UserManager.class, new UserManager());
        registerService(ProverbManager.class, new ProverbManager());
        registerService(CommandProcessor.class, new CommandProcessor());
        registerService(RoomManager.class, new RoomManager());

        logger.log(Level.CONFIG, "Iniciando servicios del servidor");
    }

    /**
     * Registra un servicio en el mapa de servicios.
     *
     * @param classService Clase del servicio a registrar.
     * @param service      Instancia del servicio.
     */
    private void registerService(Class<? extends Service> classService, Service service) {
        services.put(classService, service);
    }

    /**
     * Obtiene una instancia del servicio especificado registrado en el sistema.
     *
     * @param classService Clase del servicio que se desea obtener.
     * @return Instancia del servicio solicitado o {@code null} si no está registrado.
     */
    public synchronized <T extends Service> T getService(Class<T> classService) {
        return classService.cast(services.get(classService));
    }
}
