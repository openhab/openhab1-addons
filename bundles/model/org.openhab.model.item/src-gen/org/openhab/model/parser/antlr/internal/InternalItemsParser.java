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

package org.openhab.model.parser.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.xtext.parsetree.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.openhab.model.services.ItemsGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalItemsParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'<'", "'>'", "'('", "','", "')'", "'{'", "'}'", "'Group'", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'='"
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
    public String getGrammarFileName() { return "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g"; }



     	private ItemsGrammarAccess grammarAccess;
     	
        public InternalItemsParser(TokenStream input, IAstFactory factory, ItemsGrammarAccess grammarAccess) {
            this(input);
            this.factory = factory;
            registerRules(grammarAccess.getGrammar());
            this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected InputStream getTokenFile() {
        	ClassLoader classLoader = getClass().getClassLoader();
        	return classLoader.getResourceAsStream("org/openhab/model/parser/antlr/internal/InternalItems.tokens");
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "ItemModel";	
       	}
       	
       	@Override
       	protected ItemsGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start entryRuleItemModel
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:77:1: entryRuleItemModel returns [EObject current=null] : iv_ruleItemModel= ruleItemModel EOF ;
    public final EObject entryRuleItemModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleItemModel = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:78:2: (iv_ruleItemModel= ruleItemModel EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:79:2: iv_ruleItemModel= ruleItemModel EOF
            {
             currentNode = createCompositeNode(grammarAccess.getItemModelRule(), currentNode); 
            pushFollow(FOLLOW_ruleItemModel_in_entryRuleItemModel75);
            iv_ruleItemModel=ruleItemModel();
            _fsp--;

             current =iv_ruleItemModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleItemModel85); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleItemModel


    // $ANTLR start ruleItemModel
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:86:1: ruleItemModel returns [EObject current=null] : ( (lv_items_0_0= ruleItem ) )* ;
    public final EObject ruleItemModel() throws RecognitionException {
        EObject current = null;

        EObject lv_items_0_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:91:6: ( ( (lv_items_0_0= ruleItem ) )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:92:1: ( (lv_items_0_0= ruleItem ) )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:92:1: ( (lv_items_0_0= ruleItem ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||(LA1_0>=18 && LA1_0<=24)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:93:1: (lv_items_0_0= ruleItem )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:93:1: (lv_items_0_0= ruleItem )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:94:3: lv_items_0_0= ruleItem
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getItemModelAccess().getItemsItemParserRuleCall_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleItem_in_ruleItemModel130);
            	    lv_items_0_0=ruleItem();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getItemModelRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        try {
            	    	       		add(
            	    	       			current, 
            	    	       			"items",
            	    	        		lv_items_0_0, 
            	    	        		"Item", 
            	    	        		currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleItemModel


    // $ANTLR start entryRuleItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:124:1: entryRuleItem returns [EObject current=null] : iv_ruleItem= ruleItem EOF ;
    public final EObject entryRuleItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:125:2: (iv_ruleItem= ruleItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:126:2: iv_ruleItem= ruleItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleItem_in_entryRuleItem166);
            iv_ruleItem=ruleItem();
            _fsp--;

             current =iv_ruleItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleItem176); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleItem


    // $ANTLR start ruleItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:133:1: ruleItem returns [EObject current=null] : ( (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )* ) ;
    public final EObject ruleItem() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        Token lv_label_3_0=null;
        Token lv_icon_5_1=null;
        Token lv_icon_5_2=null;
        EObject this_NormalItem_0 = null;

        EObject this_GroupItem_1 = null;

        EObject lv_bindings_13_0 = null;

        EObject lv_bindings_15_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:138:6: ( ( (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )* ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:1: ( (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )* )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:1: ( (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:2: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:2: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=19 && LA2_0<=24)) ) {
                alt2=1;
            }
            else if ( (LA2_0==18) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("139:2: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem )", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:5: this_NormalItem_0= ruleNormalItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleNormalItem_in_ruleItem224);
                    this_NormalItem_0=ruleNormalItem();
                    _fsp--;

                     
                            current = this_NormalItem_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:150:5: this_GroupItem_1= ruleGroupItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getItemAccess().getGroupItemParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroupItem_in_ruleItem251);
                    this_GroupItem_1=ruleGroupItem();
                    _fsp--;

                     
                            current = this_GroupItem_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:158:2: ( (lv_name_2_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:159:1: (lv_name_2_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:159:1: (lv_name_2_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:160:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleItem268); 

            			createLeafNode(grammarAccess.getItemAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"name",
            	        		lv_name_2_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:182:2: ( (lv_label_3_0= RULE_STRING ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_STRING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:183:1: (lv_label_3_0= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:183:1: (lv_label_3_0= RULE_STRING )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:184:3: lv_label_3_0= RULE_STRING
                    {
                    lv_label_3_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleItem290); 

                    			createLeafNode(grammarAccess.getItemAccess().getLabelSTRINGTerminalRuleCall_2_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"label",
                    	        		lv_label_3_0, 
                    	        		"STRING", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:206:3: ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==11) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:206:5: '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>'
                    {
                    match(input,11,FOLLOW_11_in_ruleItem307); 

                            createLeafNode(grammarAccess.getItemAccess().getLessThanSignKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:210:1: ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:211:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:211:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:212:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:212:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
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
                            new NoViableAltException("212:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:213:3: lv_icon_5_1= RULE_ID
                            {
                            lv_icon_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleItem326); 

                            			createLeafNode(grammarAccess.getItemAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_5_1, 
                            	        		"ID", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:234:8: lv_icon_5_2= RULE_STRING
                            {
                            lv_icon_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleItem346); 

                            			createLeafNode(grammarAccess.getItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_5_2, 
                            	        		"STRING", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;

                    }


                    }


                    }

                    match(input,12,FOLLOW_12_in_ruleItem364); 

                            createLeafNode(grammarAccess.getItemAccess().getGreaterThanSignKeyword_3_2(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:262:3: ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==13) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:262:5: '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')'
                    {
                    match(input,13,FOLLOW_13_in_ruleItem377); 

                            createLeafNode(grammarAccess.getItemAccess().getLeftParenthesisKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:266:1: ( ( RULE_ID ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:267:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:267:1: ( RULE_ID )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:268:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleItem395); 

                    		createLeafNode(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_1_0(), "groups"); 
                    	

                    }


                    }

                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:280:2: ( ',' ( ( RULE_ID ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==14) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:280:4: ',' ( ( RULE_ID ) )
                    	    {
                    	    match(input,14,FOLLOW_14_in_ruleItem406); 

                    	            createLeafNode(grammarAccess.getItemAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:284:1: ( ( RULE_ID ) )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:285:1: ( RULE_ID )
                    	    {
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:285:1: ( RULE_ID )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:286:3: RULE_ID
                    	    {

                    	    			if (current==null) {
                    	    	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode, current);
                    	    	        }
                    	            
                    	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleItem424); 

                    	    		createLeafNode(grammarAccess.getItemAccess().getGroupsGroupItemCrossReference_4_2_1_0(), "groups"); 
                    	    	

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match(input,15,FOLLOW_15_in_ruleItem436); 

                            createLeafNode(grammarAccess.getItemAccess().getRightParenthesisKeyword_4_3(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:302:3: ( '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==16) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:302:5: '{' ( (lv_bindings_13_0= ruleBinding ) ) ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )* '}'
            	    {
            	    match(input,16,FOLLOW_16_in_ruleItem449); 

            	            createLeafNode(grammarAccess.getItemAccess().getLeftCurlyBracketKeyword_5_0(), null); 
            	        
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:306:1: ( (lv_bindings_13_0= ruleBinding ) )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:307:1: (lv_bindings_13_0= ruleBinding )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:307:1: (lv_bindings_13_0= ruleBinding )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:308:3: lv_bindings_13_0= ruleBinding
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_1_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleBinding_in_ruleItem470);
            	    lv_bindings_13_0=ruleBinding();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        try {
            	    	       		add(
            	    	       			current, 
            	    	       			"bindings",
            	    	        		lv_bindings_13_0, 
            	    	        		"Binding", 
            	    	        		currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }


            	    }

            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:330:2: ( ',' ( (lv_bindings_15_0= ruleBinding ) ) )*
            	    loop8:
            	    do {
            	        int alt8=2;
            	        int LA8_0 = input.LA(1);

            	        if ( (LA8_0==14) ) {
            	            alt8=1;
            	        }


            	        switch (alt8) {
            	    	case 1 :
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:330:4: ',' ( (lv_bindings_15_0= ruleBinding ) )
            	    	    {
            	    	    match(input,14,FOLLOW_14_in_ruleItem481); 

            	    	            createLeafNode(grammarAccess.getItemAccess().getCommaKeyword_5_2_0(), null); 
            	    	        
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:334:1: ( (lv_bindings_15_0= ruleBinding ) )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:335:1: (lv_bindings_15_0= ruleBinding )
            	    	    {
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:335:1: (lv_bindings_15_0= ruleBinding )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:336:3: lv_bindings_15_0= ruleBinding
            	    	    {
            	    	     
            	    	    	        currentNode=createCompositeNode(grammarAccess.getItemAccess().getBindingsBindingParserRuleCall_5_2_1_0(), currentNode); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleBinding_in_ruleItem502);
            	    	    lv_bindings_15_0=ruleBinding();
            	    	    _fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = factory.create(grammarAccess.getItemRule().getType().getClassifier());
            	    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	    	        }
            	    	    	        try {
            	    	    	       		add(
            	    	    	       			current, 
            	    	    	       			"bindings",
            	    	    	        		lv_bindings_15_0, 
            	    	    	        		"Binding", 
            	    	    	        		currentNode);
            	    	    	        } catch (ValueConverterException vce) {
            	    	    				handleValueConverterException(vce);
            	    	    	        }
            	    	    	        currentNode = currentNode.getParent();
            	    	    	    

            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop8;
            	        }
            	    } while (true);

            	    match(input,17,FOLLOW_17_in_ruleItem514); 

            	            createLeafNode(grammarAccess.getItemAccess().getRightCurlyBracketKeyword_5_3(), null); 
            	        

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleItem


    // $ANTLR start entryRuleGroupItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:370:1: entryRuleGroupItem returns [EObject current=null] : iv_ruleGroupItem= ruleGroupItem EOF ;
    public final EObject entryRuleGroupItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:371:2: (iv_ruleGroupItem= ruleGroupItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:372:2: iv_ruleGroupItem= ruleGroupItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroupItem_in_entryRuleGroupItem552);
            iv_ruleGroupItem=ruleGroupItem();
            _fsp--;

             current =iv_ruleGroupItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupItem562); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleGroupItem


    // $ANTLR start ruleGroupItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:379:1: ruleGroupItem returns [EObject current=null] : ( 'Group' () ) ;
    public final EObject ruleGroupItem() throws RecognitionException {
        EObject current = null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:384:6: ( ( 'Group' () ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:1: ( 'Group' () )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:1: ( 'Group' () )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:3: 'Group' ()
            {
            match(input,18,FOLLOW_18_in_ruleGroupItem597); 

                    createLeafNode(grammarAccess.getGroupItemAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:389:1: ()
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:390:5: 
            {
             
                    temp=factory.create(grammarAccess.getGroupItemAccess().getGroupAction_1().getType().getClassifier());
                    current = temp; 
                    temp = null;
                    CompositeNode newNode = createCompositeNode(grammarAccess.getGroupItemAccess().getGroupAction_1(), currentNode.getParent());
                newNode.getChildren().add(currentNode);
                moveLookaheadInfo(currentNode, newNode);
                currentNode = newNode; 
                    associateNodeWithAstElement(currentNode, current); 
                

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleGroupItem


    // $ANTLR start entryRuleNormalItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:408:1: entryRuleNormalItem returns [EObject current=null] : iv_ruleNormalItem= ruleNormalItem EOF ;
    public final EObject entryRuleNormalItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNormalItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:409:2: (iv_ruleNormalItem= ruleNormalItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:410:2: iv_ruleNormalItem= ruleNormalItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getNormalItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleNormalItem_in_entryRuleNormalItem642);
            iv_ruleNormalItem=ruleNormalItem();
            _fsp--;

             current =iv_ruleNormalItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNormalItem652); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleNormalItem


    // $ANTLR start ruleNormalItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:417:1: ruleNormalItem returns [EObject current=null] : ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ;
    public final EObject ruleNormalItem() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_1=null;
        Token lv_type_0_2=null;
        Token lv_type_0_3=null;
        Token lv_type_0_4=null;
        Token lv_type_0_5=null;
        Token lv_type_0_6=null;
        Token lv_type_0_7=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:422:6: ( ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:423:1: ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:423:1: ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:424:1: ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:424:1: ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:425:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:425:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )
            int alt10=7;
            switch ( input.LA(1) ) {
            case 19:
                {
                alt10=1;
                }
                break;
            case 20:
                {
                alt10=2;
                }
                break;
            case 21:
                {
                alt10=3;
                }
                break;
            case 22:
                {
                alt10=4;
                }
                break;
            case 23:
                {
                alt10=5;
                }
                break;
            case 24:
                {
                alt10=6;
                }
                break;
            case RULE_ID:
                {
                alt10=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("425:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollershutter' | lv_type_0_3= 'Number' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:426:3: lv_type_0_1= 'Switch'
                    {
                    lv_type_0_1=(Token)input.LT(1);
                    match(input,19,FOLLOW_19_in_ruleNormalItem696); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_1, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:444:8: lv_type_0_2= 'Rollershutter'
                    {
                    lv_type_0_2=(Token)input.LT(1);
                    match(input,20,FOLLOW_20_in_ruleNormalItem725); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeRollershutterKeyword_0_1(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_2, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:462:8: lv_type_0_3= 'Number'
                    {
                    lv_type_0_3=(Token)input.LT(1);
                    match(input,21,FOLLOW_21_in_ruleNormalItem754); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeNumberKeyword_0_2(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_3, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:480:8: lv_type_0_4= 'String'
                    {
                    lv_type_0_4=(Token)input.LT(1);
                    match(input,22,FOLLOW_22_in_ruleNormalItem783); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_3(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_4, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:498:8: lv_type_0_5= 'Dimmer'
                    {
                    lv_type_0_5=(Token)input.LT(1);
                    match(input,23,FOLLOW_23_in_ruleNormalItem812); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_4(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_5, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 6 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:516:8: lv_type_0_6= 'Contact'
                    {
                    lv_type_0_6=(Token)input.LT(1);
                    match(input,24,FOLLOW_24_in_ruleNormalItem841); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_5(), "type"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "type", lv_type_0_6, null, lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 7 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:534:8: lv_type_0_7= RULE_ID
                    {
                    lv_type_0_7=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem869); 

                    			createLeafNode(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_6(), "type"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"type",
                    	        		lv_type_0_7, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;

            }


            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleNormalItem


    // $ANTLR start entryRuleBinding
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:566:1: entryRuleBinding returns [EObject current=null] : iv_ruleBinding= ruleBinding EOF ;
    public final EObject entryRuleBinding() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBinding = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:567:2: (iv_ruleBinding= ruleBinding EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:568:2: iv_ruleBinding= ruleBinding EOF
            {
             currentNode = createCompositeNode(grammarAccess.getBindingRule(), currentNode); 
            pushFollow(FOLLOW_ruleBinding_in_entryRuleBinding912);
            iv_ruleBinding=ruleBinding();
            _fsp--;

             current =iv_ruleBinding; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBinding922); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleBinding


    // $ANTLR start ruleBinding
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:575:1: ruleBinding returns [EObject current=null] : ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) ;
    public final EObject ruleBinding() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        Token lv_configuration_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:580:6: ( ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:581:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:581:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:581:2: ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:581:2: ( (lv_type_0_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:582:1: (lv_type_0_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:582:1: (lv_type_0_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:583:3: lv_type_0_0= RULE_ID
            {
            lv_type_0_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleBinding964); 

            			createLeafNode(grammarAccess.getBindingAccess().getTypeIDTerminalRuleCall_0_0(), "type"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBindingRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"type",
            	        		lv_type_0_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }

            match(input,25,FOLLOW_25_in_ruleBinding979); 

                    createLeafNode(grammarAccess.getBindingAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:609:1: ( (lv_configuration_2_0= RULE_STRING ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:610:1: (lv_configuration_2_0= RULE_STRING )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:610:1: (lv_configuration_2_0= RULE_STRING )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:611:3: lv_configuration_2_0= RULE_STRING
            {
            lv_configuration_2_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleBinding996); 

            			createLeafNode(grammarAccess.getBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0(), "configuration"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBindingRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"configuration",
            	        		lv_configuration_2_0, 
            	        		"STRING", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleBinding


 

    public static final BitSet FOLLOW_ruleItemModel_in_entryRuleItemModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItemModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleItem_in_ruleItemModel130 = new BitSet(new long[]{0x0000000001FC0012L});
    public static final BitSet FOLLOW_ruleItem_in_entryRuleItem166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItem176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_ruleItem224 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleGroupItem_in_ruleItem251 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleItem268 = new BitSet(new long[]{0x0000000000012822L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleItem290 = new BitSet(new long[]{0x0000000000012802L});
    public static final BitSet FOLLOW_11_in_ruleItem307 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleItem326 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleItem346 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleItem364 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_13_in_ruleItem377 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleItem395 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_14_in_ruleItem406 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleItem424 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_15_in_ruleItem436 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_ruleItem449 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleBinding_in_ruleItem470 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_14_in_ruleItem481 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleBinding_in_ruleItem502 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_17_in_ruleItem514 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_entryRuleGroupItem552 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupItem562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleGroupItem597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_entryRuleNormalItem642 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNormalItem652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleNormalItem696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleNormalItem725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleNormalItem754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleNormalItem783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleNormalItem812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_ruleNormalItem841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBinding_in_entryRuleBinding912 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBinding922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleBinding964 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleBinding979 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleBinding996 = new BitSet(new long[]{0x0000000000000002L});

}