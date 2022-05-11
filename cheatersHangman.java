import java.security.KeyStore;
import java.util.*;
import java.io.*;

public class cheatersHangman {
    public static int wrongGuesses=0;

    public static Map<Integer, List<String>> createWordLists() throws FileNotFoundException {

        Scanner scanner = new Scanner(new File("word.txt"));
        Map<Integer, List<String>> wordLists = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!wordLists.containsKey(line.length())) {
                wordLists.put(line.length(), new ArrayList<String>(Arrays.asList(line)));
            } else {
                wordLists.get(line.length()).add(line);
            }

        }
        //https://stackoverflow.com/questions/19541582/storing-and-retrieving-arraylist-values-from-hashmap credit
        return wordLists;
    }


    public static Map<String, List<String>> createWordFamilies(List<String> wordList, Set<Character> guessed) {
        Map<String, List<String>> wordFamilies = new HashMap<>();

        for (String word : wordList) {
            String family = findWordFamily(word, guessed);
            if (wordFamilies.containsKey(family)) {
                List<String> list = wordFamilies.get(family);
                list.add(word);
                wordFamilies.put(family, list);
            } else {
                List<String> list = new ArrayList<>();
                list.add(word);
                wordFamilies.put(family, list);
            }
        }
        return wordFamilies;
    }

    //figure out word family for a string
    //word family is words that are possible given the letters that have been guessed
    // go through word, ask if letter has been guessed, if so, add letter, otherwise add underscore to family
    //if it has been guessed reveal the letter, otherwise leave it blank
    //go through word, if it has been guessed reveal it, otherwise leave it blank with and underscore
    public static String findWordFamily(String word, Set<Character> guessed) {
        String family = "";
        char[] Char = word.toCharArray();
        for (char c : Char) {
            if (guessed.contains(c)) {
                family = family + c;
            } else {
                family = family + "_";
            }
        }


        return family;
    }

    //the for loop was taken from stack overflow,https://stackoverflow.com/questions/46898/how-do-i-efficiently-iterate-over-each-entry-in-a-java-map
    public static String getBestFamily(Map<String, List<String>> wordFamilies) {
        String bestKey = "";
        //Iterator iterator = wordFamilies.entrySet().iterator();
        int biggest=0;

        for (Map.Entry<String, List<String>> entry : wordFamilies.entrySet()) {
            if(entry.getValue().size()>biggest){
                bestKey=entry.getKey();
                biggest=entry.getValue().size();
            }
            //System.out.println(entry.getKey() + "/" + entry.getValue());
        }
//        for(Object val : arr){
//            System.out.println(val);
//        }
        return bestKey;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Set<Character> guessed = new HashSet<>();
        List<String> list = new ArrayList<>();
        Scanner scanner= new Scanner(System.in);
        System.out.println("enter number of turns");
        int numTurns=scanner.nextInt();
        System.out.println("enter number of letters");
        int numLetters=scanner.nextInt();
        Map<Integer, List<String>> wordLists = new HashMap<>();
        wordLists=createWordLists();
        list=wordLists.get(numLetters);
        Map<String, List<String>> wordFamilies = new HashMap<>();
        String wordPrev="";
        for(int i=0;i<numLetters;i++){
            wordPrev+="_";
        }
        int i=0;
        String bestFamily = "________";
        while(numTurns>i &&(bestFamily.contains("_"))){
            System.out.println("guess a letter");
            String s=scanner.next();
            Character c=s.charAt(0);
            if(guessed.contains(c)){
                System.out.println("repeat letter, guess again");
                s=scanner.next();
                c=s.charAt(0);
            }
            guessed.add(c);
            wordFamilies=createWordFamilies(list, guessed);
            bestFamily=getBestFamily(wordFamilies);
            System.out.println(bestFamily);
            list=wordFamilies.get(bestFamily);
            if(wordPrev.equals(bestFamily)){
                i++;
                System.out.println("wrong");
            }
            System.out.println("you have made "+ i + " incorrect guesses, you have "+ (numTurns-i)+ " turns left");
            wordPrev=bestFamily;
            if(!bestFamily.contains("_")){
                System.out.println("you won, you made " + i + " incorrect guesses, the word was "+ bestFamily);
            }
        }
        if(i>=numTurns){
            System.out.println("you have lost");
        }

    }

}
