package com.connectsdk.discovery.provider.ssdp;

public class Icon {
    static final String TAG = "icon";
    static final String TAG_MIME_TYPE = "mimetype";
    static final String TAG_WIDTH = "width";
    static final String TAG_HEIGHT = "height";
    static final String TAG_DEPTH = "depth";
    static final String TAG_URL = "url";

    /* Required. Icon's MIME type. */
    String mimetype;
    /* Required. Horizontal dimension of icon in pixels. */
    String width;
    /* Required. Vertical dimension of icon in pixels. */
    String height;
    /* Required. Number of color bits per pixel. */
    String depth;
    /* Required. Pointer to icon image. */
    String url;
}
