package com.danner.java.antlr4;


import com.danner.java.antlr4.out.CalculatorBaseVisitor;
import com.danner.java.antlr4.out.CalculatorParser;

/**
 * CalculatorBaseVisitor 访问者模式
 */
public class MyCalculatorVisitor extends CalculatorBaseVisitor<Object> {
    @Override
    public Float visitAddOrSub(CalculatorParser.AddOrSubContext ctx) {
        Object obj0 = visit(ctx.expr(0));
        Object obj1 = visit(ctx.expr(1));
        if (CalculatorParser.ADD == ctx.op.getType()) {
            return (Float)obj0 + (Float)obj1;
        } else {
            return (Float)obj0 - (Float)obj1;
        }
    }

    @Override
    public Float visitMultOrDiv(CalculatorParser.MultOrDivContext ctx) {
        Object obj0 = visit(ctx.expr(0));
        Object obj1 = visit(ctx.expr(1));
        if (CalculatorParser.MUL == ctx.op.getType()) {
            return (Float)obj0 * (Float)obj1;
        } else {
            return (Float)obj0 / (Float)obj1;
        }
    }

    @Override
    public Object visitParenExpr(CalculatorParser.ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitFloat(CalculatorParser.FloatContext ctx) {
        return Float.parseFloat(ctx.getText());
    }
}
