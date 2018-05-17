package be.kdg.bhs.organizer.services;

import be.kdg.bhs.organizer.api.*;
import be.kdg.bhs.organizer.dto.DTOtoEO;
import be.kdg.bhs.organizer.dto.SuitcaseMessageDTO;
import be.kdg.bhs.organizer.model.Suitcase;
import be.kdg.bhs.organizer.repo.InMemoryRepoFactory;
import be.kdg.bhs.organizer.repo.InMemoryRepoFactoryImpl;
import be.kdg.bhs.organizer.utils.CacheObject;
import be.kdg.bhs.organizer.utils.InMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author Michael
 * @project BHS
 * Controller of the routing proces for incoming suitcases.
 */
public class RoutingService implements MessageConsumerListener {


    private Logger logger = LoggerFactory.getLogger(RoutingService.class);
    private MessageConsumerService messageConsumerService;
    private MessageProducerService producerService;
    private MessageFormatterService messageFormatterService;
    private FlightService flightService;
    private ConveyorService conveyorService;
    private CalculateRouteService calculateRouteService;
    private InMemoryRepoFactory<InMemoryCache> inMemoryRepo;

    public RoutingService(MessageConsumerService messageConsumerService, MessageProducerService producerService, MessageFormatterService messageFormatterService, FlightService flightService, ConveyorService conveyorService, CalculateRouteService calculateRouteService, InMemoryRepoFactory inMemoryRepo) {
        this.messageConsumerService = messageConsumerService;
        this.producerService = producerService;
        this.messageFormatterService = messageFormatterService;
        this.flightService = flightService;
        this.conveyorService = conveyorService;
        this.calculateRouteService = calculateRouteService;
        this.inMemoryRepo = inMemoryRepo;
    }

    public void start() {
        //Activating the messageConsumer to consume from the Queue.
        this.messageConsumerService.initialize(this,messageFormatterService);
    }

    /**
     * A procedural method, executing businesslogic
     * @param messageDTO the message converted from the wire format to a DTO.
     */
    @Override
    public void onReceive(SuitcaseMessageDTO messageDTO) {
        logger.info("Entered onReceive(): Suitcase {} is being processed", messageDTO.getId());

        //Transforming an incoming messageDTO to suitCase and adding it to the suitCaseCacheRepository managed by the InMemoryRepoFactoryImpl
        Suitcase suitcase = DTOtoEO.suitCaseDTOtoEO(messageDTO);
        inMemoryRepo.getInMemoryCache(suitcase.getClass().getSimpleName()).putCacheObject(suitcase.getId(),new CacheObject(suitcase));


        System.out.println("Handling the message");
        System.out.println(messageDTO.toString());

        if(flightService!=null) {
            suitcase.setBoardingConveyorId(flightService.flightInFormation(suitcase.getFlightNumber()));
            System.out.println("BoardingConveyor:" +suitcase.getBoardingConveyorId());
        }
        else {
            //TODO EXCEPTION
        }

        if(conveyorService!=null) {
            conveyorService.routeInformation(suitcase).getRoutes().forEach(v -> System.out.println(v));
            System.out.println();
        }
        else {
            //TODO EXCEPTION
        }

        this.calculateRouteService.shortestRoute();




    }

    @Override
    public void onError() {

    }
}
