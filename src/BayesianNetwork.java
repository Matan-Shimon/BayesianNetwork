import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BayesianNetwork {
    public ArrayList<NetNode> bayesian_network;
    private int num_of_add;
    private int num_of_multiple;
    public BayesianNetwork() {
        this.bayesian_network = new ArrayList<NetNode>();
    }

    // constructor
    public BayesianNetwork(ArrayList<NetNode> bayesian_network) {
        this.bayesian_network = new ArrayList<NetNode>();
        for (int i = 0; i < bayesian_network.size(); i++) {
            this.bayesian_network.add(bayesian_network.get(i));
        }
        // variables for checking the number of adding and multiplication operations
        num_of_add = 0;
        num_of_multiple = 0;
    }

    // bayes ball function
    public String bayesBall(String name1, String name2, ArrayList<String> evidences) {
        NetNode [] netNodes = new NetNode[2];
        int count = 0;
        for (int i = 0; i < this.bayesian_network.size(); i++) {
            // finding the two netNodes
            if (this.bayesian_network.get(i).getName().equals(name1) || this.bayesian_network.get(i).getName().equals(name2)) {
                netNodes[count] = this.bayesian_network.get(i);
                count++;
            }
        }
        // road is meant to prevent repeating the same track over and over again
        ArrayList<String> road = new ArrayList<>();
        boolean depandent = way(netNodes[0], netNodes[1], evidences, true, "default", "first" , netNodes[0].getName(), road);
        if (depandent) {
            return "no";
        }
        else { // Indepandent
            return "yes";
        }
    }

    // recursive function for bayes ball
    public boolean way(NetNode a, NetNode b, ArrayList<String> evidences, boolean isFirst, String from_where, String last_node_name, String original_a_name, ArrayList<String> road) {
        // we are staring from a node
        // if we got to b node, we finished
        if (a.getName().equals(b.getName())) {
            return true;
        }
        boolean isEvidence = false;
        for (int i = 0; i < evidences.size(); i++) {
            if (a.getName().equals(evidences.get(i))) {
                isEvidence = true;
            }
        }
        boolean add = true;
        if (isFirst) {
            // the first [a] (or last [b]) cannot be evidence
            for (int i = 0; i < a.parents.size(); i++) {
                road.add(a.getName());
                // recursive call
                boolean check = way(a.parents.get(i), b, evidences, false, "child", a.getName(), original_a_name, road);
                if (check) {
                    return true;
                }
            }
            for (int i = 0; i < a.childs.size(); i++) {
                road.add(a.getName());
                // recursive call
                boolean check = way(a.childs.get(i), b, evidences, false, "parent", a.getName(), original_a_name, road);
                if (check) {
                    return true;
                }
            }
        }
        else { // not first
            if (isEvidence) {
                if (from_where.equals("parent")) {
                    if (a.getName().equals(original_a_name)) {
                        return false;
                    }
                    for (int i = 0; i < a.parents.size(); i++) {
                        // checking if we already did this track
                        for (int j = 0; j < road.size(); j++) {
                            if (road.get(j).equals(a.getName())) {
                                if (j!= road.size()-1) {
                                    if (road.get(j+1).equals(a.parents.get(i).getName())) {
                                        add = false;
                                    }
                                }
                            }
                        }
                        // if we haven't done this track, we'll do it
                        if (add) {
                            if (road.size() > 0) {
                                if (!road.get(road.size()-1).equals(a.getName())) {
                                    road.add(a.getName());
                                }
                            }
                            else {
                                road.add(a.getName());
                            }
                            // recursive call
                            boolean check = way(a.parents.get(i), b, evidences, false, "child", a.getName(), original_a_name, road);
                            if (check) {
                                return true;
                            }
                        }
                    }
                }
            }
            else {
                if (from_where.equals("parent")) {
                    for (int i = 0; i < a.childs.size(); i++) {
                        if (!a.childs.get(i).getName().equals(original_a_name)) {
                            // checking if we already did this track
                            for (int j = 0; j < road.size(); j++) {
                                if (road.get(j).equals(a.getName())) {
                                    if (j!= road.size()-1) {
                                        if (road.get(j+1).equals(a.childs.get(i).getName())) {
                                            add = false;
                                        }
                                    }
                                }
                            }
                            // if we haven't done this track, we'll do it
                            if (add) {
                                if (road.size() > 0) {
                                    if (!road.get(road.size()-1).equals(a.getName())) {
                                        road.add(a.getName());
                                    }
                                }
                                else {
                                    road.add(a.getName());
                                }
                                // recursive call
                                boolean check = way(a.childs.get(i), b, evidences, false, "parent", a.getName(), original_a_name, road);
                                if (check) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                else { // came from child
                    for (int i = 0; i < a.parents.size(); i++) {
                        if (!a.parents.get(i).getName().equals(original_a_name)) {
                            // checking if we already did this track
                            for (int j = 0; j < road.size(); j++) {
                                if (road.get(j).equals(a.getName())) {
                                    if (j!= road.size()-1) {
                                        if (road.get(j+1).equals(a.parents.get(i).getName())) {
                                            add = false;
                                        }
                                    }
                                }
                            }
                            // if we haven't done this track, we'll do it
                            if (add) {
                                if (road.size() > 0) {
                                    if (!road.get(road.size()-1).equals(a.getName())) {
                                        road.add(a.getName());
                                    }
                                }
                                else {
                                    road.add(a.getName());
                                }
                                // recursive call
                                boolean check = way(a.parents.get(i), b, evidences, false, "child", a.getName(), original_a_name, road);
                                if (check) {
                                    return true;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < a.childs.size(); i++) {
                        if (!a.childs.get(i).getName().equals(last_node_name) && !a.childs.get(i).getName().equals(original_a_name)) {
                            // checking if we already did this track
                            for (int j = 0; j < road.size(); j++) {
                                if (road.get(j).equals(a.getName())) {
                                    if (j!= road.size()-1) {
                                        if (road.get(j+1).equals(a.childs.get(i).getName())) {
                                            add = false;
                                        }
                                    }
                                }
                            }
                            // if we haven't done this track, we'll do it
                            if (add) {
                                if (road.size() > 0) {
                                    if (!road.get(road.size()-1).equals(a.getName())) {
                                        road.add(a.getName());
                                    }
                                }
                                else {
                                    road.add(a.getName());
                                }
                                // recursive call
                                boolean check = way(a.childs.get(i), b, evidences, false, "parent", a.getName(), original_a_name, road);
                                if (check) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // variable elimination function
    public String variableElimination(String quest, ArrayList<String> hiddens) {
        num_of_multiple = 0;
        num_of_add = 0;
        // making a copy of all of the cpt's
        ArrayList<HashMap<String, Double>> factors = cpts_copy();
        // checking if we already have the answer
        for (int i = 0; i < factors.size(); i++) {
            ArrayList<String> keys = new ArrayList<>(factors.get(i).keySet());
            for (int j = 0; j < keys.size(); j++) {
                ArrayList<String> quest_list = nodes_name(quest);
                ArrayList<String> key_list = nodes_name(keys.get(j));
                int count = 0;
                for (int k = 0; k < quest_list.size(); k++) {
                    for (int l = 0; l < key_list.size(); l++) {
                        if (quest_list.get(k).equals(key_list.get(l))) {
                            count++;
                        }
                    }
                }
                // already have the answer
                if (count == key_list.size() && count == quest_list.size()) {
                    double prob = factors.get(i).get(keys.get(j));
                    // formatting to get 5 digits after the decimal point
                    double final_prob = (double)Math.round(prob * 100000d) / 100000d;
                    String answer = final_prob +","+num_of_add+","+num_of_multiple;
                    return answer;
                }
            }
        }
        // checking the evidnces in the specific question
        ArrayList<String> evidences = evidences(quest);
        // evidences for bayes ball
        ArrayList<String> bb_evidences = evidences_for_bayes_ball(quest);
        // deleting irrelevant rows (from evidences)
        factors = delete_irrelevant_rows(factors, evidences);
        // after we deleted the irrelevant rows, there's an option we have an empty factor
        // clearing empty factors
        factors = clear_empty(factors);
        // get query from quest
        String query = get_query(quest);
        // getting all of the nodes names
        ArrayList<String> names = nodes_name(quest);
        for (int i = 0; i < hiddens.size(); i++) {
            // if it's not an ancestor of the query or one of the evidences than we are deleting
            // the cpt's that contains this hidden variable
            if (!is_ancestor(hiddens.get(i), names)) {
                int num = num_of_factors(factors, hiddens.get(i));
                while (num > 0) {
                    int index = index_of_first_factor(factors, hiddens.get(i));
                    if (index != -1) {
                        factors.remove(index);
                    }
                    num = num_of_factors(factors, hiddens.get(i));
                }
            }
            else if (bayesBall(hiddens.get(i), query, bb_evidences).equals("yes")) {
                // if it's not dependant to the query given the evidences than we are deleting
                // the cpt's that contains this hidden variable
                int num = num_of_factors(factors, hiddens.get(i));
                while (num > 0) {
                    int index = index_of_first_factor(factors, hiddens.get(i));
                    if (index != -1) {
                        factors.remove(index);
                    }
                    num = num_of_factors(factors, hiddens.get(i));
                }
            }
        }
        for (int i = 0; i < hiddens.size(); i++) {
            if (is_ancestor(hiddens.get(i), names)) {
                if (bayesBall(hiddens.get(i), query, bb_evidences).equals("no")) {
                    // if it's an ancestor and dependant
                    // than we will join
                    int num = num_of_factors(factors, hiddens.get(i));
                    int index = index_of_first_factor(factors, hiddens.get(i));
                    // while numbers of the factors that contains this hidden
                    // is greater than 1, then we will keep doing join
                    while (num > 1) {
                        int [] check = find_smallest_cpts_indexes(factors, hiddens.get(i));
                        HashMap<String, Double> factor1 = new HashMap<>();
                        factor1.putAll(factors.get(check[0]));
                        HashMap<String, Double> factor2 = new HashMap<>();
                        factor2.putAll(factors.get(check[1]));
                        if (check[0] < check[1]) {
                            factors.remove(check[1]);
                            factors.remove(check[0]);
                        }
                        else {
                            factors.remove(check[0]);
                            factors.remove(check[1]);
                        }
                        factors.add(join(factor1, factor2));
                        index = factors.size()-1;
                        num = num_of_factors(factors, hiddens.get(i));
                    }
                    // now (after we have only one factor that contains this hidden) it's time to eliminate!
                    HashMap<String, Double> cpt = new HashMap<>();
                    cpt.putAll(eliminate(factors.get(index), hiddens.get(i)));
                    factors.set(index, cpt);
                }
            }
        }
        // checking if we have multiple cpt's that contains the query variable
        int index = index_of_first_factor(factors, query);
        int num = num_of_factors(factors, query);
        // while numbers of the cpt's that contains the query
        // is greater than 1, then we will keep doing join
        while (num > 1) {
            int [] check = find_smallest_cpts_indexes(factors, query);
            HashMap<String, Double> factor1 = new HashMap<>();
            factor1.putAll(factors.get(check[0]));
            HashMap<String, Double> factor2 = new HashMap<>();
            factor2.putAll(factors.get(check[1]));
            if (check[0] < check[1]) {
                factors.remove(check[1]);
                factors.remove(check[0]);
            }
            else {
                factors.remove(check[0]);
                factors.remove(check[1]);
            }
            factors.add(join(factor1, factor2));
            index = factors.size()-1;
            num = num_of_factors(factors, query);
        }

        // now (after we have only one factor) it's time to normalize
        HashMap<String, Double> final_factor = normalize(factors.get(index));
        //  keys of this factor
        ArrayList<String> keys = new ArrayList<>(final_factor.keySet());
        // getting the query with it's outcome
        String query_with_out = get_query_with_evidence(quest);
        // checking in the factor the specific case that we want
        double final_prob = check_prob(query_with_out, keys, final_factor);
        String answer = final_prob +","+num_of_add+","+num_of_multiple;
        return answer;
    }
    // returns the query (without it's outcome)
    public String get_query(String quest) {
        int ind = quest.indexOf("=");
        String query = quest.substring(2,ind);
        return query;
    }
    // returns the query ( with it's outcome)
    public String get_query_with_evidence(String quest) {
        int end = quest.indexOf("|");
        String query = quest.substring(2,end);
        return query;
    }
    // copying all of the cpt's
    public ArrayList<HashMap<String, Double>> cpts_copy() {
        ArrayList<HashMap<String, Double>> cpts = new ArrayList<>();
        for (int i = 0; i < bayesian_network.size(); i++) {
            HashMap<String, Double> cpt = new HashMap<>();
            cpt.putAll(bayesian_network.get(i).cpt);
            cpts.add(cpt);
        }
        return cpts;
    }
    // returns the evidences (with their outcomes)
    public ArrayList<String> evidences(String quest) {
        int ind = quest.indexOf("|");
        if (quest.length() > ind+2) {
            int start = quest.indexOf("|")+1;
            int end = quest.indexOf(")");
            String evidences_str = quest.substring(start, end);
            if (evidences_str.contains(",")) { // has number of evidences
                ArrayList<String> evidences = new ArrayList<>(Arrays.asList(evidences_str.split(",")));
                return evidences;
            }
            else { // has only one evidence
                ArrayList<String> evidences = new ArrayList<>();
                evidences.add(evidences_str);
                return evidences;
            }
        }
        else { // doesn't have any evidence
            ArrayList<String> no_evidences = new ArrayList<>();
            return no_evidences;
        }
    }
    // returns the query and evidences without their outcomes
    public ArrayList<String> nodes_name_without_outcome(String str) {
        ArrayList<String> variables = new ArrayList<>();
        if (str.contains("|")) {
            int ind = str.indexOf("|");
            int equals = str.indexOf("=");
            String first = str.substring(2,equals);
            variables.add(first);
            if (str.length() > ind+2) {
                int start = str.indexOf("|")+1;
                int end = str.indexOf(")");
                String subbed = str.substring(start, end);
                if (subbed.contains(",")) {
                    ArrayList<String> names = new ArrayList<>(Arrays.asList(subbed.split(",")));
                    for (int i = 0; i < names.size(); i++) {
                        equals = names.get(i).indexOf("=");
                        String var = names.get(i).substring(0, equals);
                        variables.add(var);
                    }
                    return variables;
                }
                else {
                    equals = subbed.indexOf("=");
                    String var = subbed.substring(0, equals);
                    variables.add(var);
                    return variables;
                }
            }
            else {
                return variables;
            }
        }
        else {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(str.split(",")));
            for (int i = 0; i < names.size(); i++) {
                int equals = names.get(i).indexOf("=");
                String var = names.get(i).substring(0, equals);
                variables.add(var);
            }
            return variables;
        }
    }
    // returns the evidences without their outcomes
    public ArrayList<String> evidences_for_bayes_ball(String quest) {
        ArrayList<String> bb_evidences = new ArrayList<String>();
        if (quest.indexOf("|") + 2 == quest.length()) {
            int ind = quest.indexOf("=");
            String sub_arr = quest.substring(2,ind);
            bb_evidences.add(sub_arr);
        }
        else {
            String sub_string = quest.substring(quest.indexOf("|")+1);
            String [] evidence_split = sub_string.split(",");
            for (int i = 0; i < evidence_split.length; i++) {
                int ind = evidence_split[i].indexOf("=");
                String sub_str = evidence_split[i].substring(0, ind);
                bb_evidences.add(sub_str);
            }
        }
        return bb_evidences;
    }
    // returns the evidences without their outcomes from a formed quest
    public ArrayList<String> evidences_for_var(String quest) {
        ArrayList<String> bb_evidences = new ArrayList<String>();
        String [] evidence_split = quest.split(",");
        for (int i = 0; i < evidence_split.length; i++) {
            int ind = evidence_split[i].indexOf("=");
            String sub_str = evidence_split[i].substring(0, ind);
            bb_evidences.add(sub_str);
        }
        return bb_evidences;
    }
    // returns the query and evidences with their outcomes
    public ArrayList<String> nodes_name(String str) {
        if (str.contains("|")) {
            int ind = str.indexOf("|");
            String first = str.substring(2,ind);
            if (str.length() > ind+2) {
                int start = str.indexOf("|")+1;
                int end = str.indexOf(")");
                String subbed = str.substring(start, end);
                if (subbed.contains(",")) {
                    ArrayList<String> names = new ArrayList<>(Arrays.asList(subbed.split(",")));
                    names.add(0, first);
                    return names;
                }
                else {
                    ArrayList<String> names = new ArrayList<>();
                    names.add(first);
                    names.add(subbed);
                    return names;
                }
            }
            else {
                ArrayList<String> names = new ArrayList<>();
                names.add(first);
                return names;
            }
        }
        else {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(str.split(",")));
            return names;
        }
    }
    // deleting irrelevant rows from the cpt's (from the evidences)
    public ArrayList<HashMap<String, Double>> delete_irrelevant_rows(ArrayList<HashMap<String, Double>> cpts, ArrayList<String> evidences) {
        for (int i = 0; i < cpts.size(); i++) {
            ArrayList<String> keys = new ArrayList<>(cpts.get(i).keySet());
            for (int j = 0; j < keys.size(); j++) {
                ArrayList<String> key = nodes_name(keys.get(j));
                for (int k = 0; k < key.size(); k++) {
                    int ind = key.get(k).indexOf("=");
                    String var = key.get(k).substring(0,ind);
                    for (int l = 0; l < evidences.size(); l++) {
                        int ind1 = evidences.get(l).indexOf("=");
                        String evid = evidences.get(l).substring(0, ind1);
                        if (var.equals(evid)) {
                            if (!key.get(k).equals(evidences.get(l))) {
                                cpts.get(i).remove(keys.get(j));
                            }
                        }
                    }
                }
            }
        }
        return cpts;
    }
    // clear the empty factors
    public ArrayList<HashMap<String, Double>> clear_empty(ArrayList<HashMap<String, Double>> factors) {
        for (int i = factors.size()-1; i >= 0; i--) {
            if (factors.get(i).size() == 0) {
                factors.remove(factors.get(i));
            }
        }
        return factors;
    }
    // checking if the hidden is ancestor of the query or one of the evidences
    public boolean is_ancestor(String hidden, ArrayList<String> names) {
        NetNode node = new NetNode();
        for (int i = 0; i < this.bayesian_network.size(); i++) {
            if (this.bayesian_network.get(i).getName().equals(hidden)) {
                node = new NetNode(this.bayesian_network.get(i));
                break;
            }
        }
        boolean ans = is_ancestor_recursion(node, names);
        return ans;
    }
    // recursion helper function for is_ancestor
    public boolean is_ancestor_recursion(NetNode node, ArrayList<String> names) {
        for (int i = 0; i < names.size(); i++) {
            int ind = names.get(i).indexOf("=");
            String name = names.get(i).substring(0,ind);
            if (node.getName().equals(name)) {
                return true;
            }
        }
        for (int i = 0; i < node.childs.size(); i++) {
            boolean check = is_ancestor_recursion(node.childs.get(i), names);
            if (check) {
                return true;
            }
        }
        return false;
    }
    // returns the index of the first factor that contains the node
    public int index_of_first_factor(ArrayList<HashMap<String, Double>> factors, String node) {
        for (int i = 0; i < factors.size(); i++) {
            ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
            ArrayList<String> names = nodes_name_without_outcome(check.get(0));
            for (int j = 0; j < names.size(); j++) {
                if (names.get(j).equals(node)) {
                    return i;
                }
            }
        }
        // if no factor containing that node
        return -1;
    }
    // returns the number of factors that contains the node
    public int num_of_factors(ArrayList<HashMap<String, Double>> factors, String node) {
        int count = 0;
        for (int i = 0; i < factors.size(); i++) {
            ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
            ArrayList<String> names = nodes_name_without_outcome(check.get(0));
            for (int j = 0; j < names.size(); j++) {
                if (names.get(j).equals(node)) {
                    count++;
                    j = names.size()-1;
                }
            }
        }
        return count;
    }
    // returns the indexes of the two smallest factors that contains the node
    public int[] find_smallest_cpts_indexes(ArrayList<HashMap<String, Double>> factors, String node) {
        int[] ans = new int[2];
        HashMap<String, Double> min_cpt_1 = new HashMap<>();
        int min = Integer.MAX_VALUE;
        int min_index1 = 0;
        for (int i = 0; i < factors.size(); i++) {
            ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
            ArrayList<String> names = nodes_name_without_outcome(check.get(0));
            // checking the first min size
            for (int j = 0; j < names.size(); j++) {
                if (names.get(j).equals(node)) {
                    if (factors.get(i).size() < min) {
                        min = factors.get(i).size();
                        min_index1 = i;
                    }
                }
            }
        }
        min_cpt_1.putAll(factors.get(min_index1));
        HashMap<String, Double> min_cpt_2 = new HashMap<>();
        min = Integer.MAX_VALUE;
        int min_index2 = 0;
        for (int i = 0; i < factors.size(); i++) {
            ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
            ArrayList<String> names = nodes_name_without_outcome(check.get(0));
            // checking the second min size
            for (int j = 0; j < names.size(); j++) {
                if (names.get(j).equals(node)) {
                    if (i != min_index1) {
                        if (factors.get(i).size() < min) {
                            min = factors.get(i).size();
                            min_index2 = i;
                        }
                    }
                }
            }
        }
        min_cpt_2.putAll(factors.get(min_index2));
        // if the first min is smaller than the second min than we can it's index for sure
        if (min_cpt_1.size() < min_cpt_2.size()) {
            ans[0] = min_index1;
            ArrayList<HashMap<String, Double>> temps = new ArrayList<>();
            for (int i = 0; i < factors.size(); i++) {
                ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
                ArrayList<String> names = nodes_name_without_outcome(check.get(0));
                // checking if there's more factors with the second min size
                for (int j = 0; j < names.size(); j++) {
                    if (names.get(j).equals(node)) {
                        if (i != ans[0]) {
                            if (factors.get(i).size() == min) {
                                temps.add(factors.get(i));
                            }
                        }
                    }
                }
            }
            // if there's only one factor with the second min size we can add it'sindex for sure
            if (temps.size() == 1) {
                ans[1] = min_index2;
            }
            else {
                // if there's more than one factor with this size,
                // we will check the it's min value by ascii
                HashMap<String, Double> min_hash = ascii_check(temps);
                for (int i = 0; i < factors.size(); i++) {
                    if (min_hash.equals(factors.get(i))) {
                        ans[1] = i;
                    }
                }
            }
        }
        else {
            // if first min size equals to second min size
            ArrayList<HashMap<String, Double>> temps = new ArrayList<>();
            for (int i = 0; i < factors.size(); i++) {
                ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
                ArrayList<String> names = nodes_name_without_outcome(check.get(0));
                // checking if there's more factors with their min size
                for (int j = 0; j < names.size(); j++) {
                    if (names.get(j).equals(node)) {
                        if (factors.get(i).size() == min) {
                            temps.add(factors.get(i));
                        }
                    }
                }
            }
            // if we have only 2 factors with this size (first min and second min)
            // their indexes will be out answer
            if (temps.size() == 2) {
                ans[0] = min_index1;
                ans[1] = min_index2;
            }
            else {
                // if there's more than one factor with this size,
                // we will check the it's min value by ascii
                HashMap<String, Double> min_hash = ascii_check(temps);
                for (int i = 0; i < factors.size(); i++) {
                    if (min_hash.equals(factors.get(i))) {
                        ans[0] = i;
                    }
                }
                ArrayList<HashMap<String, Double>> temps1 = new ArrayList<>();
                for (int i = 0; i < factors.size(); i++) {
                    ArrayList<String> check = new ArrayList<>(factors.get(i).keySet());
                    ArrayList<String> names = nodes_name_without_outcome(check.get(0));
                    // check if there are more factors with the same size
                    for (int j = 0; j < names.size(); j++) {
                        if (names.get(j).equals(node)) {
                            if (i != ans[0]) {
                                if (factors.get(i).size() == min) {
                                    temps1.add(factors.get(i));
                                }
                            }
                        }
                    }
                }
                // returns the index of the minimun by ascii
                HashMap<String, Double> min_hash1 = ascii_check(temps1);
                for (int i = 0; i < factors.size(); i++) {
                    if (min_hash1.equals(factors.get(i))) {
                        ans[1] = i;
                    }
                }
            }
        }
        return ans;
    }
    // returns the index of the minimum value by ascii
    public HashMap<String, Double> ascii_check(ArrayList<HashMap<String, Double>> temps) {
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < temps.size(); i++) {
            ArrayList<String> check = new ArrayList<>(temps.get(i).keySet());
            String key = check.get(0);
            // getting the names of the nodes that are in the key
            ArrayList<String> variables = nodes_name(key);
            int sum = 0;
            // computing their ascii sum
            for (int j = 0; j < variables.size(); j++) {
                int ind = variables.get(j).indexOf("=");
                String var = variables.get(j).substring(0,ind);
                if (var.length() == 1) {
                    sum += variables.get(j).charAt(0);
                }
                else {
                    for (int k = 0; k < var.length(); k++) {
                        sum += variables.get(j).charAt(k);
                    }
                }
            }
            if (sum < min) {
                min = sum;
                index = i;
            }
        }
        return temps.get(index);
    }
    // returns a joined factor
    public HashMap<String, Double> join(HashMap<String, Double> factor1, HashMap<String, Double> factor2) {
        HashMap<String, Double> multiply = new HashMap<>();
        ArrayList<String> keys1 = new ArrayList<>(factor1.keySet());
        ArrayList<String> keys2 = new ArrayList<>(factor2.keySet());
        // going through each key of factor1
        for (int i = 0; i < keys1.size(); i++) {
            String key_check1 = keys1.get(i);
            String key1 = "";
            if (key_check1.charAt(0) == 'P') {
                int ind = key_check1.indexOf("|");
                if (ind + 2 == key_check1.length()) {
                    key1 = key_check1.substring(2,ind);
                }
                else {
                    key1 = key_check1.substring(2,ind)+","+key_check1.substring(ind+1, key_check1.length()-1);
                }
            }
            else {
                key1 = key_check1;
            }
            String [] key1_vars = key1.split(",");
            // going through each key of factor2
            for (int j = 0; j < keys2.size(); j++) {
                boolean should_multiply = true;
                String key_check2 = keys2.get(j);
                String key2 = "";
                if (key_check2.charAt(0) == 'P') {
                    int ind = key_check2.indexOf("|");
                    if (ind + 2 == key_check2.length()) {
                        key2 = key_check2.substring(2,ind);
                    }
                    else {
                        key2 = key_check2.substring(2,ind)+","+key_check2.substring(ind+1, key_check2.length()-1);
                    }
                }
                else {
                    key2 = key_check2;
                }
                String [] key2_vars = key2.split(",");
                ArrayList<String> diffrent = new ArrayList<>();
                // going through every variable in each key of factor 2
                for (int k = 0; k < key2_vars.length; k++) {
                    int i2 = key2_vars[k].indexOf("=");
                    String var2 = key2_vars[k].substring(0,i2);
                    boolean is_diff = true;
                    // going through every variable in each key of factor 1
                    for (int l = 0; l < key1_vars.length; l++) {
                        int i1 = key1_vars[l].indexOf("=");
                        String var1 = key1_vars[l].substring(0,i1);
                        // checking if they are different
                        if (var1.equals(var2)) {
                            is_diff = false;
                            String out1 = key1_vars[l].substring(i1+1);
                            String out2 = key2_vars[k].substring(i2+1);
                            // it they are not different
                            // checking if their outcome is different
                            if (!out1.equals(out2)) {
                                should_multiply = false;
                                l = key1_vars.length-1;
                                k = key2_vars.length-1;
                            }
                        }
                    }
                    // after we went through all of key 1
                    //  we can know for sure if it's variable is different
                    if (is_diff) {
                        diffrent.add(key2_vars[k]);
                    }
                }
                // if every same variable has the same outcome
                // we would like to multiply their rows
                if (should_multiply) {
                    // adding the num of mul operations
                    num_of_multiple++;
                    // setting the correct key for this multiplication
                    String key = key1;
                    for (int k = 0; k < diffrent.size(); k++) {
                        key += ","+diffrent.get(k);
                    }
                    double multiple = factor1.get(keys1.get(i)) * factor2.get(keys2.get(j));
                    // adding to our joined factor the key and the probability
                    multiply.put(key, multiple);
                }
            }
        }
        return multiply;
    }
    // returns a new factor after we eliminate the given hidden from the given factor
    public HashMap<String, Double> eliminate(HashMap<String, Double> factor, String hidden) {
        HashMap<String, Double> new_fac = new HashMap<>();
        ArrayList<String> new_keys = new ArrayList<>();
        ArrayList<String> old_keys = new ArrayList<>(factor.keySet());
        for (int i = 0; i < old_keys.size(); i++) {
            // getting the keys without the hidden
            String new_key = remove_hidden_from_key(old_keys.get(i), hidden);
            boolean add = true;
            for (int j = 0; j < new_keys.size(); j++) {
                if (new_key.equals(new_keys.get(j))) {
                    add = false;
                }
            }
            if (add) {
                new_keys.add(new_key);
            }
        }
        for (int i = 0; i < new_keys.size(); i++) {
            String key = new_keys.get(i);
            // array list that holds the indexes we would like to add together
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int j = 0; j < old_keys.size(); j++) {
                String check_key = remove_hidden_from_key(old_keys.get(j), hidden);
                if (key.equals(check_key)) {
                    indexes.add(j);
                }
            }
            double adding = 0;
            // after we deleted the hidden
            // we are adding the same keys
            for (int j = 0; j < indexes.size(); j++) {
                adding += factor.get(old_keys.get(indexes.get(j)));
                // adding the number of adding operations
                num_of_add++;
            }
            // example: if we have 4 variables we want to sum, then we'll have 3 add operations
            num_of_add--;
            // adding to our new factor the key and the probability
            new_fac.put(key, adding);
        }
        return new_fac;
    }
    // returns the key without the given hidden
    public String remove_hidden_from_key(String key, String hidden) {
        String new_key = "";
        if (key.contains("|")) {
            int ind = key.indexOf("|");
            String var = key.substring(2,ind);
            if (!var.equals(hidden)) {
                new_key += var;
            }
            String [] splitted = key.substring(ind+1).split(",");
            for (int i = 0; i < splitted.length; i++) {
                ind = splitted[i].indexOf("=");
                var = splitted[i].substring(0,ind);
                if (!var.equals(hidden)) {
                    new_key += ","+var+"=";
                    String outcome = "";
                    if (splitted[i].contains(")")) {
                        int end = splitted[i].indexOf(")");
                        outcome = splitted[i].substring(ind+1,end);
                    }
                    else {
                        outcome = splitted[i].substring(ind+1);
                    }
                    new_key += outcome;
                }
            }
        }
        else {
            String [] splitted = key.split(",");
            boolean first = true;
            for (int i = 0; i < splitted.length; i++) {
                int ind = splitted[i].indexOf("=");
                String var = splitted[i].substring(0, ind);
                if (!var.equals(hidden)) {
                    if (!first) {
                        new_key += ",";
                    }
                    new_key += var+"=";
                    String outcome = splitted[i].substring(ind+1);
                    new_key += outcome;
                    first = false;
                }
            }
        }
        return new_key;
    }
    // returns a new factor after normalization
    public HashMap<String, Double> normalize(HashMap<String, Double> factor) {
        ArrayList<String> keys = new ArrayList<>(factor.keySet());
        double sum = 0;
        double prob = 0;
        for (int i = 0; i < keys.size(); i++) {
            // adding the number of adding operations
            num_of_add++;
            // calculating the su, of all of the probabilities
            sum += factor.get(keys.get(i));
        }
        // example: if we have 4 variables we want to sum, then we'll have 3 add operations
        num_of_add--;
        // removing and adding the updated (normalized) probability
        for (int i = 0; i < keys.size(); i++) {
            double prb = factor.get(keys.get(i));
            factor.remove(keys.get(i));
            factor.put(keys.get(i), prb/sum);
        }
        return factor;
    }
    // returns the specific probability
    public double check_prob(String query, ArrayList<String> keys, HashMap<String, Double> factor) {
        double prob = 0;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).contains(query)) {
                prob = factor.get(keys.get(i));
                i = keys.size()-1;
            }
        }
        // updating the probabilty so we'll have only 5 digits after the decimal point
        double final_prob = (double)Math.round(prob * 100000d) / 100000d;
        return final_prob;
    }
}