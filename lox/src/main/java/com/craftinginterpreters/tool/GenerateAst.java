package com.craftinginterpreters.tool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


/*
 * .tool instead of .lox this scripts isn't part of the interpreter itself 
 */
public class GenerateAst {
    public static void main(String[] args) throws  IOException{
        if(args.length!=1){
            System.err.println("Usage:genrate_ast <output directory>");
            System.exit(64);
        }
        String outputDir=args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
        "Binary   : Expr left,Token operator,Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Unary    : Token operator,Expr right"));
    }  

    public static void defineAst(String ouputDir,String baseName,List<String> types) throws IOException{
        String path=ouputDir +"/"+baseName+".java";
        try (PrintWriter writer = new PrintWriter(path,"UTF-8")) {
            writer.println("package com.craftinginterpreters.lox;");
            writer.println();
            writer.println("import java.util.List;");
            writer.println();
            writer.println("abstract class "+baseName+"{");
            defineVisitor(writer,baseName,types);
            for(String type:types){
                //trim语法用于去除空白字符
                String className=type.split(":")[0].trim();
                String filed=type.split(":")[1].trim();
                defineType(writer, baseName, className, filed);
            }
            writer.println();
            writer.println("\tabstract <R> R accept(Visitor<R> visitor);");
            writer.println("}");
        }
    }

    private  static  void defineVisitor(PrintWriter writer,String baseName,List<String> types){
        writer.println("\tinterface Visitor<R> {");
        
        for(String type:types){
            String typeName=type.split(":")[0].trim();
            writer.println("\t\tR visit"+typeName+baseName+"("+typeName+" "+baseName.toLowerCase()+");");   
        }
        writer.println("\t}");


    }

    private static void defineType(PrintWriter writer,String baseName,String className,String fieldList){
        writer.println("\tstatic class "+className+" extends "+baseName+" {");
        writer.println("\t\t"+className+"("+fieldList+") {");
        String[] fields=fieldList.split(",");
        for (String field :fields){
            String name=field.split(" ")[1];
            writer.println("\t\t\tthis."+name+" = "+name+";");
        }
        writer.println("\t\t}");
        writer.println("");
        writer.println("\t\t@Override");
        writer.println("\t\t<R> R accept(Visitor<R> visitor) {");
        writer.println(" \t\t\treturn visitor.visit" +
            className + baseName + "(this);");
        writer.println("\t\t}");
        writer.println("");
        for(String field:fields){
            String typename=field.split(" ")[0];
            String name=field.split(" ")[1];
            writer.println("\t\tfinal "+typename+" "+name+";");
        }
        writer.println("\t}");
        writer.println("");
    }

}
