package jkafka.estudo.consumer.eventos;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ConsumidorEvento {
    private final KafkaConsumer<String, String> consumer;

    public ConsumidorEvento() {
        consumer = criarConsumer();
    }

    public KafkaConsumer<String, String> criarConsumer() {
        if (consumer != null) {
            return consumer;
        }

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "default");

        return new KafkaConsumer<String, String>(properties);
    }

    public void executar() {
        List<String> topicos = new ArrayList<>();
        topicos.add("RegistroEvento");
        consumer.subscribe(topicos);

        log.info("Iniciando consumer...");
        boolean continuar = true;
        while(continuar){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                gravaMensagem(record.topic(), record.partition(), record.value());
                if (record.value().equals("FECHAR")) {
                    continuar = false;
                }
            }
        }
        consumer.close();
    }

    private void gravaMensagem(String topico, int particao, String mensagem) {
        log.info("Topico{}, Particao:{}, Mensagem{}", topico, particao, mensagem);
    }
}
