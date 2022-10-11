package uk.ac.aber.cs221.gp15.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.aber.cs221.gp15.BaseApplication;
import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.Map;

/**
 * Tests for Functional Requirements 1-4 - Requires Clean Unused dictionary.json
 *
 * @author mac127
 * @version 3.5
 * @since 2.0
 */

public class mac127_tests_fr1_4 {


   /**
    * Tests insertion and trimming of values
    *
    * @param "English: 'add ' Welsh: ' add' WordType: '\tnoun '"
    */
   @Test
   public void testAddWordWithNoWhiteSpace() {
      //creates dictionary to add words to
      Dictionary d = new Dictionary();

      //create word -  english, welsh and wordType parameters in that order
      Word w = new Word("add ", " add", "\tnoun ");
      d.addWord(w);

      //Assertions are how you test for validity in the data types e.g. string comparisons, the amount of values in a
      //list, etc.
      Map<String, Word> testMap = d.search("add", true, false);
      Word testWord = testMap.get(w.getEnglish());


      Assertions.assertEquals("add", testWord.getEnglish());
      Assertions.assertEquals("add", testWord.getWelsh());
      Assertions.assertEquals("noun", testWord.getWordType());
   }

   /**
    * Tests searching of dictionary for words
    *
    * @param "English: 'hey' Welsh: 'hey' WordType: 'noun'"
    * @param "English: 'no' Welsh: 'no' WordType: 'verb'"
    */
   @Test
   public void testSearchListSingle() {
      //dictionary initialisation and word generation
      Dictionary d = new Dictionary();
      Word w = new Word("hey", "hey", "noun");
      Word w1 = new Word("no", "no", "verb");

      //adding words to the list
      d.addWord(w);
      d.addWord(w1);

      //producing maps/arrays of values to checked for length
      Map<String, Word> test1 = d.search("hey", true, false);
      Map<String, Word> test2 = d.search("no", false, false);

      //length testing
      Assertions.assertEquals(1, test1.size());
      Assertions.assertEquals(1, test2.size());
   }

   /**
    * Tests multiple searching of the dictionary for various words
    *
    * @param "English: 'hey' Welsh: 'hey' WordType: 'verb'"
    * @param "English: 'no' Welsh: 'no' WordType: 'verb'"
    * @param "English: 'hey' Welsh: 'no1' WordType: 'noun'"
    */
   @Test
   public void testSearchListMultiple() {
      //test 2 and 3
      //dictionary and word initialisation
      Dictionary d = new Dictionary();
      Word w = new Word("hey", "hey", "verb");
      Word w1 = new Word("no", "no", "verb");
      Word w2 = new Word("hey", "nol", "noun");
      d.addWord(w);
      d.addWord(w1);
      d.addWord(w2);

      //searching the dictionary to count the instances of "hey" and testing the length of the map against expected
      Map<String, Word> test1 = d.search("hey", true, false);
      Assertions.assertEquals(1, test1.size());

      //searching the dictionary to check for the instances of "no" and testing the number of instances
      Map<String, Word> test2 = d.search("no", false, false);
      Assertions.assertEquals(2, test2.size());
   }

   /**
    * Tests duplicate additions of words
    *
    * @param "English: 'hey' Welsh: 'hey' WordType: 'verb'"
    * @param "English: 'hey' Welsh: 'hey' WordType: 'verb'"
    */
   @Test
   public void testDuplicateAdd() {
      //test 4
      //dictionary and word initialisation
      Dictionary d = new Dictionary();
      Word w = new Word("hey", "hey", "verb");
      Word w1 = new Word("hey", "hey", "verb");
      d.addWord(w);
      d.addWord(w1);

      //tests instances are not overlapping
      Assertions.assertEquals(d.getEnglishWords().size(), 1);
      Assertions.assertEquals(d.getWelshWords().size(), 1);
   }

   /**
    * Tests the dictionary will open empty with no autofilled words
    */
   @Test
   public void testEmptyBoot() {
      //test 6
      //initialisation of an empty dictionary
      Dictionary d = new Dictionary();

      //testing that the dictionary is empty
      Assertions.assertEquals(0, d.getEnglishWords().size());
      Assertions.assertEquals(0, d.getWelshWords().size());
   }

   /**
    * Tests error exceptions for nulls
    *
    * @param "English: 'hey' Welsh: 'null' WordType: 'bye'"
    */
   @Test
   public void testNull() {
      //test 5
      //initialisation
      boolean thrown = false;
      try {
         //creating word and seeing if the system will allow for an invalid word to be added
         Dictionary d = new Dictionary();
         Word w = new Word("hey", null, "bye");
         d.addWord(w);
         Assertions.fail("addWord() was called and successfully added the word with nulls in its values");
      } catch (RuntimeException e) {
         thrown = true;
      }
      //tests to check the response from the system
      Assertions.assertTrue(thrown);
   }

