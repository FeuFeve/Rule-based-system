package structureToBeCompleted;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Rule {
    private List<Atom> hypothesis; // l'hypothese : une liste d'atomes (H+)
    private List<Atom> negativeHypothesis; // atomes négatifs de l'hypothèse (H-)
    private Atom conclusion; // la conclusion : un atome

    /**
     * Constructeur
     *
     * @param strRule la regle, sous forme sous forme textuelle ; cette forme est
     *                "atome1;atome2;...atomek", ou les (k-1) premiers atomes
     *                forment l'hypothese, et le dernier forme la conclusion
     */
    public Rule(String strRule) {
        hypothesis = new ArrayList<>();
        negativeHypothesis = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(strRule, ";");
        boolean lastWasNegative = false;
        while (st.hasMoreTokens()) {
            String s = st.nextToken(); // s represente un atome
            // ajout de a a la liste des atomes de l'hypothese (pour l'instant, on ajoute
            // aussi celui de la conclusion)
            if (s.startsWith("!")) {
                negativeHypothesis.add(new Atom(s.substring(1), true));
                System.out.println(s.substring(1));
                lastWasNegative = true;
            }
            else {
                hypothesis.add(new Atom(s));
                lastWasNegative = false;
            }

        }
        // on a mis tous les atomes crees en hypothese
        // il reste a tranferer le dernier en conclusion
        if (lastWasNegative) {
            conclusion = negativeHypothesis.remove(negativeHypothesis.size() - 1);
        }
        else {
            conclusion = hypothesis.remove(hypothesis.size() - 1);
        }
    }

    /**
     * accesseur a l'hypothese de la regle
     *
     * @return l'hypothese de la regle
     */
    public List<Atom> getHypothesis() {
        return hypothesis;
    }

    public List<Atom> getNegativeHypothesis() {
        return negativeHypothesis;
    }

    /**
     * retourne la ieme atome de l'hypothese
     *
     * @param i le rang de l'atome a retourner (debut a 0)
     * @return le ieme atome de l'hypothese
     */
    public Atom getAtomHyp(int i) {
        return hypothesis.get(i);
    }

    /**
     * accesseur a la conclusion de la regle
     *
     * @return l'atome conclusion de la regle
     */
    public Atom getConclusion() {
        return conclusion;
    }

    /**
     * retourne une description de la regle
     *
     * @return la chaine decrivant la regle (suivant l'ecriture habituelle)
     */
    @Override
    public String toString() {
        String s = "";
        for (Atom atom : hypothesis) {
            if (!s.equals(""))
                s += " ; ";
            s += atom;
        }
        for (Atom atom : negativeHypothesis) {
            if (!s.equals(""))
                s += " ; ";
            s += atom;
        }
        s += " --> " + conclusion;
        return s;
    }

}
