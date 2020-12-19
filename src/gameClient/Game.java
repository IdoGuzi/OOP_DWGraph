package gameClient;

import Server.Agent_Graph_Algo;
import Server.Game_Server_Ex2;
import api.dw_graph_algorithms;
import api.game_service;
import api.node_data;
import classes.DWGraph_Algo;
import org.json.JSONObject;

import java.util.*;

/**
 * This class is the brain of the game.
 * it's loading the game from the server and running the game
 */
public class Game {
    private int level;
    private game_service game;
    private Arena ar;
    private MyFrame win;
    private HashMap<Integer, LinkedList<Integer>> agent_path;
    private dw_graph_algorithms ga;
    private long id;

    public Game(int lvl){
        id=-1;
        init(lvl);
    }

    public Game(Long id,int level){
        this.id=id;
        init(level);
    }

    /**
     * this is the main method of the class,
     * it's starting the game after loading the level from the server
     */
    public void play(){
        Thread server = new Thread(new ServerTalker());
        //Thread cutter = new Thread(new Cutter());
        win.show();
        sleep(1000);
        if (id!=-1) {
            game.login(id);
        }
        game.startGame();
        server.start();
        //cutter.start();
        while (game.isRunning()){
            //System.out.println("before pathFinder");
            pathFinder();
            sleep(50);
        }
        try {server.join();}catch (Exception e) {e.printStackTrace();}
        System.out.println(game.toString());
    }

    /**
     * this method decides which pokemon is the most valueble and find the closest agent
     */
    public void pathFinder(){
        PriorityQueue<CL_Pokemon> queue= new PriorityQueue<>(1,new PokemonComperator());
        queue.addAll(ar.getPokemons());
        whileloop:
        while (!queue.isEmpty()){
            CL_Pokemon p = queue.poll();
            if (p.getAssignedAgent()!=-1) continue;
            try {
                System.out.println("pokemon on edge: [" + p.get_edge().getSrc() + "," + p.get_edge().getDest() + "] is assigned to agent: " + p.getAssignedAgent());
            }catch (NullPointerException e) {
                System.out.println("pokemon on edge: [" + p.get_edge().getSrc() + "," + p.get_edge().getDest() + "] is assigned to agent: null");
            }
            CL_Agent closest=null;
            double min_dist=Double.MAX_VALUE;
            for (CL_Agent a : ar.getAgents()){
                //System.out.println("agent pokemon=["+a.get_curr_fruit().get_edge().getSrc()+","+a.get_curr_fruit().get_edge().getDest()+"]");
                //if (a.get_curr_fruit()!=null) continue;
                //if (a.getNextNode()!=-1) continue;
                if (a.getSrcNode()==p.get_edge().getSrc()){
                    agent_path.get(a.getID()).add(p.get_edge().getDest());
                    p.setAssignedAgent(a.getID());
                    a.set_curr_fruit(p);
                    continue whileloop;
                }
                if (agent_path.get(a.getID()).isEmpty()) {
                    emptyPathFiller(a);
                }
                double temp = ga.shortestPathDist(agent_path.get(a.getID()).getLast(),p.get_edge().getSrc());
                if (temp<min_dist){
                    closest=a;
                    min_dist=temp;
                }
            }
            findMiddleRoad(p,closest);
        }
    }

