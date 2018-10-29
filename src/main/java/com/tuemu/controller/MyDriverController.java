package com.tuemu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@RestController
public class MyDriverController {
	
	AWSCredentials credentials = new BasicAWSCredentials("yourAccessKeyId", "yourSecretAccessKey");
	AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
	//AmazonDynamoDBClient ddb = new AmazonDynamoDBClient(new ClasspathPropertiesFileCredentialsProvider());
	AmazonDynamoDBClient ddb = new AmazonDynamoDBClient(credentials);
    
   @RequestMapping(value = "/test")    //URL「〜/test」にアクセスされたときのアノテーション
   public String test() {
       //client.setEndpoint("http://localhost:8000", "", "local");
       
//       PutItemRequest request = new PutItemRequest()
//               .withTableName("Clients")
//               .addItemEntry("channelName", new AttributeValue("techscore"))
//               .addItemEntry("maxMessageNumber", new AttributeValue().withN("0"))
//               .addItemEntry("createdAt", new AttributeValue(OffsetDateTime.now().toString()))
//               .addItemEntry("updatedAt", new AttributeValue(OffsetDateTime.now().toString()));
//       client.putItem(request);
	   
	   this.testGeo();
	   
	   return "Hello World!";
   }
   
   private void testGeo() {
       ddb.setEndpoint("http://localhost:8000", "", "local");
       ClientConfiguration clientConfiguration = new ClientConfiguration().withMaxErrorRetry(5);
       ddb.setConfiguration(clientConfiguration);

       GeoDataManagerConfiguration config = new GeoDataManagerConfiguration(ddb, "geo-table");
       GeoDataManager geoDataManager = new GeoDataManager(config);

       GeoPoint geoPoint = new GeoPoint(47.6456, -122.3350);
       AttributeValue rangeKeyValue = new AttributeValue().withS("POI_00001");
       AttributeValue titleValue = new AttributeValue().withS("Gas Works Park");
        
       PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyValue);
       putPointRequest.getPutItemRequest().getItem().put("title", titleValue);
        
       geoDataManager.putPoint(putPointRequest);
   }
}
