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
T16 : 'Frame' ;
T17 : 'item=' ;
T18 : 'Text' ;
T19 : 'Group' ;
T20 : 'Image' ;
T21 : 'url=' ;
T22 : 'Switch' ;
T23 : 'mappings=[' ;
T24 : ']' ;
T25 : ',' ;
T26 : 'Slider' ;
T27 : 'sendFrequency=' ;
T28 : 'Selection' ;
T29 : 'List' ;
T30 : 'separator=' ;
T31 : '=' ;
T32 : 'switchSupport' ;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4710
RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_'|'0'..'9')+;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4712
RULE_INT : ('0'..'9')+;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4714
RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4716
RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4718
RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4720
RULE_WS : (' '|'\t'|'\r'|'\n')+;

// $ANTLR src "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g" 4722
RULE_ANY_OTHER : .;


