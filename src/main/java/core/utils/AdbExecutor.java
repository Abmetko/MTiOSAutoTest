package core.utils;

import org.openqa.selenium.os.CommandLine;


public class AdbExecutor {

    public static void closeEmulator(){
        CommandLine commandLine;
        try {
            commandLine = new CommandLine("cmd.exe", "/C", "adb" + " -s emulator-5554 emu kill");
            commandLine.executeAsync();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}