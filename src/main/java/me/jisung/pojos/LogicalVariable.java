package me.jisung.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jisung on 7/12/2016.
 */
public class LogicalVariable extends LogicalExpression {

  private final String variableName;

  public LogicalVariable(String variableName) {
    super("var");

    this.variableName = variableName;
  }

  @JsonProperty("n")
  public String getVariableName() {
      return variableName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LogicalVariable other = (LogicalVariable) o;
    if (variableName != null
        ? !variableName.equals(other.variableName)
        : other.variableName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return variableName != null ? variableName.hashCode() : 0;
  }
}
