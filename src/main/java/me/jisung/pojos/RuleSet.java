package me.jisung.pojos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * rule_set -> single_rule* EOF
 *
 * Created by Jisung on 7/12/2016.
 */
public class RuleSet implements RuleSetPojo {
  public final List<Rule> rules;

  /**
   * Construct empty RuleSet, and then adding rules.
   */
  public RuleSet() {
    this.rules = new ArrayList<>();

  }

  /**
   * Adding all rules at once.
   *
   * @param rules Collection of rules
   */
  public RuleSet(Collection<Rule> rules) {
    this.rules = new ArrayList<>(rules);
  }

  /**
   * Get list of whole rules which are contained in the rule set.
   * The list is unmodifiable.
   *
   * @return Unmodifiable list of whole rules contained.
   */
  public List<Rule> getRules() {
    return Collections.unmodifiableList(rules);
  }

  /**
   *
   * @param rule
   */
  public void addRule(Rule rule) {
    this.rules.add(rule);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RuleSet ruleSet = (RuleSet) o;

    return rules != null ? rules.equals(ruleSet.rules) : ruleSet.rules == null;

  }

  @Override
  public int hashCode() {
    return rules != null ? rules.hashCode() : 0;
  }
}
