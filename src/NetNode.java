import java.util.ArrayList;
import java.util.HashMap;

public class NetNode {
    private String name;
    public ArrayList<String> outcomes;
    public ArrayList<NetNode> parents;
    public ArrayList<NetNode> childs;
    public HashMap<String, Double> cpt;

    // defultive consructor
    public NetNode() {
        this.name = "";
        this.parents = new ArrayList<NetNode>();
        this.childs = new ArrayList<NetNode>();
        this.outcomes = new ArrayList<String>();
        this.cpt = new HashMap<String, Double>();
    }

    // constructor
    public NetNode(String name, ArrayList<String> outcomes) {
        this.name = name;
        this.outcomes = new ArrayList<String>();
        for (int i = 0; i < outcomes.size(); i++) {
            this.outcomes.add(outcomes.get(i));
        }
        this.parents = new ArrayList<NetNode>();
        this.childs = new ArrayList<NetNode>();
        this.cpt = new HashMap<String, Double>();
    }

    // copy constructor
    public NetNode(NetNode netNode) {
        this.name = netNode.name;
        this.outcomes = new ArrayList<String>();
        for (int i = 0; i < netNode.outcomes.size(); i++) {
            this.outcomes.add(netNode.outcomes.get(i));
        }
        this.parents = new ArrayList<NetNode>();
        for (int i = 0; i < netNode.parents.size(); i++) {
            this.parents.add(netNode.parents.get(i));
        }
        this.childs = new ArrayList<NetNode>();
        for (int i = 0; i < netNode.childs.size(); i++) {
            this.childs.add(netNode.childs.get(i));
        }
        this.cpt = new HashMap<String, Double>();
        for (int i = 0; i < netNode.cpt.size(); i++) {
            this.cpt.putAll(netNode.cpt);
        }
    }

    public String getName() {
        return this.name;
    }

    public void add_parent(NetNode parent) {
        this.parents.add(parent);
    }

    public void add_cpt(HashMap<String, Double> cpt) {
        this.cpt.putAll(cpt);
    }

    public void add_child(NetNode child) {
        this.childs.add(child);
    }

    public String toString() {
        ArrayList<String> p = new ArrayList<>();
        for (int i = 0; i < this.parents.size(); i++) {
            p.add(this.parents.get(i).name);
        }
        ArrayList<String> c = new ArrayList<>();
        for (int i = 0; i < this.childs.size(); i++) {
            c.add(this.childs.get(i).name);
        }
        return "name: "+this.name+", parents: "+p+", childs: "+c;
    }
}