import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;


public class GraphView {
    static Graph<String, String> g;
    public GraphView() {
        g = new SparseMultigraph<String, String>();
    }
    
    public static void addVertex(String name){
    	g.addVertex(name);
    }
    
    public static void addEdge(String name, String s, String d){
    	g.addEdge(name, s, d);
    }
    
}