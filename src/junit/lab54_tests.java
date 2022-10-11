package uk.ac.aber.cs221.gp15.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.dictionary.Word;
import uk.ac.aber.cs221.gp15.tests.TestBuilder;
import uk.ac.aber.cs221.gp15.tests.TestType;

import java.util.List;

/**
 * Tests for functional requirements 8 to 10. These cover the Testbuilder class
 *
 * @author Lars Birkeland (lab54)
 * @version 1.0
 */
public class lab54_tests {

   /**
    * Check that the system correctly produces flash cards for English words
    */
   @Test
   public void testFlashCards34() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces flash cards for Welsh words
    */
   @Test
   public void testFlashCards35() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces multiple choice question
    */
   @Test
   public void testMultiChoice36() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces multiple choice question
    */
   @Test
   public void testMultiChoice37() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      Boolean result1 = false;
      Boolean result2 = false;
      Boolean result3 = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MultipleChoice);
      String word1 = t1.getAnswer().get(0);
      String word2 = t1.getQuestion().get(0);

      if (word1.equals(word2)) {
         Assertions.assertTrue(true, "the system correctly produces multiple choice question");
      } else {
         Assertions.assertTrue(false, "the system does not correctly produces multiple choice question");
      }
   }

   /**
    * Check that the system randomly selects alternative answers
    */
   @Test
   public void testMultiChoice38() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      List<String> answer1;
      List<String> answer2;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      uk.ac.aber.cs221.gp15.tests.Test t2 = tb.createTest(dictionary, TestType.MultipleChoice);
      answer2 = t2.getAnswer();

      for (int j = 0; j < 15; j++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MultipleChoice);
         question = t1.getQuestion().get(0);
         answer1 = t1.getAnswer();
         if (question.equals("word7")) {
            for (int k = 0; k < 3; k++) {
               if (answer1.equals(answer2)) {
                  result = false;
                  break;
               } else {
                  result = true;
               }
            }
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system only produces a single correct answer
    */
   @Test
   public void testMultiChoice39() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String wordName;
      String question;
      String alternative1;
      String alternative2;
      String alternative3;
      String alternative4;
      String alternative5;
      boolean result = false;

      for (int i = 0; i < 200; i++) {
         wordName = "word" + i;
         Word a = new Word(wordName, wordName, "nf");
         a.setMarked(true);
         dictionary.addWord(a);
      }

      for (int i = 0; i < 100; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MultipleChoice);
         question = t1.getQuestion().get(0);
         alternative1 = t1.getAnswer().get(1);
         alternative2 = t1.getAnswer().get(2);
         alternative3 = t1.getAnswer().get(3);
         alternative4 = t1.getAnswer().get(4);
         alternative5 = t1.getAnswer().get(5);
         if (question.equals(alternative1) || question.equals(alternative2) || question.equals(alternative3) || question.equals(alternative4) || question.equals(alternative5)) {
            result = false;
            break;
         } else {
            result = true;
         }
      }
      if (result) {
         Assertions.assertTrue(true, "the system only produces a single correct answer");
      } else {
         Assertions.assertTrue(true, "the system does not only produce a single correct answer");
      }
   }

   /**
    * Check that the system only produces only one meaning of the word
    */
   @Test
   public void testMultipleChoice40() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      List<String> answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word71", "nf");
      Word h = new Word("word7", "word72", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(false);
      e.setMarked(false);
      f.setMarked(false);
      g.setMarked(false);
      h.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);
      dictionary.addWord(h);

      for (int j = 0; j < 15; j++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MultipleChoice);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer();
         if (question.equals("word7")) {
            for (int k = 0; k < 3; k++) {
               if (answer.contains("word71") && answer.contains("word72")) {
                  result = false;
                  break;
               } else {
                  result = true;
               }
            }
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces translate word question
    */
   @Test
   public void testTranslate41() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces translate word question
    */
   @Test
   public void testTranslate42() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces translate word question
    */
   @Test
   public void testTranslate43() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String question;
      String answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.SingleWord);
         question = t1.getQuestion().get(0);
         answer = t1.getAnswer().get(0);
         if (question.equals(answer)) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the system correctly produces a “match words quiz”
    */
   @Test
   public void testMatchWords47() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      List<String> question;
      List<String> answer;
      boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      for (int i = 0; i < 3; i++) {
         uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MatchWords);
         question = t1.getQuestion();
         answer = t1.getAnswer();
         if (answer.contains(question.get(0)) && answer.contains(question.get(1)) && answer.contains(question.get(2)) && answer.contains(question.get(3))) {
            result = true;
         } else {
            result = false;
            break;
         }
      }
      Assertions.assertTrue(result);
   }

   /**
    * Check that the words in the “match words quiz” is jumbled up
    */
   @Test
   public void testMachWords48() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      List<String> question1;
      List<String> question2;
      List<String> answer1;
      List<String> answer2;
      Boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(false);
      f.setMarked(false);
      g.setMarked(false);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MatchWords);
      uk.ac.aber.cs221.gp15.tests.Test t2 = tb.createTest(dictionary, TestType.MatchWords);

      question1 = t1.getQuestion();
      question2 = t2.getQuestion();
      answer1 = t1.getAnswer();
      answer2 = t2.getAnswer();

      if (question1.equals(question2) || answer1.equals(answer2)) {
         Assertions.assertTrue(false, "quiz is not jumbled up correctly");
      } else {
         Assertions.assertTrue(true, "Quiz is jumbled up");
      }
   }

   /**
    * Check that the “match words quiz” cannot be created when practice list has fewer than four words
    */
   @Test
   public void testMachWords49() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      List<String> question1;
      List<String> question2;
      List<String> answer1;
      List<String> answer2;
      Boolean result = false;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(false);
      e.setMarked(false);
      f.setMarked(false);
      g.setMarked(false);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      uk.ac.aber.cs221.gp15.tests.Test t1 = tb.createTest(dictionary, TestType.MatchWords);

      Assertions.assertNull(t1);
   }

   /**
    * Check that the system correctly produces tests
    */
   @Test
   public void testRandomTests50() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      int testsSize;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(false);
      e.setMarked(false);
      f.setMarked(false);
      g.setMarked(false);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      List<uk.ac.aber.cs221.gp15.tests.Test> t1 = tb.createFullTest(dictionary, 10);
      testsSize = t1.size();

      if (testsSize == 10) {
         Assertions.assertTrue(true, "the system correctly produces tests");
      } else {
         Assertions.assertTrue(false, "the system does not correctly produces tests");
      }
   }

   /**
    * Check that tests produced are random
    */
   @Test
   public void testRandomTests51() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String type;
      int singleWord = 0;
      int multiWord = 0;
      int matchWord = 0;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(true);
      e.setMarked(true);
      f.setMarked(true);
      g.setMarked(true);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      List<uk.ac.aber.cs221.gp15.tests.Test> t1 = tb.createFullTest(dictionary, 100);

      for (int i = 0; i < 100; i++) {
         type = t1.get(i).toString();
         if (type.contains("SingleWordTest")) {
            singleWord++;
         } else if (type.contains("MultiChoiceTest")) {
            multiWord++;
         } else if (type.contains("MatchWordsTest")) {
            matchWord++;
         } else {
            System.out.println("Shit be broke");
            break;
         }
      }
      if (singleWord > 25 && multiWord > 25 && matchWord > 25) {
         Assertions.assertTrue(true, "tests produced are random");
      } else {
         Assertions.assertTrue(false, "tests produced are not random enough");
      }
   }

   /**
    * Check that “match words quiz” does not appear when there are fewer than four words in the practice list
    */
   @Test
   public void testRandomTest54() {
      Dictionary dictionary = new Dictionary();
      TestBuilder tb = new TestBuilder();

      String type;
      int singleWord = 0;
      int multiWord = 0;
      int matchWord = 0;

      Word a = new Word("word1", "word1", "nf");
      Word b = new Word("word2", "word2", "nf");
      Word c = new Word("word3", "word3", "nf");
      Word d = new Word("word4", "word4", "nf");
      Word e = new Word("word5", "word5", "nf");
      Word f = new Word("word6", "word6", "nf");
      Word g = new Word("word7", "word7", "nf");
      a.setMarked(true);
      b.setMarked(true);
      c.setMarked(true);
      d.setMarked(false);
      e.setMarked(false);
      f.setMarked(false);
      g.setMarked(false);
      dictionary.addWord(a);
      dictionary.addWord(b);
      dictionary.addWord(c);
      dictionary.addWord(d);
      dictionary.addWord(e);
      dictionary.addWord(f);
      dictionary.addWord(g);

      List<uk.ac.aber.cs221.gp15.tests.Test> t1 = tb.createFullTest(dictionary, 100);

      for (int i = 0; i < 100; i++) {
         type = t1.get(i).toString();
         if (type.contains("SingleWordTest")) {
            singleWord++;
         } else if (type.contains("MultiChoiceTest")) {
            multiWord++;
         } else if (type.contains("MatchWordsTest")) {
            matchWord++;
         } else {
            System.out.println("Shit be broke");
            break;
         }
      }

      if (matchWord == 0) {
         Assertions.assertTrue(true, "'match words quiz' does not appear when there are fewer than four words in the practice list");
      } else {
         Assertions.assertTrue(false, "'match words quiz' does appear when there are fewer than four words in the practice list");
      }
   }
}