package gameClient;

import api.edge_data;
import classes.NodeData;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class Pokemon {

    private NodeData.GeoLocation pos;
    private edge_data edge;
    private double value;
    private int type;
    private double min_dist;
    private int min_ro;

    public Pokemon(NodeData.GeoLocation p, int t, double v, double s, edge_data e) {
        type = t;
        value = v;
        set_edge(e);
        pos = p;
        min_dist = -1;
        min_ro = -1;
    }

    //check with Ido
    public static Pokemon init_from_json(String json) {
        Pokemon ans = null;
        try {
            JSONObject p = new JSONObject(json);
            int id = p.getInt("id");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public String toString() {return "F:{v="+value+", t="+type+"}";}

    public edge_data get_edge() {
        return edge;
    }

    public void set_edge(edge_data edge) {
        this.edge = edge;
    }

    public NodeData.GeoLocation getLocation() {
        return pos;
    }

    public int getType() {return type;}

    public double getValue() {return value;}

    public double getMin_dist() {
        return min_dist;
    }

    public void setMin_dist(double mid_dist) {
        this.min_dist = mid_dist;
    }

    public int getMin_ro() {
        return min_ro;
    }

    public void setMin_ro(int min_ro) {
        this.min_ro = min_ro;
    }
}
