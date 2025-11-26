package bank.BancoCentral.controller;

import bank.BancoCentral.model.ParticipanteSPI;
import bank.BancoCentral.service.ParticipanteSPIService;

public class ParticipanteSPIController extends _GenericController<ParticipanteSPI> {

    private final ParticipanteSPIService participanteSPIService;

    protected ParticipanteSPIController(ParticipanteSPIService participanteSPIService) {
        super(participanteSPIService);
        this.participanteSPIService = participanteSPIService;
    }
}