   /**
    * Tests importation of json
    */
   @Test
   public void testJsonImport() {
      //test 1 and test 6
      BaseApplication baseApplication = new BaseApplication();
      //loads application
      try {
         //checks for empty dictionaries
         Assertions.assertEquals(0, baseApplication.getDictionary().getDictionaryList(true).size());
         Assertions.assertEquals(0, baseApplication.getDictionary().getDictionaryList(false).size());
         baseApplication.start();
         //loads dictionary and checks that it isn't empty
         Assertions.assertNotEquals(null, baseApplication.getDictionary());
         Assertions.assertNotEquals(0, baseApplication.getDictionary().
                 getDictionaryList(true).size());
         Assertions.assertNotEquals(0, baseApplication.getDictionary().getDictionaryList(false));
      } catch (Exception e) {
         Assertions.fail("start() fails to import dictionary successfully");
      }
   }

   /**
    * Tests that the tree structure is maintained regardless of contact with it
    */
   @Test
   public void testTreeOrdering() {
      //test 9
      BaseApplication baseApplication = new BaseApplication();
      BaseApplication baseApplication1 = new BaseApplication();
      try {
         baseApplication.start();
         baseApplication1.start();
         Dictionary dictionary = new Dictionary();
         dictionary = baseApplication.getDictionary();
         Dictionary dictionary1 = new Dictionary();
         dictionary1 = baseApplication1.getDictionary();
         Assertions.assertEquals(dictionary.getDictionaryList(true).keySet(),
                 dictionary1.getDictionaryList(true).keySet());
         Assertions.assertEquals(dictionary.getDictionaryList(false).keySet(),
                 dictionary1.getDictionaryList(false).keySet());
      } catch (Exception e) {
         Assertions.fail("Dictionaries do not match");
      }
   }

   /**
    * Tests for searching the dictionary
    *
    * @param "prefix:'add' english:'true' testList:'false'"
    */
   @Test
   public void testSingleSearch() {
      //test 11
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Map<String, Word> testMap = baseApplication.getDictionary().search("add", true, false);
      //System.out.println(testMap.size());
      Assertions.assertEquals(3, testMap.size());
   }

   /**
    * Tests to see if the system will return any values for * as a search param
    *
    * @param "*"
    */
   @Test
   public void testInvalidSearch() {
      //test 12
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Map<String, Word> testMap = baseApplication.getDictionary().search("*", true, false);

      Assertions.assertEquals(0, testMap.size());
   }

   /**
    * tests for the existence of a word which does not exist
    *
    * @param "prefix:'addenism' english:'true' testList:'false'"
    */
   @Test
   public void testNonExistWord() {
      //test 14
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Map<String, Word> testMap = baseApplication.getDictionary().search("addenism",
              false, false);
      Assertions.assertEquals(0, testMap.size());
   }

   /**
    * tests for the amount of expected responses based upon a given word
    *
    * @param "prefix:'add' english:'true' testList:'false'"
    */
   @Test
   public void testWordFindEnglish() {
      //test 15
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Map<String, Word> testMap = baseApplication.getDictionary().search("add",
              true, false);
      Assertions.assertEquals(3, testMap.size());
   }

   /**
    * tests for a word in welsh and the amount of expected responses
    *
    * @param "prefix:'ab' english:'false' testList:'false'"
    */
   @Test
   public void testWordFindWelsh() {
      //test 16
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Map<String, Word> testMap = baseApplication.getDictionary().search("ab", false, false);
      Assertions.assertEquals(1, testMap.size());
   }

   /**
    * tests for adding a new word and testing the size of the overall dictionary
    *
    * @param "english:'addenism' welsh:'true' wordType:'false'"
    */
   @Test
   public void testValidWordAdd() {
      //test 21
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      Word testWord = new Word("adds (numbers)", "adios", "verb");
      int dioSizeEng = baseApplication.getDictionary().getDictionaryList(true).size();
      int dictSizeWelsh = baseApplication.getDictionary().getDictionaryList(false).size();
      baseApplication.getDictionary().addWord(testWord);
      Assertions.assertEquals(dioSizeEng + 1, baseApplication.getDictionary()
              .getDictionaryList(true).size());
      Assertions.assertEquals(dictSizeWelsh + 1, baseApplication.getDictionary()
              .getDictionaryList(false).size());
   }

   /**
    * tests for invalid word on app level and not dictionary level
    *
    * @param "english:'adds (numbers)' welsh:'null' wordType:'hjbiybdyb'"
    */
   @Test
   public void testInvalidWord() {
      //test 5 extended
      BaseApplication baseApplication = new BaseApplication();
      baseApplication.start();
      int dictSize = 0;
      try {
         Word testWord = new Word("adds (numbers)", null, "hjbiybdyb");
         dictSize = baseApplication.getDictionary().getDictionaryList(true).size();
         baseApplication.getDictionary().addWord(testWord);
         if (dictSize < baseApplication.getDictionary().getDictionaryList(true).size()) {
            Assertions.fail("Word with invalid casings passes through into the dictionary");
         }
      } catch (RuntimeException e) {
      }
      if (dictSize != baseApplication.getDictionary().getDictionaryList(true).size()) {
         Map<String, Word> betaTest = baseApplication.getDictionary().search("adds (numbers)", true, false);
         if (betaTest.size() == 1)
            Assertions.fail("Error handled but not excluded from list");
      }
   }
}


