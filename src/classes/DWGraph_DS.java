package classes;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer,HashMap<Integer , edge_data>> edges;
    private int edges_size,nodes_size,mc;

    public DWGraph_DS() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }


    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if(nodes.containsKey(key))
            return nodes.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {                      // go back to it
        if(!(edges.containsKey(src) && edges.containsValue(dest))){
            return null;
        }

        return edges.get(src).get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (nodes.containsKey(n.getKey())){
            return;
        }
        edges.put(n.getKey(),new HashMap<>());

        nodes.put(n.getKey() ,n);        // nodes.put(n.getKey() , new NodeData( n.getKey() , n.getWeight()));   --Ido Recommends
        nodes_size++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {

        if(!(nodes.containsKey(src) && nodes.containsKey(dest)) && w<0){
            return;
        }
        node_data n1 = nodes.get(src);
        node_data n2 = nodes.get(dest);
        edge_data edge = new EdgeDate(n1,n2,w);
        edges.get(src).put(dest, edge);      //check it
        edges_size++;

    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        HashMap<Integer , edge_data> edgesRet = new HashMap<>();
        for (Integer i : edges.get(node_id).keySet()){
            edgesRet.put(i,getEdge(node_id,i));
        }
        return edgesRet.values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if(!nodes.containsKey(key)){
            return null;

        }
        Set<Integer> keySet = nodes.keySet();
        for (int k: keySet) {
            removeEdge(k,key);
        }
        edges.remove(key);
        node_data n = nodes.remove(key);

        nodes_size--;
        return n;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        boolean b = false;
        if(nodes.containsKey(src) && nodes.containsKey(dest)) {
            b =   edges.get(src).containsKey(dest);
        }
        if (b == true) {

            edge_data e = edges.get(src).remove(dest); // ???
            edges.get(src).remove(dest);            //???
            edges_size--;
            return e;           //return the data of the removed edge
        }
        return null;
        }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return nodes_size;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edges_size;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }
}







    class EdgeDate implements edge_data{
        private node_data src,dest;
        private double weight;
        private String info;
        private int tag;

        public EdgeDate (node_data src , node_data dest){
            this.src = src;
            this.dest = dest;
            this.weight = src.getLocation().distance(dest.getLocation());          //??? test it
         }

        public EdgeDate(node_data src, node_data dest, double w) {
            this.src = src;
            this.dest = dest;
            this.weight = w;
        }

        /**
         * The id of the source node of this edge.
         *
         * @return
         */
        @Override
        public int getSrc() {
            return src.getKey();
        }

        /**
         * The id of the destination node of this edge
         *
         * @return
         */
        @Override
        public int getDest() {
            return dest.getKey();
        }

        /**
         * @return the weight of this edge (positive value).
         */
        @Override
        public double getWeight() {
            return this.weight;
        }

        /**
         * Returns the remark (meta data) associated with this edge.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * Allows changing the remark (meta data) associated with this edge.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return this.tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking an edge - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this.tag = t;
        }
}


























class EdgeData implements edge_data{

    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    @Override
    public int getSrc() {
        return 0;
    }

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {
        return 0;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return 0;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return null;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {

    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return 0;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {

    }
}