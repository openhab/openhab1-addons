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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'{'", "'}'", "'label='", "'icon='", "'Frame'", "'item='", "'Text'", "'Group'", "'Image'", "'url='", "'Switch'", "'buttonLabels=['", "']'", "'Selection'", "'List'", "'separator='"
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


    // $ANTLR start rule__Widget__Alternatives
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:369:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );
    public final void rule__Widget__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:373:1: ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==16||(LA1_0>=18 && LA1_0<=20)) ) {
                alt1=1;
            }
            else if ( (LA1_0==22||(LA1_0>=25 && LA1_0<=26)) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("369:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:374:1: ( ruleLinkableWidget )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:374:1: ( ruleLinkableWidget )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:375:1: ruleLinkableWidget
                    {
                     before(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives730);
                    ruleLinkableWidget();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:380:6: ( ( rule__Widget__Group_1__0 ) )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:380:6: ( ( rule__Widget__Group_1__0 ) )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:381:1: ( rule__Widget__Group_1__0 )
                    {
                     before(grammarAccess.getWidgetAccess().getGroup_1()); 
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:382:1: ( rule__Widget__Group_1__0 )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:382:2: rule__Widget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives747);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:391:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) );
    public final void rule__Widget__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:395:1: ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) )
            int alt2=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt2=1;
                }
                break;
            case 25:
                {
                alt2=2;
                }
                break;
            case 26:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("391:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) | ( ruleList ) );", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:396:1: ( ruleSwitch )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:396:1: ( ruleSwitch )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:397:1: ruleSwitch
                    {
                     before(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 
                    pushFollow(FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0780);
                    ruleSwitch();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:402:6: ( ruleSelection )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:402:6: ( ruleSelection )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:403:1: ruleSelection
                    {
                     before(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 
                    pushFollow(FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0797);
                    ruleSelection();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:408:6: ( ruleList )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:408:6: ( ruleList )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:409:1: ruleList
                    {
                     before(grammarAccess.getWidgetAccess().getListParserRuleCall_1_0_2()); 
                    pushFollow(FOLLOW_ruleList_in_rule__Widget__Alternatives_1_0814);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:419:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__LabelAlternatives_1_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:423:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("419:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:424:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:424:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:425:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0846); 
                     after(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:430:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:430:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:431:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0863); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:441:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__IconAlternatives_1_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:445:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("441:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:446:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:446:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:447:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0895); 
                     after(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:453:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0912); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:463:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );
    public final void rule__LinkableWidget__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:467:1: ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) )
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
                    new NoViableAltException("463:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:468:1: ( ruleText )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:468:1: ( ruleText )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:469:1: ruleText
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_0944);
                    ruleText();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:6: ( ruleGroup )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:6: ( ruleGroup )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:475:1: ruleGroup
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_0961);
                    ruleGroup();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:6: ( ruleImage )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:6: ( ruleImage )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:481:1: ruleImage
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 
                    pushFollow(FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_0978);
                    ruleImage();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:486:6: ( ruleFrame )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:486:6: ( ruleFrame )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:487:1: ruleFrame
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3()); 
                    pushFollow(FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_0995);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:497:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__LabelAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:501:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("497:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:502:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:502:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:503:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_01027); 
                     after(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:508:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:508:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:509:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_01044); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:519:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__IconAlternatives_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:523:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("519:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:524:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:524:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:525:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_01076); 
                     after(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:530:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:530:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:531:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01093); 
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


    // $ANTLR start rule__SitemapModel__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:543:1: rule__SitemapModel__Group__0 : rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 ;
    public final void rule__SitemapModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:547:1: ( rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:548:2: rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01123);
            rule__SitemapModel__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01126);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:555:1: rule__SitemapModel__Group__0__Impl : ( 'sitemap' ) ;
    public final void rule__SitemapModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:559:1: ( ( 'sitemap' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:560:1: ( 'sitemap' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:560:1: ( 'sitemap' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:561:1: 'sitemap'
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapKeyword_0()); 
            match(input,11,FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1154); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:574:1: rule__SitemapModel__Group__1 : rule__SitemapModel__Group__1__Impl ;
    public final void rule__SitemapModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:578:1: ( rule__SitemapModel__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:579:2: rule__SitemapModel__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11185);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:585:1: rule__SitemapModel__Group__1__Impl : ( ruleSitemap ) ;
    public final void rule__SitemapModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:589:1: ( ( ruleSitemap ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:590:1: ( ruleSitemap )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:590:1: ( ruleSitemap )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:591:1: ruleSitemap
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1()); 
            pushFollow(FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1212);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:606:1: rule__Sitemap__Group__0 : rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 ;
    public final void rule__Sitemap__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:610:1: ( rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:611:2: rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01245);
            rule__Sitemap__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01248);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:618:1: rule__Sitemap__Group__0__Impl : ( ( rule__Sitemap__NameAssignment_0 ) ) ;
    public final void rule__Sitemap__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:622:1: ( ( ( rule__Sitemap__NameAssignment_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:623:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:623:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:624:1: ( rule__Sitemap__NameAssignment_0 )
            {
             before(grammarAccess.getSitemapAccess().getNameAssignment_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:625:1: ( rule__Sitemap__NameAssignment_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:625:2: rule__Sitemap__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1275);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:635:1: rule__Sitemap__Group__1 : rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 ;
    public final void rule__Sitemap__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:639:1: ( rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:640:2: rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11305);
            rule__Sitemap__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11308);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:647:1: rule__Sitemap__Group__1__Impl : ( ( rule__Sitemap__Group_1__0 )? ) ;
    public final void rule__Sitemap__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:651:1: ( ( ( rule__Sitemap__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:652:1: ( ( rule__Sitemap__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:652:1: ( ( rule__Sitemap__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:653:1: ( rule__Sitemap__Group_1__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:654:1: ( rule__Sitemap__Group_1__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==14) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:654:2: rule__Sitemap__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1335);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:664:1: rule__Sitemap__Group__2 : rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 ;
    public final void rule__Sitemap__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:668:1: ( rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:669:2: rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21366);
            rule__Sitemap__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21369);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:676:1: rule__Sitemap__Group__2__Impl : ( ( rule__Sitemap__Group_2__0 )? ) ;
    public final void rule__Sitemap__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:680:1: ( ( ( rule__Sitemap__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:681:1: ( ( rule__Sitemap__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:681:1: ( ( rule__Sitemap__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:682:1: ( rule__Sitemap__Group_2__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:683:1: ( rule__Sitemap__Group_2__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==15) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:683:2: rule__Sitemap__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1396);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:693:1: rule__Sitemap__Group__3 : rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 ;
    public final void rule__Sitemap__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:697:1: ( rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:698:2: rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31427);
            rule__Sitemap__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31430);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:705:1: rule__Sitemap__Group__3__Impl : ( '{' ) ;
    public final void rule__Sitemap__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:709:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:711:1: '{'
            {
             before(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_12_in_rule__Sitemap__Group__3__Impl1458); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:724:1: rule__Sitemap__Group__4 : rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 ;
    public final void rule__Sitemap__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:728:1: ( rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:729:2: rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41489);
            rule__Sitemap__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41492);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:736:1: rule__Sitemap__Group__4__Impl : ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) ;
    public final void rule__Sitemap__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:740:1: ( ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:741:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:741:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:743:1: ( rule__Sitemap__ChildrenAssignment_4 )
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:744:1: ( rule__Sitemap__ChildrenAssignment_4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:744:2: rule__Sitemap__ChildrenAssignment_4
            {
            pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1521);
            rule__Sitemap__ChildrenAssignment_4();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:747:1: ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:748:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:749:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==16||(LA10_0>=18 && LA10_0<=20)||LA10_0==22||(LA10_0>=25 && LA10_0<=26)) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:749:2: rule__Sitemap__ChildrenAssignment_4
            	    {
            	    pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1533);
            	    rule__Sitemap__ChildrenAssignment_4();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:760:1: rule__Sitemap__Group__5 : rule__Sitemap__Group__5__Impl ;
    public final void rule__Sitemap__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:764:1: ( rule__Sitemap__Group__5__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:765:2: rule__Sitemap__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51566);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:771:1: rule__Sitemap__Group__5__Impl : ( '}' ) ;
    public final void rule__Sitemap__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:775:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:776:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:776:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:777:1: '}'
            {
             before(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5()); 
            match(input,13,FOLLOW_13_in_rule__Sitemap__Group__5__Impl1594); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:802:1: rule__Sitemap__Group_1__0 : rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 ;
    public final void rule__Sitemap__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:806:1: ( rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:807:2: rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01637);
            rule__Sitemap__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01640);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:814:1: rule__Sitemap__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__Sitemap__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:818:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:819:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:819:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:820:1: 'label='
            {
             before(grammarAccess.getSitemapAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1668); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:833:1: rule__Sitemap__Group_1__1 : rule__Sitemap__Group_1__1__Impl ;
    public final void rule__Sitemap__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:837:1: ( rule__Sitemap__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:838:2: rule__Sitemap__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11699);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:844:1: rule__Sitemap__Group_1__1__Impl : ( ( rule__Sitemap__LabelAssignment_1_1 ) ) ;
    public final void rule__Sitemap__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:848:1: ( ( ( rule__Sitemap__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:849:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:849:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:850:1: ( rule__Sitemap__LabelAssignment_1_1 )
            {
             before(grammarAccess.getSitemapAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:851:1: ( rule__Sitemap__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:851:2: rule__Sitemap__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1726);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:865:1: rule__Sitemap__Group_2__0 : rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 ;
    public final void rule__Sitemap__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:869:1: ( rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:870:2: rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01760);
            rule__Sitemap__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01763);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:877:1: rule__Sitemap__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Sitemap__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:881:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:882:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:882:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:883:1: 'icon='
            {
             before(grammarAccess.getSitemapAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1791); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:896:1: rule__Sitemap__Group_2__1 : rule__Sitemap__Group_2__1__Impl ;
    public final void rule__Sitemap__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:900:1: ( rule__Sitemap__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:901:2: rule__Sitemap__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11822);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:907:1: rule__Sitemap__Group_2__1__Impl : ( ( rule__Sitemap__IconAssignment_2_1 ) ) ;
    public final void rule__Sitemap__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:911:1: ( ( ( rule__Sitemap__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:912:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:912:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:913:1: ( rule__Sitemap__IconAssignment_2_1 )
            {
             before(grammarAccess.getSitemapAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:914:1: ( rule__Sitemap__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:914:2: rule__Sitemap__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1849);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:928:1: rule__Widget__Group_1__0 : rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 ;
    public final void rule__Widget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:932:1: ( rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:933:2: rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01883);
            rule__Widget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01886);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:940:1: rule__Widget__Group_1__0__Impl : ( ( rule__Widget__Alternatives_1_0 ) ) ;
    public final void rule__Widget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:944:1: ( ( ( rule__Widget__Alternatives_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:945:1: ( ( rule__Widget__Alternatives_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:945:1: ( ( rule__Widget__Alternatives_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:946:1: ( rule__Widget__Alternatives_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getAlternatives_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:947:1: ( rule__Widget__Alternatives_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:947:2: rule__Widget__Alternatives_1_0
            {
            pushFollow(FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl1913);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:957:1: rule__Widget__Group_1__1 : rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 ;
    public final void rule__Widget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:961:1: ( rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:962:2: rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11943);
            rule__Widget__Group_1__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__11946);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:969:1: rule__Widget__Group_1__1__Impl : ( ( rule__Widget__Group_1_1__0 )? ) ;
    public final void rule__Widget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:973:1: ( ( ( rule__Widget__Group_1_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:974:1: ( ( rule__Widget__Group_1_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:974:1: ( ( rule__Widget__Group_1_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:975:1: ( rule__Widget__Group_1_1__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:976:1: ( rule__Widget__Group_1_1__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==14) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:976:2: rule__Widget__Group_1_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl1973);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:986:1: rule__Widget__Group_1__2 : rule__Widget__Group_1__2__Impl ;
    public final void rule__Widget__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:990:1: ( rule__Widget__Group_1__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:991:2: rule__Widget__Group_1__2__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__22004);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:997:1: rule__Widget__Group_1__2__Impl : ( ( rule__Widget__Group_1_2__0 )? ) ;
    public final void rule__Widget__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1001:1: ( ( ( rule__Widget__Group_1_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1002:1: ( ( rule__Widget__Group_1_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1002:1: ( ( rule__Widget__Group_1_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1003:1: ( rule__Widget__Group_1_2__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1004:1: ( rule__Widget__Group_1_2__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==15) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1004:2: rule__Widget__Group_1_2__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl2031);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1020:1: rule__Widget__Group_1_1__0 : rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 ;
    public final void rule__Widget__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1024:1: ( rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1025:2: rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__02068);
            rule__Widget__Group_1_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__02071);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1032:1: rule__Widget__Group_1_1__0__Impl : ( 'label=' ) ;
    public final void rule__Widget__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1036:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1037:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1037:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1038:1: 'label='
            {
             before(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2099); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1051:1: rule__Widget__Group_1_1__1 : rule__Widget__Group_1_1__1__Impl ;
    public final void rule__Widget__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1055:1: ( rule__Widget__Group_1_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1056:2: rule__Widget__Group_1_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12130);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1062:1: rule__Widget__Group_1_1__1__Impl : ( ( rule__Widget__LabelAssignment_1_1_1 ) ) ;
    public final void rule__Widget__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1066:1: ( ( ( rule__Widget__LabelAssignment_1_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1067:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1067:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1068:1: ( rule__Widget__LabelAssignment_1_1_1 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAssignment_1_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1069:1: ( rule__Widget__LabelAssignment_1_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1069:2: rule__Widget__LabelAssignment_1_1_1
            {
            pushFollow(FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2157);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1083:1: rule__Widget__Group_1_2__0 : rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 ;
    public final void rule__Widget__Group_1_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1087:1: ( rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1088:2: rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02191);
            rule__Widget__Group_1_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02194);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1095:1: rule__Widget__Group_1_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Widget__Group_1_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1099:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1100:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1100:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1101:1: 'icon='
            {
             before(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2222); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1114:1: rule__Widget__Group_1_2__1 : rule__Widget__Group_1_2__1__Impl ;
    public final void rule__Widget__Group_1_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1118:1: ( rule__Widget__Group_1_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1119:2: rule__Widget__Group_1_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12253);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1125:1: rule__Widget__Group_1_2__1__Impl : ( ( rule__Widget__IconAssignment_1_2_1 ) ) ;
    public final void rule__Widget__Group_1_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1129:1: ( ( ( rule__Widget__IconAssignment_1_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1130:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1130:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1131:1: ( rule__Widget__IconAssignment_1_2_1 )
            {
             before(grammarAccess.getWidgetAccess().getIconAssignment_1_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1132:1: ( rule__Widget__IconAssignment_1_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1132:2: rule__Widget__IconAssignment_1_2_1
            {
            pushFollow(FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2280);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1146:1: rule__LinkableWidget__Group__0 : rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 ;
    public final void rule__LinkableWidget__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1150:1: ( rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1151:2: rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02314);
            rule__LinkableWidget__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02317);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1158:1: rule__LinkableWidget__Group__0__Impl : ( ( rule__LinkableWidget__Alternatives_0 ) ) ;
    public final void rule__LinkableWidget__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1162:1: ( ( ( rule__LinkableWidget__Alternatives_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1163:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1163:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1164:1: ( rule__LinkableWidget__Alternatives_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getAlternatives_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1165:1: ( rule__LinkableWidget__Alternatives_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1165:2: rule__LinkableWidget__Alternatives_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2344);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1175:1: rule__LinkableWidget__Group__1 : rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 ;
    public final void rule__LinkableWidget__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1179:1: ( rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1180:2: rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12374);
            rule__LinkableWidget__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12377);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1187:1: rule__LinkableWidget__Group__1__Impl : ( ( rule__LinkableWidget__Group_1__0 )? ) ;
    public final void rule__LinkableWidget__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1191:1: ( ( ( rule__LinkableWidget__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1192:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1192:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1193:1: ( rule__LinkableWidget__Group_1__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1194:1: ( rule__LinkableWidget__Group_1__0 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==14) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1194:2: rule__LinkableWidget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2404);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1204:1: rule__LinkableWidget__Group__2 : rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 ;
    public final void rule__LinkableWidget__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1208:1: ( rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1209:2: rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22435);
            rule__LinkableWidget__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22438);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1216:1: rule__LinkableWidget__Group__2__Impl : ( ( rule__LinkableWidget__Group_2__0 )? ) ;
    public final void rule__LinkableWidget__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1220:1: ( ( ( rule__LinkableWidget__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1221:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1221:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1222:1: ( rule__LinkableWidget__Group_2__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1223:1: ( rule__LinkableWidget__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==15) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1223:2: rule__LinkableWidget__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2465);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1233:1: rule__LinkableWidget__Group__3 : rule__LinkableWidget__Group__3__Impl ;
    public final void rule__LinkableWidget__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1237:1: ( rule__LinkableWidget__Group__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1238:2: rule__LinkableWidget__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32496);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1244:1: rule__LinkableWidget__Group__3__Impl : ( ( rule__LinkableWidget__Group_3__0 )? ) ;
    public final void rule__LinkableWidget__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1248:1: ( ( ( rule__LinkableWidget__Group_3__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1249:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1249:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1250:1: ( rule__LinkableWidget__Group_3__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_3()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1251:1: ( rule__LinkableWidget__Group_3__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==12) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1251:2: rule__LinkableWidget__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2523);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1269:1: rule__LinkableWidget__Group_1__0 : rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 ;
    public final void rule__LinkableWidget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1273:1: ( rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1274:2: rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02562);
            rule__LinkableWidget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02565);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1281:1: rule__LinkableWidget__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__LinkableWidget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1285:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1286:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1286:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1287:1: 'label='
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2593); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1300:1: rule__LinkableWidget__Group_1__1 : rule__LinkableWidget__Group_1__1__Impl ;
    public final void rule__LinkableWidget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1304:1: ( rule__LinkableWidget__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1305:2: rule__LinkableWidget__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12624);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1311:1: rule__LinkableWidget__Group_1__1__Impl : ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) ;
    public final void rule__LinkableWidget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1317:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1318:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1318:2: rule__LinkableWidget__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2651);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1332:1: rule__LinkableWidget__Group_2__0 : rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 ;
    public final void rule__LinkableWidget__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1336:1: ( rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1337:2: rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02685);
            rule__LinkableWidget__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02688);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1344:1: rule__LinkableWidget__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__LinkableWidget__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1348:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1349:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1349:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1350:1: 'icon='
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2716); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1363:1: rule__LinkableWidget__Group_2__1 : rule__LinkableWidget__Group_2__1__Impl ;
    public final void rule__LinkableWidget__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1367:1: ( rule__LinkableWidget__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1368:2: rule__LinkableWidget__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12747);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1374:1: rule__LinkableWidget__Group_2__1__Impl : ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) ;
    public final void rule__LinkableWidget__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1378:1: ( ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1380:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1381:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1381:2: rule__LinkableWidget__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2774);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1395:1: rule__LinkableWidget__Group_3__0 : rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 ;
    public final void rule__LinkableWidget__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1399:1: ( rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1400:2: rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__02808);
            rule__LinkableWidget__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__02811);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1407:1: rule__LinkableWidget__Group_3__0__Impl : ( '{' ) ;
    public final void rule__LinkableWidget__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1411:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1413:1: '{'
            {
             before(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 
            match(input,12,FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl2839); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1426:1: rule__LinkableWidget__Group_3__1 : rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 ;
    public final void rule__LinkableWidget__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1430:1: ( rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1431:2: rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__12870);
            rule__LinkableWidget__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__12873);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1438:1: rule__LinkableWidget__Group_3__1__Impl : ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) ;
    public final void rule__LinkableWidget__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1442:1: ( ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1444:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1444:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1445:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1446:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1446:2: rule__LinkableWidget__ChildrenAssignment_3_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2902);
            rule__LinkableWidget__ChildrenAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1449:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1450:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1451:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==16||(LA16_0>=18 && LA16_0<=20)||LA16_0==22||(LA16_0>=25 && LA16_0<=26)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1451:2: rule__LinkableWidget__ChildrenAssignment_3_1
            	    {
            	    pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2914);
            	    rule__LinkableWidget__ChildrenAssignment_3_1();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1462:1: rule__LinkableWidget__Group_3__2 : rule__LinkableWidget__Group_3__2__Impl ;
    public final void rule__LinkableWidget__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1466:1: ( rule__LinkableWidget__Group_3__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1467:2: rule__LinkableWidget__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__22947);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1473:1: rule__LinkableWidget__Group_3__2__Impl : ( '}' ) ;
    public final void rule__LinkableWidget__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1477:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1478:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1478:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1479:1: '}'
            {
             before(grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2()); 
            match(input,13,FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl2975); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1498:1: rule__Frame__Group__0 : rule__Frame__Group__0__Impl rule__Frame__Group__1 ;
    public final void rule__Frame__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1502:1: ( rule__Frame__Group__0__Impl rule__Frame__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1503:2: rule__Frame__Group__0__Impl rule__Frame__Group__1
            {
            pushFollow(FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__03012);
            rule__Frame__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__03015);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1510:1: rule__Frame__Group__0__Impl : ( 'Frame' ) ;
    public final void rule__Frame__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1514:1: ( ( 'Frame' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1515:1: ( 'Frame' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1515:1: ( 'Frame' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1516:1: 'Frame'
            {
             before(grammarAccess.getFrameAccess().getFrameKeyword_0()); 
            match(input,16,FOLLOW_16_in_rule__Frame__Group__0__Impl3043); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1529:1: rule__Frame__Group__1 : rule__Frame__Group__1__Impl rule__Frame__Group__2 ;
    public final void rule__Frame__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1533:1: ( rule__Frame__Group__1__Impl rule__Frame__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1534:2: rule__Frame__Group__1__Impl rule__Frame__Group__2
            {
            pushFollow(FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__13074);
            rule__Frame__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13077);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1541:1: rule__Frame__Group__1__Impl : ( () ) ;
    public final void rule__Frame__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1545:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1546:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1546:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1547:1: ()
            {
             before(grammarAccess.getFrameAccess().getFrameAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1548:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1550:1: 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1560:1: rule__Frame__Group__2 : rule__Frame__Group__2__Impl ;
    public final void rule__Frame__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1564:1: ( rule__Frame__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1565:2: rule__Frame__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23135);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1571:1: rule__Frame__Group__2__Impl : ( ( rule__Frame__Group_2__0 )? ) ;
    public final void rule__Frame__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1575:1: ( ( ( rule__Frame__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1576:1: ( ( rule__Frame__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1576:1: ( ( rule__Frame__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1577:1: ( rule__Frame__Group_2__0 )?
            {
             before(grammarAccess.getFrameAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1578:1: ( rule__Frame__Group_2__0 )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==17) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1578:2: rule__Frame__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3162);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1594:1: rule__Frame__Group_2__0 : rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 ;
    public final void rule__Frame__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1598:1: ( rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1599:2: rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03199);
            rule__Frame__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03202);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1606:1: rule__Frame__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Frame__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1610:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1611:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1611:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1612:1: 'item='
            {
             before(grammarAccess.getFrameAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Frame__Group_2__0__Impl3230); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1625:1: rule__Frame__Group_2__1 : rule__Frame__Group_2__1__Impl ;
    public final void rule__Frame__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1629:1: ( rule__Frame__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1630:2: rule__Frame__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13261);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1636:1: rule__Frame__Group_2__1__Impl : ( ( rule__Frame__ItemAssignment_2_1 ) ) ;
    public final void rule__Frame__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1640:1: ( ( ( rule__Frame__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1641:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1641:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1642:1: ( rule__Frame__ItemAssignment_2_1 )
            {
             before(grammarAccess.getFrameAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1643:1: ( rule__Frame__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1643:2: rule__Frame__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3288);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1657:1: rule__Text__Group__0 : rule__Text__Group__0__Impl rule__Text__Group__1 ;
    public final void rule__Text__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1661:1: ( rule__Text__Group__0__Impl rule__Text__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1662:2: rule__Text__Group__0__Impl rule__Text__Group__1
            {
            pushFollow(FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03322);
            rule__Text__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03325);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1669:1: rule__Text__Group__0__Impl : ( 'Text' ) ;
    public final void rule__Text__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1673:1: ( ( 'Text' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1674:1: ( 'Text' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1674:1: ( 'Text' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1675:1: 'Text'
            {
             before(grammarAccess.getTextAccess().getTextKeyword_0()); 
            match(input,18,FOLLOW_18_in_rule__Text__Group__0__Impl3353); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1688:1: rule__Text__Group__1 : rule__Text__Group__1__Impl rule__Text__Group__2 ;
    public final void rule__Text__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1692:1: ( rule__Text__Group__1__Impl rule__Text__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1693:2: rule__Text__Group__1__Impl rule__Text__Group__2
            {
            pushFollow(FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13384);
            rule__Text__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13387);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1700:1: rule__Text__Group__1__Impl : ( () ) ;
    public final void rule__Text__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1704:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1705:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1705:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1706:1: ()
            {
             before(grammarAccess.getTextAccess().getTextAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1707:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1709:1: 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1719:1: rule__Text__Group__2 : rule__Text__Group__2__Impl ;
    public final void rule__Text__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1723:1: ( rule__Text__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1724:2: rule__Text__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23445);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1730:1: rule__Text__Group__2__Impl : ( ( rule__Text__Group_2__0 )? ) ;
    public final void rule__Text__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1734:1: ( ( ( rule__Text__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1735:1: ( ( rule__Text__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1735:1: ( ( rule__Text__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1736:1: ( rule__Text__Group_2__0 )?
            {
             before(grammarAccess.getTextAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1737:1: ( rule__Text__Group_2__0 )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==17) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1737:2: rule__Text__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3472);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1753:1: rule__Text__Group_2__0 : rule__Text__Group_2__0__Impl rule__Text__Group_2__1 ;
    public final void rule__Text__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1757:1: ( rule__Text__Group_2__0__Impl rule__Text__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1758:2: rule__Text__Group_2__0__Impl rule__Text__Group_2__1
            {
            pushFollow(FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03509);
            rule__Text__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03512);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1765:1: rule__Text__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Text__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1769:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1770:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1770:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1771:1: 'item='
            {
             before(grammarAccess.getTextAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Text__Group_2__0__Impl3540); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1784:1: rule__Text__Group_2__1 : rule__Text__Group_2__1__Impl ;
    public final void rule__Text__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1788:1: ( rule__Text__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1789:2: rule__Text__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13571);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1795:1: rule__Text__Group_2__1__Impl : ( ( rule__Text__ItemAssignment_2_1 ) ) ;
    public final void rule__Text__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1799:1: ( ( ( rule__Text__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1800:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1800:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1801:1: ( rule__Text__ItemAssignment_2_1 )
            {
             before(grammarAccess.getTextAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1802:1: ( rule__Text__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1802:2: rule__Text__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3598);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1816:1: rule__Group__Group__0 : rule__Group__Group__0__Impl rule__Group__Group__1 ;
    public final void rule__Group__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1820:1: ( rule__Group__Group__0__Impl rule__Group__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1821:2: rule__Group__Group__0__Impl rule__Group__Group__1
            {
            pushFollow(FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03632);
            rule__Group__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03635);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1828:1: rule__Group__Group__0__Impl : ( 'Group' ) ;
    public final void rule__Group__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1832:1: ( ( 'Group' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1833:1: ( 'Group' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1833:1: ( 'Group' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1834:1: 'Group'
            {
             before(grammarAccess.getGroupAccess().getGroupKeyword_0()); 
            match(input,19,FOLLOW_19_in_rule__Group__Group__0__Impl3663); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1847:1: rule__Group__Group__1 : rule__Group__Group__1__Impl ;
    public final void rule__Group__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1851:1: ( rule__Group__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1852:2: rule__Group__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13694);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1858:1: rule__Group__Group__1__Impl : ( ( rule__Group__Group_1__0 ) ) ;
    public final void rule__Group__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1862:1: ( ( ( rule__Group__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1863:1: ( ( rule__Group__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1863:1: ( ( rule__Group__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1864:1: ( rule__Group__Group_1__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1865:1: ( rule__Group__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1865:2: rule__Group__Group_1__0
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3721);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1879:1: rule__Group__Group_1__0 : rule__Group__Group_1__0__Impl rule__Group__Group_1__1 ;
    public final void rule__Group__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1883:1: ( rule__Group__Group_1__0__Impl rule__Group__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1884:2: rule__Group__Group_1__0__Impl rule__Group__Group_1__1
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03755);
            rule__Group__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03758);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1891:1: rule__Group__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Group__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1895:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1896:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1896:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1897:1: 'item='
            {
             before(grammarAccess.getGroupAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Group__Group_1__0__Impl3786); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1910:1: rule__Group__Group_1__1 : rule__Group__Group_1__1__Impl ;
    public final void rule__Group__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1914:1: ( rule__Group__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1915:2: rule__Group__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__13817);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1921:1: rule__Group__Group_1__1__Impl : ( ( rule__Group__ItemAssignment_1_1 ) ) ;
    public final void rule__Group__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1925:1: ( ( ( rule__Group__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1926:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1926:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1927:1: ( rule__Group__ItemAssignment_1_1 )
            {
             before(grammarAccess.getGroupAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1928:1: ( rule__Group__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1928:2: rule__Group__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl3844);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1942:1: rule__Image__Group__0 : rule__Image__Group__0__Impl rule__Image__Group__1 ;
    public final void rule__Image__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1946:1: ( rule__Image__Group__0__Impl rule__Image__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1947:2: rule__Image__Group__0__Impl rule__Image__Group__1
            {
            pushFollow(FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__03878);
            rule__Image__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__1_in_rule__Image__Group__03881);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1954:1: rule__Image__Group__0__Impl : ( 'Image' ) ;
    public final void rule__Image__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1958:1: ( ( 'Image' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1959:1: ( 'Image' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1959:1: ( 'Image' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1960:1: 'Image'
            {
             before(grammarAccess.getImageAccess().getImageKeyword_0()); 
            match(input,20,FOLLOW_20_in_rule__Image__Group__0__Impl3909); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1973:1: rule__Image__Group__1 : rule__Image__Group__1__Impl rule__Image__Group__2 ;
    public final void rule__Image__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1977:1: ( rule__Image__Group__1__Impl rule__Image__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1978:2: rule__Image__Group__1__Impl rule__Image__Group__2
            {
            pushFollow(FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__13940);
            rule__Image__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__2_in_rule__Image__Group__13943);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1985:1: rule__Image__Group__1__Impl : ( ( rule__Image__Group_1__0 )? ) ;
    public final void rule__Image__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1989:1: ( ( ( rule__Image__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1990:1: ( ( rule__Image__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1990:1: ( ( rule__Image__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1991:1: ( rule__Image__Group_1__0 )?
            {
             before(grammarAccess.getImageAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1992:1: ( rule__Image__Group_1__0 )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==17) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1992:2: rule__Image__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl3970);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2002:1: rule__Image__Group__2 : rule__Image__Group__2__Impl ;
    public final void rule__Image__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2006:1: ( rule__Image__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2007:2: rule__Image__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__24001);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2013:1: rule__Image__Group__2__Impl : ( ( rule__Image__Group_2__0 ) ) ;
    public final void rule__Image__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2017:1: ( ( ( rule__Image__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2018:1: ( ( rule__Image__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2018:1: ( ( rule__Image__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2019:1: ( rule__Image__Group_2__0 )
            {
             before(grammarAccess.getImageAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2020:1: ( rule__Image__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2020:2: rule__Image__Group_2__0
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl4028);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2036:1: rule__Image__Group_1__0 : rule__Image__Group_1__0__Impl rule__Image__Group_1__1 ;
    public final void rule__Image__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2040:1: ( rule__Image__Group_1__0__Impl rule__Image__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2041:2: rule__Image__Group_1__0__Impl rule__Image__Group_1__1
            {
            pushFollow(FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__04064);
            rule__Image__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__04067);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2048:1: rule__Image__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Image__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2052:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2053:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2053:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2054:1: 'item='
            {
             before(grammarAccess.getImageAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Image__Group_1__0__Impl4095); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2067:1: rule__Image__Group_1__1 : rule__Image__Group_1__1__Impl ;
    public final void rule__Image__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2071:1: ( rule__Image__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2072:2: rule__Image__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14126);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2078:1: rule__Image__Group_1__1__Impl : ( ( rule__Image__ItemAssignment_1_1 ) ) ;
    public final void rule__Image__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2082:1: ( ( ( rule__Image__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2083:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2083:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2084:1: ( rule__Image__ItemAssignment_1_1 )
            {
             before(grammarAccess.getImageAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2085:1: ( rule__Image__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2085:2: rule__Image__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4153);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2099:1: rule__Image__Group_2__0 : rule__Image__Group_2__0__Impl rule__Image__Group_2__1 ;
    public final void rule__Image__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2103:1: ( rule__Image__Group_2__0__Impl rule__Image__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2104:2: rule__Image__Group_2__0__Impl rule__Image__Group_2__1
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04187);
            rule__Image__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04190);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2111:1: rule__Image__Group_2__0__Impl : ( 'url=' ) ;
    public final void rule__Image__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2115:1: ( ( 'url=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2116:1: ( 'url=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2116:1: ( 'url=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2117:1: 'url='
            {
             before(grammarAccess.getImageAccess().getUrlKeyword_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Image__Group_2__0__Impl4218); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2130:1: rule__Image__Group_2__1 : rule__Image__Group_2__1__Impl ;
    public final void rule__Image__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2134:1: ( rule__Image__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2135:2: rule__Image__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14249);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2141:1: rule__Image__Group_2__1__Impl : ( ( rule__Image__UrlAssignment_2_1 ) ) ;
    public final void rule__Image__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2145:1: ( ( ( rule__Image__UrlAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2146:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2146:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2147:1: ( rule__Image__UrlAssignment_2_1 )
            {
             before(grammarAccess.getImageAccess().getUrlAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2148:1: ( rule__Image__UrlAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2148:2: rule__Image__UrlAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4276);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2162:1: rule__Switch__Group__0 : rule__Switch__Group__0__Impl rule__Switch__Group__1 ;
    public final void rule__Switch__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2166:1: ( rule__Switch__Group__0__Impl rule__Switch__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2167:2: rule__Switch__Group__0__Impl rule__Switch__Group__1
            {
            pushFollow(FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04310);
            rule__Switch__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04313);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2174:1: rule__Switch__Group__0__Impl : ( 'Switch' ) ;
    public final void rule__Switch__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2178:1: ( ( 'Switch' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2179:1: ( 'Switch' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2179:1: ( 'Switch' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2180:1: 'Switch'
            {
             before(grammarAccess.getSwitchAccess().getSwitchKeyword_0()); 
            match(input,22,FOLLOW_22_in_rule__Switch__Group__0__Impl4341); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2193:1: rule__Switch__Group__1 : rule__Switch__Group__1__Impl rule__Switch__Group__2 ;
    public final void rule__Switch__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2197:1: ( rule__Switch__Group__1__Impl rule__Switch__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2198:2: rule__Switch__Group__1__Impl rule__Switch__Group__2
            {
            pushFollow(FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14372);
            rule__Switch__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14375);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2205:1: rule__Switch__Group__1__Impl : ( ( rule__Switch__Group_1__0 ) ) ;
    public final void rule__Switch__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2209:1: ( ( ( rule__Switch__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2210:1: ( ( rule__Switch__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2210:1: ( ( rule__Switch__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2211:1: ( rule__Switch__Group_1__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2212:1: ( rule__Switch__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2212:2: rule__Switch__Group_1__0
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4402);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2222:1: rule__Switch__Group__2 : rule__Switch__Group__2__Impl ;
    public final void rule__Switch__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2226:1: ( rule__Switch__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2227:2: rule__Switch__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24432);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2233:1: rule__Switch__Group__2__Impl : ( ( rule__Switch__Group_2__0 )? ) ;
    public final void rule__Switch__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2237:1: ( ( ( rule__Switch__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2238:1: ( ( rule__Switch__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2238:1: ( ( rule__Switch__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2239:1: ( rule__Switch__Group_2__0 )?
            {
             before(grammarAccess.getSwitchAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2240:1: ( rule__Switch__Group_2__0 )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==23) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2240:2: rule__Switch__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4459);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2256:1: rule__Switch__Group_1__0 : rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 ;
    public final void rule__Switch__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2260:1: ( rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2261:2: rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04496);
            rule__Switch__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04499);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2268:1: rule__Switch__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Switch__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2272:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2273:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2273:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2274:1: 'item='
            {
             before(grammarAccess.getSwitchAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Switch__Group_1__0__Impl4527); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2287:1: rule__Switch__Group_1__1 : rule__Switch__Group_1__1__Impl ;
    public final void rule__Switch__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2291:1: ( rule__Switch__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2292:2: rule__Switch__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14558);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2298:1: rule__Switch__Group_1__1__Impl : ( ( rule__Switch__ItemAssignment_1_1 ) ) ;
    public final void rule__Switch__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2302:1: ( ( ( rule__Switch__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2303:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2303:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2304:1: ( rule__Switch__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSwitchAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2305:1: ( rule__Switch__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2305:2: rule__Switch__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4585);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2319:1: rule__Switch__Group_2__0 : rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 ;
    public final void rule__Switch__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2323:1: ( rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2324:2: rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04619);
            rule__Switch__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04622);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2331:1: rule__Switch__Group_2__0__Impl : ( 'buttonLabels=[' ) ;
    public final void rule__Switch__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2335:1: ( ( 'buttonLabels=[' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2336:1: ( 'buttonLabels=[' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2336:1: ( 'buttonLabels=[' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2337:1: 'buttonLabels=['
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0()); 
            match(input,23,FOLLOW_23_in_rule__Switch__Group_2__0__Impl4650); 
             after(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0()); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2350:1: rule__Switch__Group_2__1 : rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 ;
    public final void rule__Switch__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2354:1: ( rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2355:2: rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14681);
            rule__Switch__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14684);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2362:1: rule__Switch__Group_2__1__Impl : ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) ;
    public final void rule__Switch__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2366:1: ( ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2367:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2367:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2368:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2369:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2369:2: rule__Switch__ButtonLabelsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4711);
            rule__Switch__ButtonLabelsAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getSwitchAccess().getButtonLabelsAssignment_2_1()); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2379:1: rule__Switch__Group_2__2 : rule__Switch__Group_2__2__Impl ;
    public final void rule__Switch__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2383:1: ( rule__Switch__Group_2__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2384:2: rule__Switch__Group_2__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24741);
            rule__Switch__Group_2__2__Impl();
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2390:1: rule__Switch__Group_2__2__Impl : ( ']' ) ;
    public final void rule__Switch__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2394:1: ( ( ']' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2395:1: ( ']' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2395:1: ( ']' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2396:1: ']'
            {
             before(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_2()); 
            match(input,24,FOLLOW_24_in_rule__Switch__Group_2__2__Impl4769); 
             after(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_2()); 

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


    // $ANTLR start rule__Selection__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2415:1: rule__Selection__Group__0 : rule__Selection__Group__0__Impl rule__Selection__Group__1 ;
    public final void rule__Selection__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2419:1: ( rule__Selection__Group__0__Impl rule__Selection__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2420:2: rule__Selection__Group__0__Impl rule__Selection__Group__1
            {
            pushFollow(FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__04806);
            rule__Selection__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__04809);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2427:1: rule__Selection__Group__0__Impl : ( 'Selection' ) ;
    public final void rule__Selection__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2431:1: ( ( 'Selection' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2432:1: ( 'Selection' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2432:1: ( 'Selection' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2433:1: 'Selection'
            {
             before(grammarAccess.getSelectionAccess().getSelectionKeyword_0()); 
            match(input,25,FOLLOW_25_in_rule__Selection__Group__0__Impl4837); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2446:1: rule__Selection__Group__1 : rule__Selection__Group__1__Impl ;
    public final void rule__Selection__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2450:1: ( rule__Selection__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2451:2: rule__Selection__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__14868);
            rule__Selection__Group__1__Impl();
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2457:1: rule__Selection__Group__1__Impl : ( ( rule__Selection__Group_1__0 ) ) ;
    public final void rule__Selection__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2461:1: ( ( ( rule__Selection__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2462:1: ( ( rule__Selection__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2462:1: ( ( rule__Selection__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2463:1: ( rule__Selection__Group_1__0 )
            {
             before(grammarAccess.getSelectionAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2464:1: ( rule__Selection__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2464:2: rule__Selection__Group_1__0
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl4895);
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


    // $ANTLR start rule__Selection__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2478:1: rule__Selection__Group_1__0 : rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 ;
    public final void rule__Selection__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2482:1: ( rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2483:2: rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__04929);
            rule__Selection__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__04932);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2490:1: rule__Selection__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Selection__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2494:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2495:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2495:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2496:1: 'item='
            {
             before(grammarAccess.getSelectionAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Selection__Group_1__0__Impl4960); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2509:1: rule__Selection__Group_1__1 : rule__Selection__Group_1__1__Impl ;
    public final void rule__Selection__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2513:1: ( rule__Selection__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2514:2: rule__Selection__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__14991);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2520:1: rule__Selection__Group_1__1__Impl : ( ( rule__Selection__ItemAssignment_1_1 ) ) ;
    public final void rule__Selection__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2524:1: ( ( ( rule__Selection__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2525:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2525:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2526:1: ( rule__Selection__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSelectionAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2527:1: ( rule__Selection__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2527:2: rule__Selection__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl5018);
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


    // $ANTLR start rule__List__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2541:1: rule__List__Group__0 : rule__List__Group__0__Impl rule__List__Group__1 ;
    public final void rule__List__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2545:1: ( rule__List__Group__0__Impl rule__List__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2546:2: rule__List__Group__0__Impl rule__List__Group__1
            {
            pushFollow(FOLLOW_rule__List__Group__0__Impl_in_rule__List__Group__05052);
            rule__List__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group__1_in_rule__List__Group__05055);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2553:1: rule__List__Group__0__Impl : ( 'List' ) ;
    public final void rule__List__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2557:1: ( ( 'List' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2558:1: ( 'List' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2558:1: ( 'List' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2559:1: 'List'
            {
             before(grammarAccess.getListAccess().getListKeyword_0()); 
            match(input,26,FOLLOW_26_in_rule__List__Group__0__Impl5083); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2572:1: rule__List__Group__1 : rule__List__Group__1__Impl rule__List__Group__2 ;
    public final void rule__List__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2576:1: ( rule__List__Group__1__Impl rule__List__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2577:2: rule__List__Group__1__Impl rule__List__Group__2
            {
            pushFollow(FOLLOW_rule__List__Group__1__Impl_in_rule__List__Group__15114);
            rule__List__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group__2_in_rule__List__Group__15117);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2584:1: rule__List__Group__1__Impl : ( ( rule__List__Group_1__0 ) ) ;
    public final void rule__List__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2588:1: ( ( ( rule__List__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:1: ( ( rule__List__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:1: ( ( rule__List__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2590:1: ( rule__List__Group_1__0 )
            {
             before(grammarAccess.getListAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2591:1: ( rule__List__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2591:2: rule__List__Group_1__0
            {
            pushFollow(FOLLOW_rule__List__Group_1__0_in_rule__List__Group__1__Impl5144);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2601:1: rule__List__Group__2 : rule__List__Group__2__Impl ;
    public final void rule__List__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2605:1: ( rule__List__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2606:2: rule__List__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__List__Group__2__Impl_in_rule__List__Group__25174);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2612:1: rule__List__Group__2__Impl : ( ( rule__List__Group_2__0 ) ) ;
    public final void rule__List__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2616:1: ( ( ( rule__List__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2617:1: ( ( rule__List__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2617:1: ( ( rule__List__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2618:1: ( rule__List__Group_2__0 )
            {
             before(grammarAccess.getListAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2619:1: ( rule__List__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2619:2: rule__List__Group_2__0
            {
            pushFollow(FOLLOW_rule__List__Group_2__0_in_rule__List__Group__2__Impl5201);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2635:1: rule__List__Group_1__0 : rule__List__Group_1__0__Impl rule__List__Group_1__1 ;
    public final void rule__List__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2639:1: ( rule__List__Group_1__0__Impl rule__List__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2640:2: rule__List__Group_1__0__Impl rule__List__Group_1__1
            {
            pushFollow(FOLLOW_rule__List__Group_1__0__Impl_in_rule__List__Group_1__05237);
            rule__List__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group_1__1_in_rule__List__Group_1__05240);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2647:1: rule__List__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__List__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2651:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2652:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2652:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2653:1: 'item='
            {
             before(grammarAccess.getListAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__List__Group_1__0__Impl5268); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2666:1: rule__List__Group_1__1 : rule__List__Group_1__1__Impl ;
    public final void rule__List__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2670:1: ( rule__List__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2671:2: rule__List__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__List__Group_1__1__Impl_in_rule__List__Group_1__15299);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2677:1: rule__List__Group_1__1__Impl : ( ( rule__List__ItemAssignment_1_1 ) ) ;
    public final void rule__List__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2681:1: ( ( ( rule__List__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2682:1: ( ( rule__List__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2682:1: ( ( rule__List__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2683:1: ( rule__List__ItemAssignment_1_1 )
            {
             before(grammarAccess.getListAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2684:1: ( rule__List__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2684:2: rule__List__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__List__ItemAssignment_1_1_in_rule__List__Group_1__1__Impl5326);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2698:1: rule__List__Group_2__0 : rule__List__Group_2__0__Impl rule__List__Group_2__1 ;
    public final void rule__List__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2702:1: ( rule__List__Group_2__0__Impl rule__List__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2703:2: rule__List__Group_2__0__Impl rule__List__Group_2__1
            {
            pushFollow(FOLLOW_rule__List__Group_2__0__Impl_in_rule__List__Group_2__05360);
            rule__List__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__List__Group_2__1_in_rule__List__Group_2__05363);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2710:1: rule__List__Group_2__0__Impl : ( 'separator=' ) ;
    public final void rule__List__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2714:1: ( ( 'separator=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2715:1: ( 'separator=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2715:1: ( 'separator=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2716:1: 'separator='
            {
             before(grammarAccess.getListAccess().getSeparatorKeyword_2_0()); 
            match(input,27,FOLLOW_27_in_rule__List__Group_2__0__Impl5391); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2729:1: rule__List__Group_2__1 : rule__List__Group_2__1__Impl ;
    public final void rule__List__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2733:1: ( rule__List__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2734:2: rule__List__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__List__Group_2__1__Impl_in_rule__List__Group_2__15422);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2740:1: rule__List__Group_2__1__Impl : ( ( rule__List__SeparatorAssignment_2_1 ) ) ;
    public final void rule__List__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2744:1: ( ( ( rule__List__SeparatorAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2745:1: ( ( rule__List__SeparatorAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2745:1: ( ( rule__List__SeparatorAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2746:1: ( rule__List__SeparatorAssignment_2_1 )
            {
             before(grammarAccess.getListAccess().getSeparatorAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2747:1: ( rule__List__SeparatorAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2747:2: rule__List__SeparatorAssignment_2_1
            {
            pushFollow(FOLLOW_rule__List__SeparatorAssignment_2_1_in_rule__List__Group_2__1__Impl5449);
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


    // $ANTLR start rule__Sitemap__NameAssignment_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2762:1: rule__Sitemap__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Sitemap__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2766:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2767:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2767:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2768:1: RULE_ID
            {
             before(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_05488); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2777:1: rule__Sitemap__LabelAssignment_1_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2781:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2782:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2782:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2783:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_15519); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2792:1: rule__Sitemap__IconAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2796:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2797:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2797:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2798:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_15550); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2807:1: rule__Sitemap__ChildrenAssignment_4 : ( ruleWidget ) ;
    public final void rule__Sitemap__ChildrenAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2811:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2812:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2812:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2813:1: ruleWidget
            {
             before(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_45581);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2822:1: rule__Widget__LabelAssignment_1_1_1 : ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) ;
    public final void rule__Widget__LabelAssignment_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2826:1: ( ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2827:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2827:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2828:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2829:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2829:2: rule__Widget__LabelAlternatives_1_1_1_0
            {
            pushFollow(FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_15612);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2838:1: rule__Widget__IconAssignment_1_2_1 : ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) ;
    public final void rule__Widget__IconAssignment_1_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2842:1: ( ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2843:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2843:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2844:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getIconAlternatives_1_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2845:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2845:2: rule__Widget__IconAlternatives_1_2_1_0
            {
            pushFollow(FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_15645);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2854:1: rule__LinkableWidget__LabelAssignment_1_1 : ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) ;
    public final void rule__LinkableWidget__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2858:1: ( ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2859:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2859:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2860:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAlternatives_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2861:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2861:2: rule__LinkableWidget__LabelAlternatives_1_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_15678);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2870:1: rule__LinkableWidget__IconAssignment_2_1 : ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) ;
    public final void rule__LinkableWidget__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2874:1: ( ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2875:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2875:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2876:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAlternatives_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2877:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2877:2: rule__LinkableWidget__IconAlternatives_2_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_15711);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2886:1: rule__LinkableWidget__ChildrenAssignment_3_1 : ( ruleWidget ) ;
    public final void rule__LinkableWidget__ChildrenAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2890:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2891:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2891:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2892:1: ruleWidget
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_15744);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2901:1: rule__Frame__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Frame__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2905:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2906:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2906:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2907:1: RULE_ID
            {
             before(grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_15775); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2916:1: rule__Text__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Text__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2920:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2921:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2921:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2922:1: RULE_ID
            {
             before(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_15806); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2931:1: rule__Group__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Group__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2935:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2936:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2936:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2937:1: RULE_ID
            {
             before(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_15837); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2946:1: rule__Image__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Image__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2950:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2951:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2951:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2952:1: RULE_ID
            {
             before(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_15868); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2961:1: rule__Image__UrlAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Image__UrlAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2965:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2966:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2966:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2967:1: RULE_STRING
            {
             before(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_15899); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2976:1: rule__Switch__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Switch__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2980:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2981:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2981:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2982:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_15930); 
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


    // $ANTLR start rule__Switch__ButtonLabelsAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2991:1: rule__Switch__ButtonLabelsAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Switch__ButtonLabelsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2995:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2996:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2996:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2997:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_15961); 
             after(grammarAccess.getSwitchAccess().getButtonLabelsIDTerminalRuleCall_2_1_0()); 

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
    // $ANTLR end rule__Switch__ButtonLabelsAssignment_2_1


    // $ANTLR start rule__Selection__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3006:1: rule__Selection__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Selection__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3010:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3011:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3011:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3012:1: RULE_ID
            {
             before(grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_15992); 
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


    // $ANTLR start rule__List__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3021:1: rule__List__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__List__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3025:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3026:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3026:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3027:1: RULE_ID
            {
             before(grammarAccess.getListAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__List__ItemAssignment_1_16023); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3036:1: rule__List__SeparatorAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__List__SeparatorAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3040:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3041:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3041:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:3042:1: RULE_STRING
            {
             before(grammarAccess.getListAccess().getSeparatorSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__List__SeparatorAssignment_2_16054); 
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
    public static final BitSet FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleList_in_rule__Widget__Alternatives_1_0814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_0944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_0961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_0978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_0995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_01027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_01044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_01076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01123 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01245 = new BitSet(new long[]{0x000000000000D000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11305 = new BitSet(new long[]{0x0000000000009000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21366 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31427 = new BitSet(new long[]{0x00000000065D0000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Sitemap__Group__3__Impl1458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41489 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1521 = new BitSet(new long[]{0x00000000065D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1533 = new BitSet(new long[]{0x00000000065D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Sitemap__Group__5__Impl1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01637 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01760 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01883 = new BitSet(new long[]{0x000000000000C002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl1913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11943 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__11946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl1973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__22004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl2031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__02068 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__02071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02191 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02314 = new BitSet(new long[]{0x000000000000D002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12374 = new BitSet(new long[]{0x0000000000009002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22435 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02562 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02685 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__02808 = new BitSet(new long[]{0x00000000065D0000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__02811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl2839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__12870 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__12873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2902 = new BitSet(new long[]{0x00000000065D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2914 = new BitSet(new long[]{0x00000000065D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__22947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl2975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__03012 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__03015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Frame__Group__0__Impl3043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__13074 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03199 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Frame__Group_2__0__Impl3230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03322 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Text__Group__0__Impl3353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13384 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03509 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03512 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Text__Group_2__0__Impl3540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03632 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Group__Group__0__Impl3663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03755 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Group__Group_1__0__Impl3786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__13817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl3844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__03878 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_rule__Image__Group__1_in_rule__Image__Group__03881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Image__Group__0__Impl3909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__13940 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__Image__Group__2_in_rule__Image__Group__13943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl3970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__24001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl4028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__04064 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__04067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Image__Group_1__0__Impl4095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04187 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Image__Group_2__0__Impl4218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04310 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Switch__Group__0__Impl4341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14372 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04496 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Switch__Group_1__0__Impl4527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04619 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Switch__Group_2__0__Impl4650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14681 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Switch__Group_2__2__Impl4769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__04806 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__04809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Selection__Group__0__Impl4837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__14868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl4895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__04929 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__04932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Selection__Group_1__0__Impl4960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__14991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl5018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__0__Impl_in_rule__List__Group__05052 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__List__Group__1_in_rule__List__Group__05055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__List__Group__0__Impl5083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__1__Impl_in_rule__List__Group__15114 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_rule__List__Group__2_in_rule__List__Group__15117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__0_in_rule__List__Group__1__Impl5144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group__2__Impl_in_rule__List__Group__25174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__0_in_rule__List__Group__2__Impl5201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__0__Impl_in_rule__List__Group_1__05237 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__List__Group_1__1_in_rule__List__Group_1__05240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__List__Group_1__0__Impl5268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_1__1__Impl_in_rule__List__Group_1__15299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__ItemAssignment_1_1_in_rule__List__Group_1__1__Impl5326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__0__Impl_in_rule__List__Group_2__05360 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__List__Group_2__1_in_rule__List__Group_2__05363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__List__Group_2__0__Impl5391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__Group_2__1__Impl_in_rule__List__Group_2__15422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__List__SeparatorAssignment_2_1_in_rule__List__Group_2__1__Impl5449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_05488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_15519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_15550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_45581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_15612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_15645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_15678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_15711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_15744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_15775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_15806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_15837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_15868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_15899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_15930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_15961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_15992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__List__ItemAssignment_1_16023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__List__SeparatorAssignment_2_16054 = new BitSet(new long[]{0x0000000000000002L});

}