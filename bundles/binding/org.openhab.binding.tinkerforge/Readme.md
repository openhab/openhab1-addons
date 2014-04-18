# Bugfixes
 ## 1.4.0
  * Missing updates of Items if a Tinkerforge Device is referenced in several Items
# Incompatible Changes
 ## 1.4.0
  * LCDBacklight is a sub device of LCD20x4 Bricklet (items file must be changed)
  * LCD20x4Button posts an update not a command anymore (rules must be changed)
  * IndustrialQuadRelay sub id numbering now starts from zero (items file must be changed)
# New Features
 ## 1.4.0
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
 ## 1.5.0
  * new Devices
    * Remote Switch Bricklet
    * Motion Detection Bricklet
        * items file
        '''
        Contact motion      "motion [MAP(en.map):MOTION%s]" {tinkerforge="uid=m3W"}
        '''
        * sitemap file
        '''
        Text item=motion
        '''
        * en.map file
        '''
            MOTIONCLOSED=no motion
            MOTIONOPEN=montion detected
        '''
# other changes
 ## 1.4.0
   * updated Tinkerforge API to 2.0.12
 ## 1.5.0
   * Tinkerforge API 2.1.0.2
