lexer grammar InternalConfig;
@header {
package org.openhab.model.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;
}

T11 : 'Switch' ;
T12 : 'Rollerblind' ;
T13 : 'Measurement' ;
T14 : 'String' ;
T15 : 'Dimmer' ;
T16 : 'Contact' ;
T17 : 'Group' ;
T18 : '<' ;
T19 : '>' ;
T20 : '(' ;
T21 : ')' ;
T22 : ',' ;
T23 : '{' ;
T24 : '}' ;
T25 : '=' ;

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1791
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1793
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1795
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1797
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1799
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1801
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g" 1803
RULE_ANY_OTHER : .;


