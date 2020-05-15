import java.io.IOException;
import java.util.Scanner;

public class Console {

    DataLink dl = new DataLink();
    Scanner in = new Scanner(System.in);
    String s;

    // 0-9 = the organ index ( see datalink), 10-14 = the five elements
    int mode = 0;

    String foodName;
    TemperatureEnum t1;
    TemperatureEnum t2 = TemperatureEnum.SKIP;


    OrganEnum o1 = OrganEnum.KIDNEYS;
    OrganEnum o2 = OrganEnum.KIDNEYS;

    public Console() throws IOException {

        System.out.println("Database loaded. Must exit program via commandline to save to the database.");

        while(true)
        {
            System.out.println("0 - Liver" +
                    "\n1 - Gallbladder" +
                    "\n2 - Heart" +
                    "\n3 - Small intestine" +
                    "\n4 - Spleen" +
                    "\n5 - Stomach" +
                    "\n6 - Lungs" +
                    "\n7 - Large Intestine" +
                    "\n8 - Kidneys" +
                    "\n9- Bladder" +
                    "\n10 - Wood ( liver and gallbladder )" +
                    "\n11 - Fire ( heart and small intestine )" +
                    "\n12 - Earth ( spleen and stomach )" +
                    "\n13 - Metal ( lungs and large intestine )" +
                    "\n14 - Water ( kidneys and bladder )" +
                    "\nEnter mode ( 0-14 ) :");

            s = in.nextLine();
            mode = modeChecker(s);

            if(mode > -1)
            {
                break;
            }
        }



        // chooses a mode (note: for a GUI this would not be necessary)
        switch (mode) {
            case 0:
                o1 = OrganEnum.LIVER;
                break;
            case 1:
                o1 = OrganEnum.GALLBLADDER;
                break;
            case 2:
                o1 = OrganEnum.HEART;
                break;
            case 3:
                o1 = OrganEnum.SMALL_INTESTINE;
                break;
            case 4:
                o1 = OrganEnum.SPLEEN;
                break;
            case 5:
                o1 = OrganEnum.STOMACH;
                break;
            case 6:
                o1 = OrganEnum.LUNGS;
                break;
            case 7:
                o1 = OrganEnum.LARGE_INTESTINE;
                break;
            case 8:
                o1 = OrganEnum.KIDNEYS;
                break;
            case 9:
                o1 = OrganEnum.BLADDER;
                break;
            case 10:
                o1 = OrganEnum.LIVER;
                o2 = OrganEnum.GALLBLADDER;
                break;
            case 11:
                o1 = OrganEnum.HEART;
                o2 = OrganEnum.SMALL_INTESTINE;
                break;
            case 12:
                o1 = OrganEnum.SPLEEN;
                o2 = OrganEnum.STOMACH;
                break;
            case 13:
                o1 = OrganEnum.LUNGS;
                o2 = OrganEnum.LARGE_INTESTINE;
                break;
            case 14:
                o1 = OrganEnum.KIDNEYS;
                o2 = OrganEnum.BLADDER;
                break;
                default:
                    System.out.println("Invalid mode. Exiting program.");
                    System.exit(mode);

        }

        s = "Mode "+mode+" : ";

        if (mode < 10) {
            s = s + o1.name();
        } else {
            switch (mode) {
                case 10:
                    s = s + "Wood";
                    break;
                case 11:
                    s = s + "Fire";
                    break;
                case 12:
                    s = s + "Earth";
                    break;
                case 13:
                    s = s + "Metal";
                    break;
                case 14:
                    s = s + "Water";
                    break;
                default:
                    System.out.println("Invalid mode " + mode);
                    System.exit(mode);
            }

            s = s + " - " + o1 + " and " + o2;
        }

        System.out.println(s);
        System.out.println("Enter:\n- Valid food name \n(1st character must be alphabetical (a-z), any other 1st character entered will exit the program.)");

        if(mode > 9)
        {
            System.out.println("Temperature for "+o1+" then "+o2);
        }
        else
        {
            System.out.println("Temperature for "+o1);
        }

        System.out.println("(1.cold, 2.cool, 3.neutral, 4.warm, 5.hot, 0.blank over-write, other - skip)");
        System.out.println("_______________________________________________________________________________________________________________________________");


        while (true) {

            // read in the food name
            s = in.nextLine();

            if (!s.isEmpty()) {

                foodName = s.toString().toLowerCase();

                // exit code is a number or nonalphabetic character
                if (!(foodName.charAt(0) > 96 && foodName.charAt(0) < 123)) {
                    System.out.println("Non-alphabetic exit code entered");
                    dl.write();
                    System.out.println("Any updates to the database have been saved.");
                    System.exit(0);
                }

                System.out.println(o1 + " =");
                // read in the temperature(s)
                s = in.nextLine().toLowerCase();


                t1 = dl.stringToTempEnum(s);


                System.out.println(t1);


                if (mode > 9) {

                    System.out.println(o2 + " =");
                    s = in.nextLine().toLowerCase();

                    /*
                    while(!isValidTemp(s))
                    {
                        System.out.println("Enter a valid temperature:\n" +
                                "1 - cold, 2- cool, 3 - neutral, 4 - warm, 5 - hot, 0 - blank overwrite, 6-9 - skip");
                        s = in.nextLine().toLowerCase();
                    }
                    t2 = dl.convertChar(s.charAt(0));
                    */

                    t2 = dl.stringToTempEnum(s);


                    System.out.println(t2);
                }

                if (mode <= 9) {
                    dl.put(foodName, o1, t1);
                } else {
                    dl.put(foodName, o1, t1, o2, t2);
                }


                System.out.println("_______________________________________________________________________");


            }

        }
    }


    public static void main(String[] args) throws IOException {
        Console console = new Console();
    }


    public boolean isValidTemp(String str) {
        if (str.isEmpty()) {
            return false;
        } else {
            // it is a number ( 0 to 9 )
            if (str.charAt(0) > 47 && str.charAt(0) < 58) {
                return true;
            }
        }


        return false;
    }

    public int modeChecker(String s)
    {
        switch(s.charAt(0))
        {
            case '0':
                return 0;
            case '1':
                if(s.length() > 1)
                {
                    switch(s.charAt(1))
                    {
                        case '0':
                            return 10;
                        case '1':
                            return 11;
                        case '2':
                            return 12;
                        case '3':
                            return 13;
                        case '4':
                            return 14;
                        default:
                            return -1;
                    }
                }
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            default:
                // errenous input
                return -1;
        }
    }

    /*
    public TemperatureEnum convert(String str)
    {

        // can only take number inputs at present
        if(!str.isEmpty()) {
            switch (str.charAt(0)) {
                case 1:
                    return TemperatureEnum.COLD;
            }
        }

        // un-intentionally blank
        return TemperatureEnum.SKIP;
    }*/
}
