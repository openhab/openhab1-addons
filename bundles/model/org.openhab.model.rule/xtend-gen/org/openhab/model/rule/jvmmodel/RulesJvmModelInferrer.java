package org.openhab.model.rule.jvmmodel;

import java.util.Arrays;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.openhab.model.rule.rules.RuleModel;
import org.openhab.model.script.jvmmodel.ScriptJvmModelInferrer;

/**
 * <p>Infers a JVM model from the source model.</p>
 * 
 * <p>The JVM model should contain all elements that would appear in the Java code
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>
 */
@SuppressWarnings("all")
public class RulesJvmModelInferrer extends ScriptJvmModelInferrer {
  /**
   * Is called for each instance of the first argument's type contained in a resource.
   * 
   * @param element - the model to create one or more JvmDeclaredTypes from.
   * @param acceptor - each created JvmDeclaredType without a container should be passed to the acceptor in order get attached to the
   *                   current resource.
   * @param isPreLinkingPhase - whether the method is called in a pre linking phase, i.e. when the global index isn't fully updated. You
   *        must not rely on linking using the index if iPrelinkingPhase is <code>true</code>
   */
  protected void _infer(final RuleModel element, final IAcceptor<JvmDeclaredType> acceptor, final boolean isPrelinkingPhase) {
  }
  
  public void infer(final EObject element, final Object acceptor, final boolean isPrelinkingPhase) {
    if (element instanceof RuleModel
         && acceptor instanceof IAcceptor) {
      _infer((RuleModel)element, (IAcceptor<JvmDeclaredType>)acceptor, isPrelinkingPhase);
      return;
    } else if (element != null
         && acceptor instanceof IJvmDeclaredTypeAcceptor) {
      _infer(element, (IJvmDeclaredTypeAcceptor)acceptor, isPrelinkingPhase);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(element, acceptor, isPrelinkingPhase).toString());
    }
  }
}
