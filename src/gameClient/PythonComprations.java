package gameClient;

import api.dw_graph_algorithms;
import api.node_data;
import classes.DWGraph_Algo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PythonComprations {

    public static void main(String[] args) throws JSONException {
        dw_graph_algorithms ga = loading(0);
        JSONObject jo = new JSONObject();
        jo.put("shortest path", shortest_path_rumtime(ga));


    }

    public static dw_graph_algorithms loading(int number){
        DWGraph_Algo ga = new DWGraph_Algo();
        if (number==0){
            ga.load("data/G_10_80_0.json");
        }else if (number==1){
            ga.load("data/G_100_800_0.json");
        }else if (number==2){
            ga.load("data/G_1000_8000_0.json");
        }else if (number==3){
            ga.load("data/G_10000_80000_0.json");
        }else if (number==4){
            ga.load("data/G_20000_160000_0.json");
        }else if (number==5){
            ga.load("data/G_30000_240000_0.json");
        }
        return ga;
    }

    public static JSONArray shortest_path_rumtime(dw_graph_algorithms ga) throws JSONException {
        JSONArray shortest = new JSONArray();
        for (node_data n : ga.getGraph().getV()) {
            for (node_data v : ga.getGraph().getV()) {
                if (n.getKey()==v.getKey()) continue;
                long startTime = System.nanoTime();
                ga.shortestPath(n.getKey(),v.getKey());
                long endTime = System.nanoTime();
                long totalTime = endTime - startTime;
                timer t = new timer();
                t.setRunTime(totalTime);
                t.setSrc(n.getKey());
                t.setDest(v.getKey());
                shortest.put(t.toJson());
            }
        }
        return shortest;
    }

    private static class timer{
        private long runTime;
        private int src,dest;
        private String func;

        public timer(){
            runTime=-1;
            src=-1;
            dest=-1;
            func="";
        }

        public JSONObject toJson() throws JSONException {
            JSONObject jo = new JSONObject();
            jo.put("run time",runTime);
            if (src!=-1) {
                jo.put("src", src);
            }
            if (dest!=-1) {
                jo.put("dest",dest);
            }
            jo.put("function", func);
            return jo;
        }

        public int getSrc() {
            return src;
        }

        public int getDest() {
            return dest;
        }

        public long getRunTime() {
            return runTime;
        }

        public String getFunc() {
            return func;
        }

        public void setDest(int dest) {
            this.dest = dest;
        }

        public void setFunc(String func) {
            this.func = func;
        }

        public void setRunTime(long runTime) {
            this.runTime = runTime;
        }

        public void setSrc(int src) {
            this.src = src;
        }
    }
}
