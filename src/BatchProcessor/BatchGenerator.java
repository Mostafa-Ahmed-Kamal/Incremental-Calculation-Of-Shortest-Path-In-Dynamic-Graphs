package BatchProcessor;

import java.util.Random;

public class BatchGenerator {
    private final char[] writeOptions = {'a', 'd'};
    private final char[] readOption = {'q'};
    Random random = new Random();
    public String generateRandomGraphInitializer(int batchSize, int graphSize){
        StringBuilder initialGraph = new StringBuilder();
        for (int i = 0; i < batchSize; i++) {
            initialGraph.append(random.nextInt(graphSize)).append(" ").append(random.nextInt(graphSize)).append("\n");
        }
        return initialGraph.append("S").toString();
    }
    public String generateRandomBatch(int writeCount, int batchSize, int graphSize){
        StringBuilder batch = new StringBuilder();
        int currentSize = 0;
        while(batchSize-currentSize>writeCount){
            int choice = writeCount>0?random.nextInt(2):1;
            // Write
            if (choice==0){
                batch.append(addOperation(graphSize,writeOptions));
                writeCount--;
            }
            else{
                batch.append(addOperation(graphSize,readOption));
            }
            currentSize++;
        }
        for (int i = 0; i < writeCount; i++) {
            batch.append(addOperation(graphSize,writeOptions));
        }
        return batch.append("F").toString();
    }
    public String generateCustomGraphInitializer(){
        return """
                1 2
                2 3
                3 1
                4 1
                2 4
                S
                """;
    }
    public String generateCustomBatch(){
        String[] randomBatches = {"""
                A 4 5
                A 5 3
                Q 1 3
                D 2 3
                Q 1 3
                D 1 3
                F
                """,
                """
                D 4 5
                A 3 3
                Q 1 3
                D 2 3
                F
                """
        };
        return randomBatches[random.nextInt(randomBatches.length)];
    }
    private String addOperation(int graphSize, char[] options){
        return options[random.nextInt(options.length)]+" "+random.nextInt(graphSize)+" "+random.nextInt(graphSize)+"\n";
    }

    public static void main(String[] args) {
        BatchGenerator bg = new BatchGenerator();
        for (int i = 0; i < 10; i++) {
            System.out.println(bg.generateRandomBatch(2,4,10)+"\n");
        }
    }
}
