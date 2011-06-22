package org.openhab.model.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.openhab.model.services.SitemapGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSitemapParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'label='", "'icon='", "'{'", "'}'", "'Frame'", "'item='", "'Text'", "'Group'", "'Image'", "'url='", "'Switch'", "'mappings=['", "','", "']'", "'Slider'", "'sendFrequency='", "'switchSupport'", "'Selection'", "'List'", "'separator='", "'='"
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


        public InternalSitemapParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalSitemapParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalSitemapParser.tokenNames; }
    public String getGrammarFileName() { return "../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g"; }



     	private SitemapGrammarAccess grammarAccess;
     	
        public InternalSitemapParser(TokenStream input, SitemapGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "SitemapModel";	
       	}
       	
       	@Override
       	protected SitemapGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleSitemapModel"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:67:1: entryRuleSitemapModel returns [EObject current=null] : iv_ruleSitemapModel= ruleSitemapModel EOF ;
    public final EObject entryRuleSitemapModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSitemapModel = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:68:2: (iv_ruleSitemapModel= ruleSitemapModel EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:69:2: iv_ruleSitemapModel= ruleSitemapModel EOF
            {
             newCompositeNode(grammarAccess.getSitemapModelRule()); 
            pushFollow(FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel75);
            iv_ruleSitemapModel=ruleSitemapModel();

            state._fsp--;

             current =iv_ruleSitemapModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSitemapModel85); 

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
    // $ANTLR end "entryRuleSitemapModel"


    // $ANTLR start "ruleSitemapModel"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:76:1: ruleSitemapModel returns [EObject current=null] : (otherlv_0= 'sitemap' this_Sitemap_1= ruleSitemap ) ;
    public final EObject ruleSitemapModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject this_Sitemap_1 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:79:28: ( (otherlv_0= 'sitemap' this_Sitemap_1= ruleSitemap ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:80:1: (otherlv_0= 'sitemap' this_Sitemap_1= ruleSitemap )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:80:1: (otherlv_0= 'sitemap' this_Sitemap_1= ruleSitemap )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:80:3: otherlv_0= 'sitemap' this_Sitemap_1= ruleSitemap
            {
            otherlv_0=(Token)match(input,11,FOLLOW_11_in_ruleSitemapModel122); 

                	newLeafNode(otherlv_0, grammarAccess.getSitemapModelAccess().getSitemapKeyword_0());
                
             
                    newCompositeNode(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1()); 
                
            pushFollow(FOLLOW_ruleSitemap_in_ruleSitemapModel144);
            this_Sitemap_1=ruleSitemap();

            state._fsp--;

             
                    current = this_Sitemap_1; 
                    afterParserOrEnumRuleCall();
                

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
    // $ANTLR end "ruleSitemapModel"


    // $ANTLR start "entryRuleSitemap"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:101:1: entryRuleSitemap returns [EObject current=null] : iv_ruleSitemap= ruleSitemap EOF ;
    public final EObject entryRuleSitemap() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSitemap = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:102:2: (iv_ruleSitemap= ruleSitemap EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:103:2: iv_ruleSitemap= ruleSitemap EOF
            {
             newCompositeNode(grammarAccess.getSitemapRule()); 
            pushFollow(FOLLOW_ruleSitemap_in_entryRuleSitemap179);
            iv_ruleSitemap=ruleSitemap();

            state._fsp--;

             current =iv_ruleSitemap; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSitemap189); 

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
    // $ANTLR end "entryRuleSitemap"


    // $ANTLR start "ruleSitemap"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:110:1: ruleSitemap returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? otherlv_5= '{' ( (lv_children_6_0= ruleWidget ) )+ otherlv_7= '}' ) ;
    public final EObject ruleSitemap() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token lv_label_2_0=null;
        Token otherlv_3=null;
        Token lv_icon_4_0=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        EObject lv_children_6_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:113:28: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? otherlv_5= '{' ( (lv_children_6_0= ruleWidget ) )+ otherlv_7= '}' ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:114:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? otherlv_5= '{' ( (lv_children_6_0= ruleWidget ) )+ otherlv_7= '}' )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:114:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? otherlv_5= '{' ( (lv_children_6_0= ruleWidget ) )+ otherlv_7= '}' )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:114:2: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )? (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )? otherlv_5= '{' ( (lv_children_6_0= ruleWidget ) )+ otherlv_7= '}'
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:114:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:115:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:115:1: (lv_name_0_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:116:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSitemap231); 

            			newLeafNode(lv_name_0_0, grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSitemapRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:132:2: (otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) ) )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==12) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:132:4: otherlv_1= 'label=' ( (lv_label_2_0= RULE_STRING ) )
                    {
                    otherlv_1=(Token)match(input,12,FOLLOW_12_in_ruleSitemap249); 

                        	newLeafNode(otherlv_1, grammarAccess.getSitemapAccess().getLabelKeyword_1_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:136:1: ( (lv_label_2_0= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:137:1: (lv_label_2_0= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:137:1: (lv_label_2_0= RULE_STRING )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:138:3: lv_label_2_0= RULE_STRING
                    {
                    lv_label_2_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSitemap266); 

                    			newLeafNode(lv_label_2_0, grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSitemapRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"label",
                            		lv_label_2_0, 
                            		"STRING");
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:154:4: (otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==13) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:154:6: otherlv_3= 'icon=' ( (lv_icon_4_0= RULE_STRING ) )
                    {
                    otherlv_3=(Token)match(input,13,FOLLOW_13_in_ruleSitemap286); 

                        	newLeafNode(otherlv_3, grammarAccess.getSitemapAccess().getIconKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:158:1: ( (lv_icon_4_0= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:159:1: (lv_icon_4_0= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:159:1: (lv_icon_4_0= RULE_STRING )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:160:3: lv_icon_4_0= RULE_STRING
                    {
                    lv_icon_4_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSitemap303); 

                    			newLeafNode(lv_icon_4_0, grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSitemapRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"icon",
                            		lv_icon_4_0, 
                            		"STRING");
                    	    

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_14_in_ruleSitemap322); 

                	newLeafNode(otherlv_5, grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:180:1: ( (lv_children_6_0= ruleWidget ) )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==16||(LA3_0>=18 && LA3_0<=20)||LA3_0==22||LA3_0==26||(LA3_0>=29 && LA3_0<=30)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:181:1: (lv_children_6_0= ruleWidget )
            	    {
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:181:1: (lv_children_6_0= ruleWidget )
            	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:182:3: lv_children_6_0= ruleWidget
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleWidget_in_ruleSitemap343);
            	    lv_children_6_0=ruleWidget();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getSitemapRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"children",
            	            		lv_children_6_0, 
            	            		"Widget");
            	    	        afterParserOrEnumRuleCall();
            	    	    

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

            otherlv_7=(Token)match(input,15,FOLLOW_15_in_ruleSitemap356); 

                	newLeafNode(otherlv_7, grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5());
                

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
    // $ANTLR end "ruleSitemap"


    // $ANTLR start "entryRuleWidget"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:210:1: entryRuleWidget returns [EObject current=null] : iv_ruleWidget= ruleWidget EOF ;
    public final EObject entryRuleWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:211:2: (iv_ruleWidget= ruleWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:212:2: iv_ruleWidget= ruleWidget EOF
            {
             newCompositeNode(grammarAccess.getWidgetRule()); 
            pushFollow(FOLLOW_ruleWidget_in_entryRuleWidget392);
            iv_ruleWidget=ruleWidget();

            state._fsp--;

             current =iv_ruleWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWidget402); 

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
    // $ANTLR end "entryRuleWidget"


    // $ANTLR start "ruleWidget"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:219:1: ruleWidget returns [EObject current=null] : (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) ) ;
    public final EObject ruleWidget() throws RecognitionException {
        EObject current = null;

        EObject this_LinkableWidget_0 = null;

        EObject this_Switch_1 = null;

        EObject this_Selection_2 = null;

        EObject this_Slider_3 = null;

        EObject this_List_4 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:222:28: ( (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:223:1: (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:223:1: (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==16||(LA5_0>=18 && LA5_0<=20)) ) {
                alt5=1;
            }
            else if ( (LA5_0==22||LA5_0==26||(LA5_0>=29 && LA5_0<=30)) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:224:5: this_LinkableWidget_0= ruleLinkableWidget
                    {
                     
                            newCompositeNode(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleLinkableWidget_in_ruleWidget449);
                    this_LinkableWidget_0=ruleLinkableWidget();

                    state._fsp--;

                     
                            current = this_LinkableWidget_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:233:6: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:233:6: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList )
                    int alt4=4;
                    switch ( input.LA(1) ) {
                    case 22:
                        {
                        alt4=1;
                        }
                        break;
                    case 29:
                        {
                        alt4=2;
                        }
                        break;
                    case 26:
                        {
                        alt4=3;
                        }
                        break;
                    case 30:
                        {
                        alt4=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;
                    }

                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:234:5: this_Switch_1= ruleSwitch
                            {
                             
                                    newCompositeNode(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0()); 
                                
                            pushFollow(FOLLOW_ruleSwitch_in_ruleWidget477);
                            this_Switch_1=ruleSwitch();

                            state._fsp--;

                             
                                    current = this_Switch_1; 
                                    afterParserOrEnumRuleCall();
                                

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:244:5: this_Selection_2= ruleSelection
                            {
                             
                                    newCompositeNode(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_1()); 
                                
                            pushFollow(FOLLOW_ruleSelection_in_ruleWidget504);
                            this_Selection_2=ruleSelection();

                            state._fsp--;

                             
                                    current = this_Selection_2; 
                                    afterParserOrEnumRuleCall();
                                

                            }
                            break;
                        case 3 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:254:5: this_Slider_3= ruleSlider
                            {
                             
                                    newCompositeNode(grammarAccess.getWidgetAccess().getSliderParserRuleCall_1_2()); 
                                
                            pushFollow(FOLLOW_ruleSlider_in_ruleWidget531);
                            this_Slider_3=ruleSlider();

                            state._fsp--;

                             
                                    current = this_Slider_3; 
                                    afterParserOrEnumRuleCall();
                                

                            }
                            break;
                        case 4 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:264:5: this_List_4= ruleList
                            {
                             
                                    newCompositeNode(grammarAccess.getWidgetAccess().getListParserRuleCall_1_3()); 
                                
                            pushFollow(FOLLOW_ruleList_in_ruleWidget558);
                            this_List_4=ruleList();

                            state._fsp--;

                             
                                    current = this_List_4; 
                                    afterParserOrEnumRuleCall();
                                

                            }
                            break;

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
    // $ANTLR end "ruleWidget"


    // $ANTLR start "entryRuleLinkableWidget"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:280:1: entryRuleLinkableWidget returns [EObject current=null] : iv_ruleLinkableWidget= ruleLinkableWidget EOF ;
    public final EObject entryRuleLinkableWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLinkableWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:281:2: (iv_ruleLinkableWidget= ruleLinkableWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:282:2: iv_ruleLinkableWidget= ruleLinkableWidget EOF
            {
             newCompositeNode(grammarAccess.getLinkableWidgetRule()); 
            pushFollow(FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget594);
            iv_ruleLinkableWidget=ruleLinkableWidget();

            state._fsp--;

             current =iv_ruleLinkableWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkableWidget604); 

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
    // $ANTLR end "entryRuleLinkableWidget"


    // $ANTLR start "ruleLinkableWidget"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:289:1: ruleLinkableWidget returns [EObject current=null] : ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )? ) ;
    public final EObject ruleLinkableWidget() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        Token lv_label_5_1=null;
        Token lv_label_5_2=null;
        Token otherlv_6=null;
        Token lv_icon_7_1=null;
        Token lv_icon_7_2=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        EObject this_Text_0 = null;

        EObject this_Group_1 = null;

        EObject this_Image_2 = null;

        EObject this_Frame_3 = null;

        EObject lv_children_9_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:292:28: ( ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:293:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:293:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:293:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )?
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:293:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )
            int alt6=4;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt6=1;
                }
                break;
            case 19:
                {
                alt6=2;
                }
                break;
            case 20:
                {
                alt6=3;
                }
                break;
            case 16:
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:294:5: this_Text_0= ruleText
                    {
                     
                            newCompositeNode(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 
                        
                    pushFollow(FOLLOW_ruleText_in_ruleLinkableWidget652);
                    this_Text_0=ruleText();

                    state._fsp--;

                     
                            current = this_Text_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:304:5: this_Group_1= ruleGroup
                    {
                     
                            newCompositeNode(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 
                        
                    pushFollow(FOLLOW_ruleGroup_in_ruleLinkableWidget679);
                    this_Group_1=ruleGroup();

                    state._fsp--;

                     
                            current = this_Group_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:314:5: this_Image_2= ruleImage
                    {
                     
                            newCompositeNode(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 
                        
                    pushFollow(FOLLOW_ruleImage_in_ruleLinkableWidget706);
                    this_Image_2=ruleImage();

                    state._fsp--;

                     
                            current = this_Image_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:324:5: this_Frame_3= ruleFrame
                    {
                     
                            newCompositeNode(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3()); 
                        
                    pushFollow(FOLLOW_ruleFrame_in_ruleLinkableWidget733);
                    this_Frame_3=ruleFrame();

                    state._fsp--;

                     
                            current = this_Frame_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:332:2: (otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==12) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:332:4: otherlv_4= 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    {
                    otherlv_4=(Token)match(input,12,FOLLOW_12_in_ruleLinkableWidget746); 

                        	newLeafNode(otherlv_4, grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:336:1: ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:337:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:337:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:338:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:338:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
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
                            new NoViableAltException("", 7, 0, input);

                        throw nvae;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:339:3: lv_label_5_1= RULE_ID
                            {
                            lv_label_5_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget765); 

                            			newLeafNode(lv_label_5_1, grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getLinkableWidgetRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_5_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:354:8: lv_label_5_2= RULE_STRING
                            {
                            lv_label_5_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget785); 

                            			newLeafNode(lv_label_5_2, grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getLinkableWidgetRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_5_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:372:4: (otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==13) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:372:6: otherlv_6= 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    {
                    otherlv_6=(Token)match(input,13,FOLLOW_13_in_ruleLinkableWidget808); 

                        	newLeafNode(otherlv_6, grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:376:1: ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:377:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:377:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:378:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:378:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==RULE_ID) ) {
                        alt9=1;
                    }
                    else if ( (LA9_0==RULE_STRING) ) {
                        alt9=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                        throw nvae;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:379:3: lv_icon_7_1= RULE_ID
                            {
                            lv_icon_7_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget827); 

                            			newLeafNode(lv_icon_7_1, grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getLinkableWidgetRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_7_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:394:8: lv_icon_7_2= RULE_STRING
                            {
                            lv_icon_7_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget847); 

                            			newLeafNode(lv_icon_7_2, grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getLinkableWidgetRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_7_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:412:4: (otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==14) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:412:6: otherlv_8= '{' ( (lv_children_9_0= ruleWidget ) )+ otherlv_10= '}'
                    {
                    otherlv_8=(Token)match(input,14,FOLLOW_14_in_ruleLinkableWidget870); 

                        	newLeafNode(otherlv_8, grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:416:1: ( (lv_children_9_0= ruleWidget ) )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==16||(LA11_0>=18 && LA11_0<=20)||LA11_0==22||LA11_0==26||(LA11_0>=29 && LA11_0<=30)) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:417:1: (lv_children_9_0= ruleWidget )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:417:1: (lv_children_9_0= ruleWidget )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:418:3: lv_children_9_0= ruleWidget
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleWidget_in_ruleLinkableWidget891);
                    	    lv_children_9_0=ruleWidget();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getLinkableWidgetRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"children",
                    	            		lv_children_9_0, 
                    	            		"Widget");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    otherlv_10=(Token)match(input,15,FOLLOW_15_in_ruleLinkableWidget904); 

                        	newLeafNode(otherlv_10, grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2());
                        

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
    // $ANTLR end "ruleLinkableWidget"


    // $ANTLR start "entryRuleFrame"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:446:1: entryRuleFrame returns [EObject current=null] : iv_ruleFrame= ruleFrame EOF ;
    public final EObject entryRuleFrame() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFrame = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:447:2: (iv_ruleFrame= ruleFrame EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:448:2: iv_ruleFrame= ruleFrame EOF
            {
             newCompositeNode(grammarAccess.getFrameRule()); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame942);
            iv_ruleFrame=ruleFrame();

            state._fsp--;

             current =iv_ruleFrame; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame952); 

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
    // $ANTLR end "entryRuleFrame"


    // $ANTLR start "ruleFrame"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:455:1: ruleFrame returns [EObject current=null] : (otherlv_0= 'Frame' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleFrame() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token lv_item_3_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:458:28: ( (otherlv_0= 'Frame' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:459:1: (otherlv_0= 'Frame' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:459:1: (otherlv_0= 'Frame' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:459:3: otherlv_0= 'Frame' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            otherlv_0=(Token)match(input,16,FOLLOW_16_in_ruleFrame989); 

                	newLeafNode(otherlv_0, grammarAccess.getFrameAccess().getFrameKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:463:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:464:5: 
            {

                    current = forceCreateModelElement(
                        grammarAccess.getFrameAccess().getFrameAction_1(),
                        current);
                

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:469:2: (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==17) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:469:4: otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,17,FOLLOW_17_in_ruleFrame1011); 

                        	newLeafNode(otherlv_2, grammarAccess.getFrameAccess().getItemKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:473:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:474:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:474:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:475:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFrame1028); 

                    			newLeafNode(lv_item_3_0, grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getFrameRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"item",
                            		lv_item_3_0, 
                            		"ID");
                    	    

                    }


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
    // $ANTLR end "ruleFrame"


    // $ANTLR start "entryRuleText"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:499:1: entryRuleText returns [EObject current=null] : iv_ruleText= ruleText EOF ;
    public final EObject entryRuleText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleText = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:500:2: (iv_ruleText= ruleText EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:501:2: iv_ruleText= ruleText EOF
            {
             newCompositeNode(grammarAccess.getTextRule()); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText1071);
            iv_ruleText=ruleText();

            state._fsp--;

             current =iv_ruleText; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText1081); 

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
    // $ANTLR end "entryRuleText"


    // $ANTLR start "ruleText"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:508:1: ruleText returns [EObject current=null] : (otherlv_0= 'Text' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleText() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token lv_item_3_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:511:28: ( (otherlv_0= 'Text' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:512:1: (otherlv_0= 'Text' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:512:1: (otherlv_0= 'Text' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:512:3: otherlv_0= 'Text' () (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            otherlv_0=(Token)match(input,18,FOLLOW_18_in_ruleText1118); 

                	newLeafNode(otherlv_0, grammarAccess.getTextAccess().getTextKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:516:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:517:5: 
            {

                    current = forceCreateModelElement(
                        grammarAccess.getTextAccess().getTextAction_1(),
                        current);
                

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:522:2: (otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==17) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:522:4: otherlv_2= 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,17,FOLLOW_17_in_ruleText1140); 

                        	newLeafNode(otherlv_2, grammarAccess.getTextAccess().getItemKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:526:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:527:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:527:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:528:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleText1157); 

                    			newLeafNode(lv_item_3_0, grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getTextRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"item",
                            		lv_item_3_0, 
                            		"ID");
                    	    

                    }


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
    // $ANTLR end "ruleText"


    // $ANTLR start "entryRuleGroup"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:552:1: entryRuleGroup returns [EObject current=null] : iv_ruleGroup= ruleGroup EOF ;
    public final EObject entryRuleGroup() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroup = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:553:2: (iv_ruleGroup= ruleGroup EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:554:2: iv_ruleGroup= ruleGroup EOF
            {
             newCompositeNode(grammarAccess.getGroupRule()); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup1200);
            iv_ruleGroup=ruleGroup();

            state._fsp--;

             current =iv_ruleGroup; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup1210); 

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
    // $ANTLR end "entryRuleGroup"


    // $ANTLR start "ruleGroup"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:561:1: ruleGroup returns [EObject current=null] : (otherlv_0= 'Group' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleGroup() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:564:28: ( (otherlv_0= 'Group' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:565:1: (otherlv_0= 'Group' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:565:1: (otherlv_0= 'Group' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:565:3: otherlv_0= 'Group' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,19,FOLLOW_19_in_ruleGroup1247); 

                	newLeafNode(otherlv_0, grammarAccess.getGroupAccess().getGroupKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:569:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:569:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleGroup1260); 

                	newLeafNode(otherlv_1, grammarAccess.getGroupAccess().getItemKeyword_1_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:573:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:574:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:574:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:575:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroup1277); 

            			newLeafNode(lv_item_2_0, grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getGroupRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"item",
                    		lv_item_2_0, 
                    		"ID");
            	    

            }


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
    // $ANTLR end "ruleGroup"


    // $ANTLR start "entryRuleImage"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:599:1: entryRuleImage returns [EObject current=null] : iv_ruleImage= ruleImage EOF ;
    public final EObject entryRuleImage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImage = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:600:2: (iv_ruleImage= ruleImage EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:601:2: iv_ruleImage= ruleImage EOF
            {
             newCompositeNode(grammarAccess.getImageRule()); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage1319);
            iv_ruleImage=ruleImage();

            state._fsp--;

             current =iv_ruleImage; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage1329); 

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
    // $ANTLR end "entryRuleImage"


    // $ANTLR start "ruleImage"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:608:1: ruleImage returns [EObject current=null] : (otherlv_0= 'Image' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )? (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleImage() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;
        Token otherlv_3=null;
        Token lv_url_4_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:611:28: ( (otherlv_0= 'Image' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )? (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:612:1: (otherlv_0= 'Image' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )? (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:612:1: (otherlv_0= 'Image' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )? (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:612:3: otherlv_0= 'Image' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )? (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            {
            otherlv_0=(Token)match(input,20,FOLLOW_20_in_ruleImage1366); 

                	newLeafNode(otherlv_0, grammarAccess.getImageAccess().getImageKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:616:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==17) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:616:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
                    {
                    otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleImage1379); 

                        	newLeafNode(otherlv_1, grammarAccess.getImageAccess().getItemKeyword_1_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:620:1: ( (lv_item_2_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:621:1: (lv_item_2_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:621:1: (lv_item_2_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:622:3: lv_item_2_0= RULE_ID
                    {
                    lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleImage1396); 

                    			newLeafNode(lv_item_2_0, grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getImageRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"item",
                            		lv_item_2_0, 
                            		"ID");
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:638:4: (otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:638:6: otherlv_3= 'url=' ( (lv_url_4_0= RULE_STRING ) )
            {
            otherlv_3=(Token)match(input,21,FOLLOW_21_in_ruleImage1416); 

                	newLeafNode(otherlv_3, grammarAccess.getImageAccess().getUrlKeyword_2_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:642:1: ( (lv_url_4_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:643:1: (lv_url_4_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:643:1: (lv_url_4_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:644:3: lv_url_4_0= RULE_STRING
            {
            lv_url_4_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImage1433); 

            			newLeafNode(lv_url_4_0, grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getImageRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"url",
                    		lv_url_4_0, 
                    		"STRING");
            	    

            }


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
    // $ANTLR end "ruleImage"


    // $ANTLR start "entryRuleSwitch"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:668:1: entryRuleSwitch returns [EObject current=null] : iv_ruleSwitch= ruleSwitch EOF ;
    public final EObject entryRuleSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSwitch = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:669:2: (iv_ruleSwitch= ruleSwitch EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:670:2: iv_ruleSwitch= ruleSwitch EOF
            {
             newCompositeNode(grammarAccess.getSwitchRule()); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch1475);
            iv_ruleSwitch=ruleSwitch();

            state._fsp--;

             current =iv_ruleSwitch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch1485); 

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
    // $ANTLR end "entryRuleSwitch"


    // $ANTLR start "ruleSwitch"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:677:1: ruleSwitch returns [EObject current=null] : (otherlv_0= 'Switch' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? ) ;
    public final EObject ruleSwitch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;
        Token otherlv_3=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token otherlv_5=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        EObject lv_mappings_8_0 = null;

        EObject lv_mappings_10_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:680:28: ( (otherlv_0= 'Switch' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:681:1: (otherlv_0= 'Switch' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:681:1: (otherlv_0= 'Switch' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:681:3: otherlv_0= 'Switch' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )?
            {
            otherlv_0=(Token)match(input,22,FOLLOW_22_in_ruleSwitch1522); 

                	newLeafNode(otherlv_0, grammarAccess.getSwitchAccess().getSwitchKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:685:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:685:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleSwitch1535); 

                	newLeafNode(otherlv_1, grammarAccess.getSwitchAccess().getItemKeyword_1_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:689:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:690:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:690:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:691:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1552); 

            			newLeafNode(lv_item_2_0, grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSwitchRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"item",
                    		lv_item_2_0, 
                    		"ID");
            	    

            }


            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:707:3: (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==12) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:707:5: otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleSwitch1571); 

                        	newLeafNode(otherlv_3, grammarAccess.getSwitchAccess().getLabelKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:711:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:712:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:712:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:713:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:713:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==RULE_ID) ) {
                        alt16=1;
                    }
                    else if ( (LA16_0==RULE_STRING) ) {
                        alt16=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 0, input);

                        throw nvae;
                    }
                    switch (alt16) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:714:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1590); 

                            			newLeafNode(lv_label_4_1, grammarAccess.getSwitchAccess().getLabelIDTerminalRuleCall_2_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSwitchRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:729:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSwitch1610); 

                            			newLeafNode(lv_label_4_2, grammarAccess.getSwitchAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSwitchRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:747:4: (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==13) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:747:6: otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    otherlv_5=(Token)match(input,13,FOLLOW_13_in_ruleSwitch1633); 

                        	newLeafNode(otherlv_5, grammarAccess.getSwitchAccess().getIconKeyword_3_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:751:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:752:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:752:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:753:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:753:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==RULE_ID) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==RULE_STRING) ) {
                        alt18=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:754:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1652); 

                            			newLeafNode(lv_icon_6_1, grammarAccess.getSwitchAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSwitchRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:769:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSwitch1672); 

                            			newLeafNode(lv_icon_6_2, grammarAccess.getSwitchAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSwitchRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:787:4: (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==23) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:787:6: otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']'
                    {
                    otherlv_7=(Token)match(input,23,FOLLOW_23_in_ruleSwitch1695); 

                        	newLeafNode(otherlv_7, grammarAccess.getSwitchAccess().getMappingsKeyword_4_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:791:1: ( (lv_mappings_8_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:792:1: (lv_mappings_8_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:792:1: (lv_mappings_8_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:793:3: lv_mappings_8_0= ruleMapping
                    {
                     
                    	        newCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_4_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1716);
                    lv_mappings_8_0=ruleMapping();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSwitchRule());
                    	        }
                           		add(
                           			current, 
                           			"mappings",
                            		lv_mappings_8_0, 
                            		"Mapping");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:809:2: (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==24) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:809:4: otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) )
                    	    {
                    	    otherlv_9=(Token)match(input,24,FOLLOW_24_in_ruleSwitch1729); 

                    	        	newLeafNode(otherlv_9, grammarAccess.getSwitchAccess().getCommaKeyword_4_2_0());
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:813:1: ( (lv_mappings_10_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:1: (lv_mappings_10_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:1: (lv_mappings_10_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:815:3: lv_mappings_10_0= ruleMapping
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_4_2_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1750);
                    	    lv_mappings_10_0=ruleMapping();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getSwitchRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"mappings",
                    	            		lv_mappings_10_0, 
                    	            		"Mapping");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    otherlv_11=(Token)match(input,25,FOLLOW_25_in_ruleSwitch1764); 

                        	newLeafNode(otherlv_11, grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_4_3());
                        

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
    // $ANTLR end "ruleSwitch"


    // $ANTLR start "entryRuleSlider"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:843:1: entryRuleSlider returns [EObject current=null] : iv_ruleSlider= ruleSlider EOF ;
    public final EObject entryRuleSlider() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSlider = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:844:2: (iv_ruleSlider= ruleSlider EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:845:2: iv_ruleSlider= ruleSlider EOF
            {
             newCompositeNode(grammarAccess.getSliderRule()); 
            pushFollow(FOLLOW_ruleSlider_in_entryRuleSlider1802);
            iv_ruleSlider=ruleSlider();

            state._fsp--;

             current =iv_ruleSlider; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSlider1812); 

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
    // $ANTLR end "entryRuleSlider"


    // $ANTLR start "ruleSlider"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:852:1: ruleSlider returns [EObject current=null] : (otherlv_0= 'Slider' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? ) ;
    public final EObject ruleSlider() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;
        Token otherlv_3=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token otherlv_5=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token otherlv_7=null;
        Token lv_frequency_8_0=null;
        Token lv_switchEnabled_9_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:855:28: ( (otherlv_0= 'Slider' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:856:1: (otherlv_0= 'Slider' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:856:1: (otherlv_0= 'Slider' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:856:3: otherlv_0= 'Slider' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )?
            {
            otherlv_0=(Token)match(input,26,FOLLOW_26_in_ruleSlider1849); 

                	newLeafNode(otherlv_0, grammarAccess.getSliderAccess().getSliderKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:860:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:860:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleSlider1862); 

                	newLeafNode(otherlv_1, grammarAccess.getSliderAccess().getItemKeyword_1_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:864:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:865:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:865:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:866:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1879); 

            			newLeafNode(lv_item_2_0, grammarAccess.getSliderAccess().getItemIDTerminalRuleCall_1_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSliderRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"item",
                    		lv_item_2_0, 
                    		"ID");
            	    

            }


            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:882:3: (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==12) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:882:5: otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleSlider1898); 

                        	newLeafNode(otherlv_3, grammarAccess.getSliderAccess().getLabelKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:886:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:887:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:887:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:888:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:888:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==RULE_ID) ) {
                        alt22=1;
                    }
                    else if ( (LA22_0==RULE_STRING) ) {
                        alt22=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 0, input);

                        throw nvae;
                    }
                    switch (alt22) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:889:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1917); 

                            			newLeafNode(lv_label_4_1, grammarAccess.getSliderAccess().getLabelIDTerminalRuleCall_2_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSliderRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:904:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSlider1937); 

                            			newLeafNode(lv_label_4_2, grammarAccess.getSliderAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSliderRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:922:4: (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==13) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:922:6: otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    otherlv_5=(Token)match(input,13,FOLLOW_13_in_ruleSlider1960); 

                        	newLeafNode(otherlv_5, grammarAccess.getSliderAccess().getIconKeyword_3_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:926:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:927:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:927:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:928:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:928:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==RULE_ID) ) {
                        alt24=1;
                    }
                    else if ( (LA24_0==RULE_STRING) ) {
                        alt24=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 24, 0, input);

                        throw nvae;
                    }
                    switch (alt24) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:929:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1979); 

                            			newLeafNode(lv_icon_6_1, grammarAccess.getSliderAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSliderRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:944:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSlider1999); 

                            			newLeafNode(lv_icon_6_2, grammarAccess.getSliderAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSliderRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:962:4: (otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==27) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:962:6: otherlv_7= 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) )
                    {
                    otherlv_7=(Token)match(input,27,FOLLOW_27_in_ruleSlider2022); 

                        	newLeafNode(otherlv_7, grammarAccess.getSliderAccess().getSendFrequencyKeyword_4_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:966:1: ( (lv_frequency_8_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:967:1: (lv_frequency_8_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:967:1: (lv_frequency_8_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:968:3: lv_frequency_8_0= RULE_ID
                    {
                    lv_frequency_8_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider2039); 

                    			newLeafNode(lv_frequency_8_0, grammarAccess.getSliderAccess().getFrequencyIDTerminalRuleCall_4_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSliderRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"frequency",
                            		lv_frequency_8_0, 
                            		"ID");
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:984:4: ( (lv_switchEnabled_9_0= 'switchSupport' ) )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==28) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:985:1: (lv_switchEnabled_9_0= 'switchSupport' )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:985:1: (lv_switchEnabled_9_0= 'switchSupport' )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:986:3: lv_switchEnabled_9_0= 'switchSupport'
                    {
                    lv_switchEnabled_9_0=(Token)match(input,28,FOLLOW_28_in_ruleSlider2064); 

                            newLeafNode(lv_switchEnabled_9_0, grammarAccess.getSliderAccess().getSwitchEnabledSwitchSupportKeyword_5_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSliderRule());
                    	        }
                           		setWithLastConsumed(current, "switchEnabled", true, "switchSupport");
                    	    

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
    // $ANTLR end "ruleSlider"


    // $ANTLR start "entryRuleSelection"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1007:1: entryRuleSelection returns [EObject current=null] : iv_ruleSelection= ruleSelection EOF ;
    public final EObject entryRuleSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelection = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1008:2: (iv_ruleSelection= ruleSelection EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1009:2: iv_ruleSelection= ruleSelection EOF
            {
             newCompositeNode(grammarAccess.getSelectionRule()); 
            pushFollow(FOLLOW_ruleSelection_in_entryRuleSelection2114);
            iv_ruleSelection=ruleSelection();

            state._fsp--;

             current =iv_ruleSelection; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelection2124); 

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
    // $ANTLR end "entryRuleSelection"


    // $ANTLR start "ruleSelection"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1016:1: ruleSelection returns [EObject current=null] : (otherlv_0= 'Selection' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? ) ;
    public final EObject ruleSelection() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;
        Token otherlv_3=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token otherlv_5=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        EObject lv_mappings_8_0 = null;

        EObject lv_mappings_10_0 = null;


         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1019:28: ( (otherlv_0= 'Selection' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1020:1: (otherlv_0= 'Selection' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1020:1: (otherlv_0= 'Selection' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1020:3: otherlv_0= 'Selection' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )?
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleSelection2161); 

                	newLeafNode(otherlv_0, grammarAccess.getSelectionAccess().getSelectionKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1024:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1024:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleSelection2174); 

                	newLeafNode(otherlv_1, grammarAccess.getSelectionAccess().getItemKeyword_1_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1028:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1029:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1029:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1030:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2191); 

            			newLeafNode(lv_item_2_0, grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSelectionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"item",
                    		lv_item_2_0, 
                    		"ID");
            	    

            }


            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1046:3: (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==12) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1046:5: otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleSelection2210); 

                        	newLeafNode(otherlv_3, grammarAccess.getSelectionAccess().getLabelKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1050:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1051:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1051:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1052:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1052:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==RULE_ID) ) {
                        alt28=1;
                    }
                    else if ( (LA28_0==RULE_STRING) ) {
                        alt28=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 28, 0, input);

                        throw nvae;
                    }
                    switch (alt28) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1053:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2229); 

                            			newLeafNode(lv_label_4_1, grammarAccess.getSelectionAccess().getLabelIDTerminalRuleCall_2_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSelectionRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1068:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSelection2249); 

                            			newLeafNode(lv_label_4_2, grammarAccess.getSelectionAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSelectionRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1086:4: (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==13) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1086:6: otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    otherlv_5=(Token)match(input,13,FOLLOW_13_in_ruleSelection2272); 

                        	newLeafNode(otherlv_5, grammarAccess.getSelectionAccess().getIconKeyword_3_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1090:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1091:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1091:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1092:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1092:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==RULE_ID) ) {
                        alt30=1;
                    }
                    else if ( (LA30_0==RULE_STRING) ) {
                        alt30=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 30, 0, input);

                        throw nvae;
                    }
                    switch (alt30) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1093:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2291); 

                            			newLeafNode(lv_icon_6_1, grammarAccess.getSelectionAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSelectionRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1108:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSelection2311); 

                            			newLeafNode(lv_icon_6_2, grammarAccess.getSelectionAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getSelectionRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1126:4: (otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']' )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==23) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1126:6: otherlv_7= 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )* otherlv_11= ']'
                    {
                    otherlv_7=(Token)match(input,23,FOLLOW_23_in_ruleSelection2334); 

                        	newLeafNode(otherlv_7, grammarAccess.getSelectionAccess().getMappingsKeyword_4_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1130:1: ( (lv_mappings_8_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1131:1: (lv_mappings_8_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1131:1: (lv_mappings_8_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1132:3: lv_mappings_8_0= ruleMapping
                    {
                     
                    	        newCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_4_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSelection2355);
                    lv_mappings_8_0=ruleMapping();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSelectionRule());
                    	        }
                           		add(
                           			current, 
                           			"mappings",
                            		lv_mappings_8_0, 
                            		"Mapping");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1148:2: (otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==24) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1148:4: otherlv_9= ',' ( (lv_mappings_10_0= ruleMapping ) )
                    	    {
                    	    otherlv_9=(Token)match(input,24,FOLLOW_24_in_ruleSelection2368); 

                    	        	newLeafNode(otherlv_9, grammarAccess.getSelectionAccess().getCommaKeyword_4_2_0());
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1152:1: ( (lv_mappings_10_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1153:1: (lv_mappings_10_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1153:1: (lv_mappings_10_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1154:3: lv_mappings_10_0= ruleMapping
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_4_2_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSelection2389);
                    	    lv_mappings_10_0=ruleMapping();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getSelectionRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"mappings",
                    	            		lv_mappings_10_0, 
                    	            		"Mapping");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop32;
                        }
                    } while (true);

                    otherlv_11=(Token)match(input,25,FOLLOW_25_in_ruleSelection2403); 

                        	newLeafNode(otherlv_11, grammarAccess.getSelectionAccess().getRightSquareBracketKeyword_4_3());
                        

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
    // $ANTLR end "ruleSelection"


    // $ANTLR start "entryRuleList"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1182:1: entryRuleList returns [EObject current=null] : iv_ruleList= ruleList EOF ;
    public final EObject entryRuleList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleList = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1183:2: (iv_ruleList= ruleList EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1184:2: iv_ruleList= ruleList EOF
            {
             newCompositeNode(grammarAccess.getListRule()); 
            pushFollow(FOLLOW_ruleList_in_entryRuleList2441);
            iv_ruleList=ruleList();

            state._fsp--;

             current =iv_ruleList; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleList2451); 

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
    // $ANTLR end "entryRuleList"


    // $ANTLR start "ruleList"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1191:1: ruleList returns [EObject current=null] : (otherlv_0= 'List' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleList() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_item_2_0=null;
        Token otherlv_3=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token otherlv_5=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token otherlv_7=null;
        Token lv_separator_8_0=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1194:28: ( (otherlv_0= 'List' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1195:1: (otherlv_0= 'List' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1195:1: (otherlv_0= 'List' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1195:3: otherlv_0= 'List' (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) ) (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_30_in_ruleList2488); 

                	newLeafNode(otherlv_0, grammarAccess.getListAccess().getListKeyword_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1199:1: (otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1199:3: otherlv_1= 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            otherlv_1=(Token)match(input,17,FOLLOW_17_in_ruleList2501); 

                	newLeafNode(otherlv_1, grammarAccess.getListAccess().getItemKeyword_1_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1203:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1204:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1204:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1205:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2518); 

            			newLeafNode(lv_item_2_0, grammarAccess.getListAccess().getItemIDTerminalRuleCall_1_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getListRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"item",
                    		lv_item_2_0, 
                    		"ID");
            	    

            }


            }


            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1221:3: (otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==12) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1221:5: otherlv_3= 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    otherlv_3=(Token)match(input,12,FOLLOW_12_in_ruleList2537); 

                        	newLeafNode(otherlv_3, grammarAccess.getListAccess().getLabelKeyword_2_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1225:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1226:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1226:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1227:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1227:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==RULE_ID) ) {
                        alt34=1;
                    }
                    else if ( (LA34_0==RULE_STRING) ) {
                        alt34=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 34, 0, input);

                        throw nvae;
                    }
                    switch (alt34) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1228:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2556); 

                            			newLeafNode(lv_label_4_1, grammarAccess.getListAccess().getLabelIDTerminalRuleCall_2_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getListRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1243:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2576); 

                            			newLeafNode(lv_label_4_2, grammarAccess.getListAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getListRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"label",
                                    		lv_label_4_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1261:4: (otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==13) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1261:6: otherlv_5= 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    otherlv_5=(Token)match(input,13,FOLLOW_13_in_ruleList2599); 

                        	newLeafNode(otherlv_5, grammarAccess.getListAccess().getIconKeyword_3_0());
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1265:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1266:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1266:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1267:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1267:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==RULE_ID) ) {
                        alt36=1;
                    }
                    else if ( (LA36_0==RULE_STRING) ) {
                        alt36=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 36, 0, input);

                        throw nvae;
                    }
                    switch (alt36) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1268:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2618); 

                            			newLeafNode(lv_icon_6_1, grammarAccess.getListAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getListRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_1, 
                                    		"ID");
                            	    

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1283:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2638); 

                            			newLeafNode(lv_icon_6_2, grammarAccess.getListAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getListRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"icon",
                                    		lv_icon_6_2, 
                                    		"STRING");
                            	    

                            }
                            break;

                    }


                    }


                    }


                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1301:4: (otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1301:6: otherlv_7= 'separator=' ( (lv_separator_8_0= RULE_STRING ) )
            {
            otherlv_7=(Token)match(input,31,FOLLOW_31_in_ruleList2661); 

                	newLeafNode(otherlv_7, grammarAccess.getListAccess().getSeparatorKeyword_4_0());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1305:1: ( (lv_separator_8_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1306:1: (lv_separator_8_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1306:1: (lv_separator_8_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1307:3: lv_separator_8_0= RULE_STRING
            {
            lv_separator_8_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2678); 

            			newLeafNode(lv_separator_8_0, grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_4_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getListRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"separator",
                    		lv_separator_8_0, 
                    		"STRING");
            	    

            }


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
    // $ANTLR end "ruleList"


    // $ANTLR start "entryRuleMapping"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1331:1: entryRuleMapping returns [EObject current=null] : iv_ruleMapping= ruleMapping EOF ;
    public final EObject entryRuleMapping() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapping = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1332:2: (iv_ruleMapping= ruleMapping EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1333:2: iv_ruleMapping= ruleMapping EOF
            {
             newCompositeNode(grammarAccess.getMappingRule()); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping2720);
            iv_ruleMapping=ruleMapping();

            state._fsp--;

             current =iv_ruleMapping; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping2730); 

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
    // $ANTLR end "entryRuleMapping"


    // $ANTLR start "ruleMapping"
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1340:1: ruleMapping returns [EObject current=null] : ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) otherlv_1= '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) ;
    public final EObject ruleMapping() throws RecognitionException {
        EObject current = null;

        Token lv_cmd_0_1=null;
        Token lv_cmd_0_2=null;
        Token otherlv_1=null;
        Token lv_label_2_1=null;
        Token lv_label_2_2=null;

         enterRule(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1343:28: ( ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) otherlv_1= '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1344:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) otherlv_1= '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1344:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) otherlv_1= '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1344:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) otherlv_1= '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1344:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1345:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1345:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1346:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1346:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==RULE_ID) ) {
                alt38=1;
            }
            else if ( (LA38_0==RULE_STRING) ) {
                alt38=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1347:3: lv_cmd_0_1= RULE_ID
                    {
                    lv_cmd_0_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2774); 

                    			newLeafNode(lv_cmd_0_1, grammarAccess.getMappingAccess().getCmdIDTerminalRuleCall_0_0_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getMappingRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"cmd",
                            		lv_cmd_0_1, 
                            		"ID");
                    	    

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1362:8: lv_cmd_0_2= RULE_STRING
                    {
                    lv_cmd_0_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2794); 

                    			newLeafNode(lv_cmd_0_2, grammarAccess.getMappingAccess().getCmdSTRINGTerminalRuleCall_0_0_1()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getMappingRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"cmd",
                            		lv_cmd_0_2, 
                            		"STRING");
                    	    

                    }
                    break;

            }


            }


            }

            otherlv_1=(Token)match(input,32,FOLLOW_32_in_ruleMapping2814); 

                	newLeafNode(otherlv_1, grammarAccess.getMappingAccess().getEqualsSignKeyword_1());
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1384:1: ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1385:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1385:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1386:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1386:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==RULE_ID) ) {
                alt39=1;
            }
            else if ( (LA39_0==RULE_STRING) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1387:3: lv_label_2_1= RULE_ID
                    {
                    lv_label_2_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2833); 

                    			newLeafNode(lv_label_2_1, grammarAccess.getMappingAccess().getLabelIDTerminalRuleCall_2_0_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getMappingRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"label",
                            		lv_label_2_1, 
                            		"ID");
                    	    

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1402:8: lv_label_2_2= RULE_STRING
                    {
                    lv_label_2_2=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2853); 

                    			newLeafNode(lv_label_2_2, grammarAccess.getMappingAccess().getLabelSTRINGTerminalRuleCall_2_0_1()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getMappingRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"label",
                            		lv_label_2_2, 
                            		"STRING");
                    	    

                    }
                    break;

            }


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
    // $ANTLR end "ruleMapping"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemapModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleSitemapModel122 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleSitemap_in_ruleSitemapModel144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_entryRuleSitemap179 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemap189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSitemap231 = new BitSet(new long[]{0x0000000000007000L});
    public static final BitSet FOLLOW_12_in_ruleSitemap249 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap266 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_13_in_ruleSitemap286 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap303 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleSitemap322 = new BitSet(new long[]{0x00000000645D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleSitemap343 = new BitSet(new long[]{0x00000000645D8000L});
    public static final BitSet FOLLOW_15_in_ruleSitemap356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget392 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_ruleWidget449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_ruleWidget477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_ruleWidget504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSlider_in_ruleWidget531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_ruleWidget558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget594 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkableWidget604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_ruleLinkableWidget652 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleGroup_in_ruleLinkableWidget679 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleImage_in_ruleLinkableWidget706 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleFrame_in_ruleLinkableWidget733 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_12_in_ruleLinkableWidget746 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget765 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget785 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_13_in_ruleLinkableWidget808 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget827 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget847 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleLinkableWidget870 = new BitSet(new long[]{0x00000000645D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleLinkableWidget891 = new BitSet(new long[]{0x00000000645D8000L});
    public static final BitSet FOLLOW_15_in_ruleLinkableWidget904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame942 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleFrame989 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleFrame1011 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFrame1028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText1071 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleText1118 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleText1140 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleText1157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup1200 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleGroup1247 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleGroup1260 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroup1277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage1319 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage1329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleImage1366 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_17_in_ruleImage1379 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleImage1396 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_ruleImage1416 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImage1433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch1475 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch1485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleSwitch1522 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSwitch1535 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1552 = new BitSet(new long[]{0x0000000000803002L});
    public static final BitSet FOLLOW_12_in_ruleSwitch1571 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1590 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSwitch1610 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_13_in_ruleSwitch1633 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1652 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSwitch1672 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSwitch1695 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1716 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSwitch1729 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1750 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSwitch1764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSlider_in_entryRuleSlider1802 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSlider1812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleSlider1849 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSlider1862 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1879 = new BitSet(new long[]{0x0000000018003002L});
    public static final BitSet FOLLOW_12_in_ruleSlider1898 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1917 = new BitSet(new long[]{0x0000000018002002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSlider1937 = new BitSet(new long[]{0x0000000018002002L});
    public static final BitSet FOLLOW_13_in_ruleSlider1960 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1979 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSlider1999 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_27_in_ruleSlider2022 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider2039 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_ruleSlider2064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_entryRuleSelection2114 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelection2124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleSelection2161 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSelection2174 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2191 = new BitSet(new long[]{0x0000000000803002L});
    public static final BitSet FOLLOW_12_in_ruleSelection2210 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2229 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSelection2249 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_13_in_ruleSelection2272 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2291 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSelection2311 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSelection2334 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection2355 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSelection2368 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection2389 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSelection2403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_entryRuleList2441 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleList2451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleList2488 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleList2501 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2518 = new BitSet(new long[]{0x0000000080003000L});
    public static final BitSet FOLLOW_12_in_ruleList2537 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2556 = new BitSet(new long[]{0x0000000080002000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2576 = new BitSet(new long[]{0x0000000080002000L});
    public static final BitSet FOLLOW_13_in_ruleList2599 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2618 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2638 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleList2661 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping2720 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping2730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2774 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2794 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleMapping2814 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2853 = new BitSet(new long[]{0x0000000000000002L});

}