package com.craftinginterpreters.lox;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.BANG;
import static com.craftinginterpreters.lox.TokenType.MINUS;
import static com.craftinginterpreters.lox.TokenType.PLUS;
import static com.craftinginterpreters.lox.TokenType.SLASH;
import static com.craftinginterpreters.lox.TokenType.STAR;

public class Interpreter implements Expr.Visitor<Object>,Stmt.Visitor<Void>{
    @Override
    public Object visitLiteralExpr(Expr.Literal expr){
        return expr.value;
    }

    void interpret(List<Stmt> statements){
        try {
            for(Stmt statement:statements){
                exec(statement);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    private Void exec(Stmt stmt){
        stmt.accept(this);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt){
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt){
        Object vaule=evaluate(stmt.expression);
        System.out.println(stringify(vaule));
        return null;
    }

    private String stringify(Object object){
        if(object==null) return "nil";
        if(object instanceof Double){
            String text=object.toString();
            if(text.endsWith(".0")){ //如果是整数那么打印整数
                text=text.substring(0,text.length()-2);
            }
            return text;
        }
        return object.toString();
    }

    public Object evaluate(Expr expr){
        return expr.accept(this);
    }

    private void checkNumberOperand(Token operator,Object operand){
        if(operand instanceof Double) return;
        throw new RuntimeError(operator,"Operand must be a number.");
    }

    private void checkNumberOperands(Token operator,Object left,Object right){
        if(left instanceof Double &&right instanceof Double) return;
        throw new RuntimeError(operator,"Operamd must be a number.");
    }


    
    public boolean isTruthy(Object object){
        if(object==null) return false;
        if(object instanceof Boolean) return (boolean)object;
        return true;
    }

    @Override
    public  Object visitUnaryExpr(Expr.Unary expr){
        Object right=evaluate(expr.right);
        Token operator=expr.operator;
        switch(operator.type){
            case MINUS ->{
                checkNumberOperand(operator, expr);
                return -(Double)right;
            }
            case BANG -> {
                return !isTruthy(right);
            }
        }
        return null;
    }

    @Override 
    public Object visitGroupingExpr(Expr.Grouping expr){
        return evaluate(expr.expression);
    }

    @Override
    public  Object visitCommaExpr(Expr.Comma comma){
        return evaluate(comma.right);
    }

    @Override
    public  Object visitTernaryExpr(Expr.Ternary ternary){
        if(isTruthy(evaluate(ternary.condition))){
            return evaluate(ternary.left);
        }
        else return evaluate(ternary.right);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr){
        Object left=evaluate(expr.left);
        Object right=evaluate(expr.right);
        Token operator=expr.operator;
        switch (operator.type){
            case PLUS ->{
                if(left instanceof Double && right instanceof Double){
                    checkNumberOperands(operator, left,right);
                    return (Double)left+(Double)right;
                }
                if(left instanceof  String && right instanceof String){
                    return (String)left+(String)right;
                }
                if(left instanceof  String || right instanceof String){
                    return stringify(left)+stringify(right);
                }
                throw new RuntimeError(expr.operator,"Operands must be two numbers or two strings.");
            }
            case MINUS -> {
                checkNumberOperands(operator, left,right);
                return (Double)left-(Double) right;
            }
            case SLASH -> {
                checkNumberOperands(operator, left,right);
                if(right.equals(0.0)) throw new RuntimeError(expr.operator,"division by zero");
                return (Double)left/(Double) right;
            }
            case STAR ->  {
                checkNumberOperands(operator, left,right);
                return (Double)left*(Double) right;
            }
            case EQUAL_EQUAL ->{
                return isEqual(left, right);
            }
            case BANG_EQUAL ->{
                return !isEqual(left, right);
            }
            case GREATER ->{
                checkNumberOperands(operator, left,right);
                return (Double) left >(Double) right;
            }
            case GREATER_EQUAL ->{
                checkNumberOperands(operator, left,right);
                return (Double) left >=(Double) right;
            }
            case LESS  ->{
                checkNumberOperands(operator, left,right);
                return (Double) left <(Double) right;
            }
            case LESS_EQUAL ->{
                checkNumberOperands(operator, left,right);
                return (Double) left <= (Double) right;
            }
        }
        return null;
    }

    private Boolean isEqual(Object a,Object b){
        if(a==null &&b==null) return true;
        if(a==null) return false;   //为什么使用equals 而不是== 因为等于等于 比较的很可能是引用值 而不是实际内容
        return a.equals(b);     
    } 
    
}
