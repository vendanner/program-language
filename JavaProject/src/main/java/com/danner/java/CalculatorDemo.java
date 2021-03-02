package com.danner.java;

import com.danner.java.antlr4.MyCalculatorVisitor;
import com.danner.java.antlr4.out.CalculatorLexer;
import com.danner.java.antlr4.out.CalculatorParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class CalculatorDemo {
    public static void main(String[] args) {
        String query = "3.2*(6.3-4.51)";
        CalculatorLexer lexer = new CalculatorLexer(new ANTLRInputStream(query));
        CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
        MyCalculatorVisitor visitor = new MyCalculatorVisitor();
        CalculatorParser.ExprContext expr = parser.expr();
        System.out.println(visitor.visit(expr));
    }
}
