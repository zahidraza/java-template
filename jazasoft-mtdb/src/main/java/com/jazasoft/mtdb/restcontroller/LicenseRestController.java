package com.jazasoft.mtdb.restcontroller;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.dto.License;
import com.jazasoft.mtdb.service.LicenseService;
import org.hibernate.annotations.GeneratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdzahidraza on 23/09/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_LICENSE)
public class LicenseRestController {
    private final Logger logger = LoggerFactory.getLogger(LicenseRestController.class);

    private LicenseService licenseService;

    public LicenseRestController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping(ApiUrls.URL_LICENSE_ACTIVATE)
    public ResponseEntity<?> activateLicense(@RequestParam("username") String username,
                                             @RequestParam("productCode") String productCode,
                                             @RequestParam("productKey") String productKey,
                                             HttpServletRequest request) {
        logger.debug("activate license");
        String tenant = (String) request.getAttribute(Constants.CURRENT_TENANT_IDENTIFIER);
        if (tenant != null) {
            License license = licenseService.activateLicense(tenant,username,productCode,productKey);
            return ResponseEntity.ok(license);
        }else {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(ApiUrls.URL_LICENSE_CHECK)
    public ResponseEntity<?> checkLicense(HttpServletRequest request) {
        logger.debug("activate license");
        String tenant = (String) request.getAttribute(Constants.CURRENT_TENANT_IDENTIFIER);
        if (tenant != null) {
            License license = licenseService.getLicense(tenant);
            if (license == null || !license.isActivated()) {
                Map<String, Object> body = new HashMap<>();
                body.put("status", "PRODUCT_LICENSE_NOT_ACTIVATED");
                body.put("message", "Product License not activated. Activate Product License first.");
                return ResponseEntity.ok(body);
            }
            if (license.isExpired()) {
                license.setStatus("PRODUCT_LICENSE_EXPIRED");
                license.setMessage("Product License expired. Renew Product License first.");
            }
            return ResponseEntity.ok(license);
        }else {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }
    }
}
