package com.vtv.appointment.service.inspection;

import com.vtv.appointment.client.ProducerClient;
import com.vtv.appointment.exception.OrderInspectionErrorException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.exception.commons.GenericProducerException;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InspectionProducerServiceImpl implements InspectionProducerService {

    private final ProducerClient<OrderInspectionDto> producerClient;

    public static final String INSPECTION_PRODUCER_ERROR_MESSAGE = "An error occurred while generate an inspection order";
    public static final Integer INSPECTION_PRODUCER_ERROR_CODE = 560;
    public InspectionProducerServiceImpl(ProducerClient<OrderInspectionDto> producerClient) {
        this.producerClient = producerClient;
    }

   //TODO: Deberia ser asincronico enviar el evento, o analizar si tiene sentido que pasa si falla digamos
    @Override
    public void orderInspection(OrderInspectionDto orderInspectionDto) {
        try {
            producerClient.send(orderInspectionDto);
        } catch (GenericProducerException genericProducerException) {
            log.error(INSPECTION_PRODUCER_ERROR_MESSAGE, genericProducerException);
            throw new OrderInspectionErrorException(
                    ExceptionError.builder()
                            .description(INSPECTION_PRODUCER_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(INSPECTION_PRODUCER_ERROR_CODE)
                                    .message(INSPECTION_PRODUCER_ERROR_MESSAGE)
                                    .build())
                            .build(), genericProducerException);
        }

    }
}
