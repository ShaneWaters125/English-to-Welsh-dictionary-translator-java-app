package uk.ac.aber.cs221.gp15.dictionary;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class is responsible for holding all the words loaded in the dictionary and providing methods to add and retrieve
 * data. All the words loaded from the JSON file are stored in TreeMaps, one for ordering in each language. A lot of helper
 * methods are included which are only utilised by the user interface.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @version 0.1 (10/02/2020) Initial creation of class. Basic TreeMap structure created.
 * @version 0.2 (13/04/2020) Changed getters and setters for the dictionary so the user interface can use it.
 * @version 0.3 (20/04/2020) Reworked searching to support both languages.
 * @version 0.4 (28/04/2020) Reworked getters and setters for dictionary to support practice list.
 * @version 1.0 (30/04/2020) Adding prefacing of verbs and finalisation of class.
 */

public class Dictionary {

    /**
     * Storing the Word objects created by the parser in tree maps. As tree maps only support one key value
     * there has to be two trees to search in both languages.
     */
    private TreeMap<String, Word> english_words = new TreeMap<>();
    private TreeMap<String, Word> welsh_words = new TreeMap<>();

    public Dictionary() {
    }

    /**
     * Takes all the Word objects which were parsed and stored them inside the tree maps.
     *
     * @param words List of all the words which were parsed.
     */

    public void fill(List<Word> words) {
        if (words == null) {
            return;
        }
        for (Word word : words) {
            //Removing whitespace around words.
            word.setEnglish(word.getEnglish().trim());
            word.setWelsh(word.getWelsh().trim());
            String ekey = word.getEnglish().toLowerCase();
            String wkey = word.getWelsh().toLowerCase();
            if(word.getWordType().equals("verb")){
                word.setEnglish("to " + word.getEnglish());
            }
            english_words.put(ekey, word);
            welsh_words.put(wkey, word);
        }
    }

    /**
     * Returns a map containing the words which match the prefix param.
     *
     * @param prefix       Part of the word which will be searched.
     * @param english      Determines if the search will be in English or Welsh (True of False respectively).
     * @param practiceList Determines if the dictionary or the practice list will be searched.
     */

    public Map<String, Word> search(String prefix, boolean english, boolean practiceList) {
        Map<String, Word> searched_map;
        if (practiceList) {
            searched_map = findPrefix(getPracticeList(english), prefix);
        } else {
            searched_map = findPrefix(getDictionaryList(english), prefix);
        }

        return searched_map;
    }

    /**
     * As the TreeMap is a sorted map, the submap function can be used to take a 'branch' from the Map which contains
     * words with the prefix.
     *
     * @param baseMap The map being searched which holds all the Word objects.
     * @param prefix  The word being searched for within the map.
     * @return A map of all the words which follow the prefix.
     */

    private static SortedMap<String, Word> findPrefix(SortedMap<String, Word> baseMap, String prefix) {
        prefix = prefix.toLowerCase();
        if (prefix.length() > 0) {
            //Getting the next character which comes after the final character in the prefix ('a'->'b', 'ab'->'c')
            char nextLetter = (char) (prefix.charAt(prefix.length() - 1) + 1);
            //The branch will end when the last character of the prefix changes to the character which comes after it
            //so the last character of the prefix is replaced with the next character to give the end bound of the submap. ('ab'->'ac')
            String end = prefix.substring(0, prefix.length() - 1) + nextLetter;
            /*The submap of the map is the 'branch' which the prefix starts at, everything on this branch will be
            words starting with the prefix.*/
            return baseMap.subMap(prefix, end);
        }
        return baseMap;
    }

    /**
     * Adds a new word into the dictionary.
     *
     * @param word The new word which is added to the dictionary.
     */

    public void addWord(Word word) {
        String ekey = word.getEnglish();
        if(word.getWordType().equals("verb")){
            word.setEnglish("to " + word.getEnglish());
        }
        english_words.put(ekey, word);
        welsh_words.put(word.getWelsh(), word);
    }

    /**
     * Removes a word from the dictionary.
     *
     * @param word The word to be removed.
     */

    public void removeWord(Word word) {
        english_words.remove(word.getEnglish());
        welsh_words.remove(word.getWelsh());

    }

    /**
     * Gets all the words sorted alphabetically in English.
     *
     * @return a TreeMap containing all the words sorted in English.
     */

    public TreeMap<String, Word> getEnglishWords() {
        return english_words;
    }

    /**
     * Gets all the words sorted alphabetically in Welsh
     *
     * @return a TreeMap containing all the words sorted in Welsh.
     */

    public TreeMap<String, Word> getWelshWords() {
        return welsh_words;
    }

    /**
     * Marks a word so it appears in the users practice list then updates the TreeMaps.
     *
     * @param word The word to be added to the users practice list.
     */

    public void addToPracticeList(Word word) {
        word.setMarked(true);
        String ekey = word.getEnglish();
        if(word.getWordType().equals("verb")){
            ekey = word.getEnglish().substring(3);
        }
        english_words.put(ekey, word);
        welsh_words.put(word.getWelsh(), word);
    }

    /**
     * Unmarks a word so it no longer appears in the users practice list then updates the TreeMaps.
     *
     * @param word The word to be removed from the users practice list.
     */

    public void removeFromPracticeList(Word word) {
        word.setMarked(false);
        String ekey = word.getEnglish();
        if(word.getWordType().equals("verb")){
            ekey = word.getEnglish().substring(3);
        }
        english_words.put(ekey, word);
        welsh_words.put(word.getWelsh(), word);
    }

    /**
     * Gets the entire practice list ordered in English or Welsh.
     *
     * @param english Determines whether to get the practice list ordered in English or Welsh.
     * @return A TreeMap containing the entire practice list.
     */

    public TreeMap<String, Word> getPracticeList(boolean english) {
        TreeMap<String, Word> temp = new TreeMap<>();
        if (english) {
            for (Map.Entry<String, Word> entry : english_words.entrySet()) {
                if (entry.getValue().isMarked()) {
                    temp.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            for (Map.Entry<String, Word> entry : welsh_words.entrySet()) {
                if (entry.getValue().isMarked()) {
                    temp.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return temp;
    }

    /**
     * Gets the entire dictionary ordered in English or Welsh.
     *
     * @param english Determines whether to get the dictionary ordered in English or Welsh.
     * @return A TreeMap containing the entire dictionary.
     */

    public TreeMap<String, Word> getDictionaryList(boolean english) {
        TreeMap<String, Word> temp = new TreeMap<>();
        if (english) {
            for (Map.Entry<String, Word> entry : english_words.entrySet()) {
                //If a word is not marked then it is not in the users practice list so it is part of the dictionary.
                if (!entry.getValue().isMarked()) {
                    temp.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            for (Map.Entry<String, Word> entry : welsh_words.entrySet()) {
                if (!entry.getValue().isMarked()) {
                    temp.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return temp;
    }

}
