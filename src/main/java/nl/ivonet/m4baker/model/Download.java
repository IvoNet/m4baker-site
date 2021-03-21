package nl.ivonet.m4baker.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Download implements Comparable<Download> {
    private static final Pattern versionPattern = Pattern.compile(".*_([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.dmg");

    private final String dmg;
    private final String sha512;

    public Download(final String dmg,
                    final String sha512) {
        this.dmg = dmg;
        this.sha512 = sha512;
    }

    public String getDmg() {
        return dmg;
    }

    public String getSha512() {
        return sha512;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Download download)) return false;
        return Objects.equals(dmg, download.dmg) && Objects.equals(sha512, download.sha512);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dmg, sha512);
    }

    private String normailizedVersion(final String name) {
        final Matcher matcher = versionPattern.matcher(name);
        if (!matcher.matches()) {
            System.out.println("name = " + name);
            throw new IllegalArgumentException("Invalid version format");
        }
        return String.format("%s.%s.%s", matcher.group(1), matcher.group(2), matcher.group(3));
    }

    @Override
    public int compareTo(final Download that) {
        if (that == null)
            return 1;
        final String[] thisParts = this.normailizedVersion(dmg)
                                       .split("\\.");
        final String[] thatParts = this.normailizedVersion(that.getDmg())
                                       .split("\\.");
        final int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            final int thisPart = i < thisParts.length ?
                  Integer.parseInt(thisParts[i]) : 0;
            final int thatPart = i < thatParts.length ?
                  Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }

}
