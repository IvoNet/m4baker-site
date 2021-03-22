package nl.ivonet.m4baker.io;

import nl.ivonet.m4baker.model.Download;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Downloads {

    public List<Download> matchingDownloads(final String dir) {

        final Set<String> hashes = listFiles(dir, ".sha512");
        final Set<String> dmgs = listFiles(dir, ".dmg");
        final List<Download> downloads = new ArrayList<>();
        for (final String dmg : dmgs) {
            final String sha512 = dmg + ".sha512";
            if (hashes.contains(sha512)) {
                downloads.add(new Download(dmg, sha512));
            }
        }
        Collections.sort(downloads);
        Collections.reverse(downloads);
        return downloads;
    }

    private Set<String> listFiles(final String dir,
                                  final String extension) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                     .filter(file -> !file.isDirectory())
                     .map(File::getName)
                     .filter(file -> file.endsWith(extension))
                     .collect(Collectors.toSet());
    }


}
