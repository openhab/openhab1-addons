/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.openhab.binding.tinkerforge.internal.model.*;
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.DigitalActor;
import org.openhab.binding.tinkerforge.internal.model.DigitalSensor;
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.IODevice;
import org.openhab.binding.tinkerforge.internal.model.InterruptListener;
import org.openhab.binding.tinkerforge.internal.model.MActor;
import org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickServo;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight;
import org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer;
import org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR;
import org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO16;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MDualRelay;
import org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MInSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;
import org.openhab.binding.tinkerforge.internal.types.TinkerforgeValue;

import com.tinkerforge.Device;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ModelPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = ModelPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ModelSwitch<Adapter> modelSwitch =
    new ModelSwitch<Adapter>()
    {
      @Override
      public Adapter caseEcosystem(Ecosystem object)
      {
        return createEcosystemAdapter();
      }
      @Override
      public Adapter caseMBrickd(MBrickd object)
      {
        return createMBrickdAdapter();
      }
      @Override
      public Adapter caseSubDeviceAdmin(SubDeviceAdmin object)
      {
        return createSubDeviceAdminAdapter();
      }
      @Override
      public <TFC> Adapter caseMTFConfigConsumer(MTFConfigConsumer<TFC> object)
      {
        return createMTFConfigConsumerAdapter();
      }
      @Override
      public Adapter caseMBaseDevice(MBaseDevice object)
      {
        return createMBaseDeviceAdapter();
      }
      @Override
      public <TF extends Device> Adapter caseMDevice(MDevice<TF> object)
      {
        return createMDeviceAdapter();
      }
      @Override
      public <S extends MSubDevice<?>> Adapter caseMSubDeviceHolder(MSubDeviceHolder<S> object)
      {
        return createMSubDeviceHolderAdapter();
      }
      @Override
      public Adapter caseMActor(MActor object)
      {
        return createMActorAdapter();
      }
      @Override
      public Adapter caseMSwitchActor(MSwitchActor object)
      {
        return createMSwitchActorAdapter();
      }
      @Override
      public Adapter caseMOutSwitchActor(MOutSwitchActor object)
      {
        return createMOutSwitchActorAdapter();
      }
      @Override
      public Adapter caseMInSwitchActor(MInSwitchActor object)
      {
        return createMInSwitchActorAdapter();
      }
      @Override
      public Adapter caseGenericDevice(GenericDevice object)
      {
        return createGenericDeviceAdapter();
      }
      @Override
      public Adapter caseIODevice(IODevice object)
      {
        return createIODeviceAdapter();
      }
      @Override
      public <B extends MSubDeviceHolder<?>> Adapter caseMSubDevice(MSubDevice<B> object)
      {
        return createMSubDeviceAdapter();
      }
      @Override
      public Adapter caseCallbackListener(CallbackListener object)
      {
        return createCallbackListenerAdapter();
      }
      @Override
      public Adapter caseInterruptListener(InterruptListener object)
      {
        return createInterruptListenerAdapter();
      }
      @Override
      public <DeviceValue extends TinkerforgeValue> Adapter caseMSensor(MSensor<DeviceValue> object)
      {
        return createMSensorAdapter();
      }
      @Override
      public Adapter caseMTextActor(MTextActor object)
      {
        return createMTextActorAdapter();
      }
      @Override
      public Adapter caseMLCDSubDevice(MLCDSubDevice object)
      {
        return createMLCDSubDeviceAdapter();
      }
      @Override
      public Adapter caseMBrickServo(MBrickServo object)
      {
        return createMBrickServoAdapter();
      }
      @Override
      public Adapter caseMServo(MServo object)
      {
        return createMServoAdapter();
      }
      @Override
      public Adapter caseMBrickDC(MBrickDC object)
      {
        return createMBrickDCAdapter();
      }
      @Override
      public Adapter caseMDualRelayBricklet(MDualRelayBricklet object)
      {
        return createMDualRelayBrickletAdapter();
      }
      @Override
      public Adapter caseMIndustrialQuadRelayBricklet(MIndustrialQuadRelayBricklet object)
      {
        return createMIndustrialQuadRelayBrickletAdapter();
      }
      @Override
      public Adapter caseMIndustrialQuadRelay(MIndustrialQuadRelay object)
      {
        return createMIndustrialQuadRelayAdapter();
      }
      @Override
      public Adapter caseMBrickletIndustrialDigitalIn4(MBrickletIndustrialDigitalIn4 object)
      {
        return createMBrickletIndustrialDigitalIn4Adapter();
      }
      @Override
      public Adapter caseMIndustrialDigitalIn(MIndustrialDigitalIn object)
      {
        return createMIndustrialDigitalInAdapter();
      }
      @Override
      public Adapter caseMBrickletIndustrialDigitalOut4(MBrickletIndustrialDigitalOut4 object)
      {
        return createMBrickletIndustrialDigitalOut4Adapter();
      }
      @Override
      public Adapter caseDigitalActorDigitalOut4(DigitalActorDigitalOut4 object)
      {
        return createDigitalActorDigitalOut4Adapter();
      }
      @Override
      public Adapter caseDigitalActor(DigitalActor object)
      {
        return createDigitalActorAdapter();
      }
      @Override
      public Adapter caseNumberActor(NumberActor object)
      {
        return createNumberActorAdapter();
      }
      @Override
      public Adapter caseMBrickletSegmentDisplay4x7(MBrickletSegmentDisplay4x7 object)
      {
        return createMBrickletSegmentDisplay4x7Adapter();
      }
      @Override
      public Adapter caseColorActor(ColorActor object)
      {
        return createColorActorAdapter();
      }
      @Override
      public Adapter caseMBrickletLEDStrip(MBrickletLEDStrip object)
      {
        return createMBrickletLEDStripAdapter();
      }
      @Override
      public Adapter caseDigitalActorIO16(DigitalActorIO16 object)
      {
        return createDigitalActorIO16Adapter();
      }
      @Override
      public Adapter caseMBrickletIO16(MBrickletIO16 object)
      {
        return createMBrickletIO16Adapter();
      }
      @Override
      public Adapter caseDigitalSensor(DigitalSensor object)
      {
        return createDigitalSensorAdapter();
      }
      @Override
      public Adapter caseMBrickletIO4(MBrickletIO4 object)
      {
        return createMBrickletIO4Adapter();
      }
      @Override
      public Adapter caseIO4Device(IO4Device object)
      {
        return createIO4DeviceAdapter();
      }
      @Override
      public Adapter caseDigitalSensorIO4(DigitalSensorIO4 object)
      {
        return createDigitalSensorIO4Adapter();
      }
      @Override
      public Adapter caseDigitalActorIO4(DigitalActorIO4 object)
      {
        return createDigitalActorIO4Adapter();
      }
      @Override
      public Adapter caseMBrickletMultiTouch(MBrickletMultiTouch object)
      {
        return createMBrickletMultiTouchAdapter();
      }
      @Override
      public Adapter caseMultiTouchDevice(MultiTouchDevice object)
      {
        return createMultiTouchDeviceAdapter();
      }
      @Override
      public Adapter caseElectrode(Electrode object)
      {
        return createElectrodeAdapter();
      }
      @Override
      public Adapter caseProximity(Proximity object)
      {
        return createProximityAdapter();
      }
      @Override
      public Adapter caseMBrickletMotionDetector(MBrickletMotionDetector object)
      {
        return createMBrickletMotionDetectorAdapter();
      }
      @Override
      public Adapter caseMBrickletHallEffect(MBrickletHallEffect object)
      {
        return createMBrickletHallEffectAdapter();
      }
      @Override
      public Adapter caseMDualRelay(MDualRelay object)
      {
        return createMDualRelayAdapter();
      }
      @Override
      public Adapter caseMBrickletRemoteSwitch(MBrickletRemoteSwitch object)
      {
        return createMBrickletRemoteSwitchAdapter();
      }
      @Override
      public Adapter caseRemoteSwitch(RemoteSwitch object)
      {
        return createRemoteSwitchAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchA(RemoteSwitchA object)
      {
        return createRemoteSwitchAAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchB(RemoteSwitchB object)
      {
        return createRemoteSwitchBAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchC(RemoteSwitchC object)
      {
        return createRemoteSwitchCAdapter();
      }
      @Override
      public Adapter caseMBrickletHumidity(MBrickletHumidity object)
      {
        return createMBrickletHumidityAdapter();
      }
      @Override
      public Adapter caseMBrickletDistanceIR(MBrickletDistanceIR object)
      {
        return createMBrickletDistanceIRAdapter();
      }
      @Override
      public Adapter caseMBrickletTemperature(MBrickletTemperature object)
      {
        return createMBrickletTemperatureAdapter();
      }
      @Override
      public Adapter caseMBrickletTemperatureIR(MBrickletTemperatureIR object)
      {
        return createMBrickletTemperatureIRAdapter();
      }
      @Override
      public Adapter caseMTemperatureIRDevice(MTemperatureIRDevice object)
      {
        return createMTemperatureIRDeviceAdapter();
      }
      @Override
      public Adapter caseObjectTemperature(ObjectTemperature object)
      {
        return createObjectTemperatureAdapter();
      }
      @Override
      public Adapter caseAmbientTemperature(AmbientTemperature object)
      {
        return createAmbientTemperatureAdapter();
      }
      @Override
      public Adapter caseMBrickletTilt(MBrickletTilt object)
      {
        return createMBrickletTiltAdapter();
      }
      @Override
      public Adapter caseMBrickletVoltageCurrent(MBrickletVoltageCurrent object)
      {
        return createMBrickletVoltageCurrentAdapter();
      }
      @Override
      public Adapter caseVoltageCurrentDevice(VoltageCurrentDevice object)
      {
        return createVoltageCurrentDeviceAdapter();
      }
      @Override
      public Adapter caseVCDeviceVoltage(VCDeviceVoltage object)
      {
        return createVCDeviceVoltageAdapter();
      }
      @Override
      public Adapter caseVCDeviceCurrent(VCDeviceCurrent object)
      {
        return createVCDeviceCurrentAdapter();
      }
      @Override
      public Adapter caseVCDevicePower(VCDevicePower object)
      {
        return createVCDevicePowerAdapter();
      }
      @Override
      public Adapter caseMBrickletBarometer(MBrickletBarometer object)
      {
        return createMBrickletBarometerAdapter();
      }
      @Override
      public Adapter caseMBarometerTemperature(MBarometerTemperature object)
      {
        return createMBarometerTemperatureAdapter();
      }
      @Override
      public Adapter caseMBrickletAmbientLight(MBrickletAmbientLight object)
      {
        return createMBrickletAmbientLightAdapter();
      }
      @Override
      public Adapter caseMBrickletSoundIntensity(MBrickletSoundIntensity object)
      {
        return createMBrickletSoundIntensityAdapter();
      }
      @Override
      public Adapter caseMBrickletMoisture(MBrickletMoisture object)
      {
        return createMBrickletMoistureAdapter();
      }
      @Override
      public Adapter caseMBrickletDistanceUS(MBrickletDistanceUS object)
      {
        return createMBrickletDistanceUSAdapter();
      }
      @Override
      public Adapter caseMBrickletLCD20x4(MBrickletLCD20x4 object)
      {
        return createMBrickletLCD20x4Adapter();
      }
      @Override
      public Adapter caseMLCD20x4Backlight(MLCD20x4Backlight object)
      {
        return createMLCD20x4BacklightAdapter();
      }
      @Override
      public Adapter caseMLCD20x4Button(MLCD20x4Button object)
      {
        return createMLCD20x4ButtonAdapter();
      }
      @Override
      public Adapter caseTFConfig(TFConfig object)
      {
        return createTFConfigAdapter();
      }
      @Override
      public <TFC extends TFConfig, IDS extends Enum> Adapter caseOHTFDevice(OHTFDevice<TFC, IDS> object)
      {
        return createOHTFDeviceAdapter();
      }
      @Override
      public <TFC extends TFConfig, IDS extends Enum> Adapter caseOHTFSubDeviceAdminDevice(OHTFSubDeviceAdminDevice<TFC, IDS> object)
      {
        return createOHTFSubDeviceAdminDeviceAdapter();
      }
      @Override
      public Adapter caseOHConfig(OHConfig object)
      {
        return createOHConfigAdapter();
      }
      @Override
      public Adapter caseTFNullConfiguration(TFNullConfiguration object)
      {
        return createTFNullConfigurationAdapter();
      }
      @Override
      public Adapter caseTFBaseConfiguration(TFBaseConfiguration object)
      {
        return createTFBaseConfigurationAdapter();
      }
      @Override
      public Adapter caseTFObjectTemperatureConfiguration(TFObjectTemperatureConfiguration object)
      {
        return createTFObjectTemperatureConfigurationAdapter();
      }
      @Override
      public Adapter caseTFMoistureBrickletConfiguration(TFMoistureBrickletConfiguration object)
      {
        return createTFMoistureBrickletConfigurationAdapter();
      }
      @Override
      public Adapter caseTFDistanceUSBrickletConfiguration(TFDistanceUSBrickletConfiguration object)
      {
        return createTFDistanceUSBrickletConfigurationAdapter();
      }
      @Override
      public Adapter caseTFVoltageCurrentConfiguration(TFVoltageCurrentConfiguration object)
      {
        return createTFVoltageCurrentConfigurationAdapter();
      }
      @Override
      public Adapter caseTFBrickDCConfiguration(TFBrickDCConfiguration object)
      {
        return createTFBrickDCConfigurationAdapter();
      }
      @Override
      public Adapter caseTFIOActorConfiguration(TFIOActorConfiguration object)
      {
        return createTFIOActorConfigurationAdapter();
      }
      @Override
      public Adapter caseTFInterruptListenerConfiguration(TFInterruptListenerConfiguration object)
      {
        return createTFInterruptListenerConfigurationAdapter();
      }
      @Override
      public Adapter caseTFIOSensorConfiguration(TFIOSensorConfiguration object)
      {
        return createTFIOSensorConfigurationAdapter();
      }
      @Override
      public Adapter caseTFServoConfiguration(TFServoConfiguration object)
      {
        return createTFServoConfigurationAdapter();
      }
      @Override
      public Adapter caseBrickletRemoteSwitchConfiguration(BrickletRemoteSwitchConfiguration object)
      {
        return createBrickletRemoteSwitchConfigurationAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchAConfiguration(RemoteSwitchAConfiguration object)
      {
        return createRemoteSwitchAConfigurationAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchBConfiguration(RemoteSwitchBConfiguration object)
      {
        return createRemoteSwitchBConfigurationAdapter();
      }
      @Override
      public Adapter caseRemoteSwitchCConfiguration(RemoteSwitchCConfiguration object)
      {
        return createRemoteSwitchCConfigurationAdapter();
      }
      @Override
      public Adapter caseMultiTouchDeviceConfiguration(MultiTouchDeviceConfiguration object)
      {
        return createMultiTouchDeviceConfigurationAdapter();
      }
      @Override
      public Adapter caseBrickletMultiTouchConfiguration(BrickletMultiTouchConfiguration object)
      {
        return createBrickletMultiTouchConfigurationAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFConfig <em>TF Config</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFConfig
   * @generated
   */
  public Adapter createTFConfigAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.OHTFDevice <em>OHTF Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFDevice
   * @generated
   */
  public Adapter createOHTFDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.OHTFSubDeviceAdminDevice <em>OHTF Sub Device Admin Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.OHTFSubDeviceAdminDevice
   * @generated
   */
  public Adapter createOHTFSubDeviceAdminDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.OHConfig <em>OH Config</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.OHConfig
   * @generated
   */
  public Adapter createOHConfigAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.Ecosystem <em>Ecosystem</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.Ecosystem
   * @generated
   */
  public Adapter createEcosystemAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickd <em>MBrickd</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickd
   * @generated
   */
  public Adapter createMBrickdAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.SubDeviceAdmin <em>Sub Device Admin</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.SubDeviceAdmin
   * @generated
   */
  public Adapter createSubDeviceAdminAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer <em>MTF Config Consumer</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
   * @generated
   */
  public Adapter createMTFConfigConsumerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBaseDevice <em>MBase Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBaseDevice
   * @generated
   */
  public Adapter createMBaseDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MDevice <em>MDevice</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MDevice
   * @generated
   */
  public Adapter createMDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder <em>MSub Device Holder</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder
   * @generated
   */
  public Adapter createMSubDeviceHolderAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickServo <em>MBrick Servo</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickServo
   * @generated
   */
  public Adapter createMBrickServoAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration <em>TF Brick DC Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration
   * @generated
   */
  public Adapter createTFBrickDCConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC <em>MBrick DC</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickDC
   * @generated
   */
  public Adapter createMBrickDCAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet <em>MDual Relay Bricklet</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelayBricklet
   * @generated
   */
  public Adapter createMDualRelayBrickletAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet <em>MIndustrial Quad Relay Bricklet</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet
   * @generated
   */
  public Adapter createMIndustrialQuadRelayBrickletAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay <em>MIndustrial Quad Relay</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay
   * @generated
   */
  public Adapter createMIndustrialQuadRelayAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4 <em>MBricklet Industrial Digital In4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4
   * @generated
   */
  public Adapter createMBrickletIndustrialDigitalIn4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn <em>MIndustrial Digital In</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn
   * @generated
   */
  public Adapter createMIndustrialDigitalInAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalOut4 <em>MBricklet Industrial Digital Out4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalOut4
   * @generated
   */
  public Adapter createMBrickletIndustrialDigitalOut4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorDigitalOut4 <em>Digital Actor Digital Out4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActorDigitalOut4
   * @generated
   */
  public Adapter createDigitalActorDigitalOut4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor <em>Digital Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActor
   * @generated
   */
  public Adapter createDigitalActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.NumberActor <em>Number Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.NumberActor
   * @generated
   */
  public Adapter createNumberActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.ColorActor <em>Color Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.ColorActor
   * @generated
   */
  public Adapter createColorActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip <em>MBricklet LED Strip</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip
   * @generated
   */
  public Adapter createMBrickletLEDStripAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletSegmentDisplay4x7 <em>MBricklet Segment Display4x7</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletSegmentDisplay4x7
   * @generated
   */
  public Adapter createMBrickletSegmentDisplay4x7Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16 <em>Digital Actor IO16</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16
   * @generated
   */
  public Adapter createDigitalActorIO16Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MActor <em>MActor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MActor
   * @generated
   */
  public Adapter createMActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MSwitchActor <em>MSwitch Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MSwitchActor
   * @generated
   */
  public Adapter createMSwitchActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor <em>MOut Switch Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MOutSwitchActor
   * @generated
   */
  public Adapter createMOutSwitchActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MInSwitchActor <em>MIn Switch Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MInSwitchActor
   * @generated
   */
  public Adapter createMInSwitchActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.GenericDevice <em>Generic Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.GenericDevice
   * @generated
   */
  public Adapter createGenericDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration <em>TFIO Actor Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration
   * @generated
   */
  public Adapter createTFIOActorConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration <em>TF Interrupt Listener Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration
   * @generated
   */
  public Adapter createTFInterruptListenerConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIO16 <em>MBricklet IO16</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIO16
   * @generated
   */
  public Adapter createMBrickletIO16Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.IODevice <em>IO Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.IODevice
   * @generated
   */
  public Adapter createIODeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration <em>TFIO Sensor Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration
   * @generated
   */
  public Adapter createTFIOSensorConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensor <em>Digital Sensor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensor
   * @generated
   */
  public Adapter createDigitalSensorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIO4 <em>MBricklet IO4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletIO4
   * @generated
   */
  public Adapter createMBrickletIO4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.IO4Device <em>IO4 Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.IO4Device
   * @generated
   */
  public Adapter createIO4DeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4 <em>Digital Sensor IO4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4
   * @generated
   */
  public Adapter createDigitalSensorIO4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO4 <em>Digital Actor IO4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.DigitalActorIO4
   * @generated
   */
  public Adapter createDigitalActorIO4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch <em>MBricklet Multi Touch</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch
   * @generated
   */
  public Adapter createMBrickletMultiTouchAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice <em>Multi Touch Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice
   * @generated
   */
  public Adapter createMultiTouchDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.Electrode <em>Electrode</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.Electrode
   * @generated
   */
  public Adapter createElectrodeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.Proximity <em>Proximity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.Proximity
   * @generated
   */
  public Adapter createProximityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletMotionDetector <em>MBricklet Motion Detector</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletMotionDetector
   * @generated
   */
  public Adapter createMBrickletMotionDetectorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHallEffect <em>MBricklet Hall Effect</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHallEffect
   * @generated
   */
  public Adapter createMBrickletHallEffectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MSubDevice <em>MSub Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MSubDevice
   * @generated
   */
  public Adapter createMSubDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MDualRelay <em>MDual Relay</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MDualRelay
   * @generated
   */
  public Adapter createMDualRelayAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch <em>MBricklet Remote Switch</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch
   * @generated
   */
  public Adapter createMBrickletRemoteSwitchAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitch <em>Remote Switch</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitch
   * @generated
   */
  public Adapter createRemoteSwitchAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchA <em>Remote Switch A</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchA
   * @generated
   */
  public Adapter createRemoteSwitchAAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB <em>Remote Switch B</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchB
   * @generated
   */
  public Adapter createRemoteSwitchBAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC <em>Remote Switch C</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC
   * @generated
   */
  public Adapter createRemoteSwitchCAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration <em>TF Null Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFNullConfiguration
   * @generated
   */
  public Adapter createTFNullConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration <em>TF Servo Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration
   * @generated
   */
  public Adapter createTFServoConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration <em>Bricklet Remote Switch Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration
   * @generated
   */
  public Adapter createBrickletRemoteSwitchConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration <em>Remote Switch AConfiguration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration
   * @generated
   */
  public Adapter createRemoteSwitchAConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration <em>Remote Switch BConfiguration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration
   * @generated
   */
  public Adapter createRemoteSwitchBConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchCConfiguration <em>Remote Switch CConfiguration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.RemoteSwitchCConfiguration
   * @generated
   */
  public Adapter createRemoteSwitchCConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration <em>Multi Touch Device Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration
   * @generated
   */
  public Adapter createMultiTouchDeviceConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration <em>Bricklet Multi Touch Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration
   * @generated
   */
  public Adapter createBrickletMultiTouchConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MServo <em>MServo</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MServo
   * @generated
   */
  public Adapter createMServoAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener <em>Callback Listener</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.CallbackListener
   * @generated
   */
  public Adapter createCallbackListenerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.InterruptListener <em>Interrupt Listener</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.InterruptListener
   * @generated
   */
  public Adapter createInterruptListenerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MSensor <em>MSensor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MSensor
   * @generated
   */
  public Adapter createMSensorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity <em>MBricklet Humidity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletHumidity
   * @generated
   */
  public Adapter createMBrickletHumidityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR <em>MBricklet Distance IR</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceIR
   * @generated
   */
  public Adapter createMBrickletDistanceIRAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature <em>MBricklet Temperature</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperature
   * @generated
   */
  public Adapter createMBrickletTemperatureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperatureIR <em>MBricklet Temperature IR</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTemperatureIR
   * @generated
   */
  public Adapter createMBrickletTemperatureIRAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MTemperatureIRDevice <em>MTemperature IR Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MTemperatureIRDevice
   * @generated
   */
  public Adapter createMTemperatureIRDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.ObjectTemperature <em>Object Temperature</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.ObjectTemperature
   * @generated
   */
  public Adapter createObjectTemperatureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.AmbientTemperature <em>Ambient Temperature</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.AmbientTemperature
   * @generated
   */
  public Adapter createAmbientTemperatureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTilt <em>MBricklet Tilt</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletTilt
   * @generated
   */
  public Adapter createMBrickletTiltAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletVoltageCurrent <em>MBricklet Voltage Current</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletVoltageCurrent
   * @generated
   */
  public Adapter createMBrickletVoltageCurrentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.VoltageCurrentDevice <em>Voltage Current Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.VoltageCurrentDevice
   * @generated
   */
  public Adapter createVoltageCurrentDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.VCDeviceVoltage <em>VC Device Voltage</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.VCDeviceVoltage
   * @generated
   */
  public Adapter createVCDeviceVoltageAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.VCDeviceCurrent <em>VC Device Current</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.VCDeviceCurrent
   * @generated
   */
  public Adapter createVCDeviceCurrentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.VCDevicePower <em>VC Device Power</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.VCDevicePower
   * @generated
   */
  public Adapter createVCDevicePowerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration <em>TF Base Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration
   * @generated
   */
  public Adapter createTFBaseConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration <em>TF Object Temperature Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFObjectTemperatureConfiguration
   * @generated
   */
  public Adapter createTFObjectTemperatureConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration <em>TF Moisture Bricklet Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFMoistureBrickletConfiguration
   * @generated
   */
  public Adapter createTFMoistureBrickletConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFDistanceUSBrickletConfiguration <em>TF Distance US Bricklet Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFDistanceUSBrickletConfiguration
   * @generated
   */
  public Adapter createTFDistanceUSBrickletConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.TFVoltageCurrentConfiguration <em>TF Voltage Current Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.TFVoltageCurrentConfiguration
   * @generated
   */
  public Adapter createTFVoltageCurrentConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer <em>MBricklet Barometer</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletBarometer
   * @generated
   */
  public Adapter createMBrickletBarometerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature <em>MBarometer Temperature</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBarometerTemperature
   * @generated
   */
  public Adapter createMBarometerTemperatureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight <em>MBricklet Ambient Light</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLight
   * @generated
   */
  public Adapter createMBrickletAmbientLightAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletSoundIntensity <em>MBricklet Sound Intensity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletSoundIntensity
   * @generated
   */
  public Adapter createMBrickletSoundIntensityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletMoisture <em>MBricklet Moisture</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletMoisture
   * @generated
   */
  public Adapter createMBrickletMoistureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceUS <em>MBricklet Distance US</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletDistanceUS
   * @generated
   */
  public Adapter createMBrickletDistanceUSAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4 <em>MBricklet LCD2 0x4</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4
   * @generated
   */
  public Adapter createMBrickletLCD20x4Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MTextActor <em>MText Actor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MTextActor
   * @generated
   */
  public Adapter createMTextActorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice <em>MLCD Sub Device</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice
   * @generated
   */
  public Adapter createMLCDSubDeviceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight <em>MLCD2 0x4 Backlight</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight
   * @generated
   */
  public Adapter createMLCD20x4BacklightAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button <em>MLCD2 0x4 Button</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button
   * @generated
   */
  public Adapter createMLCD20x4ButtonAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //ModelAdapterFactory
