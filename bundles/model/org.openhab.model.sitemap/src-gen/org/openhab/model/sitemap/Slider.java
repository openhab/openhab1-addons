/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.sitemap;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.sitemap.Slider#getFrequency <em>Frequency</em>}</li>
 *   <li>{@link org.openhab.model.sitemap.Slider#isSwitchEnabled <em>Switch Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.sitemap.SitemapPackage#getSlider()
 * @model
 * @generated
 */
public interface Slider extends Widget
{
  /**
   * Returns the value of the '<em><b>Frequency</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Frequency</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Frequency</em>' attribute.
   * @see #setFrequency(String)
   * @see org.openhab.model.sitemap.SitemapPackage#getSlider_Frequency()
   * @model
   * @generated
   */
  String getFrequency();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Slider#getFrequency <em>Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Frequency</em>' attribute.
   * @see #getFrequency()
   * @generated
   */
  void setFrequency(String value);

  /**
   * Returns the value of the '<em><b>Switch Enabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Switch Enabled</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Switch Enabled</em>' attribute.
   * @see #setSwitchEnabled(boolean)
   * @see org.openhab.model.sitemap.SitemapPackage#getSlider_SwitchEnabled()
   * @model
   * @generated
   */
  boolean isSwitchEnabled();

  /**
   * Sets the value of the '{@link org.openhab.model.sitemap.Slider#isSwitchEnabled <em>Switch Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Switch Enabled</em>' attribute.
   * @see #isSwitchEnabled()
   * @generated
   */
  void setSwitchEnabled(boolean value);

} // Slider
