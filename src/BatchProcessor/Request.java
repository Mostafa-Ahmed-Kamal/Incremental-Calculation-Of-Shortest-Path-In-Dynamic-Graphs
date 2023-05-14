package BatchProcessor;

public class Request {
    private String requestType;
    private int fromNode;
    private int toNode;
    public Request(String request){
        String[] parameters = request.split(" ");
        if (parameters.length!=3){
            System.out.println("invalid request");
            return;
        }
        requestType = parameters[0];
        fromNode = Integer.parseInt(parameters[1]);
        toNode = Integer.parseInt(parameters[2]);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getFromNode() {
        return fromNode;
    }

    public void setFromNode(int fromNode) {
        this.fromNode = fromNode;
    }

    public int getToNode() {
        return toNode;
    }

    public void setToNode(int toNode) {
        this.toNode = toNode;
    }
}
