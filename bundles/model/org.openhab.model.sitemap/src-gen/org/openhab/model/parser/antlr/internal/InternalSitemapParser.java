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
        	return "SitemapModel";	
       	}
       	
       	@Override
       	protected SitemapGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start entryRuleSitemapModel
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:77:1: entryRuleSitemapModel returns [EObject current=null] : iv_ruleSitemapModel= ruleSitemapModel EOF ;
    public final EObject entryRuleSitemapModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSitemapModel = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:78:2: (iv_ruleSitemapModel= ruleSitemapModel EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:79:2: iv_ruleSitemapModel= ruleSitemapModel EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSitemapModelRule(), currentNode); 
            pushFollow(FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel75);
            iv_ruleSitemapModel=ruleSitemapModel();
            _fsp--;

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
    // $ANTLR end entryRuleSitemapModel


    // $ANTLR start ruleSitemapModel
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:86:1: ruleSitemapModel returns [EObject current=null] : ( 'sitemap' this_Sitemap_1= ruleSitemap ) ;
    public final EObject ruleSitemapModel() throws RecognitionException {
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
            match(input,11,FOLLOW_11_in_ruleSitemapModel120); 

                    createLeafNode(grammarAccess.getSitemapModelAccess().getSitemapKeyword_0(), null); 
                
             
                    currentNode=createCompositeNode(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1(), currentNode); 
                
            pushFollow(FOLLOW_ruleSitemap_in_ruleSitemapModel142);
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
    // $ANTLR end ruleSitemapModel


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

                if ( (LA3_0==16||(LA3_0>=18 && LA3_0<=20)||LA3_0==22||LA3_0==26||(LA3_0>=29 && LA3_0<=30)) ) {
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:257:1: ruleWidget returns [EObject current=null] : (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) ) ;
    public final EObject ruleWidget() throws RecognitionException {
        EObject current = null;

        EObject this_LinkableWidget_0 = null;

        EObject this_Switch_1 = null;

        EObject this_Selection_2 = null;

        EObject this_Slider_3 = null;

        EObject this_List_4 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:262:6: ( (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) )
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
                    new NoViableAltException("263:1: (this_LinkableWidget_0= ruleLinkableWidget | (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList ) )", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
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
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList )
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
                            new NoViableAltException("273:6: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_Slider_3= ruleSlider | this_List_4= ruleList )", 4, 0, input);

                        throw nvae;
                    }

                    switch (alt4) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:274:5: this_Switch_1= ruleSwitch
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleSwitch_in_ruleWidget467);
                            this_Switch_1=ruleSwitch();
                            _fsp--;

                             
                                    current = this_Switch_1; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;
                        case 2 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:284:5: this_Selection_2= ruleSelection
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_1(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleSelection_in_ruleWidget494);
                            this_Selection_2=ruleSelection();
                            _fsp--;

                             
                                    current = this_Selection_2; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;
                        case 3 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:294:5: this_Slider_3= ruleSlider
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getSliderParserRuleCall_1_2(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleSlider_in_ruleWidget521);
                            this_Slider_3=ruleSlider();
                            _fsp--;

                             
                                    current = this_Slider_3; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;
                        case 4 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:304:5: this_List_4= ruleList
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getListParserRuleCall_1_3(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleList_in_ruleWidget548);
                            this_List_4=ruleList();
                            _fsp--;

                             
                                    current = this_List_4; 
                                    currentNode = currentNode.getParent();
                                

                            }
                            break;

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:320:1: entryRuleLinkableWidget returns [EObject current=null] : iv_ruleLinkableWidget= ruleLinkableWidget EOF ;
    public final EObject entryRuleLinkableWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLinkableWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:321:2: (iv_ruleLinkableWidget= ruleLinkableWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:322:2: iv_ruleLinkableWidget= ruleLinkableWidget EOF
            {
             currentNode = createCompositeNode(grammarAccess.getLinkableWidgetRule(), currentNode); 
            pushFollow(FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget584);
            iv_ruleLinkableWidget=ruleLinkableWidget();
            _fsp--;

             current =iv_ruleLinkableWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkableWidget594); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:329:1: ruleLinkableWidget returns [EObject current=null] : ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) ;
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
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:334:6: ( ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:335:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:335:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:335:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:335:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )
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
                    new NoViableAltException("335:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:336:5: this_Text_0= ruleText
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleText_in_ruleLinkableWidget642);
                    this_Text_0=ruleText();
                    _fsp--;

                     
                            current = this_Text_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:346:5: this_Group_1= ruleGroup
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroup_in_ruleLinkableWidget669);
                    this_Group_1=ruleGroup();
                    _fsp--;

                     
                            current = this_Group_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:356:5: this_Image_2= ruleImage
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleImage_in_ruleLinkableWidget696);
                    this_Image_2=ruleImage();
                    _fsp--;

                     
                            current = this_Image_2; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:366:5: this_Frame_3= ruleFrame
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleFrame_in_ruleLinkableWidget723);
                    this_Frame_3=ruleFrame();
                    _fsp--;

                     
                            current = this_Frame_3; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:374:2: ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==12) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:374:4: 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleLinkableWidget734); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:378:1: ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:379:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:379:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:380:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:380:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
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
                            new NoViableAltException("380:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )", 7, 0, input);

                        throw nvae;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:381:3: lv_label_5_1= RULE_ID
                            {
                            lv_label_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget753); 

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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:402:8: lv_label_5_2= RULE_STRING
                            {
                            lv_label_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget773); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:426:4: ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==13) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:426:6: 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleLinkableWidget794); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:430:1: ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:431:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:431:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:432:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:432:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
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
                            new NoViableAltException("432:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )", 9, 0, input);

                        throw nvae;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:433:3: lv_icon_7_1= RULE_ID
                            {
                            lv_icon_7_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget813); 

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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:454:8: lv_icon_7_2= RULE_STRING
                            {
                            lv_icon_7_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget833); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:478:4: ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==14) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:478:6: '{' ( (lv_children_9_0= ruleWidget ) )+ '}'
                    {
                    match(input,14,FOLLOW_14_in_ruleLinkableWidget854); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:482:1: ( (lv_children_9_0= ruleWidget ) )+
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
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:483:1: (lv_children_9_0= ruleWidget )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:483:1: (lv_children_9_0= ruleWidget )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:484:3: lv_children_9_0= ruleWidget
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleWidget_in_ruleLinkableWidget875);
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
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    match(input,15,FOLLOW_15_in_ruleLinkableWidget886); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:518:1: entryRuleFrame returns [EObject current=null] : iv_ruleFrame= ruleFrame EOF ;
    public final EObject entryRuleFrame() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFrame = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:519:2: (iv_ruleFrame= ruleFrame EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:520:2: iv_ruleFrame= ruleFrame EOF
            {
             currentNode = createCompositeNode(grammarAccess.getFrameRule(), currentNode); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame924);
            iv_ruleFrame=ruleFrame();
            _fsp--;

             current =iv_ruleFrame; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame934); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:527:1: ruleFrame returns [EObject current=null] : ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleFrame() throws RecognitionException {
        EObject current = null;

        Token lv_item_3_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:532:6: ( ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:533:1: ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:533:1: ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:533:3: 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            match(input,16,FOLLOW_16_in_ruleFrame969); 

                    createLeafNode(grammarAccess.getFrameAccess().getFrameKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:537:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:538:5: 
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:548:2: ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==17) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:548:4: 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleFrame989); 

                            createLeafNode(grammarAccess.getFrameAccess().getItemKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:552:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:553:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:553:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:554:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFrame1006); 

                    			createLeafNode(grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0(), "item"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getFrameRule().getType().getClassifier());
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
    // $ANTLR end ruleFrame


    // $ANTLR start entryRuleText
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:584:1: entryRuleText returns [EObject current=null] : iv_ruleText= ruleText EOF ;
    public final EObject entryRuleText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleText = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:585:2: (iv_ruleText= ruleText EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:586:2: iv_ruleText= ruleText EOF
            {
             currentNode = createCompositeNode(grammarAccess.getTextRule(), currentNode); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText1049);
            iv_ruleText=ruleText();
            _fsp--;

             current =iv_ruleText; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText1059); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:593:1: ruleText returns [EObject current=null] : ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleText() throws RecognitionException {
        EObject current = null;

        Token lv_item_3_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:598:6: ( ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:599:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:599:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:599:3: 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            match(input,18,FOLLOW_18_in_ruleText1094); 

                    createLeafNode(grammarAccess.getTextAccess().getTextKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:603:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:604:5: 
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:614:2: ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==17) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:614:4: 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleText1114); 

                            createLeafNode(grammarAccess.getTextAccess().getItemKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:618:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:619:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:619:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:620:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleText1131); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:650:1: entryRuleGroup returns [EObject current=null] : iv_ruleGroup= ruleGroup EOF ;
    public final EObject entryRuleGroup() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroup = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:651:2: (iv_ruleGroup= ruleGroup EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:652:2: iv_ruleGroup= ruleGroup EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup1174);
            iv_ruleGroup=ruleGroup();
            _fsp--;

             current =iv_ruleGroup; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup1184); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:659:1: ruleGroup returns [EObject current=null] : ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleGroup() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:664:6: ( ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:665:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:665:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:665:3: 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,19,FOLLOW_19_in_ruleGroup1219); 

                    createLeafNode(grammarAccess.getGroupAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:669:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:669:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleGroup1230); 

                    createLeafNode(grammarAccess.getGroupAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:673:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:674:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:674:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:675:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroup1247); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:705:1: entryRuleImage returns [EObject current=null] : iv_ruleImage= ruleImage EOF ;
    public final EObject entryRuleImage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImage = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:706:2: (iv_ruleImage= ruleImage EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:707:2: iv_ruleImage= ruleImage EOF
            {
             currentNode = createCompositeNode(grammarAccess.getImageRule(), currentNode); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage1289);
            iv_ruleImage=ruleImage();
            _fsp--;

             current =iv_ruleImage; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage1299); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:714:1: ruleImage returns [EObject current=null] : ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleImage() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_url_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:719:6: ( ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:720:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:720:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:720:3: 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            {
            match(input,20,FOLLOW_20_in_ruleImage1334); 

                    createLeafNode(grammarAccess.getImageAccess().getImageKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:724:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==17) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:724:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleImage1345); 

                            createLeafNode(grammarAccess.getImageAccess().getItemKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:728:1: ( (lv_item_2_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:729:1: (lv_item_2_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:729:1: (lv_item_2_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:730:3: lv_item_2_0= RULE_ID
                    {
                    lv_item_2_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleImage1362); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:752:4: ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:752:6: 'url=' ( (lv_url_4_0= RULE_STRING ) )
            {
            match(input,21,FOLLOW_21_in_ruleImage1380); 

                    createLeafNode(grammarAccess.getImageAccess().getUrlKeyword_2_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:756:1: ( (lv_url_4_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:757:1: (lv_url_4_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:757:1: (lv_url_4_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:758:3: lv_url_4_0= RULE_STRING
            {
            lv_url_4_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImage1397); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:788:1: entryRuleSwitch returns [EObject current=null] : iv_ruleSwitch= ruleSwitch EOF ;
    public final EObject entryRuleSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSwitch = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:789:2: (iv_ruleSwitch= ruleSwitch EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:790:2: iv_ruleSwitch= ruleSwitch EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSwitchRule(), currentNode); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch1439);
            iv_ruleSwitch=ruleSwitch();
            _fsp--;

             current =iv_ruleSwitch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch1449); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:797:1: ruleSwitch returns [EObject current=null] : ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? ) ;
    public final EObject ruleSwitch() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        EObject lv_mappings_8_0 = null;

        EObject lv_mappings_10_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:802:6: ( ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:803:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:803:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:803:3: 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )?
            {
            match(input,22,FOLLOW_22_in_ruleSwitch1484); 

                    createLeafNode(grammarAccess.getSwitchAccess().getSwitchKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:807:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:807:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSwitch1495); 

                    createLeafNode(grammarAccess.getSwitchAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:811:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:812:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:812:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:813:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1512); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:835:3: ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==12) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:835:5: 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleSwitch1529); 

                            createLeafNode(grammarAccess.getSwitchAccess().getLabelKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:839:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:840:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:840:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:841:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:841:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
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
                            new NoViableAltException("841:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )", 16, 0, input);

                        throw nvae;
                    }
                    switch (alt16) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:842:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1548); 

                            			createLeafNode(grammarAccess.getSwitchAccess().getLabelIDTerminalRuleCall_2_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:863:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSwitch1568); 

                            			createLeafNode(grammarAccess.getSwitchAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:887:4: ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==13) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:887:6: 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleSwitch1589); 

                            createLeafNode(grammarAccess.getSwitchAccess().getIconKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:891:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:892:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:892:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:893:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:893:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
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
                            new NoViableAltException("893:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:894:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1608); 

                            			createLeafNode(grammarAccess.getSwitchAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:915:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSwitch1628); 

                            			createLeafNode(grammarAccess.getSwitchAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:939:4: ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==23) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:939:6: 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']'
                    {
                    match(input,23,FOLLOW_23_in_ruleSwitch1649); 

                            createLeafNode(grammarAccess.getSwitchAccess().getMappingsKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:943:1: ( (lv_mappings_8_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:944:1: (lv_mappings_8_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:944:1: (lv_mappings_8_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:945:3: lv_mappings_8_0= ruleMapping
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_4_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1670);
                    lv_mappings_8_0=ruleMapping();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        try {
                    	       		add(
                    	       			current, 
                    	       			"mappings",
                    	        		lv_mappings_8_0, 
                    	        		"Mapping", 
                    	        		currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:967:2: ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==24) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:967:4: ',' ( (lv_mappings_10_0= ruleMapping ) )
                    	    {
                    	    match(input,24,FOLLOW_24_in_ruleSwitch1681); 

                    	            createLeafNode(grammarAccess.getSwitchAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:971:1: ( (lv_mappings_10_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:972:1: (lv_mappings_10_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:972:1: (lv_mappings_10_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:973:3: lv_mappings_10_0= ruleMapping
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_4_2_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1702);
                    	    lv_mappings_10_0=ruleMapping();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        try {
                    	    	       		add(
                    	    	       			current, 
                    	    	       			"mappings",
                    	    	        		lv_mappings_10_0, 
                    	    	        		"Mapping", 
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
                    	    break loop20;
                        }
                    } while (true);

                    match(input,25,FOLLOW_25_in_ruleSwitch1714); 

                            createLeafNode(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_4_3(), null); 
                        

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


    // $ANTLR start entryRuleSlider
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1007:1: entryRuleSlider returns [EObject current=null] : iv_ruleSlider= ruleSlider EOF ;
    public final EObject entryRuleSlider() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSlider = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1008:2: (iv_ruleSlider= ruleSlider EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1009:2: iv_ruleSlider= ruleSlider EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSliderRule(), currentNode); 
            pushFollow(FOLLOW_ruleSlider_in_entryRuleSlider1752);
            iv_ruleSlider=ruleSlider();
            _fsp--;

             current =iv_ruleSlider; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSlider1762); 

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
    // $ANTLR end entryRuleSlider


    // $ANTLR start ruleSlider
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1016:1: ruleSlider returns [EObject current=null] : ( 'Slider' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? ) ;
    public final EObject ruleSlider() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token lv_frequency_8_0=null;
        Token lv_switchEnabled_9_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1021:6: ( ( 'Slider' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1022:1: ( 'Slider' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1022:1: ( 'Slider' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1022:3: 'Slider' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )? ( (lv_switchEnabled_9_0= 'switchSupport' ) )?
            {
            match(input,26,FOLLOW_26_in_ruleSlider1797); 

                    createLeafNode(grammarAccess.getSliderAccess().getSliderKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1026:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1026:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSlider1808); 

                    createLeafNode(grammarAccess.getSliderAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1030:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1031:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1031:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1032:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1825); 

            			createLeafNode(grammarAccess.getSliderAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1054:3: ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==12) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1054:5: 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleSlider1842); 

                            createLeafNode(grammarAccess.getSliderAccess().getLabelKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1058:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1059:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1059:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1060:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1060:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
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
                            new NoViableAltException("1060:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )", 22, 0, input);

                        throw nvae;
                    }
                    switch (alt22) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1061:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1861); 

                            			createLeafNode(grammarAccess.getSliderAccess().getLabelIDTerminalRuleCall_2_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1082:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSlider1881); 

                            			createLeafNode(grammarAccess.getSliderAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1106:4: ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==13) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1106:6: 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleSlider1902); 

                            createLeafNode(grammarAccess.getSliderAccess().getIconKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1110:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1111:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1111:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1112:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1112:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
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
                            new NoViableAltException("1112:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )", 24, 0, input);

                        throw nvae;
                    }
                    switch (alt24) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1113:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1921); 

                            			createLeafNode(grammarAccess.getSliderAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1134:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSlider1941); 

                            			createLeafNode(grammarAccess.getSliderAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1158:4: ( 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==27) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1158:6: 'sendFrequency=' ( (lv_frequency_8_0= RULE_ID ) )
                    {
                    match(input,27,FOLLOW_27_in_ruleSlider1962); 

                            createLeafNode(grammarAccess.getSliderAccess().getSendFrequencyKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1162:1: ( (lv_frequency_8_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1163:1: (lv_frequency_8_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1163:1: (lv_frequency_8_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1164:3: lv_frequency_8_0= RULE_ID
                    {
                    lv_frequency_8_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSlider1979); 

                    			createLeafNode(grammarAccess.getSliderAccess().getFrequencyIDTerminalRuleCall_4_1_0(), "frequency"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"frequency",
                    	        		lv_frequency_8_0, 
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1186:4: ( (lv_switchEnabled_9_0= 'switchSupport' ) )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==28) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1187:1: (lv_switchEnabled_9_0= 'switchSupport' )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1187:1: (lv_switchEnabled_9_0= 'switchSupport' )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1188:3: lv_switchEnabled_9_0= 'switchSupport'
                    {
                    lv_switchEnabled_9_0=(Token)input.LT(1);
                    match(input,28,FOLLOW_28_in_ruleSlider2004); 

                            createLeafNode(grammarAccess.getSliderAccess().getSwitchEnabledSwitchSupportKeyword_5_0(), "switchEnabled"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSliderRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "switchEnabled", true, "switchSupport", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
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
    // $ANTLR end ruleSlider


    // $ANTLR start entryRuleSelection
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1215:1: entryRuleSelection returns [EObject current=null] : iv_ruleSelection= ruleSelection EOF ;
    public final EObject entryRuleSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelection = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1216:2: (iv_ruleSelection= ruleSelection EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1217:2: iv_ruleSelection= ruleSelection EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSelectionRule(), currentNode); 
            pushFollow(FOLLOW_ruleSelection_in_entryRuleSelection2054);
            iv_ruleSelection=ruleSelection();
            _fsp--;

             current =iv_ruleSelection; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelection2064); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1224:1: ruleSelection returns [EObject current=null] : ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? ) ;
    public final EObject ruleSelection() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        EObject lv_mappings_8_0 = null;

        EObject lv_mappings_10_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1229:6: ( ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1230:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1230:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1230:3: 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )?
            {
            match(input,29,FOLLOW_29_in_ruleSelection2099); 

                    createLeafNode(grammarAccess.getSelectionAccess().getSelectionKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1234:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1234:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSelection2110); 

                    createLeafNode(grammarAccess.getSelectionAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1238:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1239:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1239:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1240:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2127); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1262:3: ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==12) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1262:5: 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleSelection2144); 

                            createLeafNode(grammarAccess.getSelectionAccess().getLabelKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1266:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1267:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1267:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1268:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1268:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
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
                            new NoViableAltException("1268:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )", 28, 0, input);

                        throw nvae;
                    }
                    switch (alt28) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1269:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2163); 

                            			createLeafNode(grammarAccess.getSelectionAccess().getLabelIDTerminalRuleCall_2_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1290:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSelection2183); 

                            			createLeafNode(grammarAccess.getSelectionAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1314:4: ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==13) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1314:6: 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleSelection2204); 

                            createLeafNode(grammarAccess.getSelectionAccess().getIconKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1318:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1319:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1319:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1320:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1320:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
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
                            new NoViableAltException("1320:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )", 30, 0, input);

                        throw nvae;
                    }
                    switch (alt30) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1321:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection2223); 

                            			createLeafNode(grammarAccess.getSelectionAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1342:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSelection2243); 

                            			createLeafNode(grammarAccess.getSelectionAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1366:4: ( 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']' )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==23) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1366:6: 'mappings=[' ( (lv_mappings_8_0= ruleMapping ) ) ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )* ']'
                    {
                    match(input,23,FOLLOW_23_in_ruleSelection2264); 

                            createLeafNode(grammarAccess.getSelectionAccess().getMappingsKeyword_4_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1370:1: ( (lv_mappings_8_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1371:1: (lv_mappings_8_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1371:1: (lv_mappings_8_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1372:3: lv_mappings_8_0= ruleMapping
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_4_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSelection2285);
                    lv_mappings_8_0=ruleMapping();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        try {
                    	       		add(
                    	       			current, 
                    	       			"mappings",
                    	        		lv_mappings_8_0, 
                    	        		"Mapping", 
                    	        		currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1394:2: ( ',' ( (lv_mappings_10_0= ruleMapping ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==24) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1394:4: ',' ( (lv_mappings_10_0= ruleMapping ) )
                    	    {
                    	    match(input,24,FOLLOW_24_in_ruleSelection2296); 

                    	            createLeafNode(grammarAccess.getSelectionAccess().getCommaKeyword_4_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1398:1: ( (lv_mappings_10_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1399:1: (lv_mappings_10_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1399:1: (lv_mappings_10_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1400:3: lv_mappings_10_0= ruleMapping
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_4_2_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSelection2317);
                    	    lv_mappings_10_0=ruleMapping();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        try {
                    	    	       		add(
                    	    	       			current, 
                    	    	       			"mappings",
                    	    	        		lv_mappings_10_0, 
                    	    	        		"Mapping", 
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
                    	    break loop32;
                        }
                    } while (true);

                    match(input,25,FOLLOW_25_in_ruleSelection2329); 

                            createLeafNode(grammarAccess.getSelectionAccess().getRightSquareBracketKeyword_4_3(), null); 
                        

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
    // $ANTLR end ruleSelection


    // $ANTLR start entryRuleList
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1434:1: entryRuleList returns [EObject current=null] : iv_ruleList= ruleList EOF ;
    public final EObject entryRuleList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleList = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1435:2: (iv_ruleList= ruleList EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1436:2: iv_ruleList= ruleList EOF
            {
             currentNode = createCompositeNode(grammarAccess.getListRule(), currentNode); 
            pushFollow(FOLLOW_ruleList_in_entryRuleList2367);
            iv_ruleList=ruleList();
            _fsp--;

             current =iv_ruleList; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleList2377); 

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
    // $ANTLR end entryRuleList


    // $ANTLR start ruleList
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1443:1: ruleList returns [EObject current=null] : ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleList() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_label_4_1=null;
        Token lv_label_4_2=null;
        Token lv_icon_6_1=null;
        Token lv_icon_6_2=null;
        Token lv_separator_8_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1448:6: ( ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1449:1: ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1449:1: ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1449:3: 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )? ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) )
            {
            match(input,30,FOLLOW_30_in_ruleList2412); 

                    createLeafNode(grammarAccess.getListAccess().getListKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1453:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1453:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleList2423); 

                    createLeafNode(grammarAccess.getListAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1457:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1458:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1458:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1459:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2440); 

            			createLeafNode(grammarAccess.getListAccess().getItemIDTerminalRuleCall_1_1_0(), "item"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1481:3: ( 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) ) )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==12) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1481:5: 'label=' ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleList2457); 

                            createLeafNode(grammarAccess.getListAccess().getLabelKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1485:1: ( ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1486:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1486:1: ( (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1487:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1487:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )
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
                            new NoViableAltException("1487:1: (lv_label_4_1= RULE_ID | lv_label_4_2= RULE_STRING )", 34, 0, input);

                        throw nvae;
                    }
                    switch (alt34) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1488:3: lv_label_4_1= RULE_ID
                            {
                            lv_label_4_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2476); 

                            			createLeafNode(grammarAccess.getListAccess().getLabelIDTerminalRuleCall_2_1_0_0(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1509:8: lv_label_4_2= RULE_STRING
                            {
                            lv_label_4_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2496); 

                            			createLeafNode(grammarAccess.getListAccess().getLabelSTRINGTerminalRuleCall_2_1_0_1(), "label"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1533:4: ( 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==13) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1533:6: 'icon=' ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleList2517); 

                            createLeafNode(grammarAccess.getListAccess().getIconKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1537:1: ( ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1538:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1538:1: ( (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1539:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1539:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )
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
                            new NoViableAltException("1539:1: (lv_icon_6_1= RULE_ID | lv_icon_6_2= RULE_STRING )", 36, 0, input);

                        throw nvae;
                    }
                    switch (alt36) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1540:3: lv_icon_6_1= RULE_ID
                            {
                            lv_icon_6_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList2536); 

                            			createLeafNode(grammarAccess.getListAccess().getIconIDTerminalRuleCall_3_1_0_0(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1561:8: lv_icon_6_2= RULE_STRING
                            {
                            lv_icon_6_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2556); 

                            			createLeafNode(grammarAccess.getListAccess().getIconSTRINGTerminalRuleCall_3_1_0_1(), "icon"); 
                            		

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1585:4: ( 'separator=' ( (lv_separator_8_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1585:6: 'separator=' ( (lv_separator_8_0= RULE_STRING ) )
            {
            match(input,31,FOLLOW_31_in_ruleList2577); 

                    createLeafNode(grammarAccess.getListAccess().getSeparatorKeyword_4_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1589:1: ( (lv_separator_8_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1590:1: (lv_separator_8_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1590:1: (lv_separator_8_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1591:3: lv_separator_8_0= RULE_STRING
            {
            lv_separator_8_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2594); 

            			createLeafNode(grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_4_1_0(), "separator"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"separator",
            	        		lv_separator_8_0, 
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
    // $ANTLR end ruleList


    // $ANTLR start entryRuleMapping
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1621:1: entryRuleMapping returns [EObject current=null] : iv_ruleMapping= ruleMapping EOF ;
    public final EObject entryRuleMapping() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapping = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1622:2: (iv_ruleMapping= ruleMapping EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1623:2: iv_ruleMapping= ruleMapping EOF
            {
             currentNode = createCompositeNode(grammarAccess.getMappingRule(), currentNode); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping2636);
            iv_ruleMapping=ruleMapping();
            _fsp--;

             current =iv_ruleMapping; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping2646); 

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
    // $ANTLR end entryRuleMapping


    // $ANTLR start ruleMapping
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1630:1: ruleMapping returns [EObject current=null] : ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) ;
    public final EObject ruleMapping() throws RecognitionException {
        EObject current = null;

        Token lv_cmd_0_1=null;
        Token lv_cmd_0_2=null;
        Token lv_label_2_1=null;
        Token lv_label_2_2=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1635:6: ( ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1636:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1636:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1636:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1636:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1637:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1637:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1638:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1638:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
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
                    new NoViableAltException("1638:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1639:3: lv_cmd_0_1= RULE_ID
                    {
                    lv_cmd_0_1=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2690); 

                    			createLeafNode(grammarAccess.getMappingAccess().getCmdIDTerminalRuleCall_0_0_0(), "cmd"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getMappingRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"cmd",
                    	        		lv_cmd_0_1, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1660:8: lv_cmd_0_2= RULE_STRING
                    {
                    lv_cmd_0_2=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2710); 

                    			createLeafNode(grammarAccess.getMappingAccess().getCmdSTRINGTerminalRuleCall_0_0_1(), "cmd"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getMappingRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"cmd",
                    	        		lv_cmd_0_2, 
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

            match(input,32,FOLLOW_32_in_ruleMapping2728); 

                    createLeafNode(grammarAccess.getMappingAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1688:1: ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1689:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1689:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1690:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1690:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
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
                    new NoViableAltException("1690:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1691:3: lv_label_2_1= RULE_ID
                    {
                    lv_label_2_1=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2747); 

                    			createLeafNode(grammarAccess.getMappingAccess().getLabelIDTerminalRuleCall_2_0_0(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getMappingRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"label",
                    	        		lv_label_2_1, 
                    	        		"ID", 
                    	        		lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1712:8: lv_label_2_2= RULE_STRING
                    {
                    lv_label_2_2=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2767); 

                    			createLeafNode(grammarAccess.getMappingAccess().getLabelSTRINGTerminalRuleCall_2_0_1(), "label"); 
                    		

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getMappingRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        try {
                    	       		set(
                    	       			current, 
                    	       			"label",
                    	        		lv_label_2_2, 
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
    // $ANTLR end ruleMapping


 

    public static final BitSet FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemapModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleSitemapModel120 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleSitemap_in_ruleSitemapModel142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_entryRuleSitemap177 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemap187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSitemap229 = new BitSet(new long[]{0x0000000000007000L});
    public static final BitSet FOLLOW_12_in_ruleSitemap245 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap262 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_13_in_ruleSitemap280 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSitemap297 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleSitemap314 = new BitSet(new long[]{0x00000000645D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleSitemap335 = new BitSet(new long[]{0x00000000645D8000L});
    public static final BitSet FOLLOW_15_in_ruleSitemap346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget382 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_ruleWidget439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_ruleWidget467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_ruleWidget494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSlider_in_ruleWidget521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_ruleWidget548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget584 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkableWidget594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_ruleLinkableWidget642 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleGroup_in_ruleLinkableWidget669 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleImage_in_ruleLinkableWidget696 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleFrame_in_ruleLinkableWidget723 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_12_in_ruleLinkableWidget734 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget753 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget773 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_13_in_ruleLinkableWidget794 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget813 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget833 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleLinkableWidget854 = new BitSet(new long[]{0x00000000645D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleLinkableWidget875 = new BitSet(new long[]{0x00000000645D8000L});
    public static final BitSet FOLLOW_15_in_ruleLinkableWidget886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame924 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleFrame969 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleFrame989 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFrame1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText1049 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText1059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleText1094 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleText1114 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleText1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup1174 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup1184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleGroup1219 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleGroup1230 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroup1247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage1289 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage1299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleImage1334 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_17_in_ruleImage1345 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleImage1362 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_ruleImage1380 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImage1397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch1439 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch1449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleSwitch1484 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSwitch1495 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1512 = new BitSet(new long[]{0x0000000000803002L});
    public static final BitSet FOLLOW_12_in_ruleSwitch1529 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1548 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSwitch1568 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_13_in_ruleSwitch1589 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1608 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSwitch1628 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSwitch1649 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1670 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSwitch1681 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1702 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSwitch1714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSlider_in_entryRuleSlider1752 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSlider1762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleSlider1797 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSlider1808 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1825 = new BitSet(new long[]{0x0000000018003002L});
    public static final BitSet FOLLOW_12_in_ruleSlider1842 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1861 = new BitSet(new long[]{0x0000000018002002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSlider1881 = new BitSet(new long[]{0x0000000018002002L});
    public static final BitSet FOLLOW_13_in_ruleSlider1902 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1921 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSlider1941 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_27_in_ruleSlider1962 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSlider1979 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_ruleSlider2004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_entryRuleSelection2054 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelection2064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleSelection2099 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSelection2110 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2127 = new BitSet(new long[]{0x0000000000803002L});
    public static final BitSet FOLLOW_12_in_ruleSelection2144 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2163 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSelection2183 = new BitSet(new long[]{0x0000000000802002L});
    public static final BitSet FOLLOW_13_in_ruleSelection2204 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection2223 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSelection2243 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSelection2264 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection2285 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSelection2296 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection2317 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSelection2329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_entryRuleList2367 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleList2377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleList2412 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleList2423 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2440 = new BitSet(new long[]{0x0000000080003000L});
    public static final BitSet FOLLOW_12_in_ruleList2457 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2476 = new BitSet(new long[]{0x0000000080002000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2496 = new BitSet(new long[]{0x0000000080002000L});
    public static final BitSet FOLLOW_13_in_ruleList2517 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList2536 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2556 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleList2577 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping2636 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping2646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2690 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2710 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleMapping2728 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2767 = new BitSet(new long[]{0x0000000000000002L});

}