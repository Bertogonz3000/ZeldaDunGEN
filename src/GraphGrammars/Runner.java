package GraphGrammars;

import java.io.IOException;
import java.time.LocalDateTime;

public class Runner {

    public static void main(String[] args) {
        try {
            runEverything(true);
        } catch (Exception e) {
            System.out.println("Something's gone wrong on our end, please try again.");
            e.printStackTrace();
        }
    }

    private static void runEverything(boolean openFiles) throws IOException, InterruptedException {
        System.out.println("Running...");
        MissionGraph testMission = new MissionGraph(new MissionGraphNode(alphabet.START));
        System.out.println("Starting Mission Replacements...");
        testMission.runReplacements();
        System.out.println("Mission replacements done");
        System.out.println("\nBuilding Space Graph...");
        SpaceGraph testSpace = new SpaceGraph(testMission);
        System.out.println("\nSpace Graph Complete \n");
        System.out.println("Writing to files:");

        LocalDateTime time = LocalDateTime.now();

        System.out.println("Writing GV files...");
        testMission.writeGVStringToFile(time, openFiles);
        testSpace.writeGVStringToFile(time, openFiles);

        System.out.println("Writing gen files...");
        testSpace.writeGenStringsToFiles(time);

        System.out.println("Writing analysis files...");
        testSpace.writeAnalysisStringsToFiles(time);

        System.out.println("Building Room Internals...");
        RoomRunner roomRunner = new RoomRunner();
        roomRunner.fillRooms(time, openFiles);

        System.out.println("Complete!");
    }
}
