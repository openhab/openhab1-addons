package org.openhab.model.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalItemsLexer extends Lexer {
    public static final int RULE_ID=4;
    public static final int RULE_ANY_OTHER=10;
    public static final int T29=29;
    public static final int T28=28;
    public static final int T27=27;
    public static final int T26=26;
    public static final int T25=25;
    public static final int Tokens=33;
    public static final int T24=24;
    public static final int EOF=-1;
    public static final int RULE_SL_COMMENT=8;
    public static final int T23=23;
    public static final int T22=22;
    public static final int T21=21;
    public static final int T20=20;
    public static final int RULE_ML_COMMENT=7;
    public static final int RULE_STRING=5;
    public static final int RULE_INT=6;
    public static final int T11=11;
    public static final int T12=12;
    public static final int T13=13;
    public static final int T14=14;
    public static final int RULE_WS=9;
    public static final int T15=15;
    public static final int T16=16;
    public static final int T17=17;
    public static final int T18=18;
    public static final int T30=30;
    public static final int T19=19;
    public static final int T32=32;
    public static final int T31=31;
    public InternalItemsLexer() {;} 
    public InternalItemsLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g"; }

    // $ANTLR start T11
    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:10:5: ( '<' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:10:7: '<'
            {
            match('<'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T11

    // $ANTLR start T12
    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:11:5: ( '>' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:11:7: '>'
            {
            match('>'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:12:5: ( '(' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:12:7: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T13

    // $ANTLR start T14
    public final void mT14() throws RecognitionException {
        try {
            int _type = T14;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:13:5: ( ',' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:13:7: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T14

    // $ANTLR start T15
    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:14:5: ( ')' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:14:7: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T15

    // $ANTLR start T16
    public final void mT16() throws RecognitionException {
        try {
            int _type = T16;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:15:5: ( '{' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:15:7: '{'
            {
            match('{'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T16

    // $ANTLR start T17
    public final void mT17() throws RecognitionException {
        try {
            int _type = T17;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:16:5: ( '}' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:16:7: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T17

    // $ANTLR start T18
    public final void mT18() throws RecognitionException {
        try {
            int _type = T18;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:17:5: ( 'Group' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:17:7: 'Group'
            {
            match("Group"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T18

    // $ANTLR start T19
    public final void mT19() throws RecognitionException {
        try {
            int _type = T19;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:5: ( ':' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:7: ':'
            {
            match(':'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public final void mT20() throws RecognitionException {
        try {
            int _type = T20;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:5: ( 'Switch' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:7: 'Switch'
            {
            match("Switch"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public final void mT21() throws RecognitionException {
        try {
            int _type = T21;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:5: ( 'Rollershutter' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:7: 'Rollershutter'
            {
            match("Rollershutter"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public final void mT22() throws RecognitionException {
        try {
            int _type = T22;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:5: ( 'Number' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:7: 'Number'
            {
            match("Number"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public final void mT23() throws RecognitionException {
        try {
            int _type = T23;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:5: ( 'String' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:7: 'String'
            {
            match("String"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public final void mT24() throws RecognitionException {
        try {
            int _type = T24;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:5: ( 'Dimmer' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:7: 'Dimmer'
            {
            match("Dimmer"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T24

    // $ANTLR start T25
    public final void mT25() throws RecognitionException {
        try {
            int _type = T25;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:5: ( 'Contact' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:7: 'Contact'
            {
            match("Contact"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T25

    // $ANTLR start T26
    public final void mT26() throws RecognitionException {
        try {
            int _type = T26;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:5: ( 'DateTime' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:7: 'DateTime'
            {
            match("DateTime"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public final void mT27() throws RecognitionException {
        try {
            int _type = T27;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:26:5: ( '=' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:26:7: '='
            {
            match('='); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public final void mT28() throws RecognitionException {
        try {
            int _type = T28;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:27:5: ( 'AND' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:27:7: 'AND'
            {
            match("AND"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public final void mT29() throws RecognitionException {
        try {
            int _type = T29;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:28:5: ( 'OR' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:28:7: 'OR'
            {
            match("OR"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public final void mT30() throws RecognitionException {
        try {
            int _type = T30;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:29:5: ( 'AVG' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:29:7: 'AVG'
            {
            match("AVG"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public final void mT31() throws RecognitionException {
        try {
            int _type = T31;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:30:5: ( 'MAX' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:30:7: 'MAX'
            {
            match("MAX"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public final void mT32() throws RecognitionException {
        try {
            int _type = T32;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:31:5: ( 'MIN' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:31:7: 'MIN'
            {
            match("MIN"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T32

    // $ANTLR start RULE_ID
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:812:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:812:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:812:11: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='^') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:812:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:812:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ID

    // $ANTLR start RULE_INT
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:814:10: ( ( '0' .. '9' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:814:12: ( '0' .. '9' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:814:12: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:814:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_INT

    // $ANTLR start RULE_STRING
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\"') ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("816:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\\') ) {
                            alt4=1;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFE')) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:62: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:82: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:87: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\\') ) {
                            alt5=1;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFE')) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:88: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:816:129: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_STRING

    // $ANTLR start RULE_ML_COMMENT
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:818:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:818:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:818:24: ( options {greedy=false; } : . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='*') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='/') ) {
                        alt7=2;
                    }
                    else if ( ((LA7_1>='\u0000' && LA7_1<='.')||(LA7_1>='0' && LA7_1<='\uFFFE')) ) {
                        alt7=1;
                    }


                }
                else if ( ((LA7_0>='\u0000' && LA7_0<=')')||(LA7_0>='+' && LA7_0<='\uFFFE')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:818:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match("*/"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ML_COMMENT

    // $ANTLR start RULE_SL_COMMENT
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='\t')||(LA8_0>='\u000B' && LA8_0<='\f')||(LA8_0>='\u000E' && LA8_0<='\uFFFE')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:40: ( ( '\\r' )? '\\n' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:41: ( '\\r' )? '\\n'
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:41: ( '\\r' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='\r') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:820:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_SL_COMMENT

    // $ANTLR start RULE_WS
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:822:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:822:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:822:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='\t' && LA11_0<='\n')||LA11_0=='\r'||LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_WS

    // $ANTLR start RULE_ANY_OTHER
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:824:16: ( . )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:824:18: .
            {
            matchAny(); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RULE_ANY_OTHER

    public void mTokens() throws RecognitionException {
        // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:8: ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt12=29;
        int LA12_0 = input.LA(1);

        if ( (LA12_0=='<') ) {
            alt12=1;
        }
        else if ( (LA12_0=='>') ) {
            alt12=2;
        }
        else if ( (LA12_0=='(') ) {
            alt12=3;
        }
        else if ( (LA12_0==',') ) {
            alt12=4;
        }
        else if ( (LA12_0==')') ) {
            alt12=5;
        }
        else if ( (LA12_0=='{') ) {
            alt12=6;
        }
        else if ( (LA12_0=='}') ) {
            alt12=7;
        }
        else if ( (LA12_0=='G') ) {
            int LA12_8 = input.LA(2);

            if ( (LA12_8=='r') ) {
                int LA12_34 = input.LA(3);

                if ( (LA12_34=='o') ) {
                    int LA12_55 = input.LA(4);

                    if ( (LA12_55=='u') ) {
                        int LA12_68 = input.LA(5);

                        if ( (LA12_68=='p') ) {
                            int LA12_80 = input.LA(6);

                            if ( ((LA12_80>='0' && LA12_80<='9')||(LA12_80>='A' && LA12_80<='Z')||LA12_80=='_'||(LA12_80>='a' && LA12_80<='z')) ) {
                                alt12=23;
                            }
                            else {
                                alt12=8;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
            }
            else {
                alt12=23;}
        }
        else if ( (LA12_0==':') ) {
            alt12=9;
        }
        else if ( (LA12_0=='S') ) {
            switch ( input.LA(2) ) {
            case 'w':
                {
                int LA12_37 = input.LA(3);

                if ( (LA12_37=='i') ) {
                    int LA12_56 = input.LA(4);

                    if ( (LA12_56=='t') ) {
                        int LA12_69 = input.LA(5);

                        if ( (LA12_69=='c') ) {
                            int LA12_81 = input.LA(6);

                            if ( (LA12_81=='h') ) {
                                int LA12_89 = input.LA(7);

                                if ( ((LA12_89>='0' && LA12_89<='9')||(LA12_89>='A' && LA12_89<='Z')||LA12_89=='_'||(LA12_89>='a' && LA12_89<='z')) ) {
                                    alt12=23;
                                }
                                else {
                                    alt12=10;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
                }
                break;
            case 't':
                {
                int LA12_38 = input.LA(3);

                if ( (LA12_38=='r') ) {
                    int LA12_57 = input.LA(4);

                    if ( (LA12_57=='i') ) {
                        int LA12_70 = input.LA(5);

                        if ( (LA12_70=='n') ) {
                            int LA12_82 = input.LA(6);

                            if ( (LA12_82=='g') ) {
                                int LA12_90 = input.LA(7);

                                if ( ((LA12_90>='0' && LA12_90<='9')||(LA12_90>='A' && LA12_90<='Z')||LA12_90=='_'||(LA12_90>='a' && LA12_90<='z')) ) {
                                    alt12=23;
                                }
                                else {
                                    alt12=13;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
                }
                break;
            default:
                alt12=23;}

        }
        else if ( (LA12_0=='R') ) {
            int LA12_11 = input.LA(2);

            if ( (LA12_11=='o') ) {
                int LA12_39 = input.LA(3);

                if ( (LA12_39=='l') ) {
                    int LA12_58 = input.LA(4);

                    if ( (LA12_58=='l') ) {
                        int LA12_71 = input.LA(5);

                        if ( (LA12_71=='e') ) {
                            int LA12_83 = input.LA(6);

                            if ( (LA12_83=='r') ) {
                                int LA12_91 = input.LA(7);

                                if ( (LA12_91=='s') ) {
                                    int LA12_98 = input.LA(8);

                                    if ( (LA12_98=='h') ) {
                                        int LA12_103 = input.LA(9);

                                        if ( (LA12_103=='u') ) {
                                            int LA12_106 = input.LA(10);

                                            if ( (LA12_106=='t') ) {
                                                int LA12_108 = input.LA(11);

                                                if ( (LA12_108=='t') ) {
                                                    int LA12_109 = input.LA(12);

                                                    if ( (LA12_109=='e') ) {
                                                        int LA12_110 = input.LA(13);

                                                        if ( (LA12_110=='r') ) {
                                                            int LA12_111 = input.LA(14);

                                                            if ( ((LA12_111>='0' && LA12_111<='9')||(LA12_111>='A' && LA12_111<='Z')||LA12_111=='_'||(LA12_111>='a' && LA12_111<='z')) ) {
                                                                alt12=23;
                                                            }
                                                            else {
                                                                alt12=11;}
                                                        }
                                                        else {
                                                            alt12=23;}
                                                    }
                                                    else {
                                                        alt12=23;}
                                                }
                                                else {
                                                    alt12=23;}
                                            }
                                            else {
                                                alt12=23;}
                                        }
                                        else {
                                            alt12=23;}
                                    }
                                    else {
                                        alt12=23;}
                                }
                                else {
                                    alt12=23;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
            }
            else {
                alt12=23;}
        }
        else if ( (LA12_0=='N') ) {
            int LA12_12 = input.LA(2);

            if ( (LA12_12=='u') ) {
                int LA12_40 = input.LA(3);

                if ( (LA12_40=='m') ) {
                    int LA12_59 = input.LA(4);

                    if ( (LA12_59=='b') ) {
                        int LA12_72 = input.LA(5);

                        if ( (LA12_72=='e') ) {
                            int LA12_84 = input.LA(6);

                            if ( (LA12_84=='r') ) {
                                int LA12_92 = input.LA(7);

                                if ( ((LA12_92>='0' && LA12_92<='9')||(LA12_92>='A' && LA12_92<='Z')||LA12_92=='_'||(LA12_92>='a' && LA12_92<='z')) ) {
                                    alt12=23;
                                }
                                else {
                                    alt12=12;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
            }
            else {
                alt12=23;}
        }
        else if ( (LA12_0=='D') ) {
            switch ( input.LA(2) ) {
            case 'i':
                {
                int LA12_41 = input.LA(3);

                if ( (LA12_41=='m') ) {
                    int LA12_60 = input.LA(4);

                    if ( (LA12_60=='m') ) {
                        int LA12_73 = input.LA(5);

                        if ( (LA12_73=='e') ) {
                            int LA12_85 = input.LA(6);

                            if ( (LA12_85=='r') ) {
                                int LA12_93 = input.LA(7);

                                if ( ((LA12_93>='0' && LA12_93<='9')||(LA12_93>='A' && LA12_93<='Z')||LA12_93=='_'||(LA12_93>='a' && LA12_93<='z')) ) {
                                    alt12=23;
                                }
                                else {
                                    alt12=14;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
                }
                break;
            case 'a':
                {
                int LA12_42 = input.LA(3);

                if ( (LA12_42=='t') ) {
                    int LA12_61 = input.LA(4);

                    if ( (LA12_61=='e') ) {
                        int LA12_74 = input.LA(5);

                        if ( (LA12_74=='T') ) {
                            int LA12_86 = input.LA(6);

                            if ( (LA12_86=='i') ) {
                                int LA12_94 = input.LA(7);

                                if ( (LA12_94=='m') ) {
                                    int LA12_101 = input.LA(8);

                                    if ( (LA12_101=='e') ) {
                                        int LA12_104 = input.LA(9);

                                        if ( ((LA12_104>='0' && LA12_104<='9')||(LA12_104>='A' && LA12_104<='Z')||LA12_104=='_'||(LA12_104>='a' && LA12_104<='z')) ) {
                                            alt12=23;
                                        }
                                        else {
                                            alt12=16;}
                                    }
                                    else {
                                        alt12=23;}
                                }
                                else {
                                    alt12=23;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
                }
                break;
            default:
                alt12=23;}

        }
        else if ( (LA12_0=='C') ) {
            int LA12_14 = input.LA(2);

            if ( (LA12_14=='o') ) {
                int LA12_43 = input.LA(3);

                if ( (LA12_43=='n') ) {
                    int LA12_62 = input.LA(4);

                    if ( (LA12_62=='t') ) {
                        int LA12_75 = input.LA(5);

                        if ( (LA12_75=='a') ) {
                            int LA12_87 = input.LA(6);

                            if ( (LA12_87=='c') ) {
                                int LA12_95 = input.LA(7);

                                if ( (LA12_95=='t') ) {
                                    int LA12_102 = input.LA(8);

                                    if ( ((LA12_102>='0' && LA12_102<='9')||(LA12_102>='A' && LA12_102<='Z')||LA12_102=='_'||(LA12_102>='a' && LA12_102<='z')) ) {
                                        alt12=23;
                                    }
                                    else {
                                        alt12=15;}
                                }
                                else {
                                    alt12=23;}
                            }
                            else {
                                alt12=23;}
                        }
                        else {
                            alt12=23;}
                    }
                    else {
                        alt12=23;}
                }
                else {
                    alt12=23;}
            }
            else {
                alt12=23;}
        }
        else if ( (LA12_0=='=') ) {
            alt12=17;
        }
        else if ( (LA12_0=='A') ) {
            switch ( input.LA(2) ) {
            case 'V':
                {
                int LA12_45 = input.LA(3);

                if ( (LA12_45=='G') ) {
                    int LA12_63 = input.LA(4);

                    if ( ((LA12_63>='0' && LA12_63<='9')||(LA12_63>='A' && LA12_63<='Z')||LA12_63=='_'||(LA12_63>='a' && LA12_63<='z')) ) {
                        alt12=23;
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=23;}
                }
                break;
            case 'N':
                {
                int LA12_46 = input.LA(3);

                if ( (LA12_46=='D') ) {
                    int LA12_64 = input.LA(4);

                    if ( ((LA12_64>='0' && LA12_64<='9')||(LA12_64>='A' && LA12_64<='Z')||LA12_64=='_'||(LA12_64>='a' && LA12_64<='z')) ) {
                        alt12=23;
                    }
                    else {
                        alt12=18;}
                }
                else {
                    alt12=23;}
                }
                break;
            default:
                alt12=23;}

        }
        else if ( (LA12_0=='O') ) {
            int LA12_17 = input.LA(2);

            if ( (LA12_17=='R') ) {
                int LA12_47 = input.LA(3);

                if ( ((LA12_47>='0' && LA12_47<='9')||(LA12_47>='A' && LA12_47<='Z')||LA12_47=='_'||(LA12_47>='a' && LA12_47<='z')) ) {
                    alt12=23;
                }
                else {
                    alt12=19;}
            }
            else {
                alt12=23;}
        }
        else if ( (LA12_0=='M') ) {
            switch ( input.LA(2) ) {
            case 'A':
                {
                int LA12_48 = input.LA(3);

                if ( (LA12_48=='X') ) {
                    int LA12_66 = input.LA(4);

                    if ( ((LA12_66>='0' && LA12_66<='9')||(LA12_66>='A' && LA12_66<='Z')||LA12_66=='_'||(LA12_66>='a' && LA12_66<='z')) ) {
                        alt12=23;
                    }
                    else {
                        alt12=21;}
                }
                else {
                    alt12=23;}
                }
                break;
            case 'I':
                {
                int LA12_49 = input.LA(3);

                if ( (LA12_49=='N') ) {
                    int LA12_67 = input.LA(4);

                    if ( ((LA12_67>='0' && LA12_67<='9')||(LA12_67>='A' && LA12_67<='Z')||LA12_67=='_'||(LA12_67>='a' && LA12_67<='z')) ) {
                        alt12=23;
                    }
                    else {
                        alt12=22;}
                }
                else {
                    alt12=23;}
                }
                break;
            default:
                alt12=23;}

        }
        else if ( (LA12_0=='^') ) {
            int LA12_19 = input.LA(2);

            if ( ((LA12_19>='A' && LA12_19<='Z')||LA12_19=='_'||(LA12_19>='a' && LA12_19<='z')) ) {
                alt12=23;
            }
            else {
                alt12=29;}
        }
        else if ( (LA12_0=='B'||(LA12_0>='E' && LA12_0<='F')||(LA12_0>='H' && LA12_0<='L')||(LA12_0>='P' && LA12_0<='Q')||(LA12_0>='T' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {
            alt12=23;
        }
        else if ( ((LA12_0>='0' && LA12_0<='9')) ) {
            alt12=24;
        }
        else if ( (LA12_0=='\"') ) {
            int LA12_22 = input.LA(2);

            if ( ((LA12_22>='\u0000' && LA12_22<='\uFFFE')) ) {
                alt12=25;
            }
            else {
                alt12=29;}
        }
        else if ( (LA12_0=='\'') ) {
            int LA12_23 = input.LA(2);

            if ( ((LA12_23>='\u0000' && LA12_23<='\uFFFE')) ) {
                alt12=25;
            }
            else {
                alt12=29;}
        }
        else if ( (LA12_0=='/') ) {
            switch ( input.LA(2) ) {
            case '*':
                {
                alt12=26;
                }
                break;
            case '/':
                {
                alt12=27;
                }
                break;
            default:
                alt12=29;}

        }
        else if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
            alt12=28;
        }
        else if ( ((LA12_0>='\u0000' && LA12_0<='\b')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\u001F')||LA12_0=='!'||(LA12_0>='#' && LA12_0<='&')||(LA12_0>='*' && LA12_0<='+')||(LA12_0>='-' && LA12_0<='.')||LA12_0==';'||(LA12_0>='?' && LA12_0<='@')||(LA12_0>='[' && LA12_0<=']')||LA12_0=='`'||LA12_0=='|'||(LA12_0>='~' && LA12_0<='\uFFFE')) ) {
            alt12=29;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );", 12, 0, input);

            throw nvae;
        }
        switch (alt12) {
            case 1 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:10: T11
                {
                mT11(); 

                }
                break;
            case 2 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:14: T12
                {
                mT12(); 

                }
                break;
            case 3 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:18: T13
                {
                mT13(); 

                }
                break;
            case 4 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:22: T14
                {
                mT14(); 

                }
                break;
            case 5 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:26: T15
                {
                mT15(); 

                }
                break;
            case 6 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:30: T16
                {
                mT16(); 

                }
                break;
            case 7 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:34: T17
                {
                mT17(); 

                }
                break;
            case 8 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:38: T18
                {
                mT18(); 

                }
                break;
            case 9 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:42: T19
                {
                mT19(); 

                }
                break;
            case 10 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:46: T20
                {
                mT20(); 

                }
                break;
            case 11 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:50: T21
                {
                mT21(); 

                }
                break;
            case 12 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:54: T22
                {
                mT22(); 

                }
                break;
            case 13 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:58: T23
                {
                mT23(); 

                }
                break;
            case 14 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:62: T24
                {
                mT24(); 

                }
                break;
            case 15 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:66: T25
                {
                mT25(); 

                }
                break;
            case 16 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:70: T26
                {
                mT26(); 

                }
                break;
            case 17 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:74: T27
                {
                mT27(); 

                }
                break;
            case 18 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:78: T28
                {
                mT28(); 

                }
                break;
            case 19 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:82: T29
                {
                mT29(); 

                }
                break;
            case 20 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:86: T30
                {
                mT30(); 

                }
                break;
            case 21 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:90: T31
                {
                mT31(); 

                }
                break;
            case 22 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:94: T32
                {
                mT32(); 

                }
                break;
            case 23 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:98: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 24 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:106: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 25 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:115: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 26 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:127: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 27 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:143: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 28 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:159: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 29 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:167: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


 

}