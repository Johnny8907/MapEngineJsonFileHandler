import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class Main {
    private static final String FILE_PATH = "D:\\HMI_common\\hmi-common\\NavHome\\Apps\\Denali\\HMI\\src\\main\\assets\\mapengine_config\\config\\config_map_view.json";
    private static final String FILE_PATH_COPY = "D:\\HMI_common\\hmi-common\\NavHome\\Apps\\Denali\\HMI\\src\\main\\assets\\mapengine_config\\config\\config_map_view_copy.json";

    private static Gson gson;
    public static void main(String[] args) {
        System.out.println("main begin!");
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            //1. Read file to JSON object.
            File file = new File(FILE_PATH);
            File fileCopy = new File(FILE_PATH_COPY);
            String fileContent = FileUtils.readFileToString(file, "utf8");
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();


            //2. Modify JSON object.
            JsonObject viewObject;
            if (jsonObject.has("view")) {
                viewObject = jsonObject.getAsJsonObject("view");
                addKeyValue(viewObject);
                jsonObject.add("view", viewObject);
            }

            //3. format the output JsonObject

            FileUtils.writeStringToFile(fileCopy, format(gson.toJson(jsonObject)), "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addKeyValue(JsonObject jsonObject) {
        JsonObject newObject = new JsonObject();
        newObject.addProperty("default", -1.0);
        newObject.addProperty("billboard", 0.5);
        newObject.addProperty("point", 0.5);
        newObject.addProperty("flat", 0.5);
        newObject.addProperty("incident", -1.0);

        jsonObject.add("screen_overlay_scale", newObject.getAsJsonObject());
    }

    private static String format(String inputStr) {
        StringBuilder sb = new StringBuilder(inputStr);
        int index = 0;
        boolean inBrackets = false;
        while(index < sb.length()) {
            if (!inBrackets && sb.charAt(index) != '[' && sb.charAt(index) != ']') {
                index ++;
            } else if (sb.charAt(index) == '[') {
                inBrackets = true;
                index ++;
            } else if (sb.charAt(index) == ']') {
                inBrackets = false;
                index ++;
            } else if (inBrackets) {
                if (sb.charAt(index) == '\n' || sb.charAt(index) == '\r') {
                    sb.deleteCharAt(index);
                } else if (sb.charAt(index) == ' ' && sb.charAt(index - 1) != ','){
                    sb.deleteCharAt(index);
                } else {
                    index++;
                }
            }
        }
        return sb.toString();
    }
}
