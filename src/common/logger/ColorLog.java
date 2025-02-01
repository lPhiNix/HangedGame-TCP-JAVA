package common.logger;

import java.util.logging.Level;

/**
 * La clase {@code ColorLog} proporciona una enumeración de colores para ser utilizados
 * en los mensajes de registro (logs). Los colores están asociados a diferentes niveles de
 * severidad de los mensajes de log.
 *
 * Los colores utilizados son secuencias de escape ANSI que cambian el color de los textos
 * cuando se muestran en una consola que soporta ANSI.
 */
public enum ColorLog {

    /** Color rojo para los mensajes de nivel SEVERE (error grave). */
    SEVERE("\u001B[31m"),

    /** Color amarillo para los mensajes de nivel WARNING (advertencia). */
    WARNING("\u001B[93m"),

    /** Color verde para los mensajes de nivel INFO (información). */
    INFO("\u001B[92m"),

    /** Color cian para los mensajes de nivel CONFIG (configuración). */
    CONFIG("\u001B[36m"),

    /** Color por defecto (sin color) para los mensajes sin un nivel específico. */
    DEFAULT("\u001B[0m");

    /** La secuencia de color ANSI asociada al nivel de log. */
    private final String color;

    /**
     * Constructor de la enumeración {@code ColorLog}.
     *
     * @param color La secuencia de escape ANSI asociada al nivel de log.
     */
    ColorLog(String color) {
        this.color = color;
    }

    /**
     * Obtiene el color ANSI asociado al nivel de log.
     *
     * @return El color ANSI como una cadena de texto.
     */
    public String getColor() {
        return color;
    }

    /**
     * Devuelve el color adecuado para un nivel de log específico.
     * Si no se encuentra un color para el nivel dado, se devuelve el color por defecto.
     *
     * @param level El nivel de log para el que se busca el color.
     * @return El color ANSI correspondiente al nivel de log.
     */
    public static String getColorForLevel(Level level) {
        try {
            // Se obtiene el color correspondiente al nivel de log usando su nombre
            return ColorLog.valueOf(level.getName()).getColor();
        } catch (IllegalArgumentException e) {
            // Si no se encuentra el nivel, se devuelve el color por defecto
            return DEFAULT.getColor();
        }
    }
}
