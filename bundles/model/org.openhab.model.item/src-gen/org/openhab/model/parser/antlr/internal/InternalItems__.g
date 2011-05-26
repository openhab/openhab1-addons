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
T26 : 'DateTime' ;
T27 : '=' ;
T28 : 'AND' ;
T29 : 'OR' ;
T30 : 'AVG' ;
T31 : 'MAX' ;
T32 : 'MIN' ;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 812
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 814
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 816
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 818
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 820
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 822
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 824
RULE_ANY_OTHER : .;


