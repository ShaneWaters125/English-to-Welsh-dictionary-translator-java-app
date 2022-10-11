package uk.ac.aber.cs221.gp15.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.ArrayList;
import java.util.Map;

/**
 * Tests for Dictionary Methods
 *
 * @author mac127
 * @version 3.5
 * @since 3.5
 */
public class mac127_tests_wb_dictionary {

   /**
    * Tests for an empty load into the dictionary
    */
   @Test
   public void testEmptyFill() {
      ArrayList<Word> emptyWords = new ArrayList<Word>();
      Dictionary d = new Dictionary();
      d.fill(emptyWords);
      Assertions.assertEquals(0, d.getDictionaryList(true).size());
      Assertions.assertEquals(0, d.getDictionaryList(false).size());
   }

   @Test
   public void testSearchMap() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addWord(w);
      Map<String, Word> testMap = d.search("add", true, false);
      Assertions.assertEquals(1, testMap.size());
   }

   @Test
   public void testAddWord() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addWord(w);
      Assertions.assertEquals(1, d.getDictionaryList(true).size());
      Assertions.assertEquals(1, d.getDictionaryList(false).size());
   }

   @Test
   public void testAddPartNULLWord() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", null, null);
      try {
         d.addWord(null);
         Assertions.fail("Word successfully added to list with nulls");
      } catch (RuntimeException NullPointerException) {
         Assertions.assertTrue(true);
      }
   }

   @Test
   public void testRemoveWord() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addWord(w);
      d.removeWord(w);
      Assertions.assertEquals(0, d.getDictionaryList(true).size());
      Assertions.assertEquals(0, d.getDictionaryList(false).size());
   }

   /**
    * Test to see if remove bugs out when removing a word
    */
   @Test
   public void testRemoveNoWord() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.removeWord(w);
      Assertions.assertTrue(true);
   }


   @Test
   public void testGetEnglish() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addWord(w);
      Map<String, Word> testMap = d.getEnglishWords();
      Assertions.assertEquals(1, testMap.size());
      Map<String, Word> testMap1 = d.getWelshWords();
      Assertions.assertEquals(1, testMap1.size());
   }

   @Test
   public void testAddPractice() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addToPracticeList(w);
      Map<String, Word> testMap = d.getPracticeList(true);
      Map<String, Word> testMap1 = d.getPracticeList(false);
      Assertions.assertEquals(1, testMap.size());
      Assertions.assertEquals(1, testMap1.size());
   }

   @Test
   public void testRemovePractice() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addToPracticeList(w);
      d.removeFromPracticeList(w);
      Map<String, Word> testMap = d.getPracticeList(true);
      Map<String, Word> testMap1 = d.getPracticeList(false);
      Assertions.assertEquals(0, testMap.size());
      Assertions.assertEquals(0, testMap1.size());
      testMap = d.getEnglishWords();
      testMap1 = d.getWelshWords();
      Assertions.assertEquals(1, testMap.size());
      Assertions.assertEquals(1, testMap1.size());
   }

   @Test
   public void testGetPractice() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addToPracticeList(w);
      Map<String, Word> testMap = d.getPracticeList(true);
      Assertions.assertEquals(1, testMap.size());
   }

   @Test
   public void testGetDictionary() {
      Dictionary d = new Dictionary();
      Word w = new Word("add", "add", "noun");
      d.addWord(w);
      Map<String, Word> testMap = d.getDictionaryList(true);
      Assertions.assertEquals(1, testMap.size());
   }

   @Test
   public void testInvalidWord() {
      Word w = new Word("(*&76", "Utter Gibberish", "verb");
      Dictionary d = new Dictionary();
      d.addWord(w);
      if (d.getEnglishWords().size() == 1) {
         Assertions.fail("Word with invalid casing entered and successfully added to dictionary");
      }
   }
}
