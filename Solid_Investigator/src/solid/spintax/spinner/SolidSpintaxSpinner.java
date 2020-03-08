package solid.spintax.spinner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.math.BigInteger; 
import solid.spintax.spinner.SolidSpintax.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * <i>Solid Spintax Spinner</i>
 * <p>
 * Command-line utility to generate uniquely identifiable documents using a provided spintax.
 * <p>
 * For command-line arguments, run with --help.
 * <p>
 * For additional documentation, check the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Spinner">GitHub</a>
 * repository.
 * <p>
 * Uses the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Specification">Solid Spintax</a>
 * standard from <i>Solid Security</i>.
 * 
 * @author Solid Security
 * @author Vivek Nair
 * @author Jacob Fuehne
 * @version 2.2.0
 * @since 2.0.0
 */
public class SolidSpintaxSpinner {
    public static final String SPINTAX_VERSION = "1.0.0";
    public static final String SPINNER_VERSION = "2.2.0 r01";
    private static final BigInteger FILE_WARNING_NUM = BigInteger.valueOf(100);
    private static final BigInteger FILE_REJECT_NUM = BigInteger.valueOf(100000);
    private static final StringBuilder logOutput = new StringBuilder();
    private static boolean log = false;

    public static void main(String[] args) {
        printHeader();

        ArgumentParser parser = ArgumentParsers.newFor("spinner")
                .addHelp(false)
                .defaultFormatWidth(80)
                .build()
                .description("Command-line utility to generate uniquely identifiable documents using a provided spintax.");

        parser.addArgument("file")
                .metavar("<FILE>")
                .nargs("?")
                .help("spintax file that the spinner should use to generate documents");

        ArgumentGroup modes = parser.addArgumentGroup("modes")
                .description("specify how the spinner should choose which permutations to generate");
        modes.addArgument("-r", "--random")
                .action(Arguments.storeTrue())
                .help("generate random permutations of the provided spintax");
        modes.addArgument("-s", "--sequential")
                .action(Arguments.storeTrue())
                .help("generates sequential permutations of the provided spintax, starting at the provided tag or permutation number, or 0 if neither is provided");
        modes.addArgument("-a", "--all")
                .action(Arguments.storeTrue())
                .help("generate all possible permutations of the provided spintax");

        ArgumentGroup restrictions = parser.addArgumentGroup("restrictions")
                .description("add restrictions that the spinner must follow");
        restrictions.addArgument("-u", "--unique")
                .action(Arguments.storeTrue())
                .help("require all output documents to be unique");

        ArgumentGroup identifiers = parser.addArgumentGroup("identifiers")
                .description("identifies a particular permutation to be generated");
        identifiers.addArgument("-p", "--permutation")
                .metavar("<#>")
                .type(Integer.class)
                .help("generate the output document corresponding to a specific permutation number");
        identifiers.addArgument("-t", "--tag")
                .metavar("<TAG>")
                .help("generate the output document corresponding to a specific tag");

        ArgumentGroup output = parser.addArgumentGroup("output")
                .description("specify where output files should be stored");
        output.addArgument("-n", "--count")
                .metavar("<#>")
                .type(Integer.class)
                .setDefault(1)
                .help("specifies the number of files to be generated");
        output.addArgument("-y", "--yes")
                .action(Arguments.storeTrue())
                .help("automatically acknowledge generating large number of output files");
        output.addArgument("-o", "--out")
                .metavar("<FILE>")
                .help("specifies the suffix to be appended to generated output files");
        output.addArgument("-l", "--log")
                .metavar("<FILE>")
                .help("creates a separate file with tags corresponding to generated documents");

        ArgumentGroup information = parser.addArgumentGroup("information")
                .description("information about this program or provided spintax");
        information.addArgument("-i", "--info")
                .action(Arguments.storeTrue())
                .help("print information about the provided spintax");
        information.addArgument("-v", "--version")
                .action(Arguments.storeTrue())
                .help("prints the current spintax and spinner version");
        information.addArgument("-h", "--help")
                .action(Arguments.storeTrue())
                .help("shows this help message");

        if (args.length == 0) {
            parser.printHelp();
            System.exit(0);
            return;
        }

        Namespace res;
        try {
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }
        
        boolean valid = false;

        if (res.get("help")) {
            if (args.length > 1) {
                System.out.println("\nNOTE: --help specified; all other arguments ignored\n");
            }
            parser.printHelp();
            System.exit(0);
        }

        if (res.get("version")) {
            if (args.length > 1) {
                System.out.println("\nNOTE: --version specified; all other arguments ignored\n");
            }
            System.exit(0);
        }

        if (res.get("info")) {
            valid = true;
            String[] incompatible = {"random", "sequential", "all", "unique", "permutation", "tag", "out", "log"};
            handleIncompatibles(res, incompatible, "info");
        }

        if (res.get("random")) {
            valid = true;
            String[] incompatible = {"sequential", "all", "permutation", "tag"};
            handleIncompatibles(res, incompatible, "random");
        }

        if (res.get("sequential")) {
            valid = true;
            String[] incompatible = {"random"};
            handleIncompatibles(res, incompatible, "sequential");
        }

        if ((int) res.get("count") <= 0) {
            System.out.println("ERROR: count must be greater than 0");
            System.exit(1);
        }

        if (res.get("permutation") != null) {
            valid = true;
            String[] incompatible = {"tag", "random", "all"};
            handleIncompatibles(res, incompatible, "permutation");
        }

        if (res.get("tag") != null) {
            valid = true;
            String[] incompatible = {"permutation", "random", "all"};
            handleIncompatibles(res, incompatible, "tag");
        }

        if (res.get("all")) {
            valid = true;
            String[] incompatible = {"random", "permutation", "tag"};
            handleIncompatibles(res, incompatible, "all");
        }

        if (res.get("unique") != null) {
            String[] incompatible = {"sequential"};
            handleIncompatibles(res, incompatible, "unique");
        }

        if (res.get("log") != null) {
            log = true;
        }
        
        if(!valid) {
            System.out.println("ERROR: No operation provided. One of random, "
                    + "sequential, all, permutation, tag, info, version, or help "
                    + "is required.");
            System.exit(1);
        }

        System.out.print("\n");

        if (res.get("file") == null) {
            System.out.println("\nERROR: Please specify an input file.");
            System.exit(1);
        }

        String fileInput = res.get("file");
        System.out.println("\nLoading input file (" + fileInput + ")...");
        String input;
        try {
            input = readFileAsString(fileInput);
        } catch (Exception e) {
            System.out.println("\nERROR: Unable to load specified input file.");
            System.exit(1);
            return;
        }
        System.out.println("Loaded " + input.length() + " characters.\n");

        System.out.println("Parsing input file...");
        SolidSpintaxElement spintax = parse(input);
        BigInteger permutations = spintax.countPermutations();
        int switches = spintax.countSwitches();
        System.out.println("Parsed " + switches + " switches constituting " + permutations + " permutations.\n");
        BigInteger min_perm = BigInteger.ZERO;
        BigInteger max_perm = permutations.subtract(BigInteger.ONE);

        if (res.get("info")) {
            System.out.println("Information about provided spintax file:");
            System.out.println("\tNumber of Switches: " + switches);
            System.out.println("\tNumber of Permutations: " + permutations);
            System.out.println("\nValid Permutations: [" + min_perm + ", " + max_perm + "]");
            System.out.println("\nValid Tags: [" + permToTag(min_perm) + ", " + permToTag(max_perm) + "]");
            System.exit(0);
        }

        boolean sequential = res.get("sequential");
        
        int countInt = res.get("count");
        BigInteger count = BigInteger.valueOf(countInt);

        if (res.get("all")) {
            count = permutations;
            sequential = true;
        }

        if ((count.compareTo(FILE_WARNING_NUM) >= 0) && ((boolean) res.get("yes")) == false) {
            Scanner in = new Scanner(System.in);
            boolean validAnswer = false;
            while (!validAnswer) {
                System.out.println("WARNING: this will create " + count + " output files!");
                System.out.print("Do you wish to continue? (y/n): ");
                String answer = in.nextLine().toLowerCase();
                switch (answer) {
                    case "n":
                    case "no":
                        System.exit(0);
                    case "y":
                    case "yes":
                        validAnswer = true;
                        break;
                    default:
                        System.out.print("Invalid input, please answer (y/n): ");
                        break;
                }
            }
            System.out.println("\n(Note: use -y to accept automatically.)");
        }

        if (count.compareTo(FILE_REJECT_NUM) > 0) {
            System.out.println("Error: this would create " + count + " output files!");
            System.out.println("Generating more than " + FILE_REJECT_NUM + " files is not supported.");
            System.exit(1);
        }

        BigInteger psize = BigInteger.valueOf(count.subtract(BigInteger.ONE).toString().length());

        if (res.get("permutation") != null) {
            BigInteger perm = res.get("permutation");
            if ((perm.compareTo(max_perm) > 0) || (perm.compareTo(min_perm) < 0)) {
                System.out.println("ERROR: specified permutation is outside the range of possible permutations");
                System.exit(1);
            }

            if ((boolean) res.get("sequential") == false) {
                if (!count.equals(BigInteger.ONE)) {
                    System.out.println("ERROR: can't set --permutation and --count without --sequential");
                    System.exit(1);
                }
                sequential = true;
            }
        }

        if (res.get("tag") != null) {
            BigInteger perm = tagToPerm(res.get("tag"));
            if((perm.compareTo(max_perm) > 0)|| (perm.compareTo(min_perm) < 0)) {
                System.out.println("ERROR: specified tag is outside the range of possible tags");
                System.exit(1);
            }

            if ((boolean) res.get("sequential") == false) {
                if (!count.equals(BigInteger.ONE)) {
                    System.out.println("ERROR: can't set --tag and --count without --sequential");
                    System.exit(1);
                }
                sequential = true;
            }
        }

        String suffix;
        if (res.get("out") != null) {
            suffix = res.get("out");
        } else {
            suffix = fileInput + ".out";
        }

        if (res.get("unique")) {
            if (count.compareTo(permutations) > 0){
                System.out.println("ERROR: can't generate " + count + " unique permutations; only " + permutations + " possible permutations exist");
                System.exit(1);
            }
        }

        if (res.get("random")) {
            Random rand = new Random();
            if (res.get("unique")) {
                System.out.println("Generating " + count + " random unique permutations...");

                Set<BigInteger> set = new LinkedHashSet<>();
                while(BigInteger.valueOf(set.size()).compareTo(count) < 0){
                    BigInteger randomNumber;
                    do {
                        randomNumber = new BigInteger(permutations.bitLength(), rand);
                    } while (randomNumber.compareTo(permutations) >= 0);
                        set.add(randomNumber);
                }

                int i = 0;
                System.out.println("\nOUTPUT_FILE:TAG --");
                for (BigInteger permutation : set) {
                    String document = spintax.spin(permutation);
                    String tag = permToTag(permutation);
                    String fileName = String.format("%0" + psize + "d", i) + suffix;

                    try {
                        FileWriter fw = new FileWriter(new File(fileName));
                        fw.write(document);
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("\nERROR: Unable to write output file.");
                        System.exit(1);
                    }

                    output(fileName + ":" + tag);
                    i++;
                }
            } else {
                System.out.println("Generating " + count + " random permutations...");
                System.out.println("\nOUTPUT_FILE:TAG --");
                for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
                    BigInteger randomNumber;
                    do {
                        randomNumber = new BigInteger(permutations.bitLength(), rand);
                    } while (randomNumber.compareTo(permutations) >= 0);
                    BigInteger permutation = randomNumber;
                    String document = spintax.spin(permutation);

                    String tag = permToTag(permutation);
                    String fileName = String.format("%0" + psize + "d", i) + suffix;

                    try {
                        FileWriter fw = new FileWriter(new File(fileName));
                        fw.write(document);
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("\nERROR: Unable to write output file.");
                        System.exit(1);
                    }

                    output(fileName + ":" + tag);
                }
            }
        } else if (sequential) {
            BigInteger out = BigInteger.ZERO;

            BigInteger perm = BigInteger.ZERO;
            if (res.get("permutation") != null) {
                perm = res.get("permutation");
            }
            if (res.get("tag") != null) {
                perm = tagToPerm(res.get("tag"));
            }

            System.out.println("\nOUTPUT_FILE:TAG --");
            while (out.compareTo(count) < 0) {
                String document = spintax.spin(perm);

                String tag = permToTag(perm);
                String fileName = String.format("%0" + psize + "d", out) + suffix;

                try {
                    FileWriter fw = new FileWriter(new File(fileName));
                    fw.write(document);
                    fw.close();
                } catch (IOException e) {
                    System.out.println("\nERROR: Unable to write output file.");
                    System.exit(1);
                }

                output(fileName + ":" + tag);

                perm = perm.add(BigInteger.ONE);
                if (perm.compareTo(max_perm) > 0) {
                    perm = BigInteger.ZERO;
                }

                out = out.add(BigInteger.ONE);
            }
        }

