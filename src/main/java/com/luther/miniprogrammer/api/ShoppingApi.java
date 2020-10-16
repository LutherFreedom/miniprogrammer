package com.luther.miniprogrammer.api;

import com.luther.miniprogrammer.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ShoppingApi {

    @Autowired
    private ShoppingService shoppingService;

    @PostMapping("file/analysis")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        shoppingService.analysis(file);
        return ResponseEntity.ok(null);
    }

    @GetMapping("search")
    public ResponseEntity<?> search(@RequestParam("param")String param){
        return ResponseEntity.ok(shoppingService.search(param));
    }
}
