public class Request {
    String requestType;
    int fromNode;
    int toNode;
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
}
