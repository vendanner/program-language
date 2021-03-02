/*
 antlr4 构建计算器
 词法单元以大写字母开头
 语法单元以小写字母开头
*/
grammar Calculator;

line : expr EOF ;
expr : '(' expr ')'                 # parenExpr
     | expr op=('*'|'/') expr       # multOrDiv
     | expr op=('+'|'-') expr       # addOrSub
     | FLOAT                        # float ;

WS : [ \t\n\r]+ -> skip ;
FLOAT : DIGIT+ '.' DIGIT* EXPONENT?
     | '.' DIGIT+ EXPONENT?
     | DIGIT+ EXPONENT? ;
fragment DIGIT : '0'..'9';
fragment EXPONENT : ('e'|'E') ('+'|'-')? DIGIT+ ;
MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;