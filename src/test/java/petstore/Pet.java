//1 - Pacote

package petstore;

//2 - Bibliotecas

//3 - Classe

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {

    // 3.1 - Atributos (caracteristicas de um Objeto, seria os campos que vamos preencher)
    String uri = "https://petstore.swagger.io/v2/pet";     //endereço da entidade pet


    // 3.2 - Métodos e Funções (métodos são as ações que nao retornan nenhum valor e as funções fazem as ações e retornan um resultado)

  public String lerJson(String caminhoJson) throws IOException {
      return new String(Files.readAllBytes(Paths.get(caminhoJson)));
  }

// incluir - create - post

    @Test(priority = 1)  //identifica o método/função como um teste, para o TestNG
    public void incluirPet() throws IOException {
       String jsonBody = lerJson("dadosdb/pet1.json");


        given()  //dado
            .contentType("application/json") // comum em API REST - antigas eram "text/xml"
            .log().all()
            .body(jsonBody)

        .when() //quando
            .post(uri)

        .then()   //entao
            .log().all()
            .statusCode(200)
            .body("name", is ("Legolas"))
            .body("status", is("available"))
                .body("category.name", is ("cattest2022"))
                .body("tags.name", contains("cat category test"))

        ;
  }
    @Test(priority = 2)
    public void consultarPet() {
        String petID = "0501199610";

        String token =
                given()
                        .contentType("application/json")
                        .log().all()

                .when()
                        .get(uri + "/" + petID) //GET = consultar
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Legolas"))
                        .body("category.name", is("cattest2022"))
                        .body("status", is("available"))

                .extract()
                        .path("category.name")
                ;
                System.out.println("O Token é " + token);
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
      String jsonBody = lerJson("dadosdb/pet2.json");

      given()
              .contentType("application/json")
              .log().all()
              .body(jsonBody)

      .when()
              .put(uri)  //put = alterar

      .then()
              .log().all()
              .statusCode(200)
              .body("name", is("Legolas"))
              .body("status", is("sold"))
      ;
    }

    @Test(priority = 4)
    public void excluirPet() {
        String petID = "501199610";

      given()
              .contentType("application/json")
              .log().all()

      .when()
              .delete(uri + "/" + petID)

      .then()
              .log().all()
              .statusCode(200)
              .body("code", is (200))
              .body("type", is("unknown"))
              .body("message", is(petID))
      ;
    }

}