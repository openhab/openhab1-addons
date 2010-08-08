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
import org.openhab.model.services.ConfigGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalConfigParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Group'", "'<'", "'>'", "'('", "','", "')'", "'Switch'", "'Rollerblind'", "'Measurement'", "'String'", "'Dimmer'", "'Contact'", "'{'", "'}'", "'='"
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
    public String getGrammarFileName() { return "../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g"; }



     	private ConfigGrammarAccess grammarAccess;
     	
        public InternalConfigParser(TokenStream input, IAstFactory factory, ConfigGrammarAccess grammarAccess) {
            this(input);
            this.factory = factory;
            registerRules(grammarAccess.getGrammar());
            this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected InputStream getTokenFile() {
        	ClassLoader classLoader = getClass().getClassLoader();
        	return classLoader.getResourceAsStream("org/openhab/model/parser/antlr/internal/InternalConfig.tokens");
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Model";	
       	}
       	
       	@Override
       	protected ConfigGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start entryRuleModel
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:77:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:78:2: (iv_ruleModel= ruleModel EOF )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:79:2: iv_ruleModel= ruleModel EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelRule(), currentNode); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel75);
            iv_ruleModel=ruleModel();
            _fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel85); 

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
    // $ANTLR end entryRuleModel


    // $ANTLR start ruleModel
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:86:1: ruleModel returns [EObject current=null] : ( (lv_items_0_0= ruleItem ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_items_0_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:91:6: ( ( (lv_items_0_0= ruleItem ) )* )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:92:1: ( (lv_items_0_0= ruleItem ) )*
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:92:1: ( (lv_items_0_0= ruleItem ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||LA1_0==11||(LA1_0>=17 && LA1_0<=22)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:93:1: (lv_items_0_0= ruleItem )
            	    {
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:93:1: (lv_items_0_0= ruleItem )
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:94:3: lv_items_0_0= ruleItem
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getModelAccess().getItemsItemParserRuleCall_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleItem_in_ruleModel130);
            	    lv_items_0_0=ruleItem();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getModelRule().getType().getClassifier());
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
    // $ANTLR end ruleModel


    // $ANTLR start entryRuleItem
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:124:1: entryRuleItem returns [EObject current=null] : iv_ruleItem= ruleItem EOF ;
    public final EObject entryRuleItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleItem = null;


        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:125:2: (iv_ruleItem= ruleItem EOF )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:126:2: iv_ruleItem= ruleItem EOF
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
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:133:1: ruleItem returns [EObject current=null] : (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) ;
    public final EObject ruleItem() throws RecognitionException {
        EObject current = null;

        EObject this_NormalItem_0 = null;

        EObject this_GroupItem_1 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:138:6: ( (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:139:1: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:139:1: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=17 && LA2_0<=22)) ) {
                alt2=1;
            }
            else if ( (LA2_0==11) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("139:1: (this_NormalItem_0= ruleNormalItem | this_GroupItem_1= ruleGroupItem )", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:140:5: this_NormalItem_0= ruleNormalItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getItemAccess().getNormalItemParserRuleCall_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleNormalItem_in_ruleItem223);
                    this_NormalItem_0=ruleNormalItem();
                    _fsp--;

                     
                            current = this_NormalItem_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:150:5: this_GroupItem_1= ruleGroupItem
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getItemAccess().getGroupItemParserRuleCall_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroupItem_in_ruleItem250);
                    this_GroupItem_1=ruleGroupItem();
                    _fsp--;

                     
                            current = this_GroupItem_1; 
                            currentNode = currentNode.getParent();
                        

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
    // $ANTLR end ruleItem


    // $ANTLR start entryRuleGroupItem
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:166:1: entryRuleGroupItem returns [EObject current=null] : iv_ruleGroupItem= ruleGroupItem EOF ;
    public final EObject entryRuleGroupItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupItem = null;


        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:167:2: (iv_ruleGroupItem= ruleGroupItem EOF )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:168:2: iv_ruleGroupItem= ruleGroupItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroupItem_in_entryRuleGroupItem285);
            iv_ruleGroupItem=ruleGroupItem();
            _fsp--;

             current =iv_ruleGroupItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupItem295); 

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
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:175:1: ruleGroupItem returns [EObject current=null] : ( 'Group' ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ) ;
    public final EObject ruleGroupItem() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token lv_label_2_0=null;
        Token lv_icon_4_1=null;
        Token lv_icon_4_2=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:180:6: ( ( 'Group' ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:181:1: ( 'Group' ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:181:1: ( 'Group' ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:181:3: 'Group' ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )?
            {
            match(input,11,FOLLOW_11_in_ruleGroupItem330); 

                    createLeafNode(grammarAccess.getGroupItemAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:185:1: ( (lv_name_1_0= RULE_ID ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:186:1: (lv_name_1_0= RULE_ID )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:186:1: (lv_name_1_0= RULE_ID )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:187:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroupItem347); 

            			createLeafNode(grammarAccess.getGroupItemAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"name",
            	        		lv_name_1_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:209:2: ( (lv_label_2_0= RULE_STRING ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_STRING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:210:1: (lv_label_2_0= RULE_STRING )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:210:1: (lv_label_2_0= RULE_STRING )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:211:3: lv_label_2_0= RULE_STRING
                    {
                    lv_label_2_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleGroupItem369); 

                    			createLeafNode(grammarAccess.getGroupItemAccess().getLabelSTRINGTerminalRuleCall_2_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"label",
                    	        		lv_label_2_0, 
                    	        		"STRING", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;

            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:233:3: ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==12) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:233:5: '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>'
                    {
                    match(input,12,FOLLOW_12_in_ruleGroupItem386); 

                            createLeafNode(grammarAccess.getGroupItemAccess().getLessThanSignKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:237:1: ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:238:1: ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:238:1: ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:239:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:239:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )
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
                            new NoViableAltException("239:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:240:3: lv_icon_4_1= RULE_ID
                            {
                            lv_icon_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroupItem405); 

                            			createLeafNode(grammarAccess.getGroupItemAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_4_1, 
                            	        		"ID", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:261:8: lv_icon_4_2= RULE_STRING
                            {
                            lv_icon_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleGroupItem425); 

                            			createLeafNode(grammarAccess.getGroupItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_4_2, 
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

                    match(input,13,FOLLOW_13_in_ruleGroupItem443); 

                            createLeafNode(grammarAccess.getGroupItemAccess().getGreaterThanSignKeyword_3_2(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:289:3: ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==14) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:289:5: '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')'
                    {
                    match(input,14,FOLLOW_14_in_ruleGroupItem456); 

                            createLeafNode(grammarAccess.getGroupItemAccess().getLeftParenthesisKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:293:1: ( ( RULE_ID ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:294:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:294:1: ( RULE_ID )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:295:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroupItem474); 

                    		createLeafNode(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_1_0(), "groups"); 
                    	

                    }


                    }

                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:307:2: ( ',' ( ( RULE_ID ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==15) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:307:4: ',' ( ( RULE_ID ) )
                    	    {
                    	    match(input,15,FOLLOW_15_in_ruleGroupItem485); 

                    	            createLeafNode(grammarAccess.getGroupItemAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:311:1: ( ( RULE_ID ) )
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:312:1: ( RULE_ID )
                    	    {
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:312:1: ( RULE_ID )
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:313:3: RULE_ID
                    	    {

                    	    			if (current==null) {
                    	    	            current = factory.create(grammarAccess.getGroupItemRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode, current);
                    	    	        }
                    	            
                    	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroupItem503); 

                    	    		createLeafNode(grammarAccess.getGroupItemAccess().getGroupsGroupItemCrossReference_4_2_1_0(), "groups"); 
                    	    	

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match(input,16,FOLLOW_16_in_ruleGroupItem515); 

                            createLeafNode(grammarAccess.getGroupItemAccess().getRightParenthesisKeyword_4_3(), null); 
                        

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
    // $ANTLR end ruleGroupItem


    // $ANTLR start entryRuleNormalItem
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:337:1: entryRuleNormalItem returns [EObject current=null] : iv_ruleNormalItem= ruleNormalItem EOF ;
    public final EObject entryRuleNormalItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNormalItem = null;


        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:338:2: (iv_ruleNormalItem= ruleNormalItem EOF )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:339:2: iv_ruleNormalItem= ruleNormalItem EOF
            {
             currentNode = createCompositeNode(grammarAccess.getNormalItemRule(), currentNode); 
            pushFollow(FOLLOW_ruleNormalItem_in_entryRuleNormalItem553);
            iv_ruleNormalItem=ruleNormalItem();
            _fsp--;

             current =iv_ruleNormalItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNormalItem563); 

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
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:346:1: ruleNormalItem returns [EObject current=null] : ( ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )* ) ;
    public final EObject ruleNormalItem() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_1=null;
        Token lv_type_0_2=null;
        Token lv_type_0_3=null;
        Token lv_type_0_4=null;
        Token lv_type_0_5=null;
        Token lv_type_0_6=null;
        Token lv_type_0_7=null;
        Token lv_name_1_0=null;
        Token lv_label_2_0=null;
        Token lv_icon_4_1=null;
        Token lv_icon_4_2=null;
        EObject lv_bindings_12_0 = null;

        EObject lv_bindings_14_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:351:6: ( ( ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )* ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:352:1: ( ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )* )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:352:1: ( ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )* )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:352:2: ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) ) ( (lv_name_1_0= RULE_ID ) ) ( (lv_label_2_0= RULE_STRING ) )? ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )? ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )? ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )*
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:352:2: ( ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:353:1: ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:353:1: ( (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:354:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:354:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )
            int alt8=7;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt8=1;
                }
                break;
            case 18:
                {
                alt8=2;
                }
                break;
            case 19:
                {
                alt8=3;
                }
                break;
            case 20:
                {
                alt8=4;
                }
                break;
            case 21:
                {
                alt8=5;
                }
                break;
            case 22:
                {
                alt8=6;
                }
                break;
            case RULE_ID:
                {
                alt8=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("354:1: (lv_type_0_1= 'Switch' | lv_type_0_2= 'Rollerblind' | lv_type_0_3= 'Measurement' | lv_type_0_4= 'String' | lv_type_0_5= 'Dimmer' | lv_type_0_6= 'Contact' | lv_type_0_7= RULE_ID )", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:355:3: lv_type_0_1= 'Switch'
                    {
                    lv_type_0_1=(Token)input.LT(1);
                    match(input,17,FOLLOW_17_in_ruleNormalItem608); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeSwitchKeyword_0_0_0(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:373:8: lv_type_0_2= 'Rollerblind'
                    {
                    lv_type_0_2=(Token)input.LT(1);
                    match(input,18,FOLLOW_18_in_ruleNormalItem637); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeRollerblindKeyword_0_0_1(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:391:8: lv_type_0_3= 'Measurement'
                    {
                    lv_type_0_3=(Token)input.LT(1);
                    match(input,19,FOLLOW_19_in_ruleNormalItem666); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeMeasurementKeyword_0_0_2(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:409:8: lv_type_0_4= 'String'
                    {
                    lv_type_0_4=(Token)input.LT(1);
                    match(input,20,FOLLOW_20_in_ruleNormalItem695); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeStringKeyword_0_0_3(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:427:8: lv_type_0_5= 'Dimmer'
                    {
                    lv_type_0_5=(Token)input.LT(1);
                    match(input,21,FOLLOW_21_in_ruleNormalItem724); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeDimmerKeyword_0_0_4(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:445:8: lv_type_0_6= 'Contact'
                    {
                    lv_type_0_6=(Token)input.LT(1);
                    match(input,22,FOLLOW_22_in_ruleNormalItem753); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getTypeContactKeyword_0_0_5(), "type"); 
                        

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
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:463:8: lv_type_0_7= RULE_ID
                    {
                    lv_type_0_7=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem781); 

                    			createLeafNode(grammarAccess.getNormalItemAccess().getTypeIDTerminalRuleCall_0_0_6(), "type"); 
                    		

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

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:487:2: ( (lv_name_1_0= RULE_ID ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:488:1: (lv_name_1_0= RULE_ID )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:488:1: (lv_name_1_0= RULE_ID )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:489:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem806); 

            			createLeafNode(grammarAccess.getNormalItemAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"name",
            	        		lv_name_1_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:511:2: ( (lv_label_2_0= RULE_STRING ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==RULE_STRING) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:512:1: (lv_label_2_0= RULE_STRING )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:512:1: (lv_label_2_0= RULE_STRING )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:513:3: lv_label_2_0= RULE_STRING
                    {
                    lv_label_2_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleNormalItem828); 

                    			createLeafNode(grammarAccess.getNormalItemAccess().getLabelSTRINGTerminalRuleCall_2_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"label",
                    	        		lv_label_2_0, 
                    	        		"STRING", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;

            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:535:3: ( '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==12) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:535:5: '<' ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) ) '>'
                    {
                    match(input,12,FOLLOW_12_in_ruleNormalItem845); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getLessThanSignKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:539:1: ( ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:540:1: ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:540:1: ( (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:541:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:541:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )
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
                            new NoViableAltException("541:1: (lv_icon_4_1= RULE_ID | lv_icon_4_2= RULE_STRING )", 10, 0, input);

                        throw nvae;
                    }
                    switch (alt10) {
                        case 1 :
                            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:542:3: lv_icon_4_1= RULE_ID
                            {
                            lv_icon_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem864); 

                            			createLeafNode(grammarAccess.getNormalItemAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_4_1, 
                            	        		"ID", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:563:8: lv_icon_4_2= RULE_STRING
                            {
                            lv_icon_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleNormalItem884); 

                            			createLeafNode(grammarAccess.getNormalItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_4_2, 
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

                    match(input,13,FOLLOW_13_in_ruleNormalItem902); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getGreaterThanSignKeyword_3_2(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:591:3: ( '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==14) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:591:5: '(' ( ( RULE_ID ) ) ( ',' ( ( RULE_ID ) ) )* ')'
                    {
                    match(input,14,FOLLOW_14_in_ruleNormalItem915); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getLeftParenthesisKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:595:1: ( ( RULE_ID ) )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:596:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:596:1: ( RULE_ID )
                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:597:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem933); 

                    		createLeafNode(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_1_0(), "groups"); 
                    	

                    }


                    }

                    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:609:2: ( ',' ( ( RULE_ID ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==15) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:609:4: ',' ( ( RULE_ID ) )
                    	    {
                    	    match(input,15,FOLLOW_15_in_ruleNormalItem944); 

                    	            createLeafNode(grammarAccess.getNormalItemAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:613:1: ( ( RULE_ID ) )
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:614:1: ( RULE_ID )
                    	    {
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:614:1: ( RULE_ID )
                    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:615:3: RULE_ID
                    	    {

                    	    			if (current==null) {
                    	    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode, current);
                    	    	        }
                    	            
                    	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNormalItem962); 

                    	    		createLeafNode(grammarAccess.getNormalItemAccess().getGroupsGroupItemCrossReference_4_2_1_0(), "groups"); 
                    	    	

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,16,FOLLOW_16_in_ruleNormalItem974); 

                            createLeafNode(grammarAccess.getNormalItemAccess().getRightParenthesisKeyword_4_3(), null); 
                        

                    }
                    break;

            }

            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:631:3: ( '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}' )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==23) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:631:5: '{' ( (lv_bindings_12_0= ruleBinding ) ) ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )* '}'
            	    {
            	    match(input,23,FOLLOW_23_in_ruleNormalItem987); 

            	            createLeafNode(grammarAccess.getNormalItemAccess().getLeftCurlyBracketKeyword_5_0(), null); 
            	        
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:635:1: ( (lv_bindings_12_0= ruleBinding ) )
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:636:1: (lv_bindings_12_0= ruleBinding )
            	    {
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:636:1: (lv_bindings_12_0= ruleBinding )
            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:637:3: lv_bindings_12_0= ruleBinding
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_1_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleBinding_in_ruleNormalItem1008);
            	    lv_bindings_12_0=ruleBinding();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        try {
            	    	       		add(
            	    	       			current, 
            	    	       			"bindings",
            	    	        		lv_bindings_12_0, 
            	    	        		"Binding", 
            	    	        		currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }


            	    }

            	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:659:2: ( ',' ( (lv_bindings_14_0= ruleBinding ) ) )*
            	    loop14:
            	    do {
            	        int alt14=2;
            	        int LA14_0 = input.LA(1);

            	        if ( (LA14_0==15) ) {
            	            alt14=1;
            	        }


            	        switch (alt14) {
            	    	case 1 :
            	    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:659:4: ',' ( (lv_bindings_14_0= ruleBinding ) )
            	    	    {
            	    	    match(input,15,FOLLOW_15_in_ruleNormalItem1019); 

            	    	            createLeafNode(grammarAccess.getNormalItemAccess().getCommaKeyword_5_2_0(), null); 
            	    	        
            	    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:663:1: ( (lv_bindings_14_0= ruleBinding ) )
            	    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:664:1: (lv_bindings_14_0= ruleBinding )
            	    	    {
            	    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:664:1: (lv_bindings_14_0= ruleBinding )
            	    	    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:665:3: lv_bindings_14_0= ruleBinding
            	    	    {
            	    	     
            	    	    	        currentNode=createCompositeNode(grammarAccess.getNormalItemAccess().getBindingsBindingParserRuleCall_5_2_1_0(), currentNode); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleBinding_in_ruleNormalItem1040);
            	    	    lv_bindings_14_0=ruleBinding();
            	    	    _fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = factory.create(grammarAccess.getNormalItemRule().getType().getClassifier());
            	    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	    	        }
            	    	    	        try {
            	    	    	       		add(
            	    	    	       			current, 
            	    	    	       			"bindings",
            	    	    	        		lv_bindings_14_0, 
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
            	    	    break loop14;
            	        }
            	    } while (true);

            	    match(input,24,FOLLOW_24_in_ruleNormalItem1052); 

            	            createLeafNode(grammarAccess.getNormalItemAccess().getRightCurlyBracketKeyword_5_3(), null); 
            	        

            	    }
            	    break;

            	default :
            	    break loop15;
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
    // $ANTLR end ruleNormalItem


    // $ANTLR start entryRuleBinding
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:699:1: entryRuleBinding returns [EObject current=null] : iv_ruleBinding= ruleBinding EOF ;
    public final EObject entryRuleBinding() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBinding = null;


        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:700:2: (iv_ruleBinding= ruleBinding EOF )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:701:2: iv_ruleBinding= ruleBinding EOF
            {
             currentNode = createCompositeNode(grammarAccess.getBindingRule(), currentNode); 
            pushFollow(FOLLOW_ruleBinding_in_entryRuleBinding1090);
            iv_ruleBinding=ruleBinding();
            _fsp--;

             current =iv_ruleBinding; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBinding1100); 

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
    // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:708:1: ruleBinding returns [EObject current=null] : ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) ;
    public final EObject ruleBinding() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        Token lv_configuration_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:713:6: ( ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:714:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:714:1: ( ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:714:2: ( (lv_type_0_0= RULE_ID ) ) '=' ( (lv_configuration_2_0= RULE_STRING ) )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:714:2: ( (lv_type_0_0= RULE_ID ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:715:1: (lv_type_0_0= RULE_ID )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:715:1: (lv_type_0_0= RULE_ID )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:716:3: lv_type_0_0= RULE_ID
            {
            lv_type_0_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleBinding1142); 

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

            match(input,25,FOLLOW_25_in_ruleBinding1157); 

                    createLeafNode(grammarAccess.getBindingAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:742:1: ( (lv_configuration_2_0= RULE_STRING ) )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:743:1: (lv_configuration_2_0= RULE_STRING )
            {
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:743:1: (lv_configuration_2_0= RULE_STRING )
            // ../org.openhab.model.config/src-gen/org/openhab/model/parser/antlr/internal/InternalConfig.g:744:3: lv_configuration_2_0= RULE_STRING
            {
            lv_configuration_2_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleBinding1174); 

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


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleItem_in_ruleModel130 = new BitSet(new long[]{0x00000000007E0812L});
    public static final BitSet FOLLOW_ruleItem_in_entryRuleItem166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItem176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_ruleItem223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_ruleItem250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupItem_in_entryRuleGroupItem285 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupItem295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleGroupItem330 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroupItem347 = new BitSet(new long[]{0x0000000000005022L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleGroupItem369 = new BitSet(new long[]{0x0000000000005002L});
    public static final BitSet FOLLOW_12_in_ruleGroupItem386 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroupItem405 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleGroupItem425 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleGroupItem443 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleGroupItem456 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroupItem474 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_15_in_ruleGroupItem485 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroupItem503 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_16_in_ruleGroupItem515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNormalItem_in_entryRuleNormalItem553 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNormalItem563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleNormalItem608 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_18_in_ruleNormalItem637 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_19_in_ruleNormalItem666 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_20_in_ruleNormalItem695 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_21_in_ruleNormalItem724 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_22_in_ruleNormalItem753 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem781 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem806 = new BitSet(new long[]{0x0000000000805022L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleNormalItem828 = new BitSet(new long[]{0x0000000000805002L});
    public static final BitSet FOLLOW_12_in_ruleNormalItem845 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem864 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleNormalItem884 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleNormalItem902 = new BitSet(new long[]{0x0000000000804002L});
    public static final BitSet FOLLOW_14_in_ruleNormalItem915 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem933 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_15_in_ruleNormalItem944 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNormalItem962 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_16_in_ruleNormalItem974 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleNormalItem987 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleBinding_in_ruleNormalItem1008 = new BitSet(new long[]{0x0000000001008000L});
    public static final BitSet FOLLOW_15_in_ruleNormalItem1019 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleBinding_in_ruleNormalItem1040 = new BitSet(new long[]{0x0000000001008000L});
    public static final BitSet FOLLOW_24_in_ruleNormalItem1052 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_ruleBinding_in_entryRuleBinding1090 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBinding1100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleBinding1142 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleBinding1157 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleBinding1174 = new BitSet(new long[]{0x0000000000000002L});

}