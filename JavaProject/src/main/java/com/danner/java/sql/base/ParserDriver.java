package com.danner.java.sql.base;

import com.danner.java.sql.base.out.SqlBaseLexer;
import com.danner.java.sql.base.out.SqlBaseParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * 输出 query 的逻辑计划
 * 注意一定要全部大写，坑死了
 */
public class ParserDriver {
    public static void main(String[] args) {
//        String query = "SELECT * FROM SFD WHERE DSF>19";
        String query = "select fds from ds where ds>19";
        String upper = query.toUpperCase();
        SqlBaseLexer lexer = new SqlBaseLexer(new ANTLRInputStream(upper));
        SqlBaseParser parser = new SqlBaseParser(new CommonTokenStream(lexer));
        MyVisitor visitor = new MyVisitor();
//        SqlBaseBaseVisitor<String> visitor = new SqlBaseBaseVisitor<>();

        String res = visitor.visitSingleStatement(parser.singleStatement());
        System.out.println("res="+res);
    }
}
