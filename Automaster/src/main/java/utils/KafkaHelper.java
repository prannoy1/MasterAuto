package utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.testng.asserts.SoftAssert;

//import com.qualys.automation.plugin.cas.common.QAgentDb;
import org.testng.Assert;
//
//import net.minidev.json.parser.JSONParser;

/**
 * @author prattan
 *
 */
public class KafkaHelper {

	final static Logger logger = Logger.getLogger(KafkaHelper.class);
	
	
	public static KafkaProducer<String, String> createProducer(String bootStrapServers) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
		
		KafkaProducer<String, String> k = new KafkaProducer<String, String>(props);
		return k;
	}

	public static KafkaConsumer<String, String> createConsumer(String bootStrapServers) {
		Properties props = new Properties();
		System.out.println("--------------------------------------->>>>>>>"+bootStrapServers);
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup");
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,999999999);
		props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,524288000);
		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,9999999);
		
		KafkaConsumer<String, String> k = new KafkaConsumer<String, String>(props);
		return k;
		
//		Properties props = new Properties();
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);

//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
//		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"50000");
//		KafkaConsumer<String, String> k = new KafkaConsumer<String, String>(props);
//		return k;
	}
	
	public static ConsumerRecords<String, String> seekToEndAndGetRecordsfromPartition(KafkaConsumer<String, String> consumer,String topic,int partitionNumber) {
		TopicPartition partition = new TopicPartition(topic, partitionNumber);
		Collection<TopicPartition> tp = new ArrayList<TopicPartition>();
		consumer.assign(Arrays.asList(partition));
		ConsumerRecords<String, String> consumerRecords = consumer.poll(200000);
		return consumerRecords;
	}

	public static void PushMessage(String topic, String message,KafkaProducer<String, String> producer ) throws IOException
	{
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
		try {
		producer.send(producerRecord);
		producer.flush();
		}catch(Exception e) {}
		finally {producer.close();}

	}

	public static int getNumberofPartitions(String topic, String bootStrapServers) {
		KafkaProducer<String, String>  producer = KafkaHelper.createProducer(bootStrapServers);
		List<PartitionInfo> numberofPartitions = producer.partitionsFor(topic);
		return numberofPartitions.size();
	}
	
	public static List<String> getKafkaMessages(String topic,String key) {
		String BOOTSTRAP_SERVERS="qkafka01.p03.eng.sjc01.qualys.com:50360";
		List<String> messages=new ArrayList<String>();
		KafkaConsumer<String, String> consumer = KafkaHelper.createConsumer(BOOTSTRAP_SERVERS);
		int partitions=consumer.partitionsFor(topic).size();
		
		for(int i =0;i<partitions;i++)
		{
			ConsumerRecords<String, String> cr = KafkaHelper.seekToEndAndGetRecordsfromPartition(consumer, topic, i);
			for (ConsumerRecord<String, String> record : cr) {
				String actualKey=record.value();
				
				if(actualKey.contains(key))
				{		
					messages.add(record.value());
					
				}
			}
		}
		
		consumer.close();
		return messages;
		
	}
	
	
	public static JsonNode getMessageInJSON(String BOOTSTRAP_SERVERS,String topic,String key) throws Exception {
		KafkaConsumer<String, String> consumer = KafkaHelper.createConsumer(BOOTSTRAP_SERVERS);
		JsonNode jsonObject = null;
		boolean keypresent = false;
		int partitions=KafkaHelper.getNumberofPartitions(topic,BOOTSTRAP_SERVERS);
		logger.info("Number of partions for topic " + topic+" are "+ partitions);
		out : for(int i =0;i<partitions;i++) {
			
			logger.info("checking for partion no. "+ i);
			
			ConsumerRecords<String, String> cr  = KafkaHelper.seekToEndAndGetRecordsfromPartition(consumer, topic, i);
			for (ConsumerRecord<String, String> record : cr) {
				System.out.println(record.offset());
				if(record.key().equals(key))
				{
					logger.info("needed record ------------->> "+record.value());
					keypresent=true;
					String value = record.value();
					jsonObject = JsonUtils.getObjectFromJsonString(value);
					logger.info(jsonObject);
					break out;
				}
				
			}
		}if(keypresent==false) {
			Assert.fail("Record Not found");
		}
		
		consumer.unsubscribe();
		consumer.close();
		
		return jsonObject;
	}
	
	public static JsonNode getMessageInJSONN(String BOOTSTRAP_SERVERS,String topic,String key) throws Exception {
		KafkaConsumer<String, String> consumer = KafkaHelper.createConsumer(BOOTSTRAP_SERVERS);
		JsonNode jsonObject = null;
		boolean keypresent = false;
		int partitions=KafkaHelper.getNumberofPartitions(topic,BOOTSTRAP_SERVERS);
		logger.info("Number of partions for topic " + topic+" are "+ partitions);
		out : for(int i =0;i<partitions;i++) {
			int counter=0;
			logger.info("checking for partion no. "+ i);
			while(keypresent==false && counter<2) {
			ConsumerRecords<String, String> cr  = KafkaHelper.seekToEndAndGetRecordsfromPartition(consumer, topic, i);
			for (ConsumerRecord<String, String> record : cr) {
				System.out.println(record.offset());
				if(record.key().equals(key))
				{
					logger.info("needed record ------------->> "+record.value());
					keypresent=true;
					String value = record.value();
					jsonObject = JsonUtils.getObjectFromJsonString(value);
					logger.info(jsonObject);
					break out;
				}
				
			} counter++;}
		}if(keypresent==false) {
			Assert.fail("Record Not found");
		}
		
		consumer.unsubscribe();
		consumer.close();
		
		return jsonObject;
	}
	
	
	
	
	
	
//	public static JsonNode getMessageInJSON1(String BOOTSTRAP_SERVERS,String topic,String key) throws Exception {
//		KafkaConsumer<String, String> consumer = KafkaHelper.createConsumer(BOOTSTRAP_SERVERS);
//		JsonNode jsonObject = null;
//		boolean keypresent = false;
//		int partitions=KafkaHelper.getNumberofPartitions(topic,BOOTSTRAP_SERVERS);
//		logger.info("Number of partions for topic " + topic+" are "+ partitions);
//		
//		
//		
//		out : for(int i =0;i<partitions;i++) {
//			TopicPartition partition = new TopicPartition(topic, i);
//	        consumer.off
//			
//			
//			logger.info("checking for partion no. "+ i);
//			
//			ConsumerRecords<String, String> cr  = KafkaHelper.seekToEndAndGetRecordsfromPartition(consumer, topic, i);
//			for (ConsumerRecord<String, String> record : cr) {
//	}
	

	


}