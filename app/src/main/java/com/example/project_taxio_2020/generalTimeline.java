package com.example.project_taxio_2020;

import com.akshaykale.swipetimeline.TimelineObject;

public class generalTimeline implements TimelineObject {

    String distance, url;
    Long timestamp;

    public generalTimeline(long parseLong, String s, String image) {
        distance = s;
        timestamp = parseLong;
        url = image;
    }

    @Override
    public long getTimestamp() { return timestamp; }

    @Override
    public String getTitle() {
        return distance;
    }

    @Override
    public String getImageUrl() {
        return null;
    }
}
