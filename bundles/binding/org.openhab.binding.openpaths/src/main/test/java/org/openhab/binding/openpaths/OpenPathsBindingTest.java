package org.openhab.binding.openpaths;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.openpaths.internal.OpenPathsBinding;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenPathsBindingTest {

    Hashtable<String, String> testConfigCompatible = null;
    Hashtable<String, String> testConfigNew = null;
    OpenPathsBinding binding = null;
    
    @Before
    public void setUp() {
        if( testConfigCompatible == null ) {
            this.testConfigCompatible = new Hashtable<String, String>();
            this.testConfigCompatible.put("home.lat", "52.00000");
            this.testConfigCompatible.put("home.long", "13.00000");
            this.testConfigCompatible.put("geofence", "50");
            this.testConfigCompatible.put("refresh", "150000");
            this.testConfigCompatible.put("user1.accesskey", "accesskey");
            this.testConfigCompatible.put("user1.secretkey", "secretkey");

            this.testConfigNew = new Hashtable<String, String>();
            this.testConfigNew.put("home.lat", "52.00000");
            this.testConfigNew.put("home.long", "13.00000");
            this.testConfigNew.put("home.geofence", "50");
            this.testConfigNew.put("work.lat", "53.00000");
            this.testConfigNew.put("work.long", "14.00000");
            this.testConfigNew.put("work.geofence", "100");
            this.testConfigNew.put("refresh", "150000");
            this.testConfigNew.put("user1.accesskey", "accesskey");
            this.testConfigNew.put("user1.secretkey", "secretkey");
        }
        this.binding = new OpenPathsBinding();
    }
    
    @Test
    public void testUpdated() {
        try {
            log("Test compatible config");
            this.binding.updated(testConfigCompatible);
            for( String key : this.binding.getLocations().keySet() ) {
                log(key + "=" + this.binding.getLocations().get(key).toString());
            }
            for( String key : this.binding.getUsers().keySet() ) {
                log(key + "=" + this.binding.getUsers().get(key).toString());
            }

            log("Test new config");
            this.binding.updated(testConfigNew);
            for( String key : this.binding.getLocations().keySet() ) {
                log(key + "=" + this.binding.getLocations().get(key).toString());
            }
            for( String key : this.binding.getUsers().keySet() ) {
                log(key + "=" + this.binding.getUsers().get(key).toString());
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
}
