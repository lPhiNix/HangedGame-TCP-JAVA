package common.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * La clase {@code ChatLogger} configura y gestiona el sistema de logging para la aplicación.
 * Permite el registro de eventos con diferentes niveles de severidad (como SEVERE, WARNING, INFO, etc.)
 * tanto en consola como en un archivo de log.
 * <p>
 * Los mensajes de log se formatean con una fecha y hora, el nombre de la clase que realiza el log,
 * el nivel de severidad del log, y el mensaje. Además, los errores (excepciones) también se incluyen
 * en el log si es necesario.
 * </p>
 */
public class CustomLogger {

    // Logger principal utilizado para realizar las operaciones de log
    private static final Logger logger = CustomLogger.getLogger(CustomLogger.class.getName());

    // Bloque estático que configura los manejadores de log (consola y archivo)
    static {
        // Obtener el logger raíz
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        // Eliminar cualquier manejador preexistente
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        // Configurar el manejador de consola para mostrar los logs en la consola
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter(true)); // Usar el formateador personalizado
        rootLogger.addHandler(consoleHandler);

        // Configurar el manejador de archivo para guardar los logs en un archivo log.txt
        try {
            FileHandler fileHandler = new FileHandler("log.txt", true); // Archivo con acceso de append
            fileHandler.setFormatter(new CustomFormatter(false)); // Formato diferente para archivos
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "log.txt Not Found.", e); // Si hay un error al crear el archivo de log
        }

        // Establecer el nivel de log para el logger raíz y el manejador de consola
        rootLogger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
    }

    /**
     * Obtiene un logger para una clase específica.
     *
     * @param className El nombre de la clase para la cual se obtiene el logger.
     * @return El logger correspondiente a la clase indicada.
     */
    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }

    /**
     * Formateador personalizado que define cómo se debe mostrar cada registro de log.
     * Dependiendo de si es un log para la consola o un archivo, se utiliza un formato diferente.
     */
    private static class CustomFormatter extends Formatter {
        // Formato de fecha para los logs
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Indica si el log es para consola (para aplicar colores)
        private final boolean isConsole;

        /**
         * Constructor del formateador personalizado.
         *
         * @param isConsole Indica si el log es para la consola (true) o archivo (false).
         */
        public CustomFormatter(boolean isConsole) {
            this.isConsole = isConsole;
        }

        /**
         * Formatea un registro de log, añadiendo la fecha, el nombre de la clase, el nivel,
         * el mensaje y, si es necesario, el traza de una excepción.
         *
         * @param record El registro de log a formatear.
         * @return El mensaje de log formateado.
         */
        @Override
        public String format(LogRecord record) {
            // Fecha y hora del log
            String timestamp = dateFormat.format(new Date(record.getMillis()));
            // Nombre de la clase que genera el log
            String className = record.getLoggerName();
            // Nivel del log (como SEVERE, INFO, etc.)
            String level = record.getLevel().getName();
            // El mensaje formateado
            String message = formatMessage(record);

            // Obtener el color correspondiente al nivel del log si es para consola
            String color = isConsole ? ColorLog.getColorForLevel(record.getLevel()) : "";

            // Formatear el mensaje final con los componentes de log
            String logMessage = String.format("%s[%s] <%s> [%s]: %s%s\n",
                    color, timestamp, className, level, message,
                    isConsole ? "\u001B[0m" : ""); // Resetear el color si es consola

            // Si el registro contiene una excepción, añadir su traza al mensaje
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                logMessage += sw.toString();
            }

            return logMessage;
        }
    }
}

