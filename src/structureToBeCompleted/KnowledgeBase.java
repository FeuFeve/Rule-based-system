package structureToBeCompleted;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {

    private FactBase bf; // base de faits initiale
    private RuleBase br; // base de règles
    private FactBase bfSat; // base de faits saturée - vide initialement

    public KnowledgeBase() {
        bf = new FactBase();
        br = new RuleBase();
        bfSat = new FactBase();

    }

    public KnowledgeBase(String fic) {
        this(); // initialisation des bases à vide
        BufferedReader lectureFichier;
        try {
            lectureFichier = new BufferedReader(new FileReader(fic));
        } catch (FileNotFoundException e) {
            System.err.println("Fichier base de connaissances absent: " + fic);
            e.printStackTrace();
            return;
        }
        try {
            String s = lectureFichier.readLine();
            if (s != null) { // si non vide
                bf = new FactBase(s); // 1ere ligne : factbase
                s = lectureFichier.readLine();
                while (s != null && s.length() != 0) { // arret si fin de fichier ou ligne vide
                    br.addRule(new Rule(s));
                    s = lectureFichier.readLine();
                }
            }
            lectureFichier.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FactBase getBf() {
        return bf;
    }

    public FactBase getBfSat() {
        return bfSat;
    }

    public RuleBase getBr() {
        return br;
    }

    /**
     * Retourne une description de la base de connaissances
     *
     * @return description de la base de connaissances
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "**********\nBase de connaissances: \n" + bf + br + "\n**********";
    }


    public void forwardChainingBasic() {
        // algo basique de forward chaining

        bfSat = new FactBase(); // ré-initialisation de bfSat
        bfSat.addAtoms(bf.getAtoms()); // avec les atomes de bf
        boolean fin = false;
        boolean[] Appliquee = new boolean[br.size()];
        for (int i = 0; i < br.size(); i++) {
            Appliquee[i] = false;
        }
        while (!fin) {
            FactBase newFacts = new FactBase();
            for (int i = 0; i < br.size(); i++) {
                if (!Appliquee[i]) {
                    Rule r = br.getRule(i);
                    // test d'applicabilite de la regle i
                    boolean applicable = true;
                    List<Atom> hp = r.getHypothesis();
                    for (int j = 0; applicable && j < hp.size(); j++)
                        if (!bfSat.contains(r.getAtomHyp(j)))
                            applicable = false;
                    if (applicable) {
                        Appliquee[i] = true;
                        Atom c = r.getConclusion();
                        if (!bfSat.contains(c) && !newFacts.contains(c))
                            newFacts.addAtomWithoutCheck(c);
                    }
                }
            }
            if (newFacts.isEmpty())
                fin = true;
            else
                bfSat.addAtoms(newFacts.getAtoms());
        }
    }

    public void forwardChainingOpt() {

        if (!br.estSemiPos()) {
            System.err.println("ATTENTION : La base de règle n'est pas semi-positive (un littéral négatif est aussi dans une conclusion)");
            return; // Pour arrêter l'exécution de la méthode si BR n'est pas semi-positive
        }

        /*
        Crée une nouvelle base de règle qui contiendra les règles de BR moins celles dont un des atomes de la
        base de fait apparaît en littéral négatif dans une des règles
        Par exemple si BF = {atome1}
        et que dans BR, Règle1 = {!atome1} -> atome2
        Règle1 ne sera pas dans la nouvelle base de règle (car irréalisable)
        Le temps de calcul sera réduit : l'algorithme n'aura même pas à vérifier les termes négatifs
        (car forcément négatifs puisque ni dans BF ni dans conclusion)
         */
        RuleBase validRuleBase = new RuleBase();
        for (int i = 0; i < br.size(); i++) {
            Rule rule = br.getRule(i);
            boolean isValid = true;
            for (Atom atom : bf.getAtoms()) {
                if (rule.getNegativeHypothesis().contains(atom)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                validRuleBase.addRule(rule);
            }
        }

        System.out.println("Nouvelle BR : " + validRuleBase);

        bfSat = new FactBase();
        bfSat.addAtoms(bf.getAtoms());

        List<Atom> toProcess = new ArrayList<>(bf.getAtoms());
        int[] counter = new int[validRuleBase.size()];
        for (int i = 0; i < validRuleBase.size(); i++) {
            Rule rule = validRuleBase.getRule(i);
            counter[i] = rule.getHypothesis().size();
        }

        while (!toProcess.isEmpty()) {
            Atom F = toProcess.remove(0);

            for (int i = 0; i < validRuleBase.size(); i++) {
                Rule rule = validRuleBase.getRule(i);

                if (rule.getHypothesis().contains(F)) {
                    counter[i]--;
                    if (counter[i] == 0) {
                        Atom C = rule.getConclusion();
                        if (!bfSat.contains(C)) {
                            bfSat.addAtomWithoutCheck(C);
                            toProcess.add(C);
                        }
                    }
                }
            }
        }
    }

    public boolean backwardChaining(Atom atomToProve) {
        return backwardChaining(0, atomToProve, null);
    }

    public boolean backwardChaining(int depth, Atom currentAtom, List<Atom> atomsToProve) {

        if (atomsToProve == null)
            atomsToProve = new ArrayList<>();

        System.out.println(tab(depth) + currentAtom);
        if (bf.contains(currentAtom)) {
            if (atomsToProve.isEmpty())
                System.out.println(currentAtom + " est prouvé");
            return true;
        }

        ruleLoop:
        for (int index = 0; index < br.size(); index++) {
            Rule rule = br.getRule(index);

            if (!rule.getConclusion().toString().equals(currentAtom.toString()))
                continue;

            for (Atom a : rule.getHypothesis())
                if (atomsToProve.contains(a))
                    continue ruleLoop;

            System.out.println(tab(depth + 1) + "R" + (index + 1));

            boolean everyRuleAtomsProved = true;
            for (Atom a : rule.getHypothesis()) {
                atomsToProve.add(a);
                if (backwardChaining(depth + 1, a, atomsToProve)) {
                    atomsToProve.remove(a);
                }
                else {
                    System.out.println(tab(depth + 1) + "[ Échec ]");
                    atomsToProve.remove(a);
                    everyRuleAtomsProved = false;
                    break;
                }
            }

            if (everyRuleAtomsProved) {
                if (atomsToProve.isEmpty())
                    System.out.println(currentAtom + " est prouvé");
                return true;
            }
        }

        if (atomsToProve.isEmpty())
            System.out.println("[ Échec ]");
        return false;
    }

    public boolean backwardChainingOpt(Atom Q) {
        return backwardChainingOpt(0, Q, null, null, null);
    }

    public boolean backwardChainingOpt(int depth, Atom Q, List<Atom> atomsToProve, List<Atom> provedAtoms, List<Atom> failedAtoms) {

        if (atomsToProve == null)
            atomsToProve = new ArrayList<>();
        if (provedAtoms == null)
            provedAtoms = new ArrayList<>();
        if (failedAtoms == null)
            failedAtoms = new ArrayList<>();

        if (provedAtoms.contains(Q)) {
            System.out.println(tab(depth + 1) + Q);
            System.out.println(tab(depth + 1) + "[ Déjà prouvé ]");
            return true;
        }
        if (failedAtoms.contains(Q)) {
            System.out.println(tab(depth + 1) + Q);
            System.out.println(tab(depth + 1) + "[ Déjà échec ]");
            return false;
        }

        System.out.println(tab(depth) + Q);
        if (bf.contains(Q)) {
            if (atomsToProve.isEmpty())
                System.out.println(Q + " est prouvé");
            return true;
        }

        ruleLoop:
        for (int index = 0; index < br.size(); index++) {
            Rule rule = br.getRule(index);

            if (!rule.getConclusion().toString().equals(Q.toString()))
                continue;

            for (Atom a : rule.getHypothesis())
                if (atomsToProve.contains(a))
                    continue ruleLoop;

            System.out.println(tab(depth + 1) + "R" + (index + 1));

            boolean everyRuleAtomsProved = true;
            for (Atom a : rule.getHypothesis()) {
                atomsToProve.add(a);
                if (backwardChainingOpt(depth + 1, a, atomsToProve, provedAtoms, failedAtoms)) {
                    atomsToProve.remove(a);
                }
                else {
                    System.out.println(tab(depth + 1) + "[ Échec ]");
                    atomsToProve.remove(a);
                    everyRuleAtomsProved = false;
                    break;
                }
            }

            if (everyRuleAtomsProved) {
                if (atomsToProve.isEmpty())
                    System.out.println(Q + " est prouvé");
                provedAtoms.add(Q);
                return true;
            }
        }

        if (atomsToProve.isEmpty())
            System.out.println("[ Échec ]");
        failedAtoms.add(Q);
        return false;
    }

    private static String tab(int depth) {
        if (depth == 0)
            return "";
        else
            return "-".repeat(depth * 3) + " ";
    }
}
