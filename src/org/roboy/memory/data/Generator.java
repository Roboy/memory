package org.roboy.memory.data;

import org.roboy.memory.util.Config;
import org.roboy.memory.util.MemoryOperations;
import com.google.gson.Gson;

public class Generator {

    private  static final String VERSION = "0.0.2";

    private static int incrementalCounter = 0;

    private static final String[] roboySkillsAbilitiesList = {
            //ROBOY 2.0
            "'skills':'telling jokes,telling fun facts,telling you about the famous people and places,doing the math','abilities':'talk to people,recognize objects,show emotions,move my body,shake a hand,party like there is no tomorrow,surf the internet,answer your questions','birthdate':'12.04.2018','full_name':'roboy 2.0','future':'become a robot rights advocate,help people and robots become friends,find the answer to the question of life the universe and everything,visit mars and other planets,become a music star,become a michelin star chef,get a robo pet,become as good as my father','sex':'male'",
            //ROBOY 1.0
            "\"abilities\":\"talking to people,reading wikipedia,reading reddit\",\"skills\":\"tell jokes,tell fun facts about famous people and places,recognize you next time we meet,remember your name and our conversation\",\"full_name\":\"roboy junior\",\"birthdate\":\"08.03.2013\",\"sex\":\"male\""
    };

    // LISTS OF DATA THAT SHALL BE USED
    // It is highly advisable to use lists of prime numbers. It seems to make nicer distributions.
    // The reason is probably found somewhere in the script of Discrete Structures and has something to do with groups.

    private static final String[] nameList = {
//            "Joseph", "Wagram", "Heather", "Alona", "Jason", "Lora",
            "Bob", "Dylan", "Chad", "George", "Bill", "Sam"
            ,"Aaron", "John", "Robert", "Matt", "Greg", "Joy",
            "Daniel", "Mark", "Markus", "Tristan", "Alina"
            ,"Fabian", "Fabio", "Judith", "Sandra", "Cynthia",
            "Chrissi", "Cass", "Lena", "Donald", "Chrystal"
            ,"Chris", "Lisa", "Bart", "David", "Zoe", "Leo",
            "Fatima", "Yuri","Jenny","Corey", "Davis", "Alissa"
    };

    private static final String[] workList = {
            "Roboy", "AISEC", "TUM", "Apple", "Google", "Autodesk", "uTUM", "DB", "Siemens", "Bosch", "NSA"
    };

    private static final String[] universityList = {
            "TUM", "LMU", "RWTH", "Uni Heidelberg", "KIT", "UCL", "TU Berlin", "Uni Cologne", "ETH", "MIT", "Georgia Tech", "NYU", "Penn State"
    };
    private static final String[] countryList = {
            "Suzhou", "Munich", "Berlin", "Cologne", "Columbus", "Saigon", "Shanghai"
    };
    private static final String[] hobbyList = {
            "canoe", "music", "reading"
    };

    public static void main(String[] args){

        if(Config.NEO4J_ADDRESS.contains("127.0.0.1") || Config.NEO4J_ADDRESS.contains("localhost")) {
            generateRoboy();
            genAllNodes();
            createAllRelationships();
            warnRoot();

        }
        else{
            System.out.println("Generator is programmed not to work with any addresses that are not 127.0.0.1 or localhost");
            System.out.println("Please check your environmental variables and see if they are configured correctly");
            System.out.println("Your current NEO4J_ADDRESS is "+Config.NEO4J_ADDRESS);
            System.out.println("System Expected something amongst the lines of \"bolt://localhost:7687\" or \"bolt://127.0.0.1:7687\"");
            System.out.println("See: https://roboy-memory.readthedocs.io/en/latest/Usage/1_getting_started.html#configuring-the-package");
            System.out.println("\nExiting Generator...");
        }
    }



    /**
     * Method to create Person Node Jsons. Note that this will become Deprecated as soon as Wagram's patch hits.
     */
    private static String jsonCreationPerson(String label, String name){
        return String.format("{'label':'%s','properties':{'name':'%s', 'generated':'%s',telegram_id:'local'}}", label.toLowerCase(), name.toLowerCase(), VERSION);
    }
    /**
     * Method to create all other kind of node's JSONs
     */
    private static String jsonCreation(String label, String name){
        return String.format("{'label':'%s','properties':{'name':'%s', 'generated':'%s'}}", label.toLowerCase(), name.toLowerCase(), VERSION);
    }
    private static String jsonCreation(String label, String name, String properties){
        return String.format("{'label':'%s','properties':{'name':'%s', 'generated':'%s' %s}}", label.toLowerCase(), name.toLowerCase(), VERSION, properties.isEmpty() ? "": ","+properties);
    }

