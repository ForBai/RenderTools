package me.anemoi.rendertools.utils;

import cc.polyfrost.oneconfig.utils.Notifications;

public class NotifUtil {

    //send warning
    public static void sendWarning(String title, String text, float duration) {
//        Notifications.INSTANCE.send(title, text, new Icon(SVGz.WARNING.filePath));
        Notifications.INSTANCE.send(title, text);
    }

    //send error
    public static void sendError(String title, String text, float duration) {
//        Notifications.INSTANCE.send(title, text, new Icon(SVGz.X_CIRCLE_BOLD.filePath));
        Notifications.INSTANCE.send(title, text);
    }

    //send info
    public static void sendInfo(String title, String text, float duration) {
//        Notifications.INSTANCE.send(title, text, new Icon(SVGz.OLD_BELL.filePath));
        Notifications.INSTANCE.send(title, text);
    }

    //send success
    public static void sendSuccess(String title, String text, float duration) {
//        Notifications.INSTANCE.send(title, text, new Icon(SVGz.CHECK_CIRCLE.filePath));
        Notifications.INSTANCE.send(title, text);
    }

}