    /**
     * this method find the best path for the agent to get to the pokemon through the already existing path of the agent
     * @param p - the pokemon to bee added for the agent
     * @param a - agent that this function append his path
     */
    public void findMiddleRoad(CL_Pokemon p, CL_Agent a){
        if (agent_path.get(a.getID()).isEmpty()) emptyPathFiller(a);
        double link_dist = ga.shortestPathDist(agent_path.get(a.getID()).getLast(),p.get_edge().getSrc())+p.get_edge().getWeight();
        List<Integer> path = agent_path.get(a.getID());
        int outNodeIndex=-1, inNodeIndex=-1;
        double min_dist=Double.MAX_VALUE;
        for (int i=0;i<path.size()-1;i++){
            for (int j=i+1;j<path.size();j++){
                double temp = ga.shortestPathDist(path.get(i),p.get_edge().getSrc()) + p.get_edge().getWeight() + ga.shortestPathDist(p.get_edge().getDest(),path.get(j));
                if (temp<min_dist){
                    min_dist=temp;
                    outNodeIndex=i;
                    inNodeIndex=j;
                }
            }
        }
        if (min_dist<link_dist) {
            List<node_data> path1 = ga.shortestPath(path.get(outNodeIndex), p.get_edge().getSrc());
            List<node_data> path2 = ga.shortestPath(p.get_edge().getDest(), path.get(inNodeIndex));
            System.out.println("agent number: " + a.getID() + " old path is: " + agent_path.get(a.getID()).toString());
            System.out.println("agent number: " + a.getID() + " new path1 is: " + path1.toString());
            System.out.println("agent number: " + a.getID() + " new path2 is: " + path2.toString());
            path1.remove(0);
            path2.remove(path2.size() - 1);
            for (int i=inNodeIndex; i != outNodeIndex+1; i--) {
                try {
                    agent_path.get(a.getID()).remove(outNodeIndex+1);
                }catch (Exception e){
                    System.out.println("path to remove from " +agent_path.get(a.getID()).toString() + " indexs: out: "+ outNodeIndex + ", in: " +inNodeIndex);
                    agent_path.get(a.getID()).remove(i);
                }
            }
            for (int i = path2.size()-1; 0 <= i; i--) {
                node_data n = path2.get(i);
                agent_path.get(a.getID()).add(outNodeIndex + 1, n.getKey());
            }
            for (int i = path1.size()-1; 0 <= i; i--) {
                node_data n = path1.get(i);
                agent_path.get(a.getID()).add(outNodeIndex + 1, n.getKey());
            }
            System.out.println("agent number: " + a.getID() + " new path is: " + agent_path.get(a.getID()).toString());
        }else {
            System.out.println("agent number: " + a.getID() + " old path is: " + agent_path.get(a.getID()).toString());
            System.out.println("agent number: " + a.getID() + " is on node: " + a.getSrcNode() + " and going to "+ a.getNextNode());
            if (agent_path.get(a.getID()).isEmpty()) emptyPathFiller(a);
            List<node_data> path_from_end = ga.shortestPath(agent_path.get(a.getID()).getLast(), p.get_edge().getSrc());
            for (node_data n : path_from_end){
                agent_path.get(a.getID()).add(n.getKey());
            }
            agent_path.get(a.getID()).add(p.get_edge().getDest());
            a.set_curr_fruit(p);
            System.out.println("agent number: " + a.getID() + " new path that link to the end is: " + agent_path.get(a.getID()).toString());
        }
        p.setAssignedAgent(a.getID());
    }


    /**
     * this method sets agents next node to go to for the server call
     */
    public void setAgentMove(){
        for (CL_Agent a : ar.getAgents()){
            if (a.getNextNode()!=-1) continue;
            if (!agent_path.get(a.getID()).isEmpty()) {
                int next=agent_path.get(a.getID()).remove();
                boolean flag = true;
                while(next==a.getSrcNode() && !agent_path.get(a.getID()).isEmpty()) {
                    next = agent_path.get(a.getID()).remove();
                }
                if (next==a.getNextNode()) flag=false;
                if (ga.getGraph().getEdge(a.getSrcNode(),next)==null) {
                    agent_path.get(a.getID()).clear();
                    flag=false;
                }
                if (flag) {
                    game.chooseNextEdge(a.getID(), next);
                }else {
                    System.out.println("agent can't be moved");
                    continue;
                }
                System.out.println("agent num: " + a.getID()+" is going from "+ a.getSrcNode() + " to "+ next);
                System.out.println("agent num: "+ a.getID() + " path is: "+ agent_path.get(a.getID()).toString());
            }
        }
    }

