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
import org.openhab.model.services.SitemapGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSitemapParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'label='", "'icon='", "'{'", "'}'", "'Frame'", "'Text'", "'item='", "'Group'", "'Image'", "'url='", "'Switch'", "'buttonLabels=['", "']'", "'Selection'"
    };
    public static final int RULE_ID=4;
    public static final int RULE_STRING=5;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_INT=6;
    public static final int RULE_WS=9;
    public static final int RULE_SL_COMMENT=8;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=7;

        public InternalSitemapParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g"; }



     	private SitemapGrammarAccess grammarAccess;
     	
        public InternalSitemapParser(TokenStream input, IAstFactory factory, SitemapGrammarAccess grammarAccess) {
            this(input);
            this.factory = factory;
            registerRules(grammarAccess.getGrammar());
            this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected InputStream getTokenFile() {
        	ClassLoader classLoader = getClass().getClassLoader();
        	return classLoader.getResourceAsStream("org/openhab/model/parser/antlr/internal/InternalSitemap.tokens");
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Model";	
       	}
       	
       	@Override
       	protected SitemapGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start entryRuleModel
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:77:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:78:2: (iv_ruleModel= ruleModel EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:79:2: iv_ruleModel= ruleModel EOF
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:86:1: ruleModel returns [EObject current=null] : ( 'sitemap' this_Sitemap_1= ruleSitemap ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject this_Sitemap_1 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:91:6: ( ( 'sitemap' this_Sitemap_1= ruleSitemap ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:92:1: ( 'sitemap' this_Sitemap_1= ruleSitemap )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:92:1: ( 'sitemap' this_Sitemap_1= ruleSitemap )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:92:3: 'sitemap' this_Sitemap_1= ruleSitemap
            {
            match(input,11,FOLLOW_11_in_ruleModel120); 

                    createLeafNode(grammarAccess.getModelAccess().getSitemapKeyword_0(), null); 
                
             
                    currentNode=createCompositeNode(grammarAccess.getModelAccess().getSitemapParserRuleCall_1(), currentNode); 
                
            pushFollow(FOLLOW_ruleSitemap_in_ruleModel142);
            this_Sitemap_1=ruleSitemap();
            _fsp--;

             
                    current = this_Sitemap_1; 
                    currentNode = currentNode.getParent();
                

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
    // $ANTLR end ruleModel


    // $ANTLR start entryRuleSitemap
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:113:1: entryRuleSitemap returns [EObject current=null] : iv_ruleSitemap= ruleSitemap EOF ;
    public final EObject entryRuleSitemap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSitemap = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:114:2: (iv_ruleSitemap= ruleSitemap EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:115:2: iv_ruleSitemap= ruleSitemap EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSitemapRule(), currentNode); 
            pushFollow(FOLLOW_ruleSitemap_in_entryRuleSitemap177);
            iv_ruleSitemap=ruleSitemap();
            _fsp--;

             current =iv_ruleSitemap; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSitemap187); 

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
    // $ANTLR end entryRuleSitemap


    // $ANTLR start ruleSitemap
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:122:1: ruleSitemap returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? '{' ( (lv_children_6_0= ruleWidget ) )+ '}' ) ;
    public final EObject ruleSitemap() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_label_2_0=null;
        Token lv_icon_4_0=null;
        EObject lv_children_6_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:127:6: ( ( ( (lv_name_0_0= RULE_ID ) ) ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? '{' ( (lv_children_6_0= ruleWidget ) )+ '}' ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:128:1: ( ( (lv_name_0_0= RULE_ID ) ) ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? '{' ( (lv_children_6_0= ruleWidget ) )+ '}' )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:128:1: ( ( (lv_name_0_0= RULE_ID ) ) ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? '{' ( (lv_children_6_0= ruleWidget ) )+ '}' )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:128:2: ( (lv_name_0_0= RULE_ID ) ) ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? '{' ( (lv_children_6_0= ruleWidget ) )+ '}'
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:128:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:129:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:129:1: (lv_name_0_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:130:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSitemap229); 

            			createLeafNode(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0(), "name"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getSitemapRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"name",
            	        		lv_name_0_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:152:2: ( 'label=' ( (lv_label_2_0= RULE_STRING ) ) )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==12) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:152:4: 'label=' ( (lv_label_2_0= RULE_STRING ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleSitemap245); 

                            createLeafNode(grammarAccess.getSitemapAccess().getLabelKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:156:1: ( (lv_label_2_0= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:157:1: (lv_label_2_0= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:157:1: (lv_label_2_0= RULE_STRING )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:158:3: lv_label_2_0= RULE_STRING
                    {
                    lv_label_2_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSitemap262); 

                    			createLeafNode(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSitemapRule().getType().getClassifier());
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


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:180:4: ( 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==13) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:180:6: 'icon=' ( (lv_icon_4_0= RULE_STRING ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleSitemap280); 

                            createLeafNode(grammarAccess.getSitemapAccess().getIconKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:184:1: ( (lv_icon_4_0= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:185:1: (lv_icon_4_0= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:185:1: (lv_icon_4_0= RULE_STRING )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:186:3: lv_icon_4_0= RULE_STRING
                    {
                    lv_icon_4_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSitemap297); 

                    			createLeafNode(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0(), "icon"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSitemapRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"icon",
                    	        		lv_icon_4_0, 
                    	        		"STRING", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }

            match(input,14,FOLLOW_14_in_ruleSitemap314); 

                    createLeafNode(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:212:1: ( (lv_children_6_0= ruleWidget ) )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=16 && LA3_0<=17)||(LA3_0>=19 && LA3_0<=20)||LA3_0==22||LA3_0==25) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:213:1: (lv_children_6_0= ruleWidget )
            	    {
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:213:1: (lv_children_6_0= ruleWidget )
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:214:3: lv_children_6_0= ruleWidget
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleWidget_in_ruleSitemap335);
            	    lv_children_6_0=ruleWidget();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getSitemapRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        try {
            	    	       		add(
            	    	       			current, 
            	    	       			"children",
            	    	        		lv_children_6_0, 
            	    	        		"Widget", 
            	    	        		currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }


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

            match(input,15,FOLLOW_15_in_ruleSitemap346); 

                    createLeafNode(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5(), null); 
                

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
    // $ANTLR end ruleSitemap


    // $ANTLR start entryRuleWidget
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:248:1: entryRuleWidget returns [EObject current=null] : iv_ruleWidget= ruleWidget EOF ;
    public final EObject entryRuleWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:249:2: (iv_ruleWidget= ruleWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:250:2: iv_ruleWidget= ruleWidget EOF
            {
             currentNode = createCompositeNode(grammarAccess.getWidgetRule(), currentNode); 
            pushFollow(FOLLOW_ruleWidget_in_entryRuleWidget382);
            iv_ruleWidget=ruleWidget();
            _fsp--;

             current =iv_ruleWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWidget392); 

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
    // $ANTLR end entryRuleWidget


    // $ANTLR start ruleWidget
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:257:1: ruleWidget returns [EObject current=null] : (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ) ) ;
    public final EObject ruleWidget() throws RecognitionException {
        EObject current = null;

        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        EObject this_LinkableWidget_0 = null;

        EObject this_Switch_1 = null;

        EObject this_Selection_2 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:262:6: ( (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0>=16 && LA9_0<=17)||(LA9_0>=19 && LA9_0<=20)) ) {
                alt9=1;
            }
            else if ( (LA9_0==22||LA9_0==25) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ) )", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:264:5: this_LinkableWidget_0= ruleLinkableWidget
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleLinkableWidget_in_ruleWidget439);
                    this_LinkableWidget_0=ruleLinkableWidget();
                    _fsp--;

                     
                            current = this_LinkableWidget_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection )
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==22) ) {
                        alt4=1;
                    }
                    else if ( (LA4_0==25) ) {
                        alt4=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection )", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:274:5: this_Switch_1= ruleSwitch
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleSwitch_in_ruleWidget468);
                            this_Switch_1=ruleSwitch();
                            _fsp--;

                             
                                    current = this_Switch_1; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:284:5: this_Selection_2= ruleSelection
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleSelection_in_ruleWidget495);
                            this_Selection_2=ruleSelection();
                            _fsp--;

                             
                                    current = this_Selection_2; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;

                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:292:2: ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==12) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:292:4: 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                            {
                            match(input,12,FOLLOW_12_in_ruleWidget506); 

                                    createLeafNode(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0(), null); 
                                
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:296:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:297:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                            {
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:297:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:298:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                            {
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:298:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
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
                                    new NoViableAltException("298:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )", 5, 0, input);

                                throw nvae;
                            }
                            switch (alt5) {
                                case 1 :
                                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:299:3: lv_label_4_1= RULE_ID
                                    {
                                    lv_label_4_1=(Token)input.LT(1);
                                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget525); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0(), "label"); 
                                    		

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        try {
                                    	       		set(
                                    	       			current, 
                                    	       			"label",
                                    	        		lv_label_4_1, 
                                    	        		"ID", 
                                    	        		lastConsumedNode);
                                    	        } catch (ValueConverterException vce) {
                                    				handleValueConverterException(vce);
                                    	        }
                                    	    

                                    }
                                    break;
                                case 2 :
                                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:320:8: lv_label_4_2= RULE_STRING
                                    {
                                    lv_label_4_2=(Token)input.LT(1);
                                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget545); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1(), "label"); 
                                    		

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        try {
                                    	       		set(
                                    	       			current, 
                                    	       			"label",
                                    	        		lv_label_4_2, 
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

                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:344:4: ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==13) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:344:6: 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                            {
                            match(input,13,FOLLOW_13_in_ruleWidget566); 

                                    createLeafNode(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0(), null); 
                                
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:348:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:349:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                            {
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:349:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:350:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                            {
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:350:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                            int alt7=2;
                            int LA7_0 = input.LA(1);

                            if ( (LA7_0==RULE_ID) ) {
                                alt7=1;
                            }
                            else if ( (LA7_0==RULE_STRING) ) {
                                alt7=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("350:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )", 7, 0, input);

                                throw nvae;
                            }
                            switch (alt7) {
                                case 1 :
                                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:351:3: lv_icon_6_1= RULE_ID
                                    {
                                    lv_icon_6_1=(Token)input.LT(1);
                                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget585); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0(), "icon"); 
                                    		

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        try {
                                    	       		set(
                                    	       			current, 
                                    	       			"icon",
                                    	        		lv_icon_6_1, 
                                    	        		"ID", 
                                    	        		lastConsumedNode);
                                    	        } catch (ValueConverterException vce) {
                                    				handleValueConverterException(vce);
                                    	        }
                                    	    

                                    }
                                    break;
                                case 2 :
                                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:372:8: lv_icon_6_2= RULE_STRING
                                    {
                                    lv_icon_6_2=(Token)input.LT(1);
                                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget605); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1(), "icon"); 
                                    		

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        try {
                                    	       		set(
                                    	       			current, 
                                    	       			"icon",
                                    	        		lv_icon_6_2, 
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

                    }


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
    // $ANTLR end ruleWidget


    // $ANTLR start entryRuleLinkableWidget
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:404:1: entryRuleLinkableWidget returns [EObject current=null] : iv_ruleLinkableWidget= ruleLinkableWidget EOF ;
    public final EObject entryRuleLinkableWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLinkableWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:405:2: (iv_ruleLinkableWidget= ruleLinkableWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:406:2: iv_ruleLinkableWidget= ruleLinkableWidget EOF
            {
             currentNode = createCompositeNode(grammarAccess.getLinkableWidgetRule(), currentNode); 
            pushFollow(FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget652);
            iv_ruleLinkableWidget=ruleLinkableWidget();
            _fsp--;

             current =iv_ruleLinkableWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkableWidget662); 

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
    // $ANTLR end entryRuleLinkableWidget


    // $ANTLR start ruleLinkableWidget
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:413:1: ruleLinkableWidget returns [EObject current=null] : ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) ;
    public final EObject ruleLinkableWidget() throws RecognitionException {
        EObject current = null;

        Token lv_label_5_1=null;
        Token lv_label_5_2=null;
        Token lv_icon_7_1=null;
        Token lv_icon_7_2=null;
        EObject this_Text_0 = null;

        EObject this_Group_1 = null;

        EObject this_Image_2 = null;

        EObject this_Frame_3 = null;

        EObject lv_children_9_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:418:6: ( ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:419:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:419:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:419:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:419:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )
            int alt10=4;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt10=1;
                }
                break;
            case 19:
                {
                alt10=2;
                }
                break;
            case 20:
                {
                alt10=3;
                }
                break;
            case 16:
                {
                alt10=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("419:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:420:5: this_Text_0= ruleText
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleText_in_ruleLinkableWidget710);
                    this_Text_0=ruleText();
                    _fsp--;

                     
                            current = this_Text_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:430:5: this_Group_1= ruleGroup
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroup_in_ruleLinkableWidget737);
                    this_Group_1=ruleGroup();
                    _fsp--;

                     
                            current = this_Group_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:440:5: this_Image_2= ruleImage
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleImage_in_ruleLinkableWidget764);
                    this_Image_2=ruleImage();
                    _fsp--;

                     
                            current = this_Image_2; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:450:5: this_Frame_3= ruleFrame
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleFrame_in_ruleLinkableWidget791);
                    this_Frame_3=ruleFrame();
                    _fsp--;

                     
                            current = this_Frame_3; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:458:2: ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==12) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:458:4: 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleLinkableWidget802); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:462:1: ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:463:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:463:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:464:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:464:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
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
                            new NoViableAltException("464:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )", 11, 0, input);

                        throw nvae;
                    }
                    switch (alt11) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:465:3: lv_label_5_1= RULE_ID
                            {
                            lv_label_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget821); 

                            			createLeafNode(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getLinkableWidgetRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"label",
                            	        		lv_label_5_1, 
                            	        		"ID", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:486:8: lv_label_5_2= RULE_STRING
                            {
                            lv_label_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget841); 

                            			createLeafNode(grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getLinkableWidgetRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"label",
                            	        		lv_label_5_2, 
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

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:510:4: ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==13) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:510:6: 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleLinkableWidget862); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:514:1: ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:515:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:515:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==RULE_ID) ) {
                        alt13=1;
                    }
                    else if ( (LA13_0==RULE_STRING) ) {
                        alt13=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("516:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )", 13, 0, input);

                        throw nvae;
                    }
                    switch (alt13) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:517:3: lv_icon_7_1= RULE_ID
                            {
                            lv_icon_7_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget881); 

                            			createLeafNode(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getLinkableWidgetRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_7_1, 
                            	        		"ID", 
                            	        		lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:538:8: lv_icon_7_2= RULE_STRING
                            {
                            lv_icon_7_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget901); 

                            			createLeafNode(grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getLinkableWidgetRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        try {
                            	       		set(
                            	       			current, 
                            	       			"icon",
                            	        		lv_icon_7_2, 
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

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:562:4: ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==14) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:562:6: '{' ( (lv_children_9_0= ruleWidget ) )+ '}'
                    {
                    match(input,14,FOLLOW_14_in_ruleLinkableWidget922); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:566:1: ( (lv_children_9_0= ruleWidget ) )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0>=16 && LA15_0<=17)||(LA15_0>=19 && LA15_0<=20)||LA15_0==22||LA15_0==25) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:567:1: (lv_children_9_0= ruleWidget )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:567:1: (lv_children_9_0= ruleWidget )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:568:3: lv_children_9_0= ruleWidget
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleWidget_in_ruleLinkableWidget943);
                    	    lv_children_9_0=ruleWidget();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getLinkableWidgetRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        try {
                    	    	       		add(
                    	    	       			current, 
                    	    	       			"children",
                    	    	        		lv_children_9_0, 
                    	    	        		"Widget", 
                    	    	        		currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);

                    match(input,15,FOLLOW_15_in_ruleLinkableWidget954); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2(), null); 
                        

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
    // $ANTLR end ruleLinkableWidget


    // $ANTLR start entryRuleFrame
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:602:1: entryRuleFrame returns [EObject current=null] : iv_ruleFrame= ruleFrame EOF ;
    public final EObject entryRuleFrame() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFrame = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:603:2: (iv_ruleFrame= ruleFrame EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:604:2: iv_ruleFrame= ruleFrame EOF
            {
             currentNode = createCompositeNode(grammarAccess.getFrameRule(), currentNode); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame992);
            iv_ruleFrame=ruleFrame();
            _fsp--;

             current =iv_ruleFrame; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame1002); 

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
    // $ANTLR end entryRuleFrame


    // $ANTLR start ruleFrame
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:611:1: ruleFrame returns [EObject current=null] : ( 'Frame' () ) ;
    public final EObject ruleFrame() throws RecognitionException {
        EObject current = null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:616:6: ( ( 'Frame' () ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:617:1: ( 'Frame' () )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:617:1: ( 'Frame' () )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:617:3: 'Frame' ()
            {
            match(input,16,FOLLOW_16_in_ruleFrame1037); 

                    createLeafNode(grammarAccess.getFrameAccess().getFrameKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:621:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:622:5: 
            {
             
                    temp=factory.create(grammarAccess.getFrameAccess().getFrameAction_1().getType().getClassifier());
                    current = temp; 
                    temp = null;
                    CompositeNode newNode = createCompositeNode(grammarAccess.getFrameAccess().getFrameAction_1(), currentNode.getParent());
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
    // $ANTLR end ruleFrame


    // $ANTLR start entryRuleText
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:640:1: entryRuleText returns [EObject current=null] : iv_ruleText= ruleText EOF ;
    public final EObject entryRuleText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleText = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:641:2: (iv_ruleText= ruleText EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:642:2: iv_ruleText= ruleText EOF
            {
             currentNode = createCompositeNode(grammarAccess.getTextRule(), currentNode); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText1082);
            iv_ruleText=ruleText();
            _fsp--;

             current =iv_ruleText; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText1092); 

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
    // $ANTLR end entryRuleText


    // $ANTLR start ruleText
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:649:1: ruleText returns [EObject current=null] : ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleText() throws RecognitionException {
        EObject current = null;

        Token lv_item_3_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:654:6: ( ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:655:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:655:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:655:3: 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            match(input,17,FOLLOW_17_in_ruleText1127); 

                    createLeafNode(grammarAccess.getTextAccess().getTextKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:659:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:660:5: 
            {
             
                    temp=factory.create(grammarAccess.getTextAccess().getTextAction_1().getType().getClassifier());
                    current = temp; 
                    temp = null;
                    CompositeNode newNode = createCompositeNode(grammarAccess.getTextAccess().getTextAction_1(), currentNode.getParent());
                newNode.getChildren().add(currentNode);
                moveLookaheadInfo(currentNode, newNode);
                currentNode = newNode; 
                    associateNodeWithAstElement(currentNode, current); 
                

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:670:2: ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==18) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:670:4: 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    match(input,18,FOLLOW_18_in_ruleText1147); 

                            createLeafNode(grammarAccess.getTextAccess().getItemKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:674:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:675:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:675:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:676:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleText1164); 

                    			createLeafNode(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0(), "item"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getTextRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"item",
                    	        		lv_item_3_0, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


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
    // $ANTLR end ruleText


    // $ANTLR start entryRuleGroup
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:706:1: entryRuleGroup returns [EObject current=null] : iv_ruleGroup= ruleGroup EOF ;
    public final EObject entryRuleGroup() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroup = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:707:2: (iv_ruleGroup= ruleGroup EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:708:2: iv_ruleGroup= ruleGroup EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup1207);
            iv_ruleGroup=ruleGroup();
            _fsp--;

             current =iv_ruleGroup; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup1217); 

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
    // $ANTLR end entryRuleGroup


    // $ANTLR start ruleGroup
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:715:1: ruleGroup returns [EObject current=null] : ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleGroup() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:720:6: ( ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:721:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:721:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:721:3: 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,19,FOLLOW_19_in_ruleGroup1252); 

                    createLeafNode(grammarAccess.getGroupAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:725:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:725:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,18,FOLLOW_18_in_ruleGroup1263); 

                    createLeafNode(grammarAccess.getGroupAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:729:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:730:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:730:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:731:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroup1280); 

            			createLeafNode(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getGroupRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"item",
            	        		lv_item_2_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

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
    // $ANTLR end ruleGroup


    // $ANTLR start entryRuleImage
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:761:1: entryRuleImage returns [EObject current=null] : iv_ruleImage= ruleImage EOF ;
    public final EObject entryRuleImage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImage = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:762:2: (iv_ruleImage= ruleImage EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:763:2: iv_ruleImage= ruleImage EOF
            {
             currentNode = createCompositeNode(grammarAccess.getImageRule(), currentNode); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage1322);
            iv_ruleImage=ruleImage();
            _fsp--;

             current =iv_ruleImage; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage1332); 

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
    // $ANTLR end entryRuleImage


    // $ANTLR start ruleImage
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:770:1: ruleImage returns [EObject current=null] : ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleImage() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_url_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:775:6: ( ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:776:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:776:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:776:3: 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            {
            match(input,20,FOLLOW_20_in_ruleImage1367); 

                    createLeafNode(grammarAccess.getImageAccess().getImageKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:780:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==18) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:780:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
                    {
                    match(input,18,FOLLOW_18_in_ruleImage1378); 

                            createLeafNode(grammarAccess.getImageAccess().getItemKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:784:1: ( (lv_item_2_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:785:1: (lv_item_2_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:785:1: (lv_item_2_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:786:3: lv_item_2_0= RULE_ID
                    {
                    lv_item_2_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleImage1395); 

                    			createLeafNode(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getImageRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"item",
                    	        		lv_item_2_0, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:808:4: ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:808:6: 'url=' ( (lv_url_4_0= RULE_STRING ) )
            {
            match(input,21,FOLLOW_21_in_ruleImage1413); 

                    createLeafNode(grammarAccess.getImageAccess().getUrlKeyword_2_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:812:1: ( (lv_url_4_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:813:1: (lv_url_4_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:813:1: (lv_url_4_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:3: lv_url_4_0= RULE_STRING
            {
            lv_url_4_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImage1430); 

            			createLeafNode(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0(), "url"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getImageRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"url",
            	        		lv_url_4_0, 
            	        		"STRING", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

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
    // $ANTLR end ruleImage


    // $ANTLR start entryRuleSwitch
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:844:1: entryRuleSwitch returns [EObject current=null] : iv_ruleSwitch= ruleSwitch EOF ;
    public final EObject entryRuleSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSwitch = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:845:2: (iv_ruleSwitch= ruleSwitch EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:846:2: iv_ruleSwitch= ruleSwitch EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSwitchRule(), currentNode); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch1472);
            iv_ruleSwitch=ruleSwitch();
            _fsp--;

             current =iv_ruleSwitch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch1482); 

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
    // $ANTLR end entryRuleSwitch


    // $ANTLR start ruleSwitch
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:853:1: ruleSwitch returns [EObject current=null] : ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? ) ;
    public final EObject ruleSwitch() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_buttonLabels_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:858:6: ( ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:859:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:859:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:859:3: 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )?
            {
            match(input,22,FOLLOW_22_in_ruleSwitch1517); 

                    createLeafNode(grammarAccess.getSwitchAccess().getSwitchKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:863:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:863:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,18,FOLLOW_18_in_ruleSwitch1528); 

                    createLeafNode(grammarAccess.getSwitchAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:867:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:868:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:868:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:869:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1545); 

            			createLeafNode(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"item",
            	        		lv_item_2_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:891:3: ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==23) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:891:5: 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']'
                    {
                    match(input,23,FOLLOW_23_in_ruleSwitch1562); 

                            createLeafNode(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:895:1: ( (lv_buttonLabels_4_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:896:1: (lv_buttonLabels_4_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:896:1: (lv_buttonLabels_4_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:897:3: lv_buttonLabels_4_0= RULE_ID
                    {
                    lv_buttonLabels_4_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1579); 

                    			createLeafNode(grammarAccess.getSwitchAccess().getButtonLabelsIDTerminalRuleCall_2_1_0(), "buttonLabels"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		add(
                    	       			current, 
                    	       			"buttonLabels",
                    	        		lv_buttonLabels_4_0, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }

                    match(input,24,FOLLOW_24_in_ruleSwitch1594); 

                            createLeafNode(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_2(), null); 
                        

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
    // $ANTLR end ruleSwitch


    // $ANTLR start entryRuleSelection
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:931:1: entryRuleSelection returns [EObject current=null] : iv_ruleSelection= ruleSelection EOF ;
    public final EObject entryRuleSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelection = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:932:2: (iv_ruleSelection= ruleSelection EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:933:2: iv_ruleSelection= ruleSelection EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSelectionRule(), currentNode); 
            pushFollow(FOLLOW_ruleSelection_in_entryRuleSelection1632);
            iv_ruleSelection=ruleSelection();
            _fsp--;

             current =iv_ruleSelection; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelection1642); 

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
    // $ANTLR end entryRuleSelection


    // $ANTLR start ruleSelection
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:940:1: ruleSelection returns [EObject current=null] : ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleSelection() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:945:6: ( ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:946:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:946:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:946:3: 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,25,FOLLOW_25_in_ruleSelection1677); 

                    createLeafNode(grammarAccess.getSelectionAccess().getSelectionKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:950:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:950:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,18,FOLLOW_18_in_ruleSelection1688); 

                    createLeafNode(grammarAccess.getSelectionAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:954:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:955:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:955:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:956:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection1705); 

            			createLeafNode(grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"item",
            	        		lv_item_2_0, 
            	        		"ID", 
            	        		lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

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
    // $ANTLR end ruleSelection


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleModel120 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleSitemap_in_ruleModel142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_entryRuleSitemap177 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemap187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSitemap229 = new BitSet(new long[]{0x0000000000007000L});
    public static final BitSet FOLLOW_12_in_ruleSitemap245 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap262 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_13_in_ruleSitemap280 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap297 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleSitemap314 = new BitSet(new long[]{0x00000000025B0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleSitemap335 = new BitSet(new long[]{0x00000000025B8000L});
    public static final BitSet FOLLOW_15_in_ruleSitemap346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget382 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_ruleWidget439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_ruleWidget468 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_ruleSelection_in_ruleWidget495 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_12_in_ruleWidget506 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget525 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget545 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleWidget566 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget652 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkableWidget662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_ruleLinkableWidget710 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleGroup_in_ruleLinkableWidget737 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleImage_in_ruleLinkableWidget764 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleFrame_in_ruleLinkableWidget791 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_12_in_ruleLinkableWidget802 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget821 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget841 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_13_in_ruleLinkableWidget862 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget881 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget901 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleLinkableWidget922 = new BitSet(new long[]{0x00000000025B0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleLinkableWidget943 = new BitSet(new long[]{0x00000000025B8000L});
    public static final BitSet FOLLOW_15_in_ruleLinkableWidget954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame992 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame1002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleFrame1037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText1082 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText1092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleText1127 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_ruleText1147 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleText1164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup1207 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup1217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleGroup1252 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleGroup1263 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroup1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage1322 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage1332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleImage1367 = new BitSet(new long[]{0x0000000000240000L});
    public static final BitSet FOLLOW_18_in_ruleImage1378 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleImage1395 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_ruleImage1413 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImage1430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch1472 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleSwitch1517 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleSwitch1528 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1545 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSwitch1562 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1579 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleSwitch1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_entryRuleSelection1632 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelection1642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleSelection1677 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleSelection1688 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection1705 = new BitSet(new long[]{0x0000000000000002L});

}