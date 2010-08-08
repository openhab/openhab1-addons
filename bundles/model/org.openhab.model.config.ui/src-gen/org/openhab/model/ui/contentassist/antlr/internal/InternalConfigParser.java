package org.openhab.model.ui.contentassist.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.xtext.parsetree.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.DFA;
import org.openhab.model.services.ConfigGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalConfigParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Switch'", "'Rollerblind'", "'Measurement'", "'String'", "'Dimmer'", "'Contact'", "'Group'", "'<'", "'>'", "'('", "')'", "','", "'{'", "'}'", "'='"
    };
    public static final int RULE_ID=4;
    public static final int RULE_STRING=5;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_INT=6;
    public static final int RULE_WS=9;
    public static final int RULE_SL_COMMENT=8;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=7;

        public InternalConfigParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g"; }


     
     	private ConfigGrammarAccess grammarAccess;
     	
        public void setGrammarAccess(ConfigGrammarAccess grammarAccess) {
        	this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected Grammar getGrammar() {
        	return grammarAccess.getGrammar();
        }
        
        @Override
        protected String getValueForTokenName(String tokenName) {
        	return tokenName;
        }




    // $ANTLR start entryRuleModel
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:61:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:62:1: ( ruleModel EOF )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:63:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel61);
            ruleModel();
            _fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel68); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end entryRuleModel


    // $ANTLR start ruleModel
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:70:1: ruleModel : ( ( rule__Model__ItemsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:74:2: ( ( ( rule__Model__ItemsAssignment )* ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:75:1: ( ( rule__Model__ItemsAssignment )* )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:75:1: ( ( rule__Model__ItemsAssignment )* )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:76:1: ( rule__Model__ItemsAssignment )*
            {
             before(grammarAccess.getModelAccess().getItemsAssignment()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:77:1: ( rule__Model__ItemsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||(LA1_0>=11 && LA1_0<=17)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:77:2: rule__Model__ItemsAssignment
            	    {
            	    pushFollow(FOLLOW_rule__Model__ItemsAssignment_in_ruleModel94);
            	    rule__Model__ItemsAssignment();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getItemsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleModel


    // $ANTLR start entryRuleItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:89:1: entryRuleItem : ruleItem EOF ;
    public final void entryRuleItem() throws RecognitionException {
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:90:1: ( ruleItem EOF )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:91:1: ruleItem EOF
            {
             before(grammarAccess.getItemRule()); 
            pushFollow(FOLLOW_ruleItem_in_entryRuleItem122);
            ruleItem();
            _fsp--;

             after(grammarAccess.getItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleItem129); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end entryRuleItem


    // $ANTLR start ruleItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:98:1: ruleItem : ( ( rule__Item__Alternatives ) ) ;
    public final void ruleItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:102:2: ( ( ( rule__Item__Alternatives ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:103:1: ( ( rule__Item__Alternatives ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:103:1: ( ( rule__Item__Alternatives ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:104:1: ( rule__Item__Alternatives )
            {
             before(grammarAccess.getItemAccess().getAlternatives()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:105:1: ( rule__Item__Alternatives )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:105:2: rule__Item__Alternatives
            {
            pushFollow(FOLLOW_rule__Item__Alternatives_in_ruleItem155);
            rule__Item__Alternatives();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleItem


    // $ANTLR start entryRuleGroupItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:117:1: entryRuleGroupItem : ruleGroupItem EOF ;
    public final void entryRuleGroupItem() throws RecognitionException {
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:118:1: ( ruleGroupItem EOF )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:119:1: ruleGroupItem EOF
            {
             before(grammarAccess.getGroupItemRule()); 
            pushFollow(FOLLOW_ruleGroupItem_in_entryRuleGroupItem182);
            ruleGroupItem();
            _fsp--;

             after(grammarAccess.getGroupItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupItem189); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end entryRuleGroupItem


    // $ANTLR start ruleGroupItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:126:1: ruleGroupItem : ( ( rule__GroupItem__Group__0 ) ) ;
    public final void ruleGroupItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:130:2: ( ( ( rule__GroupItem__Group__0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:131:1: ( ( rule__GroupItem__Group__0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:131:1: ( ( rule__GroupItem__Group__0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:132:1: ( rule__GroupItem__Group__0 )
            {
             before(grammarAccess.getGroupItemAccess().getGroup()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:133:1: ( rule__GroupItem__Group__0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:133:2: rule__GroupItem__Group__0
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__0_in_ruleGroupItem215);
            rule__GroupItem__Group__0();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleGroupItem


    // $ANTLR start entryRuleNormalItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:145:1: entryRuleNormalItem : ruleNormalItem EOF ;
    public final void entryRuleNormalItem() throws RecognitionException {
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:146:1: ( ruleNormalItem EOF )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:147:1: ruleNormalItem EOF
            {
             before(grammarAccess.getNormalItemRule()); 
            pushFollow(FOLLOW_ruleNormalItem_in_entryRuleNormalItem242);
            ruleNormalItem();
            _fsp--;

             after(grammarAccess.getNormalItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNormalItem249); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end entryRuleNormalItem


    // $ANTLR start ruleNormalItem
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:154:1: ruleNormalItem : ( ( rule__NormalItem__Group__0 ) ) ;
    public final void ruleNormalItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:158:2: ( ( ( rule__NormalItem__Group__0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:159:1: ( ( rule__NormalItem__Group__0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:159:1: ( ( rule__NormalItem__Group__0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:160:1: ( rule__NormalItem__Group__0 )
            {
             before(grammarAccess.getNormalItemAccess().getGroup()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:161:1: ( rule__NormalItem__Group__0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:161:2: rule__NormalItem__Group__0
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__0_in_ruleNormalItem275);
            rule__NormalItem__Group__0();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleNormalItem


    // $ANTLR start entryRuleBinding
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:173:1: entryRuleBinding : ruleBinding EOF ;
    public final void entryRuleBinding() throws RecognitionException {
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:174:1: ( ruleBinding EOF )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:175:1: ruleBinding EOF
            {
             before(grammarAccess.getBindingRule()); 
            pushFollow(FOLLOW_ruleBinding_in_entryRuleBinding302);
            ruleBinding();
            _fsp--;

             after(grammarAccess.getBindingRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBinding309); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end entryRuleBinding


    // $ANTLR start ruleBinding
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:182:1: ruleBinding : ( ( rule__Binding__Group__0 ) ) ;
    public final void ruleBinding() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:186:2: ( ( ( rule__Binding__Group__0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:187:1: ( ( rule__Binding__Group__0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:187:1: ( ( rule__Binding__Group__0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:188:1: ( rule__Binding__Group__0 )
            {
             before(grammarAccess.getBindingAccess().getGroup()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:189:1: ( rule__Binding__Group__0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:189:2: rule__Binding__Group__0
            {
            pushFollow(FOLLOW_rule__Binding__Group__0_in_ruleBinding335);
            rule__Binding__Group__0();
            _fsp--;


            }

             after(grammarAccess.getBindingAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleBinding


    // $ANTLR start rule__Item__Alternatives
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:201:1: rule__Item__Alternatives : ( ( ruleNormalItem ) | ( ruleGroupItem ) );
    public final void rule__Item__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:205:1: ( ( ruleNormalItem ) | ( ruleGroupItem ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=11 && LA2_0<=16)) ) {
                alt2=1;
            }
            else if ( (LA2_0==17) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("201:1: rule__Item__Alternatives : ( ( ruleNormalItem ) | ( ruleGroupItem ) );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:206:1: ( ruleNormalItem )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:206:1: ( ruleNormalItem )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:207:1: ruleNormalItem
                    {
                     before(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleNormalItem_in_rule__Item__Alternatives371);
                    ruleNormalItem();
                    _fsp--;

                     after(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:212:6: ( ruleGroupItem )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:212:6: ( ruleGroupItem )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:213:1: ruleGroupItem
                    {
                     before(grammarAccess.getItemAccess().getGroupItemParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleGroupItem_in_rule__Item__Alternatives388);
                    ruleGroupItem();
                    _fsp--;

                     after(grammarAccess.getItemAccess().getGroupItemParserRuleCall_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Item__Alternatives


    // $ANTLR start rule__GroupItem__IconAlternatives_3_1_0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:223:1: rule__GroupItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__GroupItem__IconAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:227:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_ID) ) {
                alt3=1;
            }
            else if ( (LA3_0==RULE_STRING) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("223:1: rule__GroupItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:228:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:228:1: ( RULE_ID )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:229:1: RULE_ID
                    {
                     before(grammarAccess.getGroupItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__GroupItem__IconAlternatives_3_1_0420); 
                     after(grammarAccess.getGroupItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:234:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:234:6: ( RULE_STRING )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:235:1: RULE_STRING
                    {
                     before(grammarAccess.getGroupItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__GroupItem__IconAlternatives_3_1_0437); 
                     after(grammarAccess.getGroupItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__IconAlternatives_3_1_0


    // $ANTLR start rule__NormalItem__TypeAlternatives_0_0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:245:1: rule__NormalItem__TypeAlternatives_0_0 : ( ( 'Switch' ) | ( 'Rollerblind' ) | ( 'Measurement' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );
    public final void rule__NormalItem__TypeAlternatives_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:249:1: ( ( 'Switch' ) | ( 'Rollerblind' ) | ( 'Measurement' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) )
            int alt4=7;
            switch ( input.LA(1) ) {
            case 11:
                {
                alt4=1;
                }
                break;
            case 12:
                {
                alt4=2;
                }
                break;
            case 13:
                {
                alt4=3;
                }
                break;
            case 14:
                {
                alt4=4;
                }
                break;
            case 15:
                {
                alt4=5;
                }
                break;
            case 16:
                {
                alt4=6;
                }
                break;
            case RULE_ID:
                {
                alt4=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("245:1: rule__NormalItem__TypeAlternatives_0_0 : ( ( 'Switch' ) | ( 'Rollerblind' ) | ( 'Measurement' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:250:1: ( 'Switch' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:250:1: ( 'Switch' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:251:1: 'Switch'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0_0()); 
                    match(input,11,FOLLOW_11_in_rule__NormalItem__TypeAlternatives_0_0470); 
                     after(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:258:6: ( 'Rollerblind' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:258:6: ( 'Rollerblind' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:259:1: 'Rollerblind'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeRollerblindKeyword_0_0_1()); 
                    match(input,12,FOLLOW_12_in_rule__NormalItem__TypeAlternatives_0_0490); 
                     after(grammarAccess.getNormalItemAccess().getTypeRollerblindKeyword_0_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:266:6: ( 'Measurement' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:266:6: ( 'Measurement' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:267:1: 'Measurement'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeMeasurementKeyword_0_0_2()); 
                    match(input,13,FOLLOW_13_in_rule__NormalItem__TypeAlternatives_0_0510); 
                     after(grammarAccess.getNormalItemAccess().getTypeMeasurementKeyword_0_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:274:6: ( 'String' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:274:6: ( 'String' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:275:1: 'String'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_0_3()); 
                    match(input,14,FOLLOW_14_in_rule__NormalItem__TypeAlternatives_0_0530); 
                     after(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_0_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:282:6: ( 'Dimmer' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:282:6: ( 'Dimmer' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:283:1: 'Dimmer'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_0_4()); 
                    match(input,15,FOLLOW_15_in_rule__NormalItem__TypeAlternatives_0_0550); 
                     after(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_0_4()); 

                    }


                    }
                    break;
                case 6 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:290:6: ( 'Contact' )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:290:6: ( 'Contact' )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:291:1: 'Contact'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_0_5()); 
                    match(input,16,FOLLOW_16_in_rule__NormalItem__TypeAlternatives_0_0570); 
                     after(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_0_5()); 

                    }


                    }
                    break;
                case 7 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:298:6: ( RULE_ID )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:298:6: ( RULE_ID )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:299:1: RULE_ID
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_0_6()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__TypeAlternatives_0_0589); 
                     after(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_0_6()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__TypeAlternatives_0_0


    // $ANTLR start rule__NormalItem__IconAlternatives_3_1_0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:309:1: rule__NormalItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__NormalItem__IconAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:313:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_ID) ) {
                alt5=1;
            }
            else if ( (LA5_0==RULE_STRING) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("309:1: rule__NormalItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:314:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:314:1: ( RULE_ID )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:315:1: RULE_ID
                    {
                     before(grammarAccess.getNormalItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__IconAlternatives_3_1_0621); 
                     after(grammarAccess.getNormalItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:320:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:320:6: ( RULE_STRING )
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:321:1: RULE_STRING
                    {
                     before(grammarAccess.getNormalItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__NormalItem__IconAlternatives_3_1_0638); 
                     after(grammarAccess.getNormalItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__IconAlternatives_3_1_0


    // $ANTLR start rule__GroupItem__Group__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:333:1: rule__GroupItem__Group__0 : rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1 ;
    public final void rule__GroupItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:337:1: ( rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:338:2: rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__0__Impl_in_rule__GroupItem__Group__0668);
            rule__GroupItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group__1_in_rule__GroupItem__Group__0671);
            rule__GroupItem__Group__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__0


    // $ANTLR start rule__GroupItem__Group__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:345:1: rule__GroupItem__Group__0__Impl : ( 'Group' ) ;
    public final void rule__GroupItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:349:1: ( ( 'Group' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:350:1: ( 'Group' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:350:1: ( 'Group' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:351:1: 'Group'
            {
             before(grammarAccess.getGroupItemAccess().getGroupKeyword_0()); 
            match(input,17,FOLLOW_17_in_rule__GroupItem__Group__0__Impl699); 
             after(grammarAccess.getGroupItemAccess().getGroupKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__0__Impl


    // $ANTLR start rule__GroupItem__Group__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:364:1: rule__GroupItem__Group__1 : rule__GroupItem__Group__1__Impl rule__GroupItem__Group__2 ;
    public final void rule__GroupItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:368:1: ( rule__GroupItem__Group__1__Impl rule__GroupItem__Group__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:369:2: rule__GroupItem__Group__1__Impl rule__GroupItem__Group__2
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__1__Impl_in_rule__GroupItem__Group__1730);
            rule__GroupItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group__2_in_rule__GroupItem__Group__1733);
            rule__GroupItem__Group__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__1


    // $ANTLR start rule__GroupItem__Group__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:376:1: rule__GroupItem__Group__1__Impl : ( ( rule__GroupItem__NameAssignment_1 ) ) ;
    public final void rule__GroupItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:380:1: ( ( ( rule__GroupItem__NameAssignment_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:381:1: ( ( rule__GroupItem__NameAssignment_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:381:1: ( ( rule__GroupItem__NameAssignment_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:382:1: ( rule__GroupItem__NameAssignment_1 )
            {
             before(grammarAccess.getGroupItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:383:1: ( rule__GroupItem__NameAssignment_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:383:2: rule__GroupItem__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__GroupItem__NameAssignment_1_in_rule__GroupItem__Group__1__Impl760);
            rule__GroupItem__NameAssignment_1();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__1__Impl


    // $ANTLR start rule__GroupItem__Group__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:393:1: rule__GroupItem__Group__2 : rule__GroupItem__Group__2__Impl rule__GroupItem__Group__3 ;
    public final void rule__GroupItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:397:1: ( rule__GroupItem__Group__2__Impl rule__GroupItem__Group__3 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:398:2: rule__GroupItem__Group__2__Impl rule__GroupItem__Group__3
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__2__Impl_in_rule__GroupItem__Group__2790);
            rule__GroupItem__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group__3_in_rule__GroupItem__Group__2793);
            rule__GroupItem__Group__3();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__2


    // $ANTLR start rule__GroupItem__Group__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:405:1: rule__GroupItem__Group__2__Impl : ( ( rule__GroupItem__LabelAssignment_2 )? ) ;
    public final void rule__GroupItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:409:1: ( ( ( rule__GroupItem__LabelAssignment_2 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:410:1: ( ( rule__GroupItem__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:410:1: ( ( rule__GroupItem__LabelAssignment_2 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:411:1: ( rule__GroupItem__LabelAssignment_2 )?
            {
             before(grammarAccess.getGroupItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:412:1: ( rule__GroupItem__LabelAssignment_2 )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_STRING) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:412:2: rule__GroupItem__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__GroupItem__LabelAssignment_2_in_rule__GroupItem__Group__2__Impl820);
                    rule__GroupItem__LabelAssignment_2();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGroupItemAccess().getLabelAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__2__Impl


    // $ANTLR start rule__GroupItem__Group__3
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:422:1: rule__GroupItem__Group__3 : rule__GroupItem__Group__3__Impl rule__GroupItem__Group__4 ;
    public final void rule__GroupItem__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:426:1: ( rule__GroupItem__Group__3__Impl rule__GroupItem__Group__4 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:427:2: rule__GroupItem__Group__3__Impl rule__GroupItem__Group__4
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__3__Impl_in_rule__GroupItem__Group__3851);
            rule__GroupItem__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group__4_in_rule__GroupItem__Group__3854);
            rule__GroupItem__Group__4();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__3


    // $ANTLR start rule__GroupItem__Group__3__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:434:1: rule__GroupItem__Group__3__Impl : ( ( rule__GroupItem__Group_3__0 )? ) ;
    public final void rule__GroupItem__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:438:1: ( ( ( rule__GroupItem__Group_3__0 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:439:1: ( ( rule__GroupItem__Group_3__0 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:439:1: ( ( rule__GroupItem__Group_3__0 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:440:1: ( rule__GroupItem__Group_3__0 )?
            {
             before(grammarAccess.getGroupItemAccess().getGroup_3()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:441:1: ( rule__GroupItem__Group_3__0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==18) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:441:2: rule__GroupItem__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__GroupItem__Group_3__0_in_rule__GroupItem__Group__3__Impl881);
                    rule__GroupItem__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGroupItemAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__3__Impl


    // $ANTLR start rule__GroupItem__Group__4
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:451:1: rule__GroupItem__Group__4 : rule__GroupItem__Group__4__Impl ;
    public final void rule__GroupItem__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:455:1: ( rule__GroupItem__Group__4__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:456:2: rule__GroupItem__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__4__Impl_in_rule__GroupItem__Group__4912);
            rule__GroupItem__Group__4__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__4


    // $ANTLR start rule__GroupItem__Group__4__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:462:1: rule__GroupItem__Group__4__Impl : ( ( rule__GroupItem__Group_4__0 )? ) ;
    public final void rule__GroupItem__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:466:1: ( ( ( rule__GroupItem__Group_4__0 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:467:1: ( ( rule__GroupItem__Group_4__0 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:467:1: ( ( rule__GroupItem__Group_4__0 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:468:1: ( rule__GroupItem__Group_4__0 )?
            {
             before(grammarAccess.getGroupItemAccess().getGroup_4()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:469:1: ( rule__GroupItem__Group_4__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==20) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:469:2: rule__GroupItem__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__GroupItem__Group_4__0_in_rule__GroupItem__Group__4__Impl939);
                    rule__GroupItem__Group_4__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGroupItemAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__4__Impl


    // $ANTLR start rule__GroupItem__Group_3__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:489:1: rule__GroupItem__Group_3__0 : rule__GroupItem__Group_3__0__Impl rule__GroupItem__Group_3__1 ;
    public final void rule__GroupItem__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:493:1: ( rule__GroupItem__Group_3__0__Impl rule__GroupItem__Group_3__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:494:2: rule__GroupItem__Group_3__0__Impl rule__GroupItem__Group_3__1
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_3__0__Impl_in_rule__GroupItem__Group_3__0980);
            rule__GroupItem__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_3__1_in_rule__GroupItem__Group_3__0983);
            rule__GroupItem__Group_3__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__0


    // $ANTLR start rule__GroupItem__Group_3__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:501:1: rule__GroupItem__Group_3__0__Impl : ( '<' ) ;
    public final void rule__GroupItem__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:505:1: ( ( '<' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:506:1: ( '<' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:506:1: ( '<' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:507:1: '<'
            {
             before(grammarAccess.getGroupItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,18,FOLLOW_18_in_rule__GroupItem__Group_3__0__Impl1011); 
             after(grammarAccess.getGroupItemAccess().getLessThanSignKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__0__Impl


    // $ANTLR start rule__GroupItem__Group_3__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:520:1: rule__GroupItem__Group_3__1 : rule__GroupItem__Group_3__1__Impl rule__GroupItem__Group_3__2 ;
    public final void rule__GroupItem__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:524:1: ( rule__GroupItem__Group_3__1__Impl rule__GroupItem__Group_3__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:525:2: rule__GroupItem__Group_3__1__Impl rule__GroupItem__Group_3__2
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_3__1__Impl_in_rule__GroupItem__Group_3__11042);
            rule__GroupItem__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_3__2_in_rule__GroupItem__Group_3__11045);
            rule__GroupItem__Group_3__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__1


    // $ANTLR start rule__GroupItem__Group_3__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:532:1: rule__GroupItem__Group_3__1__Impl : ( ( rule__GroupItem__IconAssignment_3_1 ) ) ;
    public final void rule__GroupItem__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:536:1: ( ( ( rule__GroupItem__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:537:1: ( ( rule__GroupItem__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:537:1: ( ( rule__GroupItem__IconAssignment_3_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:538:1: ( rule__GroupItem__IconAssignment_3_1 )
            {
             before(grammarAccess.getGroupItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:539:1: ( rule__GroupItem__IconAssignment_3_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:539:2: rule__GroupItem__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__GroupItem__IconAssignment_3_1_in_rule__GroupItem__Group_3__1__Impl1072);
            rule__GroupItem__IconAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getIconAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__1__Impl


    // $ANTLR start rule__GroupItem__Group_3__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:549:1: rule__GroupItem__Group_3__2 : rule__GroupItem__Group_3__2__Impl ;
    public final void rule__GroupItem__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:553:1: ( rule__GroupItem__Group_3__2__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:554:2: rule__GroupItem__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_3__2__Impl_in_rule__GroupItem__Group_3__21102);
            rule__GroupItem__Group_3__2__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__2


    // $ANTLR start rule__GroupItem__Group_3__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:560:1: rule__GroupItem__Group_3__2__Impl : ( '>' ) ;
    public final void rule__GroupItem__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:564:1: ( ( '>' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:565:1: ( '>' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:565:1: ( '>' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:566:1: '>'
            {
             before(grammarAccess.getGroupItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,19,FOLLOW_19_in_rule__GroupItem__Group_3__2__Impl1130); 
             after(grammarAccess.getGroupItemAccess().getGreaterThanSignKeyword_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_3__2__Impl


    // $ANTLR start rule__GroupItem__Group_4__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:585:1: rule__GroupItem__Group_4__0 : rule__GroupItem__Group_4__0__Impl rule__GroupItem__Group_4__1 ;
    public final void rule__GroupItem__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:589:1: ( rule__GroupItem__Group_4__0__Impl rule__GroupItem__Group_4__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:590:2: rule__GroupItem__Group_4__0__Impl rule__GroupItem__Group_4__1
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4__0__Impl_in_rule__GroupItem__Group_4__01167);
            rule__GroupItem__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_4__1_in_rule__GroupItem__Group_4__01170);
            rule__GroupItem__Group_4__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__0


    // $ANTLR start rule__GroupItem__Group_4__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:597:1: rule__GroupItem__Group_4__0__Impl : ( '(' ) ;
    public final void rule__GroupItem__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:601:1: ( ( '(' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:602:1: ( '(' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:602:1: ( '(' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:603:1: '('
            {
             before(grammarAccess.getGroupItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,20,FOLLOW_20_in_rule__GroupItem__Group_4__0__Impl1198); 
             after(grammarAccess.getGroupItemAccess().getLeftParenthesisKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__0__Impl


    // $ANTLR start rule__GroupItem__Group_4__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:616:1: rule__GroupItem__Group_4__1 : rule__GroupItem__Group_4__1__Impl rule__GroupItem__Group_4__2 ;
    public final void rule__GroupItem__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:620:1: ( rule__GroupItem__Group_4__1__Impl rule__GroupItem__Group_4__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:621:2: rule__GroupItem__Group_4__1__Impl rule__GroupItem__Group_4__2
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4__1__Impl_in_rule__GroupItem__Group_4__11229);
            rule__GroupItem__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_4__2_in_rule__GroupItem__Group_4__11232);
            rule__GroupItem__Group_4__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__1


    // $ANTLR start rule__GroupItem__Group_4__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:628:1: rule__GroupItem__Group_4__1__Impl : ( ( rule__GroupItem__GroupsAssignment_4_1 ) ) ;
    public final void rule__GroupItem__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:632:1: ( ( ( rule__GroupItem__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:633:1: ( ( rule__GroupItem__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:633:1: ( ( rule__GroupItem__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:634:1: ( rule__GroupItem__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getGroupItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:635:1: ( rule__GroupItem__GroupsAssignment_4_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:635:2: rule__GroupItem__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__GroupItem__GroupsAssignment_4_1_in_rule__GroupItem__Group_4__1__Impl1259);
            rule__GroupItem__GroupsAssignment_4_1();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getGroupsAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__1__Impl


    // $ANTLR start rule__GroupItem__Group_4__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:645:1: rule__GroupItem__Group_4__2 : rule__GroupItem__Group_4__2__Impl rule__GroupItem__Group_4__3 ;
    public final void rule__GroupItem__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:649:1: ( rule__GroupItem__Group_4__2__Impl rule__GroupItem__Group_4__3 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:650:2: rule__GroupItem__Group_4__2__Impl rule__GroupItem__Group_4__3
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4__2__Impl_in_rule__GroupItem__Group_4__21289);
            rule__GroupItem__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_4__3_in_rule__GroupItem__Group_4__21292);
            rule__GroupItem__Group_4__3();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__2


    // $ANTLR start rule__GroupItem__Group_4__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:657:1: rule__GroupItem__Group_4__2__Impl : ( ( rule__GroupItem__Group_4_2__0 )* ) ;
    public final void rule__GroupItem__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:661:1: ( ( ( rule__GroupItem__Group_4_2__0 )* ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:662:1: ( ( rule__GroupItem__Group_4_2__0 )* )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:662:1: ( ( rule__GroupItem__Group_4_2__0 )* )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:663:1: ( rule__GroupItem__Group_4_2__0 )*
            {
             before(grammarAccess.getGroupItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:664:1: ( rule__GroupItem__Group_4_2__0 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==22) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:664:2: rule__GroupItem__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__GroupItem__Group_4_2__0_in_rule__GroupItem__Group_4__2__Impl1319);
            	    rule__GroupItem__Group_4_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getGroupItemAccess().getGroup_4_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__2__Impl


    // $ANTLR start rule__GroupItem__Group_4__3
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:674:1: rule__GroupItem__Group_4__3 : rule__GroupItem__Group_4__3__Impl ;
    public final void rule__GroupItem__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:678:1: ( rule__GroupItem__Group_4__3__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:679:2: rule__GroupItem__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4__3__Impl_in_rule__GroupItem__Group_4__31350);
            rule__GroupItem__Group_4__3__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__3


    // $ANTLR start rule__GroupItem__Group_4__3__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:685:1: rule__GroupItem__Group_4__3__Impl : ( ')' ) ;
    public final void rule__GroupItem__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:689:1: ( ( ')' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:690:1: ( ')' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:690:1: ( ')' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:691:1: ')'
            {
             before(grammarAccess.getGroupItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,21,FOLLOW_21_in_rule__GroupItem__Group_4__3__Impl1378); 
             after(grammarAccess.getGroupItemAccess().getRightParenthesisKeyword_4_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4__3__Impl


    // $ANTLR start rule__GroupItem__Group_4_2__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:712:1: rule__GroupItem__Group_4_2__0 : rule__GroupItem__Group_4_2__0__Impl rule__GroupItem__Group_4_2__1 ;
    public final void rule__GroupItem__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:716:1: ( rule__GroupItem__Group_4_2__0__Impl rule__GroupItem__Group_4_2__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:717:2: rule__GroupItem__Group_4_2__0__Impl rule__GroupItem__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4_2__0__Impl_in_rule__GroupItem__Group_4_2__01417);
            rule__GroupItem__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group_4_2__1_in_rule__GroupItem__Group_4_2__01420);
            rule__GroupItem__Group_4_2__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4_2__0


    // $ANTLR start rule__GroupItem__Group_4_2__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:724:1: rule__GroupItem__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__GroupItem__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:728:1: ( ( ',' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:729:1: ( ',' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:729:1: ( ',' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:730:1: ','
            {
             before(grammarAccess.getGroupItemAccess().getCommaKeyword_4_2_0()); 
            match(input,22,FOLLOW_22_in_rule__GroupItem__Group_4_2__0__Impl1448); 
             after(grammarAccess.getGroupItemAccess().getCommaKeyword_4_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4_2__0__Impl


    // $ANTLR start rule__GroupItem__Group_4_2__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:743:1: rule__GroupItem__Group_4_2__1 : rule__GroupItem__Group_4_2__1__Impl ;
    public final void rule__GroupItem__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:747:1: ( rule__GroupItem__Group_4_2__1__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:748:2: rule__GroupItem__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__GroupItem__Group_4_2__1__Impl_in_rule__GroupItem__Group_4_2__11479);
            rule__GroupItem__Group_4_2__1__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4_2__1


    // $ANTLR start rule__GroupItem__Group_4_2__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:754:1: rule__GroupItem__Group_4_2__1__Impl : ( ( rule__GroupItem__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__GroupItem__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:758:1: ( ( ( rule__GroupItem__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:759:1: ( ( rule__GroupItem__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:759:1: ( ( rule__GroupItem__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:760:1: ( rule__GroupItem__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getGroupItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:761:1: ( rule__GroupItem__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:761:2: rule__GroupItem__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__GroupItem__GroupsAssignment_4_2_1_in_rule__GroupItem__Group_4_2__1__Impl1506);
            rule__GroupItem__GroupsAssignment_4_2_1();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getGroupsAssignment_4_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group_4_2__1__Impl


    // $ANTLR start rule__NormalItem__Group__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:775:1: rule__NormalItem__Group__0 : rule__NormalItem__Group__0__Impl rule__NormalItem__Group__1 ;
    public final void rule__NormalItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:779:1: ( rule__NormalItem__Group__0__Impl rule__NormalItem__Group__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:780:2: rule__NormalItem__Group__0__Impl rule__NormalItem__Group__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__0__Impl_in_rule__NormalItem__Group__01540);
            rule__NormalItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group__1_in_rule__NormalItem__Group__01543);
            rule__NormalItem__Group__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__0


    // $ANTLR start rule__NormalItem__Group__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:787:1: rule__NormalItem__Group__0__Impl : ( ( rule__NormalItem__TypeAssignment_0 ) ) ;
    public final void rule__NormalItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:791:1: ( ( ( rule__NormalItem__TypeAssignment_0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:792:1: ( ( rule__NormalItem__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:792:1: ( ( rule__NormalItem__TypeAssignment_0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:793:1: ( rule__NormalItem__TypeAssignment_0 )
            {
             before(grammarAccess.getNormalItemAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:794:1: ( rule__NormalItem__TypeAssignment_0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:794:2: rule__NormalItem__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__NormalItem__TypeAssignment_0_in_rule__NormalItem__Group__0__Impl1570);
            rule__NormalItem__TypeAssignment_0();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getTypeAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__0__Impl


    // $ANTLR start rule__NormalItem__Group__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:804:1: rule__NormalItem__Group__1 : rule__NormalItem__Group__1__Impl rule__NormalItem__Group__2 ;
    public final void rule__NormalItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:808:1: ( rule__NormalItem__Group__1__Impl rule__NormalItem__Group__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:809:2: rule__NormalItem__Group__1__Impl rule__NormalItem__Group__2
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__1__Impl_in_rule__NormalItem__Group__11600);
            rule__NormalItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group__2_in_rule__NormalItem__Group__11603);
            rule__NormalItem__Group__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__1


    // $ANTLR start rule__NormalItem__Group__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:816:1: rule__NormalItem__Group__1__Impl : ( ( rule__NormalItem__NameAssignment_1 ) ) ;
    public final void rule__NormalItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:820:1: ( ( ( rule__NormalItem__NameAssignment_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:821:1: ( ( rule__NormalItem__NameAssignment_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:821:1: ( ( rule__NormalItem__NameAssignment_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:822:1: ( rule__NormalItem__NameAssignment_1 )
            {
             before(grammarAccess.getNormalItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:823:1: ( rule__NormalItem__NameAssignment_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:823:2: rule__NormalItem__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__NormalItem__NameAssignment_1_in_rule__NormalItem__Group__1__Impl1630);
            rule__NormalItem__NameAssignment_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__1__Impl


    // $ANTLR start rule__NormalItem__Group__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:833:1: rule__NormalItem__Group__2 : rule__NormalItem__Group__2__Impl rule__NormalItem__Group__3 ;
    public final void rule__NormalItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:837:1: ( rule__NormalItem__Group__2__Impl rule__NormalItem__Group__3 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:838:2: rule__NormalItem__Group__2__Impl rule__NormalItem__Group__3
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__2__Impl_in_rule__NormalItem__Group__21660);
            rule__NormalItem__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group__3_in_rule__NormalItem__Group__21663);
            rule__NormalItem__Group__3();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__2


    // $ANTLR start rule__NormalItem__Group__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:845:1: rule__NormalItem__Group__2__Impl : ( ( rule__NormalItem__LabelAssignment_2 )? ) ;
    public final void rule__NormalItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:849:1: ( ( ( rule__NormalItem__LabelAssignment_2 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:850:1: ( ( rule__NormalItem__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:850:1: ( ( rule__NormalItem__LabelAssignment_2 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:851:1: ( rule__NormalItem__LabelAssignment_2 )?
            {
             before(grammarAccess.getNormalItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:852:1: ( rule__NormalItem__LabelAssignment_2 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==RULE_STRING) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:852:2: rule__NormalItem__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__NormalItem__LabelAssignment_2_in_rule__NormalItem__Group__2__Impl1690);
                    rule__NormalItem__LabelAssignment_2();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getNormalItemAccess().getLabelAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__2__Impl


    // $ANTLR start rule__NormalItem__Group__3
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:862:1: rule__NormalItem__Group__3 : rule__NormalItem__Group__3__Impl rule__NormalItem__Group__4 ;
    public final void rule__NormalItem__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:866:1: ( rule__NormalItem__Group__3__Impl rule__NormalItem__Group__4 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:867:2: rule__NormalItem__Group__3__Impl rule__NormalItem__Group__4
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__3__Impl_in_rule__NormalItem__Group__31721);
            rule__NormalItem__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group__4_in_rule__NormalItem__Group__31724);
            rule__NormalItem__Group__4();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__3


    // $ANTLR start rule__NormalItem__Group__3__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:874:1: rule__NormalItem__Group__3__Impl : ( ( rule__NormalItem__Group_3__0 )? ) ;
    public final void rule__NormalItem__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:878:1: ( ( ( rule__NormalItem__Group_3__0 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:879:1: ( ( rule__NormalItem__Group_3__0 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:879:1: ( ( rule__NormalItem__Group_3__0 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:880:1: ( rule__NormalItem__Group_3__0 )?
            {
             before(grammarAccess.getNormalItemAccess().getGroup_3()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:881:1: ( rule__NormalItem__Group_3__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==18) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:881:2: rule__NormalItem__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__NormalItem__Group_3__0_in_rule__NormalItem__Group__3__Impl1751);
                    rule__NormalItem__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getNormalItemAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__3__Impl


    // $ANTLR start rule__NormalItem__Group__4
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:891:1: rule__NormalItem__Group__4 : rule__NormalItem__Group__4__Impl rule__NormalItem__Group__5 ;
    public final void rule__NormalItem__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:895:1: ( rule__NormalItem__Group__4__Impl rule__NormalItem__Group__5 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:896:2: rule__NormalItem__Group__4__Impl rule__NormalItem__Group__5
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__4__Impl_in_rule__NormalItem__Group__41782);
            rule__NormalItem__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group__5_in_rule__NormalItem__Group__41785);
            rule__NormalItem__Group__5();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__4


    // $ANTLR start rule__NormalItem__Group__4__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:903:1: rule__NormalItem__Group__4__Impl : ( ( rule__NormalItem__Group_4__0 )? ) ;
    public final void rule__NormalItem__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:907:1: ( ( ( rule__NormalItem__Group_4__0 )? ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:908:1: ( ( rule__NormalItem__Group_4__0 )? )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:908:1: ( ( rule__NormalItem__Group_4__0 )? )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:909:1: ( rule__NormalItem__Group_4__0 )?
            {
             before(grammarAccess.getNormalItemAccess().getGroup_4()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:910:1: ( rule__NormalItem__Group_4__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==20) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:910:2: rule__NormalItem__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__NormalItem__Group_4__0_in_rule__NormalItem__Group__4__Impl1812);
                    rule__NormalItem__Group_4__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getNormalItemAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__4__Impl


    // $ANTLR start rule__NormalItem__Group__5
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:920:1: rule__NormalItem__Group__5 : rule__NormalItem__Group__5__Impl ;
    public final void rule__NormalItem__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:924:1: ( rule__NormalItem__Group__5__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:925:2: rule__NormalItem__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group__5__Impl_in_rule__NormalItem__Group__51843);
            rule__NormalItem__Group__5__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__5


    // $ANTLR start rule__NormalItem__Group__5__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:931:1: rule__NormalItem__Group__5__Impl : ( ( rule__NormalItem__Group_5__0 )* ) ;
    public final void rule__NormalItem__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:935:1: ( ( ( rule__NormalItem__Group_5__0 )* ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:936:1: ( ( rule__NormalItem__Group_5__0 )* )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:936:1: ( ( rule__NormalItem__Group_5__0 )* )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:937:1: ( rule__NormalItem__Group_5__0 )*
            {
             before(grammarAccess.getNormalItemAccess().getGroup_5()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:938:1: ( rule__NormalItem__Group_5__0 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==23) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:938:2: rule__NormalItem__Group_5__0
            	    {
            	    pushFollow(FOLLOW_rule__NormalItem__Group_5__0_in_rule__NormalItem__Group__5__Impl1870);
            	    rule__NormalItem__Group_5__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getNormalItemAccess().getGroup_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group__5__Impl


    // $ANTLR start rule__NormalItem__Group_3__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:960:1: rule__NormalItem__Group_3__0 : rule__NormalItem__Group_3__0__Impl rule__NormalItem__Group_3__1 ;
    public final void rule__NormalItem__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:964:1: ( rule__NormalItem__Group_3__0__Impl rule__NormalItem__Group_3__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:965:2: rule__NormalItem__Group_3__0__Impl rule__NormalItem__Group_3__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_3__0__Impl_in_rule__NormalItem__Group_3__01913);
            rule__NormalItem__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_3__1_in_rule__NormalItem__Group_3__01916);
            rule__NormalItem__Group_3__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__0


    // $ANTLR start rule__NormalItem__Group_3__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:972:1: rule__NormalItem__Group_3__0__Impl : ( '<' ) ;
    public final void rule__NormalItem__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:976:1: ( ( '<' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:977:1: ( '<' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:977:1: ( '<' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:978:1: '<'
            {
             before(grammarAccess.getNormalItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,18,FOLLOW_18_in_rule__NormalItem__Group_3__0__Impl1944); 
             after(grammarAccess.getNormalItemAccess().getLessThanSignKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__0__Impl


    // $ANTLR start rule__NormalItem__Group_3__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:991:1: rule__NormalItem__Group_3__1 : rule__NormalItem__Group_3__1__Impl rule__NormalItem__Group_3__2 ;
    public final void rule__NormalItem__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:995:1: ( rule__NormalItem__Group_3__1__Impl rule__NormalItem__Group_3__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:996:2: rule__NormalItem__Group_3__1__Impl rule__NormalItem__Group_3__2
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_3__1__Impl_in_rule__NormalItem__Group_3__11975);
            rule__NormalItem__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_3__2_in_rule__NormalItem__Group_3__11978);
            rule__NormalItem__Group_3__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__1


    // $ANTLR start rule__NormalItem__Group_3__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1003:1: rule__NormalItem__Group_3__1__Impl : ( ( rule__NormalItem__IconAssignment_3_1 ) ) ;
    public final void rule__NormalItem__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1007:1: ( ( ( rule__NormalItem__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1008:1: ( ( rule__NormalItem__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1008:1: ( ( rule__NormalItem__IconAssignment_3_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1009:1: ( rule__NormalItem__IconAssignment_3_1 )
            {
             before(grammarAccess.getNormalItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1010:1: ( rule__NormalItem__IconAssignment_3_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1010:2: rule__NormalItem__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__NormalItem__IconAssignment_3_1_in_rule__NormalItem__Group_3__1__Impl2005);
            rule__NormalItem__IconAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getIconAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__1__Impl


    // $ANTLR start rule__NormalItem__Group_3__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1020:1: rule__NormalItem__Group_3__2 : rule__NormalItem__Group_3__2__Impl ;
    public final void rule__NormalItem__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1024:1: ( rule__NormalItem__Group_3__2__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1025:2: rule__NormalItem__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_3__2__Impl_in_rule__NormalItem__Group_3__22035);
            rule__NormalItem__Group_3__2__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__2


    // $ANTLR start rule__NormalItem__Group_3__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1031:1: rule__NormalItem__Group_3__2__Impl : ( '>' ) ;
    public final void rule__NormalItem__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1035:1: ( ( '>' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1036:1: ( '>' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1036:1: ( '>' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1037:1: '>'
            {
             before(grammarAccess.getNormalItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,19,FOLLOW_19_in_rule__NormalItem__Group_3__2__Impl2063); 
             after(grammarAccess.getNormalItemAccess().getGreaterThanSignKeyword_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_3__2__Impl


    // $ANTLR start rule__NormalItem__Group_4__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1056:1: rule__NormalItem__Group_4__0 : rule__NormalItem__Group_4__0__Impl rule__NormalItem__Group_4__1 ;
    public final void rule__NormalItem__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1060:1: ( rule__NormalItem__Group_4__0__Impl rule__NormalItem__Group_4__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1061:2: rule__NormalItem__Group_4__0__Impl rule__NormalItem__Group_4__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4__0__Impl_in_rule__NormalItem__Group_4__02100);
            rule__NormalItem__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_4__1_in_rule__NormalItem__Group_4__02103);
            rule__NormalItem__Group_4__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__0


    // $ANTLR start rule__NormalItem__Group_4__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1068:1: rule__NormalItem__Group_4__0__Impl : ( '(' ) ;
    public final void rule__NormalItem__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1072:1: ( ( '(' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1073:1: ( '(' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1073:1: ( '(' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1074:1: '('
            {
             before(grammarAccess.getNormalItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,20,FOLLOW_20_in_rule__NormalItem__Group_4__0__Impl2131); 
             after(grammarAccess.getNormalItemAccess().getLeftParenthesisKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__0__Impl


    // $ANTLR start rule__NormalItem__Group_4__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1087:1: rule__NormalItem__Group_4__1 : rule__NormalItem__Group_4__1__Impl rule__NormalItem__Group_4__2 ;
    public final void rule__NormalItem__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1091:1: ( rule__NormalItem__Group_4__1__Impl rule__NormalItem__Group_4__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1092:2: rule__NormalItem__Group_4__1__Impl rule__NormalItem__Group_4__2
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4__1__Impl_in_rule__NormalItem__Group_4__12162);
            rule__NormalItem__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_4__2_in_rule__NormalItem__Group_4__12165);
            rule__NormalItem__Group_4__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__1


    // $ANTLR start rule__NormalItem__Group_4__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1099:1: rule__NormalItem__Group_4__1__Impl : ( ( rule__NormalItem__GroupsAssignment_4_1 ) ) ;
    public final void rule__NormalItem__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1103:1: ( ( ( rule__NormalItem__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1104:1: ( ( rule__NormalItem__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1104:1: ( ( rule__NormalItem__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1105:1: ( rule__NormalItem__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getNormalItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1106:1: ( rule__NormalItem__GroupsAssignment_4_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1106:2: rule__NormalItem__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__NormalItem__GroupsAssignment_4_1_in_rule__NormalItem__Group_4__1__Impl2192);
            rule__NormalItem__GroupsAssignment_4_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getGroupsAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__1__Impl


    // $ANTLR start rule__NormalItem__Group_4__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1116:1: rule__NormalItem__Group_4__2 : rule__NormalItem__Group_4__2__Impl rule__NormalItem__Group_4__3 ;
    public final void rule__NormalItem__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1120:1: ( rule__NormalItem__Group_4__2__Impl rule__NormalItem__Group_4__3 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1121:2: rule__NormalItem__Group_4__2__Impl rule__NormalItem__Group_4__3
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4__2__Impl_in_rule__NormalItem__Group_4__22222);
            rule__NormalItem__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_4__3_in_rule__NormalItem__Group_4__22225);
            rule__NormalItem__Group_4__3();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__2


    // $ANTLR start rule__NormalItem__Group_4__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1128:1: rule__NormalItem__Group_4__2__Impl : ( ( rule__NormalItem__Group_4_2__0 )* ) ;
    public final void rule__NormalItem__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1132:1: ( ( ( rule__NormalItem__Group_4_2__0 )* ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1133:1: ( ( rule__NormalItem__Group_4_2__0 )* )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1133:1: ( ( rule__NormalItem__Group_4_2__0 )* )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1134:1: ( rule__NormalItem__Group_4_2__0 )*
            {
             before(grammarAccess.getNormalItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1135:1: ( rule__NormalItem__Group_4_2__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==22) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1135:2: rule__NormalItem__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__NormalItem__Group_4_2__0_in_rule__NormalItem__Group_4__2__Impl2252);
            	    rule__NormalItem__Group_4_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

             after(grammarAccess.getNormalItemAccess().getGroup_4_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__2__Impl


    // $ANTLR start rule__NormalItem__Group_4__3
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1145:1: rule__NormalItem__Group_4__3 : rule__NormalItem__Group_4__3__Impl ;
    public final void rule__NormalItem__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1149:1: ( rule__NormalItem__Group_4__3__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1150:2: rule__NormalItem__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4__3__Impl_in_rule__NormalItem__Group_4__32283);
            rule__NormalItem__Group_4__3__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__3


    // $ANTLR start rule__NormalItem__Group_4__3__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1156:1: rule__NormalItem__Group_4__3__Impl : ( ')' ) ;
    public final void rule__NormalItem__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1160:1: ( ( ')' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1161:1: ( ')' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1161:1: ( ')' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1162:1: ')'
            {
             before(grammarAccess.getNormalItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,21,FOLLOW_21_in_rule__NormalItem__Group_4__3__Impl2311); 
             after(grammarAccess.getNormalItemAccess().getRightParenthesisKeyword_4_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4__3__Impl


    // $ANTLR start rule__NormalItem__Group_4_2__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1183:1: rule__NormalItem__Group_4_2__0 : rule__NormalItem__Group_4_2__0__Impl rule__NormalItem__Group_4_2__1 ;
    public final void rule__NormalItem__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1187:1: ( rule__NormalItem__Group_4_2__0__Impl rule__NormalItem__Group_4_2__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1188:2: rule__NormalItem__Group_4_2__0__Impl rule__NormalItem__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4_2__0__Impl_in_rule__NormalItem__Group_4_2__02350);
            rule__NormalItem__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_4_2__1_in_rule__NormalItem__Group_4_2__02353);
            rule__NormalItem__Group_4_2__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4_2__0


    // $ANTLR start rule__NormalItem__Group_4_2__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1195:1: rule__NormalItem__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__NormalItem__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1199:1: ( ( ',' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1200:1: ( ',' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1200:1: ( ',' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1201:1: ','
            {
             before(grammarAccess.getNormalItemAccess().getCommaKeyword_4_2_0()); 
            match(input,22,FOLLOW_22_in_rule__NormalItem__Group_4_2__0__Impl2381); 
             after(grammarAccess.getNormalItemAccess().getCommaKeyword_4_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4_2__0__Impl


    // $ANTLR start rule__NormalItem__Group_4_2__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1214:1: rule__NormalItem__Group_4_2__1 : rule__NormalItem__Group_4_2__1__Impl ;
    public final void rule__NormalItem__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1218:1: ( rule__NormalItem__Group_4_2__1__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1219:2: rule__NormalItem__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_4_2__1__Impl_in_rule__NormalItem__Group_4_2__12412);
            rule__NormalItem__Group_4_2__1__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4_2__1


    // $ANTLR start rule__NormalItem__Group_4_2__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1225:1: rule__NormalItem__Group_4_2__1__Impl : ( ( rule__NormalItem__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__NormalItem__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1229:1: ( ( ( rule__NormalItem__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1230:1: ( ( rule__NormalItem__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1230:1: ( ( rule__NormalItem__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1231:1: ( rule__NormalItem__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getNormalItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1232:1: ( rule__NormalItem__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1232:2: rule__NormalItem__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__NormalItem__GroupsAssignment_4_2_1_in_rule__NormalItem__Group_4_2__1__Impl2439);
            rule__NormalItem__GroupsAssignment_4_2_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getGroupsAssignment_4_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_4_2__1__Impl


    // $ANTLR start rule__NormalItem__Group_5__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1246:1: rule__NormalItem__Group_5__0 : rule__NormalItem__Group_5__0__Impl rule__NormalItem__Group_5__1 ;
    public final void rule__NormalItem__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1250:1: ( rule__NormalItem__Group_5__0__Impl rule__NormalItem__Group_5__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1251:2: rule__NormalItem__Group_5__0__Impl rule__NormalItem__Group_5__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5__0__Impl_in_rule__NormalItem__Group_5__02473);
            rule__NormalItem__Group_5__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_5__1_in_rule__NormalItem__Group_5__02476);
            rule__NormalItem__Group_5__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__0


    // $ANTLR start rule__NormalItem__Group_5__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1258:1: rule__NormalItem__Group_5__0__Impl : ( '{' ) ;
    public final void rule__NormalItem__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1262:1: ( ( '{' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1263:1: ( '{' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1263:1: ( '{' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1264:1: '{'
            {
             before(grammarAccess.getNormalItemAccess().getLeftCurlyBracketKeyword_5_0()); 
            match(input,23,FOLLOW_23_in_rule__NormalItem__Group_5__0__Impl2504); 
             after(grammarAccess.getNormalItemAccess().getLeftCurlyBracketKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__0__Impl


    // $ANTLR start rule__NormalItem__Group_5__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1277:1: rule__NormalItem__Group_5__1 : rule__NormalItem__Group_5__1__Impl rule__NormalItem__Group_5__2 ;
    public final void rule__NormalItem__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1281:1: ( rule__NormalItem__Group_5__1__Impl rule__NormalItem__Group_5__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1282:2: rule__NormalItem__Group_5__1__Impl rule__NormalItem__Group_5__2
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5__1__Impl_in_rule__NormalItem__Group_5__12535);
            rule__NormalItem__Group_5__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_5__2_in_rule__NormalItem__Group_5__12538);
            rule__NormalItem__Group_5__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__1


    // $ANTLR start rule__NormalItem__Group_5__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1289:1: rule__NormalItem__Group_5__1__Impl : ( ( rule__NormalItem__BindingsAssignment_5_1 ) ) ;
    public final void rule__NormalItem__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1293:1: ( ( ( rule__NormalItem__BindingsAssignment_5_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1294:1: ( ( rule__NormalItem__BindingsAssignment_5_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1294:1: ( ( rule__NormalItem__BindingsAssignment_5_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1295:1: ( rule__NormalItem__BindingsAssignment_5_1 )
            {
             before(grammarAccess.getNormalItemAccess().getBindingsAssignment_5_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1296:1: ( rule__NormalItem__BindingsAssignment_5_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1296:2: rule__NormalItem__BindingsAssignment_5_1
            {
            pushFollow(FOLLOW_rule__NormalItem__BindingsAssignment_5_1_in_rule__NormalItem__Group_5__1__Impl2565);
            rule__NormalItem__BindingsAssignment_5_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getBindingsAssignment_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__1__Impl


    // $ANTLR start rule__NormalItem__Group_5__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1306:1: rule__NormalItem__Group_5__2 : rule__NormalItem__Group_5__2__Impl rule__NormalItem__Group_5__3 ;
    public final void rule__NormalItem__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1310:1: ( rule__NormalItem__Group_5__2__Impl rule__NormalItem__Group_5__3 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1311:2: rule__NormalItem__Group_5__2__Impl rule__NormalItem__Group_5__3
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5__2__Impl_in_rule__NormalItem__Group_5__22595);
            rule__NormalItem__Group_5__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_5__3_in_rule__NormalItem__Group_5__22598);
            rule__NormalItem__Group_5__3();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__2


    // $ANTLR start rule__NormalItem__Group_5__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1318:1: rule__NormalItem__Group_5__2__Impl : ( ( rule__NormalItem__Group_5_2__0 )* ) ;
    public final void rule__NormalItem__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1322:1: ( ( ( rule__NormalItem__Group_5_2__0 )* ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1323:1: ( ( rule__NormalItem__Group_5_2__0 )* )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1323:1: ( ( rule__NormalItem__Group_5_2__0 )* )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1324:1: ( rule__NormalItem__Group_5_2__0 )*
            {
             before(grammarAccess.getNormalItemAccess().getGroup_5_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1325:1: ( rule__NormalItem__Group_5_2__0 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==22) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1325:2: rule__NormalItem__Group_5_2__0
            	    {
            	    pushFollow(FOLLOW_rule__NormalItem__Group_5_2__0_in_rule__NormalItem__Group_5__2__Impl2625);
            	    rule__NormalItem__Group_5_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

             after(grammarAccess.getNormalItemAccess().getGroup_5_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__2__Impl


    // $ANTLR start rule__NormalItem__Group_5__3
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1335:1: rule__NormalItem__Group_5__3 : rule__NormalItem__Group_5__3__Impl ;
    public final void rule__NormalItem__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1339:1: ( rule__NormalItem__Group_5__3__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1340:2: rule__NormalItem__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5__3__Impl_in_rule__NormalItem__Group_5__32656);
            rule__NormalItem__Group_5__3__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__3


    // $ANTLR start rule__NormalItem__Group_5__3__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1346:1: rule__NormalItem__Group_5__3__Impl : ( '}' ) ;
    public final void rule__NormalItem__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1350:1: ( ( '}' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1351:1: ( '}' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1351:1: ( '}' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1352:1: '}'
            {
             before(grammarAccess.getNormalItemAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,24,FOLLOW_24_in_rule__NormalItem__Group_5__3__Impl2684); 
             after(grammarAccess.getNormalItemAccess().getRightCurlyBracketKeyword_5_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5__3__Impl


    // $ANTLR start rule__NormalItem__Group_5_2__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1373:1: rule__NormalItem__Group_5_2__0 : rule__NormalItem__Group_5_2__0__Impl rule__NormalItem__Group_5_2__1 ;
    public final void rule__NormalItem__Group_5_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1377:1: ( rule__NormalItem__Group_5_2__0__Impl rule__NormalItem__Group_5_2__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1378:2: rule__NormalItem__Group_5_2__0__Impl rule__NormalItem__Group_5_2__1
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5_2__0__Impl_in_rule__NormalItem__Group_5_2__02723);
            rule__NormalItem__Group_5_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__NormalItem__Group_5_2__1_in_rule__NormalItem__Group_5_2__02726);
            rule__NormalItem__Group_5_2__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5_2__0


    // $ANTLR start rule__NormalItem__Group_5_2__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1385:1: rule__NormalItem__Group_5_2__0__Impl : ( ',' ) ;
    public final void rule__NormalItem__Group_5_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1389:1: ( ( ',' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1390:1: ( ',' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1390:1: ( ',' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1391:1: ','
            {
             before(grammarAccess.getNormalItemAccess().getCommaKeyword_5_2_0()); 
            match(input,22,FOLLOW_22_in_rule__NormalItem__Group_5_2__0__Impl2754); 
             after(grammarAccess.getNormalItemAccess().getCommaKeyword_5_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5_2__0__Impl


    // $ANTLR start rule__NormalItem__Group_5_2__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1404:1: rule__NormalItem__Group_5_2__1 : rule__NormalItem__Group_5_2__1__Impl ;
    public final void rule__NormalItem__Group_5_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1408:1: ( rule__NormalItem__Group_5_2__1__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1409:2: rule__NormalItem__Group_5_2__1__Impl
            {
            pushFollow(FOLLOW_rule__NormalItem__Group_5_2__1__Impl_in_rule__NormalItem__Group_5_2__12785);
            rule__NormalItem__Group_5_2__1__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5_2__1


    // $ANTLR start rule__NormalItem__Group_5_2__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1415:1: rule__NormalItem__Group_5_2__1__Impl : ( ( rule__NormalItem__BindingsAssignment_5_2_1 ) ) ;
    public final void rule__NormalItem__Group_5_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1419:1: ( ( ( rule__NormalItem__BindingsAssignment_5_2_1 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1420:1: ( ( rule__NormalItem__BindingsAssignment_5_2_1 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1420:1: ( ( rule__NormalItem__BindingsAssignment_5_2_1 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1421:1: ( rule__NormalItem__BindingsAssignment_5_2_1 )
            {
             before(grammarAccess.getNormalItemAccess().getBindingsAssignment_5_2_1()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1422:1: ( rule__NormalItem__BindingsAssignment_5_2_1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1422:2: rule__NormalItem__BindingsAssignment_5_2_1
            {
            pushFollow(FOLLOW_rule__NormalItem__BindingsAssignment_5_2_1_in_rule__NormalItem__Group_5_2__1__Impl2812);
            rule__NormalItem__BindingsAssignment_5_2_1();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getBindingsAssignment_5_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__Group_5_2__1__Impl


    // $ANTLR start rule__Binding__Group__0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1436:1: rule__Binding__Group__0 : rule__Binding__Group__0__Impl rule__Binding__Group__1 ;
    public final void rule__Binding__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1440:1: ( rule__Binding__Group__0__Impl rule__Binding__Group__1 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1441:2: rule__Binding__Group__0__Impl rule__Binding__Group__1
            {
            pushFollow(FOLLOW_rule__Binding__Group__0__Impl_in_rule__Binding__Group__02846);
            rule__Binding__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Binding__Group__1_in_rule__Binding__Group__02849);
            rule__Binding__Group__1();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__0


    // $ANTLR start rule__Binding__Group__0__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1448:1: rule__Binding__Group__0__Impl : ( ( rule__Binding__TypeAssignment_0 ) ) ;
    public final void rule__Binding__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1452:1: ( ( ( rule__Binding__TypeAssignment_0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1453:1: ( ( rule__Binding__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1453:1: ( ( rule__Binding__TypeAssignment_0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1454:1: ( rule__Binding__TypeAssignment_0 )
            {
             before(grammarAccess.getBindingAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1455:1: ( rule__Binding__TypeAssignment_0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1455:2: rule__Binding__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__Binding__TypeAssignment_0_in_rule__Binding__Group__0__Impl2876);
            rule__Binding__TypeAssignment_0();
            _fsp--;


            }

             after(grammarAccess.getBindingAccess().getTypeAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__0__Impl


    // $ANTLR start rule__Binding__Group__1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1465:1: rule__Binding__Group__1 : rule__Binding__Group__1__Impl rule__Binding__Group__2 ;
    public final void rule__Binding__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1469:1: ( rule__Binding__Group__1__Impl rule__Binding__Group__2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1470:2: rule__Binding__Group__1__Impl rule__Binding__Group__2
            {
            pushFollow(FOLLOW_rule__Binding__Group__1__Impl_in_rule__Binding__Group__12906);
            rule__Binding__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Binding__Group__2_in_rule__Binding__Group__12909);
            rule__Binding__Group__2();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__1


    // $ANTLR start rule__Binding__Group__1__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1477:1: rule__Binding__Group__1__Impl : ( '=' ) ;
    public final void rule__Binding__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1481:1: ( ( '=' ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1482:1: ( '=' )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1482:1: ( '=' )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1483:1: '='
            {
             before(grammarAccess.getBindingAccess().getEqualsSignKeyword_1()); 
            match(input,25,FOLLOW_25_in_rule__Binding__Group__1__Impl2937); 
             after(grammarAccess.getBindingAccess().getEqualsSignKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__1__Impl


    // $ANTLR start rule__Binding__Group__2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1496:1: rule__Binding__Group__2 : rule__Binding__Group__2__Impl ;
    public final void rule__Binding__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1500:1: ( rule__Binding__Group__2__Impl )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1501:2: rule__Binding__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Binding__Group__2__Impl_in_rule__Binding__Group__22968);
            rule__Binding__Group__2__Impl();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__2


    // $ANTLR start rule__Binding__Group__2__Impl
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1507:1: rule__Binding__Group__2__Impl : ( ( rule__Binding__ConfigurationAssignment_2 ) ) ;
    public final void rule__Binding__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1511:1: ( ( ( rule__Binding__ConfigurationAssignment_2 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1512:1: ( ( rule__Binding__ConfigurationAssignment_2 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1512:1: ( ( rule__Binding__ConfigurationAssignment_2 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1513:1: ( rule__Binding__ConfigurationAssignment_2 )
            {
             before(grammarAccess.getBindingAccess().getConfigurationAssignment_2()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1514:1: ( rule__Binding__ConfigurationAssignment_2 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1514:2: rule__Binding__ConfigurationAssignment_2
            {
            pushFollow(FOLLOW_rule__Binding__ConfigurationAssignment_2_in_rule__Binding__Group__2__Impl2995);
            rule__Binding__ConfigurationAssignment_2();
            _fsp--;


            }

             after(grammarAccess.getBindingAccess().getConfigurationAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__Group__2__Impl


    // $ANTLR start rule__Model__ItemsAssignment
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1531:1: rule__Model__ItemsAssignment : ( ruleItem ) ;
    public final void rule__Model__ItemsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1535:1: ( ( ruleItem ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1536:1: ( ruleItem )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1536:1: ( ruleItem )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1537:1: ruleItem
            {
             before(grammarAccess.getModelAccess().getItemsItemParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleItem_in_rule__Model__ItemsAssignment3036);
            ruleItem();
            _fsp--;

             after(grammarAccess.getModelAccess().getItemsItemParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Model__ItemsAssignment


    // $ANTLR start rule__GroupItem__NameAssignment_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1546:1: rule__GroupItem__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__GroupItem__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1550:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1551:1: ( RULE_ID )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1551:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1552:1: RULE_ID
            {
             before(grammarAccess.getGroupItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__GroupItem__NameAssignment_13067); 
             after(grammarAccess.getGroupItemAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__NameAssignment_1


    // $ANTLR start rule__GroupItem__LabelAssignment_2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1561:1: rule__GroupItem__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__GroupItem__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1565:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1566:1: ( RULE_STRING )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1566:1: ( RULE_STRING )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1567:1: RULE_STRING
            {
             before(grammarAccess.getGroupItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__GroupItem__LabelAssignment_23098); 
             after(grammarAccess.getGroupItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__LabelAssignment_2


    // $ANTLR start rule__GroupItem__IconAssignment_3_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1576:1: rule__GroupItem__IconAssignment_3_1 : ( ( rule__GroupItem__IconAlternatives_3_1_0 ) ) ;
    public final void rule__GroupItem__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1580:1: ( ( ( rule__GroupItem__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1581:1: ( ( rule__GroupItem__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1581:1: ( ( rule__GroupItem__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1582:1: ( rule__GroupItem__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getGroupItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1583:1: ( rule__GroupItem__IconAlternatives_3_1_0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1583:2: rule__GroupItem__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__GroupItem__IconAlternatives_3_1_0_in_rule__GroupItem__IconAssignment_3_13129);
            rule__GroupItem__IconAlternatives_3_1_0();
            _fsp--;


            }

             after(grammarAccess.getGroupItemAccess().getIconAlternatives_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__IconAssignment_3_1


    // $ANTLR start rule__GroupItem__GroupsAssignment_4_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1592:1: rule__GroupItem__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__GroupItem__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1596:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1597:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1597:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1598:1: ( RULE_ID )
            {
             before(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1599:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1600:1: RULE_ID
            {
             before(grammarAccess.getGroupItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__GroupItem__GroupsAssignment_4_13166); 
             after(grammarAccess.getGroupItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__GroupsAssignment_4_1


    // $ANTLR start rule__GroupItem__GroupsAssignment_4_2_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1611:1: rule__GroupItem__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__GroupItem__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1615:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1616:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1616:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1617:1: ( RULE_ID )
            {
             before(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1618:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1619:1: RULE_ID
            {
             before(grammarAccess.getGroupItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__GroupItem__GroupsAssignment_4_2_13205); 
             after(grammarAccess.getGroupItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 

            }

             after(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__GroupsAssignment_4_2_1


    // $ANTLR start rule__NormalItem__TypeAssignment_0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1630:1: rule__NormalItem__TypeAssignment_0 : ( ( rule__NormalItem__TypeAlternatives_0_0 ) ) ;
    public final void rule__NormalItem__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1634:1: ( ( ( rule__NormalItem__TypeAlternatives_0_0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1635:1: ( ( rule__NormalItem__TypeAlternatives_0_0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1635:1: ( ( rule__NormalItem__TypeAlternatives_0_0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1636:1: ( rule__NormalItem__TypeAlternatives_0_0 )
            {
             before(grammarAccess.getNormalItemAccess().getTypeAlternatives_0_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1637:1: ( rule__NormalItem__TypeAlternatives_0_0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1637:2: rule__NormalItem__TypeAlternatives_0_0
            {
            pushFollow(FOLLOW_rule__NormalItem__TypeAlternatives_0_0_in_rule__NormalItem__TypeAssignment_03240);
            rule__NormalItem__TypeAlternatives_0_0();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getTypeAlternatives_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__TypeAssignment_0


    // $ANTLR start rule__NormalItem__NameAssignment_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1646:1: rule__NormalItem__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__NormalItem__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1650:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1651:1: ( RULE_ID )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1651:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1652:1: RULE_ID
            {
             before(grammarAccess.getNormalItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__NameAssignment_13273); 
             after(grammarAccess.getNormalItemAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__NameAssignment_1


    // $ANTLR start rule__NormalItem__LabelAssignment_2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1661:1: rule__NormalItem__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__NormalItem__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1665:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1666:1: ( RULE_STRING )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1666:1: ( RULE_STRING )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1667:1: RULE_STRING
            {
             before(grammarAccess.getNormalItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__NormalItem__LabelAssignment_23304); 
             after(grammarAccess.getNormalItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__LabelAssignment_2


    // $ANTLR start rule__NormalItem__IconAssignment_3_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1676:1: rule__NormalItem__IconAssignment_3_1 : ( ( rule__NormalItem__IconAlternatives_3_1_0 ) ) ;
    public final void rule__NormalItem__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1680:1: ( ( ( rule__NormalItem__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1681:1: ( ( rule__NormalItem__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1681:1: ( ( rule__NormalItem__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1682:1: ( rule__NormalItem__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getNormalItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1683:1: ( rule__NormalItem__IconAlternatives_3_1_0 )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1683:2: rule__NormalItem__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__NormalItem__IconAlternatives_3_1_0_in_rule__NormalItem__IconAssignment_3_13335);
            rule__NormalItem__IconAlternatives_3_1_0();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getIconAlternatives_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__IconAssignment_3_1


    // $ANTLR start rule__NormalItem__GroupsAssignment_4_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1692:1: rule__NormalItem__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__NormalItem__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1696:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1697:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1697:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1698:1: ( RULE_ID )
            {
             before(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1699:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1700:1: RULE_ID
            {
             before(grammarAccess.getNormalItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__GroupsAssignment_4_13372); 
             after(grammarAccess.getNormalItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__GroupsAssignment_4_1


    // $ANTLR start rule__NormalItem__GroupsAssignment_4_2_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1711:1: rule__NormalItem__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__NormalItem__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1715:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1716:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1716:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1717:1: ( RULE_ID )
            {
             before(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1718:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1719:1: RULE_ID
            {
             before(grammarAccess.getNormalItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__GroupsAssignment_4_2_13411); 
             after(grammarAccess.getNormalItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 

            }

             after(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__GroupsAssignment_4_2_1


    // $ANTLR start rule__NormalItem__BindingsAssignment_5_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1730:1: rule__NormalItem__BindingsAssignment_5_1 : ( ruleBinding ) ;
    public final void rule__NormalItem__BindingsAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1734:1: ( ( ruleBinding ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1735:1: ( ruleBinding )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1735:1: ( ruleBinding )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1736:1: ruleBinding
            {
             before(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_ruleBinding_in_rule__NormalItem__BindingsAssignment_5_13446);
            ruleBinding();
            _fsp--;

             after(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__BindingsAssignment_5_1


    // $ANTLR start rule__NormalItem__BindingsAssignment_5_2_1
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1745:1: rule__NormalItem__BindingsAssignment_5_2_1 : ( ruleBinding ) ;
    public final void rule__NormalItem__BindingsAssignment_5_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1749:1: ( ( ruleBinding ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1750:1: ( ruleBinding )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1750:1: ( ruleBinding )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1751:1: ruleBinding
            {
             before(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_2_1_0()); 
            pushFollow(FOLLOW_ruleBinding_in_rule__NormalItem__BindingsAssignment_5_2_13477);
            ruleBinding();
            _fsp--;

             after(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__NormalItem__BindingsAssignment_5_2_1


    // $ANTLR start rule__Binding__TypeAssignment_0
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1760:1: rule__Binding__TypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__Binding__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1764:1: ( ( RULE_ID ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1765:1: ( RULE_ID )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1765:1: ( RULE_ID )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1766:1: RULE_ID
            {
             before(grammarAccess.getBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Binding__TypeAssignment_03508); 
             after(grammarAccess.getBindingAccess().getTypeIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__TypeAssignment_0


    // $ANTLR start rule__Binding__ConfigurationAssignment_2
    // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1775:1: rule__Binding__ConfigurationAssignment_2 : ( RULE_STRING ) ;
    public final void rule__Binding__ConfigurationAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1779:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1780:1: ( RULE_STRING )
            {
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1780:1: ( RULE_STRING )
            // ../org.openhab.model.config.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalConfig.g:1781:1: RULE_STRING
            {
             before(grammarAccess.getBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Binding__ConfigurationAssignment_23539); 
             after(grammarAccess.getBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Binding__ConfigurationAssignment_2


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Model__ItemsAssignment_in_ruleModel94 = new BitSet(new long[]{0x000000000003F812L});
    public static final BitSet FOLLOW_ruleItem_in_entryRuleItem122 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItem129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Alternatives_in_ruleItem155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_entryRuleGroupItem182 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupItem189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__0_in_ruleGroupItem215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_entryRuleNormalItem242 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNormalItem249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__0_in_ruleNormalItem275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_entryRuleBinding302 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBinding309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__0_in_ruleBinding335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_rule__Item__Alternatives371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_rule__Item__Alternatives388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__GroupItem__IconAlternatives_3_1_0420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__GroupItem__IconAlternatives_3_1_0437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__NormalItem__TypeAlternatives_0_0470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__NormalItem__TypeAlternatives_0_0490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__NormalItem__TypeAlternatives_0_0510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__NormalItem__TypeAlternatives_0_0530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__NormalItem__TypeAlternatives_0_0550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__NormalItem__TypeAlternatives_0_0570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__TypeAlternatives_0_0589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__IconAlternatives_3_1_0621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__NormalItem__IconAlternatives_3_1_0638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__0__Impl_in_rule__GroupItem__Group__0668 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__1_in_rule__GroupItem__Group__0671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__GroupItem__Group__0__Impl699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__1__Impl_in_rule__GroupItem__Group__1730 = new BitSet(new long[]{0x0000000000140022L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__2_in_rule__GroupItem__Group__1733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__NameAssignment_1_in_rule__GroupItem__Group__1__Impl760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__2__Impl_in_rule__GroupItem__Group__2790 = new BitSet(new long[]{0x0000000000140002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__3_in_rule__GroupItem__Group__2793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__LabelAssignment_2_in_rule__GroupItem__Group__2__Impl820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__3__Impl_in_rule__GroupItem__Group__3851 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__4_in_rule__GroupItem__Group__3854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__0_in_rule__GroupItem__Group__3__Impl881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__4__Impl_in_rule__GroupItem__Group__4912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__0_in_rule__GroupItem__Group__4__Impl939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__0__Impl_in_rule__GroupItem__Group_3__0980 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__1_in_rule__GroupItem__Group_3__0983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__GroupItem__Group_3__0__Impl1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__1__Impl_in_rule__GroupItem__Group_3__11042 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__2_in_rule__GroupItem__Group_3__11045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__IconAssignment_3_1_in_rule__GroupItem__Group_3__1__Impl1072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_3__2__Impl_in_rule__GroupItem__Group_3__21102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__GroupItem__Group_3__2__Impl1130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__0__Impl_in_rule__GroupItem__Group_4__01167 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__1_in_rule__GroupItem__Group_4__01170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__GroupItem__Group_4__0__Impl1198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__1__Impl_in_rule__GroupItem__Group_4__11229 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__2_in_rule__GroupItem__Group_4__11232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__GroupsAssignment_4_1_in_rule__GroupItem__Group_4__1__Impl1259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__2__Impl_in_rule__GroupItem__Group_4__21289 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__3_in_rule__GroupItem__Group_4__21292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4_2__0_in_rule__GroupItem__Group_4__2__Impl1319 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4__3__Impl_in_rule__GroupItem__Group_4__31350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__GroupItem__Group_4__3__Impl1378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4_2__0__Impl_in_rule__GroupItem__Group_4_2__01417 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4_2__1_in_rule__GroupItem__Group_4_2__01420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__GroupItem__Group_4_2__0__Impl1448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group_4_2__1__Impl_in_rule__GroupItem__Group_4_2__11479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__GroupsAssignment_4_2_1_in_rule__GroupItem__Group_4_2__1__Impl1506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__0__Impl_in_rule__NormalItem__Group__01540 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__1_in_rule__NormalItem__Group__01543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__TypeAssignment_0_in_rule__NormalItem__Group__0__Impl1570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__1__Impl_in_rule__NormalItem__Group__11600 = new BitSet(new long[]{0x0000000000940022L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__2_in_rule__NormalItem__Group__11603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__NameAssignment_1_in_rule__NormalItem__Group__1__Impl1630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__2__Impl_in_rule__NormalItem__Group__21660 = new BitSet(new long[]{0x0000000000940002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__3_in_rule__NormalItem__Group__21663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__LabelAssignment_2_in_rule__NormalItem__Group__2__Impl1690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__3__Impl_in_rule__NormalItem__Group__31721 = new BitSet(new long[]{0x0000000000900002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__4_in_rule__NormalItem__Group__31724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__0_in_rule__NormalItem__Group__3__Impl1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__4__Impl_in_rule__NormalItem__Group__41782 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__5_in_rule__NormalItem__Group__41785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__0_in_rule__NormalItem__Group__4__Impl1812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group__5__Impl_in_rule__NormalItem__Group__51843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__0_in_rule__NormalItem__Group__5__Impl1870 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__0__Impl_in_rule__NormalItem__Group_3__01913 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__1_in_rule__NormalItem__Group_3__01916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__NormalItem__Group_3__0__Impl1944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__1__Impl_in_rule__NormalItem__Group_3__11975 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__2_in_rule__NormalItem__Group_3__11978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__IconAssignment_3_1_in_rule__NormalItem__Group_3__1__Impl2005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_3__2__Impl_in_rule__NormalItem__Group_3__22035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__NormalItem__Group_3__2__Impl2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__0__Impl_in_rule__NormalItem__Group_4__02100 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__1_in_rule__NormalItem__Group_4__02103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__NormalItem__Group_4__0__Impl2131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__1__Impl_in_rule__NormalItem__Group_4__12162 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__2_in_rule__NormalItem__Group_4__12165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__GroupsAssignment_4_1_in_rule__NormalItem__Group_4__1__Impl2192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__2__Impl_in_rule__NormalItem__Group_4__22222 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__3_in_rule__NormalItem__Group_4__22225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4_2__0_in_rule__NormalItem__Group_4__2__Impl2252 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4__3__Impl_in_rule__NormalItem__Group_4__32283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__NormalItem__Group_4__3__Impl2311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4_2__0__Impl_in_rule__NormalItem__Group_4_2__02350 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4_2__1_in_rule__NormalItem__Group_4_2__02353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__NormalItem__Group_4_2__0__Impl2381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_4_2__1__Impl_in_rule__NormalItem__Group_4_2__12412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__GroupsAssignment_4_2_1_in_rule__NormalItem__Group_4_2__1__Impl2439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__0__Impl_in_rule__NormalItem__Group_5__02473 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__1_in_rule__NormalItem__Group_5__02476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__NormalItem__Group_5__0__Impl2504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__1__Impl_in_rule__NormalItem__Group_5__12535 = new BitSet(new long[]{0x0000000001400000L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__2_in_rule__NormalItem__Group_5__12538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__BindingsAssignment_5_1_in_rule__NormalItem__Group_5__1__Impl2565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__2__Impl_in_rule__NormalItem__Group_5__22595 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__3_in_rule__NormalItem__Group_5__22598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5_2__0_in_rule__NormalItem__Group_5__2__Impl2625 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5__3__Impl_in_rule__NormalItem__Group_5__32656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__NormalItem__Group_5__3__Impl2684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5_2__0__Impl_in_rule__NormalItem__Group_5_2__02723 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5_2__1_in_rule__NormalItem__Group_5_2__02726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__NormalItem__Group_5_2__0__Impl2754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__Group_5_2__1__Impl_in_rule__NormalItem__Group_5_2__12785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__BindingsAssignment_5_2_1_in_rule__NormalItem__Group_5_2__1__Impl2812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__0__Impl_in_rule__Binding__Group__02846 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__Binding__Group__1_in_rule__Binding__Group__02849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__TypeAssignment_0_in_rule__Binding__Group__0__Impl2876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__1__Impl_in_rule__Binding__Group__12906 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Binding__Group__2_in_rule__Binding__Group__12909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Binding__Group__1__Impl2937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__2__Impl_in_rule__Binding__Group__22968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__ConfigurationAssignment_2_in_rule__Binding__Group__2__Impl2995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleItem_in_rule__Model__ItemsAssignment3036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__GroupItem__NameAssignment_13067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__GroupItem__LabelAssignment_23098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__IconAlternatives_3_1_0_in_rule__GroupItem__IconAssignment_3_13129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__GroupItem__GroupsAssignment_4_13166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__GroupItem__GroupsAssignment_4_2_13205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__TypeAlternatives_0_0_in_rule__NormalItem__TypeAssignment_03240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__NameAssignment_13273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__NormalItem__LabelAssignment_23304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__IconAlternatives_3_1_0_in_rule__NormalItem__IconAssignment_3_13335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__GroupsAssignment_4_13372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__GroupsAssignment_4_2_13411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_rule__NormalItem__BindingsAssignment_5_13446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_rule__NormalItem__BindingsAssignment_5_2_13477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Binding__TypeAssignment_03508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Binding__ConfigurationAssignment_23539 = new BitSet(new long[]{0x0000000000000002L});

}