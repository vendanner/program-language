package org.danner.bigdata.flink.janino;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ScriptEvaluator;

/**
 * 代码自动生成函数
 *
 * @author reese
 * @date 2022/01/26
 */
public class HelloWorld {
    public static void main(String[] args) {
        ScriptEvaluator evaluator = new ScriptEvaluator();
        try {
            evaluator.cook("static void method() {\n" +
                    "    System.out.println(\"print hello world\");\n" +
                    "}\n" +
                    "\n" +
                    "method();");
            evaluator.evaluate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
