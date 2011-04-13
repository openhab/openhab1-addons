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
    public static final int Tokens=30;
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
    public static final int T19=19;
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:5: ( '[' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:7: '['
            {
            match('['); 

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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:5: ( ']' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:7: ']'
            {
            match(']'); 

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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:5: ( 'Switch' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:7: 'Switch'
            {
            match("Switch"); 


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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:5: ( 'Rollershutter' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:7: 'Rollershutter'
            {
            match("Rollershutter"); 


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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:5: ( 'Number' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:7: 'Number'
            {
            match("Number"); 


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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:5: ( 'String' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:7: 'String'
            {
            match("String"); 


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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:5: ( 'Dimmer' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:7: 'Dimmer'
            {
            match("Dimmer"); 


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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:5: ( 'Contact' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:7: 'Contact'
            {
            match("Contact"); 


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

    // $ANTLR start RULE_ID
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:787:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:787:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:787:11: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='^') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:787:11: '^'
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

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:787:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:789:10: ( ( '0' .. '9' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:789:12: ( '0' .. '9' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:789:12: ( '0' .. '9' )+
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
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:789:13: '0' .. '9'
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
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
                    new NoViableAltException("791:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
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
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
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
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:62: ~ ( ( '\\\\' | '\"' ) )
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
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:82: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:87: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
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
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:88: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
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
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:791:129: ~ ( ( '\\\\' | '\\'' ) )
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:793:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:793:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:793:24: ( options {greedy=false; } : . )*
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
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:793:52: .
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='\t')||(LA8_0>='\u000B' && LA8_0<='\f')||(LA8_0>='\u000E' && LA8_0<='\uFFFE')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:24: ~ ( ( '\\n' | '\\r' ) )
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

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:40: ( ( '\\r' )? '\\n' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:41: ( '\\r' )? '\\n'
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:41: ( '\\r' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='\r') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:795:41: '\\r'
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:797:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:797:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:797:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
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
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:799:16: ( . )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:799:18: .
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
        // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:8: ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt12=26;
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
                    int LA12_52 = input.LA(4);

                    if ( (LA12_52=='u') ) {
                        int LA12_61 = input.LA(5);

                        if ( (LA12_61=='p') ) {
                            int LA12_69 = input.LA(6);

                            if ( ((LA12_69>='0' && LA12_69<='9')||(LA12_69>='A' && LA12_69<='Z')||LA12_69=='_'||(LA12_69>='a' && LA12_69<='z')) ) {
                                alt12=20;
                            }
                            else {
                                alt12=8;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='[') ) {
            alt12=9;
        }
        else if ( (LA12_0==']') ) {
            alt12=10;
        }
        else if ( (LA12_0=='S') ) {
            switch ( input.LA(2) ) {
            case 'w':
                {
                int LA12_38 = input.LA(3);

                if ( (LA12_38=='i') ) {
                    int LA12_53 = input.LA(4);

                    if ( (LA12_53=='t') ) {
                        int LA12_62 = input.LA(5);

                        if ( (LA12_62=='c') ) {
                            int LA12_70 = input.LA(6);

                            if ( (LA12_70=='h') ) {
                                int LA12_77 = input.LA(7);

                                if ( ((LA12_77>='0' && LA12_77<='9')||(LA12_77>='A' && LA12_77<='Z')||LA12_77=='_'||(LA12_77>='a' && LA12_77<='z')) ) {
                                    alt12=20;
                                }
                                else {
                                    alt12=11;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
                }
                break;
            case 't':
                {
                int LA12_39 = input.LA(3);

                if ( (LA12_39=='r') ) {
                    int LA12_54 = input.LA(4);

                    if ( (LA12_54=='i') ) {
                        int LA12_63 = input.LA(5);

                        if ( (LA12_63=='n') ) {
                            int LA12_71 = input.LA(6);

                            if ( (LA12_71=='g') ) {
                                int LA12_78 = input.LA(7);

                                if ( ((LA12_78>='0' && LA12_78<='9')||(LA12_78>='A' && LA12_78<='Z')||LA12_78=='_'||(LA12_78>='a' && LA12_78<='z')) ) {
                                    alt12=20;
                                }
                                else {
                                    alt12=14;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
                }
                break;
            default:
                alt12=20;}

        }
        else if ( (LA12_0=='R') ) {
            int LA12_12 = input.LA(2);

            if ( (LA12_12=='o') ) {
                int LA12_40 = input.LA(3);

                if ( (LA12_40=='l') ) {
                    int LA12_55 = input.LA(4);

                    if ( (LA12_55=='l') ) {
                        int LA12_64 = input.LA(5);

                        if ( (LA12_64=='e') ) {
                            int LA12_72 = input.LA(6);

                            if ( (LA12_72=='r') ) {
                                int LA12_79 = input.LA(7);

                                if ( (LA12_79=='s') ) {
                                    int LA12_85 = input.LA(8);

                                    if ( (LA12_85=='h') ) {
                                        int LA12_89 = input.LA(9);

                                        if ( (LA12_89=='u') ) {
                                            int LA12_91 = input.LA(10);

                                            if ( (LA12_91=='t') ) {
                                                int LA12_92 = input.LA(11);

                                                if ( (LA12_92=='t') ) {
                                                    int LA12_93 = input.LA(12);

                                                    if ( (LA12_93=='e') ) {
                                                        int LA12_94 = input.LA(13);

                                                        if ( (LA12_94=='r') ) {
                                                            int LA12_95 = input.LA(14);

                                                            if ( ((LA12_95>='0' && LA12_95<='9')||(LA12_95>='A' && LA12_95<='Z')||LA12_95=='_'||(LA12_95>='a' && LA12_95<='z')) ) {
                                                                alt12=20;
                                                            }
                                                            else {
                                                                alt12=12;}
                                                        }
                                                        else {
                                                            alt12=20;}
                                                    }
                                                    else {
                                                        alt12=20;}
                                                }
                                                else {
                                                    alt12=20;}
                                            }
                                            else {
                                                alt12=20;}
                                        }
                                        else {
                                            alt12=20;}
                                    }
                                    else {
                                        alt12=20;}
                                }
                                else {
                                    alt12=20;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='N') ) {
            int LA12_13 = input.LA(2);

            if ( (LA12_13=='u') ) {
                int LA12_41 = input.LA(3);

                if ( (LA12_41=='m') ) {
                    int LA12_56 = input.LA(4);

                    if ( (LA12_56=='b') ) {
                        int LA12_65 = input.LA(5);

                        if ( (LA12_65=='e') ) {
                            int LA12_73 = input.LA(6);

                            if ( (LA12_73=='r') ) {
                                int LA12_80 = input.LA(7);

                                if ( ((LA12_80>='0' && LA12_80<='9')||(LA12_80>='A' && LA12_80<='Z')||LA12_80=='_'||(LA12_80>='a' && LA12_80<='z')) ) {
                                    alt12=20;
                                }
                                else {
                                    alt12=13;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='D') ) {
            int LA12_14 = input.LA(2);

            if ( (LA12_14=='i') ) {
                int LA12_42 = input.LA(3);

                if ( (LA12_42=='m') ) {
                    int LA12_57 = input.LA(4);

                    if ( (LA12_57=='m') ) {
                        int LA12_66 = input.LA(5);

                        if ( (LA12_66=='e') ) {
                            int LA12_74 = input.LA(6);

                            if ( (LA12_74=='r') ) {
                                int LA12_81 = input.LA(7);

                                if ( ((LA12_81>='0' && LA12_81<='9')||(LA12_81>='A' && LA12_81<='Z')||LA12_81=='_'||(LA12_81>='a' && LA12_81<='z')) ) {
                                    alt12=20;
                                }
                                else {
                                    alt12=15;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='C') ) {
            int LA12_15 = input.LA(2);

            if ( (LA12_15=='o') ) {
                int LA12_43 = input.LA(3);

                if ( (LA12_43=='n') ) {
                    int LA12_58 = input.LA(4);

                    if ( (LA12_58=='t') ) {
                        int LA12_67 = input.LA(5);

                        if ( (LA12_67=='a') ) {
                            int LA12_75 = input.LA(6);

                            if ( (LA12_75=='c') ) {
                                int LA12_82 = input.LA(7);

                                if ( (LA12_82=='t') ) {
                                    int LA12_88 = input.LA(8);

                                    if ( ((LA12_88>='0' && LA12_88<='9')||(LA12_88>='A' && LA12_88<='Z')||LA12_88=='_'||(LA12_88>='a' && LA12_88<='z')) ) {
                                        alt12=20;
                                    }
                                    else {
                                        alt12=16;}
                                }
                                else {
                                    alt12=20;}
                            }
                            else {
                                alt12=20;}
                        }
                        else {
                            alt12=20;}
                    }
                    else {
                        alt12=20;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='=') ) {
            alt12=17;
        }
        else if ( (LA12_0=='A') ) {
            int LA12_17 = input.LA(2);

            if ( (LA12_17=='N') ) {
                int LA12_45 = input.LA(3);

                if ( (LA12_45=='D') ) {
                    int LA12_59 = input.LA(4);

                    if ( ((LA12_59>='0' && LA12_59<='9')||(LA12_59>='A' && LA12_59<='Z')||LA12_59=='_'||(LA12_59>='a' && LA12_59<='z')) ) {
                        alt12=20;
                    }
                    else {
                        alt12=18;}
                }
                else {
                    alt12=20;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='O') ) {
            int LA12_18 = input.LA(2);

            if ( (LA12_18=='R') ) {
                int LA12_46 = input.LA(3);

                if ( ((LA12_46>='0' && LA12_46<='9')||(LA12_46>='A' && LA12_46<='Z')||LA12_46=='_'||(LA12_46>='a' && LA12_46<='z')) ) {
                    alt12=20;
                }
                else {
                    alt12=19;}
            }
            else {
                alt12=20;}
        }
        else if ( (LA12_0=='^') ) {
            int LA12_19 = input.LA(2);

            if ( ((LA12_19>='A' && LA12_19<='Z')||LA12_19=='_'||(LA12_19>='a' && LA12_19<='z')) ) {
                alt12=20;
            }
            else {
                alt12=26;}
        }
        else if ( (LA12_0=='B'||(LA12_0>='E' && LA12_0<='F')||(LA12_0>='H' && LA12_0<='M')||(LA12_0>='P' && LA12_0<='Q')||(LA12_0>='T' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {
            alt12=20;
        }
        else if ( ((LA12_0>='0' && LA12_0<='9')) ) {
            alt12=21;
        }
        else if ( (LA12_0=='\"') ) {
            int LA12_22 = input.LA(2);

            if ( ((LA12_22>='\u0000' && LA12_22<='\uFFFE')) ) {
                alt12=22;
            }
            else {
                alt12=26;}
        }
        else if ( (LA12_0=='\'') ) {
            int LA12_23 = input.LA(2);

            if ( ((LA12_23>='\u0000' && LA12_23<='\uFFFE')) ) {
                alt12=22;
            }
            else {
                alt12=26;}
        }
        else if ( (LA12_0=='/') ) {
            switch ( input.LA(2) ) {
            case '*':
                {
                alt12=23;
                }
                break;
            case '/':
                {
                alt12=24;
                }
                break;
            default:
                alt12=26;}

        }
        else if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
            alt12=25;
        }
        else if ( ((LA12_0>='\u0000' && LA12_0<='\b')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\u001F')||LA12_0=='!'||(LA12_0>='#' && LA12_0<='&')||(LA12_0>='*' && LA12_0<='+')||(LA12_0>='-' && LA12_0<='.')||(LA12_0>=':' && LA12_0<=';')||(LA12_0>='?' && LA12_0<='@')||LA12_0=='\\'||LA12_0=='`'||LA12_0=='|'||(LA12_0>='~' && LA12_0<='\uFFFE')) ) {
            alt12=26;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );", 12, 0, input);

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
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:86: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 21 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:94: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 22 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:103: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 23 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:115: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 24 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:131: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 25 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:147: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 26 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:155: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


 

}