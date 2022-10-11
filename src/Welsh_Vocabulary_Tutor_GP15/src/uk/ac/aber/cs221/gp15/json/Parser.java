package uk.ac.aber.cs221.gp15.json;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import uk.ac.aber.cs221.gp15.dictionary.Word;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is responsible for parsing it JSON file. It uses the GSON library to open the JSON file and convert
 * each item in there into a word object.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @version 0.1 (10/02/2020) Initial creation of class.
 * @version 0.2 (29/02/2020) Added exception handling for missing or corrupt files.
 * @version 1.0 (30/04/2020) Handling verb prefacing and finalisation of class.
 */

public class Parser {

    public Parser() {
    }

    /**
     * Takes the list of words which are stored and using GSON they are converted to a single string of JSON.
     * This JSON string is then written to the JSON file.
     *
     */

    public void serializeJson(TreeMap<String, Word> wordsMap) {
        List<Word> loaded_words = new ArrayList<>(wordsMap.values());
        for(Word word : loaded_words){
            if(word.getWordType().equals("verb")){
                word.setEnglish(word.getEnglish().substring(3));
            }
        }
        String json = new Gson().toJson(loaded_words);
        try (FileWriter writer = new FileWriter("dictionary.json", false);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter outfile = new PrintWriter(bw)) {
            outfile.print(json);
            //Saving shouldn't ever cause an exception but if it does the stacktrace can be checked.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the JSON file and using GSON it is parsed into Word objects and stored in a List.
     */

    public List<Word> deserializeJson() {
        List<Word> loadedWords;
        try (FileReader reader = new FileReader("dictionary.json")) {
            Type wordListType = new TypeToken<ArrayList<Word>>() {
            }.getType();
            loadedWords = new Gson().fromJson(reader, wordListType);
            return loadedWords;
            //Checking to see if the file is missing or corrupt.
        } catch (IOException | JsonSyntaxException e) {
            return null;
        }
    }
}
