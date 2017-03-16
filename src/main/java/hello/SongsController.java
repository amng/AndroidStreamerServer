package hello;

import controller.MultipartFileSender;
import javafx.util.Pair;
import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
public class SongsController {

    @RequestMapping("/disks")
    public File[] getDisks() {
        return File.listRoots();
    }

    @RequestMapping("/dir")
    public ArrayList<HashMap<String, Object>> listdir(@RequestBody(required = false) String dir) {
        dir = dir == null ? "" : dir;
        dir = dir.replaceAll("\"", "");
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        File f = dir.equals("") ? new File(new File(""), dir) : new File(dir);
        System.out.println("Trying to get files from path " + f.getAbsolutePath());
        Tika tika = new Tika();
        if (f.exists() && f.isDirectory()) {
            for(String file : f.list()) {
                HashMap<String, Object> listData = new HashMap<>();
                File current_file = new File(f, file);
                listData.put("name", file);
                listData.put("type", current_file.isDirectory());
                try {
                    listData.put("mime", tika.detect(current_file));
                } catch (IOException e) {
                    listData.put("mime", "none");
                }
                result.add(listData);
            }
        }

        return result;
    }

    @RequestMapping(value = "/file/{base64}", method = RequestMethod.GET)
    @ResponseBody
    public void getEpisodeFile(@PathVariable String base64, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        String file = new String(Base64.getDecoder().decode(base64));
        File f = new File(file);
        MultipartFileSender.fromPath(f.toPath())
                .with(request)
                .with(response)
                .serveResource();

    }

    private Pair<Integer, Integer> parseRange(String s){
        s = s.replaceAll("[^0-9]+", " ");
        List<String> arr = Arrays.asList(s.trim().split(" "));
        return new Pair<Integer, Integer>(Integer.parseInt(arr.get(0)), Integer.parseInt(arr.get(1)));
    }
}