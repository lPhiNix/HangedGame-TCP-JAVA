package common.model;

import java.util.Arrays;

public class Word {
    private static final char[] CHARACTERS_BLACKLIST = {',', '.', '!', '?', ';', ':'};
    private static final char HIDE_CHARACTER = '_';
    private final String text;
    private final char[] characters;
    private final char[] hiddenWord;

    public Word(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        this.text = text;
        this.characters = toFilteredCharArray(text);
        this.hiddenWord = toHiddenCharArray(this.characters);
    }

    public String getText() {
        return text;
    }

    public char[] getCharacters() {
        return characters.clone();
    }

    public char[] getHiddenWord() {
        return hiddenWord.clone();
    }

    public boolean guessConsonant(char c) {
        if (!isConsonant(c)) return false;
        return revealCharacter(c);
    }

    public boolean guessVowel(char c) {
        if (!isVowel(c)) return false;
        return revealCharacter(c);
    }

    private boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }

    private boolean isConsonant(char c) {
        return Character.isLetter(c) && !isVowel(c);
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

    public boolean guessCharacter(char c) {
        boolean guessedCorrectly = false;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == c) {
                hiddenWord[i] = c;
                guessedCorrectly = true;
            }
        }
        return guessedCorrectly;
    }

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
            hidden[i] = HIDE_CHARACTER; // Use an underscore to represent hidden characters
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
