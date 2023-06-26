package org.br.mineracao.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineracao.dto.ProposalDatailsDTO;

@ApplicationScoped
public interface ProposalService {

    ProposalDatailsDTO findFullProposal(long id);

    void createNewProposal(ProposalDatailsDTO proposalDatailsDTO);

    void removeProposal(long id);
}
