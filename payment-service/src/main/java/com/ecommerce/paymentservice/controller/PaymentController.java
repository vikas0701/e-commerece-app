package com.ecommerce.paymentservice.controller;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
//    public String processPayment(@RequestParam Long orderId) {
//    public String processPayment(@RequestParam PaymentEvent event) {
    public String processPayment(@RequestBody PaymentEvent event) {

//        boolean success = paymentService.processPayment(orderId);
//    	boolean success = paymentService.processPayment(event);
//
//        if(success) {
//            return "PAYMENT_SUCCESS";
//        }
//
//        throw new RuntimeException("Payment failed");
    	
    	paymentService.processPayment(event);
    	
    	 return "PAYMENT_REQUEST_RECEIVED";
    }

    @PostMapping("/refund")
    public String refundPayment(@RequestParam Long orderId) {

        System.out.println("Refunding payment for order " + orderId);

        return "REFUND_SUCCESS";
    }
}
