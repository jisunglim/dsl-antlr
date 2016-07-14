package me.jisung.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Stack;

import me.jisung.RuleSetGrammarBaseListener;
import me.jisung.RuleSetGrammarParser;
import me.jisung.pojos.AndExpression;
import me.jisung.pojos.ArithmeticExpression;
import me.jisung.pojos.ComparisonExpression;
import me.jisung.pojos.ComparisonOperand;
import me.jisung.pojos.LogicalConstant;
import me.jisung.pojos.LogicalExpression;
import me.jisung.pojos.LogicalVariable;
import me.jisung.pojos.Negation;
import me.jisung.pojos.NumericConstant;
import me.jisung.pojos.NumericVariable;
import me.jisung.pojos.OrExpression;
import me.jisung.pojos.RealArithmeticExpression;
import me.jisung.pojos.Rule;
import me.jisung.pojos.RuleSet;

/**
 *
 * Created by Jisung on 7/12/2016.
 */
public class TreeBuilder extends RuleSetGrammarBaseListener {

  private RuleSet ruleSet = null;
  private Rule rule = null;
  private LogicalExpression condition = null;

  private Stack<LogicalExpression> logicalExpressions = new Stack<>();
  private Stack<ComparisonOperand> comparisonOperands = new Stack<>();
  private Stack<ArithmeticExpression> arithmeticExpressions = new Stack<>();

  public RuleSet getRuleSet() {
    return ruleSet;

  }

  //
  @Override
  public void enterRule_set(@NotNull RuleSetGrammarParser.Rule_setContext ctx) {
    // Check Initial Condition of rule_set
    assert ruleSet == null;
    assert rule == null;
    assert condition == null;

    // Check Initial Condition of Tree Builder
    assert logicalExpressions.empty();
    assert comparisonOperands.empty();
    assert arithmeticExpressions.empty();

    // Apply tree builder to rule set
    this.ruleSet = new RuleSet();
  }

  @Override
  public void exitRule_set(RuleSetGrammarParser.Rule_setContext ctx) {
    super.exitRule_set(ctx);
  }

  //
  @Override
  public void enterSingle_rule(@NotNull RuleSetGrammarParser.Single_ruleContext ctx) {
    this.rule = new Rule();
  }

  @Override
  public void exitSingle_rule(@NotNull RuleSetGrammarParser.Single_ruleContext ctx) {
    this.rule.setCondition(this.logicalExpressions.pop());
    this.ruleSet.addRule(this.rule);
    this.rule = null;
  }

  //
  @Override
  public void enterNumericVariable(@NotNull RuleSetGrammarParser.NumericVariableContext ctx) {
    super.enterNumericVariable(ctx);
  }

  @Override
  public void exitNumericVariable(@NotNull RuleSetGrammarParser.NumericVariableContext ctx) {
    this.arithmeticExpressions.add(new NumericVariable(ctx.getText()));
  }

  @Override
  public void enterNumericConst(RuleSetGrammarParser.NumericConstContext ctx) {
    super.enterNumericConst(ctx);
  }

  @Override
  public void exitNumericConst(@NotNull RuleSetGrammarParser.NumericConstContext ctx) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator('.');
    String pattern = "#0.0#";

    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
    decimalFormat.setParseBigDecimal(true);
    BigDecimal value;
    try {
      value = (BigDecimal) decimalFormat.parse(ctx.getText());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    this.arithmeticExpressions.push(new NumericConstant(value));
  }

  @Override
  public void enterArithmeticExpressionMult(RuleSetGrammarParser.ArithmeticExpressionMultContext ctx) {
    super.enterArithmeticExpressionMult(ctx);
  }

  @Override
  public void exitArithmeticExpressionMult(RuleSetGrammarParser.ArithmeticExpressionMultContext ctx) {
    exitRealArithmeticExpression("*");
  }

  @Override
  public void enterArithmeticExpressionDiv(RuleSetGrammarParser.ArithmeticExpressionDivContext ctx) {
    super.enterArithmeticExpressionDiv(ctx);
  }

  @Override
  public void exitArithmeticExpressionDiv(RuleSetGrammarParser.ArithmeticExpressionDivContext ctx) {
    exitRealArithmeticExpression("/");
  }

