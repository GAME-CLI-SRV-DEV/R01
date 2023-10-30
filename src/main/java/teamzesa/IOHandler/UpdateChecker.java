package teamzesa.IOHandler;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UpdateChecker {
    private static class UpdateCheckerHolder {
        private static final UpdateChecker INSTANCE = new UpdateChecker();
    }

    private static double gitVersion;
    private static double localVersion;
    private File folder;
    private List<File> fileList;

    private UpdateChecker() {}

    public static UpdateChecker getUpdateChecker() {
        return UpdateCheckerHolder.INSTANCE;
    }

    public void fileLoader() {
        this.folder = DataFile.ABSOLUTE_PATH.getFileInstance();
        Optional<File[]> folderListFile = Optional.ofNullable(this.folder.listFiles());
        folderListFile.ifPresent(file -> this.fileList = List.of(file));
    }

    public void fileManager() {
        gitUpdateCheck();
        localPluginCheck();

        Bukkit.getLogger().info("[R01] Github Plugin Version > " + gitVersion);
        Bukkit.getLogger().info("[R01] Local Plugin Version > " + localVersion);

        if (gitVersion == localVersion) {
            Bukkit.getLogger().info("[R01] 최신버전 입니다.");
            return;
        }
        if (gitVersion > localVersion) {
            Bukkit.getLogger().info("[R01] 구버전 입니다. 자동 업데이트 합니다.");
            removeLegacyPlugin();
            installNewPlugin();
        }
    }

    private void installNewPlugin() {
        String gitVersionStr = Double.toString(gitVersion);
        String fileName = "R01-" + gitVersionStr + ".jar";
        String downloadLink = "https://github.com/JAXPLE/R01/releases/download/" + gitVersionStr + "/R01-" + gitVersionStr + ".jar";

        try {
            URL url = new URL(downloadLink);
            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(folder, fileName))) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLegacyPlugin() {
        fileList.stream()
                .filter(file -> file.getName().contains("R01-") && file.getName().contains(".jar"))
                .forEach(File::delete);
    }

    private void localPluginCheck() {
        fileList.stream()
                .filter(file -> file.getName().contains("R01-") && file.getName().contains(".jar"))
                .forEach(file -> localVersion = Double.parseDouble(
                        file.getName()
                                .split("R01-")[1]
                                .split(".jar")[0]
                ));
    }

    private void gitUpdateCheck() {
        try {
            String line;
            URL url = new URL("https://github.com/JAXPLE/R01/releases/latest");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((line = br.readLine()) != null) {
                if (line.contains("<title>Release")) {
                    gitVersion = Double.parseDouble(
                            line.split("<title>Release Astatine ")[1]
                                .split(" ·")[0]
                    );
                    br.close();
                    return;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
