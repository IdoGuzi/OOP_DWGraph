package classes;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;

import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph graph;
    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        graph=g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * Note: can't be done without iterating over the node twice due to exception of destination isn't added to the graph yet.
     * @return - directed_weighted_graph perfect deep copy of the graph of this object.
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g = new DWGraph_DS();
        Iterator<node_data> itr = graph.getV().iterator();
        while (itr.hasNext()){
            node_data n = itr.next();
            node_data v = new NodeData(n.getKey());
            v.setInfo(n.getInfo());
            v.setTag(n.getTag());
            g.addNode(v);
        }
        itr = graph.getV().iterator();
        while (itr.hasNext()){
            Iterator<edge_data> itr2 = graph.getE(itr.next().getKey()).iterator();
            while (itr2.hasNext()){
                edge_data e = itr2.next();
                g.connect(e.getSrc(),e.getDest(),e.getWeight());
            }
        }
        return g;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Dijksta(src,dest);
        double dist = graph.getNode(dest).getWeight();
        if (dist==Double.MAX_VALUE) return -1;
        return dist;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * this method perform the Dijksta algorithm to determine the shortest path.
     * creating the path by following the parent map return by dijksta.
     * @param src  - start node
     * @param dest - end (target) node
     * @return a list of node representing the shortest path, null if none.
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        //dijkstaoutput is the parent map of every node detected in the dijksta algo
        Map<Integer,Integer> dijkstaOutput = Dijksta(src,dest);
        //will happen if dijksta not found dest (not in the same component as src)
        if (dijkstaOutput.get(dest)==null) return null;
        List<node_data> path = new LinkedList<>();
        node_data temp = graph.getNode(dest);
        path.add(0,temp);
        //dijkstaoutput is map in a way that src is mapped to src
        while(dijkstaOutput.get(temp.getKey())!=temp.getKey()){
            temp = graph.getNode(dijkstaOutput.get(temp.getKey()));
            path.add(0,temp);
        }
        return path;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            // Writing to a file
            FileWriter fw = new FileWriter(file);
            fw.write(toJsonGraph());
            fw.flush();
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            String jsonString = scanner.useDelimiter("\\A").next();
            JSONObject jp = new JSONObject(jsonString);
            fromJsonGraph(jp);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String toJsonGraph() throws JSONException {
        JSONObject jo = new JSONObject();
        JSONArray no = new JSONArray();
        JSONArray eo = new JSONArray();
        Iterator<node_data> itr = graph.getV().iterator();
        while (itr.hasNext()){
            node_data n = itr.next();
            no.put(toJsonNode(n));
            Iterator<edge_data> itr2 = graph.getE(n.getKey()).iterator();
            while (itr2.hasNext()){
                edge_data e = itr2.next();
                eo.put(toJsonEdge(e));
            }
        }
        jo.put("Nodes",no);
        jo.put("Edges",eo);
        return jo.toString();
    }


    private void fromJsonGraph(JSONObject g) throws JSONException {
        this.graph = new DWGraph_DS();
        JSONArray ja = g.getJSONArray("Nodes");
        int size = ja.length();
        for (int i=0;i<size;i++){
            fromJsonNode(ja.getJSONObject(i));
        }
        ja = g.getJSONArray("Edges");
        size = ja.length();
        for (int i=0;i<size;i++){
            fromJsonEdge(ja.getJSONObject(i));
        }
    }

    private JSONObject toJsonEdge(edge_data edge) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("src",edge.getSrc());
        jo.put("dest",edge.getDest());
        jo.put("w",edge.getWeight());
        return jo;
    }

    private void fromJsonEdge(JSONObject edge) throws JSONException {
        int src=edge.getInt("src");
        int dest=edge.getInt("dest");
        double w = edge.getDouble("w");
        graph.connect(src,dest,w);
    }

    private JSONObject toJsonNode(node_data n) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id",n.getKey());
        jo.put("pos",n.getLocation().toString());
        return jo;
    }

    private void fromJsonNode(JSONObject node) throws JSONException {
        int key = node.getInt("id");
        graph.addNode(new NodeData(key));
        graph.getNode(key).setLocation(new Point3D(node.getString("pos")));
    }

    /**
     * reset the data of all the nodes in the graph.
     * used before performing algorithms.
     */
    private void clearTags(){
        Iterator<node_data> itr = graph.getV().iterator();
        while (itr.hasNext()) {
            node_data n = itr.next();
            n.setTag(0);
            n.setInfo("");
            n.setWeight(Double.MAX_VALUE);
        }
    }

    /**
     * This method perform the Dijksta algorithm to find shortest path between two nodes in a graph.
     * the method runtime is 0(|V|+|E|) (Theta of the size of the vertex + the size of the edges in the graph).
     *
     * @param src the starting vertex of the path.
     * @param dest - the ending vertex of the path.
     * @return a map representing the vertex who got us to the current vertex (key=current, value=previous)
     */
    private Map<Integer,Integer> Dijksta(int src, int dest){
        clearTags();
        Map<Integer,Integer> prev = new HashMap<>();
        PriorityQueue<node_data> queue = new PriorityQueue<>(6,new NodeComperator());
        queue.add(graph.getNode(src));
        graph.getNode(src).setTag(1);
        prev.put(src,src);
        while (!queue.isEmpty()){
            node_data n = queue.poll();
            Iterator<edge_data> itr = graph.getE(n.getKey()).iterator();
            while (itr.hasNext()){
                edge_data e = itr.next();
                node_data v = graph.getNode(e.getDest());
                if (v.getWeight()>n.getWeight()+e.getWeight()){
                    v.setWeight(n.getWeight()+e.getWeight());
                    prev.put(v.getKey(),n.getKey());
                    queue.add(v);
                    v.setTag(1);
                }
            }
            n.setTag(2);
        }
        return prev;

    }


    /**
     * This class is a private helper class for the dijksta algorithm.
     * main use of this class is a comparator for the priority queue.
     */
    private class NodeComperator implements Comparator<node_data> {
        private final Double EPS = 0.00001;

        @Override
        public int compare(node_data o1, node_data o2) {
            if (o1.getWeight()-o2.getWeight()<-EPS) return -1;
            if (o1.getWeight()-o2.getWeight()<EPS) return 0;
            if (o1.getWeight()-o2.getWeight()>EPS) return 1;
            throw new RuntimeException("Error: no condition met");
        }
    }
}
