package com.danner.bigdata.utils;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.template.TemplateAnalyzer;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * 数据生产器 使用 https://github.com/CloudWise-OpenSource/Data-Processer
 */
public class GeneratorData {

    public static void main(String[] args) throws Exception {
//        DicInitializer.init();
//        //编辑模版
//        String tplName = "templates/Test.tpl";
//        String tpl = "My name is $Dic{name}, my age is $Func{intRand(1,5)}";
//        //创建模版分析器（一个模版new一个TemplateAnalyzer对象即可）
//        TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
//        //分析模版生成模拟数据
//        String abc = testTplAnalyzer.analyse();
//        //打印分析结果
//        System.out.println(abc);
        DicInitializer.init();
        File templates = new File("templates");
        if(templates.isDirectory()){
            File[] tplFiles = templates.listFiles();
            for(File tplFile: tplFiles){
                if(tplFile.isFile()){
                    String tpl = FileUtils.readFileToString(tplFile);
                    String tplName = tplFile.getName();
                    System.out.println("======tplName: "+tplName+", begin===================");
                    TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
                    String abc = testTplAnalyzer.analyse();
                    System.out.println(abc);
                    System.out.println("======tplName: "+tplName+", end==================");
                    System.out.println();
                }
            }
        }
    }

}
