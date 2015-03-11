package org.openhab.binding.tinkerforge.ecosystem;

import org.openhab.binding.tinkerforge.internal.model.Ecosystem;

public class TinkerforgeContext {

  private static TinkerforgeContext instance;
  private Ecosystem ecosystem;
  
  private TinkerforgeContext() {}

  public static TinkerforgeContext getInstance() {
    if (instance == null) {
      instance = new TinkerforgeContext();
    }
    return instance;
  }

  public Ecosystem getEcosystem() {
    return ecosystem;
  }

  public void setEcosystem(Ecosystem ecosystem) {
    this.ecosystem = ecosystem;
  }

}