    /**
     * update the agents from the server
     */
    public void setUpdatedAgents(){
        List<CL_Agent> update = Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph());
        for (CL_Agent a: update){
            for (CL_Agent b : ar.getAgents()){
                if (a.getID()==b.getID()){
                    if (ar.getPokemons().contains(b.get_curr_fruit())){
                        a.set_curr_fruit(b.get_curr_fruit());
                    }else a.set_curr_fruit(null);
                }
            }
        }
        ar.setAgents(update);
    }

    /**
     * update the pokemons from the sever
     */
    public void setUpdatedPokemons(){
        //System.out.println(game.getPokemons());
        List<CL_Pokemon> pokemons = Agent_Graph_Algo.json2Pokemons(game.getPokemons());
        for (int i=0;i<pokemons.size();i++){
            Agent_Graph_Algo.updateEdge(pokemons.get(i),ar.getGraph());
        }
        List<CL_Pokemon> update = new ArrayList<>();
        for (CL_Pokemon p : pokemons){
            boolean found=false;
            for (CL_Pokemon c : ar.getPokemons()){
                if (p.equals(c)) {
                    //System.out.println("equals pokemons found: [" + c.get_edge().getSrc()+","+c.get_edge().getDest()+"]");
                    found=true;
                    update.add(c);
                }
            }
            if (!found) update.add(p);
        }
        ar.setPokemons(update);
    }

    /**
     * this method stop the running thread for the provided time (1000 is 1 second)
     * @param time
     */
    private void sleep(int time){
        try {
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * adds agents to the server by the most valueble pokemon currently known
     * @param agent_num
     */
    private void addAgents(int agent_num){
        PriorityQueue<CL_Pokemon> queue = new PriorityQueue<>(1,new PokemonComperator());
        queue.addAll(ar.getPokemons());
        if (ar.getPokemons().size()>=agent_num){
            for (int i=0;i<agent_num;i++){
                CL_Pokemon p = queue.poll();
                game.addAgent(p.get_edge().getSrc());
            }
        }else{
            int count =0;
            while (!queue.isEmpty()){
                CL_Pokemon p = queue.poll();
                game.addAgent(p.get_edge().getSrc());
                count++;
            }
            Iterator<node_data> itr  = ar.getGraph().getV().iterator();
            while (count<agent_num){
                if (!itr.hasNext()) itr = ar.getGraph().getV().iterator();
                game.addAgent(itr.next().getKey());
            }
        }
    }


    /**
     * fill an agent path with the node the agent is on/ going to
     * @param a
     */
    private void emptyPathFiller(CL_Agent a){
        if (a.getNextNode()==-1) {
            agent_path.get(a.getID()).add(a.getSrcNode());
        }else agent_path.get(a.getID()).add(a.getNextNode());
    }

    /**
     * private class that should by run on a thread that communicates with the game server
     */
    private class ServerTalker implements Runnable{
        @Override
        public void run() {
            while(game.isRunning()) {
                setUpdatedPokemons();
                setUpdatedAgents();
                setAgentMove();
                game.move();
                win.repaint();
                sleep(50);
            }
        }
    }


    private void init (int lvl){
        this.level=lvl;
        this.game= Game_Server_Ex2.getServer(this.level);
        System.out.println(game);
        this.ar=new Arena();
        DWGraph_Algo ga = new DWGraph_Algo();
        System.out.println(game.getGraph());
        ga.loadFromString(game.getGraph());
        this.ar.setGraph(ga.getGraph());
        this.ga = ga;
        System.out.println(game.getPokemons());
        this.ar.setPokemons(Agent_Graph_Algo.json2Pokemons(game.getPokemons()));
        for (int i=0;i<ar.getPokemons().size();i++) Agent_Graph_Algo.updateEdge(ar.getPokemons().get(i),ar.getGraph());
        try {
            int agent_num = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("agents");
            addAgents(agent_num);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.ar.setAgents(Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph()));
        System.out.println(game.getAgents());
        this.win=new MyFrame("my game");
        this.win.update(ar);
        this.win.setSize(1000,700);
        this.agent_path = new HashMap<>();
        for (CL_Agent a : ar.getAgents()){
            agent_path.put(a.getID(),new LinkedList<>());
        }
        for (CL_Pokemon p : ar.getPokemons()){
            for (CL_Agent a : ar.getAgents())
                if (p.get_edge().getSrc()==a.getSrcNode()){
                    p.setAssignedAgent(a.getID());
                    a.set_curr_fruit(p);
                    agent_path.get(a.getID()).add(p.get_edge().getDest());
                }
        }
    }

    /**
     * comperator for pokemons
     * if p1 cost is higher than p2 cost return value positive
     */
    private class PokemonComperator implements Comparator<CL_Pokemon>{
        private static final double EPS=0.0001;
        public PokemonComperator(){}
        @Override
        public int compare(CL_Pokemon o1, CL_Pokemon o2) {
            if (o1.getValue()-o2.getValue()>EPS) return 1;
            if (o1.getValue()-o2.getValue()<EPS) return 0;
            if (o1.getValue()-o2.getValue()<-EPS) return -1;
            return 0;
        }
    }
}