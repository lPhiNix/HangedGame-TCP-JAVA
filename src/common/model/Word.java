package common.model;

import java.util.Arrays;

/**
 * Representa una palabra dentro de un refrán en el juego.
 * <p>
 * Gestiona la ocultación y revelación de caracteres en base a las conjeturas de los jugadores.
 * </p>
 */
public class Word {
    // Caracteres no permitidos en la palabra que si aparecerán en la palabra oculta.
    private static final char[] CHARACTERS_BLACKLIST = {',', '.', '!', '?', ';', ':'};
    private static final char HIDE_CHARACTER = '_'; // Caracter utilizado para ocultar las letras de la palabra.
    private final String text; // Texto original de la palabra.
    private final char[] characters; // Caracteres filtrados de la palabra, sin símbolos no permitidos.
    private final char[] hiddenWord; // Representación oculta de la palabra con caracteres ocultos.

    /**
     * Constructor que inicializa una palabra y la oculta.
     *
     * @param text Texto de la palabra.
     * @throws IllegalArgumentException Si el texto es nulo o vacío.
     */
    public Word(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        this.text = text;
        this.characters = toFilteredCharArray(text);
        this.hiddenWord = toHiddenCharArray(this.characters);
    }

    /** @return Texto original de la palabra. */
    public String getText() {
        return text;
    }

    /** @return Copia de los caracteres filtrados de la palabra. */
    public char[] getCharacters() {
        return characters.clone();
    }

    /** @return Copia de la palabra oculta. */
    public char[] getHiddenWord() {
        return hiddenWord.clone();
    }

    /**
     * Adivina una consonante y la revela si está presente en la palabra.
     *
     * @param c Caracter consonante a adivinar.
     * @return {@code true} si la consonante está presente, {@code false} en caso contrario.
     */
    public boolean guessConsonant(char c) {
        if (!isConsonant(c)) return false;
        return revealCharacter(c);
    }

    /**
     * Adivina una vocal y la revela si está presente en la palabra.
     *
     * @param c Caracter vocal a adivinar.
     * @return {@code true} si la vocal está presente, {@code false} en caso contrario.
     */
    public boolean guessVowel(char c) {
        if (!isVowel(c)) return false;
        return revealCharacter(c);
    }

    private boolean revealCharacter(char c) {
        boolean guessedCorrectly = false;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == c) {
                hiddenWord[i] = c;
                guessedCorrectly = true;
            }
        }
        return guessedCorrectly;
    }

    private boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }

    private boolean isConsonant(char c) {
        return Character.isLetter(c) && !isVowel(c);
    }

    /**
     * Verifica si la palabra ha sido completamente revelada.
     *
     * @return {@code true} si la palabra está completamente descubierta, {@code false} en caso contrario.
     */
    public boolean isRevealed() {
        return Arrays.equals(characters, hiddenWord);
    }

    private char[] toFilteredCharArray(String wordText) {
        StringBuilder filtered = new StringBuilder();

        for (char character : wordText.toCharArray()) {
            if (isValidCharacter(character)) {
                filtered.append(character);
            }
        }

        return filtered.toString().toCharArray();
    }

    private char[] toHiddenCharArray(char[] characters) {
        char[] hidden = new char[characters.length];

        for (int i = 0; i < characters.length; i++) {
            hidden[i] = HIDE_CHARACTER;
        }

        return hidden;
    }

    private boolean isValidCharacter(char character) {
        for (char c : CHARACTERS_BLACKLIST) {
            if (c == character) {
                return false;
            }
        }
        return true;
    }

    public static String getCharactersBlacklist() {
        StringBuilder builder = new StringBuilder();

        for (char c : CHARACTERS_BLACKLIST) {
            builder.append(c);
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Word{" +
                "text='" + text + '\'' +
                ", characters=" + new String(characters) +
                ", hiddenWord=" + new String(hiddenWord) +
                '}';
    }
}
