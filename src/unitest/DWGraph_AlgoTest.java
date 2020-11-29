package unitest;

import classes.DWGraph_Algo;
import classes.DWGraph_DS;
import classes.NodeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import org.junit.jupiter.api.Timeout;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Test class for the DWGraph_Algo class.
 */
class DWGraph_AlgoTest {
    Random rand = new Random();

    /**
     * test function for the init method.
     * assumption: graph_factory, getGraph working.
     */
    @Test
    @Timeout(value = 1000,unit = TimeUnit.MILLISECONDS)
    void init() {
        int ran = random(35,50);
        int num = random(50,100);
        for (int i=0;i<ran;i++) {
            dw_graph_algorithms ga = new DWGraph_Algo();
            directed_weighted_graph diff = graph_factory(num,num);
            directed_weighted_graph old = ga.getGraph();
            ga.init(diff);
            assertNotEquals(true, diff == old);
        }
    }

    /**
     * test function for the getGraph method
     * assumption: graph_factory, init working.
     */
    @Test
    void getGraph() {
        int ran = random(35,50);
        int num = random(50,100);
        for (int i=0;i<ran;i++) {
            dw_graph_algorithms ga = new DWGraph_Algo();
            directed_weighted_graph g = graph_factory(num,num);
            ga.init(g);
            directed_weighted_graph check = ga.getGraph();
            assertEquals(true, g==check);
        }
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    private dw_graph_algorithms factory(int nodes, int edges){
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(graph_factory(nodes,edges));
        return ga;
    }

    private directed_weighted_graph graph_factory(int nodes, int edges){
        directed_weighted_graph g = new DWGraph_DS();
        for (int i=0;i<nodes;i++){
            g.addNode(new NodeData()); //to be continued;
        }
        while (g.edgeSize()<edges){
            int a = random(0,nodes);
            int b = random(0,nodes);
            double w = rand.nextDouble()*10;
            g.connect(a,b,w);
        }
        return g;
    }

    private directed_weighted_graph graph_factory(int graph){
        directed_weighted_graph g = new DWGraph_DS();
        if (graph==1){
            g.addNode(new NodeData(0));
            g.addNode(new NodeData(1));
            g.addNode(new NodeData(2));
            g.addNode(new NodeData(3));
            g.addNode(new NodeData(4));
            g.connect(0,1,3.1);
            g.connect(3,1,2.1);
            g.connect(4,0,1.74);
            g.connect(0,2,0.45);
            g.connect(2,3,2.73);
            return g;
        }
        if (graph==2){
            g.addNode(new NodeData(0));
            g.addNode(new NodeData(1));
            g.addNode(new NodeData(2));
            g.addNode(new NodeData(3));
            g.addNode(new NodeData(4));
            g.addNode(new NodeData(5));
            g.addNode(new NodeData(6));
            g.addNode(new NodeData(7));
            g.connect(0,1,1.2);
            g.connect(1,0,3.5);
            g.connect(2,1,5.6);
            g.connect(7,1,4.64);
            g.connect(6,4,2.73);
            return g;
        }
        return g;
    }


    private int random(int min, int max){
        return rand.nextInt(max-min)+min;
    }
}