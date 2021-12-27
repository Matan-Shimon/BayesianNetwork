import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class  WriteFile {
    private String answer;

    public WriteFile(String answer) {
        this.answer = answer;

        try {
            // creating a new file called output.txt
            FileWriter myWriter = new FileWriter("output.txt");
            // putting the whole answer string variable in that file
            myWriter.write(this.answer);
            // closing
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}