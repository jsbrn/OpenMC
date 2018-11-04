package io;

import java.io.*;

public class FileLoader {

    public static String getContents(String url, boolean external) {
        try {

            String contents = "";

            BufferedReader br;
            if (!external) {
                InputStream in = FileLoader.class.getResourceAsStream(url);
                InputStreamReader inr = new InputStreamReader(in);
                br = new BufferedReader(inr);
            } else {
                br = new BufferedReader(new FileReader(new File(url)));
            }

            while (true) {
                String line = br.readLine();
                if (line == null) break;
                contents+=line.trim()+"\n";
            }

            return contents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeString(String contents, String location) {
        try {
            PrintWriter out = new PrintWriter(location);
            out.println(contents);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
