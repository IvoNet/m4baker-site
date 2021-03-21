package nl.ivonet.m4baker.controller;

import nl.ivonet.m4baker.io.DirWalker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

@Controller
public class IndexController {

    private final DirWalker dirWalker;
    private final String baseDir;

    public IndexController(final DirWalker dirWalker,
                           @Value("${download.directory}") final String baseDir) {
        this.dirWalker = dirWalker;
        this.baseDir = endSlash(baseDir);
    }

    private String endSlash(final String directory) {
        return directory.endsWith("/") ? directory : directory + "/";
    }


    @GetMapping("/index.html")
    public String greeting(final Model model) {
        model.addAttribute("year", LocalDateTime.now().getYear());
        model.addAttribute("files", dirWalker.matchingDownloads(this.baseDir));
        return "home";
    }

    @GetMapping(value = "/download/{filename:.*\\.dmg}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable("filename") final String filename) throws IOException {
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                             .body(readAllBytes(get(baseDir + filename)));
    }

    @GetMapping(value = "/hash/{filename:.*\\.sha512}", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String hash(@PathVariable("filename") final String filename) throws IOException {
        return new String(readAllBytes(get(baseDir + filename)));
    }

}
