package structureToBeCompleted;

public class Application {

    public static void main(String[] args) {

        String filePath = "Réseaux/Reunion_amis.txt";
        KnowledgeBase knowledgeBase = new KnowledgeBase(filePath);

        System.out.println(knowledgeBase.getBf());
        System.out.println(knowledgeBase.getBfSat());
        System.out.println(knowledgeBase.getBr());
    }
}
