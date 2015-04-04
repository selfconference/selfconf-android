package org.selfconference.android.utils;

public final class RoomStringer {

    public static String toRoomString(String room) {
        if ("Ballroom".equalsIgnoreCase(room)) {
            return room;
        }
        return "Room " + room;
    }

    private RoomStringer() {
    }
}
