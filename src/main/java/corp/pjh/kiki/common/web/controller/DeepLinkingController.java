package corp.pjh.kiki.common.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import corp.pjh.kiki.common.android.config.AndroidProperties;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Profile("!testcase")
@Controller
@RequiredArgsConstructor
public class DeepLinkingController {

    private final ObjectMapper objectMapper;

    private final AndroidProperties androidProperties;

    @GetMapping("/app")
    public String app(@RequestParam(value = "tokens", required = false) String tokens, Model model) {
        model.addAttribute("tokens", tokens);

        return "appscheme";
    }

    @ResponseBody
    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<String> android() throws Exception {
        ObjectNode target = objectMapper.createObjectNode();
        target.put("namespace", "android_app");
        target.put("package_name", androidProperties.getPackageName());

        ArrayNode fingerprints = objectMapper.createArrayNode();
        fingerprints.add(androidProperties.getSha256FromMyKey());
        Optional.ofNullable(androidProperties.getSha256FromGoogleSigningKey()).ifPresent(fingerprints::add);

        target.set("sha256_cert_fingerprints", fingerprints);

        ObjectNode relationNode = objectMapper.createObjectNode();
        relationNode.set("target", target);
        relationNode.putArray("relation").add("delegate_permission/common.handle_all_urls");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.createArrayNode().add(relationNode)));
    }

}
