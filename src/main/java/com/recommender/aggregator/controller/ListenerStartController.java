package com.recommender.aggregator.controller;

import com.recommender.api.StartListenerApi;
import com.recommender.model.ListenerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ListenerStartController implements StartListenerApi {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Override
    public ResponseEntity<String> startListener(ListenerId listenerId) {
        MessageListenerContainer container = kafkaListenerEndpointRegistry.getListenerContainer(listenerId.getValue());

        if(container == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Listener not found");
        }

        if(!container.isRunning()){
            container.start();
            return ResponseEntity.ok("Listener started");
        }else{
            return ResponseEntity.ok("Listener already running");
        }
    }
}
