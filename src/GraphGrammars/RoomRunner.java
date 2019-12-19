package GraphGrammars;

import java.io.*;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RoomRunner {

    private final String DUNGEONS_DIR = System.getProperty("user.dir") + "/dungeons/";

    /**
     * Read in the .lp files for each room in the dungeon with the folder name specified by the
     * time given
     *
     * @param time - the date and time of the creation of this dungeon.
     */
    public void fillRooms(LocalDateTime time, boolean createGVFiles) {
        //Name the folder with the current date and time
        String folderName = time.toString().replaceAll(":", ".");
        //Get all the names of the files in this folder
        String[] lpFiles = readFiles(DUNGEONS_DIR + folderName);
        makeOutputFolder(folderName);
        //Run all commands to create these rooms
        runCommands(lpFiles, DUNGEONS_DIR + folderName + "/roomsOutput/",
                DUNGEONS_DIR + folderName + "/generation/", createGVFiles);
    }

    /**
     * Create
     *
     * @param folderName
     */
    private void makeOutputFolder(String folderName) {
        String outputFolderPath = DUNGEONS_DIR + folderName;
        File outputFolder = new File(outputFolderPath, "roomsOutput");
        outputFolder.mkdir();
    }

    /**
     * Read all the files in the folder associated with this dungeon and return all the names
     *
     * @param folderName - name of the folder to check
     * @return
     */
    private String[] readFiles(String folderName) {
        //Get the file at path
        String path = folderName + "/generation";
        File folder = new File(path);
        //return string array with all file names in this folder.
        return folder.list();
    }

    /**
     * Run commands to generate rooms
     *
     * @param lpFiles
     */
    private void runCommands(String[] lpFiles, String outputPath, String lpCodePath,
                             boolean createGVFiles) {
        //Get all path names
        String clingoPath = System.getProperty("user.dir") + "/bin/clingo";
        String aspCodePath = System.getProperty("user.dir") + "/aspCode/";
        //Get runtime for processes in command line
        Runtime rt = Runtime.getRuntime();

        //For each fileName, create an output filename and run the commands
        for (String lpFile : lpFiles) {
            String[] fileNameSplit = lpFile.split("\\.");
            String outputFileName = fileNameSplit[0] + ".out";
            //Throw together the runtime command to construct this room based on the LP file
            String command =
                    clingoPath + " -n 1 --verbose=0 --sign-def=rnd --seed="
                            + (ThreadLocalRandom.current().nextLong(1, 4000000001L))
                            + " " + aspCodePath + "domain.lp" + " " + lpCodePath + lpFile + " " + aspCodePath
                            + "gen.lp";
            System.out.println(command);
            //Try to run this process - if fail, print some good good info
            try {
                Process roomFillProcess = rt.exec(command);
                if (!roomFillProcess.isAlive()) {
                    System.out.println("Exit:"+roomFillProcess.exitValue());
                }
                writeRoomContentsToFile(roomFillProcess, outputPath, outputFileName, createGVFiles);
                roomFillProcess.waitFor();
            } catch (Exception e) {
                System.out.println("Issue constructing room at:" + outputFileName);
                e.printStackTrace();
            }
        }
    }

    /**
     * Takes a process and writes its output to a file
     *
     * @param command    - the command to get output from
     * @param outputPath - the path to write the file to
     */
    private void writeRoomContentsToFile(Process command, String outputPath,
                                         String outputFileName, boolean createGVFiles) throws IOException {
        //Get standard output from this command - done to deal with piping issue
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(command.getInputStream()));
        // Read the output from the command
        String s = stdInput.readLine();

        //Create output file
        File outputFile = new File(outputPath, outputFileName);
        outputFile.createNewFile();

        if (createGVFiles) {
            runGVCommands(s, outputPath, outputFileName);
        }

        writeToFile(s, outputFile);
    }

    /**
     * Write the contents of the string to the given file
     *
     * @param fileString
     * @param file
     */
    private void writeToFile(String fileString, File file) {
        //Try writing to output file, or raise exception
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Graph Viz files based on the input output, which should be the output from running
     * an lp file with clingo
     *
     * @param output - the output of running with clingo
     */
    private void runGVCommands(String output, String filePath, String outputFileName) {
        String gvFileString = getFullGVString(output);
        File gvFile = createGVFiles(filePath, outputFileName);
        writeToFile(gvFileString, gvFile);
        Runtime rt = Runtime.getRuntime();
        try {
            Process visualize = rt.exec("fdp -Tpng " + filePath + "visuals/" + outputFileName +
                    ".gv -o " + filePath + "visuals/" + outputFileName + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create files for Graph Viz stuffff
     *
     * @param filePath
     * @param outputFileName
     */
    private File createGVFiles(String filePath, String outputFileName) {
        try {
            File gvFolder = new File(filePath, "visuals");
            gvFolder.mkdir();
            File gvFile = new File(filePath + "visuals/", outputFileName + ".gv");
            gvFile.createNewFile();
            return gvFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("This file already exists or some other error with " +
                "creation");
    }

    /**
     * Make full Gv string to a full room file
     *
     * @param output - output of command
     * @return
     */
    private String getFullGVString(String output) {
        String[] nodes = output.split("\\s");
        StringBuilder builder = new StringBuilder("graph room {\n node [shape=\"box\"]; ");
        int count = 0;
        for (String node : nodes) {
            builder.append(nodeToGVString(node, count));
            count++;
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Parse node string into Graph Viz compliant node representation
     *
     * @param atom - the node to translate
     * @return
     */
    private String nodeToGVString(String atom, int id) {
        //Builder for gvString
        StringBuilder builder = new StringBuilder();
        //Split atom into name and args
        String node = atom.substring(0, atom.length() - 1);
        String[] split = node.split("_at\\(");
        String[] coordSplit = split[1].split(",");
        //Get node name
        String nodeName = coordSplit.length == 3 ? coordSplit[2] : split[0];
        //Get node coords
        String x = coordSplit[0];
        String y = coordSplit[1];
        builder.append(nodeName).append(id).append(" ").append("[pos=\"").append(x).append(",").append(y).append("!\"]; ");
        return builder.toString();
    }
}
