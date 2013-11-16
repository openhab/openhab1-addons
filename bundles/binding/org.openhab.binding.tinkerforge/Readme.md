# Bugfixes
 ## 1.4.0
  * Missing updates of Items if a Tinkerforge Device is referenced in several Items
# Incompatible Changes
 ## 1.4.0
  * LCDBacklight is a sub device of LCD20x4 Bricklet (items file must be changed)
  * LCD20x4Button posts an update not a command anymore (rules must be changed)
  * IndustrialQuadRelay sub id numbering now starts from zero (items file must be changed)
# New Features
  * new Devices
    * Bricklet Industrial Quad Relay
    * Bricklet Industrial Digital In 4
    * Bricklet IO-16
    * support for serveral Item types
       * NumberItem
       * SwitchItem
       * ContactItem
  * handle disconnected brickds
     * on binding startup make retries every second
     * when binding is running use the Tinkerforge autoreconnect feature
# other changes
   * updated Tinkerforge API to 2.0.12
 