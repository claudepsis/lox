package com.craftinginterpreters.lox;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static com.craftinginterpreters.lox.TokenType.BANG;
import static com.craftinginterpreters.lox.TokenType.COLON;
import static com.craftinginterpreters.lox.TokenType.COMMA;
import static com.craftinginterpreters.lox.TokenType.EOF;
import static com.craftinginterpreters.lox.TokenType.EQUAL_EQUAL;
import static com.craftinginterpreters.lox.TokenType.FALSE;
import static com.craftinginterpreters.lox.TokenType.GREATER;
import static com.craftinginterpreters.lox.TokenType.LEFT_PAREN;
import static com.craftinginterpreters.lox.TokenType.MINUS;
import static com.craftinginterpreters.lox.TokenType.NIL;
import static com.craftinginterpreters.lox.TokenType.NUMBER;
import static com.craftinginterpreters.lox.TokenType.PLUS;
import static com.craftinginterpreters.lox.TokenType.QUESTION_MARK;
import static com.craftinginterpreters.lox.TokenType.RIGHT_PAREN;
import static com.craftinginterpreters.lox.TokenType.SLASH;
import static com.craftinginterpreters.lox.TokenType.STAR;
import static com.craftinginterpreters.lox.TokenType.STRING;
import static com.craftinginterpreters.lox.TokenType.TRUE;

/**
 * Unit tests for Parser class
 */
class ParserTest {

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
    
    private List<Token> createTokens(Token... tokens) {
        return Arrays.asList(tokens);
    }

    private Token createToken(TokenType type, String lexeme, Object literal) {
        return new Token(type, lexeme, literal, 1);
    }

    // ========== Literal Expression Tests ==========

