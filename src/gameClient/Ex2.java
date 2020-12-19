package gameClient;

import Server.Agent_Graph_Algo;
import Server.Game_Server_Ex2;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;
import classes.DWGraph_Algo;
import gameClient.util.Point3D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ex2 {
    public static void main(String[] args){
        long id;
        int level=0;
        Game g;
        System.out.println(args.length);
        System.out.println(args[0]);
        if (args.length==2) {
            id = Long.parseLong(args[0]);
            level=Integer.parseInt(args[1]);
            g = new Game(id ,level);
        }else{
            if (args.length<=1) {
                level = Integer.parseInt(args[0]);
                g = new Game(level);
            }else {
                throw new RuntimeException("ERROR: wrong input format");
            }
        }
        g.play();


    }

}
