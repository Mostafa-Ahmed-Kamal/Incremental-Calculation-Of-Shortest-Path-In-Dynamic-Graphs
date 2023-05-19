package Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Graph_V1 implements IGraph{

    /**HashMap is used for simulation purposes, in a real-world scenario a distributed file system like hadoop would be used instead*/
    private final HashMap<Integer, HashSet<Integer>> graph;
    private final HashMap<Integer, HashSet<Integer>> reversedGraph;
    public Graph_V1(){
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
    }
    public Graph_V1(String initialGraph){
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
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
        graph.putIfAbsent(toNode,new HashSet<>());
        graph.get(fromNode).add(toNode);

        reversedGraph.putIfAbsent(fromNode,new HashSet<>());
        reversedGraph.putIfAbsent(toNode,new HashSet<>());
        reversedGraph.get(toNode).add(fromNode);
    }
    public void deleteEdge(int fromNode, int toNode){
        if (!graph.containsKey(fromNode) | !graph.containsKey(toNode))return;
        graph.get(fromNode).remove(toNode);
        reversedGraph.get(toNode).remove(fromNode);
    }
    /** NOTE: is BFS algorithm memory efficient for super large graphs or should we use something like iterative_DFS ? **/
    public int shortestPathLength(int fromNode, int toNode){
        if (fromNode==toNode)return 0;
        Queue<Integer> startQueue = new LinkedList<>();
        Queue<Integer> endQueue = new LinkedList<>();
        HashMap<Integer,Integer> startDistance = new HashMap<>();
        HashMap<Integer,Integer> endDistance = new HashMap<>();
        startQueue.add(fromNode);
        endQueue.add(toNode);
        int pathLength = 0;
        while(!startQueue.isEmpty() && !endQueue.isEmpty()){
            int startQueueSize = startQueue.size();
            int endQueueSize = endQueue.size();
            for (int i = 0; i < startQueueSize; i++) {
                int node = startQueue.poll();
                if (endDistance.containsKey(node))return pathLength+endDistance.get(node);
                if (startDistance.containsKey(node))continue;
                startDistance.put(node,pathLength);
                for (int neighbor:graph.getOrDefault(node,new HashSet<>())){
                    if (neighbor==toNode)return pathLength+1;
                    startQueue.add(neighbor);
                }
            }
            for (int i = 0; i < endQueueSize; i++) {
                int node = endQueue.poll();
                if (startDistance.containsKey(node))return pathLength+startDistance.get(node);
                if (endDistance.containsKey(node))continue;
                endDistance.put(node,pathLength);
                for (int neighbor:reversedGraph.getOrDefault(node,new HashSet<>())){
                    if (neighbor==fromNode)return pathLength+1;
                    endQueue.add(neighbor);
                }
            }
            pathLength++;
        }
        return -1;
    }
}
