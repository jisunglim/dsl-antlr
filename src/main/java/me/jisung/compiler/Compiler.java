package me.jisung.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import me.jisung.RuleSetGrammarLexer;
import me.jisung.RuleSetGrammarParser;
import me.jisung.pojos.RuleSet;

/**
 * Created by Jisung on 7/12/2016.
 */
public class Compiler {

  public RuleSet compile(String inputString) {
    ANTLRInputStream input = new ANTLRInputStream(inputString);
    RuleSetGrammarLexer lexer = new RuleSetGrammarLexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    RuleSetGrammarParser parser = new RuleSetGrammarParser(tokens);

    TreeBuilder treeBuilder = new TreeBuilder();
    parser.addParseListener( treeBuilder );
    parser.setErrorHandler(new ExceptionThrowingErrorHandler());

    parser.rule_set();

    return treeBuilder.getRuleSet();
  }
}
