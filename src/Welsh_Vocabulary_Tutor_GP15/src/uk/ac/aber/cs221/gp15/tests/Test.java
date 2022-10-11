package uk.ac.aber.cs221.gp15.tests;

import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.List;

/**
 * The interface for a test. All tests are similar but will have varying generate question methods.
 * The interface is mainly used to allow polymorphism.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 1.0 (28/04/2020) Initial creation and finalisation.
 */

public interface Test {

    /**
     * Selects a random word out the practice list.
     *
     * @return Returns the word randomly selected out the practice list.
     */

    Word getRandomWord(boolean fromPracticeList);

    /**
     * Gets all the answers for the test. This includes possible answers and the actual answer.
     *
     * @return Returns all the answers for a test.
     */

    List<String> getAnswer();

    /**
     *
     * @return All the questions for a test.
     */

    List<String> getQuestion();

    /**
     * Generates the questions and answers for a test. Will vary depending on the type of test but all tests
     * will make use of get random word.
     */

    void generateQuestion();


}
