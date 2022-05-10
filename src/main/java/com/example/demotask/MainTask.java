package com.example.demotask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableTask
@Component
public class MainTask implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(MainTask.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {

        // call the incident api to fetch resolved P1/P2 incidents for last 1 week
        String resourceUrl
                = "https://dev127028.service-now.com/api/now/table/incident?sysparm_query=priority%3D1%5EORpriority%3D2%5Eincident_state%3D7&sysparm_limit=20";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("admin", "-no@f1cXWAS8");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(resourceUrl, HttpMethod.GET, httpEntity, JsonNode.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        // call template confluence API

       /* OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://kiranmlb.atlassian.net/wiki/rest/api/template/688129?expand=body.storage&spaceKey=HACKATHON")
                .method("GET", null)
                .addHeader("Authorization", "Basic a2lyYW5tbGJAeWFob28uY29tOmZJdUc1SVdrSGtmOThoMTRSWWdEMTA5Nw==")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
    */

        String confluenceTemplateUrl
                = "https://kiranmlb.atlassian.net/wiki/rest/api/template/688129?expand=body.storage&spaceKey=HACKATHON";
        HttpHeaders confluenceHeader = new HttpHeaders();
        confluenceHeader.setContentType(MediaType.APPLICATION_JSON);
        confluenceHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        confluenceHeader.set("Authorization", "Basic a2lyYW5tbGJAeWFob28uY29tOmZJdUc1SVdrSGtmOThoMTRSWWdEMTA5Nw==");
        HttpEntity<String> confluenceHttpEntity = new HttpEntity<>(confluenceHeader);
        ResponseEntity<JsonNode> confluenceResponse = restTemplate.exchange(confluenceTemplateUrl, HttpMethod.GET, confluenceHttpEntity, JsonNode.class);

        System.out.println(confluenceResponse.getStatusCode());
        System.out.println(confluenceResponse.getBody());

        // for each incidents, create separate templöate storage object for each incidents

        JsonNode confleunceTemplateNode = confluenceResponse.getBody();
        JsonNode storageJsonNode = confleunceTemplateNode.get("body").get("storage").get("value");

        JsonNode incidentsReports = response.getBody();
        ArrayNode results = (ArrayNode) incidentsReports.get("result");

        for (JsonNode resultNode: results) {
            System.out.println(
                    resultNode.get("number")
            );
        }

        // push to confluence with proper name



    }
}
