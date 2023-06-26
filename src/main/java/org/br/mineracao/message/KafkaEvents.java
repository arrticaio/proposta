package org.br.mineracao.message;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineracao.dto.ProposalDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaEvents {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);


    @Channel("proposal")
    Emitter<ProposalDTO> proposalDTOEmitter;

    public void sendNewKafkaEvent(ProposalDTO proposalDTO){
        LOG.info("-- ENVIANDO NOVA PROPOSTA PARA TOPICO KAFKA --");
        proposalDTOEmitter.send(proposalDTO).toCompletableFuture().join();
    }
}
