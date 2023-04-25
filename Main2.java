import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;
import java.util.List;

/*/
Programmer Andreea Ibanescu
 */
public class Main {
    // Declare global variables
    public static ArrayList<String> variables = new ArrayList<>();
    public static ArrayList<String> functions = new ArrayList<>();
    public static ArrayList<String> procedures = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        // Retrieve the file from a direct path
        File f = new File("C:\\Users\\andre\\IdeaProjects\\proj316part2\\src\\text");

        // Check to make sure file is found and selected by the program & continue if found otherwise exception is thrown
        if (!f.exists()) {
            System.out.println("Parse.txt Does Not Exist");
            System.exit(0);
        }

        // Scanner object is created, you can use its various methods to read input from the text string. part of the java.util
        Scanner scnr = new Scanner(f);

        // Organize the Pascal Text File into string chunks, take each line one at a time to analyze
        ArrayList<String> text = new ArrayList<>();


        //Using Scanner object to go through nextlines easier
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            //Adding it to a arraylist to analyze later
            text.add(line);
            //System.out.println(line); WORKS
        }

        //tells us how many lines for code there is in the whole text file

        int i=0;
        int lineCount = text.size();


        //Pascal usually follows a general syntax format/template

        // Checking the first line to make sure it starts with "program"
        if (!text.get(0).startsWith("PROGRAM ")) {
            System.out.println("Syntax Error: The first line must start with 'program'.");
        }
        else {
            System.out.println("Line is valid. Program begins with program name.");
            //Extra program name
        }

        //Now the next Lines could be anything, so there will be futher anaylsis of the syntax by breaking the text lines down into chunks while ignoring comments


        while(i < lineCount){
            //First ignore comments or blank lines!
            if (text.get(i).equals("\n") || isComment(text.get(i))) {
                System.out.println("Valid Line. Blank line or Comment.");
                i++;
            }



            //Next checking for Variable declarations NOTE: it's important
            if(text.get(i).equals("VAR")){
                System.out.println("Valid Line. Variable Declaration Format");
                //keep a loop going until a line doesn't have :
                i++;
                //Stop checking for Variable declarations after "Begin" b/c it's not the correct syntax, otherwise keep checking for valid syntax
                while (!text.get(i).equals("BEGIN")) {
                    VariableDeclarationChecker(text.get(i));
                    i++;
                }
            }
            // ADD a BEGIN CHECKER HERE
            if(text.get(i).equals("BEGIN")) {
                System.out.println("Valid Line. Pascal Formating");

            }

            //After checking for Assignment statements they all contain ":="
            int target = text.get(i).indexOf(":=");
            if (target >= 0 ) {
                AssignmentChecker(text.get(i));
            }
            //Checking if statements
            if(text.get(i).contains("IF") && text.get(i).contains("ELSE")){
                System.out.println("Valid Line If statement: " + text.get(i));
                i++;
            }

            // standard procedures that are used to read input from the user and to write output
            if(text.get(i).contains("Write") || text.get(i).contains("Read") ){
                System.out.println("Valid Line Pascal function: " + text.get(i));
            }

            //Checking for Boolean Expressions
            if(text.get(i).contains("==")){
                System.out.println("Valid Line Boolean Statement: " + text.get(i));
            }

            //Checking Arithmetic Operations
            if (text.get(i).contains("MOD")){
                System.out.println("incorrect Pascal arithmetic syntax");
            }


            //Check for some basic syntax
            syntaxChecker(text.get(i));


            //Extra credit Loops For
            if(text.get(i).contains("for")) {
                //See function below
            ForLoopCheck(text.get(i));
            }

            // While
            if(text.get(i).contains("while")) {
                //See function below
                WhileLoopChecker((text.get(i)));
            }


            if(text.get(i).contains("END.")) {
                i++;
            }
            //Continue anaylzing the text
            i++;
        }

        //Ending part of Pascal Program

        //Checking if the last line of the program starts with "END."
        if (!text.get(lineCount-1).startsWith("END. ")) {
            System.out.println("Syntax Error: The last line must start with END.");
        }
        else
            System.out.println("Valid Line. Program ends with END program.");



        //Functions:
    }
    //Function to check T/F for is a Pascal comment
    public static boolean isComment(String s){
        if ( !s.startsWith("(*")) {
            return false;
        }
        if(!s.endsWith("*)")){
            return false;
        }
        return true;
    }
    public static void VariableDeclarationChecker(String line) {
        //If it doesn't have a ":" it's an automatic incorrect syntax
        if (line.contains(":")) {
            String[] parts = line.split("\\s*:\\s*\\s*");
            String variable = parts[0].trim();
            String dataType = parts[1].trim();

            //check if it's a valid dataType
            List<String> dataTypes = new ArrayList<>(Arrays.asList("STRING;", "BOOLEAN;", "INTEGER;", "DOUBLE;"));
            if (dataTypes.contains(dataType)) {
                //Add the unique name to a list that would be used later on to check Assignment statement
                variables.add(variable);
                System.out.println("Valid declaration for variable: " + variable);
            }
            // Otherwise it's an invalid statement
            else {
                System.out.println("Invalid declaration for variable");
            }
        }
        else
            System.out.println("Invalid declaration for variable");
    }
    public static void AssignmentChecker(String line){
        int target = line.indexOf(":=");
        String variableName = line.substring(0, target).trim();

        //Check if its defined then check if it ends with a semicolon
        if (!variables.contains(variableName)) {
            System.out.println("Invalid. Variable " + variableName + " is not defined");
            return;
        }
        else
            System.out.println("Valid Statement");


    }
    //Function that checks T/F for specific Pascal words which are called the reserved words
    public static boolean isReserved_Words(String s){
        //Reserved Words:
        List<String> reserved_Word= Arrays.asList("and","array","begin","case","const","div","do","downto","else",
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
    //Checking for some basics!
    public static void syntaxChecker(String line) {
        //if(isComment(line))
          //  return;
        if(isReserved_Words(line))
            return;
        if (line.contains("(")) {
            if (!line.contains(")") && !line.contains("*")) {
                System.out.println("Invalid "+line);
            }
        }
        if (line.contains("\"")) {
            if (!line.contains("\"")) {
                System.out.println("Invalid "+line);
            }
        }
        if (line.contains("(*")) {
            if (!line.contains("*)")) {
                System.out.println("Invalid "+line);
            }
        }
        if (!line.endsWith(";") && !line.contains("*")) {
                System.out.println("Invalid "+line);
        }


        //System.out.println(line);
    }
    public static boolean arithmeticCheck(String input){
        // Define regular expressions for operators and operands
        String operatorRegex = "[+\\-*/]";
        String integerRegex = "\\d+";
        String parenRegex = "[()]";

        // Define regular expression for arithmetic expressions
        String expressionRegex = "(" + integerRegex + "|" + operatorRegex + "|" + parenRegex + ")+";

        // Check if input string matches the arithmetic expression regex
        return input.matches(expressionRegex);
    }


    //Function to check for FOR loop
    public static void ForLoopCheck(String line){
        String pattern = "^for\\s+\\w+\\s+:=[^\\s]+\\s+to\\s+[^\\s]+\\s+do$";
        //means in regex that the line starts with for XYZ then a := then XYZ then TO then XZY then DO
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        if (matcher.matches()) {
            System.out.println("Line has correct syntax for Pascal for loop.");
        } else {
            System.out.println("Line does not have correct syntax for Pascal for loop.");
        }
    }
    //Function to check for While Loop similar to for loop but now following the general structure for pascal while loop
    public static void WhileLoopChecker(String line){
        String pattern = "^while\\s+[^\\s]+\\s+<|<=|>|>=|==|!=\\s+[^\\s]+\\s+do$";
        //
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        if (matcher.matches()) {
            System.out.println("Line has correct syntax for while loop.");
        } else {
            System.out.println("Line does not have correct syntax for while loop.");
        }
    }

}