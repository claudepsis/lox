package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.BANG;
import static com.craftinginterpreters.lox.TokenType.BANG_EQUAL;
import static com.craftinginterpreters.lox.TokenType.COMMA;
import static com.craftinginterpreters.lox.TokenType.DOT;
import static com.craftinginterpreters.lox.TokenType.EOF;
import static com.craftinginterpreters.lox.TokenType.EQUAL;
import static com.craftinginterpreters.lox.TokenType.EQUAL_EQUAL;
import static com.craftinginterpreters.lox.TokenType.GREATER;
import static com.craftinginterpreters.lox.TokenType.GREATER_EQUAL;
import static com.craftinginterpreters.lox.TokenType.IDENTIFIER;
import static com.craftinginterpreters.lox.TokenType.LEFT_BRACE;
import static com.craftinginterpreters.lox.TokenType.LEFT_PAREN;
import static com.craftinginterpreters.lox.TokenType.LESS;
import static com.craftinginterpreters.lox.TokenType.LESS_EQUAL;
import static com.craftinginterpreters.lox.TokenType.MINUS;
import static com.craftinginterpreters.lox.TokenType.NUMBER;
import static com.craftinginterpreters.lox.TokenType.PLUS;
import static com.craftinginterpreters.lox.TokenType.RIGHT_BRACE;
import static com.craftinginterpreters.lox.TokenType.RIGHT_PAREN;
import static com.craftinginterpreters.lox.TokenType.SEMICOLON;
import static com.craftinginterpreters.lox.TokenType.SLASH;
import static com.craftinginterpreters.lox.TokenType.STAR;
import static com.craftinginterpreters.lox.TokenType.STRING;
class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and",    TokenType.AND);
    keywords.put("class",  TokenType.CLASS);
    keywords.put("else",   TokenType.ELSE);
    keywords.put("false",  TokenType.FALSE);
    keywords.put("for",    TokenType.FOR);
    keywords.put("fun",    TokenType.FUN);
    keywords.put("if",     TokenType.IF);
    keywords.put("nil",    TokenType.NIL);
    keywords.put("or",     TokenType.OR);
    keywords.put("print",  TokenType.PRINT);
    keywords.put("return", TokenType.RETURN);
    keywords.put("super",  TokenType.SUPER);
    keywords.put("this",   TokenType.THIS);
    keywords.put("true",   TokenType.TRUE);
    keywords.put("var",    TokenType.VAR);
    keywords.put("while",  TokenType.WHILE);
  }

  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens(){
    while(!isAtEnd()){
        scanToken();
        start=current;
    }
    addToken(EOF);
    return tokens;
  }

  void scanToken(){
    char c=advance();
    switch (c) {
        //Single
        case '(' -> addToken(LEFT_PAREN);
        case ')' -> addToken(RIGHT_PAREN);
        case '{' -> addToken(LEFT_BRACE);
        case '}' -> addToken(RIGHT_BRACE);
        case ',' -> addToken(COMMA);
        case '.' -> addToken(DOT);
        case '+' -> addToken(PLUS);
        case '-' -> addToken(MINUS);
        case ';' -> addToken(SEMICOLON);
        case '*' -> addToken(STAR);
        //Single or Double
        case '!'-> addToken(match('=')?BANG_EQUAL:BANG);
        case '=' ->addToken(match('=')?EQUAL_EQUAL:EQUAL);
        case '>' ->addToken(match('=')?GREATER_EQUAL:GREATER);
        case '<' ->addToken(match('=')?LESS_EQUAL:LESS);
        case '/' ->{
            if(match('/')){
                while(peek()!='\n'&&(!isAtEnd())) advance();
            }
            else if(match('*')){
                while(peek()!='*'&&peekNext()!='/'&&!isAtEnd()) advance();
            }
            else addToken(SLASH);
        }
        case '\n'-> line++;
        case ' ','\t' ->{}
        case '"' -> string();
        default -> {
            if(isDigit(c)){
                number();
            }
            else if(isAlpha(c)){
                identifier();
            }
            else Lox.error(line, "Unexpected character: " + c);
        }
    }
  }

  boolean match(char expected){
    if(isAtEnd()) return false;
    if(peek()!=expected) return false;
    else{
        advance();
        return true;
    }
}





void string(){
    char c;
    while((c=peek())!='"'){
        if(isAtEnd()){
            Lox.error(line, "Unterminated string.");
            return;
        }  
        else if(c=='\n') line++;
        advance();
    }
    advance();
    addToken(STRING,source.substring(start+1,current-1));
}

void number(){
    while(isDigit(peek())) advance();
    if(peek()=='.'&&isDigit(peekNext())){
        advance();
        while(isDigit(peek())) advance();
    }
    addToken(NUMBER,Double.valueOf(source.substring(start,current)));
}

void identifier(){
    while(isAlphaNumeric(peek())) advance();
    String vaule=source.substring(start,current);
    addToken(keywords.get(vaule)!=null?keywords.get(vaule):IDENTIFIER,vaule);
}


  void addToken(TokenType type){
    addToken(type, null);
  }

  void addToken(TokenType type,Object literal){
    String text=source.substring(start,current);
    tokens.add(new Token(type,text,literal,line));
  }

  private boolean isAtEnd(){
    return current >= source.length();
  }

  private  char advance(){
    return source.charAt(current++);
  }

  private  char peek(){
    if(isAtEnd()) return '\0';
    return source.charAt(current);
  }

  private char peekNext(){
    if(current+1>=source.length()) return '\0';
    else return source.charAt(current+1);
  }

  private  boolean  isDigit(char c){
    return c>='0'&&c<='9';
  }

  private boolean  isAlpha(char c){
    return (c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='_';
  }

  private boolean  isAlphaNumeric(char c){
    return isAlpha(c)||isDigit(c);
  }



}