/*
 * MediaInfo
 * Connect SDK
 *
 * Copyright (c) 2014 LG Electronics.
 * Created by Simon Gladkoskok on 14 August 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Normalized reference object for information about a media to display. This object can be used
 * to pass as a parameter to displayImage or playMedia.
 */
public class MediaInfo {

    // @cond INTERNAL

    private String url;
    private SubtitleInfo subtitleInfo;
    private String mimeType;
    private String description;
    private String title;

    /**
     * list of imageInfo objects where [0] is icon, [1] is poster
     */
    private List<ImageInfo> allImages;

    private long duration;

    // @endcond

    public static class Builder {
        // @cond INTERNAL

        // required parameters
        private String url;
        private String mimeType;

        // optional parameters
        private String title;
        private String description;
        private List<ImageInfo> allImages;
        private SubtitleInfo subtitleInfo;

        // @endcond

        public Builder(@NonNull String mediaUrl, @NonNull String mediaMimeType) {
            this.url = mediaUrl;
            this.mimeType = mediaMimeType;
        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(@NonNull String description) {
            this.description = description;
            return this;
        }

        public Builder setIcon(@NonNull String iconUrl) {
            if (iconUrl != null) {
                createImagesList();
                allImages.set(0, new ImageInfo(iconUrl));
            }
            return this;
        }

        public Builder setIcon(@NonNull ImageInfo icon) {
            if (icon != null) {
                createImagesList();
                allImages.set(0, icon);
            }
            return this;
        }

        public Builder setSubtitleInfo(@NonNull SubtitleInfo subtitleInfo) {
            this.subtitleInfo = subtitleInfo;
            return this;
        }

        public MediaInfo build() {
            return new MediaInfo(this);
        }

        private void createImagesList() {
            if (allImages == null) {
                // Currently only one image is used by all services with index 0
                allImages = new ArrayList<ImageInfo>(Collections.<ImageInfo>nCopies(1, null));
            }
        }
    }

    private MediaInfo(MediaInfo.Builder builder) {
        url = builder.url;
        mimeType = builder.mimeType;
        title = builder.title;
        description = builder.description;
        subtitleInfo = builder.subtitleInfo;
        allImages = builder.allImages;
    }

    /**
     * This constructor is deprecated. Use `MediaInfo.Builder` instead.
     *
     * @param url         media file
     * @param mimeType    media mime type
     * @param title       optional metadata
     * @param description optional metadata
     */
    @Deprecated
    public MediaInfo(String url, String mimeType, String title, String description) {
        super();
        this.url = url;
        this.mimeType = mimeType;
        this.title = title;
        this.description = description;
    }

    /**
     * This constructor is deprecated. Use `MediaInfo.Builder` instead.
     *
     * @param url         media file
     * @param mimeType    media mime type
     * @param title       optional metadata
     * @param description optional metadata
     * @param allImages   list of imageInfo objects where [0] is icon, [1] is poster
     */
    @Deprecated
    public MediaInfo(String url, String mimeType, String title, String description,
                     List<ImageInfo> allImages) {
        this(url, mimeType, title, description);
        this.allImages = allImages;
    }

    /**
     * Gets type of a media file.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets type of a media file.
     *
     * This method is deprecated
     */
    @Deprecated
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Gets title for a media file.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of a media file.
     *
     * This method is deprecated
     */
    @Deprecated
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description for a media.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description for a media.
     * This method is deprecated
     */
    @Deprecated
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets list of ImageInfo objects for images representing a media (ex. icon, poster).
     * Where first ([0]) is icon image, and second ([1]) is poster image.
     */
    public List<ImageInfo> getImages() {
        return allImages;
    }

    /**
     * Sets list of ImageInfo objects for images representing a media (ex. icon, poster).
     * Where first ([0]) is icon image, and second ([1]) is poster image.
     *
     * This method is deprecated
     */
    @Deprecated
    public void setImages(List<ImageInfo> images) {
        this.allImages = images;
    }

    /**
     * Gets duration of a media file.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets duration of a media file.
     * This method is deprecated
     */
    @Deprecated
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Gets URL address of a media file.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets URL address of a media file.
     * This method is deprecated
     */
    @Deprecated
    public void setUrl(String url) {
        this.url = url;
    }

    public SubtitleInfo getSubtitleInfo() {
        return subtitleInfo;
    }

    /**
     * Stores ImageInfo objects.
     *
     * This method is deprecated
     */
    @Deprecated
    public void addImages(ImageInfo... images) {
        if (images == null) {
            return;
        }
        List<ImageInfo> list = new ArrayList<ImageInfo>();
        Collections.addAll(list, images);
        this.setImages(list);
    }

}
