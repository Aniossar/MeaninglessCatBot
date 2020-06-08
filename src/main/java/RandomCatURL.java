import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class RandomCatURL {

    public static void getRandomCatPost(Model model) throws IOException {
        URL url = new URL("https://api.thecatapi.com/v1/images/search");

        Scanner in = new Scanner((InputStream)url.getContent());
        String result = "";
        while(in.hasNext()){
            result += in.nextLine();
        }

        JSONArray jsonArray = new JSONArray(result);
        for(int i=0; i<1; i++){
            JSONObject child = jsonArray.getJSONObject(i);
            model.setURL(child.getString("url"));
        }

    }
}
