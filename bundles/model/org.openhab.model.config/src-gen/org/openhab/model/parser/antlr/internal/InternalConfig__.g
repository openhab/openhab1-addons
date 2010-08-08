lexer grammar InternalConfig;
@header {
package org.openhab.model.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

T11 : 'Group' ;
T12 : '<' ;
T13 : '>' ;
T14 : '(' ;
T15 : ',' ;
T16 : ')' ;
T17 : 'Switch' ;
T18 : 'Rollerblind' ;
T19 : 'Measurement' ;
T20 : 'String' ;
T21 : 'Dimmer' ;
T22 : 'Contact' ;
T23 : '{' ;
T24 : '}' ;
T25 : '=' ;

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 773
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 775
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 777
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 779
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 781
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 783
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g" 785
RULE_ANY_OTHER : .;


