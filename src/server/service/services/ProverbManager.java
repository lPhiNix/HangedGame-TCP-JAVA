package server.service.services;

import common.logger.CustomLogger;
import common.model.Proverb;
import common.model.Word;
import server.service.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para gestionar de refranes utilizados en el juego con persistencia implementada.
 * <p>
 * Esta clase se encarga de cargar, validar y gestionar los refranes almacenados
 * en un archivo de texto. Proporciona métodos para crear instancias de {@link Proverb}
 * y acceder a la lista de refranes disponibles.
 * </p>
 *
 * @see Proverb
 * @see Word
 */
public class ProverbManager implements Service {
    private static final Logger logger = CustomLogger.getLogger(ProverbManager.class.getName());
    private static final String FILE_PATH = "proverbs.txt"; // Ruta del archivo donde se almacenan los refranes.
    private static final String ABC = "abcdefghijklnmñopqrstuvwxyz"; // Alfabeto permitido para la validación de refranes.
    private Map<Integer, String> proverbs; // Mapa que almacena los refranes cargados del archivo.

    /**
     * Constructor que inicializa la carga de refranes desde el archivo.
     */
    public ProverbManager() {
        proverbs = loadProverbs();
    }

    /**
     * Carga los refranes desde el archivo de texto.
     *
     * @return Un mapa con los refranes indexados por número de línea.
     */
    private Map<Integer, String> loadProverbs() {
        File proverbsFile = new File(FILE_PATH);

        // Verifica si el archivo de refranes existe
        if (!proverbsFile.exists()) {
            logger.log(Level.SEVERE, "Archivo no encontrado: {0}", FILE_PATH);
        }

        Map<Integer, String> proverbsMap = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(proverbsFile))) {
            int lineCount = 0;
            String fileLine;

            // Lee el archivo línea por línea
            while ((fileLine = bufferedReader.readLine()) != null) {
                // Verifica si la línea contiene un refrán válido
                if (!checkProverbLine(fileLine)) {
                    logger.log(Level.WARNING, "Refrán inválido en la línea {0}: {1}",
                            new Object[]{lineCount + 1, fileLine});
                    continue;
                }

                // Agrega el refrán al mapa
                proverbsMap.put(lineCount, fileLine);
                lineCount++;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al leer el archivo de refranes", e);
        }

        return proverbsMap;
    }

    /**
     * Verifica si un refrán contiene solo caracteres válidos.
     *
     * @param proverbText Texto del refrán a validar.
     * @return {@code true} si el refrán es válido, {@code false} en caso contrario.
     */
    private boolean checkProverbLine(String proverbText) {
        char[] proverbCharacters = proverbText.toLowerCase().toCharArray();

        for (char proverbCharacter : proverbCharacters) {
            if (!isValidCharacter(proverbCharacter)) {
                logger.log(Level.WARNING, "Caracter inválido: {0}", proverbCharacter);
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica si un carácter es válido dentro de un refrán.
     *
     * @param character Carácter a validar.
     * @return {@code true} si es un carácter permitido, {@code false} en caso contrario.
     */
    private boolean isValidCharacter(char character) {
        boolean flag = false;
        char[] abc = ABC.toCharArray();

        // Verifica si el carácter está en el alfabeto permitido
        for (char c : abc) {
            if (character == c) {
                flag = true;
            }
        }

        // Permite los caracteres en la lista negra de la clase Word y los espacios
        for (char c : Word.getCharactersBlacklist().toCharArray()) {
            if (character == c || character == ' ') {
                return true;
            }
        }

        return flag;
    }

    /**
     * Crea un nuevo objeto {@link Proverb} con el texto dado.
     *
     * @param text Texto del refrán.
     * @return Instancia de {@link Proverb} con el texto especificado.
     */
    public Proverb createProverb(String text) {
        return new Proverb(text);
    }

    /**
     * Crea un nuevo objeto {@link Proverb} basado en el índice de la lista de refranes.
     *
     * @param index Índice del refrán en la lista cargada.
     * @return Instancia de {@link Proverb} con el texto correspondiente al índice.
     */
    public Proverb createProverb(int index) {
        return new Proverb(proverbs.get(index));
    }

    /**
     * Devuelve el mapa de refranes cargados.
     *
     * @return Mapa con los refranes indexados.
     */
    public Map<Integer, String> getProverbs() {
        return proverbs;
    }

    /**
     * Recarga los refranes desde el archivo de texto.
     */
    public void reloadProverbs() {
        proverbs = loadProverbs();
    }
}

