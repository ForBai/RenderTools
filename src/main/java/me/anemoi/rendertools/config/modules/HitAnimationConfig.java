package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Base64;

import static me.anemoi.rendertools.config.modules.AnimationsConfig.*;


public class HitAnimationConfig {
    //@Switch(name = "Toggled")
    //public static boolean toggled = true;

    @Slider(name = "Swing Speed", min = 0.5F, max = 5.0F)
    public static float swingSpeed = 1.0F;

    @Switch(
            name = "Ignore Haste/Mining Fatigue",
            description = "Ignore haste and mining fatigue effects when calculating the swing speed."
    )
    public static boolean ignoreHaste = false;

    @Dropdown(
            name = "Swing Type",
            description = "The type of swing animation to use.",
            options = {
                    "Vanilla",//0
                    "Custom",//1
                    "Slide",//2
                    "Custom 2",//3
                    "Custom 3",//4
                    "Custom 4",//5
                    "Front Hit"//6
            }
    )
    public static int swingType = 0;

    @Button(name = "Copy Config to clipboard", text = "Save Config")
    public static Runnable saveConfig = () -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("swingSpeed", swingSpeed);
        jsonObject.addProperty("ignoreHaste", ignoreHaste);
        jsonObject.addProperty("swingType", swingType);
        jsonObject.addProperty("toggled", toggled);
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("z", z);
        jsonObject.addProperty("pitch", pitch);
        jsonObject.addProperty("yaw", yaw);
        jsonObject.addProperty("roll", roll);
        jsonObject.addProperty("size", size);
        jsonObject.addProperty("scaledSwingSize", scaledSwingSize);
        jsonObject.addProperty("mode", mode);
        jsonObject.addProperty("swingProgress", swingProgress);
        jsonObject.addProperty("special", special);
        jsonObject.addProperty("rainbowEnchant", rainbowEnchant);
        jsonObject.addProperty("scaledSwing", scaledSwing);
        jsonObject.addProperty("disableEquipProgressY", disableEquipProgressY);
        String json = jsonObject.toString();
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(json.getBytes());

        //copy the encoded string to clipboard
        StringSelection stringSelection = new StringSelection(encoded);
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    };

    @Button(name = "Load Config from clipboard", text = "Load Config")
    public static Runnable loadConfig = () -> {
        //get the encoded string from clipboard
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        String encoded = null;
        try {
            encoded = (String) clipboard.getData(java.awt.datatransfer.DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
        Base64.Decoder decoder = Base64.getDecoder();
        String json = new String(decoder.decode(encoded));

        //parse the json string and set the config values
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        swingSpeed = jsonObject.get("swingSpeed").getAsFloat();
        ignoreHaste = jsonObject.get("ignoreHaste").getAsBoolean();
        swingType = jsonObject.get("swingType").getAsInt();
        toggled = jsonObject.get("toggled").getAsBoolean();
        x = jsonObject.get("x").getAsFloat();
        y = jsonObject.get("y").getAsFloat();
        z = jsonObject.get("z").getAsFloat();
        pitch = jsonObject.get("pitch").getAsFloat();
        yaw = jsonObject.get("yaw").getAsFloat();
        roll = jsonObject.get("roll").getAsFloat();
        size = jsonObject.get("size").getAsFloat();
        scaledSwingSize = jsonObject.get("scaledSwingSize").getAsFloat();
        mode = jsonObject.get("mode").getAsInt();
        swingProgress = jsonObject.get("swingProgress").getAsBoolean();
        special = jsonObject.get("special").getAsBoolean();
        rainbowEnchant = jsonObject.get("rainbowEnchant").getAsBoolean();
        scaledSwing = jsonObject.get("scaledSwing").getAsBoolean();
        if (jsonObject.has("disableEquipProgressY"))
            disableEquipProgressY = jsonObject.get("disableEquipProgressY").getAsBoolean();
    };


}
