import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String RESET = "\033[0m";
    public static void main(String[] args) {
        List<String> fileContents = parser("words.txt");
        System.out.println("Instructions: hit enter after every word you typed for it to count.\n");

        int numWords = prompter();
        fileContents = fileContents.subList(0, numWords);

        System.out.println(fileContents);
        final long startTime = System.currentTimeMillis();
        int[] scoreTotal = tester(fileContents, numWords);
        final long endTime = System.currentTimeMillis();
        
        long elapsedTime = (endTime - startTime) / 1000;
        giveResult(scoreTotal, elapsedTime);
    }

    static List<String> parser(String f) {
        String result = "";
        try {
            File wordFile = new File(f);
            Scanner fileReader = new Scanner(wordFile);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                result += line;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
            e.printStackTrace();
        }

        String fileContents = result.replaceAll("\\d", "").trim();
        String[] wordArr = fileContents.split(" ");
        List<String> wordList = Arrays.asList(wordArr);
        Collections.shuffle(wordList);
        return wordList;
    }

    static int prompter() {
        System.out.print("Input number of words for typing test: ");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        return Integer.parseInt(s);
    }

    static int[] tester(List<String> words, int num) {
        int score = 0;
        int total = 0;
        for (int i = 0; i < num; i++) {
            String space = new String(new char[total + 2*i]).replace("\0", " ");
            String word = words.get(i);
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            System.out.print(String.format("\033[%dA",1));
            System.out.print("\033[2K");
            if (s.equals(word)) {
                score += word.length();
                System.out.println(GREEN + String.format("\033[%dA",1) + " " + space + word);
            }
            else {
                System.out.println(RED + String.format("\033[%dA",1) + " " + space  + word);
            }
            System.out.print(RESET);
            total += word.length();
        }
        return new int[] {score, total};
    }

    static void giveResult(int[] st, long e) {
        long wpm = (st[0]/5) * 60/e;
        System.out.printf("WPM: %d\nCharacters: %d\nTime Elapsed: %d\nAccuracy %d", wpm, st[0], e, st[0] * 100/st[1]); 
    }
}