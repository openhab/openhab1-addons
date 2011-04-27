/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'AND'", "'OR'", "'AVG'", "'MAX'", "'MIN'", "'<'", "'>'", "'('", "')'", "','", "'{'", "'}'", "'Group'", "':'", "'='"
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

                if ( (LA1_0==RULE_ID||(LA1_0>=11 && LA1_0<=16)||LA1_0==29) ) {
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
            else if ( (LA2_0==29) ) {
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:400:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) );
    public final void rule__ModelGroupFunction__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:404:1: ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) )
            int alt7=5;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt7=1;
                }
                break;
            case 18:
                {
                alt7=2;
                }
                break;
            case 19:
                {
                alt7=3;
                }
                break;
            case 20:
                {
                alt7=4;
                }
                break;
            case 21:
                {
                alt7=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("400:1: rule__ModelGroupFunction__Alternatives : ( ( ( 'AND' ) ) | ( ( 'OR' ) ) | ( ( 'AVG' ) ) | ( ( 'MAX' ) ) | ( ( 'MIN' ) ) );", 7, 0, input);

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
                case 3 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:419:6: ( ( 'AVG' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:419:6: ( ( 'AVG' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:420:1: ( 'AVG' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:421:1: ( 'AVG' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:421:3: 'AVG'
                    {
                    match(input,19,FOLLOW_19_in_rule__ModelGroupFunction__Alternatives858); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:426:6: ( ( 'MAX' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:426:6: ( ( 'MAX' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:427:1: ( 'MAX' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:428:1: ( 'MAX' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:428:3: 'MAX'
                    {
                    match(input,20,FOLLOW_20_in_rule__ModelGroupFunction__Alternatives879); 

                    }

                     after(grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:433:6: ( ( 'MIN' ) )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:433:6: ( ( 'MIN' ) )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:434:1: ( 'MIN' )
                    {
                     before(grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4()); 
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:435:1: ( 'MIN' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:435:3: 'MIN'
                    {
                    match(input,21,FOLLOW_21_in_rule__ModelGroupFunction__Alternatives900); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:447:1: rule__ModelItem__Group__0 : rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 ;
    public final void rule__ModelItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:451:1: ( rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:452:2: rule__ModelItem__Group__0__Impl rule__ModelItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0933);
            rule__ModelItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0936);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:459:1: rule__ModelItem__Group__0__Impl : ( ( rule__ModelItem__Alternatives_0 ) ) ;
    public final void rule__ModelItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:463:1: ( ( ( rule__ModelItem__Alternatives_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:464:1: ( ( rule__ModelItem__Alternatives_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:464:1: ( ( rule__ModelItem__Alternatives_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:465:1: ( rule__ModelItem__Alternatives_0 )
            {
             before(grammarAccess.getModelItemAccess().getAlternatives_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:466:1: ( rule__ModelItem__Alternatives_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:466:2: rule__ModelItem__Alternatives_0
            {
            pushFollow(FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl963);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:476:1: rule__ModelItem__Group__1 : rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 ;
    public final void rule__ModelItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:480:1: ( rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:481:2: rule__ModelItem__Group__1__Impl rule__ModelItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__1993);
            rule__ModelItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__1996);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:488:1: rule__ModelItem__Group__1__Impl : ( ( rule__ModelItem__NameAssignment_1 ) ) ;
    public final void rule__ModelItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:492:1: ( ( ( rule__ModelItem__NameAssignment_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:493:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:493:1: ( ( rule__ModelItem__NameAssignment_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:494:1: ( rule__ModelItem__NameAssignment_1 )
            {
             before(grammarAccess.getModelItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:495:1: ( rule__ModelItem__NameAssignment_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:495:2: rule__ModelItem__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl1023);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:505:1: rule__ModelItem__Group__2 : rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 ;
    public final void rule__ModelItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:509:1: ( rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:510:2: rule__ModelItem__Group__2__Impl rule__ModelItem__Group__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__21053);
            rule__ModelItem__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__21056);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:517:1: rule__ModelItem__Group__2__Impl : ( ( rule__ModelItem__LabelAssignment_2 )? ) ;
    public final void rule__ModelItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:521:1: ( ( ( rule__ModelItem__LabelAssignment_2 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:522:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:522:1: ( ( rule__ModelItem__LabelAssignment_2 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:523:1: ( rule__ModelItem__LabelAssignment_2 )?
            {
             before(grammarAccess.getModelItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:524:1: ( rule__ModelItem__LabelAssignment_2 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==RULE_STRING) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:524:2: rule__ModelItem__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1083);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:534:1: rule__ModelItem__Group__3 : rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 ;
    public final void rule__ModelItem__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:538:1: ( rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:539:2: rule__ModelItem__Group__3__Impl rule__ModelItem__Group__4
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31114);
            rule__ModelItem__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31117);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:546:1: rule__ModelItem__Group__3__Impl : ( ( rule__ModelItem__Group_3__0 )? ) ;
    public final void rule__ModelItem__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:550:1: ( ( ( rule__ModelItem__Group_3__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:551:1: ( ( rule__ModelItem__Group_3__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:551:1: ( ( rule__ModelItem__Group_3__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:552:1: ( rule__ModelItem__Group_3__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_3()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:553:1: ( rule__ModelItem__Group_3__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==22) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:553:2: rule__ModelItem__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1144);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:563:1: rule__ModelItem__Group__4 : rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 ;
    public final void rule__ModelItem__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:567:1: ( rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:568:2: rule__ModelItem__Group__4__Impl rule__ModelItem__Group__5
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41175);
            rule__ModelItem__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41178);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:575:1: rule__ModelItem__Group__4__Impl : ( ( rule__ModelItem__Group_4__0 )? ) ;
    public final void rule__ModelItem__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:579:1: ( ( ( rule__ModelItem__Group_4__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:580:1: ( ( rule__ModelItem__Group_4__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:580:1: ( ( rule__ModelItem__Group_4__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:581:1: ( rule__ModelItem__Group_4__0 )?
            {
             before(grammarAccess.getModelItemAccess().getGroup_4()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:582:1: ( rule__ModelItem__Group_4__0 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==24) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:582:2: rule__ModelItem__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1205);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:592:1: rule__ModelItem__Group__5 : rule__ModelItem__Group__5__Impl ;
    public final void rule__ModelItem__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:596:1: ( rule__ModelItem__Group__5__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:597:2: rule__ModelItem__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51236);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:603:1: rule__ModelItem__Group__5__Impl : ( ( rule__ModelItem__Group_5__0 )* ) ;
    public final void rule__ModelItem__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:607:1: ( ( ( rule__ModelItem__Group_5__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:608:1: ( ( rule__ModelItem__Group_5__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:608:1: ( ( rule__ModelItem__Group_5__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:609:1: ( rule__ModelItem__Group_5__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:610:1: ( rule__ModelItem__Group_5__0 )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==27) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:610:2: rule__ModelItem__Group_5__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1263);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:632:1: rule__ModelItem__Group_3__0 : rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 ;
    public final void rule__ModelItem__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:636:1: ( rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:637:2: rule__ModelItem__Group_3__0__Impl rule__ModelItem__Group_3__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01306);
            rule__ModelItem__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01309);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:644:1: rule__ModelItem__Group_3__0__Impl : ( '<' ) ;
    public final void rule__ModelItem__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:648:1: ( ( '<' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:649:1: ( '<' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:649:1: ( '<' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:650:1: '<'
            {
             before(grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,22,FOLLOW_22_in_rule__ModelItem__Group_3__0__Impl1337); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:663:1: rule__ModelItem__Group_3__1 : rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 ;
    public final void rule__ModelItem__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:667:1: ( rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:668:2: rule__ModelItem__Group_3__1__Impl rule__ModelItem__Group_3__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11368);
            rule__ModelItem__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11371);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:675:1: rule__ModelItem__Group_3__1__Impl : ( ( rule__ModelItem__IconAssignment_3_1 ) ) ;
    public final void rule__ModelItem__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:679:1: ( ( ( rule__ModelItem__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:680:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:680:1: ( ( rule__ModelItem__IconAssignment_3_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:681:1: ( rule__ModelItem__IconAssignment_3_1 )
            {
             before(grammarAccess.getModelItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:682:1: ( rule__ModelItem__IconAssignment_3_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:682:2: rule__ModelItem__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1398);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:692:1: rule__ModelItem__Group_3__2 : rule__ModelItem__Group_3__2__Impl ;
    public final void rule__ModelItem__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:696:1: ( rule__ModelItem__Group_3__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:697:2: rule__ModelItem__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21428);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:703:1: rule__ModelItem__Group_3__2__Impl : ( '>' ) ;
    public final void rule__ModelItem__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:707:1: ( ( '>' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:708:1: ( '>' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:708:1: ( '>' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:709:1: '>'
            {
             before(grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,23,FOLLOW_23_in_rule__ModelItem__Group_3__2__Impl1456); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:728:1: rule__ModelItem__Group_4__0 : rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 ;
    public final void rule__ModelItem__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:732:1: ( rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:733:2: rule__ModelItem__Group_4__0__Impl rule__ModelItem__Group_4__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01493);
            rule__ModelItem__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01496);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:740:1: rule__ModelItem__Group_4__0__Impl : ( '(' ) ;
    public final void rule__ModelItem__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:744:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:745:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:745:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:746:1: '('
            {
             before(grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,24,FOLLOW_24_in_rule__ModelItem__Group_4__0__Impl1524); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:759:1: rule__ModelItem__Group_4__1 : rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 ;
    public final void rule__ModelItem__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:763:1: ( rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:764:2: rule__ModelItem__Group_4__1__Impl rule__ModelItem__Group_4__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11555);
            rule__ModelItem__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11558);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:771:1: rule__ModelItem__Group_4__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) ;
    public final void rule__ModelItem__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:775:1: ( ( ( rule__ModelItem__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:776:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:776:1: ( ( rule__ModelItem__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:777:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:778:1: ( rule__ModelItem__GroupsAssignment_4_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:778:2: rule__ModelItem__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1585);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:788:1: rule__ModelItem__Group_4__2 : rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 ;
    public final void rule__ModelItem__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:792:1: ( rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:793:2: rule__ModelItem__Group_4__2__Impl rule__ModelItem__Group_4__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21615);
            rule__ModelItem__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21618);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:800:1: rule__ModelItem__Group_4__2__Impl : ( ( rule__ModelItem__Group_4_2__0 )* ) ;
    public final void rule__ModelItem__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:804:1: ( ( ( rule__ModelItem__Group_4_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:805:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:805:1: ( ( rule__ModelItem__Group_4_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:806:1: ( rule__ModelItem__Group_4_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:807:1: ( rule__ModelItem__Group_4_2__0 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==26) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:807:2: rule__ModelItem__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1645);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:817:1: rule__ModelItem__Group_4__3 : rule__ModelItem__Group_4__3__Impl ;
    public final void rule__ModelItem__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:821:1: ( rule__ModelItem__Group_4__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:822:2: rule__ModelItem__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31676);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:828:1: rule__ModelItem__Group_4__3__Impl : ( ')' ) ;
    public final void rule__ModelItem__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:832:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:833:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:833:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:834:1: ')'
            {
             before(grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,25,FOLLOW_25_in_rule__ModelItem__Group_4__3__Impl1704); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:855:1: rule__ModelItem__Group_4_2__0 : rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 ;
    public final void rule__ModelItem__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:859:1: ( rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:860:2: rule__ModelItem__Group_4_2__0__Impl rule__ModelItem__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01743);
            rule__ModelItem__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01746);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:867:1: rule__ModelItem__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:871:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:872:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:872:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:873:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0()); 
            match(input,26,FOLLOW_26_in_rule__ModelItem__Group_4_2__0__Impl1774); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:886:1: rule__ModelItem__Group_4_2__1 : rule__ModelItem__Group_4_2__1__Impl ;
    public final void rule__ModelItem__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:890:1: ( rule__ModelItem__Group_4_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:891:2: rule__ModelItem__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11805);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:897:1: rule__ModelItem__Group_4_2__1__Impl : ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__ModelItem__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:901:1: ( ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:902:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:902:1: ( ( rule__ModelItem__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:903:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:904:1: ( rule__ModelItem__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:904:2: rule__ModelItem__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1832);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:918:1: rule__ModelItem__Group_5__0 : rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 ;
    public final void rule__ModelItem__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:922:1: ( rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:923:2: rule__ModelItem__Group_5__0__Impl rule__ModelItem__Group_5__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01866);
            rule__ModelItem__Group_5__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01869);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:930:1: rule__ModelItem__Group_5__0__Impl : ( '{' ) ;
    public final void rule__ModelItem__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:934:1: ( ( '{' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:935:1: ( '{' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:935:1: ( '{' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:936:1: '{'
            {
             before(grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0()); 
            match(input,27,FOLLOW_27_in_rule__ModelItem__Group_5__0__Impl1897); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:949:1: rule__ModelItem__Group_5__1 : rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 ;
    public final void rule__ModelItem__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:953:1: ( rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:954:2: rule__ModelItem__Group_5__1__Impl rule__ModelItem__Group_5__2
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11928);
            rule__ModelItem__Group_5__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11931);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:961:1: rule__ModelItem__Group_5__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) ;
    public final void rule__ModelItem__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:965:1: ( ( ( rule__ModelItem__BindingsAssignment_5_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:966:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:966:1: ( ( rule__ModelItem__BindingsAssignment_5_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:967:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:968:1: ( rule__ModelItem__BindingsAssignment_5_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:968:2: rule__ModelItem__BindingsAssignment_5_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1958);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:978:1: rule__ModelItem__Group_5__2 : rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 ;
    public final void rule__ModelItem__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:982:1: ( rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:983:2: rule__ModelItem__Group_5__2__Impl rule__ModelItem__Group_5__3
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__21988);
            rule__ModelItem__Group_5__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__21991);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:990:1: rule__ModelItem__Group_5__2__Impl : ( ( rule__ModelItem__Group_5_2__0 )* ) ;
    public final void rule__ModelItem__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:994:1: ( ( ( rule__ModelItem__Group_5_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:995:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:995:1: ( ( rule__ModelItem__Group_5_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:996:1: ( rule__ModelItem__Group_5_2__0 )*
            {
             before(grammarAccess.getModelItemAccess().getGroup_5_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:997:1: ( rule__ModelItem__Group_5_2__0 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==26) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:997:2: rule__ModelItem__Group_5_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl2018);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1007:1: rule__ModelItem__Group_5__3 : rule__ModelItem__Group_5__3__Impl ;
    public final void rule__ModelItem__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1011:1: ( rule__ModelItem__Group_5__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1012:2: rule__ModelItem__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__32049);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1018:1: rule__ModelItem__Group_5__3__Impl : ( '}' ) ;
    public final void rule__ModelItem__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1022:1: ( ( '}' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1023:1: ( '}' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1023:1: ( '}' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1024:1: '}'
            {
             before(grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,28,FOLLOW_28_in_rule__ModelItem__Group_5__3__Impl2077); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1045:1: rule__ModelItem__Group_5_2__0 : rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 ;
    public final void rule__ModelItem__Group_5_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1049:1: ( rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1050:2: rule__ModelItem__Group_5_2__0__Impl rule__ModelItem__Group_5_2__1
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02116);
            rule__ModelItem__Group_5_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02119);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1057:1: rule__ModelItem__Group_5_2__0__Impl : ( ',' ) ;
    public final void rule__ModelItem__Group_5_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1061:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1062:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1062:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1063:1: ','
            {
             before(grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0()); 
            match(input,26,FOLLOW_26_in_rule__ModelItem__Group_5_2__0__Impl2147); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1076:1: rule__ModelItem__Group_5_2__1 : rule__ModelItem__Group_5_2__1__Impl ;
    public final void rule__ModelItem__Group_5_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1080:1: ( rule__ModelItem__Group_5_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1081:2: rule__ModelItem__Group_5_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12178);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1087:1: rule__ModelItem__Group_5_2__1__Impl : ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) ;
    public final void rule__ModelItem__Group_5_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1091:1: ( ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1092:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1092:1: ( ( rule__ModelItem__BindingsAssignment_5_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1093:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            {
             before(grammarAccess.getModelItemAccess().getBindingsAssignment_5_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1094:1: ( rule__ModelItem__BindingsAssignment_5_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1094:2: rule__ModelItem__BindingsAssignment_5_2_1
            {
            pushFollow(FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2205);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1108:1: rule__ModelGroupItem__Group__0 : rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 ;
    public final void rule__ModelGroupItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1112:1: ( rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1113:2: rule__ModelGroupItem__Group__0__Impl rule__ModelGroupItem__Group__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02239);
            rule__ModelGroupItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02242);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1120:1: rule__ModelGroupItem__Group__0__Impl : ( 'Group' ) ;
    public final void rule__ModelGroupItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1124:1: ( ( 'Group' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1125:1: ( 'Group' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1125:1: ( 'Group' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1126:1: 'Group'
            {
             before(grammarAccess.getModelGroupItemAccess().getGroupKeyword_0()); 
            match(input,29,FOLLOW_29_in_rule__ModelGroupItem__Group__0__Impl2270); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1139:1: rule__ModelGroupItem__Group__1 : rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 ;
    public final void rule__ModelGroupItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1143:1: ( rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1144:2: rule__ModelGroupItem__Group__1__Impl rule__ModelGroupItem__Group__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12301);
            rule__ModelGroupItem__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12304);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1151:1: rule__ModelGroupItem__Group__1__Impl : ( () ) ;
    public final void rule__ModelGroupItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1155:1: ( ( () ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1156:1: ( () )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1156:1: ( () )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1157:1: ()
            {
             before(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1158:1: ()
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1160:1: 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1170:1: rule__ModelGroupItem__Group__2 : rule__ModelGroupItem__Group__2__Impl ;
    public final void rule__ModelGroupItem__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1174:1: ( rule__ModelGroupItem__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1175:2: rule__ModelGroupItem__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22362);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1181:1: rule__ModelGroupItem__Group__2__Impl : ( ( rule__ModelGroupItem__Group_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1185:1: ( ( ( rule__ModelGroupItem__Group_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1186:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1186:1: ( ( rule__ModelGroupItem__Group_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1187:1: ( rule__ModelGroupItem__Group_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1188:1: ( rule__ModelGroupItem__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==30) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1188:2: rule__ModelGroupItem__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2389);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1204:1: rule__ModelGroupItem__Group_2__0 : rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 ;
    public final void rule__ModelGroupItem__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1208:1: ( rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1209:2: rule__ModelGroupItem__Group_2__0__Impl rule__ModelGroupItem__Group_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02426);
            rule__ModelGroupItem__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02429);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1216:1: rule__ModelGroupItem__Group_2__0__Impl : ( ':' ) ;
    public final void rule__ModelGroupItem__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1220:1: ( ( ':' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1221:1: ( ':' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1221:1: ( ':' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1222:1: ':'
            {
             before(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_0()); 
            match(input,30,FOLLOW_30_in_rule__ModelGroupItem__Group_2__0__Impl2457); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1235:1: rule__ModelGroupItem__Group_2__1 : rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 ;
    public final void rule__ModelGroupItem__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1239:1: ( rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1240:2: rule__ModelGroupItem__Group_2__1__Impl rule__ModelGroupItem__Group_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12488);
            rule__ModelGroupItem__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12491);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1247:1: rule__ModelGroupItem__Group_2__1__Impl : ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1251:1: ( ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1252:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1252:1: ( ( rule__ModelGroupItem__TypeAssignment_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1253:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeAssignment_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1254:1: ( rule__ModelGroupItem__TypeAssignment_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1254:2: rule__ModelGroupItem__TypeAssignment_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2518);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1264:1: rule__ModelGroupItem__Group_2__2 : rule__ModelGroupItem__Group_2__2__Impl ;
    public final void rule__ModelGroupItem__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1268:1: ( rule__ModelGroupItem__Group_2__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1269:2: rule__ModelGroupItem__Group_2__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22548);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1275:1: rule__ModelGroupItem__Group_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1279:1: ( ( ( rule__ModelGroupItem__Group_2_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1280:1: ( ( rule__ModelGroupItem__Group_2_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1280:1: ( ( rule__ModelGroupItem__Group_2_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1281:1: ( rule__ModelGroupItem__Group_2_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1282:1: ( rule__ModelGroupItem__Group_2_2__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==30) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1282:2: rule__ModelGroupItem__Group_2_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__0_in_rule__ModelGroupItem__Group_2__2__Impl2575);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1298:1: rule__ModelGroupItem__Group_2_2__0 : rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1302:1: ( rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1303:2: rule__ModelGroupItem__Group_2_2__0__Impl rule__ModelGroupItem__Group_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2__02612);
            rule__ModelGroupItem__Group_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__1_in_rule__ModelGroupItem__Group_2_2__02615);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1310:1: rule__ModelGroupItem__Group_2_2__0__Impl : ( ':' ) ;
    public final void rule__ModelGroupItem__Group_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1314:1: ( ( ':' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1315:1: ( ':' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1315:1: ( ':' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1316:1: ':'
            {
             before(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_2_0()); 
            match(input,30,FOLLOW_30_in_rule__ModelGroupItem__Group_2_2__0__Impl2643); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1329:1: rule__ModelGroupItem__Group_2_2__1 : rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2 ;
    public final void rule__ModelGroupItem__Group_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1333:1: ( rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1334:2: rule__ModelGroupItem__Group_2_2__1__Impl rule__ModelGroupItem__Group_2_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2__12674);
            rule__ModelGroupItem__Group_2_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__2_in_rule__ModelGroupItem__Group_2_2__12677);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1341:1: rule__ModelGroupItem__Group_2_2__1__Impl : ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1345:1: ( ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1346:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1346:1: ( ( rule__ModelGroupItem__FunctionAssignment_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1347:1: ( rule__ModelGroupItem__FunctionAssignment_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionAssignment_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1348:1: ( rule__ModelGroupItem__FunctionAssignment_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1348:2: rule__ModelGroupItem__FunctionAssignment_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_2_1_in_rule__ModelGroupItem__Group_2_2__1__Impl2704);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1358:1: rule__ModelGroupItem__Group_2_2__2 : rule__ModelGroupItem__Group_2_2__2__Impl ;
    public final void rule__ModelGroupItem__Group_2_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1362:1: ( rule__ModelGroupItem__Group_2_2__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1363:2: rule__ModelGroupItem__Group_2_2__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2__22734);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1369:1: rule__ModelGroupItem__Group_2_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2_2__0 )? ) ;
    public final void rule__ModelGroupItem__Group_2_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1373:1: ( ( ( rule__ModelGroupItem__Group_2_2_2__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1374:1: ( ( rule__ModelGroupItem__Group_2_2_2__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1374:1: ( ( rule__ModelGroupItem__Group_2_2_2__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1375:1: ( rule__ModelGroupItem__Group_2_2_2__0 )?
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1376:1: ( rule__ModelGroupItem__Group_2_2_2__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==24) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1376:2: rule__ModelGroupItem__Group_2_2_2__0
                    {
                    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__0_in_rule__ModelGroupItem__Group_2_2__2__Impl2761);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1392:1: rule__ModelGroupItem__Group_2_2_2__0 : rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1396:1: ( rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1397:2: rule__ModelGroupItem__Group_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2__02798);
            rule__ModelGroupItem__Group_2_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2__02801);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1404:1: rule__ModelGroupItem__Group_2_2_2__0__Impl : ( '(' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1408:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1409:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1409:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1410:1: '('
            {
             before(grammarAccess.getModelGroupItemAccess().getLeftParenthesisKeyword_2_2_2_0()); 
            match(input,24,FOLLOW_24_in_rule__ModelGroupItem__Group_2_2_2__0__Impl2829); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1423:1: rule__ModelGroupItem__Group_2_2_2__1 : rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2 ;
    public final void rule__ModelGroupItem__Group_2_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1427:1: ( rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1428:2: rule__ModelGroupItem__Group_2_2_2__1__Impl rule__ModelGroupItem__Group_2_2_2__2
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2__12860);
            rule__ModelGroupItem__Group_2_2_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__2_in_rule__ModelGroupItem__Group_2_2_2__12863);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1435:1: rule__ModelGroupItem__Group_2_2_2__1__Impl : ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1439:1: ( ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1440:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1440:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1441:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1442:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1442:2: rule__ModelGroupItem__ArgsAssignment_2_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2__1__Impl2890);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1452:1: rule__ModelGroupItem__Group_2_2_2__2 : rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3 ;
    public final void rule__ModelGroupItem__Group_2_2_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1456:1: ( rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1457:2: rule__ModelGroupItem__Group_2_2_2__2__Impl rule__ModelGroupItem__Group_2_2_2__3
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2_2__22920);
            rule__ModelGroupItem__Group_2_2_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__3_in_rule__ModelGroupItem__Group_2_2_2__22923);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1464:1: rule__ModelGroupItem__Group_2_2_2__2__Impl : ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1468:1: ( ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1469:1: ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1469:1: ( ( rule__ModelGroupItem__Group_2_2_2_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1470:1: ( rule__ModelGroupItem__Group_2_2_2_2__0 )*
            {
             before(grammarAccess.getModelGroupItemAccess().getGroup_2_2_2_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1471:1: ( rule__ModelGroupItem__Group_2_2_2_2__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==26) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1471:2: rule__ModelGroupItem__Group_2_2_2_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0_in_rule__ModelGroupItem__Group_2_2_2__2__Impl2950);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1481:1: rule__ModelGroupItem__Group_2_2_2__3 : rule__ModelGroupItem__Group_2_2_2__3__Impl ;
    public final void rule__ModelGroupItem__Group_2_2_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1485:1: ( rule__ModelGroupItem__Group_2_2_2__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1486:2: rule__ModelGroupItem__Group_2_2_2__3__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2__3__Impl_in_rule__ModelGroupItem__Group_2_2_2__32981);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1492:1: rule__ModelGroupItem__Group_2_2_2__3__Impl : ( ')' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1496:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1497:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1497:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1498:1: ')'
            {
             before(grammarAccess.getModelGroupItemAccess().getRightParenthesisKeyword_2_2_2_3()); 
            match(input,25,FOLLOW_25_in_rule__ModelGroupItem__Group_2_2_2__3__Impl3009); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1519:1: rule__ModelGroupItem__Group_2_2_2_2__0 : rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1 ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1523:1: ( rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1524:2: rule__ModelGroupItem__Group_2_2_2_2__0__Impl rule__ModelGroupItem__Group_2_2_2_2__1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__03048);
            rule__ModelGroupItem__Group_2_2_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2_2__03051);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1531:1: rule__ModelGroupItem__Group_2_2_2_2__0__Impl : ( ',' ) ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1535:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1536:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1536:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1537:1: ','
            {
             before(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_2_2_2_0()); 
            match(input,26,FOLLOW_26_in_rule__ModelGroupItem__Group_2_2_2_2__0__Impl3079); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1550:1: rule__ModelGroupItem__Group_2_2_2_2__1 : rule__ModelGroupItem__Group_2_2_2_2__1__Impl ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1554:1: ( rule__ModelGroupItem__Group_2_2_2_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1555:2: rule__ModelGroupItem__Group_2_2_2_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__13110);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1561:1: rule__ModelGroupItem__Group_2_2_2_2__1__Impl : ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) ) ;
    public final void rule__ModelGroupItem__Group_2_2_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1565:1: ( ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1566:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1566:1: ( ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1567:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAssignment_2_2_2_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1568:1: ( rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1568:2: rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2_2__1__Impl3137);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1582:1: rule__ModelBinding__Group__0 : rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 ;
    public final void rule__ModelBinding__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1586:1: ( rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1587:2: rule__ModelBinding__Group__0__Impl rule__ModelBinding__Group__1
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03171);
            rule__ModelBinding__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03174);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1594:1: rule__ModelBinding__Group__0__Impl : ( ( rule__ModelBinding__TypeAssignment_0 ) ) ;
    public final void rule__ModelBinding__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1598:1: ( ( ( rule__ModelBinding__TypeAssignment_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1599:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1599:1: ( ( rule__ModelBinding__TypeAssignment_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1600:1: ( rule__ModelBinding__TypeAssignment_0 )
            {
             before(grammarAccess.getModelBindingAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1601:1: ( rule__ModelBinding__TypeAssignment_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1601:2: rule__ModelBinding__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3201);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1611:1: rule__ModelBinding__Group__1 : rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 ;
    public final void rule__ModelBinding__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1615:1: ( rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1616:2: rule__ModelBinding__Group__1__Impl rule__ModelBinding__Group__2
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13231);
            rule__ModelBinding__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13234);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1623:1: rule__ModelBinding__Group__1__Impl : ( '=' ) ;
    public final void rule__ModelBinding__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1627:1: ( ( '=' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1628:1: ( '=' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1628:1: ( '=' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1629:1: '='
            {
             before(grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1()); 
            match(input,31,FOLLOW_31_in_rule__ModelBinding__Group__1__Impl3262); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1642:1: rule__ModelBinding__Group__2 : rule__ModelBinding__Group__2__Impl ;
    public final void rule__ModelBinding__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1646:1: ( rule__ModelBinding__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1647:2: rule__ModelBinding__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23293);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1653:1: rule__ModelBinding__Group__2__Impl : ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) ;
    public final void rule__ModelBinding__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1657:1: ( ( ( rule__ModelBinding__ConfigurationAssignment_2 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1658:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1658:1: ( ( rule__ModelBinding__ConfigurationAssignment_2 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1659:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1660:1: ( rule__ModelBinding__ConfigurationAssignment_2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1660:2: rule__ModelBinding__ConfigurationAssignment_2
            {
            pushFollow(FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3320);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1677:1: rule__ItemModel__ItemsAssignment : ( ruleModelItem ) ;
    public final void rule__ItemModel__ItemsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1681:1: ( ( ruleModelItem ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1682:1: ( ruleModelItem )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1682:1: ( ruleModelItem )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1683:1: ruleModelItem
            {
             before(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3361);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1692:1: rule__ModelItem__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__ModelItem__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1696:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1697:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1697:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1698:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13392); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1707:1: rule__ModelItem__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelItem__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1711:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1712:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1712:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1713:1: RULE_STRING
            {
             before(grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23423); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1722:1: rule__ModelItem__IconAssignment_3_1 : ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) ;
    public final void rule__ModelItem__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1726:1: ( ( ( rule__ModelItem__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1727:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1727:1: ( ( rule__ModelItem__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1728:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getModelItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1729:1: ( rule__ModelItem__IconAlternatives_3_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1729:2: rule__ModelItem__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13454);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1738:1: rule__ModelItem__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1742:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1743:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1743:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1744:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1745:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1746:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13491); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1757:1: rule__ModelItem__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__ModelItem__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1761:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1762:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1762:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1763:1: ( RULE_ID )
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1764:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1765:1: RULE_ID
            {
             before(grammarAccess.getModelItemAccess().getGroupsModelGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13530); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1776:1: rule__ModelItem__BindingsAssignment_5_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1780:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1781:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1781:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1782:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13565);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1791:1: rule__ModelItem__BindingsAssignment_5_2_1 : ( ruleModelBinding ) ;
    public final void rule__ModelItem__BindingsAssignment_5_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1795:1: ( ( ruleModelBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1796:1: ( ruleModelBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1796:1: ( ruleModelBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1797:1: ruleModelBinding
            {
             before(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0()); 
            pushFollow(FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13596);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1806:1: rule__ModelGroupItem__TypeAssignment_2_1 : ( ruleModelItemType ) ;
    public final void rule__ModelGroupItem__TypeAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1810:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1811:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1811:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1812:1: ruleModelItemType
            {
             before(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13627);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1821:1: rule__ModelGroupItem__FunctionAssignment_2_2_1 : ( ruleModelGroupFunction ) ;
    public final void rule__ModelGroupItem__FunctionAssignment_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1825:1: ( ( ruleModelGroupFunction ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1826:1: ( ruleModelGroupFunction )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1826:1: ( ruleModelGroupFunction )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1827:1: ruleModelGroupFunction
            {
             before(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_2_1_0()); 
            pushFollow(FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_2_13658);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1836:1: rule__ModelGroupItem__ArgsAssignment_2_2_2_1 : ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) ) ;
    public final void rule__ModelGroupItem__ArgsAssignment_2_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1840:1: ( ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1841:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1841:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1842:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1843:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1843:2: rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_13689);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1852:1: rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1 : ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) ) ;
    public final void rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1856:1: ( ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1857:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1857:1: ( ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1858:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 )
            {
             before(grammarAccess.getModelGroupItemAccess().getArgsAlternatives_2_2_2_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1859:1: ( rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1859:2: rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0
            {
            pushFollow(FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_13722);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1868:1: rule__ModelNormalItem__TypeAssignment : ( ruleModelItemType ) ;
    public final void rule__ModelNormalItem__TypeAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1872:1: ( ( ruleModelItemType ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1873:1: ( ruleModelItemType )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1873:1: ( ruleModelItemType )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1874:1: ruleModelItemType
            {
             before(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3755);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1883:1: rule__ModelBinding__TypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__ModelBinding__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1887:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1888:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1888:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1889:1: RULE_ID
            {
             before(grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03786); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1898:1: rule__ModelBinding__ConfigurationAssignment_2 : ( RULE_STRING ) ;
    public final void rule__ModelBinding__ConfigurationAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1902:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1903:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1903:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1904:1: RULE_STRING
            {
             before(grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23817); 
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
    public static final BitSet FOLLOW_rule__ItemModel__ItemsAssignment_in_ruleItemModel94 = new BitSet(new long[]{0x000000002001F812L});
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
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItemType__Alternatives783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__ModelGroupFunction__Alternatives816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__ModelGroupFunction__Alternatives837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ModelGroupFunction__Alternatives858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__ModelGroupFunction__Alternatives879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__ModelGroupFunction__Alternatives900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__0__Impl_in_rule__ModelItem__Group__0933 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1_in_rule__ModelItem__Group__0936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Alternatives_0_in_rule__ModelItem__Group__0__Impl963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__1__Impl_in_rule__ModelItem__Group__1993 = new BitSet(new long[]{0x0000000009400022L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2_in_rule__ModelItem__Group__1996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__NameAssignment_1_in_rule__ModelItem__Group__1__Impl1023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__2__Impl_in_rule__ModelItem__Group__21053 = new BitSet(new long[]{0x0000000009400002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3_in_rule__ModelItem__Group__21056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__LabelAssignment_2_in_rule__ModelItem__Group__2__Impl1083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__3__Impl_in_rule__ModelItem__Group__31114 = new BitSet(new long[]{0x0000000009000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4_in_rule__ModelItem__Group__31117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0_in_rule__ModelItem__Group__3__Impl1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__4__Impl_in_rule__ModelItem__Group__41175 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5_in_rule__ModelItem__Group__41178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0_in_rule__ModelItem__Group__4__Impl1205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group__5__Impl_in_rule__ModelItem__Group__51236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0_in_rule__ModelItem__Group__5__Impl1263 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__0__Impl_in_rule__ModelItem__Group_3__01306 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1_in_rule__ModelItem__Group_3__01309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__ModelItem__Group_3__0__Impl1337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__1__Impl_in_rule__ModelItem__Group_3__11368 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2_in_rule__ModelItem__Group_3__11371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAssignment_3_1_in_rule__ModelItem__Group_3__1__Impl1398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_3__2__Impl_in_rule__ModelItem__Group_3__21428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__ModelItem__Group_3__2__Impl1456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__0__Impl_in_rule__ModelItem__Group_4__01493 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1_in_rule__ModelItem__Group_4__01496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__ModelItem__Group_4__0__Impl1524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__1__Impl_in_rule__ModelItem__Group_4__11555 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2_in_rule__ModelItem__Group_4__11558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_1_in_rule__ModelItem__Group_4__1__Impl1585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__2__Impl_in_rule__ModelItem__Group_4__21615 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3_in_rule__ModelItem__Group_4__21618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0_in_rule__ModelItem__Group_4__2__Impl1645 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4__3__Impl_in_rule__ModelItem__Group_4__31676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ModelItem__Group_4__3__Impl1704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__0__Impl_in_rule__ModelItem__Group_4_2__01743 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1_in_rule__ModelItem__Group_4_2__01746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelItem__Group_4_2__0__Impl1774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_4_2__1__Impl_in_rule__ModelItem__Group_4_2__11805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__GroupsAssignment_4_2_1_in_rule__ModelItem__Group_4_2__1__Impl1832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__0__Impl_in_rule__ModelItem__Group_5__01866 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1_in_rule__ModelItem__Group_5__01869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ModelItem__Group_5__0__Impl1897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__1__Impl_in_rule__ModelItem__Group_5__11928 = new BitSet(new long[]{0x0000000014000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2_in_rule__ModelItem__Group_5__11931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_1_in_rule__ModelItem__Group_5__1__Impl1958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__2__Impl_in_rule__ModelItem__Group_5__21988 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3_in_rule__ModelItem__Group_5__21991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0_in_rule__ModelItem__Group_5__2__Impl2018 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5__3__Impl_in_rule__ModelItem__Group_5__32049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__ModelItem__Group_5__3__Impl2077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__0__Impl_in_rule__ModelItem__Group_5_2__02116 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1_in_rule__ModelItem__Group_5_2__02119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelItem__Group_5_2__0__Impl2147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__Group_5_2__1__Impl_in_rule__ModelItem__Group_5_2__12178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__BindingsAssignment_5_2_1_in_rule__ModelItem__Group_5_2__1__Impl2205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__0__Impl_in_rule__ModelGroupItem__Group__02239 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1_in_rule__ModelGroupItem__Group__02242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__ModelGroupItem__Group__0__Impl2270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__1__Impl_in_rule__ModelGroupItem__Group__12301 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2_in_rule__ModelGroupItem__Group__12304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group__2__Impl_in_rule__ModelGroupItem__Group__22362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0_in_rule__ModelGroupItem__Group__2__Impl2389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__0__Impl_in_rule__ModelGroupItem__Group_2__02426 = new BitSet(new long[]{0x000000000001F810L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1_in_rule__ModelGroupItem__Group_2__02429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__ModelGroupItem__Group_2__0__Impl2457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__1__Impl_in_rule__ModelGroupItem__Group_2__12488 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2_in_rule__ModelGroupItem__Group_2__12491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__TypeAssignment_2_1_in_rule__ModelGroupItem__Group_2__1__Impl2518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2__2__Impl_in_rule__ModelGroupItem__Group_2__22548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__0_in_rule__ModelGroupItem__Group_2__2__Impl2575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2__02612 = new BitSet(new long[]{0x00000000003E0000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__1_in_rule__ModelGroupItem__Group_2_2__02615 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__ModelGroupItem__Group_2_2__0__Impl2643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2__12674 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__2_in_rule__ModelGroupItem__Group_2_2__12677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__FunctionAssignment_2_2_1_in_rule__ModelGroupItem__Group_2_2__1__Impl2704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2__22734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__0_in_rule__ModelGroupItem__Group_2_2__2__Impl2761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2__02798 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2__02801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__ModelGroupItem__Group_2_2_2__0__Impl2829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2__12860 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__2_in_rule__ModelGroupItem__Group_2_2_2__12863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2__1__Impl2890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__2__Impl_in_rule__ModelGroupItem__Group_2_2_2__22920 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__3_in_rule__ModelGroupItem__Group_2_2_2__22923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0_in_rule__ModelGroupItem__Group_2_2_2__2__Impl2950 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2__3__Impl_in_rule__ModelGroupItem__Group_2_2_2__32981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ModelGroupItem__Group_2_2_2__3__Impl3009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__0__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__03048 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1_in_rule__ModelGroupItem__Group_2_2_2_2__03051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ModelGroupItem__Group_2_2_2_2__0__Impl3079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__Group_2_2_2_2__1__Impl_in_rule__ModelGroupItem__Group_2_2_2_2__13110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_1_in_rule__ModelGroupItem__Group_2_2_2_2__1__Impl3137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__0__Impl_in_rule__ModelBinding__Group__03171 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1_in_rule__ModelBinding__Group__03174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__TypeAssignment_0_in_rule__ModelBinding__Group__0__Impl3201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__1__Impl_in_rule__ModelBinding__Group__13231 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2_in_rule__ModelBinding__Group__13234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__ModelBinding__Group__1__Impl3262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__Group__2__Impl_in_rule__ModelBinding__Group__23293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelBinding__ConfigurationAssignment_2_in_rule__ModelBinding__Group__2__Impl3320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItem_in_rule__ItemModel__ItemsAssignment3361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__NameAssignment_13392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelItem__LabelAssignment_23423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelItem__IconAlternatives_3_1_0_in_rule__ModelItem__IconAssignment_3_13454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_13491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelItem__GroupsAssignment_4_2_13530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_13565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_rule__ModelItem__BindingsAssignment_5_2_13596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelGroupItem__TypeAssignment_2_13627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelGroupFunction_in_rule__ModelGroupItem__FunctionAssignment_2_2_13658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_13689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ModelGroupItem__ArgsAlternatives_2_2_2_2_1_0_in_rule__ModelGroupItem__ArgsAssignment_2_2_2_2_13722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_rule__ModelNormalItem__TypeAssignment3755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ModelBinding__TypeAssignment_03786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__ModelBinding__ConfigurationAssignment_23817 = new BitSet(new long[]{0x0000000000000002L});

}