package gameClient;

import Server.Agent_Graph_Algo;
import Server.Game_Server_Ex2;
import api.dw_graph_algorithms;
import api.game_service;
import api.node_data;
import classes.DWGraph_Algo;
import org.json.JSONObject;

import java.util.*;

public class Game {
    private int level;
    private game_service game;
    private Arena ar;
    private MyFrame win;
    private HashMap<Integer, LinkedList<Integer>> agent_path;

    public Game(int lvl){
        this.level=lvl;
        this.game= Game_Server_Ex2.getServer(this.level);
        System.out.println(game);
        this.ar=new Arena();
        DWGraph_Algo ga = new DWGraph_Algo();
        System.out.println(game.getGraph());
        ga.loadFromString(game.getGraph());
        this.ar.setGraph(ga.getGraph());
        System.out.println(game.getPokemons());
        this.ar.setPokemons(Agent_Graph_Algo.json2Pokemons(game.getPokemons()));
        for (int i=0;i<ar.getPokemons().size();i++){
            Agent_Graph_Algo.updateEdge(ar.getPokemons().get(i),ar.getGraph());
        }
        try {
            int agent_num = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("agents");
            for (int i=0;i<agent_num;i++){
                game.addAgent(ar.getPokemons().get(i).get_edge().getSrc());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.ar.setAgents(Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph()));
        for (int i=0;i<ar.getAgents().size();i++){
            System.out.println(game.getAgents());
        }
        this.win=new MyFrame("my game");
        this.win.update(ar);
        this.win.setSize(1000,700);
        this.agent_path = new HashMap<>();
        for (CL_Agent a : ar.getAgents()){
            agent_path.put(a.getID(),new LinkedList<>());
        }
    }


    public void play(){
        game.startGame();
        //win.setVisible(true);
        win.show();
        sleep(1000);
        while (game.isRunning()){
            setUpdatedAgents();
            setUpdatedPokemons();
            for (CL_Pokemon p : ar.getPokemons()) {
                System.out.println("pokemon on edge: ["+ p.get_edge().getSrc()+","+p.get_edge().getDest()+"]");
            }
            GeneralPlanningMove();
            for (CL_Agent agent : ar.getAgents()){
                int id = agent.getID();
                System.out.println("path of agent:"+id+ " that is on "+ agent.getSrcNode() +" is:"+agent_path.get(id).toString());
                boolean flag = false;
                if (agent_path.get(id).isEmpty()) {
                    flag=agentPlan(id);
                }else flag=true;
                if (flag) {
                    while (agent.getSrcNode()==agent_path.get(id).getFirst()){
                        agent_path.get(id).remove();
                    }
                    int node = agent_path.get(id).remove();
                    game.chooseNextEdge(id,node);
                    agent.setNextNode(node);
                }
            }
            pokemonEating();
            win.repaint();
            sleep(500);
            for (CL_Agent agent :ar.getAgents()){
                System.out.println("agent: "+agent.getID()+" is going from "+agent.getSrcNode() + " to  --> " +agent.getNextNode());
            }
            game.move();
        }
        System.out.println(game.toString());
    }

    public boolean agentPlan(int agent_id){
        CL_Agent a=null;
        for (CL_Agent b : ar.getAgents()){
            if (b.getID()==agent_id) {
                a = b;
            }
        }
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(ar.getGraph());
        CL_Pokemon closest=null;
        double min_dist=Double.MAX_VALUE;
        for (CL_Pokemon p : ar.getPokemons()){
            if (p.getAssignedAgent()!=-1) continue;
            double temp_dist = ga.shortestPathDist(a.getSrcNode(),p.get_edge().getSrc());
            if (temp_dist<min_dist){
                closest=p;
                min_dist=temp_dist;
            }
        }
        if (closest==null) return false;
        List<node_data> path = ga.shortestPath(a.getSrcNode(),closest.get_edge().getSrc());
        for (node_data n:path){
            agent_path.get(a.getID()).add(n.getKey());
        }
        agent_path.get(a.getID()).add(closest.get_edge().getDest());
        closest.setAssignedAgent(a.getID());
        return true;
    }

    public void GeneralPlanningMove(){
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(ar.getGraph());
        for (CL_Pokemon p : ar.getPokemons()){
            if (p.getAssignedAgent()!=-1) continue;
            int closest_agent_id=-1;
            CL_Agent ag = null;
            double min_dist=Double.MAX_VALUE;
            for (CL_Agent a : ar.getAgents()){
                double temp_dist;
                if (agent_path.get(a.getID()).isEmpty()){
                    if (a.getSrcNode()==p.get_edge().getSrc()) {
                        temp_dist = 0;
                    }else {
                        temp_dist = ga.shortestPathDist(a.getSrcNode(), p.get_edge().getSrc());
                    }
                }else {
                    if (agent_path.get(a.getID()).getLast()==p.get_edge().getSrc()){
                        temp_dist=0;
                    }else {
                        temp_dist = ga.shortestPathDist(agent_path.get(a.getID()).getLast(), p.get_edge().getSrc());
                    }
                }
                if (temp_dist<min_dist){
                    closest_agent_id=a.getID();
                    min_dist=temp_dist;
                    ag=a;
                }
            }
            List<node_data> path;
            if (agent_path.get(closest_agent_id).isEmpty()){
                path = ga.shortestPath(ag.getSrcNode(), p.get_edge().getSrc());
            }else {
                path = ga.shortestPath(agent_path.get(closest_agent_id).getLast(), p.get_edge().getSrc());
            }
            for (node_data n : path) {
                agent_path.get(closest_agent_id).add(n.getKey());
            }
            agent_path.get(closest_agent_id).add(p.get_edge().getDest());
            p.setAssignedAgent(closest_agent_id);
        }
    }


    public void setUpdatedAgents(){
        ar.setAgents(Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph()));
        System.out.println(game.getAgents());
    }
    public void setUpdatedPokemons(){
        List<CL_Pokemon> pokemons = Agent_Graph_Algo.json2Pokemons(game.getPokemons());
        for (int i=0;i<pokemons.size();i++){
            Agent_Graph_Algo.updateEdge(pokemons.get(i),ar.getGraph());
        }
        List<CL_Pokemon> update = ar.getPokemons();
        for (CL_Pokemon p : pokemons){
            boolean found=false;
            for (CL_Pokemon c : ar.getPokemons()){
                if (p.equals(c)) found=true;
            }
            if (!found) update.add(p);
        }
        ar.setPokemons(update);
    }

    private void pokemonEating(){
        List<CL_Pokemon> toDelete = new ArrayList<>();
        for (CL_Agent agent:ar.getAgents()){
            for (CL_Pokemon poke:ar.getPokemons()){
                if (agent.getSrcNode()==poke.get_edge().getSrc() &&
                        agent.getNextNode()==poke.get_edge().getDest()){
                    toDelete.add(poke);
                }
            }
        }
        List<CL_Pokemon> update = ar.getPokemons();
        for (CL_Pokemon p : toDelete){
            update.remove(p);
        }
        ar.setPokemons(update);
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
