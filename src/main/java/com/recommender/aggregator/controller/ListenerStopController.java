package com.recommender.aggregator.controller;

import com.recommender.api.StopListenerApi;
import com.recommender.model.ListenerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ListenerStopController implements StopListenerApi {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Override
    public ResponseEntity<String> stopListener(ListenerId listenerId) {
        MessageListenerContainer container = kafkaListenerEndpointRegistry.getListenerContainer(listenerId.getValue());

        if(container == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Listener not found");
        }

        if(container.isRunning()){
            container.stop();
            return ResponseEntity.ok("Listener stopped");
        }else{
            return ResponseEntity.ok("Listener already stopped");
        }
    }
}
