import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
Andreea Ibanescu
CSCI 316 | Project 1 Part 1
PROJ DESC: to design and implement a Lexical Analyzer for a subset of the Pascal Programming Language. Your program should be able to accept as input a file
containing statements in Pascal and correctly perform a lexical analysis of each statement. code to test the validity of identifiers,  If a string does not match
any of the categories, you should indicate that as UNKNOWN. The tokens are analyzed by functions and by using REGEX: regular expressions contain a series of characters
that define a pattern of text to be matched—to make a filter more specialized, or general.
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        //Retrieve the file from a direct path
        File text = new File("C:\\Users\\andre\\IdeaProjects\\CSCI316part1\\src\\Parse");

        //Check to make sure file is found and selected by the program & continue if found otherwise exception is thrown
        if (!text.exists()) {
            System.out.println("Parse.txt Does Not Exist");
            System.exit(0);
        }

        //Organize the Pacal Text File into string chunks take each line by one to anaylze
        ArrayList<String> pascal = new ArrayList<>();

        //Scanner object is created, you can use its various methods to read input from the text string. part of the java.util
        Scanner scnr = new Scanner(text);

        //Using Scanner object to go through nextlines easier
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            //Adding it to a arraylist to analyze later
            pascal.add(line);
            //System.out.println(line); //test to see what the lines print out
        }

        //keeping track of all tokens
        int count=1;

        for (String string : pascal) {
            //Make the String into Tokens
            ArrayList<String> tokens = tokenize(string);
             System.out.println(tokens); //This line was used for testing to see what the tokenize function made into tokens
            //Go through each token
            for(String l: tokens){
                System.out.println(count+" Token: "+ l );
                count++; //Count the number of token the program analyzed

                //Analyze  the Token's Lexeme
                if(isReserved_Words(l))
                    System.out.println("Lexeme: RESERVED WORD");
                //the next two if statements will be a considered as reserved word but will check bc it may fall under the variable / function / procedure catergory as well
                if (l.equalsIgnoreCase("VAR") || l.equalsIgnoreCase("var"))
                    System.out.println("But also a Variables");
                if (l.equalsIgnoreCase("FUNCTION") || l.equalsIgnoreCase("function") ||l.equalsIgnoreCase("PROCEDURE") || l.equalsIgnoreCase("procedure"))
                    System.out.println("But also a function/procedure");
                else if(isString(l))
                    System.out.println("Lexeme: STRING");
                else if(isComment(l))
                    System.out.println("Lexeme: COMMENT");
                else if(isDigit(l))
                    System.out.println("Lexeme: IDENTIFIER, digit");
                else if(isSpecialSymbol(l))
                    System.out.println("Lexeme: IDENTIFIER, special symbols");
                else {
                    if (!isReserved_Words(l)) {
                        //Split up the string tokens up into chars to futher anaylze
                        char[] chars = l.toCharArray();

                        //Continue to loop through the char tokens
                        for (char c : chars) {
                            System.out.println(count + " Token: " + c);
                            count++; //Count the number of token the program analyzed
                            //CAPS or LOWER
                            if (isLowerCase(c))
                                System.out.println("Lexeme: IDENTIFIER, lowercase");
                            else if (isUpperCase(c))
                                System.out.println("Lexeme: IDENTIFIER, uppercase");
                                //If there are spaces
                            else if (Character.isWhitespace(c))
                                System.out.println("Lexeme: IDENTIFIER, white-space");
                            else
                                //Last thing is that we checked all possible lexeme so instead of an error we put unknown
                                System.out.println("Lexeme: UNKNOWN");
                        }
                    }
                }
                 }
        }

    }

    //Functions breaks down the string line into tokens

    public static ArrayList<String> tokenize(String s) {
            //The function will store the tokens into an arraylist called lexeme
            ArrayList<String> lexeme = new ArrayList<>();



            //Using regex to put ALL the ways to seperate a token by either  comments  (* or " or special characters & also the following
            // \\w+: matches one or more word characters (letters, digits, or underscores)
            //\\d+: matches one or more digits
            Pattern pattern = Pattern.compile("(\\s+|\".*?\"|\\(.*?\\)|\\[.*?\\]|\\{.*?\\}|\\+\\+|--|:=|\\*\\*|==|!=|<=|>=|<>|<|>|\\+|-|\\*|/|\\.|,|;|\\(|\\)|\\[|\\]|`|:|=|\\w+|\\d+)");
            //Using regex class .matcher to see if there is a symbol found within s, if not there's a blank space and its ignored
            Matcher match = pattern.matcher(s);


            //if there's a match continue to anaylze the string
            while (match.find()) {
                //get the entire regular expression pattern in the Matcher object match
                String token = match.group(0);


                //FIRST, handle white spaces! ( ^\\s+$ is regex for whitespace) ignoring them from the string
                if (!token.matches("^\\s+$")) {


                    //SECOND handle COMMENTS
                    if (token.startsWith("(*") && token.endsWith("*)")) {
                        lexeme.add(token);


                        //Third handle QUOTES
                    } else if (token.contains("\"") && token.endsWith("\""))  {
                        //split the part of the string that starts with quotes and ends with quotes
                        String[] quoteTokens = token.split("\"[^\"]*\"");

                        //Now it will iterate over each quoteToken to only add it to lexemes if its not empty and make sure the whole quote is added even if there's white spaces inbetween
                        for (String quoteToken : quoteTokens) {
                            if (!quoteToken.isEmpty()) {
                                lexeme.add(quoteToken);
                            }
                        }

                        //LASTLY HANDLE SPECIAL SYMBOLS
                    } else {

                        //If there are no quotes or comments, white-spaces then we have to check for special characters
                        Pattern symbolPattern = Pattern.compile(":=|\\+\\+|--|\\*\\*|==|!=|<=|>=|<>|[-+*/=.,;()\\[\\]{}`]|:|=");
                        //Just as before if there's a special symbol use it to futher analyze
                        Matcher symbolMatcher = symbolPattern.matcher(token);


                        int start = 0; //start is used with the symbolMatcher.start() and symbolMatcher.end() methods to extract substrings of token that are separated by symbols of regex

                        //if there is a special symbol keep going through the string
                        while (symbolMatcher.find()) {

                            //Get the substring of the index where it starts to the pattern ending
                            String beforeSymbol = token.substring(start, symbolMatcher.start());

                            //the method to add each substring of token that comes before each symbol as a separate element to the lexeme list
                            if (!beforeSymbol.isEmpty()) {
                                lexeme.add(beforeSymbol);
                            }
                            //add the actual symnbol to its own token
                            lexeme.add(symbolMatcher.group(0));  //regex function adds the matched special symbol found, but in this case its just one symbol
                            start = symbolMatcher.end();
                        }

                        //making what comes after the symbol into another token
                        String afterSymbols = token.substring(start);
                        if (!afterSymbols.isEmpty()) {
                            lexeme.add(afterSymbols);
                        }
                    }
                }
            }
            //return the tokens
            return lexeme;
        }




        //Function that checks T/F for specific Pascal words which are called the reserved words
    public static boolean isReserved_Words(String s){
        //Reserved Words:
        List<String> reserved_Word= Arrays.asList("and","array","begin","case","const","div","do","downto","else","end",
            "file","for","function","goto","if","in","label","mod","nil","not","of","or","packed","procedure","program",
            "record","repeat","set","then","to","type","until","var","while","with");

        String lowercaseS = s.toLowerCase(); // convert input string to lowercase
        String uppercaseS = s.toUpperCase(); // convert input string to uppercase


        if(reserved_Word.contains(lowercaseS) || reserved_Word.contains(uppercaseS)){
            return true;
        }
        else {
            return false;
        }

    }
    //Function to check T/F for is a Pascal comment
    public static boolean isComment(String s){
        if (!s.startsWith("{") && !s.startsWith("(*")) {
            return false;
        }
        if (s.startsWith("(*") && !s.endsWith("*)")) {
            return false;
        }
        return true;
    }
    //Function to check T/F if is a Digit
    public static boolean isDigit(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    //Function to check T/F if its String (Quote)
    public static boolean isString(String s) {
        if (s.startsWith("\"") && s.endsWith("\""))
            return true;
        else
            return false;

    }
    //Function to check T/F if string is lowercase
    public static boolean isLowerCase(char c) {
        return Character.isLowerCase(c);
    }

    //Function to check T/F if string is uppsercase
    public static boolean isUpperCase(char c) {
        return Character.isUpperCase(c);
    }
    //Function to check for special Symbols
    public static boolean isSpecialSymbol(String str) {
        String specialSymbols = "+*/:=,.;()[]={}`";
        for (int i = 0; i < str.length(); i++)
            if (specialSymbols.indexOf(str.charAt(i)) == -1)
                return false;
        return true;
    }
}