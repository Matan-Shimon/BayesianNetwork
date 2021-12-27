import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
    // this function get an xml file name, and writing from it
    // by that, this class will also create an ArrayList of NetNodes
    public ArrayList<NetNode> network(String file_name) {
        try {
            // variables from dom to read an xml file propertly
            File xmlDoc = new File(file_name);
            DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuild = dbFact.newDocumentBuilder();
            Document doc = dBuild.parse(xmlDoc);

            // read array of variables
            // this array is called NodeList
            NodeList variables = doc.getElementsByTagName("VARIABLE");
            ArrayList<NetNode> netNodes = new ArrayList<>();
            for (int i = 0; i < variables.getLength(); i++) {
                Node nNode = variables.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // name of the variable
                    String name = eElement.getElementsByTagName("NAME").item(0).getTextContent();
                    ArrayList<String> outcomes = new ArrayList<>();
                    // outcomes of the variable
                    for (int j = 0; j < eElement.getElementsByTagName("OUTCOME").getLength(); j++) {
                        String outcome = eElement.getElementsByTagName("OUTCOME").item(j).getTextContent();
                        outcomes.add(eElement.getElementsByTagName("OUTCOME").item(j).getTextContent());
                    }
                    netNodes.add(new NetNode(name, outcomes));
                }
            }
            // after we finished to create all of the net nodes
            // we will start reading the definitions
            // by that, we will add to a specific node, parents, childs and cpt
            NodeList definitions = doc.getElementsByTagName("DEFINITION");

            for (int i = 0; i < definitions.getLength(); i++) {
                Node nNode = definitions.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // given means they are the "FOR" node parents
                    for (int j = 0; j < eElement.getElementsByTagName("GIVEN").getLength(); j++) {
                        for (int k = 0; k < netNodes.size(); k++) {
                            if (netNodes.get(k).getName().equals(eElement.getElementsByTagName("GIVEN").item(j).getTextContent())) {
                                // going through all of the netNodes
                                for (int l = 0; l < netNodes.size(); l++) {
                                    // if we found an equal name, we will add to this node the "GIVEN" parent
                                    // and to the "GIVEN" node, we will add the "FOR" node as a child
                                    if (netNodes.get(l).getName().equals(eElement.getElementsByTagName("FOR").item(0).getTextContent())) {
                                        netNodes.get(l).add_parent(netNodes.get(k));
                                        netNodes.get(k).add_child(netNodes.get(l));
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    // cpt for each netNode
                    String p = eElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    ArrayList<String> cpt_string = new ArrayList<>(Arrays.asList(p.split(" ")));
                    ArrayList<Double> cpt_double = new ArrayList<>();
                    for (int j = 0; j < cpt_string.size(); j++) {
                        cpt_double.add(Double.valueOf(cpt_string.get(j)));
                    }
                    HashMap<String, Double> cpt = new HashMap<String, Double>();
                    int num_of_parents = netNodes.get(i).parents.size();
                    // counters to know when to change an outcome
                    ArrayList<Integer> counters = new ArrayList<Integer>();
                    for (int j = 0; j <= num_of_parents; j++) {
                        counters.add(0);
                    }
                    // flags to know when to restart from the first otcome
                    ArrayList<Integer> flags = new ArrayList<Integer>();
                    for (int j = 0; j <= num_of_parents; j++) {
                        flags.add(0);
                    }
                    // outcomes holds all of the netNodes outcomes
                    ArrayList<ArrayList<String>> outcomes = new ArrayList<ArrayList<String>>();
                    outcomes.add(netNodes.get(i).outcomes);
                    for (int j = 0; j < num_of_parents; j++) {
                        outcomes.add(netNodes.get(i).parents.get(j).outcomes);
                    }
                    // creating a key (of probability) in the form of the question
                    for (int j = 0; j < cpt_double.size(); j++) {
                        String prob = "P("+netNodes.get(i).getName()+"=";
                        if (counters.get(0) == outcomes.get(0).size()) {
                            prob += outcomes.get(0).get(0) + "|";
                            counters.set(0, 1);
                        }
                        else {
                            prob += outcomes.get(0).get(counters.get(0)) + "|";
                            counters.set(0, counters.get(0)+1);
                        }
                        for (int k = 1; k <= num_of_parents; k++) {
                            prob += netNodes.get(i).parents.get(k-1).getName()+"=";
                            if (k == num_of_parents) {
                                if (counters.get(k) == outcomes.get(0).size()) {
                                    flags.set(k, flags.get(k) + 1);
                                    if (flags.get(k) == outcomes.get(k).size()) {
                                        flags.set(k, 0);
                                        prob += outcomes.get(k).get(flags.get(k));
                                        counters.set(k, 1);
                                    }
                                    else {
                                        prob += outcomes.get(k).get(flags.get(k));
                                        counters.set(k, 1);
                                    }
                                }
                                else {
                                    prob += outcomes.get(k).get(flags.get(k));
                                    counters.set(k, counters.get(k) + 1);
                                }
                            }
                            else {
                                int num_of_rows = 1;
                                for (int l = k+1; l <= num_of_parents; l++) {
                                    num_of_rows *= outcomes.get(l).size();
                                }
                                num_of_rows *= outcomes.get(0).size();
                                if (counters.get(k) == num_of_rows) {
                                    flags.set(k, flags.get(k) + 1);
                                    if (flags.get(k) == outcomes.get(k).size()) {
                                        flags.set(k, 0);
                                        prob += outcomes.get(k).get(flags.get(k))+",";
                                        counters.set(k, 1);
                                    }
                                    else {
                                        prob += outcomes.get(k).get(flags.get(k))+",";
                                        counters.set(k, 1);
                                    }
                                }
                                else {
                                    prob += outcomes.get(k).get(flags.get(k))+",";
                                    counters.set(k, counters.get(k) + 1);
                                }
                            }
                        }
                        // adding the probability to the specific cpt
                        prob += ")";
                        cpt.put(prob, cpt_double.get(j));
                    }
                    // adding the cpt to the specific node
                    netNodes.get(i).add_cpt(cpt);
                }
            }
            // return an ArrayList of nodes
            return netNodes;
        }
        catch (Exception e) {
            // if it couldn't reach the xml file, it will return an empty ArrayList of netNodes
            ArrayList<NetNode> didnt_work = new ArrayList<NetNode>();
            return didnt_work;
        }
    }
}