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
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=10;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int RULE_SL_COMMENT=8;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__30=30;
    public static final int T__19=19;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_STRING=5;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int RULE_INT=6;
    public static final int RULE_WS=9;

    // delegates
    // delegators

    public InternalItemsLexer() {;} 
    public InternalItemsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalItemsLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g"; }

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:11:7: ( '<' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:11:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:12:7: ( '>' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:12:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:13:7: ( '(' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:13:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:14:7: ( ',' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:14:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:15:7: ( ')' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:15:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:16:7: ( '{' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:16:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:17:7: ( '}' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:17:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:7: ( 'Group' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:18:9: 'Group'
            {
            match("Group"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:7: ( ':' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:19:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:7: ( 'Switch' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:20:9: 'Switch'
            {
            match("Switch"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:7: ( 'Rollershutter' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:21:9: 'Rollershutter'
            {
            match("Rollershutter"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:7: ( 'Number' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:22:9: 'Number'
            {
            match("Number"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:7: ( 'String' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:23:9: 'String'
            {
            match("String"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:7: ( 'Dimmer' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:24:9: 'Dimmer'
            {
            match("Dimmer"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:7: ( 'Contact' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:25:9: 'Contact'
            {
            match("Contact"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:26:7: ( 'DateTime' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:26:9: 'DateTime'
            {
            match("DateTime"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:27:7: ( '=' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:27:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:28:7: ( 'AND' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:28:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:29:7: ( 'OR' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:29:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:30:7: ( 'AVG' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:30:9: 'AVG'
            {
            match("AVG"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:31:7: ( 'MAX' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:31:9: 'MAX'
            {
            match("MAX"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:32:7: ( 'MIN' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:32:9: 'MIN'
            {
            match("MIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:685:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:685:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:685:11: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='^') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:685:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:685:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
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
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:687:10: ( ( '0' .. '9' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:687:12: ( '0' .. '9' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:687:12: ( '0' .. '9' )+
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
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:687:13: '0' .. '9'
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
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
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\\') ) {
                            alt4=1;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFF')) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:66: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


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
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:86: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:91: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\\') ) {
                            alt5=1;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFF')) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:92: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:689:137: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:691:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:691:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:691:24: ( options {greedy=false; } : . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='*') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='/') ) {
                        alt7=2;
                    }
                    else if ( ((LA7_1>='\u0000' && LA7_1<='.')||(LA7_1>='0' && LA7_1<='\uFFFF')) ) {
                        alt7=1;
                    }


                }
                else if ( ((LA7_0>='\u0000' && LA7_0<=')')||(LA7_0>='+' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:691:52: .
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='\t')||(LA8_0>='\u000B' && LA8_0<='\f')||(LA8_0>='\u000E' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:40: ( ( '\\r' )? '\\n' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:41: ( '\\r' )? '\\n'
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:41: ( '\\r' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='\r') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:41: '\\r'
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:695:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:695:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:695:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
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
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:697:16: ( . )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:697:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:8: ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt12=29;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:10: T__11
                {
                mT__11(); 

                }
                break;
            case 2 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:16: T__12
                {
                mT__12(); 

                }
                break;
            case 3 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:22: T__13
                {
                mT__13(); 

                }
                break;
            case 4 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:28: T__14
                {
                mT__14(); 

                }
                break;
            case 5 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:34: T__15
                {
                mT__15(); 

                }
                break;
            case 6 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:40: T__16
                {
                mT__16(); 

                }
                break;
            case 7 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:46: T__17
                {
                mT__17(); 

                }
                break;
            case 8 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:52: T__18
                {
                mT__18(); 

                }
                break;
            case 9 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:58: T__19
                {
                mT__19(); 

                }
                break;
            case 10 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:64: T__20
                {
                mT__20(); 

                }
                break;
            case 11 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:70: T__21
                {
                mT__21(); 

                }
                break;
            case 12 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:76: T__22
                {
                mT__22(); 

                }
                break;
            case 13 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:82: T__23
                {
                mT__23(); 

                }
                break;
            case 14 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:88: T__24
                {
                mT__24(); 

                }
                break;
            case 15 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:94: T__25
                {
                mT__25(); 

                }
                break;
            case 16 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:100: T__26
                {
                mT__26(); 

                }
                break;
            case 17 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:106: T__27
                {
                mT__27(); 

                }
                break;
            case 18 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:112: T__28
                {
                mT__28(); 

                }
                break;
            case 19 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:118: T__29
                {
                mT__29(); 

                }
                break;
            case 20 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:124: T__30
                {
                mT__30(); 

                }
                break;
            case 21 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:130: T__31
                {
                mT__31(); 

                }
                break;
            case 22 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:136: T__32
                {
                mT__32(); 

                }
                break;
            case 23 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:142: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 24 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:150: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 25 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:159: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 26 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:171: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 27 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:187: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 28 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:203: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 29 :
                // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:1:211: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\10\uffff\1\43\1\uffff\5\43\1\uffff\3\43\1\32\2\uffff\3\32\11\uffff"+
        "\1\43\2\uffff\7\43\1\uffff\2\43\1\101\2\43\5\uffff\10\43\1\114\1"+
        "\115\1\uffff\1\116\1\117\10\43\4\uffff\1\130\7\43\1\uffff\1\140"+
        "\1\141\1\43\1\143\1\144\2\43\2\uffff\1\43\2\uffff\1\43\1\151\1\43"+
        "\1\153\1\uffff\1\43\1\uffff\3\43\1\160\1\uffff";
    static final String DFA12_eofS =
        "\161\uffff";
    static final String DFA12_minS =
        "\1\0\7\uffff\1\162\1\uffff\1\164\1\157\1\165\1\141\1\157\1\uffff"+
        "\1\116\1\122\2\101\2\uffff\2\0\1\52\11\uffff\1\157\2\uffff\1\151"+
        "\1\162\1\154\2\155\1\164\1\156\1\uffff\1\104\1\107\1\60\1\130\1"+
        "\116\5\uffff\1\165\1\164\1\151\1\154\1\142\1\155\1\145\1\164\2\60"+
        "\1\uffff\2\60\1\160\1\143\1\156\3\145\1\124\1\141\4\uffff\1\60\1"+
        "\150\1\147\3\162\1\151\1\143\1\uffff\2\60\1\163\2\60\1\155\1\164"+
        "\2\uffff\1\150\2\uffff\1\145\1\60\1\165\1\60\1\uffff\1\164\1\uffff"+
        "\1\164\1\145\1\162\1\60\1\uffff";
    static final String DFA12_maxS =
        "\1\uffff\7\uffff\1\162\1\uffff\1\167\1\157\1\165\1\151\1\157\1"+
        "\uffff\1\126\1\122\1\111\1\172\2\uffff\2\uffff\1\57\11\uffff\1\157"+
        "\2\uffff\1\151\1\162\1\154\2\155\1\164\1\156\1\uffff\1\104\1\107"+
        "\1\172\1\130\1\116\5\uffff\1\165\1\164\1\151\1\154\1\142\1\155\1"+
        "\145\1\164\2\172\1\uffff\2\172\1\160\1\143\1\156\3\145\1\124\1\141"+
        "\4\uffff\1\172\1\150\1\147\3\162\1\151\1\143\1\uffff\2\172\1\163"+
        "\2\172\1\155\1\164\2\uffff\1\150\2\uffff\1\145\1\172\1\165\1\172"+
        "\1\uffff\1\164\1\uffff\1\164\1\145\1\162\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\uffff\1\11\5\uffff\1\21"+
        "\4\uffff\1\27\1\30\3\uffff\1\34\1\35\1\1\1\2\1\3\1\4\1\5\1\6\1\7"+
        "\1\uffff\1\27\1\11\7\uffff\1\21\5\uffff\1\30\1\31\1\32\1\33\1\34"+
        "\12\uffff\1\23\12\uffff\1\22\1\24\1\25\1\26\10\uffff\1\10\7\uffff"+
        "\1\12\1\15\1\uffff\1\14\1\16\4\uffff\1\17\1\uffff\1\20\4\uffff\1"+
        "\13";
    static final String DFA12_specialS =
        "\1\1\25\uffff\1\0\1\2\131\uffff}>";
    static final String[] DFA12_transitionS = {
            "\11\32\2\31\2\32\1\31\22\32\1\31\1\32\1\26\4\32\1\27\1\3\1"+
            "\5\2\32\1\4\2\32\1\30\12\25\1\11\1\32\1\1\1\17\1\2\2\32\1\20"+
            "\1\24\1\16\1\15\2\24\1\10\5\24\1\22\1\14\1\21\2\24\1\13\1\12"+
            "\7\24\3\32\1\23\1\24\1\32\32\24\1\6\1\32\1\7\uff82\32",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\42",
            "",
            "\1\46\2\uffff\1\45",
            "\1\47",
            "\1\50",
            "\1\52\7\uffff\1\51",
            "\1\53",
            "",
            "\1\55\7\uffff\1\56",
            "\1\57",
            "\1\60\7\uffff\1\61",
            "\32\43\4\uffff\1\43\1\uffff\32\43",
            "",
            "",
            "\0\63",
            "\0\63",
            "\1\64\4\uffff\1\65",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\67",
            "",
            "",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "",
            "\1\77",
            "\1\100",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\102",
            "\1\103",
            "",
            "",
            "",
            "",
            "",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "",
            "",
            "",
            "",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\142",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\145",
            "\1\146",
            "",
            "",
            "\1\147",
            "",
            "",
            "\1\150",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "\1\152",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            "",
            "\1\154",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\12\43\7\uffff\32\43\4\uffff\1\43\1\uffff\32\43",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_22 = input.LA(1);

                        s = -1;
                        if ( ((LA12_22>='\u0000' && LA12_22<='\uFFFF')) ) {s = 51;}

                        else s = 26;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_0 = input.LA(1);

                        s = -1;
                        if ( (LA12_0=='<') ) {s = 1;}

                        else if ( (LA12_0=='>') ) {s = 2;}

                        else if ( (LA12_0=='(') ) {s = 3;}

                        else if ( (LA12_0==',') ) {s = 4;}

                        else if ( (LA12_0==')') ) {s = 5;}

                        else if ( (LA12_0=='{') ) {s = 6;}

                        else if ( (LA12_0=='}') ) {s = 7;}

                        else if ( (LA12_0=='G') ) {s = 8;}

                        else if ( (LA12_0==':') ) {s = 9;}

                        else if ( (LA12_0=='S') ) {s = 10;}

                        else if ( (LA12_0=='R') ) {s = 11;}

                        else if ( (LA12_0=='N') ) {s = 12;}

                        else if ( (LA12_0=='D') ) {s = 13;}

                        else if ( (LA12_0=='C') ) {s = 14;}

                        else if ( (LA12_0=='=') ) {s = 15;}

                        else if ( (LA12_0=='A') ) {s = 16;}

                        else if ( (LA12_0=='O') ) {s = 17;}

                        else if ( (LA12_0=='M') ) {s = 18;}

                        else if ( (LA12_0=='^') ) {s = 19;}

                        else if ( (LA12_0=='B'||(LA12_0>='E' && LA12_0<='F')||(LA12_0>='H' && LA12_0<='L')||(LA12_0>='P' && LA12_0<='Q')||(LA12_0>='T' && LA12_0<='Z')||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {s = 20;}

                        else if ( ((LA12_0>='0' && LA12_0<='9')) ) {s = 21;}

                        else if ( (LA12_0=='\"') ) {s = 22;}

                        else if ( (LA12_0=='\'') ) {s = 23;}

                        else if ( (LA12_0=='/') ) {s = 24;}

                        else if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {s = 25;}

                        else if ( ((LA12_0>='\u0000' && LA12_0<='\b')||(LA12_0>='\u000B' && LA12_0<='\f')||(LA12_0>='\u000E' && LA12_0<='\u001F')||LA12_0=='!'||(LA12_0>='#' && LA12_0<='&')||(LA12_0>='*' && LA12_0<='+')||(LA12_0>='-' && LA12_0<='.')||LA12_0==';'||(LA12_0>='?' && LA12_0<='@')||(LA12_0>='[' && LA12_0<=']')||LA12_0=='`'||LA12_0=='|'||(LA12_0>='~' && LA12_0<='\uFFFF')) ) {s = 26;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_23 = input.LA(1);

                        s = -1;
                        if ( ((LA12_23>='\u0000' && LA12_23<='\uFFFF')) ) {s = 51;}

                        else s = 26;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}