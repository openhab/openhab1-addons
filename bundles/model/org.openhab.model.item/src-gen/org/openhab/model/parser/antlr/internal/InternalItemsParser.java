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
import org.eclipse.emf.common.util.Enumerator;
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'<'", "'>'", "'('", "','", "')'", "'{'", "'}'", "'Group'", "':'", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'='", "'AND'", "'OR'", "'AVG'", "'MAX'", "'MIN'"
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
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:78:1: entryRuleItemModel returns [EObject current=null] : iv_ruleItemModel= ruleItemModel EOF ;
    public final EObject entryRuleItemModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleItemModel = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:79:2: (iv_ruleItemModel= ruleItemModel EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:80:2: iv_ruleItemModel= ruleItemModel EOF
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
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:87:1: ruleItemModel returns [EObject current=null] : ( (lv_items_0_0= ruleModelItem ) )* ;
    public final EObject ruleItemModel() throws RecognitionException {
        EObject current = null;

        EObject lv_items_0_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:92:6: ( ( (lv_items_0_0= ruleModelItem ) )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:93:1: ( (lv_items_0_0= ruleModelItem ) )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:93:1: ( (lv_items_0_0= ruleModelItem ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||LA1_0==18||(LA1_0>=20 && LA1_0<=25)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:94:1: (lv_items_0_0= ruleModelItem )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:94:1: (lv_items_0_0= ruleModelItem )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:95:3: lv_items_0_0= ruleModelItem
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleModelItem_in_ruleItemModel130);
            	    lv_items_0_0=ruleModelItem();
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
            	    	        		"ModelItem", 
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


    // $ANTLR start entryRuleModelItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:125:1: entryRuleModelItem returns [EObject current=null] : iv_ruleModelItem= ruleModelItem EOF ;
    public final EObject entryRuleModelItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:126:2: (iv_ruleModelItem= ruleModelItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:127:2: iv_ruleModelItem= ruleModelItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelItem_in_entryRuleModelItem166);
            iv_ruleModelItem=ruleModelItem();
            _fsp--;

             current =iv_ruleModelItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelItem176); 

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
    // $ANTLR end entryRuleModelItem


    // $ANTLR start ruleModelItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:134:1: ruleModelItem returns [EObject current=null] : ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )* ) ;
    public final EObject ruleModelItem() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        Token lv_label_3_0=null;
        Token lv_icon_5_1=null;
        Token lv_icon_5_2=null;
        EObject this_ModelNormalItem_0 = null;

        EObject this_ModelGroupItem_1 = null;

        EObject lv_bindings_13_0 = null;

        EObject lv_bindings_15_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:6: ( ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )* ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:1: ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )* )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:1: ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:2: (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:2: (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=20 && LA2_0<=25)) ) {
                alt2=1;
            }
            else if ( (LA2_0==18) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("140:2: (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem )", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:141:5: this_ModelNormalItem_0= ruleModelNormalItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getModelItemAccess().getModelNormalItemParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleModelNormalItem_in_ruleModelItem224);
                    this_ModelNormalItem_0=ruleModelNormalItem();
                    _fsp--;

                     
                            current = this_ModelNormalItem_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:151:5: this_ModelGroupItem_1= ruleModelGroupItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getModelItemAccess().getModelGroupItemParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleModelGroupItem_in_ruleModelItem251);
                    this_ModelGroupItem_1=ruleModelGroupItem();
                    _fsp--;

                     
                            current = this_ModelGroupItem_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:159:2: ( (lv_name_2_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:160:1: (lv_name_2_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:160:1: (lv_name_2_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:161:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem268); 

            			createLeafNode(grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
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

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:183:2: ( (lv_label_3_0= RULE_STRING ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_STRING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:184:1: (lv_label_3_0= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:184:1: (lv_label_3_0= RULE_STRING )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:185:3: lv_label_3_0= RULE_STRING
                    {
                    lv_label_3_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelItem290); 

                    			createLeafNode(grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
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

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:207:3: ( '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==11) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:207:5: '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) '>'
                    {
                    match(input,11,FOLLOW_11_in_ruleModelItem307); 

                            createLeafNode(grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:211:1: ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:212:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:212:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:213:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:213:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
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
                            new NoViableAltException("213:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:214:3: lv_icon_5_1= RULE_ID
                            {
                            lv_icon_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem326); 

                            			createLeafNode(grammarAccess.getModelItemAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
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
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:235:8: lv_icon_5_2= RULE_STRING
                            {
                            lv_icon_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelItem346); 

                            			createLeafNode(grammarAccess.getModelItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
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

                    match(input,12,FOLLOW_12_in_ruleModelItem364); 

                            createLeafNode(grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:263:3: ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==13) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:263:5: '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')'
                    {
                    match(input,13,FOLLOW_13_in_ruleModelItem377); 

                            createLeafNode(grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:267:1: ( ( RULE_ID ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:268:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:268:1: ( RULE_ID )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:269:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem395); 

                    		createLeafNode(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0(), "groups"); 
                    	

                    }


                    }

                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:281:2: ( ',' ( ( RULE_ID ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==14) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:281:4: ',' ( ( RULE_ID ) )
                    	    {
                    	    match(input,14,FOLLOW_14_in_ruleModelItem406); 

                    	            createLeafNode(grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:285:1: ( ( RULE_ID ) )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:286:1: ( RULE_ID )
                    	    {
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:286:1: ( RULE_ID )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:287:3: RULE_ID
                    	    {

                    	    			if (current==null) {
                    	    	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode, current);
                    	    	        }
                    	            
                    	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem424); 

                    	    		createLeafNode(grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0(), "groups"); 
                    	    	

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match(input,15,FOLLOW_15_in_ruleModelItem436); 

                            createLeafNode(grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:303:3: ( '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==16) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:303:5: '{' ( (lv_bindings_13_0= ruleModelBinding ) ) ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* '}'
            	    {
            	    match(input,16,FOLLOW_16_in_ruleModelItem449); 

            	            createLeafNode(grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0(), null); 
            	        
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:307:1: ( (lv_bindings_13_0= ruleModelBinding ) )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:308:1: (lv_bindings_13_0= ruleModelBinding )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:308:1: (lv_bindings_13_0= ruleModelBinding )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:309:3: lv_bindings_13_0= ruleModelBinding
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleModelBinding_in_ruleModelItem470);
            	    lv_bindings_13_0=ruleModelBinding();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        try {
            	    	       		add(
            	    	       			current, 
            	    	       			"bindings",
            	    	        		lv_bindings_13_0, 
            	    	        		"ModelBinding", 
            	    	        		currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }


            	    }

            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:331:2: ( ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )*
            	    loop8:
            	    do {
            	        int alt8=2;
            	        int LA8_0 = input.LA(1);

            	        if ( (LA8_0==14) ) {
            	            alt8=1;
            	        }


            	        switch (alt8) {
            	    	case 1 :
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:331:4: ',' ( (lv_bindings_15_0= ruleModelBinding ) )
            	    	    {
            	    	    match(input,14,FOLLOW_14_in_ruleModelItem481); 

            	    	            createLeafNode(grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0(), null); 
            	    	        
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:335:1: ( (lv_bindings_15_0= ruleModelBinding ) )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:336:1: (lv_bindings_15_0= ruleModelBinding )
            	    	    {
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:336:1: (lv_bindings_15_0= ruleModelBinding )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:337:3: lv_bindings_15_0= ruleModelBinding
            	    	    {
            	    	     
            	    	    	        currentNode=createCompositeNode(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0(), currentNode); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleModelBinding_in_ruleModelItem502);
            	    	    lv_bindings_15_0=ruleModelBinding();
            	    	    _fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = factory.create(grammarAccess.getModelItemRule().getType().getClassifier());
            	    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	    	        }
            	    	    	        try {
            	    	    	       		add(
            	    	    	       			current, 
            	    	    	       			"bindings",
            	    	    	        		lv_bindings_15_0, 
            	    	    	        		"ModelBinding", 
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

            	    match(input,17,FOLLOW_17_in_ruleModelItem514); 

            	            createLeafNode(grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3(), null); 
            	        

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
    // $ANTLR end ruleModelItem


    // $ANTLR start entryRuleModelGroupItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:371:1: entryRuleModelGroupItem returns [EObject current=null] : iv_ruleModelGroupItem= ruleModelGroupItem EOF ;
    public final EObject entryRuleModelGroupItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelGroupItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:372:2: (iv_ruleModelGroupItem= ruleModelGroupItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:373:2: iv_ruleModelGroupItem= ruleModelGroupItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelGroupItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem552);
            iv_ruleModelGroupItem=ruleModelGroupItem();
            _fsp--;

             current =iv_ruleModelGroupItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelGroupItem562); 

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
    // $ANTLR end entryRuleModelGroupItem


    // $ANTLR start ruleModelGroupItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:380:1: ruleModelGroupItem returns [EObject current=null] : ( 'Group' () ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )? ) ;
    public final EObject ruleModelGroupItem() throws RecognitionException {
        EObject current = null;

        Token lv_args_7_1=null;
        Token lv_args_7_2=null;
        Token lv_args_9_1=null;
        Token lv_args_9_2=null;
        AntlrDatatypeRuleToken lv_type_3_0 = null;

        Enumerator lv_function_5_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:6: ( ( 'Group' () ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )? ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:386:1: ( 'Group' () ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )? )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:386:1: ( 'Group' () ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )? )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:386:3: 'Group' () ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )?
            {
            match(input,18,FOLLOW_18_in_ruleModelGroupItem597); 

                    createLeafNode(grammarAccess.getModelGroupItemAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:390:1: ()
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:391:5: 
            {
             
                    temp=factory.create(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1().getType().getClassifier());
                    current = temp; 
                    temp = null;
                    CompositeNode newNode = createCompositeNode(grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1(), currentNode.getParent());
                newNode.getChildren().add(currentNode);
                moveLookaheadInfo(currentNode, newNode);
                currentNode = newNode; 
                    associateNodeWithAstElement(currentNode, current); 
                

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:401:2: ( ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )? )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==19) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:401:4: ':' ( (lv_type_3_0= ruleModelItemType ) ) ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )?
                    {
                    match(input,19,FOLLOW_19_in_ruleModelGroupItem617); 

                            createLeafNode(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:405:1: ( (lv_type_3_0= ruleModelItemType ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:406:1: (lv_type_3_0= ruleModelItemType )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:406:1: (lv_type_3_0= ruleModelItemType )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:407:3: lv_type_3_0= ruleModelItemType
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleModelItemType_in_ruleModelGroupItem638);
                    lv_type_3_0=ruleModelItemType();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"type",
                    	        		lv_type_3_0, 
                    	        		"ModelItemType", 
                    	        		currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }

                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:429:2: ( ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )? )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==19) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:429:4: ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )?
                            {
                            match(input,19,FOLLOW_19_in_ruleModelGroupItem649); 

                                    createLeafNode(grammarAccess.getModelGroupItemAccess().getColonKeyword_2_2_0(), null); 
                                
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:433:1: ( (lv_function_5_0= ruleModelGroupFunction ) )
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:434:1: (lv_function_5_0= ruleModelGroupFunction )
                            {
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:434:1: (lv_function_5_0= ruleModelGroupFunction )
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:435:3: lv_function_5_0= ruleModelGroupFunction
                            {
                             
                            	        currentNode=createCompositeNode(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_2_1_0(), currentNode); 
                            	    
                            pushFollow(FOLLOW_ruleModelGroupFunction_in_ruleModelGroupItem670);
                            lv_function_5_0=ruleModelGroupFunction();
                            _fsp--;


                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode.getParent(), current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"function",
                            	        		lv_function_5_0, 
                            	        		"ModelGroupFunction", 
                            	        		currentNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	        currentNode = currentNode.getParent();
                            	    

                            }


                            }

                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:457:2: ( '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')' )?
                            int alt13=2;
                            int LA13_0 = input.LA(1);

                            if ( (LA13_0==13) ) {
                                alt13=1;
                            }
                            switch (alt13) {
                                case 1 :
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:457:4: '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* ')'
                                    {
                                    match(input,13,FOLLOW_13_in_ruleModelGroupItem681); 

                                            createLeafNode(grammarAccess.getModelGroupItemAccess().getLeftParenthesisKeyword_2_2_2_0(), null); 
                                        
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:461:1: ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) )
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:462:1: ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) )
                                    {
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:462:1: ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) )
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:463:1: (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING )
                                    {
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:463:1: (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING )
                                    int alt10=2;
                                    int LA10_0 = input.LA(1);

                                    if ( (LA10_0==RULE_ID) ) {
                                        alt10=1;
                                    }
                                    else if ( (LA10_0==RULE_STRING) ) {
                                        alt10=2;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("463:1: (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING )", 10, 0, input);

                                        throw nvae;
                                    }
                                    switch (alt10) {
                                        case 1 :
                                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:464:3: lv_args_7_1= RULE_ID
                                            {
                                            lv_args_7_1=(Token)input.LT(1);
                                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelGroupItem700); 

                                            			createLeafNode(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_1_0_0(), "args"); 
                                            		

                                            	        if (current==null) {
                                            	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                                            	            associateNodeWithAstElement(currentNode, current);
                                            	        }
                                            	        try {
                                            	       		add(
                                            	       			current, 
                                            	       			"args",
                                            	        		lv_args_7_1, 
                                            	        		"ID", 
                                            	        		lastConsumedNode);
                                            	        } catch (ValueConverterException vce) {
                                            				handleValueConverterException(vce);
                                            	        }
                                            	    

                                            }
                                            break;
                                        case 2 :
                                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:485:8: lv_args_7_2= RULE_STRING
                                            {
                                            lv_args_7_2=(Token)input.LT(1);
                                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelGroupItem720); 

                                            			createLeafNode(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_1_0_1(), "args"); 
                                            		

                                            	        if (current==null) {
                                            	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                                            	            associateNodeWithAstElement(currentNode, current);
                                            	        }
                                            	        try {
                                            	       		add(
                                            	       			current, 
                                            	       			"args",
                                            	        		lv_args_7_2, 
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

                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:509:2: ( ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )*
                                    loop12:
                                    do {
                                        int alt12=2;
                                        int LA12_0 = input.LA(1);

                                        if ( (LA12_0==14) ) {
                                            alt12=1;
                                        }


                                        switch (alt12) {
                                    	case 1 :
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:509:4: ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) )
                                    	    {
                                    	    match(input,14,FOLLOW_14_in_ruleModelGroupItem739); 

                                    	            createLeafNode(grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_2_2_2_0(), null); 
                                    	        
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:513:1: ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) )
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:514:1: ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) )
                                    	    {
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:514:1: ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) )
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:515:1: (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING )
                                    	    {
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:515:1: (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING )
                                    	    int alt11=2;
                                    	    int LA11_0 = input.LA(1);

                                    	    if ( (LA11_0==RULE_ID) ) {
                                    	        alt11=1;
                                    	    }
                                    	    else if ( (LA11_0==RULE_STRING) ) {
                                    	        alt11=2;
                                    	    }
                                    	    else {
                                    	        NoViableAltException nvae =
                                    	            new NoViableAltException("515:1: (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING )", 11, 0, input);

                                    	        throw nvae;
                                    	    }
                                    	    switch (alt11) {
                                    	        case 1 :
                                    	            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:516:3: lv_args_9_1= RULE_ID
                                    	            {
                                    	            lv_args_9_1=(Token)input.LT(1);
                                    	            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelGroupItem758); 

                                    	            			createLeafNode(grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_2_1_0_0(), "args"); 
                                    	            		

                                    	            	        if (current==null) {
                                    	            	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                                    	            	            associateNodeWithAstElement(currentNode, current);
                                    	            	        }
                                    	            	        try {
                                    	            	       		add(
                                    	            	       			current, 
                                    	            	       			"args",
                                    	            	        		lv_args_9_1, 
                                    	            	        		"ID", 
                                    	            	        		lastConsumedNode);
                                    	            	        } catch (ValueConverterException vce) {
                                    	            				handleValueConverterException(vce);
                                    	            	        }
                                    	            	    

                                    	            }
                                    	            break;
                                    	        case 2 :
                                    	            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:537:8: lv_args_9_2= RULE_STRING
                                    	            {
                                    	            lv_args_9_2=(Token)input.LT(1);
                                    	            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelGroupItem778); 

                                    	            			createLeafNode(grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_2_1_0_1(), "args"); 
                                    	            		

                                    	            	        if (current==null) {
                                    	            	            current = factory.create(grammarAccess.getModelGroupItemRule().getType().getClassifier());
                                    	            	            associateNodeWithAstElement(currentNode, current);
                                    	            	        }
                                    	            	        try {
                                    	            	       		add(
                                    	            	       			current, 
                                    	            	       			"args",
                                    	            	        		lv_args_9_2, 
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


                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop12;
                                        }
                                    } while (true);

                                    match(input,15,FOLLOW_15_in_ruleModelGroupItem798); 

                                            createLeafNode(grammarAccess.getModelGroupItemAccess().getRightParenthesisKeyword_2_2_2_3(), null); 
                                        

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }
                    break;

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
    // $ANTLR end ruleModelGroupItem


    // $ANTLR start entryRuleModelNormalItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:573:1: entryRuleModelNormalItem returns [EObject current=null] : iv_ruleModelNormalItem= ruleModelNormalItem EOF ;
    public final EObject entryRuleModelNormalItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelNormalItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:574:2: (iv_ruleModelNormalItem= ruleModelNormalItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:575:2: iv_ruleModelNormalItem= ruleModelNormalItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelNormalItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem840);
            iv_ruleModelNormalItem=ruleModelNormalItem();
            _fsp--;

             current =iv_ruleModelNormalItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelNormalItem850); 

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
    // $ANTLR end entryRuleModelNormalItem


    // $ANTLR start ruleModelNormalItem
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:582:1: ruleModelNormalItem returns [EObject current=null] : ( (lv_type_0_0= ruleModelItemType ) ) ;
    public final EObject ruleModelNormalItem() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_type_0_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:587:6: ( ( (lv_type_0_0= ruleModelItemType ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:588:1: ( (lv_type_0_0= ruleModelItemType ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:588:1: ( (lv_type_0_0= ruleModelItemType ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:589:1: (lv_type_0_0= ruleModelItemType )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:589:1: (lv_type_0_0= ruleModelItemType )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:590:3: lv_type_0_0= ruleModelItemType
            {
             
            	        currentNode=createCompositeNode(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0(), currentNode); 
            	    
            pushFollow(FOLLOW_ruleModelItemType_in_ruleModelNormalItem895);
            lv_type_0_0=ruleModelItemType();
            _fsp--;


            	        if (current==null) {
            	            current = factory.create(grammarAccess.getModelNormalItemRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode.getParent(), current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"type",
            	        		lv_type_0_0, 
            	        		"ModelItemType", 
            	        		currentNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	        currentNode = currentNode.getParent();
            	    

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
    // $ANTLR end ruleModelNormalItem


    // $ANTLR start entryRuleModelItemType
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:620:1: entryRuleModelItemType returns [String current=null] : iv_ruleModelItemType= ruleModelItemType EOF ;
    public final String entryRuleModelItemType() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleModelItemType = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:621:2: (iv_ruleModelItemType= ruleModelItemType EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:622:2: iv_ruleModelItemType= ruleModelItemType EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelItemTypeRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelItemType_in_entryRuleModelItemType931);
            iv_ruleModelItemType=ruleModelItemType();
            _fsp--;

             current =iv_ruleModelItemType.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelItemType942); 

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
    // $ANTLR end entryRuleModelItemType


    // $ANTLR start ruleModelItemType
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:629:1: ruleModelItemType returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | this_ID_6= RULE_ID ) ;
    public final AntlrDatatypeRuleToken ruleModelItemType() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_6=null;

         setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:634:6: ( (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | this_ID_6= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:635:1: (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | this_ID_6= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:635:1: (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | this_ID_6= RULE_ID )
            int alt16=7;
            switch ( input.LA(1) ) {
            case 20:
                {
                alt16=1;
                }
                break;
            case 21:
                {
                alt16=2;
                }
                break;
            case 22:
                {
                alt16=3;
                }
                break;
            case 23:
                {
                alt16=4;
                }
                break;
            case 24:
                {
                alt16=5;
                }
                break;
            case 25:
                {
                alt16=6;
                }
                break;
            case RULE_ID:
                {
                alt16=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("635:1: (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | this_ID_6= RULE_ID )", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:636:2: kw= 'Switch'
                    {
                    kw=(Token)input.LT(1);
                    match(input,20,FOLLOW_20_in_ruleModelItemType980); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getSwitchKeyword_0(), null); 
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:643:2: kw= 'Rollershutter'
                    {
                    kw=(Token)input.LT(1);
                    match(input,21,FOLLOW_21_in_ruleModelItemType999); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getRollershutterKeyword_1(), null); 
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:650:2: kw= 'Number'
                    {
                    kw=(Token)input.LT(1);
                    match(input,22,FOLLOW_22_in_ruleModelItemType1018); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getNumberKeyword_2(), null); 
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:657:2: kw= 'String'
                    {
                    kw=(Token)input.LT(1);
                    match(input,23,FOLLOW_23_in_ruleModelItemType1037); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getStringKeyword_3(), null); 
                        

                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:664:2: kw= 'Dimmer'
                    {
                    kw=(Token)input.LT(1);
                    match(input,24,FOLLOW_24_in_ruleModelItemType1056); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getDimmerKeyword_4(), null); 
                        

                    }
                    break;
                case 6 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:671:2: kw= 'Contact'
                    {
                    kw=(Token)input.LT(1);
                    match(input,25,FOLLOW_25_in_ruleModelItemType1075); 

                            current.merge(kw);
                            createLeafNode(grammarAccess.getModelItemTypeAccess().getContactKeyword_5(), null); 
                        

                    }
                    break;
                case 7 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:677:10: this_ID_6= RULE_ID
                    {
                    this_ID_6=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItemType1096); 

                    		current.merge(this_ID_6);
                        
                     
                        createLeafNode(grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_6(), null); 
                        

                    }
                    break;

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
    // $ANTLR end ruleModelItemType


    // $ANTLR start entryRuleModelBinding
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:692:1: entryRuleModelBinding returns [EObject current=null] : iv_ruleModelBinding= ruleModelBinding EOF ;
    public final EObject entryRuleModelBinding() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelBinding = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:693:2: (iv_ruleModelBinding= ruleModelBinding EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:694:2: iv_ruleModelBinding= ruleModelBinding EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelBindingRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelBinding_in_entryRuleModelBinding1141);
            iv_ruleModelBinding=ruleModelBinding();
            _fsp--;

             current =iv_ruleModelBinding; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelBinding1151); 

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
    // $ANTLR end entryRuleModelBinding


    // $ANTLR start ruleModelBinding
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:701:1: ruleModelBinding returns [EObject current=null] : ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) ;
    public final EObject ruleModelBinding() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        Token lv_configuration_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:706:6: ( ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:707:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:707:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:707:2: ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:707:2: ( (lv_type_0_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:708:1: (lv_type_0_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:708:1: (lv_type_0_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:709:3: lv_type_0_0= RULE_ID
            {
            lv_type_0_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelBinding1193); 

            			createLeafNode(grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0(), "type"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getModelBindingRule().getType().getClassifier());
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

            match(input,26,FOLLOW_26_in_ruleModelBinding1208); 

                    createLeafNode(grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:735:1: ( (lv_configuration_2_0= RULE_STRING ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:736:1: (lv_configuration_2_0= RULE_STRING )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:736:1: (lv_configuration_2_0= RULE_STRING )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:737:3: lv_configuration_2_0= RULE_STRING
            {
            lv_configuration_2_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelBinding1225); 

            			createLeafNode(grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0(), "configuration"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getModelBindingRule().getType().getClassifier());
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
    // $ANTLR end ruleModelBinding


    // $ANTLR start ruleModelGroupFunction
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:767:1: ruleModelGroupFunction returns [Enumerator current=null] : ( ( 'AND' ) | ( 'OR' ) | ( 'AVG' ) | ( 'MAX' ) | ( 'MIN' ) ) ;
    public final Enumerator ruleModelGroupFunction() throws RecognitionException {
        Enumerator current = null;

         setCurrentLookahead(); resetLookahead(); 
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:771:6: ( ( ( 'AND' ) | ( 'OR' ) | ( 'AVG' ) | ( 'MAX' ) | ( 'MIN' ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:772:1: ( ( 'AND' ) | ( 'OR' ) | ( 'AVG' ) | ( 'MAX' ) | ( 'MIN' ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:772:1: ( ( 'AND' ) | ( 'OR' ) | ( 'AVG' ) | ( 'MAX' ) | ( 'MIN' ) )
            int alt17=5;
            switch ( input.LA(1) ) {
            case 27:
                {
                alt17=1;
                }
                break;
            case 28:
                {
                alt17=2;
                }
                break;
            case 29:
                {
                alt17=3;
                }
                break;
            case 30:
                {
                alt17=4;
                }
                break;
            case 31:
                {
                alt17=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("772:1: ( ( 'AND' ) | ( 'OR' ) | ( 'AVG' ) | ( 'MAX' ) | ( 'MIN' ) )", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:772:2: ( 'AND' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:772:2: ( 'AND' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:772:4: 'AND'
                    {
                    match(input,27,FOLLOW_27_in_ruleModelGroupFunction1278); 

                            current = grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                            createLeafNode(grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0(), null); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:778:6: ( 'OR' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:778:6: ( 'OR' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:778:8: 'OR'
                    {
                    match(input,28,FOLLOW_28_in_ruleModelGroupFunction1293); 

                            current = grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                            createLeafNode(grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1(), null); 
                        

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:784:6: ( 'AVG' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:784:6: ( 'AVG' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:784:8: 'AVG'
                    {
                    match(input,29,FOLLOW_29_in_ruleModelGroupFunction1308); 

                            current = grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                            createLeafNode(grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2(), null); 
                        

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:790:6: ( 'MAX' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:790:6: ( 'MAX' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:790:8: 'MAX'
                    {
                    match(input,30,FOLLOW_30_in_ruleModelGroupFunction1323); 

                            current = grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                            createLeafNode(grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3(), null); 
                        

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:796:6: ( 'MIN' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:796:6: ( 'MIN' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:796:8: 'MIN'
                    {
                    match(input,31,FOLLOW_31_in_ruleModelGroupFunction1338); 

                            current = grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                            createLeafNode(grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4(), null); 
                        

                    }


                    }
                    break;

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
    // $ANTLR end ruleModelGroupFunction


 

    public static final BitSet FOLLOW_ruleItemModel_in_entryRuleItemModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItemModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItem_in_ruleItemModel130 = new BitSet(new long[]{0x0000000003F40012L});
    public static final BitSet FOLLOW_ruleModelItem_in_entryRuleModelItem166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItem176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_ruleModelItem224 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_ruleModelItem251 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem268 = new BitSet(new long[]{0x0000000000012822L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelItem290 = new BitSet(new long[]{0x0000000000012802L});
    public static final BitSet FOLLOW_11_in_ruleModelItem307 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem326 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelItem346 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleModelItem364 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_13_in_ruleModelItem377 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem395 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_14_in_ruleModelItem406 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem424 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_15_in_ruleModelItem436 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_ruleModelItem449 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelBinding_in_ruleModelItem470 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_14_in_ruleModelItem481 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelBinding_in_ruleModelItem502 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_17_in_ruleModelItem514 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem552 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelGroupItem562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleModelGroupItem597 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleModelGroupItem617 = new BitSet(new long[]{0x0000000003F00010L});
    public static final BitSet FOLLOW_ruleModelItemType_in_ruleModelGroupItem638 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleModelGroupItem649 = new BitSet(new long[]{0x00000000F8000000L});
    public static final BitSet FOLLOW_ruleModelGroupFunction_in_ruleModelGroupItem670 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleModelGroupItem681 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelGroupItem700 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelGroupItem720 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_14_in_ruleModelGroupItem739 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelGroupItem758 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelGroupItem778 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_15_in_ruleModelGroupItem798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem840 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelNormalItem850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_ruleModelNormalItem895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_entryRuleModelItemType931 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItemType942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleModelItemType980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleModelItemType999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleModelItemType1018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleModelItemType1037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_ruleModelItemType1056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleModelItemType1075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItemType1096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_entryRuleModelBinding1141 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelBinding1151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelBinding1193 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleModelBinding1208 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelBinding1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_ruleModelGroupFunction1278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_ruleModelGroupFunction1293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleModelGroupFunction1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleModelGroupFunction1323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleModelGroupFunction1338 = new BitSet(new long[]{0x0000000000000002L});

}