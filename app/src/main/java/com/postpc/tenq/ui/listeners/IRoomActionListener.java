package com.postpc.tenq.ui.listeners;

import com.postpc.tenq.models.Room;

public interface IRoomActionListener {

    void onRoomExport(Room room);

    void onRoomDelete(Room room);

    void onRoomClose(Room room);
}
