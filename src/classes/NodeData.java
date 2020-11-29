package classes;
import api.geo_location;
import api.node_data;

public class NodeData implements node_data {
    private int key,tag;
    private geo_location location;
    private double weight;
    private String info;

    public NodeData(int key,double weight){   //default and copy constractors are not exist for now
        this.key = key;
        this.weight = weight;
        this.info = "";
        this.tag = -1;
        this.location = new GeoLocation();
    }



    public NodeData(int key){
        this.key = key;
        this.weight = 0;
        this.info = "";
        this.tag = -1;
        this.location = new GeoLocation();
    }



    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return this.location;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = p;
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
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
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    private class GeoLocation implements geo_location{
        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        private double x;
        private double y;
        private double z;

        public GeoLocation(double x , double y, double z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public GeoLocation(geo_location g){
            this.x = g.x();
            this.y = g.y();
            this.z = g.z();
        }

        public GeoLocation() {
            x=0;
            y=0;
            z=0;
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        @Override
        public double distance(geo_location g) {
            return Math.sqrt(   ((x-g.x())*(x-g.x())) + ((y-g.y())*(y-g.y())) + ((z-g.z())*(z-g.z()))   );
        }

        public String toString() { return "( "+x+","+y+","+z+" )"; }
    }
}





