package services;

import assertions.AssertableResponse;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class FileService {

    public AssertableResponse downloadImage() {
        return new AssertableResponse(given().get("/files/download").then());
    }

    public AssertableResponse donwloadLastUploaded() {
        return new AssertableResponse(given().get("/files/downloadLastUploaded").then());
    }

    @SneakyThrows
    public AssertableResponse uploadFile(File file) {
        return new AssertableResponse(given().contentType(ContentType.MULTIPART)
                .multiPart("file", "myFile", Files.readAllBytes(file.toPath()))
                .post("/files/upload").then());
    }
}
