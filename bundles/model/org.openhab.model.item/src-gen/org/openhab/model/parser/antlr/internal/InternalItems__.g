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
T19 : ':' ;
T20 : 'Switch' ;
T21 : 'Rollershutter' ;
T22 : 'Number' ;
T23 : 'String' ;
T24 : 'Dimmer' ;
T25 : 'Contact' ;
T26 : '=' ;
T27 : 'AND' ;
T28 : 'OR' ;
T29 : 'AVG' ;
T30 : 'MAX' ;
T31 : 'MIN' ;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 805
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 807
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 809
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 811
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 813
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 815
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 817
RULE_ANY_OTHER : .;


