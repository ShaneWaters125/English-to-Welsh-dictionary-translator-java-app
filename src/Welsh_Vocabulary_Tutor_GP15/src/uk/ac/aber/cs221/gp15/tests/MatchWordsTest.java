package uk.ac.aber.cs221.gp15.tests;

import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * The MatchWordsTest which retrieves 4 random words from the practice list and will ask the user to match them together.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 1.0 (28/04/2020) Initial creation and finalisation.
 */

public class MatchWordsTest implements Test {

    private List<Word> words;
    private List<String> question = new ArrayList<>();
    private List<String> answer = new ArrayList<>();

    /**
     * Constructor for MatchWordsTest. Stores all the words when it is created and checks if it can create a test,
     * if not then NullPointer is thrown and the exception is handled later.
     *
     * @param dictionary All the words loaded within the program.
     */

    public MatchWordsTest(Dictionary dictionary){
        words = new ArrayList<>(dictionary.getPracticeList(true).values());
        if(words.size()< 4){
            throw new NullPointerException();
        }
    }

    /**
     * Gets a random word from the dictionary, the word is also removed so there will be no duplicates
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
     * Generates the question. For a MatchWordsTest it will select 4 random words from the practice list and
     * store them in order so question[0] answer is answer[0].
     */

    @Override
    public void generateQuestion() {
        Word word;
        for(int i=0; i < 4; i++){
            word = getRandomWord(true);
            question.add(word.getEnglish());
            answer.add(word.getWelsh());
        }
    }
}
