package org.br.mineracao.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.br.mineracao.dto.ProposalDatailsDTO;
import org.br.mineracao.service.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
public class ProposalController {

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    //@RolesAllowed({"user","manager"})
    public ProposalDatailsDTO findDetailsProposal(@PathParam("id") long id){
        return proposalService.findFullProposal(id);
    }

    @POST
    //@RolesAllowed("proposql-customer")
    public Response createProposal (ProposalDatailsDTO proposalDatailsDTO){
        LOG.info("--- Recebendo Proposta de Compra ---");

        try {
            proposalService.createNewProposal(proposalDatailsDTO);

            return Response.ok().build();

        }catch (Exception e){
            return Response.serverError().build();
        }

    }

    @DELETE
    @Path("/{id}")
    //@RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") long id){

        try {
            proposalService.removeProposal(id);
            return Response.ok().build();

        }catch (Exception e){
            return Response.serverError().build();
        }

    }

}
