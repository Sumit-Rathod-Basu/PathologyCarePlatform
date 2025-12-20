package Pathology.Care.Platform.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

@Service
public class Barcodeservice {
 
     
    public String generateBarcodeBase64(String text) throws Exception {

        Code128Writer writer = new Code128Writer();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, 150, 60);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", output);

        return Base64.getEncoder().encodeToString(output.toByteArray());
    }
}
