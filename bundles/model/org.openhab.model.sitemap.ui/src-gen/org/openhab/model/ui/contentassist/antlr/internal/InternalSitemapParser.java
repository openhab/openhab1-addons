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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'{'", "'}'", "'label='", "'icon='", "'Frame'", "'item='", "'Text'", "'Group'", "'Image'", "'url='", "'Switch'", "'buttonLabels=['", "']'", "'Selection'"
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


    // $ANTLR start rule__Widget__Alternatives
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:341:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );
    public final void rule__Widget__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:345:1: ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==16||(LA1_0>=18 && LA1_0<=20)) ) {
                alt1=1;
            }
            else if ( (LA1_0==22||LA1_0==25) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("341:1: rule__Widget__Alternatives : ( ( ruleLinkableWidget ) | ( ( rule__Widget__Group_1__0 ) ) );", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:346:1: ( ruleLinkableWidget )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:346:1: ( ruleLinkableWidget )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:347:1: ruleLinkableWidget
                    {
                     before(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives670);
                    ruleLinkableWidget();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getLinkableWidgetParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:352:6: ( ( rule__Widget__Group_1__0 ) )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:352:6: ( ( rule__Widget__Group_1__0 ) )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:353:1: ( rule__Widget__Group_1__0 )
                    {
                     before(grammarAccess.getWidgetAccess().getGroup_1()); 
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:354:1: ( rule__Widget__Group_1__0 )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:354:2: rule__Widget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives687);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:363:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) );
    public final void rule__Widget__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:367:1: ( ( ruleSwitch ) | ( ruleSelection ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==22) ) {
                alt2=1;
            }
            else if ( (LA2_0==25) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("363:1: rule__Widget__Alternatives_1_0 : ( ( ruleSwitch ) | ( ruleSelection ) );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:368:1: ( ruleSwitch )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:368:1: ( ruleSwitch )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:369:1: ruleSwitch
                    {
                     before(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 
                    pushFollow(FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0720);
                    ruleSwitch();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:374:6: ( ruleSelection )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:374:6: ( ruleSelection )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:375:1: ruleSelection
                    {
                     before(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 
                    pushFollow(FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0737);
                    ruleSelection();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSelectionParserRuleCall_1_0_1()); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:385:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__LabelAlternatives_1_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:389:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("385:1: rule__Widget__LabelAlternatives_1_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:390:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:390:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:391:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0769); 
                     after(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:396:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:396:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:397:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0786); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:407:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__IconAlternatives_1_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:411:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("407:1: rule__Widget__IconAlternatives_1_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:412:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:412:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:413:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0818); 
                     after(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_1_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:418:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:418:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:419:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_1_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0835); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:429:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );
    public final void rule__LinkableWidget__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:433:1: ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) )
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
                    new NoViableAltException("429:1: rule__LinkableWidget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleFrame ) );", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:434:1: ( ruleText )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:434:1: ( ruleText )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:435:1: ruleText
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_0867);
                    ruleText();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getTextParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:440:6: ( ruleGroup )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:440:6: ( ruleGroup )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:441:1: ruleGroup
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_0884);
                    ruleGroup();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getGroupParserRuleCall_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:446:6: ( ruleImage )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:446:6: ( ruleImage )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:447:1: ruleImage
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 
                    pushFollow(FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_0901);
                    ruleImage();
                    _fsp--;

                     after(grammarAccess.getLinkableWidgetAccess().getImageParserRuleCall_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:6: ( ruleFrame )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:6: ( ruleFrame )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:453:1: ruleFrame
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getFrameParserRuleCall_0_3()); 
                    pushFollow(FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_0918);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:463:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__LabelAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:467:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("463:1: rule__LinkableWidget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:468:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:468:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:469:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_0950); 
                     after(grammarAccess.getLinkableWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:474:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:475:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_0967); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:485:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__LinkableWidget__IconAlternatives_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:489:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("485:1: rule__LinkableWidget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:490:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:490:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:491:1: RULE_ID
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_0999); 
                     after(grammarAccess.getLinkableWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:496:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:496:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:497:1: RULE_STRING
                    {
                     before(grammarAccess.getLinkableWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01016); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:509:1: rule__SitemapModel__Group__0 : rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 ;
    public final void rule__SitemapModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:513:1: ( rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:514:2: rule__SitemapModel__Group__0__Impl rule__SitemapModel__Group__1
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01046);
            rule__SitemapModel__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01049);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:521:1: rule__SitemapModel__Group__0__Impl : ( 'sitemap' ) ;
    public final void rule__SitemapModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:525:1: ( ( 'sitemap' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:526:1: ( 'sitemap' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:526:1: ( 'sitemap' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:527:1: 'sitemap'
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapKeyword_0()); 
            match(input,11,FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1077); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:540:1: rule__SitemapModel__Group__1 : rule__SitemapModel__Group__1__Impl ;
    public final void rule__SitemapModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:544:1: ( rule__SitemapModel__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:545:2: rule__SitemapModel__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11108);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:551:1: rule__SitemapModel__Group__1__Impl : ( ruleSitemap ) ;
    public final void rule__SitemapModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:555:1: ( ( ruleSitemap ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:556:1: ( ruleSitemap )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:556:1: ( ruleSitemap )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:557:1: ruleSitemap
            {
             before(grammarAccess.getSitemapModelAccess().getSitemapParserRuleCall_1()); 
            pushFollow(FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1135);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:572:1: rule__Sitemap__Group__0 : rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 ;
    public final void rule__Sitemap__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:576:1: ( rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:577:2: rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01168);
            rule__Sitemap__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01171);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:584:1: rule__Sitemap__Group__0__Impl : ( ( rule__Sitemap__NameAssignment_0 ) ) ;
    public final void rule__Sitemap__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:588:1: ( ( ( rule__Sitemap__NameAssignment_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:589:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:589:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:590:1: ( rule__Sitemap__NameAssignment_0 )
            {
             before(grammarAccess.getSitemapAccess().getNameAssignment_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:591:1: ( rule__Sitemap__NameAssignment_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:591:2: rule__Sitemap__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1198);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:601:1: rule__Sitemap__Group__1 : rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 ;
    public final void rule__Sitemap__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:605:1: ( rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:606:2: rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11228);
            rule__Sitemap__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11231);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:613:1: rule__Sitemap__Group__1__Impl : ( ( rule__Sitemap__Group_1__0 )? ) ;
    public final void rule__Sitemap__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:617:1: ( ( ( rule__Sitemap__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:618:1: ( ( rule__Sitemap__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:618:1: ( ( rule__Sitemap__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:619:1: ( rule__Sitemap__Group_1__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:620:1: ( rule__Sitemap__Group_1__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==14) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:620:2: rule__Sitemap__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1258);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:630:1: rule__Sitemap__Group__2 : rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 ;
    public final void rule__Sitemap__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:634:1: ( rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:635:2: rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21289);
            rule__Sitemap__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21292);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:642:1: rule__Sitemap__Group__2__Impl : ( ( rule__Sitemap__Group_2__0 )? ) ;
    public final void rule__Sitemap__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:646:1: ( ( ( rule__Sitemap__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:647:1: ( ( rule__Sitemap__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:647:1: ( ( rule__Sitemap__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:648:1: ( rule__Sitemap__Group_2__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:649:1: ( rule__Sitemap__Group_2__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==15) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:649:2: rule__Sitemap__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1319);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:659:1: rule__Sitemap__Group__3 : rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 ;
    public final void rule__Sitemap__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:663:1: ( rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:664:2: rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31350);
            rule__Sitemap__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31353);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:671:1: rule__Sitemap__Group__3__Impl : ( '{' ) ;
    public final void rule__Sitemap__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:675:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:676:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:676:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:677:1: '{'
            {
             before(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_12_in_rule__Sitemap__Group__3__Impl1381); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:690:1: rule__Sitemap__Group__4 : rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 ;
    public final void rule__Sitemap__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:694:1: ( rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:695:2: rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41412);
            rule__Sitemap__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41415);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:702:1: rule__Sitemap__Group__4__Impl : ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) ;
    public final void rule__Sitemap__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:706:1: ( ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:707:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:707:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:708:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:708:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:709:1: ( rule__Sitemap__ChildrenAssignment_4 )
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:1: ( rule__Sitemap__ChildrenAssignment_4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:2: rule__Sitemap__ChildrenAssignment_4
            {
            pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1444);
            rule__Sitemap__ChildrenAssignment_4();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:713:1: ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:714:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:715:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==16||(LA10_0>=18 && LA10_0<=20)||LA10_0==22||LA10_0==25) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:715:2: rule__Sitemap__ChildrenAssignment_4
            	    {
            	    pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1456);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:726:1: rule__Sitemap__Group__5 : rule__Sitemap__Group__5__Impl ;
    public final void rule__Sitemap__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:730:1: ( rule__Sitemap__Group__5__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:731:2: rule__Sitemap__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51489);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:737:1: rule__Sitemap__Group__5__Impl : ( '}' ) ;
    public final void rule__Sitemap__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:741:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:743:1: '}'
            {
             before(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5()); 
            match(input,13,FOLLOW_13_in_rule__Sitemap__Group__5__Impl1517); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:768:1: rule__Sitemap__Group_1__0 : rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 ;
    public final void rule__Sitemap__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:772:1: ( rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:773:2: rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01560);
            rule__Sitemap__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01563);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:780:1: rule__Sitemap__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__Sitemap__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:784:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:785:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:785:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:786:1: 'label='
            {
             before(grammarAccess.getSitemapAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1591); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:799:1: rule__Sitemap__Group_1__1 : rule__Sitemap__Group_1__1__Impl ;
    public final void rule__Sitemap__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:803:1: ( rule__Sitemap__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:804:2: rule__Sitemap__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11622);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:810:1: rule__Sitemap__Group_1__1__Impl : ( ( rule__Sitemap__LabelAssignment_1_1 ) ) ;
    public final void rule__Sitemap__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:814:1: ( ( ( rule__Sitemap__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:815:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:815:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:816:1: ( rule__Sitemap__LabelAssignment_1_1 )
            {
             before(grammarAccess.getSitemapAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:817:1: ( rule__Sitemap__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:817:2: rule__Sitemap__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1649);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:831:1: rule__Sitemap__Group_2__0 : rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 ;
    public final void rule__Sitemap__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:835:1: ( rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:836:2: rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01683);
            rule__Sitemap__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01686);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:843:1: rule__Sitemap__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Sitemap__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:847:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:848:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:848:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:849:1: 'icon='
            {
             before(grammarAccess.getSitemapAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1714); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:862:1: rule__Sitemap__Group_2__1 : rule__Sitemap__Group_2__1__Impl ;
    public final void rule__Sitemap__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:866:1: ( rule__Sitemap__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:867:2: rule__Sitemap__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11745);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:873:1: rule__Sitemap__Group_2__1__Impl : ( ( rule__Sitemap__IconAssignment_2_1 ) ) ;
    public final void rule__Sitemap__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:877:1: ( ( ( rule__Sitemap__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:878:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:878:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:879:1: ( rule__Sitemap__IconAssignment_2_1 )
            {
             before(grammarAccess.getSitemapAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:880:1: ( rule__Sitemap__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:880:2: rule__Sitemap__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1772);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:894:1: rule__Widget__Group_1__0 : rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 ;
    public final void rule__Widget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:898:1: ( rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:899:2: rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01806);
            rule__Widget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01809);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:906:1: rule__Widget__Group_1__0__Impl : ( ( rule__Widget__Alternatives_1_0 ) ) ;
    public final void rule__Widget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:910:1: ( ( ( rule__Widget__Alternatives_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:911:1: ( ( rule__Widget__Alternatives_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:911:1: ( ( rule__Widget__Alternatives_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:912:1: ( rule__Widget__Alternatives_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getAlternatives_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:913:1: ( rule__Widget__Alternatives_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:913:2: rule__Widget__Alternatives_1_0
            {
            pushFollow(FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl1836);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:923:1: rule__Widget__Group_1__1 : rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 ;
    public final void rule__Widget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:927:1: ( rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:928:2: rule__Widget__Group_1__1__Impl rule__Widget__Group_1__2
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11866);
            rule__Widget__Group_1__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__11869);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:935:1: rule__Widget__Group_1__1__Impl : ( ( rule__Widget__Group_1_1__0 )? ) ;
    public final void rule__Widget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:939:1: ( ( ( rule__Widget__Group_1_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:940:1: ( ( rule__Widget__Group_1_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:940:1: ( ( rule__Widget__Group_1_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:941:1: ( rule__Widget__Group_1_1__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:942:1: ( rule__Widget__Group_1_1__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==14) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:942:2: rule__Widget__Group_1_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl1896);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:952:1: rule__Widget__Group_1__2 : rule__Widget__Group_1__2__Impl ;
    public final void rule__Widget__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:956:1: ( rule__Widget__Group_1__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:957:2: rule__Widget__Group_1__2__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__21927);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:963:1: rule__Widget__Group_1__2__Impl : ( ( rule__Widget__Group_1_2__0 )? ) ;
    public final void rule__Widget__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:967:1: ( ( ( rule__Widget__Group_1_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:968:1: ( ( rule__Widget__Group_1_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:968:1: ( ( rule__Widget__Group_1_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:969:1: ( rule__Widget__Group_1_2__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:970:1: ( rule__Widget__Group_1_2__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==15) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:970:2: rule__Widget__Group_1_2__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl1954);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:986:1: rule__Widget__Group_1_1__0 : rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 ;
    public final void rule__Widget__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:990:1: ( rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:991:2: rule__Widget__Group_1_1__0__Impl rule__Widget__Group_1_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__01991);
            rule__Widget__Group_1_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__01994);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:998:1: rule__Widget__Group_1_1__0__Impl : ( 'label=' ) ;
    public final void rule__Widget__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1002:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1003:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1003:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1004:1: 'label='
            {
             before(grammarAccess.getWidgetAccess().getLabelKeyword_1_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2022); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1017:1: rule__Widget__Group_1_1__1 : rule__Widget__Group_1_1__1__Impl ;
    public final void rule__Widget__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1021:1: ( rule__Widget__Group_1_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1022:2: rule__Widget__Group_1_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12053);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1028:1: rule__Widget__Group_1_1__1__Impl : ( ( rule__Widget__LabelAssignment_1_1_1 ) ) ;
    public final void rule__Widget__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1032:1: ( ( ( rule__Widget__LabelAssignment_1_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1033:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1033:1: ( ( rule__Widget__LabelAssignment_1_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1034:1: ( rule__Widget__LabelAssignment_1_1_1 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAssignment_1_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1035:1: ( rule__Widget__LabelAssignment_1_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1035:2: rule__Widget__LabelAssignment_1_1_1
            {
            pushFollow(FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2080);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1049:1: rule__Widget__Group_1_2__0 : rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 ;
    public final void rule__Widget__Group_1_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1053:1: ( rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1054:2: rule__Widget__Group_1_2__0__Impl rule__Widget__Group_1_2__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02114);
            rule__Widget__Group_1_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02117);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1061:1: rule__Widget__Group_1_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Widget__Group_1_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1065:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1066:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1066:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1067:1: 'icon='
            {
             before(grammarAccess.getWidgetAccess().getIconKeyword_1_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2145); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1080:1: rule__Widget__Group_1_2__1 : rule__Widget__Group_1_2__1__Impl ;
    public final void rule__Widget__Group_1_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1084:1: ( rule__Widget__Group_1_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1085:2: rule__Widget__Group_1_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12176);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1091:1: rule__Widget__Group_1_2__1__Impl : ( ( rule__Widget__IconAssignment_1_2_1 ) ) ;
    public final void rule__Widget__Group_1_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1095:1: ( ( ( rule__Widget__IconAssignment_1_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1096:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1096:1: ( ( rule__Widget__IconAssignment_1_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1097:1: ( rule__Widget__IconAssignment_1_2_1 )
            {
             before(grammarAccess.getWidgetAccess().getIconAssignment_1_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1098:1: ( rule__Widget__IconAssignment_1_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1098:2: rule__Widget__IconAssignment_1_2_1
            {
            pushFollow(FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2203);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1112:1: rule__LinkableWidget__Group__0 : rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 ;
    public final void rule__LinkableWidget__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1116:1: ( rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1117:2: rule__LinkableWidget__Group__0__Impl rule__LinkableWidget__Group__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02237);
            rule__LinkableWidget__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02240);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1124:1: rule__LinkableWidget__Group__0__Impl : ( ( rule__LinkableWidget__Alternatives_0 ) ) ;
    public final void rule__LinkableWidget__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1128:1: ( ( ( rule__LinkableWidget__Alternatives_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1129:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1129:1: ( ( rule__LinkableWidget__Alternatives_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1130:1: ( rule__LinkableWidget__Alternatives_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getAlternatives_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1131:1: ( rule__LinkableWidget__Alternatives_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1131:2: rule__LinkableWidget__Alternatives_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2267);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1141:1: rule__LinkableWidget__Group__1 : rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 ;
    public final void rule__LinkableWidget__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1145:1: ( rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1146:2: rule__LinkableWidget__Group__1__Impl rule__LinkableWidget__Group__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12297);
            rule__LinkableWidget__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12300);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1153:1: rule__LinkableWidget__Group__1__Impl : ( ( rule__LinkableWidget__Group_1__0 )? ) ;
    public final void rule__LinkableWidget__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1157:1: ( ( ( rule__LinkableWidget__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1158:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1158:1: ( ( rule__LinkableWidget__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1159:1: ( rule__LinkableWidget__Group_1__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1160:1: ( rule__LinkableWidget__Group_1__0 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==14) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1160:2: rule__LinkableWidget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2327);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1170:1: rule__LinkableWidget__Group__2 : rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 ;
    public final void rule__LinkableWidget__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1174:1: ( rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1175:2: rule__LinkableWidget__Group__2__Impl rule__LinkableWidget__Group__3
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22358);
            rule__LinkableWidget__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22361);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1182:1: rule__LinkableWidget__Group__2__Impl : ( ( rule__LinkableWidget__Group_2__0 )? ) ;
    public final void rule__LinkableWidget__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1186:1: ( ( ( rule__LinkableWidget__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1187:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1187:1: ( ( rule__LinkableWidget__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1188:1: ( rule__LinkableWidget__Group_2__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1189:1: ( rule__LinkableWidget__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==15) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1189:2: rule__LinkableWidget__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2388);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1199:1: rule__LinkableWidget__Group__3 : rule__LinkableWidget__Group__3__Impl ;
    public final void rule__LinkableWidget__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1203:1: ( rule__LinkableWidget__Group__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1204:2: rule__LinkableWidget__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32419);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1210:1: rule__LinkableWidget__Group__3__Impl : ( ( rule__LinkableWidget__Group_3__0 )? ) ;
    public final void rule__LinkableWidget__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1214:1: ( ( ( rule__LinkableWidget__Group_3__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1215:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1215:1: ( ( rule__LinkableWidget__Group_3__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1216:1: ( rule__LinkableWidget__Group_3__0 )?
            {
             before(grammarAccess.getLinkableWidgetAccess().getGroup_3()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1217:1: ( rule__LinkableWidget__Group_3__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==12) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1217:2: rule__LinkableWidget__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2446);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1235:1: rule__LinkableWidget__Group_1__0 : rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 ;
    public final void rule__LinkableWidget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1239:1: ( rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1240:2: rule__LinkableWidget__Group_1__0__Impl rule__LinkableWidget__Group_1__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02485);
            rule__LinkableWidget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02488);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1247:1: rule__LinkableWidget__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__LinkableWidget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1251:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1252:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1252:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1253:1: 'label='
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2516); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1266:1: rule__LinkableWidget__Group_1__1 : rule__LinkableWidget__Group_1__1__Impl ;
    public final void rule__LinkableWidget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1270:1: ( rule__LinkableWidget__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1271:2: rule__LinkableWidget__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12547);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1277:1: rule__LinkableWidget__Group_1__1__Impl : ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) ;
    public final void rule__LinkableWidget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1281:1: ( ( ( rule__LinkableWidget__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1282:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1282:1: ( ( rule__LinkableWidget__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1283:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1284:1: ( rule__LinkableWidget__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1284:2: rule__LinkableWidget__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2574);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1298:1: rule__LinkableWidget__Group_2__0 : rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 ;
    public final void rule__LinkableWidget__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1302:1: ( rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1303:2: rule__LinkableWidget__Group_2__0__Impl rule__LinkableWidget__Group_2__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02608);
            rule__LinkableWidget__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02611);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1310:1: rule__LinkableWidget__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__LinkableWidget__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1314:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: 'icon='
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2639); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1329:1: rule__LinkableWidget__Group_2__1 : rule__LinkableWidget__Group_2__1__Impl ;
    public final void rule__LinkableWidget__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1333:1: ( rule__LinkableWidget__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1334:2: rule__LinkableWidget__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12670);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1340:1: rule__LinkableWidget__Group_2__1__Impl : ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) ;
    public final void rule__LinkableWidget__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1344:1: ( ( ( rule__LinkableWidget__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1345:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1345:1: ( ( rule__LinkableWidget__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1346:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1347:1: ( rule__LinkableWidget__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1347:2: rule__LinkableWidget__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2697);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1361:1: rule__LinkableWidget__Group_3__0 : rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 ;
    public final void rule__LinkableWidget__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1365:1: ( rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1366:2: rule__LinkableWidget__Group_3__0__Impl rule__LinkableWidget__Group_3__1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__02731);
            rule__LinkableWidget__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__02734);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1373:1: rule__LinkableWidget__Group_3__0__Impl : ( '{' ) ;
    public final void rule__LinkableWidget__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1377:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1378:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1378:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: '{'
            {
             before(grammarAccess.getLinkableWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 
            match(input,12,FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl2762); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1392:1: rule__LinkableWidget__Group_3__1 : rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 ;
    public final void rule__LinkableWidget__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1396:1: ( rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1397:2: rule__LinkableWidget__Group_3__1__Impl rule__LinkableWidget__Group_3__2
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__12793);
            rule__LinkableWidget__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__12796);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1404:1: rule__LinkableWidget__Group_3__1__Impl : ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) ;
    public final void rule__LinkableWidget__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1408:1: ( ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1409:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1409:1: ( ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1410:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) ) ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1410:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1411:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1412:2: rule__LinkableWidget__ChildrenAssignment_3_1
            {
            pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2825);
            rule__LinkableWidget__ChildrenAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1415:1: ( ( rule__LinkableWidget__ChildrenAssignment_3_1 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1416:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1417:1: ( rule__LinkableWidget__ChildrenAssignment_3_1 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==16||(LA16_0>=18 && LA16_0<=20)||LA16_0==22||LA16_0==25) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1417:2: rule__LinkableWidget__ChildrenAssignment_3_1
            	    {
            	    pushFollow(FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2837);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1428:1: rule__LinkableWidget__Group_3__2 : rule__LinkableWidget__Group_3__2__Impl ;
    public final void rule__LinkableWidget__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1432:1: ( rule__LinkableWidget__Group_3__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1433:2: rule__LinkableWidget__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__22870);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1439:1: rule__LinkableWidget__Group_3__2__Impl : ( '}' ) ;
    public final void rule__LinkableWidget__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1444:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1444:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1445:1: '}'
            {
             before(grammarAccess.getLinkableWidgetAccess().getRightCurlyBracketKeyword_3_2()); 
            match(input,13,FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl2898); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1464:1: rule__Frame__Group__0 : rule__Frame__Group__0__Impl rule__Frame__Group__1 ;
    public final void rule__Frame__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1468:1: ( rule__Frame__Group__0__Impl rule__Frame__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1469:2: rule__Frame__Group__0__Impl rule__Frame__Group__1
            {
            pushFollow(FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__02935);
            rule__Frame__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__02938);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1476:1: rule__Frame__Group__0__Impl : ( 'Frame' ) ;
    public final void rule__Frame__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1480:1: ( ( 'Frame' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1481:1: ( 'Frame' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1481:1: ( 'Frame' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1482:1: 'Frame'
            {
             before(grammarAccess.getFrameAccess().getFrameKeyword_0()); 
            match(input,16,FOLLOW_16_in_rule__Frame__Group__0__Impl2966); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1495:1: rule__Frame__Group__1 : rule__Frame__Group__1__Impl rule__Frame__Group__2 ;
    public final void rule__Frame__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1499:1: ( rule__Frame__Group__1__Impl rule__Frame__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1500:2: rule__Frame__Group__1__Impl rule__Frame__Group__2
            {
            pushFollow(FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__12997);
            rule__Frame__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13000);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1507:1: rule__Frame__Group__1__Impl : ( () ) ;
    public final void rule__Frame__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1511:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1512:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1512:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1513:1: ()
            {
             before(grammarAccess.getFrameAccess().getFrameAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1514:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1516:1: 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1526:1: rule__Frame__Group__2 : rule__Frame__Group__2__Impl ;
    public final void rule__Frame__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1530:1: ( rule__Frame__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1531:2: rule__Frame__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23058);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1537:1: rule__Frame__Group__2__Impl : ( ( rule__Frame__Group_2__0 )? ) ;
    public final void rule__Frame__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1541:1: ( ( ( rule__Frame__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1542:1: ( ( rule__Frame__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1542:1: ( ( rule__Frame__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1543:1: ( rule__Frame__Group_2__0 )?
            {
             before(grammarAccess.getFrameAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1544:1: ( rule__Frame__Group_2__0 )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==17) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1544:2: rule__Frame__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3085);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1560:1: rule__Frame__Group_2__0 : rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 ;
    public final void rule__Frame__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1564:1: ( rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1565:2: rule__Frame__Group_2__0__Impl rule__Frame__Group_2__1
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03122);
            rule__Frame__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03125);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1572:1: rule__Frame__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Frame__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1576:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1577:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1577:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1578:1: 'item='
            {
             before(grammarAccess.getFrameAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Frame__Group_2__0__Impl3153); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1591:1: rule__Frame__Group_2__1 : rule__Frame__Group_2__1__Impl ;
    public final void rule__Frame__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1595:1: ( rule__Frame__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1596:2: rule__Frame__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13184);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1602:1: rule__Frame__Group_2__1__Impl : ( ( rule__Frame__ItemAssignment_2_1 ) ) ;
    public final void rule__Frame__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1606:1: ( ( ( rule__Frame__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1607:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1607:1: ( ( rule__Frame__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1608:1: ( rule__Frame__ItemAssignment_2_1 )
            {
             before(grammarAccess.getFrameAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1609:1: ( rule__Frame__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1609:2: rule__Frame__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3211);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1623:1: rule__Text__Group__0 : rule__Text__Group__0__Impl rule__Text__Group__1 ;
    public final void rule__Text__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1627:1: ( rule__Text__Group__0__Impl rule__Text__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1628:2: rule__Text__Group__0__Impl rule__Text__Group__1
            {
            pushFollow(FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03245);
            rule__Text__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03248);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1635:1: rule__Text__Group__0__Impl : ( 'Text' ) ;
    public final void rule__Text__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1639:1: ( ( 'Text' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1640:1: ( 'Text' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1640:1: ( 'Text' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1641:1: 'Text'
            {
             before(grammarAccess.getTextAccess().getTextKeyword_0()); 
            match(input,18,FOLLOW_18_in_rule__Text__Group__0__Impl3276); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1654:1: rule__Text__Group__1 : rule__Text__Group__1__Impl rule__Text__Group__2 ;
    public final void rule__Text__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1658:1: ( rule__Text__Group__1__Impl rule__Text__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1659:2: rule__Text__Group__1__Impl rule__Text__Group__2
            {
            pushFollow(FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13307);
            rule__Text__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13310);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1666:1: rule__Text__Group__1__Impl : ( () ) ;
    public final void rule__Text__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1670:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1671:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1671:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1672:1: ()
            {
             before(grammarAccess.getTextAccess().getTextAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1673:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1675:1: 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1685:1: rule__Text__Group__2 : rule__Text__Group__2__Impl ;
    public final void rule__Text__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1689:1: ( rule__Text__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1690:2: rule__Text__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23368);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1696:1: rule__Text__Group__2__Impl : ( ( rule__Text__Group_2__0 )? ) ;
    public final void rule__Text__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1700:1: ( ( ( rule__Text__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1701:1: ( ( rule__Text__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1701:1: ( ( rule__Text__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1702:1: ( rule__Text__Group_2__0 )?
            {
             before(grammarAccess.getTextAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1703:1: ( rule__Text__Group_2__0 )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==17) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1703:2: rule__Text__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3395);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1719:1: rule__Text__Group_2__0 : rule__Text__Group_2__0__Impl rule__Text__Group_2__1 ;
    public final void rule__Text__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1723:1: ( rule__Text__Group_2__0__Impl rule__Text__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1724:2: rule__Text__Group_2__0__Impl rule__Text__Group_2__1
            {
            pushFollow(FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03432);
            rule__Text__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03435);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1731:1: rule__Text__Group_2__0__Impl : ( 'item=' ) ;
    public final void rule__Text__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1735:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1736:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1736:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1737:1: 'item='
            {
             before(grammarAccess.getTextAccess().getItemKeyword_2_0()); 
            match(input,17,FOLLOW_17_in_rule__Text__Group_2__0__Impl3463); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1750:1: rule__Text__Group_2__1 : rule__Text__Group_2__1__Impl ;
    public final void rule__Text__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1754:1: ( rule__Text__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1755:2: rule__Text__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13494);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1761:1: rule__Text__Group_2__1__Impl : ( ( rule__Text__ItemAssignment_2_1 ) ) ;
    public final void rule__Text__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1765:1: ( ( ( rule__Text__ItemAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1766:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1766:1: ( ( rule__Text__ItemAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1767:1: ( rule__Text__ItemAssignment_2_1 )
            {
             before(grammarAccess.getTextAccess().getItemAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1768:1: ( rule__Text__ItemAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1768:2: rule__Text__ItemAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3521);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1782:1: rule__Group__Group__0 : rule__Group__Group__0__Impl rule__Group__Group__1 ;
    public final void rule__Group__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1786:1: ( rule__Group__Group__0__Impl rule__Group__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1787:2: rule__Group__Group__0__Impl rule__Group__Group__1
            {
            pushFollow(FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03555);
            rule__Group__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03558);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1794:1: rule__Group__Group__0__Impl : ( 'Group' ) ;
    public final void rule__Group__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1798:1: ( ( 'Group' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1799:1: ( 'Group' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1799:1: ( 'Group' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1800:1: 'Group'
            {
             before(grammarAccess.getGroupAccess().getGroupKeyword_0()); 
            match(input,19,FOLLOW_19_in_rule__Group__Group__0__Impl3586); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1813:1: rule__Group__Group__1 : rule__Group__Group__1__Impl ;
    public final void rule__Group__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1817:1: ( rule__Group__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1818:2: rule__Group__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13617);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1824:1: rule__Group__Group__1__Impl : ( ( rule__Group__Group_1__0 ) ) ;
    public final void rule__Group__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1828:1: ( ( ( rule__Group__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1829:1: ( ( rule__Group__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1829:1: ( ( rule__Group__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1830:1: ( rule__Group__Group_1__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1831:1: ( rule__Group__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1831:2: rule__Group__Group_1__0
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3644);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1845:1: rule__Group__Group_1__0 : rule__Group__Group_1__0__Impl rule__Group__Group_1__1 ;
    public final void rule__Group__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1849:1: ( rule__Group__Group_1__0__Impl rule__Group__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1850:2: rule__Group__Group_1__0__Impl rule__Group__Group_1__1
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03678);
            rule__Group__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03681);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1857:1: rule__Group__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Group__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1861:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1862:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1862:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1863:1: 'item='
            {
             before(grammarAccess.getGroupAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Group__Group_1__0__Impl3709); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1876:1: rule__Group__Group_1__1 : rule__Group__Group_1__1__Impl ;
    public final void rule__Group__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1880:1: ( rule__Group__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1881:2: rule__Group__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__13740);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1887:1: rule__Group__Group_1__1__Impl : ( ( rule__Group__ItemAssignment_1_1 ) ) ;
    public final void rule__Group__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1891:1: ( ( ( rule__Group__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1892:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1892:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1893:1: ( rule__Group__ItemAssignment_1_1 )
            {
             before(grammarAccess.getGroupAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1894:1: ( rule__Group__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1894:2: rule__Group__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl3767);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1908:1: rule__Image__Group__0 : rule__Image__Group__0__Impl rule__Image__Group__1 ;
    public final void rule__Image__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1912:1: ( rule__Image__Group__0__Impl rule__Image__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1913:2: rule__Image__Group__0__Impl rule__Image__Group__1
            {
            pushFollow(FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__03801);
            rule__Image__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__1_in_rule__Image__Group__03804);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1920:1: rule__Image__Group__0__Impl : ( 'Image' ) ;
    public final void rule__Image__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1924:1: ( ( 'Image' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1925:1: ( 'Image' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1925:1: ( 'Image' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1926:1: 'Image'
            {
             before(grammarAccess.getImageAccess().getImageKeyword_0()); 
            match(input,20,FOLLOW_20_in_rule__Image__Group__0__Impl3832); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1939:1: rule__Image__Group__1 : rule__Image__Group__1__Impl rule__Image__Group__2 ;
    public final void rule__Image__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1943:1: ( rule__Image__Group__1__Impl rule__Image__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1944:2: rule__Image__Group__1__Impl rule__Image__Group__2
            {
            pushFollow(FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__13863);
            rule__Image__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__2_in_rule__Image__Group__13866);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1951:1: rule__Image__Group__1__Impl : ( ( rule__Image__Group_1__0 )? ) ;
    public final void rule__Image__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1955:1: ( ( ( rule__Image__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1956:1: ( ( rule__Image__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1956:1: ( ( rule__Image__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1957:1: ( rule__Image__Group_1__0 )?
            {
             before(grammarAccess.getImageAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1958:1: ( rule__Image__Group_1__0 )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==17) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1958:2: rule__Image__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl3893);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1968:1: rule__Image__Group__2 : rule__Image__Group__2__Impl ;
    public final void rule__Image__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1972:1: ( rule__Image__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1973:2: rule__Image__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__23924);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1979:1: rule__Image__Group__2__Impl : ( ( rule__Image__Group_2__0 ) ) ;
    public final void rule__Image__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1983:1: ( ( ( rule__Image__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1984:1: ( ( rule__Image__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1984:1: ( ( rule__Image__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1985:1: ( rule__Image__Group_2__0 )
            {
             before(grammarAccess.getImageAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1986:1: ( rule__Image__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1986:2: rule__Image__Group_2__0
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl3951);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2002:1: rule__Image__Group_1__0 : rule__Image__Group_1__0__Impl rule__Image__Group_1__1 ;
    public final void rule__Image__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2006:1: ( rule__Image__Group_1__0__Impl rule__Image__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2007:2: rule__Image__Group_1__0__Impl rule__Image__Group_1__1
            {
            pushFollow(FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__03987);
            rule__Image__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__03990);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2014:1: rule__Image__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Image__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2018:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2019:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2019:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2020:1: 'item='
            {
             before(grammarAccess.getImageAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Image__Group_1__0__Impl4018); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2033:1: rule__Image__Group_1__1 : rule__Image__Group_1__1__Impl ;
    public final void rule__Image__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2037:1: ( rule__Image__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2038:2: rule__Image__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14049);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2044:1: rule__Image__Group_1__1__Impl : ( ( rule__Image__ItemAssignment_1_1 ) ) ;
    public final void rule__Image__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2048:1: ( ( ( rule__Image__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2049:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2049:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2050:1: ( rule__Image__ItemAssignment_1_1 )
            {
             before(grammarAccess.getImageAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2051:1: ( rule__Image__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2051:2: rule__Image__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4076);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2065:1: rule__Image__Group_2__0 : rule__Image__Group_2__0__Impl rule__Image__Group_2__1 ;
    public final void rule__Image__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2069:1: ( rule__Image__Group_2__0__Impl rule__Image__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2070:2: rule__Image__Group_2__0__Impl rule__Image__Group_2__1
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04110);
            rule__Image__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04113);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2077:1: rule__Image__Group_2__0__Impl : ( 'url=' ) ;
    public final void rule__Image__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2081:1: ( ( 'url=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2082:1: ( 'url=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2082:1: ( 'url=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2083:1: 'url='
            {
             before(grammarAccess.getImageAccess().getUrlKeyword_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Image__Group_2__0__Impl4141); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2096:1: rule__Image__Group_2__1 : rule__Image__Group_2__1__Impl ;
    public final void rule__Image__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2100:1: ( rule__Image__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2101:2: rule__Image__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14172);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2107:1: rule__Image__Group_2__1__Impl : ( ( rule__Image__UrlAssignment_2_1 ) ) ;
    public final void rule__Image__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2111:1: ( ( ( rule__Image__UrlAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2112:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2112:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2113:1: ( rule__Image__UrlAssignment_2_1 )
            {
             before(grammarAccess.getImageAccess().getUrlAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2114:1: ( rule__Image__UrlAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2114:2: rule__Image__UrlAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4199);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2128:1: rule__Switch__Group__0 : rule__Switch__Group__0__Impl rule__Switch__Group__1 ;
    public final void rule__Switch__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2132:1: ( rule__Switch__Group__0__Impl rule__Switch__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2133:2: rule__Switch__Group__0__Impl rule__Switch__Group__1
            {
            pushFollow(FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04233);
            rule__Switch__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04236);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2140:1: rule__Switch__Group__0__Impl : ( 'Switch' ) ;
    public final void rule__Switch__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2144:1: ( ( 'Switch' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2145:1: ( 'Switch' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2145:1: ( 'Switch' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2146:1: 'Switch'
            {
             before(grammarAccess.getSwitchAccess().getSwitchKeyword_0()); 
            match(input,22,FOLLOW_22_in_rule__Switch__Group__0__Impl4264); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2159:1: rule__Switch__Group__1 : rule__Switch__Group__1__Impl rule__Switch__Group__2 ;
    public final void rule__Switch__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2163:1: ( rule__Switch__Group__1__Impl rule__Switch__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2164:2: rule__Switch__Group__1__Impl rule__Switch__Group__2
            {
            pushFollow(FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14295);
            rule__Switch__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14298);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2171:1: rule__Switch__Group__1__Impl : ( ( rule__Switch__Group_1__0 ) ) ;
    public final void rule__Switch__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2175:1: ( ( ( rule__Switch__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2176:1: ( ( rule__Switch__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2176:1: ( ( rule__Switch__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2177:1: ( rule__Switch__Group_1__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2178:1: ( rule__Switch__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2178:2: rule__Switch__Group_1__0
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4325);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2188:1: rule__Switch__Group__2 : rule__Switch__Group__2__Impl ;
    public final void rule__Switch__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2192:1: ( rule__Switch__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2193:2: rule__Switch__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24355);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2199:1: rule__Switch__Group__2__Impl : ( ( rule__Switch__Group_2__0 )? ) ;
    public final void rule__Switch__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2203:1: ( ( ( rule__Switch__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2204:1: ( ( rule__Switch__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2204:1: ( ( rule__Switch__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2205:1: ( rule__Switch__Group_2__0 )?
            {
             before(grammarAccess.getSwitchAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2206:1: ( rule__Switch__Group_2__0 )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==23) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2206:2: rule__Switch__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4382);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2222:1: rule__Switch__Group_1__0 : rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 ;
    public final void rule__Switch__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2226:1: ( rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2227:2: rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04419);
            rule__Switch__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04422);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2234:1: rule__Switch__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Switch__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2238:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2239:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2239:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2240:1: 'item='
            {
             before(grammarAccess.getSwitchAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Switch__Group_1__0__Impl4450); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2253:1: rule__Switch__Group_1__1 : rule__Switch__Group_1__1__Impl ;
    public final void rule__Switch__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2257:1: ( rule__Switch__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2258:2: rule__Switch__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14481);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2264:1: rule__Switch__Group_1__1__Impl : ( ( rule__Switch__ItemAssignment_1_1 ) ) ;
    public final void rule__Switch__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2268:1: ( ( ( rule__Switch__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2269:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2269:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2270:1: ( rule__Switch__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSwitchAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2271:1: ( rule__Switch__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2271:2: rule__Switch__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4508);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2285:1: rule__Switch__Group_2__0 : rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 ;
    public final void rule__Switch__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2289:1: ( rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2290:2: rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04542);
            rule__Switch__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04545);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2297:1: rule__Switch__Group_2__0__Impl : ( 'buttonLabels=[' ) ;
    public final void rule__Switch__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2301:1: ( ( 'buttonLabels=[' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2302:1: ( 'buttonLabels=[' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2302:1: ( 'buttonLabels=[' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2303:1: 'buttonLabels=['
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0()); 
            match(input,23,FOLLOW_23_in_rule__Switch__Group_2__0__Impl4573); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2316:1: rule__Switch__Group_2__1 : rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 ;
    public final void rule__Switch__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2320:1: ( rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2321:2: rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14604);
            rule__Switch__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14607);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2328:1: rule__Switch__Group_2__1__Impl : ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) ;
    public final void rule__Switch__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2332:1: ( ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2333:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2333:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2334:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2335:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2335:2: rule__Switch__ButtonLabelsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4634);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2345:1: rule__Switch__Group_2__2 : rule__Switch__Group_2__2__Impl ;
    public final void rule__Switch__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2349:1: ( rule__Switch__Group_2__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2350:2: rule__Switch__Group_2__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24664);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2356:1: rule__Switch__Group_2__2__Impl : ( ']' ) ;
    public final void rule__Switch__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2360:1: ( ( ']' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2361:1: ( ']' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2361:1: ( ']' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2362:1: ']'
            {
             before(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_2()); 
            match(input,24,FOLLOW_24_in_rule__Switch__Group_2__2__Impl4692); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2381:1: rule__Selection__Group__0 : rule__Selection__Group__0__Impl rule__Selection__Group__1 ;
    public final void rule__Selection__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2385:1: ( rule__Selection__Group__0__Impl rule__Selection__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2386:2: rule__Selection__Group__0__Impl rule__Selection__Group__1
            {
            pushFollow(FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__04729);
            rule__Selection__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__04732);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2393:1: rule__Selection__Group__0__Impl : ( 'Selection' ) ;
    public final void rule__Selection__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2397:1: ( ( 'Selection' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2398:1: ( 'Selection' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2398:1: ( 'Selection' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2399:1: 'Selection'
            {
             before(grammarAccess.getSelectionAccess().getSelectionKeyword_0()); 
            match(input,25,FOLLOW_25_in_rule__Selection__Group__0__Impl4760); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2412:1: rule__Selection__Group__1 : rule__Selection__Group__1__Impl ;
    public final void rule__Selection__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2416:1: ( rule__Selection__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2417:2: rule__Selection__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__14791);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2423:1: rule__Selection__Group__1__Impl : ( ( rule__Selection__Group_1__0 ) ) ;
    public final void rule__Selection__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2427:1: ( ( ( rule__Selection__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2428:1: ( ( rule__Selection__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2428:1: ( ( rule__Selection__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2429:1: ( rule__Selection__Group_1__0 )
            {
             before(grammarAccess.getSelectionAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2430:1: ( rule__Selection__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2430:2: rule__Selection__Group_1__0
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl4818);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2444:1: rule__Selection__Group_1__0 : rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 ;
    public final void rule__Selection__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2448:1: ( rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2449:2: rule__Selection__Group_1__0__Impl rule__Selection__Group_1__1
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__04852);
            rule__Selection__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__04855);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2456:1: rule__Selection__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Selection__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2460:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2461:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2461:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2462:1: 'item='
            {
             before(grammarAccess.getSelectionAccess().getItemKeyword_1_0()); 
            match(input,17,FOLLOW_17_in_rule__Selection__Group_1__0__Impl4883); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2475:1: rule__Selection__Group_1__1 : rule__Selection__Group_1__1__Impl ;
    public final void rule__Selection__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2479:1: ( rule__Selection__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2480:2: rule__Selection__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__14914);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2486:1: rule__Selection__Group_1__1__Impl : ( ( rule__Selection__ItemAssignment_1_1 ) ) ;
    public final void rule__Selection__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2490:1: ( ( ( rule__Selection__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2491:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2491:1: ( ( rule__Selection__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2492:1: ( rule__Selection__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSelectionAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2493:1: ( rule__Selection__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2493:2: rule__Selection__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl4941);
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


    // $ANTLR start rule__Sitemap__NameAssignment_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2508:1: rule__Sitemap__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Sitemap__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2512:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2513:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2513:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2514:1: RULE_ID
            {
             before(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_04980); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2523:1: rule__Sitemap__LabelAssignment_1_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2527:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2528:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2528:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2529:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_15011); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2538:1: rule__Sitemap__IconAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2542:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2543:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2543:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2544:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_15042); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2553:1: rule__Sitemap__ChildrenAssignment_4 : ( ruleWidget ) ;
    public final void rule__Sitemap__ChildrenAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2557:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2558:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2558:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2559:1: ruleWidget
            {
             before(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_45073);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2568:1: rule__Widget__LabelAssignment_1_1_1 : ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) ;
    public final void rule__Widget__LabelAssignment_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2572:1: ( ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2573:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2573:1: ( ( rule__Widget__LabelAlternatives_1_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2574:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2575:1: ( rule__Widget__LabelAlternatives_1_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2575:2: rule__Widget__LabelAlternatives_1_1_1_0
            {
            pushFollow(FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_15104);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2584:1: rule__Widget__IconAssignment_1_2_1 : ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) ;
    public final void rule__Widget__IconAssignment_1_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2588:1: ( ( ( rule__Widget__IconAlternatives_1_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2589:1: ( ( rule__Widget__IconAlternatives_1_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2590:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getIconAlternatives_1_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2591:1: ( rule__Widget__IconAlternatives_1_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2591:2: rule__Widget__IconAlternatives_1_2_1_0
            {
            pushFollow(FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_15137);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2600:1: rule__LinkableWidget__LabelAssignment_1_1 : ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) ;
    public final void rule__LinkableWidget__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2604:1: ( ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2605:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2605:1: ( ( rule__LinkableWidget__LabelAlternatives_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2606:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getLabelAlternatives_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2607:1: ( rule__LinkableWidget__LabelAlternatives_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2607:2: rule__LinkableWidget__LabelAlternatives_1_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_15170);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2616:1: rule__LinkableWidget__IconAssignment_2_1 : ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) ;
    public final void rule__LinkableWidget__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2620:1: ( ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2621:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2621:1: ( ( rule__LinkableWidget__IconAlternatives_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2622:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            {
             before(grammarAccess.getLinkableWidgetAccess().getIconAlternatives_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2623:1: ( rule__LinkableWidget__IconAlternatives_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2623:2: rule__LinkableWidget__IconAlternatives_2_1_0
            {
            pushFollow(FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_15203);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2632:1: rule__LinkableWidget__ChildrenAssignment_3_1 : ( ruleWidget ) ;
    public final void rule__LinkableWidget__ChildrenAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2636:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2637:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2637:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2638:1: ruleWidget
            {
             before(grammarAccess.getLinkableWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_15236);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2647:1: rule__Frame__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Frame__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2651:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2652:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2652:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2653:1: RULE_ID
            {
             before(grammarAccess.getFrameAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_15267); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2662:1: rule__Text__ItemAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Text__ItemAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2666:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2667:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2667:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2668:1: RULE_ID
            {
             before(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_15298); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2677:1: rule__Group__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Group__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2681:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2682:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2682:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2683:1: RULE_ID
            {
             before(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_15329); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2692:1: rule__Image__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Image__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2696:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2697:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2697:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2698:1: RULE_ID
            {
             before(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_15360); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2707:1: rule__Image__UrlAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Image__UrlAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2711:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2712:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2712:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2713:1: RULE_STRING
            {
             before(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_15391); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2722:1: rule__Switch__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Switch__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2726:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2727:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2727:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2728:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_15422); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2737:1: rule__Switch__ButtonLabelsAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Switch__ButtonLabelsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2741:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2742:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2742:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2743:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_15453); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2752:1: rule__Selection__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Selection__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2756:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2757:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2757:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2758:1: RULE_ID
            {
             before(grammarAccess.getSelectionAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_15484); 
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
    public static final BitSet FOLLOW_ruleLinkableWidget_in_rule__Widget__Alternatives670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Alternatives687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_1_0720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelection_in_rule__Widget__Alternatives_1_0737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_1_0769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_1_0786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_1_2_1_0818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_1_2_1_0835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_rule__LinkableWidget__Alternatives_0867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_rule__LinkableWidget__Alternatives_0884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_rule__LinkableWidget__Alternatives_0901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_rule__LinkableWidget__Alternatives_0918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__LabelAlternatives_1_1_0950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__LabelAlternatives_1_1_0967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__LinkableWidget__IconAlternatives_2_1_0999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__LinkableWidget__IconAlternatives_2_1_01016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__0__Impl_in_rule__SitemapModel__Group__01046 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1_in_rule__SitemapModel__Group__01049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__SitemapModel__Group__0__Impl1077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SitemapModel__Group__1__Impl_in_rule__SitemapModel__Group__11108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_rule__SitemapModel__Group__1__Impl1135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__01168 = new BitSet(new long[]{0x000000000000D000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__01171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl1198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__11228 = new BitSet(new long[]{0x0000000000009000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__11231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl1258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__21289 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__21292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31350 = new BitSet(new long[]{0x00000000025D0000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Sitemap__Group__3__Impl1381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41412 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1444 = new BitSet(new long[]{0x00000000025D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1456 = new BitSet(new long[]{0x00000000025D0002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Sitemap__Group__5__Impl1517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01560 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01683 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01806 = new BitSet(new long[]{0x000000000000C002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Alternatives_1_0_in_rule__Widget__Group_1__0__Impl1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11866 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2_in_rule__Widget__Group_1__11869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0_in_rule__Widget__Group_1__1__Impl1896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__2__Impl_in_rule__Widget__Group_1__21927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0_in_rule__Widget__Group_1__2__Impl1954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__0__Impl_in_rule__Widget__Group_1_1__01991 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1_in_rule__Widget__Group_1_1__01994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Widget__Group_1_1__0__Impl2022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_1__1__Impl_in_rule__Widget__Group_1_1__12053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAssignment_1_1_1_in_rule__Widget__Group_1_1__1__Impl2080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__0__Impl_in_rule__Widget__Group_1_2__02114 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1_in_rule__Widget__Group_1_2__02117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Widget__Group_1_2__0__Impl2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1_2__1__Impl_in_rule__Widget__Group_1_2__12176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAssignment_1_2_1_in_rule__Widget__Group_1_2__1__Impl2203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__0__Impl_in_rule__LinkableWidget__Group__02237 = new BitSet(new long[]{0x000000000000D002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1_in_rule__LinkableWidget__Group__02240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Alternatives_0_in_rule__LinkableWidget__Group__0__Impl2267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__1__Impl_in_rule__LinkableWidget__Group__12297 = new BitSet(new long[]{0x0000000000009002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2_in_rule__LinkableWidget__Group__12300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0_in_rule__LinkableWidget__Group__1__Impl2327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__2__Impl_in_rule__LinkableWidget__Group__22358 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3_in_rule__LinkableWidget__Group__22361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0_in_rule__LinkableWidget__Group__2__Impl2388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group__3__Impl_in_rule__LinkableWidget__Group__32419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0_in_rule__LinkableWidget__Group__3__Impl2446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__0__Impl_in_rule__LinkableWidget__Group_1__02485 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1_in_rule__LinkableWidget__Group_1__02488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__LinkableWidget__Group_1__0__Impl2516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_1__1__Impl_in_rule__LinkableWidget__Group_1__12547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAssignment_1_1_in_rule__LinkableWidget__Group_1__1__Impl2574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__0__Impl_in_rule__LinkableWidget__Group_2__02608 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1_in_rule__LinkableWidget__Group_2__02611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__LinkableWidget__Group_2__0__Impl2639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_2__1__Impl_in_rule__LinkableWidget__Group_2__12670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAssignment_2_1_in_rule__LinkableWidget__Group_2__1__Impl2697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__0__Impl_in_rule__LinkableWidget__Group_3__02731 = new BitSet(new long[]{0x00000000025D0000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1_in_rule__LinkableWidget__Group_3__02734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__LinkableWidget__Group_3__0__Impl2762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__1__Impl_in_rule__LinkableWidget__Group_3__12793 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2_in_rule__LinkableWidget__Group_3__12796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2825 = new BitSet(new long[]{0x00000000025D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__ChildrenAssignment_3_1_in_rule__LinkableWidget__Group_3__1__Impl2837 = new BitSet(new long[]{0x00000000025D0002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__Group_3__2__Impl_in_rule__LinkableWidget__Group_3__22870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__LinkableWidget__Group_3__2__Impl2898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__02935 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__02938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Frame__Group__0__Impl2966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__12997 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2_in_rule__Frame__Group__13000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__2__Impl_in_rule__Frame__Group__23058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0_in_rule__Frame__Group__2__Impl3085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__0__Impl_in_rule__Frame__Group_2__03122 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1_in_rule__Frame__Group_2__03125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Frame__Group_2__0__Impl3153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group_2__1__Impl_in_rule__Frame__Group_2__13184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__ItemAssignment_2_1_in_rule__Frame__Group_2__1__Impl3211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__03245 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__1_in_rule__Text__Group__03248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Text__Group__0__Impl3276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__13307 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__Text__Group__2_in_rule__Text__Group__13310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__2__Impl_in_rule__Text__Group__23368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0_in_rule__Text__Group__2__Impl3395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__0__Impl_in_rule__Text__Group_2__03432 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1_in_rule__Text__Group_2__03435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Text__Group_2__0__Impl3463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_2__1__Impl_in_rule__Text__Group_2__13494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__ItemAssignment_2_1_in_rule__Text__Group_2__1__Impl3521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__03555 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Group__Group__1_in_rule__Group__Group__03558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Group__Group__0__Impl3586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__13617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl3644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__03678 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__03681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Group__Group_1__0__Impl3709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__13740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl3767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__03801 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_rule__Image__Group__1_in_rule__Image__Group__03804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Image__Group__0__Impl3832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__13863 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__Image__Group__2_in_rule__Image__Group__13866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl3893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__23924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl3951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__03987 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__03990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Image__Group_1__0__Impl4018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__14049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl4076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__04110 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__04113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Image__Group_2__0__Impl4141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__14172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl4199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__04233 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__04236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Switch__Group__0__Impl4264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__14295 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__14298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl4325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__24355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl4382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__04419 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__04422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Switch__Group_1__0__Impl4450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__14481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl4508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__04542 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__04545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Switch__Group_2__0__Impl4573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__14604 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__14607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl4634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__24664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Switch__Group_2__2__Impl4692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__0__Impl_in_rule__Selection__Group__04729 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Selection__Group__1_in_rule__Selection__Group__04732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Selection__Group__0__Impl4760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group__1__Impl_in_rule__Selection__Group__14791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0_in_rule__Selection__Group__1__Impl4818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__0__Impl_in_rule__Selection__Group_1__04852 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1_in_rule__Selection__Group_1__04855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Selection__Group_1__0__Impl4883 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__Group_1__1__Impl_in_rule__Selection__Group_1__14914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Selection__ItemAssignment_1_1_in_rule__Selection__Group_1__1__Impl4941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_04980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_15011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_15042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_45073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAlternatives_1_1_1_0_in_rule__Widget__LabelAssignment_1_1_15104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAlternatives_1_2_1_0_in_rule__Widget__IconAssignment_1_2_15137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__LabelAlternatives_1_1_0_in_rule__LinkableWidget__LabelAssignment_1_15170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LinkableWidget__IconAlternatives_2_1_0_in_rule__LinkableWidget__IconAssignment_2_15203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__LinkableWidget__ChildrenAssignment_3_15236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Frame__ItemAssignment_2_15267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_2_15298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_15329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_15360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_15391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_15422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_15453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Selection__ItemAssignment_1_15484 = new BitSet(new long[]{0x0000000000000002L});

}