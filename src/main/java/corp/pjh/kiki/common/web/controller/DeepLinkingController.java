package corp.pjh.kiki.common.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("!testcase")
@RestController
public class DeepLinkingController {

    @Value("${client.android.package-name}")
    String androidPackageName;

    @Value("${client.android.sha-256}")
    String androidSha256;

    @GetMapping("/app")
    public String app() {
        return "딥링킹 or 플레이,앱 스토어";
    }

    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<String> android() {
        String assetLinksJson = String.format("""
                [
                  {
                    "relation": ["delegate_permission/common.handle_all_urls"],
                    "target": {
                      "namespace": "android_app",
                      "package_name": "%s",
                      "sha256_cert_fingerprints": [
                        "%s"
                      ]
                    }
                  }
                ]
                """, androidPackageName, androidSha256);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(assetLinksJson);
    }

}
