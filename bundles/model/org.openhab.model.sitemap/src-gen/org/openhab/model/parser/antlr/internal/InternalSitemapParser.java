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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'label='", "'icon='", "'{'", "'}'", "'Frame'", "'item='", "'Text'", "'Group'", "'Image'", "'url='", "'Switch'", "'mappings=['", "','", "']'", "'Selection'", "'List'", "'separator='", "'='"
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

                if ( (LA3_0==16||(LA3_0>=18 && LA3_0<=20)||LA3_0==22||(LA3_0>=26 && LA3_0<=27)) ) {
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:257:1: ruleWidget returns [EObject current=null] : (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ) ) ;
    public final EObject ruleWidget() throws RecognitionException {
        EObject current = null;

        Token lv_label_5_1=null;
        Token lv_label_5_2=null;
        Token lv_icon_7_1=null;
        Token lv_icon_7_2=null;
        EObject this_LinkableWidget_0 = null;

        EObject this_Switch_1 = null;

        EObject this_Selection_2 = null;

        EObject this_List_3 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:262:6: ( (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==16||(LA9_0>=18 && LA9_0<=20)) ) {
                alt9=1;
            }
            else if ( (LA9_0==22||(LA9_0>=26 && LA9_0<=27)) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("263:1: (this_LinkableWidget_0= ruleLinkableWidget | ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ) )", 9, 0, input);

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
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:6: ( (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList )
                    int alt4=3;
                    switch ( input.LA(1) ) {
                    case 22:
                        {
                        alt4=1;
                        }
                        break;
                    case 26:
                        {
                        alt4=2;
                        }
                        break;
                    case 27:
                        {
                        alt4=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("273:7: (this_Switch_1= ruleSwitch | this_Selection_2= ruleSelection | this_List_3= ruleList )", 4, 0, input);

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
                        case 3 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:294:5: this_List_3= ruleList
                            {
                             
                                    currentNode=createCompositeNode(grammarAccess.getWidgetAccess().getListParserRuleCall_1_0_2(), currentNode); 
                                
                            pushFollow(FOLLOW_ruleList_in_ruleWidget522);
                            this_List_3=ruleList();
                            _fsp--;

                             
                                    current = this_List_3; 
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
                            match(input,12,FOLLOW_12_in_ruleWidget533); 

                                    createLeafNode(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0(), null); 
                                
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
                                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget552); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0(), "label"); 
                                    		

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
                                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget572); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1(), "label"); 
                                    		

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
                            match(input,13,FOLLOW_13_in_ruleWidget593); 

                                    createLeafNode(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0(), null); 
                                
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
                                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleWidget612); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0(), "icon"); 
                                    		

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
                                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleWidget632); 

                                    			createLeafNode(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1(), "icon"); 
                                    		

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:414:1: entryRuleLinkableWidget returns [EObject current=null] : iv_ruleLinkableWidget= ruleLinkableWidget EOF ;
    public final EObject entryRuleLinkableWidget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLinkableWidget = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:415:2: (iv_ruleLinkableWidget= ruleLinkableWidget EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:416:2: iv_ruleLinkableWidget= ruleLinkableWidget EOF
            {
             currentNode = createCompositeNode(grammarAccess.getLinkableWidgetRule(), currentNode); 
            pushFollow(FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget679);
            iv_ruleLinkableWidget=ruleLinkableWidget();
            _fsp--;

             current =iv_ruleLinkableWidget; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkableWidget689); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:423:1: ruleLinkableWidget returns [EObject current=null] : ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) ;
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
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:428:6: ( ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:429:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:429:1: ( (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:429:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame ) ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )? ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )? ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:429:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )
            int alt10=4;
            switch ( input.LA(1) ) {
            case 18:
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
                    new NoViableAltException("429:2: (this_Text_0= ruleText | this_Group_1= ruleGroup | this_Image_2= ruleImage | this_Frame_3= ruleFrame )", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:430:5: this_Text_0= ruleText
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleText_in_ruleLinkableWidget737);
                    this_Text_0=ruleText();
                    _fsp--;

                     
                            current = this_Text_0; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:440:5: this_Group_1= ruleGroup
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleGroup_in_ruleLinkableWidget764);
                    this_Group_1=ruleGroup();
                    _fsp--;

                     
                            current = this_Group_1; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:450:5: this_Image_2= ruleImage
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleImage_in_ruleLinkableWidget791);
                    this_Image_2=ruleImage();
                    _fsp--;

                     
                            current = this_Image_2; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:460:5: this_Frame_3= ruleFrame
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleFrame_in_ruleLinkableWidget818);
                    this_Frame_3=ruleFrame();
                    _fsp--;

                     
                            current = this_Frame_3; 
                            currentNode = currentNode.getParent();
                        

                    }
                    break;

            }

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:468:2: ( 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==12) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:468:4: 'label=' ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    {
                    match(input,12,FOLLOW_12_in_ruleLinkableWidget829); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:472:1: ( ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:473:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:473:1: ( (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:474:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:474:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )
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
                            new NoViableAltException("474:1: (lv_label_5_1= RULE_ID | lv_label_5_2= RULE_STRING )", 11, 0, input);

                        throw nvae;
                    }
                    switch (alt11) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:475:3: lv_label_5_1= RULE_ID
                            {
                            lv_label_5_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget848); 

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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:496:8: lv_label_5_2= RULE_STRING
                            {
                            lv_label_5_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget868); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:520:4: ( 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==13) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:520:6: 'icon=' ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    {
                    match(input,13,FOLLOW_13_in_ruleLinkableWidget889); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:524:1: ( ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:525:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:525:1: ( (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:526:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:526:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )
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
                            new NoViableAltException("526:1: (lv_icon_7_1= RULE_ID | lv_icon_7_2= RULE_STRING )", 13, 0, input);

                        throw nvae;
                    }
                    switch (alt13) {
                        case 1 :
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:527:3: lv_icon_7_1= RULE_ID
                            {
                            lv_icon_7_1=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleLinkableWidget908); 

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
                            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:548:8: lv_icon_7_2= RULE_STRING
                            {
                            lv_icon_7_2=(Token)input.LT(1);
                            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleLinkableWidget928); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:572:4: ( '{' ( (lv_children_9_0= ruleWidget ) )+ '}' )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==14) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:572:6: '{' ( (lv_children_9_0= ruleWidget ) )+ '}'
                    {
                    match(input,14,FOLLOW_14_in_ruleLinkableWidget949); 

                            createLeafNode(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:576:1: ( (lv_children_9_0= ruleWidget ) )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==16||(LA15_0>=18 && LA15_0<=20)||LA15_0==22||(LA15_0>=26 && LA15_0<=27)) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:577:1: (lv_children_9_0= ruleWidget )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:577:1: (lv_children_9_0= ruleWidget )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:578:3: lv_children_9_0= ruleWidget
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleWidget_in_ruleLinkableWidget970);
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

                    match(input,15,FOLLOW_15_in_ruleLinkableWidget981); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:612:1: entryRuleFrame returns [EObject current=null] : iv_ruleFrame= ruleFrame EOF ;
    public final EObject entryRuleFrame() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFrame = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:613:2: (iv_ruleFrame= ruleFrame EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:614:2: iv_ruleFrame= ruleFrame EOF
            {
             currentNode = createCompositeNode(grammarAccess.getFrameRule(), currentNode); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame1019);
            iv_ruleFrame=ruleFrame();
            _fsp--;

             current =iv_ruleFrame; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame1029); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:621:1: ruleFrame returns [EObject current=null] : ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleFrame() throws RecognitionException {
        EObject current = null;

        Token lv_item_3_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:626:6: ( ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:627:1: ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:627:1: ( 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:627:3: 'Frame' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            match(input,16,FOLLOW_16_in_ruleFrame1064); 

                    createLeafNode(grammarAccess.getFrameAccess().getFrameKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:631:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:632:5: 
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:642:2: ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==17) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:642:4: 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleFrame1084); 

                            createLeafNode(grammarAccess.getFrameAccess().getItemKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:646:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:647:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:647:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:648:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFrame1101); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:678:1: entryRuleText returns [EObject current=null] : iv_ruleText= ruleText EOF ;
    public final EObject entryRuleText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleText = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:679:2: (iv_ruleText= ruleText EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:680:2: iv_ruleText= ruleText EOF
            {
             currentNode = createCompositeNode(grammarAccess.getTextRule(), currentNode); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText1144);
            iv_ruleText=ruleText();
            _fsp--;

             current =iv_ruleText; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText1154); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:687:1: ruleText returns [EObject current=null] : ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) ;
    public final EObject ruleText() throws RecognitionException {
        EObject current = null;

        Token lv_item_3_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:692:6: ( ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:693:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:693:1: ( 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:693:3: 'Text' () ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            {
            match(input,18,FOLLOW_18_in_ruleText1189); 

                    createLeafNode(grammarAccess.getTextAccess().getTextKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:697:1: ()
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:698:5: 
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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:708:2: ( 'item=' ( (lv_item_3_0= RULE_ID ) ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==17) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:708:4: 'item=' ( (lv_item_3_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleText1209); 

                            createLeafNode(grammarAccess.getTextAccess().getItemKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:712:1: ( (lv_item_3_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:713:1: (lv_item_3_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:713:1: (lv_item_3_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:714:3: lv_item_3_0= RULE_ID
                    {
                    lv_item_3_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleText1226); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:744:1: entryRuleGroup returns [EObject current=null] : iv_ruleGroup= ruleGroup EOF ;
    public final EObject entryRuleGroup() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroup = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:745:2: (iv_ruleGroup= ruleGroup EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:746:2: iv_ruleGroup= ruleGroup EOF
            {
             currentNode = createCompositeNode(grammarAccess.getGroupRule(), currentNode); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup1269);
            iv_ruleGroup=ruleGroup();
            _fsp--;

             current =iv_ruleGroup; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup1279); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:753:1: ruleGroup returns [EObject current=null] : ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) ;
    public final EObject ruleGroup() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:758:6: ( ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:759:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:759:1: ( 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:759:3: 'Group' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            {
            match(input,19,FOLLOW_19_in_ruleGroup1314); 

                    createLeafNode(grammarAccess.getGroupAccess().getGroupKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:763:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:763:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleGroup1325); 

                    createLeafNode(grammarAccess.getGroupAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:767:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:768:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:768:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:769:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleGroup1342); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:799:1: entryRuleImage returns [EObject current=null] : iv_ruleImage= ruleImage EOF ;
    public final EObject entryRuleImage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImage = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:800:2: (iv_ruleImage= ruleImage EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:801:2: iv_ruleImage= ruleImage EOF
            {
             currentNode = createCompositeNode(grammarAccess.getImageRule(), currentNode); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage1384);
            iv_ruleImage=ruleImage();
            _fsp--;

             current =iv_ruleImage; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage1394); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:808:1: ruleImage returns [EObject current=null] : ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleImage() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_url_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:813:6: ( ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:1: ( 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:814:3: 'Image' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )? ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            {
            match(input,20,FOLLOW_20_in_ruleImage1429); 

                    createLeafNode(grammarAccess.getImageAccess().getImageKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:818:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==17) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:818:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
                    {
                    match(input,17,FOLLOW_17_in_ruleImage1440); 

                            createLeafNode(grammarAccess.getImageAccess().getItemKeyword_1_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:822:1: ( (lv_item_2_0= RULE_ID ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:823:1: (lv_item_2_0= RULE_ID )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:823:1: (lv_item_2_0= RULE_ID )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:824:3: lv_item_2_0= RULE_ID
                    {
                    lv_item_2_0=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleImage1457); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:846:4: ( 'url=' ( (lv_url_4_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:846:6: 'url=' ( (lv_url_4_0= RULE_STRING ) )
            {
            match(input,21,FOLLOW_21_in_ruleImage1475); 

                    createLeafNode(grammarAccess.getImageAccess().getUrlKeyword_2_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:850:1: ( (lv_url_4_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:851:1: (lv_url_4_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:851:1: (lv_url_4_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:852:3: lv_url_4_0= RULE_STRING
            {
            lv_url_4_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImage1492); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:882:1: entryRuleSwitch returns [EObject current=null] : iv_ruleSwitch= ruleSwitch EOF ;
    public final EObject entryRuleSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSwitch = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:883:2: (iv_ruleSwitch= ruleSwitch EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:884:2: iv_ruleSwitch= ruleSwitch EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSwitchRule(), currentNode); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch1534);
            iv_ruleSwitch=ruleSwitch();
            _fsp--;

             current =iv_ruleSwitch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch1544); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:891:1: ruleSwitch returns [EObject current=null] : ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? ) ;
    public final EObject ruleSwitch() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        EObject lv_mappings_4_0 = null;

        EObject lv_mappings_6_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:896:6: ( ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:897:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:897:1: ( 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:897:3: 'Switch' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )?
            {
            match(input,22,FOLLOW_22_in_ruleSwitch1579); 

                    createLeafNode(grammarAccess.getSwitchAccess().getSwitchKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:901:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:901:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSwitch1590); 

                    createLeafNode(grammarAccess.getSwitchAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:905:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:906:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:906:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:907:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSwitch1607); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:929:3: ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==23) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:929:5: 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']'
                    {
                    match(input,23,FOLLOW_23_in_ruleSwitch1624); 

                            createLeafNode(grammarAccess.getSwitchAccess().getMappingsKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:933:1: ( (lv_mappings_4_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:934:1: (lv_mappings_4_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:934:1: (lv_mappings_4_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:935:3: lv_mappings_4_0= ruleMapping
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1645);
                    lv_mappings_4_0=ruleMapping();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        try {
                    	       		add(
                    	       			current, 
                    	       			"mappings",
                    	        		lv_mappings_4_0, 
                    	        		"Mapping", 
                    	        		currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:957:2: ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==24) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:957:4: ',' ( (lv_mappings_6_0= ruleMapping ) )
                    	    {
                    	    match(input,24,FOLLOW_24_in_ruleSwitch1656); 

                    	            createLeafNode(grammarAccess.getSwitchAccess().getCommaKeyword_2_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:961:1: ( (lv_mappings_6_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:962:1: (lv_mappings_6_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:962:1: (lv_mappings_6_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:963:3: lv_mappings_6_0= ruleMapping
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_2_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSwitch1677);
                    	    lv_mappings_6_0=ruleMapping();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getSwitchRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        try {
                    	    	       		add(
                    	    	       			current, 
                    	    	       			"mappings",
                    	    	        		lv_mappings_6_0, 
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

                    match(input,25,FOLLOW_25_in_ruleSwitch1689); 

                            createLeafNode(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_3(), null); 
                        

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:997:1: entryRuleSelection returns [EObject current=null] : iv_ruleSelection= ruleSelection EOF ;
    public final EObject entryRuleSelection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelection = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:998:2: (iv_ruleSelection= ruleSelection EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:999:2: iv_ruleSelection= ruleSelection EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSelectionRule(), currentNode); 
            pushFollow(FOLLOW_ruleSelection_in_entryRuleSelection1727);
            iv_ruleSelection=ruleSelection();
            _fsp--;

             current =iv_ruleSelection; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelection1737); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1006:1: ruleSelection returns [EObject current=null] : ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? ) ;
    public final EObject ruleSelection() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        EObject lv_mappings_4_0 = null;

        EObject lv_mappings_6_0 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1011:6: ( ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1012:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1012:1: ( 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )? )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1012:3: 'Selection' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )?
            {
            match(input,26,FOLLOW_26_in_ruleSelection1772); 

                    createLeafNode(grammarAccess.getSelectionAccess().getSelectionKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1016:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1016:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleSelection1783); 

                    createLeafNode(grammarAccess.getSelectionAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1020:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1021:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1021:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1022:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSelection1800); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1044:3: ( 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==23) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1044:5: 'mappings=[' ( (lv_mappings_4_0= ruleMapping ) ) ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )* ']'
                    {
                    match(input,23,FOLLOW_23_in_ruleSelection1817); 

                            createLeafNode(grammarAccess.getSelectionAccess().getMappingsKeyword_2_0(), null); 
                        
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1048:1: ( (lv_mappings_4_0= ruleMapping ) )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1049:1: (lv_mappings_4_0= ruleMapping )
                    {
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1049:1: (lv_mappings_4_0= ruleMapping )
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1050:3: lv_mappings_4_0= ruleMapping
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleMapping_in_ruleSelection1838);
                    lv_mappings_4_0=ruleMapping();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        try {
                    	       		add(
                    	       			current, 
                    	       			"mappings",
                    	        		lv_mappings_4_0, 
                    	        		"Mapping", 
                    	        		currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }

                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1072:2: ( ',' ( (lv_mappings_6_0= ruleMapping ) ) )*
                    loop22:
                    do {
                        int alt22=2;
                        int LA22_0 = input.LA(1);

                        if ( (LA22_0==24) ) {
                            alt22=1;
                        }


                        switch (alt22) {
                    	case 1 :
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1072:4: ',' ( (lv_mappings_6_0= ruleMapping ) )
                    	    {
                    	    match(input,24,FOLLOW_24_in_ruleSelection1849); 

                    	            createLeafNode(grammarAccess.getSelectionAccess().getCommaKeyword_2_2_0(), null); 
                    	        
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1076:1: ( (lv_mappings_6_0= ruleMapping ) )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1077:1: (lv_mappings_6_0= ruleMapping )
                    	    {
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1077:1: (lv_mappings_6_0= ruleMapping )
                    	    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1078:3: lv_mappings_6_0= ruleMapping
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_2_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMapping_in_ruleSelection1870);
                    	    lv_mappings_6_0=ruleMapping();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getSelectionRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        try {
                    	    	       		add(
                    	    	       			current, 
                    	    	       			"mappings",
                    	    	        		lv_mappings_6_0, 
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
                    	    break loop22;
                        }
                    } while (true);

                    match(input,25,FOLLOW_25_in_ruleSelection1882); 

                            createLeafNode(grammarAccess.getSelectionAccess().getRightSquareBracketKeyword_2_3(), null); 
                        

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1112:1: entryRuleList returns [EObject current=null] : iv_ruleList= ruleList EOF ;
    public final EObject entryRuleList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleList = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1113:2: (iv_ruleList= ruleList EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1114:2: iv_ruleList= ruleList EOF
            {
             currentNode = createCompositeNode(grammarAccess.getListRule(), currentNode); 
            pushFollow(FOLLOW_ruleList_in_entryRuleList1920);
            iv_ruleList=ruleList();
            _fsp--;

             current =iv_ruleList; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleList1930); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1121:1: ruleList returns [EObject current=null] : ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) ) ) ;
    public final EObject ruleList() throws RecognitionException {
        EObject current = null;

        Token lv_item_2_0=null;
        Token lv_separator_4_0=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1126:6: ( ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1127:1: ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1127:1: ( 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1127:3: 'List' ( 'item=' ( (lv_item_2_0= RULE_ID ) ) ) ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) )
            {
            match(input,27,FOLLOW_27_in_ruleList1965); 

                    createLeafNode(grammarAccess.getListAccess().getListKeyword_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1131:1: ( 'item=' ( (lv_item_2_0= RULE_ID ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1131:3: 'item=' ( (lv_item_2_0= RULE_ID ) )
            {
            match(input,17,FOLLOW_17_in_ruleList1976); 

                    createLeafNode(grammarAccess.getListAccess().getItemKeyword_1_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1135:1: ( (lv_item_2_0= RULE_ID ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1136:1: (lv_item_2_0= RULE_ID )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1136:1: (lv_item_2_0= RULE_ID )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1137:3: lv_item_2_0= RULE_ID
            {
            lv_item_2_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleList1993); 

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

            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1159:3: ( 'separator=' ( (lv_separator_4_0= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1159:5: 'separator=' ( (lv_separator_4_0= RULE_STRING ) )
            {
            match(input,28,FOLLOW_28_in_ruleList2010); 

                    createLeafNode(grammarAccess.getListAccess().getSeparatorKeyword_2_0(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1163:1: ( (lv_separator_4_0= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1164:1: (lv_separator_4_0= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1164:1: (lv_separator_4_0= RULE_STRING )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1165:3: lv_separator_4_0= RULE_STRING
            {
            lv_separator_4_0=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleList2027); 

            			createLeafNode(grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_2_1_0(), "separator"); 
            		

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getListRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        try {
            	       		set(
            	       			current, 
            	       			"separator",
            	        		lv_separator_4_0, 
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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1195:1: entryRuleMapping returns [EObject current=null] : iv_ruleMapping= ruleMapping EOF ;
    public final EObject entryRuleMapping() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapping = null;


        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1196:2: (iv_ruleMapping= ruleMapping EOF )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1197:2: iv_ruleMapping= ruleMapping EOF
            {
             currentNode = createCompositeNode(grammarAccess.getMappingRule(), currentNode); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping2069);
            iv_ruleMapping=ruleMapping();
            _fsp--;

             current =iv_ruleMapping; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping2079); 

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
    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1204:1: ruleMapping returns [EObject current=null] : ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) ;
    public final EObject ruleMapping() throws RecognitionException {
        EObject current = null;

        Token lv_cmd_0_1=null;
        Token lv_cmd_0_2=null;
        Token lv_label_2_1=null;
        Token lv_label_2_2=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1209:6: ( ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1210:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1210:1: ( ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1210:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) ) '=' ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1210:2: ( ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1211:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1211:1: ( (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1212:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1212:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )
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
                    new NoViableAltException("1212:1: (lv_cmd_0_1= RULE_ID | lv_cmd_0_2= RULE_STRING )", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1213:3: lv_cmd_0_1= RULE_ID
                    {
                    lv_cmd_0_1=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2123); 

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
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1234:8: lv_cmd_0_2= RULE_STRING
                    {
                    lv_cmd_0_2=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2143); 

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

            match(input,29,FOLLOW_29_in_ruleMapping2161); 

                    createLeafNode(grammarAccess.getMappingAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1262:1: ( ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1263:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1263:1: ( (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING ) )
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1264:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
            {
            // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1264:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==RULE_ID) ) {
                alt25=1;
            }
            else if ( (LA25_0==RULE_STRING) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1264:1: (lv_label_2_1= RULE_ID | lv_label_2_2= RULE_STRING )", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1265:3: lv_label_2_1= RULE_ID
                    {
                    lv_label_2_1=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping2180); 

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
                    // ../org.openhab.model.sitemap/src-gen/org/openhab/model/parser/antlr/internal/InternalSitemap.g:1286:8: lv_label_2_2= RULE_STRING
                    {
                    lv_label_2_2=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping2200); 

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
    public static final BitSet FOLLOW_14_in_ruleSitemap314 = new BitSet(new long[]{0x000000000C5D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleSitemap335 = new BitSet(new long[]{0x000000000C5D8000L});
    public static final BitSet FOLLOW_15_in_ruleSitemap346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget382 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_ruleWidget439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_ruleWidget468 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_ruleSelection_in_ruleWidget495 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_ruleList_in_ruleWidget522 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_12_in_ruleWidget533 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget552 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget572 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleWidget593 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleWidget612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleWidget632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget679 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkableWidget689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_ruleLinkableWidget737 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleGroup_in_ruleLinkableWidget764 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleImage_in_ruleLinkableWidget791 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_ruleFrame_in_ruleLinkableWidget818 = new BitSet(new long[]{0x0000000000007002L});
    public static final BitSet FOLLOW_12_in_ruleLinkableWidget829 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget848 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget868 = new BitSet(new long[]{0x0000000000006002L});
    public static final BitSet FOLLOW_13_in_ruleLinkableWidget889 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleLinkableWidget908 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleLinkableWidget928 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_ruleLinkableWidget949 = new BitSet(new long[]{0x000000000C5D0000L});
    public static final BitSet FOLLOW_ruleWidget_in_ruleLinkableWidget970 = new BitSet(new long[]{0x000000000C5D8000L});
    public static final BitSet FOLLOW_15_in_ruleLinkableWidget981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame1019 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame1029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleFrame1064 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleFrame1084 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFrame1101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText1144 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText1154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleText1189 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_ruleText1209 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleText1226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup1269 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup1279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleGroup1314 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleGroup1325 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleGroup1342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage1384 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage1394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_ruleImage1429 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_17_in_ruleImage1440 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleImage1457 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_ruleImage1475 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImage1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch1534 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch1544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleSwitch1579 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSwitch1590 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSwitch1607 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSwitch1624 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1645 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSwitch1656 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSwitch1677 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSwitch1689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_entryRuleSelection1727 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelection1737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleSelection1772 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleSelection1783 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSelection1800 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_23_in_ruleSelection1817 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection1838 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_ruleSelection1849 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleMapping_in_ruleSelection1870 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_ruleSelection1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_entryRuleList1920 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleList1930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_ruleList1965 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleList1976 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleList1993 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_ruleList2010 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleList2027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping2069 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping2079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2123 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2143 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_ruleMapping2161 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping2180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping2200 = new BitSet(new long[]{0x0000000000000002L});

}