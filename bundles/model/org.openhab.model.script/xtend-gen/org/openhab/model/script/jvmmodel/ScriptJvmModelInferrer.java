package org.openhab.model.script.jvmmodel;

import com.google.inject.Inject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;

/**
 * <p>Infers a JVM model from the source model.</p>
 * 
 * <p>The JVM model should contain all elements that would appear in the Java code
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>
 */
@SuppressWarnings("all")
public class ScriptJvmModelInferrer extends AbstractModelInferrer {
  /**
   * conveninence API to build and initialize JvmTypes and their members.
   */
  @Inject
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  private IQualifiedNameProvider _iQualifiedNameProvider;
}
