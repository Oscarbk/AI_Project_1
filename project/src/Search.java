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
import java.nio.file.Path;
import java.util.*;

public class Search {
    static int verticies = 0;
    public static void main(String[] args) throws Exception {
        // Initialize graph for search
        DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g =
                new DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        // Store informed search information
        Hashtable<String,String> table = new Hashtable<String, String>();
        InitializeGraph(g, table);

        // Set landmarks for A*
        ALTAdmissibleHeuristic Hero = setLandmarks(table, g);

        // Get input from user
        Scanner input = new Scanner(System.in);
        String userInput = "";
        while (!userInput.equals("E"))
        {
            System.out.println("Enter '1' to compare shortest path algorithms between two user-input vertices.");
            System.out.println("Enter '2' to compare shortest path algorithms between all vertices.");
            System.out.println("Enter 'E' to exit");
            userInput = input.nextLine();
            switch(userInput)
            {
                case "E":
                    System.exit(0);
                    break;
                case "1":
                    System.out.println("Enter vertex one in the range: [0," + verticies + ")");
                    String v1 = input.nextLine();
                    System.out.println("Enter vertex two in the range: [0," + verticies + ")");
                    String v2 = input.nextLine();
                    if ((Integer.parseInt(v1) < 0) || (Integer.parseInt(v1) >= verticies)
                            || (Integer.parseInt(v2) < 0) || (Integer.parseInt(v2) >= verticies))
                    {
                        System.out.println("Detected vertex out of range.");
                        break;
                    }

                    long start = System.nanoTime();
                    PathAndWeight path = Dijkstra(v1, v2, g);
                    long end = System.nanoTime();
                    long time = (end - start);
                    System.out.println("Dijkstra:\ntime: \t" + time + "ns" + "\nPath found: " + path.path + "\nWeight: " + path.weight + "\n");

                    start = System.nanoTime();
                    path = AStar(v1, v2, g, Hero);
                    end = System.nanoTime();
                    time = (end - start);
                    System.out.println("A*:\ntime: \t" + time + "ns" + "\nPath found: " + path.path + "\nWeight: " + path.weight + "\n");
                    break;
                case "2":
                    timeAllPairs(g, table, Hero);
                default:
                    System.out.println("Enter a valid character: 'E' '1' or '2'");
            }
        }
    }
    public static ALTAdmissibleHeuristic setLandmarks(Hashtable<String, String> table, DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g)
    {
        HashSet<String> landmarks = new HashSet<String>();
        int count = 0;
        // Get top right landmark
        for (int i = 0; i < 5; i++)
        {
            if (table.get(Integer.toString(i)) != null)
            {
                landmarks.add(table.get(Integer.toString(i)));
                count++;
                break;
            }
        }
        // Get bottom left landmark
        for (int i = 90; i >= 50; i-=10)
        {
            if (table.get(Integer.toString(i)) != null)
            {
                landmarks.add(table.get(Integer.toString(i)));
                count++;
                break;
            }
        }
        // Get top right landmark
        for (int i = 9; i >= 5; i--)
        {
            if (table.get(Integer.toString(i)) != null)
            {
                landmarks.add(table.get(Integer.toString(i)));
                count++;
                break;
            }
        }
        // Get bottom right landmark
        for (int i = 99; i >= 59; i-=10)
        {
            if (table.get(Integer.toString(i)) != null)
            {
                landmarks.add(table.get(Integer.toString(i)));
                count++;
                break;
            }
        }
        // Choose random landmarks if we haven't chosen four close to each corner yet
        Random rand = new Random();
        while (count < 4)
        {
            int i = rand.nextInt(100);
            if (!landmarks.contains(Integer.toString(i)))
            {
                landmarks.add(Integer.toString(i));
                count++;
            }
        }
        return new ALTAdmissibleHeuristic(g, landmarks);
    }
    public static void timeAllPairs(DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g, Hashtable<String, String> table, ALTAdmissibleHeuristic Hero)
    {
        // Time how long it takes for Dijkstra to find all shortest paths
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++)
            for (int j = i+1; j < 100; j++)
                Dijkstra(Integer.toString(i), Integer.toString(j), g);
        long end = System.nanoTime();
        long time = (end - start) / 1000000;
        System.out.println("Dijkstra time: " + time + "ms");

        // Time how long it takes for A* to find all shortest paths
        start = System.nanoTime();
        AStarShortestPath path = new AStarShortestPath(g, Hero);
        for (int i = 0; i < 100; i++)
            for (int j = i+1; j < 100; j++)
                AStar(Integer.toString(i), Integer.toString(j), g, Hero);
        end = System.nanoTime();
        time = (end - start) / 1000000;
        System.out.println("A* time: " + time + "ms");
    }
    public static PathAndWeight Dijkstra(String v1, String v2, DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g)
    {
        DijkstraShortestPath dijkstraShortestPath  = new DijkstraShortestPath(g);
        GraphPath shortestPath = dijkstraShortestPath.getPath(v1,v2);
        List<String> sToVPath   = shortestPath.getEdgeList();
        double       sToVWeight = shortestPath.getWeight();

        return new PathAndWeight((int)sToVWeight, sToVPath);
    }
    public static PathAndWeight AStar(String v1, String v2, DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g, ALTAdmissibleHeuristic Hero)
    {
        AStarShortestPath path;

        path = new AStarShortestPath(g, Hero);
        GraphPath shortestPath = path.getPath(v1, v2);
        List<String> sToVPath = shortestPath.getEdgeList();
        double sToVWeight = shortestPath.getWeight();

        return new PathAndWeight((int)sToVWeight, sToVPath);
    }
    public static void InitializeGraph(DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> g, Hashtable<String, String> table) throws IOException
    {
        File file = new File("p1_graph.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));

        String line;

        // Get to the point in the .txt file where vertices are defined by square
        while ((line = in.readLine()) != null)
        {
            if (line.contains("# Vertex ID, Square ID")) break;
            if ((line.contains("#")) || (line.equals(""))) continue;
        }

        // Add vertices to the graph and create hashset of vertex ID and Square ID
        while ((line = in.readLine()) != null)
        {
            if (line.equals("")) break;
            String[] tokens = line.split(",");

            g.addVertex(tokens[0]);
            table.put(tokens[1], tokens[0]);
            verticies++;
        }

        // Get to the point in the .txt file where the graph is defined
        while ((line = in.readLine()) != null)
        {
            if (line.contains("# From, To, Distance")) break;
            if ((line.contains("#")) || (line.equals(""))) continue;
        }

        // Define each edge in the graph
        while ((line = in.readLine()) != null)
        {
            if (line.equals("")) break;
            String[] tokens = line.split(",");

            DefaultWeightedEdge temp = g.addEdge(tokens[0], tokens[1]);
            g.setEdgeWeight(temp, Integer.parseInt(tokens[2]));
        }
    }
}
// Class to store a shortest path and its weight
class PathAndWeight {
    int weight;
    List<String> path;
    public PathAndWeight(int weight, List<String> path)
    {
        this.weight = weight;
        this.path = path;
    }
}