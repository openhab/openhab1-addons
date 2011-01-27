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
import org.openhab.model.services.SitemapGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSitemapParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'{'", "'}'", "'label='", "'icon='", "'Frame'", "'item='", "'Text'", "'Group'", "'Image'", "'url='", "'Switch'", "'mappings=['", "']'", "', '", "'Selection'", "'List'", "'separator='", "'='", "':'"
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
    public String getGrammarFileName() { return "../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g"; }


     
     	private SitemapGrammarAccess grammarAccess;
     	
        public void setGrammarAccess(SitemapGrammarAccess grammarAccess) {
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




    // $ANTLR start entryRuleSitemapModel
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:61:1: entryRuleSitemapModel : ruleSitemapModel EOF ;
    public final void entryRuleSitemapModel() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:62:1: ( ruleSitemapModel EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:63:1: ruleSitemapModel EOF
            {
             before(grammarAccess.getSitemapModelRule()); 
            pushFollow(FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel61);
            ruleSitemapModel();
            _fsp--;

             after(grammarAccess.getSitemapModelRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSitemapModel68); 

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
    // $ANTLR end entryRuleSitemapModel


    // $ANTLR start ruleSitemapModel
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:70:1: ruleSitemapModel : ( ( rule__SitemapModel__Group__0 ) ) ;
    public final void ruleSitemapModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:74:2: ( ( ( rule__SitemapModel__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:75:1: ( ( rule__SitemapModel__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:75:1: ( ( rule__SitemapModel__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:76:1: ( rule__SitemapModel__Group__0 )
            {
             before(grammarAccess.getSitemapModelAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:77:1: ( rule__SitemapModel__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:77:2: rule__SitemapModel__Group__0
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__0_in_ruleSitemapModel94);
            rule__SitemapModel__Group__0();
            _fsp--;


            }

             after(grammarAccess.getSitemapModelAccess().getGroup()); 

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
    // $ANTLR end ruleSitemapModel


    // $ANTLR start entryRuleSitemap
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:89:1: entryRuleSitemap : ruleSitemap EOF ;
    public final void entryRuleSitemap() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:90:1: ( ruleSitemap EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:91:1: ruleSitemap EOF
            {
             before(grammarAccess.getSitemapRule()); 
            pushFollow(FOLLOW_ruleSitemap_in_entryRuleSitemap121);
            ruleSitemap();
            _fsp--;

             after(grammarAccess.getSitemapRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSitemap128); 

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
    // $ANTLR end entryRuleSitemap


    // $ANTLR start ruleSitemap
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:98:1: ruleSitemap : ( ( rule__Sitemap__Group__0 ) ) ;
    public final void ruleSitemap() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:102:2: ( ( ( rule__Sitemap__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:103:1: ( ( rule__Sitemap__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:103:1: ( ( rule__Sitemap__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:104:1: ( rule__Sitemap__Group__0 )
            {
             before(grammarAccess.getSitemapAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:105:1: ( rule__Sitemap__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:105:2: rule__Sitemap__Group__0
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__0_in_ruleSitemap154);
            rule__Sitemap__Group__0();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getGroup()); 

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
    // $ANTLR end ruleSitemap


    // $ANTLR start entryRuleWidget
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:117:1: entryRuleWidget : ruleWidget EOF ;
    public final void entryRuleWidget() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:118:1: ( ruleWidget EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:119:1: ruleWidget EOF
            {
             before(grammarAccess.getWidgetRule()); 
            pushFollow(FOLLOW_ruleWidget_in_entryRuleWidget181);
            ruleWidget();
            _fsp--;

             after(grammarAccess.getWidgetRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWidget188); 

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
    // $ANTLR end entryRuleWidget


    // $ANTLR start ruleWidget
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:126:1: ruleWidget : ( ( rule__Widget__Alternatives ) ) ;
    public final void ruleWidget() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:130:2: ( ( ( rule__Widget__Alternatives ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:131:1: ( ( rule__Widget__Alternatives ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:131:1: ( ( rule__Widget__Alternatives ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:132:1: ( rule__Widget__Alternatives )
            {
             before(grammarAccess.getWidgetAccess().getAlternatives()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:133:1: ( rule__Widget__Alternatives )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:133:2: rule__Widget__Alternatives
            {
            pushFollow(FOLLOW_rule__Widget__Alternatives_in_ruleWidget214);
            rule__Widget__Alternatives();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getAlternatives()); 

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
    // $ANTLR end ruleWidget


    // $ANTLR start entryRuleLinkableWidget
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:145:1: entryRuleLinkableWidget : ruleLinkableWidget EOF ;
    public final void entryRuleLinkableWidget() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:146:1: ( ruleLinkableWidget EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:147:1: ruleLinkableWidget EOF
            {
             before(grammarAccess.getLinkableWidgetRule()); 
            pushFollow(FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget241);
            ruleLinkableWidget();
            _fsp--;

             after(grammarAccess.getLinkableWidgetRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLinkableWidget248); 

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
    // $ANTLR end entryRuleLinkableWidget


    // $ANTLR start ruleLinkableWidget
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:154:1: ruleLinkableWidget : ( ( rule__LinkableWidget__Group__0 ) ) ;
    public final void ruleLinkableWidget() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:158:2: ( ( ( rule__LinkableWidget__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:159:1: ( ( rule__LinkableWidget__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:159:1: ( ( rule__LinkableWidget__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:160:1: ( rule__LinkableWidget__Group__0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:161:1: ( rule__LinkableWidget__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:161:2: rule__LinkableWidget__Group__0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__0_in_ruleLinkableWidget274);
            rule__LinkableWidget__Group__0();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getGroup()); 

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
    // $ANTLR end ruleLinkableWidget


    // $ANTLR start entryRuleFrame
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:173:1: entryRuleFrame : ruleFrame EOF ;
    public final void entryRuleFrame() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:174:1: ( ruleFrame EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:175:1: ruleFrame EOF
            {
             before(grammarAccess.getFrameRule()); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame301);
            ruleFrame();
            _fsp--;

             after(grammarAccess.getFrameRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame308); 

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
    // $ANTLR end entryRuleFrame


    // $ANTLR start ruleFrame
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:182:1: ruleFrame : ( ( rule__Frame__Group__0 ) ) ;
    public final void ruleFrame() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:186:2: ( ( ( rule__Frame__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:187:1: ( ( rule__Frame__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:187:1: ( ( rule__Frame__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:188:1: ( rule__Frame__Group__0 )
            {
             before(grammarAccess.getFrameAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:189:1: ( rule__Frame__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:189:2: rule__Frame__Group__0
            {
            pushFollow(FOLLOW_rule__Frame__Group__0_in_ruleFrame334);
            rule__Frame__Group__0();
            _fsp--;


            }

             after(grammarAccess.getFrameAccess().getGroup()); 

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
    // $ANTLR end ruleFrame


    // $ANTLR start entryRuleText
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:201:1: entryRuleText : ruleText EOF ;
    public final void entryRuleText() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:202:1: ( ruleText EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:203:1: ruleText EOF
            {
             before(grammarAccess.getTextRule()); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText361);
            ruleText();
            _fsp--;

             after(grammarAccess.getTextRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText368); 

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
    // $ANTLR end entryRuleText


    // $ANTLR start ruleText
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:210:1: ruleText : ( ( rule__Text__Group__0 ) ) ;
    public final void ruleText() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:214:2: ( ( ( rule__Text__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:215:1: ( ( rule__Text__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:215:1: ( ( rule__Text__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:216:1: ( rule__Text__Group__0 )
            {
             before(grammarAccess.getTextAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:217:1: ( rule__Text__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:217:2: rule__Text__Group__0
            {
            pushFollow(FOLLOW_rule__Text__Group__0_in_ruleText394);
            rule__Text__Group__0();
            _fsp--;


            }

             after(grammarAccess.getTextAccess().getGroup()); 

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
    // $ANTLR end ruleText


    // $ANTLR start entryRuleGroup
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:229:1: entryRuleGroup : ruleGroup EOF ;
    public final void entryRuleGroup() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:230:1: ( ruleGroup EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:231:1: ruleGroup EOF
            {
             before(grammarAccess.getGroupRule()); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup421);
            ruleGroup();
            _fsp--;

             after(grammarAccess.getGroupRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup428); 

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
    // $ANTLR end entryRuleGroup


    // $ANTLR start ruleGroup
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:238:1: ruleGroup : ( ( rule__Group__Group__0 ) ) ;
    public final void ruleGroup() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:242:2: ( ( ( rule__Group__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:243:1: ( ( rule__Group__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:243:1: ( ( rule__Group__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:244:1: ( rule__Group__Group__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:245:1: ( rule__Group__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:245:2: rule__Group__Group__0
            {
            pushFollow(FOLLOW_rule__Group__Group__0_in_ruleGroup454);
            rule__Group__Group__0();
            _fsp--;


            }

             after(grammarAccess.getGroupAccess().getGroup()); 

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
    // $ANTLR end ruleGroup


    // $ANTLR start entryRuleImage
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:257:1: entryRuleImage : ruleImage EOF ;
    public final void entryRuleImage() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:258:1: ( ruleImage EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:259:1: ruleImage EOF
            {
             before(grammarAccess.getImageRule()); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage481);
            ruleImage();
            _fsp--;

             after(grammarAccess.getImageRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage488); 

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
    // $ANTLR end entryRuleImage


    // $ANTLR start ruleImage
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:266:1: ruleImage : ( ( rule__Image__Group__0 ) ) ;
    public final void ruleImage() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:270:2: ( ( ( rule__Image__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:271:1: ( ( rule__Image__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:271:1: ( ( rule__Image__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:272:1: ( rule__Image__Group__0 )
            {
             before(grammarAccess.getImageAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:273:1: ( rule__Image__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:273:2: rule__Image__Group__0
            {
            pushFollow(FOLLOW_rule__Image__Group__0_in_ruleImage514);
            rule__Image__Group__0();
            _fsp--;


            }

             after(grammarAccess.getImageAccess().getGroup()); 

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
    // $ANTLR end ruleImage


    // $ANTLR start entryRuleSwitch
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:285:1: entryRuleSwitch : ruleSwitch EOF ;
    public final void entryRuleSwitch() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:286:1: ( ruleSwitch EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:287:1: ruleSwitch EOF
            {
             before(grammarAccess.getSwitchRule()); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch541);
            ruleSwitch();
            _fsp--;

             after(grammarAccess.getSwitchRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch548); 

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
    // $ANTLR end entryRuleSwitch


    // $ANTLR start ruleSwitch
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:294:1: ruleSwitch : ( ( rule__Switch__Group__0 ) ) ;
    public final void ruleSwitch() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:298:2: ( ( ( rule__Switch__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:299:1: ( ( rule__Switch__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:299:1: ( ( rule__Switch__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:300:1: ( rule__Switch__Group__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:301:1: ( rule__Switch__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:301:2: rule__Switch__Group__0
            {
            pushFollow(FOLLOW_rule__Switch__Group__0_in_ruleSwitch574);
            rule__Switch__Group__0();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getGroup()); 

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
    // $ANTLR end ruleSwitch


    // $ANTLR start entryRuleSelection
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:313:1: entryRuleSelection : ruleSelection EOF ;
    public final void entryRuleSelection() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:314:1: ( ruleSelection EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:315:1: ruleSelection EOF
            {
             before(grammarAccess.getSelectionRule()); 
            pushFollow(FOLLOW_ruleSelection_in_entryRuleSelection601);
            ruleSelection();
            _fsp--;

             after(grammarAccess.getSelectionRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelection608); 

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
    // $ANTLR end entryRuleSelection


    // $ANTLR start ruleSelection
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:322:1: ruleSelection : ( ( rule__Selection__Group__0 ) ) ;
    public final void ruleSelection() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:326:2: ( ( ( rule__Selection__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:327:1: ( ( rule__Selection__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:327:1: ( ( rule__Selection__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:328:1: ( rule__Selection__Group__0 )
            {
             before(grammarAccess.getSelectionAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:329:1: ( rule__Selection__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:329:2: rule__Selection__Group__0
            {
            pushFollow(FOLLOW_rule__Selection__Group__0_in_ruleSelection634);
            rule__Selection__Group__0();
            _fsp--;


            }

             after(grammarAccess.getSelectionAccess().getGroup()); 

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
    // $ANTLR end ruleSelection


    // $ANTLR start entryRuleList
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:341:1: entryRuleList : ruleList EOF ;
    public final void entryRuleList() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:342:1: ( ruleList EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:343:1: ruleList EOF
            {
             before(grammarAccess.getListRule()); 
            pushFollow(FOLLOW_ruleList_in_entryRuleList661);
            ruleList();
            _fsp--;

             after(grammarAccess.getListRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleList668); 

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
    // $ANTLR end entryRuleList


    // $ANTLR start ruleList
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:350:1: ruleList : ( ( rule__List__Group__0 ) ) ;
    public final void ruleList() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:354:2: ( ( ( rule__List__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:355:1: ( ( rule__List__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:355:1: ( ( rule__List__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:356:1: ( rule__List__Group__0 )
            {
             before(grammarAccess.getListAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:357:1: ( rule__List__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:357:2: rule__List__Group__0
            {
            pushFollow(FOLLOW_rule__List__Group__0_in_ruleList694);
            rule__List__Group__0();
            _fsp--;


            }

             after(grammarAccess.getListAccess().getGroup()); 

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
    // $ANTLR end ruleList


    // $ANTLR start entryRuleMapping
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:369:1: entryRuleMapping : ruleMapping EOF ;
    public final void entryRuleMapping() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:370:1: ( ruleMapping EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:371:1: ruleMapping EOF
            {
             before(grammarAccess.getMappingRule()); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping721);
            ruleMapping();
            _fsp--;

             after(grammarAccess.getMappingRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping728); 

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
    // $ANTLR end entryRuleMapping


    // $ANTLR start ruleMapping
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:378:1: ruleMapping : ( ( rule__Mapping__Group__0 ) ) ;
    public final void ruleMapping() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:382:2: ( ( ( rule__Mapping__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:383:1: ( ( rule__Mapping__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:383:1: ( ( rule__Mapping__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:384:1: ( rule__Mapping__Group__0 )
            {
             before(grammarAccess.getMappingAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:385:1: ( rule__Mapping__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:385:2: rule__Mapping__Group__0
            {
            pushFollow(FOLLOW_rule__Mapping__Group__0_in_ruleMapping754);
            rule__Mapping__Group__0();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getGroup()); 

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
    // $ANTLR end ruleMapping


    // $ANTLR start rule__Widget__Alternatives
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:397:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );
    public final void rule__Widget__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:401:1: ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==16||(LA1_0>=18 && LA1_0<=20)) ) {
                alt1=1;
            }
            else if ( (LA1_0==22||(LA1_0>=26 && LA1_0<=27)) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("397:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:402:1: ( ruleLinkableWidget )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:402:1: ( ruleLinkableWidget )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:403:1: ruleLinkableWidget
                    {
                     before(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives790);
                    ruleLinkableWidget();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:408:6: ( ( rule__Widget__Group_1__0 ) )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:408:6: ( ( rule__Widget__Group_1__0 ) )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:409:1: ( rule__Widget__Group_1__0 )
                    {
                     before(grammarAccess.getWidgetAccess().getGroup_1()); 
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:410:1: ( rule__Widget__Group_1__0 )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:410:2: rule__Widget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives807);
                    rule__Widget__Group_1__0();
                    _fsp--;


                    }

                     after(grammarAccess.getWidgetAccess().getGroup_1()); 

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
    // $ANTLR end rule__Widget__Alternatives


    // $ANTLR start rule__Widget__Alternatives_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:419:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) );
    public final void rule__Widget__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:423:1: ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) )
            int alt2=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt2=1;
                }
                break;
            case 26:
                {
                alt2=2;
                }
                break;
            case 27:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("419:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) );", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:424:1: ( ruleSwitch )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:424:1: ( ruleSwitch )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:425:1: ruleSwitch
                    {
                     before(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 
                    pushFollow(FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0840);
                    ruleSwitch();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:430:6: ( ruleSelection )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:430:6: ( ruleSelection )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:431:1: ruleSelection
                    {
                     before(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 
                    pushFollow(FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0857);
                    ruleSelection();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:436:6: ( ruleList )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:436:6: ( ruleList )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:437:1: ruleList
                    {
                     before(grammarAccess.getWidgetAccess().getListParserRuleCall_1_0_2()); 
                    pushFollow(FOLLOW_ruleList_in_rule__Widget__Alternatives_1_0874);
                    ruleList();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getListParserRuleCall_1_0_2()); 

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
    // $ANTLR end rule__Widget__Alternatives_1_0


    // $ANTLR start rule__Widget__LabelAlternatives_1_1_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:447:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__LabelAlternatives_1_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:451:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("447:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:453:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0906); 
                     after(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:458:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:458:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:459:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0923); 
                     after(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1()); 

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
    // $ANTLR end rule__Widget__LabelAlternatives_1_1_1_0


    // $ANTLR start rule__Widget__IconAlternatives_1_2_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:469:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__IconAlternatives_1_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:473:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("469:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:475:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0955); 
                     after(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:481:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0972); 
                     after(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1()); 

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
    // $ANTLR end rule__Widget__IconAlternatives_1_2_1_0


    // $ANTLR start rule__LinkableWidget__Alternatives_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:491:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );
    public final void rule__LinkableWidget__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:495:1: ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) )
            int alt5=4;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt5=1;
                }
                break;
            case 19:
                {
                alt5=2;
                }
                break;
            case 20:
                {
                alt5=3;
                }
                break;
            case 16:
                {
                alt5=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("491:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:496:1: ( ruleText )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:496:1: ( ruleText )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:497:1: ruleText
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_01004);
                    ruleText();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:502:6: ( ruleGroup )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:502:6: ( ruleGroup )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:503:1: ruleGroup
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_01021);
                    ruleGroup();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:508:6: ( ruleImage )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:508:6: ( ruleImage )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:509:1: ruleImage
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 
                    pushFollow(FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_01038);
                    ruleImage();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:514:6: ( ruleFrame )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:514:6: ( ruleFrame )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:515:1: ruleFrame
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3()); 
                    pushFollow(FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_01055);
                    ruleFrame();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3()); 

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
    // $ANTLR end rule__LinkableWidget__Alternatives_0


    // $ANTLR start rule__LinkableWidget__LabelAlternatives_1_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:525:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__LabelAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:529:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_ID) ) {
                alt6=1;
            }
            else if ( (LA6_0==RULE_STRING) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("525:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:530:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:530:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:531:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_01087); 
                     after(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:536:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:536:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:537:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_01104); 
                     after(grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 

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
    // $ANTLR end rule__LinkableWidget__LabelAlternatives_1_1_0


    // $ANTLR start rule__LinkableWidget__IconAlternatives_2_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:547:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__IconAlternatives_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:551:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("547:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:552:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:552:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:553:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_01136); 
                     after(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:558:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:558:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:559:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01153); 
                     after(grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 

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
    // $ANTLR end rule__LinkableWidget__IconAlternatives_2_1_0


    // $ANTLR start rule__Mapping__CmdAlternatives_0_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:569:1: rule__Mapping__CmdAlternatives_0_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Mapping__CmdAlternatives_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:573:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==RULE_ID) ) {
                alt8=1;
            }
            else if ( (LA8_0==RULE_STRING) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("569:1: rule__Mapping__CmdAlternatives_0_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:574:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:574:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:575:1: RULE_ID
                    {
                     before(grammarAccess.getMappingAccess().getCmdIDTerminalRuleCall_0_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__CmdAlternatives_0_01185); 
                     after(grammarAccess.getMappingAccess().getCmdIDTerminalRuleCall_0_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:580:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:580:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:581:1: RULE_STRING
                    {
                     before(grammarAccess.getMappingAccess().getCmdSTRINGTerminalRuleCall_0_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Mapping__CmdAlternatives_0_01202); 
                     after(grammarAccess.getMappingAccess().getCmdSTRINGTerminalRuleCall_0_0_1()); 

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
    // $ANTLR end rule__Mapping__CmdAlternatives_0_0


    // $ANTLR start rule__Mapping__LabelAlternatives_2_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:591:1: rule__Mapping__LabelAlternatives_2_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Mapping__LabelAlternatives_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:595:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("591:1: rule__Mapping__LabelAlternatives_2_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:596:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:596:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:597:1: RULE_ID
                    {
                     before(grammarAccess.getMappingAccess().getLabelIDTerminalRuleCall_2_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__LabelAlternatives_2_01234); 
                     after(grammarAccess.getMappingAccess().getLabelIDTerminalRuleCall_2_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:602:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:602:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:603:1: RULE_STRING
                    {
                     before(grammarAccess.getMappingAccess().getLabelSTRINGTerminalRuleCall_2_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Mapping__LabelAlternatives_2_01251); 
                     after(grammarAccess.getMappingAccess().getLabelSTRINGTerminalRuleCall_2_0_1()); 

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
    // $ANTLR end rule__Mapping__LabelAlternatives_2_0


    // $ANTLR start rule__Mapping__IconAlternatives_3_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:613:1: rule__Mapping__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Mapping__IconAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:617:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("613:1: rule__Mapping__IconAlternatives_3_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:618:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:618:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:619:1: RULE_ID
                    {
                     before(grammarAccess.getMappingAccess().getIconIDTerminalRuleCall_3_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__IconAlternatives_3_1_01283); 
                     after(grammarAccess.getMappingAccess().getIconIDTerminalRuleCall_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:624:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:624:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:625:1: RULE_STRING
                    {
                     before(grammarAccess.getMappingAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Mapping__IconAlternatives_3_1_01300); 
                     after(grammarAccess.getMappingAccess().getIconSTRINGTerminalRuleCall_3_1_0_1()); 

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
    // $ANTLR end rule__Mapping__IconAlternatives_3_1_0


    // $ANTLR start rule__SitemapModel__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:637:1: rule__SitemapModel__Group__0 : rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 ;
    public final void rule__SitemapModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:641:1: ( rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:642:2: rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01330);
            rule__SitemapModel__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01333);
            rule__SitemapModel__Group__1();
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
    // $ANTLR end rule__SitemapModel__Group__0


    // $ANTLR start rule__SitemapModel__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:649:1: rule__SitemapModel__Group__0__Impl : ( 'sitemap' ) ;
    public final void rule__SitemapModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:653:1: ( ( 'sitemap' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:654:1: ( 'sitemap' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:654:1: ( 'sitemap' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:655:1: 'sitemap'
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapKeyword_0()); 
            match(input,11,FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1361); 
             after(grammarAccess.getSitemapModelAccess().getSitemapKeyword_0()); 

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
    // $ANTLR end rule__SitemapModel__Group__0__Impl


    // $ANTLR start rule__SitemapModel__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:668:1: rule__SitemapModel__Group__1 : rule__SitemapModel__Group__1__Impl ;
    public final void rule__SitemapModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:672:1: ( rule__SitemapModel__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:673:2: rule__SitemapModel__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11392);
            rule__SitemapModel__Group__1__Impl();
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
    // $ANTLR end rule__SitemapModel__Group__1


    // $ANTLR start rule__SitemapModel__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:679:1: rule__SitemapModel__Group__1__Impl : ( ruleSitemap ) ;
    public final void rule__SitemapModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:683:1: ( ( ruleSitemap ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:684:1: ( ruleSitemap )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:684:1: ( ruleSitemap )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:685:1: ruleSitemap
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1()); 
            pushFollow(FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1419);
            ruleSitemap();
            _fsp--;

             after(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1()); 

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
    // $ANTLR end rule__SitemapModel__Group__1__Impl


    // $ANTLR start rule__Sitemap__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:700:1: rule__Sitemap__Group__0 : rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 ;
    public final void rule__Sitemap__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:704:1: ( rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:705:2: rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01452);
            rule__Sitemap__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01455);
            rule__Sitemap__Group__1();
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
    // $ANTLR end rule__Sitemap__Group__0


    // $ANTLR start rule__Sitemap__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:712:1: rule__Sitemap__Group__0__Impl : ( ( rule__Sitemap__NameAssignment_0 ) ) ;
    public final void rule__Sitemap__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:716:1: ( ( ( rule__Sitemap__NameAssignment_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:717:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:717:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:718:1: ( rule__Sitemap__NameAssignment_0 )
            {
             before(grammarAccess.getSitemapAccess().getNameAssignment_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:719:1: ( rule__Sitemap__NameAssignment_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:719:2: rule__Sitemap__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1482);
            rule__Sitemap__NameAssignment_0();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getNameAssignment_0()); 

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
    // $ANTLR end rule__Sitemap__Group__0__Impl


    // $ANTLR start rule__Sitemap__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:729:1: rule__Sitemap__Group__1 : rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 ;
    public final void rule__Sitemap__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:733:1: ( rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:734:2: rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11512);
            rule__Sitemap__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11515);
            rule__Sitemap__Group__2();
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
    // $ANTLR end rule__Sitemap__Group__1


    // $ANTLR start rule__Sitemap__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:741:1: rule__Sitemap__Group__1__Impl : ( ( rule__Sitemap__Group_1__0 )? ) ;
    public final void rule__Sitemap__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:745:1: ( ( ( rule__Sitemap__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:746:1: ( ( rule__Sitemap__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:746:1: ( ( rule__Sitemap__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:747:1: ( rule__Sitemap__Group_1__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:748:1: ( rule__Sitemap__Group_1__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==14) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:748:2: rule__Sitemap__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1542);
                    rule__Sitemap__Group_1__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSitemapAccess().getGroup_1()); 

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
    // $ANTLR end rule__Sitemap__Group__1__Impl


    // $ANTLR start rule__Sitemap__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:758:1: rule__Sitemap__Group__2 : rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 ;
    public final void rule__Sitemap__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:762:1: ( rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:763:2: rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21573);
            rule__Sitemap__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21576);
            rule__Sitemap__Group__3();
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
    // $ANTLR end rule__Sitemap__Group__2


    // $ANTLR start rule__Sitemap__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:770:1: rule__Sitemap__Group__2__Impl : ( ( rule__Sitemap__Group_2__0 )? ) ;
    public final void rule__Sitemap__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:774:1: ( ( ( rule__Sitemap__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:775:1: ( ( rule__Sitemap__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:775:1: ( ( rule__Sitemap__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:776:1: ( rule__Sitemap__Group_2__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:777:1: ( rule__Sitemap__Group_2__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==15) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:777:2: rule__Sitemap__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1603);
                    rule__Sitemap__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSitemapAccess().getGroup_2()); 

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
    // $ANTLR end rule__Sitemap__Group__2__Impl


    // $ANTLR start rule__Sitemap__Group__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:787:1: rule__Sitemap__Group__3 : rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 ;
    public final void rule__Sitemap__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:791:1: ( rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:792:2: rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31634);
            rule__Sitemap__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31637);
            rule__Sitemap__Group__4();
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
    // $ANTLR end rule__Sitemap__Group__3


    // $ANTLR start rule__Sitemap__Group__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:799:1: rule__Sitemap__Group__3__Impl : ( '{' ) ;
    public final void rule__Sitemap__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:803:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:804:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:804:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:805:1: '{'
            {
             before(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_12_in_rule__Sitemap__Group__3__Impl1665); 
             after(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3()); 

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
    // $ANTLR end rule__Sitemap__Group__3__Impl


    // $ANTLR start rule__Sitemap__Group__4
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:818:1: rule__Sitemap__Group__4 : rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 ;
    public final void rule__Sitemap__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:822:1: ( rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:823:2: rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41696);
            rule__Sitemap__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41699);
            rule__Sitemap__Group__5();
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
    // $ANTLR end rule__Sitemap__Group__4


    // $ANTLR start rule__Sitemap__Group__4__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:830:1: rule__Sitemap__Group__4__Impl : ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) ;
    public final void rule__Sitemap__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:834:1: ( ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:835:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:835:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:836:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:836:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:837:1: ( rule__Sitemap__ChildrenAssignment_4 )
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:838:1: ( rule__Sitemap__ChildrenAssignment_4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:838:2: rule__Sitemap__ChildrenAssignment_4
            {
            pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1728);
            rule__Sitemap__ChildrenAssignment_4();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:841:1: ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:842:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:843:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==16||(LA13_0>=18 && LA13_0<=20)||LA13_0==22||(LA13_0>=26 && LA13_0<=27)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:843:2: rule__Sitemap__ChildrenAssignment_4
            	    {
            	    pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1740);
            	    rule__Sitemap__ChildrenAssignment_4();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 

            }


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
    // $ANTLR end rule__Sitemap__Group__4__Impl


    // $ANTLR start rule__Sitemap__Group__5
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:854:1: rule__Sitemap__Group__5 : rule__Sitemap__Group__5__Impl ;
    public final void rule__Sitemap__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:858:1: ( rule__Sitemap__Group__5__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:859:2: rule__Sitemap__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51773);
            rule__Sitemap__Group__5__Impl();
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
    // $ANTLR end rule__Sitemap__Group__5


    // $ANTLR start rule__Sitemap__Group__5__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:865:1: rule__Sitemap__Group__5__Impl : ( '}' ) ;
    public final void rule__Sitemap__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:869:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:870:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:870:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:871:1: '}'
            {
             before(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5()); 
            match(input,13,FOLLOW_13_in_rule__Sitemap__Group__5__Impl1801); 
             after(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5()); 

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
    // $ANTLR end rule__Sitemap__Group__5__Impl


    // $ANTLR start rule__Sitemap__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:896:1: rule__Sitemap__Group_1__0 : rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 ;
    public final void rule__Sitemap__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:900:1: ( rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:901:2: rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01844);
            rule__Sitemap__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01847);
            rule__Sitemap__Group_1__1();
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
    // $ANTLR end rule__Sitemap__Group_1__0


    // $ANTLR start rule__Sitemap__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:908:1: rule__Sitemap__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__Sitemap__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:912:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:913:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:913:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:914:1: 'label='
            {
             before(grammarAccess.getSitemapAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1875); 
             after(grammarAccess.getSitemapAccess().getLabelKeyword_1_0()); 

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
    // $ANTLR end rule__Sitemap__Group_1__0__Impl


    // $ANTLR start rule__Sitemap__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:927:1: rule__Sitemap__Group_1__1 : rule__Sitemap__Group_1__1__Impl ;
    public final void rule__Sitemap__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:931:1: ( rule__Sitemap__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:932:2: rule__Sitemap__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11906);
            rule__Sitemap__Group_1__1__Impl();
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
    // $ANTLR end rule__Sitemap__Group_1__1


    // $ANTLR start rule__Sitemap__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:938:1: rule__Sitemap__Group_1__1__Impl : ( ( rule__Sitemap__LabelAssignment_1_1 ) ) ;
    public final void rule__Sitemap__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:942:1: ( ( ( rule__Sitemap__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:943:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:943:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:944:1: ( rule__Sitemap__LabelAssignment_1_1 )
            {
             before(grammarAccess.getSitemapAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:945:1: ( rule__Sitemap__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:945:2: rule__Sitemap__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1933);
            rule__Sitemap__LabelAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getLabelAssignment_1_1()); 

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
    // $ANTLR end rule__Sitemap__Group_1__1__Impl


    // $ANTLR start rule__Sitemap__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:959:1: rule__Sitemap__Group_2__0 : rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 ;
    public final void rule__Sitemap__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:963:1: ( rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:964:2: rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01967);
            rule__Sitemap__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01970);
            rule__Sitemap__Group_2__1();
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
    // $ANTLR end rule__Sitemap__Group_2__0


    // $ANTLR start rule__Sitemap__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:971:1: rule__Sitemap__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Sitemap__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:975:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:976:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:976:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:977:1: 'icon='
            {
             before(grammarAccess.getSitemapAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1998); 
             after(grammarAccess.getSitemapAccess().getIconKeyword_2_0()); 

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
    // $ANTLR end rule__Sitemap__Group_2__0__Impl


    // $ANTLR start rule__Sitemap__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:990:1: rule__Sitemap__Group_2__1 : rule__Sitemap__Group_2__1__Impl ;
    public final void rule__Sitemap__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:994:1: ( rule__Sitemap__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:995:2: rule__Sitemap__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__12029);
            rule__Sitemap__Group_2__1__Impl();
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
    // $ANTLR end rule__Sitemap__Group_2__1


    // $ANTLR start rule__Sitemap__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1001:1: rule__Sitemap__Group_2__1__Impl : ( ( rule__Sitemap__IconAssignment_2_1 ) ) ;
    public final void rule__Sitemap__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1005:1: ( ( ( rule__Sitemap__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1006:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1006:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1007:1: ( rule__Sitemap__IconAssignment_2_1 )
            {
             before(grammarAccess.getSitemapAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1008:1: ( rule__Sitemap__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1008:2: rule__Sitemap__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl2056);
            rule__Sitemap__IconAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getIconAssignment_2_1()); 

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
    // $ANTLR end rule__Sitemap__Group_2__1__Impl


    // $ANTLR start rule__Widget__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1022:1: rule__Widget__Group_1__0 : rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 ;
    public final void rule__Widget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1026:1: ( rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1027:2: rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__02090);
            rule__Widget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__02093);
            rule__Widget__Group_1__1();
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
    // $ANTLR end rule__Widget__Group_1__0


    // $ANTLR start rule__Widget__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1034:1: rule__Widget__Group_1__0__Impl : ( ( rule__Widget__Alternatives_1_0 ) ) ;
    public final void rule__Widget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1038:1: ( ( ( rule__Widget__Alternatives_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1039:1: ( ( rule__Widget__Alternatives_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1039:1: ( ( rule__Widget__Alternatives_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1040:1: ( rule__Widget__Alternatives_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getAlternatives_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1041:1: ( rule__Widget__Alternatives_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1041:2: rule__Widget__Alternatives_1_0
            {
            pushFollow(FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl2120);
            rule__Widget__Alternatives_1_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getAlternatives_1_0()); 

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
    // $ANTLR end rule__Widget__Group_1__0__Impl


    // $ANTLR start rule__Widget__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1051:1: rule__Widget__Group_1__1 : rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 ;
    public final void rule__Widget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1055:1: ( rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1056:2: rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__12150);
            rule__Widget__Group_1__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__12153);
            rule__Widget__Group_1__2();
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
    // $ANTLR end rule__Widget__Group_1__1


    // $ANTLR start rule__Widget__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1063:1: rule__Widget__Group_1__1__Impl : ( ( rule__Widget__Group_1_1__0 )? ) ;
    public final void rule__Widget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1067:1: ( ( ( rule__Widget__Group_1_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1068:1: ( ( rule__Widget__Group_1_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1068:1: ( ( rule__Widget__Group_1_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1069:1: ( rule__Widget__Group_1_1__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1070:1: ( rule__Widget__Group_1_1__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==14) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1070:2: rule__Widget__Group_1_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl2180);
                    rule__Widget__Group_1_1__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWidgetAccess().getGroup_1_1()); 

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
    // $ANTLR end rule__Widget__Group_1__1__Impl


    // $ANTLR start rule__Widget__Group_1__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1080:1: rule__Widget__Group_1__2 : rule__Widget__Group_1__2__Impl ;
    public final void rule__Widget__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1084:1: ( rule__Widget__Group_1__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1085:2: rule__Widget__Group_1__2__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__22211);
            rule__Widget__Group_1__2__Impl();
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
    // $ANTLR end rule__Widget__Group_1__2


    // $ANTLR start rule__Widget__Group_1__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1091:1: rule__Widget__Group_1__2__Impl : ( ( rule__Widget__Group_1_2__0 )? ) ;
    public final void rule__Widget__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1095:1: ( ( ( rule__Widget__Group_1_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1096:1: ( ( rule__Widget__Group_1_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1096:1: ( ( rule__Widget__Group_1_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1097:1: ( rule__Widget__Group_1_2__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1098:1: ( rule__Widget__Group_1_2__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==15) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1098:2: rule__Widget__Group_1_2__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl2238);
                    rule__Widget__Group_1_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWidgetAccess().getGroup_1_2()); 

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
    // $ANTLR end rule__Widget__Group_1__2__Impl


    // $ANTLR start rule__Widget__Group_1_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1114:1: rule__Widget__Group_1_1__0 : rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 ;
    public final void rule__Widget__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1118:1: ( rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1119:2: rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__02275);
            rule__Widget__Group_1_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__02278);
            rule__Widget__Group_1_1__1();
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
    // $ANTLR end rule__Widget__Group_1_1__0


    // $ANTLR start rule__Widget__Group_1_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1126:1: rule__Widget__Group_1_1__0__Impl : ( 'label=' ) ;
    public final void rule__Widget__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1130:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1131:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1131:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1132:1: 'label='
            {
             before(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2306); 
             after(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0()); 

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
    // $ANTLR end rule__Widget__Group_1_1__0__Impl


    // $ANTLR start rule__Widget__Group_1_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1145:1: rule__Widget__Group_1_1__1 : rule__Widget__Group_1_1__1__Impl ;
    public final void rule__Widget__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1149:1: ( rule__Widget__Group_1_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1150:2: rule__Widget__Group_1_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12337);
            rule__Widget__Group_1_1__1__Impl();
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
    // $ANTLR end rule__Widget__Group_1_1__1


    // $ANTLR start rule__Widget__Group_1_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1156:1: rule__Widget__Group_1_1__1__Impl : ( ( rule__Widget__LabelAssignment_1_1_1 ) ) ;
    public final void rule__Widget__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1160:1: ( ( ( rule__Widget__LabelAssignment_1_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1161:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1161:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1162:1: ( rule__Widget__LabelAssignment_1_1_1 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAssignment_1_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1163:1: ( rule__Widget__LabelAssignment_1_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1163:2: rule__Widget__LabelAssignment_1_1_1
            {
            pushFollow(FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2364);
            rule__Widget__LabelAssignment_1_1_1();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getLabelAssignment_1_1_1()); 

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
    // $ANTLR end rule__Widget__Group_1_1__1__Impl


    // $ANTLR start rule__Widget__Group_1_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1177:1: rule__Widget__Group_1_2__0 : rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 ;
    public final void rule__Widget__Group_1_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1181:1: ( rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1182:2: rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02398);
            rule__Widget__Group_1_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02401);
            rule__Widget__Group_1_2__1();
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
    // $ANTLR end rule__Widget__Group_1_2__0


    // $ANTLR start rule__Widget__Group_1_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1189:1: rule__Widget__Group_1_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Widget__Group_1_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1193:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1194:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1194:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1195:1: 'icon='
            {
             before(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2429); 
             after(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0()); 

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
    // $ANTLR end rule__Widget__Group_1_2__0__Impl


    // $ANTLR start rule__Widget__Group_1_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1208:1: rule__Widget__Group_1_2__1 : rule__Widget__Group_1_2__1__Impl ;
    public final void rule__Widget__Group_1_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1212:1: ( rule__Widget__Group_1_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1213:2: rule__Widget__Group_1_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12460);
            rule__Widget__Group_1_2__1__Impl();
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
    // $ANTLR end rule__Widget__Group_1_2__1


    // $ANTLR start rule__Widget__Group_1_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1219:1: rule__Widget__Group_1_2__1__Impl : ( ( rule__Widget__IconAssignment_1_2_1 ) ) ;
    public final void rule__Widget__Group_1_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1223:1: ( ( ( rule__Widget__IconAssignment_1_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1224:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1224:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1225:1: ( rule__Widget__IconAssignment_1_2_1 )
            {
             before(grammarAccess.getWidgetAccess().getIconAssignment_1_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1226:1: ( rule__Widget__IconAssignment_1_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1226:2: rule__Widget__IconAssignment_1_2_1
            {
            pushFollow(FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2487);
            rule__Widget__IconAssignment_1_2_1();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getIconAssignment_1_2_1()); 

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
    // $ANTLR end rule__Widget__Group_1_2__1__Impl


    // $ANTLR start rule__LinkableWidget__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1240:1: rule__LinkableWidget__Group__0 : rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 ;
    public final void rule__LinkableWidget__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1244:1: ( rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1245:2: rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02521);
            rule__LinkableWidget__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02524);
            rule__LinkableWidget__Group__1();
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
    // $ANTLR end rule__LinkableWidget__Group__0


    // $ANTLR start rule__LinkableWidget__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1252:1: rule__LinkableWidget__Group__0__Impl : ( ( rule__LinkableWidget__Alternatives_0 ) ) ;
    public final void rule__LinkableWidget__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1256:1: ( ( ( rule__LinkableWidget__Alternatives_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1257:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1257:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1258:1: ( rule__LinkableWidget__Alternatives_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getAlternatives_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1259:1: ( rule__LinkableWidget__Alternatives_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1259:2: rule__LinkableWidget__Alternatives_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2551);
            rule__LinkableWidget__Alternatives_0();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getAlternatives_0()); 

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
    // $ANTLR end rule__LinkableWidget__Group__0__Impl


    // $ANTLR start rule__LinkableWidget__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1269:1: rule__LinkableWidget__Group__1 : rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 ;
    public final void rule__LinkableWidget__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1273:1: ( rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1274:2: rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12581);
            rule__LinkableWidget__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12584);
            rule__LinkableWidget__Group__2();
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
    // $ANTLR end rule__LinkableWidget__Group__1


    // $ANTLR start rule__LinkableWidget__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1281:1: rule__LinkableWidget__Group__1__Impl : ( ( rule__LinkableWidget__Group_1__0 )? ) ;
    public final void rule__LinkableWidget__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1285:1: ( ( ( rule__LinkableWidget__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1286:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1286:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1287:1: ( rule__LinkableWidget__Group_1__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1288:1: ( rule__LinkableWidget__Group_1__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==14) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1288:2: rule__LinkableWidget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2611);
                    rule__LinkableWidget__Group_1__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getLinkableWidgetAccess().getGroup_1()); 

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
    // $ANTLR end rule__LinkableWidget__Group__1__Impl


    // $ANTLR start rule__LinkableWidget__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1298:1: rule__LinkableWidget__Group__2 : rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 ;
    public final void rule__LinkableWidget__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1302:1: ( rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1303:2: rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22642);
            rule__LinkableWidget__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22645);
            rule__LinkableWidget__Group__3();
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
    // $ANTLR end rule__LinkableWidget__Group__2


    // $ANTLR start rule__LinkableWidget__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1310:1: rule__LinkableWidget__Group__2__Impl : ( ( rule__LinkableWidget__Group_2__0 )? ) ;
    public final void rule__LinkableWidget__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1314:1: ( ( ( rule__LinkableWidget__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: ( rule__LinkableWidget__Group_2__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1317:1: ( rule__LinkableWidget__Group_2__0 )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==15) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1317:2: rule__LinkableWidget__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2672);
                    rule__LinkableWidget__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getLinkableWidgetAccess().getGroup_2()); 

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
    // $ANTLR end rule__LinkableWidget__Group__2__Impl


    // $ANTLR start rule__LinkableWidget__Group__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1327:1: rule__LinkableWidget__Group__3 : rule__LinkableWidget__Group__3__Impl ;
    public final void rule__LinkableWidget__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1331:1: ( rule__LinkableWidget__Group__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1332:2: rule__LinkableWidget__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32703);
            rule__LinkableWidget__Group__3__Impl();
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
    // $ANTLR end rule__LinkableWidget__Group__3


    // $ANTLR start rule__LinkableWidget__Group__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1338:1: rule__LinkableWidget__Group__3__Impl : ( ( rule__LinkableWidget__Group_3__0 )? ) ;
    public final void rule__LinkableWidget__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1342:1: ( ( ( rule__LinkableWidget__Group_3__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1343:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1343:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1344:1: ( rule__LinkableWidget__Group_3__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_3()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1345:1: ( rule__LinkableWidget__Group_3__0 )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==12) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1345:2: rule__LinkableWidget__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2730);
                    rule__LinkableWidget__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getLinkableWidgetAccess().getGroup_3()); 

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
    // $ANTLR end rule__LinkableWidget__Group__3__Impl


    // $ANTLR start rule__LinkableWidget__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1363:1: rule__LinkableWidget__Group_1__0 : rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 ;
    public final void rule__LinkableWidget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1367:1: ( rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1368:2: rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02769);
            rule__LinkableWidget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02772);
            rule__LinkableWidget__Group_1__1();
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
    // $ANTLR end rule__LinkableWidget__Group_1__0


    // $ANTLR start rule__LinkableWidget__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1375:1: rule__LinkableWidget__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__LinkableWidget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1380:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1380:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1381:1: 'label='
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2800); 
             after(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0()); 

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
    // $ANTLR end rule__LinkableWidget__Group_1__0__Impl


    // $ANTLR start rule__LinkableWidget__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1394:1: rule__LinkableWidget__Group_1__1 : rule__LinkableWidget__Group_1__1__Impl ;
    public final void rule__LinkableWidget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1398:1: ( rule__LinkableWidget__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1399:2: rule__LinkableWidget__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12831);
            rule__LinkableWidget__Group_1__1__Impl();
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
    // $ANTLR end rule__LinkableWidget__Group_1__1


    // $ANTLR start rule__LinkableWidget__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1405:1: rule__LinkableWidget__Group_1__1__Impl : ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) ;
    public final void rule__LinkableWidget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1409:1: ( ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1410:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1410:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1411:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:2: rule__LinkableWidget__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2858);
            rule__LinkableWidget__LabelAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getLabelAssignment_1_1()); 

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
    // $ANTLR end rule__LinkableWidget__Group_1__1__Impl


    // $ANTLR start rule__LinkableWidget__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1426:1: rule__LinkableWidget__Group_2__0 : rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 ;
    public final void rule__LinkableWidget__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1430:1: ( rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1431:2: rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02892);
            rule__LinkableWidget__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02895);
            rule__LinkableWidget__Group_2__1();
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
    // $ANTLR end rule__LinkableWidget__Group_2__0


    // $ANTLR start rule__LinkableWidget__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1438:1: rule__LinkableWidget__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__LinkableWidget__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1442:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1444:1: 'icon='
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2923); 
             after(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0()); 

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
    // $ANTLR end rule__LinkableWidget__Group_2__0__Impl


    // $ANTLR start rule__LinkableWidget__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1457:1: rule__LinkableWidget__Group_2__1 : rule__LinkableWidget__Group_2__1__Impl ;
    public final void rule__LinkableWidget__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1461:1: ( rule__LinkableWidget__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1462:2: rule__LinkableWidget__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12954);
            rule__LinkableWidget__Group_2__1__Impl();
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
    // $ANTLR end rule__LinkableWidget__Group_2__1


    // $ANTLR start rule__LinkableWidget__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1468:1: rule__LinkableWidget__Group_2__1__Impl : ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) ;
    public final void rule__LinkableWidget__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1472:1: ( ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1473:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1473:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1474:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1475:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1475:2: rule__LinkableWidget__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2981);
            rule__LinkableWidget__IconAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getIconAssignment_2_1()); 

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
    // $ANTLR end rule__LinkableWidget__Group_2__1__Impl


    // $ANTLR start rule__LinkableWidget__Group_3__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1489:1: rule__LinkableWidget__Group_3__0 : rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 ;
    public final void rule__LinkableWidget__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1493:1: ( rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1494:2: rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__03015);
            rule__LinkableWidget__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__03018);
            rule__LinkableWidget__Group_3__1();
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
    // $ANTLR end rule__LinkableWidget__Group_3__0


    // $ANTLR start rule__LinkableWidget__Group_3__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1501:1: rule__LinkableWidget__Group_3__0__Impl : ( '{' ) ;
    public final void rule__LinkableWidget__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1505:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1506:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1506:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1507:1: '{'
            {
             before(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 
            match(input,12,FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl3046); 
             after(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 

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
    // $ANTLR end rule__LinkableWidget__Group_3__0__Impl


    // $ANTLR start rule__LinkableWidget__Group_3__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1520:1: rule__LinkableWidget__Group_3__1 : rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 ;
    public final void rule__LinkableWidget__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1524:1: ( rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1525:2: rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__13077);
            rule__LinkableWidget__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__13080);
            rule__LinkableWidget__Group_3__2();
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
    // $ANTLR end rule__LinkableWidget__Group_3__1


    // $ANTLR start rule__LinkableWidget__Group_3__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1532:1: rule__LinkableWidget__Group_3__1__Impl : ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) ;
    public final void rule__LinkableWidget__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1536:1: ( ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1537:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1537:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1538:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1538:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1539:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1540:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1540:2: rule__LinkableWidget__ChildrenAssignment_3_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl3109);
            rule__LinkableWidget__ChildrenAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1543:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1544:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1545:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==16||(LA19_0>=18 && LA19_0<=20)||LA19_0==22||(LA19_0>=26 && LA19_0<=27)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1545:2: rule__LinkableWidget__ChildrenAssignment_3_1
            	    {
            	    pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl3121);
            	    rule__LinkableWidget__ChildrenAssignment_3_1();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

             after(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 

            }


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
    // $ANTLR end rule__LinkableWidget__Group_3__1__Impl


    // $ANTLR start rule__LinkableWidget__Group_3__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1556:1: rule__LinkableWidget__Group_3__2 : rule__LinkableWidget__Group_3__2__Impl ;
    public final void rule__LinkableWidget__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1560:1: ( rule__LinkableWidget__Group_3__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1561:2: rule__LinkableWidget__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__23154);
            rule__LinkableWidget__Group_3__2__Impl();
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
    // $ANTLR end rule__LinkableWidget__Group_3__2


    // $ANTLR start rule__LinkableWidget__Group_3__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1567:1: rule__LinkableWidget__Group_3__2__Impl : ( '}' ) ;
    public final void rule__LinkableWidget__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1571:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1572:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1572:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1573:1: '}'
            {
             before(grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2()); 
            match(input,13,FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl3182); 
             after(grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2()); 

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
    // $ANTLR end rule__LinkableWidget__Group_3__2__Impl


    // $ANTLR start rule__Frame__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1592:1: rule__Frame__Group__0 : rule__Frame__Group__0__Impl rule__Frame__Group__1 ;
    public final void rule__Frame__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1596:1: ( rule__Frame__Group__0__Impl rule__Frame__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1597:2: rule__Frame__Group__0__Impl rule__Frame__Group__1
            {
            pushFollow(FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__03219);
            rule__Frame__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__03222);
            rule__Frame__Group__1();
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
    // $ANTLR end rule__Frame__Group__0


    // $ANTLR start rule__Frame__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1604:1: rule__Frame__Group__0__Impl : ( 'Frame' ) ;
    public final void rule__Frame__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1608:1: ( ( 'Frame' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1609:1: ( 'Frame' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1609:1: ( 'Frame' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1610:1: 'Frame'
            {
             before(grammarAccess.getFrameAccess().getFrameKeyword_0()); 
            match(input,16,FOLLOW_16_in_rule__Frame__Group__0__Impl3250); 
             after(grammarAccess.getFrameAccess().getFrameKeyword_0()); 

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
    // $ANTLR end rule__Frame__Group__0__Impl


    // $ANTLR start rule__Frame__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1623:1: rule__Frame__Group__1 : rule__Frame__Group__1__Impl rule__Frame__Group__2 ;
    public final void rule__Frame__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1627:1: ( rule__Frame__Group__1__Impl rule__Frame__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1628:2: rule__Frame__Group__1__Impl rule__Frame__Group__2
            {
            pushFollow(FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__13281);
            rule__Frame__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13284);
            rule__Frame__Group__2();
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
    // $ANTLR end rule__Frame__Group__1


    // $ANTLR start rule__Frame__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1635:1: rule__Frame__Group__1__Impl : ( () ) ;
    public final void rule__Frame__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1639:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1640:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1640:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1641:1: ()
            {
             before(grammarAccess.getFrameAccess().getFrameAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1642:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1644:1: 
            {
            }

             after(grammarAccess.getFrameAccess().getFrameAction_1()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Frame__Group__1__Impl


    // $ANTLR start rule__Frame__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1654:1: rule__Frame__Group__2 : rule__Frame__Group__2__Impl ;
    public final void rule__Frame__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1658:1: ( rule__Frame__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1659:2: rule__Frame__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23342);
            rule__Frame__Group__2__Impl();
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
    // $ANTLR end rule__Frame__Group__2


    // $ANTLR start rule__Frame__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1665:1: rule__Frame__Group__2__Impl : ( ( rule__Frame__Group_2__0 )? ) ;
    public final void rule__Frame__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1669:1: ( ( ( rule__Frame__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1670:1: ( ( rule__Frame__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1670:1: ( ( rule__Frame__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1671:1: ( rule__Frame__Group_2__0 )?
            {
             before(grammarAccess.getFrameAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1672:1: ( rule__Frame__Group_2__0 )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==17) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1672:2: rule__Frame__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3369);
                    rule__Frame__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getFrameAccess().getGroup_2()); 

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
    // $ANTLR end rule__Frame__Group__2__Impl


    // $ANTLR start rule__Frame__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1688:1: rule__Frame__Group_2__0 : rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 ;
    public final void rule__Frame__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1692:1: ( rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1693:2: rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03406);
            rule__Frame__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03409);
            rule__Frame__Group_2__1();
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
    // $ANTLR end rule__Frame__Group_2__0


    // $ANTLR start rule__Frame__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1700:1: rule__Frame__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Frame__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1704:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1705:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1705:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1706:1: 'item='
            {
             before(grammarAccess.getFrameAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Frame__Group_2__0__Impl3437); 
             after(grammarAccess.getFrameAccess().getItemKeyword_2_0()); 

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
    // $ANTLR end rule__Frame__Group_2__0__Impl


    // $ANTLR start rule__Frame__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1719:1: rule__Frame__Group_2__1 : rule__Frame__Group_2__1__Impl ;
    public final void rule__Frame__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1723:1: ( rule__Frame__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1724:2: rule__Frame__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13468);
            rule__Frame__Group_2__1__Impl();
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
    // $ANTLR end rule__Frame__Group_2__1


    // $ANTLR start rule__Frame__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1730:1: rule__Frame__Group_2__1__Impl : ( ( rule__Frame__ItemAssignment_2_1 ) ) ;
    public final void rule__Frame__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1734:1: ( ( ( rule__Frame__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1735:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1735:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1736:1: ( rule__Frame__ItemAssignment_2_1 )
            {
             before(grammarAccess.getFrameAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1737:1: ( rule__Frame__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1737:2: rule__Frame__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3495);
            rule__Frame__ItemAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getFrameAccess().getItemAssignment_2_1()); 

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
    // $ANTLR end rule__Frame__Group_2__1__Impl


    // $ANTLR start rule__Text__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1751:1: rule__Text__Group__0 : rule__Text__Group__0__Impl rule__Text__Group__1 ;
    public final void rule__Text__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1755:1: ( rule__Text__Group__0__Impl rule__Text__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1756:2: rule__Text__Group__0__Impl rule__Text__Group__1
            {
            pushFollow(FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03529);
            rule__Text__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03532);
            rule__Text__Group__1();
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
    // $ANTLR end rule__Text__Group__0


    // $ANTLR start rule__Text__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1763:1: rule__Text__Group__0__Impl : ( 'Text' ) ;
    public final void rule__Text__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1767:1: ( ( 'Text' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1768:1: ( 'Text' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1768:1: ( 'Text' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1769:1: 'Text'
            {
             before(grammarAccess.getTextAccess().getTextKeyword_0()); 
            match(input,18,FOLLOW_18_in_rule__Text__Group__0__Impl3560); 
             after(grammarAccess.getTextAccess().getTextKeyword_0()); 

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
    // $ANTLR end rule__Text__Group__0__Impl


    // $ANTLR start rule__Text__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1782:1: rule__Text__Group__1 : rule__Text__Group__1__Impl rule__Text__Group__2 ;
    public final void rule__Text__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1786:1: ( rule__Text__Group__1__Impl rule__Text__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1787:2: rule__Text__Group__1__Impl rule__Text__Group__2
            {
            pushFollow(FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13591);
            rule__Text__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13594);
            rule__Text__Group__2();
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
    // $ANTLR end rule__Text__Group__1


    // $ANTLR start rule__Text__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1794:1: rule__Text__Group__1__Impl : ( () ) ;
    public final void rule__Text__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1798:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1799:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1799:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1800:1: ()
            {
             before(grammarAccess.getTextAccess().getTextAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1801:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1803:1: 
            {
            }

             after(grammarAccess.getTextAccess().getTextAction_1()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Text__Group__1__Impl


    // $ANTLR start rule__Text__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1813:1: rule__Text__Group__2 : rule__Text__Group__2__Impl ;
    public final void rule__Text__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1817:1: ( rule__Text__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1818:2: rule__Text__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23652);
            rule__Text__Group__2__Impl();
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
    // $ANTLR end rule__Text__Group__2


    // $ANTLR start rule__Text__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1824:1: rule__Text__Group__2__Impl : ( ( rule__Text__Group_2__0 )? ) ;
    public final void rule__Text__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1828:1: ( ( ( rule__Text__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1829:1: ( ( rule__Text__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1829:1: ( ( rule__Text__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1830:1: ( rule__Text__Group_2__0 )?
            {
             before(grammarAccess.getTextAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1831:1: ( rule__Text__Group_2__0 )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==17) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1831:2: rule__Text__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3679);
                    rule__Text__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTextAccess().getGroup_2()); 

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
    // $ANTLR end rule__Text__Group__2__Impl


    // $ANTLR start rule__Text__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1847:1: rule__Text__Group_2__0 : rule__Text__Group_2__0__Impl rule__Text__Group_2__1 ;
    public final void rule__Text__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1851:1: ( rule__Text__Group_2__0__Impl rule__Text__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1852:2: rule__Text__Group_2__0__Impl rule__Text__Group_2__1
            {
            pushFollow(FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03716);
            rule__Text__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03719);
            rule__Text__Group_2__1();
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
    // $ANTLR end rule__Text__Group_2__0


    // $ANTLR start rule__Text__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1859:1: rule__Text__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Text__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1863:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1864:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1864:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1865:1: 'item='
            {
             before(grammarAccess.getTextAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Text__Group_2__0__Impl3747); 
             after(grammarAccess.getTextAccess().getItemKeyword_2_0()); 

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
    // $ANTLR end rule__Text__Group_2__0__Impl


    // $ANTLR start rule__Text__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1878:1: rule__Text__Group_2__1 : rule__Text__Group_2__1__Impl ;
    public final void rule__Text__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1882:1: ( rule__Text__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1883:2: rule__Text__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13778);
            rule__Text__Group_2__1__Impl();
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
    // $ANTLR end rule__Text__Group_2__1


    // $ANTLR start rule__Text__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1889:1: rule__Text__Group_2__1__Impl : ( ( rule__Text__ItemAssignment_2_1 ) ) ;
    public final void rule__Text__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1893:1: ( ( ( rule__Text__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1894:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1894:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1895:1: ( rule__Text__ItemAssignment_2_1 )
            {
             before(grammarAccess.getTextAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1896:1: ( rule__Text__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1896:2: rule__Text__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3805);
            rule__Text__ItemAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getTextAccess().getItemAssignment_2_1()); 

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
    // $ANTLR end rule__Text__Group_2__1__Impl


    // $ANTLR start rule__Group__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1910:1: rule__Group__Group__0 : rule__Group__Group__0__Impl rule__Group__Group__1 ;
    public final void rule__Group__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1914:1: ( rule__Group__Group__0__Impl rule__Group__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1915:2: rule__Group__Group__0__Impl rule__Group__Group__1
            {
            pushFollow(FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03839);
            rule__Group__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03842);
            rule__Group__Group__1();
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
    // $ANTLR end rule__Group__Group__0


    // $ANTLR start rule__Group__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1922:1: rule__Group__Group__0__Impl : ( 'Group' ) ;
    public final void rule__Group__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1926:1: ( ( 'Group' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1927:1: ( 'Group' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1927:1: ( 'Group' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1928:1: 'Group'
            {
             before(grammarAccess.getGroupAccess().getGroupKeyword_0()); 
            match(input,19,FOLLOW_19_in_rule__Group__Group__0__Impl3870); 
             after(grammarAccess.getGroupAccess().getGroupKeyword_0()); 

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
    // $ANTLR end rule__Group__Group__0__Impl


    // $ANTLR start rule__Group__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1941:1: rule__Group__Group__1 : rule__Group__Group__1__Impl ;
    public final void rule__Group__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1945:1: ( rule__Group__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1946:2: rule__Group__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13901);
            rule__Group__Group__1__Impl();
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
    // $ANTLR end rule__Group__Group__1


    // $ANTLR start rule__Group__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1952:1: rule__Group__Group__1__Impl : ( ( rule__Group__Group_1__0 ) ) ;
    public final void rule__Group__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1956:1: ( ( ( rule__Group__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1957:1: ( ( rule__Group__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1957:1: ( ( rule__Group__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1958:1: ( rule__Group__Group_1__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1959:1: ( rule__Group__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1959:2: rule__Group__Group_1__0
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3928);
            rule__Group__Group_1__0();
            _fsp--;


            }

             after(grammarAccess.getGroupAccess().getGroup_1()); 

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
    // $ANTLR end rule__Group__Group__1__Impl


    // $ANTLR start rule__Group__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1973:1: rule__Group__Group_1__0 : rule__Group__Group_1__0__Impl rule__Group__Group_1__1 ;
    public final void rule__Group__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1977:1: ( rule__Group__Group_1__0__Impl rule__Group__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1978:2: rule__Group__Group_1__0__Impl rule__Group__Group_1__1
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03962);
            rule__Group__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03965);
            rule__Group__Group_1__1();
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
    // $ANTLR end rule__Group__Group_1__0


    // $ANTLR start rule__Group__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1985:1: rule__Group__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Group__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1989:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1990:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1990:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1991:1: 'item='
            {
             before(grammarAccess.getGroupAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Group__Group_1__0__Impl3993); 
             after(grammarAccess.getGroupAccess().getItemKeyword_1_0()); 

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
    // $ANTLR end rule__Group__Group_1__0__Impl


    // $ANTLR start rule__Group__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2004:1: rule__Group__Group_1__1 : rule__Group__Group_1__1__Impl ;
    public final void rule__Group__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2008:1: ( rule__Group__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2009:2: rule__Group__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__14024);
            rule__Group__Group_1__1__Impl();
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
    // $ANTLR end rule__Group__Group_1__1


    // $ANTLR start rule__Group__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2015:1: rule__Group__Group_1__1__Impl : ( ( rule__Group__ItemAssignment_1_1 ) ) ;
    public final void rule__Group__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2019:1: ( ( ( rule__Group__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2020:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2020:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2021:1: ( rule__Group__ItemAssignment_1_1 )
            {
             before(grammarAccess.getGroupAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2022:1: ( rule__Group__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2022:2: rule__Group__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl4051);
            rule__Group__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getGroupAccess().getItemAssignment_1_1()); 

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
    // $ANTLR end rule__Group__Group_1__1__Impl


    // $ANTLR start rule__Image__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2036:1: rule__Image__Group__0 : rule__Image__Group__0__Impl rule__Image__Group__1 ;
    public final void rule__Image__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2040:1: ( rule__Image__Group__0__Impl rule__Image__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2041:2: rule__Image__Group__0__Impl rule__Image__Group__1
            {
            pushFollow(FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__04085);
            rule__Image__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__1_in_rule__Image__Group__04088);
            rule__Image__Group__1();
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
    // $ANTLR end rule__Image__Group__0


    // $ANTLR start rule__Image__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2048:1: rule__Image__Group__0__Impl : ( 'Image' ) ;
    public final void rule__Image__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2052:1: ( ( 'Image' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2053:1: ( 'Image' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2053:1: ( 'Image' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2054:1: 'Image'
            {
             before(grammarAccess.getImageAccess().getImageKeyword_0()); 
            match(input,20,FOLLOW_20_in_rule__Image__Group__0__Impl4116); 
             after(grammarAccess.getImageAccess().getImageKeyword_0()); 

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
    // $ANTLR end rule__Image__Group__0__Impl


    // $ANTLR start rule__Image__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2067:1: rule__Image__Group__1 : rule__Image__Group__1__Impl rule__Image__Group__2 ;
    public final void rule__Image__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2071:1: ( rule__Image__Group__1__Impl rule__Image__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2072:2: rule__Image__Group__1__Impl rule__Image__Group__2
            {
            pushFollow(FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__14147);
            rule__Image__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__2_in_rule__Image__Group__14150);
            rule__Image__Group__2();
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
    // $ANTLR end rule__Image__Group__1


    // $ANTLR start rule__Image__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2079:1: rule__Image__Group__1__Impl : ( ( rule__Image__Group_1__0 )? ) ;
    public final void rule__Image__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2083:1: ( ( ( rule__Image__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2084:1: ( ( rule__Image__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2084:1: ( ( rule__Image__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2085:1: ( rule__Image__Group_1__0 )?
            {
             before(grammarAccess.getImageAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2086:1: ( rule__Image__Group_1__0 )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==17) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2086:2: rule__Image__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl4177);
                    rule__Image__Group_1__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getImageAccess().getGroup_1()); 

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
    // $ANTLR end rule__Image__Group__1__Impl


    // $ANTLR start rule__Image__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2096:1: rule__Image__Group__2 : rule__Image__Group__2__Impl ;
    public final void rule__Image__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2100:1: ( rule__Image__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2101:2: rule__Image__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__24208);
            rule__Image__Group__2__Impl();
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
    // $ANTLR end rule__Image__Group__2


    // $ANTLR start rule__Image__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2107:1: rule__Image__Group__2__Impl : ( ( rule__Image__Group_2__0 ) ) ;
    public final void rule__Image__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2111:1: ( ( ( rule__Image__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2112:1: ( ( rule__Image__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2112:1: ( ( rule__Image__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2113:1: ( rule__Image__Group_2__0 )
            {
             before(grammarAccess.getImageAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2114:1: ( rule__Image__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2114:2: rule__Image__Group_2__0
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl4235);
            rule__Image__Group_2__0();
            _fsp--;


            }

             after(grammarAccess.getImageAccess().getGroup_2()); 

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
    // $ANTLR end rule__Image__Group__2__Impl


    // $ANTLR start rule__Image__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2130:1: rule__Image__Group_1__0 : rule__Image__Group_1__0__Impl rule__Image__Group_1__1 ;
    public final void rule__Image__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2134:1: ( rule__Image__Group_1__0__Impl rule__Image__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2135:2: rule__Image__Group_1__0__Impl rule__Image__Group_1__1
            {
            pushFollow(FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__04271);
            rule__Image__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__04274);
            rule__Image__Group_1__1();
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
    // $ANTLR end rule__Image__Group_1__0


    // $ANTLR start rule__Image__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2142:1: rule__Image__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Image__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2146:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2147:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2147:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2148:1: 'item='
            {
             before(grammarAccess.getImageAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Image__Group_1__0__Impl4302); 
             after(grammarAccess.getImageAccess().getItemKeyword_1_0()); 

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
    // $ANTLR end rule__Image__Group_1__0__Impl


    // $ANTLR start rule__Image__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2161:1: rule__Image__Group_1__1 : rule__Image__Group_1__1__Impl ;
    public final void rule__Image__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2165:1: ( rule__Image__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2166:2: rule__Image__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14333);
            rule__Image__Group_1__1__Impl();
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
    // $ANTLR end rule__Image__Group_1__1


    // $ANTLR start rule__Image__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2172:1: rule__Image__Group_1__1__Impl : ( ( rule__Image__ItemAssignment_1_1 ) ) ;
    public final void rule__Image__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2176:1: ( ( ( rule__Image__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2177:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2177:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2178:1: ( rule__Image__ItemAssignment_1_1 )
            {
             before(grammarAccess.getImageAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2179:1: ( rule__Image__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2179:2: rule__Image__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4360);
            rule__Image__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getImageAccess().getItemAssignment_1_1()); 

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
    // $ANTLR end rule__Image__Group_1__1__Impl


    // $ANTLR start rule__Image__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2193:1: rule__Image__Group_2__0 : rule__Image__Group_2__0__Impl rule__Image__Group_2__1 ;
    public final void rule__Image__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2197:1: ( rule__Image__Group_2__0__Impl rule__Image__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2198:2: rule__Image__Group_2__0__Impl rule__Image__Group_2__1
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04394);
            rule__Image__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04397);
            rule__Image__Group_2__1();
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
    // $ANTLR end rule__Image__Group_2__0


    // $ANTLR start rule__Image__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2205:1: rule__Image__Group_2__0__Impl : ( 'url=' ) ;
    public final void rule__Image__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2209:1: ( ( 'url=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2210:1: ( 'url=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2210:1: ( 'url=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2211:1: 'url='
            {
             before(grammarAccess.getImageAccess().getUrlKeyword_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Image__Group_2__0__Impl4425); 
             after(grammarAccess.getImageAccess().getUrlKeyword_2_0()); 

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
    // $ANTLR end rule__Image__Group_2__0__Impl


    // $ANTLR start rule__Image__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2224:1: rule__Image__Group_2__1 : rule__Image__Group_2__1__Impl ;
    public final void rule__Image__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2228:1: ( rule__Image__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2229:2: rule__Image__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14456);
            rule__Image__Group_2__1__Impl();
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
    // $ANTLR end rule__Image__Group_2__1


    // $ANTLR start rule__Image__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2235:1: rule__Image__Group_2__1__Impl : ( ( rule__Image__UrlAssignment_2_1 ) ) ;
    public final void rule__Image__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2239:1: ( ( ( rule__Image__UrlAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2240:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2240:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2241:1: ( rule__Image__UrlAssignment_2_1 )
            {
             before(grammarAccess.getImageAccess().getUrlAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2242:1: ( rule__Image__UrlAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2242:2: rule__Image__UrlAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4483);
            rule__Image__UrlAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getImageAccess().getUrlAssignment_2_1()); 

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
    // $ANTLR end rule__Image__Group_2__1__Impl


    // $ANTLR start rule__Switch__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2256:1: rule__Switch__Group__0 : rule__Switch__Group__0__Impl rule__Switch__Group__1 ;
    public final void rule__Switch__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2260:1: ( rule__Switch__Group__0__Impl rule__Switch__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2261:2: rule__Switch__Group__0__Impl rule__Switch__Group__1
            {
            pushFollow(FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04517);
            rule__Switch__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04520);
            rule__Switch__Group__1();
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
    // $ANTLR end rule__Switch__Group__0


    // $ANTLR start rule__Switch__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2268:1: rule__Switch__Group__0__Impl : ( 'Switch' ) ;
    public final void rule__Switch__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2272:1: ( ( 'Switch' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2273:1: ( 'Switch' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2273:1: ( 'Switch' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2274:1: 'Switch'
            {
             before(grammarAccess.getSwitchAccess().getSwitchKeyword_0()); 
            match(input,22,FOLLOW_22_in_rule__Switch__Group__0__Impl4548); 
             after(grammarAccess.getSwitchAccess().getSwitchKeyword_0()); 

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
    // $ANTLR end rule__Switch__Group__0__Impl


    // $ANTLR start rule__Switch__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2287:1: rule__Switch__Group__1 : rule__Switch__Group__1__Impl rule__Switch__Group__2 ;
    public final void rule__Switch__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2291:1: ( rule__Switch__Group__1__Impl rule__Switch__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2292:2: rule__Switch__Group__1__Impl rule__Switch__Group__2
            {
            pushFollow(FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14579);
            rule__Switch__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14582);
            rule__Switch__Group__2();
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
    // $ANTLR end rule__Switch__Group__1


    // $ANTLR start rule__Switch__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2299:1: rule__Switch__Group__1__Impl : ( ( rule__Switch__Group_1__0 ) ) ;
    public final void rule__Switch__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2303:1: ( ( ( rule__Switch__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2304:1: ( ( rule__Switch__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2304:1: ( ( rule__Switch__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2305:1: ( rule__Switch__Group_1__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2306:1: ( rule__Switch__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2306:2: rule__Switch__Group_1__0
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4609);
            rule__Switch__Group_1__0();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getGroup_1()); 

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
    // $ANTLR end rule__Switch__Group__1__Impl


    // $ANTLR start rule__Switch__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2316:1: rule__Switch__Group__2 : rule__Switch__Group__2__Impl ;
    public final void rule__Switch__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2320:1: ( rule__Switch__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2321:2: rule__Switch__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24639);
            rule__Switch__Group__2__Impl();
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
    // $ANTLR end rule__Switch__Group__2


    // $ANTLR start rule__Switch__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2327:1: rule__Switch__Group__2__Impl : ( ( rule__Switch__Group_2__0 )? ) ;
    public final void rule__Switch__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2331:1: ( ( ( rule__Switch__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2332:1: ( ( rule__Switch__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2332:1: ( ( rule__Switch__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2333:1: ( rule__Switch__Group_2__0 )?
            {
             before(grammarAccess.getSwitchAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2334:1: ( rule__Switch__Group_2__0 )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==23) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2334:2: rule__Switch__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4666);
                    rule__Switch__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSwitchAccess().getGroup_2()); 

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
    // $ANTLR end rule__Switch__Group__2__Impl


    // $ANTLR start rule__Switch__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2350:1: rule__Switch__Group_1__0 : rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 ;
    public final void rule__Switch__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2354:1: ( rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2355:2: rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04703);
            rule__Switch__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04706);
            rule__Switch__Group_1__1();
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
    // $ANTLR end rule__Switch__Group_1__0


    // $ANTLR start rule__Switch__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2362:1: rule__Switch__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Switch__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2366:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2367:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2367:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2368:1: 'item='
            {
             before(grammarAccess.getSwitchAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Switch__Group_1__0__Impl4734); 
             after(grammarAccess.getSwitchAccess().getItemKeyword_1_0()); 

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
    // $ANTLR end rule__Switch__Group_1__0__Impl


    // $ANTLR start rule__Switch__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2381:1: rule__Switch__Group_1__1 : rule__Switch__Group_1__1__Impl ;
    public final void rule__Switch__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2385:1: ( rule__Switch__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2386:2: rule__Switch__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14765);
            rule__Switch__Group_1__1__Impl();
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
    // $ANTLR end rule__Switch__Group_1__1


    // $ANTLR start rule__Switch__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2392:1: rule__Switch__Group_1__1__Impl : ( ( rule__Switch__ItemAssignment_1_1 ) ) ;
    public final void rule__Switch__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2396:1: ( ( ( rule__Switch__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2397:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2397:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2398:1: ( rule__Switch__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSwitchAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2399:1: ( rule__Switch__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2399:2: rule__Switch__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4792);
            rule__Switch__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getItemAssignment_1_1()); 

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
    // $ANTLR end rule__Switch__Group_1__1__Impl


    // $ANTLR start rule__Switch__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2413:1: rule__Switch__Group_2__0 : rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 ;
    public final void rule__Switch__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2417:1: ( rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2418:2: rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04826);
            rule__Switch__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04829);
            rule__Switch__Group_2__1();
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
    // $ANTLR end rule__Switch__Group_2__0


    // $ANTLR start rule__Switch__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2425:1: rule__Switch__Group_2__0__Impl : ( 'mappings=[' ) ;
    public final void rule__Switch__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2429:1: ( ( 'mappings=[' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2430:1: ( 'mappings=[' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2430:1: ( 'mappings=[' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2431:1: 'mappings=['
            {
             before(grammarAccess.getSwitchAccess().getMappingsKeyword_2_0()); 
            match(input,23,FOLLOW_23_in_rule__Switch__Group_2__0__Impl4857); 
             after(grammarAccess.getSwitchAccess().getMappingsKeyword_2_0()); 

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
    // $ANTLR end rule__Switch__Group_2__0__Impl


    // $ANTLR start rule__Switch__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2444:1: rule__Switch__Group_2__1 : rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 ;
    public final void rule__Switch__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2448:1: ( rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2449:2: rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14888);
            rule__Switch__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14891);
            rule__Switch__Group_2__2();
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
    // $ANTLR end rule__Switch__Group_2__1


    // $ANTLR start rule__Switch__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2456:1: rule__Switch__Group_2__1__Impl : ( ( rule__Switch__MappingsAssignment_2_1 ) ) ;
    public final void rule__Switch__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2460:1: ( ( ( rule__Switch__MappingsAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2461:1: ( ( rule__Switch__MappingsAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2461:1: ( ( rule__Switch__MappingsAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2462:1: ( rule__Switch__MappingsAssignment_2_1 )
            {
             before(grammarAccess.getSwitchAccess().getMappingsAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2463:1: ( rule__Switch__MappingsAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2463:2: rule__Switch__MappingsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Switch__MappingsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4918);
            rule__Switch__MappingsAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getMappingsAssignment_2_1()); 

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
    // $ANTLR end rule__Switch__Group_2__1__Impl


    // $ANTLR start rule__Switch__Group_2__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2473:1: rule__Switch__Group_2__2 : rule__Switch__Group_2__2__Impl rule__Switch__Group_2__3 ;
    public final void rule__Switch__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2477:1: ( rule__Switch__Group_2__2__Impl rule__Switch__Group_2__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2478:2: rule__Switch__Group_2__2__Impl rule__Switch__Group_2__3
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24948);
            rule__Switch__Group_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__3_in_rule__Switch__Group_2__24951);
            rule__Switch__Group_2__3();
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
    // $ANTLR end rule__Switch__Group_2__2


    // $ANTLR start rule__Switch__Group_2__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2485:1: rule__Switch__Group_2__2__Impl : ( ( rule__Switch__Group_2_2__0 )* ) ;
    public final void rule__Switch__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2489:1: ( ( ( rule__Switch__Group_2_2__0 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2490:1: ( ( rule__Switch__Group_2_2__0 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2490:1: ( ( rule__Switch__Group_2_2__0 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2491:1: ( rule__Switch__Group_2_2__0 )*
            {
             before(grammarAccess.getSwitchAccess().getGroup_2_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2492:1: ( rule__Switch__Group_2_2__0 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==25) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2492:2: rule__Switch__Group_2_2__0
            	    {
            	    pushFollow(FOLLOW_rule__Switch__Group_2_2__0_in_rule__Switch__Group_2__2__Impl4978);
            	    rule__Switch__Group_2_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

             after(grammarAccess.getSwitchAccess().getGroup_2_2()); 

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
    // $ANTLR end rule__Switch__Group_2__2__Impl


    // $ANTLR start rule__Switch__Group_2__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2502:1: rule__Switch__Group_2__3 : rule__Switch__Group_2__3__Impl ;
    public final void rule__Switch__Group_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2506:1: ( rule__Switch__Group_2__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2507:2: rule__Switch__Group_2__3__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__3__Impl_in_rule__Switch__Group_2__35009);
            rule__Switch__Group_2__3__Impl();
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
    // $ANTLR end rule__Switch__Group_2__3


    // $ANTLR start rule__Switch__Group_2__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2513:1: rule__Switch__Group_2__3__Impl : ( ']' ) ;
    public final void rule__Switch__Group_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2517:1: ( ( ']' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2518:1: ( ']' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2518:1: ( ']' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2519:1: ']'
            {
             before(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_3()); 
            match(input,24,FOLLOW_24_in_rule__Switch__Group_2__3__Impl5037); 
             after(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_3()); 

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
    // $ANTLR end rule__Switch__Group_2__3__Impl


    // $ANTLR start rule__Switch__Group_2_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2540:1: rule__Switch__Group_2_2__0 : rule__Switch__Group_2_2__0__Impl rule__Switch__Group_2_2__1 ;
    public final void rule__Switch__Group_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2544:1: ( rule__Switch__Group_2_2__0__Impl rule__Switch__Group_2_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2545:2: rule__Switch__Group_2_2__0__Impl rule__Switch__Group_2_2__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_2_2__0__Impl_in_rule__Switch__Group_2_2__05076);
            rule__Switch__Group_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2_2__1_in_rule__Switch__Group_2_2__05079);
            rule__Switch__Group_2_2__1();
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
    // $ANTLR end rule__Switch__Group_2_2__0


    // $ANTLR start rule__Switch__Group_2_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2552:1: rule__Switch__Group_2_2__0__Impl : ( ', ' ) ;
    public final void rule__Switch__Group_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2556:1: ( ( ', ' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2557:1: ( ', ' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2557:1: ( ', ' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2558:1: ', '
            {
             before(grammarAccess.getSwitchAccess().getCommaSpaceKeyword_2_2_0()); 
            match(input,25,FOLLOW_25_in_rule__Switch__Group_2_2__0__Impl5107); 
             after(grammarAccess.getSwitchAccess().getCommaSpaceKeyword_2_2_0()); 

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
    // $ANTLR end rule__Switch__Group_2_2__0__Impl


    // $ANTLR start rule__Switch__Group_2_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2571:1: rule__Switch__Group_2_2__1 : rule__Switch__Group_2_2__1__Impl ;
    public final void rule__Switch__Group_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2575:1: ( rule__Switch__Group_2_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2576:2: rule__Switch__Group_2_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_2_2__1__Impl_in_rule__Switch__Group_2_2__15138);
            rule__Switch__Group_2_2__1__Impl();
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
    // $ANTLR end rule__Switch__Group_2_2__1


    // $ANTLR start rule__Switch__Group_2_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2582:1: rule__Switch__Group_2_2__1__Impl : ( ( rule__Switch__MappingsAssignment_2_2_1 ) ) ;
    public final void rule__Switch__Group_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2586:1: ( ( ( rule__Switch__MappingsAssignment_2_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2587:1: ( ( rule__Switch__MappingsAssignment_2_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2587:1: ( ( rule__Switch__MappingsAssignment_2_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2588:1: ( rule__Switch__MappingsAssignment_2_2_1 )
            {
             before(grammarAccess.getSwitchAccess().getMappingsAssignment_2_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:1: ( rule__Switch__MappingsAssignment_2_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:2: rule__Switch__MappingsAssignment_2_2_1
            {
            pushFollow(FOLLOW_rule__Switch__MappingsAssignment_2_2_1_in_rule__Switch__Group_2_2__1__Impl5165);
            rule__Switch__MappingsAssignment_2_2_1();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getMappingsAssignment_2_2_1()); 

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
    // $ANTLR end rule__Switch__Group_2_2__1__Impl


    // $ANTLR start rule__Selection__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2603:1: rule__Selection__Group__0 : rule__Selection__Group__0__Impl rule__Selection__Group__1 ;
    public final void rule__Selection__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2607:1: ( rule__Selection__Group__0__Impl rule__Selection__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2608:2: rule__Selection__Group__0__Impl rule__Selection__Group__1
            {
            pushFollow(FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__05199);
            rule__Selection__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__05202);
            rule__Selection__Group__1();
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
    // $ANTLR end rule__Selection__Group__0


    // $ANTLR start rule__Selection__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2615:1: rule__Selection__Group__0__Impl : ( 'Selection' ) ;
    public final void rule__Selection__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2619:1: ( ( 'Selection' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2620:1: ( 'Selection' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2620:1: ( 'Selection' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2621:1: 'Selection'
            {
             before(grammarAccess.getSelectionAccess().getSelectionKeyword_0()); 
            match(input,26,FOLLOW_26_in_rule__Selection__Group__0__Impl5230); 
             after(grammarAccess.getSelectionAccess().getSelectionKeyword_0()); 

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
    // $ANTLR end rule__Selection__Group__0__Impl


    // $ANTLR start rule__Selection__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2634:1: rule__Selection__Group__1 : rule__Selection__Group__1__Impl rule__Selection__Group__2 ;
    public final void rule__Selection__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2638:1: ( rule__Selection__Group__1__Impl rule__Selection__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2639:2: rule__Selection__Group__1__Impl rule__Selection__Group__2
            {
            pushFollow(FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__15261);
            rule__Selection__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group__2_in_rule__Selection__Group__15264);
            rule__Selection__Group__2();
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
    // $ANTLR end rule__Selection__Group__1


    // $ANTLR start rule__Selection__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2646:1: rule__Selection__Group__1__Impl : ( ( rule__Selection__Group_1__0 ) ) ;
    public final void rule__Selection__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2650:1: ( ( ( rule__Selection__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2651:1: ( ( rule__Selection__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2651:1: ( ( rule__Selection__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2652:1: ( rule__Selection__Group_1__0 )
            {
             before(grammarAccess.getSelectionAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2653:1: ( rule__Selection__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2653:2: rule__Selection__Group_1__0
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl5291);
            rule__Selection__Group_1__0();
            _fsp--;


            }

             after(grammarAccess.getSelectionAccess().getGroup_1()); 

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
    // $ANTLR end rule__Selection__Group__1__Impl


    // $ANTLR start rule__Selection__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2663:1: rule__Selection__Group__2 : rule__Selection__Group__2__Impl ;
    public final void rule__Selection__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2667:1: ( rule__Selection__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2668:2: rule__Selection__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group__2__Impl_in_rule__Selection__Group__25321);
            rule__Selection__Group__2__Impl();
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
    // $ANTLR end rule__Selection__Group__2


    // $ANTLR start rule__Selection__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2674:1: rule__Selection__Group__2__Impl : ( ( rule__Selection__Group_2__0 )? ) ;
    public final void rule__Selection__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2678:1: ( ( ( rule__Selection__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2679:1: ( ( rule__Selection__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2679:1: ( ( rule__Selection__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2680:1: ( rule__Selection__Group_2__0 )?
            {
             before(grammarAccess.getSelectionAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2681:1: ( rule__Selection__Group_2__0 )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==23) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2681:2: rule__Selection__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Selection__Group_2__0_in_rule__Selection__Group__2__Impl5348);
                    rule__Selection__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectionAccess().getGroup_2()); 

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
    // $ANTLR end rule__Selection__Group__2__Impl


    // $ANTLR start rule__Selection__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2697:1: rule__Selection__Group_1__0 : rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 ;
    public final void rule__Selection__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2701:1: ( rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2702:2: rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__05385);
            rule__Selection__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__05388);
            rule__Selection__Group_1__1();
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
    // $ANTLR end rule__Selection__Group_1__0


    // $ANTLR start rule__Selection__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2709:1: rule__Selection__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Selection__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2713:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2714:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2714:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2715:1: 'item='
            {
             before(grammarAccess.getSelectionAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Selection__Group_1__0__Impl5416); 
             after(grammarAccess.getSelectionAccess().getItemKeyword_1_0()); 

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
    // $ANTLR end rule__Selection__Group_1__0__Impl


    // $ANTLR start rule__Selection__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2728:1: rule__Selection__Group_1__1 : rule__Selection__Group_1__1__Impl ;
    public final void rule__Selection__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2732:1: ( rule__Selection__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2733:2: rule__Selection__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__15447);
            rule__Selection__Group_1__1__Impl();
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
    // $ANTLR end rule__Selection__Group_1__1


    // $ANTLR start rule__Selection__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2739:1: rule__Selection__Group_1__1__Impl : ( ( rule__Selection__ItemAssignment_1_1 ) ) ;
    public final void rule__Selection__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2743:1: ( ( ( rule__Selection__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2744:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2744:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2745:1: ( rule__Selection__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSelectionAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2746:1: ( rule__Selection__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2746:2: rule__Selection__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl5474);
            rule__Selection__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getSelectionAccess().getItemAssignment_1_1()); 

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
    // $ANTLR end rule__Selection__Group_1__1__Impl


    // $ANTLR start rule__Selection__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2760:1: rule__Selection__Group_2__0 : rule__Selection__Group_2__0__Impl rule__Selection__Group_2__1 ;
    public final void rule__Selection__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2764:1: ( rule__Selection__Group_2__0__Impl rule__Selection__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2765:2: rule__Selection__Group_2__0__Impl rule__Selection__Group_2__1
            {
            pushFollow(FOLLOW_rule__Selection__Group_2__0__Impl_in_rule__Selection__Group_2__05508);
            rule__Selection__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_2__1_in_rule__Selection__Group_2__05511);
            rule__Selection__Group_2__1();
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
    // $ANTLR end rule__Selection__Group_2__0


    // $ANTLR start rule__Selection__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2772:1: rule__Selection__Group_2__0__Impl : ( 'mappings=[' ) ;
    public final void rule__Selection__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2776:1: ( ( 'mappings=[' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2777:1: ( 'mappings=[' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2777:1: ( 'mappings=[' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2778:1: 'mappings=['
            {
             before(grammarAccess.getSelectionAccess().getMappingsKeyword_2_0()); 
            match(input,23,FOLLOW_23_in_rule__Selection__Group_2__0__Impl5539); 
             after(grammarAccess.getSelectionAccess().getMappingsKeyword_2_0()); 

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
    // $ANTLR end rule__Selection__Group_2__0__Impl


    // $ANTLR start rule__Selection__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2791:1: rule__Selection__Group_2__1 : rule__Selection__Group_2__1__Impl rule__Selection__Group_2__2 ;
    public final void rule__Selection__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2795:1: ( rule__Selection__Group_2__1__Impl rule__Selection__Group_2__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2796:2: rule__Selection__Group_2__1__Impl rule__Selection__Group_2__2
            {
            pushFollow(FOLLOW_rule__Selection__Group_2__1__Impl_in_rule__Selection__Group_2__15570);
            rule__Selection__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_2__2_in_rule__Selection__Group_2__15573);
            rule__Selection__Group_2__2();
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
    // $ANTLR end rule__Selection__Group_2__1


    // $ANTLR start rule__Selection__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2803:1: rule__Selection__Group_2__1__Impl : ( ( rule__Selection__MappingsAssignment_2_1 ) ) ;
    public final void rule__Selection__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2807:1: ( ( ( rule__Selection__MappingsAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2808:1: ( ( rule__Selection__MappingsAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2808:1: ( ( rule__Selection__MappingsAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2809:1: ( rule__Selection__MappingsAssignment_2_1 )
            {
             before(grammarAccess.getSelectionAccess().getMappingsAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2810:1: ( rule__Selection__MappingsAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2810:2: rule__Selection__MappingsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Selection__MappingsAssignment_2_1_in_rule__Selection__Group_2__1__Impl5600);
            rule__Selection__MappingsAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getSelectionAccess().getMappingsAssignment_2_1()); 

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
    // $ANTLR end rule__Selection__Group_2__1__Impl


    // $ANTLR start rule__Selection__Group_2__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2820:1: rule__Selection__Group_2__2 : rule__Selection__Group_2__2__Impl rule__Selection__Group_2__3 ;
    public final void rule__Selection__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2824:1: ( rule__Selection__Group_2__2__Impl rule__Selection__Group_2__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2825:2: rule__Selection__Group_2__2__Impl rule__Selection__Group_2__3
            {
            pushFollow(FOLLOW_rule__Selection__Group_2__2__Impl_in_rule__Selection__Group_2__25630);
            rule__Selection__Group_2__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_2__3_in_rule__Selection__Group_2__25633);
            rule__Selection__Group_2__3();
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
    // $ANTLR end rule__Selection__Group_2__2


    // $ANTLR start rule__Selection__Group_2__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2832:1: rule__Selection__Group_2__2__Impl : ( ( rule__Selection__Group_2_2__0 )* ) ;
    public final void rule__Selection__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2836:1: ( ( ( rule__Selection__Group_2_2__0 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2837:1: ( ( rule__Selection__Group_2_2__0 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2837:1: ( ( rule__Selection__Group_2_2__0 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2838:1: ( rule__Selection__Group_2_2__0 )*
            {
             before(grammarAccess.getSelectionAccess().getGroup_2_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2839:1: ( rule__Selection__Group_2_2__0 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==25) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2839:2: rule__Selection__Group_2_2__0
            	    {
            	    pushFollow(FOLLOW_rule__Selection__Group_2_2__0_in_rule__Selection__Group_2__2__Impl5660);
            	    rule__Selection__Group_2_2__0();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

             after(grammarAccess.getSelectionAccess().getGroup_2_2()); 

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
    // $ANTLR end rule__Selection__Group_2__2__Impl


    // $ANTLR start rule__Selection__Group_2__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2849:1: rule__Selection__Group_2__3 : rule__Selection__Group_2__3__Impl ;
    public final void rule__Selection__Group_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2853:1: ( rule__Selection__Group_2__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2854:2: rule__Selection__Group_2__3__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group_2__3__Impl_in_rule__Selection__Group_2__35691);
            rule__Selection__Group_2__3__Impl();
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
    // $ANTLR end rule__Selection__Group_2__3


    // $ANTLR start rule__Selection__Group_2__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2860:1: rule__Selection__Group_2__3__Impl : ( ']' ) ;
    public final void rule__Selection__Group_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2864:1: ( ( ']' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2865:1: ( ']' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2865:1: ( ']' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2866:1: ']'
            {
             before(grammarAccess.getSelectionAccess().getRightSquareBracketKeyword_2_3()); 
            match(input,24,FOLLOW_24_in_rule__Selection__Group_2__3__Impl5719); 
             after(grammarAccess.getSelectionAccess().getRightSquareBracketKeyword_2_3()); 

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
    // $ANTLR end rule__Selection__Group_2__3__Impl


    // $ANTLR start rule__Selection__Group_2_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2887:1: rule__Selection__Group_2_2__0 : rule__Selection__Group_2_2__0__Impl rule__Selection__Group_2_2__1 ;
    public final void rule__Selection__Group_2_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2891:1: ( rule__Selection__Group_2_2__0__Impl rule__Selection__Group_2_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2892:2: rule__Selection__Group_2_2__0__Impl rule__Selection__Group_2_2__1
            {
            pushFollow(FOLLOW_rule__Selection__Group_2_2__0__Impl_in_rule__Selection__Group_2_2__05758);
            rule__Selection__Group_2_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_2_2__1_in_rule__Selection__Group_2_2__05761);
            rule__Selection__Group_2_2__1();
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
    // $ANTLR end rule__Selection__Group_2_2__0


    // $ANTLR start rule__Selection__Group_2_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2899:1: rule__Selection__Group_2_2__0__Impl : ( ', ' ) ;
    public final void rule__Selection__Group_2_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2903:1: ( ( ', ' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2904:1: ( ', ' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2904:1: ( ', ' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2905:1: ', '
            {
             before(grammarAccess.getSelectionAccess().getCommaSpaceKeyword_2_2_0()); 
            match(input,25,FOLLOW_25_in_rule__Selection__Group_2_2__0__Impl5789); 
             after(grammarAccess.getSelectionAccess().getCommaSpaceKeyword_2_2_0()); 

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
    // $ANTLR end rule__Selection__Group_2_2__0__Impl


    // $ANTLR start rule__Selection__Group_2_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2918:1: rule__Selection__Group_2_2__1 : rule__Selection__Group_2_2__1__Impl ;
    public final void rule__Selection__Group_2_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2922:1: ( rule__Selection__Group_2_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2923:2: rule__Selection__Group_2_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group_2_2__1__Impl_in_rule__Selection__Group_2_2__15820);
            rule__Selection__Group_2_2__1__Impl();
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
    // $ANTLR end rule__Selection__Group_2_2__1


    // $ANTLR start rule__Selection__Group_2_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2929:1: rule__Selection__Group_2_2__1__Impl : ( ( rule__Selection__MappingsAssignment_2_2_1 ) ) ;
    public final void rule__Selection__Group_2_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2933:1: ( ( ( rule__Selection__MappingsAssignment_2_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2934:1: ( ( rule__Selection__MappingsAssignment_2_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2934:1: ( ( rule__Selection__MappingsAssignment_2_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2935:1: ( rule__Selection__MappingsAssignment_2_2_1 )
            {
             before(grammarAccess.getSelectionAccess().getMappingsAssignment_2_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2936:1: ( rule__Selection__MappingsAssignment_2_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2936:2: rule__Selection__MappingsAssignment_2_2_1
            {
            pushFollow(FOLLOW_rule__Selection__MappingsAssignment_2_2_1_in_rule__Selection__Group_2_2__1__Impl5847);
            rule__Selection__MappingsAssignment_2_2_1();
            _fsp--;


            }

             after(grammarAccess.getSelectionAccess().getMappingsAssignment_2_2_1()); 

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
    // $ANTLR end rule__Selection__Group_2_2__1__Impl


    // $ANTLR start rule__List__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2950:1: rule__List__Group__0 : rule__List__Group__0__Impl rule__List__Group__1 ;
    public final void rule__List__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2954:1: ( rule__List__Group__0__Impl rule__List__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2955:2: rule__List__Group__0__Impl rule__List__Group__1
            {
            pushFollow(FOLLOW_rule__List__Group__0__Impl_in_rule__List__Group__05881);
            rule__List__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group__1_in_rule__List__Group__05884);
            rule__List__Group__1();
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
    // $ANTLR end rule__List__Group__0


    // $ANTLR start rule__List__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2962:1: rule__List__Group__0__Impl : ( 'List' ) ;
    public final void rule__List__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2966:1: ( ( 'List' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2967:1: ( 'List' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2967:1: ( 'List' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2968:1: 'List'
            {
             before(grammarAccess.getListAccess().getListKeyword_0()); 
            match(input,27,FOLLOW_27_in_rule__List__Group__0__Impl5912); 
             after(grammarAccess.getListAccess().getListKeyword_0()); 

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
    // $ANTLR end rule__List__Group__0__Impl


    // $ANTLR start rule__List__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2981:1: rule__List__Group__1 : rule__List__Group__1__Impl rule__List__Group__2 ;
    public final void rule__List__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2985:1: ( rule__List__Group__1__Impl rule__List__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2986:2: rule__List__Group__1__Impl rule__List__Group__2
            {
            pushFollow(FOLLOW_rule__List__Group__1__Impl_in_rule__List__Group__15943);
            rule__List__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group__2_in_rule__List__Group__15946);
            rule__List__Group__2();
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
    // $ANTLR end rule__List__Group__1


    // $ANTLR start rule__List__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2993:1: rule__List__Group__1__Impl : ( ( rule__List__Group_1__0 ) ) ;
    public final void rule__List__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2997:1: ( ( ( rule__List__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2998:1: ( ( rule__List__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2998:1: ( ( rule__List__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2999:1: ( rule__List__Group_1__0 )
            {
             before(grammarAccess.getListAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3000:1: ( rule__List__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3000:2: rule__List__Group_1__0
            {
            pushFollow(FOLLOW_rule__List__Group_1__0_in_rule__List__Group__1__Impl5973);
            rule__List__Group_1__0();
            _fsp--;


            }

             after(grammarAccess.getListAccess().getGroup_1()); 

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
    // $ANTLR end rule__List__Group__1__Impl


    // $ANTLR start rule__List__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3010:1: rule__List__Group__2 : rule__List__Group__2__Impl ;
    public final void rule__List__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3014:1: ( rule__List__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3015:2: rule__List__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__List__Group__2__Impl_in_rule__List__Group__26003);
            rule__List__Group__2__Impl();
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
    // $ANTLR end rule__List__Group__2


    // $ANTLR start rule__List__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3021:1: rule__List__Group__2__Impl : ( ( rule__List__Group_2__0 ) ) ;
    public final void rule__List__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3025:1: ( ( ( rule__List__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3026:1: ( ( rule__List__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3026:1: ( ( rule__List__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3027:1: ( rule__List__Group_2__0 )
            {
             before(grammarAccess.getListAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3028:1: ( rule__List__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3028:2: rule__List__Group_2__0
            {
            pushFollow(FOLLOW_rule__List__Group_2__0_in_rule__List__Group__2__Impl6030);
            rule__List__Group_2__0();
            _fsp--;


            }

             after(grammarAccess.getListAccess().getGroup_2()); 

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
    // $ANTLR end rule__List__Group__2__Impl


    // $ANTLR start rule__List__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3044:1: rule__List__Group_1__0 : rule__List__Group_1__0__Impl rule__List__Group_1__1 ;
    public final void rule__List__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3048:1: ( rule__List__Group_1__0__Impl rule__List__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3049:2: rule__List__Group_1__0__Impl rule__List__Group_1__1
            {
            pushFollow(FOLLOW_rule__List__Group_1__0__Impl_in_rule__List__Group_1__06066);
            rule__List__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group_1__1_in_rule__List__Group_1__06069);
            rule__List__Group_1__1();
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
    // $ANTLR end rule__List__Group_1__0


    // $ANTLR start rule__List__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3056:1: rule__List__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__List__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3060:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3061:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3061:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3062:1: 'item='
            {
             before(grammarAccess.getListAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__List__Group_1__0__Impl6097); 
             after(grammarAccess.getListAccess().getItemKeyword_1_0()); 

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
    // $ANTLR end rule__List__Group_1__0__Impl


    // $ANTLR start rule__List__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3075:1: rule__List__Group_1__1 : rule__List__Group_1__1__Impl ;
    public final void rule__List__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3079:1: ( rule__List__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3080:2: rule__List__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__List__Group_1__1__Impl_in_rule__List__Group_1__16128);
            rule__List__Group_1__1__Impl();
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
    // $ANTLR end rule__List__Group_1__1


    // $ANTLR start rule__List__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3086:1: rule__List__Group_1__1__Impl : ( ( rule__List__ItemAssignment_1_1 ) ) ;
    public final void rule__List__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3090:1: ( ( ( rule__List__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3091:1: ( ( rule__List__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3091:1: ( ( rule__List__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3092:1: ( rule__List__ItemAssignment_1_1 )
            {
             before(grammarAccess.getListAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3093:1: ( rule__List__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3093:2: rule__List__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__List__ItemAssignment_1_1_in_rule__List__Group_1__1__Impl6155);
            rule__List__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getListAccess().getItemAssignment_1_1()); 

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
    // $ANTLR end rule__List__Group_1__1__Impl


    // $ANTLR start rule__List__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3107:1: rule__List__Group_2__0 : rule__List__Group_2__0__Impl rule__List__Group_2__1 ;
    public final void rule__List__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3111:1: ( rule__List__Group_2__0__Impl rule__List__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3112:2: rule__List__Group_2__0__Impl rule__List__Group_2__1
            {
            pushFollow(FOLLOW_rule__List__Group_2__0__Impl_in_rule__List__Group_2__06189);
            rule__List__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group_2__1_in_rule__List__Group_2__06192);
            rule__List__Group_2__1();
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
    // $ANTLR end rule__List__Group_2__0


    // $ANTLR start rule__List__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3119:1: rule__List__Group_2__0__Impl : ( 'separator=' ) ;
    public final void rule__List__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3123:1: ( ( 'separator=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3124:1: ( 'separator=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3124:1: ( 'separator=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3125:1: 'separator='
            {
             before(grammarAccess.getListAccess().getSeparatorKeyword_2_0()); 
            match(input,28,FOLLOW_28_in_rule__List__Group_2__0__Impl6220); 
             after(grammarAccess.getListAccess().getSeparatorKeyword_2_0()); 

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
    // $ANTLR end rule__List__Group_2__0__Impl


    // $ANTLR start rule__List__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3138:1: rule__List__Group_2__1 : rule__List__Group_2__1__Impl ;
    public final void rule__List__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3142:1: ( rule__List__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3143:2: rule__List__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__List__Group_2__1__Impl_in_rule__List__Group_2__16251);
            rule__List__Group_2__1__Impl();
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
    // $ANTLR end rule__List__Group_2__1


    // $ANTLR start rule__List__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3149:1: rule__List__Group_2__1__Impl : ( ( rule__List__SeparatorAssignment_2_1 ) ) ;
    public final void rule__List__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3153:1: ( ( ( rule__List__SeparatorAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3154:1: ( ( rule__List__SeparatorAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3154:1: ( ( rule__List__SeparatorAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3155:1: ( rule__List__SeparatorAssignment_2_1 )
            {
             before(grammarAccess.getListAccess().getSeparatorAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3156:1: ( rule__List__SeparatorAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3156:2: rule__List__SeparatorAssignment_2_1
            {
            pushFollow(FOLLOW_rule__List__SeparatorAssignment_2_1_in_rule__List__Group_2__1__Impl6278);
            rule__List__SeparatorAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getListAccess().getSeparatorAssignment_2_1()); 

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
    // $ANTLR end rule__List__Group_2__1__Impl


    // $ANTLR start rule__Mapping__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3170:1: rule__Mapping__Group__0 : rule__Mapping__Group__0__Impl rule__Mapping__Group__1 ;
    public final void rule__Mapping__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3174:1: ( rule__Mapping__Group__0__Impl rule__Mapping__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3175:2: rule__Mapping__Group__0__Impl rule__Mapping__Group__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group__0__Impl_in_rule__Mapping__Group__06312);
            rule__Mapping__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__1_in_rule__Mapping__Group__06315);
            rule__Mapping__Group__1();
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
    // $ANTLR end rule__Mapping__Group__0


    // $ANTLR start rule__Mapping__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3182:1: rule__Mapping__Group__0__Impl : ( ( rule__Mapping__CmdAssignment_0 ) ) ;
    public final void rule__Mapping__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3186:1: ( ( ( rule__Mapping__CmdAssignment_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3187:1: ( ( rule__Mapping__CmdAssignment_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3187:1: ( ( rule__Mapping__CmdAssignment_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3188:1: ( rule__Mapping__CmdAssignment_0 )
            {
             before(grammarAccess.getMappingAccess().getCmdAssignment_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3189:1: ( rule__Mapping__CmdAssignment_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3189:2: rule__Mapping__CmdAssignment_0
            {
            pushFollow(FOLLOW_rule__Mapping__CmdAssignment_0_in_rule__Mapping__Group__0__Impl6342);
            rule__Mapping__CmdAssignment_0();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getCmdAssignment_0()); 

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
    // $ANTLR end rule__Mapping__Group__0__Impl


    // $ANTLR start rule__Mapping__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3199:1: rule__Mapping__Group__1 : rule__Mapping__Group__1__Impl rule__Mapping__Group__2 ;
    public final void rule__Mapping__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3203:1: ( rule__Mapping__Group__1__Impl rule__Mapping__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3204:2: rule__Mapping__Group__1__Impl rule__Mapping__Group__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group__1__Impl_in_rule__Mapping__Group__16372);
            rule__Mapping__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__2_in_rule__Mapping__Group__16375);
            rule__Mapping__Group__2();
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
    // $ANTLR end rule__Mapping__Group__1


    // $ANTLR start rule__Mapping__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3211:1: rule__Mapping__Group__1__Impl : ( '=' ) ;
    public final void rule__Mapping__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3215:1: ( ( '=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3216:1: ( '=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3216:1: ( '=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3217:1: '='
            {
             before(grammarAccess.getMappingAccess().getEqualsSignKeyword_1()); 
            match(input,29,FOLLOW_29_in_rule__Mapping__Group__1__Impl6403); 
             after(grammarAccess.getMappingAccess().getEqualsSignKeyword_1()); 

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
    // $ANTLR end rule__Mapping__Group__1__Impl


    // $ANTLR start rule__Mapping__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3230:1: rule__Mapping__Group__2 : rule__Mapping__Group__2__Impl rule__Mapping__Group__3 ;
    public final void rule__Mapping__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3234:1: ( rule__Mapping__Group__2__Impl rule__Mapping__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3235:2: rule__Mapping__Group__2__Impl rule__Mapping__Group__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group__2__Impl_in_rule__Mapping__Group__26434);
            rule__Mapping__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__3_in_rule__Mapping__Group__26437);
            rule__Mapping__Group__3();
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
    // $ANTLR end rule__Mapping__Group__2


    // $ANTLR start rule__Mapping__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3242:1: rule__Mapping__Group__2__Impl : ( ( rule__Mapping__LabelAssignment_2 ) ) ;
    public final void rule__Mapping__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3246:1: ( ( ( rule__Mapping__LabelAssignment_2 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3247:1: ( ( rule__Mapping__LabelAssignment_2 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3247:1: ( ( rule__Mapping__LabelAssignment_2 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3248:1: ( rule__Mapping__LabelAssignment_2 )
            {
             before(grammarAccess.getMappingAccess().getLabelAssignment_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3249:1: ( rule__Mapping__LabelAssignment_2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3249:2: rule__Mapping__LabelAssignment_2
            {
            pushFollow(FOLLOW_rule__Mapping__LabelAssignment_2_in_rule__Mapping__Group__2__Impl6464);
            rule__Mapping__LabelAssignment_2();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getLabelAssignment_2()); 

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
    // $ANTLR end rule__Mapping__Group__2__Impl


    // $ANTLR start rule__Mapping__Group__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3259:1: rule__Mapping__Group__3 : rule__Mapping__Group__3__Impl ;
    public final void rule__Mapping__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3263:1: ( rule__Mapping__Group__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3264:2: rule__Mapping__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group__3__Impl_in_rule__Mapping__Group__36494);
            rule__Mapping__Group__3__Impl();
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
    // $ANTLR end rule__Mapping__Group__3


    // $ANTLR start rule__Mapping__Group__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3270:1: rule__Mapping__Group__3__Impl : ( ( rule__Mapping__Group_3__0 )? ) ;
    public final void rule__Mapping__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3274:1: ( ( ( rule__Mapping__Group_3__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3275:1: ( ( rule__Mapping__Group_3__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3275:1: ( ( rule__Mapping__Group_3__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3276:1: ( rule__Mapping__Group_3__0 )?
            {
             before(grammarAccess.getMappingAccess().getGroup_3()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3277:1: ( rule__Mapping__Group_3__0 )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==30) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3277:2: rule__Mapping__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_3__0_in_rule__Mapping__Group__3__Impl6521);
                    rule__Mapping__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMappingAccess().getGroup_3()); 

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
    // $ANTLR end rule__Mapping__Group__3__Impl


    // $ANTLR start rule__Mapping__Group_3__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3295:1: rule__Mapping__Group_3__0 : rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1 ;
    public final void rule__Mapping__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3299:1: ( rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3300:2: rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_3__0__Impl_in_rule__Mapping__Group_3__06560);
            rule__Mapping__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_3__1_in_rule__Mapping__Group_3__06563);
            rule__Mapping__Group_3__1();
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
    // $ANTLR end rule__Mapping__Group_3__0


    // $ANTLR start rule__Mapping__Group_3__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3307:1: rule__Mapping__Group_3__0__Impl : ( ':' ) ;
    public final void rule__Mapping__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3311:1: ( ( ':' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3312:1: ( ':' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3312:1: ( ':' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3313:1: ':'
            {
             before(grammarAccess.getMappingAccess().getColonKeyword_3_0()); 
            match(input,30,FOLLOW_30_in_rule__Mapping__Group_3__0__Impl6591); 
             after(grammarAccess.getMappingAccess().getColonKeyword_3_0()); 

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
    // $ANTLR end rule__Mapping__Group_3__0__Impl


    // $ANTLR start rule__Mapping__Group_3__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3326:1: rule__Mapping__Group_3__1 : rule__Mapping__Group_3__1__Impl ;
    public final void rule__Mapping__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3330:1: ( rule__Mapping__Group_3__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3331:2: rule__Mapping__Group_3__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_3__1__Impl_in_rule__Mapping__Group_3__16622);
            rule__Mapping__Group_3__1__Impl();
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
    // $ANTLR end rule__Mapping__Group_3__1


    // $ANTLR start rule__Mapping__Group_3__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3337:1: rule__Mapping__Group_3__1__Impl : ( ( rule__Mapping__IconAssignment_3_1 ) ) ;
    public final void rule__Mapping__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3341:1: ( ( ( rule__Mapping__IconAssignment_3_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3342:1: ( ( rule__Mapping__IconAssignment_3_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3342:1: ( ( rule__Mapping__IconAssignment_3_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3343:1: ( rule__Mapping__IconAssignment_3_1 )
            {
             before(grammarAccess.getMappingAccess().getIconAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3344:1: ( rule__Mapping__IconAssignment_3_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3344:2: rule__Mapping__IconAssignment_3_1
            {
            pushFollow(FOLLOW_rule__Mapping__IconAssignment_3_1_in_rule__Mapping__Group_3__1__Impl6649);
            rule__Mapping__IconAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getIconAssignment_3_1()); 

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
    // $ANTLR end rule__Mapping__Group_3__1__Impl


    // $ANTLR start rule__Sitemap__NameAssignment_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3359:1: rule__Sitemap__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Sitemap__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3363:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3364:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3364:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3365:1: RULE_ID
            {
             before(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_06688); 
             after(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 

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
    // $ANTLR end rule__Sitemap__NameAssignment_0


    // $ANTLR start rule__Sitemap__LabelAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3374:1: rule__Sitemap__LabelAssignment_1_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3378:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3379:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3379:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3380:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_16719); 
             after(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__Sitemap__LabelAssignment_1_1


    // $ANTLR start rule__Sitemap__IconAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3389:1: rule__Sitemap__IconAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3393:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3394:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3394:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3395:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_16750); 
             after(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Sitemap__IconAssignment_2_1


    // $ANTLR start rule__Sitemap__ChildrenAssignment_4
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3404:1: rule__Sitemap__ChildrenAssignment_4 : ( ruleWidget ) ;
    public final void rule__Sitemap__ChildrenAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3408:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3409:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3409:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3410:1: ruleWidget
            {
             before(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_46781);
            ruleWidget();
            _fsp--;

             after(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 

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
    // $ANTLR end rule__Sitemap__ChildrenAssignment_4


    // $ANTLR start rule__Widget__LabelAssignment_1_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3419:1: rule__Widget__LabelAssignment_1_1_1 : ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) ;
    public final void rule__Widget__LabelAssignment_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3423:1: ( ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3424:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3424:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3425:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3426:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3426:2: rule__Widget__LabelAlternatives_1_1_1_0
            {
            pushFollow(FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_16812);
            rule__Widget__LabelAlternatives_1_1_1_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_1_0()); 

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
    // $ANTLR end rule__Widget__LabelAssignment_1_1_1


    // $ANTLR start rule__Widget__IconAssignment_1_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3435:1: rule__Widget__IconAssignment_1_2_1 : ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) ;
    public final void rule__Widget__IconAssignment_1_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3439:1: ( ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3440:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3440:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3441:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getIconAlternatives_1_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3442:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3442:2: rule__Widget__IconAlternatives_1_2_1_0
            {
            pushFollow(FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_16845);
            rule__Widget__IconAlternatives_1_2_1_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getIconAlternatives_1_2_1_0()); 

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
    // $ANTLR end rule__Widget__IconAssignment_1_2_1


    // $ANTLR start rule__LinkableWidget__LabelAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3451:1: rule__LinkableWidget__LabelAssignment_1_1 : ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) ;
    public final void rule__LinkableWidget__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3455:1: ( ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3456:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3456:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3457:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAlternatives_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3458:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3458:2: rule__LinkableWidget__LabelAlternatives_1_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_16878);
            rule__LinkableWidget__LabelAlternatives_1_1_0();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getLabelAlternatives_1_1_0()); 

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
    // $ANTLR end rule__LinkableWidget__LabelAssignment_1_1


    // $ANTLR start rule__LinkableWidget__IconAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3467:1: rule__LinkableWidget__IconAssignment_2_1 : ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) ;
    public final void rule__LinkableWidget__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3471:1: ( ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3472:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3472:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3473:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAlternatives_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3474:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3474:2: rule__LinkableWidget__IconAlternatives_2_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_16911);
            rule__LinkableWidget__IconAlternatives_2_1_0();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getIconAlternatives_2_1_0()); 

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
    // $ANTLR end rule__LinkableWidget__IconAssignment_2_1


    // $ANTLR start rule__LinkableWidget__ChildrenAssignment_3_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3483:1: rule__LinkableWidget__ChildrenAssignment_3_1 : ( ruleWidget ) ;
    public final void rule__LinkableWidget__ChildrenAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3487:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3488:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3488:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3489:1: ruleWidget
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_16944);
            ruleWidget();
            _fsp--;

             after(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 

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
    // $ANTLR end rule__LinkableWidget__ChildrenAssignment_3_1


    // $ANTLR start rule__Frame__ItemAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3498:1: rule__Frame__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Frame__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3502:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3503:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3503:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3504:1: RULE_ID
            {
             before(grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_16975); 
             after(grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Frame__ItemAssignment_2_1


    // $ANTLR start rule__Text__ItemAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3513:1: rule__Text__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Text__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3517:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3518:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3518:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3519:1: RULE_ID
            {
             before(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_17006); 
             after(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Text__ItemAssignment_2_1


    // $ANTLR start rule__Group__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3528:1: rule__Group__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Group__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3532:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3533:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3533:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3534:1: RULE_ID
            {
             before(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_17037); 
             after(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__Group__ItemAssignment_1_1


    // $ANTLR start rule__Image__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3543:1: rule__Image__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Image__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3547:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3548:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3548:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3549:1: RULE_ID
            {
             before(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_17068); 
             after(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__Image__ItemAssignment_1_1


    // $ANTLR start rule__Image__UrlAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3558:1: rule__Image__UrlAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Image__UrlAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3562:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3563:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3563:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3564:1: RULE_STRING
            {
             before(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_17099); 
             after(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Image__UrlAssignment_2_1


    // $ANTLR start rule__Switch__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3573:1: rule__Switch__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Switch__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3577:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3578:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3578:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3579:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_17130); 
             after(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__Switch__ItemAssignment_1_1


    // $ANTLR start rule__Switch__MappingsAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3588:1: rule__Switch__MappingsAssignment_2_1 : ( ruleMapping ) ;
    public final void rule__Switch__MappingsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3592:1: ( ( ruleMapping ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3593:1: ( ruleMapping )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3593:1: ( ruleMapping )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3594:1: ruleMapping
            {
             before(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleMapping_in_rule__Switch__MappingsAssignment_2_17161);
            ruleMapping();
            _fsp--;

             after(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Switch__MappingsAssignment_2_1


    // $ANTLR start rule__Switch__MappingsAssignment_2_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3603:1: rule__Switch__MappingsAssignment_2_2_1 : ( ruleMapping ) ;
    public final void rule__Switch__MappingsAssignment_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3607:1: ( ( ruleMapping ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3608:1: ( ruleMapping )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3608:1: ( ruleMapping )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3609:1: ruleMapping
            {
             before(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_2_1_0()); 
            pushFollow(FOLLOW_ruleMapping_in_rule__Switch__MappingsAssignment_2_2_17192);
            ruleMapping();
            _fsp--;

             after(grammarAccess.getSwitchAccess().getMappingsMappingParserRuleCall_2_2_1_0()); 

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
    // $ANTLR end rule__Switch__MappingsAssignment_2_2_1


    // $ANTLR start rule__Selection__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3618:1: rule__Selection__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Selection__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3622:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3623:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3623:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3624:1: RULE_ID
            {
             before(grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_17223); 
             after(grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__Selection__ItemAssignment_1_1


    // $ANTLR start rule__Selection__MappingsAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3633:1: rule__Selection__MappingsAssignment_2_1 : ( ruleMapping ) ;
    public final void rule__Selection__MappingsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3637:1: ( ( ruleMapping ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3638:1: ( ruleMapping )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3638:1: ( ruleMapping )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3639:1: ruleMapping
            {
             before(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleMapping_in_rule__Selection__MappingsAssignment_2_17254);
            ruleMapping();
            _fsp--;

             after(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Selection__MappingsAssignment_2_1


    // $ANTLR start rule__Selection__MappingsAssignment_2_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3648:1: rule__Selection__MappingsAssignment_2_2_1 : ( ruleMapping ) ;
    public final void rule__Selection__MappingsAssignment_2_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3652:1: ( ( ruleMapping ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3653:1: ( ruleMapping )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3653:1: ( ruleMapping )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3654:1: ruleMapping
            {
             before(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_2_1_0()); 
            pushFollow(FOLLOW_ruleMapping_in_rule__Selection__MappingsAssignment_2_2_17285);
            ruleMapping();
            _fsp--;

             after(grammarAccess.getSelectionAccess().getMappingsMappingParserRuleCall_2_2_1_0()); 

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
    // $ANTLR end rule__Selection__MappingsAssignment_2_2_1


    // $ANTLR start rule__List__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3663:1: rule__List__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__List__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3667:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3668:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3668:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3669:1: RULE_ID
            {
             before(grammarAccess.getListAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__List__ItemAssignment_1_17316); 
             after(grammarAccess.getListAccess().getItemIDTerminalRuleCall_1_1_0()); 

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
    // $ANTLR end rule__List__ItemAssignment_1_1


    // $ANTLR start rule__List__SeparatorAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3678:1: rule__List__SeparatorAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__List__SeparatorAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3682:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3683:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3683:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3684:1: RULE_STRING
            {
             before(grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__List__SeparatorAssignment_2_17347); 
             after(grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__List__SeparatorAssignment_2_1


    // $ANTLR start rule__Mapping__CmdAssignment_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3693:1: rule__Mapping__CmdAssignment_0 : ( ( rule__Mapping__CmdAlternatives_0_0 ) ) ;
    public final void rule__Mapping__CmdAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3697:1: ( ( ( rule__Mapping__CmdAlternatives_0_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3698:1: ( ( rule__Mapping__CmdAlternatives_0_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3698:1: ( ( rule__Mapping__CmdAlternatives_0_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3699:1: ( rule__Mapping__CmdAlternatives_0_0 )
            {
             before(grammarAccess.getMappingAccess().getCmdAlternatives_0_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3700:1: ( rule__Mapping__CmdAlternatives_0_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3700:2: rule__Mapping__CmdAlternatives_0_0
            {
            pushFollow(FOLLOW_rule__Mapping__CmdAlternatives_0_0_in_rule__Mapping__CmdAssignment_07378);
            rule__Mapping__CmdAlternatives_0_0();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getCmdAlternatives_0_0()); 

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
    // $ANTLR end rule__Mapping__CmdAssignment_0


    // $ANTLR start rule__Mapping__LabelAssignment_2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3709:1: rule__Mapping__LabelAssignment_2 : ( ( rule__Mapping__LabelAlternatives_2_0 ) ) ;
    public final void rule__Mapping__LabelAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3713:1: ( ( ( rule__Mapping__LabelAlternatives_2_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3714:1: ( ( rule__Mapping__LabelAlternatives_2_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3714:1: ( ( rule__Mapping__LabelAlternatives_2_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3715:1: ( rule__Mapping__LabelAlternatives_2_0 )
            {
             before(grammarAccess.getMappingAccess().getLabelAlternatives_2_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3716:1: ( rule__Mapping__LabelAlternatives_2_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3716:2: rule__Mapping__LabelAlternatives_2_0
            {
            pushFollow(FOLLOW_rule__Mapping__LabelAlternatives_2_0_in_rule__Mapping__LabelAssignment_27411);
            rule__Mapping__LabelAlternatives_2_0();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getLabelAlternatives_2_0()); 

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
    // $ANTLR end rule__Mapping__LabelAssignment_2


    // $ANTLR start rule__Mapping__IconAssignment_3_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3725:1: rule__Mapping__IconAssignment_3_1 : ( ( rule__Mapping__IconAlternatives_3_1_0 ) ) ;
    public final void rule__Mapping__IconAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3729:1: ( ( ( rule__Mapping__IconAlternatives_3_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3730:1: ( ( rule__Mapping__IconAlternatives_3_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3730:1: ( ( rule__Mapping__IconAlternatives_3_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3731:1: ( rule__Mapping__IconAlternatives_3_1_0 )
            {
             before(grammarAccess.getMappingAccess().getIconAlternatives_3_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3732:1: ( rule__Mapping__IconAlternatives_3_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3732:2: rule__Mapping__IconAlternatives_3_1_0
            {
            pushFollow(FOLLOW_rule__Mapping__IconAlternatives_3_1_0_in_rule__Mapping__IconAssignment_3_17444);
            rule__Mapping__IconAlternatives_3_1_0();
            _fsp--;


            }

             after(grammarAccess.getMappingAccess().getIconAlternatives_3_1_0()); 

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
    // $ANTLR end rule__Mapping__IconAssignment_3_1


 

    public static final BitSet FOLLOW_ruleSitemapModel_in_entryRuleSitemapModel61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemapModel68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__0_in_ruleSitemapModel94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_entryRuleSitemap121 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemap128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0_in_ruleSitemap154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget181 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Alternatives_in_ruleWidget214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_entryRuleLinkableWidget241 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLinkableWidget248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__0_in_ruleLinkableWidget274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame301 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0_in_ruleFrame334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText361 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0_in_ruleText394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup421 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0_in_ruleGroup454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage481 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0_in_ruleImage514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch541 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0_in_ruleSwitch574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_entryRuleSelection601 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelection608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__0_in_ruleSelection634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_entryRuleList661 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleList668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__0_in_ruleList694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping721 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__0_in_ruleMapping754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_rule__Widget__Alternatives_1_0874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_01004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_01021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_01038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_01055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_01087 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_01104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_01136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__CmdAlternatives_0_01185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Mapping__CmdAlternatives_0_01202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__LabelAlternatives_2_01234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Mapping__LabelAlternatives_2_01251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__IconAlternatives_3_1_01283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Mapping__IconAlternatives_3_1_01300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01330 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01452 = new BitSet(new long[]{0x000000000000D000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11512 = new BitSet(new long[]{0x0000000000009000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21573 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31634 = new BitSet(new long[]{0x000000000C5D0000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Sitemap__Group__3__Impl1665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41696 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1728 = new BitSet(new long[]{0x000000000C5D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1740 = new BitSet(new long[]{0x000000000C5D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Sitemap__Group__5__Impl1801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01844 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01967 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__12029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl2056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__02090 = new BitSet(new long[]{0x000000000000C002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__02093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl2120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__12150 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__12153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl2180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__22211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl2238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__02275 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__02278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02398 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02521 = new BitSet(new long[]{0x000000000000D002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12581 = new BitSet(new long[]{0x0000000000009002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22642 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02769 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02892 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__03015 = new BitSet(new long[]{0x000000000C5D0000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__03018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl3046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__13077 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__13080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl3109 = new BitSet(new long[]{0x000000000C5D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl3121 = new BitSet(new long[]{0x000000000C5D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__23154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl3182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__03219 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__03222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Frame__Group__0__Impl3250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__13281 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03406 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Frame__Group_2__0__Impl3437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03529 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Text__Group__0__Impl3560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13591 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03716 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Text__Group_2__0__Impl3747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03839 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Group__Group__0__Impl3870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03962 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Group__Group_1__0__Impl3993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__14024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl4051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__04085 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_rule__Image__Group__1_in_rule__Image__Group__04088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Image__Group__0__Impl4116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__14147 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__Image__Group__2_in_rule__Image__Group__14150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl4177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__24208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl4235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__04271 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__04274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Image__Group_1__0__Impl4302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04394 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Image__Group_2__0__Impl4425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04517 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Switch__Group__0__Impl4548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14579 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04703 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Switch__Group_1__0__Impl4734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04826 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Switch__Group_2__0__Impl4857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14888 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__MappingsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24948 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__3_in_rule__Switch__Group_2__24951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2_2__0_in_rule__Switch__Group_2__2__Impl4978 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__3__Impl_in_rule__Switch__Group_2__35009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Switch__Group_2__3__Impl5037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2_2__0__Impl_in_rule__Switch__Group_2_2__05076 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Switch__Group_2_2__1_in_rule__Switch__Group_2_2__05079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Switch__Group_2_2__0__Impl5107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2_2__1__Impl_in_rule__Switch__Group_2_2__15138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__MappingsAssignment_2_2_1_in_rule__Switch__Group_2_2__1__Impl5165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__05199 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__05202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__Selection__Group__0__Impl5230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__15261 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__Selection__Group__2_in_rule__Selection__Group__15264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl5291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__2__Impl_in_rule__Selection__Group__25321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__0_in_rule__Selection__Group__2__Impl5348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__05385 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__05388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Selection__Group_1__0__Impl5416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__15447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl5474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__0__Impl_in_rule__Selection__Group_2__05508 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__1_in_rule__Selection__Group_2__05511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Selection__Group_2__0__Impl5539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__1__Impl_in_rule__Selection__Group_2__15570 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__2_in_rule__Selection__Group_2__15573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__MappingsAssignment_2_1_in_rule__Selection__Group_2__1__Impl5600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__2__Impl_in_rule__Selection__Group_2__25630 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__3_in_rule__Selection__Group_2__25633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2_2__0_in_rule__Selection__Group_2__2__Impl5660 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2__3__Impl_in_rule__Selection__Group_2__35691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Selection__Group_2__3__Impl5719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2_2__0__Impl_in_rule__Selection__Group_2_2__05758 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Selection__Group_2_2__1_in_rule__Selection__Group_2_2__05761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Selection__Group_2_2__0__Impl5789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_2_2__1__Impl_in_rule__Selection__Group_2_2__15820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__MappingsAssignment_2_2_1_in_rule__Selection__Group_2_2__1__Impl5847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__0__Impl_in_rule__List__Group__05881 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__List__Group__1_in_rule__List__Group__05884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__List__Group__0__Impl5912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__1__Impl_in_rule__List__Group__15943 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_rule__List__Group__2_in_rule__List__Group__15946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__0_in_rule__List__Group__1__Impl5973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__2__Impl_in_rule__List__Group__26003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__0_in_rule__List__Group__2__Impl6030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__0__Impl_in_rule__List__Group_1__06066 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__List__Group_1__1_in_rule__List__Group_1__06069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__List__Group_1__0__Impl6097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__1__Impl_in_rule__List__Group_1__16128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__ItemAssignment_1_1_in_rule__List__Group_1__1__Impl6155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__0__Impl_in_rule__List__Group_2__06189 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__List__Group_2__1_in_rule__List__Group_2__06192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__List__Group_2__0__Impl6220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__1__Impl_in_rule__List__Group_2__16251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__SeparatorAssignment_2_1_in_rule__List__Group_2__1__Impl6278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__0__Impl_in_rule__Mapping__Group__06312 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__1_in_rule__Mapping__Group__06315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__CmdAssignment_0_in_rule__Mapping__Group__0__Impl6342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__1__Impl_in_rule__Mapping__Group__16372 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Mapping__Group__2_in_rule__Mapping__Group__16375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__Mapping__Group__1__Impl6403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__2__Impl_in_rule__Mapping__Group__26434 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__3_in_rule__Mapping__Group__26437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__LabelAssignment_2_in_rule__Mapping__Group__2__Impl6464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__3__Impl_in_rule__Mapping__Group__36494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__0_in_rule__Mapping__Group__3__Impl6521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__0__Impl_in_rule__Mapping__Group_3__06560 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__1_in_rule__Mapping__Group_3__06563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__Mapping__Group_3__0__Impl6591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__1__Impl_in_rule__Mapping__Group_3__16622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__IconAssignment_3_1_in_rule__Mapping__Group_3__1__Impl6649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_06688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_16719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_16750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_46781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_16812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_16845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_16878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_16911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_16944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_16975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_17006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_17037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_17068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_17099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_17130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_rule__Switch__MappingsAssignment_2_17161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_rule__Switch__MappingsAssignment_2_2_17192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_17223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_rule__Selection__MappingsAssignment_2_17254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapping_in_rule__Selection__MappingsAssignment_2_2_17285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__List__ItemAssignment_1_17316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__List__SeparatorAssignment_2_17347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__CmdAlternatives_0_0_in_rule__Mapping__CmdAssignment_07378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__LabelAlternatives_2_0_in_rule__Mapping__LabelAssignment_27411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__IconAlternatives_3_1_0_in_rule__Mapping__IconAssignment_3_17444 = new BitSet(new long[]{0x0000000000000002L});

}