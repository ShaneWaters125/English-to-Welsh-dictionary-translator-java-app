package uk.ac.aber.cs221.gp15.tests;

import uk.ac.aber.cs221.gp15.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for creating tests given the data within the dictionary. It is designed to handle
 * everything you would need for a test so there is no overhead anywhere else.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 0.1 (27/04/2020) Initial creation of class.
 * @version 1.0 (28/04/2020) Finalisation of class.
 */

public class TestBuilder {

    public TestBuilder() {
    }

    /**
     * Generate a test of a given type. NOTE: in all types, the correct answer is always the first answer in the answer list - test.getAnswer().get(0)
     *
     * @param dictionary Dictionary of all loaded words.
     * @param type       Test type generate.
     * @return NULL if test could not be generated (too few words in practice list) else the generated test is returned.
     */
    public Test createTest(Dictionary dictionary, TestType type) {
        Test test = null;

        //Checks to see if there are enough words in the dictionary.
        if (dictionary.getEnglishWords().size() <= 6) {
            return null;
        }
        try {
            switch (type) {
                case MultipleChoice:
                    test = new MultiChoiceTest(dictionary);
                    break;
                case SingleWord:
                    test = new SingleWordTest(dictionary);
                    break;
                case MatchWords:
                    test = new MatchWordsTest(dictionary);
            }
            //NullPointerException is thrown in tests if they are unable to generate a test.
        } catch (NullPointerException e) {
            return null;
        }
        test.generateQuestion();
        return test;
    }

    /**
     * Generate a full test which consists of multiple tests. The types of tests included are determined by how
     * many words are available within the practice list.
     *
     * @param dictionary   Dictionary of all loaded words.
     * @param numQuestions The amount of tests which are included within the full test.
     * @return A list of all the tests which have been generated.
     */
    public List<Test> createFullTest(Dictionary dictionary, int numQuestions) {
        Test test;
        List<Test> tests = new ArrayList<>();
        int index;
        int testTypesAvailable;
        // Depending on the amount of words available within the practice list (size), only certain tests can be
        // generated as some tests require a certain of words. If no words are available, a null will be returned and
        // handled as an exception later. The smaller testTypesAvailable gets the tests which involve a large amount of
        // words is no longer available.
        if (dictionary.getPracticeList(true).size() == 0) {
            return null;
        } else if (dictionary.getPracticeList(true).size() >= 4) {
            testTypesAvailable = 3;
        } else {
            testTypesAvailable = 2;
        }

        for (int i = 0; i < numQuestions; i++) {
            index = (int) Math.floor(Math.random() * testTypesAvailable) + 1;
            switch (index) {
                case 1:
                    test = createTest(dictionary, TestType.SingleWord);
                    break;
                case 2:
                    test = createTest(dictionary, TestType.MultipleChoice);
                    break;
                case 3:
                    test = createTest(dictionary, TestType.MatchWords);
                    break;
                default:
                    return null;
            }
            tests.add(test);
        }
        return tests;
    }

}