    @Test
    @DisplayName("Test parse literal - Number")
    void testParseLiteral_Number() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "123", 123.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Literal);
        assertEquals(123.0, ((Expr.Literal) expr).value);
    }

    @Test
    @DisplayName("Test parse literal - String")
    void testParseLiteral_String() {
        List<Token> tokens = createTokens(
            createToken(STRING, "\"hello\"", "hello"),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Literal);
        assertEquals("hello", ((Expr.Literal) expr).value);
    }

    @Test
    @DisplayName("Test parse literal - True")
    void testParseLiteral_True() {
        List<Token> tokens = createTokens(
            createToken(TRUE, "true", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Literal);
        assertEquals(true, ((Expr.Literal) expr).value);
    }

    @Test
    @DisplayName("Test parse literal - False")
    void testParseLiteral_False() {
        List<Token> tokens = createTokens(
            createToken(FALSE, "false", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Literal);
        assertEquals(false, ((Expr.Literal) expr).value);
    }

    @Test
    @DisplayName("Test parse literal - Nil")
    void testParseLiteral_Nil() {
        List<Token> tokens = createTokens(
            createToken(NIL, "nil", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Literal);
        assertNull(((Expr.Literal) expr).value);
    }

    // ========== Grouping Expression Tests ==========

    @Test
    @DisplayName("Test parse grouping - Simple")
    void testParseGrouping_Simple() {
        List<Token> tokens = createTokens(
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "123", 123.0),
            createToken(RIGHT_PAREN, ")", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Grouping);
        assertTrue(((Expr.Grouping) expr).expression instanceof Expr.Literal);
    }

    @Test
    @DisplayName("Test parse grouping - Nested")
    void testParseGrouping_Nested() {
        List<Token> tokens = createTokens(
            createToken(LEFT_PAREN, "(", null),
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "123", 123.0),
            createToken(RIGHT_PAREN, ")", null),
            createToken(RIGHT_PAREN, ")", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Grouping);
        assertTrue(((Expr.Grouping) expr).expression instanceof Expr.Grouping);
    }

    // ========== Unary Expression Tests ==========

    @Test
    @DisplayName("Test parse unary - Negation")
    void testParseUnary_Negation() {
        List<Token> tokens = createTokens(
            createToken(MINUS, "-", null),
            createToken(NUMBER, "123", 123.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Unary);
        Expr.Unary unary = (Expr.Unary) expr;
        assertEquals(MINUS, unary.operator.type);
        assertTrue(unary.right instanceof Expr.Literal);
    }

    @Test
    @DisplayName("Test parse unary - Logical NOT")
    void testParseUnary_LogicalNot() {
        List<Token> tokens = createTokens(
            createToken(BANG, "!", null),
            createToken(TRUE, "true", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Unary);
        Expr.Unary unary = (Expr.Unary) expr;
        assertEquals(BANG, unary.operator.type);
    }

    @Test
    @DisplayName("Test parse unary - Multiple negations")
    void testParseUnary_MultipleNegations() {
        List<Token> tokens = createTokens(
            createToken(MINUS, "-", null),
            createToken(MINUS, "-", null),
            createToken(NUMBER, "123", 123.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Unary);
        Expr.Unary outer = (Expr.Unary) expr;
        assertTrue(outer.right instanceof Expr.Unary);
    }

    // ========== Binary Expression Tests ==========

    @Test
    @DisplayName("Test parse binary - Addition")
    void testParseBinary_Addition() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(PLUS, binary.operator.type);
        assertTrue(binary.left instanceof Expr.Literal);
        assertTrue(binary.right instanceof Expr.Literal);
    }

    @Test
    @DisplayName("Test parse binary - Multiplication")
    void testParseBinary_Multiplication() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "3", 3.0),
            createToken(STAR, "*", null),
            createToken(NUMBER, "4", 4.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(STAR, binary.operator.type);
    }

    @Test
    @DisplayName("Test parse binary - Division")
    void testParseBinary_Division() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "10", 10.0),
            createToken(SLASH, "/", null),
            createToken(NUMBER, "2", 2.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(SLASH, binary.operator.type);
    }

    @Test
    @DisplayName("Test parse binary - Comparison")
    void testParseBinary_Comparison() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "5", 5.0),
            createToken(GREATER, ">", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(GREATER, binary.operator.type);
    }

    @Test
    @DisplayName("Test parse binary - Equality")
    void testParseBinary_Equality() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "5", 5.0),
            createToken(EQUAL_EQUAL, "==", null),
            createToken(NUMBER, "5", 5.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(EQUAL_EQUAL, binary.operator.type);
    }

    // ========== Operator Precedence Tests ==========

    @Test
    @DisplayName("Test operator precedence - Multiplication before addition")
    void testOperatorPrecedence_MultiplicationBeforeAddition() {
        // 1 + 2 * 3 should parse as 1 + (2 * 3)
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(STAR, "*", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary addition = (Expr.Binary) expr;
        assertEquals(PLUS, addition.operator.type);
        assertTrue(addition.right instanceof Expr.Binary);
        Expr.Binary multiplication = (Expr.Binary) addition.right;
        assertEquals(STAR, multiplication.operator.type);
    }

    @Test
    @DisplayName("Test operator precedence - Unary before multiplication")
    void testOperatorPrecedence_UnaryBeforeMultiplication() {
        // -2 * 3 should parse as (-2) * 3
        List<Token> tokens = createTokens(
            createToken(MINUS, "-", null),
            createToken(NUMBER, "2", 2.0),
            createToken(STAR, "*", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertEquals(STAR, binary.operator.type);
        assertTrue(binary.left instanceof Expr.Unary);
    }

    @Test
    @DisplayName("Test operator precedence - Parentheses override")
    void testOperatorPrecedence_ParenthesesOverride() {
        // (1 + 2) * 3 should parse as (1 + 2) * 3
        List<Token> tokens = createTokens(
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(RIGHT_PAREN, ")", null),
            createToken(STAR, "*", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary multiplication = (Expr.Binary) expr;
        assertEquals(STAR, multiplication.operator.type);
        assertTrue(multiplication.left instanceof Expr.Grouping);
    }

    // ========== Comma Expression Tests ==========

    @Test
    @DisplayName("Test parse comma - Simple")
    void testParseComma_Simple() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(COMMA, ",", null),
            createToken(NUMBER, "2", 2.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Comma);
        Expr.Comma comma = (Expr.Comma) expr;
        assertTrue(comma.left instanceof Expr.Literal);
        assertTrue(comma.right instanceof Expr.Literal);
    }

    @Test
    @DisplayName("Test parse comma - Multiple")
    void testParseComma_Multiple() {
        // 1, 2, 3 should parse as (1, 2), 3
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(COMMA, ",", null),
            createToken(NUMBER, "2", 2.0),
            createToken(COMMA, ",", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Comma);
        Expr.Comma outer = (Expr.Comma) expr;
        assertTrue(outer.left instanceof Expr.Comma);
        assertTrue(outer.right instanceof Expr.Literal);
    }

    // ========== Ternary Expression Tests ==========

    @Test
    @DisplayName("Test parse ternary - Simple")
    void testParseTernary_Simple() {
        // true ? 1 : 2
        List<Token> tokens = createTokens(
            createToken(TRUE, "true", null),
            createToken(QUESTION_MARK, "?", null),
            createToken(NUMBER, "1", 1.0),
            createToken(COLON, ":", null),
            createToken(NUMBER, "2", 2.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Ternary);
        Expr.Ternary ternary = (Expr.Ternary) expr;
        assertTrue(ternary.condition instanceof Expr.Literal);
        assertTrue(ternary.left instanceof Expr.Literal);
        assertTrue(ternary.right instanceof Expr.Literal);
    }

    @Test
    @DisplayName("Test parse ternary - Nested")
    void testParseTernary_Nested() {
        // true ? false ? 1 : 2 : 3
        List<Token> tokens = createTokens(
            createToken(TRUE, "true", null),
            createToken(QUESTION_MARK, "?", null),
            createToken(FALSE, "false", null),
            createToken(QUESTION_MARK, "?", null),
            createToken(NUMBER, "1", 1.0),
            createToken(COLON, ":", null),
            createToken(NUMBER, "2", 2.0),
            createToken(COLON, ":", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Ternary);
        Expr.Ternary outer = (Expr.Ternary) expr;
        assertTrue(outer.left instanceof Expr.Ternary);
    }

    // ========== Complex Expression Tests ==========

    @Test
    @DisplayName("Test parse complex - Mixed operators")
    void testParseComplex_MixedOperators() {
        // 1 + 2 * 3 - 4
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(STAR, "*", null),
            createToken(NUMBER, "3", 3.0),
            createToken(MINUS, "-", null),
            createToken(NUMBER, "4", 4.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
    }

    @Test
    @DisplayName("Test parse complex - With grouping and unary")
    void testParseComplex_WithGroupingAndUnary() {
        // -(1 + 2) * 3
        List<Token> tokens = createTokens(
            createToken(MINUS, "-", null),
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(RIGHT_PAREN, ")", null),
            createToken(STAR, "*", null),
            createToken(NUMBER, "3", 3.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        assertNotNull(expr);
        assertTrue(expr instanceof Expr.Binary);
        Expr.Binary binary = (Expr.Binary) expr;
        assertTrue(binary.left instanceof Expr.Unary);
    }

    // ========== Error Handling Tests ==========

    @Test
    @DisplayName("Test parse error - Missing right operand")
    void testParseError_MissingRightOperand() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        // Should return null or throw error depending on implementation
        // Adjust based on your error handling strategy
    }

    @Test
    @DisplayName("Test parse error - Unmatched parenthesis")
    void testParseError_UnmatchedParenthesis() {
        List<Token> tokens = createTokens(
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "1", 1.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        // Should handle error appropriately
    }

    // ========== Integration Tests with AstPrinter ==========

    @Test
    @DisplayName("Test parse and print - Simple expression")
    void testParseAndPrint_SimpleExpression() {
        List<Token> tokens = createTokens(
            createToken(NUMBER, "1", 1.0),
            createToken(PLUS, "+", null),
            createToken(NUMBER, "2", 2.0),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        AstPrinter printer = new AstPrinter();
        String result = printer.print(expr);
        
        assertEquals("(+ 1.0 2.0)", result);
    }

    @Test
    @DisplayName("Test parse and print - Complex expression")
    void testParseAndPrint_ComplexExpression() {
        // -123 * (45.67)
        List<Token> tokens = createTokens(
            createToken(MINUS, "-", null),
            createToken(NUMBER, "123", 123.0),
            createToken(STAR, "*", null),
            createToken(LEFT_PAREN, "(", null),
            createToken(NUMBER, "45.67", 45.67),
            createToken(RIGHT_PAREN, ")", null),
            createToken(EOF, "", null)
        );
        
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        
        AstPrinter printer = new AstPrinter();
        String result = printer.print(expr);
        
        assertEquals("(* (- 123.0) (group 45.67))", result);
    }
}
