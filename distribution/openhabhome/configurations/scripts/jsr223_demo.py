class TestRule(Rule):
    def __init__(self):
        self.logger = oh.getLogger("TestRule")

    def getEventTrigger(self):
        return [
            StartupTrigger(),
            ChangedEventTrigger("Heating_FF_Child", None, None),
            TimerTrigger("0/50 * * * * ?")
        ]

    def execute(self, event):
        self.logger.debug("event received {}", event)
        oh.logInfo("TestRule", str(ItemRegistry.getItem("Heating_GF_Corridor")))
        action = oh.getActions() 
        oh.logInfo("TestRule", "available actions: " + str(action.keySet()))
        ping = oh.getAction("Ping")
        oh.logInfo("TestRule", "internet reachable: " + ("yes" if ping.checkVitality("google.com", 80, 100) else "no"))
        
        def whoop():
            print "yeah"
        
        oh.createTimer(DateTime.now().plusSeconds(10), whoop)

def getRules():
    return RuleSet([
        TestRule()
    ])
