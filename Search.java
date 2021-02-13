import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.ALTAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Search {
    static int test = 0;
    public static void main(String[] args) throws Exception {
        DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g;
        g = InitializeGraph();

/*

        // 0 to 5 D is faster:
        // 0,2 0,3 0,4
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            test = i;
            AStar("0", "10", g);
            long end = System.nanoTime();
            long time = (end - start);// / 1000000;
            System.out.println("A: " + time);
        }

        start = System.nanoTime();
        Dijkstra("0", "10", g);
        end = System.nanoTime();
        time = (end - start);// / 1000000;
        System.out.println("D: " + time);
*/


        long[][] DTimes = new long[100][100];
        long start = System.nanoTime();

        for (int i = 0; i < 100; i++)
            for (int j = i+1; j < 100; j++) {
                Dijkstra(Integer.toString(i), Integer.toString(j), g);
                //DTimes[i][j] = time;
            }
        long end = System.nanoTime();
        long time = (end - start) / 1000000;
        System.out.println("Dijkstra time: " + time + "ms");

        start = System.nanoTime();
        HashSet<String> set = new HashSet<String>();
        set.add("34");
        set.add("25");
        set.add("54");
        set.add("60");
        AStarShortestPath path;
        ALTAdmissibleHeuristic Hero = new ALTAdmissibleHeuristic(g, set);
        path = new AStarShortestPath(g, Hero);
        for (int i = 0; i < 100; i++)
            for (int j = i+1; j < 100; j++)
                AStar(Integer.toString(i), Integer.toString(j), g, Hero);
        end = System.nanoTime();
        time = (end - start) / 1000000;
        System.out.println("A* time: " + time + "ms");
    }
    public static void Dijkstra(String v1, String v2, DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g) {
        DijkstraShortestPath dijkstraShortestPath  = new DijkstraShortestPath(g);
        GraphPath shortestPath = dijkstraShortestPath.getPath(v1,v2);
        List<String> sToVPath   = shortestPath.getEdgeList();
        double       sToVWeight = shortestPath.getWeight();

        //System.out.println(sToVPath.toString() + "\nWeight: " + sToVWeight);
    }
    public static void AStar(String v1, String v2, DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g, ALTAdmissibleHeuristic Hero)
    {
        AStarShortestPath path;

        path = new AStarShortestPath(g, Hero);
        GraphPath shortestPath = path.getPath(v1, v2);
        List<String> sToVPath = shortestPath.getEdgeList();
    }
    public static DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> InitializeGraph() throws IOException
    {
        DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g =
                new DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        // TODO: Generalize the vertices added
        for (int i = 0; i < 100; i++)
        {
            g.addVertex(Integer.toString(i));
        }

        File file = new File("p1_graph.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;

        // Get to the point in the .txt file where the graph is defined
        // Count number of vertices to initialize matrix
        int count = 0;
        while ((line = in.readLine()) != null)
        {
            if (line.contains("# From, To, Distance")) break;
            if ((line.contains("#")) || (line.equals(""))) continue;
            count++;
        }

        // Translate the graph into an adjacency matrix
        while ((line = in.readLine()) != null)
        {
            if (line.equals("")) break;
            String[] tokens = line.split(",");

            DefaultWeightedEdge temp = g.addEdge(tokens[0], tokens[1]);
            g.setEdgeWeight(temp, Integer.parseInt(tokens[2]));
        }
        return g;
    }
}