        if (res.get("log") != null) {
            FileWriter logf;
            try {
                logf = new FileWriter(new File((String) res.get("log")));
                logf.write(logOutput.toString());
                logf.close();
                System.out.println("\nSUCCESS: Wrote output to " + res.get("log") + ".");
            } catch (IOException e) {
                System.out.println("\nERROR: Failed to create log file.");
                System.exit(1);
            }
        }
    }
    
    private static SolidSpintaxElement innerParse(String input) {
        if (input.matches("[0-9]+-[0-9]+")) {
            SolidSpintaxBlock text = new SolidSpintaxBlock();
            int pos = input.indexOf("-");
            BigInteger min = new BigInteger(input.substring(0, pos));
            BigInteger max = new BigInteger(input.substring(pos + 1, input.length()));
            SolidSpintaxIntegerSwitch temp = new SolidSpintaxIntegerSwitch(min, max);
            text.addSwitch(temp);
            return text;
        } else {
            return parse(input);
        }
    }

    public static SolidSpintaxElement parse(String input) {
        SolidSpintaxBlock text = new SolidSpintaxBlock();
        SolidSpintaxSwitch currSwitch;
        currSwitch = new SolidSpintaxSwitch();
        String substring = "";
        int openBracesCount = 0;
        boolean firstOption = false;
        for (int i = 0; i < input.length(); i++) {
            char curr = input.charAt(i);
            if (curr == '@') {
                currSwitch = new SolidSpintaxGlobalSwitch();
                continue;
            }
            switch (curr) {
                case '{':
                    openBracesCount += 1;
                    if (openBracesCount == 1) {
                        firstOption = true;
                        SolidSpintaxText temp = new SolidSpintaxText(substring);
                        text.addSwitch(temp);
                        substring = "";
                        continue;
                    }
                    break;
                case '|':
                    if (openBracesCount == 1) {
                        firstOption = false;
                        SolidSpintaxElement option = parse(substring);
                        currSwitch.addChild(option);
                        substring = "";
                        continue;
                    }
                    break;
                case '}':
                    openBracesCount -= 1;
                    if (openBracesCount == 0) {
                        SolidSpintaxElement option;
                        if(firstOption) {
                            option = innerParse(substring);
                        } else {
                            option = parse(substring);
                        }
                        currSwitch.addChild(option);
                        text.addSwitch(currSwitch);

                        if (currSwitch instanceof SolidSpintaxGlobalSwitch) {
                            SolidSpintaxGlobalSwitch globalSwitch = (SolidSpintaxGlobalSwitch) currSwitch;
                            String contents = currSwitch.toString();
                            if (SolidSpintaxGlobalSwitch.switches.containsKey(contents)) {
                                globalSwitch.setMaster((SolidSpintaxGlobalSwitch) SolidSpintaxGlobalSwitch.switches.get(contents));
                            } else {
                                SolidSpintaxGlobalSwitch.switches.put(contents, globalSwitch);
                            }
                        }

                        currSwitch = new SolidSpintaxSwitch();
                        substring = "";
                        continue;
                    }
                    break;
                default:
                    break;
            }
            substring += input.charAt(i);
        }

        SolidSpintaxText temp = new SolidSpintaxText(substring);
        text.addSwitch(temp);
        return text;
    }

    private static String readFileAsString(String fileName) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    private static void output(String in) {
        System.out.println(in);
        if (log) {
            logOutput.append(in).append("\n");
        }
    }

    private static void printHeader() {
        System.out.print("\n"
                + "       /MMM/ yMMm .MMMs                                                         \n"
                + "       /MMMmdNMMMddMMMs                                                         \n"
                + "+ ++++++ssssdMMMMMMMMMo    -\\\\\\\\\\\\\\\\\\   `:///////-  `/-         -/` -/////////. \n"
                + " `/`++++smNNNNNNMMMMy-    oMy          /NhoooooooNh .My         sM: sMsoooooosMs\n"
                + "           syyyyMMM.      +My++++++oo. hM.       yM .My         sM: sM-       NM\n"
                + "    `/.++++sssssMMM`               sMy hM.       yM .My         sM: sM-       NM\n"
                + "     `/.+++mNNNNMMM`      +My++++++yMs /NhoooooosNh .Md+++++++: sM: sMsooooooyMo\n"
                + "         :syyyyhMMMh/      -////////-   `:///////.  `/////////- -/` -////////:` \n"
                + "  `/`+++ssssmMMMMMMMMN`                                                         \n"
                + " :./++++NMMMMMMMMMMMMM.                                                         \n"
                + "================================================================================\n\n"
                + "Solid Spinner v" + SPINNER_VERSION + " (Solid Spintax Standard v" + SPINTAX_VERSION + ")\n");
    }

    private static void handleIncompatibles(Namespace res, String[] incompatible, String with) {
        boolean error = false;
        for (String i : incompatible) {
            if (res.get(i) == null) {
                continue;
            }
            if (res.get(i) instanceof Boolean && (boolean) res.get(i) == false) {
                continue;
            }
            error = true;
            System.out.println("\nERROR: --" + i + " cannot be used with --" + with + ".\n");
        }
        if (error) {
            System.exit(1);
        }
    }

    private static String permToTag(BigInteger permutation) {
        return permutation.toString(36).toUpperCase();
    }

    private static BigInteger tagToPerm(String tag) {
        return new BigInteger(tag, 36);
    }
}
