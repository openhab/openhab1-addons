/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.openhab.model.sitemap.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.openhab.model.sitemap.SitemapPackage
 * @generated
 */
public class SitemapSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SitemapPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SitemapSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = SitemapPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case SitemapPackage.SITEMAP_MODEL:
      {
        SitemapModel sitemapModel = (SitemapModel)theEObject;
        T result = caseSitemapModel(sitemapModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.SITEMAP:
      {
        Sitemap sitemap = (Sitemap)theEObject;
        T result = caseSitemap(sitemap);
        if (result == null) result = caseSitemapModel(sitemap);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.WIDGET:
      {
        Widget widget = (Widget)theEObject;
        T result = caseWidget(widget);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.LINKABLE_WIDGET:
      {
        LinkableWidget linkableWidget = (LinkableWidget)theEObject;
        T result = caseLinkableWidget(linkableWidget);
        if (result == null) result = caseWidget(linkableWidget);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.FRAME:
      {
        Frame frame = (Frame)theEObject;
        T result = caseFrame(frame);
        if (result == null) result = caseLinkableWidget(frame);
        if (result == null) result = caseWidget(frame);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.TEXT:
      {
        Text text = (Text)theEObject;
        T result = caseText(text);
        if (result == null) result = caseLinkableWidget(text);
        if (result == null) result = caseWidget(text);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.GROUP:
      {
        Group group = (Group)theEObject;
        T result = caseGroup(group);
        if (result == null) result = caseLinkableWidget(group);
        if (result == null) result = caseWidget(group);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.IMAGE:
      {
        Image image = (Image)theEObject;
        T result = caseImage(image);
        if (result == null) result = caseLinkableWidget(image);
        if (result == null) result = caseWidget(image);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.SWITCH:
      {
        org.openhab.model.sitemap.Switch switch_ = (org.openhab.model.sitemap.Switch)theEObject;
        T result = caseSwitch(switch_);
        if (result == null) result = caseWidget(switch_);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.SLIDER:
      {
        Slider slider = (Slider)theEObject;
        T result = caseSlider(slider);
        if (result == null) result = caseWidget(slider);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.SELECTION:
      {
        Selection selection = (Selection)theEObject;
        T result = caseSelection(selection);
        if (result == null) result = caseWidget(selection);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.LIST:
      {
        List list = (List)theEObject;
        T result = caseList(list);
        if (result == null) result = caseWidget(list);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SitemapPackage.MAPPING:
      {
        Mapping mapping = (Mapping)theEObject;
        T result = caseMapping(mapping);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSitemapModel(SitemapModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Sitemap</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Sitemap</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSitemap(Sitemap object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Widget</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Widget</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWidget(Widget object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Linkable Widget</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Linkable Widget</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLinkableWidget(LinkableWidget object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Frame</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Frame</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFrame(Frame object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Text</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Text</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseText(Text object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Group</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGroup(Group object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Image</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Image</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseImage(Image object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Switch</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Switch</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSwitch(org.openhab.model.sitemap.Switch object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Slider</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Slider</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSlider(Slider object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Selection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Selection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelection(Selection object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseList(List object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Mapping</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Mapping</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMapping(Mapping object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //SitemapSwitch