  @Override
  public void enterArithmeticExpressionPlus(RuleSetGrammarParser.ArithmeticExpressionPlusContext ctx) {
    super.enterArithmeticExpressionPlus(ctx);
  }

  @Override
  public void exitArithmeticExpressionPlus(RuleSetGrammarParser.ArithmeticExpressionPlusContext ctx) {
    exitRealArithmeticExpression("+");
  }

  @Override
  public void enterArithmeticExpressionMinus(RuleSetGrammarParser.ArithmeticExpressionMinusContext ctx) {
    super.enterArithmeticExpressionMinus(ctx);
  }

  @Override
  public void exitArithmeticExpressionMinus(RuleSetGrammarParser.ArithmeticExpressionMinusContext ctx) {
    exitRealArithmeticExpression("-");
  }

  protected void exitRealArithmeticExpression(String op) {
    // popping order matters
    ArithmeticExpression right = this.arithmeticExpressions.pop();
    ArithmeticExpression left = this.arithmeticExpressions.pop();
    RealArithmeticExpression expr = new RealArithmeticExpression(op, left, right);
    this.arithmeticExpressions.push(expr);
  }

  @Override
  public void enterArithmeticExpressionNegative(RuleSetGrammarParser.ArithmeticExpressionNegativeContext ctx) {
    super.enterArithmeticExpressionNegative(ctx);
  }

  @Override
  public void exitArithmeticExpressionNegative(RuleSetGrammarParser.ArithmeticExpressionNegativeContext ctx) {
    Negation negation = new Negation(this.arithmeticExpressions.pop());
    this.arithmeticExpressions.push(negation);
  }

  @Override
  public void enterArithmeticExpressionNumbericEntity(RuleSetGrammarParser.ArithmeticExpressionNumbericEntityContext ctx) {
    super.enterArithmeticExpressionNumbericEntity(ctx);
  }

  @Override
  public void exitArithmeticExpressionNumbericEntity(RuleSetGrammarParser.ArithmeticExpressionNumbericEntityContext ctx) {
    super.exitArithmeticExpressionNumbericEntity(ctx);
  }

  @Override
  public void enterArithmeticExpressionParens(RuleSetGrammarParser.ArithmeticExpressionParensContext ctx) {
    super.enterArithmeticExpressionParens(ctx);
  }

  @Override
  public void exitArithmeticExpressionParens(RuleSetGrammarParser.ArithmeticExpressionParensContext ctx) {
    super.exitArithmeticExpressionParens(ctx);
  }

  //
  @Override
  public void enterComparisonExpression(RuleSetGrammarParser.ComparisonExpressionContext ctx) {
    super.enterComparisonExpression(ctx);
  }

  @Override
  public void exitComparisonExpression(RuleSetGrammarParser.ComparisonExpressionContext ctx) {
    super.exitComparisonExpression(ctx);
  }

  @Override
  public void enterComparison_operand(RuleSetGrammarParser.Comparison_operandContext ctx) {
    super.enterComparison_operand(ctx);
  }

  @Override
  public void exitComparison_operand(RuleSetGrammarParser.Comparison_operandContext ctx) {
    ArithmeticExpression expr = this.arithmeticExpressions.pop();
    this.comparisonOperands.push(expr);
  }

  @Override
  public void enterComparisonExpressionWithOperator(RuleSetGrammarParser.ComparisonExpressionWithOperatorContext ctx) {
    super.enterComparisonExpressionWithOperator(ctx);
  }

  @Override
  public void exitComparisonExpressionWithOperator(RuleSetGrammarParser.ComparisonExpressionWithOperatorContext ctx) {
    ComparisonOperand right = this.comparisonOperands.pop();
    ComparisonOperand left = this.comparisonOperands.pop();
    String op = ctx.getChild(1).getText();
    ComparisonExpression expr = new ComparisonExpression(op, left, right);
    this.logicalExpressions.push(expr);
  }

  @Override
  public void enterComparisonExpressionParens(RuleSetGrammarParser.ComparisonExpressionParensContext ctx) {
    super.enterComparisonExpressionParens(ctx);
  }

