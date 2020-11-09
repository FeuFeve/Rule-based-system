package structureToBeCompleted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NetworkGenerator {

    public static void main(String[] args) {
        final int varNumber = 1000;
        final int baseVarNumber = 50;

        final int ruleNumber = 30000;
        final int minRuleSize = 3;
        final int maxRuleSize = 6;


        Random random = new Random();

        String network = "";

        // Base de fait :
        network += generateNetworkLine(baseVarNumber, baseVarNumber, 1, varNumber);

        // Règles
        for (int i = 0; i < ruleNumber; i++) {
            network += generateNetworkLine(minRuleSize, maxRuleSize, 1, varNumber);
        }

        System.out.println(network);
    }

    private static String generateNetworkLine(int minSize, int maxSize, int minValue, int maxValue) {
        if (maxSize > (maxValue - minValue + 1)) {
            System.err.println("NetworkGenerator.generateNetworkLine : maxSize > (maxValue - minValue + 1)");
            return null;
        }

        Random random = new Random();

        int size = getRandomInt(minSize, maxSize);

        List<Integer> list = new ArrayList<>();
        int remaining = size;
        while (remaining > 0) {
            int value = getRandomInt(minValue, maxValue);
            if (!list.contains(value)) {
                list.add(value);
                remaining--;
            }
        }

        Collections.sort(list);

        String line = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) line += ";";
            line += "x" + list.get(i);
        }

        return line + "\n";
    }

    private static int getRandomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
