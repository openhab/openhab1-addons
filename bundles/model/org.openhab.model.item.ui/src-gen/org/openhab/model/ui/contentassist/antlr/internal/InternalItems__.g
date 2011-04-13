lexer grammar InternalItems;
@header {
package org.openhab.model.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;
}

T11 : 'Switch' ;
T12 : 'Rollershutter' ;
T13 : 'Number' ;
T14 : 'String' ;
T15 : 'Dimmer' ;
T16 : 'Contact' ;
T17 : 'AND' ;
T18 : 'OR' ;
T19 : '<' ;
T20 : '>' ;
T21 : '(' ;
T22 : ')' ;
T23 : ',' ;
T24 : '{' ;
T25 : '}' ;
T26 : 'Group' ;
T27 : '[' ;
T28 : ']' ;
T29 : '=' ;

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1863
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1865
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1867
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1869
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1871
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1873
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g" 1875
RULE_ANY_OTHER : .;


