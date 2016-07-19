import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import me.jisung.compiler.Compiler;
import me.jisung.pojos.AndExpression;
import me.jisung.pojos.ComparisonExpression;
import me.jisung.pojos.Conclusion;
import me.jisung.pojos.LogicalConstant;
import me.jisung.pojos.LogicalExpression;
import me.jisung.pojos.LogicalVariable;
import me.jisung.pojos.NumericConstant;
import me.jisung.pojos.NumericVariable;
import me.jisung.pojos.OrExpression;
import me.jisung.pojos.RealArithmeticExpression;
import me.jisung.pojos.Rule;
import me.jisung.pojos.RuleSet;


/**
 * Created by Jisung on 7/13/2016.
 */

@RunWith(Parameterized.class)
public class CompilerTest {

  private static Conclusion standardConclusion = new Conclusion("TheConclusion");

  private static RuleSet createRuleSet(LogicalExpression... conditions) {
    RuleSet ruleSet = new RuleSet();
    for (LogicalExpression condition : conditions) {
      Rule rule = new Rule(condition, standardConclusion);
      ruleSet.addRule(rule);
    }
    return ruleSet;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {
            "if A = 1 then TheConclusion;",
            createRuleSet(new ComparisonExpression("=",
                new NumericVariable("A"),
                new NumericConstant(BigDecimal.valueOf(1))))
        },

        {
            "if (true) then TheConclusion; if (false) then TheConclusion;",
            createRuleSet(LogicalConstant.getTrue(), LogicalConstant.getFalse())
        },

        {
            "if a + 2 < 1 and c or b then TheConclusion;",
            createRuleSet(
                new OrExpression(
                    new AndExpression(
                        new ComparisonExpression(
                            "<",
                            new RealArithmeticExpression(
                                "+",
                                new NumericVariable("a"),
                                new NumericConstant(BigDecimal.valueOf(2))
                            ),
                            new NumericConstant(BigDecimal.valueOf(1))
                        ),
                        new LogicalVariable("c")
                    ),
                    new LogicalVariable("b")
                )
            )
        },
    });
  }

  private final String stringToCompile;
  private final RuleSet targetRuleSet;

  public CompilerTest(String stringToCompile, RuleSet targetRuleSet) {
    this.stringToCompile = stringToCompile;
    this.targetRuleSet = targetRuleSet;
  }

  @Test
  public void testRule() {
    Compiler compiler = new Compiler();
    RuleSet gotRuleSet = compiler.compile(stringToCompile);

    System.out.println("[" + gotRuleSet.equals(targetRuleSet) + "] " + stringToCompile);

    assertEquals(targetRuleSet, gotRuleSet);

    // JSON serialization
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(gotRuleSet);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    System.out.println(jsonString);
  }
}
