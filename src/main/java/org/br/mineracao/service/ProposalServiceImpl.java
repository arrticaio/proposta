package org.br.mineracao.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.br.mineracao.dto.ProposalDTO;
import org.br.mineracao.dto.ProposalDatailsDTO;
import org.br.mineracao.entity.ProposalEntity;
import org.br.mineracao.message.KafkaEvents;
import org.br.mineracao.repository.ProposalRepository;

import java.time.LocalDate;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvents kafkaEvents;

    @Override
    public ProposalDatailsDTO findFullProposal(long id) {

        ProposalEntity proposal = proposalRepository.findById(id);

        if(proposal == null){
            return new ProposalDatailsDTO();
        }
        return  ProposalDatailsDTO.builder()
                .proposalID(proposal.getId())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .country(proposal.getCountry())
                .priceTonne(proposal.getPriceTonne())
                .customer(proposal.getCustomer())
                .tonnes(proposal.getTonnes())
                .build();

    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDatailsDTO proposalDatailsDTO) {
        ProposalDTO proposalDTO = buildAndSaveNewProposal(proposalDatailsDTO);
        kafkaEvents.sendNewKafkaEvent(proposalDTO);
    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }

    @Transactional
    public ProposalDTO buildAndSaveNewProposal(ProposalDatailsDTO proposalDatailsDTO) {

        try {
            ProposalEntity proposal = new ProposalEntity();

            proposal.setCreated(LocalDate.now());
            proposal.setProposalValidityDays(proposalDatailsDTO.getProposalValidityDays());
            proposal.setCountry(proposalDatailsDTO.getCountry());
            proposal.setCustomer(proposalDatailsDTO.getCustomer());
            proposal.setPriceTonne(proposalDatailsDTO.getPriceTonne());
            proposal.setTonnes(proposalDatailsDTO.getTonnes());

            proposalRepository.persist(proposal);

            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposal.getCustomer()).get().getId())
                    .priceTonne(proposal.getPriceTonne())
                    .customer(proposal.getCustomer())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException();

        }


    }
}
