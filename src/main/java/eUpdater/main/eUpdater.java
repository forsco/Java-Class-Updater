package eUpdater.main;

import eUpdater.analysers.mainAnalyser;
import eUpdater.deob.Deobsfucate;
import eUpdater.misc.JarHandler;
import eUpdater.misc.timer;

import java.io.File;


public class eUpdater {
    public static final int Revision = 138;
    public static final boolean simbaPrint = true;
    public static final boolean logPrint = true;

    static boolean forceDownload = false;
    static boolean forceDeob = false;

    public static final boolean findMultis = true;
    public static final boolean doRefactor = false;


    private static void downloadPack() {
        System.out.println("GamePack downloading..");
        JarHandler.downloadJar("http://oldschool38.runescape.com/gamepack_2758227.jar", null, "res/Gamepacks/" + Revision + "/");
    }

    private static void deobPack() {
        JarHandler.Parse("res/Gamepacks/" + Revision + "/GamePack.jar");
        new Deobsfucate().Run();
        JarHandler.save("res/Gamepacks/" + Revision + "/Deob.jar");
    }

    private static void start() {
        timer t = new timer(true);
        File gamepacks = new File("res/Gamepacks/");
        if (!gamepacks.exists())
            gamepacks.mkdirs();
        File file = new File("res/Gamepacks/" + Revision);

        if (!file.exists()) {
            file.mkdir();
            downloadPack();
            deobPack();
        } else if (!forceDownload)
            System.out.println("Gamepack already downloaded..\n");

        if (forceDownload)
            downloadPack();

        if (forceDeob || forceDownload)
            deobPack();

        JarHandler.Parse("res/Gamepacks/" + Revision + "/Deob.jar");
        System.out.println(JarHandler.CLASSES.values().size() + " Classes Found \n");

        mainAnalyser analyser = new mainAnalyser();
        analyser.Run();
        System.out.println("Total Runtime was " + t.getElapsedTime() + "ms");

    }

    public static void main(String[] args) {
        eUpdater.start();
    }
}
