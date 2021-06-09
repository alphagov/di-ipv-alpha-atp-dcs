package uk.gov.cabinetoffice.di.ipv.dcsatpservice.utils;

import java.io.*;

public abstract class ClasspathFileReader {
    public static Reader getResourceFileReader(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader;
//                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
