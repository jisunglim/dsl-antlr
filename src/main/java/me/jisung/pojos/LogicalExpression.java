package me.jisung.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jisung on 7/12/2016.
 */
public class LogicalExpression implements RuleSetPojo {
  private final String type;

  protected LogicalExpression(String type) {
    this.type = type;
  }

  @JsonProperty("t")
  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LogicalExpression that = (LogicalExpression) o;

    return type != null ? type.equals(that.type) : that.type == null;

  }

  @Override
  public int hashCode() {
    return type != null ? type.hashCode() : 0;
  }
}
