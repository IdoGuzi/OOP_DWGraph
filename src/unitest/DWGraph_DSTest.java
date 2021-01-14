package unitest;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import classes.DWGraph_DS;
import classes.NodeData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {


    /**
     * This test checks connectivity (connect function) and the "add node" function
     * @param nodes_size
     * @return the graph created. we use it for an other tests function that is the reason to return any value at all
     */

    public static directed_weighted_graph graph_creator(int nodes_size) {
        directed_weighted_graph g = new DWGraph_DS();
        for(int i=0;i<nodes_size;i++) {           //adding nodes
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        for (int i=0; i <7; i++){     //adding edges
            g.connect(i, 2,2.5);   //connect the i'th source to '2' node --> 7 edges total
            g.connect(5, i,3.5);
        }


        return g;
    }

    public static directed_weighted_graph simple_graph_creator() {
        directed_weighted_graph g = new DWGraph_DS();
        node_data n = new NodeData(0);
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        g.addNode(n);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);

        return g;
    }

    @Test
    void test_equals(){
        directed_weighted_graph g1 = new DWGraph_DS();
        directed_weighted_graph g2 = new DWGraph_DS();
        for (int i=0;i<5;i++){
            g1.addNode(new NodeData(i));
            g2.addNode(new NodeData(i));
        }
        g1.connect(0,1,3.45);
        g2.connect(0,1,3.45);
        g1.connect(2,3,2.523);
        g2.connect(2,3,2.523);
        assertTrue(g1.equals(g2));
    }

    /**
     * We add and removes nodes and trying to remove the same node
     */
    @Test
    void nodeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        node_data n = new NodeData(0);
        node_data n1 = new NodeData(1);
        g.addNode(n);
        g.addNode(n1);
        g.addNode(n1);

        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        assertEquals(1,s);

    }




    @org.junit.jupiter.api.Test
    void getNode() {
        directed_weighted_graph gr = graph_creator(10);

        for(int i=0;i<10;i++) {
            node_data n = new NodeData(i);
            node_data check= gr.getNode(n.getKey());
            int x = n.getKey();
            int y = check.getKey();
            assertEquals(x,y);
        }
    }

    @Test
    void edgeSize() {
        directed_weighted_graph g = simple_graph_creator();


        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(1,3,1);
        int e_size =  g.edgeSize();
        assertEquals(4, e_size);
        double w03 = g.getEdge(0,3).getWeight();
        double w13 = g.getEdge(1,3).getWeight();

        assertEquals(w03, 3);
        assertEquals(w13, 1);
    }



    @Test
    void getV() {
        directed_weighted_graph g = new DWGraph_DS();

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        Collection<node_data> v = g.getV();
        Iterator<node_data> iter = v.iterator();
        while (iter.hasNext()) {
            node_data nd = iter.next();
            assertNotNull(nd);
        }
    }

    @Test
    void connect() {
        directed_weighted_graph g = new DWGraph_DS();
        g = graph_creator(10);
        g.removeEdge(0,1);

        g.connect(0,1,1);
        double w = g.getEdge(0,1).getWeight();
        assertEquals(w,1);
    }

    @org.junit.jupiter.api.Test
    void getE() {
        directed_weighted_graph g = simple_graph_creator();
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        Collection<edge_data> x = g.getE(0);
        assertEquals(x.size(),3);

    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        directed_weighted_graph g = simple_graph_creator();
        g.removeNode(0);
        g.removeNode(1);
        Collection<node_data> left_nodes = g.getV();
        assertEquals(left_nodes.size(),2);
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        directed_weighted_graph g = simple_graph_creator();
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.removeEdge(0,2);
        int x = g.edgeSize();
        assertEquals(x,1);

    }





}