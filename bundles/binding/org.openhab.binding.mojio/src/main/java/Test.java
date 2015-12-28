import java.io.IOException;
import java.io.File;
import java.util.Collection;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonNode;
import org.openhab.binding.mojio.messages.AuthorizeRequest;
import org.openhab.binding.mojio.messages.AuthorizeResponse;
import org.openhab.binding.mojio.messages.GetVehicleData;
import org.openhab.binding.mojio.messages.VehicleStatusResponse;
import org.openhab.binding.mojio.messages.VehicleType;
import org.openhab.binding.mojio.messages.GetMojioData;
import org.openhab.binding.mojio.messages.MojioStatusResponse;
import org.openhab.binding.mojio.messages.MojioType;

public class Test {
  public static void main(String[] args) {
    String IMEI = "352648063154983";

    if(args.length > 0) {
      IMEI = args[0];
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      final AuthorizeRequest request = new AuthorizeRequest("316d35ac-4412-4ae3-9617-37bb4115f0af", 
                                                                "010111a9-36a7-45bf-aef9-7b438ab91a1b", "nathan@robotics.net", "Yate1209");
      final AuthorizeResponse response = request.execute();
      VehicleType vehicle = GetVehicleData.findByMojioIMEI(response.getAuthToken(), IMEI);
      System.out.println(mapper.writeValueAsString(vehicle));
      String json = mapper.writeValueAsString(vehicle);
      String path = "LastLocation";
      String[] pathComponents = path.split("/");
      System.out.println("Value path: "+path);
      System.out.println("Path component number: "+pathComponents.length);
      JsonNode root = mapper.readTree(json);
      JsonNode current = root;

      for(int i = 0; i < pathComponents.length; i ++) {
        System.out.println("Traversing "+pathComponents[i]);
        try {
          Integer intComponent = Integer.valueOf(pathComponents[i]);
          current = current.path(intComponent);
        } catch(Exception e) {
          current = current.path(pathComponents[i]);
        }
      }
      
      if(current.isNumber()) {
        System.out.println("Double Value: "+current.getDoubleValue());
      } else if(current.isBoolean()) {
        System.out.println("Bool Value: "+current.getBooleanValue());
      } else {
        System.out.println("Text Value: "+current.toString());
      }
    } catch(Exception e) {
      System.out.println("Error!");
    }
  }
}
