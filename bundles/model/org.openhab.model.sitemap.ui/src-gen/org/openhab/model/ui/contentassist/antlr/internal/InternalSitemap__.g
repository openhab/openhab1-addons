lexer grammar InternalSitemap;
@header {
package org.openhab.model.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;
}

T11 : 'sitemap' ;
T12 : '{' ;
T13 : '}' ;
T14 : 'label=' ;
T15 : 'icon=' ;
T16 : 'Text' ;
T17 : 'item=' ;
T18 : 'Group' ;
T19 : 'Image' ;
T20 : 'url=' ;
T21 : 'Switch' ;
T22 : 'buttonLabels=[' ;
T23 : ']' ;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1889
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1891
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1893
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1895
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1897
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1899
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 1901
RULE_ANY_OTHER : .;


