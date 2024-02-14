package com.wewoo.in.controller;

import com.wewoo.in.dto.CreateMappingDto;
import com.wewoo.in.model.UrlMapping;
import com.wewoo.in.repository.MappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UrlController {

    private final MappingRepo controller;
    private final String auth = "thisisasampleauth" ;
    private static final String salt = "samplesalt";

    @Autowired
    UrlController(MappingRepo controller){
//        this.taskModel = taskModel;
        this.controller = controller;
    }


    @PostMapping("/createMapping")
    public ResponseEntity<String> createMapping(@RequestBody CreateMappingDto request){
        if (Objects.equals(request.auth(), this.auth)){
            System.out.println(request.target());
            String url = generateIdentifier(request.target());

            boolean isPresent = controller.existsByIndex(url);

            if(!isPresent){
                UrlMapping mapping = new UrlMapping(null, url, request.target(), LocalDateTime.now());
                controller.save(mapping);
                return ResponseEntity.status(HttpStatus.OK).body("Mapping added successfully -- access it through http://wewoo.in/"+ url);
            }
            else{
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Mapping already present");
            }

            // Sorry hash conflict, the estimated number of grains of sand on Earth is around 8 * 10 ^ 18 zeros,
            // and the chance of this happening is 1 in 10^60

        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Invalid auth code");

    }
    @GetMapping("/{index}")
    public RedirectView getMapping(@PathVariable String index){
        Optional<UrlMapping> targetUrl = controller.findByIndex(index);
        return targetUrl.map(urlMapping
                -> new RedirectView(urlMapping.url())).orElseGet(()
                -> new RedirectView("/error", true));
    }


    private String generateIdentifier(String targetUrl) {
        String input = targetUrl + salt;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            String encodedHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            return encodedHash.substring(0, 7); // Use first 6 characters of the hash as identifier
        } catch (NoSuchAlgorithmException e) {
            System.out.println("!! generateIdentifier Failed");
            throw new RuntimeException(e);
        }
    }


}
