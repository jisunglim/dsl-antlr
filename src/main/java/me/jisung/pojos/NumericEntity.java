package me.jisung.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jisung on 7/12/2016.
 */
public class NumericEntity implements ArithmeticExpression, ComparisonOperand {
  private final String type;

  public NumericEntity(String type) {
    this.type = type;
  }

  @JsonProperty("t")
  public String getType() {
    return type;
  }
}
