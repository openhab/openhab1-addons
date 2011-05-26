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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'DateTime'", "'AND'", "'OR'", "'AVG'", "'MAX'", "'MIN'", "'<'", "'>'", "'('", "')'", "','", "'{'", "'}'", "'Group'", "':'", "'='"
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

                if ( (LA1_0==RULE_ID||(LA1_0>=11 && LA1_0<=17)||LA1_0==30) ) {
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

            if ( (LA2_0==RULE_ID||(LA2_0>=11 && LA2_0<=17)) ) {
                alt2=1;
            }
            else if ( (LA2_0==30) ) {
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


    // $ANTLR start rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:292:1: rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0() throws RecognitionException {

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
                    new NoViableAltException("292:1: rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:297:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:297:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:298:1: RULE_ID
                    {
                     before(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0565); 
                     after(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:303:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:303:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:304:1: RULE_STRING
                    {
                     before(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0582); 
                     after(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_1_0_1()); 

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
    // $ANTLR end rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0


    // $ANTLR start rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:314:1: rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0() throws RecognitionException {

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
                    new NoViableAltException("314:1: rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:319:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:319:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:320:1: RULE_ID
                    {
                     before(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0614); 
                     after(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:325:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:325:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:326:1: RULE_STRING
                    {
                     before(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0631); 
                     after(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_2_1_0_1()); 

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
    // $ANTLR end rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0


    // $ANTLR start rule__ModelItemType__Alternatives
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:336:1: rule__ModelItemType__Alternatives : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( 'DateTime' ) | ( RULE_ID ) );
    public final void rule__ModelItemType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:340:1: ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( 'DateTime' ) | ( RULE_ID ) )
            int alt6=8;
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
            case 17:
                {
                alt6=7;
                }
                break;
            case RULE_ID:
                {
                alt6=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("336:1: rule__ModelItemType__Alternatives : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( 'DateTime' ) | ( RULE_ID ) );", 6, 0, input);

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
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:389:6: ( 'DateTime' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:389:6: ( 'DateTime' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:390:1: 'DateTime'
                    {
                     before(grammarAccess.getModelItemTypeAccess().getDateTimeKeyword_6()); 
                    match(input,17,FOLLOW_17_in_rule__ModelItemType__Alternatives784); 
                     after(grammarAccess.getModelItemTypeAccess().getDateTimeKeyword_6()); 

                    }


                    }
                    break;
                case 8 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:397:6: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:397:6: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:398:1: RULE_ID
                    {
                     before(grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_7()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItemType__Alternatives803); 
                     after(grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_7()); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:408:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) );
    public final void rule__ModelGroupFunction__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:412:1: ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) )
            int alt7=5;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt7=1;
                }
                break;
            case 19:
                {
                alt7=2;
                }
                break;
            case 20:
                {
                alt7=3;
                }
                break;
            case 21:
                {
                alt7=4;
                }
                break;
            case 22:
                {
                alt7=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("408:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) );", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:413:1: ( ( 'AND' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:413:1: ( ( 'AND' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:414:1: ( 'AND' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:415:1: ( 'AND' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:415:3: 'AND'
                    {
                    match(input,18,FOLLOW_18_in_rule__ModelGroupFunction__Alternatives836); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:420:6: ( ( 'OR' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:420:6: ( ( 'OR' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:421:1: ( 'OR' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:422:1: ( 'OR' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:422:3: 'OR'
                    {
                    match(input,19,FOLLOW_19_in_rule__ModelGroupFunction__Alternatives857); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:427:6: ( ( 'AVG' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:427:6: ( ( 'AVG' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:428:1: ( 'AVG' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:429:1: ( 'AVG' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:429:3: 'AVG'
                    {
                    match(input,20,FOLLOW_20_in_rule__ModelGroupFunction__Alternatives878); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:434:6: ( ( 'MAX' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:434:6: ( ( 'MAX' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:435:1: ( 'MAX' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:436:1: ( 'MAX' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:436:3: 'MAX'
                    {
                    match(input,21,FOLLOW_21_in_rule__ModelGroupFunction__Alternatives899); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:441:6: ( ( 'MIN' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:441:6: ( ( 'MIN' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:442:1: ( 'MIN' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:443:1: ( 'MIN' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:443:3: 'MIN'
                    {
                    match(input,22,FOLLOW_22_in_rule__ModelGroupFunction__Alternatives920); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4()); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:455:1: rule__ModelItem__Group__0 : rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 ;
    public final void rule__ModelItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:459:1: ( rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:460:2: rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0953);
            rule__ModelItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0956);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:467:1: rule__ModelItem__Group__0__Impl : ( ( rule__ModelItem__Alternatives_0 ) ) ;
    public final void rule__ModelItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:471:1: ( ( ( rule__ModelItem__Alternatives_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__ModelItem__Alternatives_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__ModelItem__Alternatives_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:473:1: ( rule__ModelItem__Alternatives_0 )
            {
             before(grammarAccess.getModelItemAccess().getAlternatives_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:1: ( rule__ModelItem__Alternatives_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:2: rule__ModelItem__Alternatives_0
            {
            pushFollow(FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl983);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:484:1: rule__ModelItem__Group__1 : rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 ;
    public final void rule__ModelItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:488:1: ( rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:489:2: rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__11013);
            rule__ModelItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__11016);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:496:1: rule__ModelItem__Group__1__Impl : ( ( rule__ModelItem__NameAssignment_1 ) ) ;
    public final void rule__ModelItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:500:1: ( ( ( rule__ModelItem__NameAssignment_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:501:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:501:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:502:1: ( rule__ModelItem__NameAssignment_1 )
            {
             before(grammarAccess.getModelItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:503:1: ( rule__ModelItem__NameAssignment_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:503:2: rule__ModelItem__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl1043);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:513:1: rule__ModelItem__Group__2 : rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 ;
    public final void rule__ModelItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:517:1: ( rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:518:2: rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__21073);
            rule__ModelItem__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__21076);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:525:1: rule__ModelItem__Group__2__Impl : ( ( rule__ModelItem__LabelAssignment_2 )? ) ;
    public final void rule__ModelItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:529:1: ( ( ( rule__ModelItem__LabelAssignment_2 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:530:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:530:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:531:1: ( rule__ModelItem__LabelAssignment_2 )?
            {
             before(grammarAccess.getModelItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:532:1: ( rule__ModelItem__LabelAssignment_2 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==RULE_STRING) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:532:2: rule__ModelItem__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1103);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:542:1: rule__ModelItem__Group__3 : rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 ;
    public final void rule__ModelItem__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:546:1: ( rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:547:2: rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31134);
            rule__ModelItem__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31137);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:554:1: rule__ModelItem__Group__3__Impl : ( ( rule__ModelItem__Group_3__0 )? ) ;
    public final void rule__ModelItem__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:558:1: ( ( ( rule__ModelItem__Group_3__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:559:1: ( ( rule__ModelItem__Group_3__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:559:1: ( ( rule__ModelItem__Group_3__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:560:1: ( rule__ModelItem__Group_3__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_3()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:561:1: ( rule__ModelItem__Group_3__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==23) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:561:2: rule__ModelItem__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1164);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:571:1: rule__ModelItem__Group__4 : rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 ;
    public final void rule__ModelItem__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:575:1: ( rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:576:2: rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41195);
            rule__ModelItem__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41198);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:583:1: rule__ModelItem__Group__4__Impl : ( ( rule__ModelItem__Group_4__0 )? ) ;
    public final void rule__ModelItem__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:587:1: ( ( ( rule__ModelItem__Group_4__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:588:1: ( ( rule__ModelItem__Group_4__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:588:1: ( ( rule__ModelItem__Group_4__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:589:1: ( rule__ModelItem__Group_4__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_4()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:590:1: ( rule__ModelItem__Group_4__0 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==25) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:590:2: rule__ModelItem__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1225);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:600:1: rule__ModelItem__Group__5 : rule__ModelItem__Group__5__Impl ;
    public final void rule__ModelItem__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:604:1: ( rule__ModelItem__Group__5__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:605:2: rule__ModelItem__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51256);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:611:1: rule__ModelItem__Group__5__Impl : ( ( rule__ModelItem__Group_5__0 )* ) ;
    public final void rule__ModelItem__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:615:1: ( ( ( rule__ModelItem__Group_5__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:616:1: ( ( rule__ModelItem__Group_5__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:616:1: ( ( rule__ModelItem__Group_5__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:617:1: ( rule__ModelItem__Group_5__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:618:1: ( rule__ModelItem__Group_5__0 )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==28) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:618:2: rule__ModelItem__Group_5__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1283);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:640:1: rule__ModelItem__Group_3__0 : rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 ;
    public final void rule__ModelItem__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:644:1: ( rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:645:2: rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01326);
            rule__ModelItem__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01329);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:652:1: rule__ModelItem__Group_3__0__Impl : ( '<' ) ;
    public final void rule__ModelItem__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:656:1: ( ( '<' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:657:1: ( '<' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:657:1: ( '<' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:658:1: '<'
            {
             before(grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,23,FOLLOW_23_in_rule__ModelItem__Group_3__0__Impl1357); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:671:1: rule__ModelItem__Group_3__1 : rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 ;
    public final void rule__ModelItem__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:675:1: ( rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:676:2: rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11388);
            rule__ModelItem__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11391);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:683:1: rule__ModelItem__Group_3__1__Impl : ( ( rule__ModelItem__IconAssignment_3_1 ) ) ;
    public final void rule__ModelItem__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:687:1: ( ( ( rule__ModelItem__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:688:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:688:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:689:1: ( rule__ModelItem__IconAssignment_3_1 )
            {
             before(grammarAccess.getModelItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:690:1: ( rule__ModelItem__IconAssignment_3_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:690:2: rule__ModelItem__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1418);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:700:1: rule__ModelItem__Group_3__2 : rule__ModelItem__Group_3__2__Impl ;
    public final void rule__ModelItem__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:704:1: ( rule__ModelItem__Group_3__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:705:2: rule__ModelItem__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21448);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:711:1: rule__ModelItem__Group_3__2__Impl : ( '>' ) ;
    public final void rule__ModelItem__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:715:1: ( ( '>' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:716:1: ( '>' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:716:1: ( '>' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:717:1: '>'
            {
             before(grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,24,FOLLOW_24_in_rule__ModelItem__Group_3__2__Impl1476); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:736:1: rule__ModelItem__Group_4__0 : rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 ;
    public final void rule__ModelItem__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:740:1: ( rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:741:2: rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01513);
            rule__ModelItem__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01516);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:748:1: rule__ModelItem__Group_4__0__Impl : ( '(' ) ;
    public final void rule__ModelItem__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:752:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:753:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:753:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:754:1: '('
            {
             before(grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,25,FOLLOW_25_in_rule__ModelItem__Group_4__0__Impl1544); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:767:1: rule__ModelItem__Group_4__1 : rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 ;
    public final void rule__ModelItem__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:771:1: ( rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:772:2: rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11575);
            rule__ModelItem__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11578);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:779:1: rule__ModelItem__Group_4__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) ;
    public final void rule__ModelItem__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:783:1: ( ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:784:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:784:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:785:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:786:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:786:2: rule__ModelItem__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1605);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:796:1: rule__ModelItem__Group_4__2 : rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 ;
    public final void rule__ModelItem__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:800:1: ( rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:801:2: rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21635);
            rule__ModelItem__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21638);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:808:1: rule__ModelItem__Group_4__2__Impl : ( ( rule__ModelItem__Group_4_2__0 )* ) ;
    public final void rule__ModelItem__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:812:1: ( ( ( rule__ModelItem__Group_4_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:813:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:813:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:814:1: ( rule__ModelItem__Group_4_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:815:1: ( rule__ModelItem__Group_4_2__0 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==27) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:815:2: rule__ModelItem__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1665);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:825:1: rule__ModelItem__Group_4__3 : rule__ModelItem__Group_4__3__Impl ;
    public final void rule__ModelItem__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:829:1: ( rule__ModelItem__Group_4__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:830:2: rule__ModelItem__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31696);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:836:1: rule__ModelItem__Group_4__3__Impl : ( ')' ) ;
    public final void rule__ModelItem__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:840:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:841:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:841:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:842:1: ')'
            {
             before(grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,26,FOLLOW_26_in_rule__ModelItem__Group_4__3__Impl1724); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:863:1: rule__ModelItem__Group_4_2__0 : rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 ;
    public final void rule__ModelItem__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:867:1: ( rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:868:2: rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01763);
            rule__ModelItem__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01766);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:875:1: rule__ModelItem__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:879:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:880:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:880:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:881:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0()); 
            match(input,27,FOLLOW_27_in_rule__ModelItem__Group_4_2__0__Impl1794); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:894:1: rule__ModelItem__Group_4_2__1 : rule__ModelItem__Group_4_2__1__Impl ;
    public final void rule__ModelItem__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:898:1: ( rule__ModelItem__Group_4_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:899:2: rule__ModelItem__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11825);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:905:1: rule__ModelItem__Group_4_2__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__ModelItem__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:909:1: ( ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:910:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:910:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:911:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:912:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:912:2: rule__ModelItem__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1852);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:926:1: rule__ModelItem__Group_5__0 : rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 ;
    public final void rule__ModelItem__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:930:1: ( rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:931:2: rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01886);
            rule__ModelItem__Group_5__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01889);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:938:1: rule__ModelItem__Group_5__0__Impl : ( '{' ) ;
    public final void rule__ModelItem__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:942:1: ( ( '{' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:943:1: ( '{' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:943:1: ( '{' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:944:1: '{'
            {
             before(grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0()); 
            match(input,28,FOLLOW_28_in_rule__ModelItem__Group_5__0__Impl1917); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:957:1: rule__ModelItem__Group_5__1 : rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 ;
    public final void rule__ModelItem__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:961:1: ( rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:962:2: rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11948);
            rule__ModelItem__Group_5__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11951);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:969:1: rule__ModelItem__Group_5__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) ;
    public final void rule__ModelItem__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:973:1: ( ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:974:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:974:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:975:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:976:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:976:2: rule__ModelItem__BindingsAssignment_5_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1978);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:986:1: rule__ModelItem__Group_5__2 : rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 ;
    public final void rule__ModelItem__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:990:1: ( rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:991:2: rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__22008);
            rule__ModelItem__Group_5__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__22011);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:998:1: rule__ModelItem__Group_5__2__Impl : ( ( rule__ModelItem__Group_5_2__0 )* ) ;
    public final void rule__ModelItem__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1002:1: ( ( ( rule__ModelItem__Group_5_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1003:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1003:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1004:1: ( rule__ModelItem__Group_5_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1005:1: ( rule__ModelItem__Group_5_2__0 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==27) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1005:2: rule__ModelItem__Group_5_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl2038);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1015:1: rule__ModelItem__Group_5__3 : rule__ModelItem__Group_5__3__Impl ;
    public final void rule__ModelItem__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1019:1: ( rule__ModelItem__Group_5__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1020:2: rule__ModelItem__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__32069);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1026:1: rule__ModelItem__Group_5__3__Impl : ( '}' ) ;
    public final void rule__ModelItem__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1030:1: ( ( '}' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1031:1: ( '}' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1031:1: ( '}' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1032:1: '}'
            {
             before(grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,29,FOLLOW_29_in_rule__ModelItem__Group_5__3__Impl2097); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1053:1: rule__ModelItem__Group_5_2__0 : rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 ;
    public final void rule__ModelItem__Group_5_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1057:1: ( rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1058:2: rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02136);
            rule__ModelItem__Group_5_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02139);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1065:1: rule__ModelItem__Group_5_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_5_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1069:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1070:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1070:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1071:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0()); 
            match(input,27,FOLLOW_27_in_rule__ModelItem__Group_5_2__0__Impl2167); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1084:1: rule__ModelItem__Group_5_2__1 : rule__ModelItem__Group_5_2__1__Impl ;
    public final void rule__ModelItem__Group_5_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1088:1: ( rule__ModelItem__Group_5_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1089:2: rule__ModelItem__Group_5_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12198);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1095:1: rule__ModelItem__Group_5_2__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) ;
    public final void rule__ModelItem__Group_5_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1099:1: ( ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1100:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1100:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1101:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1102:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1102:2: rule__ModelItem__BindingsAssignment_5_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2225);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1116:1: rule__ModelGroupItem__Group__0 : rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 ;
    public final void rule__ModelGroupItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1120:1: ( rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1121:2: rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02259);
            rule__ModelGroupItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02262);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1128:1: rule__ModelGroupItem__Group__0__Impl : ( 'Group' ) ;
    public final void rule__ModelGroupItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1132:1: ( ( 'Group' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1133:1: ( 'Group' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1133:1: ( 'Group' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1134:1: 'Group'
            {
             before(grammarAccess.getModelGroupItemAccess().getGroupKeyword_0()); 
            match(input,30,FOLLOW_30_in_rule__ModelGroupItem__Group__0__Impl2290); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1147:1: rule__ModelGroupItem__Group__1 : rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 ;
    public final void rule__ModelGroupItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1151:1: ( rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1152:2: rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12321);
            rule__ModelGroupItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12324);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1159:1: rule__ModelGroupItem__Group__1__Impl : ( () ) ;
    public final void rule__ModelGroupItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1163:1: ( ( () ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1164:1: ( () )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1164:1: ( () )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1165:1: ()
            {
             before(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1166:1: ()
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1168:1: 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1178:1: rule__ModelGroupItem__Group__2 : rule__ModelGroupItem__Group__2__Impl ;
    public final void rule__ModelGroupItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1182:1: ( rule__ModelGroupItem__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1183:2: rule__ModelGroupItem__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22382);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1189:1: rule__ModelGroupItem__Group__2__Impl : ( ( rule__ModelGroupItem__Group_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1193:1: ( ( ( rule__ModelGroupItem__Group_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1194:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1194:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1195:1: ( rule__ModelGroupItem__Group_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1196:1: ( rule__ModelGroupItem__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==31) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1196:2: rule__ModelGroupItem__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2409);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1212:1: rule__ModelGroupItem__Group_2__0 : rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 ;
    public final void rule__ModelGroupItem__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1216:1: ( rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1217:2: rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02446);
            rule__ModelGroupItem__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02449);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1224:1: rule__ModelGroupItem__Group_2__0__Impl : ( ':' ) ;
    public final void rule__ModelGroupItem__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1228:1: ( ( ':' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1229:1: ( ':' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1229:1: ( ':' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1230:1: ':'
            {
             before(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_0()); 
            match(input,31,FOLLOW_31_in_rule__ModelGroupItem__Group_2__0__Impl2477); 
             after(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_0()); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1243:1: rule__ModelGroupItem__Group_2__1 : rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 ;
    public final void rule__ModelGroupItem__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1247:1: ( rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1248:2: rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12508);
            rule__ModelGroupItem__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12511);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1255:1: rule__ModelGroupItem__Group_2__1__Impl : ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1259:1: ( ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1260:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1260:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1261:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeAssignment_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1262:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1262:2: rule__ModelGroupItem__TypeAssignment_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2538);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1272:1: rule__ModelGroupItem__Group_2__2 : rule__ModelGroupItem__Group_2__2__Impl ;
    public final void rule__ModelGroupItem__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1276:1: ( rule__ModelGroupItem__Group_2__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1277:2: rule__ModelGroupItem__Group_2__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22568);
            rule__ModelGroupItem__Group_2__2__Impl();
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1283:1: rule__ModelGroupItem__Group_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1287:1: ( ( ( rule__ModelGroupItem__Group_2_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1288:1: ( ( rule__ModelGroupItem__Group_2_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1288:1: ( ( rule__ModelGroupItem__Group_2_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1289:1: ( rule__ModelGroupItem__Group_2_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1290:1: ( rule__ModelGroupItem__Group_2_2__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==31) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1290:2: rule__ModelGroupItem__Group_2_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__0_in_rule__ModelGroupItem__Group_2__2__Impl2595);
                    rule__ModelGroupItem__Group_2_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelGroupItemAccess().getGroup_2_2()); 

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


    // $ANTLR start rule__ModelGroupItem__Group_2_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1306:1: rule__ModelGroupItem__Group_2_2__0 : rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1310:1: ( rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1311:2: rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2__02632);
            rule__ModelGroupItem__Group_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__1_in_rule__ModelGroupItem__Group_2_2__02635);
            rule__ModelGroupItem__Group_2_2__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__0


    // $ANTLR start rule__ModelGroupItem__Group_2_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1318:1: rule__ModelGroupItem__Group_2_2__0__Impl : ( ':' ) ;
    public final void rule__ModelGroupItem__Group_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1322:1: ( ( ':' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1323:1: ( ':' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1323:1: ( ':' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1324:1: ':'
            {
             before(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_2_0()); 
            match(input,31,FOLLOW_31_in_rule__ModelGroupItem__Group_2_2__0__Impl2663); 
             after(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1337:1: rule__ModelGroupItem__Group_2_2__1 : rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2 ;
    public final void rule__ModelGroupItem__Group_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1341:1: ( rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1342:2: rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2__12694);
            rule__ModelGroupItem__Group_2_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__2_in_rule__ModelGroupItem__Group_2_2__12697);
            rule__ModelGroupItem__Group_2_2__2();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__1


    // $ANTLR start rule__ModelGroupItem__Group_2_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1349:1: rule__ModelGroupItem__Group_2_2__1__Impl : ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1353:1: ( ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1354:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1354:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1355:1: ( rule__ModelGroupItem__FunctionAssignment_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionAssignment_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1356:1: ( rule__ModelGroupItem__FunctionAssignment_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1356:2: rule__ModelGroupItem__FunctionAssignment_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_2_1_in_rule__ModelGroupItem__Group_2_2__1__Impl2724);
            rule__ModelGroupItem__FunctionAssignment_2_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getFunctionAssignment_2_2_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1366:1: rule__ModelGroupItem__Group_2_2__2 : rule__ModelGroupItem__Group_2_2__2__Impl ;
    public final void rule__ModelGroupItem__Group_2_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1370:1: ( rule__ModelGroupItem__Group_2_2__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1371:2: rule__ModelGroupItem__Group_2_2__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2__22754);
            rule__ModelGroupItem__Group_2_2__2__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__2


    // $ANTLR start rule__ModelGroupItem__Group_2_2__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1377:1: rule__ModelGroupItem__Group_2_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1381:1: ( ( ( rule__ModelGroupItem__Group_2_2_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1382:1: ( ( rule__ModelGroupItem__Group_2_2_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1382:1: ( ( rule__ModelGroupItem__Group_2_2_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1383:1: ( rule__ModelGroupItem__Group_2_2_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1384:1: ( rule__ModelGroupItem__Group_2_2_2__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==25) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1384:2: rule__ModelGroupItem__Group_2_2_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__0_in_rule__ModelGroupItem__Group_2_2__2__Impl2781);
                    rule__ModelGroupItem__Group_2_2_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1400:1: rule__ModelGroupItem__Group_2_2_2__0 : rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1404:1: ( rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1405:2: rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2__02818);
            rule__ModelGroupItem__Group_2_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2__02821);
            rule__ModelGroupItem__Group_2_2_2__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__0


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1412:1: rule__ModelGroupItem__Group_2_2_2__0__Impl : ( '(' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1416:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1417:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1417:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1418:1: '('
            {
             before(grammarAccess.getModelGroupItemAccess().getLeftParenthesisKeyword_2_2_2_0()); 
            match(input,25,FOLLOW_25_in_rule__ModelGroupItem__Group_2_2_2__0__Impl2849); 
             after(grammarAccess.getModelGroupItemAccess().getLeftParenthesisKeyword_2_2_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1431:1: rule__ModelGroupItem__Group_2_2_2__1 : rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2 ;
    public final void rule__ModelGroupItem__Group_2_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1435:1: ( rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1436:2: rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2__12880);
            rule__ModelGroupItem__Group_2_2_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__2_in_rule__ModelGroupItem__Group_2_2_2__12883);
            rule__ModelGroupItem__Group_2_2_2__2();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__1


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1443:1: rule__ModelGroupItem__Group_2_2_2__1__Impl : ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1447:1: ( ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1448:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1448:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1449:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1450:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1450:2: rule__ModelGroupItem__ArgsAssignment_2_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2__1__Impl2910);
            rule__ModelGroupItem__ArgsAssignment_2_2_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__1__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1460:1: rule__ModelGroupItem__Group_2_2_2__2 : rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3 ;
    public final void rule__ModelGroupItem__Group_2_2_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1464:1: ( rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1465:2: rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2_2__22940);
            rule__ModelGroupItem__Group_2_2_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__3_in_rule__ModelGroupItem__Group_2_2_2__22943);
            rule__ModelGroupItem__Group_2_2_2__3();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__2


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1472:1: rule__ModelGroupItem__Group_2_2_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1476:1: ( ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1477:1: ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1477:1: ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1478:1: ( rule__ModelGroupItem__Group_2_2_2_2__0 )*
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1479:1: ( rule__ModelGroupItem__Group_2_2_2_2__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==27) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1479:2: rule__ModelGroupItem__Group_2_2_2_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0_in_rule__ModelGroupItem__Group_2_2_2__2__Impl2970);
            	    rule__ModelGroupItem__Group_2_2_2_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

             after(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2_2()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__2__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1489:1: rule__ModelGroupItem__Group_2_2_2__3 : rule__ModelGroupItem__Group_2_2_2__3__Impl ;
    public final void rule__ModelGroupItem__Group_2_2_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1493:1: ( rule__ModelGroupItem__Group_2_2_2__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1494:2: rule__ModelGroupItem__Group_2_2_2__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__3__Impl_in_rule__ModelGroupItem__Group_2_2_2__33001);
            rule__ModelGroupItem__Group_2_2_2__3__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__3


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1500:1: rule__ModelGroupItem__Group_2_2_2__3__Impl : ( ')' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1504:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1505:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1505:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1506:1: ')'
            {
             before(grammarAccess.getModelGroupItemAccess().getRightParenthesisKeyword_2_2_2_3()); 
            match(input,26,FOLLOW_26_in_rule__ModelGroupItem__Group_2_2_2__3__Impl3029); 
             after(grammarAccess.getModelGroupItemAccess().getRightParenthesisKeyword_2_2_2_3()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2__3__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1527:1: rule__ModelGroupItem__Group_2_2_2_2__0 : rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1531:1: ( rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1532:2: rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__03068);
            rule__ModelGroupItem__Group_2_2_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2_2__03071);
            rule__ModelGroupItem__Group_2_2_2_2__1();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2_2__0


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1539:1: rule__ModelGroupItem__Group_2_2_2_2__0__Impl : ( ',' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1543:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1544:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1544:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1545:1: ','
            {
             before(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_2_2_2_0()); 
            match(input,27,FOLLOW_27_in_rule__ModelGroupItem__Group_2_2_2_2__0__Impl3099); 
             after(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_2_2_2_0()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2_2__0__Impl


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1558:1: rule__ModelGroupItem__Group_2_2_2_2__1 : rule__ModelGroupItem__Group_2_2_2_2__1__Impl ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1562:1: ( rule__ModelGroupItem__Group_2_2_2_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1563:2: rule__ModelGroupItem__Group_2_2_2_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__13130);
            rule__ModelGroupItem__Group_2_2_2_2__1__Impl();
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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2_2__1


    // $ANTLR start rule__ModelGroupItem__Group_2_2_2_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1569:1: rule__ModelGroupItem__Group_2_2_2_2__1__Impl : ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1573:1: ( ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1574:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1574:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1575:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1576:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1576:2: rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2_2__1__Impl3157);
            rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_2_1()); 

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
    // $ANTLR end rule__ModelGroupItem__Group_2_2_2_2__1__Impl


    // $ANTLR start rule__ModelBinding__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1590:1: rule__ModelBinding__Group__0 : rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 ;
    public final void rule__ModelBinding__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1594:1: ( rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1595:2: rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03191);
            rule__ModelBinding__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03194);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1602:1: rule__ModelBinding__Group__0__Impl : ( ( rule__ModelBinding__TypeAssignment_0 ) ) ;
    public final void rule__ModelBinding__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1606:1: ( ( ( rule__ModelBinding__TypeAssignment_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1607:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1607:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1608:1: ( rule__ModelBinding__TypeAssignment_0 )
            {
             before(grammarAccess.getModelBindingAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1609:1: ( rule__ModelBinding__TypeAssignment_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1609:2: rule__ModelBinding__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3221);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1619:1: rule__ModelBinding__Group__1 : rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 ;
    public final void rule__ModelBinding__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1623:1: ( rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1624:2: rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13251);
            rule__ModelBinding__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13254);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1631:1: rule__ModelBinding__Group__1__Impl : ( '=' ) ;
    public final void rule__ModelBinding__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1635:1: ( ( '=' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1636:1: ( '=' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1636:1: ( '=' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1637:1: '='
            {
             before(grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1()); 
            match(input,32,FOLLOW_32_in_rule__ModelBinding__Group__1__Impl3282); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1650:1: rule__ModelBinding__Group__2 : rule__ModelBinding__Group__2__Impl ;
    public final void rule__ModelBinding__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1654:1: ( rule__ModelBinding__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1655:2: rule__ModelBinding__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23313);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1661:1: rule__ModelBinding__Group__2__Impl : ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) ;
    public final void rule__ModelBinding__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1665:1: ( ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1666:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1666:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1667:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1668:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1668:2: rule__ModelBinding__ConfigurationAssignment_2
            {
            pushFollow(FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3340);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1685:1: rule__ItemModel__ItemsAssignment : ( ruleModelItem ) ;
    public final void rule__ItemModel__ItemsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1689:1: ( ( ruleModelItem ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1690:1: ( ruleModelItem )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1690:1: ( ruleModelItem )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1691:1: ruleModelItem
            {
             before(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3381);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1700:1: rule__ModelItem__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__ModelItem__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1704:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1705:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1705:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1706:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13412); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1715:1: rule__ModelItem__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelItem__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1719:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1720:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1720:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1721:1: RULE_STRING
            {
             before(grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23443); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1730:1: rule__ModelItem__IconAssignment_3_1 : ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) ;
    public final void rule__ModelItem__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1734:1: ( ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1735:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1735:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1736:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getModelItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1737:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1737:2: rule__ModelItem__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13474);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1746:1: rule__ModelItem__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1750:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1751:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1751:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1752:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1753:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1754:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13511); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1765:1: rule__ModelItem__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1769:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1770:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1770:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1771:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1772:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1773:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13550); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1784:1: rule__ModelItem__BindingsAssignment_5_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1788:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1789:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1789:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1790:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13585);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1799:1: rule__ModelItem__BindingsAssignment_5_2_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1803:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1804:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1804:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1805:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13616);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1814:1: rule__ModelGroupItem__TypeAssignment_2_1 : ( ruleModelItemType ) ;
    public final void rule__ModelGroupItem__TypeAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1818:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1819:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1819:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1820:1: ruleModelItemType
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13647);
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


    // $ANTLR start rule__ModelGroupItem__FunctionAssignment_2_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1829:1: rule__ModelGroupItem__FunctionAssignment_2_2_1 : ( ruleModelGroupFunction ) ;
    public final void rule__ModelGroupItem__FunctionAssignment_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1833:1: ( ( ruleModelGroupFunction ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1834:1: ( ruleModelGroupFunction )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1834:1: ( ruleModelGroupFunction )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1835:1: ruleModelGroupFunction
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_2_1_0()); 
            pushFollow(FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_2_13678);
            ruleModelGroupFunction();
            _fsp--;

             after(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_2_1_0()); 

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
    // $ANTLR end rule__ModelGroupItem__FunctionAssignment_2_2_1


    // $ANTLR start rule__ModelGroupItem__ArgsAssignment_2_2_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1844:1: rule__ModelGroupItem__ArgsAssignment_2_2_2_1 : ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) ) ;
    public final void rule__ModelGroupItem__ArgsAssignment_2_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1848:1: ( ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1849:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1849:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1850:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1851:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1851:2: rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_13709);
            rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_1_0()); 

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
    // $ANTLR end rule__ModelGroupItem__ArgsAssignment_2_2_2_1


    // $ANTLR start rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1860:1: rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 : ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) ) ;
    public final void rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1864:1: ( ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1865:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1865:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1866:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1867:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1867:2: rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_13742);
            rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0();
            _fsp--;


            }

             after(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_2_1_0()); 

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
    // $ANTLR end rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1


    // $ANTLR start rule__ModelNormalItem__TypeAssignment
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1876:1: rule__ModelNormalItem__TypeAssignment : ( ruleModelItemType ) ;
    public final void rule__ModelNormalItem__TypeAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1880:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1881:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1881:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1882:1: ruleModelItemType
            {
             before(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3775);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1891:1: rule__ModelBinding__TypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__ModelBinding__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1895:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1896:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1896:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1897:1: RULE_ID
            {
             before(grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03806); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1906:1: rule__ModelBinding__ConfigurationAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelBinding__ConfigurationAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1910:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1911:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1911:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1912:1: RULE_STRING
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23837); 
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
    public static final BitSet FOLLOW_rule__ItemModel__ItemsAssignment_in_ruleItemModel94 = new BitSet(new long[]{0x000000004003F812L});
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
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__ModelItemType__Alternatives664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__ModelItemType__Alternatives684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__ModelItemType__Alternatives704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__ModelItemType__Alternatives724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__ModelItemType__Alternatives744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__ModelItemType__Alternatives764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__ModelItemType__Alternatives784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItemType__Alternatives803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__ModelGroupFunction__Alternatives836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ModelGroupFunction__Alternatives857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__ModelGroupFunction__Alternatives878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__ModelGroupFunction__Alternatives899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__ModelGroupFunction__Alternatives920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0953 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__11013 = new BitSet(new long[]{0x0000000012800022L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__11016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl1043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__21073 = new BitSet(new long[]{0x0000000012800002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__21076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31134 = new BitSet(new long[]{0x0000000012000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41195 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1283 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01326 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__ModelItem__Group_3__0__Impl1357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11388 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__ModelItem__Group_3__2__Impl1476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01513 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ModelItem__Group_4__0__Impl1544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11575 = new BitSet(new long[]{0x000000000C000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21635 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1665 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelItem__Group_4__3__Impl1724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01763 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ModelItem__Group_4_2__0__Impl1794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01886 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__ModelItem__Group_5__0__Impl1917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11948 = new BitSet(new long[]{0x0000000028000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__22008 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__22011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl2038 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__32069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__ModelItem__Group_5__3__Impl2097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02136 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ModelItem__Group_5_2__0__Impl2167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02259 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__ModelGroupItem__Group__0__Impl2290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12321 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02446 = new BitSet(new long[]{0x000000000003F810L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__ModelGroupItem__Group_2__0__Impl2477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12508 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__0_in_rule__ModelGroupItem__Group_2__2__Impl2595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2__02632 = new BitSet(new long[]{0x00000000007C0000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__1_in_rule__ModelGroupItem__Group_2_2__02635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__ModelGroupItem__Group_2_2__0__Impl2663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2__12694 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__2_in_rule__ModelGroupItem__Group_2_2__12697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_2_1_in_rule__ModelGroupItem__Group_2_2__1__Impl2724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2__22754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__0_in_rule__ModelGroupItem__Group_2_2__2__Impl2781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2__02818 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2__02821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ModelGroupItem__Group_2_2_2__0__Impl2849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2__12880 = new BitSet(new long[]{0x000000000C000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__2_in_rule__ModelGroupItem__Group_2_2_2__12883 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2__1__Impl2910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2_2__22940 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__3_in_rule__ModelGroupItem__Group_2_2_2__22943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0_in_rule__ModelGroupItem__Group_2_2_2__2__Impl2970 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__3__Impl_in_rule__ModelGroupItem__Group_2_2_2__33001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelGroupItem__Group_2_2_2__3__Impl3029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__03068 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2_2__03071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ModelGroupItem__Group_2_2_2_2__0__Impl3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__13130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2_2__1__Impl3157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03191 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13251 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_rule__ModelBinding__Group__1__Impl3282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_2_13678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_13709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_13742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23837 = new BitSet(new long[]{0x0000000000000002L});

}