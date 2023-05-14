package BatchProcessor;

import Graph.Graph_V0;

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
    public synchronized String processBatch(String batch){
        if (graph==null)initializeGraph("");
        String[] requests = batch.split("\n");
        StringBuilder queryResult = new StringBuilder();
        for (String request:requests){
            if (request.equals("F"))break;
            Request requestObject = new Request(request);
            switch (requestObject.getRequestType().toLowerCase()) {
                case "q" -> queryResult.append(graph.shortestPathLength(requestObject.getFromNode(), requestObject.getToNode())).append("\n");
                case "a" -> graph.addEdge(requestObject.getFromNode(), requestObject.getToNode());
                case "d" -> graph.deleteEdge(requestObject.getFromNode(), requestObject.getToNode());
            }
        }
        return queryResult.toString();
    }
}