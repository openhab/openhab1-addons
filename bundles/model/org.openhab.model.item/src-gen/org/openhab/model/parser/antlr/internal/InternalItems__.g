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
T19 : 'Switch' ;
T20 : 'Rollerblind' ;
T21 : 'Measurement' ;
T22 : 'String' ;
T23 : 'Dimmer' ;
T24 : 'Contact' ;
T25 : '=' ;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 640
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 642
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 644
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 646
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 648
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 650
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g" 652
RULE_ANY_OTHER : .;


