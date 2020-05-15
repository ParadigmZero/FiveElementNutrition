import java.io.*;
import java.util.HashMap;
/*
Datalink is used to create or update the CSV spreadsheet
The spreadsheet is to be called foodList.csv and is 11 columns wide
The content of the header does not matter, however the rows underneath
ideally contain the temperature values of cold,cool,neutral,warm,hot,blank or skip
Any blank or invalid value will be treated as "skip" which is equivalent to a blank values
that will not over-write another food entry with the same name.
"blank" can be entered as well which will overwrite anything with that blank value but in practice
this should not be necessary to store this directly in the spreadsheet. Blank is mostly used for input
in this application and will not show in the final spreadsheet.
 */
public class DataLink {

    private File file;
    private boolean newFile = false;
    private FileReader fileReader;
    private BufferedReader reader;
    private FileWriter fileWriter;
    private BufferedWriter writer;

    HashMap<String, TemperatureEnum[]> foods = new HashMap<String, TemperatureEnum[]>();

    TemperatureEnum[] nullTemps = new TemperatureEnum[10];

    TemperatureEnum[] auxTemps;


    private String headers = "Food,Liver,Gallbladder,Heart,Small intestine,Spleen,Stomach,Lungs,Large intestine,Kidneys,Bladder\n";


    public DataLink() throws IOException {

        nullTemps[0] = TemperatureEnum.SKIP;
        nullTemps[1] = TemperatureEnum.SKIP;
        nullTemps[2] = TemperatureEnum.SKIP;
        nullTemps[3] = TemperatureEnum.SKIP;
        nullTemps[4] = TemperatureEnum.SKIP;
        nullTemps[5] = TemperatureEnum.SKIP;
        nullTemps[6] = TemperatureEnum.SKIP;
        nullTemps[7] = TemperatureEnum.SKIP;
        nullTemps[8] = TemperatureEnum.SKIP;
        nullTemps[9] = TemperatureEnum.SKIP;


        file = new File("foodList.csv");


        if (!file.exists()) {
            newFile = true;
            file.createNewFile();
        } else {
            read();
        }
    }

    public void read() throws IOException {
        fileReader = new FileReader(file);
        reader = new BufferedReader(fileReader);

        // ignore the header
        reader.readLine();


        String line = reader.readLine();

        // the lines we read from the spreadsheet, with 0 being the first column ( the food name)
        String[] readValues;

        // used for quicker comparisons
        TemperatureEnum[] values = new TemperatureEnum[10];


        while (line != null) {


            line = line.toLowerCase();

            readValues = line.split(",", -1);


            if (!readValues[0].isEmpty()) {

                for (int i = 1; i < readValues.length; i++) {
                    switch (readValues[i]) {
                        case "":
                            values[i - 1] = TemperatureEnum.SKIP;
                            break;
                        case "cold":
                            values[i - 1] = TemperatureEnum.COLD;
                            break;
                        case "cool":
                            values[i - 1] = TemperatureEnum.COOL;
                            break;
                        case "neutral":
                            values[i - 1] = TemperatureEnum.NEUTRAL;
                            break;
                        case "warm":
                            values[i - 1] = TemperatureEnum.WARM;
                            break;
                        case "hot":
                            values[i - 1] = TemperatureEnum.HOT;
                            break;
                        case "blank":
                            values[i - 1] = TemperatureEnum.HOT;
                            break;
                        default:
                            values[i - 1] = TemperatureEnum.SKIP;

                    }

                }

/*
                if(foods.containsKey(readValues[0]))
                {
                    System.out.println(readValues[0]+" already exists in the spreadsheet. Overwriting.");
                }
                */

                foods.put(readValues[0], values.clone());

            }


            line = reader.readLine();
        }


    }

    public void write() throws IOException {
        fileWriter = new FileWriter(file);
        writer = new BufferedWriter(fileWriter);
        writer.write(headers);


        for (HashMap.Entry<String, TemperatureEnum[]> entry : foods.entrySet()) {
            // sb.append(entry.getKey());
            writer.write(entry.getKey());

            for (TemperatureEnum t : entry.getValue()) {

                writer.write(",");
                writer.write((convertEnum(t)));

            }

            writer.write("\n");

        }

        writer.flush();
        writer.close();
    }

