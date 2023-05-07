public class start {
    public static void main(String[] args) {
        Server server = new Server();
        server.initializeGraph("""
                1 2
                2 3
                3 1
                4 1
                2 4
                S""");
        String Batch1Result = server.processBatch("""
                Q 1 3
                A 4 5
                Q 1 5
                Q 5 1
                F""");
        String Batch2Result = server.processBatch("""
                A 5 3
                Q 1 3
                D 2 3
                Q 1 3
                F""");
        System.out.println("Batch 1:\n"+Batch1Result);
        System.out.println("Batch 2:\n"+Batch2Result);
    }
}
