import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class BatchProcessor extends UnicastRemoteObject implements IBatchProcessor {
    Graph_V0 graph;
    public BatchProcessor() throws RemoteException {
        super();
    }
    public void initializeGraph(String initialGraph){
        graph = new Graph_V0(initialGraph);
    }
    @Override
    public String processBatch(String batch){
        if (graph==null)initializeGraph("");
        String[] requests = batch.split("\n");
        StringBuilder queryResult = new StringBuilder();
        for (String request:requests){
            if (request.equals("F"))break;
            Request request1 = new Request(request);
            switch (request1.requestType.toLowerCase()) {
                case "q" -> queryResult.append(graph.shortestPathLength(request1.fromNode, request1.toNode)).append("\n");
                case "a" -> graph.addEdge(request1.fromNode, request1.toNode);
                case "d" -> graph.deleteEdge(request1.fromNode, request1.toNode);
            }
        }
        return queryResult.toString();
    }
}
