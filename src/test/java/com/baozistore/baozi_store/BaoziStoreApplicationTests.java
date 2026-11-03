package com.baozistore.baozi_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import java.net.URI;
import java.net.http.*;
import java.util.regex.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:h2:mem:api-test;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BaoziStoreApplicationTests {
    @Autowired Environment environment;
    final HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> call(String method, String path, String json, int expected) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + environment.getProperty("local.server.port") + path))
            .header("Content-Type", "application/json")
            .method(method, json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json)).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expected, response.statusCode(), method + " " + path + " " + response.body());
        return response;
    }
    long id(HttpResponse<String> response) {
        var matcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(response.body());
        assertTrue(matcher.find()); return Long.parseLong(matcher.group(1));
    }
    @Test void crudAndValidation() throws Exception {
        call("POST", "/clientes", "{\"nome\":\" \"}", 400);
        call("POST", "/clientes", "{invalid}", 400);
        call("GET", "/clientes/999999", null, 404);
        long c = id(call("POST", "/clientes", "{\"nome\":\"Cliente Exemplo\",\"clienteDesde\":\"2026-11-02\"}", 201));
        assertTrue(call("GET", "/clientes/"+c, null, 200).body().contains("Cliente Exemplo"));
        call("POST", "/produtos", "{\"nome\":\"Baozi\",\"preco\":-1,\"estoque\":true}", 400);
        call("POST", "/produtos", "{\"nome\":\"Baozi\",\"preco\":8.555,\"estoque\":true}", 400);
        long p = id(call("POST", "/produtos", "{\"nome\":\"Baozi de porco\",\"preco\":8.50,\"estoque\":true}", 201));
        call("POST", "/pedidos", "{\"clienteId\":"+c+",\"produtoId\":"+p+",\"quantidade\":0}", 400);
        call("POST", "/pedidos", "{\"clienteId\":999999,\"produtoId\":"+p+",\"quantidade\":3}", 404);
        long unavailable = id(call("POST", "/produtos", "{\"nome\":\"Indisponível\",\"preco\":5,\"estoque\":false}", 201));
        call("POST", "/pedidos", "{\"clienteId\":"+c+",\"produtoId\":"+unavailable+",\"quantidade\":3}", 409);
        long order = id(call("POST", "/pedidos", "{\"clienteId\":"+c+",\"produtoId\":"+p+",\"quantidade\":3}", 201));
        for (String resource : new String[]{"clientes","produtos","pedidos"}) call("GET", "/"+resource, null, 200);
        call("GET", "/produtos/"+p, null, 200);
        assertTrue(call("GET", "/pedidos/"+order, null, 200).body().contains("\"quantidade\":3"));
        call("DELETE", "/clientes/"+c, null, 409);
        call("DELETE", "/produtos/"+p, null, 409);
        call("DELETE", "/pedidos/"+order, null, 204);
        call("GET", "/pedidos/"+order, null, 404);
        call("DELETE", "/clientes/"+c, null, 204);
        call("DELETE", "/produtos/"+p, null, 204);
        call("DELETE", "/produtos/"+unavailable, null, 204);
        call("DELETE", "/clientes/"+c, null, 404);
    }
}
