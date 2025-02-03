package common.model;

import java.util.ArrayList;
import java.util.List;

public class Proverb {
    private final List<Object> elements;
    private final String text;

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

    public boolean resolveProverb(String guess) {
        return text.equalsIgnoreCase(guess);
    }

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

    public String getText() {
        return text;
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
