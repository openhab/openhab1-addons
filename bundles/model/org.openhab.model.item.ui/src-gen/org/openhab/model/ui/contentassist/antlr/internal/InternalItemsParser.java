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
import org.openhab.model.services.ItemsGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalItemsParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'AND'", "'OR'", "'<'", "'>'", "'('", "')'", "','", "'{'", "'}'", "'Group'", "'['", "']'", "'='"
    };
    public static final int RULE_ID=4;
    public static final int RULE_STRING=5;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_INT=6;
    public static final int RULE_WS=9;
    public static final int RULE_SL_COMMENT=8;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=7;

        public InternalItemsParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g"; }


     
     	private ItemsGrammarAccess grammarAccess;
     	
        public void setGrammarAccess(ItemsGrammarAccess grammarAccess) {
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




    // $ANTLR start entryRuleItemModel
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:61:1: entryRuleItemModel : ruleItemModel EOF ;
    public final void entryRuleItemModel() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:62:1: ( ruleItemModel EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:63:1: ruleItemModel EOF
            {
             before(grammarAccess.getItemModelRule()); 
            pushFollow(FOLLOW_ruleItemModel_in_entryRuleItemModel61);
            ruleItemModel();
            _fsp--;

             after(grammarAccess.getItemModelRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleItemModel68); 

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
    // $ANTLR end entryRuleItemModel


    // $ANTLR start ruleItemModel
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:70:1: ruleItemModel : ( ( rule__ItemModel__ItemsAssignment )* ) ;
    public final void ruleItemModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:74:2: ( ( ( rule__ItemModel__ItemsAssignment )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:75:1: ( ( rule__ItemModel__ItemsAssignment )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:75:1: ( ( rule__ItemModel__ItemsAssignment )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:76:1: ( rule__ItemModel__ItemsAssignment )*
            {
             before(grammarAccess.getItemModelAccess().getItemsAssignment()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:77:1: ( rule__ItemModel__ItemsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||(LA1_0>=11 && LA1_0<=16)||LA1_0==26) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:77:2: rule__ItemModel__ItemsAssignment
            	    {
            	    pushFollow(FOLLOW_rule__ItemModel__ItemsAssignment_in_ruleItemModel94);
            	    rule__ItemModel__ItemsAssignment();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getItemModelAccess().getItemsAssignment()); 

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
    // $ANTLR end ruleItemModel


    // $ANTLR start entryRuleModelItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:89:1: entryRuleModelItem : ruleModelItem EOF ;
    public final void entryRuleModelItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:90:1: ( ruleModelItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:91:1: ruleModelItem EOF
            {
             before(grammarAccess.getModelItemRule()); 
            pushFollow(FOLLOW_ruleModelItem_in_entryRuleModelItem122);
            ruleModelItem();
            _fsp--;

             after(grammarAccess.getModelItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelItem129); 

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
    // $ANTLR end entryRuleModelItem


    // $ANTLR start ruleModelItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:98:1: ruleModelItem : ( ( rule__ModelItem__Group__0 ) ) ;
    public final void ruleModelItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:102:2: ( ( ( rule__ModelItem__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:103:1: ( ( rule__ModelItem__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:103:1: ( ( rule__ModelItem__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:104:1: ( rule__ModelItem__Group__0 )
            {
             before(grammarAccess.getModelItemAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:105:1: ( rule__ModelItem__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:105:2: rule__ModelItem__Group__0
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__0_in_ruleModelItem155);
            rule__ModelItem__Group__0();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getGroup()); 

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
    // $ANTLR end ruleModelItem


    // $ANTLR start entryRuleModelGroupItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:117:1: entryRuleModelGroupItem : ruleModelGroupItem EOF ;
    public final void entryRuleModelGroupItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:118:1: ( ruleModelGroupItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:119:1: ruleModelGroupItem EOF
            {
             before(grammarAccess.getModelGroupItemRule()); 
            pushFollow(FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem182);
            ruleModelGroupItem();
            _fsp--;

             after(grammarAccess.getModelGroupItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelGroupItem189); 

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
    // $ANTLR end entryRuleModelGroupItem


    // $ANTLR start ruleModelGroupItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:126:1: ruleModelGroupItem : ( ( rule__ModelGroupItem__Group__0 ) ) ;
    public final void ruleModelGroupItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:130:2: ( ( ( rule__ModelGroupItem__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:131:1: ( ( rule__ModelGroupItem__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:131:1: ( ( rule__ModelGroupItem__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:132:1: ( rule__ModelGroupItem__Group__0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:133:1: ( rule__ModelGroupItem__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:133:2: rule__ModelGroupItem__Group__0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__0_in_ruleModelGroupItem215);
            rule__ModelGroupItem__Group__0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getGroup()); 

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
    // $ANTLR end ruleModelGroupItem


    // $ANTLR start entryRuleModelNormalItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:145:1: entryRuleModelNormalItem : ruleModelNormalItem EOF ;
    public final void entryRuleModelNormalItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:146:1: ( ruleModelNormalItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:147:1: ruleModelNormalItem EOF
            {
             before(grammarAccess.getModelNormalItemRule()); 
            pushFollow(FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem242);
            ruleModelNormalItem();
            _fsp--;

             after(grammarAccess.getModelNormalItemRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelNormalItem249); 

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
    // $ANTLR end entryRuleModelNormalItem


    // $ANTLR start ruleModelNormalItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:154:1: ruleModelNormalItem : ( ( rule__ModelNormalItem__TypeAssignment ) ) ;
    public final void ruleModelNormalItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:158:2: ( ( ( rule__ModelNormalItem__TypeAssignment ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:159:1: ( ( rule__ModelNormalItem__TypeAssignment ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:159:1: ( ( rule__ModelNormalItem__TypeAssignment ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:160:1: ( rule__ModelNormalItem__TypeAssignment )
            {
             before(grammarAccess.getModelNormalItemAccess().getTypeAssignment()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:161:1: ( rule__ModelNormalItem__TypeAssignment )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:161:2: rule__ModelNormalItem__TypeAssignment
            {
            pushFollow(FOLLOW_rule__ModelNormalItem__TypeAssignment_in_ruleModelNormalItem275);
            rule__ModelNormalItem__TypeAssignment();
            _fsp--;


            }

             after(grammarAccess.getModelNormalItemAccess().getTypeAssignment()); 

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
    // $ANTLR end ruleModelNormalItem


    // $ANTLR start entryRuleModelItemType
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:173:1: entryRuleModelItemType : ruleModelItemType EOF ;
    public final void entryRuleModelItemType() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:174:1: ( ruleModelItemType EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:175:1: ruleModelItemType EOF
            {
             before(grammarAccess.getModelItemTypeRule()); 
            pushFollow(FOLLOW_ruleModelItemType_in_entryRuleModelItemType302);
            ruleModelItemType();
            _fsp--;

             after(grammarAccess.getModelItemTypeRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelItemType309); 

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
    // $ANTLR end entryRuleModelItemType


    // $ANTLR start ruleModelItemType
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:182:1: ruleModelItemType : ( ( rule__ModelItemType__Alternatives ) ) ;
    public final void ruleModelItemType() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:186:2: ( ( ( rule__ModelItemType__Alternatives ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:187:1: ( ( rule__ModelItemType__Alternatives ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:187:1: ( ( rule__ModelItemType__Alternatives ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:188:1: ( rule__ModelItemType__Alternatives )
            {
             before(grammarAccess.getModelItemTypeAccess().getAlternatives()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:189:1: ( rule__ModelItemType__Alternatives )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:189:2: rule__ModelItemType__Alternatives
            {
            pushFollow(FOLLOW_rule__ModelItemType__Alternatives_in_ruleModelItemType335);
            rule__ModelItemType__Alternatives();
            _fsp--;


            }

             after(grammarAccess.getModelItemTypeAccess().getAlternatives()); 

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
    // $ANTLR end ruleModelItemType


    // $ANTLR start entryRuleModelBinding
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:201:1: entryRuleModelBinding : ruleModelBinding EOF ;
    public final void entryRuleModelBinding() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:202:1: ( ruleModelBinding EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:203:1: ruleModelBinding EOF
            {
             before(grammarAccess.getModelBindingRule()); 
            pushFollow(FOLLOW_ruleModelBinding_in_entryRuleModelBinding362);
            ruleModelBinding();
            _fsp--;

             after(grammarAccess.getModelBindingRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelBinding369); 

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
    // $ANTLR end entryRuleModelBinding


    // $ANTLR start ruleModelBinding
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:210:1: ruleModelBinding : ( ( rule__ModelBinding__Group__0 ) ) ;
    public final void ruleModelBinding() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:214:2: ( ( ( rule__ModelBinding__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:215:1: ( ( rule__ModelBinding__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:215:1: ( ( rule__ModelBinding__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:216:1: ( rule__ModelBinding__Group__0 )
            {
             before(grammarAccess.getModelBindingAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:217:1: ( rule__ModelBinding__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:217:2: rule__ModelBinding__Group__0
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__0_in_ruleModelBinding395);
            rule__ModelBinding__Group__0();
            _fsp--;


            }

             after(grammarAccess.getModelBindingAccess().getGroup()); 

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
    // $ANTLR end ruleModelBinding


    // $ANTLR start ruleModelGroupFunction
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:230:1: ruleModelGroupFunction : ( ( rule__ModelGroupFunction__Alternatives ) ) ;
    public final void ruleModelGroupFunction() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:234:1: ( ( ( rule__ModelGroupFunction__Alternatives ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:235:1: ( ( rule__ModelGroupFunction__Alternatives ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:235:1: ( ( rule__ModelGroupFunction__Alternatives ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:236:1: ( rule__ModelGroupFunction__Alternatives )
            {
             before(grammarAccess.getModelGroupFunctionAccess().getAlternatives()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:237:1: ( rule__ModelGroupFunction__Alternatives )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:237:2: rule__ModelGroupFunction__Alternatives
            {
            pushFollow(FOLLOW_rule__ModelGroupFunction__Alternatives_in_ruleModelGroupFunction432);
            rule__ModelGroupFunction__Alternatives();
            _fsp--;


            }

             after(grammarAccess.getModelGroupFunctionAccess().getAlternatives()); 

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
    // $ANTLR end ruleModelGroupFunction


    // $ANTLR start rule__ModelItem__Alternatives_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:248:1: rule__ModelItem__Alternatives_0 : ( ( ruleModelNormalItem ) | ( ruleModelGroupItem ) );
    public final void rule__ModelItem__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:252:1: ( ( ruleModelNormalItem ) | ( ruleModelGroupItem ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=11 && LA2_0<=16)) ) {
                alt2=1;
            }
            else if ( (LA2_0==26) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("248:1: rule__ModelItem__Alternatives_0 : ( ( ruleModelNormalItem ) | ( ruleModelGroupItem ) );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:253:1: ( ruleModelNormalItem )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:253:1: ( ruleModelNormalItem )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:254:1: ruleModelNormalItem
                    {
                     before(grammarAccess.getModelItemAccess().getModelNormalItemParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleModelNormalItem_in_rule__ModelItem__Alternatives_0467);
                    ruleModelNormalItem();
                    _fsp--;

                     after(grammarAccess.getModelItemAccess().getModelNormalItemParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:259:6: ( ruleModelGroupItem )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:259:6: ( ruleModelGroupItem )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:260:1: ruleModelGroupItem
                    {
                     before(grammarAccess.getModelItemAccess().getModelGroupItemParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleModelGroupItem_in_rule__ModelItem__Alternatives_0484);
                    ruleModelGroupItem();
                    _fsp--;

                     after(grammarAccess.getModelItemAccess().getModelGroupItemParserRuleCall_0_1()); 

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
    // $ANTLR end rule__ModelItem__Alternatives_0


    // $ANTLR start rule__ModelItem__IconAlternatives_3_1_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:270:1: rule__ModelItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__ModelItem__IconAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:274:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("270:1: rule__ModelItem__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:275:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:275:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:276:1: RULE_ID
                    {
                     before(grammarAccess.getModelItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__IconAlternatives_3_1_0516); 
                     after(grammarAccess.getModelItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:281:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:281:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:282:1: RULE_STRING
                    {
                     before(grammarAccess.getModelItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelItem__IconAlternatives_3_1_0533); 
                     after(grammarAccess.getModelItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 

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
    // $ANTLR end rule__ModelItem__IconAlternatives_3_1_0


    // $ANTLR start rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:292:1: rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:296:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                alt4=1;
            }
            else if ( (LA4_0==RULE_STRING) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("292:1: rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:297:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:297:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:298:1: RULE_ID
                    {
                     before(grammarAccess.getModelGroupItemAccess().getActiveStateIDTerminalRuleCall_2_3_2_0_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0565); 
                     after(grammarAccess.getModelGroupItemAccess().getActiveStateIDTerminalRuleCall_2_3_2_0_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:303:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:303:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:304:1: RULE_STRING
                    {
                     before(grammarAccess.getModelGroupItemAccess().getActiveStateSTRINGTerminalRuleCall_2_3_2_0_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0582); 
                     after(grammarAccess.getModelGroupItemAccess().getActiveStateSTRINGTerminalRuleCall_2_3_2_0_0_1()); 

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
    // $ANTLR end rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0


    // $ANTLR start rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:314:1: rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:318:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("314:1: rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:319:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:319:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:320:1: RULE_ID
                    {
                     before(grammarAccess.getModelGroupItemAccess().getPassiveStateIDTerminalRuleCall_2_3_2_2_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0614); 
                     after(grammarAccess.getModelGroupItemAccess().getPassiveStateIDTerminalRuleCall_2_3_2_2_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:325:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:325:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:326:1: RULE_STRING
                    {
                     before(grammarAccess.getModelGroupItemAccess().getPassiveStateSTRINGTerminalRuleCall_2_3_2_2_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0631); 
                     after(grammarAccess.getModelGroupItemAccess().getPassiveStateSTRINGTerminalRuleCall_2_3_2_2_0_1()); 

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
    // $ANTLR end rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0


    // $ANTLR start rule__ModelItemType__Alternatives
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:336:1: rule__ModelItemType__Alternatives : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );
    public final void rule__ModelItemType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:340:1: ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) )
            int alt6=7;
            switch ( input.LA(1) ) {
            case 11:
                {
                alt6=1;
                }
                break;
            case 12:
                {
                alt6=2;
                }
                break;
            case 13:
                {
                alt6=3;
                }
                break;
            case 14:
                {
                alt6=4;
                }
                break;
            case 15:
                {
                alt6=5;
                }
                break;
            case 16:
                {
                alt6=6;
                }
                break;
            case RULE_ID:
                {
                alt6=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("336:1: rule__ModelItemType__Alternatives : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:341:1: ( 'Switch' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:341:1: ( 'Switch' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:342:1: 'Switch'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getSwitchKeyword_0()); 
                    match(input,11,FOLLOW_11_in_rule__ModelItemType__Alternatives664); 
                     after(grammarAccess.getModelItemTypeAccess().getSwitchKeyword_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:349:6: ( 'Rollershutter' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:349:6: ( 'Rollershutter' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:350:1: 'Rollershutter'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getRollershutterKeyword_1()); 
                    match(input,12,FOLLOW_12_in_rule__ModelItemType__Alternatives684); 
                     after(grammarAccess.getModelItemTypeAccess().getRollershutterKeyword_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:357:6: ( 'Number' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:357:6: ( 'Number' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:358:1: 'Number'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getNumberKeyword_2()); 
                    match(input,13,FOLLOW_13_in_rule__ModelItemType__Alternatives704); 
                     after(grammarAccess.getModelItemTypeAccess().getNumberKeyword_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:365:6: ( 'String' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:365:6: ( 'String' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:366:1: 'String'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getStringKeyword_3()); 
                    match(input,14,FOLLOW_14_in_rule__ModelItemType__Alternatives724); 
                     after(grammarAccess.getModelItemTypeAccess().getStringKeyword_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:373:6: ( 'Dimmer' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:373:6: ( 'Dimmer' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:374:1: 'Dimmer'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getDimmerKeyword_4()); 
                    match(input,15,FOLLOW_15_in_rule__ModelItemType__Alternatives744); 
                     after(grammarAccess.getModelItemTypeAccess().getDimmerKeyword_4()); 

                    }


                    }
                    break;
                case 6 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:381:6: ( 'Contact' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:381:6: ( 'Contact' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:382:1: 'Contact'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getContactKeyword_5()); 
                    match(input,16,FOLLOW_16_in_rule__ModelItemType__Alternatives764); 
                     after(grammarAccess.getModelItemTypeAccess().getContactKeyword_5()); 

                    }


                    }
                    break;
                case 7 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:389:6: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:389:6: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:390:1: RULE_ID
                    {
                     before(grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_6()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItemType__Alternatives783); 
                     after(grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_6()); 

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
    // $ANTLR end rule__ModelItemType__Alternatives


    // $ANTLR start rule__ModelGroupFunction__Alternatives
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:400:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) );
    public final void rule__ModelGroupFunction__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:404:1: ( ( ( 'AND' ) ) | ( ( 'OR' ) ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==17) ) {
                alt7=1;
            }
            else if ( (LA7_0==18) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("400:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:405:1: ( ( 'AND' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:405:1: ( ( 'AND' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:406:1: ( 'AND' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:407:1: ( 'AND' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:407:3: 'AND'
                    {
                    match(input,17,FOLLOW_17_in_rule__ModelGroupFunction__Alternatives816); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:412:6: ( ( 'OR' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:412:6: ( ( 'OR' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:413:1: ( 'OR' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:414:1: ( 'OR' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:414:3: 'OR'
                    {
                    match(input,18,FOLLOW_18_in_rule__ModelGroupFunction__Alternatives837); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1()); 

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
    // $ANTLR end rule__ModelGroupFunction__Alternatives


    // $ANTLR start rule__ModelItem__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:426:1: rule__ModelItem__Group__0 : rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 ;
    public final void rule__ModelItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:430:1: ( rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:431:2: rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0870);
            rule__ModelItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0873);
            rule__ModelItem__Group__1();
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
    // $ANTLR end rule__ModelItem__Group__0


    // $ANTLR start rule__ModelItem__Group__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:438:1: rule__ModelItem__Group__0__Impl : ( ( rule__ModelItem__Alternatives_0 ) ) ;
    public final void rule__ModelItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:442:1: ( ( ( rule__ModelItem__Alternatives_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:443:1: ( ( rule__ModelItem__Alternatives_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:443:1: ( ( rule__ModelItem__Alternatives_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:444:1: ( rule__ModelItem__Alternatives_0 )
            {
             before(grammarAccess.getModelItemAccess().getAlternatives_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:445:1: ( rule__ModelItem__Alternatives_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:445:2: rule__ModelItem__Alternatives_0
            {
            pushFollow(FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl900);
            rule__ModelItem__Alternatives_0();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getAlternatives_0()); 

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
    // $ANTLR end rule__ModelItem__Group__0__Impl


    // $ANTLR start rule__ModelItem__Group__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:455:1: rule__ModelItem__Group__1 : rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 ;
    public final void rule__ModelItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:459:1: ( rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:460:2: rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__1930);
            rule__ModelItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__1933);
            rule__ModelItem__Group__2();
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
    // $ANTLR end rule__ModelItem__Group__1


    // $ANTLR start rule__ModelItem__Group__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:467:1: rule__ModelItem__Group__1__Impl : ( ( rule__ModelItem__NameAssignment_1 ) ) ;
    public final void rule__ModelItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:471:1: ( ( ( rule__ModelItem__NameAssignment_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:473:1: ( rule__ModelItem__NameAssignment_1 )
            {
             before(grammarAccess.getModelItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:1: ( rule__ModelItem__NameAssignment_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:2: rule__ModelItem__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl960);
            rule__ModelItem__NameAssignment_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getNameAssignment_1()); 

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
    // $ANTLR end rule__ModelItem__Group__1__Impl


    // $ANTLR start rule__ModelItem__Group__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:484:1: rule__ModelItem__Group__2 : rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 ;
    public final void rule__ModelItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:488:1: ( rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:489:2: rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__2990);
            rule__ModelItem__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__2993);
            rule__ModelItem__Group__3();
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
    // $ANTLR end rule__ModelItem__Group__2


    // $ANTLR start rule__ModelItem__Group__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:496:1: rule__ModelItem__Group__2__Impl : ( ( rule__ModelItem__LabelAssignment_2 )? ) ;
    public final void rule__ModelItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:500:1: ( ( ( rule__ModelItem__LabelAssignment_2 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:501:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:501:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:502:1: ( rule__ModelItem__LabelAssignment_2 )?
            {
             before(grammarAccess.getModelItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:503:1: ( rule__ModelItem__LabelAssignment_2 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==RULE_STRING) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:503:2: rule__ModelItem__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1020);
                    rule__ModelItem__LabelAssignment_2();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelItemAccess().getLabelAssignment_2()); 

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
    // $ANTLR end rule__ModelItem__Group__2__Impl


    // $ANTLR start rule__ModelItem__Group__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:513:1: rule__ModelItem__Group__3 : rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 ;
    public final void rule__ModelItem__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:517:1: ( rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:518:2: rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31051);
            rule__ModelItem__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31054);
            rule__ModelItem__Group__4();
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
    // $ANTLR end rule__ModelItem__Group__3


    // $ANTLR start rule__ModelItem__Group__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:525:1: rule__ModelItem__Group__3__Impl : ( ( rule__ModelItem__Group_3__0 )? ) ;
    public final void rule__ModelItem__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:529:1: ( ( ( rule__ModelItem__Group_3__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:530:1: ( ( rule__ModelItem__Group_3__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:530:1: ( ( rule__ModelItem__Group_3__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:531:1: ( rule__ModelItem__Group_3__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_3()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:532:1: ( rule__ModelItem__Group_3__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==19) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:532:2: rule__ModelItem__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1081);
                    rule__ModelItem__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelItemAccess().getGroup_3()); 

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
    // $ANTLR end rule__ModelItem__Group__3__Impl


    // $ANTLR start rule__ModelItem__Group__4
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:542:1: rule__ModelItem__Group__4 : rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 ;
    public final void rule__ModelItem__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:546:1: ( rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:547:2: rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41112);
            rule__ModelItem__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41115);
            rule__ModelItem__Group__5();
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
    // $ANTLR end rule__ModelItem__Group__4


    // $ANTLR start rule__ModelItem__Group__4__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:554:1: rule__ModelItem__Group__4__Impl : ( ( rule__ModelItem__Group_4__0 )? ) ;
    public final void rule__ModelItem__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:558:1: ( ( ( rule__ModelItem__Group_4__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:559:1: ( ( rule__ModelItem__Group_4__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:559:1: ( ( rule__ModelItem__Group_4__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:560:1: ( rule__ModelItem__Group_4__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_4()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:561:1: ( rule__ModelItem__Group_4__0 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==21) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:561:2: rule__ModelItem__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1142);
                    rule__ModelItem__Group_4__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelItemAccess().getGroup_4()); 

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
    // $ANTLR end rule__ModelItem__Group__4__Impl


    // $ANTLR start rule__ModelItem__Group__5
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:571:1: rule__ModelItem__Group__5 : rule__ModelItem__Group__5__Impl ;
    public final void rule__ModelItem__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:575:1: ( rule__ModelItem__Group__5__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:576:2: rule__ModelItem__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51173);
            rule__ModelItem__Group__5__Impl();
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
    // $ANTLR end rule__ModelItem__Group__5


    // $ANTLR start rule__ModelItem__Group__5__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:582:1: rule__ModelItem__Group__5__Impl : ( ( rule__ModelItem__Group_5__0 )* ) ;
    public final void rule__ModelItem__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:586:1: ( ( ( rule__ModelItem__Group_5__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:587:1: ( ( rule__ModelItem__Group_5__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:587:1: ( ( rule__ModelItem__Group_5__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:588:1: ( rule__ModelItem__Group_5__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:589:1: ( rule__ModelItem__Group_5__0 )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==24) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:589:2: rule__ModelItem__Group_5__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1200);
            	    rule__ModelItem__Group_5__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

             after(grammarAccess.getModelItemAccess().getGroup_5()); 

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
    // $ANTLR end rule__ModelItem__Group__5__Impl


    // $ANTLR start rule__ModelItem__Group_3__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:611:1: rule__ModelItem__Group_3__0 : rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 ;
    public final void rule__ModelItem__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:615:1: ( rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:616:2: rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01243);
            rule__ModelItem__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01246);
            rule__ModelItem__Group_3__1();
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
    // $ANTLR end rule__ModelItem__Group_3__0


    // $ANTLR start rule__ModelItem__Group_3__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:623:1: rule__ModelItem__Group_3__0__Impl : ( '<' ) ;
    public final void rule__ModelItem__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:627:1: ( ( '<' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:628:1: ( '<' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:628:1: ( '<' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:629:1: '<'
            {
             before(grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,19,FOLLOW_19_in_rule__ModelItem__Group_3__0__Impl1274); 
             after(grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0()); 

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
    // $ANTLR end rule__ModelItem__Group_3__0__Impl


    // $ANTLR start rule__ModelItem__Group_3__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:642:1: rule__ModelItem__Group_3__1 : rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 ;
    public final void rule__ModelItem__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:646:1: ( rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:647:2: rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11305);
            rule__ModelItem__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11308);
            rule__ModelItem__Group_3__2();
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
    // $ANTLR end rule__ModelItem__Group_3__1


    // $ANTLR start rule__ModelItem__Group_3__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:654:1: rule__ModelItem__Group_3__1__Impl : ( ( rule__ModelItem__IconAssignment_3_1 ) ) ;
    public final void rule__ModelItem__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:658:1: ( ( ( rule__ModelItem__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:659:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:659:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:660:1: ( rule__ModelItem__IconAssignment_3_1 )
            {
             before(grammarAccess.getModelItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:661:1: ( rule__ModelItem__IconAssignment_3_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:661:2: rule__ModelItem__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1335);
            rule__ModelItem__IconAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getIconAssignment_3_1()); 

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
    // $ANTLR end rule__ModelItem__Group_3__1__Impl


    // $ANTLR start rule__ModelItem__Group_3__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:671:1: rule__ModelItem__Group_3__2 : rule__ModelItem__Group_3__2__Impl ;
    public final void rule__ModelItem__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:675:1: ( rule__ModelItem__Group_3__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:676:2: rule__ModelItem__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21365);
            rule__ModelItem__Group_3__2__Impl();
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
    // $ANTLR end rule__ModelItem__Group_3__2


    // $ANTLR start rule__ModelItem__Group_3__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:682:1: rule__ModelItem__Group_3__2__Impl : ( '>' ) ;
    public final void rule__ModelItem__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:686:1: ( ( '>' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:687:1: ( '>' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:687:1: ( '>' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:688:1: '>'
            {
             before(grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,20,FOLLOW_20_in_rule__ModelItem__Group_3__2__Impl1393); 
             after(grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2()); 

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
    // $ANTLR end rule__ModelItem__Group_3__2__Impl


    // $ANTLR start rule__ModelItem__Group_4__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:707:1: rule__ModelItem__Group_4__0 : rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 ;
    public final void rule__ModelItem__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:711:1: ( rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:712:2: rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01430);
            rule__ModelItem__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01433);
            rule__ModelItem__Group_4__1();
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
    // $ANTLR end rule__ModelItem__Group_4__0


    // $ANTLR start rule__ModelItem__Group_4__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:719:1: rule__ModelItem__Group_4__0__Impl : ( '(' ) ;
    public final void rule__ModelItem__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:723:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:724:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:724:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:725:1: '('
            {
             before(grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,21,FOLLOW_21_in_rule__ModelItem__Group_4__0__Impl1461); 
             after(grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0()); 

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
    // $ANTLR end rule__ModelItem__Group_4__0__Impl


    // $ANTLR start rule__ModelItem__Group_4__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:738:1: rule__ModelItem__Group_4__1 : rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 ;
    public final void rule__ModelItem__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:742:1: ( rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:743:2: rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11492);
            rule__ModelItem__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11495);
            rule__ModelItem__Group_4__2();
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
    // $ANTLR end rule__ModelItem__Group_4__1


    // $ANTLR start rule__ModelItem__Group_4__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:750:1: rule__ModelItem__Group_4__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) ;
    public final void rule__ModelItem__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:754:1: ( ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:755:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:755:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:756:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:757:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:757:2: rule__ModelItem__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1522);
            rule__ModelItem__GroupsAssignment_4_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getGroupsAssignment_4_1()); 

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
    // $ANTLR end rule__ModelItem__Group_4__1__Impl


    // $ANTLR start rule__ModelItem__Group_4__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:767:1: rule__ModelItem__Group_4__2 : rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 ;
    public final void rule__ModelItem__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:771:1: ( rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:772:2: rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21552);
            rule__ModelItem__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21555);
            rule__ModelItem__Group_4__3();
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
    // $ANTLR end rule__ModelItem__Group_4__2


    // $ANTLR start rule__ModelItem__Group_4__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:779:1: rule__ModelItem__Group_4__2__Impl : ( ( rule__ModelItem__Group_4_2__0 )* ) ;
    public final void rule__ModelItem__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:783:1: ( ( ( rule__ModelItem__Group_4_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:784:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:784:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:785:1: ( rule__ModelItem__Group_4_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:786:1: ( rule__ModelItem__Group_4_2__0 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==23) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:786:2: rule__ModelItem__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1582);
            	    rule__ModelItem__Group_4_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

             after(grammarAccess.getModelItemAccess().getGroup_4_2()); 

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
    // $ANTLR end rule__ModelItem__Group_4__2__Impl


    // $ANTLR start rule__ModelItem__Group_4__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:796:1: rule__ModelItem__Group_4__3 : rule__ModelItem__Group_4__3__Impl ;
    public final void rule__ModelItem__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:800:1: ( rule__ModelItem__Group_4__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:801:2: rule__ModelItem__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31613);
            rule__ModelItem__Group_4__3__Impl();
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
    // $ANTLR end rule__ModelItem__Group_4__3


    // $ANTLR start rule__ModelItem__Group_4__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:807:1: rule__ModelItem__Group_4__3__Impl : ( ')' ) ;
    public final void rule__ModelItem__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:811:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:812:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:812:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:813:1: ')'
            {
             before(grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,22,FOLLOW_22_in_rule__ModelItem__Group_4__3__Impl1641); 
             after(grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3()); 

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
    // $ANTLR end rule__ModelItem__Group_4__3__Impl


    // $ANTLR start rule__ModelItem__Group_4_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:834:1: rule__ModelItem__Group_4_2__0 : rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 ;
    public final void rule__ModelItem__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:838:1: ( rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:839:2: rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01680);
            rule__ModelItem__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01683);
            rule__ModelItem__Group_4_2__1();
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
    // $ANTLR end rule__ModelItem__Group_4_2__0


    // $ANTLR start rule__ModelItem__Group_4_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:846:1: rule__ModelItem__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:850:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:851:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:851:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:852:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0()); 
            match(input,23,FOLLOW_23_in_rule__ModelItem__Group_4_2__0__Impl1711); 
             after(grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0()); 

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
    // $ANTLR end rule__ModelItem__Group_4_2__0__Impl


    // $ANTLR start rule__ModelItem__Group_4_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:865:1: rule__ModelItem__Group_4_2__1 : rule__ModelItem__Group_4_2__1__Impl ;
    public final void rule__ModelItem__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:869:1: ( rule__ModelItem__Group_4_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:870:2: rule__ModelItem__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11742);
            rule__ModelItem__Group_4_2__1__Impl();
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
    // $ANTLR end rule__ModelItem__Group_4_2__1


    // $ANTLR start rule__ModelItem__Group_4_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:876:1: rule__ModelItem__Group_4_2__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__ModelItem__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:880:1: ( ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:881:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:881:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:882:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:883:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:883:2: rule__ModelItem__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1769);
            rule__ModelItem__GroupsAssignment_4_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getGroupsAssignment_4_2_1()); 

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
    // $ANTLR end rule__ModelItem__Group_4_2__1__Impl


    // $ANTLR start rule__ModelItem__Group_5__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:897:1: rule__ModelItem__Group_5__0 : rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 ;
    public final void rule__ModelItem__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:901:1: ( rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:902:2: rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01803);
            rule__ModelItem__Group_5__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01806);
            rule__ModelItem__Group_5__1();
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
    // $ANTLR end rule__ModelItem__Group_5__0


    // $ANTLR start rule__ModelItem__Group_5__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:909:1: rule__ModelItem__Group_5__0__Impl : ( '{' ) ;
    public final void rule__ModelItem__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:913:1: ( ( '{' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:914:1: ( '{' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:914:1: ( '{' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:915:1: '{'
            {
             before(grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0()); 
            match(input,24,FOLLOW_24_in_rule__ModelItem__Group_5__0__Impl1834); 
             after(grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0()); 

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
    // $ANTLR end rule__ModelItem__Group_5__0__Impl


    // $ANTLR start rule__ModelItem__Group_5__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:928:1: rule__ModelItem__Group_5__1 : rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 ;
    public final void rule__ModelItem__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:932:1: ( rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:933:2: rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11865);
            rule__ModelItem__Group_5__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11868);
            rule__ModelItem__Group_5__2();
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
    // $ANTLR end rule__ModelItem__Group_5__1


    // $ANTLR start rule__ModelItem__Group_5__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:940:1: rule__ModelItem__Group_5__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) ;
    public final void rule__ModelItem__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:944:1: ( ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:945:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:945:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:946:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:947:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:947:2: rule__ModelItem__BindingsAssignment_5_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1895);
            rule__ModelItem__BindingsAssignment_5_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getBindingsAssignment_5_1()); 

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
    // $ANTLR end rule__ModelItem__Group_5__1__Impl


    // $ANTLR start rule__ModelItem__Group_5__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:957:1: rule__ModelItem__Group_5__2 : rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 ;
    public final void rule__ModelItem__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:961:1: ( rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:962:2: rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__21925);
            rule__ModelItem__Group_5__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__21928);
            rule__ModelItem__Group_5__3();
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
    // $ANTLR end rule__ModelItem__Group_5__2


    // $ANTLR start rule__ModelItem__Group_5__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:969:1: rule__ModelItem__Group_5__2__Impl : ( ( rule__ModelItem__Group_5_2__0 )* ) ;
    public final void rule__ModelItem__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:973:1: ( ( ( rule__ModelItem__Group_5_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:974:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:974:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:975:1: ( rule__ModelItem__Group_5_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:976:1: ( rule__ModelItem__Group_5_2__0 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==23) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:976:2: rule__ModelItem__Group_5_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl1955);
            	    rule__ModelItem__Group_5_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getModelItemAccess().getGroup_5_2()); 

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
    // $ANTLR end rule__ModelItem__Group_5__2__Impl


    // $ANTLR start rule__ModelItem__Group_5__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:986:1: rule__ModelItem__Group_5__3 : rule__ModelItem__Group_5__3__Impl ;
    public final void rule__ModelItem__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:990:1: ( rule__ModelItem__Group_5__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:991:2: rule__ModelItem__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__31986);
            rule__ModelItem__Group_5__3__Impl();
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
    // $ANTLR end rule__ModelItem__Group_5__3


    // $ANTLR start rule__ModelItem__Group_5__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:997:1: rule__ModelItem__Group_5__3__Impl : ( '}' ) ;
    public final void rule__ModelItem__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1001:1: ( ( '}' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1002:1: ( '}' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1002:1: ( '}' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1003:1: '}'
            {
             before(grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,25,FOLLOW_25_in_rule__ModelItem__Group_5__3__Impl2014); 
             after(grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3()); 

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
    // $ANTLR end rule__ModelItem__Group_5__3__Impl


    // $ANTLR start rule__ModelItem__Group_5_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1024:1: rule__ModelItem__Group_5_2__0 : rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 ;
    public final void rule__ModelItem__Group_5_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1028:1: ( rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1029:2: rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02053);
            rule__ModelItem__Group_5_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02056);
            rule__ModelItem__Group_5_2__1();
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
    // $ANTLR end rule__ModelItem__Group_5_2__0


    // $ANTLR start rule__ModelItem__Group_5_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1036:1: rule__ModelItem__Group_5_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_5_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1040:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1041:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1041:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1042:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0()); 
            match(input,23,FOLLOW_23_in_rule__ModelItem__Group_5_2__0__Impl2084); 
             after(grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0()); 

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
    // $ANTLR end rule__ModelItem__Group_5_2__0__Impl


    // $ANTLR start rule__ModelItem__Group_5_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1055:1: rule__ModelItem__Group_5_2__1 : rule__ModelItem__Group_5_2__1__Impl ;
    public final void rule__ModelItem__Group_5_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1059:1: ( rule__ModelItem__Group_5_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1060:2: rule__ModelItem__Group_5_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12115);
            rule__ModelItem__Group_5_2__1__Impl();
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
    // $ANTLR end rule__ModelItem__Group_5_2__1


    // $ANTLR start rule__ModelItem__Group_5_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1066:1: rule__ModelItem__Group_5_2__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) ;
    public final void rule__ModelItem__Group_5_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1070:1: ( ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1071:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1071:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1072:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1073:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1073:2: rule__ModelItem__BindingsAssignment_5_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2142);
            rule__ModelItem__BindingsAssignment_5_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getBindingsAssignment_5_2_1()); 

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
    // $ANTLR end rule__ModelItem__Group_5_2__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1087:1: rule__ModelGroupItem__Group__0 : rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 ;
    public final void rule__ModelGroupItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1091:1: ( rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1092:2: rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02176);
            rule__ModelGroupItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02179);
            rule__ModelGroupItem__Group__1();
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
    // $ANTLR end rule__ModelGroupItem__Group__0


    // $ANTLR start rule__ModelGroupItem__Group__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1099:1: rule__ModelGroupItem__Group__0__Impl : ( 'Group' ) ;
    public final void rule__ModelGroupItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1103:1: ( ( 'Group' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1104:1: ( 'Group' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1104:1: ( 'Group' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1105:1: 'Group'
            {
             before(grammarAccess.getModelGroupItemAccess().getGroupKeyword_0()); 
            match(input,26,FOLLOW_26_in_rule__ModelGroupItem__Group__0__Impl2207); 
             after(grammarAccess.getModelGroupItemAccess().getGroupKeyword_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1118:1: rule__ModelGroupItem__Group__1 : rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 ;
    public final void rule__ModelGroupItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1122:1: ( rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1123:2: rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12238);
            rule__ModelGroupItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12241);
            rule__ModelGroupItem__Group__2();
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
    // $ANTLR end rule__ModelGroupItem__Group__1


    // $ANTLR start rule__ModelGroupItem__Group__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1130:1: rule__ModelGroupItem__Group__1__Impl : ( () ) ;
    public final void rule__ModelGroupItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1134:1: ( ( () ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1135:1: ( () )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1135:1: ( () )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1136:1: ()
            {
             before(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1137:1: ()
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1139:1: 
            {
            }

             after(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__ModelGroupItem__Group__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1149:1: rule__ModelGroupItem__Group__2 : rule__ModelGroupItem__Group__2__Impl ;
    public final void rule__ModelGroupItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1153:1: ( rule__ModelGroupItem__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1154:2: rule__ModelGroupItem__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22299);
            rule__ModelGroupItem__Group__2__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group__2


    // $ANTLR start rule__ModelGroupItem__Group__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1160:1: rule__ModelGroupItem__Group__2__Impl : ( ( rule__ModelGroupItem__Group_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1164:1: ( ( ( rule__ModelGroupItem__Group_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1165:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1165:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1166:1: ( rule__ModelGroupItem__Group_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1167:1: ( rule__ModelGroupItem__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==19) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1167:2: rule__ModelGroupItem__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2326);
                    rule__ModelGroupItem__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelGroupItemAccess().getGroup_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1183:1: rule__ModelGroupItem__Group_2__0 : rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 ;
    public final void rule__ModelGroupItem__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1187:1: ( rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1188:2: rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02363);
            rule__ModelGroupItem__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02366);
            rule__ModelGroupItem__Group_2__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2__0


    // $ANTLR start rule__ModelGroupItem__Group_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1195:1: rule__ModelGroupItem__Group_2__0__Impl : ( '<' ) ;
    public final void rule__ModelGroupItem__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1199:1: ( ( '<' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1200:1: ( '<' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1200:1: ( '<' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1201:1: '<'
            {
             before(grammarAccess.getModelGroupItemAccess().getLessThanSignKeyword_2_0()); 
            match(input,19,FOLLOW_19_in_rule__ModelGroupItem__Group_2__0__Impl2394); 
             after(grammarAccess.getModelGroupItemAccess().getLessThanSignKeyword_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1214:1: rule__ModelGroupItem__Group_2__1 : rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 ;
    public final void rule__ModelGroupItem__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1218:1: ( rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1219:2: rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12425);
            rule__ModelGroupItem__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12428);
            rule__ModelGroupItem__Group_2__2();
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
    // $ANTLR end rule__ModelGroupItem__Group_2__1


    // $ANTLR start rule__ModelGroupItem__Group_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1226:1: rule__ModelGroupItem__Group_2__1__Impl : ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1230:1: ( ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1231:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1231:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1232:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeAssignment_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1233:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1233:2: rule__ModelGroupItem__TypeAssignment_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2455);
            rule__ModelGroupItem__TypeAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getTypeAssignment_2_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1243:1: rule__ModelGroupItem__Group_2__2 : rule__ModelGroupItem__Group_2__2__Impl rule__ModelGroupItem__Group_2__3 ;
    public final void rule__ModelGroupItem__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1247:1: ( rule__ModelGroupItem__Group_2__2__Impl rule__ModelGroupItem__Group_2__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1248:2: rule__ModelGroupItem__Group_2__2__Impl rule__ModelGroupItem__Group_2__3
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22485);
            rule__ModelGroupItem__Group_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__3_in_rule__ModelGroupItem__Group_2__22488);
            rule__ModelGroupItem__Group_2__3();
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
    // $ANTLR end rule__ModelGroupItem__Group_2__2


    // $ANTLR start rule__ModelGroupItem__Group_2__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1255:1: rule__ModelGroupItem__Group_2__2__Impl : ( '>' ) ;
    public final void rule__ModelGroupItem__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1259:1: ( ( '>' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1260:1: ( '>' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1260:1: ( '>' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1261:1: '>'
            {
             before(grammarAccess.getModelGroupItemAccess().getGreaterThanSignKeyword_2_2()); 
            match(input,20,FOLLOW_20_in_rule__ModelGroupItem__Group_2__2__Impl2516); 
             after(grammarAccess.getModelGroupItemAccess().getGreaterThanSignKeyword_2_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1274:1: rule__ModelGroupItem__Group_2__3 : rule__ModelGroupItem__Group_2__3__Impl ;
    public final void rule__ModelGroupItem__Group_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1278:1: ( rule__ModelGroupItem__Group_2__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1279:2: rule__ModelGroupItem__Group_2__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__3__Impl_in_rule__ModelGroupItem__Group_2__32547);
            rule__ModelGroupItem__Group_2__3__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2__3


    // $ANTLR start rule__ModelGroupItem__Group_2__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1285:1: rule__ModelGroupItem__Group_2__3__Impl : ( ( rule__ModelGroupItem__Group_2_3__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1289:1: ( ( ( rule__ModelGroupItem__Group_2_3__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1290:1: ( ( rule__ModelGroupItem__Group_2_3__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1290:1: ( ( rule__ModelGroupItem__Group_2_3__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1291:1: ( rule__ModelGroupItem__Group_2_3__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_3()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1292:1: ( rule__ModelGroupItem__Group_2_3__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( ((LA15_0>=17 && LA15_0<=18)) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1292:2: rule__ModelGroupItem__Group_2_3__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__0_in_rule__ModelGroupItem__Group_2__3__Impl2574);
                    rule__ModelGroupItem__Group_2_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelGroupItemAccess().getGroup_2_3()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2__3__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1310:1: rule__ModelGroupItem__Group_2_3__0 : rule__ModelGroupItem__Group_2_3__0__Impl rule__ModelGroupItem__Group_2_3__1 ;
    public final void rule__ModelGroupItem__Group_2_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1314:1: ( rule__ModelGroupItem__Group_2_3__0__Impl rule__ModelGroupItem__Group_2_3__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1315:2: rule__ModelGroupItem__Group_2_3__0__Impl rule__ModelGroupItem__Group_2_3__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__0__Impl_in_rule__ModelGroupItem__Group_2_3__02613);
            rule__ModelGroupItem__Group_2_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__1_in_rule__ModelGroupItem__Group_2_3__02616);
            rule__ModelGroupItem__Group_2_3__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__0


    // $ANTLR start rule__ModelGroupItem__Group_2_3__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1322:1: rule__ModelGroupItem__Group_2_3__0__Impl : ( ( rule__ModelGroupItem__FunctionAssignment_2_3_0 ) ) ;
    public final void rule__ModelGroupItem__Group_2_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1326:1: ( ( ( rule__ModelGroupItem__FunctionAssignment_2_3_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1327:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_3_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1327:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_3_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1328:1: ( rule__ModelGroupItem__FunctionAssignment_2_3_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionAssignment_2_3_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1329:1: ( rule__ModelGroupItem__FunctionAssignment_2_3_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1329:2: rule__ModelGroupItem__FunctionAssignment_2_3_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_3_0_in_rule__ModelGroupItem__Group_2_3__0__Impl2643);
            rule__ModelGroupItem__FunctionAssignment_2_3_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getFunctionAssignment_2_3_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1339:1: rule__ModelGroupItem__Group_2_3__1 : rule__ModelGroupItem__Group_2_3__1__Impl rule__ModelGroupItem__Group_2_3__2 ;
    public final void rule__ModelGroupItem__Group_2_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1343:1: ( rule__ModelGroupItem__Group_2_3__1__Impl rule__ModelGroupItem__Group_2_3__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1344:2: rule__ModelGroupItem__Group_2_3__1__Impl rule__ModelGroupItem__Group_2_3__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__1__Impl_in_rule__ModelGroupItem__Group_2_3__12673);
            rule__ModelGroupItem__Group_2_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__2_in_rule__ModelGroupItem__Group_2_3__12676);
            rule__ModelGroupItem__Group_2_3__2();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__1


    // $ANTLR start rule__ModelGroupItem__Group_2_3__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1351:1: rule__ModelGroupItem__Group_2_3__1__Impl : ( '[' ) ;
    public final void rule__ModelGroupItem__Group_2_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1355:1: ( ( '[' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1356:1: ( '[' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1356:1: ( '[' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1357:1: '['
            {
             before(grammarAccess.getModelGroupItemAccess().getLeftSquareBracketKeyword_2_3_1()); 
            match(input,27,FOLLOW_27_in_rule__ModelGroupItem__Group_2_3__1__Impl2704); 
             after(grammarAccess.getModelGroupItemAccess().getLeftSquareBracketKeyword_2_3_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1370:1: rule__ModelGroupItem__Group_2_3__2 : rule__ModelGroupItem__Group_2_3__2__Impl ;
    public final void rule__ModelGroupItem__Group_2_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1374:1: ( rule__ModelGroupItem__Group_2_3__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1375:2: rule__ModelGroupItem__Group_2_3__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3__2__Impl_in_rule__ModelGroupItem__Group_2_3__22735);
            rule__ModelGroupItem__Group_2_3__2__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__2


    // $ANTLR start rule__ModelGroupItem__Group_2_3__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1381:1: rule__ModelGroupItem__Group_2_3__2__Impl : ( ( rule__ModelGroupItem__Group_2_3_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1385:1: ( ( ( rule__ModelGroupItem__Group_2_3_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1386:1: ( ( rule__ModelGroupItem__Group_2_3_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1386:1: ( ( rule__ModelGroupItem__Group_2_3_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1387:1: ( rule__ModelGroupItem__Group_2_3_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_3_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1388:1: ( rule__ModelGroupItem__Group_2_3_2__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==RULE_ID) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==23) ) {
                    alt16=1;
                }
            }
            else if ( (LA16_0==RULE_STRING) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1388:2: rule__ModelGroupItem__Group_2_3_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__0_in_rule__ModelGroupItem__Group_2_3__2__Impl2762);
                    rule__ModelGroupItem__Group_2_3_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelGroupItemAccess().getGroup_2_3_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1404:1: rule__ModelGroupItem__Group_2_3_2__0 : rule__ModelGroupItem__Group_2_3_2__0__Impl rule__ModelGroupItem__Group_2_3_2__1 ;
    public final void rule__ModelGroupItem__Group_2_3_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1408:1: ( rule__ModelGroupItem__Group_2_3_2__0__Impl rule__ModelGroupItem__Group_2_3_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1409:2: rule__ModelGroupItem__Group_2_3_2__0__Impl rule__ModelGroupItem__Group_2_3_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__0__Impl_in_rule__ModelGroupItem__Group_2_3_2__02799);
            rule__ModelGroupItem__Group_2_3_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__1_in_rule__ModelGroupItem__Group_2_3_2__02802);
            rule__ModelGroupItem__Group_2_3_2__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__0


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1416:1: rule__ModelGroupItem__Group_2_3_2__0__Impl : ( ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 ) ) ;
    public final void rule__ModelGroupItem__Group_2_3_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1420:1: ( ( ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1421:1: ( ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1421:1: ( ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1422:1: ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getActiveStateAssignment_2_3_2_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1423:1: ( rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1423:2: rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0_in_rule__ModelGroupItem__Group_2_3_2__0__Impl2829);
            rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getActiveStateAssignment_2_3_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1433:1: rule__ModelGroupItem__Group_2_3_2__1 : rule__ModelGroupItem__Group_2_3_2__1__Impl rule__ModelGroupItem__Group_2_3_2__2 ;
    public final void rule__ModelGroupItem__Group_2_3_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1437:1: ( rule__ModelGroupItem__Group_2_3_2__1__Impl rule__ModelGroupItem__Group_2_3_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1438:2: rule__ModelGroupItem__Group_2_3_2__1__Impl rule__ModelGroupItem__Group_2_3_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__1__Impl_in_rule__ModelGroupItem__Group_2_3_2__12859);
            rule__ModelGroupItem__Group_2_3_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__2_in_rule__ModelGroupItem__Group_2_3_2__12862);
            rule__ModelGroupItem__Group_2_3_2__2();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__1


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1445:1: rule__ModelGroupItem__Group_2_3_2__1__Impl : ( ',' ) ;
    public final void rule__ModelGroupItem__Group_2_3_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1449:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1450:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1450:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1451:1: ','
            {
             before(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_3_2_1()); 
            match(input,23,FOLLOW_23_in_rule__ModelGroupItem__Group_2_3_2__1__Impl2890); 
             after(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_3_2_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1464:1: rule__ModelGroupItem__Group_2_3_2__2 : rule__ModelGroupItem__Group_2_3_2__2__Impl rule__ModelGroupItem__Group_2_3_2__3 ;
    public final void rule__ModelGroupItem__Group_2_3_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1468:1: ( rule__ModelGroupItem__Group_2_3_2__2__Impl rule__ModelGroupItem__Group_2_3_2__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1469:2: rule__ModelGroupItem__Group_2_3_2__2__Impl rule__ModelGroupItem__Group_2_3_2__3
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__2__Impl_in_rule__ModelGroupItem__Group_2_3_2__22921);
            rule__ModelGroupItem__Group_2_3_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__3_in_rule__ModelGroupItem__Group_2_3_2__22924);
            rule__ModelGroupItem__Group_2_3_2__3();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__2


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1476:1: rule__ModelGroupItem__Group_2_3_2__2__Impl : ( ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 ) ) ;
    public final void rule__ModelGroupItem__Group_2_3_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1480:1: ( ( ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1481:1: ( ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1481:1: ( ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1482:1: ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 )
            {
             before(grammarAccess.getModelGroupItemAccess().getPassiveStateAssignment_2_3_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1483:1: ( rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1483:2: rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2_in_rule__ModelGroupItem__Group_2_3_2__2__Impl2951);
            rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getPassiveStateAssignment_2_3_2_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1493:1: rule__ModelGroupItem__Group_2_3_2__3 : rule__ModelGroupItem__Group_2_3_2__3__Impl ;
    public final void rule__ModelGroupItem__Group_2_3_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1497:1: ( rule__ModelGroupItem__Group_2_3_2__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1498:2: rule__ModelGroupItem__Group_2_3_2__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_3_2__3__Impl_in_rule__ModelGroupItem__Group_2_3_2__32981);
            rule__ModelGroupItem__Group_2_3_2__3__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__3


    // $ANTLR start rule__ModelGroupItem__Group_2_3_2__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1504:1: rule__ModelGroupItem__Group_2_3_2__3__Impl : ( ']' ) ;
    public final void rule__ModelGroupItem__Group_2_3_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1508:1: ( ( ']' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1509:1: ( ']' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1509:1: ( ']' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1510:1: ']'
            {
             before(grammarAccess.getModelGroupItemAccess().getRightSquareBracketKeyword_2_3_2_3()); 
            match(input,28,FOLLOW_28_in_rule__ModelGroupItem__Group_2_3_2__3__Impl3009); 
             after(grammarAccess.getModelGroupItemAccess().getRightSquareBracketKeyword_2_3_2_3()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_3_2__3__Impl


    // $ANTLR start rule__ModelBinding__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1531:1: rule__ModelBinding__Group__0 : rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 ;
    public final void rule__ModelBinding__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1535:1: ( rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1536:2: rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03048);
            rule__ModelBinding__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03051);
            rule__ModelBinding__Group__1();
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
    // $ANTLR end rule__ModelBinding__Group__0


    // $ANTLR start rule__ModelBinding__Group__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1543:1: rule__ModelBinding__Group__0__Impl : ( ( rule__ModelBinding__TypeAssignment_0 ) ) ;
    public final void rule__ModelBinding__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1547:1: ( ( ( rule__ModelBinding__TypeAssignment_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1548:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1548:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1549:1: ( rule__ModelBinding__TypeAssignment_0 )
            {
             before(grammarAccess.getModelBindingAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1550:1: ( rule__ModelBinding__TypeAssignment_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1550:2: rule__ModelBinding__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3078);
            rule__ModelBinding__TypeAssignment_0();
            _fsp--;


            }

             after(grammarAccess.getModelBindingAccess().getTypeAssignment_0()); 

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
    // $ANTLR end rule__ModelBinding__Group__0__Impl


    // $ANTLR start rule__ModelBinding__Group__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1560:1: rule__ModelBinding__Group__1 : rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 ;
    public final void rule__ModelBinding__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1564:1: ( rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1565:2: rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13108);
            rule__ModelBinding__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13111);
            rule__ModelBinding__Group__2();
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
    // $ANTLR end rule__ModelBinding__Group__1


    // $ANTLR start rule__ModelBinding__Group__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1572:1: rule__ModelBinding__Group__1__Impl : ( '=' ) ;
    public final void rule__ModelBinding__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1576:1: ( ( '=' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1577:1: ( '=' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1577:1: ( '=' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1578:1: '='
            {
             before(grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1()); 
            match(input,29,FOLLOW_29_in_rule__ModelBinding__Group__1__Impl3139); 
             after(grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1()); 

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
    // $ANTLR end rule__ModelBinding__Group__1__Impl


    // $ANTLR start rule__ModelBinding__Group__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1591:1: rule__ModelBinding__Group__2 : rule__ModelBinding__Group__2__Impl ;
    public final void rule__ModelBinding__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1595:1: ( rule__ModelBinding__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1596:2: rule__ModelBinding__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23170);
            rule__ModelBinding__Group__2__Impl();
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
    // $ANTLR end rule__ModelBinding__Group__2


    // $ANTLR start rule__ModelBinding__Group__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1602:1: rule__ModelBinding__Group__2__Impl : ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) ;
    public final void rule__ModelBinding__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1606:1: ( ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1607:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1607:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1608:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1609:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1609:2: rule__ModelBinding__ConfigurationAssignment_2
            {
            pushFollow(FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3197);
            rule__ModelBinding__ConfigurationAssignment_2();
            _fsp--;


            }

             after(grammarAccess.getModelBindingAccess().getConfigurationAssignment_2()); 

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
    // $ANTLR end rule__ModelBinding__Group__2__Impl


    // $ANTLR start rule__ItemModel__ItemsAssignment
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1626:1: rule__ItemModel__ItemsAssignment : ( ruleModelItem ) ;
    public final void rule__ItemModel__ItemsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1630:1: ( ( ruleModelItem ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1631:1: ( ruleModelItem )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1631:1: ( ruleModelItem )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1632:1: ruleModelItem
            {
             before(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3238);
            ruleModelItem();
            _fsp--;

             after(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0()); 

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
    // $ANTLR end rule__ItemModel__ItemsAssignment


    // $ANTLR start rule__ModelItem__NameAssignment_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1641:1: rule__ModelItem__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__ModelItem__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1645:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1646:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1646:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1647:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13269); 
             after(grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0()); 

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
    // $ANTLR end rule__ModelItem__NameAssignment_1


    // $ANTLR start rule__ModelItem__LabelAssignment_2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1656:1: rule__ModelItem__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelItem__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1660:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1661:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1661:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1662:1: RULE_STRING
            {
             before(grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23300); 
             after(grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 

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
    // $ANTLR end rule__ModelItem__LabelAssignment_2


    // $ANTLR start rule__ModelItem__IconAssignment_3_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1671:1: rule__ModelItem__IconAssignment_3_1 : ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) ;
    public final void rule__ModelItem__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1675:1: ( ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1676:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1676:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1677:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getModelItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1678:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1678:2: rule__ModelItem__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13331);
            rule__ModelItem__IconAlternatives_3_1_0();
            _fsp--;


            }

             after(grammarAccess.getModelItemAccess().getIconAlternatives_3_1_0()); 

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
    // $ANTLR end rule__ModelItem__IconAssignment_3_1


    // $ANTLR start rule__ModelItem__GroupsAssignment_4_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1687:1: rule__ModelItem__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1691:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1692:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1692:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1693:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1694:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1695:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13368); 
             after(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0()); 

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
    // $ANTLR end rule__ModelItem__GroupsAssignment_4_1


    // $ANTLR start rule__ModelItem__GroupsAssignment_4_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1706:1: rule__ModelItem__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1710:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1711:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1711:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1712:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1713:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1714:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13407); 
             after(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_2_1_0_1()); 

            }

             after(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0()); 

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
    // $ANTLR end rule__ModelItem__GroupsAssignment_4_2_1


    // $ANTLR start rule__ModelItem__BindingsAssignment_5_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1725:1: rule__ModelItem__BindingsAssignment_5_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1729:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1730:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1730:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1731:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13442);
            ruleModelBinding();
            _fsp--;

             after(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0()); 

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
    // $ANTLR end rule__ModelItem__BindingsAssignment_5_1


    // $ANTLR start rule__ModelItem__BindingsAssignment_5_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1740:1: rule__ModelItem__BindingsAssignment_5_2_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1744:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1745:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1745:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1746:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13473);
            ruleModelBinding();
            _fsp--;

             after(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0()); 

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
    // $ANTLR end rule__ModelItem__BindingsAssignment_5_2_1


    // $ANTLR start rule__ModelGroupItem__TypeAssignment_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1755:1: rule__ModelGroupItem__TypeAssignment_2_1 : ( ruleModelItemType ) ;
    public final void rule__ModelGroupItem__TypeAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1759:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1760:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1760:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1761:1: ruleModelItemType
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13504);
            ruleModelItemType();
            _fsp--;

             after(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0()); 

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
    // $ANTLR end rule__ModelGroupItem__TypeAssignment_2_1


    // $ANTLR start rule__ModelGroupItem__FunctionAssignment_2_3_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1770:1: rule__ModelGroupItem__FunctionAssignment_2_3_0 : ( ruleModelGroupFunction ) ;
    public final void rule__ModelGroupItem__FunctionAssignment_2_3_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1774:1: ( ( ruleModelGroupFunction ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1775:1: ( ruleModelGroupFunction )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1775:1: ( ruleModelGroupFunction )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1776:1: ruleModelGroupFunction
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_3_0_0()); 
            pushFollow(FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_3_03535);
            ruleModelGroupFunction();
            _fsp--;

             after(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_3_0_0()); 

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
    // $ANTLR end rule__ModelGroupItem__FunctionAssignment_2_3_0


    // $ANTLR start rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1785:1: rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0 : ( ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 ) ) ;
    public final void rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1789:1: ( ( ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1790:1: ( ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1790:1: ( ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1791:1: ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getActiveStateAlternatives_2_3_2_0_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1792:1: ( rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1792:2: rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0_in_rule__ModelGroupItem__ActiveStateAssignment_2_3_2_03566);
            rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getActiveStateAlternatives_2_3_2_0_0()); 

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
    // $ANTLR end rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0


    // $ANTLR start rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1801:1: rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2 : ( ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 ) ) ;
    public final void rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1805:1: ( ( ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1806:1: ( ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1806:1: ( ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1807:1: ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getPassiveStateAlternatives_2_3_2_2_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1808:1: ( rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1808:2: rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0_in_rule__ModelGroupItem__PassiveStateAssignment_2_3_2_23599);
            rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getPassiveStateAlternatives_2_3_2_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2


    // $ANTLR start rule__ModelNormalItem__TypeAssignment
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1817:1: rule__ModelNormalItem__TypeAssignment : ( ruleModelItemType ) ;
    public final void rule__ModelNormalItem__TypeAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1821:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1822:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1822:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1823:1: ruleModelItemType
            {
             before(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3632);
            ruleModelItemType();
            _fsp--;

             after(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0()); 

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
    // $ANTLR end rule__ModelNormalItem__TypeAssignment


    // $ANTLR start rule__ModelBinding__TypeAssignment_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1832:1: rule__ModelBinding__TypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__ModelBinding__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1836:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1837:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1837:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1838:1: RULE_ID
            {
             before(grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03663); 
             after(grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0()); 

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
    // $ANTLR end rule__ModelBinding__TypeAssignment_0


    // $ANTLR start rule__ModelBinding__ConfigurationAssignment_2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1847:1: rule__ModelBinding__ConfigurationAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelBinding__ConfigurationAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1851:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1852:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1852:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1853:1: RULE_STRING
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23694); 
             after(grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 

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
    // $ANTLR end rule__ModelBinding__ConfigurationAssignment_2


 

    public static final BitSet FOLLOW_ruleItemModel_in_entryRuleItemModel61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItemModel68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ItemModel__ItemsAssignment_in_ruleItemModel94 = new BitSet(new long[]{0x000000000401F812L});
    public static final BitSet FOLLOW_ruleModelItem_in_entryRuleModelItem122 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItem129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__0_in_ruleModelItem155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem182 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelGroupItem189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__0_in_ruleModelGroupItem215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem242 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelNormalItem249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelNormalItem__TypeAssignment_in_ruleModelNormalItem275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_entryRuleModelItemType302 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItemType309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItemType__Alternatives_in_ruleModelItemType335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_entryRuleModelBinding362 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelBinding369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__0_in_ruleModelBinding395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupFunction__Alternatives_in_ruleModelGroupFunction432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_rule__ModelItem__Alternatives_0467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_rule__ModelItem__Alternatives_0484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__IconAlternatives_3_1_0516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelItem__IconAlternatives_3_1_0533 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__ModelItemType__Alternatives664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__ModelItemType__Alternatives684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__ModelItemType__Alternatives704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__ModelItemType__Alternatives724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__ModelItemType__Alternatives744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__ModelItemType__Alternatives764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItemType__Alternatives783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__ModelGroupFunction__Alternatives816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__ModelGroupFunction__Alternatives837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0870 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__1930 = new BitSet(new long[]{0x0000000001280022L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__1933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__2990 = new BitSet(new long[]{0x0000000001280002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__2993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31051 = new BitSet(new long[]{0x0000000001200002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41112 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1200 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01243 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ModelItem__Group_3__0__Impl1274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11305 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__ModelItem__Group_3__2__Impl1393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01430 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__ModelItem__Group_4__0__Impl1461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11492 = new BitSet(new long[]{0x0000000000C00000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21552 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1582 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__ModelItem__Group_4__3__Impl1641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01680 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__ModelItem__Group_4_2__0__Impl1711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01803 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__ModelItem__Group_5__0__Impl1834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11865 = new BitSet(new long[]{0x0000000002800000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__21925 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__21928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl1955 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__31986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ModelItem__Group_5__3__Impl2014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02053 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__ModelItem__Group_5_2__0__Impl2084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02176 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelGroupItem__Group__0__Impl2207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12238 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02363 = new BitSet(new long[]{0x000000000001F810L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ModelGroupItem__Group_2__0__Impl2394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12425 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22485 = new BitSet(new long[]{0x0000000000060002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__3_in_rule__ModelGroupItem__Group_2__22488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__ModelGroupItem__Group_2__2__Impl2516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__3__Impl_in_rule__ModelGroupItem__Group_2__32547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__0_in_rule__ModelGroupItem__Group_2__3__Impl2574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__0__Impl_in_rule__ModelGroupItem__Group_2_3__02613 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__1_in_rule__ModelGroupItem__Group_2_3__02616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_3_0_in_rule__ModelGroupItem__Group_2_3__0__Impl2643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__1__Impl_in_rule__ModelGroupItem__Group_2_3__12673 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__2_in_rule__ModelGroupItem__Group_2_3__12676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ModelGroupItem__Group_2_3__1__Impl2704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3__2__Impl_in_rule__ModelGroupItem__Group_2_3__22735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__0_in_rule__ModelGroupItem__Group_2_3__2__Impl2762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__0__Impl_in_rule__ModelGroupItem__Group_2_3_2__02799 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__1_in_rule__ModelGroupItem__Group_2_3_2__02802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ActiveStateAssignment_2_3_2_0_in_rule__ModelGroupItem__Group_2_3_2__0__Impl2829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__1__Impl_in_rule__ModelGroupItem__Group_2_3_2__12859 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__2_in_rule__ModelGroupItem__Group_2_3_2__12862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__ModelGroupItem__Group_2_3_2__1__Impl2890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__2__Impl_in_rule__ModelGroupItem__Group_2_3_2__22921 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__3_in_rule__ModelGroupItem__Group_2_3_2__22924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__PassiveStateAssignment_2_3_2_2_in_rule__ModelGroupItem__Group_2_3_2__2__Impl2951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_3_2__3__Impl_in_rule__ModelGroupItem__Group_2_3_2__32981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__ModelGroupItem__Group_2_3_2__3__Impl3009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03048 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3078 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13108 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__ModelBinding__Group__1__Impl3139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_3_03535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ActiveStateAlternatives_2_3_2_0_0_in_rule__ModelGroupItem__ActiveStateAssignment_2_3_2_03566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__PassiveStateAlternatives_2_3_2_2_0_in_rule__ModelGroupItem__PassiveStateAssignment_2_3_2_23599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23694 = new BitSet(new long[]{0x0000000000000002L});

}