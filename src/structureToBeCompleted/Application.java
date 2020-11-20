package structureToBeCompleted;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        String filePath = "Réseaux/Reunion_amis.txt";
        KnowledgeBase knowledgeBase = new KnowledgeBase(filePath);

        System.out.println("### AVANT ###");
        System.out.println("BR: " + knowledgeBase.getBr());
        System.out.println("BF: " + knowledgeBase.getBf());
        System.out.println("BF*: " + knowledgeBase.getBfSat());
        System.out.println("**************************************************\n");

        ThreadMXBean thread = ManagementFactory.getThreadMXBean();

        long startTime = System.nanoTime();
        long startCpuTime = thread.getCurrentThreadCpuTime();
        long startUserTime = thread.getCurrentThreadUserTime();

//        knowledgeBase.forwardChainingBasic();
        knowledgeBase.forwardChainingOpt();

        List<Atom> atomsToCheck = new ArrayList<>();

        for (int i = 0; i < knowledgeBase.getBr().size(); i++) {
            Rule rule = knowledgeBase.getBr().getRule(i);
            for (Atom a : rule.getHypothesis()) {
                if (!atomsToCheck.contains(a)) {
                    atomsToCheck.add(a);
                }
            }
        }

        System.out.println("To check:");
        System.out.println(atomsToCheck + "\n\n");

        for (Atom a : atomsToCheck) {
            knowledgeBase.backwardChaining(a, new ArrayList<>(), 0);
            System.out.println("\n");
        }

        long userTime = thread.getCurrentThreadUserTime() - startUserTime;
        long cpuTime = thread.getCurrentThreadCpuTime() - startCpuTime;
        long sysTime = cpuTime - userTime;
        long realTime = System.nanoTime() - startTime;

        System.out.println("### APRES ###");
        System.out.println("BF*: " + knowledgeBase.getBfSat());
        System.out.println("**************************************************\n");

        System.out.println("####################");
        System.out.println("Temps d'exécution :");
        System.out.println("Real time = " + (realTime / 1000000f) + "ms");
        System.out.println("System time = " + (sysTime / 1000000f) + "ms");
        System.out.println("CPU time = " + (cpuTime / 1000000f) + "ms");
        System.out.println("User time = " + (userTime / 1000000f) + "ms");
    }
}
