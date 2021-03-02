// Generated from /Users/luyongtao/vendanner/learn/java/JavaProject/src/main/java/com/danner/java/antlr4/Calculator.g4 by ANTLR 4.8
package com.danner.java.antlr4.out;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CalculatorParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CalculatorVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CalculatorParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(CalculatorParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multOrDiv}
	 * labeled alternative in {@link CalculatorParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultOrDiv(CalculatorParser.MultOrDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addOrSub}
	 * labeled alternative in {@link CalculatorParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOrSub(CalculatorParser.AddOrSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code float}
	 * labeled alternative in {@link CalculatorParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat(CalculatorParser.FloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link CalculatorParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(CalculatorParser.ParenExprContext ctx);
}