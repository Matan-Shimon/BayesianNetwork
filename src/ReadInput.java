import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ReadInput {
    //  the answer variable has the answer for each question
    private String answer = "";

    public ReadInput(String file_input_name) {
        try {
            // creating a Buffer object to read from the text file
            BufferedReader brTest = new BufferedReader(new FileReader(file_input_name));
            // first line of the text file is the xml file name
            String xml_file_name = brTest.readLine();
            XMLParser xmlParser = new XMLParser();
            // the network func in xmlParser class returns an ArrayList of NetNodes
            // that represen the bayesian network
            BayesianNetwork bayesianNetwork = new BayesianNetwork(xmlParser.network(xml_file_name));
            // reading the next lines until the end
            String line = brTest.readLine();
            while (line != null) {
                // if the line starts with P
                // that means the line present a variable elimination question
                if (String.valueOf(line.charAt(0)).equals("P")) { // variable elimination
                    int ind = line.indexOf(")");
                    // the question itself (query and evidences)
                    String quest = line.substring(0, ind+1);
                    // the hiddens
                    String hidden_string = line.substring(ind+2);
                    String [] hidden = hidden_string.split("-");
                    ArrayList<String> hiddens = new ArrayList<>();
                    Collections.addAll(hiddens, hidden);
                    // adding the string ans that we got from the variable elimination to the answer variable
                    this.answer += bayesianNetwork.variableElimination(quest, hiddens)+'\n';
                }
                else { // bayes ball question
                    // nodes to check bayes ball
                    String [] nodes_split = line.split("-");
                    String node1 = nodes_split[0];
                    int ind = nodes_split[1].indexOf("|");
                    String node2 = nodes_split[1].substring(0, ind);
                    // evidences
                    ArrayList<String> evidences = new ArrayList<String>();
                    // if line.length() - 1 == line.indexOf('|') than we have no evidences
                    if (line.length() - 1 != line.indexOf('|')) {
                        String sub_string = line.substring(line.indexOf("|")+1);
                        String [] evidence_split = sub_string.split(",");
                        for (int i = 0; i < evidence_split.length; i++) {
                            ind = evidence_split[i].indexOf("=");
                            String sub_str = evidence_split[i].substring(0, ind);
                            // adding an evidence to the list
                            evidences.add(sub_str);
                        }
                    }
                    // adding the string ans that we got from the bayes ball to the answer variable
                    this.answer += bayesianNetwork.bayesBall(node1, node2, evidences)+'\n';
                }
                line = brTest.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns the answer after we read all of the questions
    public String getAnswer() {
        return this.answer;
    }
}