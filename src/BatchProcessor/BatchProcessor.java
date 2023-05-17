package BatchProcessor;

import Graph.Graph_V0;
import Graph.Graph_V1;
import Utils.FileManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class BatchProcessor extends UnicastRemoteObject implements IBatchProcessor {
    Graph_V1 graph;
    String workingDirectory = "logs";
    public BatchProcessor() throws RemoteException {
        super();
    }
    public void setWorkingDirectory(String workingDirectory){
        this.workingDirectory = workingDirectory;
    }
    public void initializeGraph(String initialGraph){
        graph = new Graph_V1(initialGraph);
    }
    @Override
    public synchronized String processBatch(String batch){
        if (graph==null)initializeGraph("");
        String[] requests = batch.split("\n");
        StringBuilder queryResult = new StringBuilder();
        StringBuilder batchLog = new StringBuilder();
        for (String request:requests){
            if (request.equals("F"))break;
            batchLog.append(">> ").append(request).append(" --> ");
            Request requestObject = new Request(request);
            switch (requestObject.getRequestType().toLowerCase()) {
                case "q" -> {
                    int shortestPath = graph.shortestPathLength(requestObject.getFromNode(), requestObject.getToNode());
                    queryResult.append(shortestPath).append("\n");
                    batchLog.append(shortestPath).append("\n");
                }
                case "a" -> {
                    graph.addEdge(requestObject.getFromNode(), requestObject.getToNode());
                    batchLog.append("void\n");
                }
                case "d" -> {
                    graph.deleteEdge(requestObject.getFromNode(), requestObject.getToNode());
                    batchLog.append("void\n");
                }
            }
        }
        FileManager.appendToFile(workingDirectory+"/logs.txt",batchLog.toString());
        return queryResult.toString();
    }
}
