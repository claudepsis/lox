package com.craftinginterpreters.lox;
import static com.craftinginterpreters.lox.TokenType.BANG;
import static com.craftinginterpreters.lox.TokenType.MINUS;
import static com.craftinginterpreters.lox.TokenType.PLUS;
import static com.craftinginterpreters.lox.TokenType.SLASH;
import static com.craftinginterpreters.lox.TokenType.STAR;

public class Interpreter implements Expr.Visitor<Object>{
    @Override
    public Object visitLiteralExpr(Expr.Literal expr){
        return expr.value;
    }

    public Object evaluate(Expr expr){
        return expr.accept(this);
    }


    
    public boolean isTruthy(Object object){
        if(object==null) return false;
        if(object instanceof Boolean) return (boolean)object;
        return true;
    }

    @Override
    public  Object visitUnaryExpr(Expr.Unary expr){
        Object right=evaluate(expr.right);
        switch(expr.operator.type){
            case MINUS ->{
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
        switch (expr.operator.type){
            case PLUS ->{
                if(left instanceof Double && right instanceof Double){
                    return (Double)left+(Double)right;
                }
                if(left instanceof  String && right instanceof String){
                    return (String)left+(String)right;
                }
            }
            case MINUS -> {
                return (Double)left-(Double) right;
            }
            case SLASH -> {
                return (Double)left/(Double) right;
            }
            case STAR ->  {
                return (Double)left*(Double) right;
            }
        }
        return null;
    }
    
}
