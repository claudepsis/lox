package com.craftinginterpreters.lox;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static com.craftinginterpreters.lox.TokenType.BANG;
import static com.craftinginterpreters.lox.TokenType.COMMA;
import static com.craftinginterpreters.lox.TokenType.MINUS;
import static com.craftinginterpreters.lox.TokenType.PLUS;
import static com.craftinginterpreters.lox.TokenType.SLASH;
import static com.craftinginterpreters.lox.TokenType.STAR;

/**
 * Unit tests for Interpreter class
 */
class InterpreterTest {

    static {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("✅ " + testInfo.getDisplayName() + " - PASSED");
    }

    // ========== Helper Methods ==========
    
    private Token createToken(TokenType type, String lexeme, Object literal) {
        return new Token(type, lexeme, literal, 1);
    }

    // ========== Literal Expression Tests ==========

    @Test
    @DisplayName("Test evaluate literal - Number")
    void testEvaluateLiteral_Number() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal expr = new Expr.Literal(123.0);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(123.0, result);
    }

    @Test
    @DisplayName("Test evaluate literal - String")
    void testEvaluateLiteral_String() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal expr = new Expr.Literal("hello");
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals("hello", result);
    }

    @Test
    @DisplayName("Test evaluate literal - Boolean true")
    void testEvaluateLiteral_BooleanTrue() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal expr = new Expr.Literal(true);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("Test evaluate literal - Boolean false")
    void testEvaluateLiteral_BooleanFalse() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal expr = new Expr.Literal(false);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(false, result);
    }

    @Test
    @DisplayName("Test evaluate literal - Nil")
    void testEvaluateLiteral_Nil() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal expr = new Expr.Literal(null);
        
        Object result = interpreter.evaluate(expr);
        
        assertNull(result);
    }

    // ========== Unary Expression Tests ==========

    @Test
    @DisplayName("Test evaluate unary - Minus")
    void testEvaluateUnary_Minus() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(MINUS, "-", null);
        Expr.Literal operand = new Expr.Literal(123.0);
        Expr.Unary expr = new Expr.Unary(operator, operand);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(-123.0, result);
    }

    @Test
    @DisplayName("Test evaluate unary - Bang with true")
    void testEvaluateUnary_BangTrue() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(BANG, "!", null);
        Expr.Literal operand = new Expr.Literal(true);
        Expr.Unary expr = new Expr.Unary(operator, operand);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(false, result);
    }

    @Test
    @DisplayName("Test evaluate unary - Bang with false")
    void testEvaluateUnary_BangFalse() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(BANG, "!", null);
        Expr.Literal operand = new Expr.Literal(false);
        Expr.Unary expr = new Expr.Unary(operator, operand);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("Test evaluate unary - Bang with nil")
    void testEvaluateUnary_BangNil() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(BANG, "!", null);
        Expr.Literal operand = new Expr.Literal(null);
        Expr.Unary expr = new Expr.Unary(operator, operand);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("Test evaluate unary - Bang with number (truthy)")
    void testEvaluateUnary_BangNumber() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(BANG, "!", null);
        Expr.Literal operand = new Expr.Literal(123.0);
        Expr.Unary expr = new Expr.Unary(operator, operand);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(false, result);
    }

    // ========== Binary Expression Tests ==========

    @Test
    @DisplayName("Test evaluate binary - Addition (numbers)")
    void testEvaluateBinary_AdditionNumbers() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(PLUS, "+", null);
        Expr.Literal left = new Expr.Literal(10.0);
        Expr.Literal right = new Expr.Literal(20.0);
        Expr.Binary expr = new Expr.Binary(left, operator, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(30.0, result);
    }

    @Test
    @DisplayName("Test evaluate binary - Addition (strings)")
    void testEvaluateBinary_AdditionStrings() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(PLUS, "+", null);
        Expr.Literal left = new Expr.Literal("Hello, ");
        Expr.Literal right = new Expr.Literal("World!");
        Expr.Binary expr = new Expr.Binary(left, operator, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals("Hello, World!", result);
    }

    @Test
    @DisplayName("Test evaluate binary - Subtraction")
    void testEvaluateBinary_Subtraction() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(MINUS, "-", null);
        Expr.Literal left = new Expr.Literal(50.0);
        Expr.Literal right = new Expr.Literal(20.0);
        Expr.Binary expr = new Expr.Binary(left, operator, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(30.0, result);
    }

    @Test
    @DisplayName("Test evaluate binary - Multiplication")
    void testEvaluateBinary_Multiplication() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(STAR, "*", null);
        Expr.Literal left = new Expr.Literal(5.0);
        Expr.Literal right = new Expr.Literal(6.0);
        Expr.Binary expr = new Expr.Binary(left, operator, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(30.0, result);
    }

    @Test
    @DisplayName("Test evaluate binary - Division")
    void testEvaluateBinary_Division() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(SLASH, "/", null);
        Expr.Literal left = new Expr.Literal(60.0);
        Expr.Literal right = new Expr.Literal(2.0);
        Expr.Binary expr = new Expr.Binary(left, operator, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(30.0, result);
    }

    // ========== Grouping Expression Tests ==========

    @Test
    @DisplayName("Test evaluate grouping - Simple")
    void testEvaluateGrouping_Simple() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal inner = new Expr.Literal(42.0);
        Expr.Grouping expr = new Expr.Grouping(inner);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(42.0, result);
    }

    @Test
    @DisplayName("Test evaluate grouping - With binary expression")
    void testEvaluateGrouping_WithBinary() {
        Interpreter interpreter = new Interpreter();
        Token operator = createToken(PLUS, "+", null);
        Expr.Literal left = new Expr.Literal(10.0);
        Expr.Literal right = new Expr.Literal(20.0);
        Expr.Binary binary = new Expr.Binary(left, operator, right);
        Expr.Grouping expr = new Expr.Grouping(binary);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(30.0, result);
    }

    // ========== Comma Expression Tests ==========
