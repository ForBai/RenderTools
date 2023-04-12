package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

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
                    "Custome 2",//3
                    "Custom 3",//4
                    "Custom 4",//5
                    "Front Hit"//6
            }
    )
    public static int swingType = 0;

    @Button(name = "Copy Config to clipboard", text = "Save Config")
    public static Runnable saveConfig = () -> {
        /*generate a json string of the config that looks like following
        {
            "toggled": true,
            "swingSpeed": 1.0,
            "ignoreHaste": false,
            "swingType": 0
            }
         */
        String json = "{\n" +
                "    \"swingSpeed\": " + swingSpeed + ",\n" +
                "    \"ignoreHaste\": " + ignoreHaste + ",\n" +
                "    \"swingType\": " + swingType + ",\n" +
                "    \"toggled\": " + toggled + ",\n" +
                "    \"x\": " + x + ",\n" +
                "    \"y\": " + y + ",\n" +
                "    \"z\": " + z + ",\n" +
                "    \"pitch\": " + pitch + ",\n" +
                "    \"yaw\": " + yaw + ",\n" +
                "    \"roll\": " + roll + ",\n" +
                "    \"size\": " + size + ",\n" +
                "    \"scaledSwingSize\": " + scaledSwingSize + ",\n" +
                "    \"mode\": " + mode + ",\n" +
                "    \"swingProgress\": " + swingProgress + ",\n" +
                "    \"special\": " + special + ",\n" +
                "    \"rainbowEnchant\": " + rainbowEnchant + ",\n" +
                "    \"scaledSwing\": " + scaledSwing + ",\n" +
                "}";
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(json.getBytes());
        System.out.println(encoded);

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
        System.out.println(json);

        //parse the json string and set the config values
        String[] lines = json.split("\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length != 2) {
                continue;
            }
            String key = parts[0].trim();
            String value = parts[1].replaceAll(",", "").trim();
            switch (key) {
                case "\"swingSpeed\"":
                    swingSpeed = Float.parseFloat(value);
                    break;
                case "\"ignoreHaste\"":
                    ignoreHaste = Boolean.parseBoolean(value);
                    break;
                case "\"swingType\"":
                    swingType = Integer.parseInt(value);
                    break;
                case "\"toggled\"":
                    toggled = Boolean.parseBoolean(value);
                    break;
                case "\"x\"":
                    x = Float.parseFloat(value);
                    break;
                case "\"y\"":
                    y = Float.parseFloat(value);
                    break;
                case "\"z\"":
                    z = Float.parseFloat(value);
                    break;
                case "\"pitch\"":
                    pitch = Float.parseFloat(value);
                    break;
                case "\"yaw\"":
                    yaw = Float.parseFloat(value);
                    break;
                case "\"roll\"":
                    roll = Float.parseFloat(value);
                    break;
                case "\"size\"":
                    size = Float.parseFloat(value);
                    break;
                case "\"scaledSwingSize\"":
                    scaledSwingSize = Float.parseFloat(value);
                    break;
                case "\"mode\"":
                    mode = Integer.parseInt(value);
                    break;
                case "\"swingProgress\"":
                    swingProgress = Boolean.parseBoolean(value);
                    break;
                case "\"special\"":
                    special = Boolean.parseBoolean(value);
                    break;
                case "\"rainbowEnchant\"":
                    rainbowEnchant = Boolean.parseBoolean(value);
                    break;
                case "\"scaledSwing\"":
                    scaledSwing = Boolean.parseBoolean(value);
                    break;
            }
        }
    };


}
