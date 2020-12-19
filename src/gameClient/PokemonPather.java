package gameClient;

import api.dw_graph_algorithms;
import classes.DWGraph_Algo;

import java.util.HashMap;
import java.util.List;

public class PokemonPather implements Runnable{

    @Override
    public void run() {

    }

    private void path(Arena a, CL_Pokemon p, List<CL_Agent> agents, HashMap<Integer,List<Integer>> agentPaths){
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(a.getGraph());
        double min_agent_= Double.MAX_VALUE;
        for (CL_Agent ag : agents){
            int goOutNode = -1, goInNode=-1;
            double min_path=Double.MAX_VALUE;
            for (int i : agentPaths.get(ag.getID())){
                double min_dist_inside = Double.MAX_VALUE;
                for (int j=i+1;j<agentPaths.get(ag.getID()).size();j++) {
                    double temp = ga.shortestPathDist(i, p.get_edge().getSrc()) + p.get_edge().getWeight() + ga.shortestPathDist(p.get_edge().getDest(), j);
                }
            }
        }
    }
}
