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
                for (int j=0;j<ar.getPokemons().size();j++){
                    game.addAgent(ar.getPokemons().get(j).get_edge().getSrc());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.ar.setAgents(Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph()));
        this.win=new MyFrame("my game");
        this.win.update(ar);
        this.agent_path = new HashMap<>();
        for (CL_Agent a : ar.getAgents()){
            agent_path.put(a.getID(),new LinkedList<>());
        }
    }


    public void play(){
        game.startGame();
        //win.setVisible(true);
        while (game.isRunning()){
            System.out.println("running");
            planMove();
            for (int id : agent_path.keySet()){
                int node = agent_path.get(id).remove();
                game.chooseNextEdge(id,node);
                System.out.println(id+"  --> "+node);
            }
            win.repaint();
            game.move();

        }
        System.out.println(game.toString());
    }

    public void planMove(){
        setUpdatedAgents();
        setUpdatedPokemons();
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(ar.getGraph());
        for (CL_Pokemon p : ar.getPokemons()){
            int closest_agent_id=-1;
            CL_Agent ag = null;
            double min_dist=Double.MAX_VALUE;
            for (CL_Agent a : ar.getAgents()){
                double temp_dist;
                if (agent_path.get(a.getID()).isEmpty()){
                    temp_dist = ga.shortestPathDist(a.getSrcNode(), p.get_edge().getSrc());
                }else {
                    temp_dist = ga.shortestPathDist(agent_path.get(a.getID()).getLast(), p.get_edge().getSrc());
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
        }
    }

    public void placeAgents(){

    }

    public void setUpdatedAgents(){
        ar.setAgents(Agent_Graph_Algo.getAgents(game.getAgents(),ar.getGraph()));
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
}
