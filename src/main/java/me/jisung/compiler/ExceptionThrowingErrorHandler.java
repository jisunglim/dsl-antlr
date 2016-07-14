package me.jisung.compiler;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/**
 * Created by Jisung on 7/12/2016.
 */
public class ExceptionThrowingErrorHandler extends DefaultErrorStrategy {
  @Override
  public void recover(Parser recognizer, RecognitionException e) {
    throw new RuntimeException(e);
  }

  @Override
  public Token recoverInline(Parser recognizer) throws RecognitionException {
    throw new RuntimeException(new InputMismatchException(recognizer));
  }

  @Override
  public void sync(Parser recognizer) throws RecognitionException {
  }
}