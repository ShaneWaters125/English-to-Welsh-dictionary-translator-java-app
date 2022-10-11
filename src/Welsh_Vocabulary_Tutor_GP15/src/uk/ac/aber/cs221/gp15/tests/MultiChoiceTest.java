package uk.ac.aber.cs221.gp15.tests;

import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * The MultiChoiceTest which retrieves 6 random words from the practice list and selects one as the question with its
 * corresponding translation as the answer.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 1.0 (28/04/2020) Initial creation and finalisation.
 */

public class MultiChoiceTest implements Test {

    private List<Word> pwords;
    private List<Word> dwords;
    private List<String> question = new ArrayList<>();
    private List<String> answer = new ArrayList<>();
    private boolean isEnglish;

    /**
     * Constructor for MultiChoiceTest. Stores all the words when it is created and checks if it can create a test,
     * if not then NullPointer is thrown and the exception is handled later.
     *
     * @param dictionary All the words loaded within the program.
     */

    public MultiChoiceTest(Dictionary dictionary) {
        //Practice words
        pwords = new ArrayList<>(dictionary.getPracticeList(true).values());
        //Dictionary words
        dwords = new ArrayList<>(dictionary.getEnglishWords().values());
        if (pwords.size() < 1 || dwords.size() < 6) {
            throw new NullPointerException();
        }
        isEnglish = Math.random() > 0.5;
    }

    /**
     * Gets a random word from the dictionary, the word is also removed so there will be no duplicates
     *
     * @return A random word from the dictionary.
     */

    @Override
    public Word getRandomWord(boolean fromPracticeList) {
        Word word;
        if (fromPracticeList) {
            int index = (int) Math.floor(Math.random() * pwords.size());
            word = pwords.get(index);
            pwords.remove(index);
        } else {
            int index = (int) Math.floor(Math.random() * dwords.size());
            word = dwords.get(index);
            dwords.remove(index);
        }
        return word;
    }

    /**
     * @return Gets all the possible answers and the actual answer.
     */

    @Override
    public List<String> getAnswer() {
        return answer;
    }

    /**
     * @return Gets all the questions.
     */

    @Override
    public List<String> getQuestion() {
        return question;
    }

    /**
     * Generates the question. For a MultiChoiceTest it will select a random word in either english or welsh from the
     * practice list then the other 5 words will be selected at random from all the words available within the dictionary.
     */

    @Override
    public void generateQuestion() {
        Word word = getRandomWord(true);
        if (isEnglish) {
            answer.add(word.getEnglish());
            question.add(word.getWelsh());
            for (int i = 0; i < 5; i++) {
                word = getRandomWord(false);
                answer.add(word.getEnglish());
            }
        } else {
            answer.add(word.getWelsh());
            question.add(word.getEnglish());
            for (int i = 0; i < 5; i++) {
                word = getRandomWord(false);
                answer.add(word.getWelsh());
            }
        }
    }
}