    /**
     * Generates all nodes from the data above
     *
     * TODO: Rewrite to create a Lookup Table for the Relations section --> Efficiency
     */
    private static void genAllNodes(){
        for(String str : nameList){
            if(MemoryOperations.get(jsonCreationPerson("Person", str)).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreationPerson("Person", str));
            else System.out.println(str+" already exists");
        }
        for(String str : workList){
            if(MemoryOperations.get(jsonCreation("Organization", str)).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreation("Organization", str));
            else System.out.println(str+" already exists");
        }
        for(String str : universityList){
            if(MemoryOperations.get(jsonCreation("Organization", str)).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreation("Organization", str));
            else System.out.println(str+" already exists");
        }
        for(String str : countryList){
            if(MemoryOperations.get(jsonCreation("Country", str)).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreation("Country", str));
            else System.out.println(str+" already exists");
        }
        for(String str : hobbyList){
            if(MemoryOperations.get(jsonCreation("Hobby", str)).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreation("Hobby", str));
            else System.out.println(str+" already exists");
        }
    }

    /**
     * Generate Roboy Nodes, if they do not exist
     */
    private static void generateRoboy(){
            if(MemoryOperations.get(jsonCreation("Robot", "roboy two")).equals("{\"id\":[]}")) MemoryOperations.create(jsonCreation("Robot", "roboy two", roboySkillsAbilitiesList[0]));
            if(MemoryOperations.get(jsonCreation("Robot", "roboy")).equals("{\"id\":[]}")) MemoryOperations.create(jsonCreation("Robot", "roboy", roboySkillsAbilitiesList[1]));

            int roboy1 = nameToNode("Robot", "roboy");
            int roboy2 = nameToNode("Robot", "roboy two");

            if(MemoryOperations.get(jsonCreation("Country", "zurich")).equals("{\"id\":[]}"))
                MemoryOperations.create(jsonCreation("Country", "zurich"));

            createRelationship(roboy1, roboy2, "SIBLING_OF");

            createRelationship(roboy1, nameToNode("Country", "munich"), "LIVE_IN");
            createRelationship(roboy1, nameToNode("Country", "zurich"), "FROM");

            createRelationship(roboy2, nameToNode("Country", "munich"), "LIVE_IN");
            createRelationship(roboy2, nameToNode("Country", "munich"), "FROM");
    }

    private static void warnRoot(){
        if(MemoryOperations.cypher("MATCH (n) WHERE id(n) = 0 RETURN n").equals("[]")) {
            System.out.println("No Root Detected, everything okay!");
        }
        else{
            System.err.println("Some poor node has been delegated file 0. Please report this on Github ASAP. This should NOT happen, ever.");
        }
    }

    /**
     * Creates all the relationships between each person and their country, hobby, job and uni
     */
    private static void createAllRelationships(){
        Gson gson = new Gson();
        for(String name : nameList){

            int nameID = nameToNode("Person", name);

            createRelationship(nameID, nameToNode("Country", countryList[incrementalCounter++% countryList.length]), "FROM");
            createRelationship(nameID, nameToNode("Hobby", hobbyList[incrementalCounter++%hobbyList.length]), "HAS_HOBBY");
            createRelationship(nameID, nameToNode("Organization", workList[incrementalCounter++%workList.length]), "WORK_FOR");
            createRelationship(nameID, nameToNode("Organization", universityList[incrementalCounter++%universityList.length]), "STUDY_AT");
            if(incrementalCounter%5==0) {
                createRelationship(nameID, nameToNode("Robot", "roboy"), "FRIEND_OF");
                createRelationship(nameID, nameToNode("Robot", "roboy two"), "FRIEND_OF");

            }
        }
    }

    /**
     * Creates the Relationship using this data.
     * @param personNodeID ID of the Person, from which the Relationship Propagates
     * @param targetNodeID ID of the Node, which is the target of the Relationship
     * @param relation_type Specify the type of relationship between said nodes
     */
    private static void createRelationship(int personNodeID, int targetNodeID, String relation_type){
        MemoryOperations.cypher(String.format("MATCH (n) WHERE ID(n)=%s  MATCH (m0) WHERE ID(m0) IN [%s]   MERGE (n)-[r0:%s]-(m0)  RETURN n ", personNodeID, targetNodeID, relation_type));
    }

    /**
     * Looks up the ID of a node, given it's name. This method requires the field to be specified to a degree, that it is unique. Else a RuntimeException is thrown.
     * @param type Specify Type of Node
     * @param name Specify Name of Node
     * @return Returns ID of node, which is of given type and name
     *
     * TODO: Replace Runtime Exception with Optional or Null
     */
    private static int nameToNode(String type, String name){
        String str = MemoryOperations.get(jsonCreation(type, name));
        str = str.replaceAll("[^\\d,]", ""); //This REGEX filters out all ASCII except commas and numbers
        System.out.println(str);
        if(str.equals("")) return 0;
        if(str.contains(",")) throw new RuntimeException(String.format("Double Entry Exists in NEO4J Database. %s is represented by nodes %s. Please delete these nodes manually through cypher. This error should not occur unless you have added your own duplicate nodes. If this occurs, without you having added something, report this on Github."));
        else return Integer.parseInt(str);
    }
}
