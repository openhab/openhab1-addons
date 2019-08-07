package org.openhab.persistence.dynamodb.internal;

import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveResult;
import com.amazonaws.services.dynamodbv2.model.TimeToLiveStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExpiringStringItemIntegrationTest extends StringItemIntegrationTest {

    @BeforeClass
    public static void initServiceWithExpirationEnabled() throws InterruptedException {
        // this is a bit slow
        // junit runs inherited (parent) class @BeforeClass methods first.
        // this basically, for this class, results in us doing everything again.
        // seems fine since it's an integration test, but could probably be refactored eventually
        // some ideas: use a @Before - but store table creation in a static var.
        // the impact is we must call each paren't method's @BeforeClass manually here.
        // this is all in an effort to feed a different serviceConfig to the initService
        Map<String,Object> expiringConfig = getExpiringServiceConfiguration();
        initServiceWithConfig(expiringConfig);
        checkService();
        storeData();
    }

    public static Map<String, Object> getExpiringServiceConfiguration(){
        Map<String, Object> baseConfiguration = BaseIntegrationTest.getDefaultServiceConfiguration();
        baseConfiguration.put("autoExpirationEnabled", "true");
        baseConfiguration.put("autoExpirationDays", "1");
        return baseConfiguration;
    }


    @Test
    public void testTableHasExpirationEnabled(){
        for (String table: tableNames()){
            if (table.contains("string")) {
                DescribeTimeToLiveResult describeTimeToLiveResult = service.getDb().getDynamoClient().describeTimeToLive(new DescribeTimeToLiveRequest().withTableName(table));
                assertNotNull(describeTimeToLiveResult.getTimeToLiveDescription());
                assertEquals(TimeToLiveStatus.ENABLED.toString(), describeTimeToLiveResult.getTimeToLiveDescription().getTimeToLiveStatus());
                System.out.println("checking " + table);
                service.getDb().getDynamoDB().getTable(table).scan(new ScanSpec().withConsistentRead(true)).pages().forEach(page -> {
                    assertNotNull(page);
                    page.forEach(item -> {
                        assertNotNull(item);
                        System.out.println(item.toJSON());
                        assertTrue("Expected the item fetched from the table to have an expiration time", item.isPresent(DynamoDBItem.ATTRIBUTE_NAME_EXPIRATION));
                    });
                });
            }
        }
    }
}
