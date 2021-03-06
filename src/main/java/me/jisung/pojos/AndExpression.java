package me.jisung.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jisung on 7/12/2016.
 */
public class AndExpression extends LogicalExpression {
  private final LogicalExpression left;
  private final LogicalExpression right;

  public AndExpression(LogicalExpression left, LogicalExpression right) {
    super("and");
    this.left = left;
    this.right = right;
  }

  @JsonProperty("left")
  public LogicalExpression getLeft() {
    return left;
  }

  @JsonProperty("right")
  public LogicalExpression getRight() {
    return right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AndExpression other = (AndExpression) o;

    if (left != null ? !left.equals(other.left) : other.left != null) return false;
    if (right != null ? !right.equals(other.right) : other.right != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = left != null ? left.hashCode() : 0;
    result = 31 * result + (right != null ? right.hashCode() : 0);
    return result;
  }
}
