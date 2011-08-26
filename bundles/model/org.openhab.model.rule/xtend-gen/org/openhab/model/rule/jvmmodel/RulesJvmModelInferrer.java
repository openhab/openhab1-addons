package org.openhab.model.rule.jvmmodel;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociator;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelInferrer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class RulesJvmModelInferrer implements IJvmModelInferrer {
  
  @Inject
  private IJvmModelAssociator jvmModelAssociator;
  
  public List<? extends JvmDeclaredType> inferJvmModel(final EObject sourceObject) {
    ArrayList<JvmDeclaredType> _newArrayList = CollectionLiterals.<JvmDeclaredType>newArrayList();
    return _newArrayList;
  }
}