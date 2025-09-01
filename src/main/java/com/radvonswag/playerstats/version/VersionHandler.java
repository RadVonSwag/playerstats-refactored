package com.radvonswag.playerstats.version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class VersionHandler {

    private static final String VERSION_CONFIG_DIR = "./playerstats/config/minecraft_version.dat";
    private static boolean isLegacy;

    public VersionHandler() {}

    /**
     * This method checks to see if the user has specified the version once before, making it so that they do not have to enter the version every time.
     *
     * @return "y" or "n" based on the content of the version file or the users' input if first time running.
     *
     */
    public boolean checkServerVersion(boolean doSaveVersion) throws IOException {
        File minecraftVersionFile = new File(VERSION_CONFIG_DIR);
        if (!minecraftVersionFile.exists() || !doSaveVersion) {
            Scanner input = new Scanner(System.in);
            String prompt = "Is this server on 1.13 (or newer)? (Y/N)";
            System.out.println(prompt);
            isLegacy = handleUserPrompt(input, prompt);
            if (doSaveVersion) {
                saveVersionToFile(minecraftVersionFile);
            }
            input.close();
        } else {
            Scanner reader = new Scanner(minecraftVersionFile);
            isLegacy = Boolean.parseBoolean(reader.nextLine());
            reader.close();
        }
        return isLegacy;
    }
    // TODO: Implement :D
    public boolean checkServerVersionDynamic() {

        return false;
    }

    public static boolean handleUserPrompt(Scanner input, String prompt) {
        String response;
        while (true) {
            response = input.nextLine().trim().toLowerCase();
            if ("y".equals(response)) {
                return false;
            } else if ("n".equals(response)) {
                return true;
            } else {
                System.out.println(prompt);
            }
        }
    }

    public static void saveVersionToFile(File minecraftVersionFile) throws IOException {
        if (minecraftVersionFile.getParentFile().mkdirs()) {
            FileWriter writer = new FileWriter(VERSION_CONFIG_DIR);
            writer.write(String.valueOf(isLegacy));
            writer.close();
        }
    }
}
