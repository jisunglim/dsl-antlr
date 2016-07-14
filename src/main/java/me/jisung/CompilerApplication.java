package me.jisung;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import me.jisung.compiler.Compiler;
import me.jisung.pojos.RuleSet;

/**
 * Created by Jisung on 7/12/2016.
 */
public class CompilerApplication {
  public static void main(String[] args) {
    Compiler compiler = new Compiler();
    RuleSet ruleSet = compiler.compile("if - (A + 2) > 0.5 then be_careful;");

    // JSON serialization
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(ruleSet);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    System.out.println(jsonString);
  }
}
