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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'sitemap'", "'{'", "'}'", "'label='", "'icon='", "'Frame'", "'Text'", "'item='", "'Group'", "'Image'", "'url='", "'Switch'", "'buttonLabels=['", "']'"
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




    // $ANTLR start entryRuleModel
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:61:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:62:1: ( ruleModel EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:63:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel61);
            ruleModel();
            _fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel68); 

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
    // $ANTLR end entryRuleModel


    // $ANTLR start ruleModel
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:70:1: ruleModel : ( ( rule__Model__Group__0 ) ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:74:2: ( ( ( rule__Model__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:75:1: ( ( rule__Model__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:75:1: ( ( rule__Model__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:76:1: ( rule__Model__Group__0 )
            {
             before(grammarAccess.getModelAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:77:1: ( rule__Model__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:77:2: rule__Model__Group__0
            {
            pushFollow(FOLLOW_rule__Model__Group__0_in_ruleModel94);
            rule__Model__Group__0();
            _fsp--;


            }

             after(grammarAccess.getModelAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end ruleModel


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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:126:1: ruleWidget : ( ( rule__Widget__Group__0 ) ) ;
    public final void ruleWidget() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:130:2: ( ( ( rule__Widget__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:131:1: ( ( rule__Widget__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:131:1: ( ( rule__Widget__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:132:1: ( rule__Widget__Group__0 )
            {
             before(grammarAccess.getWidgetAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:133:1: ( rule__Widget__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:133:2: rule__Widget__Group__0
            {
            pushFollow(FOLLOW_rule__Widget__Group__0_in_ruleWidget214);
            rule__Widget__Group__0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getGroup()); 

            }


            }

        }
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


    // $ANTLR start entryRuleFrame
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:145:1: entryRuleFrame : ruleFrame EOF ;
    public final void entryRuleFrame() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:146:1: ( ruleFrame EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:147:1: ruleFrame EOF
            {
             before(grammarAccess.getFrameRule()); 
            pushFollow(FOLLOW_ruleFrame_in_entryRuleFrame241);
            ruleFrame();
            _fsp--;

             after(grammarAccess.getFrameRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFrame248); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:154:1: ruleFrame : ( ( rule__Frame__Group__0 ) ) ;
    public final void ruleFrame() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:158:2: ( ( ( rule__Frame__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:159:1: ( ( rule__Frame__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:159:1: ( ( rule__Frame__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:160:1: ( rule__Frame__Group__0 )
            {
             before(grammarAccess.getFrameAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:161:1: ( rule__Frame__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:161:2: rule__Frame__Group__0
            {
            pushFollow(FOLLOW_rule__Frame__Group__0_in_ruleFrame274);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:173:1: entryRuleText : ruleText EOF ;
    public final void entryRuleText() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:174:1: ( ruleText EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:175:1: ruleText EOF
            {
             before(grammarAccess.getTextRule()); 
            pushFollow(FOLLOW_ruleText_in_entryRuleText301);
            ruleText();
            _fsp--;

             after(grammarAccess.getTextRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleText308); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:182:1: ruleText : ( ( rule__Text__Group__0 ) ) ;
    public final void ruleText() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:186:2: ( ( ( rule__Text__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:187:1: ( ( rule__Text__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:187:1: ( ( rule__Text__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:188:1: ( rule__Text__Group__0 )
            {
             before(grammarAccess.getTextAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:189:1: ( rule__Text__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:189:2: rule__Text__Group__0
            {
            pushFollow(FOLLOW_rule__Text__Group__0_in_ruleText334);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:201:1: entryRuleGroup : ruleGroup EOF ;
    public final void entryRuleGroup() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:202:1: ( ruleGroup EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:203:1: ruleGroup EOF
            {
             before(grammarAccess.getGroupRule()); 
            pushFollow(FOLLOW_ruleGroup_in_entryRuleGroup361);
            ruleGroup();
            _fsp--;

             after(grammarAccess.getGroupRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroup368); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:210:1: ruleGroup : ( ( rule__Group__Group__0 ) ) ;
    public final void ruleGroup() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:214:2: ( ( ( rule__Group__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:215:1: ( ( rule__Group__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:215:1: ( ( rule__Group__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:216:1: ( rule__Group__Group__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:217:1: ( rule__Group__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:217:2: rule__Group__Group__0
            {
            pushFollow(FOLLOW_rule__Group__Group__0_in_ruleGroup394);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:229:1: entryRuleImage : ruleImage EOF ;
    public final void entryRuleImage() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:230:1: ( ruleImage EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:231:1: ruleImage EOF
            {
             before(grammarAccess.getImageRule()); 
            pushFollow(FOLLOW_ruleImage_in_entryRuleImage421);
            ruleImage();
            _fsp--;

             after(grammarAccess.getImageRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImage428); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:238:1: ruleImage : ( ( rule__Image__Group__0 ) ) ;
    public final void ruleImage() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:242:2: ( ( ( rule__Image__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:243:1: ( ( rule__Image__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:243:1: ( ( rule__Image__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:244:1: ( rule__Image__Group__0 )
            {
             before(grammarAccess.getImageAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:245:1: ( rule__Image__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:245:2: rule__Image__Group__0
            {
            pushFollow(FOLLOW_rule__Image__Group__0_in_ruleImage454);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:257:1: entryRuleSwitch : ruleSwitch EOF ;
    public final void entryRuleSwitch() throws RecognitionException {
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:258:1: ( ruleSwitch EOF )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:259:1: ruleSwitch EOF
            {
             before(grammarAccess.getSwitchRule()); 
            pushFollow(FOLLOW_ruleSwitch_in_entryRuleSwitch481);
            ruleSwitch();
            _fsp--;

             after(grammarAccess.getSwitchRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSwitch488); 

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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:266:1: ruleSwitch : ( ( rule__Switch__Group__0 ) ) ;
    public final void ruleSwitch() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:270:2: ( ( ( rule__Switch__Group__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:271:1: ( ( rule__Switch__Group__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:271:1: ( ( rule__Switch__Group__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:272:1: ( rule__Switch__Group__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:273:1: ( rule__Switch__Group__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:273:2: rule__Switch__Group__0
            {
            pushFollow(FOLLOW_rule__Switch__Group__0_in_ruleSwitch514);
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


    // $ANTLR start rule__Widget__Alternatives_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:285:1: rule__Widget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleSwitch ) | ( ruleFrame ) );
    public final void rule__Widget__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:289:1: ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleSwitch ) | ( ruleFrame ) )
            int alt1=5;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt1=1;
                }
                break;
            case 19:
                {
                alt1=2;
                }
                break;
            case 20:
                {
                alt1=3;
                }
                break;
            case 22:
                {
                alt1=4;
                }
                break;
            case 16:
                {
                alt1=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("285:1: rule__Widget__Alternatives_0 : ( ( ruleText ) | ( ruleGroup ) | ( ruleImage ) | ( ruleSwitch ) | ( ruleFrame ) );", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:290:1: ( ruleText )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:290:1: ( ruleText )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:291:1: ruleText
                    {
                     before(grammarAccess.getWidgetAccess().getTextParserRuleCall_0_0()); 
                    pushFollow(FOLLOW_ruleText_in_rule__Widget__Alternatives_0550);
                    ruleText();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getTextParserRuleCall_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:296:6: ( ruleGroup )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:296:6: ( ruleGroup )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:297:1: ruleGroup
                    {
                     before(grammarAccess.getWidgetAccess().getGroupParserRuleCall_0_1()); 
                    pushFollow(FOLLOW_ruleGroup_in_rule__Widget__Alternatives_0567);
                    ruleGroup();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getGroupParserRuleCall_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:302:6: ( ruleImage )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:302:6: ( ruleImage )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:303:1: ruleImage
                    {
                     before(grammarAccess.getWidgetAccess().getImageParserRuleCall_0_2()); 
                    pushFollow(FOLLOW_ruleImage_in_rule__Widget__Alternatives_0584);
                    ruleImage();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getImageParserRuleCall_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:308:6: ( ruleSwitch )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:308:6: ( ruleSwitch )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:309:1: ruleSwitch
                    {
                     before(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_0_3()); 
                    pushFollow(FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_0601);
                    ruleSwitch();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getSwitchParserRuleCall_0_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:314:6: ( ruleFrame )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:314:6: ( ruleFrame )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:315:1: ruleFrame
                    {
                     before(grammarAccess.getWidgetAccess().getFrameParserRuleCall_0_4()); 
                    pushFollow(FOLLOW_ruleFrame_in_rule__Widget__Alternatives_0618);
                    ruleFrame();
                    _fsp--;

                     after(grammarAccess.getWidgetAccess().getFrameParserRuleCall_0_4()); 

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
    // $ANTLR end rule__Widget__Alternatives_0


    // $ANTLR start rule__Widget__LabelAlternatives_1_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:325:1: rule__Widget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__LabelAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:329:1: ( ( RULE_ID ) | ( RULE_STRING ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID) ) {
                alt2=1;
            }
            else if ( (LA2_0==RULE_STRING) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("325:1: rule__Widget__LabelAlternatives_1_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:330:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:330:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:331:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_0650); 
                     after(grammarAccess.getWidgetAccess().getLabelIDTerminalRuleCall_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:336:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:336:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:337:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_0667); 
                     after(grammarAccess.getWidgetAccess().getLabelSTRINGTerminalRuleCall_1_1_0_1()); 

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
    // $ANTLR end rule__Widget__LabelAlternatives_1_1_0


    // $ANTLR start rule__Widget__IconAlternatives_2_1_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:347:1: rule__Widget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );
    public final void rule__Widget__IconAlternatives_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:351:1: ( ( RULE_ID ) | ( RULE_STRING ) )
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
                    new NoViableAltException("347:1: rule__Widget__IconAlternatives_2_1_0 : ( ( RULE_ID ) | ( RULE_STRING ) );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:352:1: ( RULE_ID )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:352:1: ( RULE_ID )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:353:1: RULE_ID
                    {
                     before(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_2_1_0699); 
                     after(grammarAccess.getWidgetAccess().getIconIDTerminalRuleCall_2_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:358:6: ( RULE_STRING )
                    {
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:358:6: ( RULE_STRING )
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:359:1: RULE_STRING
                    {
                     before(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_2_1_0716); 
                     after(grammarAccess.getWidgetAccess().getIconSTRINGTerminalRuleCall_2_1_0_1()); 

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
    // $ANTLR end rule__Widget__IconAlternatives_2_1_0


    // $ANTLR start rule__Model__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:371:1: rule__Model__Group__0 : rule__Model__Group__0__Impl rule__Model__Group__1 ;
    public final void rule__Model__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:375:1: ( rule__Model__Group__0__Impl rule__Model__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:376:2: rule__Model__Group__0__Impl rule__Model__Group__1
            {
            pushFollow(FOLLOW_rule__Model__Group__0__Impl_in_rule__Model__Group__0746);
            rule__Model__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Model__Group__1_in_rule__Model__Group__0749);
            rule__Model__Group__1();
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
    // $ANTLR end rule__Model__Group__0


    // $ANTLR start rule__Model__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:383:1: rule__Model__Group__0__Impl : ( 'sitemap' ) ;
    public final void rule__Model__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:387:1: ( ( 'sitemap' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:388:1: ( 'sitemap' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:388:1: ( 'sitemap' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:389:1: 'sitemap'
            {
             before(grammarAccess.getModelAccess().getSitemapKeyword_0()); 
            match(input,11,FOLLOW_11_in_rule__Model__Group__0__Impl777); 
             after(grammarAccess.getModelAccess().getSitemapKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Model__Group__0__Impl


    // $ANTLR start rule__Model__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:402:1: rule__Model__Group__1 : rule__Model__Group__1__Impl ;
    public final void rule__Model__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:406:1: ( rule__Model__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:407:2: rule__Model__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Model__Group__1__Impl_in_rule__Model__Group__1808);
            rule__Model__Group__1__Impl();
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
    // $ANTLR end rule__Model__Group__1


    // $ANTLR start rule__Model__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:413:1: rule__Model__Group__1__Impl : ( ruleSitemap ) ;
    public final void rule__Model__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:417:1: ( ( ruleSitemap ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:418:1: ( ruleSitemap )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:418:1: ( ruleSitemap )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:419:1: ruleSitemap
            {
             before(grammarAccess.getModelAccess().getSitemapParserRuleCall_1()); 
            pushFollow(FOLLOW_ruleSitemap_in_rule__Model__Group__1__Impl835);
            ruleSitemap();
            _fsp--;

             after(grammarAccess.getModelAccess().getSitemapParserRuleCall_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Model__Group__1__Impl


    // $ANTLR start rule__Sitemap__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:434:1: rule__Sitemap__Group__0 : rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 ;
    public final void rule__Sitemap__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:438:1: ( rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:439:2: rule__Sitemap__Group__0__Impl rule__Sitemap__Group__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__0868);
            rule__Sitemap__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__0871);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:446:1: rule__Sitemap__Group__0__Impl : ( ( rule__Sitemap__NameAssignment_0 ) ) ;
    public final void rule__Sitemap__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:450:1: ( ( ( rule__Sitemap__NameAssignment_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:451:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:451:1: ( ( rule__Sitemap__NameAssignment_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:452:1: ( rule__Sitemap__NameAssignment_0 )
            {
             before(grammarAccess.getSitemapAccess().getNameAssignment_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:453:1: ( rule__Sitemap__NameAssignment_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:453:2: rule__Sitemap__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl898);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:463:1: rule__Sitemap__Group__1 : rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 ;
    public final void rule__Sitemap__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:467:1: ( rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:468:2: rule__Sitemap__Group__1__Impl rule__Sitemap__Group__2
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__1928);
            rule__Sitemap__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__1931);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:475:1: rule__Sitemap__Group__1__Impl : ( ( rule__Sitemap__Group_1__0 )? ) ;
    public final void rule__Sitemap__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:479:1: ( ( ( rule__Sitemap__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:1: ( ( rule__Sitemap__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:480:1: ( ( rule__Sitemap__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:481:1: ( rule__Sitemap__Group_1__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:482:1: ( rule__Sitemap__Group_1__0 )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==14) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:482:2: rule__Sitemap__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl958);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:492:1: rule__Sitemap__Group__2 : rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 ;
    public final void rule__Sitemap__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:496:1: ( rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:497:2: rule__Sitemap__Group__2__Impl rule__Sitemap__Group__3
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__2989);
            rule__Sitemap__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__2992);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:504:1: rule__Sitemap__Group__2__Impl : ( ( rule__Sitemap__Group_2__0 )? ) ;
    public final void rule__Sitemap__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:508:1: ( ( ( rule__Sitemap__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:509:1: ( ( rule__Sitemap__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:509:1: ( ( rule__Sitemap__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:510:1: ( rule__Sitemap__Group_2__0 )?
            {
             before(grammarAccess.getSitemapAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:511:1: ( rule__Sitemap__Group_2__0 )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==15) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:511:2: rule__Sitemap__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1019);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:521:1: rule__Sitemap__Group__3 : rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 ;
    public final void rule__Sitemap__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:525:1: ( rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:526:2: rule__Sitemap__Group__3__Impl rule__Sitemap__Group__4
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31050);
            rule__Sitemap__Group__3__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31053);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:533:1: rule__Sitemap__Group__3__Impl : ( '{' ) ;
    public final void rule__Sitemap__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:537:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:538:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:538:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:539:1: '{'
            {
             before(grammarAccess.getSitemapAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_12_in_rule__Sitemap__Group__3__Impl1081); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:552:1: rule__Sitemap__Group__4 : rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 ;
    public final void rule__Sitemap__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:556:1: ( rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:557:2: rule__Sitemap__Group__4__Impl rule__Sitemap__Group__5
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41112);
            rule__Sitemap__Group__4__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41115);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:564:1: rule__Sitemap__Group__4__Impl : ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) ;
    public final void rule__Sitemap__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:568:1: ( ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:569:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:569:1: ( ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:570:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) ) ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:570:1: ( ( rule__Sitemap__ChildrenAssignment_4 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:571:1: ( rule__Sitemap__ChildrenAssignment_4 )
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:572:1: ( rule__Sitemap__ChildrenAssignment_4 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:572:2: rule__Sitemap__ChildrenAssignment_4
            {
            pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1144);
            rule__Sitemap__ChildrenAssignment_4();
            _fsp--;


            }

             after(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:575:1: ( ( rule__Sitemap__ChildrenAssignment_4 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:576:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            {
             before(grammarAccess.getSitemapAccess().getChildrenAssignment_4()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:577:1: ( rule__Sitemap__ChildrenAssignment_4 )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>=16 && LA6_0<=17)||(LA6_0>=19 && LA6_0<=20)||LA6_0==22) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:577:2: rule__Sitemap__ChildrenAssignment_4
            	    {
            	    pushFollow(FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1156);
            	    rule__Sitemap__ChildrenAssignment_4();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:588:1: rule__Sitemap__Group__5 : rule__Sitemap__Group__5__Impl ;
    public final void rule__Sitemap__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:592:1: ( rule__Sitemap__Group__5__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:593:2: rule__Sitemap__Group__5__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51189);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:599:1: rule__Sitemap__Group__5__Impl : ( '}' ) ;
    public final void rule__Sitemap__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:603:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:604:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:604:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:605:1: '}'
            {
             before(grammarAccess.getSitemapAccess().getRightCurlyBracketKeyword_5()); 
            match(input,13,FOLLOW_13_in_rule__Sitemap__Group__5__Impl1217); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:630:1: rule__Sitemap__Group_1__0 : rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 ;
    public final void rule__Sitemap__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:634:1: ( rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:635:2: rule__Sitemap__Group_1__0__Impl rule__Sitemap__Group_1__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01260);
            rule__Sitemap__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01263);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:642:1: rule__Sitemap__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__Sitemap__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:646:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:647:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:647:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:648:1: 'label='
            {
             before(grammarAccess.getSitemapAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1291); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:661:1: rule__Sitemap__Group_1__1 : rule__Sitemap__Group_1__1__Impl ;
    public final void rule__Sitemap__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:665:1: ( rule__Sitemap__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:666:2: rule__Sitemap__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11322);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:672:1: rule__Sitemap__Group_1__1__Impl : ( ( rule__Sitemap__LabelAssignment_1_1 ) ) ;
    public final void rule__Sitemap__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:676:1: ( ( ( rule__Sitemap__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:677:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:677:1: ( ( rule__Sitemap__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:678:1: ( rule__Sitemap__LabelAssignment_1_1 )
            {
             before(grammarAccess.getSitemapAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:679:1: ( rule__Sitemap__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:679:2: rule__Sitemap__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1349);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:693:1: rule__Sitemap__Group_2__0 : rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 ;
    public final void rule__Sitemap__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:697:1: ( rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:698:2: rule__Sitemap__Group_2__0__Impl rule__Sitemap__Group_2__1
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01383);
            rule__Sitemap__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01386);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:705:1: rule__Sitemap__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Sitemap__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:709:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:710:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:711:1: 'icon='
            {
             before(grammarAccess.getSitemapAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1414); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:724:1: rule__Sitemap__Group_2__1 : rule__Sitemap__Group_2__1__Impl ;
    public final void rule__Sitemap__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:728:1: ( rule__Sitemap__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:729:2: rule__Sitemap__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11445);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:735:1: rule__Sitemap__Group_2__1__Impl : ( ( rule__Sitemap__IconAssignment_2_1 ) ) ;
    public final void rule__Sitemap__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:739:1: ( ( ( rule__Sitemap__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:740:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:740:1: ( ( rule__Sitemap__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:741:1: ( rule__Sitemap__IconAssignment_2_1 )
            {
             before(grammarAccess.getSitemapAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:1: ( rule__Sitemap__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:742:2: rule__Sitemap__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1472);
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


    // $ANTLR start rule__Widget__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:756:1: rule__Widget__Group__0 : rule__Widget__Group__0__Impl rule__Widget__Group__1 ;
    public final void rule__Widget__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:760:1: ( rule__Widget__Group__0__Impl rule__Widget__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:761:2: rule__Widget__Group__0__Impl rule__Widget__Group__1
            {
            pushFollow(FOLLOW_rule__Widget__Group__0__Impl_in_rule__Widget__Group__01506);
            rule__Widget__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group__1_in_rule__Widget__Group__01509);
            rule__Widget__Group__1();
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
    // $ANTLR end rule__Widget__Group__0


    // $ANTLR start rule__Widget__Group__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:768:1: rule__Widget__Group__0__Impl : ( ( rule__Widget__Alternatives_0 ) ) ;
    public final void rule__Widget__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:772:1: ( ( ( rule__Widget__Alternatives_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:773:1: ( ( rule__Widget__Alternatives_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:773:1: ( ( rule__Widget__Alternatives_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:774:1: ( rule__Widget__Alternatives_0 )
            {
             before(grammarAccess.getWidgetAccess().getAlternatives_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:775:1: ( rule__Widget__Alternatives_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:775:2: rule__Widget__Alternatives_0
            {
            pushFollow(FOLLOW_rule__Widget__Alternatives_0_in_rule__Widget__Group__0__Impl1536);
            rule__Widget__Alternatives_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getAlternatives_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group__0__Impl


    // $ANTLR start rule__Widget__Group__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:785:1: rule__Widget__Group__1 : rule__Widget__Group__1__Impl rule__Widget__Group__2 ;
    public final void rule__Widget__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:789:1: ( rule__Widget__Group__1__Impl rule__Widget__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:790:2: rule__Widget__Group__1__Impl rule__Widget__Group__2
            {
            pushFollow(FOLLOW_rule__Widget__Group__1__Impl_in_rule__Widget__Group__11566);
            rule__Widget__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group__2_in_rule__Widget__Group__11569);
            rule__Widget__Group__2();
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
    // $ANTLR end rule__Widget__Group__1


    // $ANTLR start rule__Widget__Group__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:797:1: rule__Widget__Group__1__Impl : ( ( rule__Widget__Group_1__0 )? ) ;
    public final void rule__Widget__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:801:1: ( ( ( rule__Widget__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:802:1: ( ( rule__Widget__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:802:1: ( ( rule__Widget__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:803:1: ( rule__Widget__Group_1__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:804:1: ( rule__Widget__Group_1__0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==14) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:804:2: rule__Widget__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Group__1__Impl1596);
                    rule__Widget__Group_1__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWidgetAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group__1__Impl


    // $ANTLR start rule__Widget__Group__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:814:1: rule__Widget__Group__2 : rule__Widget__Group__2__Impl rule__Widget__Group__3 ;
    public final void rule__Widget__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:818:1: ( rule__Widget__Group__2__Impl rule__Widget__Group__3 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:819:2: rule__Widget__Group__2__Impl rule__Widget__Group__3
            {
            pushFollow(FOLLOW_rule__Widget__Group__2__Impl_in_rule__Widget__Group__21627);
            rule__Widget__Group__2__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group__3_in_rule__Widget__Group__21630);
            rule__Widget__Group__3();
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
    // $ANTLR end rule__Widget__Group__2


    // $ANTLR start rule__Widget__Group__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:826:1: rule__Widget__Group__2__Impl : ( ( rule__Widget__Group_2__0 )? ) ;
    public final void rule__Widget__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:830:1: ( ( ( rule__Widget__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:831:1: ( ( rule__Widget__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:831:1: ( ( rule__Widget__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:832:1: ( rule__Widget__Group_2__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:833:1: ( rule__Widget__Group_2__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==15) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:833:2: rule__Widget__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_2__0_in_rule__Widget__Group__2__Impl1657);
                    rule__Widget__Group_2__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWidgetAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group__2__Impl


    // $ANTLR start rule__Widget__Group__3
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:843:1: rule__Widget__Group__3 : rule__Widget__Group__3__Impl ;
    public final void rule__Widget__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:847:1: ( rule__Widget__Group__3__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:848:2: rule__Widget__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group__3__Impl_in_rule__Widget__Group__31688);
            rule__Widget__Group__3__Impl();
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
    // $ANTLR end rule__Widget__Group__3


    // $ANTLR start rule__Widget__Group__3__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:854:1: rule__Widget__Group__3__Impl : ( ( rule__Widget__Group_3__0 )? ) ;
    public final void rule__Widget__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:858:1: ( ( ( rule__Widget__Group_3__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:859:1: ( ( rule__Widget__Group_3__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:859:1: ( ( rule__Widget__Group_3__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:860:1: ( rule__Widget__Group_3__0 )?
            {
             before(grammarAccess.getWidgetAccess().getGroup_3()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:861:1: ( rule__Widget__Group_3__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==12) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:861:2: rule__Widget__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__Widget__Group_3__0_in_rule__Widget__Group__3__Impl1715);
                    rule__Widget__Group_3__0();
                    _fsp--;


                    }
                    break;

            }

             after(grammarAccess.getWidgetAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group__3__Impl


    // $ANTLR start rule__Widget__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:879:1: rule__Widget__Group_1__0 : rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 ;
    public final void rule__Widget__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:883:1: ( rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:884:2: rule__Widget__Group_1__0__Impl rule__Widget__Group_1__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01754);
            rule__Widget__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01757);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:891:1: rule__Widget__Group_1__0__Impl : ( 'label=' ) ;
    public final void rule__Widget__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:895:1: ( ( 'label=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:896:1: ( 'label=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:896:1: ( 'label=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:897:1: 'label='
            {
             before(grammarAccess.getWidgetAccess().getLabelKeyword_1_0()); 
            match(input,14,FOLLOW_14_in_rule__Widget__Group_1__0__Impl1785); 
             after(grammarAccess.getWidgetAccess().getLabelKeyword_1_0()); 

            }


            }

        }
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:910:1: rule__Widget__Group_1__1 : rule__Widget__Group_1__1__Impl ;
    public final void rule__Widget__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:914:1: ( rule__Widget__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:915:2: rule__Widget__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11816);
            rule__Widget__Group_1__1__Impl();
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:921:1: rule__Widget__Group_1__1__Impl : ( ( rule__Widget__LabelAssignment_1_1 ) ) ;
    public final void rule__Widget__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:925:1: ( ( ( rule__Widget__LabelAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:926:1: ( ( rule__Widget__LabelAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:926:1: ( ( rule__Widget__LabelAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:927:1: ( rule__Widget__LabelAssignment_1_1 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:928:1: ( rule__Widget__LabelAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:928:2: rule__Widget__LabelAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Widget__LabelAssignment_1_1_in_rule__Widget__Group_1__1__Impl1843);
            rule__Widget__LabelAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getLabelAssignment_1_1()); 

            }


            }

        }
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


    // $ANTLR start rule__Widget__Group_2__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:942:1: rule__Widget__Group_2__0 : rule__Widget__Group_2__0__Impl rule__Widget__Group_2__1 ;
    public final void rule__Widget__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:946:1: ( rule__Widget__Group_2__0__Impl rule__Widget__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:947:2: rule__Widget__Group_2__0__Impl rule__Widget__Group_2__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_2__0__Impl_in_rule__Widget__Group_2__01877);
            rule__Widget__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_2__1_in_rule__Widget__Group_2__01880);
            rule__Widget__Group_2__1();
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
    // $ANTLR end rule__Widget__Group_2__0


    // $ANTLR start rule__Widget__Group_2__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:954:1: rule__Widget__Group_2__0__Impl : ( 'icon=' ) ;
    public final void rule__Widget__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:958:1: ( ( 'icon=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:959:1: ( 'icon=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:959:1: ( 'icon=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:960:1: 'icon='
            {
             before(grammarAccess.getWidgetAccess().getIconKeyword_2_0()); 
            match(input,15,FOLLOW_15_in_rule__Widget__Group_2__0__Impl1908); 
             after(grammarAccess.getWidgetAccess().getIconKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group_2__0__Impl


    // $ANTLR start rule__Widget__Group_2__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:973:1: rule__Widget__Group_2__1 : rule__Widget__Group_2__1__Impl ;
    public final void rule__Widget__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:977:1: ( rule__Widget__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:978:2: rule__Widget__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_2__1__Impl_in_rule__Widget__Group_2__11939);
            rule__Widget__Group_2__1__Impl();
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
    // $ANTLR end rule__Widget__Group_2__1


    // $ANTLR start rule__Widget__Group_2__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:984:1: rule__Widget__Group_2__1__Impl : ( ( rule__Widget__IconAssignment_2_1 ) ) ;
    public final void rule__Widget__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:988:1: ( ( ( rule__Widget__IconAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:989:1: ( ( rule__Widget__IconAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:989:1: ( ( rule__Widget__IconAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:990:1: ( rule__Widget__IconAssignment_2_1 )
            {
             before(grammarAccess.getWidgetAccess().getIconAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:991:1: ( rule__Widget__IconAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:991:2: rule__Widget__IconAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Widget__IconAssignment_2_1_in_rule__Widget__Group_2__1__Impl1966);
            rule__Widget__IconAssignment_2_1();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getIconAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group_2__1__Impl


    // $ANTLR start rule__Widget__Group_3__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1005:1: rule__Widget__Group_3__0 : rule__Widget__Group_3__0__Impl rule__Widget__Group_3__1 ;
    public final void rule__Widget__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1009:1: ( rule__Widget__Group_3__0__Impl rule__Widget__Group_3__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1010:2: rule__Widget__Group_3__0__Impl rule__Widget__Group_3__1
            {
            pushFollow(FOLLOW_rule__Widget__Group_3__0__Impl_in_rule__Widget__Group_3__02000);
            rule__Widget__Group_3__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_3__1_in_rule__Widget__Group_3__02003);
            rule__Widget__Group_3__1();
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
    // $ANTLR end rule__Widget__Group_3__0


    // $ANTLR start rule__Widget__Group_3__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1017:1: rule__Widget__Group_3__0__Impl : ( '{' ) ;
    public final void rule__Widget__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1021:1: ( ( '{' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1022:1: ( '{' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1022:1: ( '{' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1023:1: '{'
            {
             before(grammarAccess.getWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 
            match(input,12,FOLLOW_12_in_rule__Widget__Group_3__0__Impl2031); 
             after(grammarAccess.getWidgetAccess().getLeftCurlyBracketKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group_3__0__Impl


    // $ANTLR start rule__Widget__Group_3__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1036:1: rule__Widget__Group_3__1 : rule__Widget__Group_3__1__Impl rule__Widget__Group_3__2 ;
    public final void rule__Widget__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1040:1: ( rule__Widget__Group_3__1__Impl rule__Widget__Group_3__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1041:2: rule__Widget__Group_3__1__Impl rule__Widget__Group_3__2
            {
            pushFollow(FOLLOW_rule__Widget__Group_3__1__Impl_in_rule__Widget__Group_3__12062);
            rule__Widget__Group_3__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Widget__Group_3__2_in_rule__Widget__Group_3__12065);
            rule__Widget__Group_3__2();
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
    // $ANTLR end rule__Widget__Group_3__1


    // $ANTLR start rule__Widget__Group_3__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1048:1: rule__Widget__Group_3__1__Impl : ( ( ( rule__Widget__ChildrenAssignment_3_1 ) ) ( ( rule__Widget__ChildrenAssignment_3_1 )* ) ) ;
    public final void rule__Widget__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1052:1: ( ( ( ( rule__Widget__ChildrenAssignment_3_1 ) ) ( ( rule__Widget__ChildrenAssignment_3_1 )* ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1053:1: ( ( ( rule__Widget__ChildrenAssignment_3_1 ) ) ( ( rule__Widget__ChildrenAssignment_3_1 )* ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1053:1: ( ( ( rule__Widget__ChildrenAssignment_3_1 ) ) ( ( rule__Widget__ChildrenAssignment_3_1 )* ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1054:1: ( ( rule__Widget__ChildrenAssignment_3_1 ) ) ( ( rule__Widget__ChildrenAssignment_3_1 )* )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1054:1: ( ( rule__Widget__ChildrenAssignment_3_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1055:1: ( rule__Widget__ChildrenAssignment_3_1 )
            {
             before(grammarAccess.getWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1056:1: ( rule__Widget__ChildrenAssignment_3_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1056:2: rule__Widget__ChildrenAssignment_3_1
            {
            pushFollow(FOLLOW_rule__Widget__ChildrenAssignment_3_1_in_rule__Widget__Group_3__1__Impl2094);
            rule__Widget__ChildrenAssignment_3_1();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getChildrenAssignment_3_1()); 

            }

            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1059:1: ( ( rule__Widget__ChildrenAssignment_3_1 )* )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1060:1: ( rule__Widget__ChildrenAssignment_3_1 )*
            {
             before(grammarAccess.getWidgetAccess().getChildrenAssignment_3_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1061:1: ( rule__Widget__ChildrenAssignment_3_1 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>=16 && LA10_0<=17)||(LA10_0>=19 && LA10_0<=20)||LA10_0==22) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1061:2: rule__Widget__ChildrenAssignment_3_1
            	    {
            	    pushFollow(FOLLOW_rule__Widget__ChildrenAssignment_3_1_in_rule__Widget__Group_3__1__Impl2106);
            	    rule__Widget__ChildrenAssignment_3_1();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

             after(grammarAccess.getWidgetAccess().getChildrenAssignment_3_1()); 

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
    // $ANTLR end rule__Widget__Group_3__1__Impl


    // $ANTLR start rule__Widget__Group_3__2
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1072:1: rule__Widget__Group_3__2 : rule__Widget__Group_3__2__Impl ;
    public final void rule__Widget__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1076:1: ( rule__Widget__Group_3__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1077:2: rule__Widget__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__Widget__Group_3__2__Impl_in_rule__Widget__Group_3__22139);
            rule__Widget__Group_3__2__Impl();
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
    // $ANTLR end rule__Widget__Group_3__2


    // $ANTLR start rule__Widget__Group_3__2__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1083:1: rule__Widget__Group_3__2__Impl : ( '}' ) ;
    public final void rule__Widget__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1087:1: ( ( '}' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1088:1: ( '}' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1088:1: ( '}' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1089:1: '}'
            {
             before(grammarAccess.getWidgetAccess().getRightCurlyBracketKeyword_3_2()); 
            match(input,13,FOLLOW_13_in_rule__Widget__Group_3__2__Impl2167); 
             after(grammarAccess.getWidgetAccess().getRightCurlyBracketKeyword_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__Group_3__2__Impl


    // $ANTLR start rule__Frame__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1108:1: rule__Frame__Group__0 : rule__Frame__Group__0__Impl rule__Frame__Group__1 ;
    public final void rule__Frame__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1112:1: ( rule__Frame__Group__0__Impl rule__Frame__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1113:2: rule__Frame__Group__0__Impl rule__Frame__Group__1
            {
            pushFollow(FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__02204);
            rule__Frame__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__02207);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1120:1: rule__Frame__Group__0__Impl : ( 'Frame' ) ;
    public final void rule__Frame__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1124:1: ( ( 'Frame' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1125:1: ( 'Frame' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1125:1: ( 'Frame' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1126:1: 'Frame'
            {
             before(grammarAccess.getFrameAccess().getFrameKeyword_0()); 
            match(input,16,FOLLOW_16_in_rule__Frame__Group__0__Impl2235); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1139:1: rule__Frame__Group__1 : rule__Frame__Group__1__Impl ;
    public final void rule__Frame__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1143:1: ( rule__Frame__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1144:2: rule__Frame__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__12266);
            rule__Frame__Group__1__Impl();
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1150:1: rule__Frame__Group__1__Impl : ( () ) ;
    public final void rule__Frame__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1154:1: ( ( () ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1155:1: ( () )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1155:1: ( () )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1156:1: ()
            {
             before(grammarAccess.getFrameAccess().getFrameAction_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1157:1: ()
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1159:1: 
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


    // $ANTLR start rule__Text__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1173:1: rule__Text__Group__0 : rule__Text__Group__0__Impl rule__Text__Group__1 ;
    public final void rule__Text__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1177:1: ( rule__Text__Group__0__Impl rule__Text__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1178:2: rule__Text__Group__0__Impl rule__Text__Group__1
            {
            pushFollow(FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__02328);
            rule__Text__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group__1_in_rule__Text__Group__02331);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1185:1: rule__Text__Group__0__Impl : ( 'Text' ) ;
    public final void rule__Text__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1189:1: ( ( 'Text' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1190:1: ( 'Text' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1190:1: ( 'Text' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1191:1: 'Text'
            {
             before(grammarAccess.getTextAccess().getTextKeyword_0()); 
            match(input,17,FOLLOW_17_in_rule__Text__Group__0__Impl2359); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1204:1: rule__Text__Group__1 : rule__Text__Group__1__Impl ;
    public final void rule__Text__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1208:1: ( rule__Text__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1209:2: rule__Text__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__12390);
            rule__Text__Group__1__Impl();
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1215:1: rule__Text__Group__1__Impl : ( ( rule__Text__Group_1__0 ) ) ;
    public final void rule__Text__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1219:1: ( ( ( rule__Text__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1220:1: ( ( rule__Text__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1220:1: ( ( rule__Text__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1221:1: ( rule__Text__Group_1__0 )
            {
             before(grammarAccess.getTextAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1222:1: ( rule__Text__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1222:2: rule__Text__Group_1__0
            {
            pushFollow(FOLLOW_rule__Text__Group_1__0_in_rule__Text__Group__1__Impl2417);
            rule__Text__Group_1__0();
            _fsp--;


            }

             after(grammarAccess.getTextAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Text__Group__1__Impl


    // $ANTLR start rule__Text__Group_1__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1236:1: rule__Text__Group_1__0 : rule__Text__Group_1__0__Impl rule__Text__Group_1__1 ;
    public final void rule__Text__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1240:1: ( rule__Text__Group_1__0__Impl rule__Text__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1241:2: rule__Text__Group_1__0__Impl rule__Text__Group_1__1
            {
            pushFollow(FOLLOW_rule__Text__Group_1__0__Impl_in_rule__Text__Group_1__02451);
            rule__Text__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Text__Group_1__1_in_rule__Text__Group_1__02454);
            rule__Text__Group_1__1();
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
    // $ANTLR end rule__Text__Group_1__0


    // $ANTLR start rule__Text__Group_1__0__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1248:1: rule__Text__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Text__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1252:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1253:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1253:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1254:1: 'item='
            {
             before(grammarAccess.getTextAccess().getItemKeyword_1_0()); 
            match(input,18,FOLLOW_18_in_rule__Text__Group_1__0__Impl2482); 
             after(grammarAccess.getTextAccess().getItemKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Text__Group_1__0__Impl


    // $ANTLR start rule__Text__Group_1__1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1267:1: rule__Text__Group_1__1 : rule__Text__Group_1__1__Impl ;
    public final void rule__Text__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1271:1: ( rule__Text__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1272:2: rule__Text__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Text__Group_1__1__Impl_in_rule__Text__Group_1__12513);
            rule__Text__Group_1__1__Impl();
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
    // $ANTLR end rule__Text__Group_1__1


    // $ANTLR start rule__Text__Group_1__1__Impl
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1278:1: rule__Text__Group_1__1__Impl : ( ( rule__Text__ItemAssignment_1_1 ) ) ;
    public final void rule__Text__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1282:1: ( ( ( rule__Text__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1283:1: ( ( rule__Text__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1283:1: ( ( rule__Text__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1284:1: ( rule__Text__ItemAssignment_1_1 )
            {
             before(grammarAccess.getTextAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1285:1: ( rule__Text__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1285:2: rule__Text__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Text__ItemAssignment_1_1_in_rule__Text__Group_1__1__Impl2540);
            rule__Text__ItemAssignment_1_1();
            _fsp--;


            }

             after(grammarAccess.getTextAccess().getItemAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Text__Group_1__1__Impl


    // $ANTLR start rule__Group__Group__0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1299:1: rule__Group__Group__0 : rule__Group__Group__0__Impl rule__Group__Group__1 ;
    public final void rule__Group__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1303:1: ( rule__Group__Group__0__Impl rule__Group__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1304:2: rule__Group__Group__0__Impl rule__Group__Group__1
            {
            pushFollow(FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__02574);
            rule__Group__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group__1_in_rule__Group__Group__02577);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1311:1: rule__Group__Group__0__Impl : ( 'Group' ) ;
    public final void rule__Group__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1315:1: ( ( 'Group' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: ( 'Group' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1316:1: ( 'Group' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1317:1: 'Group'
            {
             before(grammarAccess.getGroupAccess().getGroupKeyword_0()); 
            match(input,19,FOLLOW_19_in_rule__Group__Group__0__Impl2605); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1330:1: rule__Group__Group__1 : rule__Group__Group__1__Impl ;
    public final void rule__Group__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1334:1: ( rule__Group__Group__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1335:2: rule__Group__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__12636);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1341:1: rule__Group__Group__1__Impl : ( ( rule__Group__Group_1__0 ) ) ;
    public final void rule__Group__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1345:1: ( ( ( rule__Group__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1346:1: ( ( rule__Group__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1346:1: ( ( rule__Group__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1347:1: ( rule__Group__Group_1__0 )
            {
             before(grammarAccess.getGroupAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1348:1: ( rule__Group__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1348:2: rule__Group__Group_1__0
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl2663);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1362:1: rule__Group__Group_1__0 : rule__Group__Group_1__0__Impl rule__Group__Group_1__1 ;
    public final void rule__Group__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1366:1: ( rule__Group__Group_1__0__Impl rule__Group__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1367:2: rule__Group__Group_1__0__Impl rule__Group__Group_1__1
            {
            pushFollow(FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__02697);
            rule__Group__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__02700);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1374:1: rule__Group__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Group__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1378:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1379:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1380:1: 'item='
            {
             before(grammarAccess.getGroupAccess().getItemKeyword_1_0()); 
            match(input,18,FOLLOW_18_in_rule__Group__Group_1__0__Impl2728); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1393:1: rule__Group__Group_1__1 : rule__Group__Group_1__1__Impl ;
    public final void rule__Group__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1397:1: ( rule__Group__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1398:2: rule__Group__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__12759);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1404:1: rule__Group__Group_1__1__Impl : ( ( rule__Group__ItemAssignment_1_1 ) ) ;
    public final void rule__Group__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1408:1: ( ( ( rule__Group__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1409:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1409:1: ( ( rule__Group__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1410:1: ( rule__Group__ItemAssignment_1_1 )
            {
             before(grammarAccess.getGroupAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1411:1: ( rule__Group__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1411:2: rule__Group__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl2786);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1425:1: rule__Image__Group__0 : rule__Image__Group__0__Impl rule__Image__Group__1 ;
    public final void rule__Image__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1429:1: ( rule__Image__Group__0__Impl rule__Image__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1430:2: rule__Image__Group__0__Impl rule__Image__Group__1
            {
            pushFollow(FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__02820);
            rule__Image__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__1_in_rule__Image__Group__02823);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1437:1: rule__Image__Group__0__Impl : ( 'Image' ) ;
    public final void rule__Image__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1441:1: ( ( 'Image' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1442:1: ( 'Image' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1442:1: ( 'Image' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1443:1: 'Image'
            {
             before(grammarAccess.getImageAccess().getImageKeyword_0()); 
            match(input,20,FOLLOW_20_in_rule__Image__Group__0__Impl2851); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1456:1: rule__Image__Group__1 : rule__Image__Group__1__Impl rule__Image__Group__2 ;
    public final void rule__Image__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1460:1: ( rule__Image__Group__1__Impl rule__Image__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1461:2: rule__Image__Group__1__Impl rule__Image__Group__2
            {
            pushFollow(FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__12882);
            rule__Image__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group__2_in_rule__Image__Group__12885);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1468:1: rule__Image__Group__1__Impl : ( ( rule__Image__Group_1__0 )? ) ;
    public final void rule__Image__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1472:1: ( ( ( rule__Image__Group_1__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1473:1: ( ( rule__Image__Group_1__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1473:1: ( ( rule__Image__Group_1__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1474:1: ( rule__Image__Group_1__0 )?
            {
             before(grammarAccess.getImageAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1475:1: ( rule__Image__Group_1__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==18) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1475:2: rule__Image__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl2912);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1485:1: rule__Image__Group__2 : rule__Image__Group__2__Impl ;
    public final void rule__Image__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1489:1: ( rule__Image__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1490:2: rule__Image__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__22943);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1496:1: rule__Image__Group__2__Impl : ( ( rule__Image__Group_2__0 ) ) ;
    public final void rule__Image__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1500:1: ( ( ( rule__Image__Group_2__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1501:1: ( ( rule__Image__Group_2__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1501:1: ( ( rule__Image__Group_2__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1502:1: ( rule__Image__Group_2__0 )
            {
             before(grammarAccess.getImageAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1503:1: ( rule__Image__Group_2__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1503:2: rule__Image__Group_2__0
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl2970);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1519:1: rule__Image__Group_1__0 : rule__Image__Group_1__0__Impl rule__Image__Group_1__1 ;
    public final void rule__Image__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1523:1: ( rule__Image__Group_1__0__Impl rule__Image__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1524:2: rule__Image__Group_1__0__Impl rule__Image__Group_1__1
            {
            pushFollow(FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__03006);
            rule__Image__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__03009);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1531:1: rule__Image__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Image__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1535:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1536:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1536:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1537:1: 'item='
            {
             before(grammarAccess.getImageAccess().getItemKeyword_1_0()); 
            match(input,18,FOLLOW_18_in_rule__Image__Group_1__0__Impl3037); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1550:1: rule__Image__Group_1__1 : rule__Image__Group_1__1__Impl ;
    public final void rule__Image__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1554:1: ( rule__Image__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1555:2: rule__Image__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__13068);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1561:1: rule__Image__Group_1__1__Impl : ( ( rule__Image__ItemAssignment_1_1 ) ) ;
    public final void rule__Image__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1565:1: ( ( ( rule__Image__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1566:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1566:1: ( ( rule__Image__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1567:1: ( rule__Image__ItemAssignment_1_1 )
            {
             before(grammarAccess.getImageAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1568:1: ( rule__Image__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1568:2: rule__Image__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl3095);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1582:1: rule__Image__Group_2__0 : rule__Image__Group_2__0__Impl rule__Image__Group_2__1 ;
    public final void rule__Image__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1586:1: ( rule__Image__Group_2__0__Impl rule__Image__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1587:2: rule__Image__Group_2__0__Impl rule__Image__Group_2__1
            {
            pushFollow(FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__03129);
            rule__Image__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__03132);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1594:1: rule__Image__Group_2__0__Impl : ( 'url=' ) ;
    public final void rule__Image__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1598:1: ( ( 'url=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1599:1: ( 'url=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1599:1: ( 'url=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1600:1: 'url='
            {
             before(grammarAccess.getImageAccess().getUrlKeyword_2_0()); 
            match(input,21,FOLLOW_21_in_rule__Image__Group_2__0__Impl3160); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1613:1: rule__Image__Group_2__1 : rule__Image__Group_2__1__Impl ;
    public final void rule__Image__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1617:1: ( rule__Image__Group_2__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1618:2: rule__Image__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__13191);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1624:1: rule__Image__Group_2__1__Impl : ( ( rule__Image__UrlAssignment_2_1 ) ) ;
    public final void rule__Image__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1628:1: ( ( ( rule__Image__UrlAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1629:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1629:1: ( ( rule__Image__UrlAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1630:1: ( rule__Image__UrlAssignment_2_1 )
            {
             before(grammarAccess.getImageAccess().getUrlAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1631:1: ( rule__Image__UrlAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1631:2: rule__Image__UrlAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl3218);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1645:1: rule__Switch__Group__0 : rule__Switch__Group__0__Impl rule__Switch__Group__1 ;
    public final void rule__Switch__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1649:1: ( rule__Switch__Group__0__Impl rule__Switch__Group__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1650:2: rule__Switch__Group__0__Impl rule__Switch__Group__1
            {
            pushFollow(FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__03252);
            rule__Switch__Group__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__03255);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1657:1: rule__Switch__Group__0__Impl : ( 'Switch' ) ;
    public final void rule__Switch__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1661:1: ( ( 'Switch' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1662:1: ( 'Switch' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1662:1: ( 'Switch' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1663:1: 'Switch'
            {
             before(grammarAccess.getSwitchAccess().getSwitchKeyword_0()); 
            match(input,22,FOLLOW_22_in_rule__Switch__Group__0__Impl3283); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1676:1: rule__Switch__Group__1 : rule__Switch__Group__1__Impl rule__Switch__Group__2 ;
    public final void rule__Switch__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1680:1: ( rule__Switch__Group__1__Impl rule__Switch__Group__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1681:2: rule__Switch__Group__1__Impl rule__Switch__Group__2
            {
            pushFollow(FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__13314);
            rule__Switch__Group__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__13317);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1688:1: rule__Switch__Group__1__Impl : ( ( rule__Switch__Group_1__0 ) ) ;
    public final void rule__Switch__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1692:1: ( ( ( rule__Switch__Group_1__0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1693:1: ( ( rule__Switch__Group_1__0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1693:1: ( ( rule__Switch__Group_1__0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1694:1: ( rule__Switch__Group_1__0 )
            {
             before(grammarAccess.getSwitchAccess().getGroup_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1695:1: ( rule__Switch__Group_1__0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1695:2: rule__Switch__Group_1__0
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl3344);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1705:1: rule__Switch__Group__2 : rule__Switch__Group__2__Impl ;
    public final void rule__Switch__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1709:1: ( rule__Switch__Group__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1710:2: rule__Switch__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__23374);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1716:1: rule__Switch__Group__2__Impl : ( ( rule__Switch__Group_2__0 )? ) ;
    public final void rule__Switch__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1720:1: ( ( ( rule__Switch__Group_2__0 )? ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1721:1: ( ( rule__Switch__Group_2__0 )? )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1721:1: ( ( rule__Switch__Group_2__0 )? )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1722:1: ( rule__Switch__Group_2__0 )?
            {
             before(grammarAccess.getSwitchAccess().getGroup_2()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1723:1: ( rule__Switch__Group_2__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==23) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1723:2: rule__Switch__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl3401);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1739:1: rule__Switch__Group_1__0 : rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 ;
    public final void rule__Switch__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1743:1: ( rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1744:2: rule__Switch__Group_1__0__Impl rule__Switch__Group_1__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__03438);
            rule__Switch__Group_1__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__03441);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1751:1: rule__Switch__Group_1__0__Impl : ( 'item=' ) ;
    public final void rule__Switch__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1755:1: ( ( 'item=' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1756:1: ( 'item=' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1756:1: ( 'item=' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1757:1: 'item='
            {
             before(grammarAccess.getSwitchAccess().getItemKeyword_1_0()); 
            match(input,18,FOLLOW_18_in_rule__Switch__Group_1__0__Impl3469); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1770:1: rule__Switch__Group_1__1 : rule__Switch__Group_1__1__Impl ;
    public final void rule__Switch__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1774:1: ( rule__Switch__Group_1__1__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1775:2: rule__Switch__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__13500);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1781:1: rule__Switch__Group_1__1__Impl : ( ( rule__Switch__ItemAssignment_1_1 ) ) ;
    public final void rule__Switch__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1785:1: ( ( ( rule__Switch__ItemAssignment_1_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1786:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1786:1: ( ( rule__Switch__ItemAssignment_1_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1787:1: ( rule__Switch__ItemAssignment_1_1 )
            {
             before(grammarAccess.getSwitchAccess().getItemAssignment_1_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1788:1: ( rule__Switch__ItemAssignment_1_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1788:2: rule__Switch__ItemAssignment_1_1
            {
            pushFollow(FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl3527);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1802:1: rule__Switch__Group_2__0 : rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 ;
    public final void rule__Switch__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1806:1: ( rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1807:2: rule__Switch__Group_2__0__Impl rule__Switch__Group_2__1
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__03561);
            rule__Switch__Group_2__0__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__03564);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1814:1: rule__Switch__Group_2__0__Impl : ( 'buttonLabels=[' ) ;
    public final void rule__Switch__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1818:1: ( ( 'buttonLabels=[' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1819:1: ( 'buttonLabels=[' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1819:1: ( 'buttonLabels=[' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1820:1: 'buttonLabels=['
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsKeyword_2_0()); 
            match(input,23,FOLLOW_23_in_rule__Switch__Group_2__0__Impl3592); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1833:1: rule__Switch__Group_2__1 : rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 ;
    public final void rule__Switch__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1837:1: ( rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1838:2: rule__Switch__Group_2__1__Impl rule__Switch__Group_2__2
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__13623);
            rule__Switch__Group_2__1__Impl();
            _fsp--;

            pushFollow(FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__13626);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1845:1: rule__Switch__Group_2__1__Impl : ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) ;
    public final void rule__Switch__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1849:1: ( ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1850:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1850:1: ( ( rule__Switch__ButtonLabelsAssignment_2_1 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1851:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsAssignment_2_1()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1852:1: ( rule__Switch__ButtonLabelsAssignment_2_1 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1852:2: rule__Switch__ButtonLabelsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl3653);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1862:1: rule__Switch__Group_2__2 : rule__Switch__Group_2__2__Impl ;
    public final void rule__Switch__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1866:1: ( rule__Switch__Group_2__2__Impl )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1867:2: rule__Switch__Group_2__2__Impl
            {
            pushFollow(FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__23683);
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1873:1: rule__Switch__Group_2__2__Impl : ( ']' ) ;
    public final void rule__Switch__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1877:1: ( ( ']' ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1878:1: ( ']' )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1878:1: ( ']' )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1879:1: ']'
            {
             before(grammarAccess.getSwitchAccess().getRightSquareBracketKeyword_2_2()); 
            match(input,24,FOLLOW_24_in_rule__Switch__Group_2__2__Impl3711); 
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


    // $ANTLR start rule__Sitemap__NameAssignment_0
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1899:1: rule__Sitemap__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Sitemap__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1903:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1904:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1904:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1905:1: RULE_ID
            {
             before(grammarAccess.getSitemapAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_03753); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1914:1: rule__Sitemap__LabelAssignment_1_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1918:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1919:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1919:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1920:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getLabelSTRINGTerminalRuleCall_1_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_13784); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1929:1: rule__Sitemap__IconAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Sitemap__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1933:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1934:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1934:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1935:1: RULE_STRING
            {
             before(grammarAccess.getSitemapAccess().getIconSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_13815); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1944:1: rule__Sitemap__ChildrenAssignment_4 : ( ruleWidget ) ;
    public final void rule__Sitemap__ChildrenAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1948:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1949:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1949:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1950:1: ruleWidget
            {
             before(grammarAccess.getSitemapAccess().getChildrenWidgetParserRuleCall_4_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_43846);
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


    // $ANTLR start rule__Widget__LabelAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1959:1: rule__Widget__LabelAssignment_1_1 : ( ( rule__Widget__LabelAlternatives_1_1_0 ) ) ;
    public final void rule__Widget__LabelAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1963:1: ( ( ( rule__Widget__LabelAlternatives_1_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1964:1: ( ( rule__Widget__LabelAlternatives_1_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1964:1: ( ( rule__Widget__LabelAlternatives_1_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1965:1: ( rule__Widget__LabelAlternatives_1_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1966:1: ( rule__Widget__LabelAlternatives_1_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1966:2: rule__Widget__LabelAlternatives_1_1_0
            {
            pushFollow(FOLLOW_rule__Widget__LabelAlternatives_1_1_0_in_rule__Widget__LabelAssignment_1_13877);
            rule__Widget__LabelAlternatives_1_1_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getLabelAlternatives_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__LabelAssignment_1_1


    // $ANTLR start rule__Widget__IconAssignment_2_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1975:1: rule__Widget__IconAssignment_2_1 : ( ( rule__Widget__IconAlternatives_2_1_0 ) ) ;
    public final void rule__Widget__IconAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1979:1: ( ( ( rule__Widget__IconAlternatives_2_1_0 ) ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1980:1: ( ( rule__Widget__IconAlternatives_2_1_0 ) )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1980:1: ( ( rule__Widget__IconAlternatives_2_1_0 ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1981:1: ( rule__Widget__IconAlternatives_2_1_0 )
            {
             before(grammarAccess.getWidgetAccess().getIconAlternatives_2_1_0()); 
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1982:1: ( rule__Widget__IconAlternatives_2_1_0 )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1982:2: rule__Widget__IconAlternatives_2_1_0
            {
            pushFollow(FOLLOW_rule__Widget__IconAlternatives_2_1_0_in_rule__Widget__IconAssignment_2_13910);
            rule__Widget__IconAlternatives_2_1_0();
            _fsp--;


            }

             after(grammarAccess.getWidgetAccess().getIconAlternatives_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__IconAssignment_2_1


    // $ANTLR start rule__Widget__ChildrenAssignment_3_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1991:1: rule__Widget__ChildrenAssignment_3_1 : ( ruleWidget ) ;
    public final void rule__Widget__ChildrenAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1995:1: ( ( ruleWidget ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1996:1: ( ruleWidget )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1996:1: ( ruleWidget )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:1997:1: ruleWidget
            {
             before(grammarAccess.getWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_ruleWidget_in_rule__Widget__ChildrenAssignment_3_13943);
            ruleWidget();
            _fsp--;

             after(grammarAccess.getWidgetAccess().getChildrenWidgetParserRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Widget__ChildrenAssignment_3_1


    // $ANTLR start rule__Text__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2006:1: rule__Text__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Text__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2010:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2011:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2011:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2012:1: RULE_ID
            {
             before(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_1_13974); 
             after(grammarAccess.getTextAccess().getItemIDTerminalRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end rule__Text__ItemAssignment_1_1


    // $ANTLR start rule__Group__ItemAssignment_1_1
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2021:1: rule__Group__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Group__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2025:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2026:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2026:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2027:1: RULE_ID
            {
             before(grammarAccess.getGroupAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_14005); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2036:1: rule__Image__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Image__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2040:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2041:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2041:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2042:1: RULE_ID
            {
             before(grammarAccess.getImageAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_14036); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2051:1: rule__Image__UrlAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Image__UrlAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2055:1: ( ( RULE_STRING ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2056:1: ( RULE_STRING )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2056:1: ( RULE_STRING )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2057:1: RULE_STRING
            {
             before(grammarAccess.getImageAccess().getUrlSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_14067); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2066:1: rule__Switch__ItemAssignment_1_1 : ( RULE_ID ) ;
    public final void rule__Switch__ItemAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2070:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2071:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2071:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2072:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getItemIDTerminalRuleCall_1_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_14098); 
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
    // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2081:1: rule__Switch__ButtonLabelsAssignment_2_1 : ( RULE_ID ) ;
    public final void rule__Switch__ButtonLabelsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2085:1: ( ( RULE_ID ) )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2086:1: ( RULE_ID )
            {
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2086:1: ( RULE_ID )
            // ../org.openhab.model.sitemap.ui/src-gen/org/openhab/model/ui/contentassist/antlr/internal/InternalSitemap.g:2087:1: RULE_ID
            {
             before(grammarAccess.getSwitchAccess().getButtonLabelsIDTerminalRuleCall_2_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_14129); 
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


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Model__Group__0_in_ruleModel94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_entryRuleSitemap121 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSitemap128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0_in_ruleSitemap154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_entryRuleWidget181 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWidget188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group__0_in_ruleWidget214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_entryRuleFrame241 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFrame248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0_in_ruleFrame274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_entryRuleText301 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleText308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0_in_ruleText334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_entryRuleGroup361 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroup368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0_in_ruleGroup394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_entryRuleImage421 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImage428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0_in_ruleImage454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_entryRuleSwitch481 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSwitch488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0_in_ruleSwitch514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleText_in_rule__Widget__Alternatives_0550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroup_in_rule__Widget__Alternatives_0567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImage_in_rule__Widget__Alternatives_0584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSwitch_in_rule__Widget__Alternatives_0601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFrame_in_rule__Widget__Alternatives_0618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__LabelAlternatives_1_1_0650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__LabelAlternatives_1_1_0667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Widget__IconAlternatives_2_1_0699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Widget__IconAlternatives_2_1_0716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Model__Group__0__Impl_in_rule__Model__Group__0746 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Model__Group__1_in_rule__Model__Group__0749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__Model__Group__0__Impl777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Model__Group__1__Impl_in_rule__Model__Group__1808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSitemap_in_rule__Model__Group__1__Impl835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__0__Impl_in_rule__Sitemap__Group__0868 = new BitSet(new long[]{0x000000000000D000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1_in_rule__Sitemap__Group__0871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__NameAssignment_0_in_rule__Sitemap__Group__0__Impl898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__1__Impl_in_rule__Sitemap__Group__1928 = new BitSet(new long[]{0x0000000000009000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2_in_rule__Sitemap__Group__1931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0_in_rule__Sitemap__Group__1__Impl958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__2__Impl_in_rule__Sitemap__Group__2989 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3_in_rule__Sitemap__Group__2992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0_in_rule__Sitemap__Group__2__Impl1019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__3__Impl_in_rule__Sitemap__Group__31050 = new BitSet(new long[]{0x00000000005B0000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4_in_rule__Sitemap__Group__31053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Sitemap__Group__3__Impl1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__4__Impl_in_rule__Sitemap__Group__41112 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5_in_rule__Sitemap__Group__41115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1144 = new BitSet(new long[]{0x00000000005B0002L});
    public static final BitSet FOLLOW_rule__Sitemap__ChildrenAssignment_4_in_rule__Sitemap__Group__4__Impl1156 = new BitSet(new long[]{0x00000000005B0002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group__5__Impl_in_rule__Sitemap__Group__51189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Sitemap__Group__5__Impl1217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__0__Impl_in_rule__Sitemap__Group_1__01260 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1_in_rule__Sitemap__Group_1__01263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Sitemap__Group_1__0__Impl1291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_1__1__Impl_in_rule__Sitemap__Group_1__11322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__LabelAssignment_1_1_in_rule__Sitemap__Group_1__1__Impl1349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__0__Impl_in_rule__Sitemap__Group_2__01383 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1_in_rule__Sitemap__Group_2__01386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Sitemap__Group_2__0__Impl1414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__Group_2__1__Impl_in_rule__Sitemap__Group_2__11445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Sitemap__IconAssignment_2_1_in_rule__Sitemap__Group_2__1__Impl1472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group__0__Impl_in_rule__Widget__Group__01506 = new BitSet(new long[]{0x000000000000D002L});
    public static final BitSet FOLLOW_rule__Widget__Group__1_in_rule__Widget__Group__01509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Alternatives_0_in_rule__Widget__Group__0__Impl1536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group__1__Impl_in_rule__Widget__Group__11566 = new BitSet(new long[]{0x0000000000009002L});
    public static final BitSet FOLLOW_rule__Widget__Group__2_in_rule__Widget__Group__11569 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0_in_rule__Widget__Group__1__Impl1596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group__2__Impl_in_rule__Widget__Group__21627 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__Widget__Group__3_in_rule__Widget__Group__21630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_2__0_in_rule__Widget__Group__2__Impl1657 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group__3__Impl_in_rule__Widget__Group__31688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__0_in_rule__Widget__Group__3__Impl1715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__0__Impl_in_rule__Widget__Group_1__01754 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1_in_rule__Widget__Group_1__01757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Widget__Group_1__0__Impl1785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_1__1__Impl_in_rule__Widget__Group_1__11816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAssignment_1_1_in_rule__Widget__Group_1__1__Impl1843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_2__0__Impl_in_rule__Widget__Group_2__01877 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Widget__Group_2__1_in_rule__Widget__Group_2__01880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Widget__Group_2__0__Impl1908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_2__1__Impl_in_rule__Widget__Group_2__11939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAssignment_2_1_in_rule__Widget__Group_2__1__Impl1966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__0__Impl_in_rule__Widget__Group_3__02000 = new BitSet(new long[]{0x00000000005B0000L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__1_in_rule__Widget__Group_3__02003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Widget__Group_3__0__Impl2031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__1__Impl_in_rule__Widget__Group_3__12062 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__2_in_rule__Widget__Group_3__12065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__ChildrenAssignment_3_1_in_rule__Widget__Group_3__1__Impl2094 = new BitSet(new long[]{0x00000000005B0002L});
    public static final BitSet FOLLOW_rule__Widget__ChildrenAssignment_3_1_in_rule__Widget__Group_3__1__Impl2106 = new BitSet(new long[]{0x00000000005B0002L});
    public static final BitSet FOLLOW_rule__Widget__Group_3__2__Impl_in_rule__Widget__Group_3__22139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Widget__Group_3__2__Impl2167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__0__Impl_in_rule__Frame__Group__02204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1_in_rule__Frame__Group__02207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Frame__Group__0__Impl2235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Frame__Group__1__Impl_in_rule__Frame__Group__12266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__0__Impl_in_rule__Text__Group__02328 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_rule__Text__Group__1_in_rule__Text__Group__02331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Text__Group__0__Impl2359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group__1__Impl_in_rule__Text__Group__12390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_1__0_in_rule__Text__Group__1__Impl2417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_1__0__Impl_in_rule__Text__Group_1__02451 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Text__Group_1__1_in_rule__Text__Group_1__02454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Text__Group_1__0__Impl2482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__Group_1__1__Impl_in_rule__Text__Group_1__12513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Text__ItemAssignment_1_1_in_rule__Text__Group_1__1__Impl2540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__0__Impl_in_rule__Group__Group__02574 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_rule__Group__Group__1_in_rule__Group__Group__02577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Group__Group__0__Impl2605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group__1__Impl_in_rule__Group__Group__12636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0_in_rule__Group__Group__1__Impl2663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__0__Impl_in_rule__Group__Group_1__02697 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1_in_rule__Group__Group_1__02700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Group__Group_1__0__Impl2728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__Group_1__1__Impl_in_rule__Group__Group_1__12759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Group__ItemAssignment_1_1_in_rule__Group__Group_1__1__Impl2786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__0__Impl_in_rule__Image__Group__02820 = new BitSet(new long[]{0x0000000000240000L});
    public static final BitSet FOLLOW_rule__Image__Group__1_in_rule__Image__Group__02823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Image__Group__0__Impl2851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__1__Impl_in_rule__Image__Group__12882 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__Image__Group__2_in_rule__Image__Group__12885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0_in_rule__Image__Group__1__Impl2912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group__2__Impl_in_rule__Image__Group__22943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0_in_rule__Image__Group__2__Impl2970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__0__Impl_in_rule__Image__Group_1__03006 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1_in_rule__Image__Group_1__03009 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Image__Group_1__0__Impl3037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_1__1__Impl_in_rule__Image__Group_1__13068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__ItemAssignment_1_1_in_rule__Image__Group_1__1__Impl3095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__0__Impl_in_rule__Image__Group_2__03129 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1_in_rule__Image__Group_2__03132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Image__Group_2__0__Impl3160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__Group_2__1__Impl_in_rule__Image__Group_2__13191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Image__UrlAssignment_2_1_in_rule__Image__Group_2__1__Impl3218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__0__Impl_in_rule__Switch__Group__03252 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_rule__Switch__Group__1_in_rule__Switch__Group__03255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Switch__Group__0__Impl3283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__1__Impl_in_rule__Switch__Group__13314 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2_in_rule__Switch__Group__13317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0_in_rule__Switch__Group__1__Impl3344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group__2__Impl_in_rule__Switch__Group__23374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0_in_rule__Switch__Group__2__Impl3401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__0__Impl_in_rule__Switch__Group_1__03438 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1_in_rule__Switch__Group_1__03441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Switch__Group_1__0__Impl3469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_1__1__Impl_in_rule__Switch__Group_1__13500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ItemAssignment_1_1_in_rule__Switch__Group_1__1__Impl3527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__0__Impl_in_rule__Switch__Group_2__03561 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1_in_rule__Switch__Group_2__03564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Switch__Group_2__0__Impl3592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__1__Impl_in_rule__Switch__Group_2__13623 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2_in_rule__Switch__Group_2__13626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__ButtonLabelsAssignment_2_1_in_rule__Switch__Group_2__1__Impl3653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Switch__Group_2__2__Impl_in_rule__Switch__Group_2__23683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Switch__Group_2__2__Impl3711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Sitemap__NameAssignment_03753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__LabelAssignment_1_13784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Sitemap__IconAssignment_2_13815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__Sitemap__ChildrenAssignment_43846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__LabelAlternatives_1_1_0_in_rule__Widget__LabelAssignment_1_13877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Widget__IconAlternatives_2_1_0_in_rule__Widget__IconAssignment_2_13910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWidget_in_rule__Widget__ChildrenAssignment_3_13943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Text__ItemAssignment_1_13974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Group__ItemAssignment_1_14005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Image__ItemAssignment_1_14036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Image__UrlAssignment_2_14067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ItemAssignment_1_14098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Switch__ButtonLabelsAssignment_2_14129 = new BitSet(new long[]{0x0000000000000002L});

}