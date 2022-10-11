package uk.ac.aber.cs221.gp15.tests;

import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * The SingleWordTest which retrieves a single random word from the users practice list.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 1.0 (28/04/2020) Initial creation and finalisation.
 */

public class SingleWordTest implements Test {

    /**List of words loaded from the dictionary**/
    private List<Word> words;
    private List<String> question = new ArrayList<>();
    private List<String> answer = new ArrayList<>();
    private boolean isEnglish;

    /**
     * Constructor for SingleWordTest. Stores all the words when it is created and checks if it can create a test,
     * if not then NullPointer is thrown and the exception is handled later.
     *
     * @param dictionary All the words loaded within the program.
     */

    public SingleWordTest(Dictionary dictionary){
        words = new ArrayList<>(dictionary.getPracticeList(true).values());
        if(words.size() == 0){
            throw new NullPointerException();
        }
        isEnglish = Math.random() > 0.5;
    }

    /**
     * Gets a random word from the dictionary, the word is also removed so there will be no duplicates
     * (Not applicable for this specific test as it only has one word.)
     *
     * @return A random word from the dictionary.
     */

    @Override
    public Word getRandomWord(boolean fromPracticeList) {
        int index = (int) Math.floor(Math.random()*words.size());
        Word word = words.get(index);
        words.remove(index);
        return word;
    }

    /**
     *
     * @return Gets all the possible answers and the actual answer.
     */

    @Override
    public List<String> getAnswer() {
        return answer;
    }

    /**
     *
     * @return Gets all the questions.
     */

    @Override
    public List<String> getQuestion() {
        return question;
    }

    /**
     * Generates the question. For a SingleWordTest it will select a random word in either english or welsh then set
     * the question and answer variables.
     */

    @Override
    public void generateQuestion() {
        if(isEnglish){
            Word word = getRandomWord(true);
            question.add(word.getEnglish());
            answer.add(word.getWelsh());
        } else{
            Word word = getRandomWord(true);
            question.add(word.getWelsh());
            answer.add(word.getEnglish());
        }
    }
}
