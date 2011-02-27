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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'<'", "'>'", "'('", "')'", "','", "'{'", "'}'", "'Group'", "'='"
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

                if ( (LA1_0==RULE_ID||(LA1_0>=11 && LA1_0<=16)||LA1_0==24) ) {
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


    // $ANTLR start entryRuleItem
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:89:1: entryRuleItem : ruleItem EOF ;
    public final void entryRuleItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:90:1: ( ruleItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:91:1: ruleItem EOF
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:98:1: ruleItem : ( ( rule__Item__Group__0 ) ) ;
    public final void ruleItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:102:2: ( ( ( rule__Item__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:103:1: ( ( rule__Item__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:103:1: ( ( rule__Item__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:104:1: ( rule__Item__Group__0 )
            {
             before(grammarAccess.getItemAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:105:1: ( rule__Item__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:105:2: rule__Item__Group__0
            {
            pushFollow(FOLLOW_rule__Item__Group__0_in_ruleItem155);
            rule__Item__Group__0();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getGroup()); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:117:1: entryRuleGroupItem : ruleGroupItem EOF ;
    public final void entryRuleGroupItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:118:1: ( ruleGroupItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:119:1: ruleGroupItem EOF
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:126:1: ruleGroupItem : ( ( rule__GroupItem__Group__0 ) ) ;
    public final void ruleGroupItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:130:2: ( ( ( rule__GroupItem__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:131:1: ( ( rule__GroupItem__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:131:1: ( ( rule__GroupItem__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:132:1: ( rule__GroupItem__Group__0 )
            {
             before(grammarAccess.getGroupItemAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:133:1: ( rule__GroupItem__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:133:2: rule__GroupItem__Group__0
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:145:1: entryRuleNormalItem : ruleNormalItem EOF ;
    public final void entryRuleNormalItem() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:146:1: ( ruleNormalItem EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:147:1: ruleNormalItem EOF
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:154:1: ruleNormalItem : ( ( rule__NormalItem__TypeAssignment ) ) ;
    public final void ruleNormalItem() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:158:2: ( ( ( rule__NormalItem__TypeAssignment ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:159:1: ( ( rule__NormalItem__TypeAssignment ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:159:1: ( ( rule__NormalItem__TypeAssignment ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:160:1: ( rule__NormalItem__TypeAssignment )
            {
             before(grammarAccess.getNormalItemAccess().getTypeAssignment()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:161:1: ( rule__NormalItem__TypeAssignment )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:161:2: rule__NormalItem__TypeAssignment
            {
            pushFollow(FOLLOW_rule__NormalItem__TypeAssignment_in_ruleNormalItem275);
            rule__NormalItem__TypeAssignment();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getTypeAssignment()); 

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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:173:1: entryRuleBinding : ruleBinding EOF ;
    public final void entryRuleBinding() throws RecognitionException {
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:174:1: ( ruleBinding EOF )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:175:1: ruleBinding EOF
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:182:1: ruleBinding : ( ( rule__Binding__Group__0 ) ) ;
    public final void ruleBinding() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:186:2: ( ( ( rule__Binding__Group__0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:187:1: ( ( rule__Binding__Group__0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:187:1: ( ( rule__Binding__Group__0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:188:1: ( rule__Binding__Group__0 )
            {
             before(grammarAccess.getBindingAccess().getGroup()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:189:1: ( rule__Binding__Group__0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:189:2: rule__Binding__Group__0
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


    // $ANTLR start rule__Item__Alternatives_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:201:1: rule__Item__Alternatives_0 : ( ( ruleNormalItem ) | ( ruleGroupItem ) );
    public final void rule__Item__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:205:1: ( ( ruleNormalItem ) | ( ruleGroupItem ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=11 && LA2_0<=16)) ) {
                alt2=1;
            }
            else if ( (LA2_0==24) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("201:1: rule__Item__Alternatives_0 : ( ( ruleNormalItem ) | ( ruleGroupItem ) );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:206:1: ( ruleNormalItem )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:206:1: ( ruleNormalItem )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:207:1: ruleNormalItem
                    {
                     before(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleNormalItem_in_rule__Item__Alternatives_0371);
                    ruleNormalItem();
                    _fsp--;

                     after(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:212:6: ( ruleGroupItem )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:212:6: ( ruleGroupItem )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:213:1: ruleGroupItem
                    {
                     before(grammarAccess.getItemAccess().getGroupItemParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleGroupItem_in_rule__Item__Alternatives_0388);
                    ruleGroupItem();
                    _fsp--;

                     after(grammarAccess.getItemAccess().getGroupItemParserRuleCall_0_1()); 

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
    // $ANTLR end rule__Item__Alternatives_0


    // $ANTLR start rule__Item__IconAlternatives_3_1_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:223:1: rule__Item__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Item__IconAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:227:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("223:1: rule__Item__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:228:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:228:1: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:229:1: RULE_ID
                    {
                     before(grammarAccess.getItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Item__IconAlternatives_3_1_0420); 
                     after(grammarAccess.getItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:234:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:234:6: ( RULE_STRING )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:235:1: RULE_STRING
                    {
                     before(grammarAccess.getItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Item__IconAlternatives_3_1_0437); 
                     after(grammarAccess.getItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 

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
    // $ANTLR end rule__Item__IconAlternatives_3_1_0


    // $ANTLR start rule__NormalItem__TypeAlternatives_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:245:1: rule__NormalItem__TypeAlternatives_0 : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );
    public final void rule__NormalItem__TypeAlternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:249:1: ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) )
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
                    new NoViableAltException("245:1: rule__NormalItem__TypeAlternatives_0 : ( ( 'Switch' ) | ( 'Rollershutter' ) | ( 'Number' ) | ( 'String' ) | ( 'Dimmer' ) | ( 'Contact' ) | ( RULE_ID ) );", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:250:1: ( 'Switch' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:250:1: ( 'Switch' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:251:1: 'Switch'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0()); 
                    match(input,11,FOLLOW_11_in_rule__NormalItem__TypeAlternatives_0470); 
                     after(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:258:6: ( 'Rollershutter' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:258:6: ( 'Rollershutter' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:259:1: 'Rollershutter'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeRollershutterKeyword_0_1()); 
                    match(input,12,FOLLOW_12_in_rule__NormalItem__TypeAlternatives_0490); 
                     after(grammarAccess.getNormalItemAccess().getTypeRollershutterKeyword_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:266:6: ( 'Number' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:266:6: ( 'Number' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:267:1: 'Number'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeNumberKeyword_0_2()); 
                    match(input,13,FOLLOW_13_in_rule__NormalItem__TypeAlternatives_0510); 
                     after(grammarAccess.getNormalItemAccess().getTypeNumberKeyword_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:274:6: ( 'String' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:274:6: ( 'String' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:275:1: 'String'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_3()); 
                    match(input,14,FOLLOW_14_in_rule__NormalItem__TypeAlternatives_0530); 
                     after(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:282:6: ( 'Dimmer' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:282:6: ( 'Dimmer' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:283:1: 'Dimmer'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_4()); 
                    match(input,15,FOLLOW_15_in_rule__NormalItem__TypeAlternatives_0550); 
                     after(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_4()); 

                    }


                    }
                    break;
                case 6 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:290:6: ( 'Contact' )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:290:6: ( 'Contact' )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:291:1: 'Contact'
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_5()); 
                    match(input,16,FOLLOW_16_in_rule__NormalItem__TypeAlternatives_0570); 
                     after(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_5()); 

                    }


                    }
                    break;
                case 7 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:298:6: ( RULE_ID )
                    {
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:298:6: ( RULE_ID )
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:299:1: RULE_ID
                    {
                     before(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_6()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__NormalItem__TypeAlternatives_0589); 
                     after(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_6()); 

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
    // $ANTLR end rule__NormalItem__TypeAlternatives_0


    // $ANTLR start rule__Item__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:311:1: rule__Item__Group__0 : rule__Item__Group__0__Impl rule__Item__Group__1 ;
    public final void rule__Item__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:315:1: ( rule__Item__Group__0__Impl rule__Item__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:316:2: rule__Item__Group__0__Impl rule__Item__Group__1
            {
            pushFollow(FOLLOW_rule__Item__Group__0__Impl_in_rule__Item__Group__0619);
            rule__Item__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group__1_in_rule__Item__Group__0622);
            rule__Item__Group__1();
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
    // $ANTLR end rule__Item__Group__0


    // $ANTLR start rule__Item__Group__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:323:1: rule__Item__Group__0__Impl : ( ( rule__Item__Alternatives_0 ) ) ;
    public final void rule__Item__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:327:1: ( ( ( rule__Item__Alternatives_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:328:1: ( ( rule__Item__Alternatives_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:328:1: ( ( rule__Item__Alternatives_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:329:1: ( rule__Item__Alternatives_0 )
            {
             before(grammarAccess.getItemAccess().getAlternatives_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:330:1: ( rule__Item__Alternatives_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:330:2: rule__Item__Alternatives_0
            {
            pushFollow(FOLLOW_rule__Item__Alternatives_0_in_rule__Item__Group__0__Impl649);
            rule__Item__Alternatives_0();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getAlternatives_0()); 

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
    // $ANTLR end rule__Item__Group__0__Impl


    // $ANTLR start rule__Item__Group__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:340:1: rule__Item__Group__1 : rule__Item__Group__1__Impl rule__Item__Group__2 ;
    public final void rule__Item__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:344:1: ( rule__Item__Group__1__Impl rule__Item__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:345:2: rule__Item__Group__1__Impl rule__Item__Group__2
            {
            pushFollow(FOLLOW_rule__Item__Group__1__Impl_in_rule__Item__Group__1679);
            rule__Item__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group__2_in_rule__Item__Group__1682);
            rule__Item__Group__2();
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
    // $ANTLR end rule__Item__Group__1


    // $ANTLR start rule__Item__Group__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:352:1: rule__Item__Group__1__Impl : ( ( rule__Item__NameAssignment_1 ) ) ;
    public final void rule__Item__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:356:1: ( ( ( rule__Item__NameAssignment_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:357:1: ( ( rule__Item__NameAssignment_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:357:1: ( ( rule__Item__NameAssignment_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:358:1: ( rule__Item__NameAssignment_1 )
            {
             before(grammarAccess.getItemAccess().getNameAssignment_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:359:1: ( rule__Item__NameAssignment_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:359:2: rule__Item__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__Item__NameAssignment_1_in_rule__Item__Group__1__Impl709);
            rule__Item__NameAssignment_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getNameAssignment_1()); 

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
    // $ANTLR end rule__Item__Group__1__Impl


    // $ANTLR start rule__Item__Group__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:369:1: rule__Item__Group__2 : rule__Item__Group__2__Impl rule__Item__Group__3 ;
    public final void rule__Item__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:373:1: ( rule__Item__Group__2__Impl rule__Item__Group__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:374:2: rule__Item__Group__2__Impl rule__Item__Group__3
            {
            pushFollow(FOLLOW_rule__Item__Group__2__Impl_in_rule__Item__Group__2739);
            rule__Item__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group__3_in_rule__Item__Group__2742);
            rule__Item__Group__3();
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
    // $ANTLR end rule__Item__Group__2


    // $ANTLR start rule__Item__Group__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:381:1: rule__Item__Group__2__Impl : ( ( rule__Item__LabelAssignment_2 )? ) ;
    public final void rule__Item__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:385:1: ( ( ( rule__Item__LabelAssignment_2 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:386:1: ( ( rule__Item__LabelAssignment_2 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:386:1: ( ( rule__Item__LabelAssignment_2 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:387:1: ( rule__Item__LabelAssignment_2 )?
            {
             before(grammarAccess.getItemAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:388:1: ( rule__Item__LabelAssignment_2 )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_STRING) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:388:2: rule__Item__LabelAssignment_2
                    {
                    pushFollow(FOLLOW_rule__Item__LabelAssignment_2_in_rule__Item__Group__2__Impl769);
                    rule__Item__LabelAssignment_2();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getItemAccess().getLabelAssignment_2()); 

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
    // $ANTLR end rule__Item__Group__2__Impl


    // $ANTLR start rule__Item__Group__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:398:1: rule__Item__Group__3 : rule__Item__Group__3__Impl rule__Item__Group__4 ;
    public final void rule__Item__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:402:1: ( rule__Item__Group__3__Impl rule__Item__Group__4 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:403:2: rule__Item__Group__3__Impl rule__Item__Group__4
            {
            pushFollow(FOLLOW_rule__Item__Group__3__Impl_in_rule__Item__Group__3800);
            rule__Item__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group__4_in_rule__Item__Group__3803);
            rule__Item__Group__4();
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
    // $ANTLR end rule__Item__Group__3


    // $ANTLR start rule__Item__Group__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:410:1: rule__Item__Group__3__Impl : ( ( rule__Item__Group_3__0 )? ) ;
    public final void rule__Item__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:414:1: ( ( ( rule__Item__Group_3__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:415:1: ( ( rule__Item__Group_3__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:415:1: ( ( rule__Item__Group_3__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:416:1: ( rule__Item__Group_3__0 )?
            {
             before(grammarAccess.getItemAccess().getGroup_3()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:417:1: ( rule__Item__Group_3__0 )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==17) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:417:2: rule__Item__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__Item__Group_3__0_in_rule__Item__Group__3__Impl830);
                    rule__Item__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getItemAccess().getGroup_3()); 

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
    // $ANTLR end rule__Item__Group__3__Impl


    // $ANTLR start rule__Item__Group__4
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:427:1: rule__Item__Group__4 : rule__Item__Group__4__Impl rule__Item__Group__5 ;
    public final void rule__Item__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:431:1: ( rule__Item__Group__4__Impl rule__Item__Group__5 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:432:2: rule__Item__Group__4__Impl rule__Item__Group__5
            {
            pushFollow(FOLLOW_rule__Item__Group__4__Impl_in_rule__Item__Group__4861);
            rule__Item__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group__5_in_rule__Item__Group__4864);
            rule__Item__Group__5();
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
    // $ANTLR end rule__Item__Group__4


    // $ANTLR start rule__Item__Group__4__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:439:1: rule__Item__Group__4__Impl : ( ( rule__Item__Group_4__0 )? ) ;
    public final void rule__Item__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:443:1: ( ( ( rule__Item__Group_4__0 )? ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:444:1: ( ( rule__Item__Group_4__0 )? )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:444:1: ( ( rule__Item__Group_4__0 )? )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:445:1: ( rule__Item__Group_4__0 )?
            {
             before(grammarAccess.getItemAccess().getGroup_4()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:446:1: ( rule__Item__Group_4__0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==19) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:446:2: rule__Item__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__Item__Group_4__0_in_rule__Item__Group__4__Impl891);
                    rule__Item__Group_4__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getItemAccess().getGroup_4()); 

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
    // $ANTLR end rule__Item__Group__4__Impl


    // $ANTLR start rule__Item__Group__5
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:456:1: rule__Item__Group__5 : rule__Item__Group__5__Impl ;
    public final void rule__Item__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:460:1: ( rule__Item__Group__5__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:461:2: rule__Item__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group__5__Impl_in_rule__Item__Group__5922);
            rule__Item__Group__5__Impl();
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
    // $ANTLR end rule__Item__Group__5


    // $ANTLR start rule__Item__Group__5__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:467:1: rule__Item__Group__5__Impl : ( ( rule__Item__Group_5__0 )* ) ;
    public final void rule__Item__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:471:1: ( ( ( rule__Item__Group_5__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__Item__Group_5__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:472:1: ( ( rule__Item__Group_5__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:473:1: ( rule__Item__Group_5__0 )*
            {
             before(grammarAccess.getItemAccess().getGroup_5()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:1: ( rule__Item__Group_5__0 )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==22) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:474:2: rule__Item__Group_5__0
            	    {
            	    pushFollow(FOLLOW_rule__Item__Group_5__0_in_rule__Item__Group__5__Impl949);
            	    rule__Item__Group_5__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

             after(grammarAccess.getItemAccess().getGroup_5()); 

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
    // $ANTLR end rule__Item__Group__5__Impl


    // $ANTLR start rule__Item__Group_3__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:496:1: rule__Item__Group_3__0 : rule__Item__Group_3__0__Impl rule__Item__Group_3__1 ;
    public final void rule__Item__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:500:1: ( rule__Item__Group_3__0__Impl rule__Item__Group_3__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:501:2: rule__Item__Group_3__0__Impl rule__Item__Group_3__1
            {
            pushFollow(FOLLOW_rule__Item__Group_3__0__Impl_in_rule__Item__Group_3__0992);
            rule__Item__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_3__1_in_rule__Item__Group_3__0995);
            rule__Item__Group_3__1();
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
    // $ANTLR end rule__Item__Group_3__0


    // $ANTLR start rule__Item__Group_3__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:508:1: rule__Item__Group_3__0__Impl : ( '<' ) ;
    public final void rule__Item__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:512:1: ( ( '<' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:513:1: ( '<' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:513:1: ( '<' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:514:1: '<'
            {
             before(grammarAccess.getItemAccess().getLessThanSignKeyword_3_0()); 
            match(input,17,FOLLOW_17_in_rule__Item__Group_3__0__Impl1023); 
             after(grammarAccess.getItemAccess().getLessThanSignKeyword_3_0()); 

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
    // $ANTLR end rule__Item__Group_3__0__Impl


    // $ANTLR start rule__Item__Group_3__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:527:1: rule__Item__Group_3__1 : rule__Item__Group_3__1__Impl rule__Item__Group_3__2 ;
    public final void rule__Item__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:531:1: ( rule__Item__Group_3__1__Impl rule__Item__Group_3__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:532:2: rule__Item__Group_3__1__Impl rule__Item__Group_3__2
            {
            pushFollow(FOLLOW_rule__Item__Group_3__1__Impl_in_rule__Item__Group_3__11054);
            rule__Item__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_3__2_in_rule__Item__Group_3__11057);
            rule__Item__Group_3__2();
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
    // $ANTLR end rule__Item__Group_3__1


    // $ANTLR start rule__Item__Group_3__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:539:1: rule__Item__Group_3__1__Impl : ( ( rule__Item__IconAssignment_3_1 ) ) ;
    public final void rule__Item__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:543:1: ( ( ( rule__Item__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:544:1: ( ( rule__Item__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:544:1: ( ( rule__Item__IconAssignment_3_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:545:1: ( rule__Item__IconAssignment_3_1 )
            {
             before(grammarAccess.getItemAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:546:1: ( rule__Item__IconAssignment_3_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:546:2: rule__Item__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__Item__IconAssignment_3_1_in_rule__Item__Group_3__1__Impl1084);
            rule__Item__IconAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getIconAssignment_3_1()); 

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
    // $ANTLR end rule__Item__Group_3__1__Impl


    // $ANTLR start rule__Item__Group_3__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:556:1: rule__Item__Group_3__2 : rule__Item__Group_3__2__Impl ;
    public final void rule__Item__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:560:1: ( rule__Item__Group_3__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:561:2: rule__Item__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group_3__2__Impl_in_rule__Item__Group_3__21114);
            rule__Item__Group_3__2__Impl();
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
    // $ANTLR end rule__Item__Group_3__2


    // $ANTLR start rule__Item__Group_3__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:567:1: rule__Item__Group_3__2__Impl : ( '>' ) ;
    public final void rule__Item__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:571:1: ( ( '>' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:572:1: ( '>' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:572:1: ( '>' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:573:1: '>'
            {
             before(grammarAccess.getItemAccess().getGreaterThanSignKeyword_3_2()); 
            match(input,18,FOLLOW_18_in_rule__Item__Group_3__2__Impl1142); 
             after(grammarAccess.getItemAccess().getGreaterThanSignKeyword_3_2()); 

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
    // $ANTLR end rule__Item__Group_3__2__Impl


    // $ANTLR start rule__Item__Group_4__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:592:1: rule__Item__Group_4__0 : rule__Item__Group_4__0__Impl rule__Item__Group_4__1 ;
    public final void rule__Item__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:596:1: ( rule__Item__Group_4__0__Impl rule__Item__Group_4__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:597:2: rule__Item__Group_4__0__Impl rule__Item__Group_4__1
            {
            pushFollow(FOLLOW_rule__Item__Group_4__0__Impl_in_rule__Item__Group_4__01179);
            rule__Item__Group_4__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_4__1_in_rule__Item__Group_4__01182);
            rule__Item__Group_4__1();
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
    // $ANTLR end rule__Item__Group_4__0


    // $ANTLR start rule__Item__Group_4__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:604:1: rule__Item__Group_4__0__Impl : ( '(' ) ;
    public final void rule__Item__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:608:1: ( ( '(' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:609:1: ( '(' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:609:1: ( '(' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:610:1: '('
            {
             before(grammarAccess.getItemAccess().getLeftParenthesisKeyword_4_0()); 
            match(input,19,FOLLOW_19_in_rule__Item__Group_4__0__Impl1210); 
             after(grammarAccess.getItemAccess().getLeftParenthesisKeyword_4_0()); 

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
    // $ANTLR end rule__Item__Group_4__0__Impl


    // $ANTLR start rule__Item__Group_4__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:623:1: rule__Item__Group_4__1 : rule__Item__Group_4__1__Impl rule__Item__Group_4__2 ;
    public final void rule__Item__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:627:1: ( rule__Item__Group_4__1__Impl rule__Item__Group_4__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:628:2: rule__Item__Group_4__1__Impl rule__Item__Group_4__2
            {
            pushFollow(FOLLOW_rule__Item__Group_4__1__Impl_in_rule__Item__Group_4__11241);
            rule__Item__Group_4__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_4__2_in_rule__Item__Group_4__11244);
            rule__Item__Group_4__2();
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
    // $ANTLR end rule__Item__Group_4__1


    // $ANTLR start rule__Item__Group_4__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:635:1: rule__Item__Group_4__1__Impl : ( ( rule__Item__GroupsAssignment_4_1 ) ) ;
    public final void rule__Item__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:639:1: ( ( ( rule__Item__GroupsAssignment_4_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:640:1: ( ( rule__Item__GroupsAssignment_4_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:640:1: ( ( rule__Item__GroupsAssignment_4_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:641:1: ( rule__Item__GroupsAssignment_4_1 )
            {
             before(grammarAccess.getItemAccess().getGroupsAssignment_4_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:642:1: ( rule__Item__GroupsAssignment_4_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:642:2: rule__Item__GroupsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__Item__GroupsAssignment_4_1_in_rule__Item__Group_4__1__Impl1271);
            rule__Item__GroupsAssignment_4_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getGroupsAssignment_4_1()); 

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
    // $ANTLR end rule__Item__Group_4__1__Impl


    // $ANTLR start rule__Item__Group_4__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:652:1: rule__Item__Group_4__2 : rule__Item__Group_4__2__Impl rule__Item__Group_4__3 ;
    public final void rule__Item__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:656:1: ( rule__Item__Group_4__2__Impl rule__Item__Group_4__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:657:2: rule__Item__Group_4__2__Impl rule__Item__Group_4__3
            {
            pushFollow(FOLLOW_rule__Item__Group_4__2__Impl_in_rule__Item__Group_4__21301);
            rule__Item__Group_4__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_4__3_in_rule__Item__Group_4__21304);
            rule__Item__Group_4__3();
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
    // $ANTLR end rule__Item__Group_4__2


    // $ANTLR start rule__Item__Group_4__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:664:1: rule__Item__Group_4__2__Impl : ( ( rule__Item__Group_4_2__0 )* ) ;
    public final void rule__Item__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:668:1: ( ( ( rule__Item__Group_4_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:669:1: ( ( rule__Item__Group_4_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:669:1: ( ( rule__Item__Group_4_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:670:1: ( rule__Item__Group_4_2__0 )*
            {
             before(grammarAccess.getItemAccess().getGroup_4_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:671:1: ( rule__Item__Group_4_2__0 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==21) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:671:2: rule__Item__Group_4_2__0
            	    {
            	    pushFollow(FOLLOW_rule__Item__Group_4_2__0_in_rule__Item__Group_4__2__Impl1331);
            	    rule__Item__Group_4_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getItemAccess().getGroup_4_2()); 

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
    // $ANTLR end rule__Item__Group_4__2__Impl


    // $ANTLR start rule__Item__Group_4__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:681:1: rule__Item__Group_4__3 : rule__Item__Group_4__3__Impl ;
    public final void rule__Item__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:685:1: ( rule__Item__Group_4__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:686:2: rule__Item__Group_4__3__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group_4__3__Impl_in_rule__Item__Group_4__31362);
            rule__Item__Group_4__3__Impl();
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
    // $ANTLR end rule__Item__Group_4__3


    // $ANTLR start rule__Item__Group_4__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:692:1: rule__Item__Group_4__3__Impl : ( ')' ) ;
    public final void rule__Item__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:696:1: ( ( ')' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:697:1: ( ')' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:697:1: ( ')' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:698:1: ')'
            {
             before(grammarAccess.getItemAccess().getRightParenthesisKeyword_4_3()); 
            match(input,20,FOLLOW_20_in_rule__Item__Group_4__3__Impl1390); 
             after(grammarAccess.getItemAccess().getRightParenthesisKeyword_4_3()); 

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
    // $ANTLR end rule__Item__Group_4__3__Impl


    // $ANTLR start rule__Item__Group_4_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:719:1: rule__Item__Group_4_2__0 : rule__Item__Group_4_2__0__Impl rule__Item__Group_4_2__1 ;
    public final void rule__Item__Group_4_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:723:1: ( rule__Item__Group_4_2__0__Impl rule__Item__Group_4_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:724:2: rule__Item__Group_4_2__0__Impl rule__Item__Group_4_2__1
            {
            pushFollow(FOLLOW_rule__Item__Group_4_2__0__Impl_in_rule__Item__Group_4_2__01429);
            rule__Item__Group_4_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_4_2__1_in_rule__Item__Group_4_2__01432);
            rule__Item__Group_4_2__1();
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
    // $ANTLR end rule__Item__Group_4_2__0


    // $ANTLR start rule__Item__Group_4_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:731:1: rule__Item__Group_4_2__0__Impl : ( ',' ) ;
    public final void rule__Item__Group_4_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:735:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:736:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:736:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:737:1: ','
            {
             before(grammarAccess.getItemAccess().getCommaKeyword_4_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Item__Group_4_2__0__Impl1460); 
             after(grammarAccess.getItemAccess().getCommaKeyword_4_2_0()); 

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
    // $ANTLR end rule__Item__Group_4_2__0__Impl


    // $ANTLR start rule__Item__Group_4_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:750:1: rule__Item__Group_4_2__1 : rule__Item__Group_4_2__1__Impl ;
    public final void rule__Item__Group_4_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:754:1: ( rule__Item__Group_4_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:755:2: rule__Item__Group_4_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group_4_2__1__Impl_in_rule__Item__Group_4_2__11491);
            rule__Item__Group_4_2__1__Impl();
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
    // $ANTLR end rule__Item__Group_4_2__1


    // $ANTLR start rule__Item__Group_4_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:761:1: rule__Item__Group_4_2__1__Impl : ( ( rule__Item__GroupsAssignment_4_2_1 ) ) ;
    public final void rule__Item__Group_4_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:765:1: ( ( ( rule__Item__GroupsAssignment_4_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:766:1: ( ( rule__Item__GroupsAssignment_4_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:766:1: ( ( rule__Item__GroupsAssignment_4_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:767:1: ( rule__Item__GroupsAssignment_4_2_1 )
            {
             before(grammarAccess.getItemAccess().getGroupsAssignment_4_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:768:1: ( rule__Item__GroupsAssignment_4_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:768:2: rule__Item__GroupsAssignment_4_2_1
            {
            pushFollow(FOLLOW_rule__Item__GroupsAssignment_4_2_1_in_rule__Item__Group_4_2__1__Impl1518);
            rule__Item__GroupsAssignment_4_2_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getGroupsAssignment_4_2_1()); 

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
    // $ANTLR end rule__Item__Group_4_2__1__Impl


    // $ANTLR start rule__Item__Group_5__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:782:1: rule__Item__Group_5__0 : rule__Item__Group_5__0__Impl rule__Item__Group_5__1 ;
    public final void rule__Item__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:786:1: ( rule__Item__Group_5__0__Impl rule__Item__Group_5__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:787:2: rule__Item__Group_5__0__Impl rule__Item__Group_5__1
            {
            pushFollow(FOLLOW_rule__Item__Group_5__0__Impl_in_rule__Item__Group_5__01552);
            rule__Item__Group_5__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_5__1_in_rule__Item__Group_5__01555);
            rule__Item__Group_5__1();
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
    // $ANTLR end rule__Item__Group_5__0


    // $ANTLR start rule__Item__Group_5__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:794:1: rule__Item__Group_5__0__Impl : ( '{' ) ;
    public final void rule__Item__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:798:1: ( ( '{' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:799:1: ( '{' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:799:1: ( '{' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:800:1: '{'
            {
             before(grammarAccess.getItemAccess().getLeftCurlyBracketKeyword_5_0()); 
            match(input,22,FOLLOW_22_in_rule__Item__Group_5__0__Impl1583); 
             after(grammarAccess.getItemAccess().getLeftCurlyBracketKeyword_5_0()); 

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
    // $ANTLR end rule__Item__Group_5__0__Impl


    // $ANTLR start rule__Item__Group_5__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:813:1: rule__Item__Group_5__1 : rule__Item__Group_5__1__Impl rule__Item__Group_5__2 ;
    public final void rule__Item__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:817:1: ( rule__Item__Group_5__1__Impl rule__Item__Group_5__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:818:2: rule__Item__Group_5__1__Impl rule__Item__Group_5__2
            {
            pushFollow(FOLLOW_rule__Item__Group_5__1__Impl_in_rule__Item__Group_5__11614);
            rule__Item__Group_5__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_5__2_in_rule__Item__Group_5__11617);
            rule__Item__Group_5__2();
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
    // $ANTLR end rule__Item__Group_5__1


    // $ANTLR start rule__Item__Group_5__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:825:1: rule__Item__Group_5__1__Impl : ( ( rule__Item__BindingsAssignment_5_1 ) ) ;
    public final void rule__Item__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:829:1: ( ( ( rule__Item__BindingsAssignment_5_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:830:1: ( ( rule__Item__BindingsAssignment_5_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:830:1: ( ( rule__Item__BindingsAssignment_5_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:831:1: ( rule__Item__BindingsAssignment_5_1 )
            {
             before(grammarAccess.getItemAccess().getBindingsAssignment_5_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:832:1: ( rule__Item__BindingsAssignment_5_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:832:2: rule__Item__BindingsAssignment_5_1
            {
            pushFollow(FOLLOW_rule__Item__BindingsAssignment_5_1_in_rule__Item__Group_5__1__Impl1644);
            rule__Item__BindingsAssignment_5_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getBindingsAssignment_5_1()); 

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
    // $ANTLR end rule__Item__Group_5__1__Impl


    // $ANTLR start rule__Item__Group_5__2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:842:1: rule__Item__Group_5__2 : rule__Item__Group_5__2__Impl rule__Item__Group_5__3 ;
    public final void rule__Item__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:846:1: ( rule__Item__Group_5__2__Impl rule__Item__Group_5__3 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:847:2: rule__Item__Group_5__2__Impl rule__Item__Group_5__3
            {
            pushFollow(FOLLOW_rule__Item__Group_5__2__Impl_in_rule__Item__Group_5__21674);
            rule__Item__Group_5__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_5__3_in_rule__Item__Group_5__21677);
            rule__Item__Group_5__3();
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
    // $ANTLR end rule__Item__Group_5__2


    // $ANTLR start rule__Item__Group_5__2__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:854:1: rule__Item__Group_5__2__Impl : ( ( rule__Item__Group_5_2__0 )* ) ;
    public final void rule__Item__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:858:1: ( ( ( rule__Item__Group_5_2__0 )* ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:859:1: ( ( rule__Item__Group_5_2__0 )* )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:859:1: ( ( rule__Item__Group_5_2__0 )* )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:860:1: ( rule__Item__Group_5_2__0 )*
            {
             before(grammarAccess.getItemAccess().getGroup_5_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:861:1: ( rule__Item__Group_5_2__0 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==21) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:861:2: rule__Item__Group_5_2__0
            	    {
            	    pushFollow(FOLLOW_rule__Item__Group_5_2__0_in_rule__Item__Group_5__2__Impl1704);
            	    rule__Item__Group_5_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

             after(grammarAccess.getItemAccess().getGroup_5_2()); 

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
    // $ANTLR end rule__Item__Group_5__2__Impl


    // $ANTLR start rule__Item__Group_5__3
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:871:1: rule__Item__Group_5__3 : rule__Item__Group_5__3__Impl ;
    public final void rule__Item__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:875:1: ( rule__Item__Group_5__3__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:876:2: rule__Item__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group_5__3__Impl_in_rule__Item__Group_5__31735);
            rule__Item__Group_5__3__Impl();
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
    // $ANTLR end rule__Item__Group_5__3


    // $ANTLR start rule__Item__Group_5__3__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:882:1: rule__Item__Group_5__3__Impl : ( '}' ) ;
    public final void rule__Item__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:886:1: ( ( '}' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:887:1: ( '}' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:887:1: ( '}' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:888:1: '}'
            {
             before(grammarAccess.getItemAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,23,FOLLOW_23_in_rule__Item__Group_5__3__Impl1763); 
             after(grammarAccess.getItemAccess().getRightCurlyBracketKeyword_5_3()); 

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
    // $ANTLR end rule__Item__Group_5__3__Impl


    // $ANTLR start rule__Item__Group_5_2__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:909:1: rule__Item__Group_5_2__0 : rule__Item__Group_5_2__0__Impl rule__Item__Group_5_2__1 ;
    public final void rule__Item__Group_5_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:913:1: ( rule__Item__Group_5_2__0__Impl rule__Item__Group_5_2__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:914:2: rule__Item__Group_5_2__0__Impl rule__Item__Group_5_2__1
            {
            pushFollow(FOLLOW_rule__Item__Group_5_2__0__Impl_in_rule__Item__Group_5_2__01802);
            rule__Item__Group_5_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Item__Group_5_2__1_in_rule__Item__Group_5_2__01805);
            rule__Item__Group_5_2__1();
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
    // $ANTLR end rule__Item__Group_5_2__0


    // $ANTLR start rule__Item__Group_5_2__0__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:921:1: rule__Item__Group_5_2__0__Impl : ( ',' ) ;
    public final void rule__Item__Group_5_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:925:1: ( ( ',' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:926:1: ( ',' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:926:1: ( ',' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:927:1: ','
            {
             before(grammarAccess.getItemAccess().getCommaKeyword_5_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Item__Group_5_2__0__Impl1833); 
             after(grammarAccess.getItemAccess().getCommaKeyword_5_2_0()); 

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
    // $ANTLR end rule__Item__Group_5_2__0__Impl


    // $ANTLR start rule__Item__Group_5_2__1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:940:1: rule__Item__Group_5_2__1 : rule__Item__Group_5_2__1__Impl ;
    public final void rule__Item__Group_5_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:944:1: ( rule__Item__Group_5_2__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:945:2: rule__Item__Group_5_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Item__Group_5_2__1__Impl_in_rule__Item__Group_5_2__11864);
            rule__Item__Group_5_2__1__Impl();
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
    // $ANTLR end rule__Item__Group_5_2__1


    // $ANTLR start rule__Item__Group_5_2__1__Impl
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:951:1: rule__Item__Group_5_2__1__Impl : ( ( rule__Item__BindingsAssignment_5_2_1 ) ) ;
    public final void rule__Item__Group_5_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:955:1: ( ( ( rule__Item__BindingsAssignment_5_2_1 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:956:1: ( ( rule__Item__BindingsAssignment_5_2_1 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:956:1: ( ( rule__Item__BindingsAssignment_5_2_1 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:957:1: ( rule__Item__BindingsAssignment_5_2_1 )
            {
             before(grammarAccess.getItemAccess().getBindingsAssignment_5_2_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:958:1: ( rule__Item__BindingsAssignment_5_2_1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:958:2: rule__Item__BindingsAssignment_5_2_1
            {
            pushFollow(FOLLOW_rule__Item__BindingsAssignment_5_2_1_in_rule__Item__Group_5_2__1__Impl1891);
            rule__Item__BindingsAssignment_5_2_1();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getBindingsAssignment_5_2_1()); 

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
    // $ANTLR end rule__Item__Group_5_2__1__Impl


    // $ANTLR start rule__GroupItem__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:972:1: rule__GroupItem__Group__0 : rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1 ;
    public final void rule__GroupItem__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:976:1: ( rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:977:2: rule__GroupItem__Group__0__Impl rule__GroupItem__Group__1
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__0__Impl_in_rule__GroupItem__Group__01925);
            rule__GroupItem__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__GroupItem__Group__1_in_rule__GroupItem__Group__01928);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:984:1: rule__GroupItem__Group__0__Impl : ( 'Group' ) ;
    public final void rule__GroupItem__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:988:1: ( ( 'Group' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:989:1: ( 'Group' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:989:1: ( 'Group' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:990:1: 'Group'
            {
             before(grammarAccess.getGroupItemAccess().getGroupKeyword_0()); 
            match(input,24,FOLLOW_24_in_rule__GroupItem__Group__0__Impl1956); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1003:1: rule__GroupItem__Group__1 : rule__GroupItem__Group__1__Impl ;
    public final void rule__GroupItem__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1007:1: ( rule__GroupItem__Group__1__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1008:2: rule__GroupItem__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__GroupItem__Group__1__Impl_in_rule__GroupItem__Group__11987);
            rule__GroupItem__Group__1__Impl();
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1014:1: rule__GroupItem__Group__1__Impl : ( () ) ;
    public final void rule__GroupItem__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1018:1: ( ( () ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1019:1: ( () )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1019:1: ( () )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1020:1: ()
            {
             before(grammarAccess.getGroupItemAccess().getGroupAction_1()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1021:1: ()
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1023:1: 
            {
            }

             after(grammarAccess.getGroupItemAccess().getGroupAction_1()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__GroupItem__Group__1__Impl


    // $ANTLR start rule__Binding__Group__0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1037:1: rule__Binding__Group__0 : rule__Binding__Group__0__Impl rule__Binding__Group__1 ;
    public final void rule__Binding__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1041:1: ( rule__Binding__Group__0__Impl rule__Binding__Group__1 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1042:2: rule__Binding__Group__0__Impl rule__Binding__Group__1
            {
            pushFollow(FOLLOW_rule__Binding__Group__0__Impl_in_rule__Binding__Group__02049);
            rule__Binding__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Binding__Group__1_in_rule__Binding__Group__02052);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1049:1: rule__Binding__Group__0__Impl : ( ( rule__Binding__TypeAssignment_0 ) ) ;
    public final void rule__Binding__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1053:1: ( ( ( rule__Binding__TypeAssignment_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1054:1: ( ( rule__Binding__TypeAssignment_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1054:1: ( ( rule__Binding__TypeAssignment_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1055:1: ( rule__Binding__TypeAssignment_0 )
            {
             before(grammarAccess.getBindingAccess().getTypeAssignment_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1056:1: ( rule__Binding__TypeAssignment_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1056:2: rule__Binding__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__Binding__TypeAssignment_0_in_rule__Binding__Group__0__Impl2079);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1066:1: rule__Binding__Group__1 : rule__Binding__Group__1__Impl rule__Binding__Group__2 ;
    public final void rule__Binding__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1070:1: ( rule__Binding__Group__1__Impl rule__Binding__Group__2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1071:2: rule__Binding__Group__1__Impl rule__Binding__Group__2
            {
            pushFollow(FOLLOW_rule__Binding__Group__1__Impl_in_rule__Binding__Group__12109);
            rule__Binding__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Binding__Group__2_in_rule__Binding__Group__12112);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1078:1: rule__Binding__Group__1__Impl : ( '=' ) ;
    public final void rule__Binding__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1082:1: ( ( '=' ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1083:1: ( '=' )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1083:1: ( '=' )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1084:1: '='
            {
             before(grammarAccess.getBindingAccess().getEqualsSignKeyword_1()); 
            match(input,25,FOLLOW_25_in_rule__Binding__Group__1__Impl2140); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1097:1: rule__Binding__Group__2 : rule__Binding__Group__2__Impl ;
    public final void rule__Binding__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1101:1: ( rule__Binding__Group__2__Impl )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1102:2: rule__Binding__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Binding__Group__2__Impl_in_rule__Binding__Group__22171);
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1108:1: rule__Binding__Group__2__Impl : ( ( rule__Binding__ConfigurationAssignment_2 ) ) ;
    public final void rule__Binding__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1112:1: ( ( ( rule__Binding__ConfigurationAssignment_2 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1113:1: ( ( rule__Binding__ConfigurationAssignment_2 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1113:1: ( ( rule__Binding__ConfigurationAssignment_2 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1114:1: ( rule__Binding__ConfigurationAssignment_2 )
            {
             before(grammarAccess.getBindingAccess().getConfigurationAssignment_2()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1115:1: ( rule__Binding__ConfigurationAssignment_2 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1115:2: rule__Binding__ConfigurationAssignment_2
            {
            pushFollow(FOLLOW_rule__Binding__ConfigurationAssignment_2_in_rule__Binding__Group__2__Impl2198);
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


    // $ANTLR start rule__ItemModel__ItemsAssignment
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1132:1: rule__ItemModel__ItemsAssignment : ( ruleItem ) ;
    public final void rule__ItemModel__ItemsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1136:1: ( ( ruleItem ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1137:1: ( ruleItem )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1137:1: ( ruleItem )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1138:1: ruleItem
            {
             before(grammarAccess.getItemModelAccess().getItemsItemParserRuleCall_0()); 
            pushFollow(FOLLOW_ruleItem_in_rule__ItemModel__ItemsAssignment2239);
            ruleItem();
            _fsp--;

             after(grammarAccess.getItemModelAccess().getItemsItemParserRuleCall_0()); 

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


    // $ANTLR start rule__Item__NameAssignment_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1147:1: rule__Item__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Item__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1151:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1152:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1152:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1153:1: RULE_ID
            {
             before(grammarAccess.getItemAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Item__NameAssignment_12270); 
             after(grammarAccess.getItemAccess().getNameIDTerminalRuleCall_1_0()); 

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
    // $ANTLR end rule__Item__NameAssignment_1


    // $ANTLR start rule__Item__LabelAssignment_2
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1162:1: rule__Item__LabelAssignment_2 : ( RULE_STRING ) ;
    public final void rule__Item__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1166:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1167:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1167:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1168:1: RULE_STRING
            {
             before(grammarAccess.getItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Item__LabelAssignment_22301); 
             after(grammarAccess.getItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 

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
    // $ANTLR end rule__Item__LabelAssignment_2


    // $ANTLR start rule__Item__IconAssignment_3_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1177:1: rule__Item__IconAssignment_3_1 : ( ( rule__Item__IconAlternatives_3_1_0 ) ) ;
    public final void rule__Item__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1181:1: ( ( ( rule__Item__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1182:1: ( ( rule__Item__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1182:1: ( ( rule__Item__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1183:1: ( rule__Item__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getItemAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1184:1: ( rule__Item__IconAlternatives_3_1_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1184:2: rule__Item__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__Item__IconAlternatives_3_1_0_in_rule__Item__IconAssignment_3_12332);
            rule__Item__IconAlternatives_3_1_0();
            _fsp--;


            }

             after(grammarAccess.getItemAccess().getIconAlternatives_3_1_0()); 

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
    // $ANTLR end rule__Item__IconAssignment_3_1


    // $ANTLR start rule__Item__GroupsAssignment_4_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1193:1: rule__Item__GroupsAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__Item__GroupsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1197:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1198:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1198:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1199:1: ( RULE_ID )
            {
             before(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1200:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1201:1: RULE_ID
            {
             before(grammarAccess.getItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Item__GroupsAssignment_4_12369); 
             after(grammarAccess.getItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_1_0()); 

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
    // $ANTLR end rule__Item__GroupsAssignment_4_1


    // $ANTLR start rule__Item__GroupsAssignment_4_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1212:1: rule__Item__GroupsAssignment_4_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__Item__GroupsAssignment_4_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1216:1: ( ( ( RULE_ID ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1217:1: ( ( RULE_ID ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1217:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1218:1: ( RULE_ID )
            {
             before(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1219:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1220:1: RULE_ID
            {
             before(grammarAccess.getItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Item__GroupsAssignment_4_2_12408); 
             after(grammarAccess.getItemAccess().getGroupsGroupItemIDTerminalRuleCall_4_2_1_0_1()); 

            }

             after(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_2_1_0()); 

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
    // $ANTLR end rule__Item__GroupsAssignment_4_2_1


    // $ANTLR start rule__Item__BindingsAssignment_5_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1231:1: rule__Item__BindingsAssignment_5_1 : ( ruleBinding ) ;
    public final void rule__Item__BindingsAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1235:1: ( ( ruleBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1236:1: ( ruleBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1236:1: ( ruleBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1237:1: ruleBinding
            {
             before(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_ruleBinding_in_rule__Item__BindingsAssignment_5_12443);
            ruleBinding();
            _fsp--;

             after(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_1_0()); 

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
    // $ANTLR end rule__Item__BindingsAssignment_5_1


    // $ANTLR start rule__Item__BindingsAssignment_5_2_1
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1246:1: rule__Item__BindingsAssignment_5_2_1 : ( ruleBinding ) ;
    public final void rule__Item__BindingsAssignment_5_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1250:1: ( ( ruleBinding ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1251:1: ( ruleBinding )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1251:1: ( ruleBinding )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1252:1: ruleBinding
            {
             before(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_2_1_0()); 
            pushFollow(FOLLOW_ruleBinding_in_rule__Item__BindingsAssignment_5_2_12474);
            ruleBinding();
            _fsp--;

             after(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_2_1_0()); 

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
    // $ANTLR end rule__Item__BindingsAssignment_5_2_1


    // $ANTLR start rule__NormalItem__TypeAssignment
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1261:1: rule__NormalItem__TypeAssignment : ( ( rule__NormalItem__TypeAlternatives_0 ) ) ;
    public final void rule__NormalItem__TypeAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1265:1: ( ( ( rule__NormalItem__TypeAlternatives_0 ) ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1266:1: ( ( rule__NormalItem__TypeAlternatives_0 ) )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1266:1: ( ( rule__NormalItem__TypeAlternatives_0 ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1267:1: ( rule__NormalItem__TypeAlternatives_0 )
            {
             before(grammarAccess.getNormalItemAccess().getTypeAlternatives_0()); 
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1268:1: ( rule__NormalItem__TypeAlternatives_0 )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1268:2: rule__NormalItem__TypeAlternatives_0
            {
            pushFollow(FOLLOW_rule__NormalItem__TypeAlternatives_0_in_rule__NormalItem__TypeAssignment2505);
            rule__NormalItem__TypeAlternatives_0();
            _fsp--;


            }

             after(grammarAccess.getNormalItemAccess().getTypeAlternatives_0()); 

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
    // $ANTLR end rule__NormalItem__TypeAssignment


    // $ANTLR start rule__Binding__TypeAssignment_0
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1277:1: rule__Binding__TypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__Binding__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1281:1: ( ( RULE_ID ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1282:1: ( RULE_ID )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1282:1: ( RULE_ID )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1283:1: RULE_ID
            {
             before(grammarAccess.getBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Binding__TypeAssignment_02538); 
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
    // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1292:1: rule__Binding__ConfigurationAssignment_2 : ( RULE_STRING ) ;
    public final void rule__Binding__ConfigurationAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1296:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1297:1: ( RULE_STRING )
            {
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1297:1: ( RULE_STRING )
            // ../org.openhab.model.item.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalItems.g:1298:1: RULE_STRING
            {
             before(grammarAccess.getBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Binding__ConfigurationAssignment_22569); 
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


 

    public static final BitSet FOLLOW_ruleItemModel_in_entryRuleItemModel61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItemModel68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ItemModel__ItemsAssignment_in_ruleItemModel94 = new BitSet(new long[]{0x000000000101F812L});
    public static final BitSet FOLLOW_ruleItem_in_entryRuleItem122 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItem129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__0_in_ruleItem155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_entryRuleGroupItem182 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupItem189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__0_in_ruleGroupItem215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_entryRuleNormalItem242 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNormalItem249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__TypeAssignment_in_ruleNormalItem275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_entryRuleBinding302 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBinding309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__0_in_ruleBinding335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_rule__Item__Alternatives_0371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_rule__Item__Alternatives_0388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Item__IconAlternatives_3_1_0420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Item__IconAlternatives_3_1_0437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__NormalItem__TypeAlternatives_0470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__NormalItem__TypeAlternatives_0490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__NormalItem__TypeAlternatives_0510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__NormalItem__TypeAlternatives_0530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__NormalItem__TypeAlternatives_0550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__NormalItem__TypeAlternatives_0570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__NormalItem__TypeAlternatives_0589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__0__Impl_in_rule__Item__Group__0619 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Item__Group__1_in_rule__Item__Group__0622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Alternatives_0_in_rule__Item__Group__0__Impl649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__1__Impl_in_rule__Item__Group__1679 = new BitSet(new long[]{0x00000000004A0022L});
    public static final BitSet FOLLOW_rule__Item__Group__2_in_rule__Item__Group__1682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__NameAssignment_1_in_rule__Item__Group__1__Impl709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__2__Impl_in_rule__Item__Group__2739 = new BitSet(new long[]{0x00000000004A0002L});
    public static final BitSet FOLLOW_rule__Item__Group__3_in_rule__Item__Group__2742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__LabelAssignment_2_in_rule__Item__Group__2__Impl769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__3__Impl_in_rule__Item__Group__3800 = new BitSet(new long[]{0x0000000000480002L});
    public static final BitSet FOLLOW_rule__Item__Group__4_in_rule__Item__Group__3803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_3__0_in_rule__Item__Group__3__Impl830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__4__Impl_in_rule__Item__Group__4861 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_rule__Item__Group__5_in_rule__Item__Group__4864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4__0_in_rule__Item__Group__4__Impl891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group__5__Impl_in_rule__Item__Group__5922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5__0_in_rule__Item__Group__5__Impl949 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_rule__Item__Group_3__0__Impl_in_rule__Item__Group_3__0992 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Item__Group_3__1_in_rule__Item__Group_3__0995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Item__Group_3__0__Impl1023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_3__1__Impl_in_rule__Item__Group_3__11054 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_rule__Item__Group_3__2_in_rule__Item__Group_3__11057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__IconAssignment_3_1_in_rule__Item__Group_3__1__Impl1084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_3__2__Impl_in_rule__Item__Group_3__21114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Item__Group_3__2__Impl1142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4__0__Impl_in_rule__Item__Group_4__01179 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Item__Group_4__1_in_rule__Item__Group_4__01182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Item__Group_4__0__Impl1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4__1__Impl_in_rule__Item__Group_4__11241 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_rule__Item__Group_4__2_in_rule__Item__Group_4__11244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__GroupsAssignment_4_1_in_rule__Item__Group_4__1__Impl1271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4__2__Impl_in_rule__Item__Group_4__21301 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_rule__Item__Group_4__3_in_rule__Item__Group_4__21304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4_2__0_in_rule__Item__Group_4__2__Impl1331 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_rule__Item__Group_4__3__Impl_in_rule__Item__Group_4__31362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Item__Group_4__3__Impl1390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4_2__0__Impl_in_rule__Item__Group_4_2__01429 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Item__Group_4_2__1_in_rule__Item__Group_4_2__01432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Item__Group_4_2__0__Impl1460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_4_2__1__Impl_in_rule__Item__Group_4_2__11491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__GroupsAssignment_4_2_1_in_rule__Item__Group_4_2__1__Impl1518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5__0__Impl_in_rule__Item__Group_5__01552 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Item__Group_5__1_in_rule__Item__Group_5__01555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Item__Group_5__0__Impl1583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5__1__Impl_in_rule__Item__Group_5__11614 = new BitSet(new long[]{0x0000000000A00000L});
    public static final BitSet FOLLOW_rule__Item__Group_5__2_in_rule__Item__Group_5__11617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__BindingsAssignment_5_1_in_rule__Item__Group_5__1__Impl1644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5__2__Impl_in_rule__Item__Group_5__21674 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_rule__Item__Group_5__3_in_rule__Item__Group_5__21677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5_2__0_in_rule__Item__Group_5__2__Impl1704 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_rule__Item__Group_5__3__Impl_in_rule__Item__Group_5__31735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Item__Group_5__3__Impl1763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5_2__0__Impl_in_rule__Item__Group_5_2__01802 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Item__Group_5_2__1_in_rule__Item__Group_5_2__01805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Item__Group_5_2__0__Impl1833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__Group_5_2__1__Impl_in_rule__Item__Group_5_2__11864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__BindingsAssignment_5_2_1_in_rule__Item__Group_5_2__1__Impl1891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__0__Impl_in_rule__GroupItem__Group__01925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__1_in_rule__GroupItem__Group__01928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__GroupItem__Group__0__Impl1956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupItem__Group__1__Impl_in_rule__GroupItem__Group__11987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__0__Impl_in_rule__Binding__Group__02049 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__Binding__Group__1_in_rule__Binding__Group__02052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__TypeAssignment_0_in_rule__Binding__Group__0__Impl2079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__1__Impl_in_rule__Binding__Group__12109 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Binding__Group__2_in_rule__Binding__Group__12112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Binding__Group__1__Impl2140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__Group__2__Impl_in_rule__Binding__Group__22171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Binding__ConfigurationAssignment_2_in_rule__Binding__Group__2__Impl2198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleItem_in_rule__ItemModel__ItemsAssignment2239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Item__NameAssignment_12270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Item__LabelAssignment_22301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Item__IconAlternatives_3_1_0_in_rule__Item__IconAssignment_3_12332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Item__GroupsAssignment_4_12369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Item__GroupsAssignment_4_2_12408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_rule__Item__BindingsAssignment_5_12443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_rule__Item__BindingsAssignment_5_2_12474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__NormalItem__TypeAlternatives_0_in_rule__NormalItem__TypeAssignment2505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Binding__TypeAssignment_02538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Binding__ConfigurationAssignment_22569 = new BitSet(new long[]{0x0000000000000002L});

}