package structureToBeCompleted;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RuleBase {

    private List<Rule> rules; // l'ensemble des regles

    /**
     * Constructeur : cree une base de regles vide
     */
    public RuleBase() {
        rules = new ArrayList<>();
    }

    /**
     * ajoute une regle a la base de regles (sans verifier si elle y est deja)
     *
     * @param r regle a ajouter
     */
    public void addRule(Rule r) {
        rules.add(r);
    }

    /**
     * retourne le nombre de regles
     *
     * @return le nombre de regles dans la base
     */
    public int size() {
        return rules.size();
    }

    /**
     * teste si la base est vide
     *
     * @return vrai si la base est vide
     */
    public boolean isEmpty() {
        return rules.isEmpty();
    }

    public boolean estSemiPos() {
        for (Rule rule1 : rules) {
            for (Rule rule2 : rules) {
                if (rule2.getNegativeHypothesis().contains(rule1.getConclusion())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * retourne la regle de rang i
     *
     * @param i le rang de la regle (debut a 0)
     * @return la regle de rang i
     */
    public Rule getRule(int i) {
        return rules.get(i);
    }

    /**
     * retourne une description de la base de regles
     *
     * @return description de la base de faits
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String s = "Base de Regles" + " (" + rules.size() + ")" + ":\n";
        for (int i = 0; i < rules.size(); i++)
            s += "\t Regle " + (i + 1) + " : " + rules.get(i) + "\n";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RuleBase ruleBase = (RuleBase) o;
        return Objects.equals(rules, ruleBase.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }
}
