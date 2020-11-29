package unitest;

import api.node_data;
import classes.DWGraph_Algo;
import classes.DWGraph_DS;
import classes.NodeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Test class for the DWGraph_Algo class.
 */
class DWGraph_AlgoTest {
    private static final double EPSILON = 0.0001;
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
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(graph_factory(1));
        assertEquals(true,Math.abs(ga.shortestPathDist(4,1)-4.56)<EPSILON);
        assertEquals(true,Math.abs(ga.shortestPathDist(0,4)-(-1))<EPSILON);
        ga.init(graph_factory(2));
        assertEquals(true,Math.abs(ga.shortestPathDist(3,4)-7.7)<EPSILON);
        ga.init(graph_factory(3));
        assertEquals(true,Math.abs(ga.shortestPathDist(0,4)-3.7)<EPSILON);
        assertEquals(true,Math.abs(ga.shortestPathDist(1,4)-6.1)<EPSILON);
    }


    @Test
    void shortestPath() {
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(graph_factory(1));
        List<node_data> path = ga.shortestPath(4,1);
        assertEquals(4,path.get(0).getKey());
        assertEquals(0,path.get(1).getKey());
        assertEquals(2,path.get(2).getKey());
        assertEquals(3,path.get(3).getKey());
        assertEquals(1,path.get(4).getKey());
        path = ga.shortestPath(0,4);
        assertNull(path);
        ga.init(graph_factory(2));
        path = ga.shortestPath(3,4);
        assertEquals(3,path.get(0).getKey());
        assertEquals(7,path.get(1).getKey());
        assertEquals(2,path.get(2).getKey());
        assertEquals(0,path.get(3).getKey());
        assertEquals(5,path.get(4).getKey());
        assertEquals(4,path.get(5).getKey());
        ga.init(graph_factory(3));
        path = ga.shortestPath(0,4);
        assertEquals(0,path.get(0).getKey());
        assertEquals(2,path.get(1).getKey());
        assertEquals(4,path.get(2).getKey());
        path = ga.shortestPath(1,4);
        assertEquals(1,path.get(0).getKey());
        assertEquals(3,path.get(1).getKey());
        assertEquals(2,path.get(2).getKey());
        assertEquals(4,path.get(3).getKey());
    }


    /**
     * Test function for the save, load methods.
     * assumptions: factory working.
     */
    @Test
    void save_load() {
        int ran = random(30,50);
        for (int i=0;i<ran;i++){
            dw_graph_algorithms ga = factory(ran,ran*2);
            ga.save("graph");
            directed_weighted_graph g = ga.getGraph();
            ga.load("graph");
            assertEquals(g,ga.getGraph());
        }
    }

    private dw_graph_algorithms factory(int nodes, int edges){
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(graph_factory(nodes,edges));
        return ga;
    }

    private directed_weighted_graph graph_factory(int nodes, int edges){
        directed_weighted_graph g = new DWGraph_DS();
        for (int i=0;i<nodes;i++){
            g.addNode(new NodeData(i)); //to be continued;
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
            /*
            facts list:
            no strongly connected
            path(4,1): 4 -> 0 -> 2 -> 3 -> 1 : dist=1.74+0.45+1.13+1.24=4.56
            path(0,4): null : dist -1
             */
            g.addNode(new NodeData(0));
            g.addNode(new NodeData(1));
            g.addNode(new NodeData(2));
            g.addNode(new NodeData(3));
            g.addNode(new NodeData(4));
            g.connect(0,1,3.1);
            g.connect(3,1,1.24);
            g.connect(4,0,1.74);
            g.connect(0,2,0.45);
            g.connect(2,3,1.13);
            return g;
        }
        if (graph==2){
            /*
            facts list:
            strongly connected
            path(3,4): 3 -> 7 -> 2 -> 0 -> 5 -> 4 : dist = 0.4 + 1.7 + 1.8 + 0.2 + 3.6 = 7.7
             */
            g.addNode(new NodeData(0));
            g.addNode(new NodeData(1));
            g.addNode(new NodeData(2));
            g.addNode(new NodeData(3));
            g.addNode(new NodeData(4));
            g.addNode(new NodeData(5));
            g.addNode(new NodeData(6));
            g.addNode(new NodeData(7));
            g.connect(0,1,0.73);
            g.connect(0,5,0.2);
            g.connect(1,3,1.12);
            g.connect(2,0,1.8);
            g.connect(3,7,0.4);
            g.connect(4,0,1.6);
            g.connect(5,4,3.6);
            g.connect(6,4,2.7);
            g.connect(7,2,1.7);
            g.connect(7,6,5.4);
            return g;
        }
        if (graph==3){
            /*
            facts list
            strongly connected
            path(0,4): 0 -> 2 -> 4 : dist = 2.5 + 1.2 = 3.7
            path(1,4): 1 -> 3 -> 2 -> 4 : dist = 1.7 + 3.2 + 1.2 = 6.1
             */
            g.addNode(new NodeData(0));
            g.addNode(new NodeData(1));
            g.addNode(new NodeData(2));
            g.addNode(new NodeData(3));
            g.addNode(new NodeData(4));
            g.connect(0,1,3.7);
            g.connect(0,2,2.5);
            g.connect(0,3,4.9);
            g.connect(1,3,1.7);
            g.connect(1,4,6.3);
            g.connect(2,0,2.1);
            g.connect(2,4,1.2);
            g.connect(3,2,3.2);
            g.connect(4,3,2.3);
        }
        return g;
    }


    private int random(int min, int max){
        return rand.nextInt(max-min)+min;
    }
}