  @Override
  public void exitComparisonExpressionParens(RuleSetGrammarParser.ComparisonExpressionParensContext ctx) {
    super.exitComparisonExpressionParens(ctx);
  }


  @Override
  public void enterComp_operator(RuleSetGrammarParser.Comp_operatorContext ctx) {
    super.enterComp_operator(ctx);
  }

  @Override
  public void exitComp_operator(RuleSetGrammarParser.Comp_operatorContext ctx) {
    super.exitComp_operator(ctx);
  }


  //
  @Override
  public void enterCondition(RuleSetGrammarParser.ConditionContext ctx) {
    super.enterCondition(ctx);
  }

  @Override
  public void exitCondition(RuleSetGrammarParser.ConditionContext ctx) {
    super.exitCondition(ctx);
  }

  //
  @Override
  public void enterConclusion(RuleSetGrammarParser.ConclusionContext ctx) {
    super.enterConclusion(ctx);
  }

  @Override
  public void exitConclusion(@NotNull RuleSetGrammarParser.ConclusionContext ctx) {
    this.rule.setConclusion(ctx.getText());
  }

  //
  @Override
  public void enterLogicalEntity(RuleSetGrammarParser.LogicalEntityContext ctx) {
    super.enterLogicalEntity(ctx);
  }

  @Override
  public void exitLogicalEntity(RuleSetGrammarParser.LogicalEntityContext ctx) {
    super.exitLogicalEntity(ctx);
  }


  @Override
  public void enterLogicalExpressionInParen(RuleSetGrammarParser.LogicalExpressionInParenContext ctx) {
    super.enterLogicalExpressionInParen(ctx);
  }

  @Override
  public void exitLogicalExpressionInParen(RuleSetGrammarParser.LogicalExpressionInParenContext ctx) {
    super.exitLogicalExpressionInParen(ctx);
  }




  @Override
  public void enterLogicalConst(RuleSetGrammarParser.LogicalConstContext ctx) {
    super.enterLogicalConst(ctx);
  }

  @Override
  public void exitLogicalConst(RuleSetGrammarParser.LogicalConstContext ctx) {
    switch (ctx.getText().toUpperCase()) {
      case "TRUE" :
        this.logicalExpressions.push(LogicalConstant.getTrue());
        break;
      case "FALSE" :
        this.logicalExpressions.push(LogicalConstant.getFalse());
        break;
      default:
        throw new RuntimeException("Unknown logical constant: " + ctx.getText());
    }
  }

  @Override
  public void enterLogicalVariable(RuleSetGrammarParser.LogicalVariableContext ctx) {
    super.enterLogicalVariable(ctx);
  }

  @Override
  public void exitLogicalVariable(RuleSetGrammarParser.LogicalVariableContext ctx) {
    LogicalVariable variable = new LogicalVariable(ctx.getText());
    this.logicalExpressions.push(variable);
  }

  @Override
  public void enterLogicalExpressionAnd(RuleSetGrammarParser.LogicalExpressionAndContext ctx) {
    super.enterLogicalExpressionAnd(ctx);
  }

  @Override
  public void exitLogicalExpressionAnd(RuleSetGrammarParser.LogicalExpressionAndContext ctx) {
    LogicalExpression right = logicalExpressions.pop();
    LogicalExpression left = logicalExpressions.pop();
    AndExpression expr = new AndExpression(left, right);
    this.logicalExpressions.push(expr);
  }

  @Override
  public void enterLogicalExpressionOr(RuleSetGrammarParser.LogicalExpressionOrContext ctx) {
    super.enterLogicalExpressionOr(ctx);
  }

  @Override
  public void exitLogicalExpressionOr(RuleSetGrammarParser.LogicalExpressionOrContext ctx) {
    LogicalExpression right = logicalExpressions.pop();
    LogicalExpression left = logicalExpressions.pop();
    OrExpression expr = new OrExpression(left, right);
    this.logicalExpressions.push(expr);
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    super.enterEveryRule(ctx);
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    super.exitEveryRule(ctx);
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    super.visitErrorNode(node);
  }

}
