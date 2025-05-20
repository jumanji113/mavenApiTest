package bft.schol.fileTests;

import bft.schol.BaseApiTest;
import io.qameta.allure.Attachment;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static assertions.Conditions.haseStatusCode;

@Epic("Апи тесты с файлами")
public class FileTests extends BaseApiTest {

    @Attachment(value = "downloaded", type = "image/png")
    public byte[] attachFile(byte[] bytes){
        return bytes;
    }

    @Test
    @DisplayName("Позитивный тест на скачивание файлов")
    public void downloadImage(){
        byte[] arrayByteFile = fileService.downloadImage()
                .asResponse()
                .asByteArray();
        attachFile(arrayByteFile);
        File expectedFile = new File("src/test/resources/threadqa.jpeg");

        Assertions.assertEquals(expectedFile.length(), arrayByteFile.length);
    }

    @Test
    @DisplayName("Позитивный тест на загрузку файла")
    public void uploadImage(){
        File expectedFile = new File("src/test/resources/threadqa.jpeg");

        fileService.uploadFile(expectedFile)
                .should(haseStatusCode(200));

        byte[] actualFile = fileService.donwloadLastUploaded()
                .asResponse().asByteArray();
        attachFile(actualFile);

        Assertions.assertTrue(actualFile.length != 0);
        Assertions.assertEquals(actualFile.length, expectedFile.length());
    }
}
