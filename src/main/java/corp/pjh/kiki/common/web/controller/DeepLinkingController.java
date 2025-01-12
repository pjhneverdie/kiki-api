package corp.pjh.kiki.common.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Profile("!testcase")
@RestController
@RequiredArgsConstructor
public class DeepLinkingController {

    @Value("${client.android.package-name}")
    String androidPackageName;

    @Value("${client.android.sha256-from-my-key}")
    String androidMySha256;

    @Value("${client.android.sha256-from-google-signing-key}")
    String androidGoogleSha256;

    private final ObjectMapper objectMapper;

    @GetMapping("/app")
    public String app() {
        return "딥링킹 or 플레이,앱 스토어";
    }

    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<String> android() throws Exception {
        ObjectNode target = objectMapper.createObjectNode();
        target.put("namespace", "android_app");
        target.put("package_name", androidPackageName);

        ArrayNode fingerprints = objectMapper.createArrayNode();
        fingerprints.add(androidMySha256);
        Optional.ofNullable(androidGoogleSha256).ifPresent(fingerprints::add);

        target.set("sha256_cert_fingerprints", fingerprints);

        ObjectNode relationNode = objectMapper.createObjectNode();
        relationNode.set("target", target);
        relationNode.putArray("relation").add("delegate_permission/common.handle_all_urls");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.createArrayNode().add(relationNode)));
    }

}
