/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.openhab.model.sitemap.SitemapFactory
 * @model kind="package"
 * @generated
 */
public interface SitemapPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "sitemap";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.openhab.org/model/Sitemap";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "sitemap";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SitemapPackage eINSTANCE = org.openhab.model.sitemap.impl.SitemapPackageImpl.init();

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.ModelImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.SitemapImpl <em>Sitemap</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.SitemapImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getSitemap()
   * @generated
   */
  int SITEMAP = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITEMAP__NAME = MODEL_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITEMAP__LABEL = MODEL_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITEMAP__ICON = MODEL_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITEMAP__CHILDREN = MODEL_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Sitemap</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SITEMAP_FEATURE_COUNT = MODEL_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.WidgetImpl <em>Widget</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.WidgetImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getWidget()
   * @generated
   */
  int WIDGET = 2;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WIDGET__LABEL = 0;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WIDGET__ICON = 1;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WIDGET__CHILDREN = 2;

  /**
   * The number of structural features of the '<em>Widget</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WIDGET_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.TextImpl <em>Text</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.TextImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getText()
   * @generated
   */
  int TEXT = 3;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT__LABEL = WIDGET__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT__ICON = WIDGET__ICON;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT__CHILDREN = WIDGET__CHILDREN;

  /**
   * The feature id for the '<em><b>Item</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT__ITEM = WIDGET_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Text</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_FEATURE_COUNT = WIDGET_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.GroupImpl <em>Group</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.GroupImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getGroup()
   * @generated
   */
  int GROUP = 4;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP__LABEL = WIDGET__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP__ICON = WIDGET__ICON;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP__CHILDREN = WIDGET__CHILDREN;

  /**
   * The feature id for the '<em><b>Item</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP__ITEM = WIDGET_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Group</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_FEATURE_COUNT = WIDGET_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.ImageImpl <em>Image</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.ImageImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getImage()
   * @generated
   */
  int IMAGE = 5;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__LABEL = WIDGET__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__ICON = WIDGET__ICON;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__CHILDREN = WIDGET__CHILDREN;

  /**
   * The feature id for the '<em><b>Url</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__URL = WIDGET_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Image</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE_FEATURE_COUNT = WIDGET_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.openhab.model.sitemap.impl.SwitchImpl <em>Switch</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.sitemap.impl.SwitchImpl
   * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getSwitch()
   * @generated
   */
  int SWITCH = 6;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH__LABEL = WIDGET__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH__ICON = WIDGET__ICON;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH__CHILDREN = WIDGET__CHILDREN;

  /**
   * The feature id for the '<em><b>Item</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH__ITEM = WIDGET_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Button Labels</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH__BUTTON_LABELS = WIDGET_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Switch</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SWITCH_FEATURE_COUNT = WIDGET_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see org.openhab.model.sitemap.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Sitemap <em>Sitemap</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Sitemap</em>'.
   * @see org.openhab.model.sitemap.Sitemap
   * @generated
   */
  EClass getSitemap();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Sitemap#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openhab.model.sitemap.Sitemap#getName()
   * @see #getSitemap()
   * @generated
   */
  EAttribute getSitemap_Name();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Sitemap#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.openhab.model.sitemap.Sitemap#getLabel()
   * @see #getSitemap()
   * @generated
   */
  EAttribute getSitemap_Label();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Sitemap#getIcon <em>Icon</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Icon</em>'.
   * @see org.openhab.model.sitemap.Sitemap#getIcon()
   * @see #getSitemap()
   * @generated
   */
  EAttribute getSitemap_Icon();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.sitemap.Sitemap#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Children</em>'.
   * @see org.openhab.model.sitemap.Sitemap#getChildren()
   * @see #getSitemap()
   * @generated
   */
  EReference getSitemap_Children();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Widget <em>Widget</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Widget</em>'.
   * @see org.openhab.model.sitemap.Widget
   * @generated
   */
  EClass getWidget();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Widget#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.openhab.model.sitemap.Widget#getLabel()
   * @see #getWidget()
   * @generated
   */
  EAttribute getWidget_Label();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Widget#getIcon <em>Icon</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Icon</em>'.
   * @see org.openhab.model.sitemap.Widget#getIcon()
   * @see #getWidget()
   * @generated
   */
  EAttribute getWidget_Icon();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.sitemap.Widget#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Children</em>'.
   * @see org.openhab.model.sitemap.Widget#getChildren()
   * @see #getWidget()
   * @generated
   */
  EReference getWidget_Children();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Text <em>Text</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Text</em>'.
   * @see org.openhab.model.sitemap.Text
   * @generated
   */
  EClass getText();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Text#getItem <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Item</em>'.
   * @see org.openhab.model.sitemap.Text#getItem()
   * @see #getText()
   * @generated
   */
  EAttribute getText_Item();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Group <em>Group</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Group</em>'.
   * @see org.openhab.model.sitemap.Group
   * @generated
   */
  EClass getGroup();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Group#getItem <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Item</em>'.
   * @see org.openhab.model.sitemap.Group#getItem()
   * @see #getGroup()
   * @generated
   */
  EAttribute getGroup_Item();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Image <em>Image</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Image</em>'.
   * @see org.openhab.model.sitemap.Image
   * @generated
   */
  EClass getImage();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Image#getUrl <em>Url</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Url</em>'.
   * @see org.openhab.model.sitemap.Image#getUrl()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Url();

  /**
   * Returns the meta object for class '{@link org.openhab.model.sitemap.Switch <em>Switch</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Switch</em>'.
   * @see org.openhab.model.sitemap.Switch
   * @generated
   */
  EClass getSwitch();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.sitemap.Switch#getItem <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Item</em>'.
   * @see org.openhab.model.sitemap.Switch#getItem()
   * @see #getSwitch()
   * @generated
   */
  EAttribute getSwitch_Item();

  /**
   * Returns the meta object for the attribute list '{@link org.openhab.model.sitemap.Switch#getButtonLabels <em>Button Labels</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Button Labels</em>'.
   * @see org.openhab.model.sitemap.Switch#getButtonLabels()
   * @see #getSwitch()
   * @generated
   */
  EAttribute getSwitch_ButtonLabels();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SitemapFactory getSitemapFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.ModelImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.SitemapImpl <em>Sitemap</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.SitemapImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getSitemap()
     * @generated
     */
    EClass SITEMAP = eINSTANCE.getSitemap();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SITEMAP__NAME = eINSTANCE.getSitemap_Name();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SITEMAP__LABEL = eINSTANCE.getSitemap_Label();

    /**
     * The meta object literal for the '<em><b>Icon</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SITEMAP__ICON = eINSTANCE.getSitemap_Icon();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SITEMAP__CHILDREN = eINSTANCE.getSitemap_Children();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.WidgetImpl <em>Widget</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.WidgetImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getWidget()
     * @generated
     */
    EClass WIDGET = eINSTANCE.getWidget();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WIDGET__LABEL = eINSTANCE.getWidget_Label();

    /**
     * The meta object literal for the '<em><b>Icon</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WIDGET__ICON = eINSTANCE.getWidget_Icon();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference WIDGET__CHILDREN = eINSTANCE.getWidget_Children();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.TextImpl <em>Text</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.TextImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getText()
     * @generated
     */
    EClass TEXT = eINSTANCE.getText();

    /**
     * The meta object literal for the '<em><b>Item</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TEXT__ITEM = eINSTANCE.getText_Item();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.GroupImpl <em>Group</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.GroupImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getGroup()
     * @generated
     */
    EClass GROUP = eINSTANCE.getGroup();

    /**
     * The meta object literal for the '<em><b>Item</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GROUP__ITEM = eINSTANCE.getGroup_Item();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.ImageImpl <em>Image</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.ImageImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getImage()
     * @generated
     */
    EClass IMAGE = eINSTANCE.getImage();

    /**
     * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__URL = eINSTANCE.getImage_Url();

    /**
     * The meta object literal for the '{@link org.openhab.model.sitemap.impl.SwitchImpl <em>Switch</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.sitemap.impl.SwitchImpl
     * @see org.openhab.model.sitemap.impl.SitemapPackageImpl#getSwitch()
     * @generated
     */
    EClass SWITCH = eINSTANCE.getSwitch();

    /**
     * The meta object literal for the '<em><b>Item</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SWITCH__ITEM = eINSTANCE.getSwitch_Item();

    /**
     * The meta object literal for the '<em><b>Button Labels</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SWITCH__BUTTON_LABELS = eINSTANCE.getSwitch_ButtonLabels();

  }

} //SitemapPackage
