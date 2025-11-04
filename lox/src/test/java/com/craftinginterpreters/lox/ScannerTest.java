package com.craftinginterpreters.lox;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static com.craftinginterpreters.lox.TokenType.*;

/**
 * Unit tests for Scanner class
 */
class ScannerTest {


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

    // ========== Helper Method Tests ==========
    @Test
    @DisplayName("Test isAtEnd() - Not at end")
    void testIsAtEnd_NotAtEnd() throws Exception {
        Scanner scanner = new Scanner("test");
        Method isAtEndMethod = Scanner.class.getDeclaredMethod("isAtEnd");
        isAtEndMethod.setAccessible(true);
        
        assertFalse((Boolean) isAtEndMethod.invoke(scanner));
    }

    @Test
    @DisplayName("Test isAtEnd() - At end")
    void testIsAtEnd_AtEnd() throws Exception {
        Scanner scanner = new Scanner("");
        Method isAtEndMethod = Scanner.class.getDeclaredMethod("isAtEnd");
        isAtEndMethod.setAccessible(true);
        
        assertTrue((Boolean) isAtEndMethod.invoke(scanner));
    }

    @Test
    @DisplayName("Test isDigit() - Valid digits")
    void testIsDigit_ValidDigits() throws Exception {
        Scanner scanner = new Scanner("");
        Method isDigitMethod = Scanner.class.getDeclaredMethod("isDigit", char.class);
        isDigitMethod.setAccessible(true);
        
        assertTrue((Boolean) isDigitMethod.invoke(scanner, '0'));
        assertTrue((Boolean) isDigitMethod.invoke(scanner, '5'));
        assertTrue((Boolean) isDigitMethod.invoke(scanner, '9'));
    }

    @Test
    @DisplayName("Test isDigit() - Invalid characters")
    void testIsDigit_InvalidCharacters() throws Exception {
        Scanner scanner = new Scanner("");
        Method isDigitMethod = Scanner.class.getDeclaredMethod("isDigit", char.class);
        isDigitMethod.setAccessible(true);
        
        assertFalse((Boolean) isDigitMethod.invoke(scanner, 'a'));
        assertFalse((Boolean) isDigitMethod.invoke(scanner, 'Z'));
        assertFalse((Boolean) isDigitMethod.invoke(scanner, ' '));
    }

    @Test
    @DisplayName("Test isAlpha() - Valid characters")
    void testIsAlpha_ValidCharacters() throws Exception {
        Scanner scanner = new Scanner("");
        Method isAlphaMethod = Scanner.class.getDeclaredMethod("isAlpha", char.class);
        isAlphaMethod.setAccessible(true);
        
        assertTrue((Boolean) isAlphaMethod.invoke(scanner, 'a'));
        assertTrue((Boolean) isAlphaMethod.invoke(scanner, 'Z'));
        assertTrue((Boolean) isAlphaMethod.invoke(scanner, '_'));
    }

    @Test
    @DisplayName("Test isAlpha() - Invalid characters")
    void testIsAlpha_InvalidCharacters() throws Exception {
        Scanner scanner = new Scanner("");
        Method isAlphaMethod = Scanner.class.getDeclaredMethod("isAlpha", char.class);
        isAlphaMethod.setAccessible(true);
        
        assertFalse((Boolean) isAlphaMethod.invoke(scanner, '0'));
        assertFalse((Boolean) isAlphaMethod.invoke(scanner, '9'));
        assertFalse((Boolean) isAlphaMethod.invoke(scanner, ' '));
    }

