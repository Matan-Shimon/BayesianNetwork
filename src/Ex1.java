public class Ex1 {

    public static void main(String[] args) {
        // sending the text file name to the ReadInput class
        ReadInput readInput = new ReadInput("input3.txt");
        // taking the whole answer
        String answer = readInput.getAnswer();
        // reducing the last empty line
        String final_answer = answer.substring(0, answer.length()-1);
        // sending the string to the WriteFile class
        WriteFile outputFile = new WriteFile(final_answer);
    }
}