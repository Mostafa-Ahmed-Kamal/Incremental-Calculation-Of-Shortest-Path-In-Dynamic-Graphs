import java.util.*;

public class Graph_V0 {
    private final HashMap<Integer, HashSet<Integer>> graph;
    public Graph_V0(){
        graph = new HashMap<>();
    }
    public Graph_V0(String initialGraph){
        graph = new HashMap<>();
        String[] edges = initialGraph.split("\n");
        for (String edge:edges){
            if (edge.equals("S"))return;
            String[] nodes = edge.split("\s+");
            if(nodes.length!=2){
                System.out.println("invalid input");
                break;
            }
            addEdge(Integer.parseInt(nodes[0]),Integer.parseInt(nodes[1]));
        }
    }
    public void addEdge(int fromNode, int toNode){
        graph.putIfAbsent(fromNode,new HashSet<>());
        graph.putIfAbsent(fromNode,new HashSet<>());
        graph.get(fromNode).add(toNode);
    }
    public void deleteEdge(int fromNode, int toNode){
        if (!graph.containsKey(fromNode))return;
        graph.get(fromNode).remove(toNode);
    }
    /** NOTE: is BFS algorithm memory efficient for super large graphs or should we use something like iterative_DFS ? **/
    public int shortestPathLength(int fromNode, int toNode){
        if (fromNode==toNode)return 0;
        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(fromNode);
        int pathLength = 1;
        while(!queue.isEmpty()){
            int queueSize = queue.size();
            for (int i = 0; i < queueSize; i++) {
                int node = queue.poll();
                if (visited.contains(node))continue;
                visited.add(node);
                for (int neighbor:graph.getOrDefault(node,new HashSet<>())){
                    if (neighbor==toNode)return pathLength;
                    queue.add(neighbor);
                }
            }
            pathLength++;
        }
        return -1;
    }
}
