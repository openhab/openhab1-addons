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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'label='", "'icon='", "'{'", "'}'", "'Text'", "'item='", "'Group'", "'Image'", "'url='", "'Switch'", "'buttonLabels=['", "']'"
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

                if ( (LA3_0==16||(LA3_0>=18 && LA3_0<=19)||LA3_0==21) ) {
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:257:1: ruleWidget returns [EObject current=null] : ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) ;
    public final EObject ruleWidget() throws RecognitionException {
        EObject current = null;

        Token lv_label_5_1=null;
        Token lv_label_5_2=null;
        Token lv_icon_7_1=null;
        Token lv_icon_7_2=null;
        EObject this_Text_0 = null;

        EObject this_Group_1 = null;

        EObject this_Image_2 = null;

        EObject this_Switch_3 = null;

        EObject lv_children_9_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:262:6: ( ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch )
            int alt4=4;
            switch ( input.LA(1) ) {
            case 16:
                {
                alt4=1;
                }
                break;
            case 18:
                {
                alt4=2;
                }
                break;
            case 19:
                {
                alt4=3;
                }
                break;
            case 21:
                {
                alt4=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("263:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Switch_3= ruleSwitch )", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:264:5: this_Text_0= ruleText
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getTextParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleText_in_ruleWidget440);
                    this_Text_0=ruleText();
                    _fsp--;

                     
                            current = this_Text_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:274:5: this_Group_1= ruleGroup
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getGroupParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroup_in_ruleWidget467);
                    this_Group_1=ruleGroup();
                    _fsp--;

                     
                            current = this_Group_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:284:5: this_Image_2= ruleImage
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getImageParserRuleCall_0_2(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleImage_in_ruleWidget494);
                    this_Image_2=ruleImage();
                    _fsp--;

                     
                            current = this_Image_2; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:294:5: this_Switch_3= ruleSwitch
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_0_3(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleSwitch_in_ruleWidget521);
                    this_Switch_3=ruleSwitch();
                    _fsp--;

                     
                            current = this_Switch_3; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:302:2: ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==12) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:302:4: 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleWidget532); 

                            createLeafNode(grammarAccess.getWidgetAccess().getLabelKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:306:1: ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:307:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:307:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:308:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:308:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
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
                            new NoViableAltException("308:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )", 5, 0, input);

                        throw nvae;
                    }
                    switch (alt5) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:309:3: lv_label_5_1= RULE_ID
                            {
                            lv_label_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget551); 

                            			createLeafNode(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:330:8: lv_label_5_2= RULE_STRING
                            {
                            lv_label_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget571); 

                            			createLeafNode(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:354:4: ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==13) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:354:6: 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleWidget592); 

                            createLeafNode(grammarAccess.getWidgetAccess().getIconKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:358:1: ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:359:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:359:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:360:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:360:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
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
                            new NoViableAltException("360:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )", 7, 0, input);

                        throw nvae;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:361:3: lv_icon_7_1= RULE_ID
                            {
                            lv_icon_7_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget611); 

                            			createLeafNode(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:382:8: lv_icon_7_2= RULE_STRING
                            {
                            lv_icon_7_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget631); 

                            			createLeafNode(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:406:4: ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==14) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:406:6: '{' ( (lv_children_9_0= ruleWidget ) )+ '}'
                    {
                    match(input,14,FOLLOW_14_in_ruleWidget652); 

                            createLeafNode(grammarAccess.getWidgetAccess().getLeftCurlyBracketKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:410:1: ( (lv_children_9_0= ruleWidget ) )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==16||(LA9_0>=18 && LA9_0<=19)||LA9_0==21) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:411:1: (lv_children_9_0= ruleWidget )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:411:1: (lv_children_9_0= ruleWidget )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:412:3: lv_children_9_0= ruleWidget
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleWidget_in_ruleWidget673);
                    	    lv_children_9_0=ruleWidget();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getWidgetRule().getType().getClassifier());
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
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);

                    match(input,15,FOLLOW_15_in_ruleWidget684); 

                            createLeafNode(grammarAccess.getWidgetAccess().getRightCurlyBracketKeyword_3_2(), null); 
                        

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
    // $ANTLR end ruleWidget


    // $ANTLR start entryRuleText
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:446:1: entryRuleText returns [EObject current=null] : iv_ruleText= ruleText EOF ;
    public final EObject entryRuleText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleText = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:447:2: (iv_ruleText= ruleText EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:448:2: iv_ruleText= ruleText EOF
            {
             currentNode = createCompositeNode(grammarAccess.getTextRule(), currentNode); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText722);
            iv_ruleText=ruleText();
            _fsp--;

             current =iv_ruleText; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText732); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:455:1: ruleText returns [EObject current=null] : ( 'Text' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleText() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:460:6: ( ( 'Text' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:461:1: ( 'Text' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:461:1: ( 'Text' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:461:3: 'Text' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,16,FOLLOW_16_in_ruleText767); 

                    createLeafNode(grammarAccess.getTextAccess().getTextKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:465:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:465:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleText778); 

                    createLeafNode(grammarAccess.getTextAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:469:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:470:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:470:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:471:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleText795); 

            			createLeafNode(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getTextRule().getType().getClassifier());
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
    // $ANTLR end ruleText


    // $ANTLR start entryRuleGroup
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:501:1: entryRuleGroup returns [EObject current=null] : iv_ruleGroup= ruleGroup EOF ;
    public final EObject entryRuleGroup() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroup = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:502:2: (iv_ruleGroup= ruleGroup EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:503:2: iv_ruleGroup= ruleGroup EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup837);
            iv_ruleGroup=ruleGroup();
            _fsp--;

             current =iv_ruleGroup; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup847); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:510:1: ruleGroup returns [EObject current=null] : ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleGroup() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:515:6: ( ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:3: 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,18,FOLLOW_18_in_ruleGroup882); 

                    createLeafNode(grammarAccess.getGroupAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:520:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:520:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleGroup893); 

                    createLeafNode(grammarAccess.getGroupAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:524:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:525:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:525:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:526:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroup910); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:556:1: entryRuleImage returns [EObject current=null] : iv_ruleImage= ruleImage EOF ;
    public final EObject entryRuleImage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImage = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:557:2: (iv_ruleImage= ruleImage EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:558:2: iv_ruleImage= ruleImage EOF
            {
             currentNode = createCompositeNode(grammarAccess.getImageRule(), currentNode); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage952);
            iv_ruleImage=ruleImage();
            _fsp--;

             current =iv_ruleImage; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage962); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:565:1: ruleImage returns [EObject current=null] : ( 'Image' ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleImage() throws RecognitionException {
        EObject current = null;

        Token lv_url_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:570:6: ( ( 'Image' ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:571:1: ( 'Image' ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:571:1: ( 'Image' ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:571:3: 'Image' ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) )
            {
            match(input,19,FOLLOW_19_in_ruleImage997); 

                    createLeafNode(grammarAccess.getImageAccess().getImageKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:575:1: ( 'url=' ( (lv_url_2_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:575:3: 'url=' ( (lv_url_2_0= RULE_STRING ) )
            {
            match(input,20,FOLLOW_20_in_ruleImage1008); 

                    createLeafNode(grammarAccess.getImageAccess().getUrlKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:579:1: ( (lv_url_2_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:580:1: (lv_url_2_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:580:1: (lv_url_2_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:581:3: lv_url_2_0= RULE_STRING
            {
            lv_url_2_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImage1025); 

            			createLeafNode(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_1_1_0(), "url"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getImageRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"url",
            	        		lv_url_2_0, 
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:611:1: entryRuleSwitch returns [EObject current=null] : iv_ruleSwitch= ruleSwitch EOF ;
    public final EObject entryRuleSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSwitch = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:612:2: (iv_ruleSwitch= ruleSwitch EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:613:2: iv_ruleSwitch= ruleSwitch EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSwitchRule(), currentNode); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch1067);
            iv_ruleSwitch=ruleSwitch();
            _fsp--;

             current =iv_ruleSwitch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch1077); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:620:1: ruleSwitch returns [EObject current=null] : ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? ) ;
    public final EObject ruleSwitch() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_buttonLabels_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:625:6: ( ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:626:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:626:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:626:3: 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )?
            {
            match(input,21,FOLLOW_21_in_ruleSwitch1112); 

                    createLeafNode(grammarAccess.getSwitchAccess().getSwitchKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:630:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:630:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSwitch1123); 

                    createLeafNode(grammarAccess.getSwitchAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:634:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:635:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:635:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:636:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1140); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:658:3: ( 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==22) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:658:5: 'buttonLabels=[' ( (lv_buttonLabels_4_0= RULE_ID ) ) ']'
                    {
                    match(input,22,FOLLOW_22_in_ruleSwitch1157); 

                            createLeafNode(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:662:1: ( (lv_buttonLabels_4_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:663:1: (lv_buttonLabels_4_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:663:1: (lv_buttonLabels_4_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:664:3: lv_buttonLabels_4_0= RULE_ID
                    {
                    lv_buttonLabels_4_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1174); 

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

                    match(input,23,FOLLOW_23_in_ruleSwitch1189); 

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
    public static final BitSet FOLLOW_14_in_ruleSitemap314 = new BitSet(new long[]{0x00000000002D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleSitemap335 = new BitSet(new long[]{0x00000000002D8000L});
    public static final BitSet FOLLOW_15_in_ruleSitemap346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget382 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_ruleWidget440 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleGroup_in_ruleWidget467 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleImage_in_ruleWidget494 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleSwitch_in_ruleWidget521 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_12_in_ruleWidget532 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget551 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget571 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_13_in_ruleWidget592 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget611 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget631 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleWidget652 = new BitSet(new long[]{0x00000000002D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleWidget673 = new BitSet(new long[]{0x00000000002D8000L});
    public static final BitSet FOLLOW_15_in_ruleWidget684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText722 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleText767 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleText778 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleText795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup837 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleGroup882 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleGroup893 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroup910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage952 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleImage997 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleImage1008 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImage1025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch1067 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch1077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleSwitch1112 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSwitch1123 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1140 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_22_in_ruleSwitch1157 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1174 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_ruleSwitch1189 = new BitSet(new long[]{0x0000000000000002L});

}