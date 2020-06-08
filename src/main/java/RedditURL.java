import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class RedditURL {

    public static void getCatPost(Model model) throws IOException {
        URL url = new URL("https://www.reddit.com/r/cat/.json?limit=1");

        Scanner in = new Scanner((InputStream)url.getContent());
        String result = "";
        while(in.hasNext()){
            result += in.nextLine();
        }

        JSONObject jsonObject = new JSONObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray children = data.getJSONArray("children");

        for(int i=0; i<1; i++){
            JSONObject child = children.getJSONObject(i);
            JSONObject dataChildren = child.getJSONObject("data");
            model.setURL(dataChildren.getString("url"));
            model.setTitle(dataChildren.getString("title"));
        }
    }
}
