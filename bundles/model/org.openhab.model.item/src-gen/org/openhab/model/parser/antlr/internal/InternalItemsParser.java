package org.openhab.model.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.openhab.model.services.ItemsGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalItemsParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'<'", "'>'", "'('", "','", "')'", "'{'", "'}'", "'Group'", "':'", "'Switch'", "'Rollershutter'", "'Number'", "'String'", "'Dimmer'", "'Contact'", "'DateTime'", "'='", "'AND'", "'OR'", "'AVG'", "'MAX'", "'MIN'"
    };
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
    public static final int RULE_SL_COMMENT=8;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__30=30;
    public static final int T__19=19;
    public static final int T__31=31;
    public static final int RULE_STRING=5;
    public static final int T__32=32;
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


        public InternalItemsParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalItemsParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalItemsParser.tokenNames; }
    public String getGrammarFileName() { return "../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g"; }



     	private ItemsGrammarAccess grammarAccess;
     	
        public InternalItemsParser(TokenStream input, ItemsGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "ItemModel";	
       	}
       	
       	@Override
       	protected ItemsGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleItemModel"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:68:1: entryRuleItemModel returns [EObject current=null] : iv_ruleItemModel= ruleItemModel EOF ;
    public final EObject entryRuleItemModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleItemModel = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:69:2: (iv_ruleItemModel= ruleItemModel EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:70:2: iv_ruleItemModel= ruleItemModel EOF
            {
             newCompositeNode(grammarAccess.getItemModelRule()); 
            pushFollow(FOLLOW_ruleItemModel_in_entryRuleItemModel75);
            iv_ruleItemModel=ruleItemModel();

            state._fsp--;

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
    // $ANTLR end "entryRuleItemModel"


    // $ANTLR start "ruleItemModel"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:77:1: ruleItemModel returns [EObject current=null] : ( (lv_items_0_0= ruleModelItem ) )* ;
    public final EObject ruleItemModel() throws RecognitionException {
        EObject current = null;

        EObject lv_items_0_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:80:28: ( ( (lv_items_0_0= ruleModelItem ) )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:81:1: ( (lv_items_0_0= ruleModelItem ) )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:81:1: ( (lv_items_0_0= ruleModelItem ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID||LA1_0==18||(LA1_0>=20 && LA1_0<=26)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:82:1: (lv_items_0_0= ruleModelItem )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:82:1: (lv_items_0_0= ruleModelItem )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:83:3: lv_items_0_0= ruleModelItem
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getItemModelAccess().getItemsModelItemParserRuleCall_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleModelItem_in_ruleItemModel130);
            	    lv_items_0_0=ruleModelItem();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getItemModelRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"items",
            	            		lv_items_0_0, 
            	            		"ModelItem");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleItemModel"


    // $ANTLR start "entryRuleModelItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:107:1: entryRuleModelItem returns [EObject current=null] : iv_ruleModelItem= ruleModelItem EOF ;
    public final EObject entryRuleModelItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:108:2: (iv_ruleModelItem= ruleModelItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:109:2: iv_ruleModelItem= ruleModelItem EOF
            {
             newCompositeNode(grammarAccess.getModelItemRule()); 
            pushFollow(FOLLOW_ruleModelItem_in_entryRuleModelItem166);
            iv_ruleModelItem=ruleModelItem();

            state._fsp--;

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
    // $ANTLR end "entryRuleModelItem"


    // $ANTLR start "ruleModelItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:116:1: ruleModelItem returns [EObject current=null] : ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )? (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )? (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )* ) ;
    public final EObject ruleModelItem() throws RecognitionException {
        EObject current = null;

        Token lv_name_2_0=null;
        Token lv_label_3_0=null;
        Token otherlv_4=null;
        Token lv_icon_5_1=null;
        Token lv_icon_5_2=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        EObject this_ModelNormalItem_0 = null;

        EObject this_ModelGroupItem_1 = null;

        EObject lv_bindings_13_0 = null;

        EObject lv_bindings_15_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:119:28: ( ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )? (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )? (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )* ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:120:1: ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )? (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )? (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )* )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:120:1: ( (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )? (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )? (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )* )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:120:2: (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem ) ( (lv_name_2_0= RULE_ID ) ) ( (lv_label_3_0= RULE_STRING ) )? (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )? (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )? (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )*
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:120:2: (this_ModelNormalItem_0= ruleModelNormalItem | this_ModelGroupItem_1= ruleModelGroupItem )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID||(LA2_0>=20 && LA2_0<=26)) ) {
                alt2=1;
            }
            else if ( (LA2_0==18) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:121:5: this_ModelNormalItem_0= ruleModelNormalItem
                    {
                     
                            newCompositeNode(grammarAccess.getModelItemAccess().getModelNormalItemParserRuleCall_0_0()); 
                        
                    pushFollow(FOLLOW_ruleModelNormalItem_in_ruleModelItem224);
                    this_ModelNormalItem_0=ruleModelNormalItem();

                    state._fsp--;

                     
                            current = this_ModelNormalItem_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:131:5: this_ModelGroupItem_1= ruleModelGroupItem
                    {
                     
                            newCompositeNode(grammarAccess.getModelItemAccess().getModelGroupItemParserRuleCall_0_1()); 
                        
                    pushFollow(FOLLOW_ruleModelGroupItem_in_ruleModelItem251);
                    this_ModelGroupItem_1=ruleModelGroupItem();

                    state._fsp--;

                     
                            current = this_ModelGroupItem_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:139:2: ( (lv_name_2_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:1: (lv_name_2_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:140:1: (lv_name_2_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:141:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem268); 

            			newLeafNode(lv_name_2_0, grammarAccess.getModelItemAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getModelItemRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_2_0, 
                    		"ID");
            	    

            }


            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:157:2: ( (lv_label_3_0= RULE_STRING ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_STRING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:158:1: (lv_label_3_0= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:158:1: (lv_label_3_0= RULE_STRING )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:159:3: lv_label_3_0= RULE_STRING
                    {
                    lv_label_3_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelItem290); 

                    			newLeafNode(lv_label_3_0, grammarAccess.getModelItemAccess().getLabelSTRINGTerminalRuleCall_2_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getModelItemRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"label",
                            		lv_label_3_0, 
                            		"STRING");
                    	    

                    }


                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:175:3: (otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==11) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:175:5: otherlv_4= '<' ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) ) otherlv_6= '>'
                    {
                    otherlv_4=(Token)match(input,11,FOLLOW_11_in_ruleModelItem309); 

                        	newLeafNode(otherlv_4, grammarAccess.getModelItemAccess().getLessThanSignKeyword_3_0());
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:179:1: ( ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:180:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:180:1: ( (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:181:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:181:1: (lv_icon_5_1= RULE_ID | lv_icon_5_2= RULE_STRING )
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
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:182:3: lv_icon_5_1= RULE_ID
                            {
                            lv_icon_5_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem328); 

                            			newLeafNode(lv_icon_5_1, grammarAccess.getModelItemAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getModelItemRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_5_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:197:8: lv_icon_5_2= RULE_STRING
                            {
                            lv_icon_5_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelItem348); 

                            			newLeafNode(lv_icon_5_2, grammarAccess.getModelItemAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getModelItemRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_5_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }

                    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleModelItem368); 

                        	newLeafNode(otherlv_6, grammarAccess.getModelItemAccess().getGreaterThanSignKeyword_3_2());
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:219:3: (otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==13) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:219:5: otherlv_7= '(' ( (otherlv_8= RULE_ID ) ) (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )* otherlv_11= ')'
                    {
                    otherlv_7=(Token)match(input,13,FOLLOW_13_in_ruleModelItem383); 

                        	newLeafNode(otherlv_7, grammarAccess.getModelItemAccess().getLeftParenthesisKeyword_4_0());
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:223:1: ( (otherlv_8= RULE_ID ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:224:1: (otherlv_8= RULE_ID )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:224:1: (otherlv_8= RULE_ID )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:225:3: otherlv_8= RULE_ID
                    {

                    			if (current==null) {
                    	            current = createModelElement(grammarAccess.getModelItemRule());
                    	        }
                            
                    otherlv_8=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem403); 

                    		newLeafNode(otherlv_8, grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_1_0()); 
                    	

                    }


                    }

                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:236:2: (otherlv_9= ',' ( (otherlv_10= RULE_ID ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==14) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:236:4: otherlv_9= ',' ( (otherlv_10= RULE_ID ) )
                    	    {
                    	    otherlv_9=(Token)match(input,14,FOLLOW_14_in_ruleModelItem416); 

                    	        	newLeafNode(otherlv_9, grammarAccess.getModelItemAccess().getCommaKeyword_4_2_0());
                    	        
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:240:1: ( (otherlv_10= RULE_ID ) )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:241:1: (otherlv_10= RULE_ID )
                    	    {
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:241:1: (otherlv_10= RULE_ID )
                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:242:3: otherlv_10= RULE_ID
                    	    {

                    	    			if (current==null) {
                    	    	            current = createModelElement(grammarAccess.getModelItemRule());
                    	    	        }
                    	            
                    	    otherlv_10=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItem436); 

                    	    		newLeafNode(otherlv_10, grammarAccess.getModelItemAccess().getGroupsModelGroupItemCrossReference_4_2_1_0()); 
                    	    	

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    otherlv_11=(Token)match(input,15,FOLLOW_15_in_ruleModelItem450); 

                        	newLeafNode(otherlv_11, grammarAccess.getModelItemAccess().getRightParenthesisKeyword_4_3());
                        

                    }
                    break;

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:257:3: (otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==16) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:257:5: otherlv_12= '{' ( (lv_bindings_13_0= ruleModelBinding ) ) (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )* otherlv_16= '}'
            	    {
            	    otherlv_12=(Token)match(input,16,FOLLOW_16_in_ruleModelItem465); 

            	        	newLeafNode(otherlv_12, grammarAccess.getModelItemAccess().getLeftCurlyBracketKeyword_5_0());
            	        
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:261:1: ( (lv_bindings_13_0= ruleModelBinding ) )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:262:1: (lv_bindings_13_0= ruleModelBinding )
            	    {
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:262:1: (lv_bindings_13_0= ruleModelBinding )
            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:263:3: lv_bindings_13_0= ruleModelBinding
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleModelBinding_in_ruleModelItem486);
            	    lv_bindings_13_0=ruleModelBinding();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getModelItemRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"bindings",
            	            		lv_bindings_13_0, 
            	            		"ModelBinding");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }

            	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:279:2: (otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) ) )*
            	    loop8:
            	    do {
            	        int alt8=2;
            	        int LA8_0 = input.LA(1);

            	        if ( (LA8_0==14) ) {
            	            alt8=1;
            	        }


            	        switch (alt8) {
            	    	case 1 :
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:279:4: otherlv_14= ',' ( (lv_bindings_15_0= ruleModelBinding ) )
            	    	    {
            	    	    otherlv_14=(Token)match(input,14,FOLLOW_14_in_ruleModelItem499); 

            	    	        	newLeafNode(otherlv_14, grammarAccess.getModelItemAccess().getCommaKeyword_5_2_0());
            	    	        
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:283:1: ( (lv_bindings_15_0= ruleModelBinding ) )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:284:1: (lv_bindings_15_0= ruleModelBinding )
            	    	    {
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:284:1: (lv_bindings_15_0= ruleModelBinding )
            	    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:285:3: lv_bindings_15_0= ruleModelBinding
            	    	    {
            	    	     
            	    	    	        newCompositeNode(grammarAccess.getModelItemAccess().getBindingsModelBindingParserRuleCall_5_2_1_0()); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleModelBinding_in_ruleModelItem520);
            	    	    lv_bindings_15_0=ruleModelBinding();

            	    	    state._fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = createModelElementForParent(grammarAccess.getModelItemRule());
            	    	    	        }
            	    	           		add(
            	    	           			current, 
            	    	           			"bindings",
            	    	            		lv_bindings_15_0, 
            	    	            		"ModelBinding");
            	    	    	        afterParserOrEnumRuleCall();
            	    	    	    

            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop8;
            	        }
            	    } while (true);

            	    otherlv_16=(Token)match(input,17,FOLLOW_17_in_ruleModelItem534); 

            	        	newLeafNode(otherlv_16, grammarAccess.getModelItemAccess().getRightCurlyBracketKeyword_5_3());
            	        

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelItem"


    // $ANTLR start "entryRuleModelGroupItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:313:1: entryRuleModelGroupItem returns [EObject current=null] : iv_ruleModelGroupItem= ruleModelGroupItem EOF ;
    public final EObject entryRuleModelGroupItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelGroupItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:314:2: (iv_ruleModelGroupItem= ruleModelGroupItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:315:2: iv_ruleModelGroupItem= ruleModelGroupItem EOF
            {
             newCompositeNode(grammarAccess.getModelGroupItemRule()); 
            pushFollow(FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem572);
            iv_ruleModelGroupItem=ruleModelGroupItem();

            state._fsp--;

             current =iv_ruleModelGroupItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelGroupItem582); 

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
    // $ANTLR end "entryRuleModelGroupItem"


    // $ANTLR start "ruleModelGroupItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:322:1: ruleModelGroupItem returns [EObject current=null] : (otherlv_0= 'Group' () (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )? ) ;
    public final EObject ruleModelGroupItem() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token lv_args_7_1=null;
        Token lv_args_7_2=null;
        Token otherlv_8=null;
        Token lv_args_9_1=null;
        Token lv_args_9_2=null;
        Token otherlv_10=null;
        AntlrDatatypeRuleToken lv_type_3_0 = null;

        Enumerator lv_function_5_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:325:28: ( (otherlv_0= 'Group' () (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )? ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:326:1: (otherlv_0= 'Group' () (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )? )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:326:1: (otherlv_0= 'Group' () (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )? )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:326:3: otherlv_0= 'Group' () (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )?
            {
            otherlv_0=(Token)match(input,18,FOLLOW_18_in_ruleModelGroupItem619); 

                	newLeafNode(otherlv_0, grammarAccess.getModelGroupItemAccess().getGroupKeyword_0());
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:330:1: ()
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:331:5: 
            {

                    current = forceCreateModelElement(
                        grammarAccess.getModelGroupItemAccess().getModelGroupItemAction_1(),
                        current);
                

            }

            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:336:2: (otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )? )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==19) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:336:4: otherlv_2= ':' ( (lv_type_3_0= ruleModelItemType ) ) (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )?
                    {
                    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleModelGroupItem641); 

                        	newLeafNode(otherlv_2, grammarAccess.getModelGroupItemAccess().getColonKeyword_2_0());
                        
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:340:1: ( (lv_type_3_0= ruleModelItemType ) )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:341:1: (lv_type_3_0= ruleModelItemType )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:341:1: (lv_type_3_0= ruleModelItemType )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:342:3: lv_type_3_0= ruleModelItemType
                    {
                     
                    	        newCompositeNode(grammarAccess.getModelGroupItemAccess().getTypeModelItemTypeParserRuleCall_2_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleModelItemType_in_ruleModelGroupItem662);
                    lv_type_3_0=ruleModelItemType();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getModelGroupItemRule());
                    	        }
                           		set(
                           			current, 
                           			"type",
                            		lv_type_3_0, 
                            		"ModelItemType");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:358:2: (otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )? )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==19) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:358:4: otherlv_4= ':' ( (lv_function_5_0= ruleModelGroupFunction ) ) (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )?
                            {
                            otherlv_4=(Token)match(input,19,FOLLOW_19_in_ruleModelGroupItem675); 

                                	newLeafNode(otherlv_4, grammarAccess.getModelGroupItemAccess().getColonKeyword_2_2_0());
                                
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:362:1: ( (lv_function_5_0= ruleModelGroupFunction ) )
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:363:1: (lv_function_5_0= ruleModelGroupFunction )
                            {
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:363:1: (lv_function_5_0= ruleModelGroupFunction )
                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:364:3: lv_function_5_0= ruleModelGroupFunction
                            {
                             
                            	        newCompositeNode(grammarAccess.getModelGroupItemAccess().getFunctionModelGroupFunctionEnumRuleCall_2_2_1_0()); 
                            	    
                            pushFollow(FOLLOW_ruleModelGroupFunction_in_ruleModelGroupItem696);
                            lv_function_5_0=ruleModelGroupFunction();

                            state._fsp--;


                            	        if (current==null) {
                            	            current = createModelElementForParent(grammarAccess.getModelGroupItemRule());
                            	        }
                                   		set(
                                   			current, 
                                   			"function",
                                    		lv_function_5_0, 
                                    		"ModelGroupFunction");
                            	        afterParserOrEnumRuleCall();
                            	    

                            }


                            }

                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:380:2: (otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')' )?
                            int alt13=2;
                            int LA13_0 = input.LA(1);

                            if ( (LA13_0==13) ) {
                                alt13=1;
                            }
                            switch (alt13) {
                                case 1 :
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:380:4: otherlv_6= '(' ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) ) (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )* otherlv_10= ')'
                                    {
                                    otherlv_6=(Token)match(input,13,FOLLOW_13_in_ruleModelGroupItem709); 

                                        	newLeafNode(otherlv_6, grammarAccess.getModelGroupItemAccess().getLeftParenthesisKeyword_2_2_2_0());
                                        
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:384:1: ( ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) ) )
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:1: ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) )
                                    {
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:385:1: ( (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING ) )
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:386:1: (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING )
                                    {
                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:386:1: (lv_args_7_1= RULE_ID | lv_args_7_2= RULE_STRING )
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
                                            new NoViableAltException("", 10, 0, input);

                                        throw nvae;
                                    }
                                    switch (alt10) {
                                        case 1 :
                                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:387:3: lv_args_7_1= RULE_ID
                                            {
                                            lv_args_7_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelGroupItem728); 

                                            			newLeafNode(lv_args_7_1, grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_1_0_0()); 
                                            		

                                            	        if (current==null) {
                                            	            current = createModelElement(grammarAccess.getModelGroupItemRule());
                                            	        }
                                                   		addWithLastConsumed(
                                                   			current, 
                                                   			"args",
                                                    		lv_args_7_1, 
                                                    		"ID");
                                            	    

                                            }
                                            break;
                                        case 2 :
                                            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:402:8: lv_args_7_2= RULE_STRING
                                            {
                                            lv_args_7_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelGroupItem748); 

                                            			newLeafNode(lv_args_7_2, grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_1_0_1()); 
                                            		

                                            	        if (current==null) {
                                            	            current = createModelElement(grammarAccess.getModelGroupItemRule());
                                            	        }
                                                   		addWithLastConsumed(
                                                   			current, 
                                                   			"args",
                                                    		lv_args_7_2, 
                                                    		"STRING");
                                            	    

                                            }
                                            break;

                                    }


                                    }


                                    }

                                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:420:2: (otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) ) )*
                                    loop12:
                                    do {
                                        int alt12=2;
                                        int LA12_0 = input.LA(1);

                                        if ( (LA12_0==14) ) {
                                            alt12=1;
                                        }


                                        switch (alt12) {
                                    	case 1 :
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:420:4: otherlv_8= ',' ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) )
                                    	    {
                                    	    otherlv_8=(Token)match(input,14,FOLLOW_14_in_ruleModelGroupItem769); 

                                    	        	newLeafNode(otherlv_8, grammarAccess.getModelGroupItemAccess().getCommaKeyword_2_2_2_2_0());
                                    	        
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:424:1: ( ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) ) )
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:425:1: ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) )
                                    	    {
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:425:1: ( (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING ) )
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:426:1: (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING )
                                    	    {
                                    	    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:426:1: (lv_args_9_1= RULE_ID | lv_args_9_2= RULE_STRING )
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
                                    	            new NoViableAltException("", 11, 0, input);

                                    	        throw nvae;
                                    	    }
                                    	    switch (alt11) {
                                    	        case 1 :
                                    	            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:427:3: lv_args_9_1= RULE_ID
                                    	            {
                                    	            lv_args_9_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelGroupItem788); 

                                    	            			newLeafNode(lv_args_9_1, grammarAccess.getModelGroupItemAccess().getArgsIDTerminalRuleCall_2_2_2_2_1_0_0()); 
                                    	            		

                                    	            	        if (current==null) {
                                    	            	            current = createModelElement(grammarAccess.getModelGroupItemRule());
                                    	            	        }
                                    	                   		addWithLastConsumed(
                                    	                   			current, 
                                    	                   			"args",
                                    	                    		lv_args_9_1, 
                                    	                    		"ID");
                                    	            	    

                                    	            }
                                    	            break;
                                    	        case 2 :
                                    	            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:442:8: lv_args_9_2= RULE_STRING
                                    	            {
                                    	            lv_args_9_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelGroupItem808); 

                                    	            			newLeafNode(lv_args_9_2, grammarAccess.getModelGroupItemAccess().getArgsSTRINGTerminalRuleCall_2_2_2_2_1_0_1()); 
                                    	            		

                                    	            	        if (current==null) {
                                    	            	            current = createModelElement(grammarAccess.getModelGroupItemRule());
                                    	            	        }
                                    	                   		addWithLastConsumed(
                                    	                   			current, 
                                    	                   			"args",
                                    	                    		lv_args_9_2, 
                                    	                    		"STRING");
                                    	            	    

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

                                    otherlv_10=(Token)match(input,15,FOLLOW_15_in_ruleModelGroupItem830); 

                                        	newLeafNode(otherlv_10, grammarAccess.getModelGroupItemAccess().getRightParenthesisKeyword_2_2_2_3());
                                        

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

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelGroupItem"


    // $ANTLR start "entryRuleModelNormalItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:472:1: entryRuleModelNormalItem returns [EObject current=null] : iv_ruleModelNormalItem= ruleModelNormalItem EOF ;
    public final EObject entryRuleModelNormalItem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelNormalItem = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:473:2: (iv_ruleModelNormalItem= ruleModelNormalItem EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:474:2: iv_ruleModelNormalItem= ruleModelNormalItem EOF
            {
             newCompositeNode(grammarAccess.getModelNormalItemRule()); 
            pushFollow(FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem872);
            iv_ruleModelNormalItem=ruleModelNormalItem();

            state._fsp--;

             current =iv_ruleModelNormalItem; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelNormalItem882); 

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
    // $ANTLR end "entryRuleModelNormalItem"


    // $ANTLR start "ruleModelNormalItem"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:481:1: ruleModelNormalItem returns [EObject current=null] : ( (lv_type_0_0= ruleModelItemType ) ) ;
    public final EObject ruleModelNormalItem() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_type_0_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:484:28: ( ( (lv_type_0_0= ruleModelItemType ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:485:1: ( (lv_type_0_0= ruleModelItemType ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:485:1: ( (lv_type_0_0= ruleModelItemType ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:486:1: (lv_type_0_0= ruleModelItemType )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:486:1: (lv_type_0_0= ruleModelItemType )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:487:3: lv_type_0_0= ruleModelItemType
            {
             
            	        newCompositeNode(grammarAccess.getModelNormalItemAccess().getTypeModelItemTypeParserRuleCall_0()); 
            	    
            pushFollow(FOLLOW_ruleModelItemType_in_ruleModelNormalItem927);
            lv_type_0_0=ruleModelItemType();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getModelNormalItemRule());
            	        }
                   		set(
                   			current, 
                   			"type",
                    		lv_type_0_0, 
                    		"ModelItemType");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelNormalItem"


    // $ANTLR start "entryRuleModelItemType"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:511:1: entryRuleModelItemType returns [String current=null] : iv_ruleModelItemType= ruleModelItemType EOF ;
    public final String entryRuleModelItemType() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleModelItemType = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:512:2: (iv_ruleModelItemType= ruleModelItemType EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:513:2: iv_ruleModelItemType= ruleModelItemType EOF
            {
             newCompositeNode(grammarAccess.getModelItemTypeRule()); 
            pushFollow(FOLLOW_ruleModelItemType_in_entryRuleModelItemType963);
            iv_ruleModelItemType=ruleModelItemType();

            state._fsp--;

             current =iv_ruleModelItemType.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelItemType974); 

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
    // $ANTLR end "entryRuleModelItemType"


    // $ANTLR start "ruleModelItemType"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:520:1: ruleModelItemType returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | kw= 'DateTime' | this_ID_7= RULE_ID ) ;
    public final AntlrDatatypeRuleToken ruleModelItemType() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_7=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:523:28: ( (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | kw= 'DateTime' | this_ID_7= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:524:1: (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | kw= 'DateTime' | this_ID_7= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:524:1: (kw= 'Switch' | kw= 'Rollershutter' | kw= 'Number' | kw= 'String' | kw= 'Dimmer' | kw= 'Contact' | kw= 'DateTime' | this_ID_7= RULE_ID )
            int alt16=8;
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
            case 26:
                {
                alt16=7;
                }
                break;
            case RULE_ID:
                {
                alt16=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:525:2: kw= 'Switch'
                    {
                    kw=(Token)match(input,20,FOLLOW_20_in_ruleModelItemType1012); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getSwitchKeyword_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:532:2: kw= 'Rollershutter'
                    {
                    kw=(Token)match(input,21,FOLLOW_21_in_ruleModelItemType1031); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getRollershutterKeyword_1()); 
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:539:2: kw= 'Number'
                    {
                    kw=(Token)match(input,22,FOLLOW_22_in_ruleModelItemType1050); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getNumberKeyword_2()); 
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:546:2: kw= 'String'
                    {
                    kw=(Token)match(input,23,FOLLOW_23_in_ruleModelItemType1069); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getStringKeyword_3()); 
                        

                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:553:2: kw= 'Dimmer'
                    {
                    kw=(Token)match(input,24,FOLLOW_24_in_ruleModelItemType1088); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getDimmerKeyword_4()); 
                        

                    }
                    break;
                case 6 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:560:2: kw= 'Contact'
                    {
                    kw=(Token)match(input,25,FOLLOW_25_in_ruleModelItemType1107); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getContactKeyword_5()); 
                        

                    }
                    break;
                case 7 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:567:2: kw= 'DateTime'
                    {
                    kw=(Token)match(input,26,FOLLOW_26_in_ruleModelItemType1126); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getModelItemTypeAccess().getDateTimeKeyword_6()); 
                        

                    }
                    break;
                case 8 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:573:10: this_ID_7= RULE_ID
                    {
                    this_ID_7=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelItemType1147); 

                    		current.merge(this_ID_7);
                        
                     
                        newLeafNode(this_ID_7, grammarAccess.getModelItemTypeAccess().getIDTerminalRuleCall_7()); 
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelItemType"


    // $ANTLR start "entryRuleModelBinding"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:588:1: entryRuleModelBinding returns [EObject current=null] : iv_ruleModelBinding= ruleModelBinding EOF ;
    public final EObject entryRuleModelBinding() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelBinding = null;


        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:589:2: (iv_ruleModelBinding= ruleModelBinding EOF )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:590:2: iv_ruleModelBinding= ruleModelBinding EOF
            {
             newCompositeNode(grammarAccess.getModelBindingRule()); 
            pushFollow(FOLLOW_ruleModelBinding_in_entryRuleModelBinding1192);
            iv_ruleModelBinding=ruleModelBinding();

            state._fsp--;

             current =iv_ruleModelBinding; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelBinding1202); 

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
    // $ANTLR end "entryRuleModelBinding"


    // $ANTLR start "ruleModelBinding"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:597:1: ruleModelBinding returns [EObject current=null] : ( ( (lv_type_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) ;
    public final EObject ruleModelBinding() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        Token otherlv_1=null;
        Token lv_configuration_2_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:600:28: ( ( ( (lv_type_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_configuration_2_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:601:1: ( ( (lv_type_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:601:1: ( ( (lv_type_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_configuration_2_0= RULE_STRING ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:601:2: ( (lv_type_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_configuration_2_0= RULE_STRING ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:601:2: ( (lv_type_0_0= RULE_ID ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:602:1: (lv_type_0_0= RULE_ID )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:602:1: (lv_type_0_0= RULE_ID )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:603:3: lv_type_0_0= RULE_ID
            {
            lv_type_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModelBinding1244); 

            			newLeafNode(lv_type_0_0, grammarAccess.getModelBindingAccess().getTypeIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getModelBindingRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"type",
                    		lv_type_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,27,FOLLOW_27_in_ruleModelBinding1261); 

                	newLeafNode(otherlv_1, grammarAccess.getModelBindingAccess().getEqualsSignKeyword_1());
                
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:623:1: ( (lv_configuration_2_0= RULE_STRING ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:624:1: (lv_configuration_2_0= RULE_STRING )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:624:1: (lv_configuration_2_0= RULE_STRING )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:625:3: lv_configuration_2_0= RULE_STRING
            {
            lv_configuration_2_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleModelBinding1278); 

            			newLeafNode(lv_configuration_2_0, grammarAccess.getModelBindingAccess().getConfigurationSTRINGTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getModelBindingRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"configuration",
                    		lv_configuration_2_0, 
                    		"STRING");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelBinding"


    // $ANTLR start "ruleModelGroupFunction"
    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:649:1: ruleModelGroupFunction returns [Enumerator current=null] : ( (enumLiteral_0= 'AND' ) | (enumLiteral_1= 'OR' ) | (enumLiteral_2= 'AVG' ) | (enumLiteral_3= 'MAX' ) | (enumLiteral_4= 'MIN' ) ) ;
    public final Enumerator ruleModelGroupFunction() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;

         enterRule(); 
        try {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:651:28: ( ( (enumLiteral_0= 'AND' ) | (enumLiteral_1= 'OR' ) | (enumLiteral_2= 'AVG' ) | (enumLiteral_3= 'MAX' ) | (enumLiteral_4= 'MIN' ) ) )
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:652:1: ( (enumLiteral_0= 'AND' ) | (enumLiteral_1= 'OR' ) | (enumLiteral_2= 'AVG' ) | (enumLiteral_3= 'MAX' ) | (enumLiteral_4= 'MIN' ) )
            {
            // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:652:1: ( (enumLiteral_0= 'AND' ) | (enumLiteral_1= 'OR' ) | (enumLiteral_2= 'AVG' ) | (enumLiteral_3= 'MAX' ) | (enumLiteral_4= 'MIN' ) )
            int alt17=5;
            switch ( input.LA(1) ) {
            case 28:
                {
                alt17=1;
                }
                break;
            case 29:
                {
                alt17=2;
                }
                break;
            case 30:
                {
                alt17=3;
                }
                break;
            case 31:
                {
                alt17=4;
                }
                break;
            case 32:
                {
                alt17=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:652:2: (enumLiteral_0= 'AND' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:652:2: (enumLiteral_0= 'AND' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:652:4: enumLiteral_0= 'AND'
                    {
                    enumLiteral_0=(Token)match(input,28,FOLLOW_28_in_ruleModelGroupFunction1333); 

                            current = grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_0, grammarAccess.getModelGroupFunctionAccess().getANDEnumLiteralDeclaration_0()); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:658:6: (enumLiteral_1= 'OR' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:658:6: (enumLiteral_1= 'OR' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:658:8: enumLiteral_1= 'OR'
                    {
                    enumLiteral_1=(Token)match(input,29,FOLLOW_29_in_ruleModelGroupFunction1350); 

                            current = grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_1, grammarAccess.getModelGroupFunctionAccess().getOREnumLiteralDeclaration_1()); 
                        

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:664:6: (enumLiteral_2= 'AVG' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:664:6: (enumLiteral_2= 'AVG' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:664:8: enumLiteral_2= 'AVG'
                    {
                    enumLiteral_2=(Token)match(input,30,FOLLOW_30_in_ruleModelGroupFunction1367); 

                            current = grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_2, grammarAccess.getModelGroupFunctionAccess().getAVGEnumLiteralDeclaration_2()); 
                        

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:670:6: (enumLiteral_3= 'MAX' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:670:6: (enumLiteral_3= 'MAX' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:670:8: enumLiteral_3= 'MAX'
                    {
                    enumLiteral_3=(Token)match(input,31,FOLLOW_31_in_ruleModelGroupFunction1384); 

                            current = grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_3, grammarAccess.getModelGroupFunctionAccess().getMAXEnumLiteralDeclaration_3()); 
                        

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:676:6: (enumLiteral_4= 'MIN' )
                    {
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:676:6: (enumLiteral_4= 'MIN' )
                    // ../org.openhab.model.item/src-gen/org/openhab/model/parser/antlr/internal/InternalItems.g:676:8: enumLiteral_4= 'MIN'
                    {
                    enumLiteral_4=(Token)match(input,32,FOLLOW_32_in_ruleModelGroupFunction1401); 

                            current = grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_4, grammarAccess.getModelGroupFunctionAccess().getMINEnumLiteralDeclaration_4()); 
                        

                    }


                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModelGroupFunction"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleItemModel_in_entryRuleItemModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleItemModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItem_in_ruleItemModel130 = new BitSet(new long[]{0x0000000007F40012L});
    public static final BitSet FOLLOW_ruleModelItem_in_entryRuleModelItem166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItem176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_ruleModelItem224 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_ruleModelItem251 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem268 = new BitSet(new long[]{0x0000000000012822L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelItem290 = new BitSet(new long[]{0x0000000000012802L});
    public static final BitSet FOLLOW_11_in_ruleModelItem309 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem328 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelItem348 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleModelItem368 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_13_in_ruleModelItem383 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem403 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_14_in_ruleModelItem416 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItem436 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_15_in_ruleModelItem450 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_ruleModelItem465 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelBinding_in_ruleModelItem486 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_14_in_ruleModelItem499 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleModelBinding_in_ruleModelItem520 = new BitSet(new long[]{0x0000000000024000L});
    public static final BitSet FOLLOW_17_in_ruleModelItem534 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_ruleModelGroupItem_in_entryRuleModelGroupItem572 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelGroupItem582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleModelGroupItem619 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleModelGroupItem641 = new BitSet(new long[]{0x0000000007F00010L});
    public static final BitSet FOLLOW_ruleModelItemType_in_ruleModelGroupItem662 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleModelGroupItem675 = new BitSet(new long[]{0x00000001F0000000L});
    public static final BitSet FOLLOW_ruleModelGroupFunction_in_ruleModelGroupItem696 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleModelGroupItem709 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelGroupItem728 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelGroupItem748 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_14_in_ruleModelGroupItem769 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelGroupItem788 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelGroupItem808 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_15_in_ruleModelGroupItem830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelNormalItem_in_entryRuleModelNormalItem872 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelNormalItem882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_ruleModelNormalItem927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelItemType_in_entryRuleModelItemType963 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelItemType974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleModelItemType1012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleModelItemType1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleModelItemType1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleModelItemType1069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_ruleModelItemType1088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleModelItemType1107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleModelItemType1126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelItemType1147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelBinding_in_entryRuleModelBinding1192 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelBinding1202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModelBinding1244 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleModelBinding1261 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleModelBinding1278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_ruleModelGroupFunction1333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleModelGroupFunction1350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleModelGroupFunction1367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleModelGroupFunction1384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_ruleModelGroupFunction1401 = new BitSet(new long[]{0x0000000000000002L});

}