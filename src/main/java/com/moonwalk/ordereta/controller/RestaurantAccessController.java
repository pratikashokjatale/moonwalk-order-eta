package com.moonwalk.ordereta.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.moonwalk.ordereta.entity.Restaurant;
import com.moonwalk.ordereta.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants/{id}")
@RequiredArgsConstructor
public class RestaurantAccessController {

    private final RestaurantService restaurantService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    // Helper method to build the URL
    private String getOrderingUrl(Long restaurantId) {
        return frontendUrl + "/order?restaurantId=" + restaurantId;
    }

    @GetMapping("/order-link")
    public ResponseEntity<Map<String, String>> getOrderLink(@PathVariable Long id) {
        // Verify restaurant exists
        Restaurant restaurant = restaurantService.getRestaurant(id);
        
        String url = getOrderingUrl(restaurant.getId());
        return ResponseEntity.ok(Map.of("restaurantName", restaurant.getName(), "url", url));
    }

    @GetMapping("/qr-code")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long id) {
        try {
            // Verify restaurant exists
            Restaurant restaurant = restaurantService.getRestaurant(id);
            
            String url = getOrderingUrl(restaurant.getId());
            
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray(); 
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            
            return new ResponseEntity<>(pngData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