    @Test
    @DisplayName("Test isAlphaNumeric() - Valid characters")
    void testIsAlphaNumeric_ValidCharacters() throws Exception {
        Scanner scanner = new Scanner("");
        Method isAlphaNumericMethod = Scanner.class.getDeclaredMethod("isAlphaNumeric", char.class);
        isAlphaNumericMethod.setAccessible(true);
        
        assertTrue((Boolean) isAlphaNumericMethod.invoke(scanner, 'a'));
        assertTrue((Boolean) isAlphaNumericMethod.invoke(scanner, 'Z'));
        assertTrue((Boolean) isAlphaNumericMethod.invoke(scanner, '0'));
        assertTrue((Boolean) isAlphaNumericMethod.invoke(scanner, '9'));
        assertTrue((Boolean) isAlphaNumericMethod.invoke(scanner, '_'));
    }

    // ========== Single Character Token Tests ==========

    @Test
    @DisplayName("Test single character tokens - Parentheses")
    void testSingleCharacterTokens_Parentheses() {
        Scanner scanner = new Scanner("()");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(RIGHT_PAREN, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test single character tokens - Braces")
    void testSingleCharacterTokens_Braces() {
        Scanner scanner = new Scanner("{}");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(LEFT_BRACE, tokens.get(0).type);
        assertEquals(RIGHT_BRACE, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test single character tokens - Operators")
    void testSingleCharacterTokens_Operators() {
        Scanner scanner = new Scanner("+-*,;.");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(7, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(STAR, tokens.get(2).type);
        assertEquals(COMMA, tokens.get(3).type);
        assertEquals(SEMICOLON, tokens.get(4).type);
        assertEquals(DOT, tokens.get(5).type);
        assertEquals(EOF, tokens.get(6).type);
    }

    // ========== Two Character Token Tests ==========

    @Test
    @DisplayName("Test two character tokens - Equal and Equal Equal")
    void testTwoCharacterTokens_Equal() {
        Scanner scanner = new Scanner("= ==");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(EQUAL, tokens.get(0).type);
        assertEquals(EQUAL_EQUAL, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test two character tokens - Bang and Bang Equal")
    void testTwoCharacterTokens_Bang() {
        Scanner scanner = new Scanner("! !=");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(BANG, tokens.get(0).type);
        assertEquals(BANG_EQUAL, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test two character tokens - Less and Less Equal")
    void testTwoCharacterTokens_Less() {
        Scanner scanner = new Scanner("< <=");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(LESS, tokens.get(0).type);
        assertEquals(LESS_EQUAL, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test two character tokens - Greater and Greater Equal")
    void testTwoCharacterTokens_Greater() {
        Scanner scanner = new Scanner("> >=");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(GREATER, tokens.get(0).type);
        assertEquals(GREATER_EQUAL, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    // ========== Comment Tests ==========

    @Test
    @DisplayName("Test division operator")
    void testDivision() {
        Scanner scanner = new Scanner("/ 2");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(SLASH, tokens.get(0).type);
        assertEquals(NUMBER, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test single line comment")
    void testSingleLineComment() {
        Scanner scanner = new Scanner("// this is a comment\n+");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(EOF, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test comment at end of file")
    void testCommentAtEndOfFile() {
        Scanner scanner = new Scanner("+ // comment");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(EOF, tokens.get(1).type);
    }

    // ========== Whitespace Tests ==========

    @Test
    @DisplayName("Test whitespace handling - Spaces")
    void testWhitespace_Spaces() {
        Scanner scanner = new Scanner("  +  -  ");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test whitespace handling - Tabs and carriage returns")
    void testWhitespace_TabsAndCarriageReturns() {
        Scanner scanner = new Scanner("\t+\r-");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(EOF, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test whitespace handling - Newlines and line numbers")
    void testWhitespace_Newlines() {
        Scanner scanner = new Scanner("+\n-\n*");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(4, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(1, tokens.get(0).line);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(2, tokens.get(1).line);
        assertEquals(STAR, tokens.get(2).type);
        assertEquals(3, tokens.get(2).line);
    }

    // ========== String Literal Tests ==========

    @Test
    @DisplayName("Test simple string")
    void testString_Simple() {
        Scanner scanner = new Scanner("\"hello\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("hello", tokens.get(0).literal);
        assertEquals("\"hello\"", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test empty string")
    void testString_Empty() {
        Scanner scanner = new Scanner("\"\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("", tokens.get(0).literal);
    }

    @Test
    @DisplayName("Test multiline string")
    void testString_Multiline() {
        Scanner scanner = new Scanner("\"hello\nworld\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("hello\nworld", tokens.get(0).literal);
        assertEquals(2, tokens.get(0).line); // String ends on line 2
    }

    @Test
    @DisplayName("Test unterminated string")
    void testString_Unterminated() {
        Scanner scanner = new Scanner("\"hello");
        List<Token> tokens = scanner.scanTokens();
        
        // Should only have EOF since unterminated string reports error but doesn't add token
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

    // ========== Number Literal Tests ==========

    @Test
    @DisplayName("Test integer number")
    void testNumber_Integer() {
        Scanner scanner = new Scanner("123");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.0, tokens.get(0).literal);
        assertEquals("123", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test floating point number")
    void testNumber_Float() {
        Scanner scanner = new Scanner("123.456");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.456, tokens.get(0).literal);
        assertEquals("123.456", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test number with leading dot (should be processed separately)")
    void testNumber_LeadingDot() {
        Scanner scanner = new Scanner(".123");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(DOT, tokens.get(0).type);
        assertEquals(NUMBER, tokens.get(1).type);
        assertEquals(123.0, tokens.get(1).literal);
    }

    @Test
    @DisplayName("Test number with trailing dot (should be processed separately)")
    void testNumber_TrailingDot() {
        Scanner scanner = new Scanner("123.");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.0, tokens.get(0).literal);
        assertEquals(DOT, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test zero")
    void testNumber_Zero() {
        Scanner scanner = new Scanner("0");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(0.0, tokens.get(0).literal);
    }

    // ========== Identifier and Keyword Tests ==========

    @Test
    @DisplayName("Test simple identifier")
    void testIdentifier_Simple() {
        Scanner scanner = new Scanner("foo");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("foo", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test identifier with underscore")
    void testIdentifier_WithUnderscore() {
        Scanner scanner = new Scanner("foo_bar");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("foo_bar", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test identifier with numbers")
    void testIdentifier_WithNumbers() {
        Scanner scanner = new Scanner("var123");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("var123", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test keyword - var")
    void testKeyword_Var() {
        Scanner scanner = new Scanner("var");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(VAR, tokens.get(0).type);
        assertEquals("var", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test keywords - if else")
    void testKeyword_IfElse() {
        Scanner scanner = new Scanner("if else");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(IF, tokens.get(0).type);
        assertEquals(ELSE, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test keywords - while for")
    void testKeyword_WhileFor() {
        Scanner scanner = new Scanner("while for");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(WHILE, tokens.get(0).type);
        assertEquals(FOR, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test keywords - true false nil")
    void testKeyword_Literals() {
        Scanner scanner = new Scanner("true false nil");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(4, tokens.size());
        assertEquals(TRUE, tokens.get(0).type);
        assertEquals(FALSE, tokens.get(1).type);
        assertEquals(NIL, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test keywords - fun return")
    void testKeyword_Function() {
        Scanner scanner = new Scanner("fun return");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(FUN, tokens.get(0).type);
        assertEquals(RETURN, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test keywords - class this super")
    void testKeyword_Class() {
        Scanner scanner = new Scanner("class this super");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(4, tokens.size());
        assertEquals(CLASS, tokens.get(0).type);
        assertEquals(THIS, tokens.get(1).type);
        assertEquals(SUPER, tokens.get(2).type);
    }

    @Test
    @DisplayName("Test keywords - and or")
    void testKeyword_Logical() {
        Scanner scanner = new Scanner("and or");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(AND, tokens.get(0).type);
        assertEquals(OR, tokens.get(1).type);
    }

    @Test
    @DisplayName("Test keyword - print")
    void testKeyword_Print() {
        Scanner scanner = new Scanner("print");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PRINT, tokens.get(0).type);
    }

    @Test
    @DisplayName("Test identifiers similar to keywords")
    void testIdentifier_SimilarToKeyword() {
        Scanner scanner = new Scanner("variable ifelse");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("variable", tokens.get(0).lexeme);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("ifelse", tokens.get(1).lexeme);
    }

    // ========== Complex Tests ==========

    @Test
    @DisplayName("Complex test - Variable declaration")
    void testComplex_VariableDeclaration() {
        Scanner scanner = new Scanner("var x = 10;");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(6, tokens.size());
        assertEquals(VAR, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("x", tokens.get(1).lexeme);
        assertEquals(EQUAL, tokens.get(2).type);
        assertEquals(NUMBER, tokens.get(3).type);
        assertEquals(10.0, tokens.get(3).literal);
        assertEquals(SEMICOLON, tokens.get(4).type);
        assertEquals(EOF, tokens.get(5).type);
    }

    @Test
    @DisplayName("Complex test - Expression")
    void testComplex_Expression() {
        Scanner scanner = new Scanner("1 + 2 * 3");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(6, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(1.0, tokens.get(0).literal);
        assertEquals(PLUS, tokens.get(1).type);
        assertEquals(NUMBER, tokens.get(2).type);
        assertEquals(2.0, tokens.get(2).literal);
        assertEquals(STAR, tokens.get(3).type);
        assertEquals(NUMBER, tokens.get(4).type);
        assertEquals(3.0, tokens.get(4).literal);
    }

    @Test
    @DisplayName("Complex test - Comparison expression")
    void testComplex_Comparison() {
        Scanner scanner = new Scanner("x >= 10 and y != 5");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(8, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals(GREATER_EQUAL, tokens.get(1).type);
        assertEquals(NUMBER, tokens.get(2).type);
        assertEquals(AND, tokens.get(3).type);
        assertEquals(IDENTIFIER, tokens.get(4).type);
        assertEquals(BANG_EQUAL, tokens.get(5).type);
        assertEquals(NUMBER, tokens.get(6).type);
    }

    @Test
    @DisplayName("Complex test - If statement")
    void testComplex_IfStatement() {
        Scanner scanner = new Scanner("if (x > 0) { print x; }");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(12, tokens.size());
        assertEquals(IF, tokens.get(0).type);
        assertEquals(LEFT_PAREN, tokens.get(1).type);
        assertEquals(IDENTIFIER, tokens.get(2).type);
        assertEquals(GREATER, tokens.get(3).type);
        assertEquals(NUMBER, tokens.get(4).type);
        assertEquals(RIGHT_PAREN, tokens.get(5).type);
        assertEquals(LEFT_BRACE, tokens.get(6).type);
        assertEquals(PRINT, tokens.get(7).type);
        assertEquals(IDENTIFIER, tokens.get(8).type);
        assertEquals(SEMICOLON, tokens.get(9).type);
        assertEquals(RIGHT_BRACE, tokens.get(10).type);
        assertEquals(EOF, tokens.get(11).type);
    }

    @Test
    @DisplayName("Complex test - Function definition")
    void testComplex_FunctionDefinition() {
        Scanner scanner = new Scanner("fun add(a, b) { return a + b; }");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(15, tokens.size());
        assertEquals(FUN, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("add", tokens.get(1).lexeme);
        assertEquals(LEFT_PAREN, tokens.get(2).type);
        assertEquals(IDENTIFIER, tokens.get(3).type);
        assertEquals(COMMA, tokens.get(4).type);
        assertEquals(IDENTIFIER, tokens.get(5).type);
        assertEquals(RIGHT_PAREN, tokens.get(6).type);
        assertEquals(LEFT_BRACE, tokens.get(7).type);
        assertEquals(RETURN, tokens.get(8).type);
    }

    @Test
    @DisplayName("Complex test - Multiline program")
    void testComplex_MultilineProgram() {
        String source = "var x = 10;\n" +
                       "var y = 20;\n" +
                       "print x + y;";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        
        // var x = 10 ; var y = 20 ; print x + y ; EOF
        assertEquals(16, tokens.size());
        
        // Check line numbers
        assertEquals(1, tokens.get(0).line); // var
        assertEquals(1, tokens.get(4).line); // ;
        assertEquals(2, tokens.get(5).line); // var
        assertEquals(2, tokens.get(9).line); // ;
        assertEquals(3, tokens.get(10).line); // print
    }

    @Test
    @DisplayName("Complex test - Strings and comments mixed")
    void testComplex_StringsAndComments() {
        String source = "// This is a comment\n" +
                       "var name = \"Alice\";\n" +
                       "// Another comment\n" +
                       "print name;";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        
        // var name = "Alice" ; print name ; EOF
        assertEquals(9, tokens.size());
        assertEquals(VAR, tokens.get(0).type);
        assertEquals(EQUAL, tokens.get(2).type);
        assertEquals("Alice", tokens.get(3).literal);
        assertEquals(EOF,tokens.get(8).type);
        assertEquals(null,tokens.get(8).literal);
    }

    @Test
    @DisplayName("Test empty source code")
    void testEmpty_Source() {
        Scanner scanner = new Scanner("");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

    @Test
    @DisplayName("Test source with only whitespace")
    void testEmpty_OnlyWhitespace() {
        Scanner scanner = new Scanner("   \n\t\r  ");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

    @Test
    @DisplayName("Test source with only comments")
    void testEmpty_OnlyComments() {
        Scanner scanner = new Scanner("// just a comment");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

        // ========== 边界情况和错误处理测试 ==========

    @Test
    @DisplayName("Test unexpected character - @")
    void testUnexpectedCharacter_AtSign() {
        Scanner scanner = new Scanner("@");
        List<Token> tokens = scanner.scanTokens();
        
        // 应该只有 EOF，@ 是非法字符
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

    // @Test
    // @DisplayName("Test unexpected character - #")
    // void testUnexpectedCharacter_Hash() {
    //     Scanner scanner = new Scanner("var x = 10 # comment?");
    //     List<Token> tokens = scanner.scanTokens();
        
    //     // var x = 10 然后遇到非法字符 #
    //     assertEquals(5, tokens.size()); // var, x, =, 10, EOF
    //     assertEquals(VAR, tokens.get(0).type);
    // }

    @Test
    @DisplayName("Test multiple unexpected characters")
    void testUnexpectedCharacter_Multiple() {
        Scanner scanner = new Scanner("+ @ - $ *");
        List<Token> tokens = scanner.scanTokens();
        
        // 应该有 + - * EOF，@ 和 $ 被跳过
        assertEquals(4, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(STAR, tokens.get(2).type);
    }

    // ========== 数字边界测试 ==========

    @Test
    @DisplayName("Test number - Very large number")
    void testNumber_VeryLarge() {
        Scanner scanner = new Scanner("999999999999.999999");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(999999999999.999999, tokens.get(0).literal);
    }

    @Test
    @DisplayName("Test number - Multiple decimals (should handle first only)")
    void testNumber_MultipleDecimals() {
        Scanner scanner = new Scanner("123.456.789");
        List<Token> tokens = scanner.scanTokens();
        
        // 应该是 123.456 然后 . 然后 789
        assertEquals(4, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.456, tokens.get(0).literal);
        assertEquals(DOT, tokens.get(1).type);
        assertEquals(NUMBER, tokens.get(2).type);
        assertEquals(789.0, tokens.get(2).literal);
    }

    @Test
    @DisplayName("Test number - Leading zeros")
    void testNumber_LeadingZeros() {
        Scanner scanner = new Scanner("007 00.5");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(7.0, tokens.get(0).literal);
        assertEquals(NUMBER, tokens.get(1).type);
        assertEquals(0.5, tokens.get(1).literal);
    }

    // ========== 字符串边界测试 ==========

    @Test
    @DisplayName("Test string - With special characters")
    void testString_SpecialCharacters() {
        Scanner scanner = new Scanner("\"hello!@#$%^&*()world\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("hello!@#$%^&*()world", tokens.get(0).literal);
    }

    @Test
    @DisplayName("Test string - With escape-like sequences")
    void testString_EscapeLike() {
        Scanner scanner = new Scanner("\"hello\\nworld\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        // 注意：简单的 Lox scanner 不处理转义，所以是字面的 \n
        assertEquals("hello\\nworld", tokens.get(0).literal);
    }

    @Test
    @DisplayName("Test string - Multiple strings")
    void testString_Multiple() {
        Scanner scanner = new Scanner("\"first\" + \"second\"");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(4, tokens.size());
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("first", tokens.get(0).literal);
        assertEquals(PLUS, tokens.get(1).type);
        assertEquals(STRING, tokens.get(2).type);
        assertEquals("second", tokens.get(2).literal);
    }

    @Test
    @DisplayName("Test string - Unterminated multiline")
    void testString_UnterminatedMultiline() {
        Scanner scanner = new Scanner("\"hello\nworld\nno end");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(1, tokens.size());
        assertEquals(EOF, tokens.get(0).type);
    }

    // ========== 标识符边界测试 ==========

    @Test
    @DisplayName("Test identifier - Starting with underscore")
    void testIdentifier_StartingUnderscore() {
        Scanner scanner = new Scanner("_private _123");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(3, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("_private", tokens.get(0).lexeme);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("_123", tokens.get(1).lexeme);
    }

    @Test
    @DisplayName("Test identifier - Only underscore")
    void testIdentifier_OnlyUnderscore() {
        Scanner scanner = new Scanner("_");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("_", tokens.get(0).lexeme);
    }

    @Test
    @DisplayName("Test identifier - Keyword prefix")
    void testIdentifier_KeywordPrefix() {
        Scanner scanner = new Scanner("varx variable ifx forloop");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(5, tokens.size());
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("varx", tokens.get(0).lexeme);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("variable", tokens.get(1).lexeme);
        assertEquals(IDENTIFIER, tokens.get(2).type);
        assertEquals("ifx", tokens.get(2).lexeme);
        assertEquals(IDENTIFIER, tokens.get(3).type);
        assertEquals("forloop", tokens.get(3).lexeme);
    }

    // ========== 注释边界测试 ==========

    @Test
    @DisplayName("Test comment - Multiple consecutive comments")
    void testComment_MultipleConsecutive() {
        Scanner scanner = new Scanner("// comment 1\n// comment 2\n// comment 3\n+");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(4, tokens.get(0).line);
    }

    @Test
    @DisplayName("Test comment - With special characters")
    void testComment_SpecialCharacters() {
        Scanner scanner = new Scanner("// !@#$%^&*(){}[]<>?/\n+");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
    }

    @Test
    @DisplayName("Test division vs comment")
    void testDivision_VsComment() {
        Scanner scanner = new Scanner("10 / 2 // not a comment operator");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(4, tokens.size());
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(SLASH, tokens.get(1).type);
        assertEquals(NUMBER, tokens.get(2).type);
        assertEquals(EOF, tokens.get(3).type);
    }

    // ========== 复杂组合测试 ==========

    @Test
    @DisplayName("Complex test - Adjacent operators")
    void testComplex_AdjacentOperators() {
        Scanner scanner = new Scanner("+-*/");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(5, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(MINUS, tokens.get(1).type);
        assertEquals(STAR, tokens.get(2).type);
        assertEquals(SLASH, tokens.get(3).type);
    }

    @Test
    @DisplayName("Complex test - All comparison operators")
    void testComplex_AllComparisons() {
        Scanner scanner = new Scanner("< <= > >= == !=");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(7, tokens.size());
        assertEquals(LESS, tokens.get(0).type);
        assertEquals(LESS_EQUAL, tokens.get(1).type);
        assertEquals(GREATER, tokens.get(2).type);
        assertEquals(GREATER_EQUAL, tokens.get(3).type);
        assertEquals(EQUAL_EQUAL, tokens.get(4).type);
        assertEquals(BANG_EQUAL, tokens.get(5).type);
    }

    @Test
    @DisplayName("Complex test - Class with methods")
    void testComplex_ClassWithMethods() {
        String source = "class Person {\n" +
                    "  init(name) {\n" +
                    "    this.name = name;\n" +
                    "  }\n" +
                    "}";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(CLASS, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals("Person", tokens.get(1).lexeme);
        assertEquals(LEFT_BRACE, tokens.get(2).type);
        // 验证关键 token 存在
        assertTrue(tokens.stream().anyMatch(t -> t.type == THIS));
        assertTrue(tokens.stream().anyMatch(t -> t.type == DOT));
    }

    @Test
    @DisplayName("Complex test - Nested expressions")
    void testComplex_NestedExpressions() {
        Scanner scanner = new Scanner("((1 + 2) * (3 - 4)) / 5");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(16, tokens.size());
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(LEFT_PAREN, tokens.get(1).type);
        assertEquals(RIGHT_PAREN, tokens.get(5).type);
        assertEquals(RIGHT_PAREN, tokens.get(11).type);
    }

    @Test
    @DisplayName("Complex test - All keywords")
    void testComplex_AllKeywords() {
        String source = "and class else false for fun if nil or print return super this true var while";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(17, tokens.size()); // 15 keywords + EOF
        assertEquals(AND, tokens.get(0).type);
        assertEquals(CLASS, tokens.get(1).type);
        assertEquals(ELSE, tokens.get(2).type);
        assertEquals(FALSE, tokens.get(3).type);
        assertEquals(FOR, tokens.get(4).type);
        assertEquals(FUN, tokens.get(5).type);
        assertEquals(IF, tokens.get(6).type);
        assertEquals(NIL, tokens.get(7).type);
        assertEquals(OR, tokens.get(8).type);
        assertEquals(PRINT, tokens.get(9).type);
        assertEquals(RETURN, tokens.get(10).type);
        assertEquals(SUPER, tokens.get(11).type);
        assertEquals(THIS, tokens.get(12).type);
        assertEquals(TRUE, tokens.get(13).type);
        assertEquals(VAR, tokens.get(14).type);
        assertEquals(WHILE, tokens.get(15).type);
    }

    // ========== 行号追踪测试 ==========

    @Test
    @DisplayName("Test line tracking - Complex multiline")
    void testLineTracking_ComplexMultiline() {
        String source = "var x = 1;\n" +
                    "\n" +  // 空行
                    "// comment\n" +
                    "var y = 2;\n" +
                    "\"multiline\n" +
                    "string\" + x";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        
        // 验证行号正确
        assertEquals(1, tokens.get(0).line); // var
        assertEquals(4, tokens.stream().filter(t -> t.type == VAR).skip(1).findFirst().get().line); // 第二个 var
        assertEquals(6, tokens.stream().filter(t -> t.type == STRING).findFirst().get().line); // string 结束行
    }

    @Test
    @DisplayName("Test line tracking - Only newlines")
    void testLineTracking_OnlyNewlines() {
        Scanner scanner = new Scanner("\n\n\n+");
        List<Token> tokens = scanner.scanTokens();
        
        assertEquals(2, tokens.size());
        assertEquals(PLUS, tokens.get(0).type);
        assertEquals(4, tokens.get(0).line);
    }

}