@Test
@DisplayName("Test evaluate comma - Returns right value")
void testEvaluateComma_ReturnsRight() {
    Interpreter interpreter = new Interpreter();
    Token commaToken = createToken(COMMA, ",", null);
    Expr.Literal left = new Expr.Literal(10.0);
    Expr.Literal right = new Expr.Literal(20.0);
    Expr.Comma expr = new Expr.Comma(left, commaToken, right);
    
    Object result = interpreter.evaluate(expr);
    
    assertNotNull(result);
    assertEquals(20.0, result);
}

@Test
@DisplayName("Test evaluate comma - Multiple comma expressions")
void testEvaluateComma_Multiple() {
    Interpreter interpreter = new Interpreter();
    Token commaToken1 = createToken(COMMA, ",", null);
    Token commaToken2 = createToken(COMMA, ",", null);
    
    // 10, 20, 30 (should return 30)
    Expr.Literal ten = new Expr.Literal(10.0);
    Expr.Literal twenty = new Expr.Literal(20.0);
    Expr.Literal thirty = new Expr.Literal(30.0);
    
    Expr.Comma firstComma = new Expr.Comma(ten, commaToken1, twenty);
    Expr.Comma expr = new Expr.Comma(firstComma, commaToken2, thirty);
    
    Object result = interpreter.evaluate(expr);
    
    assertNotNull(result);
    assertEquals(30.0, result);
}

