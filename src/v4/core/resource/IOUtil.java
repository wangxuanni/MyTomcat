package v4.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil{

        public static byte[] getBytesFromFile(String fileName) throws IOException {
            InputStream in = IOUtil.class.getResourceAsStream(fileName);
            if (in == null) {
                throw new FileNotFoundException();
            }
            return getBytesFromStream(in);
        }

        public static byte[] getBytesFromStream(InputStream in) throws IOException {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = in.read(buffer)) != -1){
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            in.close();
            return outStream.toByteArray();
        }

    }

