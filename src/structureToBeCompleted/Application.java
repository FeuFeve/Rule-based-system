package structureToBeCompleted;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        String filePath = "Réseaux/Alpha-Bravo-Charlie.txt";
        KnowledgeBase knowledgeBase = new KnowledgeBase(filePath);

        System.out.println("BR : " + knowledgeBase.getBr());
        System.out.println("BF : " + knowledgeBase.getBf());
        System.out.println("BF* (avant) : " + knowledgeBase.getBfSat());
        System.out.println("*****\n");

        ThreadMXBean thread = ManagementFactory.getThreadMXBean();

        long startTime = System.nanoTime();
        long startCpuTime = thread.getCurrentThreadCpuTime();
        long startUserTime = thread.getCurrentThreadUserTime();

        knowledgeBase.forwardChainingOpt();

        long userTime = thread.getCurrentThreadUserTime() - startUserTime;
        long cpuTime = thread.getCurrentThreadCpuTime() - startCpuTime;
        long sysTime = cpuTime - userTime;
        long realTime = System.nanoTime() - startTime;

        System.out.println("BF* (après) : " + knowledgeBase.getBfSat());

        System.out.println("####################");
        System.out.println("Temps d'exécution :");
        System.out.println("Real time = " + (realTime / 1000000f) + "ms");
        System.out.println("System time = " + (sysTime / 1000000f) + "ms");
        System.out.println("CPU time = " + (cpuTime / 1000000f) + "ms");
        System.out.println("User time = " + (userTime / 1000000f) + "ms");


        Scanner scanner = new Scanner(System.in);
        String atomString;
        Atom atom;
        String result;

        while(true) {
            System.out.println("\n**************************************************\n\n");
            System.out.println("Nommer un atome à prouver :");

            atomString = scanner.nextLine();
            if (atomString.equals("/stop"))
                break;
            atom = new Atom(atomString);

            if (knowledgeBase.getBfSat().contains(atom))
                result = "oui";
            else
                result = "non";
            System.out.println("\n> Présence de '" + atom + "' dans BF* : " + result + "\n");

            System.out.println("Recherche de '" + atom + "' via chaînage arrière :");
            if (knowledgeBase.backwardChainingOpt(atom))
                result = "oui";
            else
                result = "non";
            System.out.println("> Atome prouvé par chaînage arrière : " + result);
        }
    }
}
