package uk.ac.aber.cs221.gp15.dictionary;

/**
 * This class is responsible for holding all the data for a given word. It stores all the translations
 * of a word and whether or not is is part of the practice list or dictionary.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @version 0.1 (10/02/2020) Initial creation of class.
 * @version 0.2 (20/02/2020) Added support for practice list.
 * @version 1.0 (28/04/2020) Finalisation of class.
 */

public class Word {

    private String english;
    private String welsh;
    private String wordType;
    private boolean marked;

    public Word(){}

    /**
     * Constructor for Word.
     *
     * @param english The English translation of the word.
     * @param welsh The Welsh translation of the word.
     * @param wordType The wordtype of a word.
     */

    public Word(String english, String welsh, String wordType){
        this.english = english;
        this.welsh = welsh;
        this.wordType = wordType;
    }

    /**
     *
     * @return The English translation of the word.
     */

    public String getEnglish() {
        return english;
    }

    /**
     *
     * @param english The English translation to be set.
     */

    public void setEnglish(String english) {
        this.english = english;
    }

    /**
     *
     * @return The Welsh translation of the word.
     */

    public String getWelsh() {
        return welsh;
    }

    /**
     *
     * @param welsh The Welsh translation to be set.
     */

    public void setWelsh(String welsh) {
        this.welsh = welsh;
    }

    /**
     *
     * @return The wordtype of the word.
     */

    public String getWordType() {
        return wordType;
    }

    /**
     *
     * @param wordtype The wordtype to be set.
     */

    public void setWordtype(String wordtype) {
        this.wordType = wordType;
    }

    /**
     *
     * @return If the word part of the users practice list.
     */

    public boolean isMarked() {
        return marked;
    }

    /**
     *
     * @param marked Determines whether the word is part of the users practice list.
     */

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", welsh='" + welsh + '\'' +
                ", wordtype='" + wordType + '\'' +
                '}';
    }
}
