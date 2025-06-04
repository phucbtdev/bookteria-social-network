package com.recruiment.payment_service.factory;

import com.recruiment.payment_service.service.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentGatewayFactory {

    private final Map<String, PaymentGateway> gateways;

    public PaymentGatewayFactory(List<PaymentGateway> gatewayList) {
        this.gateways = gatewayList.stream()
                .collect(Collectors.toMap(
                        PaymentGateway::getGatewayName,
                        Function.identity()
                ));
    }

    public PaymentGateway getGateway(String gatewayName) {
        PaymentGateway gateway = gateways.get(gatewayName.toUpperCase());
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment gateway: " + gatewayName);
        }
        return gateway;
    }

    public List<String> getSupportedGateways() {
        return List.copyOf(gateways.keySet());
    }
}