@Test
@DisplayName("Test evaluate comma - With expressions")
void testEvaluateComma_WithExpressions() {
    Interpreter interpreter = new Interpreter();
    Token commaToken = createToken(COMMA, ",", null);
    Token plusOp = createToken(PLUS, "+", null);
    Token multOp = createToken(STAR, "*", null);
    
    // (1 + 2), (3 * 4) should return 12
    Expr.Literal one = new Expr.Literal(1.0);
    Expr.Literal two = new Expr.Literal(2.0);
    Expr.Binary leftExpr = new Expr.Binary(one, plusOp, two);
    
    Expr.Literal three = new Expr.Literal(3.0);
    Expr.Literal four = new Expr.Literal(4.0);
    Expr.Binary rightExpr = new Expr.Binary(three, multOp, four);
    
    Expr.Comma expr = new Expr.Comma(leftExpr, commaToken, rightExpr);
    
    Object result = interpreter.evaluate(expr);
    
    assertNotNull(result);
    assertEquals(12.0, result);
}

    // ========== Ternary Expression Tests ==========

    @Test
    @DisplayName("Test evaluate ternary - Condition true")
    void testEvaluateTernary_ConditionTrue() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal condition = new Expr.Literal(true);
        Expr.Literal left = new Expr.Literal(100.0);
        Expr.Literal right = new Expr.Literal(200.0);
        Expr.Ternary expr = new Expr.Ternary(condition, left, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(100.0, result);
    }

    @Test
    @DisplayName("Test evaluate ternary - Condition false")
    void testEvaluateTernary_ConditionFalse() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal condition = new Expr.Literal(false);
        Expr.Literal left = new Expr.Literal(100.0);
        Expr.Literal right = new Expr.Literal(200.0);
        Expr.Ternary expr = new Expr.Ternary(condition, left, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(200.0, result);
    }

    @Test
    @DisplayName("Test evaluate ternary - Condition nil (falsy)")
    void testEvaluateTernary_ConditionNil() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal condition = new Expr.Literal(null);
        Expr.Literal left = new Expr.Literal(100.0);
        Expr.Literal right = new Expr.Literal(200.0);
        Expr.Ternary expr = new Expr.Ternary(condition, left, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(200.0, result);
    }

    @Test
    @DisplayName("Test evaluate ternary - Condition number (truthy)")
    void testEvaluateTernary_ConditionNumber() {
        Interpreter interpreter = new Interpreter();
        Expr.Literal condition = new Expr.Literal(42.0);
        Expr.Literal left = new Expr.Literal(100.0);
        Expr.Literal right = new Expr.Literal(200.0);
        Expr.Ternary expr = new Expr.Ternary(condition, left, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(100.0, result);
    }

    // ========== Complex Expression Tests ==========

    @Test
    @DisplayName("Test evaluate complex - (10 + 20) * 3")
    void testEvaluateComplex_GroupedMultiplication() {
        Interpreter interpreter = new Interpreter();
        
        // (10 + 20)
        Token plusOp = createToken(PLUS, "+", null);
        Expr.Literal ten = new Expr.Literal(10.0);
        Expr.Literal twenty = new Expr.Literal(20.0);
        Expr.Binary addition = new Expr.Binary(ten, plusOp, twenty);
        Expr.Grouping grouped = new Expr.Grouping(addition);
        
        // (10 + 20) * 3
        Token multOp = createToken(STAR, "*", null);
        Expr.Literal three = new Expr.Literal(3.0);
        Expr.Binary expr = new Expr.Binary(grouped, multOp, three);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(90.0, result);
    }

    @Test
    @DisplayName("Test evaluate complex - -(10 + 5)")
    void testEvaluateComplex_NegatedGrouping() {
        Interpreter interpreter = new Interpreter();
        
        // 10 + 5
        Token plusOp = createToken(PLUS, "+", null);
        Expr.Literal ten = new Expr.Literal(10.0);
        Expr.Literal five = new Expr.Literal(5.0);
        Expr.Binary addition = new Expr.Binary(ten, plusOp, five);
        Expr.Grouping grouped = new Expr.Grouping(addition);
        
        // -(10 + 5)
        Token minusOp = createToken(MINUS, "-", null);
        Expr.Unary expr = new Expr.Unary(minusOp, grouped);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(-15.0, result);
    }

    @Test
    @DisplayName("Test evaluate complex - true ? 10 + 5 : 20 * 2")
    void testEvaluateComplex_TernaryWithBinary() {
        Interpreter interpreter = new Interpreter();
        
        // Condition: true
        Expr.Literal condition = new Expr.Literal(true);
        
        // Left: 10 + 5
        Token plusOp = createToken(PLUS, "+", null);
        Expr.Literal ten = new Expr.Literal(10.0);
        Expr.Literal five = new Expr.Literal(5.0);
        Expr.Binary left = new Expr.Binary(ten, plusOp, five);
        
        // Right: 20 * 2
        Token multOp = createToken(STAR, "*", null);
        Expr.Literal twenty = new Expr.Literal(20.0);
        Expr.Literal two = new Expr.Literal(2.0);
        Expr.Binary right = new Expr.Binary(twenty, multOp, two);
        
        // Ternary
        Expr.Ternary expr = new Expr.Ternary(condition, left, right);
        
        Object result = interpreter.evaluate(expr);
        
        assertNotNull(result);
        assertEquals(15.0, result);
    }

    // ========== isTruthy Tests ==========

    @Test
    @DisplayName("Test isTruthy - null is falsy")
    void testIsTruthy_Null() {
        Interpreter interpreter = new Interpreter();
        assertFalse(interpreter.isTruthy(null));
    }

    @Test
    @DisplayName("Test isTruthy - false is falsy")
    void testIsTruthy_False() {
        Interpreter interpreter = new Interpreter();
        assertFalse(interpreter.isTruthy(false));
    }

    @Test
    @DisplayName("Test isTruthy - true is truthy")
    void testIsTruthy_True() {
        Interpreter interpreter = new Interpreter();
        assertTrue(interpreter.isTruthy(true));
    }

    @Test
    @DisplayName("Test isTruthy - number is truthy")
    void testIsTruthy_Number() {
        Interpreter interpreter = new Interpreter();
        assertTrue(interpreter.isTruthy(42.0));
        assertTrue(interpreter.isTruthy(0.0));
    }

    @Test
    @DisplayName("Test isTruthy - string is truthy")
    void testIsTruthy_String() {
        Interpreter interpreter = new Interpreter();
        assertTrue(interpreter.isTruthy("hello"));
        assertTrue(interpreter.isTruthy(""));
    }
}

