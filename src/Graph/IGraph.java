package Graph;

public interface IGraph {
    public void addEdge(int fromNode, int toNode);
    public void deleteEdge(int fromNode, int toNode);
    public int shortestPathLength(int fromNode, int toNode);
}
