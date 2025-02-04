package common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un refrán en el juego.
 * <p>
 * Gestiona la ocultación y revelación de palabras, además de permitir intentos de resolución.
 * </p>
 */
public class Proverb {
    private final List<Object> elements; // Lista de elementos del refrán, que pueden ser palabras o separadores.
    private final String text; // Texto completo del refrán.

    /**
     * Crea un refrán a partir de una cadena de texto.
     *
     * @param text Texto del refrán.
     * @throws IllegalArgumentException Si el texto es nulo o vacío.
     */
    public Proverb(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        this.text = text;
        this.elements = parseTextToElements(text);
    }

    private List<Object> parseTextToElements(String text) {
        List<Object> result = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                currentWord.append(c);
            } else {
                if (!currentWord.isEmpty()) {
                    result.add(new Word(currentWord.toString())); // Agregar palabra
                    currentWord.setLength(0);
                }
                result.add(String.valueOf(c)); // Agregar separador
            }
        }

        if (!currentWord.isEmpty()) {
            result.add(new Word(currentWord.toString()));
        }

        return result;
    }

    /** @return Texto original del refrán. */
    public String getText() {
        return text;
    }

    /**
     * Intenta adivinar una consonante en el refrán.
     *
     * @param c Consonante a adivinar.
     * @return {@code true} si la consonante está en alguna palabra, {@code false} en caso contrario.
     */
    public boolean guessConsonant(char c) {
        boolean guessedCorrectly = false;
        for (Object element : elements) {
            if (element instanceof Word word) {
                if (word.guessConsonant(c)) {
                    guessedCorrectly = true;
                }
            }
        }
        return guessedCorrectly;
    }

    /**
     * Intenta adivinar una vocal en el refrán.
     *
     * @param c Vocal a adivinar.
     * @return {@code true} si la vocal está en alguna palabra, {@code false} en caso contrario.
     */
    public boolean guessVowel(char c) {
        boolean guessedCorrectly = false;
        for (Object element : elements) {
            if (element instanceof Word word) {
                if (word.guessVowel(c)) {
                    guessedCorrectly = true;
                }
            }
        }
        return guessedCorrectly;
    }

    /**
     * Intenta resolver el refrán completo.
     *
     * @param guess Refrán ingresado por el jugador.
     * @return {@code true} si el refrán es correcto, {@code false} en caso contrario.
     */
    public boolean resolveProverb(String guess) {
        return text.equalsIgnoreCase(guess);
    }

    /**
     * Verifica si el refrán ha sido completamente descubierto.
     *
     * @return {@code true} si todas las palabras han sido reveladas, {@code false} en caso contrario.
     */
    public boolean isRevealed() {
        for (Object element : elements) {
            if (element instanceof Word word) {
                if (!word.isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Object element : elements) {
            if (element instanceof Word word) {
                result.append(new String(word.getHiddenWord()));
            } else {
                result.append(element);
            }
        }
        return result.toString();
    }
}

