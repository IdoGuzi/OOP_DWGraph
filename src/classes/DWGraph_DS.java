package classes;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    //We use HashMaps to store the nodes and the edges
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer,HashMap<Integer , edge_data>> edges;
    private HashMap<Integer,Set<Integer>> inEdges;
    private int edges_size,nodes_size,mc;

    public DWGraph_DS() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        inEdges = new HashMap<>();
        edges_size = 0;
        nodes_size = 0;
        mc = 0;
    }


    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method run in O(1) time.
     *
     * @param src - the key of the src of the edge
     * @param dest - the key of the dest of the edge
     * @return - edge connecting src and dest.
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!(nodes.containsKey(src) && edges.get(src).containsKey(dest) )){
            return null;
        }

        return edges.get(src).get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method run in O(1) time.
     *
     * @param n - the node to add to the graph
     */
    @Override
    public void addNode(node_data n) {
        if (nodes.containsKey(n.getKey())){
            return;
        }
        edges.put(n.getKey(),new HashMap<>());
        inEdges.put(n.getKey(),new HashSet<>());

        nodes.put(n.getKey() ,n);
        nodes_size++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(w<0){
            throw new IllegalArgumentException("ERROR: edges cant be negative weightes");
        }

        if(!(nodes.containsKey(src) && nodes.containsKey(dest))){
            return;
        }
        edge_data edge = getEdge(src,dest);
        if (edge!=null) return;
        edge = new EdgeDate(src,dest,w);
        edges.get(src).put(dest, edge);
        inEdges.get(dest).add(src);
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
     * Note: this method run in O(1) time.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        return edges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method run in O(k), V.degree=k, as all the edges is being removed (in and out edges).
     *
     * @param key - the key of the node to be removed
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if(!nodes.containsKey(key)){
            return null;
        }
        for (int neighbor : inEdges.get(key)){
            removeEdge(neighbor,key);
        }
        int size = edges.get(key).size();
        edges.remove(key);
        edges_size -= size;
        nodes_size--;
        return nodes.remove(key);
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
        if(!(nodes.containsKey(src) && nodes.containsKey(dest))) {
            return null;
        }
        edge_data e = edges.get(src).remove(dest);
        if (e==null) return null;
        edges.get(src).remove(dest);
        inEdges.get(dest).remove(src);
        edges_size--;
        return e;           //return the data of the removed edge
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return The amount of nodes in the graph
     */
    @Override
    public int nodeSize() {
        return nodes_size;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return The amount of edges in the graph
     */
    @Override
    public int edgeSize() {
        return edges_size;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return mode count (mc)
     */
    @Override
    public int getMC() {
        return mc;
    }



    @Override
    public boolean equals(Object o){
        if (o==null) return false;
        if (!(o instanceof directed_weighted_graph)) return false;
        directed_weighted_graph g = (directed_weighted_graph) o; // check if casting works
        if (nodeSize()!=g.nodeSize()) return false;
        if (edgeSize()!=g.edgeSize()) return false;
        Iterator<node_data> itr = getV().iterator();
        while (itr.hasNext()){
            node_data n = itr.next();
            if (g.getNode(n.getKey())==null) return false;
            Iterator<edge_data> itr2 = getE(n.getKey()).iterator();
            while (itr2.hasNext()){
                edge_data e  = itr2.next();
                node_data v = getNode(e.getDest());
                if (g.getNode(v.getKey())==null) return false;
                if (g.getEdge(n.getKey(),v.getKey())==null) return false;
                if (getEdge(n.getKey(),v.getKey())!=g.getEdge(n.getKey(),v.getKey())) return false;
            }
        }
        return true;
    }



    /**
     * This Class represent an Edge (in a graph), basically has 2 ints for source and destination nodes, and a weight.
     */
    private class EdgeDate implements edge_data{
        private int src,dest;
        private double weight;
        private String info;
        private int tag;


        public EdgeDate(int src, int dest, double w) {
            this.src = src;
            this.dest = dest;
            this.weight = w;
        }

        /**
         * The id of the source node of this edge.
         *
         * @return source node id
         */
        @Override
        public int getSrc() {
            return src;
        }

        /**
         * The id of the destination node of this edge
         *
         * @return destination node id
         */
        @Override
        public int getDest() {
            return dest;
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
         * @return information of the edge
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
         * @return temp data
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

}






























