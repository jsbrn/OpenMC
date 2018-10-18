package io;

import misc.MiscCommands;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Converts OpenMC data files from one version to another. DataConverter is only capable of
 * direct conversions between file versions.
 */
public abstract class DataConverter {

    public static final int CURRENT_VERSION = 3;
    private static HashMap<String, DataConverter> converters = new HashMap<String, DataConverter>();

    /**
     * Parse the file into an ArrayList of String[] instances, representing the text model.
     * Each String[] is an element, like a PlayerData or a Plot.
     * @param f The file to load.
     * @return An ArrayList, or null if the file could not be loaded (i.e. read error OR no converter found).
     */
    public static ArrayList<HashMap<String, String>> parse(File f) {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        try {
            //parse the file into a list of string -> string hashmaps
            BufferedReader in = new BufferedReader(new FileReader(f));
            int fileVersion = Integer.MAX_VALUE;
            boolean fileEmpty = true;
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                if (line.trim().length() == 0) continue;
                fileEmpty = false;
                HashMap<String, String> valueMap = new HashMap<String, String>();
                String params[] = line.trim().split(",\\s*");
                for (String param: params)
                    valueMap.put(param.trim().replaceAll("\\s*=.*", ""), param.trim().replaceAll(".*=\\s*", ""));
                data.add(valueMap);
                MiscCommands.printMap(valueMap);
                if (valueMap.get("type").equals("meta"))
                    fileVersion = Integer.parseInt(valueMap.get("version"));
            }
            if (fileEmpty) { System.out.println("data.txt is empty!"); return data; }
            //find a converter for the file and convert it into a proper format
            //(will default to making no changes if no converter is found or if file is latest version)
            DataConverter converter = get(fileVersion, CURRENT_VERSION);
            if (converter == null) {
                System.out.println("Cannot convert from version "+fileVersion+" to version "+CURRENT_VERSION+"!");
                return null;
            } else {
                if (fileVersion != CURRENT_VERSION)
                    System.out.println("Converting to version "+CURRENT_VERSION+" (from version "+fileVersion+")...");
                return converter.convert(data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find data.txt in the plugin directory!");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("An error occurred trying to read the data file.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the converter from inVersion to outVersion.
     * @param inVersion The output file version.
     * @param outVersion The output file version.
     * @return Returns a DataConverter if found, null otherwise.
     */
    private static DataConverter get(int inVersion, int outVersion) {
        if (inVersion == outVersion) return converters.get("default");
        return converters.get(inVersion+" -> "+outVersion);
    }

    public static void register() {
        //a converter that does no conversion at all, used when the versions are identical
        converters.put("default", new DataConverter() {
            public ArrayList<HashMap<String, String>> convert(ArrayList<HashMap<String, String>> data) { return data; }
        });
    }

    abstract ArrayList<HashMap<String, String>> convert(ArrayList<HashMap<String, String>> source);

}
