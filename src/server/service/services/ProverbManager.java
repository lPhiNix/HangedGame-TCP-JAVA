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

public class ProverbManager implements Service {
    private static final Logger logger = CustomLogger.getLogger(ProverbManager.class.getName());
    private static final String FILE_PATH = "proverbs.txt";
    private static final String ABC = "abcdefghijklnmñopqrstuvwxyz";
    private Map<Integer, String> proverbs;

    public ProverbManager() {
        proverbs = loadProverbs();
    }

    private Map<Integer, String> loadProverbs() {
        File proverbsFile = new File(FILE_PATH);

        if (!proverbsFile.exists()) {
            logger.log(Level.SEVERE, "File not found: {0}", FILE_PATH);
        }

        Map<Integer, String> proverbsMap = new HashMap<>();

        try (BufferedReader bufferedWriter = new BufferedReader(new FileReader(proverbsFile))) {
            int lineCount = 0;
            String fileLine;

            while ((fileLine = bufferedWriter.readLine()) != null) {

                if (!checkProverbLine(fileLine)) {
                    logger.log(Level.WARNING, "Invalid proverb at line {0}: {1}", new Object[]{lineCount + 1, fileLine});
                    continue;
                }

                proverbsMap.put(lineCount, fileLine);
                lineCount++;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading proverbs file", e);
        }

        return proverbsMap;
    }

    private boolean checkProverbLine(String proverbText) {
        char[] proverbCharacters = proverbText.toLowerCase().toCharArray();

        for (char proverbCharacter : proverbCharacters) {
            if (!isValidCharacter(proverbCharacter)) {
                logger.log(Level.WARNING, "Invalid character: {0}", proverbCharacter);
                return false;
            }
        }

        return true;
    }

    private boolean isValidCharacter(char character) {
        boolean flag = false;
        char[] abc = ABC.toCharArray();

        for (char c : abc) {
            if (character == c) {
                flag = true;
            }
        }

        for (char c : Word.getCharactersBlacklist().toCharArray()) {
            if (character == c || character == ' ') {
                return true;
            }
        }

        return flag;
    }

    public Proverb createProverb(String text) {
        return new Proverb(text);
    }

    public Proverb createProverb(int index) {
        return new Proverb(proverbs.get(index));
    }

    public Map<Integer, String> getProverbs() {
        return proverbs;
    }

    public void reloadProverbs() {
        proverbs = loadProverbs();
    }
}
