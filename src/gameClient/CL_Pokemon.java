package gameClient;

import api.edge_data;
import classes.DWGraph_DS;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

public class CL_Pokemon {
	private edge_data _edge;
	private int assignedAgent;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		assignedAgent=-1;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");
			//this._edge = new DWGraph_DS.EdgeDate()

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

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

	public int getAssignedAgent(){
		return assignedAgent;
	}

	public void setAssignedAgent(int assignedAgent) {
		this.assignedAgent = assignedAgent;
	}

	@Override
	public boolean equals(Object o){
		if (o==null) return false;
		if (!(o instanceof CL_Pokemon)) return false;
		CL_Pokemon p = (CL_Pokemon) o;
		if (this.get_edge().getSrc()!=p.get_edge().getSrc()) return false;
		if (this.get_edge().getDest()!=p.get_edge().getDest()) return false;
		if (this.get_edge().getWeight()-p.get_edge().getWeight()>0.0001) return false;
		if (!this.getLocation().equals(p.getLocation())) return false;
		if (this.getValue()-p.getValue()>0.0001) return false;
		if (this.getType()!=p.getType()) return false;
		return true;
	}

}