    /*

    // could be used to consolidate rows in a pre-existing CSV database
    public static void main(String[] args) throws IOException {
        DataLink dl = new DataLink();
        dl.write();
    }
    */

    public String convertEnum(TemperatureEnum t) {
        switch (t) {
            case COLD:
                return "cold";
            case COOL:
                return "cool";
            case NEUTRAL:
                return "neutral";
            case WARM:
                return "warm";
            case HOT:
                return "hot";
            default:
                return "";
        }
    }

    /*

    // can be used for GUIs etc.
    public static TemperatureEnum basicStringToTempEnum(String s) {
        switch (s) {
            case "cold":
                return TemperatureEnum.COLD;
            case "cool":
                return TemperatureEnum.COOL;
            case "neutral":
                return TemperatureEnum.NEUTRAL;
            case "warm":
                return TemperatureEnum.WARM;
            case "hot":
                return TemperatureEnum.HOT;
            default:
                return TemperatureEnum.BLANK;
        }
    }

*/

    public TemperatureEnum stringToTempEnum(String s) {
        if (s.isEmpty()) {
            return TemperatureEnum.SKIP;
        }

        if (s.length() >= 3) {
            if (s.substring(0, 3).equals("col")) {
                return TemperatureEnum.COLD;
            } else if (s.substring(0, 3).equals("coo")) {
                return TemperatureEnum.COOL;
            }
        }


        if (s.charAt(0) > 47 && s.charAt(0) < 58) {
            return convertChar(s.charAt(0));
        }

        switch (s.charAt(0)) {
            case 'n':
                return TemperatureEnum.NEUTRAL;
            case 'w':
                return TemperatureEnum.WARM;
            case 'h':
                return TemperatureEnum.HOT;
            case 'b':
                return TemperatureEnum.BLANK;

        }

        // invalid value will just be skipped
        return TemperatureEnum.SKIP;
    }


    public TemperatureEnum convertChar(char c) {

        switch (c) {
            case '0':
                return TemperatureEnum.BLANK;
            case '1':
                return TemperatureEnum.COLD;
            case '2':
                return TemperatureEnum.COOL;
            case '3':
                return TemperatureEnum.NEUTRAL;
            case '4':
                return TemperatureEnum.WARM;
            case '5':
                return TemperatureEnum.HOT;
            default:
                return TemperatureEnum.SKIP;
        }
    }


    // enter all the organs values
    public void putAll(String foodName, TemperatureEnum[] t) {
        // not as simple as put.. Need to over-write
        if (foods.containsKey(foodName)) {
            System.out.println(foodName + " exists. May over-write pre-existing temperature(s) with non-skip values");

            for (int i = 0; i < t.length; i++) {
                // if the value is not a skip then over-write it
                if (!(t[i].equals(TemperatureEnum.SKIP))) {

                    // only report a non-intentionally blank (non-skip) value being over-written
                    if (foods.get(foodName)[i].equals(TemperatureEnum.SKIP)) {
                        System.out.println("input: " + t[i]);
                    } else {
                        System.out.println(t[i] + " over-writes " + foods.get(foodName)[i]);
                    }

                    foods.get(foodName)[i] = t[i];
                }
            }
        } else {
            foods.put(foodName, t);
        }
    }


    // enter one organs value
    public void put(String s, OrganEnum o, TemperatureEnum e) {
        auxTemps = nullTemps.clone();

        auxTemps[getOrganIndex(o)] = e;

        putAll(s, auxTemps.clone());
    }

    // enter an organ pairs value (wood,fire,earth,metal and water settings)
    public void put(String s, OrganEnum o, TemperatureEnum e, OrganEnum o2, TemperatureEnum e2) {
        auxTemps = nullTemps.clone();

        auxTemps[getOrganIndex(o)] = e;
        auxTemps[getOrganIndex(o2)] = e2;

        putAll(s, auxTemps.clone());
    }


    public static int getOrganIndex(OrganEnum o) {
        switch (o) {
            case LIVER:
                return 0;
            case GALLBLADDER:
                return 1;
            case HEART:
                return 2;
            case SMALL_INTESTINE:
                return 3;
            case SPLEEN:
                return 4;
            case STOMACH:
                return 5;
            case LUNGS:
                return 6;
            case LARGE_INTESTINE:
                return 7;
            case KIDNEYS:
                return 8;
            case BLADDER:
                return 9;
            default:
                return -1; // error


        }
    }
}
