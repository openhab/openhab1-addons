lexer grammar InternalItems;
@header {
package org.openhab.model.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

T11 : '<' ;
T12 : '>' ;
T13 : '(' ;
T14 : ',' ;
T15 : ')' ;
T16 : '{' ;
T17 : '}' ;
T18 : 'Group' ;
T19 : '[' ;
T20 : ']' ;
T21 : 'Switch' ;
T22 : 'Rollershutter' ;
T23 : 'Number' ;
T24 : 'String' ;
T25 : 'Dimmer' ;
T26 : 'Contact' ;
T27 : '=' ;
T28 : 'AND' ;
T29 : 'OR' ;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 787
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 789
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 791
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 793
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 795
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 797
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 799
RULE_ANY_OTHER : .;


